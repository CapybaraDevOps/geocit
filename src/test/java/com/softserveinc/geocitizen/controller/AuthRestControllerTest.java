package com.softserveinc.geocitizen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.softserveinc.geocitizen.configuration.AppConfig;
import com.softserveinc.geocitizen.controller.api.AuthRestController;
import com.softserveinc.geocitizen.dto.PasswordRecoveryDTO;
import com.softserveinc.geocitizen.dto.RegisterUserDTO;
import com.softserveinc.geocitizen.dto.RegisteredUserDTO;
import com.softserveinc.geocitizen.dto.UserSessionDTO;
import com.softserveinc.geocitizen.entity.User;
import com.softserveinc.geocitizen.service.AuthServiceImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class AuthRestControllerTest {

	@Mock
	private AuthServiceImpl service;

	@InjectMocks
	private AuthRestController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testGetCurrentSession() throws Exception {

		UserSessionDTO dto = new UserSessionDTO(1, "testLogin", User.Type.ROLE_USER);

		when(service.getCurrentSession()).thenReturn(dto);
		mockMvc.perform(get("/auth/currentSession").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].type", is(dto.getType().name())));

		verify(service, times(1)).getCurrentSession();
		verifyNoMoreInteractions(service);
	}

	@Test
	public void testRecoverPassword() throws Exception {

		RegisteredUserDTO userDto = new RegisteredUserDTO();
		userDto.setId(1);
		userDto.setLogin("testLogin");

		PasswordRecoveryDTO passDto = new PasswordRecoveryDTO();
		passDto.setLogin(userDto.getLogin());
		passDto.setPassword("testPassword");
		passDto.setToken("testToken");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(passDto);

		when(service.recoverPassword(passDto)).thenReturn(userDto);
		mockMvc.perform(post("/auth/recoverPassword").contentType(APPLICATION_JSON_UTF8)
				.content(requestJson)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		verify(service, times(1)).recoverPassword(refEq(passDto));
		verifyNoMoreInteractions(service);
	}

	@Test
	public void testSignUp() throws Exception {

		RegisterUserDTO userDto = new RegisterUserDTO();
		userDto.setLogin("testLogin");
		userDto.setEmail("testEmail");
		userDto.setName("testName");
		userDto.setSurname("testSurname");

		RegisteredUserDTO registeredUserDTO = new RegisteredUserDTO();
		registeredUserDTO.setLogin(userDto.getLogin());

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(userDto);

		when(service.signUp(userDto)).thenReturn(registeredUserDTO);
		mockMvc.perform(post("/auth/signUp").contentType(APPLICATION_JSON_UTF8)
				.content(requestJson)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		verify(service, times(1)).signUp(refEq(userDto));
		verifyNoMoreInteractions(service);
	}
}
