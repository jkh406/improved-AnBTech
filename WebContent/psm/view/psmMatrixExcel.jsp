<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "PSM ��ü��Ȳ(TABLE) LIST EXCEL���"		
	contentType = "application/vnd.ms-excel; charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp"		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.psm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.psm.entity.psmMasterTable table;
	com.anbtech.psm.entity.psmEnvTable color;

	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String psm_start_date = (String)request.getAttribute("psm_start_date"); 
	if(psm_start_date == null) psm_start_date = "";
	String first_year = (String)request.getAttribute("first_year"); if(first_year == null) first_year = "";
	String last_year = (String)request.getAttribute("last_year"); if(last_year == null) last_year = "";
	String max_cnt = (String)request.getAttribute("max_cnt"); if(max_cnt == null) max_cnt = "0";
	int max_count = Integer.parseInt(max_cnt); 		//����ī�װ��� �ִ� ������
	int span=max_count * 2 + 6;

	//�⵵ ���� ���ϱ�
	int fy = Integer.parseInt(first_year);
	int ly = Integer.parseInt(last_year);
	int df = ly - fy + 2;

	String[][] year = new String[df][2];
	year[0][0] = "";
	year[0][1] = "��ü�⵵";
	for(int i=1,j=0; i<df; i++,j++) {
		year[i][0] = Integer.toString(fy+j);
		year[i][1] = Integer.toString(fy+j);
	}

	//--------------------------------------
	//����Ʈ ��������
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("PSM_List");
	table = new psmMasterTable();
	Iterator table_iter = table_list.iterator();

	//--------------------------------------
	//�������ະ COLOR����Ʈ ��������
	//--------------------------------------
	ArrayList color_list = new ArrayList();
	color_list = (ArrayList)request.getAttribute("COLOR_List");
	int color_cnt = color_list.size();
	String[][] status_color = new String[color_cnt][2];

	color = new psmEnvTable();
	Iterator color_iter = color_list.iterator();
	int n=0;
	while(color_iter.hasNext()){
		color = (psmEnvTable)color_iter.next();
		status_color[n][0] = color.getEnvStatus();
		status_color[n][1] = color.getEnvName();
		n++;
	}
	
%>

<HTML><HEAD><TITLE>PSM ��ü��Ȳ(TABLE) LIST EXCEL���</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
</head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- Ÿ��Ʋ �� ������ ���� -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR>
					<TD valign='middle' colspan=4>
					<%
						for(int si=0; si<df; si++) {
							if(psm_start_date.equals(year[si][0])) 
								out.println(year[si][1]);
						}
					%> �������� ��Ȳ</TD>
					</TR></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--����Ʈ-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1>
				<TBODY>
					<TR vAlign=middle height=23>
						<TD noWrap width=120 align=middle >������</TD>
						<TD noWrap width=80 align=middle >ī�װ�</TD>
<% for(int i=0,t=1; i<max_count; i++,t++) { 
		out.println("<TD noWrap width=70 align=middle >"+t+"</TD>");
	}
%>	
					
					</TR>
<%
	String status="",colour="",psm_subject="";
	int sep=0;
	while(table_iter.hasNext()){
		table = (psmMasterTable)table_iter.next();
		status = table.getPsmStatus(); if(status.equals("11")) status="1";
		if(status.equals("1")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "������";
			psm_subject = table.getPsmKorea()+"["+status+"]";
		} else if(status.equals("2")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "����";
			psm_subject = table.getPsmKorea()+"["+status+"]";
		}else if(status.equals("3")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "������";
			psm_subject = table.getPsmKorea()+"["+status+"]";
		}else if(status.equals("4")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "�Ϸ�";
			psm_subject = table.getPsmKorea()+"["+status+"]";
		}else if(status.equals("5")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "����";
			psm_subject = table.getPsmKorea()+"["+status+"]";
		}else if(status.equals("6")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "���";
			psm_subject = table.getPsmKorea()+"["+status+"]";
		} else {
			colour="white";
			psm_subject = "";
		}

		//HTML ���常���
		if(sep == 0) {
			out.println("<TR>");
			out.println("<TD align=middle height='24'>"+table.getCompName()+"</TD>");
			out.println("<TD align=middle >"+table.getCompCategory()+"</td>");
		}  

		out.println("<TD align=middle bgcolor="+colour+">");
		out.println("<font color='black'><b>"+psm_subject+"</b></font>");
		out.println("</td>");
	
		sep++;
		if(max_count == sep) {
			out.println("</TR>");
			sep=0;
		}  
	}
%>
				</TBODY></TABLE></TD></TR>

</TBODY></TABLE>

</body>
</html>
