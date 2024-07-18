package com.s_kugel.schneider.generator.enums;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * EnumGeneratorTask
 *
 * @author i-zacky
 */
@Slf4j
public class EnumGeneratorTask extends DefaultTask {

  private final EnumGeneratorExtension extension;

  public String url;

  public String user;

  public String password;

  public List<String> tables;

  public String packageName;

  public String outDir;

  public EnumGeneratorTask() {
    setGroup("code generator");
    setDescription("Generate Enum Codes");
    this.extension =
        (EnumGeneratorExtension) getProject().getExtensions().findByName("enumGenerator");
  }

  @TaskAction
  public void execute() {
    try {
      // 設定値のセットアップ
      var extension = setupExtension();

      // コード値の収集
      var definitions = collectDefinitions(extension);

      // ソースコードの生成
      generate(definitions);
    } catch (Exception e) {
      log.error("failed to generate enum codes", e);
    }
  }

  EnumGeneratorExtension setupExtension() {
    return new EnumGeneratorExtension()
        .withUrl(
            Optional.ofNullable(this.extension) //
                .map(EnumGeneratorExtension::getUrl) //
                .orElse(this.url))
        .withUser(
            Optional.ofNullable(this.extension) //
                .map(EnumGeneratorExtension::getUser) //
                .orElse(this.user))
        .withPassword(
            Optional.ofNullable(this.extension) //
                .map(EnumGeneratorExtension::getPassword) //
                .orElse(this.password))
        .withTables(
            Optional.ofNullable(this.extension) //
                .map(EnumGeneratorExtension::getTables) //
                .orElse(this.tables))
        .withPackageName(
            Optional.ofNullable(this.extension) //
                .map(EnumGeneratorExtension::getPackageName) //
                .orElse(this.packageName))
        .withOutDir(
            Optional.ofNullable(this.extension) //
                .map(EnumGeneratorExtension::getOutDir) //
                .orElse(this.outDir));
  }

  @SneakyThrows
  List<EnumDefinition> collectDefinitions(EnumGeneratorExtension extension) {
    List<EnumDefinition> definitions = Lists.newArrayList();

    try (var conn =
        DriverManager.getConnection(
            extension.getUrl(), extension.getUser(), extension.getPassword())) {
      for (var table : extension.getTables()) {
        var definition = new EnumDefinition();

        // テーブル情報の取得
        try (var stmt =
            conn.prepareStatement(
                "SELECT TABLE_NAME, TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_NAME = ?")) {
          stmt.setString(1, table);
          var rs = stmt.executeQuery();
          while (rs.next()) {
            var tableName = rs.getString("TABLE_NAME");
            var tableComment = rs.getString("TABLE_COMMENT");
            definition =
                new EnumDefinition() //
                    .withPhysicalName(tableName) //
                    .withLogicalName(tableComment);
          }
        }

        // コード値のデータ取得
        List<EnumRecord> records = Lists.newArrayList();
        try (var stmt = conn.prepareStatement("SELECT * FROM %s".formatted(table))) {
          var rs = stmt.executeQuery();
          while (rs.next()) {
            var code = rs.getString("code");
            var label = rs.getString("label");
            records.add(new EnumRecord(code, label));
          }
        }

        definitions.add(definition.withRecords(records));
      }
    }

    return definitions;
  }

  @SneakyThrows
  void generate(List<EnumDefinition> definitions) {
    var outputDirectory = Paths.get(outDir, packageName.split("\\."));
    if (Files.notExists(outputDirectory)) {
      Files.createDirectories(outputDirectory);
    }

    var cfg = new Configuration(Configuration.VERSION_2_3_33);
    cfg.setClassForTemplateLoading(this.getClass(), "/templates");
    cfg.setDefaultEncoding("UTF-8");
    cfg.setAutoFlush(true);
    var template = cfg.getTemplate("enum.ftl");

    for (var definition : definitions) {
      Map<String, Object> model = Maps.newHashMap();
      model.put("packageName", packageName);
      model.put("physicalName", definition.getPhysicalName());
      model.put("logicalName", definition.getLogicalName());
      model.put("records", definition.getRecords());

      var outputFile =
          Paths.get(outputDirectory.toString(), definition.getPhysicalName() + ".java");
      if (!Files.exists(outputFile)) {
        Files.createFile(outputFile);
      }
      try (val writer = new FileWriter(outputFile.toFile(), StandardCharsets.UTF_8)) {
        template.process(model, writer);
        System.out.printf("Created: %s%n", outputFile);
      }
    }
  }
}
