package com.aps.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aps.book.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
	@Query(value = "select a from Book a")
	List<Book> findAllBook();
	
	@Query(value = "select a from Book a where studentId = ?1")
	List<Book> findBookByStudentId(String studentId);
	
	@Query(value = "select a from Book a where id = ?1")
	Book findBookById(Integer studentId);
}
