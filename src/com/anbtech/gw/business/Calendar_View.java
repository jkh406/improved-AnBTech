package com.anbtech.gw.business;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import com.anbtech.BoardListBean;

public class Calendar_View
{
	// Database Wrapper Class 선언
	private BoardListBean bean = null;	
		
	private String query = "";			//query문장 만들기
	private String[] indColumns = {"pid","id","myear","mmonth","mday","eyear","emonth","eday","mtime","item","isopen"};

	/***************************************************************************
	 * 생성자 메소드
	 **************************************************************************/
	public Calendar_View() 
	{	
		bean = new BoardListBean();
	}
	
	/***************************************************************************
	 * 개인 일정 구하기 
	 **************************************************************************/	
	public String Owner_View(String rid,String ryear,String rmonth) throws SQLException
	{	
		String Schedule_items = "";
		
		//1.동일한 년/월에 해당되는 경우
		Schedule_items = getOwnerMonth(rid,ryear,rmonth);
		
		//2.동일한 년에 다른월에 걸쳐있는 경우
		Schedule_items += getOwnerDiffMonth(rid,ryear,rmonth);
		
		//3.다른 년도에 걸쳐있는 경우
		Schedule_items += getOwnerDiffYear(rid,ryear,rmonth);
		
		return Schedule_items;	
	}
	
	/***************************************************************************
	 * Others(공유자,회사,부서) 일정 구하기
	 * Owner와 동일 (단,공개된 경우만 출력함) 
	 * type : 개인일정(INI), 회사일정(COM), 부서일정(DIV)
	 **************************************************************************/	
	public String Other_View(String rid,String ryear,String rmonth,String type) throws SQLException
	{	
		String Schedule_items = "";
		
		//1.동일한 년/월에 해당되는 경우
		Schedule_items = getOtherMonth(rid,ryear,rmonth,type);
		
		//2.동일한 년에 다른월에 걸쳐있는 경우
		Schedule_items += getOtherDiffMonth(rid,ryear,rmonth,type);
		
		//3.다른 년도에 걸쳐있는 경우
		Schedule_items += getOtherDiffYear(rid,ryear,rmonth,type);
		
		return Schedule_items;		
	}
	
