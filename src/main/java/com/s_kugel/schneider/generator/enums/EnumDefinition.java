package com.s_kugel.schneider.generator.enums;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@NoArgsConstructor
@AllArgsConstructor
@With
public class EnumDefinition {

  String logicalName;

  String physicalName;

  List<EnumRecord> records;
}
