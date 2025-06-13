/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstore.api.resources;

import com.mycompany.bookstore.api.models.Order;
import com.mycompany.bookstore.api.models.Cart;
import com.mycompany.bookstore.api.models.Book;
import com.mycompany.bookstore.api.exceptions.CartNotFoundException;
import com.mycompany.bookstore.api.exceptions.CustomerNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static final Map<Integer, Order> orders = new HashMap<>();
    private static int nextId = 1;

    @POST
    public Response createOrder(@PathParam("customerId") int customerId) {
        if (!com.mycompany.bookstore.api.resources.CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        Cart cart = com.mycompany.bookstore.api.resources.CartResource.carts.get(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " is empty or does not exist");
        }
        double totalPrice = 0;
        for (Map.Entry<Integer, Integer> item : cart.getItems().entrySet()) {
            Book book = com.mycompany.bookstore.api.resources.BookResource.books.get(item.getKey());
            if (book != null) {
                totalPrice += book.getPrice() * item.getValue();
            }
        }
        Order order = new Order(nextId++, customerId, new HashMap<>(cart.getItems()), totalPrice);
        orders.put(order.getId(), order);
        cart.getItems().clear();
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public List<Order> getAllOrders(@PathParam("customerId") int customerId) {
        if (!com.mycompany.bookstore.api.resources.CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        return orders.values().stream()
                .filter(order -> order.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{orderId}")
    public Order getOrderById(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId) {
        if (!com.mycompany.bookstore.api.resources.CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        Order order = orders.get(orderId);
        if (order == null || order.getCustomerId() != customerId) {
            throw new NotFoundException("Order with ID " + orderId + " does not exist for customer " + customerId);
        }
        return order;
    }
}