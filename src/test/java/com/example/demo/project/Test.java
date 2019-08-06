package com.example.demo.project;

import com.example.demo.DBQueryExecuter;
import com.example.demo.PdfUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    private static String FILE_TEMPLATE = "resources/pdfs/template/template1.pdf";

    private static final String RESULT_PATH = "results/template/result.pdf";

    public static void main(String[] args) throws Exception {
        System.out.println();
        List<Map<String, Object>> list = new DBQueryExecuter().queryTable("基本信息表")
                .getResultMapConvertChineseToFirstChar();
        Map<String, Object> map = list.get(0);
        byte[] bytes = PdfUtil.generatePdfStream(FILE_TEMPLATE, convertMapValueToString(map));
        PdfUtil.createFile(bytes, RESULT_PATH);
        System.out.println(convertMapValueToString(map));
    }


    public static Map<String, String> convertMapValueToString(Map<String, Object> map) {
        Map<String, String> result = new HashMap<>(map.size());
        map.forEach((key, val) -> result.put(key, val == null ? "" : val.toString()));
        return result;
    }
}
