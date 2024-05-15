package com.softserveinc.geocitizen.repository;

import com.softserveinc.geocitizen.entity.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<UserConnection, String> {

	UserConnection getByUserIdAndProvider(String userId, String provider);
}
