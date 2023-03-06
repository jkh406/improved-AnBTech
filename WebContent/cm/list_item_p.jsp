<%@ include file="../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.cm.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%!
	PartInfoTable table;
	CodeLinkUrl redirect;
%>
<%
	//품목분류 가져오기
	table = (PartInfoTable)request.getAttribute("ITEM_CLASS");

	String code_b_s		= table.getCodeBig();
	String code_m_s		= table.getCodeMid();
	String code_s_s		= table.getCodeSmall();
	
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("LIST_ITEM");
	table = new PartInfoTable();
	Iterator table_iter = table_list.iterator();

	//링크 문자열 가져오기
	redirect = new CodeLinkUrl();
	redirect = (CodeLinkUrl)request.getAttribute("Redirect");
	
	String view_pagecut = redirect.getViewPagecut();
	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();

%>


<HTML><HEAD><TITLE>품목검색</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../cm/css/style.css" rel=stylesheet>
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="98%" border='0' align='center'>
			<TBODY>
			<TR><TD align="left" width="600" height="32">
				   <form method='get' action='../servlet/CodeMgrServlet' name='srForm' onSubmit="if(this.searchword.value.length< 2){alert('검색어는 2자이상 입력하셔야 합니다.');this.searchword.value='';this.searchword.focus();return false;}else{return true;}" style="margin:0">
					<select name="B" onChange="javascript:changeClass('B');">
						<option value="">대분류 선택</option><%=code_b_s%>
					</select>
					<select name="M" onChange="javascript:changeClass('M');">
						<option value="">중분류 선택</option><%=code_m_s%>
					</select>
					<select name="S" onChange="javascript:changeClass('S');">
						<option value="">소분류 선택</option><%=code_s_s%>
					</select></TD>
				 <TD align=right width='250'><img src="../cm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../cm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../cm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR>
			<TR><TD height='1' bgcolor='#9DA8BA' colspan='3'></TD></TR>
			<TR><TD width='600'>
			   <table cellSpacing=0 cellPadding=0 width="100%" border='0'>
				<tr><td align="left" width="200">
					<select name='searchscope' onChange="selSearchScope();">
					  <option value='item_no'>품목코드</option>
					  <option value='item_desc'>품목설명</option>
					  <option value='register_info'>등록자</option>
					  <option value='register_date'>등록일자</option></select>&nbsp;</td>
				<td width="100%">
					<div id="register_date" class="collapsed" style="position:relative;">
					  <input type="text" name="s_date" size="8" maxlength="8"> ~ <input type="text" name="e_date" size="8" maxlength="8"> <a href="javascript:checkForm();"><img src="../mr/images/bt_search3.gif" border="0" align="absmiddle"></a>(예:20040210~20040225)</div>
					<div id="register_etc" class="expanded" style="position:relative;">
					   <input type="text" name="searchword" size="10" onFocus="document.srForm.searchword.value==''">
					   <input type="image" onfocus=blur() src="../mr/images/bt_search3.gif" border="0" align="absmiddle"></a></div></td></tr></table></TD><%=input_hidden_search%></form>
				 <TD height="32" align='right' width='250'><%=view_pagecut%></TD></TR></TBODY></TABLE></TD></TR>
  
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 align='center'>
        <TBODY>
		    <TR><TD height='2' bgcolor='#9CA9BA' colspan='11'></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=300 align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=180 align=middle class='list_title'>등록자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>등록일자</TD>

			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (PartInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getItemNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRegisterInfo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRegisterDate()%></td>
			  <TD><IMG height=1 width=1></TD>
			 
			</TR>
			<TR><TD colSpan=11 background="../cm/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE></TD></TR>		
</TABLE>
</body>
</html>


<script language='javascript'>

function search_detail() {
	var url = "CodeMgrServlet?mode=search_by_spec";
	wopen(url,'search_detail','820','450','scrollbars=yes,toolbar=no,status=no,resizable=yes');
}

function returnValue(item_code,item_desc,item_name,item_unit,item_type) {
	
	parent.document.forms[0].item_code.value = item_code;
	parent.document.forms[0].item_name.value = item_name;
	parent.document.forms[0].item_desc.value = item_desc;
	parent.document.forms[0].item_unit.value = item_unit;
	parent.document.forms[0].item_type.value = item_type;
	
	parent.return_value();
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//분류변경 처리
function changeClass(field){ 
	var f = document.srForm;

	var code_b = "";
	var code_m = "";
	var code_s = "";

	for(var i=0; i<f.B.options.length; i++){
		if(f.B.options[i].selected)
			code_b = f.B.options[i].value;
	}

	for(var i=0; i<f.M.options.length; i++){
		if(f.M.options[i].selected)
			code_m = f.M.options[i].value;
	}

	for(var i=0; i<f.S.options.length; i++){
		if(f.S.options[i].selected)
			code_s = f.S.options[i].value;
	}

	var category = code_b + code_m + code_s;

	if(field == 'M') code_s = '';

	if(code_b.length > 0) category = code_b;
	if(code_m.length > 0) category = code_m;
	if(code_s.length > 0) category = code_s;

	location.href = "../servlet/CodeMgrServlet?mode=list_item_p&category="+category+"&&code_big="+code_b+"&code_mid="+code_m+"&code_small="+code_s;

}

//검색 체크
function checkForm(){
	var f = document.srForm;
    
	if(f.searchscope.value == 'register_date'){
		f.searchword.value = f.s_date.value + f.e_date.value;

		if(f.searchword.value.length != 16){
			alert("검색일자를 올바르게 입력하십시오.");
			return;
		}
	}else{
		if(f.searchword.value.length < 2){
			alert("검색어를 2자 이상 입력하십시오.");
			f.searchword.focus();
			return;
		}	
	}	
	f.submit();
}

//검색필드 선택 처리
function selSearchScope(){
	var f = document.srForm;
    
	if(f.searchscope.value == 'register_date'){
		show('register_date');
		hide('register_etc');
		//hide('field');
	
	}else{
		hide('register_date');
		show('register_etc');
		//hide('field');
	}
}

// 선택된 레이어를 숨김
function hide( menuname )
{
  if (navigator.appName =="Netscape" ) {
	  document.layers[menuname].visibility="hide";
  } else {
	  document.all[menuname].className="collapsed"
   }
}

// 선택된 레이이를 보여줌
function show( menuname )
{
  if (navigator.appName =="Netscape" ) {
	   document.layers[menuname].visibility="show";
  } else {
	   
	   document.all[menuname].className="expanded"
  }
}

/* 입력값이 특정 문자(chars)만으로 되어있는지 체크 */
function containsCharsOnly(input,chars) {
	for (var inx = 0; inx < input.value.length; inx++) {
	   if (chars.indexOf(input.value.charAt(inx)) == -1)
		   return false;
	}
	return true;
}
		
/* 입력값에 숫자만 있는지 체크 */
function isNumber(input) {
	var chars = "0123456789";
	return containsCharsOnly(input,chars);
}
</script>