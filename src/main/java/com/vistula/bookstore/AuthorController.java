package com.vistula.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Map<String, Object>> allAuthors() {
        return jdbcTemplate.queryForList("SELECT * FROM authors");
    }

    @GetMapping("/{id}")
    public Map<String, Object> oneAuthor(@PathVariable Long id) {
        try {
            return jdbcTemplate.queryForMap("SELECT * FROM authors WHERE id = ?", id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Author not found with id: " + id);
        }
    }

    @PostMapping
    public void newAuthor(@RequestBody Author author) {
        jdbcTemplate.update("INSERT INTO authors (name) VALUES (?)", author.getName());
    }

    @PutMapping("/{id}")
    public void updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        jdbcTemplate.update("UPDATE authors SET name = ? WHERE id = ?", author.getName(), id);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        jdbcTemplate.update("DELETE FROM authors WHERE id = ?", id);
    }

    @GetMapping("/{id}/with-books")
    public Map<String, Object> authorWithBooks(@PathVariable Long id) {
        try {
            Map<String, Object> author = jdbcTemplate.queryForMap("SELECT * FROM authors WHERE id = ?", id);
            List<Map<String, Object>> books = jdbcTemplate.queryForList("SELECT * FROM books WHERE author_id = ?", id);
            author.put("books", books);
            return author;
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Author not found with id: " + id);
        }
    }
}