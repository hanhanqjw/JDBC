package com.lj4.exer;

import com.lj3.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import static com.lj3.util.JDBCUtils.closeResource;
import static com.lj3.util.JDBCUtils.getConnection;

/**
 * @author luojie
 * @Description
 * @date 2021/10/3 17/06
 */
public class Exer2Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("四级/六级：");
        int type = scanner.nextInt();
        System.out.print("身份证号:");
        String IDCard = scanner.next();
        System.out.print("准考证号:");
        String ExamCard = scanner.next();
        System.out.print("学生姓名:");
        String StudentName = scanner.next();
        System.out.print("所在城市:");
        String Location = scanner.next();
        System.out.print("考试成绩:");
        String Grade = scanner.next();

        String sql = "INSERT INTO examstudent(`Type`,`IDCard`,ExamCard,`StudentName`,`Location`,`Grade`) VALUES(?,?,?,?,?,?) ";
        int insert = update(sql, type, IDCard, ExamCard, StudentName, Location, Grade);
        if(insert>0){
            System.out.println("插入成功");
        }else{
            System.out.println("插入失败");
        }

    }
    public static int update(String sql, Object... args){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);

            for(int i = 0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }

           return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn,ps);
        }
        return 0;
    }
}
