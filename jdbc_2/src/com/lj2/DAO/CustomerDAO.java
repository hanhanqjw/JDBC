package com.lj2.DAO;

import com.lj2.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**此接口针对于customers表的常用操作
 * @author luojie
 * @Description
 * @date 2021/10/10 09/32
 */
public interface CustomerDAO {
    /**
     * 将cust对象添加到数据中
     * @param conn
     * @param cust
     */
     void insert(Connection conn, Customer cust);

    /**
     * 针对指定的id删除表中的一条数据
     * @param conn
     * @param id
     */
     void deleteById(Connection conn,int id);

    /**
     * 针对内存中的cust对象，去修改数据表中指定的记录
     * @param conn
     * @param cust
     */
     void update(Connection conn,Customer cust);

    /**
     * 针对指定的id查询得到对应的Customer
     * @param conn
     * @param id
     */
     Customer getCustomerById(Connection conn,int id);

    /**
     * 查询表中所有记录构成的集合
     * @param conn
     * @return
     */
     List<Customer> getAll(Connection conn);

    /**
     * 返回数据表中的数据的条目数
     * @param conn
     * @return
     */
     Long getCount(Connection conn);

    /**
     * 返回数据表中最大的生日
     * @param conn
     * @return
     */
     Date getMaxBirth(Connection conn);
}
