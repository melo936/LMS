package ge.edu.ibsu.lms.services;

import ge.edu.ibsu.lms.dto.Paging;
import ge.edu.ibsu.lms.entities.Book;
import ge.edu.ibsu.lms.entities.User;
import ge.edu.ibsu.lms.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public BookService(BookRepository bookRepository, UserService userService, AuthenticationService authenticationService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book getById(String id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Boolean delete(String id) {
        try {
            getById(id);
        } catch (Exception e) {
            return true;
        }
        bookRepository.deleteById(id);
        return true;
    }

    public Page<Book> getByTitle(String title, Paging paging) {
        Pageable pageable = PageRequest.of(paging.page() - 1, paging.size(), Sort.by("title").ascending());
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Page<Book> getByAuthor(String author, Paging paging) {
        Pageable pageable = PageRequest.of(paging.page() - 1, paging.size(), Sort.by("author").ascending());
        return bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
    }

    public Page<Book> getByIsbn(String isbn, Paging paging) {
        Pageable pageable = PageRequest.of(paging.page() - 1, paging.size(), Sort.by("isbn").ascending());
        return bookRepository.findByIsbnContainingIgnoreCase(isbn, pageable);
    }

    public boolean isBorrowed(String id) {
        return bookRepository.isBorrowed(id) != null;
    }

    public Book borrowBook(String bookId) {
        try {
            Book book = getById(bookId);
            if (book.borrowerId != null && !book.borrowerId.isEmpty()) {
                throw new RuntimeException("Book is already borrowed by " + book.borrowerId);
            }

            User requester = authenticationService.getCurrentUser();

            book.borrowerId = requester.getId();
            save(book);
            requester.getBorrowedBooks().add(book);
            return book;
        } catch (OptimisticLockingFailureException e) {
            throw new RuntimeException("Book is already borrowed, " + e.getMessage());
        }
    }

    public Book returnBook(String bookId) {
        Book book = getById(bookId);

        if (book.borrowerId != null && !book.borrowerId.isEmpty()) {

            final User requester = authenticationService.getCurrentUser();
            if (!requester.getId().equals(book.borrowerId)) {
                throw new RuntimeException("You are not the borrower of this book");
            }

            User user = userService.getById(book.borrowerId);
            book.borrowerId = null;
            save(book);

            user.getBorrowedBooks().remove(book);
            return book;
        } else {
            throw new RuntimeException("Book is not borrowed");
        }
    }

}
