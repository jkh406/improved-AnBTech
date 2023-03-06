<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "����ٹ���û�� ���ۼ�"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"

	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.text.*"

	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.util.normalFormat"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	textFileReader text = new com.anbtech.file.textFileReader();			//�������� �б�
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������

	String filepath =  upload_path+"/es/"+login_id+"/addfile";
	int maxFileSize = 10;
	com.oreilly.servlet.MultipartRequest multi;
	multi = new com.oreilly.servlet.MultipartRequest(request,filepath,maxFileSize*1024*1024,"euc-kr");

	//��ȼ� �������
	String query = "";
	String writer_id = "";			//�����(�븮����ϼ��� ����) ���
	String writer_name = "";		//�����(�븮����ϼ��� ����) �̸�

	String user_name = "";			//�ش��� ��
	String rank_code = "";			//�ش��� ����code
	String user_rank = "";			//�ش��� ����
	String div_id = "";				//�ش��� �μ��� �����ڵ�
	String div_name = "";			//�ش��� �μ���
	String div_code = "";			//�ش��� �μ��ڵ�

	int work_cnt = 22;				//����ٹ���û �ۼ� �÷���
	
	/*********************************************************************
	 	�����(login) �˾ƺ��� : ���ڰ��� ����� ���� [����]
	*********************************************************************/	
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//����� ���
		writer_name = bean.getData("name");				//����� ��
	} //while


	/*********************************************************************
	 	�ش��� ���� �˾ƺ��� (�����) : ����� ���� [����]
	*********************************************************************/	
	String user_id = multi.getParameter("user_id"); if(user_id == null) user_id = login_id;
	String[] idColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+user_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_name = bean.getData("name");				//�ش��� ��
		rank_code = bean.getData("ar_code");			//�ش��� ���� code
		user_rank = bean.getData("ar_name");			//�ش��� ����
		div_id = bean.getData("ac_id");					//�ش��� �μ��� �����ڵ�
		div_name = bean.getData("ac_name");				//�ش��� �μ��� 
		div_code = bean.getData("ac_code");				//�ش��� �μ��ڵ�
	} //while

	/*********************************************************************
	 	view.jsp�κ��� ���� ÷������ ����
	*********************************************************************/	
	String read_worker = multi.getParameter("read_worker"); //����ٹ��� ����� �ޱ����� �ӽò���ǥ
	String doc_id = multi.getParameter("doc_id");
	String old_id = multi.getParameter("old_id"); if(old_id == null) old_id = doc_id;	//���ʷ� �Ѱܹ�������
	
	//�ٹ��� ã��
	String[][] works = new String[work_cnt][5]; //�ٹ��ڻ��,�̸�,����,��ǽð�,�μ���Ȯ��
	for(int i=0; i<work_cnt; i++) for(int j=0; j<5; j++) works[i][j] = "";
	if(read_worker != null) {
		String[] workColumn = {"worker_id","worker_name","content","close_time","cfm"};
		bean.setTable("janeup_worker");			
		bean.setColumns(workColumn);
		bean.setOrder("ju_cid ASC");	
		query = "where (ju_id ='"+doc_id+"')";
		bean.setSearchWrite(query);
		bean.init_write();
		
		int w = 0;
		while(bean.isAll()) {
			works[w][0] = bean.getData("worker_id");			//�ٹ��ڻ��
			works[w][1] = bean.getData("worker_name");		//�̸�
			works[w][2] = bean.getData("content");			//����
			works[w][3] = bean.getData("close_time");		//��ǽð�
			works[w][4] = bean.getData("cfm");				//�μ���Ȯ��
			w++;
		} //while
	}

	/*********************************************************************
	 	���� ������ ó��
	*********************************************************************/
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";		//���缱
	//���� �⵵
	String year = anbdt.getYear();			
	int syear = Integer.parseInt(year);
	int ey = syear + 5;
	String sel_syear = multi.getParameter("doc_syear"); if(sel_syear == null) sel_syear = year;
	//���� ��
	String month = anbdt.getMonth();
	String sel_smonth = multi.getParameter("doc_smonth");	
	if(sel_smonth == null) sel_smonth = month;
	//���� ��
	String dates = anbdt.getDates();
	String sel_sdate = multi.getParameter("doc_sdate");	
	if(sel_sdate == null) sel_sdate = dates;
	int maxdates = anbdt.getDateMaximum(Integer.parseInt(sel_syear),Integer.parseInt(sel_smonth),1);

	//�ٹ��� ã��
	if(read_worker == null) {
		for(int n=0; n<work_cnt; n++) {
			String work_id = multi.getParameter("work_id"+n); if(work_id == null) work_id = "";
			String[] wColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
			bean.setTable("user_table a,class_table b,rank_table c");			
			bean.setColumns(wColumn);
			bean.setOrder("a.id ASC");	
			query = "where (a.id ='"+work_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
			bean.setSearchWrite(query);
			bean.init_write();

			while(bean.isAll()) {
				works[n][0] = work_id;							//�ٹ��� ���
				works[n][1] = bean.getData("name");				//�ٹ��� ��
			} //while
			works[n][2] = multi.getParameter("content"+n); if(works[n][2] == null) works[n][2] = "";
			works[n][3] = multi.getParameter("close_time"+n); if(works[n][3] == null) works[n][3] = "";
			works[n][4] = multi.getParameter("cfm"+n); if(works[n][4] == null) works[n][4] = "";
		}
	}

	//���ϱٹ�
	String job_kind = multi.getParameter("job_kind"); if(job_kind == null) job_kind = "";
	String hd_sel = "";
	String nd_sel = "";
	if(job_kind.equals("���ϱٹ�")) hd_sel = "selected";
	else if(job_kind.equals("�߰��ٹ�")) nd_sel = "selected";

	//�ĺ�����Ȯ��
	String cost_prs = multi.getParameter("cost_prs"); if(cost_prs == null) cost_prs = "";
	
