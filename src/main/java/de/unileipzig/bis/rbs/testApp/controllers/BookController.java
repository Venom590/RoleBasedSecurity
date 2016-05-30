package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.RoleObjectConsistencyException;
import de.unileipzig.bis.rbs.testApp.model.Author;
import de.unileipzig.bis.rbs.testApp.model.Book;
import de.unileipzig.bis.rbs.testApp.model.Role;
import de.unileipzig.bis.rbs.testApp.model.User;
import de.unileipzig.bis.rbs.testApp.service.AuthorRepository;
import de.unileipzig.bis.rbs.testApp.service.BookRepository;
import de.unileipzig.bis.rbs.testApp.service.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * The book controller to manage books in this application.
 *
 * @author Lukas Werner
 */
@Controller
@RequestMapping("/manage/book")
public class BookController extends AbstractController {

    /**
     * The book repository to persist changes
     */
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    /**
     * Get all books
     *
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String books(Model model) {
        User user = this.getCurrentUser();
        Iterable<Book> allBooks = bookRepository.findAll();
        List<Book> books = new ArrayList<>();
        for (Book book : allBooks) {
            if (user.canRead(book)) {
                books.add(book);
            }
        }
        model.addAttribute("books", books);
        return "book/all-books";
    };

    /**
     * Get book by id
     *
     * @param bookid the id
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/{bookid}", method = RequestMethod.GET)
    public String book(@PathVariable String bookid, Model model) {
        Book book = bookRepository.findOne(Long.valueOf(bookid));
        if (canRead(book)) {
            model.addAttribute("book", book);
            return "book/book";
        } else {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You do not have read rights for object with id: " + bookid));
            return "redirect:/manage/book";
        }
    }

    /**
     * Create new book (show mask)
     *
     * @return the book creation mask
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        if (isAdmin()) {
            Iterable<Author> allAuthors = authorRepository.findAll();
            model.addAttribute("authors", allAuthors);
            Iterable<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);
            return "book/create";
        } else {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You do not have create rights for objects here"));
            return "redirect:/manage/book";
        }
    }

    /**
     * Create new book (action)
     *
     * @param isbn the bookname
     * @param title the password
     * @param authorId the name
     * @return the view (redirect)
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String doCreate(@RequestParam(value = "isbn") String isbn,
                           @RequestParam(value = "title") String title,
                           @RequestParam(value = "author_id") Long authorId,
                           @RequestParam(value = "can_read[]", required = false) Long[] canReadRoleIds,
                           @RequestParam(value = "can_write[]", required = false) Long[] canWriteRoleIds,
                           @RequestParam(value = "can_delete[]", required = false) Long[] canDeleteRoleIds) {
        if (isAdmin()) {
            Author author = authorRepository.findOne(Long.valueOf(authorId));
            Book book = new Book(isbn, title, author);
            try {
                this.setRoleObjectsToObject(book, canReadRoleIds, canWriteRoleIds, canDeleteRoleIds);

                bookRepository.save(book);
            } catch (RoleObjectConsistencyException e) {
                setHintMessage(new HintMessage(HintMessage.HintStatus.danger, e.getMessage()));
            }
        } else {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You do not have create rights for objects here"));
        }
        return "redirect:/manage/book";
    }

    /**
     * Edit existing book (show mask)
     *
     * @param bookid the book id
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/edit/{bookid}", method = RequestMethod.GET)
    public String edit(@PathVariable String bookid, Model model) {
        Book book = bookRepository.findOne(Long.valueOf(bookid));
        if (canWrite(book)) {
            model.addAttribute("book", book);
            Iterable<Author> allAuthors = authorRepository.findAll();
            model.addAttribute("authors", allAuthors);
            Iterable<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);
            return "book/edit";
        } else {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You do not have write rights for object with id: " + bookid));
            return "redirect:/manage/book";
        }
    }

    /**
     * Edit existing book (action)
     *
     * @param bookid the book id
     * @param isbn the new bookname
     * @param title the new password
     * @param authorId the new name
     * @return the view (redirect)
     */
    @RequestMapping(value = "/edit/{bookid}", method = RequestMethod.POST)
    public String doEdit(@PathVariable String bookid,
                         @RequestParam(value = "isbn") String isbn,
                         @RequestParam(value = "title") String title,
                         @RequestParam(value = "author_id") Long authorId,
                         @RequestParam(value = "can_read[]", required = false) Long[] canReadRoleIds,
                         @RequestParam(value = "can_write[]", required = false) Long[] canWriteRoleIds,
                         @RequestParam(value = "can_delete[]", required = false) Long[] canDeleteRoleIds) {
        Book book = bookRepository.findOne(Long.valueOf(bookid));
        if (canWrite(book)) {
            Author author = authorRepository.findOne(Long.valueOf(authorId));
            book.setIsbn(isbn);
            book.setTitle(title);
            book.setAuthor(author);
            try {
                this.setRoleObjectsToObject(book, canReadRoleIds, canWriteRoleIds, canDeleteRoleIds);

                bookRepository.save(book);
            } catch (RoleObjectConsistencyException e) {
                setHintMessage(new HintMessage(HintMessage.HintStatus.danger, e.getMessage()));
            }
        } else {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You do not have write rights for object with id: " + bookid));
        }
        return "redirect:/manage/book/" + bookid;
    }

    /**
     * Delete existing book
     *
     * @param bookid the book id
     * @return the view (redirect)
     */
    @RequestMapping(value = "/delete/{bookid}", method = RequestMethod.GET)
    public String delete(@PathVariable String bookid) {
        if (canDelete(Long.valueOf(bookid))) {
            bookRepository.delete(Long.valueOf(bookid));
        } else {
            this.setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You do not have delete rights for object with id: " + bookid));
        }
        return "redirect:/manage/book";
    }

}