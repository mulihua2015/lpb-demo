package com.example.demo;

import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AccessQueryCriteria {

    private ResultSet rs;

    public AccessQueryCriteria internalQuery(String tableName, String dbUrl, String driverName) {
        // url表示需要连接的数据源的位置，此时使用的是JDBC-ODBC桥的连接方式，url是"jdbc:odbc:数据源名"
        if (StringUtils.isEmpty(tableName)) {
            throw new RuntimeException("所需要查询的表名为空");
        }
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");//加载ucanaccess驱动
            Connection conn = DriverManager.getConnection(dbUrl);
            Statement stat = conn.createStatement();
            String sql = "select * from " + tableName;
            ResultSet rs = stat.executeQuery(sql);
            this.rs = rs;

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("查询错误" + e);
        }
        return this;
    }

    /**
     * 获取结果集
     */
    public List<Map<String, Object>> getResultMap() throws SQLException {
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        List<Map<String, Object>> resultList = new LinkedList<>();
        while (rs.next()) {
            Map<String, Object> singleRow = new LinkedHashMap<>();
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                String columnName = resultSetMetaData.getColumnLabel(i);
                Object columnValue = rs.getObject(columnName);
                singleRow.put(columnName, columnValue);
            }
            resultList.add(singleRow);
        }
        return resultList;
    }

    /**
     * 拼英转化为首字母
     */
    public List<Map<String, Object>> getResultMapConvertChineseToFirstChar() throws SQLException {
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        List<Map<String, Object>> resultList = new LinkedList<>();
        while (rs.next()) {
            Map<String, Object> singleRow = new LinkedHashMap<>();
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                String columnName = resultSetMetaData.getColumnLabel(i);
                Object columnValue = rs.getObject(columnName);
                singleRow.put(StringUtil.converPinYinHeadChar(columnName), columnValue);
            }
            resultList.add(singleRow);
        }
        return resultList;
    }
}
