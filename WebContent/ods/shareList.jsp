<%@ include file="../admin/configPopUp.jsp"%>
<%@ page language="java"
    contentType="text/html;charset=KSC5601" 
	errorPage = "../admin/errorpage.jsp" 
%>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	String target = request.getParameter("target");
	String id = request.getParameter("id");	if(id == null) id = "";
	String tablename = request.getParameter("tablename"); if(tablename == null) tablename = "";
	String mode = Hanguel.toHanguel(request.getParameter("mode"));
	String query = "";
	/*********************************************************************
	 	��ܰ��� ������ ������ �ʿ��� �μ��ڵ� ã��
	*********************************************************************/
	String ac_code = "";
	String[] idColumn = {"b.ac_code"};
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(query);
	bean.init_write();

	if(bean.isAll()) {
		ac_code = bean.getData("ac_code");				//�ش��� �μ��ڵ�
	} //while

	/*********************************************************************
	 	���������� ������ ã��
		mode : IDR_S, ODR_S [�μ������� mail_add�� �а�]
		mode : ODR_G [�μ����� �д´�]
	*********************************************************************/	
	String share_id = "";		//mode������ �б�
	String read_col = "";		//���� �÷���
	if(mode.equals("IDR_S") || mode.equals("ODR_S")) read_col = "mail_add";
	else if(mode.equals("ODR_G")) read_col = "receive";
	
	//�系���� ������ ����
	if(mode.equals("IDR_S")) {
		String[] aColumn = {"mail_add"};
		bean.setTable(tablename);			
		bean.setColumns(aColumn);
		query = "where id ='"+id+"'";
	}
	//��ܰ��� ������ ����
	else if(mode.equals("ODR_S")) {
		String[] bColumn = {"a.mail_add"};
		bean.setTable("OutDocShare_receive a,OutDocument_receive b");			
		bean.setColumns(bColumn);
		query = "where a.id ='"+id+"' and a.id = b.id and a.ac_code='"+ac_code+"'";
	}
	//��ܰ��� ������[�μ���] ���� 
	else if(mode.equals("ODR_G")) {
		 String[] cColumn = {"receive"};
		bean.setTable(tablename);			
		bean.setColumns(cColumn);
		query = "where id ='"+id+"'";
	}
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		share_id += bean.getData(read_col);		//mode���� ���� �б�
	} //while
	
%>


<HTML>
<HEAD>
<META content="text/html;charset=euc-kr" http-equiv=content-type>
<LINK href="css/style.css" rel=stylesheet>
</HEAD>

<BODY topmargin="0" leftmargin=5 marginwidth=0>

<!-- ��ܿ��� -->
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- �׵θ� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="120" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD>

	<!--����-->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="223" align='center'>
		<TR height='25px;'>
			<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;" BACKGROUND='images/title_bm_bg.gif'>
			<font color="#4D91DC"><b>���õ� ������ ����Ʈ</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD height='4'></TD></TR>
		<TR><TD width="100%" height="130" valign="top" align='middle'>
			<!-- ���� ����Ʈ ����-->
			<form name="listForm" method="post" style="margin:0">
			<select name="user_list" multiple size="8">
			<OPTGROUP label='--------------------'>
		<%
				StringTokenizer share = new StringTokenizer(share_id,";");
			while(share.hasMoreTokens()){
			String sh = share.nextToken();
			if(sh.length() > 5) {
				sh = sh.trim()+";";
				out.println("<option value='"+sh+"'>"+sh+"</option>");
			}
		}
		%>	</select></FORM></TD></TR>
		<TR><TD width="100%" height="25" valign='top' style='padding-left:10px;'>
				<a href='javascript:delSelected();'><img src='images/bt_del_sel.gif' border='0'></a></td></TR>
	</TABLE>

</TD></TR></TABLE><!--�׵θ� ��-->

<!-- �߰����� -->
<TABLE><TR><TD height='7px;'></TD></TR></TABLE>

