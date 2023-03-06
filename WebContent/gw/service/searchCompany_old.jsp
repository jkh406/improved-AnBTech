<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "고객사 검색 및 리턴 처리"		
	contentType = "text/html; charset=euc-kr" 		
%>
<%@	page import="com.anbtech.text.Hanguel" %>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%	
	String search_item = request.getParameter("sItem")==null?"name":request.getParameter("sItem");
	String search_word = request.getParameter("sWord")==null?"*":request.getParameter("sWord");
	if(search_word != null) 
		search_word = new String(search_word.getBytes("ISO-8859-1"), "euc-kr");	

	String sql="";
	if(search_item.equals("name")){
		sql = "SELECT company_no,name_kor,name_eng,company_post_no,company_address,chief_name FROM company_customer WHERE name_kor LIKE '%"+search_word+"%' or name_eng LIKE '%"+search_word+"%' ORDER BY name_kor DESC";
	}else{
		sql = "SELECT company_no,name_kor,name_eng,company_post_no,company_address,chief_name FROM company_customer WHERE " + search_item + " LIKE '%"+search_word+"%' ORDER BY name_kor DESC";
	}
	bean.openConnection();	
	bean.executeQuery(sql);
%>


<HTML>
<HEAD>
<title>고객사 검색 및 선택</title>
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<!--<BODY topmargin="0" leftmargin="0" onLoad="javascript:centerWindow();">-->
<BODY topmargin="0" leftmargin="0">
<table width='100%' border='0' cellspacing='0' cellpadding='0' height='41'>
	<tr>
		<td background='../img/pop_title_bg.gif'><img src='../img/pop_title_search_company.gif'></td>
	</tr>
</table>

<table border="0" cellpadding="5" cellspacing="0" width="98%">
<tr><td align="right"><a href='javascript:self.close()'><img src='../img/002_013_del.gif' border='0'></a></td></tr></table>

<table border="0" cellpadding="2" cellspacing="0" width="590" align="center">
<tr><td align="left" width=10%><form method="post" action="searchCompany.jsp" name="sForm" onSubmit="return checkForm()">
		<SELECT name='sItem'>
			<OPTION value='name'>고객사명</OPTION>
			<OPTION value='chief_name'>대표자명</OPTION>
			<OPTION value='address'>주 소</OPTION>
		</SELECT> 
		<%	if(search_item !=null){	%>
			<script language='javascript'>
				document.sForm.sItem.value = '<%=search_item%>';
			</script>
		<%	}	%></td>
		<td align="left" width=20%><input type=text name=sWord size=20 style="border:1 solid #787878;"></td>
		<td align="left" width=70%><input type=submit value=찾기> <font color=red> * 검색어를 2자 이상 입력하세요.</font></td></tr></table>
<table border="0" cellspacing="1" width="590" bgcolor="#AFAFAF" style="border-style:none;" align="center">
  <tr>
	<td width="20%" height="20" bgcolor="#FFF9E2"><p align="center"><b>고객사명</b></td>
    <td width="15%" height="20" bgcolor="#FFF9E2"><p align="center"><b>대표자명</b></td>
	<td width="65%" height="20" bgcolor="#FFF9E2"><p align="center"><b>주소</b></td>
  </tr>
<%	while(bean.next()){
		String name = "<a href=\"javascript:returnValue('"+bean.getData("company_no")+"','"+bean.getData("name_kor")+"/"+bean.getData("name_eng")+"');\">"+bean.getData("name_kor")+"</a>";
%>
  <tr>
	<td width="20%" height="20" bgcolor="#FFFFFF">&nbsp;<%=name%></td>
    <td width="15%" height="20" bgcolor="#FFFFFF"><p align=center><%=bean.getData("chief_name")%></td>
	<td width="65%" height="20" bgcolor="#FFFFFF"><%=bean.getData("company_address")%></td>
  </tr>
<%	}	%>
</table>
</form>
</BODY>
</HTML>

<script language="javascript">

function centerWindow() 
{ 
        var sampleWidth = 630;                        // 윈도우의 가로 사이즈 지정 
        var sampleHeight = 400;                       // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

function returnValue(id,name)
{
	var f = opener.document.frm1;
	f.ap_id.value = id;
	f.ap_name.value = name;
	self.close();
}


function go()
{
	opener.opener.location.href = "../custominfo/customerl.jsp";
	opener.location.href = "../custominfo/customeri.jsp";
	self.close();
}

function changeAll()
{
	var f = document.mForm;
	if(f.sItem.options[0].selected){
		f.sItem.value = "name";
		f.sWord.value = "";
		f.submit();
	}

}
function checkForm()
{
	var f = document.sForm;

	if(f.sWord.value.length < 2){
			alert("검색어를 2단어 이상 입력하세요.");
			f.sWord.focus();
			return false;
	}
}
</script>