	/***************************************************************************
	 * 개인 일정 : 1.동일한 년/월에 해당되는 경우 
	 **************************************************************************/	
	public String getOwnerMonth(String rid,String ryear,String rmonth) throws SQLException
	{	
		String cal_dl = "";
		String rYM = "";		//요청한 yyyyMM구하기 
		if((ryear != null) && (rmonth != null)) rYM = ryear+rmonth;
		
		bean.setTable("CALENDAR_SCHEDULE");
		bean.setColumns(indColumns);
		bean.setOrder("mtime ASC");
		bean.setClear();
		bean.setSearch("id",rid,"myear",ryear,"mmonth",rmonth);
		bean.init_unique();	
		
		while(bean.isAll()) {
			String sch_date = bean.getData("myear") + "/" + bean.getData("mmonth") + "/" + bean.getData("mday");
			String sch_file = "8A2329406E";
			String sch_time = bean.getData("mtime");			//시간
			String sch_item = bean.getData("item");				//항목

			String ISOPEN = bean.getData("isopen");				//공개 미공개
			String YN = "";
			if(ISOPEN.equals("0")) YN = "N";
			else YN = "Y";

			String PID = bean.getData("pid");					//관리번호
		
			//기간(2일이상의 일정) 해당일에 모두 표시하기
			String tY = bean.getData("myear");
			String tM = bean.getData("mmonth"); if(tM.length() == 1) tM = "0" + tM;
			String tD = bean.getData("mday");	if(tD.length() == 1) tD = "0" + tD;
			String tYMD = tY+tM+tD;
			int tymd = 0;
			if((tYMD != null) && (tYMD.length() != 0))	tymd = Integer.parseInt(tYMD);	//시작년월일

			String nY = bean.getData("eyear");
			String nM = bean.getData("emonth"); if(nM.length() == 1) nM = "0" + nM;
			String nD = bean.getData("eday");	if(nD.length() == 1) nD = "0" + nD;
			String nYMD = nY+nM+nD;
			int nymd = 0;
			if((nYMD != null) && (nYMD.length() != 0))	nymd = Integer.parseInt(nYMD);	//종료년월일

			//주어진 일자로 현재일자를 셋팅하여 기간일자을 모두표시한다. 
			int syr = Integer.parseInt(bean.getData("myear"));
			int smn = Integer.parseInt(bean.getData("mmonth"));
			int sde = Integer.parseInt(bean.getData("mday"));

			//시작일과 종료일이 있는경우
			if((tymd != 0) && (nymd != 0)) {
				int diff = nymd - tymd;				//차이
				
				//당일일 경우 
				if(diff == 0) {
					cal_dl += sch_date + "*" + sch_file + "@Calendar_View@" + YN + "@" + sch_item + "(" + sch_time + ")" + "@" + PID + "@" + ";";	
				//기간일 경우 
				} else {
					//기간만큼을 출력을 더한다.
					for(int wi = 0; wi <= diff; wi++) {
						String chkymd = getDate(syr,smn,sde,wi);		//1일더하여지 날자
						String chkYM = chkymd.substring(0,6);			//년월 (yyyyMM)
						
						//요청한 년월만 출력한다.
						if(rYM.equals(chkYM)) {				
							sch_date = chkymd.substring(0,4)+"/"+chkymd.substring(4,6)+"/"+chkymd.substring(6,8);
							if((wi != 0) && (nYMD.equals(chkymd))) {		//마지막 일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"(["+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
								break;
							} else if((wi == 0) && (!nYMD.equals(chkymd))) { //처음일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
							} else {										//중간 일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+bean.getData("mmonth")+"/"+bean.getData("mday")+"~"+bean.getData("emonth")+"/"+bean.getData("eday")+")"+"@"+PID+"@"+";";
							} 
						}//if(요청한 년월만 출력)
					} //for
				} //if 
			//시작일만 있는경우
			} else {
				cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
			}	

		} //while
		return cal_dl;
	}
	
