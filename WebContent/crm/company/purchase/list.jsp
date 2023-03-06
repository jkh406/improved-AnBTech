<%@ include file= "../../../admin/configHead.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.crm.entity.*"
%>
<%!
	CompanyInfoTable company;
	CrmLinkUrl redirect;
%>

<%
	String mode = request.getParameter("mode");
	//리스트 가져오기
	ArrayList company_list = new ArrayList();
	company_list = (ArrayList)request.getAttribute("CompanyList");
	company = new CompanyInfoTable();
	Iterator company_iter = company_list.iterator();

	//링크 문자열 가져오기
	redirect = new CrmLinkUrl();
	redirect = (CrmLinkUrl)request.getAttribute("Redirect");
	
	String view_pagecut = redirect.getViewPagecut();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();

%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../crm/css/style.css" rel=stylesheet>
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../crm/images/blet.gif" align="absmiddle"> 공급업체 등록현황</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../crm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../crm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../crm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='70'><form method='get' action='../servlet/CrmServlet' name='srForm' style="margin:0">
				  <select name="searchscope" onChange="sel_scope();">
						<option value='company_name'>공급업체명</option>
						<option value='chief_name'>대표자명</option>
						<option value='business_type'>고객유형</option>
						<option value='business_item'>사업종목</option>
				  </select>&nbsp;</TD>
			  <TD align=left width='70%'>
					<div id="scope1" class="collapsed" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'>
						  <tr><td>
						    <select name="type">
								<option value='매출고객'>매출고객</option>
								<option value='협력업체'>협력업체</option>
								<option value='공통'>공통</option>
								<option value='기타'>기타</option>
							</select></td><td width='100%'>&nbsp;<a href="javascript:checkForm();"><img src="../crm/images/bt_search3.gif" border="0"></a> <a href="../servlet/CrmServlet?mode=company_write&module=purchase"><img src="../crm/images/bt_add_new2.gif" border="0"></a></td></tr></table></div>
					<div id="scope2" class="collapsed" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td width='400'>
		<%
			//종목이 변경될 경우 배열 item_list[] 인자만 변경하면 됨.
			//다른 부분은 수정될 필요 없음.
			String[] item_list = {"시스템","단말기","DRYER","개발","원자재","자산","기타"};
			int sel_cnt = item_list.length;

			for(int i=0; i<sel_cnt; i++) {
				out.println("<input type='checkbox' name='item' value='"+item_list[i]+"'>"+item_list[i]);

			}
		%>							
							</td><td><a href="javascript:checkForm();"><img src="../crm/images/bt_search3.gif" border="0"></a> <a href="../servlet/CrmServlet?mode=company_write&module=basic"><img src="../crm/images/bt_add_new2.gif" border="0"></a></td></tr></table></div>
					<div id="scope3" class="expanded" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td>
						   <input type="text" name="searchword" size="10" onFocus="document.srForm.searchword.value==''"></td><td width='100%'>&nbsp;<a href="javascript:checkForm();"><img src="../crm/images/bt_search3.gif" border="0"></a> <a href="../servlet/CrmServlet?mode=company_write&module=purchase"><img src="../crm/images/bt_add_new2.gif" border="0"></a></td></tr></table></div>
				<%=input_hidden_search%></form></TD>
			  <TD width='200' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0><form name='listForm' style='magrgin:0'>
        <TBODY>
			<TR vAlign=middle height=22>
			  <TD noWrap width=50 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../crm/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>공급업체명1</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../crm/images/list_tep.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>공급업체명2</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../crm/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>대표전화번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../crm/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>대표팩스번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../crm/images/list_tep.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>홈페이주소</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	int no = 1;
	while(company_iter.hasNext()){
		company = (CompanyInfoTable)company_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=company.getNameKor()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=company.getNameEng()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=company.getMainTelNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=company.getMainFaxNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=company.getHomepageUrl()%></td>
			</TR>
			<TR><TD colSpan=11 background="../crm/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE></form></TD></TR></TBODY></TABLE>
</body>
</html>


<script language='javascript'>

	//검색체크
	function checkForm(){
		var f = document.srForm;

		if(f.searchscope.value == 'business_type'){
			f.searchword.value = f.type.value;

			if(f.searchword.value.length == ''){
				alert("고객유형을 선택하십시오.");
				return;
			}
		}else if(f.searchscope.value == 'business_item'){
			var items = "";
			var s_count = 0;
			for(i=0;i<f.item.length;i++){
				if(f.item[i].checked){
					items += f.item[i].value + ",";
					s_count ++;
				}
			}
			if(s_count == 0){
			   alert("종목을 한개 이상 선택하십시오.");
			   return;
			}
			
			f.searchword.value = items;

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
		
		if(f.searchscope.value == 'business_type'){
			show('scope1');
			hide('scope2');
			hide('scope3');
		}else if(f.searchscope.value == 'business_item'){
			hide('scope1');
			show('scope2');
			hide('scope3');
		}else{
			hide('scope1');
			hide('scope2');
			show('scope3');
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
</script>