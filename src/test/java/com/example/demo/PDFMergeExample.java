package com.example.demo;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Test;

/**
 * This class is used to merge two or more
 * existing pdf file using iText jar.
 *
 * Step:
 * 1.	Prepare input pdf file list as list of input stream.
 * 2.	Prepare output stream for merged pdf file.
 * 3.	call method to merge pdf files.
 * 4.	Create document and pdfReader objects.
 * 5.	Create pdf Iterator object using inputPdfList.
 * 6.	Create reader list for the input pdf files.
 * 7.	Create writer for the outputStream.
 * 8.	Open document.
 * 9.	Get PdfContentByte instance from writer object.
 * 10.	Iterate and process the reader list.
 * 11.	Create page and add content.
 * 12.	Close document and outputStream.
 *
 * @author zhusidao
 */
public class PDFMergeExample {

    public static final String PDF1 = "resources/pdfs/nanning_contract_record_prove_template.pdf";
    public static final String PDF2 = "resources/pdfs/pages.pdf";
    public static final String PDF3 = "resources/pdfs/template_prim_output.pdf";
    public static final String DEST = "results/stamper/mergePDF.pdf";

    static void mergePdfFiles1(List<InputStream> inputPdfList, OutputStream outputStream) throws Exception {

        //Create document and pdfReader objects.
        Document document = new Document();

        //Create pdf Iterator object using inputPdfList.
        Iterator<InputStream> pdfIterator = inputPdfList.iterator();

        int totalPages = 0;
        List<PdfReader> readers = new ArrayList<>();
        // Create reader list for the input pdf files.
        while (pdfIterator.hasNext()) {
            InputStream pdf = pdfIterator.next();
            PdfReader pdfReader = new PdfReader(pdf);
            readers.add(pdfReader);
            totalPages = totalPages + pdfReader.getNumberOfPages();
        }

        // Create writer for the outputStream
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        //Open document.
        document.open();

        //Contain the pdf data.
        PdfContentByte pageContentByte = writer.getDirectContent();

        PdfImportedPage pdfImportedPage;
        int currentPdfReaderPage = 1;
        Iterator<PdfReader> iteratorPDFReader = readers.iterator();

        // Iterate and process the reader list.
        while (iteratorPDFReader.hasNext()) {
            PdfReader pdfReader = iteratorPDFReader.next();
            //Create page and add content.
            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                document.setPageSize(pdfReader.getPageSize(currentPdfReaderPage));
                document.newPage();
                pdfImportedPage = writer.getImportedPage(
                        pdfReader, currentPdfReaderPage);
                pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                currentPdfReaderPage++;
            }
            currentPdfReaderPage = 1;
        }

        //Close document and outputStream.
        outputStream.flush();
        document.close();
        outputStream.close();

        System.out.println("Pdf files merged successfully.");
    }


    static void mergePdfFiles2(List<byte[]> inputPdfList, OutputStream outputStream) throws Exception {

        //Create document and pdfReader objects.
        Document document = new Document();

        //Create pdf Iterator object using inputPdfList.
        Iterator<byte[]> pdfIterator = inputPdfList.iterator();

        int totalPages = 0;
        List<PdfReader> readers = new ArrayList<>();
        // Create reader list for the input pdf files.
        while (pdfIterator.hasNext()) {
            byte[] pdf = pdfIterator.next();
            PdfReader pdfReader = new PdfReader(pdf);
            readers.add(pdfReader);
            totalPages = totalPages + pdfReader.getNumberOfPages();
        }

        // Create writer for the outputStream
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        //Open document.
        document.open();

        //Contain the pdf data.
        PdfContentByte pageContentByte = writer.getDirectContent();

        PdfImportedPage pdfImportedPage;
        int currentPdfReaderPage = 1;
        Iterator<PdfReader> iteratorPDFReader = readers.iterator();

        // Iterate and process the reader list.
        while (iteratorPDFReader.hasNext()) {
            PdfReader pdfReader = iteratorPDFReader.next();
            //Create page and add content.
            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                document.setPageSize(pdfReader.getPageSize(currentPdfReaderPage));
                document.newPage();
                pdfImportedPage = writer.getImportedPage(
                        pdfReader, currentPdfReaderPage);
                pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                currentPdfReaderPage++;
            }
            currentPdfReaderPage = 1;
        }

        //Close document and outputStream.
        outputStream.flush();
        document.close();
        outputStream.close();

        System.out.println("Pdf files merged successfully.");
    }

    @Test
    public void test() {
        try {
            //Prepare input pdf file list as list of input stream.
            List<InputStream> inputPdfList = new ArrayList<InputStream>();
            inputPdfList.add(new FileInputStream(PDF1));
       //     inputPdfList.add(new FileInputStream(PDF2));
            inputPdfList.add(new FileInputStream(PDF3));
            //Prepare output stream for merged pdf file.
            OutputStream outputStream =
                    new FileOutputStream(DEST);

            //call method to merge pdf files.
            mergePdfFiles1(inputPdfList, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(PDF1));
        Rectangle one = new Rectangle(70,140);
        Rectangle two = new Rectangle(700,400);
        document.setPageSize(one);
        document.setMargins(2, 2, 2, 2);
        document.open();
        Paragraph p = new Paragraph("Hi");
        document.add(p);
        document.setPageSize(two);
        document.setMargins(20, 20, 20, 20);
        document.newPage();
        document.add(p);
        document.close();
    }
}