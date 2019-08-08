package com.example.demo;

import org.junit.Test;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Desc
 * @Author HUANGZECHENG928
 * @Date 2019/8/7 10:17
 * @Version 1.0
 **/
public class FileUtils {

    @Test
    public void testReadFile() throws Exception{
        List<Map<String, String>> maps= GenData.genHouseAreaLocationData();
        String imgPath = "resources/images/";
        for(Map<String, String> map:maps){
            if(null!=map.get("IMG")){
                String path= imgPath+map.get("IMG");
                // 检查文件
                File file = new File(path);
                if(file.exists()){
                    System.out.println(path+"文件存在");
                } else {
                    System.out.println(path+"文件不存在");
                }
            }
        }
        // 检查文件
        File file = new File(imgPath);
        if(file.exists()){
            file.delete();
        }
    }

}
