package com.example.demo.access;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScanPackageUtils {

    public static void main(String[] args) throws Exception {
        List<Class> aClass = findClass("com.example.demo");

        aClass.forEach(x -> {
            System.out.println(x.getName());
        });
    }
    
    public static  List<Class> findClass(String packageName) throws IOException,ClassNotFoundException{
        return findClass(packageName,new ArrayList<>());
    }


    private static List<Class> findClass(String packageName,List<Class> clazzs) throws ClassNotFoundException{
        String fileName = packageName.replaceAll("\\.","/");
        String path = Thread.currentThread().getContextClassLoader().getResource(fileName).getFile().trim();
        File file = new File(path);
        File[] files = file.listFiles();
        for (File f:files){
            if (f.isDirectory()) {
                String currentPathName = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(File.separator)+1);
                findClass(packageName+"."+currentPathName,clazzs);
            }else {
                if (f.getName().endsWith(".class")) {
                    Class clazz = Thread.currentThread().getContextClassLoader().
                            loadClass(packageName+"."+f.getName().replace(".class",""));
                    clazzs.add(clazz);
                }
            }
        }
        return clazzs;
    }
}
