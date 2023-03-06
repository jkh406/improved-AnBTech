package com.anbtech.qc.business;

import com.anbtech.qc.entity.*;
import com.anbtech.qc.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;

public class QualityCtrlBO{

	private Connection con;

	public QualityCtrlBO(Connection con){
		this.con = con;
	}

	/**********************************************************
	 * 검사결과 등록 및 수정 폼
	 **********************************************************/
	public InspectionMasterTable getInspectionResultWriteForm(String mode,String request_no,String item_code,String login_id) throws Exception{
		QualityCtrlDAO qcDAO = new QualityCtrlDAO(con);
		InspectionMasterTable table = new InspectionMasterTable();
		
		table = qcDAO.getInspectionInfo(request_no,item_code);

		//검사의뢰번호 콤보리스트 만들기
		String pre_request_nos  = table.getPreRequestNo();
		String options = "";
		if(pre_request_nos != null){
			StringTokenizer nos = new StringTokenizer(pre_request_nos,"|");

			options = "<select name=\"pre_request_no\" onChange=\"view_pre_info('" + item_code + "');\"><option value=''>검사이력보기</option>";
			int no = nos.countTokens();
			while(nos.hasMoreTokens()) {
				String pre_request_no = nos.nextToken();
				options += "<option value='" + pre_request_no + "'";
//				if(request_no.equals(pre_request_no)) options += "selected ";
				options += ">" + no + "회:" + pre_request_no + "</option>";

				no--;
			}
			options += "</select>";
		}

		table.setPreRequestNo(options);

		return table;
	}

	/**********************************************************
	 * 재검사 등록 처리
	 **********************************************************/
	public void saveReworkItemInfo(String request_no,String item_code,String return_quantity,String serial_no_s,String serial_no_e) throws Exception{
		QualityCtrlDAO qcDAO = new QualityCtrlDAO(con);
		InspectionMasterTable table = new InspectionMasterTable();
		
		table = qcDAO.getInspectionInfo(request_no,item_code);

		//검사의뢰번호를 새로 발급한다.
		String new_request_no = qcDAO.getRequestNo("RTS");
		//이전검사의뢰번호
		String pre_request_no = table.getRequestNo() + "|" + table.getPreRequestNo();

		//에러제품에 대한 일련번호를 계산한다.
		//생성된 제품일련번호 중 뒤에서부터 불량품에 대한 일련번호를 할당한다.
		int bad_serial_no_e = Integer.parseInt(serial_no_e.substring(serial_no_e.length()-4));
		int bad_serial_no_s = bad_serial_no_e - Integer.parseInt(return_quantity) + 1;
		String bad_goods = serial_no_e.substring(0,serial_no_e.length()-4);

		DecimalFormat fmt = new DecimalFormat("0000");
		String bad_goods_serial_no_s = bad_goods + fmt.format(bad_serial_no_s);
		String bad_goods_serial_no_e = bad_goods + fmt.format(bad_serial_no_e);

		//재작업품목으로 등록한다.
		qcDAO.saveInspectionInfo(new_request_no,item_code,table.getItemName(),table.getItemDesc(),table.getSupplyerCode(),table.getSupplyerName(),table.getLotNo(),return_quantity,table.getRequesterDivCode(),table.getRequesterDivName(),table.getRequesterId(),table.getRequesterInfo(),table.getFactoryCode(),table.getFactoryName(),"S07",pre_request_no,bad_goods_serial_no_s,bad_goods_serial_no_e);
	}

	/*****************************************************************
	 * 쿼리문의 검색 조건을 만든다.
	 *****************************************************************/
	public String getWhere(String mode,String searchword,String searchscope,String category,String login_id) throws Exception{
		String where = "", where_and = "", where_sea = "", where_cat = "";

		if (searchword.length() > 0){
			if (searchscope.equals("request_no")){
				where_sea += "(request_no LIKE '%" +  searchword + "%')";
			}
			else if (searchscope.equals("item_code")){
				where_sea += "(item_code LIKE '%" +  searchword + "%')";
			}
			else if (searchscope.equals("inspection_result")){
				where_sea += "(inspection_result = '" +  searchword + "')";
			}

			where = " WHERE " + where_sea;
			if(mode.equals("list_inspect")) where += " AND (process_stat = 'S01')";
			else if(mode.equals("list_result")) where += " AND (process_stat = 'S05')";
			else if(mode.equals("list_return")) where += " AND (process_stat = 'S03')";
			else if(mode.equals("list_rework")) where += " AND (process_stat = 'S07')";
		}else{
			if(mode.equals("list_inspect")) where += " WHERE process_stat = 'S01'";
			else if(mode.equals("list_result")) where += " WHERE process_stat = 'S05'";
			else if(mode.equals("list_return")) where += " WHERE process_stat = 'S03'";
			else if(mode.equals("list_rework")) where += " WHERE process_stat = 'S07'";
		}

		return where;
	}

