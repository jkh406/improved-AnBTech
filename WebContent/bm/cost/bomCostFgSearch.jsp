<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "정전개할 FG 찾기"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();

	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String gid = (String)request.getAttribute("gid"); if(gid == null) gid = "";
	String parent_code = (String)request.getAttribute("parent_code"); 
	if(parent_code == null) parent_code = "";

	String sel_date = (String)request.getAttribute("sel_date"); 
	if(sel_date.length() ==0) sel_date = anbdt.getDate(0);
	if(sel_date.length() == 8) sel_date = anbdt.getSepDate(sel_date,"/");

	String step = (String)request.getAttribute("step"); if(step.length() ==0) step="M";

	//-----------------------------------
	//	FG 리스트
	//-----------------------------------
	com.anbtech.bm.entity.mbomMasterTable fg;
	ArrayList fg_list = new ArrayList();
	fg_list = (ArrayList)request.getAttribute("FG_List");
	fg = new mbomMasterTable();
	Iterator fg_iter = fg_list.iterator();

	//-----------------------------------
	//	ASSY 리스트
	//-----------------------------------
	com.anbtech.bm.entity.mbomStrTable assy;
	ArrayList assy_list = new ArrayList();
	assy_list = (ArrayList)request.getAttribute("ASSY_List");
	assy = new mbomStrTable();
	Iterator assy_iter = assy_list.iterator();
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<SCRIPT language=javascript>
<!--
//검색하기
function goSearch()
{
	var sWord = document.sForm.sWord.value; 
	if(sWord.length < 4) {
		alert('검색할 품목코드는 4자 이상 입력해야 합니다.'); return;
	}

	document.sForm.action='../servlet/BomShowServlet';
	document.sForm.mode.value='cost_fgsearch';
	document.sForm.gid.value='';
	document.sForm.parent_code.value='';
	document.sForm.level_no.value='0';
	document.sForm.step.value='M';
	document.sForm.submit();

}
//원가산출내용조회
function goView()
{
	//모델선택정보
	var a = document.sForm.fg_code.selectedIndex; 
	if(a != -1) {
		var fg = document.sForm.fg_code.options[a].value.split("|"); 
		var gid = fg[0];
		var model_code = fg[1];
		var org_model_code = fg[1];
	}
	else  { alert('F/G코드 검색후 진행하십시오'); return; }

	//ASSY선택정보
	var level_no = "0";
	var b = document.sForm.assy_code.selectedIndex;
	if(b != -1) {
		var assy_code = document.sForm.assy_code.options[b].value;
		var assy_lvn = assy_code.split("|");
		level_no = assy_lvn[0];
		if(b != 0) model_code = assy_lvn[1];
		
	}
	
	//다단계 또는 단단계 선택내용
	var step = "";
	step = document.sForm.ck.value;

	//선택일자
	var sel_date = document.sForm.sel_date.value;

	//LIST 보기 [해당ASSY]
	parent.list.document.sForm.action='../servlet/BomShowServlet';
	parent.list.document.sForm.mode.value='cost_list';
	parent.list.document.sForm.gid.value=gid;
	parent.list.document.sForm.level_no.value=level_no;
	parent.list.document.sForm.parent_code.value=model_code;
	parent.list.document.sForm.step.value=step;
	parent.list.document.sForm.sel_date.value=sel_date;
	parent.list.document.sForm.submit();
}
//유효일자 찾기
function OpenCalendar(FieldName) {
	var strUrl = "../bm/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//엑셀리포트
function goExcel()
{
	//모델선택정보
	var a = document.sForm.fg_code.selectedIndex; 
	if(a != -1) {
		var fg = document.sForm.fg_code.options[a].value.split("|"); 
		var gid = fg[0];
		var model_code = fg[1];
	} else { alert('FG코드가 없습니다. 먼저 FG 코드를 검색하십시오.'); return; }

	//선택일자
	var sel_date = document.sForm.sel_date.value;

	var para = "gid="+gid+"&parent_code="+model_code+"&level_no=0&sel_date="+sel_date;
	wopen("../servlet/BomShowServlet?mode=cost_excel&"+para,"proxy","870","580","scrollbars=no,toolbar=no,status=yes,resizable=yes,menubar=yes");
}
//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
-->
</SCRIPT>

<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>
<FORM name="sForm" method="post" style="margin:0">

<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=0 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>	
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="center">
		<TBODY>
			<TR height="25"><!--버튼 및 페이징-->
					<TD vAlign="BOTTOM" align='left' width='19%' style='padding-left:5px'>
						<INPUT type='text' name='sWord' value='<%=sWord%>' size='11' onfocus="javascript:empty();">
					<A href='javascript:goSearch();'><img src='../bm/images/bt_search.gif' align='absmiddle' border=0></a></TD>
					<TD width='45%' vAlign="BOTTOM"  align='center'>
						<SELECT name="fg_code" style='font-size:9pt;color="black";'> 
						<OPTGROUP label='---------'>
				<%
						String sel = "";
						while(fg_iter.hasNext()) {
							fg = (mbomMasterTable)fg_iter.next(); 
							if(gid.equals(fg.getPid())) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+fg.getPid()+"|"+fg.getModelCode()+"'>");
							out.println(fg.getFgCode()+"</option>");
						} 
				%>		</SELECT>
				
						<SELECT name="assy_code" style='font-size:9pt;color="black";'> 
						<OPTGROUP label='---------'>
						<OPTION value='0|0'>0: 전체보기</option>
					<%
							String asel = "";
							while(assy_iter.hasNext()) {
								assy = (mbomStrTable)assy_iter.next(); 
								if(parent_code.equals(assy.getParentCode())) asel = "selected";
								else asel = "";
								out.print("<option "+asel+" value='"+assy.getLevelNo()+"|"+assy.getParentCode()+"'>");
								out.println(assy.getLevelNo()+": "+assy.getParentCode()+"</option>");
						} 
					%>	</SELECT>
					
						<SELECT name='ck' style='font-size:9pt;color="black";'>
							<OPTION value='M'>다단계</OPTION>
							<OPTION value='S'>단단계</OPTION>

				<% if(step.equals("M")) {%>
						<SCRIPT language='javascript'>
							document.sForm.ck.value='M';
						</SCRIPT>
				<% } else { %>
						<SCRIPT language='javascript'>
							document.sForm.ck.value='S';
						</SCRIPT>	
						<% }%>
						</SELECT>

						<A href='javascript:goView();'><img src='../bm/images/bt_sel.gif' align='absmiddle' border=0></a>
					</TD>
					<TD width='33%' vAlign="BOTTOM"  align='right' style='padding-right:5px'>
						<IMG src='../bm/images/valid_date.gif' align='absmiddle' border='0'>
						<INPUT type='text' name='sel_date' value='<%=sel_date%>' size='9'  style='font-size:9pt;color="black";' readonly>
						<A Href="Javascript:OpenCalendar('sel_date');"><img src="../bm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
						<a href="javascript:goExcel();"  onfocus="this.blur();"><img src="../bm/images/bt_excel.gif" border="0" align="absmiddle"></a>
</TD></TR></TABLE>
<INPUT type='hidden' name='sItem' value='fg_code'>
<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="gid" size="15" value="">
<INPUT type="hidden" name="parent_code" size="15" value="">
<INPUT type="hidden" name="level_no" size="15" value="">
<INPUT type="hidden" name="step" size="15" value="">
</FORM>

</BODY>
</HTML>

<SCRIPT language='javascript'>
// 검색 field 초기화
function empty(){
	document.sForm.sWord.value="";
}
</SCRIPT>