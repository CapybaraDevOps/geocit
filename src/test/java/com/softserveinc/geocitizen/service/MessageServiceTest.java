package com.softserveinc.geocitizen.service;

import com.softserveinc.geocitizen.configuration.AppConfig;
import com.softserveinc.geocitizen.entity.FullMessage;
import com.softserveinc.geocitizen.repository.MessageRepository;
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

import java.time.LocalDateTime;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class MessageServiceTest {

	@Mock
	private MessageRepository repository;

	@InjectMocks
	private MessageServiceImpl service;

	private FullMessage testMessage;

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);

		testMessage = new FullMessage();
		testMessage.setText("test");
		testMessage.setUserId(1L);
		testMessage.setAuthorId(2L);
		testMessage.setIssueId(3L);
		testMessage.setDate(LocalDateTime.now());
	}

	@Test
	public void testSaveMessage() throws Exception {

		when(repository.save(testMessage)).thenReturn(testMessage);
		FullMessage message = service.saveMessage(testMessage);
		assertNotNull(message);
	}

	@Ignore
	@Test
	public void testGetMessage() throws Exception {

		when(repository.findOne(1L)).thenReturn(testMessage);
		FullMessage message = service.getMessage(1L);
		assertNotNull(message);
		assertEquals(message.getText(), testMessage.getText());
	}

	@Ignore
	@Test
	public void testCheckChat() throws Exception {

		when(repository.existsByIssueIdAndUserId(testMessage.getIssueId(), testMessage.getUserId()))
				.thenReturn(true);
		boolean result = service.checkChat(testMessage.getIssueId(), testMessage.getUserId());
		assertTrue(result);
	}
}
