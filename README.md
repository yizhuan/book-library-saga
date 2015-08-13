# book-library-saga

This is a simple library management program using Domain Driven Design.
 
Each reader can borrow up to 3 books and one book can only be borrowed by one reader a time.

There are 2 aggregates in the domain model: Reader and Book. When a reader borrows a book, it needs to update to both Reader and Book.
When BorrowCommand is received, Reader raises an BorrowEvent. A Saga is used to send a LendCommand to Book to update the book. 
If the book is taken, a RejectCommand is sent to Reader to rollback the Reader updates.
 
http://cqrs.nu/Faq/aggregates suggests if I need to update two aggregates in one transaction,  the aggregate boundaries are not well-defined.
What is the solution for this library management scenario? 
(https://www.youtube.com/watch?v=oFPbEi2463c&list=PLzsUBUx6tYGDmQEoi8i86eUar7moANEDS)

This application uses:

- Domain Driven Design
- CQRS
- Event Sourcing
- Spring Boot
- Axon Framework
- RESTful service

# Build

$ mvn package

# Start service

net start MongoDB

java -jar target/book-library-saga-0.1.0.jar

# Tests

## Register books
curl -X POST -d '{"title":"Load of the Rings", "author": "J. R. R. Tolkien"}' -H "Content-Type:application/json" http://localhost:8080/api/books
curl -X POST -d '{"title":"The Importance of Living", "author": "Lin Yutang"}' -H "Content-Type:application/json" http://localhost:8080/api/books
curl -X POST -d '{"title":"War and Peace", "author": "Leo Tolstoy"}' -H "Content-Type:application/json" http://localhost:8080/api/books

## Register readers
curl -X POST -d '{"name":"John Smith"}' -H "Content-Type:application/json" http://localhost:8080/api/readers
curl -X POST -d '{"name":"Tom Hanks"}' -H "Content-Type:application/json" http://localhost:8080/api/readers


## List books
curl http://localhost:8080/api/books

## List readers
curl http://localhost:8080/api/readers


## Borrow a book
curl -X POST -d '{"bookId":"3f7bf042-489a-4d5a-b0cb-ba62bf71ae32"}' -H "Content-Type:application/json" http://localhost:8080/api/readers/a5704803-ec72-4748-9453-8b7ac40674cf/borrow

curl -X POST -d '{"bookId":"dd6985cf-50b6-44b4-9cea-03c1770cb049"}' -H "Content-Type:application/json" http://localhost:8080/api/readers/a5704803-ec72-4748-9453-8b7ac40674cf/borrow



# Check MongoDB


&gt; mongo

&gt; show dbs
&gt; use axonframework
switched to db axonframework

&gt; show collections

domainevents
system.indexes

&gt; db.domainevents.find()


# TODO

This version is just a rough sketch. Still lots to do.


# Reference

http://cqrs.nu/Faq/aggregates

