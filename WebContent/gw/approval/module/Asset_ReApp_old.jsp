<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "�ڻ� ���� 2���μ� ������"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.gw.db.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������
	StringProcess str = new com.anbtech.text.StringProcess();				//���ڿ� �ٷ��
	AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(); //���ڰ��系�� & ���缱

	//���� �������
	String query = "";

	//���缱 ����
	String doc_id = "";				//���ڰ��� ������ȣ
	String link_id = "";			//���ù��� ������ȣ
	String line="";					//�������� ���缱
	String r_line = "";				//���ۼ����� �Ѱ��ֱ�
	String vdate = "";				//������ ���� ����
	String ddate = "";				//������ ���� ����
	String wid = "";				//����ڻ��
	String vid = "";				//�����ڻ��
	String did = "";				//�����ڻ��
	String wname = "";				//�����
	String vname = "";				//������
	String dname = "";				//������
	String PROCESS = "";			//PROCESS
	String doc_ste = "";			//doc_ste

	//2���� ���� ����
	String line2="";				//�������� ���缱
	String writer_id = "";			//����� ���
	String writer_name = "";		//����� ��

	/*********************************************************************
	 	�����(login) �˾ƺ���
	*********************************************************************/	
	String[] uColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(uColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//����� ���
		writer_name = bean.getData("name");				//����� ��
	} //while

	/*********************************************************************
	 	���޺���
		�������� [o_status : t:�ڻ��̰�, o:�ڻ����]
		������� [2:1����Ŵ��,3:1������Ϸ�,4:2����Ŵ��,5:2������Ϸ�]
	*********************************************************************/	
	String title_name = "";
	
	String mode = request.getParameter("mode");			if(mode == null) mode = "";		//mode
	String h_no = request.getParameter("h_no");			if(h_no == null) h_no = "";		//������ȣ
	String as_no = request.getParameter("as_no");		if(as_no == null) as_no = "";	//�ڻ��ȣ
	String o_status = request.getParameter("o_status");	if(o_status == null) o_status = "";	//��������
	String as_status = request.getParameter("as_status");if(as_status == null) as_status = "";	//�������
	if(o_status.equals("t")) title_name = "�ڻ��̰�";
	else if(o_status.equals("o")) title_name = "�ڻ����";
	else if(o_status.equals("l")) title_name = "�ڻ�뿩";

	String[] otColumn = {"pid"};
	bean.setTable("as_history");			
	bean.setColumns(otColumn);
	bean.setClear();
	bean.setOrder("pid DESC");	
	bean.setSearch("h_no",h_no);
	bean.init_unique();

	link_id = "";		//1�� ������ε� ������ ���������ȣ
	String plid = "";
	if(bean.isAll()){
		link_id = bean.getData("pid");
	}

	//*********************************************************************
	// 1�� �ְ��μ� ���缱 ���� �ޱ�
	//*********************************************************************
	masterDAO.getTable_MasterPid(link_id);	
	TableAppLine rline = new com.anbtech.gw.entity.TableAppLine();	
	ArrayList table_line = new ArrayList();				//���缱
	table_line = masterDAO.getTable_line();		
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		rline = (TableAppLine)line_iter.next();
										
		if(rline.getApStatus().equals("���")) {
			wname = rline.getApName();	//�����
			wid = rline.getApSabun();	//����� ���
		}
		if(rline.getApStatus().equals("����"))  {
			vname = rline.getApName();	//������
			vid = rline.getApSabun();	//������ ���
			vdate = rline.getApDate();	//������ �������� (������ �����ϰ� ������ ����ʵ�)
		}
		if(rline.getApStatus().equals("����"))  {
			dname = rline.getApName();	//������
			did = rline.getApSabun();	//������ ���
			ddate = rline.getApDate();	//������ �������� (������ �����ϰ� ������ ����ʵ�)\
		}
			
		line += rline.getApStatus()+" "+rline.getApSabun()+" "+rline.getApName()+" "+rline.getApRank()+" "+rline.getApDivision()+" "+rline.getApDate()+" "+rline.getApComment()+"<br>";
	}

	/*********************************************************************
	 	���� ������ ó��
	*********************************************************************/	
	line2 = request.getParameter("doc_app_line"); if(line2 == null) line2 = "";	//���缱

%>

<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title><%=title_name%></title>
<LINK href="../../../gw/css/style.css" rel=stylesheet>
</head>
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>
<script language=javascript>
<!--
//���缱 �������� 
function eleApprovalManagerLineSelect()
{
	window.open("../eleApproval_Share.jsp?target=eForm.doc_app_line","eleA_app_search_select","width=680,height=600,scrollbar=yes,toolbar=no,status=no,resizable=no");
}

//���� ��� 
function eleApprovalRequest()
{
	if (eForm.doc_app_line.value =="") { alert("���缱�� �Է��Ͻʽÿ�."); return; }
	
	 //���缱 �˻�
	data = eForm.doc_app_line.value;		//���缱 ����
	s = 0;								//substring������
	e = data.length;					//���ڿ� ����
	decision = agree = 0;
	for(j=0; j<e; j++){
		ocnt = data.indexOf("\n");
		j += ocnt + 1;

		rstr = data.substring(s,ocnt);
		if(rstr.indexOf("����") != -1) decision++;
		if(rstr.indexOf("����") != -1) agree++;
	
		if(rstr.length == 0) j = e;
		data = data.substring(ocnt+1,e);
	}
	if(decision == 0) { alert("�����ڰ� �������ϴ�"); return; }

	//ó���� �޽��� ���
	document.all['lding'].style.visibility="visible";

	//�ϰ����� ����������
	document.eForm.action='../../../servlet/ApprovalAssetServlet';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.submit();
}

