package com.guo.tools;

import com.guo.tools.testClass.TempClassify;
import com.guo.tools.util.jdbc.JDBCUtil;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class ToolsTest {

    @Test
    public void testJDBC() throws SQLException {
        String sql = "select * from temp_classify";

        List<TempClassify> classifies = JDBCUtil.getResultV2(sql, TempClassify.class);
        for (TempClassify classify : classifies) {
            System.out.println(classify);
        }
    }
}
