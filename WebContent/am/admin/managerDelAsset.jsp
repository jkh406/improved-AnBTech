<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkAM02.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.am.entity.*"%>

<%
	com.anbtech.am.entity.AsInfoTable asinfotable;
	com.anbtech.am.entity.AMLinkTable amLinkTable;
%>

<%
	String sb=(String)request.getAttribute("CategoryList");							 //  카테고리 String 가져오기
	String c_no=request.getParameter("c_no")==null?"0":request.getParameter("c_no");
	
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("assetList");
	asinfotable = new com.anbtech.am.entity.AsInfoTable();
	Iterator table_iter = table_list.iterator();

	//-- 링크 문자열 가져오기 
	amLinkTable = new AMLinkTable();
	amLinkTable = (AMLinkTable)request.getAttribute("Redirect");
	String view_total = ""+amLinkTable.getViewTotal();
	String view_boardpage = amLinkTable.getViewBoardpage();
	String view_totalpage = ""+amLinkTable.getViewtotalpage();
	String view_pagecut = amLinkTable.getViewPagecut();
	String category_full = amLinkTable.getWhereCategory();

	int       as_no		=0;			// 관리번호
	String    as_mid	="";			// 자산번호
	String    ct_id		="";		//	카테고리ID
	//String    c_no		="";
	String    as_item_no="";			//	자산수량no
	String	model_name	="";		//	모델명
	String	as_name		="";		//	자산품목이름
	String	as_serial	="";		//	자산품시리얼
	String	buy_date	="";		//	구매일자
	String	as_price	="";		//	품목 가격
	String	dc_date		="";		//	감가제한년도
	String	dc_bound	="";		//	감가제한횟수
	String	as_each_dc	="";		//	품목 감가 비율
	String	as_value	="";		//	자산가격
	String	crr_id		="";		//	현재 사용자 ID
	String	crr_name	="";		//	현재 사용자 이름
	String	crr_rank	="";		//	현재 사용자 부서
	String	bw_tel		="";		//	구매회사연락처
	String	bw_address	="";		//	구매회사주소
	String	bw_employee	="";		//	구매회사담당자
	String	bw_mgr_tel	="";		//	구매회사담당자연락처
	String	etc			="";		//	기타
	String	as_status	="";		//	자산상태(폐기,사용중,감각비해제)
	String	as_except_day   ="";	//	자산 폐기 일자
	String	as_except_reason="";	// 자산 폐기 이유
	String	as_status_name  ="";	// 자산상태text
%>


