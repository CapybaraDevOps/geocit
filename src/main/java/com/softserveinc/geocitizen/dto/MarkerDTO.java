/*
 * The following code have been created by Yurii Kiziuk.
 * The code can be used in non-commercial way.
 * For any commercial use it needs an author's agreement.
 * Please contact the author:
 *  - yurakizyuk@gmail.com
 *
 * Copyright (c) 2017 by Yurii Kiziuk.
 */

package com.softserveinc.geocitizen.dto;

public class MarkerDTO {

	private Double lat;

	private Double lng;

	private Integer type;

	public MarkerDTO(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public MarkerDTO(double lat, double lng, int type) {
		this.lat = lat;
		this.lng = lng;
		this.type = type;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
