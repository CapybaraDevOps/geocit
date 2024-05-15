package com.softserveinc.geocitizen.service.interfaces;

import com.softserveinc.geocitizen.dto.EditUserDTO;
import com.softserveinc.geocitizen.dto.UserProfileDTO;
import com.softserveinc.geocitizen.entity.Image;
import com.softserveinc.geocitizen.entity.User;
import com.softserveinc.geocitizen.exception.AbstractCitizenException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface for getting user accounts information.
 *
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0 Created 12/24/17 at 1:29 AM
 */
public interface IUserService {

	List<User> getAllUsers();

	User getUser(int id);

	User findById(Integer id) throws AbstractCitizenException;

	User findByLogin(String login) throws AbstractCitizenException;

	Page<User> findByLoginOrEmailOrNameOrSurname(String login, String email, String name, String surname, Pageable pageable);

	User setUserStatus(User.Type type, Integer id) throws AbstractCitizenException;

	Page<User> findByType(User.Type type, Pageable pageable);

	Page<User> findAll(Pageable pageable);

	void edit(EditUserDTO dto);

	void updateImage(Image image);

	UserProfileDTO getUserProfile(int id);
}
