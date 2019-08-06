package com.example.demo;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FillTest {

    private static final String path = "resources/pdfs/fill.pdf";
    private static final String path1 = "results/stamper/fill_res.pdf";
    private static final String fontName = "STSong-Light";
    private static final String DEST = "results/stamper/fill_Table.pdf";
    private static final String ENCODE = "UniGB-UCS2-H";
    private static Map<String, String> map = new HashMap<>();
    // 32
    private static int rowCount = 50;

    static {
        for (int i = 1; i <= 5 * rowCount; i++) {
            map.put("fill_" + i, i + "");
        }
        map.put("footer", "123");
    }

    @Test
    public void test() throws Exception {
        List<byte[]> list = new ArrayList<>();
        for (int i = 0; i <= map.size() / (5 * 32); i++) {
            byte[] bytes = PdfUtil.generatePdfStream(path, map, false);
            list.add(bytes);
            PdfUtil.createFile(bytes, path1);
        }
        OutputStream outputStream =
                new FileOutputStream(DEST);
        PDFMergeExample.mergePdfFiles2(list, outputStream);
    }

}
