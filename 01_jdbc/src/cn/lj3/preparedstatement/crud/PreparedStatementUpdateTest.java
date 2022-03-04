package cn.lj3.preparedstatement.crud;

import com.lj3.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import static com.lj3.util.JDBCUtils.*;

/**
 *
 * 使用PreparedStatement来替换Statement,实现对数据库的增删改查操作
 * 增删改；查
 * @author luojie
 * @Description
 * @date 2021/10/2 10/22
 */
public class PreparedStatementUpdateTest {

    //通用的增删改操作
    public void update(String sql,Object...args){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);

            for(int i = 0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn,ps);
        }
    }


    //删除customers表的一条数据

    @Test
    public void testDelete() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();

            String sql = "DELETE FROM `customers` WHERE id=?;";

            ps = conn.prepareStatement(sql);
            ps.setObject(1,18);

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn,ps);
        }



    }

    //修改customers表的一条记录
    @Test
    public void testUpdate(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();

            String sql = "update customers set name=? where id=?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,"莫扎特");
            ps.setObject(2,18);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }



    }
    @Test
    public void testInsert() {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            //读取配置文件中的四个基本信息
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties pros = new Properties();
            pros.load(is);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            //加载驱动
            Class.forName(driverClass);

            //回去连接
            conn = DriverManager.getConnection(url, user, password);
//        System.out.println(conn);

            //预编译sql语句，返回PreparedStatement的实例
            String sql = "insert into customers(name,email,birth)values(?,?,?)";//?占位符
            ps = conn.prepareStatement(sql);


            //填充占位符
            ps.setString(1,"赵丽颖");
            ps.setString(2,"zhaoly@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1989-07-06");
            ps.setDate(3,new Date(date.getTime()));

            //执行操作
            ps.execute();
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            //资源关闭

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        }



    }
}
