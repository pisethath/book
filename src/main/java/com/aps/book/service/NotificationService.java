package com.aps.book.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aps.book.model.Notifications;
import com.aps.book.repository.NotificationRepository;

@Service
public class NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	public List<Notifications> findAllNotifications(){
		return notificationRepository.findAllNotifications();
    }
	
	public List<Notifications> findBorrowNotificationToStudent(String studentId, Date notificationDate){
		return notificationRepository.findBorrowNotificationToStudent(studentId, notificationDate);
    }
	
	public List<Notifications> findNewNotificationToStudent(String category, Date notificationDate){
		return notificationRepository.findNewNotificationToStudent(category, notificationDate);
    }
	
	public Boolean addNotifications(Notifications notifications){
		try {
			notificationRepository.save(notifications);
			return true;
		}catch(Exception e) {
			return false;
		} 
	}

}
