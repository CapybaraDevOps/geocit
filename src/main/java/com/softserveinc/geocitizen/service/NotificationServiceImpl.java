package com.softserveinc.geocitizen.service;

import com.softserveinc.geocitizen.entity.Notification;
import com.softserveinc.geocitizen.repository.NotificationRepository;
import com.softserveinc.geocitizen.service.interfaces.INotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements INotificationService {

	@Resource
	NotificationRepository notificationRepository;

	@Override
	public Notification addNotification(Notification notification) {
		return notificationRepository.save(notification);
	}

	@Override
	public Long removeNotification(Notification notification) {
		return notificationRepository.deleteByIssueIdAndUserId(notification.getIssueId(), notification.getUserId());
	}

	@Override
	public List<Notification> getAllNotifications() {
		return notificationRepository.findAll();
	}

	@Override
	public Notification setWaiting(Notification notification) {
		Notification getedNot = notificationRepository.findByIssueIdAndUserId(notification.getIssueId(),
				notification.getUserId());
		getedNot.setWaiting(notification.getWaiting());
		return notificationRepository.save(getedNot);
	}
}
