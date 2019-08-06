package com.example.demo;

import net.arnx.wmf2svg.gdi.svg.SvgGdi;
import net.arnx.wmf2svg.gdi.wmf.WmfParser;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * @Desc
 * @Author HUANGZECHENG928
 * @Date 2019-8-2
 * @Version 1.0.0
 **/
public class WmfToPng {

    @Test
    public void testWmfToPng(){
        String result = convert("resources/images/AP01.wmf");
    }

    public static String convert(String path) {
        try {
            String svgFile=path.substring(0,path.lastIndexOf(".wmf"))+".svg";
            wmfToSvg(path, svgFile);
            String jpgFile=path.substring(0,path.lastIndexOf(".wmf"))+".png";
            svgToJpg(svgFile, jpgFile);
            return jpgFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将svg转化为JPG
     *
     * @param src
     * @param dest
     */
    public static void svgToJpg(String src, String dest) {
        FileOutputStream jpgOut = null;
        FileInputStream svgStream = null;
        ByteArrayOutputStream svgOut = null;
        ByteArrayInputStream svgInputStream = null;
        ByteArrayOutputStream jpg = null;
        File svg=null;
        try {
            // 获取到svg文件
            svg = new File(src);
            svgStream = new FileInputStream(svg);
            svgOut = new ByteArrayOutputStream();
            // 获取到svg的stream
            int noOfByteRead = 0;
            while ((noOfByteRead = svgStream.read()) != -1) {
                svgOut.write(noOfByteRead);
            }
            ImageTranscoder it = new PNGTranscoder();
            jpg = new ByteArrayOutputStream();
            svgInputStream = new ByteArrayInputStream(svgOut.toByteArray());
            it.transcode(new TranscoderInput(svgInputStream),
                    new TranscoderOutput(jpg));
            jpgOut = new FileOutputStream(dest);
            jpgOut.write(jpg.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (svgInputStream != null) {
                    svgInputStream.close();
                }
                if (jpg != null) {
                    jpg.close();
                }
                if (svgStream != null) {
                    svgStream.close();
                }
                if (svgOut != null) {
                    svgOut.close();
                }
                if (jpgOut != null) {
                    jpgOut.flush();
                    jpgOut.close();
                }
                if(svg!=null&&svg.exists()){
                    svg.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将wmf转换为svg
     *
     * @param src
     * @param dest
     */
    public static void wmfToSvg(String src, String dest) {
        File file=new File(src);
        boolean compatible = false;
        try {
            InputStream in = new FileInputStream(file);
            WmfParser parser = new WmfParser();
            final SvgGdi gdi = new SvgGdi(compatible);
            parser.parse(in, gdi);

            Document doc = gdi.getDocument();
            OutputStream out = new FileOutputStream(dest);
            if (dest.endsWith(".svgz")) {
                out = new GZIPOutputStream(out);
            }

            output(doc, out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        }
    }

    private static void output(Document doc, OutputStream out) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
                "-//W3C//DTD SVG 1.0//EN");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
        transformer.transform(new DOMSource(doc), new StreamResult(out));
        out.flush();
        out.close();
    }

}
