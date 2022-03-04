package cn.lj3.preparedstatement.crud;

import com.lj3.bean.Order;
import com.lj3.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @author luojie
 * @Description
 * @date 2021/10/3 11/33
 */
public class OrderForQuery {
    @Test
    public void testqueryForOrder(){
        String sql = "select order_id id,order_name 'name',order_date 'date' from `order` where `order_id`=?";
        Order order = queryForOrder(sql, 1);
        System.out.println(order);
    }
    public Order queryForOrder(String sql,Object...args) {
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

            if(rs.next()) {
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    Object columValue = rs.getObject(i + 1);

                    //获取列名
//                    String columnName = rsmd.getColumnName(i + 1);
                    //获取别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给order对象指定的columnName属性，赋值为columValue，通过反射
                    Field field = Order.class.getDeclaredField(columnLabel);

                    field.setAccessible(true);
                    field.set(order,columValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }


        return null;
    }
}
