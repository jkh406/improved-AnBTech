package com.anbtech.gw.business;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.anbtech.dbconn.*;

import com.anbtech.dms.entity.*;
import com.anbtech.dms.db.*;
import com.anbtech.dms.business.*;
import com.anbtech.dms.admin.*;

import com.anbtech.ca.entity.*;
import com.anbtech.ca.db.*;
import com.anbtech.ca.business.*;

public class ModuleApprovalBO
{
	// Database Wrapper Class ����
	private DBConnectionManager connMgr;
	private Connection con;
	
	/***************************************************************************
	 * ������ �޼ҵ�
	 **************************************************************************/
	public ModuleApprovalBO(Connection con) 
	{	
		this.con = con;
	}

	/***************************************************************************
	 * ������� ���ڰ��� ���� �ݿ��ϱ�
	 **************************************************************************/
	public void approvalTD(String aid,String mid,String tablename,String version) throws Exception 
	{	
		com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
		
		masterDAO.getAppInfoAndSave(aid);
		masterDAO.updateStat(tablename,mid,version,"3");	
		if(version.equals("1.0")) masterDAO.updateStat("","3",mid);
		masterDAO.updateAid(tablename,mid,version,aid);
		
	}

	/***************************************************************************
	 * ������� ���ڰ��� �ݷ� �ݿ��ϱ�
	 **************************************************************************/
	public void rejectTD(String aid,String mid,String tablename,String version) throws Exception 
	{	
		com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
		
		masterDAO.updateStat(tablename,mid,version,"4");
		if(version.equals("1.0")) masterDAO.updateStat("","4",mid);
		
	}

	/***************************************************************************
	 * ���ο� ���ڰ��� ���� �ݿ��ϱ�
	 **************************************************************************/
	public void approvalCA(String aid,String mid) throws Exception 
	{	
		com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
		com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);
		com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

		String write_type = mid.substring(mid.lastIndexOf("|")+1, mid.length());
		mid = mid.substring(0, mid.lastIndexOf("|"));

		
		// 1.���ڰ��� ��⿡�� ���� ����(app_save table)�� ������ ��, approval_info ���̺� �����Ѵ�.
		appDAO.getAppInfoAndSave("ca_approval_info",aid);

		// 2.�ӽý��ι�ȣ�� �Է����� �޾� ���Ľ��� ��ȣ�� ���Ѵ�.
		String approval_no = caBO.getApprovalNo(mid);

		// 3.��û��忡 ���� ó��
		if(write_type.equals("report_w") || write_type.equals("report_r")){
			caBO.updateApprovalAndSaveHistoryInfo(write_type,mid,approval_no,aid);
		}else if(write_type.equals("report_d")){
			caBO.deleteApprovalAndSaveHistoryInfo(mid,aid);
		}
		
	}

}
