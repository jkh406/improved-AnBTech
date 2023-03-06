package com.anbtech.date;
import java.util.*;
import java.text.*;

public class anbDate
{
	
	/***************************************************************************
	 * ������ 
	 **************************************************************************/
	public anbDate()
	{
		
	}	

	/***************************************************************************
	 * 			���� ���ϱ� 
	 **************************************************************************/	 			
	/*-------------------------------------------------------------------------
	 * ��������� �����ϴ� �޼ҵ� (�Է�: yyyy, mm, dd)
	 * 1:�� 2:�� 3:ȭ 4:�� 5:�� 6:�� 7:��  
	 --------------------------------------------------------------------------*/
	public int getDay(int year,int month,int date)
	{
		GregorianCalendar gc=new GregorianCalendar(year, month -1 , date);
		return gc.get(gc.DAY_OF_WEEK); 
	}	


	/***************************************************************************
	 * 			������ ���ϱ� (�ְ�����)
	 **************************************************************************/
	/*-------------------------------------------------------------------------
	 * ����� ������ ������������ �����ϴ� �޼ҵ� (�Է�: yyyy, mm, dd)
	 * return : 1 ~ 53���� / �� 
	 --------------------------------------------------------------------------*/
	public int getWeekOfYear(int year,int month,int date)
	{
		GregorianCalendar gc=new GregorianCalendar(year, month -1 , date);
		return gc.get(gc.WEEK_OF_YEAR); 
	}	

	/*-------------------------------------------------------------------------
	 * �ޱ��� ������ ������������ �����ϴ� �޼ҵ� (�Է�: yyyy, mm, dd)
	 * return : 1 ~ 6���� / �� 
	 --------------------------------------------------------------------------*/
	public int getWeekOfMonth(int year,int month,int date)
	{
		GregorianCalendar gc=new GregorianCalendar(year, month -1 , date);
		return gc.get(gc.WEEK_OF_MONTH); 
	}	


	/***************************************************************************
	 * 		���� ���ϱ� (��-��-��)
	 **************************************************************************/
	/*--------------------------------------------------------------------------
	 * ���� ���� (��-��-��)
	 --------------------------------------------------------------------------*/
	public String getDate()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("yyyy-MM-dd");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * ���� �⵵ ���ϱ� 
	 --------------------------------------------------------------------------*/
	public String getYear()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("yyyy");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * ���� �� ���ϱ�  
	 --------------------------------------------------------------------------*/
	public String getMonth()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("MM");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * ���� �� ���ϱ� 
	 --------------------------------------------------------------------------*/
	public String getDates()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("dd");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * ���Ͽ��� ���� ���ϱ� 
	 * ����      : ���� ���� (��,��,��)
	 * ���� ���� : date
	 --------------------------------------------------------------------------*/
	public String getDate(int date)
	{

		//�־��� ���� ��ŭ ���Ѵ�.
		Calendar calendar = new GregorianCalendar();
		calendar.add(calendar.DATE,date);

		//������ ���ڸ� ���Ѵ�.
		Date now = calendar.getTime();
				
		//String �����Ѵ�.
		SimpleDateFormat vans = new SimpleDateFormat("yyyy/MM/dd");
		String request_date = vans.format(now);
		return request_date;
	}
		
	/*--------------------------------------------------------------------------
	 * ���Ͽ��� ���� ���ϱ� 
	 * ����      : �־��� ���� Setting (syear, smonth, sdate) 
	 * ���� ���� : date
	 --------------------------------------------------------------------------*/
	public String getDate(int syear,int smonth, int sdate, int date)
	{
		//�־��� ��,��,�Ϸ� ���� setting
		Calendar calendar = new GregorianCalendar();
		calendar.set(syear,smonth-1,sdate);
		
		//�־��� ���� ��ŭ ���Ѵ�.
		calendar.add(calendar.DATE,date);
		
		//������ ���ڸ� ���Ѵ�.
		Date now = calendar.getTime();
				
		//String �����Ѵ�.
		SimpleDateFormat vans = new SimpleDateFormat("yyyy/MM/dd");
		String request_date = vans.format(now);
		return request_date;		
	}
	/*--------------------------------------------------------------------------
	 * ���Ͽ��� ���� ���ϱ� 
	 * ����      : �־��� ���� Setting (yyyymmdd) 
	 * ���� ���� : date
	 --------------------------------------------------------------------------*/
	public String getDate(String ymd, int date, String outformat)
	{
		String request_date="";		//return date
		//�Է��� �˻�
		if(ymd.length() != 8) return request_date;

		//�־��� ���� ����
		int syear = Integer.parseInt(ymd.substring(0,4));
		int smonth = Integer.parseInt(ymd.substring(4,6));
		int sdate = Integer.parseInt(ymd.substring(6,8));
		
		//�־��� ��,��,�Ϸ� ���� setting
		Calendar calendar = new GregorianCalendar();
		calendar.set(syear,smonth-1,sdate);
		
		//�־��� ���� ��ŭ ���Ѵ�.
		calendar.add(calendar.DATE,date);
		
		//������ ���ڸ� ���Ѵ�.
		Date now = calendar.getTime();
				
		//String �����Ѵ�.
		String fmt = "yyyy"+outformat+"MM"+outformat+"dd";
		SimpleDateFormat vans = new SimpleDateFormat(fmt);
		request_date = vans.format(now);
		return request_date;		
	}
	
