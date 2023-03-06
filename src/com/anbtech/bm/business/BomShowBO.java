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
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	private com.anbtech.util.SortArray sort = new com.anbtech.util.SortArray();		//�����ϱ�
	
	private String query = "";
	private String[][] item = null;				//TREE������ �迭�� ���
	private int an = 0;							//items�� �迭 ����

	private String[][] plist = null;			//���� ���ϳ����� �迭�� ��� 
	private int elecnt=0;						//���� ������ �� ���δ� ������ ���� 
	private int linecnt=0;						//���� ������ ���ΰ��� 

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public BomShowBO(Connection con) 
	{
		this.con = con;
		showDAO = new com.anbtech.bm.db.BomShowDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		����Ʈ ����ϱ�
	//
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * MBOM������ ArrayList�� ��� ����Ʈ�� ����ϱ�
	 *  sel_date : ��ȿ����
	 **********************************************************************/
	public ArrayList sortArrayStrList(String gid,String level_no,String parent_code,String sel_date,
		String bundle) throws Exception
	{
		//����
		int bun = Integer.parseInt(bundle);		//���� ����(�⺻ 8);
		ArrayList item_list = new ArrayList();

		//���� �迭�����
		saveArrayStrList(gid,level_no,parent_code,sel_date);

		//���� �ľ��ϱ�
		if(an == 0) return item_list;
		
		//������ ��/��ǰ�� ���� ���� Location���� ���� �迭����
		String[][] data = new String[an][10];
		for(int i=0; i<an; i++) for(int j=0; j<10; j++) data[i][j]="";

		//���ο� �迭�� ��� : Location ��ȣ 8���� ���
		int cnt = an -1;
		int n=0,q=1;					//�űԹ迭��ȣ, ����
		for(int i=0; i<=cnt; i++) {
			//ó�������ʹ� ������ �迭�� ��´�.
			if(i == 0) {
				data[n][1] = item[i][1];		//��ǰ���ڵ�
				data[n][2] = item[i][2];		//��ǰ���ڵ�
				data[n][3] = item[i][3];		//Level no
				data[n][4] = item[i][4];		//Part Name
				data[n][5] = item[i][5];		//Part Spec
				data[n][6] = item[i][6];		//Location
				data[n][7] = item[i][7];		//Op code
				data[n][8] = item[i][8];		//Qty unit
				data[n][9] = "1";				//Qty
				n++;
			}
			//�߰������� ó���ϱ�
			else if(i < cnt) {
				//��ǰ���ڵ�� ��ǰ���ڵ尡 ������ : location �� ���
				if(item[i][1].equals(item[i+1][1]) && item[i][2].equals(item[i+1][2])) {
					if(q%bun==0)	data[n][6] += item[i][6]+",<br>";	//location : n���� ���´�.
					else		data[n][6] += item[i][6]+",";			//location
					q++;
				} 
				//��ǰ���ڵ�� ��ǰ���ڵ尡 �ٸ��� : ���θ� �ۼ��Ѵ�.
				else {
					data[n][1] = item[i][1];			//��ǰ���ڵ�
					data[n][2] = item[i][2];			//��ǰ���ڵ�
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
			//������ �����ʹ� ������ �迭�� ��´�.
			else {
				data[n][1] = item[i][1];				//��ǰ���ڵ�
				data[n][2] = item[i][2];				//��ǰ���ڵ�
				data[n][3] = item[i][3];				//Level no
				data[n][4] = item[i][4];				//Part Name
				data[n][5] = item[i][5];				//Part Spec
				data[n][6] += item[i][6];				//Location
				data[n][7] = item[i][7];				//Op code
				data[n][8] = item[i][8];				//Qty unit
				data[n][9] = Integer.toString(q);		//Qty
			} //else
		} //for

		//����� �����ͷ� ArrayList�� ��� �����ϱ�
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
	 * MBOM������ �迭�� ���
	 *  sel_date : ��ȿ����
	 **********************************************************************/
	public void saveArrayStrList(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardBomItems(gid,level_no,parent_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][10];
		for(int i=0; i<cnt; i++) for(int j=0; j<10; j++) item[i][j]="";

		//�迭�� ���
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
	//		Ȯ���� ������ ���� ����
	//
	//
	//---------------------------------------------------------------------

	/**********************************************************************
	 * �ٴܰ� MBOM_STR���� BOM TREE������ ���ϱ� : TEXT
	 *  sel_date : ��ȿ����
	 **********************************************************************/
	public ArrayList viewStrList(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardBomItems(gid,level_no,parent_code,sel_date);

		//����غ���
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
	 * �ܴܰ� MBOM_STR���� BOM TREE������ ���ϱ� : TEXT
	 *  sel_date : ��ȿ����
	 **********************************************************************/
	public ArrayList viewSingleStrList(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardSingleBomItems(gid,level_no,parent_code,sel_date);
		return item_list;
	}

	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ� : TREE
	 *  sel_date : ��ȿ����
	 **********************************************************************/
	public String makeFrdTree(String gid,String level_no,String parent_code,String sel_date,String url) throws Exception
	{
		String tree = "";				//tree
		
		//�迭 Item���� ���
		saveFrdStrArray(gid,level_no,parent_code,sel_date);
		int cnt = an;

		//tree_items����� (an : item�� ����)
		if(an > 0){
			String space = " ";
			int st = 0;		//������ level
			int cu = 0;		//�� ���� level
			int di = 0;		//���� : cu - st 

			String link = "";	// ��ũURL�� ���� ����
			
			tree = "var TREE_ITEMS = [";	//���� ������
			for(int bi=0; bi<cnt; bi++){
				cu = Integer.parseInt(item[bi][3]);
				//link = url+"?mode=frd_tree&gid="+gid+"&level_no="+Integer.toString(cu+1)+"&parent_code="+item[bi][2]+"&url="+url;
				
				//������
				if(bi == 0) {
					st = Integer.parseInt(item[bi][3]);
					space = " ";
					for(int s=0; s < st; s++) space += "   ";
					tree += space + "['"+item[bi][2]+"','"+link+"'";
					if(cnt == 1) tree += "],";		//1���ϰ��
				} else if(bi == (cnt-1)) {
					cu = Integer.parseInt(item[bi][3]);
					di = cu - st;
				
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						tree += ",";		//����
						tree += space + "['"+item[bi][2]+"','"+link+"'],";		//������
					} else if(di == 0) {
						tree += "],";		//����
						tree += space + "['"+item[bi][2]+"','"+link+"'],";		//������
					} else {
						tree += "],";		//����
						for(int m=0; m > di; di++) tree += space + "],"; //�շ��� ����
						tree += space + "['"+item[bi][2]+"','"+link+"'],";		//������
					}
					
					//������ ���� �ݱ�
					di = cu - 0;
					for(int e=di; e > 0; e--) {
						space = " ";
						for(int es=1; es < e; es++) space += "   ";
						tree += space + "],"; //����
					}
				} else {
					cu = Integer.parseInt(item[bi][3]);
					di = cu - st;
					
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						tree += ",";		//����
						tree += space + "['"+item[bi][2]+"','"+link+"'";		//������
					} else if(di == 0) {
						tree += "],";		//����
						tree += space + "['"+item[bi][2]+"','"+link+"'";		//������
					} else {
						tree += "],";		//����
						for(int m=0; m > di; di++) tree += space + "],";		//�շ��� ����
						tree += space + "['"+item[bi][2]+"','"+link+"'";		//������
					}
					st = Integer.parseInt(item[bi][3]);
				} //if	
			} //for
			tree += "];";	//�� ������
		} //if 
		return tree;
	} 
	
	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ�
	 * ������ TREE ����ü�踦 �迭�� ��� : �Ϻα��� ��ü
	 **********************************************************************/
	public void saveFrdStrArray(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardBomItems(gid,level_no,parent_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][10];
		for(int i=0; i<cnt; i++) for(int j=0; j<10; j++) item[i][j]="";

		//�迭�� ���
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
	//		Ȯ���� BOM ������ ����
	//
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * MBOM_STR���� ������ BOM TREE������ ���ϱ� : TEXT
	 *  sel_date : ��ȿ����
	 **********************************************************************/
	public ArrayList viewRevStrList(String child_code,String sel_date) throws Exception
	{
		//�迭�� ���
		makeRevTextArray(child_code,sel_date);

		//ArrayList�� ���
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
	 * MBOM_STR���� ������ BOM TREE������ ���ϱ� : TREE
	 *  sel_date : ��ȿ����
	 **********************************************************************/
	public String makeRevTree(String child_code,String sel_date,String url) throws Exception
	{
		String tree = "";				//tree

		//��ü������ �迭 �����
		makeRevStrArray(child_code,sel_date);
		int cnt = an;
		
		//tree_items����� (an : item�� ����)
		if(an > 0){
			String space = " ";
			int st = 0;		//������ level
			int cu = 0;		//�� ���� level
			int di = 0;		//���� : cu - st 

			String link = "";	// ��ũURL�� ���� ����
			
			tree = "var TREE_ITEMS = [";	//���� ������
			for(int bi=0; bi<cnt; bi++){
				
				//������
				if(bi == 0) {
					st = Integer.parseInt(item[bi][0]);
					space = " ";
					for(int s=0; s < st; s++) space += "   ";
					tree += space + "['"+item[bi][1]+"','"+link+"'";
					if(cnt == 1) tree += "],";		//1���ϰ��
				} else if(bi == (cnt-1)) {
					cu = Integer.parseInt(item[bi][0]);
					di = cu - st;
				
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						tree += ",";		//����
						tree += space + "['"+item[bi][1]+"','"+link+"'],";		//������
					} else if(di == 0) {
						tree += "],";		//����
						tree += space + "['"+item[bi][1]+"','"+link+"'],";		//������
					} else {
						tree += "],";		//����
						for(int m=0; m > di; di++) tree += space + "],"; //�շ��� ����
						tree += space + "['"+item[bi][1]+"','"+link+"'],";		//������
					}
					
					//������ ���� �ݱ�
					di = cu - 0;
					for(int e=di; e > 0; e--) {
						space = " ";
						for(int es=1; es < e; es++) space += "   ";
						tree += space + "],"; //����
					}
				} else {
					cu = Integer.parseInt(item[bi][0]);
					di = cu - st;
					
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						tree += ",";		//����
						tree += space + "['"+item[bi][1]+"','"+link+"'";		//������
					} else if(di == 0) {
						tree += "],";		//����
						tree += space + "['"+item[bi][1]+"','"+link+"'";		//������
					} else {
						tree += "],";		//����
						for(int m=0; m > di; di++) tree += space + "],"; //�շ��� ����
						tree += space + "['"+item[bi][1]+"','"+link+"'";		//������
					}
					st = Integer.parseInt(item[bi][0]);
				} //if	
			} //for
			tree += "];";	//�� ������
		} //if 
		return tree;
	} 

	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ�
	 * ������ TREE ����ü�踦 �迭�� ���
	 **********************************************************************/
	public void saveRevStrArray(String child_code,String sel_date) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getReverseBomItems(child_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][7];
		for(int i=0; i<cnt; i++) for(int j=0; j<7; j++) item[i][j]="0";

		//�迭�� ���
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
	 * MBOM_STR���� BOM TREE������ ���ϱ�
	 * ������ TEXT ����ü�踦 ��������� �迭�� �ٽ� ��� : ���ڰ��赵 �ٲ۴�.
	 **********************************************************************/
	public void makeRevTextArray(String child_code,String sel_date) throws Exception
	{
		//�迭�� ���
		saveRevStrArray(child_code,sel_date);

		//TREE������ ��������� �迭�� �ٽø����
		String[][] data = new String[an][7];
		for(int i=1; i<an; i++) for(int j=0; j<7; j++) data[i][j]="";
		////System.out.println(data[0][0]+":"+data[0][1]);

		int n = 1, k = 0;
		for(int i=0; i<an; i++) {
			if(item[i][2].equals("0")) { 
				n = 1;
			} else {
				data[k][0] = Integer.toString(n);	//Level No
				data[k][1] = item[i][1];			//Parent Code (��:���ڵ� -> ���ڵ��)
				data[k][2] = item[i][0];			//Child Code  (��:���ڵ� -> ���ڵ��)
				data[k][3] = item[i+1][3];			//Part Name
				data[k][4] = item[i+1][4];			//Part Spec
				data[k][5] = item[i+1][5];			//Location
				data[k][6] = item[i+1][6];			//OP Code

				////System.out.println(data[k][0]+":"+data[k][1]+":"+data[k][2]+":"+data[k][3]+":"+data[k][4]+":"+data[k][5]+":"+data[k][6]);
				n++;
				k++;
			} //if
		} //for

		//item�迭�� �ٽ� ���
		item = new String[k][7];
		an = k;
		for(int i=0; i<k; i++) {
			for(int j=0; j<7; j++) item[i][j] = data[i][j];		
//			//System.out.println(item[i][0]+":"+item[i][1]+":"+item[i][2]);
		}
	}

	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ�
	 * ������ TREE ����ü�踦 ��������� �迭�� �ٽ� ���
	 **********************************************************************/
	public void makeRevStrArray(String child_code,String sel_date) throws Exception
	{
		//�迭�� ���
		saveRevStrArray(child_code,sel_date);
		if(an == 0) return;

		//TREE������ ��������� �迭�� �ٽø����
		String[][] data = new String[an][2];
		data[0][0] = "0"; data[0][1] = child_code; 	//0������ child code�� �Ѵ�.
		for(int i=1; i<an; i++) data[i][0]=data[i][1]="";
		////System.out.println(data[0][0]+":"+data[0][1]);

		int n = 1, k = 1;
		for(int i=0; i<an; i++) {
			if(item[i][2].equals("0")) { 
				n = 1;
			} else {
				data[k][0] = Integer.toString(n);	//Level No
				data[k][1] = item[i][0];			//Parent Code
				//if(item[i][2].equals("1")) data[k][1] = "M:"+item[i][0];	//���ڵ����� ǥ��
				////System.out.println(data[k][0]+":"+data[k][1]);
				n++;
				k++;
			} //if
		} //for

		//item�迭�� �ٽ� ���
		item = new String[k][2];
		an = k;
		for(int i=0; i<k; i++) {
			item[i][0] = data[i][0];				//Level No
			item[i][1] = data[i][1];				//Parent Code
			////System.out.println(item[i][0]+":"+item[i][1]);
		}
	}

	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ�
	 * ��/������ TREE ����ü�踦 ��������� �迭����
	 **********************************************************************/
	public String getArrayCount() 
	{
		return Integer.toString(an);
	}

	//--------------------------------------------------------------------
	//
	//		BOM�� �̿��Ͽ� ��ǰ���ڵ���� Unique�� �������� �����ϱ�
	//		[���� �����Ҷ� Ȱ��]
	//		* ���� ��ǰ���ڵ�� unique�� ���븸 �����(location no������� ����)
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ�  [���������Ҷ� Ȱ��]
	 * ������ TREE ����ü�踦 �迭�� ��� : �Ϻα��� ��ü
	 **********************************************************************/
	public ArrayList getUniqueMultiLevelBom(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardBomItems(gid,level_no,parent_code,sel_date);
		int cnt = item_list.size();
		if(cnt == 0) return item_list;

		String[][] data = new String[cnt][11];
		for(int i=0; i<cnt; i++) for(int j=0; j<11; j++) data[i][j]="";

		//�迭�� ���
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
			data[n][10] = "";					//������ ��ǰ������ �Է��ϱ� ����
			////System.out.println(data[n][3]+":"+data[n][1]+":"+data[n][2]);
			n++;
		}
		//Unique�� ��ǰ���ڵ�� ���� ����ϱ�
		sort.bubbleUniqueSortStringMultiAsc(data,2);
		
		//���Ϻ�ǰ���� �����Կ� ���� ��迭 ���ְ� �ܰ��Է��ϱ�[�迭�߰�]
		//10:����,11:ǥ�شܰ�,12:��մܰ�,13:����ܰ�,14:ǥ���Ѿ�,15:����Ѿ�,16:�����Ѿ�
		String where = "";
		String t[][] = new String[n][17];
		an = 0;
		for(int i=0; i<n; i++) {
			if(data[i][0].length() != 0) {
				for(int j=0; j<11; j++) t[i][j] = data[i][j];
				
				where = "where item_code = '"+t[i][2]+"'";
				t[i][11] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_s",where);//ǥ�شܰ�
				t[i][12] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_a",where);//��մܰ�
				t[i][13] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_c",where);//����ܰ�
				
				t[i][14] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][11]));
				t[i][15] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][12]));
				t[i][16] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][13]));
				
				an++;
			}
		}

		//Array List�� ���
		ArrayList price_list = new ArrayList();
		primeCostTable price = null;
		for(int i=0; i<an; i++) {
			price = new primeCostTable();	
			price.setItemCode(t[i][2]);		//ǰ���ڵ�
			price.setItemName(t[i][4]);		//ǰ���̸�
			price.setItemDesc(t[i][5]);		//ǰ��԰�
			price.setItemCount(t[i][10]);	//ǰ�����
			price.setStdPrice(t[i][11]);	//ǥ�شܰ�
			price.setAvePrice(t[i][12]);	//��մܰ�
			price.setCurPrice(t[i][13]);	//����ܰ�
			price.setStdSum(t[i][14]);		//ǥ�شܰ� �Ѿ�
			price.setAveSum(t[i][15]);		//��մܰ� �Ѿ�
			price.setCurSum(t[i][16]);		//����ܰ� �Ѿ�

			price_list.add(price);
		}

