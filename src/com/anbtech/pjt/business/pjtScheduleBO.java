package com.anbtech.pjt.business;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.anbtech.date.anbDate;
import java.util.StringTokenizer;

public class pjtScheduleBO
{
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();	//���ڰ��

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public pjtScheduleBO() 
	{
		
	}

	//*******************************************************************
	//	������ ������������ �� ����ū���� �� ���ϱ�  : �迭�� ������ (n X 4)���϶�
	//  �������� : yyyyMMdd
	//*******************************************************************/
	public String[][] searchFirstLastDate(String[][] data) throws Exception
	{
		String[][] FNL = new String[2][3];
		int psd=0,ped=0,csd=0,ced=0;
		int len = data.length;

		//���ذ��� ��´�.
		if(len > 0) {
			psd = Integer.parseInt(data[0][0]);	//��ȹ ������
			ped = Integer.parseInt(data[0][1]);	//��ȹ ������
			csd = Integer.parseInt(data[0][2]);	//���� ������
			ced = Integer.parseInt(data[0][3]);	//���� ������
		}
	
		//���Ͽ� �������� �ְ��� ���Ѵ�.
		for(int i=1; i<len; i++) {
			//��ȹ�� ���ϱ�
			if(psd > Integer.parseInt(data[i][0])) psd = Integer.parseInt(data[i][0]);	//��ȹ ���� ������
			if(ped < Integer.parseInt(data[i][1])) ped = Integer.parseInt(data[i][1]);	//��ȹ ���� ū��
			//������ ���ϱ�
			if(csd > Integer.parseInt(data[i][2])) csd = Integer.parseInt(data[i][2]);	//��ȹ ���� ������
			if(ced < Integer.parseInt(data[i][3])) ced = Integer.parseInt(data[i][3]);	//��ȹ ���� ū��
		}

		//�ش� ������ �迭�� ���, �� ���� 0�ΰ��� ""���� ��´�.
		FNL[0][0] = Integer.toString(psd);		//��ȹ ������
		FNL[0][1] = Integer.toString(ped);		//��ȹ ������
		FNL[0][2] = "0";		//��ȹ �ϼ�
		FNL[1][0] = Integer.toString(csd);		//���� ������
		FNL[1][1] = Integer.toString(ced);		//���� ������
		FNL[1][2] = "0";		//���� �ϼ�

		//��ȹ �Ⱓ �ϼ� ���ϱ� [������ ���Ե� �����]
		if(!FNL[0][0].equals("0") && !FNL[0][1].equals("0")) {
			int pn_cnt = anbdt.getPeriodDate(FNL[0][0],FNL[0][1]);
			FNL[0][2] = Integer.toString(pn_cnt);		//��ȹ �ϼ�
		}
		//���� �Ⱓ �ϼ� ���ϱ� [������ ���Ե� �����]
		if(!FNL[1][0].equals("0") && !FNL[1][1].equals("0")) {
			int cn_cnt = anbdt.getPeriodDate(FNL[1][0],FNL[1][1]);
			FNL[1][2] = Integer.toString(cn_cnt);		//���� �ϼ�
		}
		return FNL;			
	}

	//*******************************************************************
	//	������ ������������ �� ����ū���� �� ���ϱ�  : �迭�� ������ (n X 2)���϶�
	//  �������� : yyyyMMdd
	//*******************************************************************/
	public String[][] completeFirstLastDate(String[][] data) throws Exception
	{
		String[][] FNL = new String[1][3];
		int rsd=0,red=0;
		int len = data.length;

		//���ذ��� ��´�.
		if(len > 0) {
			rsd = Integer.parseInt(data[0][0]);	//���� ������
			red = Integer.parseInt(data[0][1]);	//���� ������
		}

		//���Ͽ� �������� �ְ��� ���Ѵ�.
		for(int i=1; i<len; i++) {
			if((data[i][0] != null) || (data[i][1] != null)) {
				if(rsd > Integer.parseInt(data[i][0])) rsd = Integer.parseInt(data[i][0]);	//���� ���� ������
				if(red < Integer.parseInt(data[i][1])) red = Integer.parseInt(data[i][1]);	//���� ���� ū��
			}
		}

		//�ش� ������ �迭�� ���, �� ���� 0�ΰ��� ""���� ��´�.
		FNL[0][0] = Integer.toString(rsd);		//���� ������
		FNL[0][1] = Integer.toString(red);		//���� ������
		FNL[0][2] = "0";		//���� �ϼ�
		
		//���� �Ⱓ �ϼ� ���ϱ� [������ ���Ե� �����]
		if(!FNL[0][0].equals("0") && !FNL[0][1].equals("0")) {
			int pn_cnt = anbdt.getPeriodDate(FNL[0][0],FNL[0][1]);
			FNL[0][2] = Integer.toString(pn_cnt);		//���� �ϼ�
		}
		
		return FNL;			
	}

	//*******************************************************************
	//	������ �������������� ���ϱ�  : �迭�� ������ (n X 1)���϶�
	//  �������� : yyyyMMdd
	//*******************************************************************/
	public String completeFirstDate(String[] data) throws Exception
	{
		String FNL = "";
		int rsd=0;
		int len = data.length;

		//���ذ��� ��´�.
		if(len > 0) {
			rsd = Integer.parseInt(data[0]);	//���� ������
		}

		//���Ͽ� �������� ���Ѵ�.
		for(int i=1; i<len; i++) {
			if(data[i] != null) {
				if(rsd > Integer.parseInt(data[i])) rsd = Integer.parseInt(data[i]);	//���� ���� ������
			}
		}
		FNL = Integer.toString(rsd);
		
		return FNL;			
	}

	//*******************************************************************
	//	������ ����ū������ ���ϱ�  : �迭�� ������ (n X 1)���϶�
	//  �������� : yyyyMMdd
	//*******************************************************************/
	public String completeLastDate(String[] data) throws Exception
	{
		String FNL = "";
		int red=0;
		int len = data.length;

		//���ذ��� ��´�.
		if(len > 0) {
			red = Integer.parseInt(data[0]);	//���� ������
		}

		//���Ͽ� �ְ��� ���Ѵ�.
		for(int i=1; i<len; i++) {
			if(data[i] != null) {
				if(red < Integer.parseInt(data[i])) red = Integer.parseInt(data[i]);	//���� ���� ū��
			}
		}
		FNL = Integer.toString(red);
		
		return FNL;			
	}

	//*******************************************************************
	//	�Ⱓ ���ϱ�
	//  �������� : yyyyMMdd
	//*******************************************************************/
	public String getPeriodDate(String fromDate,String toDate) throws Exception
	{
		String ped = "0";
		//null���� �˻�
		if((fromDate == null) || (toDate == null)) return ped;
		
		//������ �´��� ����
		if((fromDate.length() != 8) || (toDate.length() != 8))	return ped;

		int pn_cnt = anbdt.getPeriodDate(fromDate,toDate);
		ped = Integer.toString(pn_cnt);		//�Ⱓ
		
		return ped;			
	}
}

