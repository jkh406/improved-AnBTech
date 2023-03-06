package com.anbtech.pjt.business;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.anbtech.date.anbDate;
import java.util.StringTokenizer;

public class pjtScheduleBO
{
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();	//날자계산

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public pjtScheduleBO() 
	{
		
	}

	//*******************************************************************
	//	일자중 가장작은일자 와 가장큰일자 을 구하기  : 배열의 갯수가 (n X 4)개일때
	//  일자형태 : yyyyMMdd
	//*******************************************************************/
	public String[][] searchFirstLastDate(String[][] data) throws Exception
	{
		String[][] FNL = new String[2][3];
		int psd=0,ped=0,csd=0,ced=0;
		int len = data.length;

		//기준값을 담는다.
		if(len > 0) {
			psd = Integer.parseInt(data[0][0]);	//계획 최초일
			ped = Integer.parseInt(data[0][1]);	//계획 최종일
			csd = Integer.parseInt(data[0][2]);	//수정 최초일
			ced = Integer.parseInt(data[0][3]);	//수정 최종일
		}
	
		//비교하여 최저값과 최고값을 구한다.
		for(int i=1; i<len; i++) {
			//계획일 구하기
			if(psd > Integer.parseInt(data[i][0])) psd = Integer.parseInt(data[i][0]);	//계획 가장 작은수
			if(ped < Integer.parseInt(data[i][1])) ped = Integer.parseInt(data[i][1]);	//계획 가장 큰수
			//수정일 구하기
			if(csd > Integer.parseInt(data[i][2])) csd = Integer.parseInt(data[i][2]);	//계획 가장 작은수
			if(ced < Integer.parseInt(data[i][3])) ced = Integer.parseInt(data[i][3]);	//계획 가장 큰수
		}

		//해당 내용을 배열에 담기, 단 값이 0인것은 ""으로 담는다.
		FNL[0][0] = Integer.toString(psd);		//계획 시작일
		FNL[0][1] = Integer.toString(ped);		//계획 종료일
		FNL[0][2] = "0";		//계획 일수
		FNL[1][0] = Integer.toString(csd);		//수정 시작일
		FNL[1][1] = Integer.toString(ced);		//수정 종료일
		FNL[1][2] = "0";		//수정 일수

		//계획 기간 일수 구하기 [휴일이 포함된 경우임]
		if(!FNL[0][0].equals("0") && !FNL[0][1].equals("0")) {
			int pn_cnt = anbdt.getPeriodDate(FNL[0][0],FNL[0][1]);
			FNL[0][2] = Integer.toString(pn_cnt);		//계획 일수
		}
		//수정 기간 일수 구하기 [휴일이 포함된 경우임]
		if(!FNL[1][0].equals("0") && !FNL[1][1].equals("0")) {
			int cn_cnt = anbdt.getPeriodDate(FNL[1][0],FNL[1][1]);
			FNL[1][2] = Integer.toString(cn_cnt);		//수정 일수
		}
		return FNL;			
	}

	//*******************************************************************
	//	일자중 가장작은일자 와 가장큰일자 을 구하기  : 배열의 갯수가 (n X 2)개일때
	//  일자형태 : yyyyMMdd
	//*******************************************************************/
	public String[][] completeFirstLastDate(String[][] data) throws Exception
	{
		String[][] FNL = new String[1][3];
		int rsd=0,red=0;
		int len = data.length;

		//기준값을 담는다.
		if(len > 0) {
			rsd = Integer.parseInt(data[0][0]);	//실적 최초일
			red = Integer.parseInt(data[0][1]);	//실적 최종일
		}

		//비교하여 최저값과 최고값을 구한다.
		for(int i=1; i<len; i++) {
			if((data[i][0] != null) || (data[i][1] != null)) {
				if(rsd > Integer.parseInt(data[i][0])) rsd = Integer.parseInt(data[i][0]);	//실적 가장 작은수
				if(red < Integer.parseInt(data[i][1])) red = Integer.parseInt(data[i][1]);	//실적 가장 큰수
			}
		}

		//해당 내용을 배열에 담기, 단 값이 0인것은 ""으로 담는다.
		FNL[0][0] = Integer.toString(rsd);		//실적 시작일
		FNL[0][1] = Integer.toString(red);		//실적 종료일
		FNL[0][2] = "0";		//실적 일수
		
		//실적 기간 일수 구하기 [휴일이 포함된 경우임]
		if(!FNL[0][0].equals("0") && !FNL[0][1].equals("0")) {
			int pn_cnt = anbdt.getPeriodDate(FNL[0][0],FNL[0][1]);
			FNL[0][2] = Integer.toString(pn_cnt);		//실적 일수
		}
		
		return FNL;			
	}

	//*******************************************************************
	//	일자중 가장작은일자을 구하기  : 배열의 갯수가 (n X 1)개일때
	//  일자형태 : yyyyMMdd
	//*******************************************************************/
	public String completeFirstDate(String[] data) throws Exception
	{
		String FNL = "";
		int rsd=0;
		int len = data.length;

		//기준값을 담는다.
		if(len > 0) {
			rsd = Integer.parseInt(data[0]);	//실적 최초일
		}

		//비교하여 최저값을 구한다.
		for(int i=1; i<len; i++) {
			if(data[i] != null) {
				if(rsd > Integer.parseInt(data[i])) rsd = Integer.parseInt(data[i]);	//실적 가장 작은수
			}
		}
		FNL = Integer.toString(rsd);
		
		return FNL;			
	}

	//*******************************************************************
	//	일자중 가장큰일자을 구하기  : 배열의 갯수가 (n X 1)개일때
	//  일자형태 : yyyyMMdd
	//*******************************************************************/
	public String completeLastDate(String[] data) throws Exception
	{
		String FNL = "";
		int red=0;
		int len = data.length;

		//기준값을 담는다.
		if(len > 0) {
			red = Integer.parseInt(data[0]);	//실적 최초일
		}

		//비교하여 최고값을 구한다.
		for(int i=1; i<len; i++) {
			if(data[i] != null) {
				if(red < Integer.parseInt(data[i])) red = Integer.parseInt(data[i]);	//실적 가장 큰수
			}
		}
		FNL = Integer.toString(red);
		
		return FNL;			
	}

	//*******************************************************************
	//	기간 구하기
	//  일자형태 : yyyyMMdd
	//*******************************************************************/
	public String getPeriodDate(String fromDate,String toDate) throws Exception
	{
		String ped = "0";
		//null인지 검사
		if((fromDate == null) || (toDate == null)) return ped;
		
		//포멧이 맞는지 조사
		if((fromDate.length() != 8) || (toDate.length() != 8))	return ped;

		int pn_cnt = anbdt.getPeriodDate(fromDate,toDate);
		ped = Integer.toString(pn_cnt);		//기간
		
		return ped;			
	}
}

