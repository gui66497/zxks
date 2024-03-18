package zzjz.util;

import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author 梅宏振
 * @version 2016/9/8 14:26
 * @ClassName: DBUtil
 * @Description: 数据库操作工具类
 */
public class DBUtil {

    /**
     * 关闭回话.
     *
     * @param statement statement
     */
    public static void close(Statement statement) {
        if (null != statement) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭结果集.
     *
     * @param rs rs
     */
    public static void close(ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭数据库链接.
     *
     * @param conn conn
     */
    public static void close(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param statement statement
     */
    public static void close(PreparedStatement statement) {
        if (null != statement) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