//����ϱ�
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
//�ݱ�
function winClose()
{
	window.returnValue='';
	self.close();
}
-->
</script>

<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>
<center>
<form name="eForm" method="post">
<table width='640' border="0" cellspacing="0" cellpadding="0">
    <tr><td width=100% align='right' height=20>
		<div id="print" style="position:absolute;left:420px;top:60px;width:300px;height:10px;visibility:visible;">
			<a href="Javascript:eleApprovalManagerLineSelect();"><img src='../../../gw/img/button_line_call.gif' align='middle' border='0'></a> <!-- ���缱 -->
			<a href="Javascript:eleApprovalRequest();"><img src='../../../gw/img/button_sangshin.gif' align='middle' border='0'></a> <!-- ��� -->
			<a href='Javascript:winprint();'><img src='../../../gw/img/button_print.gif' align='middle' border='0'></a> <!-- ��� -->
		</div>
	</td></tr>
</table>

<table width='640' border="0" cellspacing="0" cellpadding="0">
    <tr><td width=100% align='center' height=30><font size=3><b><%=title_name%> </b></font></td></tr>
</table>

<table width='640' border="0" cellspacing="0" cellpadding="0">
	<tr><td width=100% align='left' height=20><img src='../../../gw/img/slink_logo.jpg' align='middle' border='0'></td></tr>
</table>

<table width='640' border='1' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111" bgcolor='#F2F2F2'> 
	<tr>            
		<td width="20" height="96" rowspan=3 align="center">��<p>��</td>
		<td width="420" height="96" rowspan=3 valign="top"><%=line%></td>
		<td width="20" height="96" rowspan=3 align="center">��<p>��</td>                     
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">�� ��</td>            
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">�� ��</td>            
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">�� ��</td>            
	</tr>          
	<tr height="26">         
		<td width="60" height="50" align="center"><img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></td>   
		<td width="60" height="50" align="center">
		<% //������ ������ ���� ǥ���ϱ� (��, �ݷ������� �ƴѰ�츸)
			if(vdate.length() == 0)	{//������
				if(ddate.length() == 0) out.println("&nbsp;");
				else out.println("����");
			} else {
				out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
			}
		%></td>   
		<td width="60" height="50" align="center">
		<%
			if(ddate.length() == 0)	{//������
				out.println("&nbsp;");
			} else {
				out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
			}
		%>	
		</td>   
	</tr>   
	<tr height="26">         
		<td width="60" height="24" align="center">&nbsp;<%=wname%>&nbsp;</td>   
		<td width="60" height="24" align="center">&nbsp;<%=vname%>&nbsp;</td>   
		<td width="60" height="24" align="center">&nbsp;<%=dname%>&nbsp;</td>   
	</tr>   
</table>
		
<iframe id="iframe_main" src="../../../servlet/AssetServlet?mode=<%=mode%>&h_no=<%=h_no%>&as_no=<%=as_no%>" width="640" height="330" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="yes">
</iframe>

<table width='640' border='1' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111" bgcolor='#F2F2F2'> 
	<tr>            
		<td width="20" height="96" rowspan=3 align="center">��<p>��</td>
		<td width="420" height="96" rowspan=3>
		<TEXTAREA NAME="doc_app_line" rows=6 cols=57 readOnly style="border:1px solid #787878;"><%=line2%></TEXTAREA>
		</td>
		<td width="20" height="96" rowspan=3 align="center">��<p>��</td>                     
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">�� ��</td>            
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">�� ��</td>            
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">�� ��</td>            
	</tr>          
	<tr height="26">         
		<td width="60" height="50" align="center">&nbsp;</td>   
		<td width="60" height="50" align="center">&nbsp;</td>   
		<td width="60" height="50" align="center">&nbsp;</td>   
	</tr>   
	<tr height="26">         
		<td width="60" height="24" align="center">&nbsp;</td>   
		<td width="60" height="24" align="center">&nbsp;</td>   
		<td width="60" height="24" align="center">&nbsp;</td>   
	</tr>   
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0'> 
	<tr>            
		<td width="50%" height="40" rowspan=3 align="left">SLCG-004-0</td> 
		<td width="50%" height="40" rowspan=3 align="right">A4(210x297mm)�������75g/m<sup>2</sup></td> 
	</tr>   
</table>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value='<%=title_name%>'>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='h_no' value='<%=h_no%>'>					<% //�ڻ� ������ȣ %>
<input type='hidden' name='as_no' value='<%=as_no%>'>				<% //�ڻ��ȣ %>
<input type='hidden' name='o_status' value='<%=o_status%>'>			<% //�������� %>
<input type='hidden' name='as_status' value='<%=as_status%>'>		<% //������� %>

<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
</form>  

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">������ ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.</marquee>
	</td> 
	</tr>
</table>
</div>

</center>
</body>
</html>

