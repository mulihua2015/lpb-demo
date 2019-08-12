package com.example.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.TableWidthType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class Test {
    public static void main(String[] args) {
        // 文档生成方法
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p1 = doc.createParagraph(); // 创建段落
        XWPFRun r1 = p1.createRun(); // 创建段落文本
        r1.setText("目录"); // 设置文本
        FileOutputStream out = null; // 创建输出流
        try {
            // 向word文档中添加内容
            XWPFParagraph p3 = doc.createParagraph(); // 创建段落
            XWPFRun r3 = p3.createRun(); // 创建段落文本
            r3.addTab();// tab
            r3.addBreak();// 换行
            r3.setBold(true);
            XWPFParagraph p2 = doc.createParagraph(); // 创建段落
            XWPFRun r2 = p2.createRun(); // 创建段落文本
            // 设置文本
            r2.setText("表名");
            r2.setFontSize(14);
            r2.setBold(true);
            XWPFTable table1 = doc.createTable(8, 10);
            table1.setWidthType(TableWidthType.AUTO);

            // 获取到刚刚插入的行
            XWPFTableRow row1 = table1.getRow(0);
            // 设置单元格内容
            row1.getCell(0).setText("字段名");
            row1.getCell(1).setText("字段说明");
            row1.getCell(2).setText("数据类型");
            row1.getCell(3).setText("长度");
            row1.getCell(4).setText("索引");
            row1.getCell(5).setText("是否为空");
            row1.getCell(6).setText("主键");
            row1.getCell(7).setText("外键");
            row1.getCell(8).setText("缺省值");
            row1.getCell(9).setText("备注");

            doc.setTable(0, table1);
            String filePath = "simple.docx";
            out = new FileOutputStream(new File(filePath));
            doc.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

        }

    }
}