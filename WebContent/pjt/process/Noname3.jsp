<%@ include file="../../admin/configPopUp.jsp"%>
<%@ 	page		
	info= "고객이력 Excel출력"		
	contentType = "application/vnd.ms-excel; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.Hanguel"	
%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.office.OfficeListBean" />
<%!
	private String Sdate = "";			//날자선택
	private String Stime = "";			//검색 시작일 
	private String Etime = "";			//검색 종료일

	private String Scompany = "";		//기업선택
	private String Cname = "";			//선택된 기업명
	private String Cname_id = "";		//선택된 기업명 ID

	private String Scustomer = "";		//고객선택
	private String Sname = "";			//선택된 고객명
	private String Sname_id = "";		//선택된 고객명 ID

	private String Spurpose = "";		//목적선택
	private String Pname = "";			//선택된 목적명

	private String Subject = "";		//제목
	private String Content = "";		//내용
	private String Issue = "";			//이슈사항

	private String arg1 = "";			//정렬1
	private String arg2 = "";			//정렬2
	private String arg3 = "";			//정렬3

	private int rcnt = 0;				//전달받은 갯수
	private int twidth = 0;				//윈도우 너비
%>
<%	
	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	rcnt = 1;	//clear(작성자)
	twidth = 80;
	/***********************************************************************
		전달변수 받기 (from reportServiceExcel.jsp)
	***********************************************************************/
	Sdate = request.getParameter("sdate");							//날자선택
	if(Sdate != null) {
		Stime = request.getParameter("stime");						//검색 시작일 
		Etime = request.getParameter("etime");						//검색 종료일
		twidth += 220;
		rcnt++;	rcnt++; rcnt++;		
	} else {
		Stime = anbdt.getYear() + "/" + anbdt.getMonth() + "/" + "01"; //해당월 1일
		Etime = anbdt.getDate(0);									//오늘날자
	}

	Scompany = request.getParameter("scompany");					//기업선택
	if(Scompany != null) {
		Cname = Hanguel.toHanguel(request.getParameter("cname"));	//선택된 기업명
		Cname_id = request.getParameter("cname_id");				//선택된 기업명 ID
		twidth += 80;
		rcnt++;
	} else {
		Cname = "";
		Cname_id = "";
	}
//out.println(Scompany + ":" + Cname +":" + Cname_id);
	Scustomer = request.getParameter("scustomer");					//고객선택
	if(Scustomer != null) {
		Sname = Hanguel.toHanguel(request.getParameter("sname"));	//선택된 고객명
		Sname_id = request.getParameter("sname_id");				//선택된 고객명 ID
		twidth += 60;
		rcnt++;
	} else {
		Sname = "";
		Sname_id = "";
	}

	Spurpose = request.getParameter("spurpose");					//목적선택
	Pname = Hanguel.toHanguel(request.getParameter("pname"));		//선택된 목적명
	if(Spurpose.length() != 0) { twidth += 90;	rcnt++;}

	Subject = request.getParameter("subject");						//제목
	if(Subject.length() != 0) { twidth += 200;	rcnt++;}
	Content = request.getParameter("content");						//내용
	if(Content.length() != 0) { twidth += 200;	rcnt++;}
	Issue = request.getParameter("result");							//이슈사항
	if(Issue.length() != 0) { twidth += 200;	rcnt++;}

	arg1 = request.getParameter("arrange1");						//정렬1
	arg2 = request.getParameter("arrange2");						//정렬2
	arg3 = request.getParameter("arrange3");						//정렬3

	//window width
	if(twidth >= 1000) twidth = 1000;
	/***********************************************************************
		등록정보 조회하기
	***********************************************************************/
	//컴럼명을 구한다.
	String[] dbColumns ={"a.*","b.name_kor","c.name_kor","d.name"};
	String query = "where a.ap_id=b.company_no and a.at_id=c.at_id and a.au_id=d.id ";	//기본조건
	if(Sdate.length() != 0)	{														//날자
		query += "and (a.s_day >= '" + str.repWord(Stime,"/","") + "' and a.s_day <= '" + str.repWord(Etime,"/","") + "') ";
	}
	if(Scompany.length() != 0) {													//기업명 ID
		if(!Cname_id.equals("all"))
			query += "and a.ap_id = '" + Cname_id + "' "; 
	}
	if(Scustomer.length() != 0) {													//고객명 ID
		if(!Sname_id.equals("all"))
			query += "and a.at_id = '" + Sname_id + "' "; 
	}
	if(Spurpose.length() != 0) {													//목적
		if(!Pname.equals("all"))
			query += "and a.class = '" + Pname + "' "; 
	}
	query += "order by "+arg1+","+arg2+","+arg3;	//정렬
	
	bean.setTable("history_table a,company_customer b,personal_customer c,user_table d");
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
<title>고객이력 Excel출력</title>
</head>
<BODY leftmargin='0' bgcolor='#F7F7F7' topmargin='3' marginwidth='3' marginheight='3'  onLoad="javascript:centerWindow();">

<% if(!Cname_id.equals("all")) { //기업명을 표기한다.
	out.println("<table border='0' cellspacing='0' width='100%' id='AutoNumber0' height=''>");
	if(!Sname_id.equals("all")) { //기업명 및 고객명
		rcnt--; rcnt--;
		out.println("<TR height=30><TD class='subB' align=center colspan='"+rcnt+"'><B>고 객 이 력</B></TD></TR>");
		out.println("<TR height=30><td class='subE' colspan='"+rcnt+"' align='right'>기업명: "+Cname+"&nbsp;&nbsp;&nbsp; 고객명: " + Sname + "&nbsp;&nbsp;&nbsp;</td></TR>");
	} else {						//기업명만
		rcnt--;
		out.println("<TR height=30><TD class='subB' align=center colspan='"+rcnt+"'><B>고 객 이 력</B></TD></TR>");
		out.println("<td class='subE' colspan='"+rcnt+"' align='right'>기업명: "+Cname+"&nbsp;&nbsp;&nbsp;</td>");
	}
	out.println("</table>");
} %>