<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../am/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=28>
		<TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../am/images/blet.gif" align="absmiddle"> 폐기자산관리</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../am/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../am/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../am/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top><!--버튼 및 페이징-->
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>  
			<form method=get action='AssetServlet' name=asForm 
					onSubmit="if( !document.asForm.searchscope.value=='' && document.asForm.searchword.value=='') {
								alert('검색어를 기입해 주십시요');
								return false;
							} else if(document.asForm.searchscope.value=='' && !document.asForm.searchword.value==''){
								alert('검색 항목을 선택해 주십시요');
								return false;
							} else return true">
				<TR><TD align=left width='100%' style='padding-left:5px'>
					<SELECT name="c_no"  >
					<OPTION value=''>전체</OPTION>
					<%=sb%>
				<%	if(!c_no.equals("")){	%>
						<script language='javascript'>
							document.asForm.c_no.value = "<%=c_no%>";
						</script>		
				<%	}	%>
				</SELECT>
				
				<SELECT name='searchscope' >
						<OPTION value="" selected>검색항목</OPTION>
						<OPTION value="as_mid">자산번호</OPTION>
						<OPTION value="model_name">모델명</OPTION>
						<OPTION value="as_serial">고유번호</OPTION>
						<OPTION value="crr_name">사용자</OPTION>
						<OPTION value="crr_rank">부서별</OPTION>
						<OPTION value="as_mid">카테고리약식코드</OPTION>
				</SELECT>
			
				<input type='text' size=10 name='searchword'> <input type="image" onfocus=blur() src="../am/images/bt_search3.gif" border="0" align="absmiddle">
				<!--<a href="javascript:search_detail()"><img src='../am/images/bt_search_d.gif' border='0' align='absmiddle'></a>-->
				<input type='hidden' name='mode' value='asset_del_list'>
				<input type='hidden' name='div' value='general'>
			</TD>
			<TD align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY></form></TABLE></TD></TR>

	<TR height=100%>
		<TD vAlign=top><!--리스트-->

	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR bgColor=#9DA9B9 height=2><TD colspan=13></TD></TR>
			<TR vAlign=middle height=23>
				<TD noWrap width=100 align=middle class='list_title'>품명</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>자산번호</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>모델명</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>마지막사용자</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>구입일자</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>폐기일자</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>비고</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	while(table_iter.hasNext()){
		   	asinfotable = (com.anbtech.am.entity.AsInfoTable)table_iter.next();

			as_no		=	asinfotable.getAsNo();			
		    as_mid		=	asinfotable.getAsMid();	
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
		    crr_name	= asinfotable.getCrrName();		
		    //crr_rank	= asinfotable.getCrrRank();		
		    bw_tel		= asinfotable.getBwTel();		
		    bw_address	= asinfotable.getBwAddress();	
		    bw_employee	= asinfotable.getBwEmployee();	
		    bw_mgr_tel	= asinfotable.getBwMgrTel();	
		    //etc= asinfotable.getEtc();			
		    as_status	= asinfotable.getAsStatus();
			as_status_name	= asinfotable.getAsStatusName();
			as_except_day	= asinfotable.getAsExceptDay();
			
			if(as_except_day==null || as_except_day.equals("")) {
				as_except_day="";
			} else as_except_day = as_except_day.substring(0,4)+"-"+as_except_day.substring(4,6)+"-"+as_except_day.substring(6,8);
		  
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle class='list_bg' height="24"><%=as_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=as_mid%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=model_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=crr_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=buy_date.substring(0,4) + "-" + buy_date.substring(4,6) + "-" + buy_date.substring(6,8)%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=as_except_day%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="javascript:go_asInfo('<%=as_no%>','<%=c_no%>')"><img src='../am/images/lt_view_d.gif' border='0' align='absmiddle'></a></td>
			</TR>
			<TR><TD colSpan=13 background="../am/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></TD></TR>
	</TBODY>
</TABLE>
</body>
</html>

<script language="javascript">

	var f = document.asForm;

	// 자산 등록 폼으로 이동
	function go_asset_form(){
		var c_no = f.c_no.value;
		location.href ="../servlet/AssetServlet?mode=asset_form&div=input&c_no="+c_no;
	}

	// 자산 정보 보기
	function go_asInfo(as_no,c_no){
		location.href ="../servlet/AssetServlet?mode=asset_form&div=delete_view&as_no="+as_no+"&c_no="+c_no;
	}

	
	function go_form(div,as_no,c_no){
		location.href ="../servlet/AssetServlet?mode=asset_form&div="+div+"&as_no="+as_no+"&c_no="+c_no;
	}

	// 일반 검색
	function go_search(){

		if( !f.searchscope.value=="" && f.searchword.value=="") {
			alert("검색어를 기입해 주십시요");
			return;
		} else if(f.searchscope.value=="" && !f.searchword.value==""){
			alert("검색 항목을 선택해 주십시요");
			return;
		} 


		var c_no = f.c_no.value;				// 콤포List 에서 선택된 카테고리 id
		var searchscope = f.searchscope.value;  // 검색 항목 (자산번호, 품명별, 고유번호 등등)
		var searchword = f.searchword.value;	// 검색어
		var div ="general";
	
		f.action = "../servlet/AssetServlet?mode=asset_del_list&c_no="+c_no+"&searchscope="+searchscope+"&searchword="+searchword+"&div="+div;
		f.submit();
	}
	 
	function repair(as_mid,as_name,as_no){
	
		if(confirm("<%=as_name%>(<%=as_mid%>)를 복원하시겠습니까?")){

		location.href="../servlet/AssetServlet?mode=asset_repair&as_no="+as_no;
		} else {
			return
		}
	
	}
</script>

