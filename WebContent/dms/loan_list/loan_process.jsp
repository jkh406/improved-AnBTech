<%@ include file= "../../admin/configPopUp.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "com.anbtech.text.Hanguel"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%
	String loan_period	= request.getParameter("loan_period")==null?"":request.getParameter("loan_period");
	String tablename	= request.getParameter("tablename");
	String searchscope	= request.getParameter("searchscope");
	String searchword	= Hanguel.toHanguel(request.getParameter("searchword"));
	String page_no		= request.getParameter("page");
	String no			= request.getParameter("no");
	String d_id			= request.getParameter("d_id");
	String ver			= request.getParameter("ver");
%>
<html>
<HTML><HEAD><TITLE>��������ó��</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>

<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_loan_p.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<form name="loanForm" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="35%" height="25" class="bg_03" background="../images/bg-01.gif">ó������</td>
           <td width="65%" height="25" class="bg_04">
				<select name="loan_mode">
					<option value="loan_commit">���� ó��</option>
					<option value="loan_reject">���� �ݷ�</option>
					<option value="loan_return">�ݳ� ó��</option>
				</select>
		   </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="35%" height="25" class="bg_03" background="../images/bg-01.gif">ó������</td>
           <td width="65%" height="25" class="bg_04"><textarea name='why' rows="3" cols="40"></textarea></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="35%" height="25" class="bg_03" background="../images/bg-01.gif">�����ϼ�</td>
           <td width="65%" height="25" class="bg_04"><INPUT type=text name="loanday" size=3 maxlength="3" value='<%=loan_period%>' class="text_01"> ��</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table></form>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:go();'><img src='../images/bt_confirm.gif' border='0' align='absmiddle'></a> <a href='javascript:self.close();'><img src='../images/bt_cancel.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>


<script language='javascript'>
<!--
function go() {
	var f = document.loanForm;

	//����ó�� �۾� �� �����ϼ��� �ݵ�� �Է��ϰ�
	if((f.loan_mode.value == "loan_commit") && (f.loanday.value == "")){
		
		alert("�����ϼ��� �Է��Ͻʽÿ�.");
		f.loanday.focus();

		return;
	}

	//����ó�� �ܿ��� �����ϼ��� 0���� �����.
	if(f.loan_mode.value == "loan_reject" || f.loan_mode.value == "loan_return"){
		f.loanday.value = "0";
	}

	//����Ⱓ�� ���ڸ� �Էµǰ�
	if(f.loan_mode.value == "loan_commit") {
		for(i=0; i<f.loanday.value.length; i++){
			var code = f.loanday.value.charCodeAt(i)
			var ch = f.loanday.value.substr(i,1)
			
			if(ch<"0"||ch>"9"){
				alert("�����ϼ��� �ùٷ� �Է��Ͻʽÿ�.")	
				return;
			}
		}
	}
	//alert(f.loanday.value);
//	window.returnValue = f.loan_mode.value + "|" + f.why.value + "|" + f.loanday.value;

	var mode = f.loan_mode.value;
	var why  = f.why.value;
	var loan_day = f.loanday.value;
	
	var sParam = "tablename=<%=tablename%>&searchscope=<%=searchscope%>&searchword=<%=searchword%>";
	sParam += "&page=<%=page_no%>&no=<%=no%>&d_id=<%=d_id%>&ver=<%=ver%>&mode="+mode+"&why="+why+"&loan_day="+loan_day;;
	opener.location.href = "../../servlet/AnBDMS?" + sParam;
	self.close();

}

//-->
</script>