package com.example.demo.access;

import org.springframework.util.CollectionUtils;

import javax.print.attribute.standard.MediaSize;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AccessDataSourceUtil1s {

    static AccessDataSourcePool connPoll = null ;

    private static Map <String,AccessDataSourcePool> map = new HashMap<>();
    
    private static String SET_PREFIX = "set";
    
    private static String IS_PREFIX = "is";



    public static void main(String[] args)  throws Exception{

        String tableName = "层表";
        ReturnDataQuery dataQuery = getDataQuery(tableName);
        List<Tire> result = dataQuery.getResult();
        result.forEach(x -> {
            System.out.println(x);
        });
        /*List<Object> list = AccessDataExchange(tableName);
        AccessDataSourcePool accessDataSourcePool = new AccessDataSourcePool("");
        accessDataSourcePool.init();
        map.put("1",new AccessDataSourcePool(""));
        accessDataSourcePool.close();
        accessDataSourcePool = map.get("1");
*/
    }
    
    
    private static<T> List<T> AccessDataExchange(String tableName) throws Exception{

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
                    //fields[i].setAccessible(true);
                    AccessColumns annotation = fields[i].getAnnotation(AccessColumns.class);
                    if (!Objects.isNull(annotation)) {
                        Map<String, MdbColumn> map = mdbColumnList.stream().collect(Collectors.toMap(MdbColumn::getColumnName, Function.identity()));
                        Set<Map.Entry<String, MdbColumn>> entries = map.entrySet();
                        for (Map.Entry<String,MdbColumn> mdbColumnEntry: entries){
                            if (mdbColumnEntry.getKey().equalsIgnoreCase(annotation.value())) {
                                String filedName = fields[i].getName();
                                String name = filedName.substring(0, 1).toUpperCase() + filedName.substring(1, filedName.length());
                                Class<?> type = fields[i].getType();
                                if (type.getName().equalsIgnoreCase("java.lang.string")) {
                                    Method method = clazz.getDeclaredMethod(SET_PREFIX + name,type);
                                    method.setAccessible(true);
                                    method.invoke(o,rs.getString(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                else if (type.getName().equalsIgnoreCase("java.lang.Integer")|| type.getName().equalsIgnoreCase("int")) {
                                    Method method = clazz.getDeclaredMethod(SET_PREFIX + name,type);
                                    method.setAccessible(true);
                                    method.invoke(o,rs.getInt(mdbColumnEntry.getValue().getColumnIndex()),type);
                                }
                                else if (type.getName().equalsIgnoreCase("java.lang.Long")|| type.getName().equalsIgnoreCase("long")) {
                                    Method method = clazz.getDeclaredMethod(SET_PREFIX + name,type);
                                    method.setAccessible(true);
                                    method.invoke(o,rs.getLong(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                else if (type.getName().equalsIgnoreCase("java.lang.Short")|| type.getName().equalsIgnoreCase("short")) {
                                    Method method = clazz.getDeclaredMethod(SET_PREFIX + name,type);
                                    method.setAccessible(true);
                                    method.invoke(o,rs.getShort(mdbColumnEntry.getValue().getColumnIndex()),type);
                                }
                                else if (type.getName().equalsIgnoreCase("java.lang.Byte")|| type.getName().equalsIgnoreCase("int")) {
                                    Method method = clazz.getDeclaredMethod(SET_PREFIX + name,type);
                                    method.setAccessible(true);
                                    method.invoke(o,rs.getByte(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                else if (type.getName().equalsIgnoreCase("java.lang.Float")|| type.getName().equalsIgnoreCase("float")) {
                                    Method method = clazz.getDeclaredMethod(SET_PREFIX + name,type);
                                    method.setAccessible(true);
                                    method.invoke(o,rs.getFloat(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                else if (type.getName().equalsIgnoreCase("java.lang.Double")|| type.getName().equalsIgnoreCase("double")) {
                                    Method method = clazz.getDeclaredMethod(SET_PREFIX + name,type);
                                    method.setAccessible(true);
                                    method.invoke(o,rs.getDouble(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                else if (type.getName().equalsIgnoreCase("java.lang.BigDecimal")) {
                                    Method method = clazz.getDeclaredMethod(SET_PREFIX + name,type);
                                    method.setAccessible(true);
                                    method.invoke(o,rs.getBigDecimal(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                else if (type.getName().equalsIgnoreCase("java.lang.Boolean")|| type.getName().equalsIgnoreCase("boolean")) {
                                    Method method = clazz.getDeclaredMethod(IS_PREFIX + name,type);
                                    method.setAccessible(true);
                                    method.invoke(o,rs.getBoolean(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                else if (type.getName().equalsIgnoreCase("java.sql.Clob")) {
                                    Method method = clazz.getDeclaredMethod(SET_PREFIX + name,type);
                                    method.setAccessible(true);
                                    method.invoke(o,rs.getClob(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                else if (type.getName().equalsIgnoreCase("java.sql.Blob")) {
                                    Method method = clazz.getDeclaredMethod(SET_PREFIX + name,type);
                                    method.setAccessible(true);
                                    method.invoke(o,rs.getBlob(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                                else if (type.getName().equalsIgnoreCase("java.util.Date")) {
                                    Method method = clazz.getDeclaredMethod(SET_PREFIX + name,type);
                                    method.setAccessible(true);
                                    method.invoke(o,rs.getDate(mdbColumnEntry.getValue().getColumnIndex()));
                                }
                            }
                        }
                    }
                }

                list.add(o);
               
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
        List<T> list1 = BeanUtil.copyList(list, clazz);
        return list1;
    }
    
    public static ReturnDataQuery getDataQuery(String tableName){
        ReturnDataQuery query = new ReturnDataQuery();
        try {
            List<Object> list = AccessDataExchange(tableName);
            query.setQuery(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return query;
    }
}
