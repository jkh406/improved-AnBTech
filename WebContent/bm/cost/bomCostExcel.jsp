<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "BOM 원가산출 EXCEL LIST"		
	contentType = "application/vnd.ms-excel; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%

	//초기화 선언
	com.anbtech.bm.entity.primeCostTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");		//출력형태

	//----------------------------------------------------
	//	BOM MASTER정보
	//----------------------------------------------------
	String model_code="",fg_code="",gid=""; 

	com.anbtech.bm.entity.mbomMasterTable masterT;
	masterT = (mbomMasterTable)request.getAttribute("MASTER_List");
	model_code = masterT.getModelCode();
	fg_code = masterT.getFgCode();

	String model_code_empty = model_code;
	String fg_code_empty = fg_code;

	if(model_code==null) model_code_empty ="";
	if(fg_code==null) fg_code_empty ="";

	//-----------------------------------
	//	BOM 원가산출 LIST
	//-----------------------------------
	float std_total=0,ave_total=0,cur_total=0; 
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("PART_List");
	table = new primeCostTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD><TITLE>원가산출 EXCEL출력</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR valign='top'><TD>	
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=1 valign="top">
	<TBODY>
		<TR height=25>
			<TD vAlign="center" align='left'>BOM 원가산출 LIST</TD></TR>
		<TR height="25"><TD vAlign="center" align='left'>모델코드: <%=model_code_empty%> 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;F/G코드: <%=fg_code_empty%></TD></TR>
		<TR height=100% height="25">
			<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1>
			<TBODY>
				<TR vAlign=middle height=25>
				  <TD noWrap width=90 align=middle>품목코드</TD>
				  <TD noWrap width=100 align=middle>품목명</TD>				  
				  <TD noWrap width=400 align=middle>품목설명</TD>				  
				  <TD noWrap width=40 align=middle>수량</TD>				  			  
				  <TD noWrap width=60 align=middle>평균단가</TD>				  
				  <TD noWrap width=70 align=middle>평균총액</TD>  
				</TR>

	<%  int cnt = 1;
		while(table_iter.hasNext()) {
			table = (primeCostTable)table_iter.next();
			//std_total += Double.parseDouble(table.getStdSum());
			ave_total += Double.parseDouble(table.getAveSum());
			//cur_total += Double.parseDouble(table.getCurSum()); 
	%>	
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24"><%=table.getItemCode()%></TD>
				  <TD align=left><%=table.getItemName()%></TD>
				  <TD align=left><%=table.getItemDesc()%></TD>
				  <TD align=middle><%=table.getItemCount()%></TD>
				  <TD align=right><%=nfm.StringToString(table.getAvePrice())%></TD>
				  <TD align=right><%=nfm.StringToString(table.getAveSum())%></TD>
				</TR>
	<% 
		cnt++;
		}  //while 

	%>
				<TR>
				  <TD align=middle height="24">총 합계</TD>
				  <TD align=left height="24">&nbsp;</TD>
				  <TD align=left>&nbsp;</TD>
				  <TD align=middle>&nbsp;</TD>
				  <TD align=right>&nbsp;</TD>
				  <TD align=right><%=nfm.DoubleToString(ave_total)%></TD>
				</TR>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</TD></TR></TABLE>
</BODY>
</HTML>

