/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstore.api.resources;

import com.mycompany.bookstore.api.models.Cart;
import com.mycompany.bookstore.api.models.Book;
import com.mycompany.bookstore.api.exceptions.CartNotFoundException;
import com.mycompany.bookstore.api.exceptions.BookNotFoundException;
import com.mycompany.bookstore.api.exceptions.CustomerNotFoundException;
import com.mycompany.bookstore.api.exceptions.OutOfStockException;
import com.mycompany.bookstore.api.exceptions.InvalidInputException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    static final Map<Integer, Cart> carts = new HashMap<>();

    static {
        carts.put(1, new Cart(1));
    }

    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") int customerId, Map<String, Integer> item) {
        if (!com.mycompany.bookstore.api.resources.CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        int bookId = item.getOrDefault("bookId", 0);
        int quantity = item.getOrDefault("quantity", 0);
        if (bookId == 0 || quantity <= 0) {
            throw new InvalidInputException("Invalid item data");
        }
        Book book = com.mycompany.bookstore.api.resources.BookResource.books.get(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist");
        }
        if (book.getStock() < quantity) {
            throw new OutOfStockException("Book with ID " + bookId + " is out of stock");
        }
        Cart cart = carts.computeIfAbsent(customerId, k -> new Cart(customerId));
        cart.getItems().put(bookId, cart.getItems().getOrDefault(bookId, 0) + quantity);
        book.setStock(book.getStock() - quantity);
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Cart getCart(@PathParam("customerId") int customerId) {
        if (!com.mycompany.bookstore.api.resources.CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        Cart cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist");
        }
        return cart;
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateCartItem(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId, Map<String, Integer> item) {
        if (!com.mycompany.bookstore.api.resources.CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        Cart cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist");
        }
        int quantity = item.getOrDefault("quantity", 0);
        if (quantity < 0) {
            throw new InvalidInputException("Quantity cannot be negative");
        }
        Book book = com.mycompany.bookstore.api.resources.BookResource.books.get(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist");
        }
        int currentQuantity = cart.getItems().getOrDefault(bookId, 0);
        int stockAdjustment = quantity - currentQuantity;
        if (stockAdjustment > book.getStock()) {
            throw new OutOfStockException("Book with ID " + bookId + " does not have enough stock");
        }
        if (quantity == 0) {
            cart.getItems().remove(bookId);
        } else {
            cart.getItems().put(bookId, quantity);
        }
        book.setStock(book.getStock() - stockAdjustment);
        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItemFromCart(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId) {
        if (!com.mycompany.bookstore.api.resources.CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        Cart cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist");
        }
        Integer quantity = cart.getItems().remove(bookId);
        if (quantity == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " is not in the cart");
        }
        Book book = com.mycompany.bookstore.api.resources.BookResource.books.get(bookId);
        if (book != null) {
            book.setStock(book.getStock() + quantity);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}