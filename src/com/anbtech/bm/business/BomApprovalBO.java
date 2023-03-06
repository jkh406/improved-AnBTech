package com.anbtech.bm.business;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class BomApprovalBO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.bm.db.BomApprovalDAO appDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	
	private String query = "",update="";
	private String[][] item = null;				//TREE정보를 배열로 담기
	private int an = 0;							//items의 배열 갯수

	private String[][] plist = null;			//읽은 파일내용을 배열에 담기 
	private int elecnt=0;						//읽은 파일의 한 라인당 데이터 갯수 
	private int linecnt=0;						//읽은 파일의 라인갯수 

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public BomApprovalBO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
		appDAO = new com.anbtech.bm.db.BomApprovalDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		BOM결재시 검사항목 검사하기
	//		1. Phantom Assy일 가능성이 있는지.
	//		2. 회로Assy인 경우 Location No 있는지, 있으면 중복인지 검사하기 
	//---------------------------------------------------------------------
	/**********************************************************************
	 * Phantom Assy가 있는지 판단하기 : 단, phantom_assy라 표시된것은 제외
	 * [자품목코드가 이중으로 모품목 코드로 등록되는 경우]
	 **********************************************************************/
	public String checkPhantomAssy(String gid) throws Exception
	{
		
		String rtn = "";			//리턴값
		String where="",model_code="";

		//모델코드 찾기
		where = "where pid='"+gid+"'";
		model_code = modDAO.getColumData("MBOM_MASTER","model_code",where);

		//현재등록된 BOM LIST
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getForwardItems(gid,"0",model_code);
		int item_len = item_list.size();		

		//배열을 만들고 clear한다.
		String[][] list = new String[item_len][4];
		for(int i=0; i<item_len; i++) for(int j=0; j<4; j++) list[i][j] = "";

		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		int n=0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			list[n][0] = table.getLevelNo();
			list[n][1] = table.getParentCode();
			list[n][2] = table.getChildCode();
			list[n][3] = table.getAssyDup();
			
			//Phantom Assy라 지정된경우는 제외시킴
			if(list[n][3].equals("D")) list[n][2]="";	

			n++;
		}

		//모품목코드가 자품목코드에 2개이상 있으면 1개를 제외한 나머지는 phantom assy코드임
		for(int i=0; i<item_len; i++) {
			int dn = 0;					//중복되는 값 갯수
			String sm = "";				//첫번째 같은값
			for(int j=0; j<item_len; j++) {
				if(list[i][1].equals(list[j][2])) {
					dn++;
					if(dn == 1)		sm = list[j][0]+": "+list[j][1]+"/[*"+list[j][2]+"], ";
					if(dn == 2)		rtn += sm+list[j][0]+": "+list[j][1]+"/[*"+list[j][2]+"], ";
					else if(dn > 2) rtn += list[j][0]+": "+list[j][1]+"/[*"+list[j][2]+"], ";
				}
			}
		}
		if(rtn.length() != 0) {
			String msg = "*** "+rtn;
			rtn = "<검사:Assy코드가 중복[Phantom Assy제외]으로 자품목으로 입력된 경우>||";
			rtn += msg;
			rtn += "||[*표시는 Assy코드로 Phantom Assy인 경우를 제외하고는 ";
			rtn += "자품목으로 이중 등록할 수 없습니다. | Phantom Assy라 판단되는 코드를";
			rtn += "삭제후 다시 입력하면 Phantom Assy라 자동 표시됩니다.]||";
		}
		return rtn;
	}

	/**********************************************************************
	 * Location 검사하기
	 * 1. Location 유무 2. 중복검사
	 **********************************************************************/
	public String checkLocation(String gid) throws Exception
	{
		
		String rtn = "";			//리턴값
		
		//현재등록된 회로만 출력한 BOM LIST
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getElectronicItems(gid);
		int item_len = item_list.size();		

		//배열을 만들고 clear한다.
		String[][] list = new String[item_len][4];
		for(int i=0; i<item_len; i++) for(int j=0; j<4; j++) list[i][j] = "";

		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		int n=0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			list[n][0] = table.getLevelNo();
			list[n][1] = table.getParentCode();
			list[n][2] = table.getChildCode();
			list[n][3] = table.getLocation();
			list[n][3] = list[n][3].trim();
			n++;
		}
		//검사1. Location No 유무 검사하기
		String loc_yn = "";
		for(int i=0; i<item_len; i++) {
			if(list[i][3].length() == 0) {
				loc_yn += list[i][0]+": "+list[i][1]+"/[*"+list[i][2]+"], <br>";
			}
		}
		if(loc_yn.length() != 0) {
			rtn = "<검사1: Location 입력 유무 검사 결과 [레벨:모품목코드:자품목코드]>||";
			rtn += loc_yn+"||";
		}

		//검사2. Location No 중복 검사하기
		String loc_dup = "";
		int dup = 1;
		for(int i=0; i<item_len-1; i++) {
			for(int j=i+1; j<item_len; j++) {
				if(list[i][3].equals(list[j][3]) && (list[i][3].length() != 0)) {
					if(dup%2 == 0) loc_dup += list[i][0]+": "+list[i][1]+"/[*"+list[i][2]+" : "+list[i][3]+"], <br>";
					else loc_dup += list[i][0]+": "+list[i][1]+"/[*"+list[i][2]+" : "+list[i][3]+"], ";
					dup++;
					break;
				}
			}
		}
		if(loc_dup.length() != 0) {
			rtn += "<검사2: Location 중복입력 검사 결과 [레벨:모품목코드:자품목코드]>||";
			rtn += loc_dup;
		}
		return rtn;
	}

	/**********************************************************************
	 * BOM구성 검사하기
	 * 1레벨의 BOM이 구성되었는지 검사하기
	 **********************************************************************/
	public String checkAssySet(String gid) throws Exception
	{
		String data = "";
		int cnt = 0;

		//1레벨의 ASSY코드만 찾기
		ArrayList item_list = new ArrayList();
		item_list = appDAO.getLevelOneAssy(gid);

		//검사하기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();

			query = "select count(*) from mbom_str where gid='"+gid+"' and parent_code='"+table.getChildCode()+"'";
			cnt = modDAO.getTotalCount(query);
			if(cnt == 0) data += table.getChildCode()+", ";
		}
		if(data.length() != 0) data += " 는 미구성 ASSY SET 코드입니다. BOM을 구성후 진행하십시오.";
		
		return data;
	}
}