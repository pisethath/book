package com.aps.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aps.book.model.Book;
import com.aps.book.repository.BookRepository;

@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	public List<Book> findAllBook(){
		return bookRepository.findAllBook();
    }
	
	public List<Book> findBookByStudentId(String studentId){
        return bookRepository.findBookByStudentId(studentId);
	}
	
	public Book findBookById(Integer studentId){
        return bookRepository.findBookById(studentId);
	}
	
	public Book save(Book book){
        return bookRepository.save(book);
	}
	
	public Book update(Book book){		
		Book updateBook = bookRepository.findBookById(book.getId());
		if(book.getAuthor() != null) updateBook.setAuthor(book.getAuthor());
		if(book.getTitle() != null) updateBook.setTitle(book.getTitle());
		if(book.getCategory() != null) updateBook.setCategory(book.getCategory());
		if(book.getDescription() != null) updateBook.setDescription(book.getDescription());
		if(book.getStudentId() != null) updateBook.setStudentId(book.getStudentId());
		if(book.getBorrowDate() != null) updateBook.setBorrowDate(book.getBorrowDate());
		if(book.getBookStatus() != null) updateBook.setBookStatus(book.getBookStatus());
        return bookRepository.save(updateBook);
	}

}
