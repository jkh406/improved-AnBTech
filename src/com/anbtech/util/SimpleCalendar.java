package com.anbtech.util;

import java.util.*;

public class SimpleCalendar {
	private Calendar now;

	// 디폴트 생성자
	// 현재의 날짜 정보를 저장한다.
	public SimpleCalendar(){
		now = Calendar.getInstance();
		now.set(Calendar.DATE, 1);
	}
	
	// 지정된 날짜로 날짜 정보를 저장한다.
	public void setCalendar(int year, int month){
		now.set(Calendar.YEAR, year);
		now.set(Calendar.MONTH, month-1);
		now.set(Calendar.DATE, 1);
	}
	
	public void setCalendar(String year, String month){
		try {
			now.set(Calendar.YEAR, Integer.parseInt(year));
			now.set(Calendar.MONTH, Integer.parseInt(month) -1);
			now.set(Calendar.DATE, 1);
		}catch (NumberFormatException e){
			
		}
	}

	// mode 값 별로 날짜 정보를 저장한다.
	public void setCalendar(String mode){
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day = now.get(Calendar.DATE);
		
		if ("prevyear".equals(mode))
			year--;
		else if ("nextyear".equals(mode))
			year++;
		else if ("prevmonth".equals(mode))
			month--;
		else if ("nextmonth".equals(mode))
			month++;
		else if ("prevday".equals(mode))
			day--;
		else if ("nextday".equals(mode))
			day++;
		
		setCalendar(year, month);
	}
	
	public int getYear(){
		return now.get(Calendar.YEAR);
	}
	public int getMonth(){
		return now.get(Calendar.MONTH) + 1;
	}
	public int getDay(){
		return now.get(Calendar.DATE);
	}
	public int getDayOfWeek(){
		return now.get(Calendar.DAY_OF_WEEK)-1;
	}
	// 현재 달의 날짜의 개수를 리턴한다.
	public int getDayCount(){
		int count = 0;
		int year = now.get(Calendar.YEAR);
		switch (now.get(Calendar.MONTH)+1){
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				count = 31; break;
			case 4:
			case 6:
			case 9:
			case 11:
				count = 30; break;
			default:
				if((year % 4 ==0) &&(year % 100 !=0 ) ||(year % 400 ==0)){
					count = 29;
				}else{
					count = 28;
				}
		}
		
		return count;
	}
}