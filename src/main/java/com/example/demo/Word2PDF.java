package com.example.demo;

import java.io.*;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * 优点：开源、不用额外安装软件
 * 缺点：仅支持.docx转pdf，.doc不支持
 */
public class Word2PDF {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        String docPath = "C:\\Users\\ad\\Desktop\\temp\\666.docx";
        String pdfPath = "C:\\Users\\ad\\Desktop\\temp\\777.pdf";


        XWPFDocument document;
        InputStream doc = new FileInputStream(docPath);
        document = new XWPFDocument(doc);
        PdfOptions options = PdfOptions.create();
        OutputStream out = new FileOutputStream(pdfPath);
        PdfConverter.getInstance().convert(document, out, options);

        doc.close();
        out.close();
        System.out.println("转换完成");
        long end = System.currentTimeMillis();
        System.out.println("总共花费时间： " + (end-start));
    }

}
