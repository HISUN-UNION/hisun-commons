package com.hisun.util.test;

import java.sql.*;
/**
 * Created by zhouying on 2017/9/4.
 */
public class SqliteTest {
    public static void main( String args[] )
    {

        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/zhouying/Desktop/zzb-app.sqlite");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}
