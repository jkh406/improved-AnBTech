package com.anbtech.util;

import java.text.SimpleDateFormat;

public class Date{
	// ��¥�� ���Ѵ�
	public String getCurrentDate(){
		java.util.Date now = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String thedate = sdf.format(now);
		return thedate;
	}
	public String getCurrentDate(String format){
		java.util.Date now = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String thedate = sdf.format(now);
		return thedate;
	}
}