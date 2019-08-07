package com.example.demo.access.domain;

import com.example.demo.access.AccessColumns;
import com.example.demo.access.AccessTable;
import com.example.demo.access.utils.annotation.ConvertKey;
import lombok.Data;

@Data
@AccessTable("RP房屋建筑面积分户汇总表")
public class RPPublicArea {

    @AccessColumns("房号")
    private String hf;

    @AccessColumns("套内面积")
    private String tnmj;

    @AccessColumns("分摊面积")
    private String ftmj;

    @AccessColumns("建筑面积")
    private String jzmj;

    @AccessColumns("备注")
    @ConvertKey("aabbb")
    private String bj;
}
