package com.anbtech.ca.business;

import com.anbtech.ca.entity.*;
import com.anbtech.ca.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;

public class ComponentApprovalBO{

	private Connection con;

	public ComponentApprovalBO(Connection con){
		this.con = con;
	}
	

	/*****************************************************************
	 * 등록의뢰 구분 코드에 대응하는 이름를 가져온다.
	 *****************************************************************/
	public String getRequestCodeName(String code) throws Exception{
		String code_name = "";

		if(code.equals("1")) code_name = "신규";
		else if(code.equals("2")) code_name = "사양변경";
		else if(code.equals("3")) code_name = "업체추가";
		else if(code.equals("4")) code_name = "부품승인취소";
		else if(code.equals("5")) code_name = "업체승인취소";

		return code_name;
	}

	/*****************************************************************
	 * 승인 판정 코드에 대응하는 이름을 가져온다.
	 *****************************************************************/
	public String getApproveCodeName(String code) throws Exception{

		String code_name = "";

		if(code.equals("A")) code_name = "합격";
		else if(code.equals("B")) code_name = "한정승인";
		else if(code.equals("C")) code_name = "RESERVED";
		else if(code.equals("D")) code_name = "RESERVED";
		else if(code.equals("E")) code_name = "RESERVED";
		else if(code.equals("F")) code_name = "사용금지";

		return code_name;
	}

	/*****************************************************************
	 * 품목단위 코드에 대응하는 이름을 가져온다.
	 *****************************************************************/
	public String getUnitCodeName(String code) throws Exception{

		String code_name = "";

		if(code.equals("EA")) code_name = "개";

		return code_name;
	}

	/*****************************************************************
	 * 첨부문서 종류 코드에 대응하는 이름을 가져온다.
	 *****************************************************************/
	public String getAttachFileCodeName(String code) throws Exception{

		String code_name = "";

		if(code.equals("1")) code_name = "승인원";
		else if(code.equals("2")) code_name = "시험/검사 성적서";
		else if(code.equals("3")) code_name = "부품사양서";
		else if(code.equals("4")) code_name = "데이터북";
		else if(code.equals("5")) code_name = "도면";
		else if(code.equals("6")) code_name = "견본품";
		else if(code.equals("7")) code_name = "카달로그";
		else if(code.equals("8")) code_name = "RESERVED";
		else if(code.equals("9")) code_name = "RESERVED";
		else if(code.equals("0")) code_name = "기타";

		return code_name;
	}

	/**********************************************************
	 * 선택한 승인문건의 관리번호에 해당하는 정보를 가져와서 출력폼으로 만든다.
	 **********************************************************/
	public CaMasterTable getViewForm(String no) throws Exception{

		CaMasterTable table = new CaMasterTable();
		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);
		table = caDAO.getApprovalInfoByMid(no);

		//첨부파일 리스트 가져오기
		String mid = table.getMid();
		String umask = table.getUmask();

		Iterator file_iter = caDAO.getFile_list(mid).iterator();

		int i = 1;
		String filelink = "&nbsp;";
		String filepreview = "<TABLE CELLPADDING=1 CELLPADDING=0>";
		while(file_iter.hasNext()){
			CaMasterTable file = (CaMasterTable)file_iter.next();
			filelink += "<a href='ComponentApprovalServlet?mode=download&no="+mid+"_"+i+"&umask="+umask+"_"+i+"'";
			filelink += "onMouseOver=\"window.status='Download "+file.getFileName()+" ("+file.getFileSize()+" bytes)';return true;\" ";
			filelink += "onMouseOut=\"window.status='';return true;\" >";
			filelink += "<img src='../ca/mimetype/" + com.anbtech.util.Module.getMIME(file.getFileName(), file.getFileType()) + ".gif' border=0> "+file.getFileName()+"</a>";
			filelink += " - "+file.getFileSize()+" bytes <br>&nbsp;";

			if (file.getFileType().indexOf("mage")>0){
				filepreview = filepreview + "<TR><TD><font size=1>"+file.getFileName()+"</font><br><img src='ComponentApprovalServlet?mode=download&no="+mid+"_"+i+"' border=1></TD></TR>";
			}
			i++;
		}
		filepreview = filepreview + "</TABLE>";
		table.setFileLink(filelink);
		table.setFilePreview(filepreview);

