package com.anbtech.dms.business;

import com.anbtech.dms.entity.*;
import com.anbtech.dms.db.*;
import com.anbtech.dms.admin.*;
import com.oreilly.servlet.MultipartRequest;

import java.sql.*;
import java.util.*;
import java.io.*;

public class MasterBO{

	private Connection con;

	public MasterBO(Connection con){
		this.con = con;
	}

	/*****************************************************************
	 * 새문서등록,리비젼,수정 모드로 들어갈 때 master_data 내용을 가져와
	 * MasterTable 빈즈에 넣는다.
	 *****************************************************************/
	public MasterTable getWrite_form(String tablename,String no,String mode,String category,String why_revision) throws Exception{

		int m_id;				// 관리번호
		String doc_no;			// 문서번호
		String category_id;		// 카테고리 코드
		String where_category;	// 현재 카테고리의 분류 표시
		String data_id;			// 문서 관리번호(최초버젼의 것)
		String subject;			// 문서 제목
		String writer;			// 작성자(최종버젼)
		String written_day;		// 작성일(최종버젼)
		String register;		// 등록자(최종버젼)
		String register_day;	// 등록일(최종버젼)
		String search_keyword;	// 검색어
		String hit;				// 열람횟수
		String curr_version;	// 현재버젼
		String last_version;	// 최종버젼
		String stat;			// 문서의 현재 상태


		MasterTable master = new MasterTable();
		MasterDAO masterDAO = new MasterDAO(con);		
		com.anbtech.pjt.db.pjtDocumentDAO pjt = new com.anbtech.pjt.db.pjtDocumentDAO(con);
		com.anbtech.gm.entity.GoodsInfoTable goods = new com.anbtech.gm.entity.GoodsInfoTable();
		com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);

		if ("write".equals(mode)){ //신규문서일 경우
			curr_version  = "1.0";
			master.setLastVersion(curr_version);
			master.setModelCode("");
			master.setModelName("");
			master.setPjtCode("");
			master.setPjtName("");
			master.setNodeCode("");
			master.setNodeName("");	
		}else if (("modify".equals(mode) || "modify_a".equals(mode) || "revision".equals(mode)) && no != null){ // 수정일 경우
			master = masterDAO.getMasterData(tablename, no);

			String pjt_name = pjt.getProjectName(master.getPjtCode());
			String node_name = pjt.getDocumentName(master.getPjtCode(),master.getNodeCode());
			goods = goodsDAO.getGoodsInfoByCode(master.getModelCode());
			String model_name = goods.getGoodsName();

			master.setPjtName(pjt_name);
			master.setNodeName(node_name);
			master.setModelName(model_name);

		}
		// 넘어온 카테고리 코드를 가지고 현재 카테고리의 상위 분류를 나타내는 문자열을 만든다.
		String where = "";
		com.anbtech.dms.admin.makeDocCategory view_category = new com.anbtech.dms.admin.makeDocCategory();
		where_category = view_category.viewCategory(Integer.parseInt(category),where);

		master.setWhereCategory(where_category);

