<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= ""	
	errorPage	= "../../admin/errorpage.jsp"
	contentType = "text/html; charset=euc-kr" 		
%>
<%@	page import="com.anbtech.text.Hanguel" %>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%	
	String search_item = request.getParameter("sItem")==null?"item_no":request.getParameter("sItem");
	String search_word = request.getParameter("sWord")==null?"*":request.getParameter("sWord");
	if(search_word != null) 
		search_word = new String(search_word.getBytes("ISO-8859-1"), "euc-kr");	

	String sql="";
	if(search_item.equals("item_no")){
		
	}else if(search_item.equals("model_code")){
		
	}else {
		
	}

	
%>


<HTML>
<HEAD>
<title>Bom 정보 가져오기</title>
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
<tr><td align="left" width=10%><form method="post" action="searchBomInfo.jsp" name="sForm" onSubmit="return checkForm()">
		<SELECT name='sItem'>
			<OPTION value='name'>모델명</OPTION>
			<OPTION value='model_code'>모델코드</OPTION>
			<OPTION value='item_no'>F/G코드</OPTION>
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
	<td width="20%" height="20" bgcolor="#FFF9E2"><p align="center"><b>제품군</b></td>
    <td width="20%" height="20" bgcolor="#FFF9E2"><p align="center"><b>제품명</b></td>
	<td width="20%" height="20" bgcolor="#FFF9E2"><p align="center"><b>모델군</b></td>
	<td width="20%" height="20" bgcolor="#FFF9E2"><p align="center"><b>모델명</b></td>
	<td width="20%" height="20" bgcolor="#FFF9E2"><p align="center"><b>F/G코드</b></td>
  </tr>

  <tr>
	<td width="20%" height="20" bgcolor="#FFFFFF"></td>
    <td width="20%" height="20" bgcolor="#FFFFFF"></td>
	<td width="20%" height="20" bgcolor="#FFFFFF"></td>
	<td width="20%" height="20" bgcolor="#FFFFFF"></td>
	<td width="20%" height="20" bgcolor="#FFFFFF"></td>
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

function returnValue(id,name,post_no,address)
{
	var f = opener.document.writeForm;
	f.company_no.value	= id;
	f.company_name.value = name;
	f.company_post_no.value = post_no;
	f.company_address.value = address;

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
