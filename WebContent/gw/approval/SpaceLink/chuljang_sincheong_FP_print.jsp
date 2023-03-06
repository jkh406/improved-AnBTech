<%@ include file="../../../admin/configPopUp.jsp"%>
<%@ page		
	info= "�����û�� ����"		
	contentType = "text/html; charset=euc-kr"
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.gw.db.*"
	import="java.sql.Connection"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������
	normalFormat money = new com.anbtech.util.normalFormat("#,000");		//������� (���)
	StringProcess str = new com.anbtech.text.StringProcess();				//���ڿ� �ٷ��

	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con); 
	com.anbtech.gw.entity.TableAppLine app = new com.anbtech.gw.entity.TableAppLine();		//1���϶�
	com.anbtech.gw.entity.TableAppLine app2 = new com.anbtech.gw.entity.TableAppLine();		//2���϶�

	//�����û�� �������
	String query		= "&nbsp;";
	String div_name		= "&nbsp;";		//�μ���
	String prj_code		= "&nbsp;";		//project code
	String user_id		= "&nbsp;";		//����� id
	String user_name	= "&nbsp;";		//����� ��
	String fellow_names = "&nbsp;";		//������	 ���/�̸�;
	String f_names		= "&nbsp;";		//������	 �̸�,
	String bistrip_kind = "&nbsp;";		//����/���� ����
	String bistrip_country = "&nbsp;";	//������
	String bistrip_city = "&nbsp;";		//���ø�
	String traffic_way	= "&nbsp;";		//������
	String purpose		= "&nbsp;";		//����
	String syear		= "";			//���� ��
	String smonth		= "";			//    ��
	String sdate		= "";			//    ��
	String edyear		= "";			//���� ��
	String edmonth		= "";			//    ��
	String eddate		= "";			//    ��
	String rec			= "&nbsp;";		//�μ��ΰ���
	String tel			= "&nbsp;";		//��޿���ó
	String receiver_id	= "&nbsp;";		//����� ������ id
	String receiver_name = "&nbsp;";	//����� ������ ��
	String doc_date		= "";			//�ۼ� �����
	int period_n		= 0;			//from ~ to �Ⱓ : ��
	int period			= 0;			//from ~ to �Ⱓ : ��

	//���缱 ����
	String pid		= "";		//������ȣ
	String doc_id	= "";		//���ù��� ������ȣ
	String line		= "";		//�������� ���缱
	String r_line	= "";		//���ۼ����� �Ѱ��ֱ�
	String vdate	= "";		//������ ���� ����
	String ddate	= "";		//������ ���� ����
	String wid		= "";		//����ڻ��
	String vid		= "";		//�����ڻ��
	String did		= "";		//�����ڻ��
	String wname	= "";		//�����
	String vname	= "";		//������
	String dname	= "";		//������
	String PROCESS  = "";		//PROCESS
	String doc_ste  = "";		//doc_ste

	//2���� ���� ����
	String line2	= "";		//�������� ���缱
	String vdate2	= "";		//������ ���� ����
	String ddate2	= "";		//������ ���� ����
	String wid2		= "";		//����ڻ��
	String vid2		= "";		//�����ڻ��
	String did2		= "";		//�����ڻ��
	String wname2	= "";		//�����
	String vname2	= "";		//������
	String dname2	= "";		//������
	String PROCESS2 = "";		//PROCESS
	String doc_ste2 = "";		//doc_ste

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
		masterDAO.getTable_MasterPid(pid);	
		ArrayList app_line = new ArrayList();				
		app_line = masterDAO.getTable_line();		
		Iterator app_iter = app_line.iterator();

		while(app_iter.hasNext()) {
			app = (TableAppLine)app_iter.next();
			
			//���缱
			cmt = app.getApComment(); if(cmt == null) cmt = "";
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
		masterDAO.getTable_MasterPid(doc_id);	
		ArrayList app_line = new ArrayList();				
		app_line = masterDAO.getTable_line();		
		Iterator app_iter = app_line.iterator();

		while(app_iter.hasNext()) {
			app = (TableAppLine)app_iter.next();
			
			//���缱
			cmt = app.getApComment(); if(cmt == null) cmt = "";
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
			else {	//���� : ����Թ����� �����ڸ� ������ �ڷ� ������ ����
				if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_BTR")) {
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
		masterDAO.getTable_MasterPid(pid);	
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
	connMgr.freeConnection("mssql",con);		//�ݱ�

	/*********************************************************************
	 	����� �׸� �� ������ ����
	*********************************************************************/	
	//�����׸� ã��
	String[] csColumn = {"ys_name","ys_value"};
	bean.setTable("yangsic_env");			
	bean.setColumns(csColumn);
	bean.setClear();
	bean.setOrder("ys_name DESC");	
	bean.setSearch("ys_value","�����");
	bean.init_unique();

	String cs_code = "";
	if(bean.isAll()) cs_code = bean.getData("ys_name");	//����� �ڵ�(��з�����)
	String cs_head = cs_code.substring(0,4);			//�Ϸù�ȣ ������ ������ �κ�

	//����� �׸� �迭�� ���
	bean.setOrder("ys_name ASC");	
	bean.setSearch("ys_name",cs_head);
	bean.init();

	int cnt = bean.getTotalCount();
	String[][] btrip = new String[cnt][4];

	int i = 0;
	while(bean.isAll()) {
		btrip[i][0] = bean.getData("ys_name");				//��������ڵ�
		btrip[i][1] = bean.getData("ys_value");				//���������
		i++;
	} //while

	//������ ã��
	int sum = 0;			//��� �հ�
	String[] costColumn = {"gt_id","at_var","gt_cost","cost_cont"};
	bean.setTable("geuntae_account");
	bean.setColumns(costColumn);
	bean.setOrder("at_var ASC");
	for(int c=0; c<i; c++) {
		bean.setSearch("gt_id",doc_id,"at_var",btrip[c][0]);
		bean.init_unique();
		if(bean.isAll()) {
			btrip[c][2] = bean.getData("gt_cost");		//����ݾ�
			btrip[c][3] = bean.getData("cost_cont");	//����ݾ� ����
			//����հ� ����ϱ�
			btrip[c][2] = str.repWord(btrip[c][2],",","");
			sum += Integer.parseInt(btrip[c][2]);
		}
		else {
			btrip[c][2] = "000";
			btrip[c][3] = "";
		}

	}


	/*********************************************************************
	// 	���� ���� �˾ƺ���
	*********************************************************************/	
	String[] Column = {"ac_name","user_id","user_name","fellow_names","prj_code","gt_purpose","u_year","u_month","u_date",
						"tu_year","tu_month","tu_date","gt_dest","country_class","gt_country",
						"traffic_way","receiver_id","receiver_name","proxy","em_tel","in_date"};
	bean.setTable("geuntae_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (gt_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name	= bean.getData("ac_name")==""?"&nbsp;":bean.getData("ac_name");			//�μ���
		user_id		= bean.getData("user_id");			//�ۼ��� ���
		user_name	= bean.getData("user_name");		//�ۼ��� ��
		fellow_names = bean.getData("fellow_names");//������ ���/�̸�
		prj_code	= bean.getData("prj_code");		//project code
		purpose		= bean.getData("gt_purpose");		//����
		syear		= bean.getData("u_year");				//���� ��
		smonth		= fmt.toDigits(Integer.parseInt(bean.getData("u_month")));			//    ��
		sdate		= fmt.toDigits(Integer.parseInt(bean.getData("u_date")));				//    ��
		edyear		= bean.getData("tu_year");			//���� ��
		edmonth		= fmt.toDigits(Integer.parseInt(bean.getData("tu_month")));			//    ��
		eddate		= fmt.toDigits(Integer.parseInt(bean.getData("tu_date")));			//    ��
		bistrip_city = bean.getData("gt_dest");		//������ ���ø�
		bistrip_kind = bean.getData("country_class");	//�������ܱ���
		bistrip_country = bean.getData("gt_country");	//������
		traffic_way = bean.getData("traffic_way");		//������
		receiver_id = bean.getData("receiver_id");		//������ id
		receiver_name = bean.getData("receiver_name");	//�����θ�
		rec			= bean.getData("proxy");				//�μ��ΰ���
		tel			= bean.getData("em_tel");				//��޿���ó
		doc_date	= bean.getData("in_date");			//�ۼ������
	} //while

	//�Ⱓ���ϱ�
	if(syear.length() != 0) {
		period_n = anbdt.getPeriodDate(Integer.parseInt(syear),Integer.parseInt(smonth),Integer.parseInt(sdate),Integer.parseInt(edyear),Integer.parseInt(edmonth),Integer.parseInt(eddate));
		period = period_n + 1;
	}

	//�ۼ������ ���ϱ�
	String wyear="",wmonth="",wdate="";
	if(doc_date.length() != 0) {
		wyear = doc_date.substring(0,4);			//�ۼ���
		wmonth = doc_date.substring(5,7);		//	  ��
		wdate = doc_date.substring(8,10);		//	  ��
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

%>

<html>
<head>
<title>�����û��</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>
</head>

<BODY topmargin="5" leftmargin="5" oncontextmenu="return false">
<!-- �ΰ�,����,��ư -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">�����û��</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
			<a href='Javascript:winprint();'><img src='../../images/bt_print.gif' align='absmiddle' border='0'></a> <!-- ��� -->
			<a href='Javascript:self.close();'><img src='../../images/bt_close.gif' align='absmiddle' border='0'></a> <!-- �ݱ� -->
	</div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>

<!-- �������� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
	<TR vAlign=middle height=23>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
		<TD noWrap width=370 align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=50 readOnly style="border:0"><%=t_line%></TEXTAREA></TD>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
		<TD noWrap width=50 align=middle class="bg_07">�����</TD>
		<TD noWrap width=50 align=middle class="bg_07">������</TD>
		<TD noWrap width=50 align=middle class="bg_07">������</TD></TR>
	<TR vAlign=middle height=50>
		<TD noWrap width=50 align=middle class="bg_06"><img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></TD>
		<TD noWrap width=50 align=middle class="bg_06">
		<% //������ ������ ���� ǥ���ϱ� (��, �ݷ������� �ƴѰ�츸)
			if(vdate.length() == 0)	{//������
				if(ddate.length() == 0) out.println("&nbsp;");
				else out.println("����");
			} else {
				out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
			}
		%>		
		</TD>
		<TD noWrap width=50 align=middle class="bg_06">
		<%
			if(ddate.length() == 0)	{//������
				out.println("&nbsp;");
			} else {
				out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
			}
		%>			
		</TD></TR>
	<TR vAlign=middle height=23>
		<TD noWrap width=50 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- ���� ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			 <tr>
				<td width="15%" height="25" align="middle" class="bg_05">�ҼӺμ�</td>
				<td width="35%" class="bg_06"><%=div_name%><img src='' width='0' height='0'></td>
				<td width="15%" height="25" align="middle" class="bg_05">�����</td>
				<td width="35%" class="bg_06"><%=user_name%><img src='' width='0' height='0'></td></tr>
			 <tr>
				<td width="15%" height="25" align="middle" class="bg_05">������</td>
				<td width="35%" class="bg_06"><%=syear%>�� <%=smonth%>�� <%=sdate%>��</td>
				<td width="15%" height="25" align="middle" class="bg_05">������</td>
				<td width="35%" class="bg_06"><%=edyear%>�� <%=edmonth%>�� <%=eddate%>��</td></tr>
			 <tr>
				<td width="15%" height="25" align="middle" class="bg_05">�����ϼ�</td>
				<td width="35%" class="bg_06"><%=period_n%>�� <%=period%> �ϰ�</td>
				<td width="15%" height="25" align="middle" class="bg_05">������</td>
				<td width="35%" class="bg_06"><%=bistrip_kind%><img src='' width='0' height='0'></td></tr>
			 <tr>
				<td width="15%" height="25" align="middle" class="bg_05">�������</td>
				<td width="35%" class="bg_06"><%=purpose%><img src='' width='0' height='0'></td>
				<td width="15%" height="25" align="middle" class="bg_05">������</td>
				<td width="35%" class="bg_06"><%=traffic_way%><img src='' width='0' height='0'></td></tr>
			 <tr>
				<td width="15%" height="25" align="middle" class="bg_05">���ð���</td>
				<td width="35%" class="bg_06"><%=prj_code%><img src='' width='0' height='0'></td>
				<td width="15%" height="25" align="middle" class="bg_05">������</td>
				<td width="35%" class="bg_06"><%=f_names%><img src='' width='0' height='0'></td></tr>				
			 <tr>
				<td width="13%" height="25" align="middle"  class="bg_05">�����μ���</td>
				<td width="37%" height="25" class="bg_06"><%=rec%><img src='' width='0' height='0'></td>
				<td width="13%" height="25" align="middle"  class="bg_05">��޿���ó</td>
				<td width="37%" height="25" class="bg_06"><%=tel%><img src='' width='0' height='0'></td></tr>
			 <tr>
				<td width="13%" height="25" align="middle"  class="bg_05">��û����</td>
				<td width="87%" height="25" class="bg_06" colspan='3'><%=wyear%>�� <%=wmonth%>�� <%=wdate%>��</td>
			 </tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		</table>
	</td></tr></table>

<% if(line2.length() != 0) { //2���� �μ� ���ڰ���ø� �����ش� %>
	<TABLE cellSpacing=0 cellPadding=0 width="700" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
		<TBODY>
			<TR vAlign=middle height=23>
				<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
				<TD noWrap width=100% align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=63 readOnly style="border:0"><%=t_line%></TEXTAREA></TD>
				<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
				<TD noWrap width=50 align=middle class="bg_07">�����</TD>
				<TD noWrap width=50 align=middle class="bg_07">������</TD>
				<TD noWrap width=50 align=middle class="bg_07">������</TD></TR>
			<TR vAlign=middle height=50>
				<TD noWrap width=50 align=middle class="bg_06"><img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></TD>
				<TD noWrap width=50 align=middle class="bg_06">
					<% if(wid2.length() != 0) { %>
						<img src="../../../gw/approval/sign/<%=wid2%>.gif" width=60 height=50 align="center"></td>   
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
			<TR vAlign=middle height=23>
				<TD noWrap width=50 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
				<TD noWrap width=50 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
				<TD noWrap width=50 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>


<% } %>

<form action="chuljang_sincheong_FP_write.jsp" name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='doc_app_line' value=''>
	<input type='hidden' name='user_id' value='<%=user_id%>'>
	<input type='hidden' name='prj_code' value='<%=prj_code%>'>
	<input type='hidden' name='fellow_names' value='<%=fellow_names%>'>
	<input type='hidden' name='traffic_way' value='<%=traffic_way%>'>
	<input type='hidden' name='bistrip_kind' value='<%=bistrip_kind%>'>
	<input type='hidden' name='bistrip_country' value='<%=bistrip_country%>'>
	<input type='hidden' name='bistrip_city' value='<%=bistrip_city%>'>
	<input type='hidden' name='purpose' value='<%=purpose%>'>
	<input type='hidden' name='doc_syear' value='<%=syear%>'>
	<input type='hidden' name='doc_smonth' value='<%=smonth%>'>
	<input type='hidden' name='doc_sdate' value='<%=sdate%>'>
	<input type='hidden' name='doc_edyear' value='<%=edyear%>'>
	<input type='hidden' name='doc_edmonth' value='<%=edmonth%>'>
	<input type='hidden' name='doc_eddate' value='<%=eddate%>'>
	<input type='hidden' name='doc_receiver' value='<%=rec%>'>
	<input type='hidden' name='doc_tel' value='<%=tel%>'>
<%
	int hrlen = btrip.length-1;
	for(int m=1; m <= hrlen; m++) {
			out.println("<input type='hidden' name='code"+m+"' value='"+btrip[m][0]+"'>");	//�׸�����ڵ�
			int item_cost = Integer.parseInt(btrip[m][2]);
			out.println("<input type='hidden' name='cost"+m+"' value='"+money.toDigits(item_cost)+"'>");//���(�ݾ�)
			out.println("<input type='hidden' name='cont"+m+"' value='"+btrip[m][3]+"'></td>");//���⳻��
	}

%>
	<input type='hidden' name='receiver_id' value='<%=receiver_id%>'>
	<input type='hidden' name='doc_id' value='<%=doc_id%>'>
</form>
</body></html>

<script language=javascript>
<!--
function wopen(url, t, w, h) {
	var sw;
	var sh;
alert(url);
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

//���ۼ��ϱ�
function winRewrite()
{
	var line = '<%=r_line%>';
	var ln = "";
	for(i=0; i<line.length; i++) {
		if(line.charAt(i) == '@') ln += '\n';
		else ln += line.charAt(i);
	}
	
	document.eForm.action = "chuljang_sincheong_FP_Rewrite.jsp";
	document.eForm.doc_app_line.value=ln;
	document.eForm.submit();
	window.returnValue='RL';
}
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}

-->
</script>
