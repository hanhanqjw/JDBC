package com.lj4.exer;

import com.lj3.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author luojie
 * @Description
 * @date 2021/10/4 16/17
 */
public class Exer3Test {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请选择您要输入的类型：");
        System.out.println("a:准考证号");
        System.out.println("b:身份证号");

        String selection = scanner.next();
        if("a".equalsIgnoreCase(selection)){
            System.out.println("请输入准考证号：");
            String ExamCard = scanner.next();

            String sql = "SELECT FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade FROM examstudent WHERE examCard=?";
            Student student = queryForOrder(Student.class,sql, ExamCard);
            System.out.println(student);

        }else if("b".equalsIgnoreCase(selection)){
            System.out.println("请输入身份证号：");

            String IDCard = scanner.next();
            String sql = "SELECT FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade FROM examstudent WHERE IDCard=?";
            Student student = queryForOrder(Student.class,sql,IDCard);
            System.out.println(student);

        }else{
            System.out.println("您的输入有误，请重新进入程序。");
        }
    }

    public static <T>T queryForOrder(Class<T> clazz, String sql, Object...args){
        Connection conn = null;
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

            if (rs.next()){
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
            JDBCUtils.closeResource(conn,ps,rs);
        }

        return null;
    }
}
