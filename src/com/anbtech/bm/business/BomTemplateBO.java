package com.anbtech.bm.business;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class BomTemplateBO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.bm.db.BomTemplateDAO tmpDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	
	private String query = "",update="";
	private String[][] item = null;				//TREE정보를 배열로 담기
	private int an = 0;							//items의 배열 갯수

	private String[][] plist = null;			//읽은 파일내용을 배열에 담기 
	private int elecnt=0;						//읽은 파일의 한 라인당 데이터 갯수 
	private int linecnt=0;						//읽은 파일의 라인갯수 
	private String e_assy="1E";					//회로 ASSY 코드 시작점

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public BomTemplateBO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
		tmpDAO = new com.anbtech.bm.db.BomTemplateDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		Template 정보
	//			
	//			
	//			
	//---------------------------------------------------------------------

	//*******************************************************************
	// Template 정보를 이용하여 Template BOM 입력하기
	// 1 : 모품목코드
	// 2 : Level No
	// 3 : Template tag 정보 : A100,A200,A400, : 반드시 마지막에 , 가 있어야
	//*******************************************************************/	
	public String inputTemplateBom(String gid,String parent_code,String level_no,String template_tag) throws Exception
	{
		String data="",input="",where="",op_code="",bom_status="";
		int lvn = 0;

		//모품목코드의 공정코드 찾기
		where = "where item_no = '"+parent_code+"'";
		op_code = modDAO.getColumData("ITEM_MASTER","op_code",where);

		//1.시작 레벨 번호 구하기
		if(level_no.length() != 0) lvn = Integer.parseInt(level_no)+1;
		else {data="Level No가 없습니다."; return data; }

		//2.검사: BOM STATUS를 구하여 Template정보가 등록되었는지 판단한다.
		where = "where pid = '"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "이미 등록된 Template BOM입니다."; return data;
		}

		//template tag을 이용해 모,자,레벨,규격정보를 배열에 담는다.
		int cnt=0,len=template_tag.length();
		for(int i=0; i<len; i++) if(template_tag.charAt(i) == ',') cnt++;
		String[][] temp = new String[cnt][5];	//모,자,레벨,spec
		template_tag = template_tag.substring(0,len-1);
		
		StringTokenizer temp_list = new StringTokenizer(template_tag,",");
		int n = 0;
		while(temp_list.hasMoreTokens()) {
			if(n == 0) {
				temp[n][0] = parent_code;
				temp[n][1] = temp_list.nextToken();				//공정코드[ASSY생성용]
				temp[n][2] = Integer.toString(lvn);
				temp[n][3] = tmpDAO.getOpCodeSpec(temp[n][1]);
				temp[n][4] = op_code;							//모품목코드의 공정코드[op_code]
				op_code = temp[n][1];							//공정코드
			} else {
				temp[n][0] = temp[n-1][1];
				temp[n][1] = temp_list.nextToken();				//공정코드
				temp[n][2] = Integer.toString(lvn);
				temp[n][3] = tmpDAO.getOpCodeSpec(temp[n][1]);
				temp[n][4] = op_code;							//모품목코드의 공정코드[op_code]
				op_code = temp[n][1];							//공정코드
			}
			////System.out.println(temp[n][0]+" : "+temp[n][1]+" : "+temp[n][2]+" : "+temp[n][3]);
			lvn++;
			n++;
		}

		//Template정보를 MBOM STR에 담는다.
		String add_date = anbdt.getDateNoformat();
		for(int i=0; i<cnt; i++) {
			String pid = anbdt.getNumID(i);
			input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
			input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
			input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,item_type) values('";
			input += pid+"','"+gid+"','"+temp[i][0]+"','"+temp[i][1]+"','"+temp[i][2]+"','"+""+"','";
			input += temp[i][3]+"','"+""+"','"+temp[i][4]+"','"+"EA"+"','"+"1"+"','"+""+"','"+""+"','";
			input += "원"+"','"+"0"+"','"+add_date+"','"+""+"','"+""+"','"+""+"','"+"0"+"','"+"0"+"','";
			input += "0"+"','"+"1"+"','"+"1"+"')";
			modDAO.executeUpdate(input);
		}

		//6.Template정보임을 MBOM MASTER에 담는다.
		update = "UPDATE MBOM_MASTER set bom_status='2' where pid='"+gid+"'";
		modDAO.executeUpdate(update);

		data = "정상적으로 등록되었습니다.";
		return data;
	}

	//*******************************************************************
	// MBOM_STR에 Template정보 데이터 삭제하기
	//*******************************************************************/	
	public String deleteTemplateBom(String gid) throws Exception
	{
		String delete = "",data="";
		String where = "where pid='"+gid+"'";
		
		//진행상태 검사하기
		String bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("4")) {
			data = "결재상신중인 BOM으로 삭제할 수 없습니다.";
		} else if(bom_status.equals("5")) {
			data = "확정된 BOM으로 삭제할 수 없습니다. 설계변경에서 진행하십시요.";
		} else {
			delete = "DELETE from MBOM_STR where gid='"+gid+"' and tag='1'";
			modDAO.executeUpdate(delete);
			data = "정상적으로 삭제되었습니다.";
		}

		//MBOM_MASTER에 BOM상태 전달하기 
		query = "SELECT COUNT(*) FROM mbom_str WHERE gid = '"+gid+"'";
		int cnt = modDAO.getTotalCount(query);
		if(cnt == 1) {			//bom_status = '1' : 기초정보 작성중
			update = "UPDATE mbom_master SET bom_status='1' where pid = '"+gid+"'";
			modDAO.executeUpdate(update);
		} else {				//bom_status = '3' : BOM등록중
			update = "UPDATE mbom_master SET bom_status='3' where pid = '"+gid+"'";
			modDAO.executeUpdate(update);
		}

		return data;
	}

	/**********************************************************************
	 * Template을 Assy코드로 바꿔 등록하기
	 **********************************************************************/
	public String changeAssyCode(String gid,String login_id) throws Exception
	{
		String data="",where="",model_code="",bom_status="";

		//모델코드,bom_status 정보를 찾기
		where = "where pid='"+gid+"'";
		model_code = modDAO.getColumData("mbom_master","model_code",where);
		bom_status = modDAO.getColumData("mbom_master","bom_status",where);

		//BOM진행 상태 검사
		if(!bom_status.equals("2")) {
			data = "공정템플릿정보가 있을경우만 공정ASSY 등록 가능합니다.";
			return data;
		}

		//Template 정보를 배열에 담기
		ArrayList item_list = new ArrayList();
		item_list = tmpDAO.getTempBomList(gid);
		int cnt = item_list.size();
		item = new String[cnt][4];
		for(int i=0; i<cnt; i++) for(int j=0; j<4; j++) item[i][j]="";

		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			item[an][0] = table.getPid();
			item[an][1] = table.getParentCode();
			item[an][2] = table.getChildCode();
			item[an][3] = table.getLevelNo();
			
			////System.out.println(item[an][3]+":"+item[an][1]+":"+item[an][2]);
			an++;
		}
		if(cnt == 0) {
			data = "등록한 공정템플릿정보가 없습니다.";
			return data;
		}

		//----------------------------------------------------
		//ASSY코드,Spec,tag='2'[Assy코드임]으로 바꿔 등록하기
		//----------------------------------------------------
		String parent_code = item[0][1];						//모품목코드
		String state_name = tmpDAO.getStateName(parent_code);	//형상명 (ASSY규격의 첫번째 필드값)

		//item_master에 등록할 정보
		String register_info = tmpDAO.getRegInfo(login_id);		//등록자정보(소속/직급/이름)
		String register_date = anbdt.getDate();					//등록일자 (yyyy-mm-dd)
		String one_class="",serial_no="";						//제품군분류코드,Serial No

		//맨처음 모품목코드를 검사
		if(parent_code.length() > 9) {
			one_class = parent_code.substring(3,4);				//제품군분류코드
			serial_no = parent_code.substring(4,8);				//Serial No
		} else {
			data = "모품목코드가 부품코드채번규칙에 벗어났습니다. 확인바랍니다.";
			return data;
		}

		//공정Assy코드 update및 item_master에 공정Assy코드 등록하기
		for(int i=0; i<cnt; i++) {
			parent_code = saveAssyCode(item[i][0],gid,parent_code,one_class,serial_no,item[i][2],state_name,login_id,register_info,register_date,model_code,one_class);
		}

		//MBOM_MASTER의 bom_status='3'으로 바꿔주기
		update = "UPDATE mbom_master SET bom_status='3' where pid = '"+gid+"'";
		modDAO.executeUpdate(update);

		
		data = "정상적으로 등록되었습니다.";
		return data="";
	}

	/**********************************************************************
	 * Template을 Assy코드 해당DB에 저장하기
	 * serial_no:채번규칙상의 시리얼번호,op_code:공정코드,state_name:형상명
	 * register_id,register_info,register_date : 사번,소속/직급/이름,입력일
	 * model_code:모델코드,pdg_kind_code:제품군분류코드
	 **********************************************************************/
	public String saveAssyCode(String pid,String gid,String parent_code,String one_class,String serial_no,String op_code,
						String state_name,String register_id,String register_info,String register_date,
						String model_code,String pdg_kind_code) throws Exception
	{
		String item_no="",item_name="PCB ASSY",stock_unit="EA",item_type="1",where="",input="",update="";
		//String spec=state_name+",";			//규격
		String spec="";			//규격
		String[] data = new String[6];

		//해당ASSY코드 조합하여 만들기
		item_no = "1EP"+one_class+serial_no+op_code;							//변환된 공정ASSY코드

		//공정코드에 해당되는 규격정보 가져와 해당공정코드의 규격만들기
		where = "where m_code='"+op_code+"'";
		spec += modDAO.getColumData("mbom_env","spec",where);	
		
		//해당Assy코드가 이미 부품마스터에서 채번되었는지 알아보기
		//[0부품이름,1규격,2메이커명,3형명,4수량단위,5item_type] 
		data = modDAO.getComponentInfo(item_no);
		if(data[0].length() != 0) {
			item_name = data[0];		
			spec = data[1];
			stock_unit = data[4];
			item_type=data[5];
		}

		//MBOM_STR에 해당정보 UPDATE하기
		update = "UPDATE mbom_str SET parent_code='"+parent_code+"',child_code='"+item_no+"',"; 
		//update += "part_spec='"+spec+"',op_code='"+op_code+"',tag='2' ";
		update += "part_spec='"+spec+"',tag='2' ";
		update += "WHERE pid='"+pid+"' and gid='"+gid+"'";
		modDAO.executeUpdate(update);

		//ITEM_MASTER에 해당정보 입력하기
		if(data[0].length() == 0) {
			input = "INSERT INTO item_master(item_no,item_desc,register_id,register_info,register_date,stat,";
			input += "item_type,item_name,stock_unit,model_code,code_type,one_class,serial_no,assy_type,";
			input += "op_code) values('";
			input += item_no+"','"+spec+"','"+register_id+"','"+register_info+"','"+register_date+"','6','";
			input += item_type+"','"+item_name+"','"+stock_unit+"','"+model_code+"','"+"1"+"','"+pdg_kind_code+"','";
			input += serial_no+"','"+"P"+"','"+op_code+"')";
			modDAO.executeUpdate(input);
		}
		return item_no;
	}

	/**********************************************************************
	 * 공정TEMPLATE구성을 위한 BOM LIST 단, 회로ASSY일때만 링크달기
	 * gid : group관리코드, level_no : 0, parent_code : model_code
	 **********************************************************************/
	public ArrayList getStrListEleLink(String gid,String level_no,String parent_code) throws Exception
	{
		String pcode="",ccode="",c_head="";
		// 배열에 담는다.
		saveBomArray(gid,level_no,parent_code);

		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		for(int i=0; i<an; i++) {
			table = new mbomStrTable();
			table.setPid(item[i][0]);

			pcode = item[i][1];						//parent_code
			ccode = item[i][2];						//child_code
			c_head = ccode.substring(0,2);			//자품목코드의 두자리
			if(!item[i][3].equals("0") & c_head.equals(e_assy))	//첫번째값은 제외[level no : 0]
				pcode = "<a href=\"javascript:strView('"+item[i][0]+"','"+gid+"','"+parent_code+"');\">"+item[i][1]+"</a>";
			table.setParentCode(pcode);
			table.setChildCode(ccode);

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

}