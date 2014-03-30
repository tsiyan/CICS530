package com.carethy.model;

public class Recommendation {
<<<<<<< HEAD
	
	private long _id;
	private int recom_id;
	private String recom;
	
	public Recommendation(){
		
	}

	public long getId() {
		return _id;
	}

	public void setId(long _id) {
		this._id = _id;
	}

	public int getRecomId() {
		return recom_id;
	}

	public void setRecomId(int recom_id) {
		this.recom_id = recom_id;
	}

	public String getRecom() {
		return recom;
	}

	public void setRecom(String recom) {
		this.recom = recom;
	}
	
	public String toString(){
		return recom_id + " - " + recom;
	}
=======
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

>>>>>>> origin/master
}
