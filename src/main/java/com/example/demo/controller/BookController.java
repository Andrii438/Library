package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.advice.BookNotFoundException;
import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;

@RestController
@RequestMapping("/books")
public class BookController {
	
	@Autowired
	BookRepository bookRepo;
	
	@GetMapping("/")
	public List<Book> getBooks(){
		return (List<Book>) bookRepo.findAll();
	}
	
	@GetMapping("/{bookId}")
	public Optional<Book> getBook(@PathVariable Long bookId) {
		Optional<Book> theBook = bookRepo.findById(bookId);
		
		if(theBook.isEmpty())
			throw new BookNotFoundException("Book id not found - " + bookId);
		return theBook;
		
	}
	
	@PostMapping("/")
	public Book addBook(@RequestBody Book book) {
		return bookRepo.save(book);
		
	}
	@PutMapping("/{bookId}")
	public Book addBook(@PathVariable Long bookId,@RequestBody Book book) {
		Optional<Book> oldBook=bookRepo.findById(bookId);
		if(oldBook.isEmpty())
			throw new BookNotFoundException("Book id not found - " + bookId);
		Book newBook=oldBook.get();
		newBook.setName(book.getName());
		newBook.setNumber(book.getNumber());
		return bookRepo.save(newBook);
		
	}
	@DeleteMapping("/{bookId}")
	public void deleteBook(@PathVariable Long bookId){
		bookRepo.deleteById(bookId);
	}
	
	
}
