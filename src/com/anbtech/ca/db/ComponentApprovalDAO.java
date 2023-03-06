package com.anbtech.ca.db;

import com.anbtech.ca.entity.*;
import com.anbtech.ca.business.*;
import java.sql.*;
import java.util.*;

public class ComponentApprovalDAO{
	private Connection con;

	/*******************************************************************
	 * ������
	 *******************************************************************/
	public ComponentApprovalDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	 * ���ڵ��� ��ü ������ ���Ѵ�.
	 *******************************************************************/
	public int getTotalCount(String tablename, String where) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		int total_count = 0;

		String query = "SELECT COUNT(*) FROM " + tablename + where;

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		total_count = Integer.parseInt(rs.getString(1));
		stmt.close();
		rs.close();
		return total_count;
	}


	/*****************************************************************************
	 * �˻� ���ǿ� �´� ca_master ���̺� ����Ʈ�� �����´�.
	 *****************************************************************************/
	public ArrayList getCaMasterList(String mode,String searchword,String searchscope,String category,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.ca.entity.CaMasterTable table = new com.anbtech.ca.entity.CaMasterTable();
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 17;			// ������������ ����� ���ڵ� ��
		int l_maxpage = 7;			// ���������� ǥ���� �ٷΰ��� �������� ��
		int l_maxsubjectlen = 30;	// ������ �ִ� ǥ�ñ���

		int current_page_num =Integer.parseInt(page);

		com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
		String where = "";
		if(searchscope.equals("detail")) where = caBO.getWhere(mode,category,searchword);
		else where = caBO.getWhere(mode,searchword, searchscope, category);

		int total = getTotalCount("ca_master", where);	// ��ü ���ڵ� ����
		int recNum = total;

		//�˻����ǿ� �´� �Խù��� �����´�.
		String query = "SELECT mid,aid,approval_no,item_name,item_type,item_unit,item_no,item_desc,approve_type,maker_code,maker_name,maker_part_no,approver_info,approve_date,model_code,model_name,prj_code,prj_name FROM ca_master " + where + " ORDER BY mid DESC";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String mid				= rs.getString("mid");
			String aid				= rs.getString("aid");
			String approval_no		= rs.getString("approval_no");
			String item_no			= rs.getString("item_no");
			String item_name		= rs.getString("item_name");
			String item_type		= rs.getString("item_type");
			String item_desc		= rs.getString("item_desc");
			String approve_type		= rs.getString("approve_type");
			String maker_code		= rs.getString("maker_code");
			String maker_name		= rs.getString("maker_name");
			String maker_part_no	= rs.getString("maker_part_no");
			String approver_info	= rs.getString("approver_info");
			String approve_date		= rs.getString("approve_date");
			String prj_code			= rs.getString("prj_code");
			String prj_name			= rs.getString("prj_name");
			String model_code		= rs.getString("model_code");
			String model_name		= rs.getString("model_name");

			// ���ι�ȣ�� ��ũ ����
			String approval_no_link = "";
			approval_no_link = "<A HREF='ComponentApprovalServlet?&mode=view_a";
			approval_no_link += "&page="+page+"&searchword="+searchword;
			approval_no_link += "&searchscope="+searchscope+"&category="+category;
			approval_no_link += "&no="+mid+"'>";
			approval_no_link = approval_no_link + approval_no + "</a>";

			// ǰ���ȣ�� ��ũ ����
			String item_no_link = "";
			item_no_link = "<A HREF='ComponentApprovalServlet?&mode=view_i";
			item_no_link += "&page="+page+"&searchword="+searchword;
			item_no_link += "&searchscope="+searchscope+"&category="+category;
			item_no_link += "&no="+mid+"&item_no="+item_no+"'>";
			item_no_link = item_no_link + item_no + "</a>";

			table = new com.anbtech.ca.entity.CaMasterTable();
			table.setApprovalNo(approval_no_link);
			table.setItemNo(item_no_link);
			table.setItemName(item_name);
			table.setItemType(item_type);
			table.setItemDesc(item_desc);
			table.setApproveType(caBO.getApproveCodeName(approve_type));
			table.setMakerCode(maker_code);
			table.setMakerName(maker_name);
			table.setMakerPartNo(maker_part_no);
			table.setApproverInfo(approver_info);
			table.setApproveDate(approve_date);
			table.setPrjCode(prj_code);
			table.setPrjName(prj_name);
			table.setModelCode(model_code);
			table.setModelName(model_name);

			table_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();
		return table_list;
	}

	/*****************************************************************************
	 * �Ƿ��� ������� �����(flag='EN'), �űԵ�Ϲ���(ancestor='NEW') ����Ʈ�� �����´�.
	 * ��� ���� ���� �Ǵ� ������ �� �� �ְ� �ϱ� ����.
	 *****************************************************************************/
	public ArrayList getCaMasterListByRequestorId(String requestor_id,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.ca.entity.CaMasterTable table = new com.anbtech.ca.entity.CaMasterTable();
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 17;			// ������������ ����� ���ڵ� ��
		int l_maxpage = 7;			// ���������� ǥ���� �ٷΰ��� �������� ��
		int l_maxsubjectlen = 30;	// ������ �ִ� ǥ�ñ���

		int current_page_num =Integer.parseInt(page);

		com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
		String where = " WHERE requestor_id = '" + requestor_id + "' and aid = 'EN' and ancestor = 'NEW'";


		int total = getTotalCount("ca_master", where);	// ��ü ���ڵ� ����
		int recNum = total;

		//�˻����ǿ� �´� �Խù��� �����´�.
		String query = "SELECT mid,aid,tmp_approval_no,item_no,item_name,item_type,item_desc,approve_type,maker_code,maker_name,maker_part_no,request_date,prj_code,prj_name,model_code,model_name FROM ca_master " + where + " ORDER BY mid DESC";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String mid				= "<input type=checkbox name=checkbox value="+rs.getString("mid")+">";
			String aid				= rs.getString("aid");
			String tmp_approval_no	= rs.getString("tmp_approval_no");
			String item_no			= rs.getString("item_no");
			String item_desc		= rs.getString("item_desc");
			String approve_type		= rs.getString("approve_type");
			String maker_code		= rs.getString("maker_code");
			String maker_part_no	= rs.getString("maker_part_no");
			String request_date		= rs.getString("request_date");
			String item_name		= rs.getString("item_name");
			String item_type		= rs.getString("item_type");
			String maker_name		= rs.getString("maker_name");
			String prj_code			= rs.getString("prj_code");
			String prj_name			= rs.getString("prj_name");
			String model_code		= rs.getString("model_code");
			String model_name		= rs.getString("model_name");
/*
			// ���ι�ȣ�� ��ũ ����
			String tmp_approval_no_link = "";
			tmp_approval_no_link = "<A HREF='ComponentApprovalServlet?&mode=view_a";
			tmp_approval_no_link += "&page="+page+"&no="+mid+"'>";
			tmp_approval_no_link = tmp_approval_no_link + tmp_approval_no + "</a>";

			// ǰ���ȣ�� ��ũ ����
			String item_no_link = "";
			item_no_link = "<A HREF='ComponentApprovalServlet?&mode=view_i";
			item_no_link += "&page="+page+"&no="+item_no+"'>";
			item_no_link = item_no_link + item_no + "</a>";
*/
			table = new com.anbtech.ca.entity.CaMasterTable();
			table.setMid(mid);
			table.setApprovalNo(tmp_approval_no);
			table.setItemNo(item_no);
			table.setItemDesc(item_desc);
			table.setApproveType(caBO.getApproveCodeName(approve_type));
			table.setMakerCode(maker_code);
			table.setMakerPartNo(maker_part_no);
			table.setRequestDate(request_date);
			table.setItemName(item_name);
			table.setItemType(item_type);
			table.setMakerName(maker_name);
			table.setPrjCode(prj_code);
			table.setPrjName(prj_name);
			table.setModelCode(model_code);
			table.setModelName(model_name);

			table_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();
		return table_list;
	}

	/*****************************************************************************
	 * ���õ� ǰ���ȣ�� �ڽ��� ǰ���ȣ�� ���� ���ι��� ����Ʈ�� �����´�.
	 *****************************************************************************/
	public ArrayList getApprovalListByItemNo(String no) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.ca.entity.CaMasterTable table = new com.anbtech.ca.entity.CaMasterTable();
		com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
		ArrayList table_list = new ArrayList();
		
		//�˻����ǿ� �´� �Խù��� �����´�.
		String query = "SELECT mid,approval_no,approve_type,maker_code,maker_name,maker_part_no,approver_info,";
		query += "approve_date FROM ca_master WHERE item_no = '" + no + "' and aid not in('EN','EE') ORDER BY mid ASC";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){

			String mid = rs.getString("mid");
			String approval_no = rs.getString("approval_no");
			String approve_type = rs.getString("approve_type");
			String maker_code = rs.getString("maker_code");
			String maker_name = rs.getString("maker_name");
			String maker_part_no = rs.getString("maker_part_no");
			String approver_info = rs.getString("approver_info");
			String approve_date = rs.getString("approve_date");

			// ���ι�ȣ�� ��ũ ����
			String approval_no_link = "";
			approval_no_link = "<A HREF='ComponentApprovalServlet?&mode=view_a";
			approval_no_link += "&no="+mid+"'>";
			approval_no_link = approval_no_link + approval_no + "</a>";

			table = new com.anbtech.ca.entity.CaMasterTable();
			table.setApprovalNo(approval_no_link);
			table.setApproveType(caBO.getApproveCodeName(approve_type));
			table.setMakerCode(maker_code);
			table.setMakerName(maker_name);
			table.setMakerPartNo(maker_part_no);
			table.setApproverInfo(approver_info);
			table.setApproveDate(approve_date);

			table_list.add(table);

		}
		stmt.close();
		rs.close();

		return table_list;
	}

	/*****************************************************************************
	 * ���õ� ���ι����� ������ �����´�.(mid�� ������)
	 *****************************************************************************/
	public CaMasterTable getApprovalInfoByMid(String no) throws Exception{
		CaMasterTable table = new CaMasterTable();
		Statement stmt = null;
		ResultSet rs = null;
		
		//�˻����ǿ� �´� �Խù��� �����´�.
		String query = "SELECT * FROM ca_master WHERE mid='"+no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){
			table = new CaMasterTable();
			table.setMid(rs.getString("mid"));						//������ȣ
			table.setPrjCode(rs.getString("prj_code"));				//������Ʈ�ڵ�
			table.setPrjName(rs.getString("prj_name"));				//������Ʈ��
			table.setModelCode(rs.getString("model_code"));			//���ڵ�
			table.setModelName(rs.getString("model_name"));			//�𵨸�
			table.setItemNo(rs.getString("item_no"));				//ǰ���ȣ
			table.setItemName(rs.getString("item_name"));			//ǰ���
			table.setItemType(rs.getString("item_type"));			//ǰ�����
			table.setItemDesc(rs.getString("item_desc"));			//ǰ�񼳸�
			table.setItemUnit(rs.getString("item_unit"));			//ǰ�����
			table.setNoType(rs.getString("no_type"));				//ȸ�ι�/�ⱸ�� ����
			table.setApprovalNo(rs.getString("approval_no"));		//���ι�ȣ
			table.setMakerCode(rs.getString("maker_code"));			//���ξ�ü�ڵ�
			table.setMakerName(rs.getString("maker_name"));			//���ξ�ü��
			table.setMakerPartNo(rs.getString("maker_part_no"));	//��ü���� �����ϴ� ��ǰ��ȣ
			table.setApproveType(rs.getString("approve_type"));		//���������ڵ�
			table.setApplyDate(rs.getString("apply_date"));			//��������
			table.setApplyQuantity(rs.getString("apply_quantity"));	//�������
			table.setWhyApprove(rs.getString("why_approve"));		//���λ���
			table.setOtherInfo(rs.getString("other_info"));			//��Ÿ����
			table.setRequestorInfo(rs.getString("requestor_info"));	//�Ƿ��� ����
			table.setRequestDate(rs.getString("request_date"));		//�Ƿ�����
			table.setApproverInfo(rs.getString("approver_info"));	//������ ����
			table.setApproveDate(rs.getString("approve_date"));		//��������
			table.setAid(rs.getString("aid"));						//���ڰ��� ���� ID
			table.setUmask(rs.getString("umask"));					//umask
			table.setFileName(rs.getString("fname"));				//�����̸�
			table.setFileSize(rs.getString("fsize"));				//���ϻ�����
			table.setFileType(rs.getString("ftype"));				//����Ÿ��
			table.setUmask(rs.getString("umask"));					//umask
			table.setAncestor(rs.getString("ancestor"));			//���� ���ι����� ������ȣ
			table.setChangeInfo(rs.getString("change_info"));		//�������(���� ��)
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*****************************************************************************
	 * �ӽý����ڵ� �� ���� ���߿� ��ϵ� ���� mid�� �����´�.
	 * ���� ��� �ÿ� �ι�° ��� ���� ���� ������ �ѷ��ֱ� ����.
	 *****************************************************************************/
	public String getMaxMid(String no) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		
		String max_mid = "";
		String query = "SELECT MAX(mid) FROM ca_master WHERE tmp_approval_no = '" + no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){
			max_mid = rs.getString(1);
		}
		stmt.close();
		rs.close();

		return max_mid;
	}

	/*****************************************************************
	 * ������ȣ�� �ش��ϴ� ÷������ ����Ʈ ��������
	 *****************************************************************/
	public ArrayList getFile_list(String mid) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		int total = 0;
		ArrayList file_list = new ArrayList();

		String query = "SELECT fname,fsize,ftype FROM ca_master WHERE mid = '"+mid+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		if(rs.next()){ 

			Iterator filename_iter = com.anbtech.util.Token.getTokenList(rs.getString("fname")).iterator();
			Iterator filesize_iter = com.anbtech.util.Token.getTokenList(rs.getString("fsize")).iterator();
			Iterator filetype_iter = com.anbtech.util.Token.getTokenList(rs.getString("ftype")).iterator();

			while (filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()){
				CaMasterTable file = new CaMasterTable();
				file.setFileName((String)filename_iter.next());
				file.setFileType((String)filetype_iter.next());
				file.setFileSize((String)filesize_iter.next());
				file_list.add(file);
			}
		}
		stmt.close();
		rs.close();

		return file_list;
	} //getFile_list()

	/*****************************************************************************
	 * ǰ���ȣ�� �ش��ϴ� �̷� ����Ʈ�� �����´�.
	 *****************************************************************************/
	public ArrayList getHistoryList(String no) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.ca.entity.CaHistoryInfoTable table = new com.anbtech.ca.entity.CaHistoryInfoTable();
		ArrayList table_list = new ArrayList();
		
		String query = "SELECT * FROM history_info WHERE item_no = '" + no + "' ORDER BY hid ASC";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.ca.entity.CaHistoryInfoTable();
			table.setHid(rs.getString("hid"));
			table.setItemNo(rs.getString("item_no"));
			table.setContents(rs.getString("contents"));
			table.setApplyDate(rs.getString("apply_date"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table_list.add(table);
//System.out.println(rs.getString("requestor_info"));
		}
		stmt.close();
		rs.close();
		return table_list;
	}


	/*****************************************************************************
	 * ��ǰ���� ���� �����ϱ�
	 *****************************************************************************/
	 public void saveApprovalInfo(String tmp_approval_no,String login_id,String prj_code,String prj_name,String model_code,String model_name,String item_no,String item_name,String item_type,String item_unit,String maker_code,String maker_name,String maker_part_no,String item_desc,String approve_type,String apply_date,String apply_quantity,String why_approve,String other_info,String no_type,String umask,String ancestor) throws Exception{
		PreparedStatement pstmt = null;

		//��Ͻð�
		java.util.Date now = new java.util.Date();
//		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy.MM.dd a hh:mm");
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//��û�� ����
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();

		String requestor_info = division+"/"+user_rank+"/"+user_name;

		String query = "INSERT INTO ca_master (prj_code,prj_name,model_code,model_name,item_no,item_name,item_type,item_desc,item_unit,tmp_approval_no,maker_code,maker_name,maker_part_no,approve_type,apply_date,apply_quantity,why_approve,other_info,requestor_id,requestor_info,request_date,no_type,no_year,no_month,no_serial,umask,ancestor,aid) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,prj_code);
		pstmt.setString(2,prj_name);
		pstmt.setString(3,model_code);
		pstmt.setString(4,model_name);
		pstmt.setString(5,item_no);
		pstmt.setString(6,item_name);
		pstmt.setString(7,item_type);
		pstmt.setString(8,item_desc);
		pstmt.setString(9,item_unit);
		pstmt.setString(10,tmp_approval_no);
		pstmt.setString(11,maker_code);
		pstmt.setString(12,maker_name);
		pstmt.setString(13,maker_part_no);
		pstmt.setString(14,approve_type);
		pstmt.setString(15,apply_date);
		pstmt.setString(16,apply_quantity);
		pstmt.setString(17,why_approve);
		pstmt.setString(18,other_info);
		pstmt.setString(19,login_id);
		pstmt.setString(20,requestor_info);
		pstmt.setString(21,w_time);
		pstmt.setString(22,no_type);
		pstmt.setString(23,"");
		pstmt.setString(24,"");
		pstmt.setString(25,"");
		pstmt.setString(26,umask);
		pstmt.setString(27,ancestor);
		pstmt.setString(28,"EN");
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*****************************************************************************
	 * ��ǰ���� ���� �����ϱ� (��ŵǱ� ���� ���Ǹ� ���� ������.)
	 *****************************************************************************/
	 public void updateApprovalInfo(String mid,String prj_code,String model_code,String item_no,String item_unit,String maker_code,String maker_part_no,String item_desc,String approve_type,String apply_date,String apply_quantity,String why_approve,String other_info,String no_type) throws Exception{
		PreparedStatement pstmt = null;

		//��Ͻð�
		java.util.Date now = new java.util.Date();
//		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy.MM.dd a hh:mm");
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		String query = "UPDATE ca_master SET prj_code=?,model_code=?,item_no=?,item_desc=?,item_unit=?,maker_code=?,maker_part_no=?,approve_type=?,apply_date=?,apply_quantity=?,why_approve=?,other_info=?,request_date=?,no_type=? WHERE mid = '" + mid + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,prj_code);
		pstmt.setString(2,model_code);
		pstmt.setString(3,item_no);
		pstmt.setString(4,item_desc);
		pstmt.setString(5,item_unit);
		pstmt.setString(6,maker_code);
		pstmt.setString(7,maker_part_no);
		pstmt.setString(8,approve_type);
		pstmt.setString(9,apply_date);
		pstmt.setString(10,apply_quantity);
		pstmt.setString(11,why_approve);
		pstmt.setString(12,other_info);
		pstmt.setString(13,w_time);
		pstmt.setString(14,no_type);
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*************************************
	 * ÷������ ������ DB�� �����Ѵ�.
	 *************************************/
	public void updTable(String set, String where) throws Exception{
		Statement stmt = null;
		String query = "UPDATE ca_master" + set + where;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*************************************
	 * ������ �Ƿ� ������ ����Ѵ�.
	 *************************************/
	public void updateApprovalInfo(String no,String login_id,String change_info) throws Exception{
		Statement stmt = null;

		//��Ͻð�
		java.util.Date now = new java.util.Date();
//		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy.MM.dd a hh:mm");
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//��û�� ����
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();

		String requestor_info = division+"/"+user_rank+"/"+user_name;

		change_info = change_info + "<br>";
		change_info = change_info + "�Ƿ���:" + requestor_info +",�Ƿ��Ͻ�:" + w_time;

		String query = "UPDATE ca_master SET ancestor = 'DEL',change_info = '" + change_info + "' WHERE mid = '" + no + "'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}


	/*****************************************************************************
	 * ���õ� �ӽý��ι�ȣ(no)�� ���� ���ڵ��� ������ȣ(mid) ����Ʈ�� �����´�.
	 *****************************************************************************/
	public ArrayList getMidListByTmpAppNo(String no) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT mid FROM ca_master WHERE tmp_approval_no = '" + no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		String mid = "";
		ArrayList table_list = new ArrayList();

		while(rs.next()){
			mid = rs.getString("mid");
			table_list.add(mid);
		}
		stmt.close();
		rs.close();

		return table_list;
	}


	/*****************************************************************************
	 * approval_info ���̺��� ���õ� pid�� �ش��ϴ� ����ڿ� ������ ������ �����´�.
	 *****************************************************************************/
	public CaMasterTable getAppInfoByPid(String pid) throws Exception{
		CaMasterTable table = new CaMasterTable();
		Statement stmt = null;
		ResultSet rs = null;
		
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		
		//�˻����ǿ� �´� �Խù��� �����´�.
		String query = "SELECT writer,write_date,decision,decision_date FROM ca_approval_info WHERE pid = '" + pid + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			//����� ����
			String requestor_id = rs.getString("writer");
			userinfo = userDAO.getUserListById(requestor_id);
			String requestor_division = userinfo.getDivision();
			String requestor_rank = userinfo.getUserRank();
			String requestor_name = userinfo.getUserName();
			String requestor_info = requestor_division+"/"+requestor_rank+"/"+requestor_name;
			String request_date = rs.getString("write_date");

			//������ ����
			String approver_id = rs.getString("decision");
			userinfo = userDAO.getUserListById(approver_id);
			String approver_division = userinfo.getDivision();
			String approver_rank = userinfo.getUserRank();
			String approver_name = userinfo.getUserName();
			String approver_info = approver_division+"/"+approver_rank+"/"+approver_name;
			String approve_date = rs.getString("decision_date");

//			table = new CaMasterTable();

			table.setRequestorId(requestor_id);		//�Ƿ��� ���
			table.setRequestorInfo(requestor_info);	//�Ƿ��� ����
			table.setRequestDate(request_date);		//�Ƿ�����

			table.setApproverId(approver_id);		//������ ���
			table.setApproverInfo(approver_info);	//������ ����
			table.setApproveDate(approve_date);		//��������
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*************************************************************************************************
	 * �������簡 ���� ���ι����� ���ڵ��� ���������� ������Ʈ�Ѵ�.
	 *update �׸� : no_year,no_month,no_serial,approval_no,approver_id,approver_info,approve_date,aid
	 *************************************************************************************************/
	public void updateApprovalInfo(String mid,String no_year,String no_month,String no_serial,String approval_no,String approver_id,String approver_info,String approve_date,String aid) throws Exception{
		PreparedStatement pstmt = null;

		String query= "UPDATE ca_master SET no_year=?,no_month=?,no_serial=?,approval_no=?,approver_id=?,approver_info=?,approve_date=?,aid=? WHERE mid = '" + mid + "'";
		pstmt = con.prepareStatement(query);
			
		pstmt.setString(1,no_year);
		pstmt.setString(2,no_month);
		pstmt.setString(3,no_serial);
		pstmt.setString(4,approval_no);
		pstmt.setString(5,approver_id);
		pstmt.setString(6,approver_info);
		pstmt.setString(7,approve_date);
		pstmt.setString(8,aid);
			
		pstmt.executeUpdate();
		pstmt.close();
	}


	/*****************************************************************************
	 * ���õ� mid�� ���� ���ι����� item_no(ǰ���ȣ)�� �����´�.
	 *****************************************************************************/
	public String getItemNoByMid(String mid) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT item_no FROM ca_master WHERE mid = '" + mid + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		String item_no = "";
		while(rs.next()){
			item_no = rs.getString("item_no");
		}
		stmt.close();
		rs.close();

		return item_no;
	}

	/*****************************************************************************
	 * ���õ� mid�� ���� ���ι����� ancestor ������ �����´�.
	 * ��纯�� �ÿ� �ҽ� ������ ������ȣ�� �������� ����.
	 *****************************************************************************/
	public String getAncestor(String mid) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT ancestor FROM ca_master WHERE mid = '" + mid + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		String ancestor = "";
		while(rs.next()){
			ancestor = rs.getString("ancestor");
		}
		stmt.close();
		rs.close();

		return ancestor;
	}

	/*****************************************************************************
	 * ���õ� �ӽý��ι�ȣ ���� ������ ancestor ������ �����´�.
	 *****************************************************************************/
	public String getMidByTmpApprovalNo(String tmp_approval_no) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT mid FROM ca_master WHERE tmp_approval_no = '" + tmp_approval_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		String mid = "";
		while(rs.next()){
			mid = rs.getString("mid");
		}
		stmt.close();
		rs.close();

		return mid;
	}

	/*************************************************************************************************
	 * ���õ� ������ȣ�� ������ ���ι����� ��������� ������ ������ �����Ѵ�.
	 *************************************************************************************************/
	public void updateApproveType(String mid,String approve_type) throws Exception{
		PreparedStatement pstmt = null;

		String query= "UPDATE ca_master SET approve_type=? WHERE mid = '" + mid + "'";
		pstmt = con.prepareStatement(query);
			
		pstmt.setString(1,approve_type);
		
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*****************************************************************************
	 * history_info ���̺� �ش� ǰ���ȣ�� ���� �̷� ������ �Է��Ѵ�.
	 *****************************************************************************/
	 public void saveHistoryInfo(String item_no,String contents,String approve_date,String requestor_info) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO history_info (item_no,contents,apply_date,requestor_info) VALUES (?,?,?,?)";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_no);
		pstmt.setString(2,contents);
		pstmt.setString(3,approve_date);
		pstmt.setString(4,requestor_info);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*****************************************************************************
	 * ���õ� �ӽý��ι�ȣ�� ���� ���ι����� ����(E or M)�ڵ带 �����´�.
	 *****************************************************************************/
	public String getNoType(String no) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT no_type FROM ca_master WHERE tmp_approval_no = '" + no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		String no_type = "";
		while(rs.next()){
			no_type = rs.getString("no_type");
		}
		stmt.close();
		rs.close();

		return no_type;
	}

	/*****************************************************************************
	 * ���س⵵, ���õ� ���������� ���� ������ �ִ� �Ϸú�ȣ+1�� �����´�.
	 * ���� ���ι�ȣ�� ����ϴµ� �ʿ��� �Ϸù�ȣ�� ���ϱ� �����̴�.
	 * �ִ밪�� ���� ��� 001�� �����Ѵ�.
	 *****************************************************************************/
	public String getMaxSerialNo(String year,String no_type) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		
		String max_serial = "";
		String query = "SELECT MAX(no_serial) FROM ca_master WHERE no_type = '" + no_type + "' and no_year = '" + year + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){
			max_serial = rs.getString(1);
		}

		if(max_serial == null){
			max_serial = "001";
		}else{
			com.anbtech.util.normalFormat nf = new com.anbtech.util.normalFormat("000");
			max_serial = nf.toDigits(Integer.parseInt(max_serial)+1);		
		}
