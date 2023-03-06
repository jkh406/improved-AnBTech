<%@ include file="../../admin/configPopUp.jsp"%>
<%@ include file="../../admin/chk/chkCM01.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
    bean.openConnection();	

	String query		= "";
	String caption		= "";
	String j			= request.getParameter("j");		// 모드	
	String id			= request.getParameter("id");		// 관리번호

	String code			= "";								// 품목코드
	String name			= "";								// 표준품목명
	String desc			= "";								// 품목설명

	String ancestor		= request.getParameter("ancestor") == null?"0":request.getParameter("ancestor");
	String level		= request.getParameter("level") == null?"1":request.getParameter("level"); // 해당 분류의 레벨

	if( j.equals("u")){ // 수정모드
		query = "SELECT * FROM item_class WHERE mid = '" + id +"'";
		bean.executeQuery(query);

		while(bean.next()){
			code		= bean.getData("item_code");
			name		= bean.getData("item_name");
			desc		= bean.getData("item_desc");
			level		= bean.getData("item_level");
		}
		caption = "수정";
	}else if(j.equals("a")){ // 현재분류의 하위분류 입력모드
		caption = "추가";
	}
%>

<HTML><HEAD><TITLE>품목분류<%=caption%></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>

<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<form name="frm1" method="post" action="category_input_p.jsp" onSubmit="return checkForm()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_add_item.gif" hspace="10" alt="품목분류관리"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody><form name="frm1" style="margin:0">
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">품목분류코드</td>
           <td width="70%" height="25" class="bg_04"><input type='text' name='code' value='<%=code%>' size='5' maxlength="5" class="text_01"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">품목분류명</td>
           <td width="70%" height="25" class="bg_04"><input type='text' name='name' value='<%=name%>' size='20' maxlength="20" class="text_01"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">품목분류설명</td>
           <td width="70%" height="25" class="bg_04"><input type='text' name='desc' value='<%=desc%>' size='40' maxlength="40"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 </tbody></table><br>
	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"> 
			<IMG src='../images/bt_save.gif' onclick='javascript:checkForm()' align='absmiddle' style='cursor:hand'>
		    <IMG src='../images/bt_cancel.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE>
<input type='hidden' name='j' value='<%=j%>'>
<input type='hidden' name='id' value='<%=id%>'>
<input type='hidden' name='level' value='<%=level%>'>
<input type='hidden' name='ancestor' value='<%=ancestor%>'>		
</form></td></tr></table></BODY></HTML>


<script language=javascript>

function checkForm()
{
	var f = document.frm1;

	if(f.code.value == ''){
			alert("품목코드를 입력하십시오.");
			f.code.focus();
			return;
	}
	if(f.level.value == '1' && f.code.value.length != '1'){
			alert("대분류 품목코드는 영문대문자 또는 숫자 1자릿수로 입력하셔야 합니다.");
			f.code.focus();
			return;
	}
	if(f.level.value == '2' && f.code.value.length != '3'){
			alert("중분류 품목코드는 영문대문자 또는 숫자 3자릿수로 입력하셔야 합니다.(대분류품목코드 + 2자릿수)");
			f.code.focus();
			return;
	}
	if(f.level.value == '3' && f.code.value.length != '5'){
			alert("소분류 품목코드는 영문대문자 또는 숫자 5자릿수로 입력하셔야 합니다.(대분류품목코드 + 중분류품목코드 + 2자릿수)");
			f.code.focus();
			return;
	}

	if(f.name.value == ''){
			alert("표준품목명을 입력하십시오.");
			f.name.focus();
			return;
	}

	f.submit();
}
</script>