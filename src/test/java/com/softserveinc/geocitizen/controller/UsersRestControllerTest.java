package com.softserveinc.geocitizen.controller;

import com.softserveinc.geocitizen.configuration.AppConfig;
import com.softserveinc.geocitizen.controller.api.UsersRestController;
import com.softserveinc.geocitizen.entity.User;
import com.softserveinc.geocitizen.service.UserServiceImpl;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class UsersRestControllerTest {

	@Mock
	private UserServiceImpl service;

	@InjectMocks
	private UsersRestController controller;

	private MockMvc mockMvc;

	private User testUser;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		testUser = User.Builder.anUser()
				.setId(1)
				.setLogin("testLogin")
				.setEmail("testEmail")
				.setPassword("testPassword")
				.setName("testName")
				.setSurname("testSurname")
				.setImage(null)
				.setType(User.Type.ROLE_USER)
				.setFailedAuthCount(0)
				.build();
	}

	@Test
	public void testGetAllUsers() throws Exception {
		List<User> list = new ArrayList<>();
		list.add(testUser);

		when(service.getAllUsers()).thenReturn(list);
		mockMvc.perform(get("/users/getAll").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data", hasSize(1)));

		verify(service, times(1)).getAllUsers();
		verifyNoMoreInteractions(service);
	}

	@Test
	public void testGetUserInfo() throws Exception {

		when(service.getUser(1)).thenReturn(testUser);
		mockMvc.perform(get("/users/get/{id}", 1).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].id", is(testUser.getId())));

		verify(service, times(1)).getUser(1);
		verifyNoMoreInteractions(service);
	}
}
