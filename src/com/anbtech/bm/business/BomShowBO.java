package com.anbtech.bm.business;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class BomShowBO
{
	private Connection con;
	private com.anbtech.bm.db.BomShowDAO showDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	private com.anbtech.util.SortArray sort = new com.anbtech.util.SortArray();		//정렬하기
	
	private String query = "";
	private String[][] item = null;				//TREE정보를 배열로 담기
	private int an = 0;							//items의 배열 갯수

	private String[][] plist = null;			//읽은 파일내용을 배열에 담기 
	private int elecnt=0;						//읽은 파일의 한 라인당 데이터 갯수 
	private int linecnt=0;						//읽은 파일의 라인갯수 

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public BomShowBO(Connection con) 
	{
		this.con = con;
		showDAO = new com.anbtech.bm.db.BomShowDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		리포트 출력하기
	//
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * MBOM정보를 ArrayList로 담아 리포트로 출력하기
	 *  sel_date : 유효일자
	 **********************************************************************/
	public ArrayList sortArrayStrList(String gid,String level_no,String parent_code,String sel_date,
		String bundle) throws Exception
	{
		//선언
		int bun = Integer.parseInt(bundle);		//묶음 단위(기본 8);
		ArrayList item_list = new ArrayList();

		//전역 배열만들기
		saveArrayStrList(gid,level_no,parent_code,sel_date);

		//갯수 파악하기
		if(an == 0) return item_list;
		
		//동일한 모/자품목에 대한 같은 Location끼리 묶을 배열선언
		String[][] data = new String[an][10];
		for(int i=0; i<an; i++) for(int j=0; j<10; j++) data[i][j]="";

		//새로운 배열로 담기 : Location 번호 8개씩 묶어서
		int cnt = an -1;
		int n=0,q=1;					//신규배열번호, 갯수
		for(int i=0; i<=cnt; i++) {
			//처음데이터는 무조건 배열에 담는다.
			if(i == 0) {
				data[n][1] = item[i][1];		//모품목코드
				data[n][2] = item[i][2];		//자품목코드
				data[n][3] = item[i][3];		//Level no
				data[n][4] = item[i][4];		//Part Name
				data[n][5] = item[i][5];		//Part Spec
				data[n][6] = item[i][6];		//Location
				data[n][7] = item[i][7];		//Op code
				data[n][8] = item[i][8];		//Qty unit
				data[n][9] = "1";				//Qty
				n++;
			}
			//중간데이터 처리하기
			else if(i < cnt) {
				//모품목코드와 자품목코드가 같으면 : location 만 기록
				if(item[i][1].equals(item[i+1][1]) && item[i][2].equals(item[i+1][2])) {
					if(q%bun==0)	data[n][6] += item[i][6]+",<br>";	//location : n개씩 묶는다.
					else		data[n][6] += item[i][6]+",";			//location
					q++;
				} 
				//모품목코드와 자품목코드가 다르면 : 전부를 작성한다.
				else {
					data[n][1] = item[i][1];			//모품목코드
					data[n][2] = item[i][2];			//자품목코드
					data[n][3] = item[i][3];			//Level no
					data[n][4] = item[i][4];			//Part Name
					data[n][5] = item[i][5];			//Part Spec
					data[n][6] += item[i][6];			//Location
					data[n][7] = item[i][7];			//Op code
					data[n][8] = item[i][8];			//Qty unit
					data[n][9] = Integer.toString(q);	//qty
					n++;
					q=1;
				}
			}
			//마지막 데이터는 무조건 배열에 담는다.
			else {
				data[n][1] = item[i][1];				//모품목코드
				data[n][2] = item[i][2];				//자품목코드
				data[n][3] = item[i][3];				//Level no
				data[n][4] = item[i][4];				//Part Name
				data[n][5] = item[i][5];				//Part Spec
				data[n][6] += item[i][6];				//Location
				data[n][7] = item[i][7];				//Op code
				data[n][8] = item[i][8];				//Qty unit
				data[n][9] = Integer.toString(q);		//Qty
			} //else
		} //for

		//출력할 데이터로 ArrayList로 담아 전달하기
		for(int i=0; i<=n; i++) {
			mbomStrTable mst = new mbomStrTable();
			mst.setParentCode(data[i][1]);
			mst.setChildCode(data[i][2]);
			mst.setLevelNo(data[i][3]);
			mst.setPartName(data[i][4]);
			mst.setPartSpec(data[i][5]);
			mst.setLocation(data[i][6]);
			mst.setOpCode(data[i][7]);
			mst.setQtyUnit(data[i][8]);
			mst.setQty(data[i][9]);
			item_list.add(mst); 
		}
		return item_list;

	}

	/**********************************************************************
	 * MBOM정보를 배열에 담기
	 *  sel_date : 유효일자
	 **********************************************************************/
	public void saveArrayStrList(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardBomItems(gid,level_no,parent_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][10];
		for(int i=0; i<cnt; i++) for(int j=0; j<10; j++) item[i][j]="";

		//배열에 담기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		an=0;
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
	//		확정된 정전개 구조 보기
	//
	//
	//---------------------------------------------------------------------

	/**********************************************************************
	 * 다단계 MBOM_STR에서 BOM TREE정보를 구하기 : TEXT
	 *  sel_date : 유효일자
	 **********************************************************************/
	public ArrayList viewStrList(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardBomItems(gid,level_no,parent_code,sel_date);

		//출력해보기
/*		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			//System.out.println(table.getParentCode()+":"+table.getChildCode()+":"+table.getLevelNo()+":"+table.getLocation());
		}
*/
		return item_list;
	}
	/**********************************************************************
	 * 단단계 MBOM_STR에서 BOM TREE정보를 구하기 : TEXT
	 *  sel_date : 유효일자
	 **********************************************************************/
	public ArrayList viewSingleStrList(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardSingleBomItems(gid,level_no,parent_code,sel_date);
		return item_list;
	}

	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기 : TREE
	 *  sel_date : 유효일자
	 **********************************************************************/
	public String makeFrdTree(String gid,String level_no,String parent_code,String sel_date,String url) throws Exception
	{
		String tree = "";				//tree
		
		//배열 Item으로 담기
		saveFrdStrArray(gid,level_no,parent_code,sel_date);
		int cnt = an;

		//tree_items만들기 (an : item의 갯수)
		if(an > 0){
			String space = " ";
			int st = 0;		//시작점 level
			int cu = 0;		//현 읽은 level
			int di = 0;		//차이 : cu - st 

			String link = "";	// 링크URL을 담을 변수
			
			tree = "var TREE_ITEMS = [";	//최초 시작점
			for(int bi=0; bi<cnt; bi++){
				cu = Integer.parseInt(item[bi][3]);
				//link = url+"?mode=frd_tree&gid="+gid+"&level_no="+Integer.toString(cu+1)+"&parent_code="+item[bi][2]+"&url="+url;
				
				//시작점
				if(bi == 0) {
					st = Integer.parseInt(item[bi][3]);
					space = " ";
					for(int s=0; s < st; s++) space += "   ";
					tree += space + "['"+item[bi][2]+"','"+link+"'";
					if(cnt == 1) tree += "],";		//1개일경우
				} else if(bi == (cnt-1)) {
					cu = Integer.parseInt(item[bi][3]);
					di = cu - st;
				
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						tree += ",";		//끝점
						tree += space + "['"+item[bi][2]+"','"+link+"'],";		//시작점
					} else if(di == 0) {
						tree += "],";		//끝점
						tree += space + "['"+item[bi][2]+"','"+link+"'],";		//시작점
					} else {
						tree += "],";		//끝점
						for(int m=0; m > di; di++) tree += space + "],"; //앞레벨 끝점
						tree += space + "['"+item[bi][2]+"','"+link+"'],";		//시작점
					}
					
					//마지막 레벨 닫기
					di = cu - 0;
					for(int e=di; e > 0; e--) {
						space = " ";
						for(int es=1; es < e; es++) space += "   ";
						tree += space + "],"; //끝점
					}
				} else {
					cu = Integer.parseInt(item[bi][3]);
					di = cu - st;
					
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						tree += ",";		//끝점
						tree += space + "['"+item[bi][2]+"','"+link+"'";		//시작점
					} else if(di == 0) {
						tree += "],";		//끝점
						tree += space + "['"+item[bi][2]+"','"+link+"'";		//시작점
					} else {
						tree += "],";		//끝점
						for(int m=0; m > di; di++) tree += space + "],";		//앞레벨 끝점
						tree += space + "['"+item[bi][2]+"','"+link+"'";		//시작점
					}
					st = Integer.parseInt(item[bi][3]);
				} //if	
			} //for
			tree += "];";	//맨 마지막
		} //if 
		return tree;
	} 
	
	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기
	 * 정전개 TREE 구조체계를 배열에 담기 : 하부구조 전체
	 **********************************************************************/
	public void saveFrdStrArray(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardBomItems(gid,level_no,parent_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][10];
		for(int i=0; i<cnt; i++) for(int j=0; j<10; j++) item[i][j]="";

		//배열에 담기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
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

			////System.out.println(item[an][3]+":"+item[an][1]+":"+item[an][2]);
			an++;
		}
	}

	//--------------------------------------------------------------------
	//
	//		확정된 BOM 역전개 보기
	//
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * MBOM_STR에서 역전개 BOM TREE정보를 구하기 : TEXT
	 *  sel_date : 유효일자
	 **********************************************************************/
	public ArrayList viewRevStrList(String child_code,String sel_date) throws Exception
	{
		//배열에 담기
		makeRevTextArray(child_code,sel_date);

		//ArrayList에 담기
		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		for(int i=0; i<an; i++) {
			table = new mbomStrTable();
			table.setLevelNo(item[i][0]); 
			table.setParentCode(item[i][1]);
			table.setChildCode(item[i][2]);		
			table.setPartName(item[i][3]);	
			table.setPartSpec(item[i][4]);	
			table.setLocation(item[i][5]);	
			table.setOpCode(item[i][6]);
			////System.out.println(item[i][0]+":"+item[i][1]+":"+item[i][1]);
			table_list.add(table);
		}
		return table_list;
	}

	/**********************************************************************
	 * MBOM_STR에서 역전개 BOM TREE정보를 구하기 : TREE
	 *  sel_date : 유효일자
	 **********************************************************************/
	public String makeRevTree(String child_code,String sel_date,String url) throws Exception
	{
		String tree = "";				//tree

		//전체수량및 배열 만들기
		makeRevStrArray(child_code,sel_date);
		int cnt = an;
		
		//tree_items만들기 (an : item의 갯수)
		if(an > 0){
			String space = " ";
			int st = 0;		//시작점 level
			int cu = 0;		//현 읽은 level
			int di = 0;		//차이 : cu - st 

			String link = "";	// 링크URL을 담을 변수
			
			tree = "var TREE_ITEMS = [";	//최초 시작점
			for(int bi=0; bi<cnt; bi++){
				
				//시작점
				if(bi == 0) {
					st = Integer.parseInt(item[bi][0]);
					space = " ";
					for(int s=0; s < st; s++) space += "   ";
					tree += space + "['"+item[bi][1]+"','"+link+"'";
					if(cnt == 1) tree += "],";		//1개일경우
				} else if(bi == (cnt-1)) {
					cu = Integer.parseInt(item[bi][0]);
					di = cu - st;
				
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						tree += ",";		//끝점
						tree += space + "['"+item[bi][1]+"','"+link+"'],";		//시작점
					} else if(di == 0) {
						tree += "],";		//끝점
						tree += space + "['"+item[bi][1]+"','"+link+"'],";		//시작점
					} else {
						tree += "],";		//끝점
						for(int m=0; m > di; di++) tree += space + "],"; //앞레벨 끝점
						tree += space + "['"+item[bi][1]+"','"+link+"'],";		//시작점
					}
					
					//마지막 레벨 닫기
					di = cu - 0;
					for(int e=di; e > 0; e--) {
						space = " ";
						for(int es=1; es < e; es++) space += "   ";
						tree += space + "],"; //끝점
					}
				} else {
					cu = Integer.parseInt(item[bi][0]);
					di = cu - st;
					
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						tree += ",";		//끝점
						tree += space + "['"+item[bi][1]+"','"+link+"'";		//시작점
					} else if(di == 0) {
						tree += "],";		//끝점
						tree += space + "['"+item[bi][1]+"','"+link+"'";		//시작점
					} else {
						tree += "],";		//끝점
						for(int m=0; m > di; di++) tree += space + "],"; //앞레벨 끝점
						tree += space + "['"+item[bi][1]+"','"+link+"'";		//시작점
					}
					st = Integer.parseInt(item[bi][0]);
				} //if	
			} //for
			tree += "];";	//맨 마지막
		} //if 
		return tree;
	} 

	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기
	 * 역전개 TREE 구조체계를 배열에 담기
	 **********************************************************************/
	public void saveRevStrArray(String child_code,String sel_date) throws Exception
	{
		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getReverseBomItems(child_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][7];
		for(int i=0; i<cnt; i++) for(int j=0; j<7; j++) item[i][j]="0";

		//배열에 담기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			item[an][0] = table.getParentCode();
			item[an][1] = table.getChildCode();
			item[an][2] = table.getLevelNo();
			item[an][3] = table.getPartName();
			item[an][4] = table.getPartSpec();
			item[an][5] = "";	//location
			item[an][6] = table.getOpCode();

			////System.out.println(item[an][2]+":"+item[an][0]+":"+item[an][1]);
			an++;
		}
	}

	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기
	 * 역전개 TEXT 구조체계를 만들기위한 배열로 다시 담기 : 모자관계도 바꾼다.
	 **********************************************************************/
	public void makeRevTextArray(String child_code,String sel_date) throws Exception
	{
		//배열로 담기
		saveRevStrArray(child_code,sel_date);

		//TREE구조를 만들기위해 배열을 다시만들기
		String[][] data = new String[an][7];
		for(int i=1; i<an; i++) for(int j=0; j<7; j++) data[i][j]="";
		////System.out.println(data[0][0]+":"+data[0][1]);

		int n = 1, k = 0;
		for(int i=0; i<an; i++) {
			if(item[i][2].equals("0")) { 
				n = 1;
			} else {
				data[k][0] = Integer.toString(n);	//Level No
				data[k][1] = item[i][1];			//Parent Code (원:자코드 -> 모코드로)
				data[k][2] = item[i][0];			//Child Code  (원:모코드 -> 자코드로)
				data[k][3] = item[i+1][3];			//Part Name
				data[k][4] = item[i+1][4];			//Part Spec
				data[k][5] = item[i+1][5];			//Location
				data[k][6] = item[i+1][6];			//OP Code

				////System.out.println(data[k][0]+":"+data[k][1]+":"+data[k][2]+":"+data[k][3]+":"+data[k][4]+":"+data[k][5]+":"+data[k][6]);
				n++;
				k++;
			} //if
		} //for

		//item배열로 다시 담기
		item = new String[k][7];
		an = k;
		for(int i=0; i<k; i++) {
			for(int j=0; j<7; j++) item[i][j] = data[i][j];		
//			//System.out.println(item[i][0]+":"+item[i][1]+":"+item[i][2]);
		}
	}

	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기
	 * 역전개 TREE 구조체계를 만들기위한 배열로 다시 담기
	 **********************************************************************/
	public void makeRevStrArray(String child_code,String sel_date) throws Exception
	{
		//배열로 담기
		saveRevStrArray(child_code,sel_date);
		if(an == 0) return;

		//TREE구조를 만들기위해 배열을 다시만들기
		String[][] data = new String[an][2];
		data[0][0] = "0"; data[0][1] = child_code; 	//0레벨을 child code로 한다.
		for(int i=1; i<an; i++) data[i][0]=data[i][1]="";
		////System.out.println(data[0][0]+":"+data[0][1]);

		int n = 1, k = 1;
		for(int i=0; i<an; i++) {
			if(item[i][2].equals("0")) { 
				n = 1;
			} else {
				data[k][0] = Integer.toString(n);	//Level No
				data[k][1] = item[i][0];			//Parent Code
				//if(item[i][2].equals("1")) data[k][1] = "M:"+item[i][0];	//모델코드임을 표시
				////System.out.println(data[k][0]+":"+data[k][1]);
				n++;
				k++;
			} //if
		} //for

		//item배열로 다시 담기
		item = new String[k][2];
		an = k;
		for(int i=0; i<k; i++) {
			item[i][0] = data[i][0];				//Level No
			item[i][1] = data[i][1];				//Parent Code
			////System.out.println(item[i][0]+":"+item[i][1]);
		}
	}

	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기
	 * 정/역전개 TREE 구조체계를 만들기위한 배열갯수
	 **********************************************************************/
	public String getArrayCount() 
	{
		return Integer.toString(an);
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
	public ArrayList getUniqueMultiLevelBom(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardBomItems(gid,level_no,parent_code,sel_date);
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
	public ArrayList getUniqueSingleLevelBom(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardSingleBomItems(gid,level_no,parent_code,sel_date);
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
