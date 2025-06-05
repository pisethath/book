package com.aps.book.model;

import java.sql.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Integer id;
	
	@Column(name = "author", nullable = true, length = 100)
	private String author;
	
	@Column(name = "title", unique = true, nullable = false, length = 128)
	private String title;
	
	@Column(name = "category", nullable = true, length = 100)
	private String category;
	
	@Column(name = "description", nullable = true, length = 2000)
	private String description;
	
	@Column(name="student_id", nullable = true, length = 100)
	private String studentId;
	
	@Column(name = "borrow_date", columnDefinition = "datetime")
    private Date borrowDate;
	
	@Column(name = "book_status", nullable = true, length = 20)
    private String bookStatus;
}
