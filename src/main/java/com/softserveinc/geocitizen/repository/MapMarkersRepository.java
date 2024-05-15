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

import com.softserveinc.geocitizen.entity.MapMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MapMarkersRepository extends JpaRepository<MapMarker, Integer> {

	MapMarker findByLatAndLng(double lat, double lng);

	Optional<MapMarker> findById(int id);

	@Modifying
	@Query("UPDATE MapMarker m SET m.hidden = ?1 WHERE m.id = ?2")
	Integer setHiddenStatus(boolean hidden, int id);

	@Query("SELECT m FROM MapMarker m WHERE m.hidden = false")
	List<MapMarker> findByHiddenFalse();
}
