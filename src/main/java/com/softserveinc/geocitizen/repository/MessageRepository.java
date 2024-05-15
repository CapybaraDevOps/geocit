package com.softserveinc.geocitizen.repository;

import com.softserveinc.geocitizen.entity.FullMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<FullMessage, Long> {

	boolean existsByIssueIdAndUserId(long issueId, long userId);

	List<FullMessage> findAllByIssueIdAndUserId(long issueId, long userId);

	@Query(value = "SELECT * FROM (SELECT DISTINCT ON (issueid, userid) * FROM message WHERE authorid = ?1) AS chatrooms ORDER BY date DESC", nativeQuery = true)
	List<FullMessage> findAllChatRooms(long adminId);

	boolean existsByIssueIdAndUserIdAndAuthorId(long issueId, long userId, long authorId);
}
