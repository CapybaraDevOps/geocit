package com.softserveinc.geocitizen.repository;

import com.softserveinc.geocitizen.configuration.TestDatabaseConfig;
import com.softserveinc.geocitizen.entity.FullMessage;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.List;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestDatabaseConfig.class })
@WebAppConfiguration
@Transactional
public class MessageRepositoryTest extends TestCase {

	@Autowired
	private MessageRepository repository;

	@Test
	public void testExistsByIssueIdAndUserId() throws Exception {

		long nonexistentUserId = -1L;
		long nonexistentIssueId = -1L;
		boolean result = repository.existsByIssueIdAndUserId(nonexistentIssueId, nonexistentUserId);
		assertTrue(result == false);
	}

	@Test
	public void testFindAllByIssueIdAndUserId() throws Exception {

		long nonexistentUserId = -1L;
		long nonexistentIssueId = -1L;
		List<FullMessage> list = repository.findAllByIssueIdAndUserId(nonexistentIssueId, nonexistentUserId);
		assertTrue(list.size() == 0);
	}
}
