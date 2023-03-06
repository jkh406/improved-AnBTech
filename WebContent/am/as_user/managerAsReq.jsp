<%@ include file="../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage = "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.am.entity.*,com.anbtech.util.normalFormat,java.text.NumberFormat"%>
<%!
	com.anbtech.am.entity.AsInfoTable asinfotable;
	com.anbtech.am.entity.AMLinkTable amLinkTable;
%>

<%
	String sb=(String)request.getAttribute("CategoryList");
	String c_no=request.getParameter("c_no")==null?"0":request.getParameter("c_no");
	String login=request.getParameter("login_id")==null?"":request.getParameter("login_id");
	
	//java.anbtech.util.normalFormat app = new java.anbtech.util.normalFormat("0,000");
	NumberFormat nf = NumberFormat.getInstance();

	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("assetList");
	asinfotable = new com.anbtech.am.entity.AsInfoTable();
	Iterator table_iter = table_list.iterator();

	//링크 문자열 가져오기
	amLinkTable = new AMLinkTable();
	amLinkTable = (AMLinkTable)request.getAttribute("Redirect");
	String view_total = ""+amLinkTable.getViewTotal();
	String view_boardpage = amLinkTable.getViewBoardpage();
	String view_totalpage = ""+amLinkTable.getViewtotalpage();
	String view_pagecut = amLinkTable.getViewPagecut();
	String category_full = amLinkTable.getWhereCategory();
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../am/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../am/images/blet.gif" align="absmiddle"> 자산검색</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../am/images/setup_total.gif" border="0" align="absmiddle"><%=view_total%> <img src="../am/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../am/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top><!--버튼 및 페이징-->
		<form method="get" name="asForm" style="margin:0" action='../servlet/AssetServlet' 
				  onSubmit="if( !document.asForm.searchscope.value=='' && document.asForm.searchword.value=='') {
								alert('검색어를 기입해 주십시요');
								return false;
							} else if(document.asForm.searchscope.value=='' && !document.asForm.searchword.value==''){
								alert('검색 항목을 선택해 주십시요');
								return false;
							} else return true">
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY> 
			
			<TR><TD align=left width='700'>
				&nbsp;
				<SELECT name="c_no">
					<OPTION value=''>전체</OPTION>
					<%=sb%>
				<%	if(!c_no.equals("")){	%>
						<script language='javascript'>
							document.asForm.c_no.value = "<%=c_no%>";
						</script>
				<%	}	%>
				</SELECT>
				<SELECT name='searchscope'>
					<OPTION value="" selected>검색항목</OPTION>
					<OPTION value="as_mid">자산번호</OPTION>
					<OPTION value="model_name">모델명</OPTION>
					<OPTION value="as_serial">고유번호</OPTION>
					<OPTION value="crr_name">사용자</OPTION>
					<OPTION value="crr_rank">부서별</OPTION>
					<OPTION value="as_mid">카테고리약식코드</OPTION>
				</SELECT>
				<input type='text' size=10 name='searchword'>
				<input type="image" onfocus=blur() src="../am/images/bt_search3.gif" border="0" align="absmiddle" >
				<a href='javascript:search_detail()'><IMG src="../am/images/bt_search_d.gif" border='0' align="absmiddle"></a>
				</TD>
				<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR>
				<input type='hidden' name=mode value='user_asset_list'>
				<input type='hidden' name=div  value='general'>
	</form></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--리스트-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=2><TD colspan=13></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=100 align=middle class='list_title'>품명</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>자산번호</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>모델명</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>현사용자</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>구입일자</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>자산가치</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=70 align=middle class='list_title'>비고</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
		<%
				  int     as_no=0;			// 관리번호
				  String  as_mid="";		// 자산번호
				  String  ct_id="";			//카테고리ID
				  //String  c_no="";
				  String  as_item_no="";	//자산수량no
				  String  w_id="";
				  String  w_name="";
				  String  w_rank="";
				  String  b_id="";			//자산품구매자ID
				  String  b_name="";		//자산품구매자이름
				  String  b_rank="";		//자산품부서명
				  String  model_name="";	//모델명
				  String  as_name="";		//자산품목이름
				  String  as_serial="";		//자산품시리얼
				  String  buy_date="";		//구매일자
				  String  as_price="";		//품목 가격
				  String  dc_date="";		//감가제한년도
				  String  dc_bound="";		//감가제한횟수
				  String  as_each_dc="";	//품목 감가 비율
				  String  as_value="";		//자산가격
				  String  crr_id="";		//현재 사용자 ID
				  String  crr_name="";		//현재 사용자 이름
				  String  crr_rank="";		//현재 사용자 부서
				  String  buy_where="";		//구매지(회사)
				  String  as_maker="";		//자산품만든곳(회사)
				  String  as_setting="";	//규격(사양)
				  String  bw_tel="";		//구매회사연락처
				  String  bw_address="";	//구매회사주소
				  String  bw_employee="";	//구매회사담당자
				  String  bw_mgr_tel="";	//구매회사담당자연락처
				  String  etc="";			//기타
				  String  as_status="";		//자산상태(폐기,사용중,감각비해제)
				  String  as_status_name="";
				  String  as_except_day="";	//자산 폐기 일자
				  String  as_except_reason="";// 자산 폐기 이유
				  String  handle ="";
				  String  now_status ="";
				  String  u_id = "";
				  String  u_name = "";

			while(table_iter.hasNext()){
				asinfotable = (com.anbtech.am.entity.AsInfoTable)table_iter.next();

					as_no		= asinfotable.getAsNo();			
					as_mid		= asinfotable.getAsMid();	
					c_no		= asinfotable.getCno();
					as_item_no	= asinfotable.getAsItemNo();	
					model_name	= asinfotable.getModelName();	
					as_name		= asinfotable.getAsName();		
					as_serial	= asinfotable.getAsSerial();		
					buy_date	= asinfotable.getBuyDate();		
					//as_price	= asinfotable.getAsPrice();		
					//dc_date	= asinfotable.getDcDate();		
					dc_bound	= asinfotable.getDcBound();		
					as_each_dc	= asinfotable.getAsEachDc();	
					as_value	= asinfotable.getAsValue();	
					crr_id		= asinfotable.getCrrId();
					crr_name	= asinfotable.getCrrName();		
					u_id		= asinfotable.getUid();
					u_name		= asinfotable.getUname();
					//crr_rank	= asinfotable.getCrrRank();		
					buy_where	= asinfotable.getBuyWhere();		
					as_maker	= asinfotable.getAsMaker();		
					//as_setting= asinfotable.getAsSetting();	
					bw_tel		= asinfotable.getBwTel();		
					bw_address	= asinfotable.getBwAddress();	
					bw_employee	= asinfotable.getBwEmployee();	
					bw_mgr_tel	= asinfotable.getBwMgrTel();	
					//etc		= asinfotable.getEtc();			
					as_status	= asinfotable.getAsStatus();		
					as_status_name=asinfotable.getAsStatusName();
					handle		= asinfotable.getHandle();
					now_status	= asinfotable.getNowStatus();
					
					if(now_status==null || now_status.equals("")) now_status="사용가능";
					if(as_status.equals("13")) now_status = as_status_name;
		%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg' height="24"><%=as_name%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=as_mid%>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=model_name%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=u_name%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=buy_date.substring(0,4)%>-<%=buy_date.substring(4,6)%>-<%=buy_date.substring(6,8)%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=right class='list_bg'><%=nf.format(nf.parse(as_value))%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><a href="javascript:go_history('<%=as_no%>','<%=c_no%>')"><img src='../am/images/lt_view_d.gif' border='0' align='absmiddle'></a></td>
						<TD><IMG height=1 width=1></TD>
					</TR>
					<TR><TD colspan=13 background="../am/images/dot_line.gif"></TD></TR>
		<input type='hidden' name="c_no" value=<%=c_no%>>
		<%
			}
		%>
				</TBODY></TABLE></TD></TR>

</TBODY></TABLE>
</body>
</html>

<script language='javascript'>

	var f = document.asForm;

	//상세보기
	function go_asInfo(as_no,c_no){
		location.href ="../servlet/AssetServlet?mode=asset_form&div=view&as_no="+as_no+"&c_no="+c_no;
	}

	// 이력보기
	function go_history(as_no,c_no){
		location.href ="../servlet/AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no;
	}

	// 반출/이관/대여 Form으로 이동
	function go_form(as_no,status){
		location.href = "../servlet/AssetServlet?mode=user_moving_req&as_no="+as_no+"&o_status="+status;
	}

	function wopen(url, t, w, h,st) {
		var sw;
		var sh;
		sw = (screen.Width - w) / 2;
		sh = (screen.Height - h) / 2 - 50;

		window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
	}

	//상세검색
	function search_detail() {
				
	var c_no = f.c_no.value;
		wopen("../am/as_user/search.jsp?category_full=<%=category_full%>&c_no="+c_no,'search','515','285','scrollbars=no,toolbar=no,status=no,resizable=no');
	}
</script>

