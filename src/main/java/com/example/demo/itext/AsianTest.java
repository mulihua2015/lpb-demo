package com.example.demo.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;


import java.awt.Color;

public class AsianTest {

    public static void main(String[] args) {

        // 创建一个Document对象
        Document document = new Document();

        try {

            // 生成名为 AsianTest.pdf 的文档
            PdfWriter.getInstance(document, new FileOutputStream("results/tables/AsianTest2.pdf"));

            /**  新建一个字体,iText的方法
             *  STSongStd-Light 是字体，在iTextAsian.jar 中以property为后缀
             *  UniGB-UCS2-H   是编码，在iTextAsian.jar 中以cmap为后缀
             *  H 代表文字版式是 横版， 相应的 V 代表竖版
             */
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font fontChinese = new Font(bfChinese);

            // 打开文档，将要写入内容
            document.open();

            // 插入一个段落
            Paragraph par = new Paragraph("我们", fontChinese);

            document.add(par);

        } catch (Exception de) {
            System.err.println(de.getMessage());
        }

        // 关闭打开的文档
        document.close();
    }
}
