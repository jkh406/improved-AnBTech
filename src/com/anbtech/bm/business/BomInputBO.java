package com.anbtech.bm.business;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class BomInputBO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.bm.db.BomShowDAO showDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	private com.anbtech.util.SortArray sort = new com.anbtech.util.SortArray();		//배열정렬하기
	
	private String query = "",update="";
	private String[][] item = null;				//TREE정보를 배열로 담기
	private int an = 0;							//items의 배열 갯수

	private String[][] plist = null;			//읽은 파일내용을 배열에 담기 
	private int elecnt=0;						//읽은 파일의 한 라인당 데이터 갯수 
	private int linecnt=0;						//읽은 파일의 라인갯수 

	private String assy_ini = "1";				//ASSY코드 시작 첫문자
	private String phantom = "1PH";				//팬텀 ASSY코드 

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public BomInputBO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
		showDAO = new com.anbtech.bm.db.BomShowDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		BOM STRUCTURE 에 관한 메소드 정의
	//			등록/수정/삭제 
	//			BOM TREE보기
	//---------------------------------------------------------------------

	//*******************************************************************
	// MBOM_STR에 데이터 입력하기
	//*******************************************************************/	
	public String insertStr(String gid,String parent_code,String child_code,
		String location,String op_code,String qty_unit,String qty,String part_cnt) throws Exception
	{
		String input="",data="",where="",bom_status="",assy_dup="";
		int cnt = Integer.parseInt(part_cnt);
		String[] part = new String[6];
		for(int i=0; i<6; i++) part[i] = "";		//초기화

		//검사1 BOM상태검사 : BOM상태를 판단하여 Template상태이면 리턴함
		where = "where pid='"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "공정템플릿 구성 상태입니다. 공정Assy코드로 등록후 진행하십시오.";
			return data;
		}

		//LEVEL NO구하기 : Assy 중복코드[assy_dup='D']는 제외함.
		query = "SELECT level_no FROM MBOM_STR where gid='"+gid+"' and child_code='"+parent_code+"'";
		query += " and assy_dup != 'D'";
		int level_no = modDAO.getLevelNo(query);

		//검사2 모품목검사 : PARENT_CODE가 해당모델구조체계에 있는지 검사하기
		if(level_no == 0) {
			data = "모품목코드["+parent_code+"]가 해당BOM내에 없거나 Phantom Assy공정코드입니다. 확인후 다시 입력하십시요.";
			return data;
		}

		//검사3 모품목검사 : 이미 다른모델에 적용된 경우는 설변을 이용해야함으로 리턴
		query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and parent_code='"+parent_code+"'"; 
		if(modDAO.getTotalCount(query) > 0) {
				data = "모품목코드["+parent_code+"]가 이미 다른모델에 적용되어 있습니다. ";
				return data;
		}

		//검사4 자품목코드검사 : ASSY코드일때 검사
		String child_ini = child_code.substring(0,1);
		if(child_ini.equals(assy_ini)) {
			//4-1 자신의 모델에 중복검사
			query = "SELECT count(*) FROM mbom_str where gid='"+gid+"' and child_code='"+child_code+"'"; 
			if(modDAO.getTotalCount(query) > 0) {
				data = "ASSY코드["+child_code+"]는 중복으로 등록할 수 없습니다."; return data;
			}

			//4-2 다른 모델에서 사용되었으면 입력불가
			query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and child_code='"+child_code+"'"; 
			if(modDAO.getTotalCount(query) > 0) {
				data = "다른모델에 사용된 ASSY코드["+child_code+"]입니다. ASSY코드는 중복으로 등록할 수 없습니다."; return data;
			}
		}

		//검사5.0부품이름,1규격,2메이커명,3형명,4수량단위,5item_type,등록일
		part = modDAO.getComponentInfo(child_code);
		if(part[1].length() == 0) {
			data = "미등록 부품["+child_code+"]입니다. 확인후 다시 입력하십시요.";
			return data;
		}
		
		//자품목코드가 phantom assy코드인지 판단하기
		if(child_code.indexOf(phantom) != -1) assy_dup = "D"; 

		//MBOM_STR에 입력하기 : 수량을 1개씩 등록한다.[개별편집을 위해]
		String add_date = anbdt.getDateNoformat();
		for(int i=0; i<cnt; i++) {
			String pid = anbdt.getNumID(i);
			input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
			input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
			input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,assy_dup,item_type) values('";
			input += pid+"','"+gid+"','"+parent_code+"','"+child_code+"','"+level_no+"','"+part[0]+"','";
			input += part[1]+"','"+location+"','"+op_code+"','"+part[4]+"','"+qty+"','"+part[2]+"','"+part[3]+"','";
			input += "원"+"','"+"0"+"','"+add_date+"','"+""+"','"+""+"','"+""+"','"+"0"+"','"+"0"+"','";
			input += "0"+"','"+"0"+"','"+assy_dup+"','"+part[5]+"')";
			////System.out.println("input : " + input);
			modDAO.executeUpdate(input);
		}

		//BOM상태를 MBOM_MASTER에 전달하기 (bom_status = '3' : BOM 등록중)
		update = "UPDATE mbom_master SET bom_status='3' where pid = '"+gid+"'";
		modDAO.executeUpdate(update);

		//data = "정상적으로 등록되었습니다.";
		data = "";
		return data;
	}

	//*******************************************************************
	// MBOM_STR에 데이터 수정하기
	// part_type : A[Assy Code임:하부구조가 있는지 없는지로 판단됨.]
	//*******************************************************************/	
	public String updateStr(String pid,String parent_code,String child_code,String location,String op_code,
		String qty_unit,String qty,String gid,String part_type) throws Exception
	{
		String data="",where="",bom_status="",assy_dup="";
		String[] part = new String[6];
		for(int i=0; i<6; i++) part[i] = "";		//초기화

		//수정전의 자품목코드 찾기
		where = "where pid = '"+pid+"'";
		String org_code = modDAO.getColumData("mbom_str","child_code",where);

		//검사1 BOM상태검사 : BOM상태를 판단하여 Template상태이면 리턴함
		where = "where pid='"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "공정템플릿 구성 상태입니다. 공정Assy코드로 등록후 진행하십시오.";
			return data;
		}

		//검사2 모품목검사 : 이미 다른모델에 적용된 경우는 설변을 이용해야함으로 리턴
		query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and parent_code='"+parent_code+"'"; 
		if(modDAO.getTotalCount(query) > 0) {
				data = "모품목코드["+parent_code+"]가 이미 다른모델에 사용되어 있습니다. ";
				data += "설계변경을 이용하거나, 모품목코드를 다른품목으로 변경후 진행하십시오."; 
				return data;
		}

		//검사3 자품목검사 : ASSY코드이면 자신에 중복검사,다른모델에 중복검사,
		String child_ini = child_code.substring(0,1);
		if(child_ini.equals(assy_ini)) {
			//자신의 모델에 중복사용검사
			query = "SELECT count(*) FROM mbom_str where gid='"+gid+"' and child_code='"+child_code+"'"; 
			query += " and pid != '"+pid+"'";
			if(modDAO.getTotalCount(query) > 0) {
				data = "ASSY코드["+child_code+"]는 중복으로 등록할 수 없습니다."; return data;
			}

			//다른 모델에서 사용되었으면 입력불가
			query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and child_code='"+child_code+"'"; 
			if(modDAO.getTotalCount(query) > 0) {
				data = "다른모델에 사용된 ASSY코드["+child_code+"]입니다. ASSY코드는 중복으로 등록할 수 없습니다."; return data;
			}

		}

		//검사4. 0부품이름,1규격,2메이커명,3형명,4수량단위,5item_type,등록일
		part = modDAO.getComponentInfo(child_code);
		if(part[1].length() == 0) {
			data = "미등록 부품["+child_code+"]입니다. 확인후 다시 입력하십시요.";
			return data;
		}

		//자품목코드가 phantom assy코드인지 판단하기
		if(child_code.indexOf(phantom) != -1) assy_dup = "D"; 
		
		//MBOM_STR에 수정하기
		String add_date = anbdt.getDateNoformat();
		update = "UPDATE MBOM_STR set child_code='"+child_code+"',location='"+location;
		update += "',part_name='"+part[0]+"',part_spec='"+part[1]+"',maker_name='"+part[2];
		update += "',maker_code='"+part[3]+"',price_unit='"+"원"+"',price='"+"0"+"',add_date='"+add_date;
		update += "',op_code='"+op_code+"',qty_unit='"+part[4]+"',qty='"+qty+"',assy_dup='"+assy_dup;
		update += "',item_type='"+part[5]+"' where pid='"+pid+"'";
		modDAO.executeUpdate(update);

		//부품TYPE이 공정ASSY코드이면 해당되는 공정Assy코드 전체를 바꿔준다
		if(part_type.equals("A")) {
			//op코드 구하기
			where = "where item_no='"+child_code+"'";
			op_code = modDAO.getColumData("ITEM_MASTER","op_code",where);

			update = "UPDATE MBOM_STR set parent_code='"+child_code+"',op_code='"+op_code;
			update += "' where gid='"+gid+"' and parent_code='"+org_code+"'";
			modDAO.executeUpdate(update);
		}
		//data = "정상적으로 수정 되었습니다.";
		data="";
		return data;
	}

	//*******************************************************************
	// MBOM_STR에 데이터 삭제하기
	//*******************************************************************/	
	public String deleteStr(String pid,String gid,String parent_code) throws Exception
	{
		String delete = "",data="",bom_status="";
		String where = "where pid='"+gid+"'";

		//진행상태 검사
		where = "where pid = '"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "공정템플릿 구성 상태입니다. 공정Assy코드로 등록후 진행하십시오.";
			return data;
		} 

		//검사1 모품목검사 : 이미 다른모델에 적용된 경우는 설변을 이용해야함으로 리턴
		query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and parent_code='"+parent_code+"'"; 
		if(modDAO.getTotalCount(query) > 0) {
				data = "모품목코드["+parent_code+"]가 이미 다른모델에 사용되고 있어 개별삭제를 할 수 없습니다. ";
				data += "설계변경을 이용하십시오."; 
				return data;
		}

		//개별삭제
		delete = "DELETE from MBOM_STR where pid='"+pid+"' and gid='"+gid+"'";
		modDAO.executeUpdate(delete);
		//data = "정상적으로 삭제되었습니다.";
		data = "";
		

		//MBOM_MASTER에 BOM상태 전달하기 (bom_status = '1' : 기초정보 작성중)
		query = "SELECT COUNT(*) FROM mbom_str WHERE gid = '"+gid+"'";
		int cnt = modDAO.getTotalCount(query);
		if(cnt == 1) {
			update = "UPDATE mbom_master SET bom_status='1' where pid = '"+gid+"'";
			modDAO.executeUpdate(update);
		}

		return data;
	}

	//*******************************************************************
	// MBOM_STR에 데이터 삭제하기 : 하부구조 전체를 삭제하기
	//*******************************************************************/	
	public String deleteAllStr(String pid,String gid,String parent_code) throws Exception
	{
		String delete = "",data="",bom_status="",level_no="",child_code="",p_code="";
		String where = "where pid='"+gid+"'";

		//진행상태 검사
		where = "where pid = '"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "공정템플릿 구성 상태입니다. 공정Assy코드로 등록후 진행하십시오.";
			return data;
		} 

		//검사1 모품목검사 : 이미 다른모델에 적용된 경우는 설변을 이용해야함으로 리턴
		query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and parent_code='"+parent_code+"'"; 
		if(modDAO.getTotalCount(query) > 0) {
				//전체삭제가능한 최상위레벨 모품목구하기
				ArrayList assy_list = new ArrayList();
				assy_list = modDAO.getAssyListCP(gid);
				Iterator table_iter = assy_list.iterator();
				com.anbtech.bm.entity.mbomStrTable table = new com.anbtech.bm.entity.mbomStrTable();
				while(table_iter.hasNext()) {
					table = (mbomStrTable)table_iter.next();
					p_code = table.getParentCode();
					query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and parent_code='"+p_code+"'"; 
					if(modDAO.getTotalCount(query) > 0) break;
				}

				//삭제하고자하는 모품목코드와 중복모품목코드중 최상위코드가 같지않으면
				if(!p_code.equals(parent_code)) {
					//메시지 전달
					data = "모품목코드["+parent_code+"]가 이미 다른모델에 사용되고 있어 삭제할 수 없습니다. ";
					data += "설계변경을 이용하거나, 자품목 ["+p_code+"] 에서 전체삭제를 진행 하십시오."; 
					return data;
				}
		}

		//하부구조데이터 전체를 구하기 : gid,level_no,parent_code
		//단, assy_dup='D'인 부분은 제외함.
		where = "where gid='"+gid+"' and pid='"+pid+"'";
		level_no = modDAO.getColumData("MBOM_STR","level_no",where);
		level_no = Integer.toString(Integer.parseInt(level_no)+1);
		child_code = modDAO.getColumData("MBOM_STR","child_code",where);

		//하부구조 전체 삭제하기
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getForwardItems(gid,level_no,child_code);

		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			delete = "DELETE from MBOM_STR where pid='"+table.getPid()+"' and gid='"+gid+"'";
			modDAO.executeUpdate(delete);
		}

		//선택한 컬럼 삭제하기
		delete = "DELETE from MBOM_STR where pid='"+pid+"' and gid='"+gid+"'";
		modDAO.executeUpdate(delete);

		//선택한 모품목과 같은 레벨의 품목코드 삭제하기
		delete = "DELETE from MBOM_STR where gid='"+gid+"' and parent_code='"+parent_code+"'";
		modDAO.executeUpdate(delete);

		
		//MBOM_MASTER에 BOM상태 전달하기 (bom_status = '1' : 기초정보 작성중)
		query = "SELECT COUNT(*) FROM mbom_str WHERE gid = '"+gid+"'";
		int cnt = modDAO.getTotalCount(query);
		if(cnt == 1) {
			update = "UPDATE mbom_master SET bom_status='1' where pid = '"+gid+"'";
			modDAO.executeUpdate(update);
		}

		//data = "정상적으로 삭제되었습니다.";
		data = "";
		return data;
	}

	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기
	 * gid : group관리코드, level_no : 0, parent_code : 0, url : 링크
	 **********************************************************************/
	public ArrayList getStrList(String gid,String level_no,String parent_code) throws Exception
	{
		// 배열에 담는다.
		saveBomArray(gid,level_no,parent_code);

		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		for(int i=0; i<an; i++) {
			table = new mbomStrTable();
			table.setPid(item[i][0]);
			String pcode = item[i][1];
			if(!item[i][3].equals("0"))	//첫번째값은 제외[level no : 0]
				pcode = "<a href=\"javascript:strView('"+item[i][0]+"','"+gid+"','"+parent_code+"');\">"+item[i][1]+"</a>";
			table.setParentCode(pcode);
			table.setChildCode(item[i][2]);
			table.setLevelNo(item[i][3]);
			table.setPartName(item[i][4]);
			table.setPartSpec(item[i][5]);
			table.setLocation(item[i][6]);
			table.setOpCode(item[i][7]);
			table.setQtyUnit(item[i][8]);
			table.setQty(item[i][9]);		
			table_list.add(table);
		}
		return table_list;
	}

	/**********************************************************************
	 * MBOM_STR에서 임시BOM TREE정보를 구하기
	 * gid : group관리코드, level_no : 0, parent_code : 0, url : 링크
	 **********************************************************************/
	public ArrayList getTbomStrList(String gid,String level_no,String parent_code) throws Exception
	{
		// 배열에 담는다.
		saveBomArray(gid,level_no,parent_code);

		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		for(int i=0; i<an; i++) {
			table = new mbomStrTable();
			table.setPid(item[i][0]);
			table.setParentCode(item[i][1]);
			table.setChildCode(item[i][2]);
			table.setLevelNo(item[i][3]);
			table.setPartName(item[i][4]);
			table.setPartSpec(item[i][5]);
			table.setLocation(item[i][6]);
			table.setOpCode(item[i][7]);
			table.setQtyUnit(item[i][8]);
			table.setQty(item[i][9]);		
			table_list.add(table);
		}
		return table_list;
	}

	/**********************************************************************
	 * 등록된 BOM구조를 배열에 담기
	 * 등록된 내용 출력보기
	 **********************************************************************/
	public void saveBomArray(String gid,String level_no,String parent_code) throws Exception
	{
		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getForwardItems(gid,level_no,parent_code);
		int cnt = item_list.size();
		item = new String[cnt][10];
		for(int i=0; i<cnt; i++) for(int j=0; j<10; j++) item[i][j]="";

		//배열에 담기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			item[an][0] = table.getPid();
			item[an][1] = table.getParentCode();
			item[an][2] = table.getChildCode();
			item[an][3] = table.getLevelNo();
			item[an][4] = table.getPartName();
			item[an][5] = table.getPartSpec();
			item[an][6] = table.getLocation();
			item[an][7] = table.getOpCode();
			item[an][8] = table.getQtyUnit();
			item[an][9] = table.getQty();
			an++;
		}
	}

	//--------------------------------------------------------------------
	//
	//		P/L IMPORT에 관한 정보 1
	//		Format : 모품목 / 자품목 / Location 일때
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * P/L IMPORT 읽기 
	 * Format : 모품목 / 자품목 / Location 일때
	 * multi로 파일읽고, ck로 구분하기
	 **********************************************************************/
	public String getImportList(MultipartRequest multi,String path,String ck,
		String parent_code,String level_no,String gid) throws Exception
	{
		String data = "",where="",bom_status="",pdg_code="",op_code="",child_code="";
		String input = "";
		String[] part = new String[7];				//부품정보를 담기위해
		for(int i=0; i<7; i++) part[i] = "";		//초기화
		String add_date = anbdt.getDateNoformat();	//등록일


		//검사1 BOM진행상태 : BOM상태를 판단하여 Template상태이면 리턴함
		where = "where pid='"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "공정템플릿 구성 상태입니다. 공정Assy코드로 등록후 진행하십시오.";
			return data;
		}

		//제품군코드 구하기
		pdg_code = modDAO.getColumData("MBOM_MASTER","pdg_code",where);

		//파일읽기및 검사 : Import PL을 읽어 지역배열item에 담고 결과를 리턴
		//item[][0]:level_no,item[][1]:pcd,item[][2]:ccd,item[][3]:location
		String importPLOK = readImportList(multi,path,ck,parent_code,level_no);
		if(importPLOK.equals("N")) {
			for(int i=0; i<an; i++) {
				if(item[i][0].equals("99")) data += item[i][1]+"/"+item[i][2]+"/"+item[i][3]+", ";
			}
			if(data.length() == 0) data = "구분자가 틀리거나, 파일에 모품목코드["+parent_code+"]가 없습니다.";
			else data += " 가  BOM TREE구조를 만드는데 시작될 모품목코드가 없어 등록할 수 없습니다. 수정후 다시 진행하십시오.";
			return data;
		}

		//검사2 모품목코드가 기존BOM의 모품목과 중복이면 리턴
		for(int i=0; i<an-1; i++) {
			if(!item[i][1].equals(item[i+1][1])) {
				query = "select count(*) from mbom_str where parent_code='"+item[i][1]+"' and gid != '"+gid+"'";
				int dup_cnt = modDAO.getTotalCount(query);
				if(dup_cnt != 0) data += item[i][1]+", ";
			}
		}
		if(data.length() != 0) { 
			data += " 는 다른모델에서 사용한 품목코드로 등록할 수 없습니다.";
			return data;
		}
			
		//검사3 모품목검사 : 모품목코드[4번째문자 = pdg_code]가 같은 제품군내의 코드인지 판단하기
		String four_char = "";	//4번째문자는
		int icnt = an - 1;
		for(int i=0; i<icnt; i++) {
			if(item[i][1] != item[i+1][1]) {	//모품목끼리가 같지않으면 (중복피하기위해)
				four_char = item[i][1].substring(3,4);				
				if(!four_char.equals(pdg_code)) data += item[i][1]+"/"+item[i][2]+"/"+item[i][3]+", ";
			}
		}
		if(data.length() != 0) {
			data += " 의 모품목코드는 동일한 제품군내의 코드가 아닙니다. 따라서 입력할 수 없습니다.";
			return data;
		}

		//검사4 모품목중복 : 기존의 모품목코드와 IMPORT한 모품목코드가 중복되는지 검사
		//			 모품목코드는 같은데 레벨이 다르면 Error
		String was_pcd = "";		//기존등록된 모품목코드

		com.anbtech.bm.entity.mbomStrTable assy = new com.anbtech.bm.entity.mbomStrTable();
		ArrayList assy_list = new ArrayList();
		assy_list = modDAO.getAssyList(gid);
		Iterator assy_iter = assy_list.iterator();			//기존의 Assy 코드
		String f_lno = Integer.toString(Integer.parseInt(level_no));
		while(assy_iter.hasNext()) {
			assy = (mbomStrTable)assy_iter.next(); 

			was_pcd = assy.getParentCode();
			//모품목코드 비교하기 
			for(int i=0; i<an; i++) {
				if((was_pcd.equals(item[i][1])) && (!f_lno.equals(item[i][0]))){
					data += item[i][1]+"|"+item[i][2]+", ";
				}
			}
		}
		if(data.length() != 0) {
			data += " 이 이미 등록되어 있는지, 선택한 ASSY코드가 BOM TREE구조를 만드는데 시작될 모품목코드인지 확인후 다시 진행하십시오.";
			return data;
		}

		
		//검사5 중복등록검사(모,자,레벨) 
		//이미등록된 ASSY코드가 IMPORT할 ASSY코드와 중복되면 이중으로입력된다.
		int cnt = 0;
		for(int i=0; i<an; i++) {
			query = "select COUNT(*) from MBOM_STR where gid='"+gid+"' and parent_code='"+item[i][1]+"' ";
			query += "and child_code='"+item[i][2]+"' and level_no='"+item[i][0]+"'";
			int dcnt = modDAO.getTotalCount(query);
			
			if(dcnt != 0) data += item[i][1]+"/"+item[i][2]+"/"+item[i][3]+", ";
			cnt += dcnt;
		}
		if(cnt != 0) {
			data += "["+cnt+"개중복]이 있어 등록할 수 없습니다.";
			return data;
		}

		//검사6 부품마스터 등록여부 
		String[] indb = new String[an];			//문장을 배열에 담는다.
		for(int i=0; i<an; i++) {
			//0:부품이름,1:규격,2:메이커명,3:형명,4:수량단위,5:item_type,6:op_code
			part = modDAO.getComponentInfo(item[i][2]);

			//모품목코드의 OP CODE를 구한다.
			where = "where item_no='"+item[i][1]+"'";
			op_code = modDAO.getColumData("ITEM_MASTER","op_code",where);

			//부품마스터 등록검사
			if(part[1].length() == 0) data += item[i][1]+"|"+item[i][2]+"|"+item[i][3]+", ";
			String pid = anbdt.getNumID(i);

			//INSERT문장을 배열에 담아 검사후 정상이면 입력한다.
			input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
			input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,";
			input += "buy_type,eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,item_type) values('";
			input += pid+"','"+gid+"','"+item[i][1]+"','"+item[i][2]+"','"+Integer.parseInt(item[i][0])+"','";
			input += part[0]+"','"+part[1]+"','"+item[i][3]+"','"+op_code+"','"+part[4]+"','"+"1"+"','"+part[2]+"','";
			input += part[3]+"','"+"원"+"','"+"0"+"','"+add_date+"','"+""+"','"+""+"','"+""+"','"+"0"+"','";
			input += "0"+"','"+"0"+"','"+"0"+"','"+part[5]+"')";
			indb[i] = input;	
		}

		//등록 결과 상태 판단하여 부품정보 등록하기
		if(data.length() != 0) data += "은 미등록 부품입니다. 확인후 다시 입력하십시요.";
		else {
			//INSERT문장을 배열을 DB에 입력한다.
			for(int i=0; i<an; i++) modDAO.executeUpdate(indb[i]);

			//BOM상태를 MBOM_MASTER에 전달하기 (bom_status = '3' : BOM 등록중)
			update = "UPDATE mbom_master SET bom_status='3' where pid = '"+gid+"'";
			modDAO.executeUpdate(update);

			data = "정상적으로 입력되었습니다.";
		}
		
		return data;
		
	}

	/**********************************************************************
	 * P/L IMPORT 읽기 : 모품목코드,자품목코드,Location
	 * multi로 파일읽고, ck로 구분하여 전역변수배열 item에 담기
	 * 정상적으로 TREE구조가되면 Y, 아니면 N [level no : 99]을 리턴한다.
	 **********************************************************************/
	public String readImportList(MultipartRequest multi,String path,String ck,String parent_code,String level_no) throws Exception
	{
		String isTreeOK = "Y";
		int cnt = 0, ele = 0;
		int SheetNo = 0;			//Excel 파일의 읽을 Sheet No (0,1,2 ...)

		com.anbtech.file.textFileReader reader = new com.anbtech.file.textFileReader();		//Text로 읽기
		com.anbtech.file.excelFileReader excel = new com.anbtech.file.excelFileReader();	//Excel로 읽기

		String file_name = multi.getFilesystemName("file_name");	//파일이름
		String file = path+"/"+file_name;							//읽을파일지정

		//확장자 찾기 (xls : excel 그외 : text로 인식)
		String ext_name = file_name.substring(file_name.lastIndexOf(".")+1,file_name.length());

		//EXCEL파일로 읽기
		if(ext_name.equals("xls")) {
			//1.파일을 읽어 구분자로 구분하여 배열에 담기
			excel.setBomSheetArray(file,SheetNo);						//file을 읽어 배열에 담기
			excel.delFilename(file);									//IMPORT한 파일 삭제하기		

			//2.배열데이터를 가져와 배열item에 담기
			//item[][0]:level_no,item[][1]:pcd,item[][2]:ccd,item[][3]:location
			cnt = excel.getRowCount();									//데이터 line 갯수
			this.an = cnt;
			ele = excel.getMaxColumnCount();							//line당 구분자로 구분된 element갯수
			item = new String[cnt][ele];			
			item = excel.readSheetArray();								//읽은 데이터 배열로 가져오기

		}
		//TEXT파일로 읽기
		else {
			//1.파일을 읽어 구분자로 구분하여 배열에 담기
			reader.readFileArray(file,ck);								//file을 읽어 배열에 담기
			reader.delFilename(file);									//IMPORT한 파일 삭제하기		

			//2.배열데이터를 가져와 배열item에 담기
			//item[][0]:level_no,item[][1]:pcd,item[][2]:ccd,item[][3]:location
			cnt = reader.getFileArrayCount();						//데이터 line 갯수
			this.an = cnt;
			ele = reader.getFileElementCount();						//line당 구분자로 구분된 element갯수
			item = new String[cnt][ele];			
			item = reader.getFileArray();							//읽은 데이터 배열로 가져오기
		}

		//3.주어진 코드와 배열1의 내용이 같으면 레벨번호 입력
		//파일의 한라인은 BOM상의 한라인과 같아야 한다.이를 시점으로 레벨을 구성한다.
		int pc = 0;
		for(int i=0; i<cnt; i++) {
			if(item[i][1].equals(parent_code)) { 
				//item[i][0] = Integer.toString(Integer.parseInt(level_no)+1); 
				item[i][0] = Integer.toString(Integer.parseInt(level_no)); 
				pc++; 
			}
		}
		if(pc == 0) {		//구분자가 틀리거나 모품목코드와 맞는것이 하나도 없음
			isTreeOK = "N"; return isTreeOK;
		}

		//4.레벨번호가 있으면 앞으로 올린다.
		bubbleSort(cnt,ele);

		//5.배열번호가 없는 나머지 배열의 레벨번호를 입력한다.
		String pcd = "";
		int lvn = 0;
		for(int i=0; i<cnt; i++) {			//전체 실행횟수
			pcd = item[i][2];				//자품목을 모품목으로 대치
			if(item[i][0].length() != 0) {
				lvn = Integer.parseInt(item[i][0])+1;
				//레벨번호 입력
				for(int j=i; j<cnt; j++) if(item[j][1].equals(pcd)) item[j][0] = Integer.toString(lvn);
				//레벨번호가 있으면 앞으로 올린다.
				bubbleSort(cnt,ele);
			}
		}

		//6.레벨번호를 입력할 수 없는[TREE구조가 될수없는]PL의 레벨번호는 99를 입력한다.
		for(int i=0; i<cnt; i++) if(item[i][0].length() == 0) { item[i][0] = "99"; isTreeOK="N"; }

		//7.정렬하기 (모품목코드로)
		sort.bubbleSortStringMultiDesc(item,1);

		return isTreeOK;
	}

	/**********************************************************************
	 * SORT
	 * 레벨번호가 있으면 앞으로 올린다.
	 **********************************************************************/
	public void bubbleSort(int cnt,int ele)
	{
		String[] swap = new String[ele];	//swap할 임시배열
		for(int i=1; i<cnt; i++) {			//전체 실행횟수
			for(int j=0; j<cnt-1; j++) {	//비교
				if(item[j][0].length() == 0) {
					for(int s=0; s<ele; s++) {
						swap[s] = item[j][s];
						item[j][s] = item[j+1][s];
						item[j+1][s] = swap[s];
					}
				}
			}
		}
	}

	//--------------------------------------------------------------------
	//
	//		P/L IMPORT에 관한 정보 2
	//		Format : 품목코드,공정코드,Location 일때
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * P/L IMPORT 읽기 
	 * Format : 품목코드 / 공정코드 / Location 일때
	 * multi로 파일읽고, ck로 구분하기
	 **********************************************************************/
	public String getImportPartList(MultipartRequest multi,String path,String ck,String gid) throws Exception
	{
		String data="",where="",bom_status="",input="";
		String[] part = new String[6];				//부품정보를 담기위해
		for(int i=0; i<6; i++) part[i] = "";		//초기화
		String add_date = anbdt.getDateNoformat();	//등록일

		//1.BOM상태를 판단하여 Template상태이면 리턴함
		where = "where pid='"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "공정템플릿 구성 상태입니다. 공정Assy코드로 등록후 진행하십시오.";
			return data;
		}

		//2.Import PL을 읽어 지역배열item에 담고 결과를 리턴
		//item[][0]:level_no,item[][1]:pcd,item[][2]:ccd,item[][3]:location,item[][4]:op code
		String importPLOK = readImportPartList(multi,path,ck,gid);
		if(!importPLOK.equals("Y")) {
			return importPLOK;
		}

		//3.신규 등록일때 부품정보 검사하기
		String[] indb = new String[an];			//문장을 배열에 담는다.
		for(int i=0; i<an; i++) {
			//0부품이름,1규격,2메이커명,3형명,4수량단위,5item_type
			part = modDAO.getComponentInfo(item[i][2]);
			if(part[1].length() == 0) data += item[i][1]+"|"+item[i][2]+"|"+item[i][3]+", ";
			String pid = anbdt.getNumID(i);

			//INSERT문장을 배열에 담아 검사후 정상이면 입력한다.
			input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
			input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,";
			input += "buy_type,eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,item_type) values('";
			input += pid+"','"+gid+"','"+item[i][1]+"','"+item[i][2]+"','"+Integer.parseInt(item[i][0])+"','";
			input += part[0]+"','"+part[1]+"','"+item[i][3]+"','"+item[i][4]+"','"+part[4]+"','"+"1"+"','"+part[2]+"','";
			input += part[3]+"','"+"원"+"','"+"0"+"','"+add_date+"','"+""+"','"+""+"','"+""+"','"+"0"+"','";
			input += "0"+"','"+"0"+"','"+"0"+"','"+part[5]+"')";
			indb[i] = input;	
		}

		//4.신규부품이 부품마스터에 등록되었는지 판단하여 BOM등록하기
		if(data.length() != 0) data += "은 미등록 부품입니다. 확인후 다시 입력하십시요.";
		else {
			//INSERT문장을 배열을 DB에 입력한다.
			for(int i=0; i<an; i++) modDAO.executeUpdate(indb[i]);

			//BOM상태를 MBOM_MASTER에 전달하기 (bom_status = '3' : BOM 등록중)
			update = "UPDATE mbom_master SET bom_status='3' where pid = '"+gid+"'";
			modDAO.executeUpdate(update);

			//Template을 이용한 품목등록이 되었음을 MBOM_STR에 전달하기 (tag = '0' )
			update = "UPDATE mbom_str SET tag='0' where gid = '"+gid+"'";
			modDAO.executeUpdate(update);

			data = "정상적으로 입력되었습니다.";
		}
		
		return data;
	}

	/**********************************************************************
	 * P/L IMPORT 읽기 : 품목코드,공정코드,Location
	 * multi로 파일읽고, ck로 구분하여 전역변수배열 item에 담기
	 **********************************************************************/
	public String readImportPartList(MultipartRequest multi,String path,String ck,String gid) throws Exception
	{
		String isTreeOK = "";
		String[][] part;
		int cnt = 0, ele = 0;
		int SheetNo = 0;			//Excel 파일의 읽을 Sheet No (0,1,2 ...)

		com.anbtech.file.textFileReader reader = new com.anbtech.file.textFileReader();		//Text로 읽기
		com.anbtech.file.excelFileReader excel = new com.anbtech.file.excelFileReader();	//Excel로 읽기

		String file_name = multi.getFilesystemName("file_name");	//파일이름
		String file = path+"/"+file_name;							//읽을파일지정

		//확장자 찾기 (xls : excel 그외 : text로 인식)
		String ext_name = file_name.substring(file_name.lastIndexOf(".")+1,file_name.length());

		//EXCEL파일로 읽기
		if(ext_name.equals("xls")) {
			//1.파일을 읽어 구분자로 구분하여 배열에 담기
			excel.setBomSheetArray(file,SheetNo);						//file을 읽어 배열에 담기
			excel.delFilename(file);									//IMPORT한 파일 삭제하기		

			//2.배열데이터를 가져와 배열item에 담기
			//item[][0]:level_no,item[][1]:pcd,item[][2]:ccd,item[][3]:location
			cnt = excel.getRowCount();									//데이터 line 갯수
			this.an = cnt;
			ele = excel.getMaxColumnCount();							//line당 구분자로 구분된 element갯수
			part = new String[cnt][ele];		
			for(int i=0; i<cnt; i++) for(int j=0; j<ele; j++) part[i][j]="";			
			part = excel.readSheetArray();								//읽은 데이터 배열로 가져오기

		}
		//TEXT파일로 읽기
		else {

			//1.파일을 읽어 구분자로 구분하여 배열에 담기
			reader.readFileArray(file,ck);								//file을 읽어 배열에 담기
			reader.delFilename(file);									//IMPORT한 파일 삭제하기		

			//2.부품리스트를 읽어 배열 part에 담기
			//part[][0]:level_no,part[][1]:ccd,part[][2]:op code,part[][3]:location
			cnt = reader.getFileArrayCount();						//데이터 line 갯수
			this.an = cnt;
			ele = reader.getFileElementCount();						//line당 구분자로 구분된 element갯수
			part = new String[cnt][ele];		
			for(int i=0; i<cnt; i++) for(int j=0; j<ele; j++) part[i][j]="";
			part = reader.getFileArray();								//읽은 데이터 배열로 가져오기
		}

		//파일 선택검사
		if(cnt == 0) {
			isTreeOK = "파일을 읽을 수 없습니다. 확인하시고 다시 등록하십시오.";
			return isTreeOK;
		}
		
		//구분자 검사
		if(ele < 4) {
			for(int i=0; i<cnt; i++) for(int j=0; j<ele; j++) isTreeOK += part[i][j]+", ";
			isTreeOK += "구분자를 확인하시고 다시 등록하십시오.";
			return isTreeOK;
		}
		
		//3.배열 part의 내용을 전역배열 item으로 옮겨담기
		//item[][0]:level_no,item[][1]:pcd,item[][2]:ccd,item[][3]:location,item[][4]:op code
		item = new String[cnt][5];
		for(int i=0; i<cnt; i++) {
			item[i][0] = "";
			item[i][1] = "";
			item[i][2] = part[i][1];		
			item[i][3] = part[i][3];
			item[i][4] = part[i][2];
		}
		
		//4.해당BOM의 Template으로 등록된 Assy Code 찾기
		mbomStrTable assy = null;
		ArrayList assy_list = new ArrayList();
		assy_list = modDAO.getAssyListTemp(gid);
		Iterator assy_iter = assy_list.iterator();			
		int assy_cnt = assy_list.size();

		String[][] data = new String[assy_cnt][3];
		int as = 0;
		while(assy_iter.hasNext()) {
			assy = (mbomStrTable)assy_iter.next(); 
			data[as][0] = assy.getLevelNo();
			data[as][1] = assy.getParentCode();
			data[as][2] = assy.getOpCode();
			as++;
		}
		if(assy_cnt == 0) {
			isTreeOK = "공정템플릿으로 구성된 경우만 품목리스트 등록이 가능합니다.";
			isTreeOK += "이미 품목LIST등록을 하였거나 공정템플릿으로 구성되지 않았습니다.";
			return isTreeOK;
		}

		//5.item배열에 Level no 와 parent code 입력하기
		for(int i=0; i<cnt; i++) {
			for(int j=0; j<as; j++) {
				if(item[i][4].equals(data[j][2])) {
					item[i][0] = data[j][0];
					item[i][1] = data[j][1];
				}
			}
		}
		
		//6.Level no가 없는경우는 품목LIST와 공정템플릿이 다른경우로 메시지 출력
		for(int i=0; i<cnt; i++) {
			if(item[i][0].length() == 0) isTreeOK += item[i][2]+"/"+item[i][3]+"/"+item[i][4]+", ";
		}
		if(isTreeOK.length() !=0) {
			isTreeOK += " 는 공정코드에 해당되는 ASSY코드가 없거나 구분자가 다릅니다.";
			isTreeOK += " 확인후 다시 등록하십시오.";
			return isTreeOK;
		}

		isTreeOK = "Y";
		return isTreeOK;
	}

	//--------------------------------------------------------------------
	//
	//		BOM을 이용하여 자품목코드기준 Unique한 내용으로 정렬하기
	//		[원가 산출할때 활용]
	//		* 단지 자품목코드로 unique한 내용만 출력함(location no고려하지 않음)
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기  [원가산출할때 활용]
	 * 정전개 TREE 구조체계를 배열에 담기 : 하부구조 전체
	 **********************************************************************/
	public ArrayList getUniqueMultiLevelBom(String gid,String level_no,String parent_code) throws Exception
	{
		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getForwardItems(gid,level_no,parent_code);
		int cnt = item_list.size();
		if(cnt == 0) return item_list;

		String[][] data = new String[cnt][11];
		for(int i=0; i<cnt; i++) for(int j=0; j<11; j++) data[i][j]="";

		//배열에 담기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			data[n][0] = table.getPid();
			data[n][1] = table.getParentCode();
			data[n][2] = table.getChildCode();
			data[n][3] = table.getLevelNo();
			data[n][4] = table.getPartName();
			data[n][5] = table.getPartSpec();
			data[n][6] = table.getLocation();
			data[n][7] = table.getOpCode();
			data[n][8] = table.getQtyUnit();
			data[n][9] = table.getQty();
			data[n][10] = "";					//동일한 부품갯수를 입력하기 위해
			////System.out.println(data[n][3]+":"+data[n][1]+":"+data[n][2]);
			n++;
		}
		//Unique한 자품목코드로 갯수 출력하기
		sort.bubbleUniqueSortStringMultiAsc(data,2);
		
		//동일부품으로 정렬함에 따라 빈배열 없애고 단가입력하기[배열추가]
		//10:갯수,11:표준단가,12:평균단가,13:현재단가,14:표준총액,15:평균총액,16:현재총액
		String where = "";
		String t[][] = new String[n][17];
		an = 0;
		for(int i=0; i<n; i++) {
			if(data[i][0].length() != 0) {
				for(int j=0; j<11; j++) t[i][j] = data[i][j];
				
				where = "where item_code = '"+t[i][2]+"'";
				t[i][11] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_s",where);//표준단가
				t[i][12] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_a",where);//평균단가
				t[i][13] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_c",where);//현재단가
				
				t[i][14] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][11]));
				t[i][15] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][12]));
				t[i][16] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][13]));
				
				an++;
			}
		}

		//Array List로 담기
		ArrayList price_list = new ArrayList();
		primeCostTable price = null;
		for(int i=0; i<an; i++) {
			price = new primeCostTable();	
			price.setItemCode(t[i][2]);		//품목코드
			price.setItemName(t[i][4]);		//품목이름
			price.setItemDesc(t[i][5]);		//품목규격
			price.setItemCount(t[i][10]);	//품목수량
			price.setStdPrice(t[i][11]);	//표준단가
			price.setAvePrice(t[i][12]);	//평균단가
			price.setCurPrice(t[i][13]);	//현재단가
			price.setStdSum(t[i][14]);		//표준단가 총액
			price.setAveSum(t[i][15]);		//평균단가 총액
			price.setCurSum(t[i][16]);		//현재단가 총액

			price_list.add(price);
		}

