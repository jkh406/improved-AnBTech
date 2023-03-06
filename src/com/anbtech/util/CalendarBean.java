package com.anbtech.util;

import java.util.Date;
import java.util.*;
import java.text.*;

public class CalendarBean{

	//calendar를 위한 변수 설정
	int Year,Month,Day,First_of_Month,LastDay;
	Calendar today,newdate,p_date;
	
	public CalendarBean(){
	   p_date = Calendar.getInstance();
	   doRun(p_date);
	}
	
	private void doRun(Calendar p_date){
		
	   today = p_date;
		
	   Year = today.get(Calendar.YEAR);
	   Month = today.get(Calendar.MONTH)+1;
	   Day = today.get(Calendar.DATE);
		
	   //정확한 달과 그달 첫번일의 요일을 얻는 방법1
	   today.set(Calendar.DAY_OF_MONTH, 1);
	   
	   First_of_Month = today.get(Calendar.DAY_OF_WEEK);
	   LastDay = getMonthLastDay(Year,Month);
	}

	// jsp에서 적용된 값을 새로 설정한다.
	public void setNewDate(Calendar p_date){
		
           newdate = p_date;
           		
	   Year = newdate.get(Calendar.YEAR);
	   Month = newdate.get(Calendar.MONTH)+1;
	   Day = newdate.get(Calendar.DATE);

	   First_of_Month = newdate.get(Calendar.DAY_OF_WEEK);
	   LastDay=getMonthLastDay(Year,Month);
		
	}

	//각 달의 날짜수를 구한다.
	private int getMonthLastDay(int year, int month){
	   switch (month) {
     		case 1:
     		case 3:
     		case 5:
     		case 7:
     		case 8:
            case 10:
            case 12:
        	  return (31);
	
   	        case 4:
            case 6:
            case 9:
            case 11:
        	  return (30);

   	        default:
        	  if(((year%4==0)&&(year%100!=0)) || (year%400==0) )
			     return (29);   // 2월 윤년계산을 위해서 
	      	  else 
	      	     return (28);
	   }
	}
	//set
	public void setMyYear(int value){
	   this.Year=value;
	}
	public void setMyMonth(int value){
	   this.Month=value;
	}
	public void setMyDay(int value){
	   this.Day=value;
	}
	public void setMyWeek(int value){
	   this.First_of_Month=value;
	}
	
	public void setWeekNo(String opt){

           newdate = Calendar.getInstance();

           if (opt.equals("prev")) { 
              newdate.set(this.Year,(this.Month-1),(this.Day-7));
           } else if (opt.equals("next")){   
              newdate.set(this.Year,(this.Month-1),(this.Day+7));
           }
                      		
	   this.Year = newdate.get(Calendar.YEAR);
	   this.Month = newdate.get(Calendar.MONTH)+1;
	   this.Day = newdate.get(Calendar.DATE);

	}

	//get
	public int getMyYear(){
	   int value=this.Year;
	   return value;
	}
	public int getMyMonth(){
	   int value=this.Month;
	   return value;
	}
	public int getMyDay(){
	   int value=this.Day;
	   return value;
	}
	public int getMyWeek(){
	   int value=this.First_of_Month;
	   return value;
	}
	public int getMyLastDay(){
	   int value=this.LastDay;
	   return value;
	}

	public int getWeekOfMonth (int yyyy,int mm,int dd) {
           Calendar t_date;
           t_date = Calendar.getInstance();
           t_date.set(Calendar.YEAR, yyyy);
           t_date.set(Calendar.MONTH, mm - 1);
           t_date.set(Calendar.DATE, dd);
           		
	   return t_date.get(Calendar.WEEK_OF_MONTH);
	}
	
	//Date Check
    public boolean DateCheck(String dt){//input date = "yyyy-mm-dd"

           boolean value = true;

           try {
              DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
              df.setLenient(false); 
              Date dt2 = df.parse(dt); 
           } catch (ParseException e) {          //input이 yyyy-mm-dd format이 아닐경우
              value = false; 
           } catch (IllegalArgumentException e) { // yyyy,mm,dd가 유효하지 않은 날짜 일경우...
              value = false;
           }
           
           return value;
           
    }//end of constructor
        

    public String DateFormat(String d_fmt, Date d){
           SimpleDateFormat sdf = new SimpleDateFormat (d_fmt);
           String value = sdf.format(d);
           return value;
    }   
}
