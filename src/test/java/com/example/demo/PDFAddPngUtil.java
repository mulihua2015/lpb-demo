package com.example.demo;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.junit.Test;
import java.io.*;
import java.util.*;
/**
 * @Desc
 * @Author HUANGZECHENG928
 * @Date 2019/8/2 14:34
 * @Version 1.0
 **/
public class PDFAddPngUtil {

    private static final String path = "resources/pdfs/nanning_contract_record_prove_template.pdf";
    //    public static final String DEST = "results/stamper/fill_with_tem.pdf";
    // 生成的新文件路径
    public static final String DEST = "results/template_output/house_area_household_plan_output.pdf";
    private static final String ENCODE = "UniGB-UCS2-H";
    private static final String fontName = "STSong-Light";
    public static final String IMG = "resources/images/AP01.png";

    /**
     * 手动生成 房屋建筑面积分户平面图
     * 模板输出带图片的Pdf
     */
    @Test
    public void testCreatePdf() throws Exception {

        // 转换图片格式
        String imgPath = "resources/images/AP01.wmf";
        WmfToPng.convert(imgPath);

        // 多条数据
        List<byte[]> list = new ArrayList<>();
        // 模板路径
        String templatePath = "resources/template/house_area_household_plan_template.pdf";
        byte[] bytes = generatePdfStream(templatePath, fontName, map);
        byte[] images = createImage(IMG);
        byte[] resultBytes = writeImageToPDF(bytes, images, "IMG");
        list.add(resultBytes);
//        list.add(resultBytes);
        // 检查文件
        File file = new File(DEST);
        if(file.exists()){
            file.delete();
        }
        OutputStream outputStream = new FileOutputStream(DEST);
        mergePdfFiles2(list, outputStream);
        // write with image
//        createFile(resultBytes); // 单条
    }

    @Test
    public void testDB(){
//        System.out.println(DBUtils.internalQuery(""));
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
    private static Map<String, String> map = new HashMap<String, String>() {
        {
            put("ZDH","K202-0031");
            put("ZDDM","440305007003GB00034");
            put("DH","太子湾商贸大厦1栋商业");
            put("CC","01");
            put("FH","01");
            put("JZMJLB","预售测绘");
            put("JZMJ","63.75");
            put("TNJZMJ","58.48");
            put("FTGYMJ","5.27");
            put("REMARK","公用建筑面积包括：\n" +
                    "应分摊的公用建筑面积；\n" +
                    "不分摊的公用建筑面积。\n" +
                    "具体内容参见：\n" +
                    "《公用建筑面积分层汇总表》；\n" +
                    "《公用建筑面积分层平面图》；\n" +
                    "《房屋建筑面积分户位置图》。");
            put("CHDW","深圳市地籍测绘大队");
            put("CHRQ","2018年8月3日");
            put("G_REMARK","注:套内建筑面积中含承重支撑体面积0.66平方米(设计值)");
        }
    };

    /**
     * 模板输出Pdf不带图片
     */
    @Test
    public void test0() throws Exception {
        // write without image
        createFile(generatePdfStream(path, fontName, map));
    }

    /**
     * 模板输出带图片的Pdf
     */
    @Test
    public void test1() throws Exception {
        byte[] bytes = generatePdfStream(path, fontName, map);
        byte[] images = createImage(IMG);
        // write with image
        createFile(writeImageToPDF(bytes, images, "test0"));
    }

    /**
     * 创建文件
     * @param bytes
     * @throws IOException
     */
    public static void createFile(byte[] bytes) throws IOException {
        File file = new File(DEST);
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
    }

    /**
     * 创建文件
     * @param bytes
     * @throws IOException
     */
    public static void createFile(byte[] bytes, String descSrc) throws IOException {
        File file = new File(descSrc);
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
    }

    /**
     * 生成pdf
     */
    public static byte[] generatePdfStream(String fileName, String fontName, Map<String, String> data) throws Exception {
        PdfReader reader = new PdfReader(fileName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        /* 将要生成的目标PDF文件名称 */
        //  PdfStamper ps = new PdfStamper(reader, new FileOutputStream(DEST));
        PdfStamper ps = new PdfStamper(reader, bos);
        PdfContentByte under = ps.getUnderContent(1);
        /* 使用中文字体 */
        BaseFont bf = BaseFont.createFont(fontName, "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//        BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/SIMYOU.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        /* 取出报表模板中的所有字段 */
        AcroFields fields = ps.getAcroFields();
        fields.addSubstitutionFont(bf);
        fillData(fields, data);
        /* 必须要调用这个，否则文档不会生成的。如果设置为false生成的pdf文件还是可以编辑， 一定要设为true*/
        ps.setFormFlattening(false);
        ps.close();
        return bos.toByteArray();
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
//        fields.addSubstitutionFont(BaseFont.createFont(fontName, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED));
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