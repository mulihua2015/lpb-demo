package com.example.demo.access;

import com.example.demo.access.utils.annotation.ConvertKey;
import lombok.Data;

@Data
@AccessTable(value = "层表")
public class Tire {

    @AccessColumns(value = "层名称")
    private String tireName;

    @AccessColumns(value = "层ID")
    private String id;

    private String tireModelId;

    private String functionAreaId;

    private String conSetNumber;

    private Integer bcfhs;

    private String cg;

    private String zg;

    private Long zrch;

    private String remark;


}
