package com.lj3.DAO.junit;

import com.lj4.util.JDBCUtils;
import com.lj3.DAO.CustomerDAOImpl;
import com.lj2.bean.Customer;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author luojie
 * @Description
 * @date 2021/10/10 10/43
 */
class CustomerDAOImplTest {

    private CustomerDAOImpl dao = new CustomerDAOImpl();
    @Test
    public void testinsert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer cust = new Customer(1,"罗洁","13@126.com",new Date(37843654));
            dao.insert(conn,cust);
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }

    }

    @Test
    void deleteById() throws Exception {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            dao.deleteById(conn,23);
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }


    }

    @Test
    void update() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            Customer cust = new Customer(21,"贝多芬","beiduof@126.com",new Date(233441343L));
            dao.update(conn,cust);

            System.out.println("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @Test
    void getCustomerById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection2();

            Customer customer = dao.getCustomerById(conn, 25);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @Test
    void getAll() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            List<Customer> all = dao.getAll(conn);
            all.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @Test
    void getCount() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            Long count = dao.getCount(conn);

            System.out.println("个数:" + count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @Test
    void getMaxBirth() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            Date maxBirth = dao.getMaxBirth(conn);

            System.out.println(maxBirth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }
}