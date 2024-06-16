package ge.edu.ibsu.lms.controllers;

import ge.edu.ibsu.lms.dto.AddBook;
import ge.edu.ibsu.lms.dto.BorrowBody;
import ge.edu.ibsu.lms.dto.Paging;
import ge.edu.ibsu.lms.dto.RequestData;
import ge.edu.ibsu.lms.entities.Book;
import ge.edu.ibsu.lms.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(produces = {"application/json"})
    public List<Book> getAll() {
        return bookService.getAll();
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public Book getById(@PathVariable String id) {
        return bookService.getById(id);
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public Book postSave(@RequestBody AddBook addBook) {
        Book book = new Book();
        book.setTitle(addBook.title());
        book.setAuthor(addBook.author());
        book.setIsbn(addBook.isbn());

        return bookService.save(book);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/json"})
    public Book putUpdate(@PathVariable String id, @RequestBody AddBook book) {
        Book existingBook = bookService.getById(id);
        existingBook.setTitle(book.title());
        existingBook.setAuthor(book.author());
        existingBook.setIsbn(book.isbn());

        return bookService.save(existingBook);
    }

    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    public Boolean delete(@PathVariable String id) {
        return bookService.delete(id);
    }

    @GetMapping(value = "/search/title", produces = {"application/json"})
    public Page<Book> getByTitle(@RequestParam("query") String query, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Paging paging = new Paging(page, size);
        return bookService.getByTitle(query, paging);
    }

    @GetMapping(value = "/search/author", produces = {"application/json"})
    public Page<Book> getByAuthor(@RequestParam("query") String query, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Paging paging = new Paging(page, size);
        return bookService.getByAuthor(query, paging);
    }

    @GetMapping(value = "/search/isbn", produces = {"application/json"})
    public Page<Book> getByISBN(@RequestParam("query") String query, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Paging paging = new Paging(page, size);
        return bookService.getByIsbn(query, paging);
    }

    @GetMapping(value = "/status/{id}", produces = {"application/json"})
    public boolean isBorrowed(@PathVariable String id) {
        return bookService.isBorrowed(id);
    }

    @PostMapping(value = "/borrow", produces = {"application/json"})
    public Book borrowBook(@RequestParam("bookId") String bookId) {
        return bookService.borrowBook(bookId);
    }

    @PostMapping(value = "/return", produces = {"application/json"})
    public Book returnBook(@RequestParam("bookId") String bookId) {
        return bookService.returnBook(bookId);
    }
}