%>

<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title>����ٹ���û��</title>
<LINK href="../../../gw/css/style.css" rel=stylesheet>
</head>

<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>
<center>
<form name="eForm" method="post" encType="multipart/form-data">

<table width='640' border="0" cellspacing="0" cellpadding="0">
    <tr><td width=100% align='right' height=20>
		<div id="print" style="position:absolute;left:370px;top:60px;width:300px;height:10px;visibility:visible;">
			<a href="Javascript:eleApprovalManagerLineSelect();"><img src='../../../gw/img/button_line_call.gif' align='middle' border='0'></a> <!-- ���缱 -->
			<a href="Javascript:eleApprovalRequest();"><img src='../../../gw/img/button_sangshin.gif' align='middle' border='0'></a> <!-- ��� -->
			<% if(user_id.equals(login_id)) { //�븮��Ͻô� �ӽ�����޴� ���� %>
			<a href="Javascript:eleApprovalTemp();"><img src='../../../gw/img/002_007_save.gif' align='middle' border='0'></a> <!-- �ӽ����� -->
			<% } %>
			<a href='Javascript:winprint();'><img src='../../../gw/img/button_print.gif' align='middle' border='0'></a> <!-- ��� -->
		</div>
	</td></tr>
</table>

<table width='640' border="0" cellspacing="0" cellpadding="0">
    <tr><td width=100% align='center' height=30><font size=3><b>�� �� �� �� �� û �� </b></font></td></tr>
</table>

<table width='640' border="0" cellspacing="0" cellpadding="0">
	<tr><td width=100% align='left' height=20><img src='../../../gw/img/slink_logo.jpg' align='middle' border='0'></td></tr>
</table>

<table width='640' border='1' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111" bgcolor='#F2F2F2'> 
	<tr>            
		<td width="20" height="96" rowspan=3 align="center">��<p>��</td>
		<td width="420" height="96" rowspan=3>
		<TEXTAREA NAME="doc_app_line" rows=6 cols=57 readOnly style="border:1px solid #787878;"><%=line%></TEXTAREA>
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
		
