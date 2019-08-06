package com.example.demo.access;

import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AccessDataUtils {

    public static void main(String[] args)  throws Exception{
        List<Object> list = AccessDataExchange();

    }
    
    
    private static List<Object> AccessDataExchange() throws Exception{
        String url = "jdbc:ucanaccess://E:\\idea_work_space\\demo\\resources\\分摊库.mdb";
        
        String tableName = "层表";
        
        List<Class> aClass = null;

        try {
            aClass = ScanPackageUtils.findClass("com.example.demo.access");
        } catch (IOException e) {
            
        } catch (ClassNotFoundException e) {
            
        }
        AccessTable accessTable = null;
        Class clazz = null;
        if (!CollectionUtils.isEmpty(aClass)) {
            for (Class clazz1 : aClass){
                Annotation[] annotations = clazz1.getAnnotations();
                for (int i = 0; i <annotations.length ; i++) {
                    if (annotations[i] instanceof AccessTable) {
                        accessTable = (AccessTable) annotations[i];
                        if (accessTable.value().equalsIgnoreCase(tableName)){
                            clazz = clazz1;
                            break;
                        }
                    }
                }
            }
        }
        if (Objects.isNull(clazz)) {
            throw  new Exception("表名不对");
        }
        
        List<Object> list = new ArrayList<>();
        
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        Connection conn = DriverManager.getConnection(url);

        Statement stat = conn.createStatement();
        
        String sql = "select * from " + tableName;

        ResultSet rs = stat.executeQuery(sql);

        ResultSetMetaData metaData = rs.getMetaData();

        int columnCount = metaData.getColumnCount();
        
        List<MdbColumn> mdbColumnList = new ArrayList<>();

        for (int i = 0; i < columnCount; i++) {
            MdbColumn mdbColumn = new MdbColumn();
            String columnName = metaData.getColumnName(i + 1);
            mdbColumn.setColumnIndex(i+1);
            mdbColumn.setColumnName(columnName);
            mdbColumnList.add(mdbColumn);
        }
        if (CollectionUtils.isEmpty(mdbColumnList)) {
            return null;
        }

        while (rs.next()){
            Object o = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i <fields.length ; i++) {
                fields[i].setAccessible(true);
                AccessColumns annotation = fields[i].getAnnotation(AccessColumns.class);
                if (!Objects.isNull(annotation)) {
                    Map<String, MdbColumn> map = mdbColumnList.stream().collect(Collectors.toMap(MdbColumn::getColumnName, Function.identity()));
                    Set<Map.Entry<String, MdbColumn>> entries = map.entrySet();
                    for (Map.Entry<String, MdbColumn> mdbColumnEntry: entries){
                        if (mdbColumnEntry.getKey().equalsIgnoreCase(annotation.value())) {
                            Class<?> type = fields[i].getType();
                            if (type.getName().equalsIgnoreCase("java.lang.string")) {
                                fields[i].set(o,rs.getString(mdbColumnEntry.getValue().getColumnIndex()));
                            }
                            if (type.getName().equalsIgnoreCase("java.lang.Integer")|| type.getName().equalsIgnoreCase("int")) {
                                fields[i].set(o,rs.getString(mdbColumnEntry.getValue().getColumnIndex()));
                            }
                        }
                    }
                }
            }
            list.add(o);
            list.forEach(x -> {
                Tire tire = (Tire) x;
                System.out.println(tire);
            });
        }
        return list;
    }
}
