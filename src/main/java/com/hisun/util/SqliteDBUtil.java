package com.hisun.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouying on 2017/9/14.
 */
public class SqliteDBUtil {

    private final static Logger logger = Logger.getLogger(SqliteDBUtil.class);
    private static SqliteDBUtil util = null;

    public static SqliteDBUtil newInstance() {

        if (util == null) {
            synchronized (SqliteDBUtil.class) {
                if (util == null) {
                        util = new SqliteDBUtil();
                }
            }
        }
        return util;
    }


    /**
     * 获取数据库连接
     *
     * @param dbname 数据库名称
     * @return conn
     * @throws ClassNotFoundException
     */
    private   Connection getConnection(String dbname) throws SQLException, ClassNotFoundException {
        if (StringUtils.isBlank(dbname)) {
            return null;
        }
        Class.forName("org.sqlite.JDBC");
        if (!dbname.endsWith(".db")) {
            dbname = dbname + ".db";
        }
        return DriverManager.getConnection("jdbc:sqlite:" + dbname);
    }

    /**
     * 获取一个数据连接声明
     *
     * @param conn 数据库连接
     * @return statement
     * @throws SQLException
     */
    private Statement getStatement(Connection conn) throws SQLException {
        if (null == conn) {
            return null;
        }
        return conn.createStatement();
    }

    /**
     * 根据数据库名称获取数据库连接声明
     *
     * @param dbname 数据库名称
     * @return statement
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Statement getStatementByDBName(String dbname) throws SQLException, ClassNotFoundException {
        if (StringUtils.isBlank(dbname)) {
            return null;
        }
        return getStatement(getConnection(dbname));
    }

    /**
     * 创建sqlite数据库
     *
     * @param dbname 数据库名称
     * @return 0：失败；1：成功
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Statement createDatabase(String dbname) throws ClassNotFoundException, SQLException {
        Statement statement = getStatementByDBName(dbname);

        if (null != statement) {
            return statement;
        }
        return null;
    }

    /**
     * 关闭声明
     *
     * @param statement
     * @throws SQLException
     */
    public  void closeStatement(Statement statement) throws SQLException {
        if (null != statement && !statement.isClosed()) {
            Connection conn = statement.getConnection();
            statement.close();
            closeConnection(conn);
        }
    }

    /**
     * 关闭声明
     *
     * @param conn
     * @throws SQLException
     */
    public  void closeConnection(Connection conn) throws SQLException {
        if (null != conn && !conn.isClosed()) {
            conn.close();
        }
    }

