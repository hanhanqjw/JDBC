package com.lj4.exer;

import com.lj3.util.JDBCUtils;
import com.lj3.util.JDBCUtils.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * @author luojie
 * @Description
 * @date 2021/10/4 18/39
 */
public class Exer4Test {
    public static void main(String[] args) {
        System.out.println("-------------删除功能-------------");
        System.out.println("请输入学生的考号：");
        Scanner scanner = new Scanner(System.in);
        String examCard = scanner.next();
        String sql = "DELETE FROM examstudent WHERE examCard=?";
        int i = update(sql, examCard);

        if(i > 0 ){
            System.out.println("删除成功");
        }else{
            System.out.println("查无此人");
        }
    }
    public static int update(String sql,Object...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }

           return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }
        return 0;
    }
}