/*		//����غ���
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
	 * MBOM_STR���� BOM TREE������ ���ϱ� [���������Ҷ� Ȱ��]
	 * ������ TREE ����ü�踦 �迭�� ��� : �ش�Assy
	 **********************************************************************/
	public ArrayList getUniqueSingleLevelBom(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardSingleBomItems(gid,level_no,parent_code,sel_date);
		int cnt = item_list.size();
		if(cnt == 0) return item_list;

		String[][] data = new String[cnt][11];
		for(int i=0; i<cnt; i++) for(int j=0; j<11; j++) data[i][j]="";

		//�迭�� ���
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
			data[n][10] = "";					//������ ��ǰ������ �Է��ϱ� ����
			////System.out.println(data[n][3]+":"+data[n][1]+":"+data[n][2]);
			n++;
		}
		//Unique�� ��ǰ���ڵ�� ���� ����ϱ�
		sort.bubbleUniqueSortStringMultiAsc(data,2);
		
		//���Ϻ�ǰ���� �����Կ� ���� ��迭 ���ְ� �ܰ��Է��ϱ�[�迭�߰�]
		//10:����,11:ǥ�شܰ�,12:��մܰ�,13:����ܰ�,14:ǥ���Ѿ�,15:����Ѿ�,16:�����Ѿ�
		String where = "";
		String t[][] = new String[n][17];
		an = 0;
		for(int i=0; i<n; i++) {
			if(data[i][0].length() != 0) {
				for(int j=0; j<11; j++) t[i][j] = data[i][j];
				
				where = "where item_code = '"+t[i][2]+"'";
				t[i][11] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_s",where);//ǥ�شܰ�
				t[i][12] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_a",where);//��մܰ�
				t[i][13] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_c",where);//����ܰ�
				
				t[i][14] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][11]));
				t[i][15] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][12]));
				t[i][16] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][13]));
				
				an++;
			}
		}

		//Array List�� ���
		ArrayList price_list = new ArrayList();
		primeCostTable price = null;
		for(int i=0; i<an; i++) {
			price = new primeCostTable();	
			price.setItemCode(t[i][2]);		//ǰ���ڵ�
			price.setItemName(t[i][4]);		//ǰ���̸�
			price.setItemDesc(t[i][5]);		//ǰ��԰�
			price.setItemCount(t[i][10]);	//ǰ�����
			price.setStdPrice(t[i][11]);	//ǥ�شܰ�
			price.setAvePrice(t[i][12]);	//��մܰ�
			price.setCurPrice(t[i][13]);	//����ܰ�
			price.setStdSum(t[i][14]);		//ǥ�شܰ� �Ѿ�
			price.setAveSum(t[i][15]);		//��մܰ� �Ѿ�
			price.setCurSum(t[i][16]);		//����ܰ� �Ѿ�

			price_list.add(price);
		}

/*		//����غ���
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
