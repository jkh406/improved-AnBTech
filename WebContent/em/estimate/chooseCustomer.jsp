<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*"
%>
<%@	page import="com.anbtech.text.Hanguel" %>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%	
	String ap_id = request.getParameter("ap_id")==null?"":request.getParameter("ap_id");
	String name = request.getParameter("name")==null?"":Hanguel.toHanguel(request.getParameter("name"));

	String sql="SELECT mid,company_no,name_kor,name_eng,division_name,position_rank FROM personal_customer WHERE company_no = '"+ap_id+"' and (name_kor like '%"+name+"%' or name_eng like '%"+name+"%') order by name_kor desc";

	bean.openConnection();	
	bean.executeQuery(sql);
%>
<HTML><HEAD><TITLE>���纰 ���� �˻�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_cus_sel.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>


	<table border="0" cellpadding="5" cellspacing="0" width="100%">
	  <TR>
          <TD height="5"></TD></TR>
	  <tr>
		<td width="50%" align=""><form name=frm1 method=post action='chooseCustomer.jsp'>
			<select name='ap_id' SIZE=15 onChange="returnValue();">
				<OPTGROUP label='�˻��� ������ Ŭ���ϼ���.'>
				<OPTGROUP label='-------------------------'>
	<%	while(bean.next()){	%>
				<option value='<%=bean.getData("mid")%>'><%=bean.getData("division_name")%>/ <%=bean.getData("position_rank")%>/<%=bean.getData("name_kor")%></option>
	<%	}	%>
			</select>
		</td></form>
		<td width="50%" valign="top">
			���� ��Ͽ� ��� ������ ��Ÿ���� ������ ��� ���� ������ DB�� ����Ǿ� ���� ����
			����Դϴ�. <br><br>�̷� ��쿡�� ������ DB�� �ش� �� ������ ���� ����Ͻ� �Ŀ� �����Ͻðų� ���� �Է��Ͻʽÿ�.
		</td>
	  </tr>
	</table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
		   <TR>
            <TD width="100%" height=5 colSpan=4></TD>
          </TR>
		  <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../images/close.gif" width="46" height="19" hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table>
</BODY></HTML>


<script language="javascript">

function returnValue()
{
	var f = document.frm1.ap_id;
	var tmp;
	for (i=0;i<f.length;i++)
    {
        if(f.options[i].selected == true){
			tmp = f.options[i].text.split("/")
			opener.document.writeForm.charge_name.value = tmp[2];
			opener.document.writeForm.charge_rank.value = tmp[1];
			opener.document.writeForm.charge_div.value = tmp[0];
			break;
		}
    }
	this.close();
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

</script>