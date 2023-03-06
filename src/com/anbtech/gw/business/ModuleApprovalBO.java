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
	// Database Wrapper Class 선언
	private DBConnectionManager connMgr;
	private Connection con;
	
	/***************************************************************************
	 * 생성자 메소드
	 **************************************************************************/
	public ModuleApprovalBO(Connection con) 
	{	
		this.con = con;
	}

	/***************************************************************************
	 * 기술문서 전자결재 승인 반영하기
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
	 * 기술문서 전자결재 반려 반영하기
	 **************************************************************************/
	public void rejectTD(String aid,String mid,String tablename,String version) throws Exception 
	{	
		com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
		
		masterDAO.updateStat(tablename,mid,version,"4");
		if(version.equals("1.0")) masterDAO.updateStat("","4",mid);
		
	}

	/***************************************************************************
	 * 승인원 전자결재 승인 반영하기
	 **************************************************************************/
	public void approvalCA(String aid,String mid) throws Exception 
	{	
		com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
		com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);
		com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

		String write_type = mid.substring(mid.lastIndexOf("|")+1, mid.length());
		mid = mid.substring(0, mid.lastIndexOf("|"));

		
		// 1.전자결재 모듈에서 결재 정보(app_save table)를 가져온 후, approval_info 테이블에 저장한다.
		appDAO.getAppInfoAndSave("ca_approval_info",aid);

		// 2.임시승인번호를 입력으로 받아 정식승인 번호를 구한다.
		String approval_no = caBO.getApprovalNo(mid);

		// 3.신청모드에 따른 처리
		if(write_type.equals("report_w") || write_type.equals("report_r")){
			caBO.updateApprovalAndSaveHistoryInfo(write_type,mid,approval_no,aid);
		}else if(write_type.equals("report_d")){
			caBO.deleteApprovalAndSaveHistoryInfo(mid,aid);
		}
		
	}

}
