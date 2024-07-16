package com.s_kugel.schneider.generator;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/** entity generator */
public class EntityGenerator implements Plugin<Project> {

  /** default constructor */
  public EntityGenerator() {
    // non-use
  }

  @Override
  public void apply(Project project) {
    project
        .getTasks()
        .register(
            "generateEntity",
            task -> {
              task.setGroup("code generator");
              task.doLast(s -> System.out.println("Hello from plugin 'org.example.greeting'"));
            });
  }
}
