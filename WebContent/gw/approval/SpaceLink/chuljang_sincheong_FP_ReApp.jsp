<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "�����û�� 2���μ� ������"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.gw.db.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������
	normalFormat money = new com.anbtech.util.normalFormat("#,###");		//������� (���)
	StringProcess str = new com.anbtech.text.StringProcess();				//���ڿ� �ٷ��
	AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(); //���ڰ��系�� & ���缱

	//�����û�� �������
	String query = "";
	String div_name = "";			//�μ���
	String prj_code = "";			//project code
	String user_id = "";			//����� id
	String user_name = "";			//����� ��
	String fellow_names = "";		//������	 ���/�̸�;
	String f_names = "";			//������	 �̸�,
	String bistrip_kind = "";		//����/���� ����
	String bistrip_country = "";	//������
	String bistrip_city = "";		//���ø�
	String traffic_way = "";		//������
	String purpose = "";			//����
	String syear = "";				//���� ��
	String smonth = "";				//    ��
	String sdate = "";				//    ��
	String edyear = "";				//���� ��
	String edmonth = "";			//    ��
	String eddate = "";				//    ��
	String rec = "";				//�μ��ΰ���
	String tel = "";				//��޿���ó
	String bank_no = "";			//���¹�ȣ
	String receiver_id = "";		//����� ������ id
	String receiver_name = "";		//����� ������ ��
	String doc_date = "";			//�ۼ� �����
	String wyear = "";				//�ۼ���
	String wmonth = "";				//	  ��
	String wdate = "";				//	  ��
	int period_n = 0;				//from ~ to �Ⱓ : ��
	int period = 0;					//from ~ to �Ⱓ : ��

	//���缱 ����
	String doc_id = "";				//���ڰ��� ������ȣ
	String link_id = "";			//���ù��� ������ȣ
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
	String writer_id = "";			//����� ���
	String writer_name = "";		//����� ��

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
	// 2�� �������� ���ù��� ������ȣ ���� �ޱ�
	//*********************************************************************
	link_id = request.getParameter("link_id");	if(link_id == null) link_id = "";	//���ù��� ������ȣ

	//*********************************************************************
	// 1�� �ְ��μ� ���缱 ���� �ޱ�
	//*********************************************************************
	String cmt = "";
	masterDAO.getTable_MasterPid(link_id);	
	TableAppLine app = new com.anbtech.gw.entity.TableAppLine();	
	ArrayList table_line = new ArrayList();				//���缱
	table_line = masterDAO.getTable_line();		
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		app = (TableAppLine)line_iter.next();

		//���缱
		cmt = app.getApComment(); if(cmt == null) cmt = "";
		if(cmt.length() != 0) {
			cmt = "\r    "+cmt; 
		}
										
		if(app.getApStatus().equals("���")) {
			wname = app.getApName();	//�����
			wid = app.getApSabun();	//����� ���
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"\r";
		}
		if(app.getApStatus().equals("����"))  {
			vname = app.getApName();	//������
			vid = app.getApSabun();	//������ ���
			vdate = app.getApDate();	//������ �������� (������ �����ϰ� ������ ����ʵ�)
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"\r";
		}
		if(app.getApStatus().equals("����"))  {
			dname = app.getApName();	//������
			did = app.getApSabun();		//������ ���
			ddate = app.getApDate();	//������ �������� (������ �����ϰ� ������ ����ʵ�)\
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"\r";
		}
		if(app.getApStatus().equals("�뺸"))  {	
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"\r";
		}
	}

	/*********************************************************************
	 	����� �׸� ����
	*********************************************************************/	
	String[] csColumn = {"code","code_name"};
	bean.setTable("system_minor_code");			
	bean.setColumns(csColumn);
	bean.setClear();
	bean.setOrder("code DESC");	
	query = "WHERE type = 'BT_COST'";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] btrip = new String[cnt][5];

	int i = 0;
	while(bean.isAll()) {
		btrip[i][0] = bean.getData("code");				//��������ڵ�
		btrip[i][1] = bean.getData("code_name");		//���������
		i++;
	} //while

	//������ ã��
	int sum = 0;			//��û��� �հ�
	int ep_sum = 0;			//���޺�� �հ�
	String[] costColumn = {"gt_id","at_var","gt_cost","cost_cont","ep_cost"};
	bean.setTable("geuntae_account");
	bean.setColumns(costColumn);
	bean.setOrder("at_var ASC");
	for(int c=0; c<i; c++) {
		bean.setSearch("gt_id",link_id,"at_var",btrip[c][0]);
		bean.init_unique();
		if(bean.isAll()) {
			btrip[c][2] = bean.getData("gt_cost");		//����ݾ�
			btrip[c][3] = bean.getData("cost_cont");	//����ݾ� ����
			btrip[c][4] = bean.getData("ep_cost");		//���ޱݾ� ����
			//����հ� ����ϱ�
			btrip[c][2] = str.repWord(btrip[c][2],",","");
			sum += Integer.parseInt(btrip[c][2]);
			btrip[c][4] = str.repWord(btrip[c][4],",","");
			ep_sum += Integer.parseInt(btrip[c][4]);
		}
		else {
			btrip[c][2] = "000";
			btrip[c][3] = "";
			btrip[c][4] = "000";
		}

	}


	/*********************************************************************
	// 	���� ���� �˾ƺ���
	*********************************************************************/	
	String[] Column = {"ac_name","user_id","user_name","fellow_names","prj_code","gt_purpose","u_year","u_month","u_date",
						"tu_year","tu_month","tu_date","gt_dest","country_class","gt_country",
						"traffic_way","receiver_id","receiver_name","proxy","em_tel","bank_no","in_date"};
	bean.setTable("geuntae_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (gt_id ='"+link_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//�μ���
		user_id = bean.getData("user_id");			//�ۼ��� ���
		user_name = bean.getData("user_name");		//�ۼ��� ��
		fellow_names = bean.getData("fellow_names");//������ ���/�̸�
		prj_code = bean.getData("prj_code");		//project code
		purpose = bean.getData("gt_purpose");		//����
		syear = bean.getData("u_year");				//���� ��
		smonth = fmt.toDigits(Integer.parseInt(bean.getData("u_month")));			//    ��
		sdate = fmt.toDigits(Integer.parseInt(bean.getData("u_date")));				//    ��
		edyear = bean.getData("tu_year");			//���� ��
		edmonth = fmt.toDigits(Integer.parseInt(bean.getData("tu_month")));			//    ��
		eddate = fmt.toDigits(Integer.parseInt(bean.getData("tu_date")));			//    ��
		bistrip_city = bean.getData("gt_dest");		//������ ���ø�
		bistrip_kind = bean.getData("country_class");	//�������ܱ���
		bistrip_country = bean.getData("gt_country");	//������
		traffic_way = bean.getData("traffic_way");		//������
		receiver_id = bean.getData("receiver_id");		//������ id
		receiver_name = bean.getData("receiver_name");	//�����θ�
		rec = bean.getData("proxy");				//�μ��ΰ���
		tel = bean.getData("em_tel");				//��޿���ó
		bank_no = bean.getData("bank_no");			//���¹�ȣ
		doc_date = bean.getData("in_date");			//�ۼ������
	} //while

	//�Ⱓ���ϱ�
	period_n = anbdt.getPeriodDate(Integer.parseInt(syear),Integer.parseInt(smonth),Integer.parseInt(sdate),Integer.parseInt(edyear),Integer.parseInt(edmonth),Integer.parseInt(eddate));
	period = period_n + 1;

	//�ۼ������ ���ϱ�
	wyear = doc_date.substring(0,4);		//�ۼ���
	wmonth = doc_date.substring(5,7);		//	  ��
	wdate = doc_date.substring(8,10);		//	  ��

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

	/*********************************************************************
	 	���� ������ ó��
	*********************************************************************/	
	line2 = request.getParameter("doc_app_line"); if(line2 == null) line2 = "";	//���缱
