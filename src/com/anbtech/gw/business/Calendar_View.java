package com.anbtech.gw.business;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import com.anbtech.BoardListBean;

public class Calendar_View
{
	// Database Wrapper Class ����
	private BoardListBean bean = null;	
		
	private String query = "";			//query���� �����
	private String[] indColumns = {"pid","id","myear","mmonth","mday","eyear","emonth","eday","mtime","item","isopen"};

	/***************************************************************************
	 * ������ �޼ҵ�
	 **************************************************************************/
	public Calendar_View() 
	{	
		bean = new BoardListBean();
	}
	
	/***************************************************************************
	 * ���� ���� ���ϱ� 
	 **************************************************************************/	
	public String Owner_View(String rid,String ryear,String rmonth) throws SQLException
	{	
		String Schedule_items = "";
		
		//1.������ ��/���� �ش�Ǵ� ���
		Schedule_items = getOwnerMonth(rid,ryear,rmonth);
		
		//2.������ �⿡ �ٸ����� �����ִ� ���
		Schedule_items += getOwnerDiffMonth(rid,ryear,rmonth);
		
		//3.�ٸ� �⵵�� �����ִ� ���
		Schedule_items += getOwnerDiffYear(rid,ryear,rmonth);
		
		return Schedule_items;	
	}
	
	/***************************************************************************
	 * Others(������,ȸ��,�μ�) ���� ���ϱ�
	 * Owner�� ���� (��,������ ��츸 �����) 
	 * type : ��������(INI), ȸ������(COM), �μ�����(DIV)
	 **************************************************************************/	
	public String Other_View(String rid,String ryear,String rmonth,String type) throws SQLException
	{	
		String Schedule_items = "";
		
		//1.������ ��/���� �ش�Ǵ� ���
		Schedule_items = getOtherMonth(rid,ryear,rmonth,type);
		
		//2.������ �⿡ �ٸ����� �����ִ� ���
		Schedule_items += getOtherDiffMonth(rid,ryear,rmonth,type);
		
		//3.�ٸ� �⵵�� �����ִ� ���
		Schedule_items += getOtherDiffYear(rid,ryear,rmonth,type);
		
		return Schedule_items;		
	}
	
