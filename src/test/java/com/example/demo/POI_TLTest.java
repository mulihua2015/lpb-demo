package com.example.demo;

import com.deepoove.poi.XWPFTemplate;
import org.junit.Test;

import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * @describe：
 * @Date：2019/8/8 10:03
 * @author：wbx
 */
public class POI_TLTest {

    @Test
    public void helloWorld() throws Exception{
        //核心API采用了极简设计，只需要一行代码
        XWPFTemplate template = XWPFTemplate.compile("resources/template/汇总表/test.doc").render(new HashMap<String, Object>(){{
            put("title", "Poi-tl 模板引擎");
        }});
        FileOutputStream out = new FileOutputStream("out_template.docx");
        template.write(out);
        out.flush();
        out.close();
        template.close();
    }
}
