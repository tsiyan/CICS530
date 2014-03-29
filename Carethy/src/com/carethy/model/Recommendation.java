package com.carethy.model;

public class Recommendation {
	private String title;
	private String content;
	private int level;
	public boolean isRead;

	public Recommendation(String title,String content,int level,boolean isRead) {
		this.title=title;
		this.content=content;
		this.level=level;
		this.isRead=isRead;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

}
