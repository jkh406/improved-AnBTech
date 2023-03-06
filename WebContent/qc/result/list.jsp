<%@ include file="../../admin/configHead.jsp"%>
<%@ page
	language="java"
	contentType="text/html;charset=euc-kr"
	errorPage="../../admin/errorpage.jsp"
	import="java.util.*,com.anbtech.qc.entity.*"
%>
<%!
	InspectionMasterTable table;
	QualityCtrlLinkUrl redirect;
%>

<%
	String mode = request.getParameter("mode");

	//리스트 가져오기
	ArrayList list = new ArrayList();
	list = (ArrayList)request.getAttribute("INSPECT_LIST");
	table = new InspectionMasterTable();
	Iterator table_iter = list.iterator();

	//링크 문자열 가져오기
	redirect = new QualityCtrlLinkUrl();
	redirect = (QualityCtrlLinkUrl)request.getAttribute("Redirect");
	
	String view_pagecut = redirect.getViewPagecut();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();

%>


<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../qc/css/style.css" rel=stylesheet>
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onLoad="display();">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../qc/images/blet.gif" align="absmiddle"> 검사결과현황</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../qc/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../qc/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../qc/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='70'><form method='get' action='../servlet/QualityCtrlServlet' name='srForm' style="margin:0">
				  <select name="searchscope" onChange="sel_scope();">
						<option value='request_no'>검사의뢰번호</option>
						<option value='item_code'>품목코드</option>
						<option value='inspection_result'>판정결과</option>
				  </select>&nbsp;</TD>
			  <TD align='left' width='150'>
					<div id="result_type" class="collapsed" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td>
						    <select name='inspection_result'>
								<option value='PASS'>합격</option>
								<option value='PASS2'>특채</option>
								<option value='REWORK'>재작업</option>
								<option value='RETURN'>반품</option>
								<option value='FAIL'>폐기</option></select> <a href="javascript:checkForm();"><img src="../mr/images/bt_search3.gif" border="0" align="absmiddle"></a></td></tr></table></div>
					<div id="sword" class="expanded" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td>
						   <input type="text" name="searchword" size="10" onFocus="document.srForm.searchword.value==''">
						   <a href="javascript:checkForm();"><img src="../mr/images/bt_search3.gif" border="0" align="absmiddle"></a></td></tr></table></div>			  
			  <%=input_hidden_search%></form></TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:418; overflow-x:auto; overflow-y:auto;">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0><form name='listForm' style='magrgin:0'>
        <TBODY>
			<TR vAlign=middle height=22>
			  <TD noWrap width=100 align=middle class='list_title'>검사의뢰번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>판정결과</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>검사대상수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>검사수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>불량수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>불량율(%)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=180 align=middle class='list_title'>공급업체명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>검사의뢰자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>검사의뢰일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>검사자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>검사일자</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=25></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (InspectionMasterTable)table_iter.next();
		String inspection_result = table.getInspectionResult();
		if(inspection_result.equals("PASS")) inspection_result = "합격";
		else if(inspection_result.equals("PASS2")) inspection_result = "특채";
		else if(inspection_result.equals("REWORK")) inspection_result = "재작업";
		else if(inspection_result.equals("FAIL")) inspection_result = "폐기";
		else if(inspection_result.equals("RETURN")) inspection_result = "반품";
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=table.getRequestNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=inspection_result%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD class='list_bg' align=left style='padding-left:10px;'><%=table.getItemDesc()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getLotQuantity()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getInspectedQuantity()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getBadItemQuantity()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getBadPercentage()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getSupplyerName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRequesterInfo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRequestDate()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getInspectorInfo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getInspectDate()%></td>
			</TR>
			<TR><TD colSpan=25 background="../qc/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></DIV></form></TD></TR></TBODY></TABLE>
</body>
</html>

<script language='javascript'>

	function checkForm(){
		var f = document.srForm;

		if(f.searchscope.value == 'inspection_result'){
			f.searchword.value = f.inspection_result.value;
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
	function sel_scope(){
		var f = document.srForm;
		
		if(f.searchscope.value == 'inspection_result'){
			show('result_type');
			hide('sword');
		}else{
			hide('result_type');
			show('sword');
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

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//var div_h = h - 353;
	var div_h = c_h - 62;
	item_list.style.height = div_h;

}
</script>