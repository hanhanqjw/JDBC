package cn.lj3.preparedstatement.crud;

import com.lj3.bean.Customer;
import com.lj3.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**针对于Customers表的查询操作
 * @author luojie
 * @Description
 * @date 2021/10/3 09/54
 */
public class CustomerForQuery {
    @Test
    public void testqueryForCustomer(){
        String sql = "select id,name,birth,email from customers where id=?";

        Customer customer = queryForCustomer(sql, 13);
        System.out.println(customer);
        String sql1 = "select id,name,birth from customers where name=?";
        Customer customer1 = queryForCustomer(sql1, "迪丽热巴");
        System.out.println(customer1);
    }

    public Customer queryForCustomer(String sql,Object ...args) {
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
                Customer cust = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    Object columValue = rs.getObject(i + 1);

                    //获取每个列的列名
//                    String columnName = rsmd.getColumnName(i + 1);

                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给cust对象指定的columnName属性，赋值为columValue，通过反射

                    Field field = Customer.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(cust,columValue);
                }
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }

            return null;
    }
    @Test
    public void testQuery1(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();

            String sql = "select id,name,email,birth from customers where id=?";
            ps = conn.prepareStatement(sql);

            ps.setObject(1,1);

            //执行并返回结果集
            resultSet = ps.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

    //            Object[] data = new Object[]{id,name,email,birth};

                //将数据封装为一个对象

                Customer customer = new Customer(id,name,email,birth);

                System.out.println(customer);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps,resultSet);
        }


    }
}
