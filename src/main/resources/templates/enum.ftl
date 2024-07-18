package ${packageName};

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * ${logicalName}
 *
 * @author schneider-code-generator
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ${physicalName} {

  <#list records as record>
  /**
   * ${record.label}
   */
  ${record.code}("${record.label}")<#if record_has_next>,<#else>;</#if>

  </#list>
  final String label;

  public static Optional<${physicalName}> fromCode(String code) {
    return Arrays.stream(values()).filter(v -> Objects.equals(v.name(), code)).findFirst();
  }

  public static Optional<${physicalName}> fromLabel(String label) {
    return Arrays.stream(values()).filter(v -> Objects.equals(v.label, label)).findFirst();
  }
}
