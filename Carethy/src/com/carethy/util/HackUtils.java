package com.carethy.util;

import com.carethy.application.Carethy;

public class HackUtils {
	
	public static final boolean test = false;
	
	public static void hackIntoRecomDB(int recomId, String recommend){
		Carethy.datasource.insertIntoTable(recomId, recommend, "www.google.com");
	}

}
