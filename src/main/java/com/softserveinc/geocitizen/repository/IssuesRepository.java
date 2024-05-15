/*
 * The following code have been created by Yaroslav Zhyravov (shrralis).
 * The code can be used in non-commercial way for everyone.
 * But for any commercial way it needs a author's agreement.
 * Please contact the author for that:
 *  - https://t.me/Shrralis
 *  - https://twitter.com/Shrralis
 *  - shrralis@gmail.com
 *
 * Copyright (c) 2017 by shrralis (Yaroslav Zhyravov).
 */

package com.softserveinc.geocitizen.repository;

import com.softserveinc.geocitizen.entity.Issue;
import com.softserveinc.geocitizen.entity.MapMarker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IssuesRepository extends JpaRepository<Issue, Integer> {

	Optional<Issue> findById(int id);

	List<Issue> findByMapMarker_IdAndHiddenFalse(int mapMarkerId);

	@Query(value = "SELECT type_id FROM issues WHERE map_marker_id = ?1 AND hidden = false", nativeQuery = true)
	int[] getIssueTypeByIdAndHiddenFalse(int id);

	Page<Issue> findByTitleContainingOrTextContainingOrAuthor_LoginContainingAllIgnoreCase(String title, String text, String author, Pageable pageable);

	Page<Issue> findByAuthor_Id(Integer id, Pageable pageable);

	Integer deleteById(Integer id);

	@Modifying
	@Query("UPDATE Issue i SET i.closed = ?1, i.updatedAt = ?2 WHERE i.id = ?3")
	Integer setClosedStatus(boolean closed, LocalDateTime updatedAt, int id);

	@Modifying
	@Query("UPDATE Issue i SET i.hidden = ?1, i.updatedAt = ?2 WHERE i.id = ?3")
	Integer setHiddenStatus(boolean hidden, LocalDateTime updatedAt, int id);

	@Query("SELECT i FROM Issue i WHERE i.closed = TRUE AND i.type = 1")
	Page<Issue> findByClosedTrue(Pageable pageable);

	@Query("SELECT i FROM Issue i WHERE i.closed = FALSE AND i.type = 1")
	Page<Issue> findByClosedFalse(Pageable pageable);

	Page<Issue> findAll(Pageable pageable);

	Integer countAllByMapMarker(MapMarker mapMarker);

	Integer countAllByMapMarkerAndHiddenFalse(MapMarker mapMarker);

	Page<Issue> findByHiddenTrue(Pageable pageable);

	Page<Issue> findByHiddenFalse(Pageable pageable);

	Page<Issue> findByType(Issue.Type type, Pageable pageable);

	@Query("SELECT i FROM Issue i WHERE i.closed = ?1 AND i.hidden = ?2 AND i.type.name = ?3")
	Page<Issue> findByClosedAndHiddenAndType(boolean closed, boolean hidden, String type, Pageable pageable);
}
