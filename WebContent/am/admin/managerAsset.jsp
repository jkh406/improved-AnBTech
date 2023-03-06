<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkAM02.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.am.entity.*,java.text.NumberFormat"%>

<%
	com.anbtech.am.entity.AsInfoTable asinfotable;
	com.anbtech.am.entity.AMLinkTable amLinkTable;
%>

<%
	String sb=(String)request.getAttribute("CategoryList");							 //  카테고리 String 가져오기
	String c_no=request.getParameter("c_no")==null?"0":request.getParameter("c_no");
	
	NumberFormat nf = NumberFormat.getInstance();

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

%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../am/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
	<TR height=27>
		<TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../am/images/blet.gif" align="absmiddle"> 자산등록관리</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../am/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../am/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../am/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top><!--버튼 및 페이징-->
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY> 
			<form method="get" name="asForm" style="margin:0" action='../servlet/AssetServlet' 
				  onSubmit="if( !document.asForm.searchscope.value=='' && document.asForm.searchword.value=='') {
								alert('검색어를 기입해 주십시요');
								return false;
							} else if(document.asForm.searchscope.value=='' && !document.asForm.searchword.value==''){
								alert('검색 항목을 선택해 주십시요');
								return false;
							} else return true">
			<TR><TD align=left width='90%' style='padding-left:5px'>
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
				<IMG src="../am/images/bt_search3.gif" onclick ="javascript:go_search()" border='0' style='cursor:hand' align='absmiddle'>
				<IMG src="../am/images/bt_search_d.gif" onclick ="javascript:search_detail()"  border='0' style='cursor:hand' align='absmiddle'></TD>
				 <TD width='10%' align='right' style="padding-right:10px"><%=view_pagecut%></TD>
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan='3'></TD></TR>
				<TR><TD height='25' style='padding-left:5px;'>
				<a href='javascript:go_asset_form()'><IMG src="../am/images/bt_reg.gif" border='0' align='absmiddle'></a>
				<IMG src="../am/images/bt_am_pm.gif" onclick ="javascript:openwin()" border='0' style='cursor:hand' align='absmiddle'>
				<a href='javascript:go_cleanDB()'><IMG src="../am/images/bt_clean.gif" border='0' align='absmiddle' alt='CLEAN'></a>
				</TD>
			 </TR>
			  <input type='hidden' name=mode value='asset_list'>
			  <input type='hidden' name=div  value='general'>
			  </form></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--리스트-->
<%
		  int     as_no		=0;			// 관리번호
		  String  as_mid	="";		// 자산번호
		  String  ct_id		="";			//카테고리ID
		  //String  c_no	="";
		  String  as_item_no="";	//자산수량no
		  String  w_id		="";			//작성자ID
		  String  w_name	="";		//작성자ID/이름
		  String  w_rank	="";		//작성자부서
		  String  b_id		="";			//자산품구매자ID
		  String  b_name	="";		//자산품구매자이름
		  String  b_rank	="";		//자산품부서명
		  String  model_name="";	//모델명
		  String  as_name	="";		//자산품목이름
		  String  as_serial	="";		//자산품시리얼
		  String  buy_date	="";		//구매일자
		  String  as_price	="";		//품목 가격
		  String  dc_date	="";		//감가제한년도
		 
		  String  as_each_dc="";	//품목 감가 비율
		  String  as_value	="";		//자산가격
		  String  crr_id	="";		//현재 사용자 ID
		  String  crr_name	="";		//현재 사용자 이름
		  String  crr_rank	="";		//현재 사용자 부서
		  String  buy_where	="";		//구매지(회사)
		  String  as_maker	="";		//자산품만든곳(회사)
		  String  as_setting="";	//규격(사양)
		  String  bw_tel	="";		//구매회사연락처
		  String  bw_address="";	//구매회사주소
		  String  bw_employee="";	//구매회사담당자
		  String  bw_mgr_tel="";	//구매회사담당자연락처
		  String  etc		="";			//기타
		  String  as_status	="";		//자산상태(폐기,사용중,감각비해제)
		  String  as_except_day="";	//자산 폐기 일자
		  String  as_except_reason="";	// 자산 폐기 이유
		  String  as_status_name="";	// 자산상태text
		  String  now_status	="";
		  String  u_name		="";
