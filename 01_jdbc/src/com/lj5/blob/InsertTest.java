package com.lj5.blob;

import com.lj3.util.JDBCUtils;
import jdk.nashorn.internal.scripts.JD;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 使用PreparedStatement实现批量数据的操作
 * 批量插入
 * 方式一：Statement
 * @author luojie
 * @Description
 * @date 2021/10/9 14/53
 */
public class InsertTest {
    @Test
    public void testInsert2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();

            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <=20000 ; i++) {
                ps.setObject(1,"name_"+i);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println(end-start);//106052
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }

    }

    /**
     * 批量插入的方式三
     * 1、addBatch()、executeBatch()、clearBatch()
     */
    @Test
    public void InserTest3(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <=20000 ; i++) {
                ps.setObject(1,"name_"+i);
                ps.addBatch();
                if(i%500 == 0){
                    ps.executeBatch();
                    conn.commit();
                }


            }
            long end = System.currentTimeMillis();
            System.out.println(end-start);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }

    }
}
