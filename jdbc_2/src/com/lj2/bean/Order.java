package com.lj2.bean;

import java.sql.Date;

/**
 * @author luojie
 * @Description
 * @date 2021/10/3 11/32
 */
public class Order {
    private int id;
    private String name;
    private Date date;

    public Order(int id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Order() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
