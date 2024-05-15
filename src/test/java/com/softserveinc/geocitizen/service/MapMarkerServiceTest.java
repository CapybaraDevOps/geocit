package com.softserveinc.geocitizen.service;

import com.softserveinc.geocitizen.configuration.AppConfig;
import com.softserveinc.geocitizen.entity.MapMarker;
import com.softserveinc.geocitizen.repository.MapMarkersRepository;
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

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class MapMarkerServiceTest {

	@Mock
	private MapMarkersRepository repository;

	@InjectMocks
	private MapMarkersServiceImpl service;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetMarker() throws Exception {

		MapMarker testMarker = new MapMarker();
		testMarker.setId(1);
		testMarker.setLat(1.0);
		testMarker.setLng(2.0);
		when(repository.findByLatAndLng(1.0, 2.0)).thenReturn(testMarker);
		MapMarker result = service.getMarker(1.0, 2.0);
		assertNotNull(result);
	}
}