%>

	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
		<TR bgColor=#9DA9B9 height=2><TD colspan=19></TD></TR>
		<TR vAlign=middle height=23>
			  <TD noWrap width=100 align=middle class='list_title'>품명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>자산번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>모델명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>현사용자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>구입일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>자산가치</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>상태</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>비고</TD>

				 <!-- <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>비고</TD>-->
		</TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
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
			  
				as_each_dc	= asinfotable.getAsEachDc();	
				as_value	= asinfotable.getAsValue();		
				crr_name	= asinfotable.getCrrName();	
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
				as_status_name= asinfotable.getAsStatusName();
				now_status	= asinfotable.getNowStatus();

				if(now_status==null || now_status.equals("")) now_status="처리가능"; // 현재 상태 
	%>
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle class='list_bg' height="24"><%=as_name%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=as_mid%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=model_name%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=u_name%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=buy_date.substring(0,4)%>-<%=buy_date.substring(4,6)%>-<%=buy_date.substring(6,8)%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg'><%=nf.format(nf.parse(as_value))%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=as_status_name%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'>
						<a href="javascript:go_asInfo('<%=as_no%>','<%=c_no%>')">
						<IMG src='../am/images/lt_view_d.gif' border='0' align='absmiddle'></a></td>
				 <!-- <TD align=middle class='list_bg'>
						<a href="javascript:go_form('modify','<%=as_no%>','<%=c_no%>')">수정</a>|<a href="javascript:go_form('delete','<%=as_no%>','<%=c_no%>')">폐기</a></td>
						<input type='hidden' name="c_no" value=<%=c_no%>>--></TR>
			<TR><TD colSpan=19 background="../am/images/dot_line.gif"></TD></TR>
<%
		}
%>
			</TBODY></TABLE>
	
		</TD>
	</TR>
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
	location.href ="../servlet/AssetServlet?mode=user_asset_view&div=view&as_no="+as_no+"&c_no="+c_no;
}

	
function go_form(div,as_no,c_no){
	location.href ="../servlet/AssetServlet?mode=asset_form&div="+div+"&as_no="+as_no+"&c_no="+c_no;
}

// 일반 검색
function go_search(){

	if( !f.searchscope.value=="" && f.searchword.value=="") {
		alert("검색어를 기입해 주십시요");
		return false;
	} else if(f.searchscope.value=="" && !f.searchword.value==""){
		alert("검색 항목을 선택해 주십시요");
		return false;
	} 

	var c_no = f.c_no.value;				// 콤포List 에서 선택된 카테고리 id
	var searchscope = f.searchscope.value;  // 검색 항목 (자산번호, 품명별, 고유번호 등등)
	var searchword = f.searchword.value;	// 검색어
	var div ="general";
	
	f.action = "../servlet/AssetServlet?mode=asset_list&c_no="+c_no+"&searchscope="+searchscope+"&searchword="+searchword+"&div="+div;
	f.submit();
}

// 자산감가처리
function openwin() {

	wopen('../am/admin/measurePopup.jsp','measure','350','193','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//상세검색
function search_detail() {
	var c_no = f.c_no.value;
	wopen("../am/admin/search.jsp?category_full=<%=category_full%>&c_no="+c_no,'search','515','279','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

// 쓰레기 정보 삭제처리
function go_cleanDB(){
	if(confirm("쓰레기 정보를 정리 하시겠습니까?")) 
	location.href ="../servlet/AssetServlet?mode=cleanDb";
}
</script>