    /**
     * 创建数据库表
     *
     * @param dbname 数据库名称
     * @param sql    创建语句
     * @return 0：创建失败；1：创建成功
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public int createTables(String dbname, String sql) throws ClassNotFoundException, SQLException {
        if (StringUtils.isBlank(sql)) {
            return 0;
        }
        Statement statement = getStatementByDBName(dbname);
        if (null != statement) {
            try {

                statement.executeUpdate(sql);

            } catch (Exception e) {

                System.err.println(e.getMessage());

            } finally {

                closeStatement(statement);

            }

            return 1;
        }
        return 0;
    }

    /**
     * 插入数据
     *
     * @param dbname 数据库名称
     * @param sql    insert语句
     * @return 0：插入失败；1：插入成功
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public int insert(String dbname, String sql) throws ClassNotFoundException, SQLException {
        if (StringUtils.isBlank(sql)) {
            return 0;
        }
        Statement statement = getStatementByDBName(dbname);
        if (null != statement) {
            statement.executeUpdate(sql);
            closeStatement(statement);
            return 1;
        }
        return 0;
    }

    /**
     * 修改数据
     *
     * @param dbname 数据库名称
     * @param sql    update语句
     * @return 0：插入失败；1：插入成功
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public int update(String dbname, String sql) throws ClassNotFoundException, SQLException {
        if (StringUtils.isBlank(sql)) {
            return 0;
        }
        Statement statement = getStatementByDBName(dbname);
        if (null != statement) {
            statement.executeUpdate(sql);
            closeStatement(statement);
            return 1;
        }
        return 0;
    }

    /**
     * 批量插入数据
     *
     * @param dbname 数据库名称
     * @param sqls    insert语句
     * @return 0：插入失败；1：插入成功
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public int insertBatch(String dbname, List<String> sqls) throws ClassNotFoundException, SQLException {
        if (null == sqls || sqls.isEmpty()) {
            return 0;
        }
        Connection conn = getConnection(dbname);

        if (null == conn) {
            return 0;
        }

        conn.setAutoCommit(false);
        Statement statement = getStatement(conn);

        if (null == statement) {
            closeConnection(conn);
            return 0;
        }

        for (String sql : sqls) {
            if (StringUtils.isNotBlank(sql)) {
                statement.executeUpdate(sql);
            }
        }
        closeStatement(statement);
        conn.commit();
        closeConnection(conn);
        return 1;
    }

    /**
     * 更新和删除
     *
     * @param dbname     数据库名称
     * @param sql
     * @param parameters
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public  int update(String dbname, String sql, Object[] parameters)
            throws ClassNotFoundException, SQLException {
        return execute(dbname, sql, parameters, 0);
    }

    /**
     * 添加
     *
     * @param dbname     数据库名称
     * @param sql
     * @param parameters
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public  int insert(String dbname, String sql, Object[] parameters)
            throws ClassNotFoundException, SQLException {
        return execute(dbname, sql, parameters, 1);
    }

    /**
     * 执行增删改
     *
     * @param dbname     数据库名称
     * @param sql
     * @param parameters
     * @param type       0为删改，1为增加
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private  int execute(String dbname, String sql, Object[] parameters, int type)
            throws SQLException, ClassNotFoundException {
        Connection conn = getConnection(dbname);
        PreparedStatement ps = null;
        int count = 0;
        if (conn != null) {
            try {

                ps = conn.prepareStatement(sql);
                if (null == ps) {
                    closeConnection(conn);
                    return count;
                }
                for (int i = type + 1; i <= parameters.length + type; i++) {
                    ps.setObject(i, parameters[i - (1 + type)]);
                }
                count = ps.executeUpdate();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                closeStatement(ps);
            }
        }
        return count;
    }

    /**
     * 执行查询，并将值反射到bean
     *
     * @param sql
     * @param parameters
     * @param clazz
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public  <T> List<T> select(String dbname, String sql, Object[] parameters, Class<T> clazz)
            throws ClassNotFoundException, SQLException {
        List<T> list = new ArrayList<T>();
        Connection conn = getConnection(dbname);
        if (null == conn) {
            return null;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = conn.prepareStatement(sql);
        if (parameters != null && ps != null) {

            try {
                for (int i = 1; i <= parameters.length; i++) {
                    ps.setObject(i, parameters[i - 1]);
                }
                // 执行查询方法
                rs = ps.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                List<String> columnList = new ArrayList<String>();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    columnList.add(rsmd.getColumnName(i + 1));
                }
                // 循环遍历记录
                while (rs.next()) {
                    // 创建封装记录的对象
                    T obj = clazz.newInstance();
                    // 遍历一个记录中的所有列
                    for (int i = 0; i < columnList.size(); i++) {
                        // 获取列名
                        String column = columnList.get(i);
                        // 根据列名创建set方法
                        String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
                        // 获取clazz中所有方法对应的Method对象
                        Method[] ms = clazz.getMethods();
                        // 循环遍历ms
                        for (int j = 0; j < ms.length; j++) {
                            // 获取每一个method对象
                            Method m = ms[j];
                            // 判断m中对应的方法名和数据库中列名创建的set方法名是否形同
                            if (m.getName().equals(setMethd)) {
                                // 反调set方法封装数据
                                m.invoke(obj, rs.getObject(column));// 获取rs中对应的值，封装到obj中
                                break; // 提高效率
                            }
                        }
                    }
                    list.add(obj);
                }

            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                closeStatement(ps);
            }
        } else {
            closeConnection(conn);
        }
        return list;
    }

    /**
     * 执行查询，并将值反射到map
     *
     * @param sql
     * @param parameters
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public  List<Map<String, Object>> select(String dbname, String sql, Object[] parameters)
            throws ClassNotFoundException, SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        Connection conn = getConnection(dbname);
        if (null == conn) {
            return null;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = conn.prepareStatement(sql);
        if (ps != null) {

            try {
                if (parameters != null) {
                    for (int i = 1; i <= parameters.length; i++) {
                        ps.setObject(i, parameters[i - 1]);
                    }
                }
                // 执行查询方法
                rs = ps.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                List<String> columnList = new ArrayList<String>();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    columnList.add(rsmd.getColumnName(i + 1));
                }
                // 循环遍历记录
                while (rs.next()) {
                    Map<String, Object> obj = new HashMap<>();
                    // 遍历一个记录中的所有列
                    for (int i = 0; i < columnList.size(); i++) {
                        // 获取列名
                        String column = columnList.get(i);
                        obj.put(column, rs.getObject(column));
                    }
                    list.add(obj);
                }

            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                closeStatement(ps);
            }
        } else {
            closeConnection(conn);
        }
        return list;
    }

}
