/*
 * The following code have been created by Yurii Kiziuk.
 * The code can be used in non-commercial way.
 * For any commercial use it needs an author's agreement.
 * Please contact the author:
 *  - yurakizyuk@gmail.com
 *
 * Copyright (c) 2017 by Yurii Kiziuk.
 */

package com.softserveinc.geocitizen.service.interfaces;

import com.softserveinc.geocitizen.dto.MarkerDTO;
import com.softserveinc.geocitizen.entity.MapMarker;

import java.util.List;

public interface IMapMarkersService {

	List<MarkerDTO> loadAllMarkers();

	MapMarker getMarker(double lat, double lng);

	MapMarker saveMarker(MapMarker marker);
}
