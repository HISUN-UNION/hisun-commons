package com.hisun.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * Created by zhouying on 2017/12/14.
 */
public class C3p0Util {

    public static DataSource getSqlServerDataSource(String ip,
                                                    String port,
                                                    String dbname,
                                                    String user,
                                                    String password) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setJdbcUrl("jdbc:sqlserver://"+ip+":"+port+";DatabaseName="+dbname);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        dataSource.setInitialPoolSize(10);  //初始化连接个数
        dataSource.setMaxPoolSize(40);   //最大链接数
        dataSource.setMinPoolSize(5);  //设置最小链接数
        dataSource.setAcquireIncrement(2);    //设置每次增加的连接数
        return dataSource;
    }


    public static DataSource getMySQLDataSource(String ip,
                                                    String port,
                                                    String dbname,
                                                    String user,
                                                    String password) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://"+ip+":"+port+"/"+dbname+"?" +
                "useUnicode=true&characterEncoding=utf-8&autoReconnect=true" +
                "&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true");
        dataSource.setUser(user);
        dataSource.setPassword(password);

        dataSource.setInitialPoolSize(10);  //初始化连接个数
        dataSource.setMaxPoolSize(40);   //最大链接数
        dataSource.setMinPoolSize(5);  //设置最小链接数
        dataSource.setAcquireIncrement(2);    //设置每次增加的连接数
        return dataSource;
    }


    public static void main(String[] args) throws Exception{
        System.out.println(getMySQLDataSource("192.168.70.59","45017","zwhzyq","root","admin"));
    }

}
