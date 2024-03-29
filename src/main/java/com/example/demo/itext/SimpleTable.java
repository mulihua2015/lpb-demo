package com.example.demo.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SimpleTable {

    public static final String DEST = "results/tables/simple_table.pdf";

    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleTable().createPdf(DEST);
    }
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(5);
        for(int aw = 0; aw < 16; aw++){
            table.addCell("hi");
        }
        table.addCell("栋号或名称");
        table.addCell("层数");
        table.addCell("基底面积");
        table.addCell("栋号或名称");
        table.addCell("备      注");
        document.add(table);
        document.close();
    }

}
