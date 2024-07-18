package com.s_kugel.schneider.generator.enums;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * EnumGeneratorPlugin
 *
 * @author i-zacky
 */
public class EnumGeneratorPlugin implements Plugin<Project> {

  @Override
  public void apply(Project project) {
    project.getExtensions().create("enumGenerator", EnumGeneratorExtension.class);
    project.getTasks().register("enumGenerator", EnumGeneratorTask.class);
  }
}
