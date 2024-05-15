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

package com.softserveinc.geocitizen.service;

import com.softserveinc.geocitizen.dto.EditUserDTO;
import com.softserveinc.geocitizen.dto.UserProfileDTO;
import com.softserveinc.geocitizen.entity.Image;
import com.softserveinc.geocitizen.entity.User;
import com.softserveinc.geocitizen.exception.AbstractCitizenException;
import com.softserveinc.geocitizen.exception.AccessDeniedException;
import com.softserveinc.geocitizen.exception.EntityNotExistException;
import com.softserveinc.geocitizen.repository.ConnectionRepository;
import com.softserveinc.geocitizen.repository.UsersRepository;
import com.softserveinc.geocitizen.security.model.AuthorizedUser;
import com.softserveinc.geocitizen.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

	private final UsersRepository userRepository;
	private final ConnectionRepository connectionRepository;

	@Autowired
	public UserServiceImpl(UsersRepository userRepository, ConnectionRepository connectionRepository) {
		this.userRepository = userRepository;
		this.connectionRepository = connectionRepository;
	}

	@Override
	@ReadOnlyProperty
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	@ReadOnlyProperty
	public User getUser(int id) {
		return userRepository.getOne(id);
	}

	@Override
	@ReadOnlyProperty
	public User findById(Integer id) throws AbstractCitizenException {
		return userRepository.findById(id).orElseThrow(() -> new EntityNotExistException(EntityNotExistException.Entity.USER));
	}

	@Override
	@ReadOnlyProperty
	public User findByLogin(String login) throws AbstractCitizenException {
		return userRepository.findByLogin(login).orElseThrow(() -> new EntityNotExistException(EntityNotExistException.Entity.USER));
	}

	@Override
	@ReadOnlyProperty
	public Page<User> findByLoginOrEmailOrNameOrSurname(String login, String email, String name, String surname, Pageable pageable) {
		return userRepository.findByLoginContainingOrEmailContainingOrNameContainingOrSurnameContainingAllIgnoreCase(login, email, name, surname, pageable);
	}

	@Override
	public User setUserStatus(User.Type type, Integer id) throws AbstractCitizenException {
		User.Type curUser = AuthorizedUser.getCurrent().getType();
		if (curUser == User.Type.ROLE_MASTER) {
			return setStatusByMaster(type, id);
		} else if (curUser == User.Type.ROLE_ADMIN) {
			return setStatusByAdmin(type, id);
		} else {
			throw new AccessDeniedException();
		}
	}

	private User setStatusByMaster(User.Type type, Integer id) throws AbstractCitizenException {
		User user = userRepository.getOne(id);
		if (AuthorizedUser.getCurrent().getId() == user.getId() || type == user.getType()) {
			throw new AccessDeniedException();
		}
		user.setType(type);
		return userRepository.save(user);
	}

	private User setStatusByAdmin(User.Type type, Integer id) throws AbstractCitizenException {
		User user = userRepository.getOne(id);
		if (AuthorizedUser.getCurrent().getId() == user.getId() || type == user.getType()
				|| type == User.Type.ROLE_MASTER || type == User.Type.ROLE_ADMIN
				|| user.getType() == User.Type.ROLE_MASTER || user.getType() == User.Type.ROLE_ADMIN) {
			throw new AccessDeniedException();
		}
		user.setType(type);
		return userRepository.save(user);
	}

	@Override
	@ReadOnlyProperty
	public Page<User> findByType(User.Type type, Pageable pageable) {
		return userRepository.findByType(type, pageable);
	}

	@Override
	@ReadOnlyProperty
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public void edit(final EditUserDTO dto) {
		final User user = userRepository.findOne(AuthorizedUser.getCurrent().getId());
		user.setName(dto.getName());
		user.setSurname(dto.getSurname());
		userRepository.save(user);
	}

	@Override
	public void updateImage(final Image image) {
		final User user = userRepository.getOne(AuthorizedUser.getCurrent().getId());
		user.setImage(image);
		userRepository.save(user);
	}

	@Override
	public UserProfileDTO getUserProfile(int id) {
		UserProfileDTO dto = new UserProfileDTO();
		User user = userRepository.findById(id).get();
		dto.setId(id);
		dto.setLogin(user.getLogin());
		dto.setEmail(user.getEmail());
		dto.setType(user.getType());
		dto.setImage(user.getImage());
		dto.setName(user.getName());
		dto.setSurname(user.getSurname());
		if (connectionRepository.getByUserIdAndProvider(String.valueOf(id), "facebook") != null) {
			dto.setFacebookConnected(true);
		}
		if (connectionRepository.getByUserIdAndProvider(String.valueOf(id), "google") != null) {
			dto.setGoogleConnected(true);
		}
		return dto;
	}
}
