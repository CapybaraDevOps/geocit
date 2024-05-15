package com.softserveinc.geocitizen.repository;

import com.softserveinc.geocitizen.configuration.TestDatabaseConfig;
import com.softserveinc.geocitizen.entity.Image;
import com.softserveinc.geocitizen.entity.Issue;
import com.softserveinc.geocitizen.entity.MapMarker;
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
public class IssueRepositoryTest extends TestCase {

	@Autowired
	MapMarkersRepository mapMarkersRepository;
	@Autowired
	private IssuesRepository repository;
	private User testUser;
	private Issue testIssue;
	private MapMarker testMarker;
	private Image testImage;
	private Issue.Type testType;

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

		testMarker = new MapMarker();
		testMarker.setLat(1.23456);
		testMarker.setLng(6.54321);
		mapMarkersRepository.save(testMarker);

		testImage = new Image();
		testImage.setSrc("test/src");
		testImage.setHash("testHash");
		testImage.setType(Image.Type.ISSUE);

		testType = new Issue.Type();
		testType.setName("PROBLEM");

		testIssue = Issue.Builder.anIssue()
				.setMapMarker(testMarker)
				.setTitle("title")
				.setText("description")
				.setAuthor(testUser)
				.setImage(testImage)
				.setType(testType)
				.setClosed(false)
				.setCreatedAt(LocalDateTime.now())
				.setUpdatedAt(LocalDateTime.now())
				.build();
		repository.save(testIssue);
	}

	@Test
	public void testFindById() throws Exception {
		Optional<Issue> issue = repository.findById(1);
		assertNotNull(issue);
	}

	@Test
	public void testGetIssueTypeById() throws Exception {

		int[] result = repository.getIssueTypeByIdAndHiddenFalse(testMarker.getId());
		assertEquals(result.length, 1);
	}
}
