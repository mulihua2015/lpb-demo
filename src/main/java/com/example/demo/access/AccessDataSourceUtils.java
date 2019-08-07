package com.example.demo.access;

import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AccessDataSourceUtils {

    static AccessDataSourcePool connPoll = null ;

    private static Map <String,AccessDataSourcePool> map = new HashMap<>();



    public static void main(String[] args)  throws Exception{

        String tableName = "层表";
        List<Object> list = AccessDataExchange(tableName);
        AccessDataSourcePool accessDataSourcePool = new AccessDataSourcePool("");
        accessDataSourcePool.init();
        map.put("1",new AccessDataSourcePool(""));
        accessDataSourcePool.close();
        accessDataSourcePool = map.get("1");

    }
    
    
    private static List<Object> AccessDataExchange(String tableName) throws Exception{

        Statement stat = null;

        ResultSet rs = null;

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

        AccessDataSourcePool connPoll = new AccessDataSourcePool("");

        connPoll.init();

        Connection conn = connPoll.getConnection();

        stat = conn.createStatement();
        
        String sql = "select * from " + tableName;

        rs = stat.executeQuery(sql);

        try {
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
                        for (Map.Entry<String,MdbColumn> mdbColumnEntry: entries){
                            if (mdbColumnEntry.getKey().equalsIgnoreCase(annotation.value())) {
                                Class<?> type = fields[i].getType();
                                if (type.getName().equalsIgnoreCase("java.lang.string")) {
                                    fields[i].set(o,rs.getString(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                if (type.getName().equalsIgnoreCase("java.lang.Integer")|| type.getName().equalsIgnoreCase("int")) {
                                    fields[i].set(o,rs.getString(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                if (type.getName().equalsIgnoreCase("java.lang.Long")|| type.getName().equalsIgnoreCase("long")) {
                                    fields[i].set(o,rs.getLong(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                if (type.getName().equalsIgnoreCase("java.lang.Short")|| type.getName().equalsIgnoreCase("short")) {
                                    fields[i].set(o,rs.getShort(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                if (type.getName().equalsIgnoreCase("java.lang.Byte")|| type.getName().equalsIgnoreCase("int")) {
                                    fields[i].set(o,rs.getByte(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                if (type.getName().equalsIgnoreCase("java.lang.Float")|| type.getName().equalsIgnoreCase("float")) {
                                    fields[i].set(o,rs.getFloat(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                if (type.getName().equalsIgnoreCase("java.lang.Double")|| type.getName().equalsIgnoreCase("double")) {
                                    fields[i].set(o,rs.getDouble(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                if (type.getName().equalsIgnoreCase("java.lang.BigDecimal")) {
                                    fields[i].set(o,rs.getBigDecimal(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                if (type.getName().equalsIgnoreCase("java.lang.Boolean")|| type.getName().equalsIgnoreCase("boolean")) {
                                    fields[i].set(o,rs.getBoolean(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                if (type.getName().equalsIgnoreCase("java.sql.Clob")) {
                                    fields[i].set(o,rs.getClob(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                if (type.getName().equalsIgnoreCase("java.sql.Blob")) {
                                    fields[i].set(o,rs.getBlob(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                if (type.getName().equalsIgnoreCase("java.util.Date")) {
                                    fields[i].set(o,rs.getDate(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                            }
                        }
                    }
                }
                list.add(o);
                list.forEach(x -> {
                    System.out.println(x.getClass().getName());
                });
            }
        } catch (SQLException e) {

        } catch (InstantiationException e) {

        } catch (IllegalAccessException e) {

        } catch (SecurityException e) {

        } catch (IllegalArgumentException e) {

        }finally {


            connPoll.relaseConnection(conn);
            rs.close();
            stat.close();
        }
        return list;
    }
}
