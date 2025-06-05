package com.aps.book.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aps.book.model.Book;
import com.aps.book.model.ErrorResponse;
import com.aps.book.model.Notifications;
import com.aps.book.service.BookService;
import com.aps.book.service.NotificationService;
import com.aps.book.uti.JwtUti;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@CrossOrigin("*")
@RestController
@RequestMapping("/book")
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private JwtUti jwtUti;
	
	@GetMapping("")
	public ResponseEntity<?> findAll(@RequestParam Map<String, String> reqParam) {
		try {
			List<Book> book = bookService.findAllBook();
			if(book.size() > 0){
				return new ResponseEntity<>(book, HttpStatus.OK);
			}else{
	        	ErrorResponse error = errorNotFound();
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	        }
		}catch(Exception e) {			
			ErrorResponse error = errorBadRequest();
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/student")
	public ResponseEntity<?> findBook(@RequestParam Map<String, String> reqParam) {
		try {
			String studentId;
			if(reqParam.get("studentid") != null) studentId = reqParam.get("studentid"); else studentId = getStudentId();
			
			List<Book> book = bookService.findBookByStudentId(studentId);
			if(book.size() > 0){
				return new ResponseEntity<>(book, HttpStatus.OK);
			}else{
				List<Book> nullBook = new ArrayList<Book>();
				return new ResponseEntity<>(nullBook, HttpStatus.OK);
	        }
		}catch(Exception e) {
			ErrorResponse error = errorBadRequest();
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/info")
	public ResponseEntity<?> findBookById(@RequestParam Map<String, String> reqParam) {
		try {
			Integer id = Integer.parseInt(reqParam.get("id"));
			Book book = bookService.findBookById(id);
			if(book != null){
				return new ResponseEntity<>(book, HttpStatus.OK);
			}else{
	        	ErrorResponse error = errorNotFound();
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	        }
		}catch(Exception e) {
			ErrorResponse error = errorBadRequest();
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/new")
	public ResponseEntity<?> newBook(@RequestBody Book book) {
		try {
			book.setBookStatus("Available");
			Book bookData = bookService.save(book);
			
			Notifications notifications = new Notifications();
			notifications.setBook(bookData);
			notifications.setCategory("New Book");
			notifications.setNotificationDate(currentDate());
			notificationService.addNotifications(notifications);
			
			if(bookData != null){
	            return new ResponseEntity<>(bookData, HttpStatus.OK);
	        }else{
	        	ErrorResponse error = errorNotFound();
				return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
	        }
		} catch (Exception e) {
			ErrorResponse error = errorBadRequest();
			return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updateBook(@RequestBody Book book) {
		try {
			Book bookData = bookService.update(book);
			if(bookData != null){
	            return new ResponseEntity<>(bookData, HttpStatus.OK);
	        }else{
	        	ErrorResponse error = errorNotFound();
				return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
	        }
		} catch (Exception e) {
			ErrorResponse error = errorBadRequest();
			return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/borrow")
	public ResponseEntity<?> borrowBook(@RequestBody Book book) {
		try {
			String studentId = getStudentId();
			Date currentDate = currentDate();
			List<Book> brrowBook = bookService.findBookByStudentId(studentId);
			if(brrowBook.size() < 3) {
				Book getBook = bookService.findBookById(book.getId());
				Notifications notifications = new Notifications();
				notifications.setBook(getBook);
				notifications.setCategory("Borrow");
				notifications.setStudentId(studentId);
				notifications.setNotificationDate(currentDate);
				notificationService.addNotifications(notifications);
				
				getBook.setStudentId(studentId);
				getBook.setBorrowDate(currentDate);
				getBook.setBookStatus("Unavailable");
				Book bookData = bookService.update(getBook);
				
				if(bookData != null){
		            return new ResponseEntity<>(bookData, HttpStatus.OK);
		        }else{
		        	ErrorResponse error = errorNotFound();
					return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
		        }
			}else {
				ErrorResponse error = new ErrorResponse();
				error.setCode("200");
				error.setMessage("You cannot borrow more than 3 books");
				return new ResponseEntity<>(error, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			ErrorResponse error = errorBadRequest();
			return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/return")
	public ResponseEntity<?> returnBook(@RequestBody Book book) {
		try {
			Book getBook = bookService.findBookById(book.getId());			
			getBook.setStudentId("");
			getBook.setBookStatus("Available");
			Book bookData = bookService.update(getBook);
			
			if(bookData != null){
	            return new ResponseEntity<>(bookData, HttpStatus.OK);
	        }else{
	        	ErrorResponse error = errorNotFound();
				return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
	        }
		} catch (Exception e) {
			ErrorResponse error = errorBadRequest();
			return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	public Date currentDate() {
		Date sqlDate = new Date(System.currentTimeMillis());
		return sqlDate;  
	}
	
	private String getStudentId() {
		String Auth = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
		Jws<Claims> jws = jwtUti.validateToken(Auth.substring(7));
		return jws.getBody().getSubject();
	}
	
	private ErrorResponse errorNotFound() {
		ErrorResponse error = new ErrorResponse();
		error.setCode("404");
		error.setMessage("Data is not found.");
		
		return error;
	}
	
	private ErrorResponse errorBadRequest() {
		ErrorResponse error = new ErrorResponse();
		error.setCode("400");
		error.setMessage("Your request is not acceptable.");
		
		return error;
	}
}
