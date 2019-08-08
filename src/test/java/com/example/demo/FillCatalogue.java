package com.example.demo;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @describe：
 * @Date：2019/8/7 14:07
 * @author：wbx
 */
public class FillCatalogue {

    static Map<String,String> map = new HashMap<String, String>(){
        {
            LocalDate now = LocalDate.now();
            put("Parcel_no","K202-0031");
            put("Parcel_Code", "440305007003GB00034");
            put("PROJ_NAME", "太子湾商贸大厦");
            put("address", "南山区招商蛇口太子湾片区");
            put("BUILD_NAME", "深圳市蛇口海滨置业有限公司");
            put("year", now.getYear()+"");
            put("month", now.getMonthValue()+"");
            put("day", now.getDayOfMonth()+"");
        }
    };
    private static final String fontName = "STSong-Light";

    @Test
    public void index() throws Exception{
        // 模板路径
        String templatePath = "resources/template/汇总表/total_form_index_template.pdf";
        byte[] bytes = PdfUtil.generatePdfStream(templatePath,map);
        PdfUtil.createFile(bytes,"results","template_output/分类汇总表-首页.pdf");
    }



}
