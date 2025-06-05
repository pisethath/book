package com.aps.book.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aps.book.model.Notifications;

public interface NotificationRepository extends JpaRepository<Notifications, Integer> {
	@Query(value = "select a from Notifications a")
	List<Notifications> findAllNotifications();
	
	@Query(value = "select a from Notifications a where studentId=?1 and notificationDate=?2")
	List<Notifications> findBorrowNotificationToStudent(String studentId, Date notificationDate);
	
	@Query(value = "select a from Notifications a where category=?1 and notificationDate=?2")
	List<Notifications> findNewNotificationToStudent(String category, Date notificationDate);
}
