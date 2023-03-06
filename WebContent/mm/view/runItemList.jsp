<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "재공자재현황 LIST"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.bm.entity.primeCostTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");		//출력형태

	//----------------------------------------------------
	//	파라미터 받기
	//----------------------------------------------------
	String Factory_List = (String)request.getAttribute("Factory_List"); 
	if(Factory_List == null) Factory_List = "";
	String MFG_List = (String)request.getAttribute("MFG_List"); 
	if(MFG_List == null) MFG_List = "";
	String factory_no = (String)request.getAttribute("factory_no"); 
	if(factory_no == null) factory_no = "";
	String factory_code = (String)request.getAttribute("factory_code"); 
	if(factory_code == null) factory_code = "";
	String mfg_no = (String)request.getAttribute("mfg_no"); 
	if(mfg_no == null) mfg_no = "";
	String MODEL_List = (String)request.getAttribute("MODEL_List"); 
	if(MODEL_List == null) MODEL_List = "";
	String model_code = (String)request.getAttribute("model_code"); 
	if(model_code == null) model_code = "";

	//-----------------------------------
	//	재공자재 현황및 원가산출 LIST
	//-----------------------------------
	float std_total=0,ave_total=0,cur_total=0; 
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ITEM_List");
	table = new primeCostTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display()'>
<FORM name="sForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><IMG src="../mm/images/blet.gif"> 재공자재현황</TD>
					</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0>
				<TBODY>
				<TR>
					<TD align=left width=5 ></TD>
					<TD width='600' vAlign="BOTTOM"  align='left'>
						<SELECT name="factory_code" style='font-size:9pt;color="black";'> 
						<OPTGROUP label='---------'>
						<OPTION value=''>전체공장</option>
				<%
						String sel = "", fc="";
						StringTokenizer fc_list = new StringTokenizer(Factory_List,"|");
						while(fc_list.hasMoreTokens()) {
							fc = fc_list.nextToken(); 
							if(factory_code.equals(fc)) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+fc+"'>"+fc+"</option>");
						} 
				%>		</SELECT>

						<SELECT name="model_code" style='font-size:9pt;color="black";' onChange="javascript:goMfgNo()"> 
						<OPTGROUP label='---------'>
						<OPTION value=''>전체모델</option>
				<%
						String msel = "", mc="",model_no="";
						StringTokenizer mc_list = new StringTokenizer(MODEL_List,"|");
						while(mc_list.hasMoreTokens()) {
							mc = mc_list.nextToken(); 
							model_no=mc.substring(0,mc.indexOf(":"));
							if(model_code.equals(model_no)) msel = "selected";
							else msel = "";
							out.print("<option "+msel+" value='"+model_no+"'>"+mc+"</option>");
						} 
				%>		</SELECT>
				
						<SELECT name="mfg_no" style='font-size:9pt;color="black";'> 
						<OPTGROUP label='---------'>
						<OPTION value='<%=MFG_List%>'>전체보기</option>
					<%
						String asel = "", mf="";
						StringTokenizer mf_list = new StringTokenizer(MFG_List,"|");
						while(mf_list.hasMoreTokens()) {
							mf = mf_list.nextToken(); 
							if(mfg_no.equals(mf)) asel = "selected";
							else asel = "";
							out.print("<option "+asel+" value='"+mf+"'>"+mf+"</option>");
						} 
					%>	</SELECT>
					
						<A href='javascript:goView();'><img src='../mm/images/bt_sel.gif' align='absmiddle' border=0></a>
						<A href='javascript:goExcel();'><img src='../mm/images/bt_excel.gif' align='absmiddle' border=0></a>
					</TD>
					<TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY></TABLE>
	</TD></TR>
</TABLE>

<!-- LIST -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
<TR valign='top'><TD>	
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=100%>
			<TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=25>
				  <TD noWrap width=90 align=middle class='list_title'>품목코드</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
				  <TD noWrap width=160 align=middle class='list_title'>품목명</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
				  <TD noWrap width=100% align=middle class='list_title'>품목설명</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
				  <TD noWrap width=60 align=middle class='list_title'>출고수량</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
				  <TD noWrap width=60 align=middle class='list_title'>평균단가</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
				  <TD noWrap width=70 align=middle class='list_title'>평균총액</TD>
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>

	<%  int cnt = 1;
		while(table_iter.hasNext()) {
			table = (primeCostTable)table_iter.next();
			ave_total += Double.parseDouble(table.getAveSum());
	%>	
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=table.getItemCode()%>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg'><%=table.getItemName()%>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg'><%=table.getItemDesc()%>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=nfm.StringToString(table.getItemCount())%>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg'><%=nfm.StringToString(table.getAvePrice())%>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg'><%=nfm.StringToString(table.getAveSum())%>&nbsp;</TD>
				</TR>
				<TR><TD colSpan=19 background="../mm/images/dot_line.gif"></TD></TR>
	<% 
		cnt++;
		}  //while 

	%>
	<% if(cnt>1) {%>
				<TR bgColor=#F5F5F5>
				  <TD align=middle height="24" class='list_bg'>총 합계</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left height="24">&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg'>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg'>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg'><%=nfm.DoubleToString(ave_total)%>&nbsp;</TD>
				</TR>
			
		<%}%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>

<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="factory_no" value='<%=factory_no%>'>
</FORM>

<SCRIPT language=javascript>
<!--
//FG&ModelCode바로가기
function goMfgNo()
{	
	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='run_item_search';
	document.sForm.submit();
}
//내용보기
function goView()
{	
	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='run_item';
	document.sForm.submit();
}
//Excel하기
function goExcel()
{	
	var mfg_no = document.sForm.mfg_no.value;
	var model_code = document.sForm.model_code.value;
	var factory_code = document.sForm.factory_code.value;
	var factory_no = document.sForm.factory_no.value;
	var para = "mfg_no="+mfg_no+"&factory_code="+factory_code;
		para +="&model_code="+model_code+"&factory_no="+factory_no;
	wopen("../servlet/mfgOrderServlet?mode=run_item_excel&"+para,"proxy","870","580","scrollbars=no,toolbar=no,status=yes,resizable=yes,menubar=yes");
}
//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//var div_h = h - 373 ;
	var div_h = c_h +379;
	item_list.style.height = div_h;
}
-->
</SCRIPT>

</TD></TR></TABLE>
</BODY>
</HTML>

