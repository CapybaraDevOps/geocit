package com.softserveinc.geocitizen.repository;

import com.softserveinc.geocitizen.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	boolean existsByIssueIdAndUserId(long issueId, long userId);

	long deleteByIssueIdAndUserId(long issueId, long userId);

	Notification findByIssueIdAndUserId(long issueId, long userId);
}
