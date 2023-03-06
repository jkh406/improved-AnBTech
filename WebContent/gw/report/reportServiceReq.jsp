<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= "고객이력 출력리포트 입력받기"		
	contentType = "text/html; charset=euc-kr" 		
	import		= "java.io.*,java.util.*,com.anbtech.text.Hanguel,com.anbtech.date.anbDate,com.anbtech.text.StringProcess"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
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
%>
<%	
	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자

	//선택된 내용 
	Sdate = request.getParameter("sdate");							//날자선택
	if(Sdate != null) {
		Stime = request.getParameter("stime");						//검색 시작일 
		Etime = request.getParameter("etime");						//검색 종료일
	} else {
		Stime = anbdt.getYear() + "/" + anbdt.getMonth() + "/" + "01"; //해당월 1일
		Etime = anbdt.getDate(0);									//오늘날자
	}

	Scompany = request.getParameter("scompany");					//기업선택
	if(Scompany != null) {
		Cname = request.getParameter("cname");						//선택된 기업명
		Cname_id = request.getParameter("company_id");				//선택된 기업ID	
	} else {
		Cname = "";
		Cname_id = "";
	}


	Scustomer = request.getParameter("scustomer");					//고객선택
	if(Scustomer != null) {
		Sname = request.getParameter("sname");						//선택된 고객명
		Sname_id = request.getParameter("customer_id");				//선택된 기업ID	
	} else {
		Sname = "";
		Sname_id = "";
	}

	Spurpose = request.getParameter("spurpose");					//목적선택
	Pname = Hanguel.toHanguel(request.getParameter("pname"));		//선택된 목적명

	Subject = request.getParameter("subject");						//제목
	Content = request.getParameter("content");						//내용
	Issue = request.getParameter("result");							//이슈사항

	arg1 = request.getParameter("arrange1");						//정렬1
	arg2 = request.getParameter("arrange2");						//정렬2
	arg3 = request.getParameter("arrange3");						//정렬3

