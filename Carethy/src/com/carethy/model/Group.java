package com.carethy.model;

import java.util.ArrayList;

public class Group {

	public String string;
	public ArrayList<Recommendation> children = new ArrayList<Recommendation>();

	public Group(String string) {
		this.string = string;
	}
}