	/*--------------------------------------------------------------------------
	 * ����Ϸ� Setting�ϱ� 
	 * ����      : �־��� ���� Setting (syear, smonth, sdate) 
	 * retun  	 : Seting yyyy-MM-dd
	 --------------------------------------------------------------------------*/
	public String setDate(int syear,int smonth, int sdate)
	{
		//�־��� ��,��,�Ϸ� ���� setting
		Calendar calendar = new GregorianCalendar();
		calendar.set(syear,smonth-1,sdate);
		
		//������ ���ڸ� ���Ѵ�.
		Date now = calendar.getTime();
				
		//String �����Ѵ�.
		SimpleDateFormat vans = new SimpleDateFormat("yyyy/MM/dd");
		String request_date = vans.format(now);
		return request_date;		
	}	
	

	/***************************************************************************
	 * 		�ð� ���ϱ� (��-��-�� ��:��)
	 **************************************************************************/
	/*--------------------------------------------------------------------------
	 * ����ð��� �����ϴ� �޼ҵ�
	 --------------------------------------------------------------------------*/	
	public String getTime()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String request_date = vans.format(now);
		return request_date;
	}
		
	/*--------------------------------------------------------------------------
	 * ����ð��� �����ϴ� �޼ҵ�
	 --------------------------------------------------------------------------*/			
	public String getHours()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("HH");
		String request_date = vans.format(now);
		return request_date;
	}	
	
	/***************************************************************************
	 * 		Data������ ���� ���ϱ� (�����) : format�� ���� 
	 **************************************************************************/
	/*--------------------------------------------------------------------------
	 * ���糯���� �����ϴ� �޼ҵ�
	 --------------------------------------------------------------------------*/
	public String getDateNoformat()
	{
		Date now = new Date();
		SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * �־��� �⵵��ŭ ���Ͽ� �����ϴ� �޼ҵ�
	 --------------------------------------------------------------------------*/
	public String getAddYearNoformat(int year)
	{ 	
		//�־��� �⵵ ��ŭ ���Ѵ�.
		Calendar calendar = new GregorianCalendar();
		calendar.add(calendar.YEAR,year);

		//������ ���ڸ� ���Ѵ�.
		Date now = calendar.getTime();
				
		//String �����Ѵ�.
		SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * �־��� ����ŭ ���Ͽ� �����ϴ� �޼ҵ�
	 --------------------------------------------------------------------------*/
	public String getAddMonthNoformat(int month)
	{
		//�־��� �� ��ŭ ���Ѵ�.
		Calendar calendar = new GregorianCalendar();
		calendar.add(calendar.MONTH,month);

		//������ ���ڸ� ���Ѵ�.
		Date now = calendar.getTime();
				
		//String �����Ѵ�.
		SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
		String request_date = vans.format(now);
		return request_date;
	}	

	/*--------------------------------------------------------------------------
	 * �־��� ���� ��ŭ ���Ͽ� �����ϴ� �޼ҵ�
	 --------------------------------------------------------------------------*/
	public String getAddDateNoformat(int date)
	{
		//�־��� ���� ��ŭ ���Ѵ�.
		Calendar calendar = new GregorianCalendar();
		calendar.add(calendar.DATE,date);

		//������ ���ڸ� ���Ѵ�.
		Date now = calendar.getTime();
				
		//String �����Ѵ�.
		SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
		String request_date = vans.format(now);
		return request_date;
	}	
	/***************************************************************************
	 * 		���ڰ� ���� ���ϱ� (�Ⱓ ���ϱ�)
	 **************************************************************************/
	/*--------------------------------------------------------------------------
	 * ����(�����)�� ��(�����)�� ���Ͽ� �Ⱓ���ϱ� : �������� ������(0��)
	 syear/smonth/sdate : ���� ��/��/��, eyear/emonth/edate/ �� ��/��/��
	 --------------------------------------------------------------------------*/
	 public int getPeriodDate(int syear,int smonth,int sdate,int eyear,int emonth,int edate)
	{
		 int period = 0;			//�Ⱓ ���ϰ�
		 smonth = smonth - 1;		//�����ϴ� �� (���� 0���� ���۵�)
		 emonth = emonth - 1;		//������ �� (���� ������ 0����)
		 Calendar calendar = new GregorianCalendar();

		int end_period = 0;						//�ѱⰣ����
		//1. ���۳⵵�� ������ �⵵�� ������
		if(syear == eyear) {
			//������� ������
			if((smonth == emonth) && (sdate == edate)) {
				end_period = 0;
			} 
			//����� ������
			else if((smonth == emonth) && (sdate != edate)){
				end_period = edate - sdate;
			} else {
				int dif = emonth - smonth;		//��� �����ΰ�
				for(int i=0; i<=dif; i++) {
					int nmonth = smonth + i;	//1���� ���ο� ��
					//1���Ѵ��� ���۴ް� ������
					if(nmonth == smonth) {								//���۴� ���� �ϼ�
						calendar.set(syear,smonth,sdate);
						int start_period = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);		
						end_period += start_period - sdate;
						int d1 = nmonth + 1;	int dc = start_period - sdate;
						//System.out.println(syear + " : " + d1 + " : " + dc);
					}
					//1���Ѵ��� ������ �ް� ������
					else if(nmonth == emonth) {						//�����´� �ϼ�
						end_period += edate;
						int d2 = emonth + 1;
						//System.out.println(syear + " : " + d2 + " : " + edate);
					} 
					//1���Ѵ��� �߰��� �ִ´�
					else {											//���۰� ���� ������ �� ���ϼ�
						calendar.set(eyear,nmonth,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d3 = i + 1;
						//System.out.println(syear + " : " + d3 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					} //if
				} //for
			}
		}
		//2. ���۳⵵�� ������ �⵵�� �ٸ���
		else {
			for(int n = syear; n <=eyear; n++) { 
				int nyear = n;
				//1. ���۳⵵ �϶�
				if(nyear == syear) {
					//���۴� ��¥
					calendar.set(nyear,smonth,sdate);
					int str_period = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					end_period += str_period - sdate;
					int d21 = smonth + 1;
					//System.out.println(nyear + " : " + d21 + " : " + end_period);		
					
					//������ �� (���۴� ������ ~ 12������)
					for(int j=smonth+1; j<12; j++) {									
						calendar.set(nyear,j,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d22 = j + 1;
						//System.out.println(nyear + " : " + d22 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					} //for
				}
				//2. ������ �⵵�϶�
				else if(nyear == eyear) {
					//�������� (1�� ~ ���������� ����)
					int last_month = emonth - 1;		//������ ����
					for(int m = 0; m <= last_month; m++) {
						calendar.set(nyear,m,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d32 = m + 1;
						//System.out.println(nyear + " : " + d32 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					}

					//�������� ����
					end_period += edate;
					int d31 = emonth + 1;
					//System.out.println(nyear + " : " + d31 + " : " + edate);
				}
				//3. �߰��� �ִ� �⵵�϶�
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
		period = end_period;	//���ϼ�
		return period;
	}
	/*--------------------------------------------------------------------------
	 * ����(�����)�� ��(�����)�� ���Ͽ� �Ⱓ���ϱ� : �������� ����(1��)
	 startDate : ���� yyyymmdd or yyyy/mm/dd, endDate �� yyyymmdd or yyyy/mm/dd
	 --------------------------------------------------------------------------*/
	 public int getPeriodDate(String startDate, String endDate)
	{
		//��������
		int period = 0;				//�Ⱓ ���ϰ�
		int syear=0,smonth=0,sdate=0,eyear=0,emonth=0,edate=0;

		//�Էµ� ���ڸ� int�� �ٲٱ�
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
	
		//�ʱ�ȭ ����
		smonth = smonth - 1;		//�����ϴ� �� (���� 0���� ���۵�)
		emonth = emonth - 1;		//������ �� (���� ������ 0����)
		Calendar calendar = new GregorianCalendar();

		int end_period = 0;						//�ѱⰣ����
		//1. ���۳⵵�� ������ �⵵�� ������
		if(syear == eyear) {
			//������� ������
			if((smonth == emonth) && (sdate == edate)) {
				end_period = 0;
			} 
			//����� ������
			else if((smonth == emonth) && (sdate != edate)){
				end_period = edate - sdate;
			} else {
				int dif = emonth - smonth;		//��� �����ΰ�
				for(int i=0; i<=dif; i++) {
					int nmonth = smonth + i;	//1���� ���ο� ��
					//1���Ѵ��� ���۴ް� ������
					if(nmonth == smonth) {								//���۴� ���� �ϼ�
						calendar.set(syear,smonth,sdate);
						int start_period = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);		
						end_period += start_period - sdate;
						int d1 = nmonth + 1;	int dc = start_period - sdate;
						//System.out.println(syear + " : " + d1 + " : " + dc);
					}
					//1���Ѵ��� ������ �ް� ������
					else if(nmonth == emonth) {						//�����´� �ϼ�
						end_period += edate;
						int d2 = emonth + 1;
						//System.out.println(syear + " : " + d2 + " : " + edate);
					} 
					//1���Ѵ��� �߰��� �ִ´�
					else {											//���۰� ���� ������ �� ���ϼ�
						calendar.set(eyear,nmonth,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d3 = i + 1;
						//System.out.println(syear + " : " + d3 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					} //if
				} //for
			}
		}
		//2. ���۳⵵�� ������ �⵵�� �ٸ���
		else {
			for(int n = syear; n <=eyear; n++) { 
				int nyear = n;
				//1. ���۳⵵ �϶�
				if(nyear == syear) {
					//���۴� ��¥
					calendar.set(nyear,smonth,sdate);
					int str_period = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					end_period += str_period - sdate;
					int d21 = smonth + 1;
					//System.out.println(nyear + " : " + d21 + " : " + end_period);		
					
					//������ �� (���۴� ������ ~ 12������)
					for(int j=smonth+1; j<12; j++) {									
						calendar.set(nyear,j,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d22 = j + 1;
						//System.out.println(nyear + " : " + d22 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					} //for
				}
				//2. ������ �⵵�϶�
				else if(nyear == eyear) {
					//�������� (1�� ~ ���������� ����)
					int last_month = emonth - 1;		//������ ����
					for(int m = 0; m <= last_month; m++) {
						calendar.set(nyear,m,1);
						end_period += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int d32 = m + 1;
						//System.out.println(nyear + " : " + d32 + " : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					}

					//�������� ����
					end_period += edate;
					int d31 = emonth + 1;
					//System.out.println(nyear + " : " + d31 + " : " + edate);
				}
				//3. �߰��� �ִ� �⵵�϶�
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
		period = end_period+1;	//���ϼ�
		return period;
	}
	/*--------------------------------------------------------------------------
	 * �־��� ����Ϸ� �ִ볯�ڼ� ���ϱ�
	 syear/smonth/sdate : ��/��/��
	 --------------------------------------------------------------------------*/
	 public int getDateMaximum(int syear,int smonth,int sdate)
	{
		smonth = smonth - 1;		//������ �� (���� ������ 0����)
		Calendar calendar = new GregorianCalendar();
		calendar.set(syear,smonth,sdate);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/*--------------------------------------------------------------------------
	 * �־��� ����Ϸ� �������ڼ� ���ϱ� : �����ϱ���
	 syear/smonth/sdate : ��/��/��
	 --------------------------------------------------------------------------*/
	 public int getDatePeriod(int syear,int smonth,int sdate)
	{
		smonth = smonth - 1;		//������ �� (���� ������ 0����)
		Calendar calendar = new GregorianCalendar();
		calendar.set(syear,smonth,sdate);
		int m = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int r = m - sdate + 1;
		return r;
	}

	/***************************************************************************
	 * 		��Ÿ utility ���
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
	// ���� �߻� (19�ڸ�) 
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
	//�־��� ����ŭ���� ID�� ���Ѵ�. : ���(+), ����(-) ��밡�� 
	//--------------------------------------------------
	public String getID(int date)
	{ 	
		//�־��� ���� ��ŭ ���Ѵ�.
		Calendar calendar = new GregorianCalendar();
		calendar.add(calendar.DATE,date);

		//������ ���ڸ� ���Ѵ�.
		Date now = calendar.getTime();
				
		//String �����Ѵ�.
		SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
		String request_date = vans.format(now)+"0000001";
		return request_date;
	}

	//--------------------------------------------------
	//�־��� num�� �ݿ��Ͽ� ���ϱ� : ���(+), ����(-) ��밡�� 
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
	 * �־��� ���ڸ� ��/��/�Ϸ� �����ϱ� 
	 * �Է°�:yyyyMMdd => format���
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