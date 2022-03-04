package com.lj5.blob;

import com.lj3.bean.Customer;
import com.lj3.util.JDBCUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

/**测试使用PreparedStatement操作Blob类型的数据
 * @author luojie
 * @Description
 * @date 2021/10/6 22/45
 */
public class BlobTest {
    //插入Blob类型的数据
    @Test
    public void testInsert() throws Exception {
        Connection conn = JDBCUtils.getConnection();

        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setObject(1,"罗洁");
        ps.setObject(2,"lj@qq.com");
        ps.setObject(3,"2000-10-04");
        FileInputStream fi = new FileInputStream("girl1.jpg");
        ps.setBlob(4,fi);

        ps.execute();

        JDBCUtils.closeResource(conn,ps);

    }
    //查询Blon字段
    @Test
    public void testSelect() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            conn = JDBCUtils.getConnection();

            String sql = "select * from customers where id=?";

            ps = conn.prepareStatement(sql);

            ps.setObject(1,23);

            rs = ps.executeQuery();

            if(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer cust = new Customer(id, name, email, birth);

                System.out.println(cust);
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("luojie.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while((len=is.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is!=null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos!=null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn,ps,rs);
        }


    }
}
