package com.vistula.bookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class BookstoreApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(BookstoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... args) {
		log.info("Creating tables");

		jdbcTemplate.execute("DROP TABLE books IF EXISTS");
		jdbcTemplate.execute("DROP TABLE authors IF EXISTS");

		jdbcTemplate.execute("""
                CREATE TABLE authors (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL
                )
                """);

		jdbcTemplate.execute("""
                CREATE TABLE books (
                    id SERIAL PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    author_id INTEGER NOT NULL,
                    FOREIGN KEY (author_id) REFERENCES authors(id)
                )
                """);

		log.info("Inserting data");

		jdbcTemplate.update("INSERT INTO authors(name) VALUES (?)", "Adam Mickiewicz");
		jdbcTemplate.update("INSERT INTO authors(name) VALUES (?)", "Bob Wand");

		jdbcTemplate.update("INSERT INTO books(title, author_id) VALUES (?, ?)", "Dziady", 1);
		jdbcTemplate.update("INSERT INTO books(title, author_id) VALUES (?, ?)", "Pan Tadeusz", 1);
		jdbcTemplate.update("INSERT INTO books(title, author_id) VALUES (?, ?)", "Book of Life", 2);
	}
}
