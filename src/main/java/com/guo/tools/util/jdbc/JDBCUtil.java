package com.guo.tools.util.jdbc;

import com.google.gson.Gson;
import com.guo.tools.constant.CommonConstant;
import com.guo.tools.util.CommonUtil;
import com.guo.tools.util.PropertyUtil;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBCUtil {

    static Connection conn = null;
    static PreparedStatement statement = null;
    static ResultSet rs = null;

    private static void getConnection(String sql) {
        try {
            Properties jdbcPro = PropertyUtil.readProperties(CommonConstant.JDBC_CONFIG_URL);

            String driver = jdbcPro.getProperty(CommonConstant.JDBC_DRIVER);
            String url = jdbcPro.getProperty(CommonConstant.JDBC_URL);
            String username = jdbcPro.getProperty(CommonConstant.JDBC_USERNAME);
            String passwd = jdbcPro.getProperty(CommonConstant.JDBC_PASSWD);

            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, passwd);
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 6. 关闭SQL声明Statement
        if(statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 6. 关闭连接
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定一个类，通过sql语句返回。要求类中字段的顺序和类型必须严格一致
     * @param quarySql
     * @param type
     * @return
     */
    public static List<Object> getResult(String quarySql, Class<?> type) {
        try {
            return result(quarySql, type);
        } catch (SQLException | InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Object> result(String quarySql, Class<?> type) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // 获取连接
        getConnection(quarySql);

        // 获取数据
        List<Object> list = new ArrayList<>();

        while (rs.next()) {
            ResultSetMetaData meta = rs.getMetaData();
            // 获取列数量
            int count = meta.getColumnCount();
            Object[] objects = new Object[count];
            for (int i = 1; i <= count; i++) {
                String columnClassName = meta.getColumnClassName(i);
                // 如果无法获取，请升级jdbc
                //Object object = rs.getObject(i, Class.forName(columnClassName));
                //System.out.println(columnClassName);

                objects[i - 1] = rs.getObject(i);

                //System.out.println(object);
            }
            Object record = CommonUtil.setFields(objects, type);
            //Object record = "123";
            // 将一列写入list
            list.add(record);

        }

        // 关闭连接
        close();

        return list;
    }

    public static <T> List<T> getResultV2(String quarySql,Class<T> classes) throws SQLException {
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();

        // 获取连接
        getConnection(quarySql);

        while (rs.next()) {
            ResultSetMetaData meta = rs.getMetaData();
            StringBuilder builder = new StringBuilder("{");
            // 获取列数量
            int count = meta.getColumnCount();
            for (int i = 1; i <= count; i++) {

                String columnName = meta.getColumnName(i);
                builder.append("\"").append(columnName).append("\"").append(":");


                Object columnValue = rs.getObject(i);
                builder.append("\"").append(columnValue).append("\"").append(",");
            }
            builder.insert(builder.length() - 1, "}");
            String json = builder.substring(0, builder.length() - 1);
            list.add(gson.fromJson(json, classes));
        }

        // 关闭连接
        close();

        return list;
    }

    private static List<String> result(String quarySql) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // 获取连接
        getConnection(quarySql);

        // 获取数据
        List<String> list = new ArrayList<>();

        while (rs.next()) {
            ResultSetMetaData meta = rs.getMetaData();
            StringBuilder builder = new StringBuilder("{");
            // 获取列数量
            int count = meta.getColumnCount();
            for (int i = 1; i <= count; i++) {

                String columnName = meta.getColumnName(i);
                builder.append("\"").append(columnName).append("\"").append(":");

                //String columnValue = rs.getObject(i).toString();
                //builder.append("\"").append(columnValue).append("\"").append(",");

                Object columnValue = rs.getObject(i);
                builder.append("\"").append(columnValue).append("\"").append(",");
            }
            builder.insert(builder.length() - 1, "}");
            list.add(builder.substring(0, builder.length() - 1));
        }

        // 关闭连接
        close();

        return list;
    }
}

