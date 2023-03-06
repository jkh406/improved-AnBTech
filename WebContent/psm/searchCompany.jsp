<%@ include file="../admin/configPopUp.jsp"%>
<%@ page 
	info= "업체정보 검색"	
	language="java" contentType="text/html;charset=KSC5601" 
	errorPage	= "../admin/errorpage.jsp"
	import="java.sql.*, java.io.*, java.util.*, com.anbtech.text.Hanguel"%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//초기화
	//사업자등록번호,한글업체명,영문업체명
	String[] data = new String[3];
	for(int i=0; i<3; i++) data[i]="";

	//자체적으로 
	data[0] = request.getParameter("target_id");		if(data[0] == null) data[0]="";
	data[1] = request.getParameter("target_kor");		if(data[1] == null) data[1]="";
	data[2] = request.getParameter("target_eng");		if(data[2] == null) data[2]="";
	String sWord = Hanguel.toHanguel(request.getParameter("sWord"));		if(sWord == null) sWord = "";

	//넘겨줄 파라미터 처리하기 : from opener 로 부터
	if(data[0].length() == 0) {
		String target = request.getParameter("target");
		StringTokenizer list = new StringTokenizer(target,"/");
		int t=0;
		while(list.hasMoreTokens()) {
			data[t] = list.nextToken();
			t++;
		}
	}

	//공정코드 읽기
	String sql = "";
	bean.openConnection();

	sql = "SELECT count(*) FROM company_customer where name_kor like '%"+sWord+"%'";
	bean.executeQuery(sql);
	bean.next();
	int cnt = Integer.parseInt(bean.getData(1));
	String[][] industry = new String[cnt][3];

	sql = "SELECT * FROM company_customer where name_kor like '%"+sWord+"%' order by name_kor asc";
	bean.executeQuery(sql);
	int n=0;
	while(bean.next()) {
		industry[n][0] = bean.getData("company_no");				//사업자등록번호
		industry[n][1] = bean.getData("name_kor");					//이름(한글)
		industry[n][2] = bean.getData("name_eng");					//이름(영문)
		n++;
	}

%>

<HTML><HEAD><TITLE>업체정보 검색</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<form name='sForm' method='post' style="margin:0">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">

	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="./images/pop_company_search.gif" border='0' align='absmiddle' alt='업체정보검색'></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<!-- 본문 -->
	<table cellspacing=0 cellpadding=0 width="94%" border=0>
	   <tbody>
         <tr><td height="12"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1></td></tr>
         <tr>
           <td width="50%" style="padding-left:4px" background="./images/bg-011.gif">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
				  <tr><td width="100%" height="250" valign='top'>
						<select MULTIPLE size='17' name='industry' onChange='javascript:goIndustry()' style=font-size:9pt;color='black';>
						<OPTGROUP label='----------------------'>
				<%
				for(int i=0; i<cnt; i++) {
					out.print("<option  value='");
					out.print(industry[i][0]+"|"+industry[i][1]+"|"+industry[i][2]+"'>");
					out.println(industry[i][0]+" "+industry[i][1]+"</option>");
				}
				%>
						</select>
					</td></tr>
					<tr><td width="100%" height="30" bgcolor="#EAF3FD"  align="center">
						업체명 <input type='text' name='sWord' size='10' value='<%=sWord%>'> <a href="javascript:go_search();"><img src="./images/bt_search.gif" border="0" align="absmiddle"></a>
				    </td></tr>
				</table>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height="13" colspan="2"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:self.close();"><img src="./images/close.gif" width="46" height="19" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE>
	
	</td></tr>
</table>
<input type='hidden' name='target_id' value='<%=data[0]%>'>
<input type='hidden' name='target_kor' value='<%=data[1]%>'>
<input type='hidden' name='target_eng' value='<%=data[2]%>'>
</form>
</BODY>
</HTML>
<script language='javascript'>
<!--
//선택하기
function goIndustry()
{
	var industry = document.sForm.industry.value;
	var comp = industry.split('|');

	var target_id = document.sForm.target_id.value; 
	var target_kor = document.sForm.target_kor.value; 
	var target_eng = document.sForm.target_eng.value; 

	eval("opener.document." + target_id).value = comp[0]; 
	eval("opener.document." + target_kor).value = comp[1]; 
	eval("opener.document." + target_eng).value = comp[2]; 
	self.close();
}
//검색하기
function go_search()
{
	document.sForm.action='searchCompany.jsp';
	document.sForm.submit();
}
//-->
</script>