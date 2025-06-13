package com.mycompany.bookstore.api;

import com.mycompany.bookstore.api.resources.BookResource;
import com.mycompany.bookstore.api.resources.AuthorResource;
import com.mycompany.bookstore.api.resources.CustomerResource;
import com.mycompany.bookstore.api.resources.CartResource;
import com.mycompany.bookstore.api.resources.OrderResource;
import com.mycompany.bookstore.api.exceptions.GlobalExceptionMapper;
import org.glassfish.jersey.server.ResourceConfig;

public class JakartaRestConfiguration extends ResourceConfig {
    public JakartaRestConfiguration() {
        register(BookResource.class);
        register(AuthorResource.class);
        register(CustomerResource.class);
        register(CartResource.class);
        register(OrderResource.class);
    }
}