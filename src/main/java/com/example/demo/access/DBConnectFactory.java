package com.example.demo.access;


import org.apache.commons.pool.PoolableObjectFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectFactory implements PoolableObjectFactory {

    private int count =0 ;
    private String driver ;
    private String className;
    public DBConnectFactory(String className,String driver){
        this.className = className;
        this.driver = driver;
    }
    /**
     * 对象初始化后加入池的时候使用，设置对象为可用状态。
     */
    @Override
    public void activateObject(Object arg0) throws Exception {
        //直接加入

    }
    /**
     * 对象销毁方法
     */
    @Override
    public void destroyObject(Object arg0) throws Exception {
        System.err.println("destroyObject Object " + arg0.getClass().getName());
        if(arg0!=null){
            Connection con = (Connection)arg0;
            try{
                con.close();
                count --;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    /**
     * 对象创建方法
     */
    @Override
    public Object makeObject() throws Exception {
        try {
                Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        count ++;
        return  DriverManager.getConnection(driver);

    }
    /**
     * 非创建对象回到池的时使用,设置对象为可用.
     */
    @Override
    public void passivateObject(Object arg0) throws Exception {
        Connection con = (Connection)arg0;
        //如果非自动commit类型连接，强制comit后返回连接池
        if(!con.getAutoCommit()){
            con.commit();
        }

    }
    /**
     * 状态检查对象是否可用
     */
    @Override
    public boolean validateObject(Object arg0) {

        try{
            Connection con = (Connection)arg0;
            return con!=null&&!con.isClosed();
        }catch(Exception e){
            return false ;
        }

    }


}

