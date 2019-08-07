package com.example.demo.access;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import java.sql.Connection;

public class AccessDataSourcePool {

    GenericObjectPool pool  = null;
    PoolableObjectFactory factory = null;

    public AccessDataSourcePool(String accessFilePath){
        String className = "net.ucanaccess.jdbc.UcanaccessDriver"	;
        String driver = "jdbc:ucanaccess://E:\\idea_work_space\\demo\\resources\\分摊库.mdb";
        driver = driver + accessFilePath;
        factory = new DBConnectFactory(className,driver);

    }
    public void init(){
        pool = new GenericObjectPool(factory);
        //最大连接数
        pool.setMaxActive(30);
        //最大空闲连接数
        pool.setMaxIdle(10);
        //连接等待1分钟
        pool.setMaxWait(60000);
    }


    public void close() throws Exception{
         pool.close();
        //pool.clear();
    }


    /** 获得连接*/
    public Connection getConnection() {
        try {
            Connection connection = (Connection) pool.borrowObject();
            factory.validateObject(connection);
            return	connection;
        }catch (Exception e){
            throw new RuntimeException("get connect error",e);
        }

    }
    /**释放连接*/
    public void relaseConnection(Connection con){
        try {
            pool.returnObject(con);
        } catch (Exception e) {
            throw new RuntimeException("relase connect error",e);
        }
    }



}
