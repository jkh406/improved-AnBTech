<%@ include file="../../admin/configPopUp.jsp"%>

<%@ page		
	info= "�׸� �����ϱ�"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="java.util.StringTokenizer"
%>
<%@	page import="com.anbtech.text.Hanguel" 								%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	String id = "";				//������ ID
	String items = "";			//���������׸�
	String comm_items = "";		//���������׸�
	String NY = "";				//�űԵ�� ���� ��������
	
	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������

	/*********************************************************************
	 	����� login �˾ƺ���
	*********************************************************************/
	id = login_id;

	/*****************************************************
	// ���� �����׸��� ã�´�.
	*****************************************************/
	String[] commColumns = {"id","item","nlist"};
	String comm_data = "where item='CIT'";
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(commColumns);
	bean.setSearchWrite(comm_data);
	bean.init_write();
	
	comm_items = "";					//���� �׸�LIST (������ : ';')
	while(bean.isAll()) {
		comm_items += bean.getData("nlist");
	}	

	/*********************************************************************
	 	�׸��߰�/���� �����ϱ�
	*********************************************************************/
	NY = Hanguel.toHanguel(request.getParameter("NY"));
	items = Hanguel.toHanguel(request.getParameter("Ritems"));
	String REQ = request.getParameter("req"); //��û����
	if(REQ != null) {
		//���� ��û�� ó���ϱ�
		if(REQ.equals("DEL")) {			
			String delD = Hanguel.toHanguel(request.getParameter("items"));	//������ �����׸�
			if(delD != null) {
				String upitems = str.repWord(items,delD+";","");
				String delData = "update calendar_common set nlist='" + upitems + "' where id='" + id + "' and item='IIT'";
				bean.execute(delData);
			} //if (del)

		//�߰� ��û�� ó���ϱ�
		} else if (REQ.equals("ADD")) { //�߰�
			String addD = Hanguel.toHanguel(request.getParameter("add_item"));	//�߰��� �����׸�
			if(addD != null) {
				String additems = "";	//�߰��� �����׸�
				String addData = "";	//query ����
				//�ű��߰�
				if(NY.equals("new")) {
					additems = addD;
					addData = "insert into calendar_common (pid,id,item,nlist) values('"+bean.getID()+"',";
					addData += "'"+id+"','IIT','"+additems+"')";
				//update
				} else {
					additems = items+addD;
					addData = "update calendar_common set nlist='" + additems + "' where id='" + id + "' and item='IIT'";
				}
				bean.execute(addData);
			}//if (add)
		} //if (del/add)
	} //if (��ü)
	
	/*****************************************************
	//���� �����׸� ��������
	*****************************************************/
	String[] itemColumns = {"nlist"};
	String item_data = "where item='IIT' and id='" + id + "' order by nlist DESC"; 
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(itemColumns);
	bean.setSearchWrite(item_data);
	bean.init_write();
	
	items = "";							//���� �����׸�LIST (������ : ';')
	NY = "";							//�ű� clear
	if(bean.isEmpty()) { 
		NY = "new";						//�űԵ����
		items = "";						//������ ����
	} else {
		while(bean.isAll()) items += bean.getData("nlist");
	}	

%>
<HTML><HEAD><TITLE>�����׸� ����</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_sch_i.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody><form name="sForm" action="Calendar_addItem.jsp" method="post" style="margin:0">
         <tr><td height=20 colspan="4"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="40%" height="25" class="bg_01" background="../images/bg-01.gif">
				<table border="0">
				<tr><td><b>�����׸��</b><br><input type="text" name="add_item" size="15"></td></tr>
				<tr><td height="30" align="center"><a href="javascript:addItem();"><img src='../images/bt_reg.gif' border='0' align='absmiddle'></a> <a href="javascript:delItem();"><img src='../images/bt_del.gif' border='0' align='absmiddle'></a></td></tr></table>
		   </td>
           <td width="60%" height="25" colspan="3" class="bg_02">
				<select name="items" size="5">
				<OPTGROUP label='----------------'>
				<% //����� ���������׸� �ҷ�����
					StringTokenizer strs = new StringTokenizer(items,";");
					while(strs.hasMoreTokens()) {
						String item=strs.nextToken();
						out.println("<option value='"+item+"'>" + item + "</option>");
					}
				%>
				</select>
				<input type="hidden" name="req">
				<input type='hidden' name='Ritems' value='<%=items%>'>
				<input type='hidden' name='NY' value='<%=NY%>'>
				</form></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style='padding-right:10px'><a href="javascript:closeItem();"><img src="../images/close.gif" border="0" align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<Script language = "Javascript">
 <!-- 
//�׸� �����ϱ� 
function delItem()
{
	var num = document.sForm.items.selectedIndex;
	if(num < 0){
		alert("������ �׸��� ������ �ֽʽÿ�.");
		return;
	}
	if(!confirm("�����Ͻðڽ��ϱ�?"))
		return;
	document.sForm.action="Calendar_addItem.jsp";
	document.sForm.req.value="DEL";
	document.sForm.items.options[num].value;
	document.sForm.submit();
}

//�׸� �߰��ϱ� 
function addItem()
{
	var data = document.sForm.add_item.value;
	var comm = '<%=comm_items%>';
	var indi = '<%=items%>';
	if(data == ""){
		alert("�߰��� �׸��� �Է��� �ֽʽÿ�.");
		return;
	} else if(comm.indexOf(data) != -1){
		alert("���� �����׸����� �̵̹�ϵ� �����Դϴ�.");
		return;
	} else if(indi.indexOf(data) != -1){
		alert("���� �����׸����� �̵̹�ϵ� �����Դϴ�.");
		return;
	}
	document.sForm.action="Calendar_addItem.jsp";
	document.sForm.req.value="ADD";
	document.sForm.add_item.value=data+';';
	document.sForm.submit();
}

//�ݱ�
function closeItem()
{
	opener.location.reload();
	self.close();
}
-->
</Script>