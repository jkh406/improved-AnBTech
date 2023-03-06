<%@ include file="../admin/configHead.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.ca.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%!
	CaMasterTable table;
	CaLinkUrl redirect;
%>
<%
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("CA_List");
	table = new CaMasterTable();
	Iterator table_iter = table_list.iterator();

	//링크 문자열 가져오기
	redirect = new CaLinkUrl();
	redirect = (CaLinkUrl)request.getAttribute("Redirect");
	
	String view_pagecut = redirect.getViewPagecut();
	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();

%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../ca/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ca/images/blet.gif" align="absmiddle"> 승인원검색</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../ca/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../ca/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../ca/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				  <form method=get action='../servlet/ComponentApprovalServlet' name=srForm onSubmit="if(this.searchword.value.length< 2){alert('검색어는 2자이상 입력하셔야 합니다.');this.searchword.value='';this.searchword.focus();return false;}else{return true;}" style="margin:0">
					  <select name=searchscope onChange="">
						  <option value='approval_no'>승인번호</option>
						  <option value='item_no'>부품번호</option>
						  <option value='maker_name'>승인업체명</option>
						  <option value='maker_part_no'>업체부품번호</option>
						  <option value='approver_info'>승인자 이름</option>
						  <option value='requestor_info'>의뢰자 이름</option>
					  </select>
					  <input type='text' name='searchword' size='10'>
					  <input type="image" onfocus=blur() src="../ca/images/bt_search3.gif" border="0" align="absmiddle">
					  <a href="javascript:search_detail();"><img src="../ca/images/bt_search_d.gif" border="0" align="absmiddle"></a>
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
			  <TD noWrap width=100 align=middle class='list_title'>승인번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>승인업체명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>품목번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=130 align=middle class='list_title'>업체부품번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>승인일자</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (CaMasterTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=table.getApprovalNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getMakerName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getItemNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getMakerPartNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getApproveDate()%></td>
			</TR>
			<TR><TD colSpan=11 background="../ca/images/dot_line.gif"></TD></TR>
<%
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

	//상세검색
	function search_detail() {

		var category = document.srForm.category.value;
		var mode = document.srForm.mode.value;
		wopen("../ca/search.jsp?category="+category+"&mode="+mode,'search_detail','350','377','scrollbars=no,toolbar=no,status=no,resizable=no');
/*
		var sParam = "src=search.jsp&frmWidth=600&frmHeight=500&title=search_detail";
		var category = document.srForm.category.value;
		var mode = document.srForm.mode.value;

		var sRtnValue=showModalDialog("../ca/modalFrm.jsp?"+sParam,"search","dialogWidth:600px;dialogHeight:500px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

		if (typeof sRtnValue != "undefined" && sRtnValue != "")
		{
			sParam = "&category="+category+"&searchscope=detail&searchword="+sRtnValue;
			location.href = "../servlet/ComponentApprovalServlet?mode="+mode + sParam;
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