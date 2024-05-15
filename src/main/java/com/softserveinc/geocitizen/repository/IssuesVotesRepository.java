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

import com.softserveinc.geocitizen.entity.IssueVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssuesVotesRepository extends JpaRepository<IssueVote, IssueVote.Id> {

	Optional<IssueVote> findByVoter_IdAndIssue_Id(int voterId, int issueId);

	void deleteAllByVoter_IdAndIssue_Id(int voterId, int issueId);

	long countByVoteAndIssue_Id(boolean vote, int issueId);

	@Modifying
	@Query(value = "INSERT INTO issues_votes(issue_id, voter_id, vote) VALUES(?1, ?2, ?3)", nativeQuery = true)
	void insertLikeOrDislike(int issueId, int voterId, boolean vote);
}