<table width='640' border='1' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111"> 
	<tr>
		<td width="100" height="30" align="center" valign="middle">�� �� �� ��</td>
		<td width="540" height="30" align="center" colspan=3>
			<input type="hidden" name="user_code" value='<%=rank_code%>'>
			<input type="hidden" name="user_rank" value='<%=user_rank%>'>
			<input type="hidden" name="user_id" value='<%=user_id%>'>
			<input type="hidden" name="user_name" value='<%=user_name%>'>
			<input type="hidden" name="div_id" value='<%=div_id%>'>
			<input type="hidden" name="div_code" value='<%=div_code%>'>
			<input type="hidden" name="div_name" value='<%=div_name%>'>&nbsp;&nbsp;<%=div_name%></td>
	</tr>
	<tr>
		<td width="100" height="30" align="center" valign="middle">���� �ٹ���</td>
		<td width="540" height="30" align="center" colspan=3>
		<%
			//���� �⵵						
			out.println("&nbsp;<SELECT NAME='doc_syear'>");
			for(int iy = syear; iy < ey; iy++) {
				String sel = "";
				if(iy == Integer.parseInt(sel_syear)) sel = "selected";
				else sel = "";
				out.println("<option "+sel+" value='"+iy+"'>"+iy);
			}
			out.println("</SELECT>��"); 

			//���� ��
			out.println("<SELECT NAME='doc_smonth'>");
			for(int iy = 1; iy < 13; iy++) {
				String sel = "";
				if(iy == Integer.parseInt(sel_smonth)) sel = "selected";
				else sel = "";
				out.println("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
			}
			out.println("</SELECT>��"); 

			//���� ��
			out.println("<SELECT NAME='doc_sdate'>");
			for(int iy = 1; iy <= maxdates; iy++) {
				String sel = "";
				if(iy == Integer.parseInt(sel_sdate)) sel = "selected";
				else sel = "";
				out.println("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
			}
			out.println("</SELECT>��");
	   %>
	   </td>
	</tr>
	<tr>
		<td width="100" height="30" align="center" valign="middle">�� �� ��</td>
		<td width="340" height="30" align="center" valign="middle">��������</td>
		<td width="100" height="30" align="center" valign="middle">��ǽð�</td>
		<td width="100" height="30" align="center" valign="middle">�μ���Ȯ��</td>
	</tr>
	<tr>
		<td width="100" height="470" align="center" valign="middle">
			<table width='100' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%	//�ٹ���
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='100' height='20' align='center' valign='middle'>");
					out.println("<input class='box' type='hidden' name='work_id"+i+"' value='"+works[i][0]+"'>");
					out.println("<input class='box' size='5' type='text' name='work_name"+i+"' value='"+works[i][1]+"'>");
					out.println("<a href=\"Javascript:searchUser('eForm.work_id"+i+"','eForm.work_name"+i+"');\">A</a>");
					out.println("<a href=\"Javascript:deleteUser('eForm.work_id"+i+"','eForm.work_name"+i+"');\">D</a>");
					out.println("</td></tr>");
				}
				%>
			</table>
		</td>
		<td width="340" height="470" align="center" valign="middle">
			<table width='340' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='340' height='20' align='center' valign='middle'>");
					out.println("<input class='box' size='45' type='text' name='content"+i+"' value='"+works[i][2]+"'></td>");
					out.println("</tr>");
				}
				%>
			</table>
		</td>
		<td width="100" height="470" align="center" valign="middle">
			<table width='100' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='100' height='20' align='center' valign='middle'>");
					out.println("<input class='box' size='8' type='text' name='close_time"+i+"' value='"+works[i][3]+"'></td>");
					out.println("</tr>");
				}
				%>
			</table>
		</td>
		<td width="90" height="470" align="center" valign="middle">
			<table width='90' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='90' height='20' align='center' valign='middle'>");
					out.println("<input class='box' size='8' type='text' name='cfm"+i+"' value='"+works[i][4]+"'></td>");
					out.println("</tr>");
				}
				%>
			</table>
		</td>
	</tr>
	<tr>
		<td width="100" height="30" align="center" valign="middle">�� ��</td>
		<td width="340" height="30" align="center" valign="middle">
			<select name='job_kind'">
				<OPTION value='���ϱٹ�' <%=hd_sel%> >���ϱٹ�</OPTION>
				<OPTION value='�߰��ٹ�' <%=nd_sel%> >�߰��ٹ�</OPTION>
			</select></td>
		<td width="100" height="30" align="center" valign="middle">�ĺ�����Ȯ��</td>
		<td width="100" height="30" align="center" valign="middle">
			<input class=box type="text" size=8 name="cost_prs" value='<%=cost_prs%>'></td>
	</tr>
	<tr>
		<td width="640" height="60" align="center" colspan=4><br><br>
			���� ���� Ư�� �ٹ��� ��û�մϴ�. <br><br>
			<%=anbdt.getYear()%> �� <%=anbdt.getMonth()%> �� <%=anbdt.getDates()%> �� <br><br>
			��û�� : <%=user_name%> <br><br></td>
	</tr>
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0'> 
	<tr>            
		<td width="50%" height="40" rowspan=3 align="left">SLCG-005-0</td> 
		<td width="50%" height="40" rowspan=3 align="right">A4(210x297mm)�������75g/m<sup>2</sup></td> 
	</tr>   
</table>
<% //���긴 ������ ó�� %>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='work_cnt' value='<%=work_cnt%>'>
<input type='hidden' name='doc_sub' value='[����ٹ���û��]'>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='old_id' value='<%=old_id%>'>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>
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
<script language=javascript>
<!--
//�ٹ��� ã��
function searchUser(a,b)
{
	window.open("searchUser.jsp?target="+a+"/"+b,"user","width=460","height=480","scrollbars=yes,toolbar=no,status=no,resizable=no");

}
//�Է��� �ٹ��� ����ϱ� 
function deleteUser(a,b)
{
	var ask = confirm("������ �ٹ��ڸ� ��ܿ��� �����Ͻðڽ��ϱ�?");
	if(ask == false) return;

	var id = eval("document."+a);
	id.value = "";
	var name = eval("document."+b);
	name.value = "";
}
//����ϱ�
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}

//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

//���缱 �������� 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
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

	document.onmousedown=dbclick;  // ����Ŭ�� check

	//�ϰ����� ����������
	document.eForm.action='../../../servlet/JanEupServlet';
	document.eForm.mode.value='R_YEONJANG';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.submit();
}

//���� �ӽú���
function eleApprovalTemp()
{
	//ó���� �޽��� ���
	document.all['lding'].style.visibility="visible";

	document.onmousedown=dbclick;  // ����Ŭ�� check

	document.eForm.action="../../../servlet/JanEupServlet";
	document.eForm.mode.value='R_YEONJANG_TMP';
	document.eForm.app_mode.value='TMP';	
	document.eForm.submit();
}

// ����Ŭ�� ����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>
