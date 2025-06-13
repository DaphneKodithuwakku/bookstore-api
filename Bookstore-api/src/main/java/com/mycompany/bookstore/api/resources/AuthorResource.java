/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstore.api.resources;

import com.mycompany.bookstore.api.models.Author;
import com.mycompany.bookstore.api.models.Book;
import com.mycompany.bookstore.api.exceptions.AuthorNotFoundException;
import com.mycompany.bookstore.api.exceptions.InvalidInputException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    static final Map<Integer, Author> authors = new HashMap<>();
    private static final Map<Integer, Book> books = new HashMap<>();
    private static int nextId = 1;

    static {
        authors.put(1, new Author(1, "J.R.R. Tolkien", "Fantasy author"));
        Book put = books.put(1, new Book(1, "The Lord of the Rings", 1, "978-0-618-05326-7", 1954, 20.99, 100));
    }

    @POST
    public Response createAuthor(Author author) {
        if (author.getName() == null || author.getBiography() == null) {
            throw new InvalidInputException("Invalid author data");
        }
        author.setId(nextId++);
        authors.put(author.getId(), author);
        return Response.status(Response.Status.CREATED).entity(author).build();
    }

    @GET
    public List<Author> getAllAuthors() {
        return new ArrayList<>(authors.values());
    }

    @GET
    @Path("/{id}")
    public Author getAuthorById(@PathParam("id") int id) {
        Author author = authors.get(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }
        return author;
    }

    @PUT
    @Path("/{id}")
    public Author updateAuthor(@PathParam("id") int id, Author updatedAuthor) {
        Author author = authors.get(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }
        if (updatedAuthor.getName() != null) author.setName(updatedAuthor.getName());
        if (updatedAuthor.getBiography() != null) author.setBiography(updatedAuthor.getBiography());
        return author;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        Author author = authors.remove(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/{id}/books")
    public List<Book> getBooksByAuthor(@PathParam("id") int id) {
        if (!authors.containsKey(id)) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }
        return books.values().stream()
                .filter(book -> book.getAuthorId() == id)
                .collect(Collectors.toList());
    }
}