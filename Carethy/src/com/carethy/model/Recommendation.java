package com.carethy.model;

public class Recommendation {

	private long _id;
	private int recom_id;// level of severity
	private String recom;
	private boolean isRead;
	private String url;
	private String sdate;

	public Recommendation() {

	}

	public Recommendation(int _id, int recom_id, String recom) {
		this._id = _id;
		this.recom_id = recom_id;
		this.recom = recom;
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

	public boolean isRead() {
		return isRead;
	}

	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String toString() {
		return recom_id + " - " + recom;
	}

	public String getSaveDate() {
		return sdate;
	}

	public void setSavedate(String sdate) {
		this.sdate = sdate;
	}
}
