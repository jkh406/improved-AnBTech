<%@ include file="../../admin/configPopUp.jsp"%>
<%@ 	page		
	info= "고객정보 Excel출력"		
	contentType = "application/vnd.ms-excel; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.Hanguel"	
%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.office.OfficeListBean" />
<%!
	private String Pname = "";			//기업명
	private String Dname = "";			//부서명
	private String Cname = "";			//고객명
	private String Crank = "";			//직위
	private String Coffitel = "";		//전화번호
	private String Cfax = "";			//팩스
	private String Chandtel = "";		//핸드폰
	private String Cemail = "";			//전자우편
	private String Cjob = "";			//담당업무
	private String Cpostno = "";		//우편번호
	private String Caddress = "";		//회사주소
	private String Ctype = "";			//당사제품
	private String Chobby = "";			//취미
	private String Cspec = "";			//특기
	private String Cweday = "";			//결혼기념일
	private String Cbirth = "";			//생일
	private String Chometel = "";		//자택전화
	private String Cmemo = "";			//기타사항
	private String Cclass = "";			//고객유형
	
	private int rcnt = 0;				//전달받은 갯수
	private int twidth = 0;				//윈도우 너비
%>
<%	
	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	rcnt = 0;
	twidth = 80;
	Pname=Dname=Cname=Crank=Coffitel=Cfax=Chandtel=Cemail=Cjob=Cpostno="";
	Caddress=Ctype=Chobby=Cspec=Cweday=Cbirth=Chometel=Cmemo=Cclass="";

	/***********************************************************************
		전달변수 받기 (from reportCustomerExcel.jsp)
	***********************************************************************/
	Pname = request.getParameter("pname");			//기업명
	if(Pname.length() != 0) {rcnt++; twidth += 80; }

	Dname = request.getParameter("dname");			//부서명
	if(Dname.length() != 0) {rcnt++; twidth += 80; }

	Cname = request.getParameter("cname");			//고객명
	if(Cname.length() != 0) {rcnt++; twidth += 80; }

	Crank = request.getParameter("crank");			//직위
	if(Crank.length() != 0) {rcnt++; twidth += 80; }

	Coffitel = request.getParameter("coffitel");	//전화번호
	if(Coffitel.length() != 0) {rcnt++; twidth += 80; }

	Cfax = request.getParameter("cfax");			//팩스
	if(Cfax.length() != 0) {rcnt++; twidth += 80; }

	Chandtel = request.getParameter("chandtel");	//핸드폰
	if(Chandtel.length() != 0) {rcnt++; twidth += 80; }

	Cemail = request.getParameter("cemail");		//전자우편
	if(Cemail.length() != 0) {rcnt++; twidth += 80; }

	Cjob = request.getParameter("cjob");			//담당업무
	if(Cjob.length() != 0) {rcnt++; twidth += 80; }

	Cpostno = request.getParameter("cpostno");		//우편번호
	if(Cpostno.length() != 0) {rcnt++; twidth += 80; }

	Caddress = request.getParameter("caddress");	//회사주소
	if(Caddress.length() != 0) {rcnt++; twidth += 80; }

	Ctype = request.getParameter("ctype");			//당사제품
	if(Ctype.length() != 0) {rcnt++; twidth += 80; }

	Chobby = request.getParameter("chobby");		//취미
	if(Chobby.length() != 0) {rcnt++; twidth += 80; }

	Cspec = request.getParameter("cspec");			//특기
	if(Cspec.length() != 0) {rcnt++; twidth += 80; }

	Cweday = request.getParameter("cweday");		//결혼기념일
	if(Cweday.length() != 0) {rcnt++; twidth += 80; }

	Cbirth = request.getParameter("cbirth");		//생일
	if(Cbirth.length() != 0) {rcnt++; twidth += 80; }

	Chometel = request.getParameter("chometel");	//자택전화
	if(Chometel.length() != 0) {rcnt++; twidth += 80; }

	Cmemo = request.getParameter("cmemo");			//기타사항
	if(Cmemo.length() != 0) {rcnt++; twidth += 80; }

	Cclass = request.getParameter("cclass");		//고객유형
	if(Cclass.length() != 0) {rcnt++; twidth += 80; }

	/***********************************************************************
		등록정보 조회하기
	***********************************************************************/
	//컴럼명을 구한다.
	String[] dbColumns ={"a.*","b.name 'pname'"};
	String query = "where a.ap_id = b.ap_id order by name ASC";	//기본조건
	bean.setTable("customer_table a,company_table b");
	bean.setColumns(dbColumns);
	bean.setSearchWrite(query);
	bean.init_write();	