%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_print_ex.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
	<form method="POST" name="frm1" action="reportServiceReq.jsp" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">
			<%	
				if(Sdate != null) out.print("<input type='checkbox' name='sdate' value='s_day' checked>등록기간");
				else out.print("<input type='checkbox' name='sdate' value='s_day'>등록기간");
			%>		   
		   </td>
           <td width="80%" height="25" class="bg_02"><input type="text" name="stime" size="10" value="<%=Stime%>"> <A Href="Javascript:OpenCalendar('stime');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A> ~ <input type="text" name="etime" size="10" value="<%=Etime%>"> <A Href="Javascript:OpenCalendar('etime');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">
			<%
				if(Scompany != null) out.print("<input type='checkbox' name='scompany' value='ap_id' checked>고객사명");
				else out.print("<input type='checkbox' name='scompany' value='ap_id'>기업명");
			%>		   
		   </td>
           <td width="80%" height="25" class="bg_02">
        	<select size="1" name="cname" onChange="javascript:selectComp();">
				<option value="all">기업명을 선택하십시요</option>
				<% 
					String[] columns={"company_no","name_kor"};
					bean.setColumns(columns);
					bean.setTable("COMPANY_CUSTOMER");
					bean.setOrder("name_kor desc");
					bean.setClear();
					bean.setSearch("","");
					bean.init_unique();

					String sel = "";
					while(bean.isAll()){
						if(bean.getData("company_no").equals(Cname)) sel = "selected";
						else sel = "";
						out.println("<option value='"+bean.getData("company_no")+"' "+sel+">"+bean.getData("name_kor") );
						out.println("</option>");
					}

				%>
          	</select><input type='hidden' name='company_id'>
		   </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">
			<%
				if(Scustomer != null) out.print("<input type='checkbox' name='scustomer' value='at_i' checked>고객명");
				else out.print("<input type='checkbox' name='scustomer' value='at_i'>고객명");
			%>		   
		   </td>
           <td width="80%" height="25" class="bg_02">
        	<select size="1" name="sname">
				<option value="all">고객명을 선택하십시요</option>
				<%
					String[] Ccolumns={"company_no","name_kor"};
					bean.setColumns(Ccolumns);
					bean.setTable("PERSONAL_CUSTOMER");
					bean.setOrder("name_kor desc");
					bean.setClear();
					bean.setSearch("company_no",Cname_id);
					bean.init_unique();

					String Cel = "";
					while(bean.isAll()){
						if(bean.getData("company_no").equals(Sname)) Cel = "selected";
						else Cel = "";
						out.println("<option value='"+bean.getData("company_no")+"' "+Cel+">"+bean.getData("name_kor") );
						out.println("</option>");
					}
				%>
          	</select><input type='hidden' name='customer_id'>		   
		   </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">
			<%
				if(Spurpose != null) out.print("<input type='checkbox' name='spurpose' value='class' checked>목적");
				else out.print("<input type='checkbox' name='spurpose' value='class'>목적");
			%>
		   </td>
           <td width="80%" height="25" class="bg_02">
        	<select size="1" name="pname">
				<option value="all">목적을 선택하십시요&nbsp;&nbsp;&nbsp;</option>
				<%
					String[] Ocolumns={"class"};
					bean.setColumns(Ocolumns);
					bean.setTable("HISTORY_TABLE");
					bean.setOrder("class desc");
					bean.setClear();
					bean.setSearch("","");
					bean.init_unique();
					
					String Pel = "";
					while(bean.isAll()){
						if(bean.getData("class").equals(Pname)) Pel = "selected";
						else Pel = "";
						out.println("<option value='"+bean.getData("class")+"' "+Pel+">"+bean.getData("class") );
						out.println("</option>");
					}
				%>
          	</select>		   
		   </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">
			<%
				if(Subject != null) out.print("<input type='checkbox' name='subject' value='subject' checked>제목");
				else out.print("<input type='checkbox' name='subject' value='subject'>제목");
			%>
		   </td>
           <td width="80%" height="25" class="bg_02"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">
		  <%
			 if(Subject != null) out.print("<input type='checkbox' name='content' checked value='content'>내용");
			 else out.print("<input type='checkbox' name='content' value='content'>내용");
		  %>
		   </td>
           <td width="80%" height="25" class="bg_02"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">
		  <%
			 if(Subject != null)out.print("<input type='checkbox' name='result' value='result' checked>이슈사항");
			else out.print("<input type='checkbox' name='result' value='result'>이슈사항");
		  %>
		   </td>
           <td width="80%" height="25" class="bg_02"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">정렬조건</td>
           <td width="80%" height="25" class="bg_02">
			<% 
				String[] LIST = {"a.s_day","b.name_kor","c.name_kor"};
				String[] NAME = {"날짜순","기업순","고객순"};
				String A1 = ""; String A2 = ""; String A3= "";
				out.println("<table border=0><tr><td valign=middle>정렬1</td>");
				out.println("<td valign=middle><select size='1' name='arrange1'>");
				for(int i = 0; i<3; i++) {
					if(LIST[i].equals(arg1)) A1 = "selected";
					else A1= "";
					out.println("<option " + A1 + " value='" + LIST[i] + "'>" + NAME[i] + "</option>"); 
				}
				out.println("</select></td>");

				out.println("<td valign=middle>정렬2</td>");
				out.println("<td valign=middle><select size='1' name='arrange2'>");
				for(int i = 0; i<3; i++) {
					if(LIST[i].equals(arg2)) A2 = "selected";
					else A2= "";
					out.println("<option " + A2 + " value='" + LIST[i] + "'>" + NAME[i] + "</option>"); 
				}
				out.println("</select></td>");

				out.println("<td valign=middle>정렬3</td>");
				out.println("<td valign=middle><select size='1' name='arrange3'>");
				for(int i = 0; i<3; i++) {
					if(LIST[i].equals(arg3)) A3 = "selected";
					else A3= "";
					out.println("<option " + A3 + " value='" + LIST[i] + "'>" + NAME[i] + "</option>"); 
				}
				out.println("</select></td></tr></table>");
			%>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:reportExcel()"><img src='../images/bt_excel.gif' border='0' align='absmiddle'></a> <a href='javascript:self.close()'><img src='../images/bt_close.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></form></td></tr></table></BODY></HTML>

