<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "PSM 전체현황 LIST EXCEL출력"		
	contentType = "application/vnd.ms-excel; charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.psm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.psm.entity.psmMasterTable table;
	com.anbtech.psm.entity.psmEnvTable color;
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String sItem = (String)request.getAttribute("sItem"); if(sItem == null) sItem = "pid";
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String psm_start_date = (String)request.getAttribute("psm_start_date"); 
	if(psm_start_date == null) psm_start_date = "";
	String first_year = (String)request.getAttribute("first_year"); if(first_year == null) first_year = "";
	String last_year = (String)request.getAttribute("last_year"); if(last_year == null) last_year = "";

	//년도 구간 구하기
	int fy = Integer.parseInt(first_year);
	int ly = Integer.parseInt(last_year);
	int df = ly - fy + 2;

	String[][] year = new String[df][2];
	year[0][0] = "";
	year[0][1] = "전체년도";
	for(int i=1,j=0; i<df; i++,j++) {
		year[i][0] = Integer.toString(fy+j);
		year[i][1] = Integer.toString(fy+j);
	}

	//--------------------------------------
	//리스트 가져오기
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("PSM_List");
	table = new psmMasterTable();
	Iterator table_iter = table_list.iterator();

	//--------------------------------------
	//과제진행별 COLOR리스트 가져오기
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

<HTML><HEAD><TITLE>PSM 전체현황 LIST EXCEL출력</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
</head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR>
					<TD valign='middle'></TD>
					<TD valign='middle' colspan=4> 
					<%
						for(int si=0; si<df; si++) {
							if(psm_start_date.equals(year[si][0])) 
								out.println(year[si][1]);
						}
					%> 년도 과제진행 현황</TD>
					</TR></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--리스트-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1>
				<TBODY>
					<TR vAlign=middle height=23>
						<TD noWrap width=90 align=middle>과제코드</TD>
						<TD noWrap width=100% align=middle>과제명(한글)</TD>
						<TD noWrap width=70 align=middle>과제종류</TD>						
						<TD noWrap width=100 align=middle>과제고객</TD>
						<TD noWrap width=80 align=middle>카테고리</TD>						
						<TD noWrap width=60 align=middle>과제상태</TD>						
						<TD noWrap width=80 align=middle>등록일자</TD>					
						<TD noWrap width=80 align=middle>계약일자</TD>						
						<TD noWrap width=110 align=middle>과제PM</TD>
					</TR>
<%
	String status="",colour="";
	while(table_iter.hasNext()){
		table = (psmMasterTable)table_iter.next();
		status = table.getPsmStatus();
		if(status.equals("1") || status.equals("11")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "미진행";
		}else if(status.equals("2")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "진행";
		}else if(status.equals("3")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "재진행";
		}else if(status.equals("4")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "완료";
		}else if(status.equals("5")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "보류";
		}else if(status.equals("6")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "취소";
		}
%>
					<TR>
						<TD align=middle height="24"><%=table.getPsmCode()%></TD>
						<TD align=middle><%=table.getPsmKorea()%></td>		
						<TD align=middle><%=table.getPsmType()%></td>
						<TD align=middle><%=table.getCompName()%></td>						
						<TD align=middle><%=table.getCompCategory()%></td>					
						<TD align=middle>
							<span style="color:<%=colour%>;font-weight:bold;"><%=status%></span></td>
						<TD align=middle><%=table.getRegDate()%></td>
						<TD align=middle><%=table.getContractDate()%></td>
						<TD align=middle><%=table.getPsmPm()%></td>	
					</TR>
<%		
	}
%>
				</TBODY></TABLE></TD></TR>

</TBODY></TABLE>

</body>
</html>
