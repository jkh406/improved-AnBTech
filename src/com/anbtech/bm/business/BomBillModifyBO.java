package com.anbtech.bm.business;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class BomBillModifyBO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	
	private String query = "";
	private String[][] item = null;				//TREE정보를 배열로 담기
	private int an = 0;							//items의 배열 갯수

	private String[][] plist = null;			//읽은 파일내용을 배열에 담기 
	private int elecnt=0;						//읽은 파일의 한 라인당 데이터 갯수 
	private int linecnt=0;						//읽은 파일의 라인갯수 

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public BomBillModifyBO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		BOM 복사,붙이기 기능에 대한 메소드 정의
	//			BOM의 쉬운 편집을 위해 복사 -> 붙이기 지원을 위해
	//			모델단위 복사, Assy단위 복사 지원
	//---------------------------------------------------------------------
	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기
	 * BOM을 복사하기 : 하부구조 전체
	 * gid : group관리코드, level_no : 0, parent_code : 0, url : 링크
	 **********************************************************************/
	public ArrayList getCpyStrList(String gid,String level_no,String parent_code) throws Exception
	{
		String sel_date = anbdt.getDateNoformat();		//현재일자로 BOM변경고려
		ArrayList item_list = new ArrayList();
//		item_list = modDAO.getForwardItems(gid,level_no,parent_code);		//BOM승인전 내용
		item_list = modDAO.getCopyForwardItems(gid,level_no,parent_code,sel_date); //BOM승인전후 내용

/*		//출력해보기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			//System.out.println(table.getParentCode()+":"+table.getChildCode()+":"+table.getLevelNo());
		}
*/
		return item_list;
	}

	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기
	 * BOM을 복사하여 붙이기
	 * gid : group관리코드, level_no : 0, parent_code : 0, url : 링크
	 **********************************************************************/
	public String pastePartList(ArrayList paste_list) throws Exception
	{
		String input="",data="",pid="";
		String pcd="",ccd="",gid="";				//검사기준 모품목,자품목코드,그룹코드
		int lvn=0;									//검사기준 레벨번호
		String[] part = new String[6];				//부품정보를 담기위해
		for(int i=0; i<6; i++) part[i] = "";		//초기화
		String add_date = anbdt.getDateNoformat();	//등록일

		com.anbtech.bm.entity.mbomStrTable chk = new com.anbtech.bm.entity.mbomStrTable();
		com.anbtech.bm.entity.mbomStrTable assy = new com.anbtech.bm.entity.mbomStrTable();
		com.anbtech.bm.entity.mbomStrTable copy = new com.anbtech.bm.entity.mbomStrTable();
		com.anbtech.bm.entity.mbomStrTable paste = new com.anbtech.bm.entity.mbomStrTable();

		//검사를 위해 첫라인의 모품목코드,자품목코드,레벨번호 읽기
		// 첫라인은 붙이기할 모델의 정보임, 나머지가 대상모델[복사한]의 정보임
		Iterator chk_iter = paste_list.iterator();
		if(chk_iter.hasNext()) {
			chk = (mbomStrTable)chk_iter.next();
			gid = chk.getGid();
			pcd = chk.getParentCode();
			ccd = chk.getChildCode();
			lvn = Integer.parseInt(chk.getLevelNo());
			//if(pcd.equals("0")) pcd = ccd;	//모델단위로 복사할때
		}

		//검사하기 : 공정템플릿구성중은 복사/붙이기 할 수 없음
		String where = "where pid='"+gid+"'";
		String bom_status = modDAO.getColumData("mbom_master","bom_status",where);
		if(bom_status.equals("2")) {
			data = "공정템플릿 구성 상태입니다. 공정Assy코드로 등록후 진행하십시오.";
			return data;
		}

		//검사하기 : 기존의 모품목코드와 복사한 모품목코드가 중복되면 복사/붙이기 할 수 없음
		String was_pcd = "";		//기존등록된 모품목코드
		ArrayList assy_list = new ArrayList();
		assy_list = modDAO.getAssyListCP(gid);
		Iterator assy_iter = assy_list.iterator();			//기존의 Assy 코드
		while(assy_iter.hasNext()) {
			assy = (mbomStrTable)assy_iter.next(); 

			was_pcd = assy.getParentCode();
			//모품목코드 비교하기
			Iterator copy_iter = paste_list.iterator();		//복사한 부품정보
			int fno = 0;									//첫번째 정보는 붙이기할 모/자품목코드로 제외
			while(copy_iter.hasNext()) {
				copy = (mbomStrTable)copy_iter.next(); 
				if(was_pcd.equals(copy.getParentCode()) && (fno != 0)) {
					data += copy.getParentCode()+"/"+copy.getChildCode()+", ";
				}
				fno++;
			}
		}
		if(data.length() != 0) {
			String msg = data;
			data = " [모품목코드 중복 등록 LIST ERROR] " + msg + " 이 이미 등록되어 있습니다. 확인후 다시 진행하십시오.";
			return data;
		}

		//부품마스터에 등록된 부품인지 검사한다. (향후 부품삭제될 경우 대비)
		//등록하기 단,첫모품목코드는 선택한 품목코드로 바꿔준다.
		int pst_cnt = paste_list.size();			//전체수량
		String[] inDB = new String[pst_cnt];		//입력문장 배열에 담기
		int inDB_n = 0;								//문장갯수

		int first_line = 0;			//첫라인 skip조건
		int level_no = lvn;			//레벨번호 
		int before_lvn = lvn;		//바로전에 읽은 레벨번호
		int current_lvn = 0;		//지금읽은 레벨번호
		String before_pcd = "";		//바로전에 읽은 모품목코드
		String current_pcd = "";	//지금읽은 모품목 코드
		String parent_code = "";	//모품목코드
		Iterator part_iter = paste_list.iterator();
		while(part_iter.hasNext()) {
			paste = (mbomStrTable)part_iter.next();

			//첫라인 붙이기는 skip[붙이기의 최상단과 동일] 한다. 단 이어주길할 pid값만 돌려준다.
			if(first_line == 0) { 
				pid = paste.getPid();
			}
			//첫라인 붙이기의 연결부위로 skip한다.
			else {
				//레벨번호
				current_lvn = Integer.parseInt(paste.getLevelNo());
				if(first_line == 1) level_no++;
				else if(before_lvn < current_lvn) level_no++;
				else if(before_lvn > current_lvn) level_no--;
				before_lvn = current_lvn;						//레벨번호 대입

				//붙이기 ASSY코드와 복사의 첫번째 모품목코드를 모/자 구성하기
				if(first_line == 1) {
					parent_code = ccd;
					String child_code = paste.getParentCode();

					//0부품이름,1규격,2메이커명,3형명,4수량단위,5item_type,6op_code
					part = modDAO.getComponentInfo(child_code);
					if(part[1].length() == 0) data += child_code+", ";

					//등록할 쿼리문장 만들기
					input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
					input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
					input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,assy_dup,item_type) values('";
					input += pid+"','"+paste.getGid()+"','"+parent_code+"','";
					input += child_code+"','"+level_no+"','"+part[0]+"','"+part[1]+"','";
					input += paste.getLocation()+"','"+part[6]+"','"+paste.getQtyUnit()+"','";
					input += paste.getQty()+"','"+part[2]+"','"+part[3]+"','"+"원"+"','"+"0"+"','";
					input += add_date+"','"+""+"','"+""+"','"+""+"','"+"0"+"','"+"0"+"','"+"0"+"','"+"0"+"','";
					input += paste.getAssyDup()+"','"+part[5]+"')";
					
					//배열에 담는다.
					inDB[inDB_n] = input;
					inDB_n++;
					level_no++;
				}

				//ccd를 모품목코드로 할경우(복사의 첫번째 모품목이 붙이기의 ASSY로 대치할 때
				//모품목 코드
				//current_pcd = paste.getParentCode();
				//if(first_line == 1) { parent_code = ccd;	before_pcd = current_pcd; }
				//else if(before_pcd.equals(current_pcd)) parent_code = ccd;
				//else parent_code = current_pcd;

				//모품목 코드
				parent_code = paste.getParentCode();

				//0부품이름,1규격,2메이커명,3형명,4수량단위,5item_type
				part = modDAO.getComponentInfo(paste.getChildCode());
				if(part[1].length() == 0) data += paste.getChildCode()+", ";
				
				//등록할 쿼리문장 만들기
				input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
				input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
				input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,assy_dup,item_type) values('";
				input += paste.getPid()+"','"+paste.getGid()+"','"+parent_code+"','";
				input += paste.getChildCode()+"','"+level_no+"','"+part[0]+"','"+part[1]+"','";
				input += paste.getLocation()+"','"+paste.getOpCode()+"','"+paste.getQtyUnit()+"','";
				input += paste.getQty()+"','"+part[2]+"','"+part[3]+"','"+"원"+"','"+"0"+"','";
				input += add_date+"','"+""+"','"+""+"','"+""+"','"+"0"+"','"+"0"+"','"+"0"+"','"+"0"+"','";
				input += paste.getAssyDup()+"','"+part[5]+"')";
				
				//배열에 담는다.
				inDB[inDB_n] = input;
				inDB_n++;
			} //첫라인 skip
			first_line++;


		} //while
		//결과 검색
		if(data.length() != 0) {
			data += " 는 부품마스터에 없는 부품입니다. 붙이기를 진행 할 수 없습니다.";
			return data;
		}

		//DB로 담기
		for(int i=0; i<inDB_n; i++) modDAO.executeUpdate(inDB[i]);

		//BOM상태를 MBOM_MASTER에 전달하기 (bom_status = '3' : BOM 등록중)
		where = "where pid='"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("1")) {
				String update = "UPDATE mbom_master SET bom_status='3' where pid = '"+gid+"'";
				modDAO.executeUpdate(update);
		}
		
		return data;
	}

	/**********************************************************************
	 * BOM을 복사하기가 가능한지 판단하기 : 설변중인 경우는 복사가 불가하도록
	 * gid : group관리코드
	 **********************************************************************/
	public String checkCBomStatus(String gid) throws Exception
	{
		String input="",data="",where="",fg_code="";
		String eco_no="",ecc_status="",tmp="";

		//FG CODE 구하기
		where = "where pid='"+gid+"'";
		fg_code = modDAO.getColumData("mbom_master","fg_code",where);

		//-------------------------------------------------------
		//FG CODE에 해당되는 내용이 설계변경 진행중인지 판단하기
		//-------------------------------------------------------
		//FG_CODE에 해당되는 설계변경번호 찾기 (단,설변중이거나 완료일 수 있음) : 다수로 콤마로 구분됨
		where = "where fg_code='"+fg_code+"'";
		eco_no = modDAO.getColumAllData("ecc_model","eco_no",where);

		//검사2. 설계변경번호에 해당되는 내용이 설변중인지 판단하기
		//설변중이면 설계변경번호 묶기
		StringTokenizer list = new StringTokenizer(eco_no,",");
		while(list.hasMoreTokens()) {
			tmp = list.nextToken();
			where = "where eco_no='"+tmp+"'";
			ecc_status = modDAO.getColumData("ecc_com","ecc_status",where);
			if(ecc_status.length() != 0 & !ecc_status.equals("9")){		//설변진행중임
				data += tmp+",";
			}
		}

		//최종리턴값 판단하기
		if(data.length() != 0) data += " 로 설계변경중입니다.";

		return data;
	}
}