	/***************************************************************************
	 * ���� ���� : 1.������ ��/���� �ش�Ǵ� ��� 
	 **************************************************************************/	
	public String getOwnerMonth(String rid,String ryear,String rmonth) throws SQLException
	{	
		String cal_dl = "";
		String rYM = "";		//��û�� yyyyMM���ϱ� 
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
			String sch_time = bean.getData("mtime");			//�ð�
			String sch_item = bean.getData("item");				//�׸�

			String ISOPEN = bean.getData("isopen");				//���� �̰���
			String YN = "";
			if(ISOPEN.equals("0")) YN = "N";
			else YN = "Y";

			String PID = bean.getData("pid");					//������ȣ
		
			//�Ⱓ(2���̻��� ����) �ش��Ͽ� ��� ǥ���ϱ�
			String tY = bean.getData("myear");
			String tM = bean.getData("mmonth"); if(tM.length() == 1) tM = "0" + tM;
			String tD = bean.getData("mday");	if(tD.length() == 1) tD = "0" + tD;
			String tYMD = tY+tM+tD;
			int tymd = 0;
			if((tYMD != null) && (tYMD.length() != 0))	tymd = Integer.parseInt(tYMD);	//���۳����

			String nY = bean.getData("eyear");
			String nM = bean.getData("emonth"); if(nM.length() == 1) nM = "0" + nM;
			String nD = bean.getData("eday");	if(nD.length() == 1) nD = "0" + nD;
			String nYMD = nY+nM+nD;
			int nymd = 0;
			if((nYMD != null) && (nYMD.length() != 0))	nymd = Integer.parseInt(nYMD);	//��������

			//�־��� ���ڷ� �������ڸ� �����Ͽ� �Ⱓ������ ���ǥ���Ѵ�. 
			int syr = Integer.parseInt(bean.getData("myear"));
			int smn = Integer.parseInt(bean.getData("mmonth"));
			int sde = Integer.parseInt(bean.getData("mday"));

			//�����ϰ� �������� �ִ°��
			if((tymd != 0) && (nymd != 0)) {
				int diff = nymd - tymd;				//����
				
				//������ ��� 
				if(diff == 0) {
					cal_dl += sch_date + "*" + sch_file + "@Calendar_View@" + YN + "@" + sch_item + "(" + sch_time + ")" + "@" + PID + "@" + ";";	
				//�Ⱓ�� ��� 
				} else {
					//�Ⱓ��ŭ�� ����� ���Ѵ�.
					for(int wi = 0; wi <= diff; wi++) {
						String chkymd = getDate(syr,smn,sde,wi);		//1�ϴ��Ͽ��� ����
						String chkYM = chkymd.substring(0,6);			//��� (yyyyMM)
						
						//��û�� ����� ����Ѵ�.
						if(rYM.equals(chkYM)) {				
							sch_date = chkymd.substring(0,4)+"/"+chkymd.substring(4,6)+"/"+chkymd.substring(6,8);
							if((wi != 0) && (nYMD.equals(chkymd))) {		//������ ���� 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"(["+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
								break;
							} else if((wi == 0) && (!nYMD.equals(chkymd))) { //ó������ 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
							} else {										//�߰� ���� 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+bean.getData("mmonth")+"/"+bean.getData("mday")+"~"+bean.getData("emonth")+"/"+bean.getData("eday")+")"+"@"+PID+"@"+";";
							} 
						}//if(��û�� ����� ���)
					} //for
				} //if 
			//�����ϸ� �ִ°��
			} else {
				cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
			}	

		} //while
		return cal_dl;
	}
	
	/***************************************************************************
	 * ���� ���� : 2.(�Ⱓ)������ �⿡ �ٸ����� �����ִ� ���
	 **************************************************************************/	
	public String getOwnerDiffMonth(String rid,String ryear,String rmonth) throws SQLException
	{	
		String cal_dl = "";
		String rYM = "";		//��û�� yyyyMM���ϱ� 
		if((ryear != null) && (rmonth != null)) rYM = ryear+rmonth;
		
		int qyear = Integer.parseInt(ryear);			//query�� �⵵
		int qmonth = Integer.parseInt(rmonth);			//query�� ��
		
		query = "where id='"+rid+"' and ("+qyear+" = myear and eyear = "+qyear+" and eyear != 0) and ";
		query += "(mmonth <= "+qmonth+" and "+qmonth+" <= emonth) order by mtime ASC";
		
		bean.setTable("CALENDAR_SCHEDULE");
		bean.setColumns(indColumns);
		bean.setSearchWrite(query);
		bean.init_write();
		
		while(bean.isAll()) {
			String sch_date = bean.getData("myear") + "/" + bean.getData("mmonth") + "/" + bean.getData("mday");
			String sch_file = "8A2329406E";
			String sch_item = bean.getData("item");				//�׸�
			String sch_time = bean.getData("mtime");			//�ð�

			String ISOPEN = bean.getData("isopen");				//���� �̰���
			String YN = "";
			if(ISOPEN.equals("0")) YN = "N";
			else YN = "Y";

			String PID = bean.getData("pid");					//������ȣ
		
			//�Ⱓ(2���̻��� ����) �ش��Ͽ� ��� ǥ���ϱ�
			String tY = bean.getData("myear");
			String tM = bean.getData("mmonth"); if(tM.length() == 1) tM = "0" + tM;
			String tD = bean.getData("mday");	if(tD.length() == 1) tD = "0" + tD;
			String tYMD = tY+tM+tD;
			int tymd = 0;
			if((tYMD != null) && (tYMD.length() != 0))	tymd = Integer.parseInt(tYMD);	//���۳����

			String nY = bean.getData("eyear");
			String nM = bean.getData("emonth"); if(nM.length() == 1) nM = "0" + nM;
			String nD = bean.getData("eday");	if(nD.length() == 1) nD = "0" + nD;
			String nYMD = nY+nM+nD;
			int nymd = 0;
			if((nYMD != null) && (nYMD.length() != 0))	nymd = Integer.parseInt(nYMD);	//��������

			//�־��� ���ڷ� �������ڸ� �����Ͽ� �Ⱓ������ ���ǥ���Ѵ�. 
			int syr = Integer.parseInt(bean.getData("myear"));
			int smn = Integer.parseInt(bean.getData("mmonth"));
			int sde = Integer.parseInt(bean.getData("mday"));

			//�����ϰ� �������� �ִ°��
			if((tymd != 0) && (nymd != 0)) {
				int diff = nymd - tymd;				//����
				
				//�Ⱓ�� ��츸 ����Ѵ�.
				if(diff != 0) {
					//�Ⱓ��ŭ�� ����� ���Ѵ�.
					for(int wi = 0; wi <= diff; wi++) {
						String chkymd = getDate(syr,smn,sde,wi);				//1�ϴ��Ͽ��� ����
						String chkYM = chkymd.substring(0,6);					//��� (yyyyMM)
						String smonth = chkymd.substring(4,6);					//�� 
						
						//��û�� ����� ����Ѵ�.
						//�������۴ް� �������� ���� (������ �����)	
						if(!tM.equals(smonth) && (rYM.equals(chkYM))) {	
							sch_date = chkymd.substring(0,4)+"/"+chkymd.substring(4,6)+"/"+chkymd.substring(6,8);
							if((wi != 0) && (nYMD.equals(chkymd))) {		//������ ���� 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"(["+bean.getData("mmonth")+"/"+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
								break;
							} else if((wi == 0) && (!nYMD.equals(chkymd))) { //ó������ 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
							} else {										//�߰� ���� 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+bean.getData("mmonth")+"/"+bean.getData("mday")+"~"+bean.getData("emonth")+"/"+bean.getData("eday")+")"+"@"+PID+"@"+";";
							} 
						} //if (������ ����)
					} //for
				} //if 
			} //if	

		} //while
		return cal_dl;
	}
	
	/***************************************************************************
	 * ���� ���� : 3.(�Ⱓ)�ٸ� �⵵�� �����ִ� ���
	 **************************************************************************/	
	public String getOwnerDiffYear(String rid,String ryear,String rmonth) throws SQLException
	{	
		String cal_dl = "";
		String rYM = "";		//��û�� yyyyMM���ϱ� 
		if((ryear != null) && (rmonth != null)) rYM = ryear+rmonth;
		
		int qyear = Integer.parseInt(ryear);			//query�� �⵵
		int qmonth = Integer.parseInt(rmonth);			//query�� ��
		
		int qbyear = qyear - 1;	//1����
		query = "where id='"+rid+"' and ("+qbyear+" = myear and eyear = "+qyear+" and eyear != 0) and ";
		query += "("+qmonth+" <= emonth) order by mtime ASC";
		
		bean.setTable("CALENDAR_SCHEDULE");
		bean.setColumns(indColumns);
		bean.setSearchWrite(query);
		bean.init_write();
		
		while(bean.isAll()) {
			String sch_date = bean.getData("myear") + "/" + bean.getData("mmonth") + "/" + bean.getData("mday");
			String sch_file = "8A2329406E";
			String sch_item = bean.getData("item");				//�׸�
			String sch_time = bean.getData("mtime");			//�ð�

			String ISOPEN = bean.getData("isopen");				//���� �̰���
			String YN = "";
			if(ISOPEN.equals("0")) YN = "N";
			else YN = "Y";

			String PID = bean.getData("pid");					//������ȣ
		
			//�Ⱓ(2���̻��� ����) �ش��Ͽ� ��� ǥ���ϱ�
			String tY = bean.getData("myear");
			String tM = bean.getData("mmonth"); if(tM.length() == 1) tM = "0" + tM;
			String tD = bean.getData("mday");	if(tD.length() == 1) tD = "0" + tD;
			String tYMD = tY+tM+tD;
			int tymd = 0;
			if((tYMD != null) && (tYMD.length() != 0))	tymd = Integer.parseInt(tYMD);	//���۳����

			String nY = bean.getData("eyear");
			String nM = bean.getData("emonth"); if(nM.length() == 1) nM = "0" + nM;
			String nD = bean.getData("eday");	if(nD.length() == 1) nD = "0" + nD;
			String nYMD = nY+nM+nD;
			int nymd = 0;
			if((nYMD != null) && (nYMD.length() != 0))	nymd = Integer.parseInt(nYMD);	//��������

			//�־��� ���ڷ� �������ڸ� �����Ͽ� �Ⱓ������ ���ǥ���Ѵ�. 
			int syr = Integer.parseInt(bean.getData("myear"));
			int smn = Integer.parseInt(bean.getData("mmonth"));
			int sde = Integer.parseInt(bean.getData("mday"));

			//�����ϰ� �������� �ִ°��
			if((tymd != 0) && (nymd != 0)) {
				int diff = nymd - tymd;				//����
				
				//�Ⱓ�� ��츸 ����Ѵ�. 
				if(diff!= 0) {
					//�Ⱓ��ŭ�� ����� ���Ѵ�.
					for(int wi = 0; wi <= diff; wi++) {
						String chkymd = getDate(syr,smn,sde,wi);		//1�ϴ��Ͽ��� ����
						String chkYM = chkymd.substring(0,6);			//��� (yyyyMM)
						
						//��û�� ����� ����Ѵ�.
						if(rYM.equals(chkYM)) {	
							sch_date = chkymd.substring(0,4)+"/"+chkymd.substring(4,6)+"/"+chkymd.substring(6,8);
							if((wi != 0) && (nYMD.equals(chkymd))) {		//������ ���� 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"(["+bean.getData("mmonth")+"/"+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
								break;
							} else if((wi == 0) && (!nYMD.equals(chkymd))) { //ó������ 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
							} else {										//�߰� ���� 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+sch_item+"("+bean.getData("mmonth")+"/"+bean.getData("mday")+"~"+bean.getData("emonth")+"/"+bean.getData("eday")+")"+"@"+PID+"@"+";";
							} 
						} //if ��û�� ����� ���
					} //for
				} //if 
			} 	
		} //while
		return cal_dl;
	}
	/***************************************************************************
	 * Others ���� : 1.������ ��/���� �ش�Ǵ� ��� 
	 * type : ��������(INI), ȸ������(COM), �μ�����(DIV)
	 **************************************************************************/	
	public String getOtherMonth(String rid,String ryear,String rmonth,String type) throws SQLException
	{	
		String cal_dl = "";				//return�� 
		String rYM = "";				//��û�� yyyyMM���ϱ� 
		if((ryear != null) && (rmonth != null)) rYM = ryear+rmonth;
		
		String html_fs = "";			//html�� ���� ���� 
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
			String sch_item = bean.getData("item");				//�׸�
			String sch_time = bean.getData("mtime");			//�ð�

			String ISOPEN = bean.getData("isopen");				//���� �̰���
			String YN = "";
			if(ISOPEN.equals("0")) YN = "N";
			else YN = "Y";

			String PID = bean.getData("pid");					//������ȣ
		
			//�Ⱓ(2���̻��� ����) �ش��Ͽ� ��� ǥ���ϱ�
			String tY = bean.getData("myear");
			String tM = bean.getData("mmonth"); if(tM.length() == 1) tM = "0" + tM;
			String tD = bean.getData("mday");	if(tD.length() == 1) tD = "0" + tD;
			String tYMD = tY+tM+tD;
			int tymd = 0;
			if((tYMD != null) && (tYMD.length() != 0))	tymd = Integer.parseInt(tYMD);	//���۳����

			String nY = bean.getData("eyear");
			String nM = bean.getData("emonth"); if(nM.length() == 1) nM = "0" + nM;
			String nD = bean.getData("eday");	if(nD.length() == 1) nD = "0" + nD;
			String nYMD = nY+nM+nD;
			int nymd = 0;
			if((nYMD != null) && (nYMD.length() != 0))	nymd = Integer.parseInt(nYMD);	//��������

			//�־��� ���ڷ� �������ڸ� �����Ͽ� �Ⱓ������ ���ǥ���Ѵ�. 
			int syr = Integer.parseInt(bean.getData("myear"));
			int smn = Integer.parseInt(bean.getData("mmonth"));
			int sde = Integer.parseInt(bean.getData("mday"));

			//�����ϰ� �������� �ִ°��
			if((tymd != 0) && (nymd != 0)) {
				int diff = nymd - tymd;				//����
				
				//������ ��� 
				if(diff == 0) {
					cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";	
				//�Ⱓ�� ��� 
				} else {
					//�Ⱓ��ŭ�� ����� ���Ѵ�.
					for(int wi = 0; wi <= diff; wi++) {
						String chkymd = getDate(syr,smn,sde,wi);		//1�ϴ��Ͽ��� ����
						String chkYM = chkymd.substring(0,6);			//��� (yyyyMM)
				
						//��û�� ����� ����Ѵ�.
						if(rYM.equals(chkYM)) {	
							sch_date = chkymd.substring(0,4)+"/"+chkymd.substring(4,6)+"/"+chkymd.substring(6,8);
							if((wi != 0) && (nYMD.equals(chkymd))) {		//������ ���� 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"(["+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
								break;
							} else if((wi == 0) && (!nYMD.equals(chkymd))) { //ó������ 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
							} else {										//�߰� ���� 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+bean.getData("mmonth")+"/"+bean.getData("mday")+"~"+bean.getData("emonth")+"/"+bean.getData("eday")+")"+"@"+PID+"@"+";";
							} 
						} //if(��û�� ����� ���)
					} //for
				} //if 
			//�����ϸ� �ִ°��
			} else {
				cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
			}	

		} //while
		return cal_dl;
	}
	
	/***************************************************************************
	 * Others ���� : 2. (�Ⱓ)������ �⿡ �ٸ����� �����ִ� ���
	 * type : ��������(INI), ȸ������(COM), �μ�����(DIV)
	 **************************************************************************/	
	public String getOtherDiffMonth(String rid,String ryear,String rmonth, String type) throws SQLException
	{	
		String cal_dl = "";				//return�� 
		String rYM = "";				//��û�� yyyyMM���ϱ� 
		if((ryear != null) && (rmonth != null)) rYM = ryear+rmonth;
		
		String html_fs = "";			//html�� ���� ���� 
		if(type == null) type = "";
		else {
			if(type.equals("COM")) html_fs = "<font color='#CC2900'><b>(C)</b></font>";
			else if(type.equals("DIV")) html_fs = "<font color='A300CC'><b>(G)</b></font>";  	
		}
		
		int qyear = Integer.parseInt(ryear);			//query�� �⵵
		int qmonth = Integer.parseInt(rmonth);			//query�� ��
				
		query = "where id='"+rid+"' and ("+qyear+" = myear and eyear = "+qyear+" and eyear != 0) and ";
		query += "(mmonth <= "+qmonth+" and "+qmonth+" <= emonth) and isopen='1' order by mtime ASC";
		
		bean.setTable("CALENDAR_SCHEDULE");
		bean.setColumns(indColumns);
		bean.setSearchWrite(query);
		bean.init_write();
		
		while(bean.isAll()) {
			String sch_date = bean.getData("myear") + "/" + bean.getData("mmonth") + "/" + bean.getData("mday");
			String sch_file = "8A2329406E";
			String sch_item = bean.getData("item");				//�׸�
			String sch_time = bean.getData("mtime");			//�ð�

			String ISOPEN = bean.getData("isopen");				//���� �̰���
			String YN = "";
			if(ISOPEN.equals("0")) YN = "N";
			else YN = "Y";

			String PID = bean.getData("pid");					//������ȣ
		
			//�Ⱓ(2���̻��� ����) �ش��Ͽ� ��� ǥ���ϱ�
			String tY = bean.getData("myear");
			String tM = bean.getData("mmonth"); if(tM.length() == 1) tM = "0" + tM;
			String tD = bean.getData("mday");	if(tD.length() == 1) tD = "0" + tD;
			String tYMD = tY+tM+tD;
			int tymd = 0;
			if((tYMD != null) && (tYMD.length() != 0))	tymd = Integer.parseInt(tYMD);	//���۳����

			String nY = bean.getData("eyear");
			String nM = bean.getData("emonth"); if(nM.length() == 1) nM = "0" + nM;
			String nD = bean.getData("eday");	if(nD.length() == 1) nD = "0" + nD;
			String nYMD = nY+nM+nD;
			int nymd = 0;
			if((nYMD != null) && (nYMD.length() != 0))	nymd = Integer.parseInt(nYMD);	//��������

			//�־��� ���ڷ� �������ڸ� �����Ͽ� �Ⱓ������ ���ǥ���Ѵ�. 
			int syr = Integer.parseInt(bean.getData("myear"));
			int smn = Integer.parseInt(bean.getData("mmonth"));
			int sde = Integer.parseInt(bean.getData("mday"));

			//�����ϰ� �������� �ִ°��
			if((tymd != 0) && (nymd != 0)) {
				int diff = nymd - tymd;				//����
				
				//�Ⱓ�� ��츸 ����Ѵ�. 
				if(diff!= 0) {
					//�Ⱓ��ŭ�� ����� ���Ѵ�.
					for(int wi = 0; wi <= diff; wi++) {
						String chkymd = getDate(syr,smn,sde,wi);				//1�ϴ��Ͽ��� ����
						String chkYM = chkymd.substring(0,6);					//��� (yyyyMM)
						String smonth = chkymd.substring(4,6);					//��  
						sch_date = chkymd.substring(0,4)+"/"+chkymd.substring(4,6)+"/"+chkymd.substring(6,8);
						
						//��û�� ����� ����Ѵ�.
						//�������۴ް� �������� ���� (������ �����)	
						if(!tM.equals(smonth) && (rYM.equals(chkYM))) {	
							if((wi != 0) && (nYMD.equals(chkymd))) {		//������ ���� 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"(["+bean.getData("mmonth")+"/"+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
								break;
							} else if((wi == 0) && (!nYMD.equals(chkymd))) { //ó������ 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
							} else {										//�߰� ���� 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+bean.getData("mmonth")+"/"+bean.getData("mday")+"~"+bean.getData("emonth")+"/"+bean.getData("eday")+")"+"@"+PID+"@"+";";
							} 
						} //������ 
					} //for
				} //if 
			} 	

		} //while
		return cal_dl;
	}
	
	/***************************************************************************
	 * Other ���� : 3.(�Ⱓ)�ٸ� �⵵�� �����ִ� ���
	 * type : ��������(INI), ȸ������(COM), �μ�����(DIV)
	 **************************************************************************/	
	public String getOtherDiffYear(String rid,String ryear,String rmonth,String type) throws SQLException
	{	
		String cal_dl = "";				//return�� 
		String rYM = "";				//��û�� yyyyMM���ϱ� 
		if((ryear != null) && (rmonth != null)) rYM = ryear+rmonth;
		
		String html_fs = "";			//html�� ���� ���� 
		if(type == null) type = "";
		else {
			if(type.equals("COM")) html_fs = "<font color='#CC2900'><b>(C)</b></font>";
			else if(type.equals("DIV")) html_fs = "<font color='A300CC'><b>(G)</b></font>";  	
		}
		
		int qyear = Integer.parseInt(ryear);			//query�� �⵵
		int qmonth = Integer.parseInt(rmonth);			//query�� ��
		
		int qbyear = qyear - 1;	//1����
		
		query = "where id='"+rid+"' and ("+qbyear+" = myear and eyear = "+qyear+" and eyear != 0) and ";
		query += "("+qmonth+" <= emonth) and isopen='1' order by mtime ASC"; 
		
		bean.setTable("CALENDAR_SCHEDULE");
		bean.setColumns(indColumns);
		bean.setSearchWrite(query);
		bean.init_write();
		
		while(bean.isAll()) {
			String sch_date = bean.getData("myear") + "/" + bean.getData("mmonth") + "/" + bean.getData("mday");
			String sch_file = "8A2329406E";
			String sch_item = bean.getData("item");				//�׸�
			String sch_time = bean.getData("mtime");			//�ð�

			String ISOPEN = bean.getData("isopen");				//���� �̰���
			String YN = "";
			if(ISOPEN.equals("0")) YN = "N";
			else YN = "Y";

			String PID = bean.getData("pid");					//������ȣ
		
			//�Ⱓ(2���̻��� ����) �ش��Ͽ� ��� ǥ���ϱ�
			String tY = bean.getData("myear");
			String tM = bean.getData("mmonth"); if(tM.length() == 1) tM = "0" + tM;
			String tD = bean.getData("mday");	if(tD.length() == 1) tD = "0" + tD;
			String tYMD = tY+tM+tD;
			int tymd = 0;
			if((tYMD != null) && (tYMD.length() != 0))	tymd = Integer.parseInt(tYMD);	//���۳����

			String nY = bean.getData("eyear");
			String nM = bean.getData("emonth"); if(nM.length() == 1) nM = "0" + nM;
			String nD = bean.getData("eday");	if(nD.length() == 1) nD = "0" + nD;
			String nYMD = nY+nM+nD;
			int nymd = 0;
			if((nYMD != null) && (nYMD.length() != 0))	nymd = Integer.parseInt(nYMD);	//��������

			//�־��� ���ڷ� �������ڸ� �����Ͽ� �Ⱓ������ ���ǥ���Ѵ�. 
			int syr = Integer.parseInt(bean.getData("myear"));
			int smn = Integer.parseInt(bean.getData("mmonth"));
			int sde = Integer.parseInt(bean.getData("mday"));

			//�����ϰ� �������� �ִ°��
			if((tymd != 0) && (nymd != 0)) {
				int diff = nymd - tymd;				//����
				
				//�Ⱓ�� ��츸 ����Ѵ�. 
				if(diff!= 0) {
					//�Ⱓ��ŭ�� ����� ���Ѵ�.
					for(int wi = 0; wi <= diff; wi++) {
						String chkymd = getDate(syr,smn,sde,wi);		//1�ϴ��Ͽ��� ����
						String chkYM = chkymd.substring(0,6);			//��� (yyyyMM)
						
						//��û�� ����� ����Ѵ�.
						if(rYM.equals(chkYM)) {	
							sch_date = chkymd.substring(0,4)+"/"+chkymd.substring(4,6)+"/"+chkymd.substring(6,8);
							if((wi != 0) && (nYMD.equals(chkymd))) {		//������ ���� 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"(["+bean.getData("mmonth")+"/"+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
								break;
							} else if((wi == 0) && (!nYMD.equals(chkymd))) { //ó������ 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"("+sch_time+")"+"@"+PID+"@"+";";
							} else {										//�߰� ���� 
								cal_dl += sch_date+"*"+sch_file+"@Calendar_View@"+YN+"@"+html_fs+sch_item+"(["+bean.getData("mday")+"]"+sch_time+")"+"@"+PID+"@"+";";
							} 
						} //if(��û�� ����� ���)
					} //for
				} //if 
			} 	

		} //while
		return cal_dl;
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
		java.util.Date now = calendar.getTime();
				
		//String �����Ѵ�.
		SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
		String request_date = vans.format(now);
		return request_date;		
	}

}