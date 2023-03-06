<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "재공자재현황 LIST"		
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
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");		//출력형태

	//----------------------------------------------------
	//	파라미터 받기
	//----------------------------------------------------
	String factory_code = (String)request.getAttribute("factory_code"); 
	if(factory_code == null) factory_code = "전공장";
	if(factory_code.length() == 0) factory_code = "전공장";

	String mfg_no = (String)request.getAttribute("mfg_no"); 
	if(mfg_no == null) mfg_no = "전체";
	mfg_no = str.repWord(mfg_no,"|",",");

	String MODEL_List = (String)request.getAttribute("MODEL_List"); 
	if(MODEL_List == null) MODEL_List = "";
	String model_code = (String)request.getAttribute("model_code"); 
	if(model_code == null) model_code = "전체";
	if(model_code.length() == 0) model_code = MODEL_List;
	model_code = str.repWord(model_code,"|",",");

	//-----------------------------------
	//	BOM 원가산출 LIST
	//-----------------------------------
	float std_total=0,ave_total=0,cur_total=0; 
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ITEM_List");
	table = new primeCostTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD><TITLE>재공자재 EXCEL출력</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR valign='top'><TD>	
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=1 valign="top">
	<TBODY>
		<TR height=25>
			<TD vAlign="center" align='left'>재공자재현황 LIST</TD></TR>
			<TD vAlign="center" align='left'>공장번호: <%=factory_code%></TD></TR>
			<TD vAlign="center" align='left'>모델코드: <%=model_code%></TD></TR>
			<TD vAlign="center" align='left'>작업지시번호: <%=mfg_no%></TD></TR>
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
			ave_total += Double.parseDouble(table.getAveSum());
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