	/***************************************************************************
	 * 개인 일정 : 2.(기간)동일한 년에 다른월에 걸쳐있는 경우
	 **************************************************************************/	
	public String getOwnerDiffMonth(String rid,String ryear,String rmonth) throws SQLException
	{	
		String cal_dl = "";
		String rYM = "";		//요청한 yyyyMM구하기 
		if((ryear != null) && (rmonth != null)) rYM = ryear+rmonth;
		
		int qyear = Integer.parseInt(ryear);			//query할 년도
		int qmonth = Integer.parseInt(rmonth);			//query할 월
		
		query = "where id='"+rid+"' and ("+qyear+" = myear and eyear = "+qyear+" and eyear != 0) and ";
		query += "(mmonth <= "+qmonth+" and "+qmonth+" <= emonth) order by mtime ASC";
		
		bean.setTable("CALENDAR_SCHEDULE");
		bean.setColumns(indColumns);
		bean.setSearchWrite(query);
		bean.init_write();
		
		while(bean.isAll()) {
			String sch_date = bean.getData("myear") + "/" + bean.getData("mmonth") + "/" + bean.getData("mday");
			String sch_file = "8A2329406E";
			String sch_item = bean.getData("item");				//항목
			String sch_time = bean.getData("mtime");			//시간

			String ISOPEN = bean.getData("isopen");				//공개 미공개
			String YN = "";
			if(ISOPEN.equals("0")) YN = "N";
			else YN = "Y";

			String PID = bean.getData("pid");					//관리번호
		
			//기간(2일이상의 일정) 해당일에 모두 표시하기
			String tY = bean.getData("myear");
			String tM = bean.getData("mmonth"); if(tM.length() == 1) tM = "0" + tM;
			String tD = bean.getData("mday");	if(tD.length() == 1) tD = "0" + tD;
			String tYMD = tY+tM+tD;
			int tymd = 0;
			if((tYMD != null) && (tYMD.length() != 0))	tymd = Integer.parseInt(tYMD);	//시작년월일

			String nY = bean.getData("eyear");
			String nM = bean.getData("emonth"); if(nM.length() == 1) nM = "0" + nM;
			String nD = bean.getData("eday");	if(nD.length() == 1) nD = "0" + nD;
			String nYMD = nY+nM+nD;
			int nymd = 0;
			if((nYMD != null) && (nYMD.length() != 0))	nymd = Integer.parseInt(nYMD);	//종료년월일

			//주어진 일자로 현재일자를 셋팅하여 기간일자을 모두표시한다. 
			int syr = Integer.parseInt(bean.getData("myear"));
			int smn = Integer.parseInt(bean.getData("mmonth"));
			int sde = Integer.parseInt(bean.getData("mday"));

			//시작일과 종료일이 있는경우
			if((tymd != 0) && (nymd != 0)) {
				int diff = nymd - tymd;				//차이
				
				//기간일 경우만 출력한다.
				if(diff != 0) {
					//기간만큼을 출력을 더한다.
					for(int wi = 0; wi <= diff; wi++) {
						String chkymd = getDate(syr,smn,sde,wi);				//1일더하여지 날자
						String chkYM = chkymd.substring(0,6);					//년월 (yyyyMM)
						String smonth = chkymd.substring(4,6);					//달 
						
						//요청한 년월만 출력한다.
						//일정시작달과 같은달은 제외 (위에서 출력함)	
						if(!tM.equals(smonth) && (rYM.equals(chkYM))) {	
							sch_date = chkymd.substring(0,4)+"/"+chkymd.substring(4,6)+"/"+chkymd.substring(6,8);
							if((wi != 0) && (nYMD.equals(chkymd))) {		//마지막 일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"(["+bean.getData("mmonth")+"/"+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
								break;
							} else if((wi == 0) && (!nYMD.equals(chkymd))) { //처음일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
							} else {										//중간 일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+bean.getData("mmonth")+"/"+bean.getData("mday")+"~"+bean.getData("emonth")+"/"+bean.getData("eday")+")"+"@"+PID+"@"+";";
							} 
						} //if (같은달 제외)
					} //for
				} //if 
			} //if	

		} //while
		return cal_dl;
	}
	
