package com.carethy.model;

public class Recommendation {
	
	private long _id;
	private int recom_id;//level of severity
	private String recom;
	
	public Recommendation(){
		
	}
	
	public Recommendation(int _id, int recom_id, String recom){
		this._id=_id;
		this.recom_id=recom_id;
		this.recom=recom;
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
}
