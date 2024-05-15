package com.softserveinc.geocitizen.service.interfaces;

import com.softserveinc.geocitizen.entity.Notification;

import java.util.List;

public interface INotificationService {

	Notification addNotification(Notification notification);

	Long removeNotification(Notification notification);

	List<Notification> getAllNotifications();

	Notification setWaiting(Notification notification);
}