<table border="1" cellspacing="0" width="100%" id="AutoNumber1" height="">
	<TR height=30>
		<% 
		if(Sdate.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center >날&nbsp;&nbsp;자</TD>");
			out.println("<TD class='subB' WIDTH=40 align=center >요일</TD>");
			out.println("<TD class='subB' WIDTH=80 align=center >시&nbsp;&nbsp;간</TD>");
		}
		if((Scompany.length() != 0) && (Cname_id.equals("all"))){
			out.println("<TD class='subB' WIDTH=80 align=center >기업명</TD>");
		}

		if((Scustomer.length() != 0) && (Sname_id.equals("all"))){
			out.println("<TD class='subB' WIDTH=60  align=center >고객명</TD>");
		}
		if(Spurpose.length() != 0) {
			out.println("<TD class='subB' WIDTH=90  align=center >목&nbsp;&nbsp;적</TD>");
		}
		if(Subject.length() != 0) {
			out.println("<TD class='subB' WIDTH=200  align=center >제&nbsp;&nbsp;목</TD>");
		}
		if(Content.length() != 0) {
			out.println("<TD class='subB' WIDTH=200  align=center >내&nbsp;&nbsp;용</TD>");
		}
		if(Issue.length() != 0) {
			out.println("<TD class='subB' WIDTH=200  align=center >ISSUE</TD>");
		}
		out.println("<TD class='subB' WIDTH=60  align=center>작성자</TD>");

		%>
	</TR>

	<% if (bean.isEmpty()) { %>
	<tr>
			<td colspan=<%=rcnt%> rowspan=5><span class="text"><center>내용이 없습니다.</center></td>
	</tr> 
	<% } %>	

	<% 
		while(bean.isAll()) { 
		out.println("<TR height=25>");

		if(Sdate.length() != 0) { 
			String dd = bean.getData("a.s_day");
			String cdate = "";
			int dayi = 0;
			if(dd != null) {
				int year = Integer.parseInt(dd.substring(0,4));
				int month = Integer.parseInt(dd.substring(5,6));
				int date = Integer.parseInt(dd.substring(7,8));
				dayi = anbdt.getDay(year,month,date);				//요일 (숫자)
				cdate = dd.substring(0,4) + "-" +dd.substring(5,6) + "-" + dd.substring(7,8); //날자
			} else dayi = 0;
			String[] day = {"","일요일","월요일","화요일","수요일","목요일","금요일","토요일"};
			
			out.println("<TD align=center valign=top>" + cdate + "</TD>");
			out.println("<TD align=center valign=top>" + day[dayi] + "</TD>");
			out.println("<TD align=center valign=top>" + str.repWord(bean.getData("a.s_time"),"/","~") + "</TD>");

		}
	
		if((Scompany.length() != 0) && (Cname_id.equals("all")))		//기업명
			out.println("<TD align=left valign=top>" + bean.getData("b.name") + "</TD>");
		if((Scustomer.length() != 0) && (Sname_id.equals("all")))		//고객명
			out.println("<TD align=center valign=top>" + bean.getData("c.name") + "</TD>");
		if(Spurpose.length() != 0)										//목적
			out.println("<TD align=center valign=top>" + bean.getData("a.class") + "</TD>");
		if(Subject.length() != 0)										//제목
			out.println("<TD align=left valign=top>" + bean.getData("a.subject") + "</TD>");
		if(Content.length() != 0) {										//내용
			//본문내용 넣기 (긴내용 중간에 리턴키(br)넣기)
			String bonmun = bean.getData("a.content");
			Content = "";
			int tlen = bonmun.length();		//본문길이
			if(tlen > 60) {
				int mcut = 60;
				for(int m=0; m < tlen; ){
					if(mcut == tlen)	//마지막 라인 읽기
						Content += bonmun.substring(m,mcut);
					else 
						Content += bonmun.substring(m,mcut) + "<br>";
					m += 60;
					mcut += 60;
					if(tlen < mcut) mcut = tlen;		//마지막라인 
				}//for
			} else Content=bonmun;
			out.println("<TD align=left valign=top>" + Content + "</TD>");
//			out.println("<TD align=left valign=top><TEXTAREA>" + bean.getData("a.content") + "</TEXTAREA></TD>");
		}
		
		if(Issue.length() != 0) {										//이슈사항
			//Issue내용 넣기 (긴내용 중간에 리턴키(br)넣기)
			String result = bean.getData("a.result");
			Issue = "";
			int tlen = result.length();		//본문길이
			if(tlen > 60) {
				int mcut = 60;
				for(int m=0; m < tlen; ){
					if(mcut == tlen)	//마지막 라인 읽기
						Issue += result.substring(m,mcut);
					else 
						Issue += result.substring(m,mcut) + "<br>";
					m += 60;
					mcut += 60;
					if(tlen < mcut) mcut = tlen;		//마지막라인 
				}//for
			} else Issue=result;
			out.println("<TD align=left valign=top>" + Issue + "</TD>");
//			out.println("<TD align=left valign=top><TEXTAREA>" + bean.getData("a.result") + "</TEXTAREA></TD>");
		}
		out.println("<TD align=center valign=top>" + bean.getData("d.name") + "</TD>"); //작성자
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
        var sampleHeight = 600;                       // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 
-->
</script>
