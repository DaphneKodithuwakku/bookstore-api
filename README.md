# Bookstore API

A RESTful API built with Java JAX-RS to manage an online bookstore system. The API supports CRUD operations for books, authors, customers, carts, and orders. Postman was used for endpoint testing.

## Technologies Used
- Java
- JAX-RS (Jersey)
- JSON for data exchange
- Postman (for API testing)

## Features
- Add, view, update, and delete books
- Add and manage authors
- Error handling for invalid inputs and IDs
- Simulated bookstore domain with test cases

## Endpoints Overview

### Book Endpoints
| Method | Endpoint         | Description               |
|--------|------------------|---------------------------|
| POST   | `/books`         | Create a new book         |
| GET    | `/books`         | Retrieve all books        |
| GET    | `/books/{id}`    | Retrieve a book by ID     |
| PUT    | `/books/{id}`    | Update an existing book   |
| DELETE | `/books/{id}`    | Delete a book by ID       |

### Author Endpoints
| Method | Endpoint          | Description              |
|--------|-------------------|--------------------------|
| POST   | `/authors`        | Create a new author      |
| GET    | `/authors`        | Retrieve all authors     |

## Testing
All endpoints were tested using Postman. Each scenario was validated for both success and failure responses, including:
- Valid and invalid inputs
- Missing or incorrect resource IDs
- Required field validations

Sample test case result:
```json
{
  "title": "Just Our Luck",
  "authorId": 1,
  "isbn": "9781542026876",
  "publicationYear": 2025,
  "price": 18.99,
  "stock": 75
}
