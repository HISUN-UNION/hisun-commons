package com.hisun.util;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created by zhouying on 2017/9/14.
 */
public class SqliteDBUtil {

    private final static Logger logger = Logger.getLogger(SqliteDBUtil.class);
    private static SqliteDBUtil util = null;
    private static Connection conn = null;

    public static SqliteDBUtil newInstance(String sqlitedb) {

        if (util == null) {
            synchronized (SqliteDBUtil.class) {
                if (util == null) {
                    try {
                        util = new SqliteDBUtil();
                        Class.forName("org.sqlite.JDBC");
                        if (sqlitedb == null || sqlitedb.length() == 0) {
                            conn = DriverManager.getConnection("jdbc:sqlite:"
                                    +SqliteDBUtil.class.getClassLoader().getResource("").getPath()
                                    + "zzb-app.sqlite");
                        }else{
                            conn = DriverManager.getConnection("jdbc:sqlite:" + sqlitedb);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
        return util;
    }

    public PreparedStatement getPreparedStatement(String StatementString) {
        PreparedStatement preparedstatement = null;
        try {
            preparedstatement = conn.prepareStatement(StatementString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preparedstatement;

    }

    public Statement getStatement() {
        Statement statement = null;
        try {
            statement = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statement;
    }

    public void Commit() {
        try {
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Rollback() {
        try {
            conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CloseConnection(boolean isSuccess) {
        try {
            // Rollback or commit the transaction

            try {
                if (isSuccess == false) {
                    conn.rollback();
                } else {
                    if (conn != null) {
                        if (!conn.getAutoCommit()) {
                            try {
                                conn.commit();
                            } catch (Exception e) {
                                conn.rollback();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    conn = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void CloseConnection() {
        try {
            //  commit the transaction

            try {

                if (conn != null) {
                    if (!conn.getAutoCommit()) {
                        try {
                            conn.commit();
                        } catch (Exception e) {
                            conn.rollback();
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    conn = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void CloseStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
       // SqliteDBUtil.newInstance("");
       // System.out.println(SqliteDBUtil.class.getClassLoader().getResource("").getPath());
    }

}
