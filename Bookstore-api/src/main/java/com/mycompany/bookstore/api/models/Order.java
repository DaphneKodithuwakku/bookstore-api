/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstore.api.models;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private int id;
    private int customerId;
    private Map<Integer, Integer> items; // bookId -> quantity
    private double totalPrice;

    public Order() {
        this.items = new HashMap<>();
    }

    public Order(int id, int customerId, Map<Integer, Integer> items, double totalPrice) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public Map<Integer, Integer> getItems() { return items; }
    public void setItems(Map<Integer, Integer> items) { this.items = items; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}