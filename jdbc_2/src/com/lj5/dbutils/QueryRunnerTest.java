package com.lj5.dbutils;

import com.lj2.bean.Customer;
import com.lj4.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luojie
 * @Description
 * @date 2021/10/14 20/48
 */
public class QueryRunnerTest {

    //测试插入
    @Test
    public void testInsert() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection2();
            String sql = "insert into customers(name,email,birth)value(?,?,?)";
            int i = runner.update(conn, sql, "蔡徐坤", "caixk@qq.com", new Date(67363477L));
            System.out.println("添加了" + i+"条数据");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }


    //测试查询

    /**
     * BeanHandler:是ResultSetHandler接口的实现类，用于封装表中的一条记录
     * @throws Exception
     */
    @Test
    public void testQuery1() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection2();
            String sql = "select id,name,email,birth from customers where id=?";

            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            Customer customer = runner.query(conn, sql, handler, 26);
            System.out.println(customer);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    /**
     * BeanListHandler:是ResultSetHandler接口的实现类，用于封装表中的多条记录
     * @throws Exception
     */
    @Test
    public void testQuery2() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection2();
            String sql = "select id,name,email,birth from customers where id<?";

            BeanListHandler<Customer> listHandler = new BeanListHandler<>(Customer.class);
            List<Customer> customers = runner.query(conn, sql, listHandler, 26);
            customers.forEach(System.out::println);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    /**
     * MapHandler:是ResultSetHandler接口的实现类，对应表中的一条数据
     * 将字段及相应字段的值作为map中的key 和 value
     * @throws Exception
     */
    @Test
    public void testQuery3() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection2();
            String sql = "select id,name,email,birth from customers where id=?";

            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(conn, sql, handler, 26);
            System.out.println(map);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    /**
     * MapListHandler:是ResultSetHandler接口的实现类，对应表中的多条数据
     * 将字段及相应字段的值作为map中的key 和 value，将这些map添加到list
     * @throws Exception
     */
    @Test
    public void testQuery4() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection2();
            String sql = "select id,name,email,birth from customers where id<?";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> mapList = runner.query(conn, sql, handler, 26);
            mapList.forEach(System.out::println);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    /**
     * ScalarHandler:用于处理特殊值
     */
    @Test
    public void testQuery5() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection2();
            String sql = "select count(*) from customers";
            ScalarHandler handler = new ScalarHandler();
            long count = (Long) runner.query(conn, sql, handler);
            System.out.println(count);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

}