	/***************************************************************************
	 * 개인 일정 : 3.(기간)다른 년도에 걸쳐있는 경우
	 **************************************************************************/	
	public String getOwnerDiffYear(String rid,String ryear,String rmonth) throws SQLException
	{	
		String cal_dl = "";
		String rYM = "";		//요청한 yyyyMM구하기 
		if((ryear != null) && (rmonth != null)) rYM = ryear+rmonth;
		
		int qyear = Integer.parseInt(ryear);			//query할 년도
		int qmonth = Integer.parseInt(rmonth);			//query할 월
		
		int qbyear = qyear - 1;	//1년전
		query = "where id='"+rid+"' and ("+qbyear+" = myear and eyear = "+qyear+" and eyear != 0) and ";
		query += "("+qmonth+" <= emonth) order by mtime ASC";
		
		bean.setTable("CALENDAR_SCHEDULE");
		bean.setColumns(indColumns);
		bean.setSearchWrite(query);
		bean.init_write();
		
		while(bean.isAll()) {
			String sch_date = bean.getData("myear") + "/" + bean.getData("mmonth") + "/" + bean.getData("mday");
			String sch_file = "8A2329406E";
			String sch_item = bean.getData("item");				//항목
			String sch_time = bean.getData("mtime");			//시간

			String ISOPEN = bean.getData("isopen");				//공개 미공개
			String YN = "";
			if(ISOPEN.equals("0")) YN = "N";
			else YN = "Y";

			String PID = bean.getData("pid");					//관리번호
		
			//기간(2일이상의 일정) 해당일에 모두 표시하기
			String tY = bean.getData("myear");
			String tM = bean.getData("mmonth"); if(tM.length() == 1) tM = "0" + tM;
			String tD = bean.getData("mday");	if(tD.length() == 1) tD = "0" + tD;
			String tYMD = tY+tM+tD;
			int tymd = 0;
			if((tYMD != null) && (tYMD.length() != 0))	tymd = Integer.parseInt(tYMD);	//시작년월일

			String nY = bean.getData("eyear");
			String nM = bean.getData("emonth"); if(nM.length() == 1) nM = "0" + nM;
			String nD = bean.getData("eday");	if(nD.length() == 1) nD = "0" + nD;
			String nYMD = nY+nM+nD;
			int nymd = 0;
			if((nYMD != null) && (nYMD.length() != 0))	nymd = Integer.parseInt(nYMD);	//종료년월일

			//주어진 일자로 현재일자를 셋팅하여 기간일자을 모두표시한다. 
			int syr = Integer.parseInt(bean.getData("myear"));
			int smn = Integer.parseInt(bean.getData("mmonth"));
			int sde = Integer.parseInt(bean.getData("mday"));

			//시작일과 종료일이 있는경우
			if((tymd != 0) && (nymd != 0)) {
				int diff = nymd - tymd;				//차이
				
				//기간일 경우만 출력한다. 
				if(diff!= 0) {
					//기간만큼을 출력을 더한다.
					for(int wi = 0; wi <= diff; wi++) {
						String chkymd = getDate(syr,smn,sde,wi);		//1일더하여지 날자
						String chkYM = chkymd.substring(0,6);			//년월 (yyyyMM)
						
						//요청한 년월만 출력한다.
						if(rYM.equals(chkYM)) {	
							sch_date = chkymd.substring(0,4)+"/"+chkymd.substring(4,6)+"/"+chkymd.substring(6,8);
							if((wi != 0) && (nYMD.equals(chkymd))) {		//마지막 일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"(["+bean.getData("mmonth")+"/"+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
								break;
							} else if((wi == 0) && (!nYMD.equals(chkymd))) { //처음일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
							} else {										//중간 일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+bean.getData("mmonth")+"/"+bean.getData("mday")+"~"+bean.getData("emonth")+"/"+bean.getData("eday")+")"+"@"+PID+"@"+";";
							} 
						} //if 요청한 년월만 출력
					} //for
				} //if 
			} 	
		} //while
		return cal_dl;
	}
	/***************************************************************************
	 * Others 일정 : 1.동일한 년/월에 해당되는 경우 
	 * type : 개인일정(INI), 회사일정(COM), 부서일정(DIV)
	 **************************************************************************/	
	public String getOtherMonth(String rid,String ryear,String rmonth,String type) throws SQLException
	{	
		String cal_dl = "";				//return값 
		String rYM = "";				//요청한 yyyyMM구하기 
		if((ryear != null) && (rmonth != null)) rYM = ryear+rmonth;
		
		String html_fs = "";			//html로 일정 구분 
		if(type == null) type = "";
		else {
			if(type.equals("COM")) html_fs = "<font color='#CC2900'><b>(C)</b></font>";
			else if(type.equals("DIV")) html_fs = "<font color='A300CC'><b>(G)</b></font>";  	
		}
		
		bean.setTable("CALENDAR_SCHEDULE");
		bean.setColumns(indColumns);
		bean.setOrder("mtime ASC");
		bean.setClear();
		bean.setSearch("id",rid,"myear",ryear,"mmonth",rmonth,"isopen","1");
		bean.init_unique();
	
		while(bean.isAll()) {
			String sch_date = bean.getData("myear") + "/" + bean.getData("mmonth") + "/" + bean.getData("mday");
			String sch_file = "8A2329406E";
			String sch_item = bean.getData("item");				//항목
			String sch_time = bean.getData("mtime");			//시간

			String ISOPEN = bean.getData("isopen");				//공개 미공개
			String YN = "";
			if(ISOPEN.equals("0")) YN = "N";
			else YN = "Y";

			String PID = bean.getData("pid");					//관리번호
		
			//기간(2일이상의 일정) 해당일에 모두 표시하기
			String tY = bean.getData("myear");
			String tM = bean.getData("mmonth"); if(tM.length() == 1) tM = "0" + tM;
			String tD = bean.getData("mday");	if(tD.length() == 1) tD = "0" + tD;
			String tYMD = tY+tM+tD;
			int tymd = 0;
			if((tYMD != null) && (tYMD.length() != 0))	tymd = Integer.parseInt(tYMD);	//시작년월일

			String nY = bean.getData("eyear");
			String nM = bean.getData("emonth"); if(nM.length() == 1) nM = "0" + nM;
			String nD = bean.getData("eday");	if(nD.length() == 1) nD = "0" + nD;
			String nYMD = nY+nM+nD;
			int nymd = 0;
			if((nYMD != null) && (nYMD.length() != 0))	nymd = Integer.parseInt(nYMD);	//종료년월일

			//주어진 일자로 현재일자를 셋팅하여 기간일자을 모두표시한다. 
			int syr = Integer.parseInt(bean.getData("myear"));
			int smn = Integer.parseInt(bean.getData("mmonth"));
			int sde = Integer.parseInt(bean.getData("mday"));

			//시작일과 종료일이 있는경우
			if((tymd != 0) && (nymd != 0)) {
				int diff = nymd - tymd;				//차이
				
				//당일일 경우 
				if(diff == 0) {
					cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";	
				//기간일 경우 
				} else {
					//기간만큼을 출력을 더한다.
					for(int wi = 0; wi <= diff; wi++) {
						String chkymd = getDate(syr,smn,sde,wi);		//1일더하여지 날자
						String chkYM = chkymd.substring(0,6);			//년월 (yyyyMM)
				
						//요청한 년월만 출력한다.
						if(rYM.equals(chkYM)) {	
							sch_date = chkymd.substring(0,4)+"/"+chkymd.substring(4,6)+"/"+chkymd.substring(6,8);
							if((wi != 0) && (nYMD.equals(chkymd))) {		//마지막 일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"(["+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
								break;
							} else if((wi == 0) && (!nYMD.equals(chkymd))) { //처음일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
							} else {										//중간 일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+bean.getData("mmonth")+"/"+bean.getData("mday")+"~"+bean.getData("emonth")+"/"+bean.getData("eday")+")"+"@"+PID+"@"+";";
							} 
						} //if(요청한 년월만 출력)
					} //for
				} //if 
			//시작일만 있는경우
			} else {
				cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
			}	

		} //while
		return cal_dl;
	}
	
