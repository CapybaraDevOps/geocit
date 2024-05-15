package com.softserveinc.geocitizen.dto.mapper;

import com.softserveinc.geocitizen.dto.RegisteredUserDTO;
import com.softserveinc.geocitizen.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 12/27/17 at 5:53 PM
 */
@Mapper(componentModel = "spring")
public interface RegisteredUserMapper {

	@Mappings({
			@Mapping(source = "user.login", target = "login"),
			@Mapping(source = "user.id", target = "id")
	})
	RegisteredUserDTO userToRegisteredUserDto(User user);
}