		return table;
	}


	/**********************************************************
	 * 신규,다중,수정 시의 입력폼을 만든다.
	 * no:관리번호,item_no:품목번호
	 **********************************************************/
	public CaMasterTable getWriteForm(String mode,String no,String item_no) throws Exception{

		CaMasterTable table = new CaMasterTable();
		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");

		//멀티 등록 모드일 경우에는 임시승인번호를 가지고 mid를 구해온다.
		if(mode.equals("write_m")) no = caDAO.getMaxMid(no);

		if(mode.equals("write_s") || mode.equals("write_m") || mode.equals("write_r") || mode.equals("modify") || mode.equals("write_a")){
			table = caDAO.getApprovalInfoByMid(no);
		}

		if(mode.equals("write_s") || mode.equals("write_m") || mode.equals("write_r") || mode.equals("write_a")){
			table.setRequestDate(vans.format(now));
		}

		//멀티 등록 모드일 경우
		if(mode.equals("write_m")){
			table.setItemNo("");		// 품목번호 초기화
			table.setItemName("");		// 품목명 초기화
			table.setItemDesc("");		// 품목설명 초기화
			table.setMakerPartNo("");	// 업체부품번호 초기화
			table.setWhyApprove("");	// 판정사유 초기화
			table.setApproveType("");	// 승인구분
			table.setApplyQuantity("");	// 적용수량
			table.setApplyDate("");		// 적용일자

		}else if(mode.equals("write_a")){
			table.setMakerCode("");
			table.setMakerName("");
			table.setMakerPartNo("");
			table.setPrjCode("");
			table.setPrjName("");
			table.setModelCode("");
			table.setModelName("");
		}

		return table;
	}


	/******************************************
	 * 신규 등록 시 첨부파일을 처리한다.
	 ******************************************/
	public CaMasterTable getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception{

		String filename = "";
		String filetype = "";
		String filesize = "";
		String did = "";

		int i = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();
			String name = "attachfile"+i;
			//filename과 type
			String fname = multi.getFilesystemName(name);
			if(fname != null){
				String ftype = multi.getContentType(name);

				//file의 사이즈를 재기위한것
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir, no+"_"+i+".bin");
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);

				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize +fsize + "|";
			}
			i++;
		}
		CaMasterTable file = new CaMasterTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	} //getFile_frommulti()


	/******************************************
	 * 수정 시 첨부파일을 처리한다.
	 ******************************************/
	public CaMasterTable getFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

		Iterator file_iter = file_list.iterator();
		String filename = "";
		String filetype = "";
		String filesize = "";
		String did = "";

		int i = 1,j = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();

			CaMasterTable file = new CaMasterTable();
			if(file_iter.hasNext()) file = (CaMasterTable)file_iter.next();

			String deletefile = multi.getParameter("deletefile"+i);
			String name = "attachfile"+i;

			//filename과 type
			String fname = multi.getFilesystemName(name);
			if(fname != null){
				String ftype = multi.getContentType(name);

				//file의 사이즈를 재기위한것
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir, no+"_"+j+".bin");
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);

				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize + fsize + "|";
				j++;
			}else 	if(deletefile != null){
				File myDir = new File(filepath);
				File delFile = new File(myDir, no+"_"+i+".bin");
				if(delFile.exists()) delFile.delete();
			}else 	if(file.getFileName() != null){
				File myDir = new File(filepath);
				File chFile = new File(myDir, no+"_"+i+".bin");
				File myFile = new File(myDir, no+"_"+j+".bin");
				chFile.renameTo(myFile);

				filename = filename + file.getFileName() + "|";
				filetype = filetype + file.getFileType() + "|";
				filesize = filesize + file.getFileSize() + "|";
				j++;
			}
			i++;
		}
		CaMasterTable file = new CaMasterTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	}//getFile_frommulti()


	/**************************************************
	 * 첨부파일을 다운로드하기 위한 파일 리스트 출력
	 **************************************************/
	public CaMasterTable getFile_fordown(String no) throws Exception{

		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));

		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);
		Iterator file_iter = caDAO.getFile_list(fileno).iterator();

		String filename="",filetype="",filesize="",did="";
		int i = 1;
		while (file_iter.hasNext()){
			CaMasterTable file = (CaMasterTable)file_iter.next();
			if(i == Integer.parseInt(file_no)){
				filename = file.getFileName();
				filetype = file.getFileType();
				filesize = file.getFileSize();
			}else{
			}
			i++;
		}
		CaMasterTable file = new CaMasterTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	}


	/**************************************************
	 * 첨부파일 정보를 DB에 저장하기 위한 쿼리문 생성
	 **************************************************/
	public void updFile(String umask, String filename, String filetype, String filesize) throws Exception{
		String set = " SET fname='"+filename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
		String where = " WHERE umask = '" + umask + "'";

		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);
		caDAO.updTable(set, where);
	}


	/*****************************************************************
	 * 쿼리문의 검색 조건을 만든다.
	 *****************************************************************/
	public String getWhere(String mode,String searchword, String searchscope, String category) throws Exception{

		String where = "", where_and = "", where_sea = "";

		if (searchword.length() > 0){
			if (searchscope.equals("approval_no")){			// 승인번호
				where_sea += "( approval_no LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("item_no")){		// 품목번호
				where_sea += "( item_no LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("maker_part_no")){	// 업체부품번호
				where_sea += "( maker_part_no LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("maker_name")){		// 업체코드
				where_sea += "( maker_name LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("approve_type")){	// 승인판정코드
				where_sea += "( approve_type = '" +  searchword + "' )";
			}
			else if (searchscope.equals("approver_info")){	// 승인자 이름
				where_sea += "( approver_info LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("requestor_info")){	// 의뢰자 이름
				where_sea += "( requestor_info LIKE '%" +  searchword + "%' )";
			}

			where = " WHERE " + where_sea + " and aid not in('EN','EE')";
		}
		else{
			where = " WHERE aid not in('EN','EE')";
		}

		if(mode.equals("list")) where += " and (approve_type in('A','B'))";

		return where;
	}

	/*****************************************************************
	 * 검색조건 문자열이 직접 넘어오는 경우의 where 구문을 만든다.
	 *****************************************************************/
	public String getWhere(String mode,String category,String where_str) throws Exception{

		String where = "";
		String where_cat = "";
		String where_sea = "";
		String where_mode = "";
		
		if(category.length() > 0) where_cat = "category like '"+category+"%' ";

		if (where_str.length() > 0){
			//where_str = "and|approval_no|E03-009,and|item_no|320200010, ..... 형태로 넘어옴.

			StringTokenizer str = new StringTokenizer(where_str, ",");
			int scope_count = str.countTokens();
			String scope[] = new String[scope_count];
			String[][] search = new String[scope_count][3];

			for(int i=0; i<scope_count; i++){ 
				scope[i] = str.nextToken();

				StringTokenizer str2 = new StringTokenizer(scope[i],"|");

				for(int j=0; j<3; j++){ 
					search[i][j] = str2.nextToken();

				}
			}
			
			for(int i = 0; i< scope_count-1; i++){
				if(search[i][1].equals("approve_date")){
					//s_day = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.

					String s_day = search[i][2].substring(0,8);		// 시작일 
					String e_day = search[i][2].substring(8,16);	// 종료일
					where_sea += " (approve_date >= '" + s_day + "' and approve_date <= '" + e_day +"') " + search[i][0];
				}else{
					where_sea += " (" + search[i][1] + " like '%" + search[i][2] + "%') " + search[i][0];
				}
			}
			// 마지막 검색항목에는 and를 붙이지 않기 위해 따로 처리함.
			if(search[scope_count-1][1].equals("approve_date")){
				String s_day = search[scope_count-1][2].substring(0,8);		// 시작일 
				String e_day = search[scope_count-1][2].substring(8,16);	// 종료일
				where_sea += " (approve_date >= '" + s_day + "' and approve_date <= '" + e_day +"')";
			}else{
				where_sea += " (" + search[scope_count-1][1] + " like '%" + search[scope_count-1][2] + "%')";
			}
			where = " WHERE " + where_cat + where_sea;

			//결재가 완료된 문건만 나타나게
			where += " and aid not in('EN','EE')";
		}else{
			//결재가 완료된 문건만 나타나게
			where += " WHERE aid not in('EN','EE')";		
		}

		if(mode.equals("list")) where += " and (approve_type in('A','B'))";

		return where;
	}


	/*****************************************************************************************************
	 * 임시승인번호(no)를 가지는 승인정보 리스트를 가져온 후, 각각에 정식승인번호와 결재정보를 입력한다.
	 * update 항목 : no_year,no_month,no_serial,approval_no,approver_id,approver_info,approve_date,aid
	 *****************************************************************************************************/
	public void updateApprovalAndSaveHistoryInfo(String write_type,String no,String approval_no,String pid) throws Exception{

		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);

		// 1.승인번호를 서브스트링하여 필요한 정보 추출
		String no_year = approval_no.substring(1,3);
		String no_month = approval_no.substring(3,5);
		String no_serial = approval_no.substring(6,9);

		// 2.approver_info 테이블에서 pid == aid 에 해당하는 레코드 정보를 가져온다.(결재정보만)
		CaMasterTable table =  new CaMasterTable();
		table = caDAO.getAppInfoByPid(pid);

		String requestor_id = table.getRequestorId();
		String requestor_info = table.getRequestorInfo();
		String request_date = table.getRequestDate();

		String approver_id = table.getApproverId();
		String approver_info = table.getApproverInfo();

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String approve_date = vans.format(now);

		// 3.임시승인번호(no)를 갖는 승인정보의 mid 리스트를 가져온다.
		Iterator mid_iter = caDAO.getMidListByTmpAppNo(no).iterator();

		// 4.각각의 승인문건의 정보를 업데이트하고, history_info 테이블에 이력을 추가한다.
		while(mid_iter.hasNext()){
			String mid = (String)mid_iter.next();
			
			//ca_master 테이블의 승인정보 업데이트
			caDAO.updateApprovalInfo(mid,no_year,no_month,no_serial,approval_no,approver_id,approver_info,approve_date,pid);

			//ca_master 테이블에서 mid 에 해당하는 레코드의 item_no 값을 가져온다.
			String item_no = caDAO.getItemNoByMid(mid);

			//history_info 테이블에 이력 추가
			String contents = "";
			if(write_type.equals("report_w")){
				contents = "승인업체 등록(승인번호:" + approval_no + ",승인자:" + approver_info + ")";
			}else if(write_type.equals("report_r")){
				contents = "사양변경 등록(승인번호:" + approval_no + ",승인자:" + approver_info + ")";
			}
			caDAO.saveHistoryInfo(item_no,contents,approve_date,requestor_info);

			//사양변경일 경우 이전 승인문건의 판정등급을 F로 처리한다.
			if(write_type.equals("report_r")){
				String ancestor = caDAO.getAncestor(mid);
				caDAO.updateApproveType(ancestor,"F");
			}
			
	//		System.out.println(contents);
	//		System.out.println(approve_date);
	//		System.out.println(requestor_info);
		}
	}


	/*****************************************************************************************************
	 * 선택된 관리번호를 가지는 승인문건을 사용금지 처리한다.
	 *****************************************************************************************************/
	public void deleteApprovalAndSaveHistoryInfo(String no,String pid) throws Exception{

		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);

		// approver_info 테이블에서 pid == aid 에 해당하는 레코드 정보를 가져온다.(결재정보만)
		CaMasterTable table =  new CaMasterTable();
		table = caDAO.getAppInfoByPid(pid);

		String requestor_id = table.getRequestorId();
		String requestor_info = table.getRequestorInfo();
		String request_date = table.getRequestDate();

		String approver_id = table.getApproverId();
		String approver_info = table.getApproverInfo();
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String approve_date = vans.format(now);

		//삭제대상 문건의 판정등급을 F 처리한다.
		caDAO.updateApproveType(no,"F");
		
		//ca_master 테이블에서 mid 에 해당하는 레코드의 item_no 값을 가져온다.
		String item_no = caDAO.getItemNoByMid(no);

		//history_info 테이블에 이력 추가
		String contents = "승인 취소에 의한 부품사용금지(승인자:" + approver_info + ")";
		caDAO.saveHistoryInfo(item_no,contents,approve_date,requestor_info);
	}

	/*****************************************************************************************************
	 * 정식 승인번호를 계산하여 리턴한다.
	 *****************************************************************************************************/
	public String getApprovalNo(String no) throws Exception{

		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);

		//현재 년도 계산
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyMM");
		String now_year	= vans.format(now).substring(0,2);
		String now_month = vans.format(now).substring(2,4);

		//승인원 종류코드(E or M)을 가져온다.
		String no_type = caDAO.getNoType(no);

		//일련번호 계산
		String no_serial = caDAO.getMaxSerialNo(now_year,no_type);

		//정식 승인번호 계산
		String approval_no = no_type + now_year + now_month + "-" + no_serial;

		return approval_no;

	}

	/*****************************************************************************************************
	 * 선택된 임시승인번호를 가지는 문건의 리스트를 가져온다. (전자결재용 화면 출력을 위함)
	 *****************************************************************************************************/
	public ArrayList getApprovalList(String no) throws Exception{

		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);

		ArrayList approval_info = new ArrayList();
		// 임시승인번호(no)를 갖는 승인정보의 mid 리스트를 가져온다.
		Iterator mid_iter = caDAO.getMidListByTmpAppNo(no).iterator();

		// 각각의 승인문건의 정보를 
		while(mid_iter.hasNext()){
			String mid = (String)mid_iter.next();
			CaMasterTable table = new CaMasterTable();
			
			table = caDAO.getApprovalInfoByMid(mid);

			//첨부파일 리스트 가져오기
			String umask = table.getUmask();
			Iterator file_iter = caDAO.getFile_list(mid).iterator();

			int i = 1;
			String filelink = "&nbsp;";
			String filepreview = "<TABLE CELLPADDING=1 CELLPADDING=0>";
			while(file_iter.hasNext()){
				CaMasterTable file = (CaMasterTable)file_iter.next();
				filelink += "<a href='ComponentApprovalServlet?mode=download&no="+mid+"_"+i+"&umask="+umask+"_"+i+"'";
				filelink += "onMouseOver=\"window.status='Download "+file.getFileName()+" ("+file.getFileSize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../ca/mimetype/" + com.anbtech.util.Module.getMIME(file.getFileName(), file.getFileType()) + ".gif' border=0> "+file.getFileName()+"</a>";
				filelink += " - "+file.getFileSize()+" bytes <br>&nbsp;";

				if (file.getFileType().indexOf("mage")>0){
					filepreview = filepreview + "<TR><TD><font size=1>"+file.getFileName()+"</font><br><img src='ComponentApprovalServlet?mode=download&no="+mid+"_"+i+"' border=1></TD></TR>";
				}
				i++;
			}
			filepreview = filepreview + "</TABLE>";
			table.setFileLink(filelink);
			table.setFilePreview(filepreview);

			approval_info.add(table);
		}

		return approval_info;
	}

	/*****************************************************************************
	 * 선택된 관리번호에 해당하는 문건을 삭제한다.
	 * mids = 34|24|45| 의 형태로 넘어온다.
	 *****************************************************************************/
	public void dropApprovalInfos(String mids) throws Exception{
		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);
		ArrayList mid_list = com.anbtech.util.Token.getTokenList(mids);

		int i = 0;
		while(i < mid_list.size()){
			String mid = (String)mid_list.get(i);
			caDAO.dropApprovalInfo(mid);
			i++;
		}
	}

	/*****************************************************************************
	 * 선택된 관리번호에 해당하는 문건의 임시승인번호를 업데이트한다.
	 * mid = 34|24|45| 의 형태로 넘어온다.
	 *****************************************************************************/
	public void updateApprovalInfos(String mids,String tmp_approval_no) throws Exception{
		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);
		ArrayList mid_list = com.anbtech.util.Token.getTokenList(mids);

		int i = 0;
		while(i < mid_list.size()){
			String mid = (String)mid_list.get(i);
			caDAO.updateTmpApprovalNo(mid,tmp_approval_no);
			i++;
		}
	}

}