%>

<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title>�����û��(�����μ���)</title>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>

<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>
<form action="chuljang_sincheong_FP_ReApp.jsp" name="eForm" method="post" encType="multipart/form-data">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> �����û�� [����μ�]</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<a href="Javascript:eleApprovalManagerLineSelect();"><img src='../../images/bt_sel_line.gif' align='middle' border='0'></a> 
					<a href="Javascript:eleApprovalRequest();"><img src='../../images/bt_sangsin.gif' align='middle' border='0'></a> 
					<a href='Javascript:winprint();'><img src='../../images/bt_print.gif' align='middle' border='0'></a> 
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=60% align=left><TEXTAREA NAME="" rows=6 cols=66 readOnly style="border:0"><%=line%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=40% align=left><!-- ����ĭ-->
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
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�ҼӺμ�</td>
           <td width="37%" height="25" class="bg_04"><%=div_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><%=user_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><%=syear%>�� <%=smonth%>�� <%=sdate%>��</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><%=edyear%>�� <%=edmonth%>�� <%=eddate%>��</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�����ϼ�</td>
           <td width="37%" height="25" class="bg_04"><%=period_n%>�� <%=period%> �ϰ�</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><%=bistrip_kind%> : <%=bistrip_city%> <%=bistrip_country%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><%=purpose%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><%=traffic_way%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������Ʈ�ڵ�</td>
           <td width="37%" height="25" class="bg_04"><%=prj_code%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><%=f_names%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�����μ���</td>
           <td width="37%" height="25" class="bg_04"><%=rec%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��޿���ó</td>
           <td width="37%" height="25" class="bg_04"><%=tel%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�� �� ��<br>û ��</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
		   <%
				out.print("<TABLE cellSpacing=2 cellPadding=0 width=90% border=0 bordercolordark=white bordercolorlight=#9CA9BA>");
				out.print("<tr><td height=23 class=bg_05 width='15%' align='center'>�׸�</td>"); 
				out.print("<td class=bg_05 width='20%' align='center'>û���ݾ�(��)</td>"); 
				out.print("<td class=bg_05 width='20%' align='center'>���ޱݾ�(��)</td>"); 
				out.print("<td class=bg_05 width='45%' align='center'>û���ݾ׻��⳻��</td></tr>");

				for(int n=1,p=0; p < cnt; n++,p++) {
					out.print("<tr><td class=bg_07 align='center'><input type='hidden' name='code"+n+"' value='"+btrip[p][0]+"'>");
					out.print(btrip[p][1]+"</td>");
					out.print("<td class=bg_07><input class='money' size=15 type='text' name='cost"+n+"' value='"+money.StringToString(btrip[p][2])+"' style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this);' readonly></td>");//û���ݾ�(�ݾ�)
					out.print("<td class=bg_07><input class='money' size=15 type='text' name='ep_cost"+n+"' value='"+money.StringToString(btrip[p][2])+"' style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this);'></td>");//���ޱݾ�(�ݾ�)
					out.print("<td class=bg_07><input size=40 type='text' name='cont"+n+"' value='"+btrip[p][3]+"' readonly></td></tr>");
				}
				out.print("<tr><td class=bg_07 align='center'><b>�հ�</b></td><td class=bg_07 align='right'> &nbsp;"+money.toDigits(sum)+"&nbsp;</td>");
				out.print("<td class=bg_07><input class='money' size=15 type='text' name='ep_sum' value='"+money.toDigits(sum)+"' readonly></td></tr>");
				out.print("</table>");
		   %>
		   </td>
         </tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��û����</td>
           <td width="37%" height="25" class="bg_04"><%=wyear%>�� <%=wmonth%>�� <%=wdate%>��</td>
		   <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">���¹�ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=bank_no%></td>
		 </tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>

