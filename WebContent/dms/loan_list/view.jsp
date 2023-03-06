<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.dms.entity.*,java.io.*"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%!
	LoanTable loan;
	LinkUrl redirect;
	DmsEnvTable dmsenv;
%>

<%  
	/*********************************************************************
	 	사용자 정보 가져오기
	*********************************************************************/
	String id = sl.id; 			//접속자 ID
	String name = sl.name;			//접속자 이름
	String passwd = sl.passwd;		//접속자 패스워드
	String division = sl.division;		//접속자 부서

	java.util.Date now = new java.util.Date();
	java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd 'at' hh:mm:ss");
	String regi_date 	= vans.format(now);


	String mode = request.getParameter("mode");
	String no = request.getParameter("no");
	String data_id = request.getParameter("d_id");
	String page_no = request.getParameter("page");
	String searchword = request.getParameter("searchword");
	String searchscope = request.getParameter("searchscope");

	//loan_list 에서 가져오기
	loan = new LoanTable();
	loan = (LoanTable)request.getAttribute("LoanInfo");

	String loan_no = loan.getLoanNo()==null?"":loan.getLoanNo();
	String doc_no = loan.getDocNo()==null?"":loan.getDocNo();
	String ver_code = loan.getVerCode()==null?"":loan.getVerCode();
	String requestor = loan.getRequestor()==null?"":loan.getRequestor();
	String req_date = loan.getReqDate()==null?"":loan.getReqDate();
	String return_date = loan.getReturnDate()==null?"":loan.getReturnDate();
	String why_loan = loan.getWhyLoan()==null?"":loan.getWhyLoan();
	String copy_num = loan.getCopyNum()==null?"1":loan.getCopyNum();
	String stat = loan.getStat()==null?"":loan.getStat();
	String why_reject = loan.getWhyReject()==null?"":loan.getWhyReject();
	String loan_end = 	loan.getLoanEndDate()==null?"":loan.getLoanEndDate();

	//링크 문자열 가져오기
	redirect = new LinkUrl();
	redirect = (LinkUrl)request.getAttribute("RedirectInView");
	String link_list = redirect.getLinkList();
	String link_modify = redirect.getLinkModify();
	String input_hidden = redirect.getInputHidden();
	String link_commit = redirect.getLinkCommit();
	String delete_loan = redirect.getLinkDeleteLoan();
	String doc_info_url = redirect.getLinkDocInfoUrl();

	//현재 카테고리에 대한 환경 가져오기
	dmsenv = new DmsEnvTable();
	dmsenv = (DmsEnvTable)request.getAttribute("DmsEnv");
	String loan_period = dmsenv.getLoanPeriod();
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../dms/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../dms/images/blet.gif"> 대출신청정보</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<a href='<%=link_list%>'><img src="../dms/images/bt_list.gif" border="0" align="absmiddle"></a>
					<a href='<%=link_commit%>'><img src="../dms/images/bt_loan.gif" border="0" align="absmiddle"></a>
					<a href='<%=delete_loan%>'><img src="../dms/images/bt_del.gif" border="0" align="absmiddle"></a>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--내용-->
<form method=get name="viewForm" action='../servlet/AnBDMS' style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">대출번호</td>
           <td width="37%" height="25" class="bg_04"><%=loan_no%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">대출신청자</td>
           <td width="37%" height="25" class="bg_04"><%=requestor%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">신청사유</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=why_loan%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">대출문건수</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=copy_num%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">대출신청일</td>
           <td width="37%" height="25" class="bg_04"><%=req_date%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">반납예정일</td>
           <td width="37%" height="25" class="bg_04"><%=loan_end%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">처리결과</td>
           <td width="37%" height="25" class="bg_04"><%=stat%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">처리일자</td>
           <td width="37%" height="25" class="bg_04"><%=return_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">처리결과</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=why_reject%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!--기본정보-->
	<table cellspacing=0 cellpadding=0 width="100%" border=0>
	   <tbody>
         <tr><td><IFRAME id=iframe  marginwidth="0" marginheight="0" scrolling="no" frameborder=0 src="<%=doc_info_url%>" width="100%" height="700"></IFRAME></td></tr></tbody></table>
<%=input_hidden%><input type='hidden' name='loan_period' value='<%=loan_period%>'>
</form>
</td></tr></table>
</body>
</html>



<script language="javascript">

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//대출처리
function loan_process(tablename,searchscope,searchword,page,no,data_id,ver) {

	var url = "../dms/loan_list/loan_process.jsp?tablename="+tablename+"&searchscope="+searchscope+"&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

	wopen(url,'loan_process','420','224','scrollbars=no,toolbar=no,status=no,resizable=no');

/*

	var sParam = "src=loan_process.jsp&frmWidth=500&frmHeight=300&title=LOAN&loan_period=<%=loan_period%>";
	
	var sRtnValue=showModalDialog("../dms/loan_list/modalFrm.jsp?"+sParam,"LOAN","dialogWidth:430px;dialogHeight:200px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

	if (typeof sRtnValue != "undefined" && sRtnValue != "")
	{
		var r_value = sRtnValue;
		var s_value = r_value.split("|");
		var mode	= s_value[0];
		var why		= s_value[1];
		var loan_day = s_value[2];

		sParam = "&tablename="+tablename+"&searchscope="+searchscope+"&searchword="+searchword;
		sParam = sParam + "&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver+"&why="+why+"&loan_day="+loan_day;
		location.href = "../servlet/AnBDMS?mode="+mode+sParam;

	}
*/
}

function deleteloanConfirm(tablename,searchscope,searchword,page,no,data_id,ver){

		if (confirm("정말 삭제 하시겠습니까?")){
				var sParam = "&tablename="+tablename+"&searchscope="+searchscope+"&searchword="+searchword;
				sParam = sParam + "&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;
				location.href = "../servlet/AnBDMS?mode=loan_del" + sParam;   
		} else {
		   return;
		}
		
	}

function document_info(tablename,mode,page,no,data_id,ver){
	    var page_temp="info"
		var sParam = "tablename="+tablename+"&mode="+mode+"&page="+page_temp+"&no="+no+"&data_id="+data_id+"&ver="+ver;
		window.open("../servlet/AnBDMS?"+sParam,"win","toolbar=no,status=no,menubar=no,width=500,height=500");
}

</script>