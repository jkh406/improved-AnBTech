<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "������û�� 1���μ� ������"		
	contentType = "text/html; charset=euc-kr" 
	errorPage	= "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.gw.db.*"
	import="java.sql.Connection"
	import="com.anbtech.gw.entity.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������
	StringProcess str = new com.anbtech.text.StringProcess();				//���ڿ� �ٷ��
	
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con); 
	com.anbtech.gw.entity.TableAppLine app = new com.anbtech.gw.entity.TableAppLine();		//1���϶�
	com.anbtech.gw.entity.TableAppLine app2 = new com.anbtech.gw.entity.TableAppLine();		//2���϶�

	//�����û�� �������
	String query = "";
	String writer_id = "";			//�ۼ��� ���
	String writer_name = "";		//�ۼ��� �̸�
	String c_id = "";				//����������ȣ
	String v_no = "";				//������ȣ
	String v_model = "";			//�����𵨸�
	String in_date = "";			//��û����
	String wyear = "";				//�ۼ���
	String wmonth = "";				//	  ��
	String wdate = "";				//	  ��
	String ac_name = "";			//�ҼӺμ���
	String user_name = "";			//����� ��
	String user_rank = "";			//����� ����
	String fellow_names = "";		//������	 ���/�̸�;
	String f_names = "";			//������	 �̸�,
	String u_year = "";				//������û���� ��
	String u_month = "";			//������û���� ��
	String u_date = "";				//������û���� ��
	String u_time = "";				//������û���� ��
	String tu_year = "";			//������û���� ��
	String tu_month = "";			//������û���� ��
	String tu_date = "";			//������û���� ��
	String tu_time = "";			//������û���� ��
	String purpose = "";			//����
	String cr_dest = "";			//�༱��
	String content = "";			//��������
	String em_tel = "";				//��޿���ó

	//���缱 ����
	String pid = "";				//������ȣ
	String doc_id = "";				//���ù��� ������ȣ
	String line="";					//�������� ���缱
	String r_line = "";				//���ۼ����� �Ѱ��ֱ�
	String vdate = "";				//������ ���� ����
	String ddate = "";				//������ ���� ����
	String wid = "";				//����ڻ��
	String vid = "";				//�����ڻ��
	String did = "";				//�����ڻ��
	String wname = "";				//�����
	String vname = "";				//������
	String dname = "";				//������
	String PROCESS = "";			//PROCESS
	String doc_ste = "";			//doc_ste

	//2���� ���� ����
	String line2="";				//�������� ���缱
	String vdate2 = "";				//������ ���� ����
	String ddate2 = "";				//������ ���� ����
	String wid2 = "";				//����ڻ��
	String vid2 = "";				//�����ڻ��
	String did2 = "";				//�����ڻ��
	String wname2 = "";				//�����
	String vname2 = "";				//������
	String dname2 = "";				//������
	String PROCESS2 = "";			//PROCESS
	String doc_ste2 = "";			//doc_ste

	/*********************************************************************
	 	�����(login) �˾ƺ���
	*********************************************************************/	
	String[] uColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(uColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//����� ���
		writer_name = bean.getData("name");				//����� ��
	} //while

	//*********************************************************************
	// ���缱 ���� �ޱ�
	//*********************************************************************
	pid = request.getParameter("pid");	if(pid == null) pid = "";				//������ȣ
	doc_id = request.getParameter("doc_id");	if(doc_id == null) doc_id = "";	//���ù��� ������ȣ

	//1���� ����������ȣ���� 2���� ����������ȣ ���� �˾ƺ���
	String[] otColumn = {"pid","plid"};
	bean.setTable("app_master");			
	bean.setColumns(otColumn);
	bean.setClear();
	bean.setOrder("pid DESC");	
	bean.setSearch("pid",pid);
	bean.init_unique();

	String one_two = "";
	String plid = "";
	if(bean.isAll()) plid = bean.getData("plid");
	if(plid.equals(pid)) one_two = "one";			//1���� ������ȣ�� ��ũ������ȣ�� ����. (pid == plid)
	else one_two = "two";							//2���� ������ȣ�� ��ũ������ȣ�� �ٸ���.

	PROCESS = request.getParameter("PROCESS");	if(PROCESS == null) PROCESS = "";		//PROCESS��
	doc_ste = request.getParameter("doc_ste");	if(doc_ste == null) doc_ste = "";		//doc_ste

	//���缱 1.2�� ����
	String ag_line="",a_line="",cmt="",t_cmt="",t_line="";		//1��:�������缱,����TextArea,�����ǰ�,TextArea
	int line_cnt = 0;											//1��:���缱�� ��µ� ���� ����
	String ag_line2="",a_line2="",cmt2="",t_cmt2="",t_line2="";	//2��:�������缱,����TextArea,�����ǰ�,TextArea
	int line_cnt2 = 0;											//2��:���缱�� ��µ� ���� ����
	//--------------------------------
	//	1���μ� ���ڰ���� (2���μ� �������)
	//--------------------------------
	if(one_two.equals("one")) {
		//1���� ���缱 ����(���缱 ����)
		//��������(anb) ���� ��������(storehouse)�� ���� ���� : 200408 : ����
		if(PROCESS.equals("DEL_BOX")) masterDAO.getTable_MasterPid(pid,"storehouse.dbo.app_save");
		else masterDAO.getTable_MasterPid(pid);		
		ArrayList app_line = new ArrayList();				
		app_line = masterDAO.getTable_line();		
		Iterator app_iter = app_line.iterator();

		while(app_iter.hasNext()) {
			app = (TableAppLine)app_iter.next();
			
			//���缱
			cmt = app.getApComment(); if(cmt == null) cmt = "";
			t_cmt="";
			if(cmt.length() != 0) { 
				t_cmt = "\r    "+cmt; 
				cmt = "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+cmt; 
				line_cnt++; 
			}

			if(app.getApStatus().equals("���")) {
				wname = app.getApName();	if(wname == null) wname="";		//�����
				wid = app.getApSabun();		if(wid == null) wid="";			//����� ���
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else if(app.getApStatus().equals("����"))  {
				vname = app.getApName();	if(vname == null) vname="";		//������
				vid = app.getApSabun();		if(vid == null) vid="";			//������ ���
				vdate = app.getApDate();	if(vdate == null) vdate="";		//������ ��������(���������,���������ʵ�)
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else if(app.getApStatus().equals("����"))  {
				dname = app.getApName();	if(dname == null) dname="";		//������
				did = app.getApSabun();		if(did == null) did="";			//������ ���
				ddate = app.getApDate();	if(ddate == null) ddate="";		//������ �������� (���������,���������ʵ�)
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else {	//����
				if(PROCESS.equals("APP_BOX")) {
					ag_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

					a_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

					r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

					line_cnt++;
				} else {
					line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

					t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

					r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

					line_cnt++;
				}
			}

		}
		if(ag_line.length() != 0) { line += ag_line;	t_line += a_line; }	

	}

	//--------------------------------
	//	2���μ� ���ڰ���� (1���μ� ��������)
	//--------------------------------
	else {	
		//1���� ���缱 ����(���缱 ����)
		//��������(anb) ���� ��������(storehouse)�� ���� ���� : 200408 : ����
		if(PROCESS.equals("DEL_BOX")) masterDAO.getTable_MasterPid(plid,"storehouse.dbo.app_save");
		else masterDAO.getTable_MasterPid(plid);	
		ArrayList app_line = new ArrayList();				
		app_line = masterDAO.getTable_line();		
		Iterator app_iter = app_line.iterator();

		while(app_iter.hasNext()) {
			app = (TableAppLine)app_iter.next();
			
			//���缱
			cmt = app.getApComment(); if(cmt == null) cmt = "";
			t_cmt="";
			if(cmt.length() != 0) { 
				t_cmt = "\r    "+cmt; 
				cmt = "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+cmt; 
				line_cnt++; 
			}

			if(app.getApStatus().equals("���")) {
				wname = app.getApName();	if(wname == null) wname="";		//�����
				wid = app.getApSabun();		if(wid == null) wid="";			//����� ���
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else if(app.getApStatus().equals("����"))  {
				vname = app.getApName();	if(vname == null) vname="";		//������
				vid = app.getApSabun();		if(vid == null) vid="";			//������ ���
				vdate = app.getApDate();	if(vdate == null) vdate="";		//������ ��������(���������,���������ʵ�)
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else if(app.getApStatus().equals("����"))  {
				dname = app.getApName();	if(dname == null) dname="";		//������
				did = app.getApSabun();		if(did == null) did="";			//������ ���
				ddate = app.getApDate();	if(ddate == null) ddate="";		//������ �������� (���������,���������ʵ�)
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else {	//����
				if(PROCESS.equals("APP_BOX")) {
					ag_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

					a_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

					r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

					line_cnt++;
				} else {
					line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

					t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

					r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

					line_cnt++;
				}
			}
		}
		if(ag_line.length() != 0) { line += ag_line;	t_line += a_line; }	

		//2���� ���缱 ���ϱ�
		//��������(anb) ���� ��������(storehouse)�� ���� ���� : 200408 : ����
		if(PROCESS.equals("DEL_BOX")) masterDAO.getTable_MasterPid(pid,"storehouse.dbo.app_save");
		else masterDAO.getTable_MasterPid(pid);	
		ArrayList app2_line = new ArrayList();				
		app2_line = masterDAO.getTable_line();		
		Iterator app2_iter = app2_line.iterator();

		while(app2_iter.hasNext()) {
			app2 = (TableAppLine)app2_iter.next();
		
			//���缱
			cmt2 = app2.getApComment(); if(cmt2 == null) cmt2 = "";
			if(cmt2.length() != 0) { 
				t_cmt2 = "\r    "+cmt2; 
				cmt2 = "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+cmt2; 
				line_cnt2++; 
			}

			if(app2.getApStatus().equals("���")) {
				wname2 = app2.getApName();		if(wname2 == null) wname2="";		//�����
				wid2 = app2.getApSabun();		if(wid2 == null) wid2="";			//����� ���
				line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

				t_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
				line_cnt2++;
			}
			else if(app2.getApStatus().equals("����"))  {
				vname2 = app2.getApName();		if(vname2 == null) vname2="";		//������
				vid2 = app2.getApSabun();		if(vid2 == null) vid2="";			//������ ���
				vdate2 = app2.getApDate();		if(vdate2 == null) vdate2="";		//������ ��������(���������,���������ʵ�)
				line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

				t_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
				line_cnt2++;
			}
			else if(app2.getApStatus().equals("����"))  {
				dname2 = app2.getApName();		if(dname2 == null) dname2="";		//������
				did2 = app2.getApSabun();		if(did2 == null) did2="";			//������ ���
				ddate2 = app2.getApDate();		if(ddate2 == null) ddate2="";		//������ �������� (���������,���������ʵ�)
				line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

				t_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
				line_cnt2++;
			}
			else {	//����
				if(PROCESS.equals("APP_BOX")) {
					ag_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

					a_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
					line_cnt2++;
				} else {
					line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

					t_line += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
					line_cnt2++;
				}
			}

		} //while
		if(ag_line2.length() != 0) { line2 += ag_line2;	t_line2 += a_line2; }	

	}//if
	
	//���缱 ������� �˾ƺ��� [APV(����)�ܰ��̸� ����ڰ� �����Ұ���] : 200408 : ����
	String app_status = "",wrt_id="",app_line_data="",app_cancel="N";
	com.anbtech.gw.entity.TableAppMaster tabM = new com.anbtech.gw.entity.TableAppMaster();
	ArrayList tabML = new ArrayList();	
	tabML = masterDAO.getTable_MasterPid(pid);	
	Iterator tab_iter = tabML.iterator();
	while(tab_iter.hasNext()) {
		tabM = (TableAppMaster)tab_iter.next();
		app_status = tabM.getAmAppStatus();						//������ �������
		wrt_id = tabM.getAmWriter();							//�ۼ���
		app_line_data = tabM.getAmAppLine(); 
		if(app_line_data.length() >2)
			app_line_data = app_line_data.substring(0,3);		//��������� ù�ܰ�
	}
	//������� ���ɿ��� �Ǵ�
	if(wrt_id.equals(login_id) && app_status.equals(app_line_data)) {
		app_cancel="Y";
	}

	//�ݱ�
	connMgr.freeConnection("mssql",con);				//Ŀ�ؼ� �ݱ�

	/*********************************************************************
	// 	���� ���� �˾ƺ���
	*********************************************************************/	
	String[] Column = {"c_id","in_date","ac_name","user_name","user_rank","fellow_names",
				"u_year","u_month","u_date","u_time","tu_year","tu_month","tu_date","tu_time",
				"cr_purpose","cr_dest","content","em_tel"};
	bean.setTable("charyang_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (cr_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		c_id = bean.getData("c_id");					//����������ȣ
		in_date = bean.getData("in_date");				//��û����
		ac_name = bean.getData("ac_name");				//�ҼӺμ���
		user_name = bean.getData("user_name");			//����� ��
		user_rank = bean.getData("user_rank");			//����� ����
		fellow_names = bean.getData("fellow_names");	//������	 ���/�̸�;
		u_year = bean.getData("u_year");				//������û���� ��
		u_month = bean.getData("u_month");				//������û���� ��
		u_date = bean.getData("u_date");				//������û���� ��
		u_time = bean.getData("u_time");				//������û���� ��
		tu_year = bean.getData("tu_year");				//������û���� ��
		tu_month = bean.getData("tu_month");			//������û���� ��
		tu_date = bean.getData("tu_date");				//������û���� ��
		tu_time = bean.getData("tu_time");				//������û���� ��
		purpose = bean.getData("cr_purpose");			//����
		cr_dest = bean.getData("cr_dest");				//�༱��
		content = bean.getData("content");				//��������
		em_tel = bean.getData("em_tel");				//��� ����ó
	} //while		

	//�ۼ������ ���ϱ�
	if(in_date.length() != 0) {
		wyear = in_date.substring(0,4);		//�ۼ���
		wmonth = in_date.substring(5,7);		//	  ��
		wdate = in_date.substring(8,10);		//	  ��
	} 

	//������ �̸��� ���ϱ�
	StringTokenizer names = new StringTokenizer(fellow_names,";");
	while(names.hasMoreTokens()) {
		String nms = names.nextToken();
		if(nms.length() < 3) break;
		
		StringTokenizer name = new StringTokenizer(nms,"/");
		int nm = 0;
		while(name.hasMoreTokens()) {
			String n = name.nextToken();
			if(nm == 1) f_names += n + ",";
			nm++;
			if(nm > 2) break;
		}
	}
	if(f_names.length() != 0) f_names = f_names.substring(0,f_names.length()-1);

	//���� ����
	String[] carColumn = {"car_no","model_name"};
	bean.setTable("car_info");			
	bean.setColumns(carColumn);
	bean.setOrder("car_no ASC");	
	query = "where (cid ='"+c_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();
	while(bean.isAll()) {
		v_no = bean.getData("car_no");
		v_model = bean.getData("model_name");
	}

%>

<html>
<head><title>������û��</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> ���������û��</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
				<% 	//���繮���� ��� ó��
					if(PROCESS.equals("APP_ING")) {  %>
						<% if(doc_ste.equals("APV") || doc_ste.equals("APL")) { //����, ���δܰ� %>
							<a href='Javascript:winDecision();'><img src='../../images/bt_commit_app.gif' align='absmiddle' border='0' alt='����'></a>
							<a href='Javascript:winReject();'><img src='../../images/bt_reject_app.gif' align='absmiddle' border='0' alt='�ݷ�'></a>
						<% } else { //�����ܰ� %>
							<a href='Javascript:winDecision();'><img src='../../images/bt_commit_app.gif' align='absmiddle' border='0' alt='����'></a>
							<a href='Javascript:winReject();'><img src='../../images/bt_reject_app.gif' align='absmiddle' border='0' alt='�ݷ�'></a>
						<% } %>
							<a href='Javascript:history.go(-1);'><img src='../../images/bt_list.gif' align='absmiddle' border='0' alt='���'></a>
				<%	} else { //���ۼ��� View ����	 %> 
						<% if(PROCESS.equals("TMP_BOX") || PROCESS.equals("REJ_BOX")) {	//�ӽú�����,�ݷ��� ó�� %> 
							<a href='Javascript:winDelete(<%=pid%>);'><img src='../../images/bt_del.gif' align='absmiddle' border='0' alt='����'></a>
						<%	} %>

						<!-- ���������� ���޹������ �� �׷��� ������� �޴� : 200408 : ���� -->
						<% if(PROCESS.equals("DEL_BOX")) {	%>
							<a href='Javascript:winclose();'><img src='../../images/bt_close.gif' align='absmiddle' border='0' alt='�ݱ�'></a>
						<% } else { %>
							<a href='Javascript:history.go(-1);'><img src='../../images/bt_list.gif' align='absmiddle' border='0' alt='���'></a>
						<% } %>
				<% } %>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=t_line%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=36% align=left><!-- ����ĭ-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">�����</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">
						<img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"><img src='' width='0' height='0'>					
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<% //������ ������ ���� ǥ���ϱ� (��, �ݷ������� �ƴѰ�츸)
						if(vdate.length() == 0)	{//������
							if(ddate.length() == 0) out.println("&nbsp;");
							else out.println("����");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
						}
					%>												
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<%
						if(ddate.length() == 0)	{//������
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
						}
					%>						
					</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- ����ĭ �� -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=v_no%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�𵨸�</td>
           <td width="37%" height="25" class="bg_04"><%=v_model%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr><td height=5 colspan="4"></td></tr></tbody></table>

  <tr><td align="center">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�����Ͻ�</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=u_year%>�� <%=u_month%>�� <%=u_date%>�� <%=u_time%> ~  <%=tu_year%> ��<%=tu_month%>�� <%=tu_date%>�� <%=tu_time%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��û����</td>
           <td width="37%" height="25" class="bg_04"><%=purpose%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�༱��</td>
           <td width="37%" height="25" class="bg_04"><%=cr_dest%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">���������</td>
           <td width="37%" height="25" class="bg_04"><%=user_rank%> <%=user_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��޿���ó</td>
           <td width="37%" height="25" class="bg_04"><%=em_tel%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=f_names%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��Ÿ����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=content%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr><td height=5 colspan="4"></td></tr></tbody></table>

  <tr><td align="center">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��û����</td>
           <td width="37%" height="25" class="bg_04"><%=wyear%>�� <%=wmonth%>�� <%=wdate%>��</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��û�μ���</td>
           <td width="37%" height="25" class="bg_04"><%=ac_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr><td height=10 colspan="4"></td></tr></tbody></table>  
  </td></tr></table>

<%	if(line2.length() != 0) { //2���� �μ� ���ڰ���ø� �����ش� %>
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=t_line2%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=36% align=left><!-- ����ĭ-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">�����</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">
					<% if(wid2.length() != 0) { %>
						<img src="../../../gw/approval/sign/<%=wid2%>.gif" width=60 height=50 align="center"><img src='' width='0' height='0'>
					<% } %>
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<% //������ ������ ���� ǥ���ϱ� (��, �ݷ������� �ƴѰ�츸)
						if(vdate2.length() == 0)	{//������
							if(ddate2.length() == 0) out.println("&nbsp;");
							else out.println("����");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + vid2 + ".gif' width=60 height=50 align='center'>");
						}
					%>												
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<%
						if(ddate2.length() == 0)	{//������
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did2 + ".gif' width=60 height=50 align='center'>");
						}
					%>						
					</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07"><%=wname2%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=vname2%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=dname2%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- ����ĭ �� -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>
<%	}	%>

<form name="eForm" method="post" encType="multipart/form-data">
<input type="hidden" name="mode" value=''>
<input type="hidden" name="PID" value='<%=pid%>'>
</form>

</body>
</html>

<script language=javascript>
<!--
function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

//�����ϱ�
function winDecision()
{
	wopen('../../../servlet/ApprovalProcessServlet?PID=<%=pid%>&mode=PRS',"eleA_app_decision","400","200","scrollbar=no,toolbar=no,status=no,resizable=no");
}
//�ݷ��ϱ�
function winReject()
{
	wopen('../../../servlet/ApprovalProcessServlet?PID=<%=pid%>&mode=PRS_REJ',"eleA_app_decision","400","200","scrollbar=no,toolbar=no,status=no,resizable=no");
}

//����ϱ�
function winprint()
{
	wopen('../../../servlet/ApprovalDetailServlet?PID=<%=doc_id%>&mode=APP_PNT',"print","730","600","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//�ݱ�
function winclose()
{
	if(window.name == 'save_doc') self.close();		//�������� ���ڿ����� ����
	else history.go(-1);							//�������� ����ȭ�鿡�� ����
}
//������ �����ϱ�
function appCancel(pid)
{
	document.eForm.action = "../../../servlet/ApprovalProcessServlet?mode=APP_CANCEL&PID="+pid;
	document.eForm.submit();
}
//����,�ݷ����� �����ϱ�
function winDelete(pid)
{
	document.eForm.action = "../../../servlet/ApprovalProcessServlet?mode=APP_DELETE&PID="+pid;
	document.eForm.submit();
}
-->
</script>
