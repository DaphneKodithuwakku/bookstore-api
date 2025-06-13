/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstore.api.resources;

import com.mycompany.bookstore.api.models.Book;
import com.mycompany.bookstore.api.exceptions.BookNotFoundException;
import com.mycompany.bookstore.api.exceptions.AuthorNotFoundException;
import com.mycompany.bookstore.api.exceptions.InvalidInputException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    static final Map<Integer, Book> books = new HashMap<>();
    private static int nextId = 1;

    static {
        books.put(1, new Book(1, "The Lord of the Rings", 1, "978-0-618-05326-7", 1954, 20.99, 100));
    }

    @POST
    public Response createBook(Book book) {
        if (book.getTitle() == null || book.getIsbn() == null || book.getPrice() <= 0 || book.getStock() < 0) {
            throw new InvalidInputException("Invalid book data");
        }
        if (!com.mycompany.bookstore.api.resources.AuthorResource.authors.containsKey(book.getAuthorId())) {
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist");
        }
        if (book.getPublicationYear() > LocalDate.now().getYear()) {
            throw new InvalidInputException("Publication year cannot be in the future");
        }
        book.setId(nextId++);
        books.put(book.getId(), book);
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @GET
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    @GET
    @Path("/{id}")
    public Book getBookById(@PathParam("id") int id) {
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist");
        }
        return book;
    }

    @PUT
    @Path("/{id}")
    public Book updateBook(@PathParam("id") int id, Book updatedBook) {
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist");
        }
        if (updatedBook.getTitle() != null) book.setTitle(updatedBook.getTitle());
        if (updatedBook.getAuthorId() != 0 && com.mycompany.bookstore.api.resources.AuthorResource.authors.containsKey(updatedBook.getAuthorId())) {
            book.setAuthorId(updatedBook.getAuthorId());
        } else if (updatedBook.getAuthorId() != 0) {
            throw new AuthorNotFoundException("Author with ID " + updatedBook.getAuthorId() + " does not exist");
        }
        if (updatedBook.getIsbn() != null) book.setIsbn(updatedBook.getIsbn());
        if (updatedBook.getPublicationYear() != 0 && updatedBook.getPublicationYear() <= LocalDate.now().getYear()) {
            book.setPublicationYear(updatedBook.getPublicationYear());
        } else if (updatedBook.getPublicationYear() != 0) {
            throw new InvalidInputException("Publication year cannot be in the future");
        }
        if (updatedBook.getPrice() > 0) book.setPrice(updatedBook.getPrice());
        if (updatedBook.getStock() >= 0) book.setStock(updatedBook.getStock());
        return book;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        Book book = books.remove(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist");
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}