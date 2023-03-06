<%@ include file="../admin/configHead.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>
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

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title>�� ������ 5</title>
</head>

<body>

<form name="listForm" action="">
  <select size="15" name="user_list" multiple>
	 <OPTGROUP label='--------------'>
	 <%
		StringTokenizer share = new StringTokenizer(share_id,";");
		while(share.hasMoreTokens()){
			String sh = share.nextToken();
			if(sh.length() > 5) {
				sh = sh.trim()+";";
				out.println("<option value='"+sh+"'>"+sh+"</option>");
			}
		}
	 %>
  </select>
  <br>
  <input type='button' value='�����׸� ����' onClick='javascript:delSelected();'>
  <br><br>
  <input type='button' value='��������' onClick='javascript:transferList();'> 
  <input type='button' value='���' onClick='javascript:top.close();'>  
</form>

<form name="eForm" method='post'>
	<input type='hidden' name='share_id'>
	<input type='hidden' name='target' value='<%=target%>'>
	<input type='hidden' name='id' value='<%=id%>'>
	<input type='hidden' name='tablename' value='<%=tablename%>'>

	<input type='hidden' name='mode'>
</form>
</body>

</html>

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