//out.println("q:" + bean.makeQuery_write() + "<br>");
//out.println("q:" + bean.isAll() + "<br>");
%>
<html>
<head>
<meta http-equiv="Content-Language" content="ko">
<meta name="GENERATOR" content="Microsoft FrontPage 5.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">
<LINK href="../css/style.css" rel=stylesheet>
<title>고객정보 Excel출력</title>
</head>
<BODY leftmargin='0' bgcolor='#F7F7F7' topmargin='3' marginwidth='3' marginheight='3'  onLoad="javascript:centerWindow();">

<table border="1" cellspacing="0" width="100%" id="AutoNumber1" height="">
	<TR height=30><TD class='subB' align=center colspan='<%=rcnt%>'><B>고 객 정 보</B></TD></TR>
	<TR height=30>
		<% 
		if(Pname.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>기업명</TD>");
		}
		if(Dname.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>부서명</TD>");
		}
		if(Cclass.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>고객유형</TD>");
		}
		if(Cname.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>고객명</TD>");
		}
		if(Crank.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>직 위</TD>");
		}
		if(Coffitel.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>전화번호</TD>");
		}
		if(Cfax.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>팩 스</TD>");
		}
		if(Chandtel.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>핸드폰</TD>");
		}
		if(Cemail.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>전자우편</TD>");
		}
		if(Cjob.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>담당업무</TD>");
		}
		if(Cpostno.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>우편번호</TD>");
		}
		if(Caddress.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>회사주소</TD>");
		}
		if(Ctype.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>당사제품</TD>");
		}
		if(Chobby.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>취 미</TD>");
		}
		if(Cspec.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>특 기</TD>");
		}
		if(Cweday.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>결혼기념일</TD>");
		}
		if(Cbirth.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>생 일</TD>");
		}
		if(Chometel.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>자택전화</TD>");
		}
		if(Cmemo.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>기타사항</TD>");
		}	

		%>
	</TR>

	<% if (bean.isEmpty()) { %>
	<tr>
			<td colspan='<%=rcnt%>'><center>내용이 없습니다.</center></td>
	</tr> 
	<% } %>	

	<% 
		while(bean.isAll()) { 
			out.println("<TR height=25>");
	
			if(Pname.length() != 0) out.println("<TD>&nbsp;"+bean.getData("pname")+"</TD>");
			if(Dname.length() != 0) out.println("<TD>&nbsp;"+bean.getData("division")+"</TD>");	
			if(Cclass.length() != 0) out.println("<TD>&nbsp;"+bean.getData("customer_class")+"</TD>");
			if(Cname.length() != 0) out.println("<TD>&nbsp;"+bean.getData("name")+"</TD>");
			if(Crank.length() != 0) out.println("<TD>&nbsp;"+bean.getData("rank")+"</TD>");
			if(Coffitel.length() != 0)out.println("<TD>&nbsp;"+bean.getData("office_tel")+"</TD>");
			if(Cfax.length() != 0) out.println("<TD>&nbsp;"+bean.getData("fax")+"</TD>");
			if(Chandtel.length() != 0) out.println("<TD>&nbsp;"+bean.getData("hand_tel")+"</TD>");
			if(Cemail.length() != 0) out.println("<TD>&nbsp;"+bean.getData("email")+"</TD>");
			if(Cjob.length() != 0) out.println("<TD>&nbsp;"+bean.getData("main_job")+"</TD>");
			if(Cpostno.length() != 0) out.println("<TD>&nbsp;"+bean.getData("post_no")+"</TD>");
			if(Caddress.length() != 0) out.println("<TD>&nbsp;"+bean.getData("address")+"</TD>");
			if(Ctype.length() != 0) out.println("<TD>&nbsp;"+bean.getData("customer_type")+"</TD>");
			if(Chobby.length() != 0) out.println("<TD>&nbsp;"+bean.getData("hobby")+"</TD>");
			if(Cspec.length() != 0) out.println("<TD>&nbsp;"+bean.getData("speciality")+"</TD>");
			if(Cweday.length() != 0) out.println("<TD>&nbsp;"+bean.getData("wedding_day")+"</TD>");
			if(Cbirth.length() != 0) out.println("<TD>&nbsp;"+bean.getData("birthday")+"</TD>");
			if(Chometel.length() != 0) out.println("<TD>&nbsp;"+bean.getData("home_tel")+"</TD>");
			if(Cmemo.length() != 0) out.println("<TD>&nbsp;"+bean.getData("memo")+"</TD>");

			out.println("</TR>");
		} //while 

	%>
	
</table>
</body>
</html>

<script language=javascript>
<!--
function centerWindow() 
{ 
        var sampleWidth = <%=twidth%>;                        // 윈도우의 가로 사이즈 지정 
        var sampleHeight = 600;								 // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

-->
</script>