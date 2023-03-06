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
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	
	private String query = "",update="";
	private String[][] item = null;				//TREE������ �迭�� ���
	private int an = 0;							//items�� �迭 ����

	private String[][] plist = null;			//���� ���ϳ����� �迭�� ��� 
	private int elecnt=0;						//���� ������ �� ���δ� ������ ���� 
	private int linecnt=0;						//���� ������ ���ΰ��� 
	private String e_assy="1E";					//ȸ�� ASSY �ڵ� ������

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public BomTemplateBO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
		tmpDAO = new com.anbtech.bm.db.BomTemplateDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		Template ����
	//			
	//			
	//			
	//---------------------------------------------------------------------

	//*******************************************************************
	// Template ������ �̿��Ͽ� Template BOM �Է��ϱ�
	// 1 : ��ǰ���ڵ�
	// 2 : Level No
	// 3 : Template tag ���� : A100,A200,A400, : �ݵ�� �������� , �� �־��
	//*******************************************************************/	
	public String inputTemplateBom(String gid,String parent_code,String level_no,String template_tag) throws Exception
	{
		String data="",input="",where="",op_code="",bom_status="";
		int lvn = 0;

		//��ǰ���ڵ��� �����ڵ� ã��
		where = "where item_no = '"+parent_code+"'";
		op_code = modDAO.getColumData("ITEM_MASTER","op_code",where);

		//1.���� ���� ��ȣ ���ϱ�
		if(level_no.length() != 0) lvn = Integer.parseInt(level_no)+1;
		else {data="Level No�� �����ϴ�."; return data; }

		//2.�˻�: BOM STATUS�� ���Ͽ� Template������ ��ϵǾ����� �Ǵ��Ѵ�.
		where = "where pid = '"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "�̹� ��ϵ� Template BOM�Դϴ�."; return data;
		}

		//template tag�� �̿��� ��,��,����,�԰������� �迭�� ��´�.
		int cnt=0,len=template_tag.length();
		for(int i=0; i<len; i++) if(template_tag.charAt(i) == ',') cnt++;
		String[][] temp = new String[cnt][5];	//��,��,����,spec
		template_tag = template_tag.substring(0,len-1);
		
		StringTokenizer temp_list = new StringTokenizer(template_tag,",");
		int n = 0;
		while(temp_list.hasMoreTokens()) {
			if(n == 0) {
				temp[n][0] = parent_code;
				temp[n][1] = temp_list.nextToken();				//�����ڵ�[ASSY������]
				temp[n][2] = Integer.toString(lvn);
				temp[n][3] = tmpDAO.getOpCodeSpec(temp[n][1]);
				temp[n][4] = op_code;							//��ǰ���ڵ��� �����ڵ�[op_code]
				op_code = temp[n][1];							//�����ڵ�
			} else {
				temp[n][0] = temp[n-1][1];
				temp[n][1] = temp_list.nextToken();				//�����ڵ�
				temp[n][2] = Integer.toString(lvn);
				temp[n][3] = tmpDAO.getOpCodeSpec(temp[n][1]);
				temp[n][4] = op_code;							//��ǰ���ڵ��� �����ڵ�[op_code]
				op_code = temp[n][1];							//�����ڵ�
			}
			////System.out.println(temp[n][0]+" : "+temp[n][1]+" : "+temp[n][2]+" : "+temp[n][3]);
			lvn++;
			n++;
		}

		//Template������ MBOM STR�� ��´�.
		String add_date = anbdt.getDateNoformat();
		for(int i=0; i<cnt; i++) {
			String pid = anbdt.getNumID(i);
			input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
			input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
			input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,item_type) values('";
			input += pid+"','"+gid+"','"+temp[i][0]+"','"+temp[i][1]+"','"+temp[i][2]+"','"+""+"','";
			input += temp[i][3]+"','"+""+"','"+temp[i][4]+"','"+"EA"+"','"+"1"+"','"+""+"','"+""+"','";
			input += "��"+"','"+"0"+"','"+add_date+"','"+""+"','"+""+"','"+""+"','"+"0"+"','"+"0"+"','";
			input += "0"+"','"+"1"+"','"+"1"+"')";
			modDAO.executeUpdate(input);
		}

		//6.Template�������� MBOM MASTER�� ��´�.
		update = "UPDATE MBOM_MASTER set bom_status='2' where pid='"+gid+"'";
		modDAO.executeUpdate(update);

		data = "���������� ��ϵǾ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// MBOM_STR�� Template���� ������ �����ϱ�
	//*******************************************************************/	
	public String deleteTemplateBom(String gid) throws Exception
	{
		String delete = "",data="";
		String where = "where pid='"+gid+"'";
		
		//������� �˻��ϱ�
		String bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("4")) {
			data = "���������� BOM���� ������ �� �����ϴ�.";
		} else if(bom_status.equals("5")) {
			data = "Ȯ���� BOM���� ������ �� �����ϴ�. ���躯�濡�� �����Ͻʽÿ�.";
		} else {
			delete = "DELETE from MBOM_STR where gid='"+gid+"' and tag='1'";
			modDAO.executeUpdate(delete);
			data = "���������� �����Ǿ����ϴ�.";
		}

		//MBOM_MASTER�� BOM���� �����ϱ� 
		query = "SELECT COUNT(*) FROM mbom_str WHERE gid = '"+gid+"'";
		int cnt = modDAO.getTotalCount(query);
		if(cnt == 1) {			//bom_status = '1' : �������� �ۼ���
			update = "UPDATE mbom_master SET bom_status='1' where pid = '"+gid+"'";
			modDAO.executeUpdate(update);
		} else {				//bom_status = '3' : BOM�����
			update = "UPDATE mbom_master SET bom_status='3' where pid = '"+gid+"'";
			modDAO.executeUpdate(update);
		}

		return data;
	}

	/**********************************************************************
	 * Template�� Assy�ڵ�� �ٲ� ����ϱ�
	 **********************************************************************/
	public String changeAssyCode(String gid,String login_id) throws Exception
	{
		String data="",where="",model_code="",bom_status="";

		//���ڵ�,bom_status ������ ã��
		where = "where pid='"+gid+"'";
		model_code = modDAO.getColumData("mbom_master","model_code",where);
		bom_status = modDAO.getColumData("mbom_master","bom_status",where);

		//BOM���� ���� �˻�
		if(!bom_status.equals("2")) {
			data = "�������ø������� ������츸 ����ASSY ��� �����մϴ�.";
			return data;
		}

		//Template ������ �迭�� ���
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
			data = "����� �������ø������� �����ϴ�.";
			return data;
		}

		//----------------------------------------------------
		//ASSY�ڵ�,Spec,tag='2'[Assy�ڵ���]���� �ٲ� ����ϱ�
		//----------------------------------------------------
		String parent_code = item[0][1];						//��ǰ���ڵ�
		String state_name = tmpDAO.getStateName(parent_code);	//����� (ASSY�԰��� ù��° �ʵ尪)

		//item_master�� ����� ����
		String register_info = tmpDAO.getRegInfo(login_id);		//���������(�Ҽ�/����/�̸�)
		String register_date = anbdt.getDate();					//������� (yyyy-mm-dd)
		String one_class="",serial_no="";						//��ǰ���з��ڵ�,Serial No

		//��ó�� ��ǰ���ڵ带 �˻�
		if(parent_code.length() > 9) {
			one_class = parent_code.substring(3,4);				//��ǰ���з��ڵ�
			serial_no = parent_code.substring(4,8);				//Serial No
		} else {
			data = "��ǰ���ڵ尡 ��ǰ�ڵ�ä����Ģ�� ������ϴ�. Ȯ�ιٶ��ϴ�.";
			return data;
		}

		//����Assy�ڵ� update�� item_master�� ����Assy�ڵ� ����ϱ�
		for(int i=0; i<cnt; i++) {
			parent_code = saveAssyCode(item[i][0],gid,parent_code,one_class,serial_no,item[i][2],state_name,login_id,register_info,register_date,model_code,one_class);
		}

		//MBOM_MASTER�� bom_status='3'���� �ٲ��ֱ�
		update = "UPDATE mbom_master SET bom_status='3' where pid = '"+gid+"'";
		modDAO.executeUpdate(update);

		
		data = "���������� ��ϵǾ����ϴ�.";
		return data="";
	}

	/**********************************************************************
	 * Template�� Assy�ڵ� �ش�DB�� �����ϱ�
	 * serial_no:ä����Ģ���� �ø����ȣ,op_code:�����ڵ�,state_name:�����
	 * register_id,register_info,register_date : ���,�Ҽ�/����/�̸�,�Է���
	 * model_code:���ڵ�,pdg_kind_code:��ǰ���з��ڵ�
	 **********************************************************************/
	public String saveAssyCode(String pid,String gid,String parent_code,String one_class,String serial_no,String op_code,
						String state_name,String register_id,String register_info,String register_date,
						String model_code,String pdg_kind_code) throws Exception
	{
		String item_no="",item_name="PCB ASSY",stock_unit="EA",item_type="1",where="",input="",update="";
		//String spec=state_name+",";			//�԰�
		String spec="";			//�԰�
		String[] data = new String[6];

		//�ش�ASSY�ڵ� �����Ͽ� �����
		item_no = "1EP"+one_class+serial_no+op_code;							//��ȯ�� ����ASSY�ڵ�

		//�����ڵ忡 �ش�Ǵ� �԰����� ������ �ش�����ڵ��� �԰ݸ����
		where = "where m_code='"+op_code+"'";
		spec += modDAO.getColumData("mbom_env","spec",where);	
		
		//�ش�Assy�ڵ尡 �̹� ��ǰ�����Ϳ��� ä���Ǿ����� �˾ƺ���
		//[0��ǰ�̸�,1�԰�,2����Ŀ��,3����,4��������,5item_type] 
		data = modDAO.getComponentInfo(item_no);
		if(data[0].length() != 0) {
			item_name = data[0];		
			spec = data[1];
			stock_unit = data[4];
			item_type=data[5];
		}

		//MBOM_STR�� �ش����� UPDATE�ϱ�
		update = "UPDATE mbom_str SET parent_code='"+parent_code+"',child_code='"+item_no+"',"; 
		//update += "part_spec='"+spec+"',op_code='"+op_code+"',tag='2' ";
		update += "part_spec='"+spec+"',tag='2' ";
		update += "WHERE pid='"+pid+"' and gid='"+gid+"'";
		modDAO.executeUpdate(update);

		//ITEM_MASTER�� �ش����� �Է��ϱ�
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
	 * ����TEMPLATE������ ���� BOM LIST ��, ȸ��ASSY�϶��� ��ũ�ޱ�
	 * gid : group�����ڵ�, level_no : 0, parent_code : model_code
	 **********************************************************************/
	public ArrayList getStrListEleLink(String gid,String level_no,String parent_code) throws Exception
	{
		String pcode="",ccode="",c_head="";
		// �迭�� ��´�.
		saveBomArray(gid,level_no,parent_code);

		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		for(int i=0; i<an; i++) {
			table = new mbomStrTable();
			table.setPid(item[i][0]);

			pcode = item[i][1];						//parent_code
			ccode = item[i][2];						//child_code
			c_head = ccode.substring(0,2);			//��ǰ���ڵ��� ���ڸ�
			if(!item[i][3].equals("0") & c_head.equals(e_assy))	//ù��°���� ����[level no : 0]
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
	 * ��ϵ� BOM������ �迭�� ���
	 * ��ϵ� ���� ��º���
	 **********************************************************************/
	public void saveBomArray(String gid,String level_no,String parent_code) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getForwardItems(gid,level_no,parent_code);
		int cnt = item_list.size();
		item = new String[cnt][10];
		for(int i=0; i<cnt; i++) for(int j=0; j<10; j++) item[i][j]="";

		//�迭�� ���
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