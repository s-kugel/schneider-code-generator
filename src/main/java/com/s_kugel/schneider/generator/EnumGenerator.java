package com.s_kugel.schneider.generator;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/** enum generator */
public class EnumGenerator implements Plugin<Project> {

  /** default constructor */
  public EnumGenerator() {
    // non-use
  }

  @Override
  public void apply(Project project) {
    project
        .getTasks()
        .register(
            "generateEnum",
            task -> {
              task.setGroup("code generator");
              task.doLast(s -> System.out.println("Hello from plugin 'org.example.greeting'"));
            });
  }
}
