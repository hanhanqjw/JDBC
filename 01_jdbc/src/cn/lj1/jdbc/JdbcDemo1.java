package cn.lj1.jdbc;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author luojie
 * @Description
 * @date 2021/10/1 17/11
 */
public class JdbcDemo1 {

    //方式一
    @Test
    public void testConnection1() throws SQLException {
        Driver driver = new com.mysql.jdbc.Driver();

        //获取要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test";
        //用户名和密码
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","1234");

        Connection conn = driver.connect(url, info);

        System.out.println(conn);
    }
    //方式二
    @Test
    public void testConnection2() throws Exception {
        //1.获取Driver实现类对象，使用反射

        Class clazz = Class.forName("com.mysql.jdbc.Driver");

        Driver driver  = (Driver) clazz.newInstance();
        //获取要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test";

        //用户名和密码
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","1234");

        Connection conn = driver.connect(url,info);
    }

    //方式三：使用DriverManager替换Driver
    @Test
    public void testConnection3() throws Exception {
        //1.获取Driver实现类对象，使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");

        Driver driver  = (Driver) clazz.newInstance();

        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "1234";
        //3注册驱动
        DriverManager.registerDriver(driver);
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    //方式四：可以只是加载驱动，不用显示的注册驱动了
    @Test
    public void testConnection4() throws Exception {
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "1234";

        //1.获取Driver实现类对象，使用反射
        Class.forName("com.mysql.jdbc.Driver");

        //相较于方式三可以省略以下操作
//        Driver driver  = (Driver) clazz.newInstance();
//
//        //3注册驱动
//        DriverManager.registerDriver(driver);
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    //方式五：将数据库连接需要的四个基本信息声明在配置文件中
    //通过读取配置文件获取链接
    @Test
    public void testConnection5() throws Exception {
        InputStream is = JdbcDemo1.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        Class.forName(driverClass);

        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }


}
