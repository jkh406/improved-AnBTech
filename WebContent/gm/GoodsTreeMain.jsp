<%@ include file="../admin/configPopUp.jsp"%>
<%@ page
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		="java.sql.*, java.io.*, java.util.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<HTML><HEAD><TITLE>��ǰƮ������</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <TR><TD align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="images/pop_view_t.gif" hspace="10" alt="��ǰƮ������"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<TABLE cellspacing=0 cellpadding=0 width="100%" border=0>
	   <TBODY>
         <TR>
		   <TD height=22>&nbsp;</TD></TR>
         <TR bgcolor="c7c7c7"><TD height=1></TD></TR>
         <TR>
           <TD width="100%" style="padding-left:4px" background="images/bg-011.gif"><!-- ���Խ��� -->
			<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="230">
			  <tbody>
			  <tr>
				<td valign='top'>
					<iframe name="tree" src="GoodsTree.jsp" width="320" height="310" border="0" frameborder="0" scrolling=yes>
					�������� �ζ��� �������� �������� �ʰų� ���� �ζ��� �������� ǥ������ �ʵ��� �����Ǿ� �ֽ��ϴ�.</iframe>
				</td>
			  </tr>
			  <tr><td width="100%" height="30" bgcolor="#EAF3FD" align='middle'>
				<form name='sForm' method=post action="javascript:go_search();" onSubmit="go_search();" style="margin:0">
				<select name='searchscope'><option value='code'>���ڵ�</option><option value='name'>�ѱ۸𵨸�</option><option value='name2'>�����𵨸�</option></select> <input type='text' name='searchword' size='10'>
				<a href='javascript:go_search();'><img src='images/bt_search.gif' border='0' align='absmiddle'></a>
				</form>
			  </td></tr></tbody></table><!-- ���Գ� -->   
		   </TD></TR>
         <TR bgcolor="C7C7C7"><TD height="1"></TD></TR>
         <TR><TD height=20></TD></TR></TBODY></TABLE>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:self.close();"><img src="images/close.gif" width="46" height="19" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></TD></TR></table></BODY></HTML>

<script language='javascript'>
<!--
function go_search()
{
	var f = document.sForm;
	var searchscope = f.searchscope.value;
	var searchword = f.searchword.value;

	if(searchword.length < 2){
		alert("�˻���� 2�� �̻��̾�� �մϴ�.");
		return;
	}

	tree.location.href = "GoodsTree.jsp?searchscope="+searchscope+"&searchword="+searchword;
}
//-->
</script>