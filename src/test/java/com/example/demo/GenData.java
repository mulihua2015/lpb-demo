package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc
 * @Author HUANGZECHENG928
 * @Date 2019/8/7 10:19
 * @Version 1.0
 **/
public class GenData {

    /*
    房屋建筑面积分户平面图
     */
    public static List<Map<String, String>> genHouseHoldPlanData() throws Exception{
        List<Map<String, Object>> list = new DBQueryExecuter().queryTable("RP房屋建筑面积分户平面图").getResultMapConvertChineseToFirstChar();

        // 整理数据
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> obj = null;
        for (Map<String, Object> map : list) {
            obj = new HashMap<>();
            String zdh = (null != map.get("zdh") ? map.get("zdh").toString() : "");
            obj.put("zdh", zdh.length() > 9 ? zdh.substring(0, 9) : "");
            obj.put("zddm", zdh.length() > 28 ? zdh.substring(10, 29) : "");
            obj.put("dh", null != map.get("jzwmc") ? map.get("jzwmc").toString() : "");
            obj.put("fh", null != map.get("txh") ? map.get("txh").toString() : "");
            obj.put("cc", null != map.get("cxmc") ? map.get("cxmc").toString() : "");
            obj.put("jzmjlb",null != map.get("jzmjlb") ? map.get("jzmjlb").toString() : "");
            obj.put("jzmj",null != map.get("jzmj") ? map.get("jzmj").toString() : "");
            obj.put("tnjzmj",null != map.get("tnmj") ? map.get("tnmj").toString() : "");
            obj.put("ftgymj",null != map.get("ftmj") ? map.get("ftmj").toString() : "");
            obj.put("bz", null != map.get("bz") ? "注:"+map.get("bz").toString() : "");
            obj.put("chdw", null != map.get("chdw") ? map.get("chdw").toString() : "");
            obj.put("chrq", null != map.get("chsj") ? map.get("chsj").toString() : "");
            obj.put("img", null != map.get("wmftxmc") ? map.get("wmftxmc").toString() : "");
            data.add(obj);
        }
        return data;
    }

    /*
    RP房屋建筑面积分户位置图
     */
    public static List<Map<String, String>> genHouseAreaLocationData() throws Exception{
        List<Map<String, Object>> list = new DBQueryExecuter().queryTable("RP房屋建筑面积分户位置图").getResultMapConvertChineseToFirstChar();

        // 整理数据
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> obj = null;
        for (Map<String, Object> map : list) {
            obj = new HashMap<>();
            String zdh = (null != map.get("zdh") ? map.get("zdh").toString() : "");
            obj.put("zdh", zdh.length() > 9 ? zdh.substring(0, 9) : "");
            obj.put("zddm", zdh.length() > 28 ? zdh.substring(10, 29) : "");
            obj.put("dh", null != map.get("dhjmc") ? map.get("dhjmc").toString() : "");
            obj.put("cc", null != map.get("cc") ? map.get("cc").toString() : "");
            obj.put("bdzcs", null != map.get("bdzcs") ? map.get("bdzcs").toString() : "");
            obj.put("bcfhs", null != map.get("bcfhs") ? map.get("bcfhs").toString() : "");
            obj.put("bz", null != map.get("bz") ? map.get("bz").toString() : "");
            obj.put("chdw", null != map.get("chdw") ? map.get("chdw").toString() : "");
            obj.put("chsj", null != map.get("chsj") ? map.get("chsj").toString() : "");
            obj.put("img", null != map.get("WMFt") ? map.get("WMFt").toString() : "");
            data.add(obj);
        }
        return data;
    }

    /*
    RP公用建筑面积分层平面图
     */
    public static List<Map<String, String>> genPublicAreaPlanData() throws Exception{
        List<Map<String, Object>> list = new DBQueryExecuter().queryTable("RP公用建筑面积分层平面图").getResultMapConvertChineseToFirstChar();

        // 整理数据
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> obj = null;
        for (Map<String, Object> map : list) {
            obj = new HashMap<>();
            String zdh = (null != map.get("zdh") ? map.get("zdh").toString() : "");
            obj.put("zdh", zdh.length() > 9 ? zdh.substring(0, 9) : "");
            obj.put("zddm", zdh.length() > 28 ? zdh.substring(10, 29) : "");
            obj.put("dh", null != map.get("dhjmc") ? map.get("dhjmc").toString() : "");
            obj.put("cc", null != map.get("ccjmc") ? map.get("ccjmc").toString() : "");
            obj.put("ground", null != map.get("bdzcs（ds）") ? map.get("bdzcs（ds）").toString() : "");
            obj.put("underground", null != map.get("bdzcs（dxbkbdxs）") ? map.get("bdzcs（dxbkbdxs）").toString() : "");
            obj.put("jzmjlb", null != map.get("jzmjlb") ? map.get("jzmjlb").toString() : "");
            obj.put("bz", null != map.get("bz") ? map.get("bz").toString() : "");
            obj.put("jzmj", null != map.get("jzmj") ? map.get("jzmj").toString() : "");
            obj.put("chdw", null != map.get("chdw") ? map.get("chdw").toString() : "");
            obj.put("chrq", null != map.get("chsj") ? map.get("chsj").toString() : "");
            obj.put("img", null != map.get("wmftxmc") ? map.get("wmftxmc").toString() : "");
            data.add(obj);
        }
        return data;
    }

    /*
    RP房屋层次及房号编号立面图
    */
    public static List<Map<String, String>> genHouseNumberPlanData() throws Exception{
        List<Map<String, Object>> list = new DBQueryExecuter().queryTable("基本信息表").getResultMapConvertChineseToFirstChar();

        // 整理数据
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> obj = null;
        for (Map<String, Object> map : list) {
            obj = new HashMap<>();
            obj.put("img", null != map.get("lmt") ? map.get("lmt").toString() : "");
            data.add(obj);
        }
        return data;
    }

    /*
    测绘项目平面位置示意图
    */
    public static List<Map<String, String>> genSurveyProjectData() throws Exception{
        List<Map<String, Object>> list = new DBQueryExecuter().queryTable("基本信息表").getResultMapConvertChineseToFirstChar();

        // 整理数据
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> obj = null;
        for (Map<String, Object> map : list) {
            obj = new HashMap<>();
            String zdh = (null != map.get("zdh") ? map.get("zdh").toString() : "");
            obj.put("zdh", zdh.length() > 9 ? zdh.substring(0, 9) : "");
            obj.put("zddm", zdh.length() > 28 ? zdh.substring(10, 29) : "");
            obj.put("dh", null != map.get("jzwmc") ? map.get("jzwmc").toString() : "");
            obj.put("desc", null != map.get("chxmgksm") ? map.get("chxmgksm").toString() : "");
            obj.put("chdw", null != map.get("chdw") ? map.get("chdw").toString() : "");
            obj.put("chrq", null != map.get("chsj") ? map.get("chsj").toString() : "");
            obj.put("img", null != map.get("pmwzt") ? map.get("pmwzt").toString() : "");
            data.add(obj);
        }
        return data;
    }
}
