package com.s_kugel.schneider.generator.entity;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Entity Generator Plugin
 *
 * @author i-zacky
 */
public class EntityGeneratorPlugin implements Plugin<Project> {

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
