package com.guo.tools.util.jdbc;


import com.guo.tools.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.*;
import java.util.*;

/**
 * jdbc工具类
 */
public class JDBCHelper implements Serializable{

    //日志记录
    private final static Logger log = LoggerFactory.getLogger(JDBCHelper.class);

    //默认配置
    private static String DEF_JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String DEF_JDBC_CONF_MYSQL = "jdbc.properties";

    //配置参数
    public static final String KEY_JDBC_DRIVER = "jdbc.driver";
    public static final String KEY_JDBC_URL = "jdbc.url";
    public static final String KEY_JDBC_USER = "jdbc.user";
    public static final String KEY_JDBC_PASSWORD = "jdbc.password";


    //clickhouse连接参数
    public static final String KEY_JDBC_CH_CONNECT_TIMEOUT = "connect_timeout";
    public static final String KEY_JDBC_CH_CONNS_FAIL_MAX_TRIES = "connections_with_failover_max_tries";
    public static final String KEY_JDBC_CH_MAX_EXECUTIONTIME = "max_execution_time";
    public static final String KEY_JDBC_CH_MAX_RESULT_ROWS = "max_result_rows";
    public static final String KEY_JDBC_CH_RESULT_OVERFLOW_MODE = "result_overflow_mode";



    /**
     * 创建连接
     * @param confPath 配置参数文件路径
     * @return
     * @throws SQLException
     */
    public static Connection createConnection(String confPath) throws SQLException {
        Connection conn = null;

        //检测配置文件
        if(StringUtils.isEmpty(confPath)){
            log.error("jdbc.conf.err");
            throw new SQLException("dbc.conf.err");
        }

        //读取配置文件
        Properties pro = PropertyUtil.readProperties(confPath);
        String driver = pro.getProperty(KEY_JDBC_DRIVER);
        String user = pro.getProperty(KEY_JDBC_USER);
        String password = pro.getProperty(KEY_JDBC_PASSWORD);
        String url = pro.getProperty(KEY_JDBC_URL);

        //加载驱动
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error("jdbc.Class.forName.err=", e);
            System.exit(1);
        }
        return conn;
    }


    /**
     * 读取数据
     * @param confPath
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<Map<String,Object>> readJDBCData(String confPath, String sql) throws SQLException {
        List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        //检测配置文件
        if(StringUtils.isEmpty(confPath)){
            log.error("jdbc.conf.err");
            throw new SQLException("dbc.conf.err");
        }

        //检测执行sql
        if(StringUtils.isEmpty(sql)){
            log.error("jdbc.sql.err");
            throw new SQLException("dbc.sql.err");
        }
        log.info("Running: " + sql);

        try{
            conn = createConnection(confPath);
            if(null == conn){
                return records;
            }

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, Object> result = new HashMap<String, Object>();

                //元数据信息
                ResultSetMetaData metaData = rs.getMetaData();
                //列数量
                int colCount = metaData.getColumnCount();

                for(int i=1; i<=colCount; i++){
                    String colName = metaData.getColumnName(i);
                    Object colValue = rs.getObject(colName);

                    //System.out.println("colName=" + colName + ",colValue=" + colValue);
                    result.put(colName, colValue);
                }
                records.add(result);
            }
        }catch (Exception e){
            log.error("jdbc.execute.err", e.getMessage());
        }finally {
            if(null != rs){
                rs.close();
            }
            if(null != stmt){
                stmt.close();
            }
            if(null != conn){
                conn.close();
            }
        }

        return records;
    }



    public static void main(String[] args) throws Exception{

        JDBCHelper helper = new JDBCHelper();

        String conf = "jdbc.properties";
        String sql = "select * from travel.temps ";
        List<Map<String,Object>> datas = helper.readJDBCData(conf, sql);
        System.out.println("datas=" + datas.toString());

    }


}
