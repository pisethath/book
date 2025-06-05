package com.aps.book.controller;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aps.book.model.ErrorResponse;
import com.aps.book.model.Notifications;
import com.aps.book.service.NotificationService;
import com.aps.book.uti.JwtUti;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@RestController
@RequestMapping("/notification")
public class NotificationController {
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private JwtUti jwtUti;
	
	@GetMapping("/borrow-book")
	public ResponseEntity<?> findBorrowNotificationToStudent(@RequestParam Map<String, String> reqParam) {
		try {
			String studentId = getStudentId();
			Date currentDate = currentDate();
			List<Notifications> notification = notificationService.findBorrowNotificationToStudent(studentId, currentDate);
			if(notification.size() > 0){
				return new ResponseEntity<>(notification, HttpStatus.OK);
			}else{
	        	ErrorResponse error = errorNotFound();
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	        }
		}catch(Exception e) {			
			ErrorResponse error = errorBadRequest();
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/new-book")
	public ResponseEntity<?> findNewNotificationToStudent(@RequestParam Map<String, String> reqParam) {
		try {
			Date currentDate = currentDate();
			List<Notifications> notification = notificationService.findNewNotificationToStudent("New Book", currentDate);
			if(notification.size() > 0){
				return new ResponseEntity<>(notification, HttpStatus.OK);
			}else{
	        	ErrorResponse error = errorNotFound();
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	        }
		}catch(Exception e) {			
			ErrorResponse error = errorBadRequest();
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
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
