package com.aps.book.model;


import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notifications {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Integer id;
	
	@Column(name="category", nullable = false, length = 10)
	private String category;
	
	@Column(name="student_id", nullable = true, length = 100)
	private String studentId;
	
	@JsonProperty(access = Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", columnDefinition = "int", referencedColumnName = "id")
    private Book book;
	
	@Column(name = "notification_date", columnDefinition = "datetime")
    private Date notificationDate;

}