/*		//출력해보기
		primeCostTable view = new primeCostTable();
		Iterator price_iter = price_list.iterator();
		while(price_iter.hasNext()) {
			view = (primeCostTable)price_iter.next();
			//System.out.println(view.getItemCode()+":"+view.getItemCount()+":"+view.getAvePrice()+":"+view.getAveSum());
		}
*/
		return price_list;

	}

	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기 [원가산출할때 활용]
	 * 정전개 TREE 구조체계를 배열에 담기 : 해당Assy
	 **********************************************************************/
	public ArrayList getUniqueSingleLevelBom(String gid,String level_no,String parent_code) throws Exception
	{
		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getSingleForwardItems(gid,level_no,parent_code);
		int cnt = item_list.size();
		if(cnt == 0) return item_list;

		String[][] data = new String[cnt][11];
		for(int i=0; i<cnt; i++) for(int j=0; j<11; j++) data[i][j]="";

		//배열에 담기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			data[n][0] = table.getPid();
			data[n][1] = table.getParentCode();
			data[n][2] = table.getChildCode();
			data[n][3] = table.getLevelNo();
			data[n][4] = table.getPartName();
			data[n][5] = table.getPartSpec();
			data[n][6] = table.getLocation();
			data[n][7] = table.getOpCode();
			data[n][8] = table.getQtyUnit();
			data[n][9] = table.getQty();
			data[n][10] = "";					//동일한 부품갯수를 입력하기 위해
			////System.out.println(data[n][3]+":"+data[n][1]+":"+data[n][2]);
			n++;
		}
		//Unique한 자품목코드로 갯수 출력하기
		sort.bubbleUniqueSortStringMultiAsc(data,2);
		
		//동일부품으로 정렬함에 따라 빈배열 없애고 단가입력하기[배열추가]
		//10:갯수,11:표준단가,12:평균단가,13:현재단가,14:표준총액,15:평균총액,16:현재총액
		String where = "";
		String t[][] = new String[n][17];
		an = 0;
		for(int i=0; i<n; i++) {
			if(data[i][0].length() != 0) {
				for(int j=0; j<11; j++) t[i][j] = data[i][j];
				
				where = "where item_code = '"+t[i][2]+"'";
				t[i][11] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_s",where);//표준단가
				t[i][12] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_a",where);//평균단가
				t[i][13] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_c",where);//현재단가
				
				t[i][14] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][11]));
				t[i][15] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][12]));
				t[i][16] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][13]));
				
				an++;
			}
		}

		//Array List로 담기
		ArrayList price_list = new ArrayList();
		primeCostTable price = null;
		for(int i=0; i<an; i++) {
			price = new primeCostTable();	
			price.setItemCode(t[i][2]);		//품목코드
			price.setItemName(t[i][4]);		//품목이름
			price.setItemDesc(t[i][5]);		//품목규격
			price.setItemCount(t[i][10]);	//품목수량
			price.setStdPrice(t[i][11]);	//표준단가
			price.setAvePrice(t[i][12]);	//평균단가
			price.setCurPrice(t[i][13]);	//현재단가
			price.setStdSum(t[i][14]);		//표준단가 총액
			price.setAveSum(t[i][15]);		//평균단가 총액
			price.setCurSum(t[i][16]);		//현재단가 총액

			price_list.add(price);
		}

/*		//출력해보기
		primeCostTable view = new primeCostTable();
		Iterator price_iter = price_list.iterator();
		while(price_iter.hasNext()) {
			view = (primeCostTable)price_iter.next();
			//System.out.println(view.getItemCode()+":"+view.getItemCount()+":"+view.getAvePrice()+":"+view.getAveSum());
		}
*/
		return price_list;
	}
}