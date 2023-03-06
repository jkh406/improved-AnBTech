package com.anbtech.date;
import java.util.*;
import java.text.*;

public class anbDate
{
	
	/***************************************************************************
	 * 생성자 
	 **************************************************************************/
	public anbDate()
	{
		
	}	

	/***************************************************************************
	 * 			요일 구하기 
	 **************************************************************************/	 			
	/*-------------------------------------------------------------------------
	 * 현재요일을 리턴하는 메소드 (입력: yyyy, mm, dd)
	 * 1:일 2:월 3:화 4:수 5:목 6:금 7:토  
	 --------------------------------------------------------------------------*/
	public int getDay(int year,int month,int date)
	{
		GregorianCalendar gc=new GregorianCalendar(year, month -1 , date);
		return gc.get(gc.DAY_OF_WEEK); 
	}	


	/***************************************************************************
	 * 			몇주차 구하기 (주간단위)
	 **************************************************************************/
	/*-------------------------------------------------------------------------
	 * 년기준 몇주차 인지차인지을 리턴하는 메소드 (입력: yyyy, mm, dd)
	 * return : 1 ~ 53주차 / 년 
	 --------------------------------------------------------------------------*/
	public int getWeekOfYear(int year,int month,int date)
	{
		GregorianCalendar gc=new GregorianCalendar(year, month -1 , date);
		return gc.get(gc.WEEK_OF_YEAR); 
	}	

	/*-------------------------------------------------------------------------
	 * 달기준 몇주차 인지차인지을 리턴하는 메소드 (입력: yyyy, mm, dd)
	 * return : 1 ~ 6주차 / 년 
	 --------------------------------------------------------------------------*/
	public int getWeekOfMonth(int year,int month,int date)
	{
		GregorianCalendar gc=new GregorianCalendar(year, month -1 , date);
		return gc.get(gc.WEEK_OF_MONTH); 
	}	


	/***************************************************************************
	 * 		날자 구하기 (년-월-일)
	 **************************************************************************/
	/*--------------------------------------------------------------------------
	 * 현재 날자 (년-월-일)
	 --------------------------------------------------------------------------*/
	public String getDate()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("yyyy-MM-dd");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * 현재 년도 구하기 
	 --------------------------------------------------------------------------*/
	public String getYear()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("yyyy");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * 현재 월 구하기  
	 --------------------------------------------------------------------------*/
	public String getMonth()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("MM");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * 현재 일 구하기 
	 --------------------------------------------------------------------------*/
	public String getDates()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("dd");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * 더하여진 날자 구하기 
	 * 기준      : 현재 날자 (년,월,일)
	 * 더할 날자 : date
	 --------------------------------------------------------------------------*/
	public String getDate(int date)
	{

		//주어진 날자 만큼 더한다.
		Calendar calendar = new GregorianCalendar();
		calendar.add(calendar.DATE,date);

		//현재의 날자를 구한다.
		Date now = calendar.getTime();
				
		//String 리턴한다.
		SimpleDateFormat vans = new SimpleDateFormat("yyyy/MM/dd");
		String request_date = vans.format(now);
		return request_date;
	}
		
