package com.lj1.transaction;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lj1.util.JDBCUtils;
import org.junit.Test;

/**
 * @author luojie
 * @Description
 * @date 2021/10/9 16/16
 */
public class TransactionTest {
    @Test
    public void testUpdate(){
        String sql1 = "UPDATE user_table SET balance=balance-100 WHERE USER=?";
        update(sql1,"AA");

        String sql2 = "UPDATE `user_table` SET balance=balance+100 WHERE USER=?";
        update(sql2,"BB");
    }

    //通用的增删改操作---version 1.0
    public int update(String sql,Object...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

           return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }


    //考虑数据库事务后的转账操作

    @Test
    public void testUpdateWithTx(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            //1取消数据的自动提交功能

            conn.setAutoCommit(false);

            String sql1 = "UPDATE user_table SET balance=balance-100 WHERE USER=?";
            update(conn,sql1,"AA");

            System.out.println(10/0);

            String sql2 = "UPDATE `user_table` SET balance=balance+100 WHERE USER=?";
            update(conn,sql2,"BB");

            System.out.println("转账成功");

            //2提交数据
            conn.commit();
            
        } catch (Exception e) {
            e.printStackTrace();
            //3回滚操作
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtils.closeResource(conn,null);
        }

    }
    //通用的增删改操作---version 2.0(考虑上事务)
    public int update(Connection conn,String sql,Object...args) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }

    @Test
    public void testTransactionSelect() throws Exception {
        Connection conn = JDBCUtils.getConnection();

        System.out.println(conn.getTransactionIsolation());
        String sql = "select user,password,balance from user_table where user=?";
        User user = getInstance(conn, User.class, sql, "CC");
        System.out.println(user);

    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();

        String sql = "update user_table set balance=? where user=?";

        update(conn,sql,500,"CC");
        Thread.sleep(15000);
        System.out.println("修改结束");

    }

    //通用的查询操作,用于返回数据表中的一条记录（version2.0，考虑上事务）
    public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object...args){

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();

            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if(rs.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columValue = rs.getObject(i + 1);

                    //获取每个列的列名
//                    String columnName = rsmd.getColumnName(i + 1);

                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给t对象指定的columnName属性，赋值为columValue，通过反射

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }

        return null;
    }
}
