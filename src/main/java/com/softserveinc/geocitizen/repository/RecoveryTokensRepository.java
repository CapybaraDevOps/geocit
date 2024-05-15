package com.softserveinc.geocitizen.repository;

import com.softserveinc.geocitizen.entity.RecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/2/18 at 2:49 PM
 */
@Repository
public interface RecoveryTokensRepository extends JpaRepository<RecoveryToken, Integer> {

	Optional<RecoveryToken> findByToken(String token);

	RecoveryToken getByToken(String token);

	@Query(value = "SELECT COUNT(*) FROM " + RecoveryToken.TABLE_NAME +
			" WHERE " + RecoveryToken.USER_COLUMN_NAME + " = ?1 AND " +
			RecoveryToken.EXPIRES_AT_COLUMN_NAME + " >= NOW() ", nativeQuery = true)
	int countNonExpiredByUser(int userId);
}