	/*--------------------------------------------------------------------------
	 * 더하여진 날자 구하기 
	 * 기준      : 주어진 날자 Setting (syear, smonth, sdate) 
	 * 더할 날자 : date
	 --------------------------------------------------------------------------*/
	public String getDate(int syear,int smonth, int sdate, int date)
	{
		//주어진 년,월,일로 날자 setting
		Calendar calendar = new GregorianCalendar();
		calendar.set(syear,smonth-1,sdate);
		
		//주어진 일자 만큼 더한다.
		calendar.add(calendar.DATE,date);
		
		//현재의 날자를 구한다.
		Date now = calendar.getTime();
				
		//String 리턴한다.
		SimpleDateFormat vans = new SimpleDateFormat("yyyy/MM/dd");
		String request_date = vans.format(now);
		return request_date;		
	}
	/*--------------------------------------------------------------------------
	 * 더하여진 날자 구하기 
	 * 기준      : 주어진 날자 Setting (yyyymmdd) 
	 * 더할 날자 : date
	 --------------------------------------------------------------------------*/
	public String getDate(String ymd, int date, String outformat)
	{
		String request_date="";		//return date
		//입력일 검사
		if(ymd.length() != 8) return request_date;

		//주어진 날자 분해
		int syear = Integer.parseInt(ymd.substring(0,4));
		int smonth = Integer.parseInt(ymd.substring(4,6));
		int sdate = Integer.parseInt(ymd.substring(6,8));
		
		//주어진 년,월,일로 날자 setting
		Calendar calendar = new GregorianCalendar();
		calendar.set(syear,smonth-1,sdate);
		
		//주어진 일자 만큼 더한다.
		calendar.add(calendar.DATE,date);
		
		//현재의 날자를 구한다.
		Date now = calendar.getTime();
				
		//String 리턴한다.
		String fmt = "yyyy"+outformat+"MM"+outformat+"dd";
		SimpleDateFormat vans = new SimpleDateFormat(fmt);
		request_date = vans.format(now);
		return request_date;		
	}
	
	/*--------------------------------------------------------------------------
	 * 년월일로 Setting하기 
	 * 기준      : 주어진 날자 Setting (syear, smonth, sdate) 
	 * retun  	 : Seting yyyy-MM-dd
	 --------------------------------------------------------------------------*/
	public String setDate(int syear,int smonth, int sdate)
	{
		//주어진 년,월,일로 날자 setting
		Calendar calendar = new GregorianCalendar();
		calendar.set(syear,smonth-1,sdate);
		
		//현재의 날자를 구한다.
		Date now = calendar.getTime();
				
		//String 리턴한다.
		SimpleDateFormat vans = new SimpleDateFormat("yyyy/MM/dd");
		String request_date = vans.format(now);
		return request_date;		
	}	
	

