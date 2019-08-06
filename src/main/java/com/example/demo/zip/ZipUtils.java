package com.example.demo.zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;

public class ZipUtils {


    private static void unzip(String realPath,String destPath,String password) {
        try {
            ZipFile zipFile = new ZipFile(realPath);
            zipFile.setFileNameCharset("gbk");

            if (!zipFile.isValidZipFile()){
                throw new ZipException("传入的文件不合格");
            }
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            File file = new File(destPath);
            if (file.isDirectory() && !file.exists()) {
                file.mkdirs();
            }

            zipFile.extractAll(destPath);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    private static void zipFile(String destPath,String zipName,String orgPath) throws ZipException {
        // 生成的压缩文件
        ZipFile zipFile = new ZipFile(destPath + "\\" + zipName + ".zip");
        ZipParameters parameters = new ZipParameters();
        // 压缩方式
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        // 要打包的文件夹
        File currentFile = new File(orgPath);
        File[] fs = currentFile.listFiles();
        // 遍历test文件夹下所有的文件、文件夹
        for (File f : fs) {
            if (f.isDirectory()) {
                zipFile.addFolder(f.getPath(), parameters);
            } else {
                zipFile.addFile(f, parameters);
            }
        }
    }
}
