<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
    contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.qc.entity.*"
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
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display()'>

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../qc/images/blet.gif" align="absmiddle"> 재검사대상</TD>
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
				  </select></TD>
			  <TD align='left' width='90'>&nbsp;<input type=text name='searchword' size='10'></TD>
			  <TD align='left' width='30'><a href="javascript:checkForm();"><img src="../qc/images/bt_search3.gif" border="0"></a><%=input_hidden_search%></form></TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:418; overflow-x:auto; overflow-y:auto;">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0><form name='listForm' style='magrgin:0'>
        <TBODY>
			<TR vAlign=middle height=22>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=90 align=middle class='list_title'>검사의뢰번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>재검수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=180 align=middle class='list_title'>공급업체명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>검사의뢰자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>검사의뢰일자</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (InspectionMasterTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRequestNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD class='list_bg' align=left style='padding-left:10px;'><%=table.getItemDesc()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getLotQuantity()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getSupplyerName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRequesterInfo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRequestDate()%></td>
			</TR>
			<TR><TD colSpan=15 background="../qc/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE></DIV></form></TD></TR></TBODY></TABLE>
</body>
</html>

<script language='javascript'>

	function checkForm(){
		var f = document.srForm;

		if(f.searchword.value.length< 2){
			alert('검색어는 2자이상 입력하셔야 합니다.');
			f.searchword.value='';
			f.searchword.focus();
			return;
		}


		var fromField=f.searchword.value.split("/")
		var register_id =  fromField[0];
		var register_name = fromField[1];
		f.searchword.value = register_id;

		f.submit();

	}
	//검색 항목 변경
	function sel_scope() {
		var f = document.srForm;
		var scope = f.searchscope.value;

		if(scope == 'company_name' || scope == 'company_subj' || scope == 'company_no' || scope == 'charge_name'){
			f.searchword.value = '';
			f.searchword.focus();
		}else if(scope == 'company_type'){
			alert("company_type");
		}else if(scope == 'publish_type'){
			alert("publish_type");
		}else if(scope == 'written_day'){
			alert("written_day");
		}else if(scope == 'register_id'){
			var to = "srForm.searchword";
			window.open("../admin/UserTreeMainForSingle.jsp?target="+to+"&type=single","user","width=300,height=400,scrollbar=yes,toolbar=no,status=no,resizable=no");
		}else if(scope == 'company_item'){
			alert("company_item");
		}
	}	

	
	//상세검색
	function search_detail() {

		var sParam = "src=search.jsp&frmWidth=600&frmHeight=500&title=search_detail";

		var sRtnValue=showModalDialog("../em/customer/search_detail.jsp?"+sParam,"search","dialogWidth:600px;dialogHeight:500px;toolbar=0;loemtion=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

		if (typeof sRtnValue != "undefined" && sRtnValue != "")
		{
			sParam = "&category=&searchscope=detail&searchword="+sRtnValue;
			location.href = "../servlet/EstimateMgrServlet?mode=<%=mode%>" + sParam;
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