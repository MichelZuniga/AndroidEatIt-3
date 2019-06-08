package com.example.ayomide.androideatit.Model;

import java.util.List;

public class Request {
    private String phone, name, address, comment, total, status;
    private List<Order> foods; //list of food order

    public Request() {
    }

    public Request(String phone, String name, String address, String comment, String total, String status, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.comment = comment;
        this.total = total;
        this.status = status;
        this.foods = foods;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
