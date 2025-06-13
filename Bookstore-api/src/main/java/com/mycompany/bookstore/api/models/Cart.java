/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstore.api.models;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private int customerId;
    private Map<Integer, Integer> items; // bookId -> quantity

    public Cart() {
        this.items = new HashMap<>();
    }

    public Cart(int customerId) {
        this.customerId = customerId;
        this.items = new HashMap<>();
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public Map<Integer, Integer> getItems() { return items; }
    public void setItems(Map<Integer, Integer> items) { this.items = items; }
}
