package com.softserveinc.geocitizen.repository;

import com.softserveinc.geocitizen.configuration.TestDatabaseConfig;
import com.softserveinc.geocitizen.entity.RecoveryToken;
import com.softserveinc.geocitizen.entity.User;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestDatabaseConfig.class })
@WebAppConfiguration
@Transactional
public class RecoveryTokenRepositoryTest extends TestCase {

	@Autowired
	private RecoveryTokensRepository repository;

	private RecoveryToken testToken;
	private User testUser;

	@Before
	public void setUp() throws Exception {

		testUser = User.Builder.anUser()
				.setLogin("testLogin")
				.setEmail("testEmail")
				.setPassword("testPassword")
				.setName("testName")
				.setSurname("testSurname")
				.setImage(null)
				.setType(User.Type.ROLE_USER)
				.setFailedAuthCount(0)
				.build();

		testToken = RecoveryToken.Builder.aRecoveryToken()
				.setToken("123456")
				.setExpiresAt(LocalDateTime.now())
				.setUser(testUser)
				.build();
		repository.save(testToken);
	}

	@Test
	public void testFindByToken() throws Exception {

		Optional<RecoveryToken> token = repository.findByToken(testToken.getToken());
		assertNotNull(token);
	}

	@Test
	public void testCountNonExpiredByUser() throws Exception {

		int nonexistentUserId = -1;
		int count = repository.countNonExpiredByUser(nonexistentUserId);
		assertTrue(count == 0);

	}
}
