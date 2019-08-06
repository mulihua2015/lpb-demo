package com.example.demo.project;

import com.example.demo.DBQueryExecuter;
import com.example.demo.PdfUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestTemplate {

    private static String FILE_TEMPLATE1 = "resources/pdfs/template/template1.pdf";
    private static String FILE_TEMPLATE2 = "resources/pdfs/template/template2.pdf";
    private static String FILE_TEMPLATE3 = "resources/pdfs/template/template3.pdf";
    private static final String RESULT_PATH1 = "result1.pdf";
    private static final String RESULT_PATH2 = "result2.pdf";
    private static final String RESULT_PATH3 = "result3.pdf";

    private static final String FILE_OUTPUT_DIRECTORY = "results/template";

    @Test
    public void testTemplate1() throws Exception {
        List<Map<String, Object>> list = new DBQueryExecuter().queryTable("基本信息表")
                .getResultMapConvertChineseToFirstChar();
        Map<String, Object> map = list.get(0);
        byte[] bytes = PdfUtil.generatePdfStream(FILE_TEMPLATE1, convertMapValueToString(map));
        PdfUtil.createFile(bytes, FILE_OUTPUT_DIRECTORY, RESULT_PATH1);
        System.out.println(convertMapValueToString(map));
    }

    @Test
    public void testTemplate2() throws Exception {
        List<Map<String, Object>> list = new DBQueryExecuter().queryTable("基本信息表")
                .getResultMapConvertChineseToFirstChar();
        Map<String, Object> map = list.get(0);
        byte[] bytes = PdfUtil.generatePdfStream(FILE_TEMPLATE2, convertMapValueToString(map));
        PdfUtil.createFile(bytes, FILE_OUTPUT_DIRECTORY, RESULT_PATH2);
        System.out.println(convertMapValueToString(map));
    }

    @Test
    public void testTemplate3() throws Exception {
        List<Map<String, Object>> list = new DBQueryExecuter().queryTable("RP公用建筑面积分层汇总表")
                .getResultMapConvertChineseToFirstChar();
        Map<String, Object> map = list.get(0);
        byte[] bytes = PdfUtil.generatePdfStream(FILE_TEMPLATE2, convertMapValueToString(map));
        PdfUtil.createFile(bytes, FILE_OUTPUT_DIRECTORY, RESULT_PATH2);
        System.out.println(convertMapValueToString(map));
    }


    public static Map<String, String> convertMapValueToString(Map<String, Object> map) {
        Map<String, String> result = new HashMap<>(map.size());
        map.forEach((key, val) -> result.put(key, val == null ? "" : val.toString()));
        return result;
    }
}
