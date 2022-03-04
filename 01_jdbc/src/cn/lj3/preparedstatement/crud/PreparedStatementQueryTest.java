package cn.lj3.preparedstatement.crud;

import com.lj3.bean.Customer;
import com.lj3.bean.Order;
import com.lj3.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用PreparedStatement实现针对于不同表的通用查询操作
 * @author luojie
 * @Description
 * @date 2021/10/3 15/32
 */
public class PreparedStatementQueryTest {

    @Test
    public void testgetForList(){
        String sql = "select id,name,birth,email from customers where id<?";

        List<Customer> list = getForList(Customer.class, sql, 12);

        list.forEach(System.out::println);
    }

    public <T> List<T> getForList(Class<T> clazz, String sql, Object...args){
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

            //船舰集合对象
            ArrayList<T> list = new ArrayList<>();

            while (rs.next()){
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
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }

        return null;
    }





    @Test
    public void testGetInstance(){

        String sql = "select id,name,birth,email from customers where id=?";
        Customer instance = getInstance(Customer.class, sql, 12);

        System.out.println(instance);

    }
    @Test
    public void testGetInstance2(){
        String sql = "select order_id id,order_name 'name',order_date 'date' from `order` where `order_id`=?";

        Order order = getInstance(Order.class, sql, 2);
        System.out.println(order);
    }

    /**
     * 针对不同的2表通用的查询操作，返回表中的一条数据
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> clazz,String sql,Object...args){
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
            JDBCUtils.closeResource(conn,ps,rs);
        }

        return null;
    }
}