//System.out.println(max_serial);
		stmt.close();
		rs.close();

		return max_serial;
	}

	/*****************************************************************************
	 * ���õ� ������ȣ�� �ش��ϴ� ������ umask ���� �����´�.
	 *****************************************************************************/
	public String getUmask(String mid) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		
		String umask = "";
		String query = "SELECT umask FROM ca_master WHERE mid = '" + mid + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){
			umask = rs.getString("umask");
		}
		stmt.close();
		rs.close();

		return umask;
	}

	/*****************************************************************************
	 * ���õ� ������ȣ�� �ش��ϴ� ������ �����Ѵ�.
	 *****************************************************************************/
	public void dropApprovalInfo(String mid) throws Exception{

		Statement stmt = null;
		
		String query = "DELETE FROM ca_master WHERE mid = '" + mid + "'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);

		stmt.close();
	}

	/*****************************************************************************
	 * ���õ� ������ȣ�� �ش��ϴ� ������ �ӽý��ι�ȣ�� ������Ʈ�Ѵ�.
	 *****************************************************************************/
	public void updateTmpApprovalNo(String mid,String tmp_approval_no) throws Exception{

		Statement stmt = null;
		
		String query = "UPDATE ca_master SET tmp_approval_no = '" + tmp_approval_no + "' WHERE mid = '" + mid + "'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);

		stmt.close();
	}
}		