	/***************************************************************************
	 * Others 일정 : 2. (기간)동일한 년에 다른월에 걸쳐있는 경우
	 * type : 개인일정(INI), 회사일정(COM), 부서일정(DIV)
	 **************************************************************************/	
	public String getOtherDiffMonth(String rid,String ryear,String rmonth, String type) throws SQLException
	{	
		String cal_dl = "";				//return값 
		String rYM = "";				//요청한 yyyyMM구하기 
		if((ryear != null) && (rmonth != null)) rYM = ryear+rmonth;
		
		String html_fs = "";			//html로 일정 구분 
		if(type == null) type = "";
		else {
			if(type.equals("COM")) html_fs = "<font color='#CC2900'><b>(C)</b></font>";
			else if(type.equals("DIV")) html_fs = "<font color='A300CC'><b>(G)</b></font>";  	
		}
		
		int qyear = Integer.parseInt(ryear);			//query할 년도
		int qmonth = Integer.parseInt(rmonth);			//query할 월
				
		query = "where id='"+rid+"' and ("+qyear+" = myear and eyear = "+qyear+" and eyear != 0) and ";
		query += "(mmonth <= "+qmonth+" and "+qmonth+" <= emonth) and isopen='1' order by mtime ASC";
		
		bean.setTable("CALENDAR_SCHEDULE");
		bean.setColumns(indColumns);
		bean.setSearchWrite(query);
		bean.init_write();
		
		while(bean.isAll()) {
			String sch_date = bean.getData("myear") + "/" + bean.getData("mmonth") + "/" + bean.getData("mday");
			String sch_file = "8A2329406E";
			String sch_item = bean.getData("item");				//항목
			String sch_time = bean.getData("mtime");			//시간

			String ISOPEN = bean.getData("isopen");				//공개 미공개
			String YN = "";
			if(ISOPEN.equals("0")) YN = "N";
			else YN = "Y";

			String PID = bean.getData("pid");					//관리번호
		
			//기간(2일이상의 일정) 해당일에 모두 표시하기
			String tY = bean.getData("myear");
			String tM = bean.getData("mmonth"); if(tM.length() == 1) tM = "0" + tM;
			String tD = bean.getData("mday");	if(tD.length() == 1) tD = "0" + tD;
			String tYMD = tY+tM+tD;
			int tymd = 0;
			if((tYMD != null) && (tYMD.length() != 0))	tymd = Integer.parseInt(tYMD);	//시작년월일

			String nY = bean.getData("eyear");
			String nM = bean.getData("emonth"); if(nM.length() == 1) nM = "0" + nM;
			String nD = bean.getData("eday");	if(nD.length() == 1) nD = "0" + nD;
			String nYMD = nY+nM+nD;
			int nymd = 0;
			if((nYMD != null) && (nYMD.length() != 0))	nymd = Integer.parseInt(nYMD);	//종료년월일

			//주어진 일자로 현재일자를 셋팅하여 기간일자을 모두표시한다. 
			int syr = Integer.parseInt(bean.getData("myear"));
			int smn = Integer.parseInt(bean.getData("mmonth"));
			int sde = Integer.parseInt(bean.getData("mday"));

			//시작일과 종료일이 있는경우
			if((tymd != 0) && (nymd != 0)) {
				int diff = nymd - tymd;				//차이
				
				//기간일 경우만 출력한다. 
				if(diff!= 0) {
					//기간만큼을 출력을 더한다.
					for(int wi = 0; wi <= diff; wi++) {
						String chkymd = getDate(syr,smn,sde,wi);				//1일더하여지 날자
						String chkYM = chkymd.substring(0,6);					//년월 (yyyyMM)
						String smonth = chkymd.substring(4,6);					//달  
						sch_date = chkymd.substring(0,4)+"/"+chkymd.substring(4,6)+"/"+chkymd.substring(6,8);
						
						//요청한 년월만 출력한다.
						//일정시작달과 같은달은 제외 (위에서 출력함)	
						if(!tM.equals(smonth) && (rYM.equals(chkYM))) {	
							if((wi != 0) && (nYMD.equals(chkymd))) {		//마지막 일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"(["+bean.getData("mmonth")+"/"+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
								break;
							} else if((wi == 0) && (!nYMD.equals(chkymd))) { //처음일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
							} else {										//중간 일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+bean.getData("mmonth")+"/"+bean.getData("mday")+"~"+bean.getData("emonth")+"/"+bean.getData("eday")+")"+"@"+PID+"@"+";";
							} 
						} //같은달 
					} //for
				} //if 
			} 	

		} //while
		return cal_dl;
	}
	