<!-- �׵θ� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="70" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD colspan='5'>	

	<!-- ���� -->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="223" align='left'>
		<TR height='25px;'><TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='images/title_bm_bg.gif' colspan='5'><font color="#4D91DC"><b>���� ���޴�� ���ù��</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD width="100%" height="100" valign="middle" bgcolor="F5F5F5" style='padding-left:5px;padding-right:5px;'>
			<font color="565656">
				1. ���޴�� �������� �˻�ȭ�鿡��<br>&nbsp;&nbsp;&nbsp;&nbsp;�˻��Ѵ�.<br>
				2. �˻��� �������� �̸��� Ŭ���Ͽ�<br>&nbsp;&nbsp;&nbsp;&nbsp;����Ʈ�� �߰��Ѵ�.<br>
				3. ���ÿϷ� ��ư�� ���� �����Ѵ�.	
			</font>
			</TD></TR></TABLE>
<form name="eForm" method='post' style="margin:0">
	<input type='hidden' name='share_id'>
	<input type='hidden' name='target' value='<%=target%>'>
	<input type='hidden' name='id' value='<%=id%>'>
	<input type='hidden' name='tablename' value='<%=tablename%>'>

	<input type='hidden' name='mode'>
</form>
</TD></TR></TABLE><!--�׵θ�-->

</BODY>
</HTML>



<script language="javascript">

//����Ʈ���� �׸� �����ϱ�
function delSelected()
{
	var num = document.listForm.user_list.selectedIndex;
	if(num < 0){
		alert("������ ����� ������ �ֽʽÿ�.");
		return;
	}

	var Frm = document.listForm.user_list;
	var len = Frm.length;
	for (i=len-1;i>=0 ;i--) {
        if(Frm.options[i].selected == true) Frm.options[i] = null;
    }	
}
//����Ʈ�� �ִ� ���� opener �� ������
function transferList()
{
	var from = document.listForm.user_list;

	var user_list = "";
	for(i=0;i<from.length;i++) {
		user_list += from.options[i].value + "\n";
	} //for
	
	var mode = '<%=mode%>';
	if(mode == "IDR_S") {			//�系���Ź��� ��������[����]
		document.eForm.action='../servlet/InDocumentRecServlet';
		document.eForm.share_id.value = user_list;
		document.eForm.mode.value='IDR_S';
		document.eForm.submit();
	} else if (mode == "ODR_S") {	//��ܼ��Ź��� ��������[����]
		document.eForm.action='../servlet/OutDocumentRecServlet';
		document.eForm.share_id.value = user_list;
		document.eForm.mode.value='ODR_S';
		document.eForm.submit();
	} else if (mode == "ODR_G") {	//��ܼ��Ź��� ����[�μ�]
		document.eForm.action='../servlet/OutDocumentRecServlet';
		document.eForm.share_id.value = user_list;
		document.eForm.mode.value='ODR_G';
		document.eForm.submit();
	}
}

function addUsers(item) // item == usr|A030003/�븮/�ڵ��� , div|34/��ſ�����
{
    var fromField=item.split("|");			//�����(usr) or �μ�(div) ����	
    var type =  fromField[0];				//usr or div
	var user = fromField[1];				//������

	var fromField2=user.split("/");			// ���/����/�̸�
	var id;
	var name;
	var rank;

	var mode = '<%=mode%>';					//������ �μ������� �μ������� �Ǵ�

	if(type == "usr"){	id = fromField2[0]; 	rank = fromField2[1]; 	name = fromField2[2];}
	else{				id = fromField2[0];		rank = "";				name = fromField2[1];}
	user = id + "/" + name + ";";

	if((mode == "IDR_S") || (mode == "ODR_S")) {	
		if(type == "div") { alert("�μ����� �����Ͻʽÿ�."); 	return; }
	} else if(mode == "ODR_G") {						
		if(type == "usr") { alert("�μ����� �����Ͻʽÿ�."); 	return; }
	}

	var where_list = document.listForm.user_list;

	//�ߺ� �߰��� �� ���� ó��
	var length = where_list.length;
	for(j=0;j<length;j++) {	
		if(where_list.options[j].value == user) {
			alert('[�ߺ�]�̹� �߰��� �׸��Դϴ�.');
			return;
		}
	}
	//����Ʈ�� �߰�
	var option0 = new Option(id+"/"+name+";",id+"/"+name+";");	//text,value
	where_list.options[length] = option0;
}

//-->
</script>
