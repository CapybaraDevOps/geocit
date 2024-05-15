package com.softserveinc.geocitizen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.softserveinc.geocitizen.configuration.AppConfig;
import com.softserveinc.geocitizen.controller.api.MapRestController;
import com.softserveinc.geocitizen.dto.MarkerDTO;
import com.softserveinc.geocitizen.entity.MapMarker;
import com.softserveinc.geocitizen.service.MapMarkersServiceImpl;
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
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class MapRestControllerTest {

	@Mock
	private MapMarkersServiceImpl markerService;

	@InjectMocks
	private MapRestController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testLoadAllMarkers() throws Exception {

		List<MarkerDTO> markers = new ArrayList<>();
		markers.add(new MarkerDTO(1.123456, 6.54321, 1));
		markers.add(new MarkerDTO(9.909999, 8.88888, 1));

		when(markerService.loadAllMarkers()).thenReturn(markers);
		mockMvc.perform(get("/map").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data", hasSize(2)))
				.andExpect(jsonPath("$.data[0].lat", is(markers.get(0).getLat())))
				.andExpect(jsonPath("$.data[0].lng", is(markers.get(0).getLng())))
				.andExpect(jsonPath("$.data[1].lat", is(markers.get(1).getLat())))
				.andExpect(jsonPath("$.data[1].lng", is(markers.get(1).getLng())));

		verify(markerService, times(1)).loadAllMarkers();
		verifyNoMoreInteractions(markerService);
	}

	@Test
	public void testGetMarkerByCoords() throws Exception {

		MapMarker testMarker = new MapMarker();
		testMarker.setId(1);
		testMarker.setLat(1.111);
		testMarker.setLng(2.222);

		when(markerService.getMarker(1.111, 2.222)).thenReturn(testMarker);
		mockMvc.perform(get("/map/marker/{lat}/{lng}/", 1.111, 2.222)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].id", is(1)));

		verify(markerService, times(1)).getMarker(1.111, 2.222);
		verifyNoMoreInteractions(markerService);
	}

	@Test
	public void testSaveMarker() throws Exception {

		MapMarker testMarker = new MapMarker();
		testMarker.setLat(1.111);
		testMarker.setLng(2.222);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(testMarker);

		when(markerService.saveMarker(testMarker)).thenReturn(testMarker);
		mockMvc.perform(post("/map/marker").contentType(APPLICATION_JSON_UTF8)
				.content(requestJson)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		verify(markerService, times(1)).saveMarker(refEq(testMarker));
		verifyNoMoreInteractions(markerService);
	}
}