	/***************************************************************************
	 * Other 일정 : 3.(기간)다른 년도에 걸쳐있는 경우
	 * type : 개인일정(INI), 회사일정(COM), 부서일정(DIV)
	 **************************************************************************/	
	public String getOtherDiffYear(String rid,String ryear,String rmonth,String type) throws SQLException
	{	
		String cal_dl = "";				//return값 
		String rYM = "";				//요청한 yyyyMM구하기 
		if((ryear != null) && (rmonth != null)) rYM = ryear+rmonth;
		
		String html_fs = "";			//html로 일정 구분 
		if(type == null) type = "";
		else {
			if(type.equals("COM")) html_fs = "<font color='#CC2900'><b>(C)</b></font>";
			else if(type.equals("DIV")) html_fs = "<font color='A300CC'><b>(G)</b></font>";  	
		}
		
		int qyear = Integer.parseInt(ryear);			//query할 년도
		int qmonth = Integer.parseInt(rmonth);			//query할 월
		
		int qbyear = qyear - 1;	//1년전
		
		query = "where id='"+rid+"' and ("+qbyear+" = myear and eyear = "+qyear+" and eyear != 0) and ";
		query += "("+qmonth+" <= emonth) and isopen='1' order by mtime ASC"; 
		
		bean.setTable("CALENDAR_SCHEDULE");
		bean.setColumns(indColumns);
		bean.setSearchWrite(query);
		bean.init_write();
		
		while(bean.isAll()) {
			String sch_date = bean.getData("myear") + "/" + bean.getData("mmonth") + "/" + bean.getData("mday");
			String sch_file = "8A2329406E";
			String sch_item = bean.getData("item");				//항목
			String sch_time = bean.getData("mtime");			//시간

			String ISOPEN = bean.getData("isopen");				//공개 미공개
			String YN = "";
			if(ISOPEN.equals("0")) YN = "N";
			else YN = "Y";

			String PID = bean.getData("pid");					//관리번호
		
			//기간(2일이상의 일정) 해당일에 모두 표시하기
			String tY = bean.getData("myear");
			String tM = bean.getData("mmonth"); if(tM.length() == 1) tM = "0" + tM;
			String tD = bean.getData("mday");	if(tD.length() == 1) tD = "0" + tD;
			String tYMD = tY+tM+tD;
			int tymd = 0;
			if((tYMD != null) && (tYMD.length() != 0))	tymd = Integer.parseInt(tYMD);	//시작년월일

			String nY = bean.getData("eyear");
			String nM = bean.getData("emonth"); if(nM.length() == 1) nM = "0" + nM;
			String nD = bean.getData("eday");	if(nD.length() == 1) nD = "0" + nD;
			String nYMD = nY+nM+nD;
			int nymd = 0;
			if((nYMD != null) && (nYMD.length() != 0))	nymd = Integer.parseInt(nYMD);	//종료년월일

			//주어진 일자로 현재일자를 셋팅하여 기간일자을 모두표시한다. 
			int syr = Integer.parseInt(bean.getData("myear"));
			int smn = Integer.parseInt(bean.getData("mmonth"));
			int sde = Integer.parseInt(bean.getData("mday"));

			//시작일과 종료일이 있는경우
			if((tymd != 0) && (nymd != 0)) {
				int diff = nymd - tymd;				//차이
				
				//기간일 경우만 출력한다. 
				if(diff!= 0) {
					//기간만큼을 출력을 더한다.
					for(int wi = 0; wi <= diff; wi++) {
						String chkymd = getDate(syr,smn,sde,wi);		//1일더하여지 날자
						String chkYM = chkymd.substring(0,6);			//년월 (yyyyMM)
						
						//요청한 년월만 출력한다.
						if(rYM.equals(chkYM)) {	
							sch_date = chkymd.substring(0,4)+"/"+chkymd.substring(4,6)+"/"+chkymd.substring(6,8);
							if((wi != 0) && (nYMD.equals(chkymd))) {		//마지막 일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"(["+bean.getData("mmonth")+"/"+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
								break;
							} else if((wi == 0) && (!nYMD.equals(chkymd))) { //처음일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
							} else {										//중간 일자 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"(["+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
							} 
						} //if(요청한 년월만 출력)
					} //for
				} //if 
			} 	

		} //while
		return cal_dl;
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
		java.util.Date now = calendar.getTime();
				
		//String 리턴한다.
		SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
		String request_date = vans.format(now);
		return request_date;		
	}

}