package com.softserveinc.geocitizen.repository;

import com.softserveinc.geocitizen.configuration.TestDatabaseConfig;
import com.softserveinc.geocitizen.entity.Image;
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

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestDatabaseConfig.class })
@WebAppConfiguration
@Transactional
public class ImagesRepositoryTest extends TestCase {

	@Autowired
	private ImagesRepository repository;

	private Image testImage;

	@Before
	public void setUp() throws Exception {

		testImage = new Image();
		testImage.setSrc("test/src");
		testImage.setHash("testHash");
		testImage.setType(Image.Type.ISSUE);
		repository.save(testImage);
	}

	@Test
	public void testGetByHash() throws Exception {

		Image image = repository.getByHash(testImage.getHash());
		assertNotNull(image);
		assertEquals(image.getHash(), testImage.getHash());
	}
}