	/***************************************************************************
	 * 		시간 구하기 (년-월-일 시:분)
	 **************************************************************************/
	/*--------------------------------------------------------------------------
	 * 현재시간을 리턴하는 메소드
	 --------------------------------------------------------------------------*/	
	public String getTime()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String request_date = vans.format(now);
		return request_date;
	}
		
	/*--------------------------------------------------------------------------
	 * 현재시각을 리턴하는 메소드
	 --------------------------------------------------------------------------*/			
	public String getHours()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("HH");
		String request_date = vans.format(now);
		return request_date;
	}	
	
	/***************************************************************************
	 * 		Data관리용 날자 구하기 (년월일) : format이 없음 
	 **************************************************************************/
	/*--------------------------------------------------------------------------
	 * 현재날자을 리턴하는 메소드
	 --------------------------------------------------------------------------*/
	public String getDateNoformat()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * 주어진 년도만큼 더하여 리턴하는 메소드
	 --------------------------------------------------------------------------*/
	public String getAddYearNoformat(int year)
	{ 	
		//주어진 년도 만큼 더한다.
		Calendar calendar = new GregorianCalendar();
		calendar.add(calendar.YEAR,year);

		//현재의 날자를 구한다.
		Date now = calendar.getTime();
				
		//String 리턴한다.
		SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * 주어진 월만큼 더하여 리턴하는 메소드
	 --------------------------------------------------------------------------*/
	public String getAddMonthNoformat(int month)
	{
		//주어진 월 만큼 더한다.
		Calendar calendar = new GregorianCalendar();
		calendar.add(calendar.MONTH,month);

		//현재의 날자를 구한다.
		Date now = calendar.getTime();
				
		//String 리턴한다.
		SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * 주어진 날자 만큼 더하여 리턴하는 메소드
	 --------------------------------------------------------------------------*/
	public String getAddDateNoformat(int date)
	{
		//주어진 날자 만큼 더한다.
		Calendar calendar = new GregorianCalendar();
		calendar.add(calendar.DATE,date);

		//현재의 날자를 구한다.
		Date now = calendar.getTime();
				
		//String 리턴한다.
		SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
		String request_date = vans.format(now);
		return request_date;
	}	
	/***************************************************************************
	 * 		날자간 차이 구하기 (기간 구하기)
	 **************************************************************************/
	/*--------------------------------------------------------------------------
	 * 시작(년월이)과 끝(년월일)을 비교하여 기간구하기 : 같은날자 미포함(0일)
	 syear/smonth/sdate : 시작 년/월/일, eyear/emonth/edate/ 끝 년/월/일
	 --------------------------------------------------------------------------*/
	 public int getPeriodDate(int syear,int smonth,int sdate,int eyear,int emonth,int edate)
	{
		 int period = 0;			//기간 리턴값
		 smonth = smonth - 1;		//시작하는 달 (달은 0부터 시작됨)
		 emonth = emonth - 1;		//끝나는 달 (달의 시작은 0부터)
		 Calendar calendar = new GregorianCalendar();

		int end_period = 0;						//총기간일자
		//1. 시작년도와 끝나는 년도가 같을때
		if(syear == eyear) {
			//년월일이 같으면
			if((smonth == emonth) && (sdate == edate)) {
				end_period = 0;
			} 
			//년월이 같으면
			else if((smonth == emonth) && (sdate != edate)){
				end_period = edate - sdate;
			} else {
				int dif = emonth - smonth;		//몇달 차이인가
				for(int i=0; i<=dif; i++) {
					int nmonth = smonth + i;	//1더한 새로운 달
					//1더한달이 시작달과 같을때
					if(nmonth == smonth) {								//시작달 남은 일수
						calendar.set(syear,smonth,sdate);
						int start_period = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);		
						end_period += start_period - sdate;
						int d1 = nmonth + 1;	int dc = start_period - sdate;
						//System.out.println(syear + " : " + d1 + " : " + dc);
					}
					//1더한달이 끝나는 달과 같을때
					else if(nmonth == emonth) {						//끝나는달 일수
						end_period += edate;
						int d2 = emonth + 1;
						//System.out.println(syear + " : " + d2 + " : " + edate);
					} 
					//1더한달이 중간에 있는달
					else {											//시작과 끝달 사이의 달 총일수
						calendar.set(eyear,nmonth,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d3 = i + 1;
						//System.out.println(syear + " : " + d3 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					} //if
				} //for
			}
		}
		//2. 시작년도와 끝나는 년도가 다를때
		else {
			for(int n = syear; n <=eyear; n++) { 
				int nyear = n;
				//1. 시작년도 일때
				if(nyear == syear) {
					//시작달 날짜
					calendar.set(nyear,smonth,sdate);
					int str_period = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					end_period += str_period - sdate;
					int d21 = smonth + 1;
					//System.out.println(nyear + " : " + d21 + " : " + end_period);		
					
					//나머지 달 (시작달 다음달 ~ 12월까지)
					for(int j=smonth+1; j<12; j++) {									
						calendar.set(nyear,j,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d22 = j + 1;
						//System.out.println(nyear + " : " + d22 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					} //for
				}
				//2. 끝나는 년도일때
				else if(nyear == eyear) {
					//나머지달 (1월 ~ 마지막전달 까지)
					int last_month = emonth - 1;		//마지막 전달
					for(int m = 0; m <= last_month; m++) {
						calendar.set(nyear,m,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d32 = m + 1;
						//System.out.println(nyear + " : " + d32 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					}

					//마지막달 날자
					end_period += edate;
					int d31 = emonth + 1;
					//System.out.println(nyear + " : " + d31 + " : " + edate);
				}
				//3. 중간에 있는 년도일때
				else {
					for(int k=0; k<12; k++) {
						calendar.set(nyear,k,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d221 = k + 1;
						//System.out.println(nyear + " : " + d221 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					} //for
				} //if
			}//for
		} //if
		period = end_period;	//총일수
		return period;
	}
	/*--------------------------------------------------------------------------
	 * 시작(년월이)과 끝(년월일)을 비교하여 기간구하기 : 같은날자 포함(1일)
	 startDate : 시작 yyyymmdd or yyyy/mm/dd, endDate 끝 yyyymmdd or yyyy/mm/dd
	 --------------------------------------------------------------------------*/
	 public int getPeriodDate(String startDate, String endDate)
	{
		//변수선언
		int period = 0;				//기간 리턴값
		int syear=0,smonth=0,sdate=0,eyear=0,emonth=0,edate=0;

		//입력된 날자를 int로 바꾸기
		if((startDate.length() == 8) && (endDate.length() == 8)) {
			syear = Integer.parseInt(startDate.substring(0,4));
			smonth = Integer.parseInt(startDate.substring(4,6));
			sdate = Integer.parseInt(startDate.substring(6,8));
			eyear = Integer.parseInt(endDate.substring(0,4));
			emonth = Integer.parseInt(endDate.substring(4,6));
			edate = Integer.parseInt(endDate.substring(6,8));
		}
		else if((startDate.length() == 10) && (endDate.length() == 10)) {
			syear = Integer.parseInt(startDate.substring(0,4));
			smonth = Integer.parseInt(startDate.substring(5,7));
			sdate = Integer.parseInt(startDate.substring(8,10));
			eyear = Integer.parseInt(endDate.substring(0,4));
			emonth = Integer.parseInt(endDate.substring(5,7));
			edate = Integer.parseInt(endDate.substring(8,10));
		}
		else {
			return period;
		}
	
		//초기화 보상
		smonth = smonth - 1;		//시작하는 달 (달은 0부터 시작됨)
		emonth = emonth - 1;		//끝나는 달 (달의 시작은 0부터)
		Calendar calendar = new GregorianCalendar();

		int end_period = 0;						//총기간일자
		//1. 시작년도와 끝나는 년도가 같을때
		if(syear == eyear) {
			//년월일이 같으면
			if((smonth == emonth) && (sdate == edate)) {
				end_period = 0;
			} 
			//년월이 같으면
			else if((smonth == emonth) && (sdate != edate)){
				end_period = edate - sdate;
			} else {
				int dif = emonth - smonth;		//몇달 차이인가
				for(int i=0; i<=dif; i++) {
					int nmonth = smonth + i;	//1더한 새로운 달
					//1더한달이 시작달과 같을때
					if(nmonth == smonth) {								//시작달 남은 일수
						calendar.set(syear,smonth,sdate);
						int start_period = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);		
						end_period += start_period - sdate;
						int d1 = nmonth + 1;	int dc = start_period - sdate;
						//System.out.println(syear + " : " + d1 + " : " + dc);
					}
					//1더한달이 끝나는 달과 같을때
					else if(nmonth == emonth) {						//끝나는달 일수
						end_period += edate;
						int d2 = emonth + 1;
						//System.out.println(syear + " : " + d2 + " : " + edate);
					} 
					//1더한달이 중간에 있는달
					else {											//시작과 끝달 사이의 달 총일수
						calendar.set(eyear,nmonth,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d3 = i + 1;
						//System.out.println(syear + " : " + d3 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					} //if
				} //for
			}
		}
		//2. 시작년도와 끝나는 년도가 다를때
		else {
			for(int n = syear; n <=eyear; n++) { 
				int nyear = n;
				//1. 시작년도 일때
				if(nyear == syear) {
					//시작달 날짜
					calendar.set(nyear,smonth,sdate);
					int str_period = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					end_period += str_period - sdate;
					int d21 = smonth + 1;
					//System.out.println(nyear + " : " + d21 + " : " + end_period);		
					
					//나머지 달 (시작달 다음달 ~ 12월까지)
					for(int j=smonth+1; j<12; j++) {									
						calendar.set(nyear,j,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d22 = j + 1;
						//System.out.println(nyear + " : " + d22 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					} //for
				}
				//2. 끝나는 년도일때
				else if(nyear == eyear) {
					//나머지달 (1월 ~ 마지막전달 까지)
					int last_month = emonth - 1;		//마지막 전달
					for(int m = 0; m <= last_month; m++) {
						calendar.set(nyear,m,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d32 = m + 1;
						//System.out.println(nyear + " : " + d32 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					}

					//마지막달 날자
					end_period += edate;
					int d31 = emonth + 1;
					//System.out.println(nyear + " : " + d31 + " : " + edate);
				}
				//3. 중간에 있는 년도일때
				else {
					for(int k=0; k<12; k++) {
						calendar.set(nyear,k,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d221 = k + 1;
						//System.out.println(nyear + " : " + d221 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					} //for
				} //if
			}//for
		} //if
		period = end_period+1;	//총일수
		return period;
	}
	/*--------------------------------------------------------------------------
	 * 주어진 년월일로 최대날자수 구하기
	 syear/smonth/sdate : 년/월/일
	 --------------------------------------------------------------------------*/
	 public int getDateMaximum(int syear,int smonth,int sdate)
	{
		smonth = smonth - 1;		//끝나는 달 (달의 시작은 0부터)
		Calendar calendar = new GregorianCalendar();
		calendar.set(syear,smonth,sdate);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/*--------------------------------------------------------------------------
	 * 주어진 년월일로 남은날자수 구하기 : 현재일기준
	 syear/smonth/sdate : 년/월/일
	 --------------------------------------------------------------------------*/
	 public int getDatePeriod(int syear,int smonth,int sdate)
	{
		smonth = smonth - 1;		//끝나는 달 (달의 시작은 0부터)
		Calendar calendar = new GregorianCalendar();
		calendar.set(syear,smonth,sdate);
		int m = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int r = m - sdate + 1;
		return r;
	}

	/***************************************************************************
	 * 		기타 utility 사용
	 **************************************************************************/	
	public String getID()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("000");	
		String ID = y + fmt.toDigits(Integer.parseInt(s));
		
		return ID;
	}
	//--------------------------------------------------
	// 난수 발생 (19자리) 
	//--------------------------------------------------
	public String getMID()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("000");	
		com.anbtech.util.normalFormat ram = new com.anbtech.util.normalFormat("0000");	
		String ID = y + fmt.toDigits(Integer.parseInt(s))+ram.toDigits((int)(Math.random()*3000));
		
		return ID;
	}
	
	//--------------------------------------------------
	//주어진 월만큼더한 ID을 구한다. : 양수(+), 음수(-) 사용가능 
	//--------------------------------------------------
	public String getID(int date)
	{ 	
		//주어진 날자 만큼 더한다.
		Calendar calendar = new GregorianCalendar();
		calendar.add(calendar.DATE,date);

		//현재의 날자를 구한다.
		Date now = calendar.getTime();
				
		//String 리턴한다.
		SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
		String request_date = vans.format(now)+"0000001";
		return request_date;
	}

	//--------------------------------------------------
	//주어진 num을 반영하여 구하기 : 양수(+), 음수(-) 사용가능 
	//--------------------------------------------------
	public String getNumID(int num)
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("000");	
		String ID = y + fmt.toDigits(Integer.parseInt(s)+num);
		
		return ID;
	}

	/*--------------------------------------------------------------------------
	 * 주어진 날자를 년/월/일로 구분하기 
	 * 입력값:yyyyMMdd => format대로
	 --------------------------------------------------------------------------*/
	 public String getSepDate(String in_date,String format)
	{
		 String rtn = in_date;
		if(in_date.length() != 8) return rtn;

		String yy="",mm="",dd="";
		yy = in_date.substring(0,4);
		mm = in_date.substring(4,6);
		dd = in_date.substring(6,8);
		rtn = yy+format+mm+format+dd;
		return rtn;
	}
}