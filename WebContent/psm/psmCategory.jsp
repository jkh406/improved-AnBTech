<%@ include file="../admin/configPopUp.jsp"%>
<%@ page		
	info= "����ī�װ��˻��ϱ� : SPACK LINK��"		
	contentType = "text/html; charset=KSC5601" 
	errorPage	= "../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.psm.entity.*"
	import="com.anbtech.psm.db.*"
	import="com.anbtech.psm.business.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();

	//���� ���޹ޱ�
	String env_status = request.getParameter("env_status");		//��������[1:����,2:����]
	String target = request.getParameter("target");
	String target_name = "";
	String target_cate = "";
	int sh = target.indexOf("/");
	if(sh != -1) {
		target_name = target.substring(0,sh);
		target_cate = target.substring(sh+1,target.length());
	} else {
		target_name = target;
		target_cate = target;
	}

	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String sItem = Hanguel.toHanguel(request.getParameter("sItem")); if(sItem == null) sItem = "comp_korea";
	String sWord = Hanguel.toHanguel(request.getParameter("sWord")); if(sWord == null) sWord = "";
	
	//---------------------------------------------
	//	DB�� �����Ͽ� �ش系�� ��������
	//---------------------------------------------
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.psm.db.psmModifyDAO psmDAO = new com.anbtech.psm.db.psmModifyDAO(con);

	//����ī�װ����� ������ �б�
	ArrayList cate_list = new ArrayList();
	cate_list = psmDAO.readPsmCategoryList(env_status,sItem,sWord);	
	
	psmCategoryTable cate = new psmCategoryTable();
	Iterator cate_iter = cate_list.iterator();
	int cat_cnt = cate_list.size();

	String[][] category;
	if(cat_cnt > 0) category = new String[cat_cnt][4];		//�����Ͱ� ������
	else {													//�����Ͱ� ������
		category = new String[1][4];
		category[0][0]=category[0][1]=category[0][2]=category[0][3]="";
	}

	int p = 0;
	while(cate_iter.hasNext()) {
		cate = (psmCategoryTable)cate_iter.next();
		category[p][0] = cate.getKoreaName();
		category[p][1] = cate.getEnglishName();
		category[p][2] = cate.getCompKorea(); 
		category[p][3] = cate.getCompEnglish(); 
		p++;
	}
	connMgr.freeConnection("mssql",con);		//�ݱ�
%>

<HTML>
<HEAD>
<title>������ �� ī�װ� ã�� </title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../psm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">

	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../psm/images/pop_psm_search.gif" border='0' align='absmiddle' alt='����ī�װ�ã��'></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<!-- ���� -->
	<table cellspacing=0 cellpadding=0 width="94%" border=0>
	   <tbody>
         <tr><td height="12"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1></td></tr>
         <tr>
           <td width="50%" style="padding-left:4px" background="../psm/images/bg-011.gif">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
				  <tr><td width="100%" height="250" valign='top'>
						<form name="eForm" method="post" style="margin:0">
						<select MULTIPLE size=17 name='category' onChange='javascript:goProject()' style=font-size:9pt;color='black';>
						<OPTGROUP label='-------------------------------------'>
						<%
							for(int i=0; i<cat_cnt; i++) {
								out.print("<option value='"+category[i][3]+"|"+category[i][0]+"'>");
								out.print(category[i][2]+"["+category[i][3]+"] / ");
								out.println(category[i][0]+"["+category[i][1]+"] </option>");
							}
						%></select>
						<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
						<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
						</form>
					</td></tr>
					<tr><td width="100%" height="30" bgcolor="#EAF3FD"  align="center">
						<form name="sForm" method="post" style="margin:0">
						<select name="sItem" style=font-size:9pt;color="black";>  
						<%
							String[] sitems = {"comp_korea","comp_english","korea_name","english_name"};
							String[] snames = {"��ü��(�ѱ�)","��ü��(����)","ī�װ�(�ѱ�)","ī�װ�(����)"};
							String sel = "";
							for(int si=0; si<sitems.length; si++) {
								if(sItem.equals(sitems[si])) sel = "selected";
								else sel = "";
								out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
							}
						%>
						</select>
						<input type="text" name="sWord" size="20" value="<%=sWord%>">
						<input type='hidden' name='target_name' value='<%=target_name%>'>
						<input type='hidden' name='target_cate' value='<%=target_cate%>'>
						<input type='hidden' name='target' value='<%=target%>'>
						<input type='hidden' name='env_status' value='<%=env_status%>'>
						<a href='Javascript:goSearch();'><img src='../psm/images/bt_search3.gif' border='0' align='absmiddle'></a>
						</form>
				    </td></tr>
				</table>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height="13" colspan="2"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:self.close();"><img src="../psm/images/close.gif" width="46" height="19" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE>
	
	</td></tr>
</table>

</BODY>
</HTML>

<script language=javascript>
<!--
//�˻��ϱ�
function goSearch()
{
	document.sForm.action='psmCategory.jsp';
	document.sForm.submit();
}
//���������ϱ�
function goProject()
{
	var env_status = document.sForm.env_status.value;

	var category = document.eForm.category.value;
	var cate = category.split('|');

//	if(env_status == '1') {			//�������
//		name = "����";
//		category = cate[1];
//	} else {						//���İ���
		name = cate[0];
		category = cate[1];
//	}

	//opener�� �����ϱ�
	var target_name = document.sForm.target_name.value; 
	var target_cate = document.sForm.target_cate.value; 
	eval("opener.document." + target_name).value = name; 
	eval("opener.document." + target_cate).value = category; 
	self.close();

}

-->
</script>