<script language=javascript>
<!--
function centerWindow() 
{ 
        var sampleWidth = 550;                        // 윈도우의 가로 사이즈 지정 
        var sampleHeight = 380;                       // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

//기업명 선택
function selectComp() {
	var sec = document.frm1.cname.selectedIndex;	//선택된 index번호
	id = document.frm1.cname.options[sec].value;	//해당하는 기업id
	document.location.action='reportServiceReq.jsp';
	document.frm1.company_id.value=id;
	document.frm1.submit();
}

//달력
function OpenCalendar(FieldName) {
	var strUrl = "Calendar.jsp?FieldName="+FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=200, height=270");
}

//Excel로 출력하기
function reportExcel() {

	if(document.frm1.sdate.checked == true) {			//일자 및 시간
		sdate = document.frm1.sdate.value; 
		stime = document.frm1.stime.value;
		etime = document.frm1.etime.value;
	} else {
		sdate="";
		stime="";
		etime="";
	}
	
	if(document.frm1.scompany.checked == true) {
		scompany = document.frm1.scompany.value;
		cname_id = document.frm1.cname.options[document.frm1.cname.selectedIndex].value;
		cname = document.frm1.cname.options[document.frm1.cname.selectedIndex].text;
	} else {
		scompany="";
		cname_id = "";
		cname = "";
	}

	if(document.frm1.scustomer.checked == true) {
		scustomer= document.frm1.scustomer.value; 
		sname_id = document.frm1.sname.options[document.frm1.sname.selectedIndex].value;
		sname = document.frm1.sname.options[document.frm1.sname.selectedIndex].text;
	} else {
		scustomer="";
		sname_id = "";
		sname = "";
	}

	if(document.frm1.spurpose.checked == true) { 
		spurpose = document.frm1.spurpose.value;
		pname = document.frm1.pname.options[document.frm1.pname.selectedIndex].value;
	} else { 
		spurpose="";
		pname = "";
	}

	if(document.frm1.subject.checked == true) subject = document.frm1.subject.value; else subject="";
	if(document.frm1.content.checked == true) content = document.frm1.content.value; else content="";
	if(document.frm1.result.checked == true) result = document.frm1.result.value; else result="";

	arrange1 = document.frm1.arrange1.options[document.frm1.arrange1.selectedIndex].value;
	arrange2 = document.frm1.arrange2.options[document.frm1.arrange2.selectedIndex].value;
	arrange3 = document.frm1.arrange3.options[document.frm1.arrange3.selectedIndex].value;
	//arrange가 같은면 메시지 출력
	if(arrange1 == arrange2) { arrange2=""; }
	if(arrange1 == arrange3) { arrange3=""; }
	if(arrange2 == arrange3) { arrange3=""; }

	var prg = 'reportServiceExcel.jsp?sdate='+sdate+"&stime="+stime+"&etime="+etime;
	prg += "&scompany=" + scompany + "&cname_id=" + cname_id + "&cname=" + cname;
	prg += "&scustomer=" + scustomer + "&sname_id=" + sname_id + "&sname=" + sname;
	prg += "&spurpose=" + spurpose + "&pname=" + pname;
	prg += "&subject=" + subject;
	prg += "&content=" + content;
	prg += "&result=" + result;
	prg += "&arrange1=" + arrange1;
	prg += "&arrange2=" + arrange2;
	prg += "&arrange3=" + arrange3;
	window.open(prg,'report_view','width=800,height=500,scrollbars=yes,toolbar=no,menubar=yes,status=yes,resizable=yes');

}
-->
</script>