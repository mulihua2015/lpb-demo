package com.example.demo.itext;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class IncompleteTable {
    public static final String DEST = "results/tables/incomplete_table.pdf";

    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new IncompleteTable().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font fontChinese = new Font(bfChinese);

        document.open();
        PdfPTable table = new PdfPTable(5);
        table.setHeaderRows(1);
        table.setSplitRows(false);
        table.setComplete(false);


        table.addCell(new PdfPCell(new Phrase("栋号或名称", fontChinese)));
        table.addCell(new PdfPCell(new Phrase("层数", fontChinese)));
        table.addCell(new PdfPCell(new Phrase("基底面积", fontChinese)));
        table.addCell(new PdfPCell(new Phrase("建筑面积", fontChinese)));
        table.addCell(new PdfPCell(new Phrase("备注", fontChinese)));

        for (int i = 0; i < 50; i++) {
            if (i % 5 == 0) {
                document.add(table);
            }
            table.addCell("Test" + i);
        }

        table.setComplete(true);
        document.add(table);
        document.close();
    }

}
