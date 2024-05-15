package com.softserveinc.geocitizen.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class FullMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	private String text;
	private Long userId;
	private Long issueId;
	private Long authorId;
	private LocalDateTime date;
	private Long msgid;

	public FullMessage(Long id, String text) {
		this.id = id;
		this.text = text;
	}

	public FullMessage() {
		this.text = "default";
	}

	public static FullMessage messageBuilder(Message input, Long userId, Long issueId) {
		FullMessage message = new FullMessage();
		message.setText(input.getText());
		message.setIssueId(issueId);
		message.setUserId(userId);
		message.setAuthorId(input.getAuthorId());
		message.setDate(LocalDateTime.now());
		message.setMsgId(input.getUserId());
		return message;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getIssueId() {
		return issueId;
	}

	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Long getMsgid() {
		return msgid;
	}

	public void setMsgId(Long msgid) {
		this.msgid = msgid;
	}
}
