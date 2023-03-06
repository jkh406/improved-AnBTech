<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= "고객 정보 검색 및 리턴 처리"		
	contentType = "text/html; charset=euc-kr"
	import		= "com.anbtech.text.Hanguel"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%	
	String no = request.getParameter("no")==null?"1":request.getParameter("no");
	String ap_id = request.getParameter("ap_id")==null?"":request.getParameter("ap_id");
	String name = request.getParameter("name")==null?"":Hanguel.toHanguel(request.getParameter("name"));

	String sql="SELECT mid,company_no,name_kor,name_eng,division_name,position_rank FROM personal_customer WHERE company_no = '"+ap_id+"' and (name_kor like '%"+name+"%' or name_eng like '%"+name+"%') order by name_kor desc";

	bean.openConnection();	
	bean.executeQuery(sql);
%>
<HTML><HEAD><TITLE>고객사별 고객명 검색</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_cus_sel.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>


	<table border="0" cellpadding="5" cellspacing="0" width="100%">
	  <TR>
          <TD height="5"></TD></TR>
	  <tr>
		<td width="50%" align=""><form name=frm1 method=post action='chooseCustomer.jsp'>
			<select name='ap_id' SIZE=15 onChange="returnValue();">
				<OPTGROUP label='-------------------------'>
	<%	while(bean.next()){	%>
				<option value='<%=bean.getData("mid")%>'><%=bean.getData("division_name")%>/ <%=bean.getData("position_rank")%>/<%=bean.getData("name_kor")%></option>
	<%	}	%>
			</select>
		</td></form>
		<td width="50%" valign="top">
			<TABLE cellSpacing=0 cellPadding=0 width="100%" height="70" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD colspan='5'>	

			<!-- 내용 -->
			<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="223" align='left'>
				<TR height='25px;'><TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='../images/title_bm_bg.gif' colspan='5'><font color="#4D91DC"><b>고객선택</b></font></td></TR>
				<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
				<TR><TD width="100%" height="130" valign="middle" bgcolor="F5F5F5" style='padding-left:5px;padding-right:5px;'>
					<font color="565656">
						왼쪽 목록에 대상 고객명이 나타나지 <br>않으면 대상 고객이 고객관리 DB에<br>저장되어 있지 않은
			경우입니다. <br><br>이런 경우에는 고객관리 DB에 해당<br>고객 정보를 먼저 등록하신 후에 진행<br>하십시요.
					</font>
					</TD></TR></TABLE>

			</TD></TR></TABLE>
			
			
		</td>
	  </tr>
	</table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
		   <TR>
            <TD width="100%" height=5 colSpan=4></TD>
          </TR>
		  <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../images/bt_close.gif" hspace="10" border="0"></a></TD>
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
			opener.document.frm1.at_id<%=no%>.value = f.options[i].value;

			tmp = f.options[i].text.split("/")
			opener.document.frm1.at_name<%=no%>.value = tmp[1] + " " + tmp[2];
//			opener.document.frm1.at_name<%=no%>.value = f.options[i].text;
			break;
		}
    }
	this.close();
}


function go()
{
	opener.opener.location.href = "../../servlet/CrmServlet?mode=customer_write";
	opener.close();
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

</script>