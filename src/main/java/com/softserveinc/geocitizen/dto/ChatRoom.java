package com.softserveinc.geocitizen.dto;

public class ChatRoom {

	private String login;
	private String issueTitle;
	private int userId;
	private int issueId;

	public ChatRoom(String login, String issueTitle, int userId, int issueId) {
		this.login = login;
		this.issueTitle = issueTitle;
		this.userId = userId;
		this.issueId = issueId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getIssueTitle() {
		return issueTitle;
	}

	public void setIssueTitle(String issueTitle) {
		this.issueTitle = issueTitle;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getIssueId() {
		return issueId;
	}

	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}
}
