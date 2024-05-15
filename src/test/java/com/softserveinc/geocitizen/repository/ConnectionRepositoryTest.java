package com.softserveinc.geocitizen.repository;

import com.softserveinc.geocitizen.configuration.TestDatabaseConfig;
import com.softserveinc.geocitizen.entity.UserConnection;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestDatabaseConfig.class })
@WebAppConfiguration
@Transactional
public class ConnectionRepositoryTest extends TestCase {

	@Autowired
	ConnectionRepository repository;

	UserConnection userProvider;

	@Test
	public void testGetByEmail() throws Exception {

		UserConnection userProvider = repository.getByUserIdAndProvider("5", "facebook");
		System.out.println(userProvider);
	}
}
