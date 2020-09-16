package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.demo.entity.Reader;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReaderRepository;

@RestController
@RequestMapping("/readers")
public class ReaderController {
	@Autowired
	private ReaderRepository readerRepo;
	
	@Autowired
	private BookRepository bookRepo;
	@GetMapping("/")
	public List<Reader> getReaders(){
		return (List<Reader>) readerRepo.findAll();
	}
	@GetMapping("/{readerId}")
	public Optional<Reader> getReader(@PathVariable Long readerId) {
		Optional<Reader> theReader = readerRepo.findById(readerId);
		
		if(theReader.isEmpty())
			throw new BookNotFoundException("Reader id not found - " + readerId);
		return theReader;
	}

	@PostMapping("/")
	public ResponseEntity<Reader> addReader(@RequestBody Reader reader) {
		readerRepo.save(reader);
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	@PutMapping("/{readerId}")
	public ResponseEntity<Void> addReader(@PathVariable Long readerId,@RequestBody Reader reader) {
		Optional<Reader> oldReader=readerRepo.findById(readerId);
		if(oldReader.isEmpty())
			throw new BookNotFoundException("Reader id not found - " + readerId);
		Reader newReader=oldReader.get();
		newReader.setFirstName(reader.getFirstName());
		newReader.setLastName(reader.getLastName());
		readerRepo.save(newReader);
		 return new ResponseEntity<>(HttpStatus.OK);
		
	}
	@DeleteMapping("/{readerId}")
	public ResponseEntity<Void> deleteBook(@PathVariable Long readerId){
		readerRepo.deleteById(readerId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/{readerId}/buyBook/{bookId}")
	public ResponseEntity<Void> buyBook(@PathVariable Long bookId, @PathVariable Long readerId) {
		
		Optional<Reader> reader = readerRepo.findById(readerId);
		if(reader.isEmpty())
			throw new BookNotFoundException("Reader id not found - " + readerId);
		
		Optional<Book> oldBook=bookRepo.findById(bookId);
		if(oldBook.isEmpty())
			throw new BookNotFoundException("Book id not found - " + bookId);
		
		Reader newReader=reader.get();
		Book book = oldBook.get();
		
		newReader.getBooks().add(book);
		book.setNumber(book.getNumber()-1);
		
		readerRepo.save(newReader);
		bookRepo.save(book);
		
		return new ResponseEntity<>(HttpStatus.OK);	
	}
}
