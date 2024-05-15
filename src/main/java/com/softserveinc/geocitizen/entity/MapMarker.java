/*
 * The following code have been created by Yaroslav Zhyravov (shrralis).
 * The code can be used in non-commercial way for everyone.
 * But for any commercial way it needs a author's agreement.
 * Please contact the author for that:
 *  - https://t.me/Shrralis
 *  - https://twitter.com/Shrralis
 *  - shrralis@gmail.com
 *
 * Copyright (c) 2017 by shrralis (Yaroslav Zhyravov).
 */

package com.softserveinc.geocitizen.entity;

import com.softserveinc.geocitizen.entity.interfaces.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.softserveinc.geocitizen.entity.MapMarker.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
public class MapMarker implements Identifiable<Integer> {

	public static final String TABLE_NAME = "map_markers";
	public static final String ID_COLUMN_NAME = "id";
	public static final String LAT_COLUMN_NAME = "lat";
	public static final String LNG_COLUMN_NAME = "lng";
	public static final String HIDDEN_COLUMN_NAME = "hidden";

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "map_markers_seq_gen")
	@SequenceGenerator(name = "map_markers_seq_gen", sequenceName = "map_markers_id_seq", allocationSize = 1)
	@Column(name = ID_COLUMN_NAME, nullable = false, unique = true)
	private Integer id;

	@NotNull
	@Column(name = LAT_COLUMN_NAME, nullable = false)
	private Double lat;

	@NotNull
	@Column(name = LNG_COLUMN_NAME, nullable = false)
	private Double lng;

	@NotNull
	@Column(name = HIDDEN_COLUMN_NAME, nullable = true)
	private Boolean hidden = false;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Boolean isHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
}
