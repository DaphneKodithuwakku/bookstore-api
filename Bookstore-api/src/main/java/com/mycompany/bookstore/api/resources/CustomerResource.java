/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstore.api.resources;

import com.mycompany.bookstore.api.models.Customer;
import com.mycompany.bookstore.api.exceptions.CustomerNotFoundException;
import com.mycompany.bookstore.api.exceptions.InvalidInputException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    public static Map<Integer, Customer> customers = new HashMap<>(); // Made public for access by CartResource and OrderResource
    private static int nextId = 1;

    static {
        Customer put = customers.put(1, new Customer(1, "John Doe", "john@example.com", "pass123"));
    }

    @POST
    public Response createCustomer(Customer customer) {
        if (customer.getName() == null || customer.getEmail() == null || customer.getPassword() == null) {
            throw new InvalidInputException("Invalid customer data");
        }
        customer.setId(nextId++);
        customers.put(customer.getId(), customer);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    @GET
    @Path("/{id}")
    public Customer getCustomerById(@PathParam("id") int id) {
        Customer customer = customers.get(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist");
        }
        return customer;
    }

    @PUT
    @Path("/{id}")
    public Customer updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {
        Customer customer = customers.get(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist");
        }
        if (updatedCustomer.getName() != null) customer.setName(updatedCustomer.getName());
        if (updatedCustomer.getEmail() != null) customer.setEmail(updatedCustomer.getEmail());
        if (updatedCustomer.getPassword() != null) customer.setPassword(updatedCustomer.getPassword());
        return customer;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        Customer customer = customers.remove(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist");
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}