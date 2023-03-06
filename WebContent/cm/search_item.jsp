<%@ include file="../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.cm.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>

<%!
	PartInfoTable part;
	SpecCodeTable spec;
%>

<%
	String mode = request.getParameter("mode");				// 모드

	//선택된 품목 대한 정보 가져오기
	part = (PartInfoTable)request.getAttribute("PART_INFO");

	String code_b_s		= part.getCodeBig();
	String code_m_s		= part.getCodeMid();
	String code_s_s		= part.getCodeSmall();
	String item_no		= part.getItemNo();
	String item_desc	= part.getItemDesc();
	String mfg_no		= part.getMfgNo();

	//해당분류에 정의된 스펙리스트 가져오기
//	spec = new SpecCodeTable();
	ArrayList spec_list = new ArrayList();
	spec_list = (ArrayList)request.getAttribute("SPEC_LIST");
	Iterator spec_iter = spec_list.iterator();
%>

<html>
<link rel="stylesheet" type="text/css" href="../cm/css/style.css">
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
<head>

<script language="Javascript">
function checkForm()
{
	var f = document.forms[0];

	//결과문자열 만들기
	var spec_str = "";
	var code_item = f.code_str.value.split(",");
	for(var i=0; i<code_item.length-1; i++){
		if(eval("f.v_"+code_item[i]+".value;") != '')
			spec_str += code_item[i] + "|" + eval("f.v_"+code_item[i]+".value;") + "|" + eval("f.u_"+code_item[i]+".value;") + ",";
	}
	
	var code_b		= f.code_big.value;
	var code_m		= f.code_mid.value;
	var code_s		= f.code_small.value;
	var category	= "";

	if(code_b.length > 0) category = code_b;
	if(code_m.length > 0) category = code_m;
	if(code_s.length > 0) category = code_s;

	opener.location.href = "CodeMgrServlet?mode=list_item&category="+category+"&code_big="+code_b+"&code_mid="+code_m+"&code_small="+code_s+"&searchscope=spec&searchword=" + spec_str;

	self.close();
}

var select_obj;
	
function ANB_layerAction(obj,status) { 

	var _tmpx,_tmpy, marginx, marginy;
		_tmpx = event.clientX + parseInt(obj.offsetWidth);
		_tmpy = event.clientY + parseInt(obj.offsetHeight);
		_marginx = document.body.clientWidth - _tmpx;
		_marginy = document.body.clientHeight - _tmpy ;
	if(_marginx < 0)
		_tmpx = event.clientX + document.body.scrollLeft + _marginx ;
	else
		_tmpx = event.clientX + document.body.scrollLeft ;
	if(_marginy < 0)
		_tmpy = event.clientY + document.body.scrollTop + _marginy +20;
	else
		_tmpy = event.clientY + document.body.scrollTop ;

	obj.style.posLeft=_tmpx-13;
	obj.style.posTop=_tmpy+20;

	if(status=='visible') {
		if(select_obj) {
			select_obj.style.visibility='hidden';
			select_obj=null;
		}
		select_obj=obj;
	}else{
		select_obj=null;
	}
	obj.style.visibility=status; 
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
</script>

<title>품목상세검색</title>
</head>

<body topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="reg_item" method="post" action="CodeMgrServlet" enctype="multipart/form-data">

<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center"><!--타이틀-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../cm/images/pop_item_dsearch.gif" border='0' align='absmiddle' alt='품목상세검색'></TD></TR>
				<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
	<TR><TD height=35><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0 border=0>
			<TBODY>
				<TR><TD align=left style='padding-left:12px'>
					<SELECT name="code_big" onChange="javascript:changeClass('B');">
						<OPTION value="">대분류 선택</OPTION><%=code_b_s%>
					</SELECT>
					<SELECT name="code_mid" onChange="javascript:changeClass('M');">
						<OPTION value="">중분류 선택</OPTION><%=code_m_s%>
					</SELECT>
					<SELECT name="code_small" onChange="javascript:changeClass('S');">
						<OPTION value="">소분류 선택</OPTION><%=code_s_s%>
					</SELECT>
					<IMG src='../cm/images/bt_search.gif' value='저장' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
					<IMG src='../cm/images/bt_cancel.gif' value='취소' onClick='javascript:self.close();' style='cursor:hand' align='absmiddle'>
				</TD></TR></TBODY></TABLE></TD></TR>
				
	<TR><TD height='394'>
	<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
		<TABLE  cellSpacing=0 cellPadding=0 width="98%" border=0 align='center'>
			<tbody>
				<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
         		<TR height=23>  <TD noWrap width=60 align=middle class='list_title'>항목코드</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
					  <TD noWrap width=200 align=middle class='list_title'>항목명</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
					  <TD noWrap width=150 align=middle class='list_title'>항목값</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
					  <TD noWrap width=80 align=middle class='list_title'>항목단위</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
					  <TD noWrap width=100% align=middle class='list_title'></TD>
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<%
		String code_str = "";		// 정의된 스펙코드들
		while(spec_iter.hasNext()){
			spec = (SpecCodeTable)spec_iter.next();
			code_str += spec.getSpecCode() + ",";
%>

				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					<TD align=middle height="24" class='list_bg'><%=spec.getSpecCode()%></td>
					<TD><IMG height=1 width=1></TD>
					<TD align=left class='list_bg' style='padding-left:5px'><%=spec.getSpecName()%></td>
					<TD><IMG height=1 width=1></TD>
					<TD align=left class='list_bg' style='padding-left:5px'><%=spec.getSpecValue()%></td>
					<TD><IMG height=1 width=1></TD>
					<TD align=left class='list_bg' style='padding-left:5px'><%=spec.getSpecUnit()%></td>
					<TD><IMG height=1 width=1></TD>
					<TD align=left class='list_bg' style='padding-left:5px'><%=spec.getSpecDesc()%></td>
				</TR>
				<TR><TD colSpan=9 background="../cm/images/dot_line.gif"></TD></TR>
<%			}
%>			
			</TBODY></TABLE></DIV></TD></TR>

			<!--꼬릿말-->
			<TR><TD>
				<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
				<TBODY>
					<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
						<a href='javascript:self.close()'><img src='../cm/images/bt_close.gif' border='0' align='absmiddle'></a></TD>
					</TR>
					<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
	        </TBODY></TABLE></TD></TR>

<INPUT type='hidden' name='mode' value='<%=mode%>'>
<INPUT type='hidden' name='code_str' value='<%=code_str%>'>
</TD></TR></FORM>
</TABLE>
</BODY>
</HTML>


<script language=javascript>
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//분류변경 처리
function changeClass(field){ 
	var f = document.forms[0];
	var code_b = f.code_big.value;
	var code_m = f.code_mid.value;
	var code_s = f.code_small.value;

	if(field == 'M') code_s = '';

	location.href = "../servlet/CodeMgrServlet?mode=search_by_spec&code_big="+code_b+"&code_mid="+code_m+"&code_small="+code_s;
}
</script>