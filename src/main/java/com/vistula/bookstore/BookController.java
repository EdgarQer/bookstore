package com.vistula.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Map<String, Object>> allBooks() {
        return jdbcTemplate.queryForList("SELECT * FROM books");
    }

    @GetMapping("/{id}")
    public Map<String, Object> oneBook(@PathVariable Long id) {
        try {
            return jdbcTemplate.queryForMap("SELECT * FROM books WHERE id = ?", id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Book not found with id: " + id);
        }
    }

    @PostMapping
    public void newBook(@RequestBody Book book) {
        jdbcTemplate.update("INSERT INTO books (title, author_id) VALUES (?, ?)",
                book.getTitle(), book.getAuthorId());
    }

    @PutMapping("/{id}")
    public void updateBook(@PathVariable Long id, @RequestBody Book book) {
        jdbcTemplate.update("UPDATE books SET title = ?, author_id = ? WHERE id = ?",
                book.getTitle(), book.getAuthorId(), id);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        jdbcTemplate.update("DELETE FROM books WHERE id = ?", id);
    }

    @GetMapping("/{id}/with-author")
    public Map<String, Object> bookWithAuthor(@PathVariable Long id) {
        try {
            return jdbcTemplate.queryForMap("""
                SELECT b.id AS book_id, b.title, a.id AS author_id, a.name AS author_name 
                FROM books b 
                JOIN authors a ON b.author_id = a.id 
                WHERE b.id = ?
                """, id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Book with author not found with id: " + id);
        }
    }
}