<!-- 2�� ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<br>��<br>��</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line2%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=36% align=left><!-- ����ĭ-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">�����</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- ����ĭ �� -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='link_id' value='<%=link_id%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value='������Ʈ <%=prj_code%> �� ���� ����� ����ó�� ��û [<%=user_name%>]'>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='period' value='<%=period%>'>
<input type='hidden' name='account_cnt' value='<%=i%>'>
</form>  

</body>
</html>

<script language=javascript>
<!--
//���缱 �������� 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
	
}

//���� ��� 
function eleApprovalRequest()
{
	if (document.eForm.doc_app_line.value =="") { alert("���缱�� �Է��Ͻʽÿ�."); return; }
	
	 //���缱 �˻�
	data = document.eForm.doc_app_line.value;		//���缱 ����
	s = 0;								//substring������
	e = data.length;					//���ڿ� ����
	decision = agree = 0;
	for(j=0; j<e; j++){
		ocnt = data.indexOf("\n");
		j += ocnt + 1;

		rstr = data.substring(s,ocnt);
		if(rstr.indexOf("����") != -1) decision++;
		if(rstr.indexOf("����") != -1) agree++;
	
		if(rstr.length == 0) j = e;
		data = data.substring(ocnt+1,e);
	}
	if(decision == 0) { alert("�����ڰ� �������ϴ�"); return; }

	document.onmousedown=dbclick;

	//�ϰ����� ����������
	document.eForm.action='../../../servlet/GeunTaeServlet';
	document.eForm.mode.value='CHULJANG_SINCHEONG_SEC';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.submit();

}

//����ϱ�
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
//�ݱ�
function winClose()
{
	window.returnValue='';
	self.close();
}
//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
//�ݾ� õ������ �޸��ֱ�
function InputMoney(input){
	str = input.value;
	str = unComma(str);
	str = Comma(str);
	input.value = str;

	sumMoney();
}
//�Ѿ�ǥ���ϱ�
function sumMoney(){
	var n = '<%=cnt%>';
	var ep_sum = 0;
	for(i=0,j=1; i<n; i++,j++) {
		ep_sum += unCommaObj(eval("document.eForm.ep_cost"+j+".value"));
	}
	document.eForm.ep_sum.value=Comma(ep_sum);
}
function isNumObj(obj)
{
	for (var i = 0; i < obj.value.length ; i++){
		chr = obj.value.substr(i,1);		
		chr = escape(chr);
		key_eg = chr.charAt(1);
		if (key_eg == 'u'){
			key_num = chr.substr(i,(chr.length-1));			
			if((key_num < "AC00") || (key_num > "D7A3")) { 
				event.returnValue = false;
			} 			
		}
	}
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false;
	}
}
function Comma(num) {
	re = /(\d+)/;
	if(re.test(num)){ 
		re.exec(num); num = RegExp.$1; 
		re = /(\d+)(\d{3})/;
		while(re.test(num)){ num = num.replace(re,"$1,$2"); }
	}
    return (num);
}
function unComma(str) {
	return str.replace(/,/g,"");
}

//obj�ι޾� �޸� ���ֱ�
function unCommaObj(input) {
   var inputString = new String;
   var outputString = new String;
   var outputNumber = new Number;
   var counter = 0;
   if (input == '')
   {
	return 0
   }
   inputString=input;
   outputString='';
   for (counter=0;counter <inputString.length; counter++)
   {
      outputString += (inputString.charAt(counter) != ',' ?inputString.charAt(counter) : '');
   }
   outputNumber = parseFloat(outputString);
   return (outputNumber);
}

// ����Ŭ�� ����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>