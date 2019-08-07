package com.example.demo;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PdfUtil {

    private static final String FONT_Name = "STSong-Light";
    private static final String ENCODE = "UniGB-UCS2-H";

    /**
     * 创建文件
     *
     * @param bytes    二进制流
     * @param dir      目录名称
     * @param fileName 文件名
     * @throws IOException
     */
    public static void createFile(byte[] bytes, String dir, String fileName) throws IOException {
        File directory = new File(dir);
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        File file = new File(directory, fileName);
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
    }

    /**
     * @param fileName 文件名
     * @param fontName 字体名
     * @param encoding 编码
     * @param data
     * @param flat     生成的文档是否还可以编辑.
     *                 <CODE>true</CODE>是不可编辑，
     *                 <CODE>true</CODE>不可编辑.
     * @return byte[] pdf的二进制流
     * @throws Exception
     */
    public static byte[] generatePdfStream(String fileName, String fontName, String encoding,
                                           Map<String, String> data, boolean flat) throws Exception {
        PdfReader reader = new PdfReader(fileName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        /* 将要生成的目标PDF文件名称 */
        //  PdfStamper ps = new PdfStamper(reader, new FileOutputStream(DEST));
        PdfStamper ps = new PdfStamper(reader, bos);
        /* 使用中文字体 */
        BaseFont bf = BaseFont.createFont(fontName, encoding, BaseFont.NOT_EMBEDDED);
        /* 取出报表模板中的所有字段 */
        AcroFields fields = ps.getAcroFields();
        fields.addSubstitutionFont(bf);
        fillData(fields, data);
        /*
         *  生成的文档是否还可以更改
         *  设置为false生成的pdf文件还是可以编辑
         */
        ps.setFormFlattening(flat);
        ps.close();
        return bos.toByteArray();
    }

    /**
     * {@link PdfUtil#generatePdfStream}
     */
    public static byte[] generatePdfStream(String fileName, Map<String, String> data) throws Exception {
        return generatePdfStream(fileName, data, true);
    }

    /**
     * {@link PdfUtil#generatePdfStream}
     */
    public static byte[] generatePdfStream(String fileName,
                                           Map<String, String> data, boolean flat) throws Exception {
        return generatePdfStream(fileName, FONT_Name, data, flat);
    }

    /**
     * {@link PdfUtil#generatePdfStream}
     */
    public static byte[] generatePdfStream(String fileName, String fontName,
                                           Map<String, String> data, boolean flat) throws Exception {
        return generatePdfStream(fileName, fontName, ENCODE, data, flat);
    }

    /**
     * 创建图片
     */
    public static byte[] createImage(String imgSrc) throws IOException {
        FileInputStream fin = new FileInputStream(new File(imgSrc));
        // 需要改造
        byte[] bytes = new byte[fin.available()];
        // 将文件内容写入字节数组，提供测试的case
        fin.read(bytes);
        fin.close();
        return bytes;
    }

    /**
     * 输出pdf带图片
     */
    public static byte[] writeImageToPDF(byte[] pdf, byte[] images, String filedName) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(pdf);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, bos);
        // 获取待填充属性
        AcroFields fields = stamper.getAcroFields();
        // 设置字体
        fields.addSubstitutionFont(BaseFont.createFont(FONT_Name, "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
        // 获取操作页号
        int pageNo = fields.getFieldPositions(filedName).get(0).page;
        Rectangle rectangle = fields.getFieldPositions(filedName).get(0).position;
        //制作图片
        Image image = Image.getInstance(images);
        //根据页码获取页
        PdfContentByte pdfContentByte = stamper.getOverContent(pageNo);
        //根据域的大小放缩图片
        image.scaleToFit(rectangle);
        //设置位置
        image.setAbsolutePosition(rectangle.getLeft(), rectangle.getBottom());
        pdfContentByte.addImage(image);
        stamper.setFormFlattening(true);
        stamper.close();
        reader.close();
        return bos.toByteArray();
    }

    /**
     * 合并PDF
     * @param inputPdfList
     * @param outputStream
     * @throws Exception
     */
    static void mergePdfFiles(List<byte[]> inputPdfList, OutputStream outputStream) throws Exception {

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

    /**
     * 填充数据
     */
    public static void fillData(AcroFields fields, Map<String, String> data)
            throws IOException, DocumentException {
        for (String key : data.keySet()) {
            String value = data.get(key);
            fields.setField(key, value); // 为字段赋值,注意字段名称是区分大小写
        }
    }
}