	/************************************
	 * 선택된 검사의뢰건,품목에 정의된 검사항목 리스트 가져오기
	 * 기존에 저장된 값이 있으면 값을 세팅해서 가져온다.
	 ************************************/
	public ArrayList getInspectionItemByItem(String mode,String request_no,String item_code,String login_id) throws Exception {
		InspectionItemByItemTable table = new InspectionItemByItemTable();
		InspectionResultTable result_table = new InspectionResultTable();
		QualityCtrlDAO qcDAO = new QualityCtrlDAO(con);
		
		//해당 품목코드에 정의된 검사항목 리스트를 가져온다.
		ArrayList list = new ArrayList();
		list = qcDAO.getInspectionItemByItem(mode,item_code,login_id);

		//각각의 검사항목에 대해서 기존에 저장된 정보가 있으면 세팅한다.
		ArrayList inspection_item_list = new ArrayList();

		Iterator item_iter = list.iterator();
		while(item_iter.hasNext()){
			table = (InspectionItemByItemTable)item_iter.next();

			String inspection_code = table.getInspectionCode();
			result_table = qcDAO.getInspectionResult(request_no,item_code,inspection_code);

			table.setSampledQuantity(result_table.getSampledQuantity());
			table.setGoodQuantity(result_table.getGoodQuantity());
			table.setBadQuantity(result_table.getBadQuantity());
	
			inspection_item_list.add(table);
		}

		return inspection_item_list;
	}

	/************************************
	 * 선택된 검사의뢰건,품목,검사항목에 저장된 검사내역 가져오기
	 ************************************/
	public ArrayList getInspectionValueList(String request_no,String item_code,String inspection_code,String sampled_quantity) throws Exception {
		QualityCtrlDAO qcDAO = new QualityCtrlDAO(con);
		ArrayList list = new ArrayList();
		
		//저장된 데이터가 있으면 가져온다.
		String values = qcDAO.getInspectionValue(request_no,item_code,inspection_code);

		java.util.StringTokenizer stokens = new java.util.StringTokenizer(values,"#");
		int tokens_count = stokens.countTokens();


		for(int j = 0 ; j<Integer.parseInt(sampled_quantity) ; j++){
			String[] value = new String[2];

			if(j < tokens_count){
				values = stokens.nextToken();
				ArrayList value_list = com.anbtech.util.Token.getTokenList(values); 
				value[0] = (String)value_list.get(0);	//측정값
				value[1] = (String)value_list.get(1);	//합격여부(Y or N)
			}else{
				value[0] = "";	//측정값
				value[1] = "Y";	//합격여부(Y or N)
			}
			list.add(value);
		}

		return list;
	}


	/*********************************************
	 * 제품의 시리얼 번호 생성하기
	 *********************************************/
	public String[] getGoodsSerialNo(String produce_year,String produce_month,String item_code,String produce_quantity) throws Exception{
		QualityCtrlDAO qcDAO = new QualityCtrlDAO(con);
		com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

		//모델코드 가져오기
		String model_code = cmDAO.getModelCode(item_code);
		//시작 일련번호 가져오기
		String serial_no_s = qcDAO.getGoodsSerialNoS(produce_year,produce_month,item_code);
		//종료 일련번호 계산
		DecimalFormat fmt = new DecimalFormat("0000");
		String serial_no_e = fmt.format(Integer.parseInt(serial_no_s) + Integer.parseInt(produce_quantity) - 1);

		//일련번호 계산
		String[] goods_serial = new String[2];
		goods_serial[0] = produce_year + produce_month + model_code + serial_no_s;
		goods_serial[1] = produce_year + produce_month + model_code + serial_no_e;

		return goods_serial;
	}

	/*****************************************************************
	 * 검색조건 문자열이 직접 넘어오는 경우의 where 구문을 만든다.
	 * 불량정보 검색시
	 *****************************************************************/
	public String getWhereForFailureInfoList(String mode,String where_str,String login_id) throws Exception{

		String where = "";
		String where_cat = "";
		String where_sea = "";
		String where_mode = "";
		
		if (where_str.length() > 0){
			//where_str = "subject|제목,writer|작성자, ..... 형태로 넘어옴.

			StringTokenizer str = new StringTokenizer(where_str, ",");
			int scope_count = str.countTokens();
			String scope[] = new String[scope_count];
			String[][] search = new String[scope_count][2];

			for(int i=0; i<scope_count; i++){ 
				scope[i] = str.nextToken();

				StringTokenizer str2 = new StringTokenizer(scope[i],"|");

				for(int j=0; j<2; j++){ 
					search[i][j] = str2.nextToken();

				}
			}
			
			if(search[0][0].equals("inspect_date")){
				//inspect_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
				String s_date = search[0][1].substring(0,4) + "-" + search[0][1].substring(4,6) + "-" + search[0][1].substring(6,8);
				String e_date = search[0][1].substring(8,12) + "-" + search[0][1].substring(12,14) + "-" + search[0][1].substring(14,16);
				where_sea += "(inspect_date >= '" + s_date + "' and inspect_date <= '" + e_date +"')";
			}else{
				where_sea += "(" + search[0][0] + " LIKE '%" + search[0][1] + "%') ";
			}

			for(int i = 1; i< scope_count; i++){
				if(search[i][0].equals("inspect_date")){
					//inspect_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.

					String s_date = search[i][1].substring(0,4) + "-" + search[i][1].substring(4,6) + "-" + search[i][1].substring(6,8);
					String e_date = search[i][1].substring(8,12) + "-" + search[i][1].substring(12,14) + "-" + search[i][1].substring(14,16);
					where_sea += "AND (inspect_date >= '" + s_date + "' and inspect_date <= '" + e_date +"')";
				}else{
					where_sea += "AND (" + search[i][0] + " LIKE '%" + search[i][1] + "%') ";
				}
			}
			where = " WHERE " + where_sea;
		}

		return where;
	}
}