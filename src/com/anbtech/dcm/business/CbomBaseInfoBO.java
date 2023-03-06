package com.anbtech.dcm.business;
import com.anbtech.dcm.entity.*;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class CbomBaseInfoBO
{
	private Connection con;
	private com.anbtech.dcm.db.CbomModifyDAO cmodDAO = null;						//ECC����
	private com.anbtech.bm.db.BomShowDAO showDAO = null;							//BOM����
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	
	private String query = "";
	private String[][] item = null;				//TREE������ �迭�� ���
	private int an = 0;							//items�� �迭 ����

	private String[][] plist = null;			//���� ���ϳ����� �迭�� ��� 
	private int elecnt=0;						//���� ������ �� ���δ� ������ ���� 
	private int linecnt=0;						//���� ������ ���ΰ��� 

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public CbomBaseInfoBO(Connection con) 
	{
		this.con = con;
		cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);
		showDAO = new com.anbtech.bm.db.BomShowDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		ECR �� ���� �޼ҵ� ���� : �⺻����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ECC_COM:ECR�� ������ �Է��ϱ�
	//	#default�� ECC_REQ���� �Է��Ѵ�.
	//*******************************************************************/	
	public String insertECR(String pid,String ecc_subject,String ecr_id,String ecr_name,String ecr_code,
		String ecr_div_code,String ecr_div_name,String ecr_tel,String ecr_date,String mgr_id,
		String ecc_kind,String pdg_code,String pd_code,String fg_code,String part_code,
		String chg_position,String trouble,String condition,String solution) throws Exception
	{
		String input="",data="";

		//ECO NO �ڵ�ä�� ���ϱ�
		String eco_no = getEcoNo("ECR");			//ECO NO �ڵ�ä��

		//å���� ���� ���ϱ� [�迭: [0]���,[1]�̸�,[2]�μ������ڵ�,[3]�μ��ڵ�,[4]�μ���,[5]��ȭ��ȣ]
		String mgr[] = new String[6];
		mgr = cmodDAO.getUserData(mgr_id);

		//ECC_COM�� �Է��ϱ�
		input = "INSERT INTO ecc_com (pid,ecc_subject,eco_no,ecr_id,ecr_name,ecr_code,ecr_div_code,ecr_div_name,";
		input += "ecr_tel,ecr_date,mgr_id,mgr_name,mgr_code,mgr_div_code,mgr_div_name,eco_id,eco_name,";
		input += "eco_code,eco_div_code,eco_div_name,eco_tel,ecc_reason,ecc_factor,ecc_scope,ecc_kind,";
		input += "pdg_code,pd_code,fg_code,part_code,order_date,fix_date,ecc_status) values('";
		input += pid+"','"+ecc_subject+"','"+eco_no+"','"+ecr_id+"','"+ecr_name+"','"+ecr_code+"','";
		input += ecr_div_code+"','"+ecr_div_name+"','"+ecr_tel+"','"+ecr_date+"','"+mgr_id+"','";
		input += mgr[1]+"','"+mgr[2]+"','"+mgr[3]+"','"+mgr[4]+"','"+""+"','";
		input += ""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+ecc_kind+"','";
		input += pdg_code+"','"+pd_code+"','"+fg_code+"','"+part_code+"','"+""+"','"+""+"','"+"1"+"')";
		cmodDAO.executeUpdate(input);

		//ECC_REQ �Է��ϱ�
		input = "INSERT INTO ecc_req (pid,chg_position,trouble,condition,solution,";
		input += "fname,sname,ftype,fsize,app_no) values('";
		input += pid+"','"+chg_position+"','"+trouble+"','"+condition+"','"+solution+"','";
		input += ""+"','"+""+"','"+""+"','"+""+"','"+""+"')";
		cmodDAO.executeUpdate(input);
		
		data = "���������� ECR��ϵǾ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// ECC_COM:ECR�� ������ �����ϱ�
	//	#default�� ECC_REQ���� �����Ѵ�.
	//*******************************************************************/	
	public String updateECR(String pid,String ecc_subject,String ecr_id,String ecr_name,String ecr_code,
		String ecr_div_code,String ecr_div_name,String ecr_tel,String ecr_date,String mgr_id,
		String ecc_kind,String pdg_code,String pd_code,String fg_code,String part_code,
		String chg_position,String trouble,String condition,String solution) throws Exception
	{
		String update="",data="";

		//���������� ���¸� �˻��Ѵ�. �ʱ��ϻ���[1]�� �ݷ�[0]�� �ٽ��ۼ��ø� ������
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		if(ecc_status.equals("0")) {			//�ݷ��� �ٽ� �ۼ��� ���¸� �ۼ����� �ٲ��ش�.
			update = "UPDATE ecc_com SET ecc_status='1' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}
		else if(!ecc_status.equals("1")) {
			data = "�ۼ������϶��� ���� �� �� �ֽ��ϴ�.";
			return data;
		}

		//å���� ���� ���ϱ� [�迭: [0]���,[1]�̸�,[2]�μ������ڵ�,[3]�μ��ڵ�,[4]�μ���,[5]��ȭ��ȣ]
		String mgr[] = new String[6];
		mgr = cmodDAO.getUserData(mgr_id);
		
		//ECC_COM�� �����ϱ�
		update = "UPDATE ecc_com SET ecc_subject='"+ecc_subject+"',ecr_id='"+ecr_id;
		update += "',ecr_name='"+ecr_name+"',ecr_code='"+ecr_code+"',ecr_div_code='"+ecr_div_code;
		update += "',ecr_div_name='"+ecr_div_name+"',ecr_tel='"+ecr_tel+"',ecr_date='"+ecr_date;
		update += "',mgr_id='"+mgr_id+"',mgr_name='"+mgr[1]+"',mgr_code='"+mgr[2];
		update += "',mgr_div_code='"+mgr[3]+"',mgr_div_name='"+mgr[4]+"',ecc_kind='"+ecc_kind;
		update += "',pdg_code='"+pdg_code+"',pd_code='"+pd_code+"',fg_code='"+fg_code;
		update += "',part_code='"+part_code+"' where pid='"+pid+"'";
		cmodDAO.executeUpdate(update);

		//ECC_REQ�� �����ϱ�
		update = "UPDATE ecc_req SET chg_position='"+chg_position+"',trouble='"+trouble;
		update += "',condition='"+condition+"',solution='"+solution;
		update += "' where pid='"+pid+"'";
		cmodDAO.executeUpdate(update);

		data = "���������� ECR�����Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// ECC_COM:REQ�� ������ �����ϱ�
	//	#default�� ECC_REQ���� �����Ѵ�.
	//*******************************************************************/	
	public String deleteECR(String pid,String filepath) throws Exception
	{
		String delete = "",data="";

		//���������� ���¸� �˻��Ѵ�. �ʱ��ϻ��¸� ������
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		if(!ecc_status.equals("1")) {
			data = "�ۼ������϶��� ���� �� �� �ֽ��ϴ�.";
			return data;
		}

		//÷������ �����ϱ�
		com.anbtech.dcm.entity.eccReqTable ecrT = new com.anbtech.dcm.entity.eccReqTable();
		ecrT = cmodDAO.readEccReq(pid);
		String sname = ecrT.getSname();								//÷������ �����
		int cnt = 0;
		for(int i=0; i<sname.length(); i++) if(sname.charAt(i) == '|') cnt++;
		for(int i=0; i<cnt; i++) {
			StringTokenizer o = new StringTokenizer(sname,"|");		
			while(o.hasMoreTokens()) {
				String redfile = o.nextToken();
				String delfile = filepath+"/"+redfile.trim()+".bin";
				File FN = new File(delfile);
				if(FN.exists()) FN.delete();
			}
		}
		
		//ECC_COM �����ϱ�
		delete = "DELETE from ECC_COM where pid='"+pid+"' ";
		cmodDAO.executeUpdate(delete);

		//ECC_REQ �����ϱ�
		delete = "DELETE from ECC_REQ where pid='"+pid+"' ";
		cmodDAO.executeUpdate(delete);

		data = "���������� ECR�����Ǿ����ϴ�.";
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		ECO �� ���� �޼ҵ� ���� : �⺻����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ECC_COM:ECR�� ������ �Է��ϱ�  
	//	#default�� ECC_ORD���� �Է��Ѵ�.
	//*******************************************************************/	
	public String insertECO(String pid,String ecc_subject,String ecr_id,String ecr_name,String ecr_code,
		String ecr_div_code,String ecr_div_name,String ecr_tel,String ecr_date,String eco_id,String eco_tel,
		String ecc_reason,String ecc_factor,String ecc_scope,String ecc_kind,String pdg_code,String pd_code,
		String fg_code,String part_code,String order_date,String chg_position,String trouble,
		String condition,String solution) throws Exception
	{
		String input="",update="",data="";
		String eco_no = getEcoNo("ECO");

		//ECR ���� ���۵� ECO���� ������ ���� ECO������ �Ǵ��Ͽ� ����Ѵ�.
		String mgr_id = cmodDAO.getColumData("ECC_COM","mgr_id","where pid ='"+pid+"'");

		//����� ���� ���ϱ� [�迭: [0]���,[1]�̸�,[2]�μ������ڵ�,[3]�μ��ڵ�,[4]�μ���,[5]��ȭ��ȣ]
		String eco[] = new String[6];
		eco= cmodDAO.getUserData(eco_id);
		
		//ECR���� ���ǵ� ��� : ECC_COM�� UPDATE�Ѵ�.
		if(mgr_id.length() !=0) {
			update = "UPDATE ecc_com set ecc_reason='"+ecc_reason+"',ecc_factor='"+ecc_factor;
			update += "',ecc_scope='"+ecc_scope+"',order_date='"+order_date;
			update += "' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}
		//ECO���� ���ǵ� ��� : ECC_COM�� INSERT�Ѵ�.
		else {
			input = "INSERT INTO ecc_com (pid,ecc_subject,eco_no,ecr_id,ecr_name,ecr_code,ecr_div_code,ecr_div_name,";
			input += "ecr_tel,ecr_date,mgr_id,mgr_name,mgr_code,mgr_div_code,mgr_div_name,eco_id,eco_name,";
			input += "eco_code,eco_div_code,eco_div_name,eco_tel,ecc_reason,ecc_factor,ecc_scope,ecc_kind,";
			input += "pdg_code,pd_code,fg_code,part_code,order_date,fix_date,ecc_status) values('";
			input += pid+"','"+ecc_subject+"','"+eco_no+"','"+ecr_id+"','"+ecr_name+"','"+ecr_code+"','";
			input += ecr_div_code+"','"+ecr_div_name+"','"+ecr_tel+"','"+ecr_date+"','"+""+"','";
			input += ""+"','"+""+"','"+""+"','"+""+"','"+eco_id+"','"+eco[1]+"','"+eco[2]+"','";
			input += eco[3]+"','"+eco[4]+"','"+eco_tel+"','"+ecc_reason+"','"+ecc_factor+"','";
			input += ecc_scope+"','"+ecc_kind+"','"+pdg_code+"','"+pd_code+"','"+fg_code+"','"+part_code+"','";
			input += order_date+"','"+""+"','"+"6"+"')";
			cmodDAO.executeUpdate(input);
		}

		//ECC_ORD �Է��ϱ� : ����
		input = "INSERT INTO ecc_ord (pid,chg_position,trouble,condition,solution,";
		input += "fname,sname,ftype,fsize,app_no) values('";
		input += pid+"','"+chg_position+"','"+trouble+"','"+condition+"','"+solution+"','";
		input += ""+"','"+""+"','"+""+"','"+""+"','"+""+"')";
		cmodDAO.executeUpdate(input);
		
		data = "���������� ECO��ϵǾ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// ECC_COM:ECO�� ������ �����ϱ�
	//	#default�� ECC_ORD���� �����Ѵ�.
	//*******************************************************************/	
	public String updateECO(String pid,String ecc_subject,String ecr_id,String ecr_name,String ecr_code,
		String ecr_div_code,String ecr_div_name,String ecr_tel,String ecr_date,String eco_id,String eco_tel,
		String ecc_reason,String ecc_factor,String ecc_scope,String ecc_kind,String pdg_code,String pd_code,
		String fg_code,String part_code,String order_date,String chg_position,String trouble,
		String condition,String solution) throws Exception
	{
		String update="",data="",input="";

		//----------------------------------------
		//  ���/���� ���� ���� �˻��ϱ�
		//----------------------------------------
		//���������� ���¸� �˻��Ѵ�. �ʱ��ϻ���[6]�� �ݷ�[5]���½�,������������[4]�ø� ������
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		//������� ����� ������ �ۼ�����
		if(ecc_status.equals("4")) {				
			update = "UPDATE ecc_com SET ecc_status='6' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}
		//�ݷ������϶�
		else if(ecc_status.equals("5")) {				
			update = "UPDATE ecc_com SET ecc_status='6' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}
		//�ۼ����°� �ƴϸ� ����
		else if(!ecc_status.equals("6")) {
			data = "�ۼ������϶��� ���� �� �� �ֽ��ϴ�.";
			return data;
		}

		//----------------------------------------
		//  ���/������ �η����� ���ϱ�
		//----------------------------------------
		//����� ���� ���ϱ� [�迭: [0]���,[1]�̸�,[2]�μ������ڵ�,[3]�μ��ڵ�,[4]�μ���,[5]��ȭ��ȣ]
		String eco[] = new String[6];
		eco= cmodDAO.getUserData(eco_id);

		//ECR ���� ���۵� ECO���� ������ ���� ECO������ �Ǵ��Ͽ� ����Ѵ�.
		String mgr_id = cmodDAO.getColumData("ECC_COM","mgr_id","where pid ='"+pid+"'");
		
		//----------------------------------------
		//  ECC_COM�� �����ϱ�
		//----------------------------------------
		//ECR���� ���ǵ� ��� : ECC_COM�� UPDATE�Ѵ�.
		if(mgr_id.length() !=0) {
			update = "UPDATE ecc_com SET ecc_reason='"+ecc_reason+"',ecc_factor='"+ecc_factor;
			update += "',ecc_scope='"+ecc_scope+"',fg_code='"+fg_code+"',part_code='"+part_code;
			update += "',order_date='"+order_date+"' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}
		//ECO���� ���ǵ� ��� : ECC_COM�� INSERT�Ѵ�.
		else {
			update = "UPDATE ecc_com SET ecc_subject='"+ecc_subject+"',ecr_id='"+ecr_id;
			update += "',ecr_name='"+ecr_name+"',ecr_code='"+ecr_code+"',ecr_div_code='"+ecr_div_code;
			update += "',ecr_div_name='"+ecr_div_name+"',ecr_tel='"+ecr_tel+"',ecr_date='"+ecr_date;
			update += "',eco_id='"+eco_id+"',eco_name='"+eco[1]+"',eco_code='"+eco[2];
			update += "',eco_div_code='"+eco[3]+"',eco_div_name='"+eco[4]+"',eco_tel='"+eco_tel;
			update += "',ecc_reason='"+ecc_reason+"',ecc_factor='"+ecc_factor+"',ecc_scope='"+ecc_scope;
			update += "',ecc_kind='"+ecc_kind+"',pdg_code='"+pdg_code+"',pd_code='"+pd_code;
			update += "',fg_code='"+fg_code+"',part_code='"+part_code;
			update += "',order_date='"+order_date+"' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}

		//----------------------------------------
		//  ECC_ORD�� �Է�/�����ϱ�
		//----------------------------------------
		if(ecc_status.equals("4")) {	//������� ����ڷμ� �����Է½�
			//ECC_ORD �Է��ϱ� : ����
			input = "INSERT INTO ecc_ord (pid,chg_position,trouble,condition,solution,";
			input += "fname,sname,ftype,fsize,app_no) values('";
			input += pid+"','"+chg_position+"','"+trouble+"','"+condition+"','"+solution+"','";
			input += ""+"','"+""+"','"+""+"','"+""+"','"+""+"')";
			cmodDAO.executeUpdate(input);
		} else {						//�ۼ��ڷμ� ������
			//ECC_ORD�� �����ϱ�
			update = "UPDATE ecc_ord SET chg_position='"+chg_position+"',trouble='"+trouble;
			update += "',condition='"+condition+"',solution='"+solution;
			update += "' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}

		data = "���������� ECO�����Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// ECC_COM:REQ�� ������ �����ϱ�
	//	#default�� ECC_REQ���� �����Ѵ�.
	//*******************************************************************/	
	public String deleteECO(String pid,String filepath) throws Exception
	{
		String delete = "",data="";

		//���������� ���¸� �˻��Ѵ�. �ʱ��ϻ��¸� ������
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		if(!ecc_status.equals("6")) {
			data = "�ۼ������϶��� ���� �� �� �ֽ��ϴ�.";
			return data;
		}

		//ECR ���� ���۵� ECO�̸� ������ �� ����
		String mgr_id = cmodDAO.getColumData("ECC_COM","mgr_id","where pid ='"+pid+"'");
		if(mgr_id.length() !=0) {
			data = "ECR���Ǻ��� ���۵� ECO�� ���� �� �� �ֽ��ϴ�.";
			return data;
		}

		//÷������ �����ϱ�
		com.anbtech.dcm.entity.eccOrdTable ecoT = new com.anbtech.dcm.entity.eccOrdTable();
		ecoT = cmodDAO.readEccOrd(pid);
		String sname = ecoT.getSname();								//÷������ �����
		int cnt = 0;
		for(int i=0; i<sname.length(); i++) if(sname.charAt(i) == '|') cnt++;
		for(int i=0; i<cnt; i++) {
			StringTokenizer o = new StringTokenizer(sname,"|");		
			while(o.hasMoreTokens()) {
				String redfile = o.nextToken();
				String delfile = filepath+"/"+redfile.trim()+".bin";
				File FN = new File(delfile);
				if(FN.exists()) FN.delete();
			}
		}
		
		//ECC_COM �����ϱ�
		delete = "DELETE from ECC_COM where pid='"+pid+"' ";
		cmodDAO.executeUpdate(delete);

		//ECC_REQ �����ϱ�
		delete = "DELETE from ECC_REQ where pid='"+pid+"' ";
		cmodDAO.executeUpdate(delete);

		data = "���������� ECO�����Ǿ����ϴ�.";
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		÷�� ���� �����ϱ� 
	//
	//
	//---------------------------------------------------------------------
	/*******************************************************************
	* ÷������ �����ϱ� (�űԷ� ó�� ÷���Ҷ�)
	 *******************************************************************/
	 public int setAddFile(MultipartRequest multi,String tablename,String pid,String save_id,String filepath) throws Exception
	{
		String filename = "";		//�����̸� ���ϸ�
		String savename = "";		//���� ���ϸ�
		String filetype = "";		//�����̸� ���� Ȯ���ڸ�
		String filesize = "";		//�����̸� ���ϻ�����

		int i = 1;					//÷������ Ȯ����
		int atcnt = 0;				//÷������ ����
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();				//�ش����� �б�
			String name = "attachfile"+i;		//upload�� input file type name parameter
			String fname = multi.getFilesystemName(name);	//upload�� ���ϸ�
			if(fname != null) {
				String ftype = multi.getContentType(name);	//upload�� ����type
				//file size���ϱ�
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,save_id+"_"+i+".bin");
				upFile.renameTo(myFile);					//�����̸� �ٲٱ�

				filename += fname + "|";
				savename += save_id + "_" + i + "|";
				filetype += ftype + "|";
				filesize += fsize + "|";
				atcnt++;
			}
			i++;
		}//while

		//Table�� �����ϱ�
		if(i > 1) {
			setAddFileUpdate(tablename,pid,filename,savename,filetype,filesize);
		}
		return atcnt;
	}
	
	/*******************************************************************
	* ÷������ �����ϱ� (�ӽ������� �����Ͽ� ÷���Ҷ�)
	* save_head : ������ ������ ���ι���
	* delfile �� ������ ������
	 *******************************************************************/
	 public int setUpdateFile(MultipartRequest multi,String tablename,String pid,String save_head,String filepath,
		 String fname,String sname,String ftype,String fsize,String attache_cnt,String[] chkDelFile) throws Exception
	{
		String save_id = save_head+anbdt.getID();		//�űԷ� ������ ���ϸ�
		String filename = "";		//�����̸� ���ϸ�
		String savename = "";		//���� ���ϸ�
		String filetype = "";		//�����̸� ���� Ȯ���ڸ�
		String filesize = "";		//�����̸� ���ϻ�����
		int att_cnt = Integer.parseInt(attache_cnt);	//÷������ �ִ���� �̸�
		String newdata = "";
		
		//------------------------------
		//���������� check�� ���� �����ϱ�
		//------------------------------
		int cnt = chkDelFile.length;
		for(int i=0; i<cnt; i++) {
			String dfile = filepath+"/"+chkDelFile[i];
			File FN = new File(dfile);
			if(FN.exists()) FN.delete();	
		}

		//------------------------------
		//������ ���������� �迭�� ���
		//------------------------------
		String[][] fdata = null;
		int flen = fname.length();
		int alen = 0,hi = 0;			//������ϵ� ���ϼ�
		if(flen != 0) {
			for(int i=0; i<flen; i++) if(fname.charAt(i) == '|') alen++;
			fdata = new String[alen][4];

			//�������� �������ϸ�
			fname = fname.substring(0,fname.length());	//������ '|'����
			java.util.StringTokenizer o_fname = new StringTokenizer(fname,"|");			
			hi = 0;
			while(o_fname.hasMoreTokens()) {
				String read = o_fname.nextToken();
				if(read.length() != 0) fdata[hi][0] = read;
				hi++;
			}
			//�������� �������ϸ�
			sname = sname.substring(0,sname.length());	//������ '|'����
			java.util.StringTokenizer o_sname = new StringTokenizer(sname,"|");			
			hi = 0;
			while(o_sname.hasMoreTokens()) {
				String read = o_sname.nextToken();
				if(read.length() != 0) fdata[hi][1] = read;
				hi++;
			}
			//�������� ��������Type��
			ftype = ftype.substring(0,ftype.length());	//������ '|'����
			java.util.StringTokenizer o_ftype = new StringTokenizer(ftype,"|");			
			hi = 0;
			while(o_ftype.hasMoreTokens()) {
				String read = o_ftype.nextToken();
				if(read.length() != 0) fdata[hi][2] = read;
				hi++;
			}
			//�������� �������� Size
			fsize = fsize.substring(0,fsize.length());	//������ '|'����
			java.util.StringTokenizer o_fsize = new StringTokenizer(fsize,"|");			
			hi = 0;
			while(o_fsize.hasMoreTokens()) {
				String read = o_fsize.nextToken();
				if(read.length() != 0) fdata[hi][3] = read;
				hi++;
			}

			//fdata�迭�� ������ ������ ������ �ش�迭�� ���� clear��Ų��.
			for(int i=0; i<cnt; i++) {
				if(chkDelFile[i].length() != 0) fdata[i][0]=fdata[i][1]=fdata[i][2]=fdata[i][3]="";
			}
		}

		//------------------------------
		//�űԷ� ÷���� ����
		//------------------------------
		int i = 1;		//÷������Ȯ����
		int n = 0;		//����迭�� ����
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();							//�ش����� �б�
			String name = "attachfile"+i;					//upload�� input file type name parameter
			String uname = multi.getFilesystemName(name);	//upload�� ���ϸ�
			
			//��������� ������ ������ �����ϱ�
			if((alen > n) && (uname != null)) {
				String delfile = filepath+"/"+fdata[n][1]+".bin";
				File FN = new File(delfile);
				if(FN.exists()) FN.delete();
			}

			//÷���� ���� �̸��ٲ� �����ϱ�
			if(uname != null) {
				String utype = multi.getContentType(name);	//upload�� ����type
				//file size���ϱ�
				File upFile = multi.getFile(name);
				String usize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,save_id+"_"+i+".bin");
				upFile.renameTo(myFile);					//�����̸� �ٲٱ�

				newdata += uname + "|";
				newdata += save_id + "_" + i + "|";
				newdata += utype + "|";
				newdata += usize + ";";
			}
			else newdata += " | | |;";
			i++;
			n++;
		}//while

		//------------------------------
		//DB�� ������ data �����
		//------------------------------
		//�űԷ� ������ ������ �迭�� ���
		String[][] ndata = new String[att_cnt-1][4];
		for(int a=0; a<n; a++) for(int b=0; b<4; b++) ndata[a][b] = "";

		java.util.StringTokenizer rdata = new StringTokenizer(newdata,";");
		int ai = 0;
		while(rdata.hasMoreTokens()) {
			String nnd = rdata.nextToken();		//1���� �б�
			java.util.StringTokenizer nndata = new StringTokenizer(nnd,"|");
			
			int ni = 0;
			while(nndata.hasMoreTokens()) {
				ndata[ai][ni] = nndata.nextToken();
				ni++;
			}
			ai++;
		}
		

		//������ ������ ������
		int atcnt = 0;			//÷������ ���� ���ϰ�
		for(int p=0; p<att_cnt-1; p++) {
			//����÷���� ���븸 ������
			if((n >= p) && (ndata[p][0].length() > 1)) {				
				filename += ndata[p][0] + "|";
				savename += ndata[p][1] + "|";
				filetype += ndata[p][2] + "|";
				filesize += ndata[p][3] + "|"; 
				atcnt++;
			} 
			//�űԷ� ÷���� ������ �����鼭 ������ϵ� ������ ������
			else if((alen > p) && (ndata[p][0].length() <= 1)) {		
				if(fdata[p][0].length() != 0) {		//������ ������ ����
					filename += fdata[p][0] + "|";
					savename += fdata[p][1] + "|";
					filetype += fdata[p][2] + "|";
					filesize += fdata[p][3] + "|"; 
					atcnt++;
				}
			} 
		}

		//------------------------------
		//Table�� �����ϱ�
		//------------------------------
		setAddFileUpdate(tablename,pid,filename,savename,filetype,filesize);
		
		return atcnt;

	}
	/*******************************************************************
	* ÷������ �������� Table�� update�ϱ�
	 *******************************************************************/
	 private void setAddFileUpdate(String tablename,String pid, String filename, String savename, String filetype, String filesize) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "update "+tablename+" set fname='"+filename+"',sname='"+savename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
			update += " where pid='"+pid+"'";
		int er = stmt.executeUpdate(update);
		
		stmt.close();
	}

	//--------------------------------------------------------------------
	//
	//		���� �޼ҵ� �ۼ�
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ECO NO �ڵ� ä�� �����ϱ�
	//*******************************************************************/	
	public String getEcoNo(String headchar) throws Exception
	{
		String eco_no = "";
		com.anbtech.util.normalFormat nmf = new com.anbtech.util.normalFormat("000");

		//�˻��� String����� : ECO NO : HEADERCHAR + YY + MM + '-' + nnn 
		//HEADERCHAR�� ECO ���� ���۽� : ECO,  ECR���� ���۽� : ECR
		String yy = anbdt.getYear();
		String eco_body = headchar+yy.substring(2,4)+anbdt.getMonth();

		//�̹��޿� ��ϵ� ECO NO�� ���Ѵ�.
		String where = "where eco_no like '"+eco_body+"%' order by eco_no desc";
		String was_eco_no = cmodDAO.getColumData("ECC_COM","eco_no",where);

		//���� eco_no ���� �Ϸù�ȣ�� +1�� �Ѵ�.
		if(was_eco_no.length() == 0) {
			eco_no = eco_body + "-" + "001";
		} else {
			String serial_no = was_eco_no.substring(was_eco_no.length()-3,was_eco_no.length());
			int s_no = Integer.parseInt(serial_no)+1;
			eco_no = eco_body + "-" + nmf.toDigits(s_no);
		}

		return eco_no;
	}

	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ� : �ش��ǰ �˻���
	 *  sel_date : ��ȿ����
	 **********************************************************************/
	public ArrayList viewStrList(String fg_code) throws Exception
	{
		String sel_date = anbdt.getDateNoformat();

		//fg_code�� gid ���ϱ�
		String where = "where fg_code = '"+fg_code+"'";
		String gid = cmodDAO.getColumData("MBOM_MASTER","pid",where);
		String model_code = cmodDAO.getColumData("MBOM_MASTER","model_code",where);

		//BOM LIST ���ϱ�
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardBomItems(gid,"0",model_code,sel_date);

		//BOM LIST�� �迭�� ���
		int cnt = item_list.size();
		item = new String[cnt][5];
		
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			item[an][0] = table.getLevelNo();
			item[an][1] = table.getParentCode();
			item[an][2] = table.getChildCode();
			item[an][3] = table.getLocation();
			item[an][4] = table.getPartSpec();
			an++;
		}

		//��ũ �ޱ�
		ArrayList part_list = new ArrayList();
		for(int i=0; i<cnt; i++) {
			mbomStrTable mst = new mbomStrTable();
			mst.setLevelNo(item[i][0]);
			mst.setParentCode(item[i][1]);
			String child_code = "<a href=\"javascript:itemView('"+item[i][0]+"','";
			child_code += item[i][1]+"','"+item[i][2]+"','"+item[i][3]+"','"+item[i][4];
			child_code += "');\">"+item[i][2]+"</a>";
			mst.setChildCode(child_code);
			mst.setLocation(item[i][3]);
			mst.setPartSpec(item[i][4]);
			part_list.add(mst); 
		}

		return part_list;
	}

}