package com.softserveinc.geocitizen.service;

import com.softserveinc.geocitizen.configuration.AppConfig;
import com.softserveinc.geocitizen.entity.Notification;
import com.softserveinc.geocitizen.repository.NotificationRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class NotificationServiceTest {

	@Mock
	private NotificationRepository repository;

	@InjectMocks
	private NotificationServiceImpl service;

	private Notification testNotification;

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);

		testNotification = new Notification();
		testNotification.setLogin("login");
		testNotification.setText("text");
		testNotification.setWaiting(false);
		testNotification.setIssueId(1L);
		testNotification.setUserId(2L);
	}

	@Test
	public void testRemoveNotification() throws Exception {

		when(repository.deleteByIssueIdAndUserId(testNotification.getIssueId(), testNotification.getUserId()))
				.thenReturn(1L);
		long id = service.removeNotification(testNotification);
		assertTrue(id == 1L);
	}

	@Test
	public void testAddNotification() throws Exception {

		when(repository.save(testNotification)).thenReturn(testNotification);
		Notification notification = service.addNotification(testNotification);
		assertNotNull(notification);
		assertEquals(notification.getId(), testNotification.getId());
	}
}