		return master;
	}

	/*****************************************************************
	 * no에 해당하는 테이블 내용을 가져와서 출력형태로 변환시킨다.
	 *****************************************************************/
	public MasterTable getData(String tablename,String no) throws Exception{

		int m_id;				// 관리번호
		String doc_no;			// 문서번호
		String category_id;		// 카테고리 코드
		String where_category;	// 현재 카테고리의 분류 표시
		String data_id;			// 문서 관리번호(최초버젼의 것)
		String subject;			// 문서 제목
		String writer;			// 작성자(최종버젼)
		String written_day;		// 작성일(최종버젼)
		String register;		// 등록자(최종버젼)
		String register_day;	// 등록일(최종버젼)
		String search_keyword;	// 검색어
		String hit;				// 열람횟수
		String curr_version;	// 현재버젼
		String last_version;	// 최종버젼
		String stat;			// 문서의 현재 상태

		MasterTable master = new MasterTable();
		MasterDAO masterDAO = new MasterDAO(con);
		com.anbtech.pjt.db.pjtDocumentDAO pjt = new com.anbtech.pjt.db.pjtDocumentDAO(con);
		com.anbtech.gm.entity.GoodsInfoTable goods = new com.anbtech.gm.entity.GoodsInfoTable();
		com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);

		master = masterDAO.getMasterData(tablename, no);

		//카테고리 분류 표시 문자열 생성 및 저장
		String category = master.getCategoryId();
		String where = "";
		com.anbtech.dms.admin.makeDocCategory view_category = new com.anbtech.dms.admin.makeDocCategory();
		where_category = view_category.viewCategory(Integer.parseInt(category),where);
		master.setWhereCategory(where_category);

		String pjt_name = pjt.getProjectName(master.getPjtCode());
		String node_name = pjt.getDocumentName(master.getPjtCode(),master.getNodeCode());
		goods = goodsDAO.getGoodsInfoByCode(master.getModelCode());
		String model_name = goods.getGoodsName();

		master.setPjtName(pjt_name);
		master.setNodeName(node_name);
		master.setModelName(model_name);

		return master;
	} //getData()

	/*****************************************************************
	 * 최신버젼의 문서에 대한 쿼리문의 검색 조건을 만든다.
	 * EDMS 전체를 검색시에 사용된다. 따라서, 상세검색은 되지 않는다.
	 *****************************************************************/
	public String getWhere(String mode,String searchword, String searchscope, String category) throws Exception{

		//검색조건에 맞게 where변수를 수정한다.
		String where = "", where_cat = "", where_and = "", where_sea = "";
		if(category.length() > 0) where_cat = "category_id like '"+category+"%'";
		if (searchword.length() > 0){
			if ("subject".equals(searchscope)){
				where_sea = "( subject like '%" +  searchword + "%' )";
			}
			else if ("writer_s".equals(searchscope)){
				where_sea = "( writer_s like '%" +  searchword + "%' )";
			}
			else if ("register_s".equals(searchscope)){
				where_sea = "( register_s like '%" +  searchword + "%' )";
			}
			else if ("doc_no".equals(searchscope)){
				where_sea = "( doc_no like '%" +  searchword + "%' )";
			}
			else if ("model_code".equals(searchscope)){
				where_sea = "( model_code like '" +  searchword + "%' )";
			}
		}
		if(category.length() > 0 && searchword.length() > 0) where_and = " and ";
		if(category.length() > 0 || searchword.length() > 0) where = " WHERE " + where_cat + where_and + where_sea;

		String where_mode = "";

		//결재가 완료된 문서일 경우
		if(mode.equals("list")) where_mode = " and stat = '5'";

		//최종승인 득후, 문서관리자 확인중인 문서
		else if(mode.equals("processing")) where_mode = " and stat = '3'";

		where = where + where_mode;
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
		
		if(category.length() > 0) where_cat = "category_id like '"+category+"%' ";
		if (where_str.length() > 0){
			//where_str = "and|subject|제목,or|writer|작성자, ..... 형태로 넘어옴.

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
			
			for(int i = 0; i< scope_count; i++){
				if(search[i][1].equals("register_day")){
					//s_day = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.

					String s_day = search[i][2].substring(0,8);		// 시작일 
					String e_day = search[i][2].substring(8,16);	// 종료일
					where_sea += search[i][0] + " (register_day_s >= '" + s_day + "' and register_day_s <= '" + e_day +"') ";
				}else{
					where_sea += search[i][0] + " (" + search[i][1] + " like '%" + search[i][2] + "%') ";
				}
			}

		}
		
		where = " WHERE " + where_cat + where_sea;

		//결재가 완료된 문서일 경우
		if(mode.equals("list")) where_mode = " and stat = '5'";
		//최종승인 득후, 문서관리자 확인중인 문서
		else if(mode.equals("processing")) where_mode = " and stat = '3'";

		where = where + where_mode;

		return where;
	}


	/*****************************************************************
	 * 자신이 등록한 문서 중 상신되기 전의 문서(stat == 1) 및 
	 * 전자결재에서 반려된 문서를 가져온다.
	 * 상신하기 전에 수정 또는 삭제할 수 있도록 하기 위함.
	 *****************************************************************/
	public String getWhere(String login_id) throws Exception{

		String where = " WHERE writer = '" + login_id + "' and stat in ('1','4') ";
		return where;
	}

	/*****************************************************************
	 * 상태코드를 받아서 상태 문자열을 만들어 준다.
	 *****************************************************************/
	public String getStatus(String stat) throws Exception{

		String status = "";

		if(stat.equals("1"))	  status = "등록대기";	// 상신 전
		else if(stat.equals("2")) status = "결재중";	// 결재 진행중
		else if(stat.equals("3")) status = "승인완료";	// 최종 승인 완료,문서 관리자 확인 대기
		else if(stat.equals("4")) status = "반려";		// 전자결재에서 반려된 문서
		else if(stat.equals("5")) status = "정상";		// 문서 관리자 확인까지 득한 문서
		else if(stat.equals("6")) status = "대출중";
		else if(stat.equals("7")) status = "Reserved";
		else if(stat.equals("8")) status = "Reserved";
		else if(stat.equals("9")) status = "삭제됨";	// 최종 승인이 완료된 후에 삭제 처리된 문서

		return status;
	}

}