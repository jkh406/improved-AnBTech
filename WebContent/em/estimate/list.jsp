<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.em.entity.*"
%>
<%!
	EstimateInfoTable estimate;
	EmLinkUrl redirect;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
%>

<%
	
	String mode = request.getParameter("mode");
	//리스트 가져오기
	ArrayList estimate_list = new ArrayList();
	estimate_list = (ArrayList)request.getAttribute("Estimate_List");
	estimate = new EstimateInfoTable();
	Iterator estimate_iter = estimate_list.iterator();

	//링크 문자열 가져오기
	redirect = new EmLinkUrl();
	redirect = (EmLinkUrl)request.getAttribute("Redirect");
	
	String view_pagecut = redirect.getViewPagecut();
	String link_write = "";
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();

%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../em/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../em/images/blet.gif" align="absmiddle"> 견적서 검색</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../em/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../em/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../em/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				  <form method='get' action='../servlet/EstimateMgrServlet' name='srForm' style="margin:0">
					  <select name="searchscope" onChange="sel_scope();">
						<option value='company_name'>견적고객사 이름</option>
						<option value='charge_name'>견적고객 이름</option>
						<option value='estimate_subj'>견적서 제목</option>
						<option value='estimate_no'>견적 번호</option>
						<option value='writer'>작성자명</option>
					  </select>
					  <input type='text' name='searchword' size='15'>
					  <input type="image" onfocus=blur() src="../em/images/bt_search3.gif" border="0" align="absmiddle">
			<%	if(mode.equals("list")){ %>
					  <a href="javascript:search_detail();"><img src="../em/images/bt_search_d.gif" border="0" align="absmiddle"></a>
			<%	}	%>
					  <%=input_hidden_search%></form>
					  </TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>견적제목</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>벼젼</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>견적회사명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>견적가합계(원)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>작성일자</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	int no = 1;
	while(estimate_iter.hasNext()){
		estimate = (EstimateInfoTable)estimate_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=estimate.getEstimateSubj()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=estimate.getVersion()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=estimate.getCompanyName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(estimate.getTotalAmount(),"")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=estimate.getWrittenDay()%></td>
			</TR>
			<TR><TD colSpan=11 background="../em/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>
<script language='javascript'>

	function wopen(url, t, w, h,st) {
		var sw;
		var sh;
		sw = (screen.Width - w) / 2;
		sh = (screen.Height - h) / 2 - 50;

		window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
	}

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

		if(scope == 'company_name' || scope == 'estimate_subj' || scope == 'estimate_no' || scope == 'charge_name'){
			f.searchword.value = '';
			f.searchword.focus();
		}else if(scope == 'estimate_type'){
			alert("estimate_type");
		}else if(scope == 'publish_type'){
			alert("publish_type");
		}else if(scope == 'written_day'){
			alert("written_day");
		}else if(scope == 'register_id'){
			var to = "srForm.searchword";
			window.open("../admin/UserTreeMainForSingle.jsp?target="+to+"&type=single","user","width=300,height=400,scrollbar=yes,toolbar=no,status=no,resizable=no");
		}else if(scope == 'estimate_item'){
			alert("estimate_item");
		}
	}	

	
	//상세검색
	function search_detail() {

		wopen("../em/estimate/search.jsp",'search_detail','350','298','scrollbars=no,toolbar=no,status=no,resizable=no');
/*
		
		var sParam = "src=search.jsp&frmWidth=600&frmHeight=500&title=search_detail";

		var sRtnValue=showModalDialog("../em/estimate/search_detail.jsp?"+sParam,"search","dialogWidth:600px;dialogHeight:500px;toolbar=0;loemtion=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

		if (typeof sRtnValue != "undefined" && sRtnValue != "")
		{
			sParam = "&category=&searchscope=detail&searchword="+sRtnValue;
			location.href = "../servlet/EstimateMgrServlet?mode=<%=mode%>" + sParam;
		}
*/
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