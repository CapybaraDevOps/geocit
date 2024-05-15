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

import com.softserveinc.geocitizen.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//import com.softserveinc.geocitizen.entity.QUser;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	Optional<User> findByLogin(String login);

	User getByEmail(String email);

	User getByLogin(String login);

	Optional<User> findById(int id);

	Page<User> findByLoginContainingOrEmailContainingOrNameContainingOrSurnameContainingAllIgnoreCase(String login, String email, String name, String surname, Pageable pageable);

	Page<User> findByType(User.Type type, Pageable pageable);

	Page<User> findAll(Pageable pageable);
}
