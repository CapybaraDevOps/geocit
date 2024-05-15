package com.softserveinc.geocitizen.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "message")
public class Msg {

	@Id
	private String id;
	private String text;

	@PersistenceConstructor
	public Msg(String id, String text) {
			this.id = id;
			this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
			return text;
	}

	public void setText(String text) {
			this.text = text;
	}

	@Override
	public String toString() {
		return String.format("Msg{id='%s', text='%s'}", id, text);
	}
}
