<%@ include file="../../admin/configPopUp.jsp"%>
<%@ 	page		
	info= "������ Excel���"		
	contentType = "application/vnd.ms-excel; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.Hanguel"	
%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.office.OfficeListBean" />
<%!
	private String Pname = "";			//�����
	private String Dname = "";			//�μ���
	private String Cname = "";			//����
	private String Crank = "";			//����
	private String Coffitel = "";		//��ȭ��ȣ
	private String Cfax = "";			//�ѽ�
	private String Chandtel = "";		//�ڵ���
	private String Cemail = "";			//���ڿ���
	private String Cjob = "";			//������
	private String Cpostno = "";		//�����ȣ
	private String Caddress = "";		//ȸ���ּ�
	private String Ctype = "";			//�����ǰ
	private String Chobby = "";			//���
	private String Cspec = "";			//Ư��
	private String Cweday = "";			//��ȥ�����
	private String Cbirth = "";			//����
	private String Chometel = "";		//������ȭ
	private String Cmemo = "";			//��Ÿ����
	private String Cclass = "";			//������
	
	private int rcnt = 0;				//���޹��� ����
	private int twidth = 0;				//������ �ʺ�
%>
<%	
	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	rcnt = 0;
	twidth = 80;
	Pname=Dname=Cname=Crank=Coffitel=Cfax=Chandtel=Cemail=Cjob=Cpostno="";
	Caddress=Ctype=Chobby=Cspec=Cweday=Cbirth=Chometel=Cmemo=Cclass="";

	/***********************************************************************
		���޺��� �ޱ� (from reportCustomerExcel.jsp)
	***********************************************************************/
	Pname = request.getParameter("pname");			//�����
	if(Pname.length() != 0) {rcnt++; twidth += 80; }

	Dname = request.getParameter("dname");			//�μ���
	if(Dname.length() != 0) {rcnt++; twidth += 80; }

	Cname = request.getParameter("cname");			//����
	if(Cname.length() != 0) {rcnt++; twidth += 80; }

	Crank = request.getParameter("crank");			//����
	if(Crank.length() != 0) {rcnt++; twidth += 80; }

	Coffitel = request.getParameter("coffitel");	//��ȭ��ȣ
	if(Coffitel.length() != 0) {rcnt++; twidth += 80; }

	Cfax = request.getParameter("cfax");			//�ѽ�
	if(Cfax.length() != 0) {rcnt++; twidth += 80; }

	Chandtel = request.getParameter("chandtel");	//�ڵ���
	if(Chandtel.length() != 0) {rcnt++; twidth += 80; }

	Cemail = request.getParameter("cemail");		//���ڿ���
	if(Cemail.length() != 0) {rcnt++; twidth += 80; }

	Cjob = request.getParameter("cjob");			//������
	if(Cjob.length() != 0) {rcnt++; twidth += 80; }

	Cpostno = request.getParameter("cpostno");		//�����ȣ
	if(Cpostno.length() != 0) {rcnt++; twidth += 80; }

	Caddress = request.getParameter("caddress");	//ȸ���ּ�
	if(Caddress.length() != 0) {rcnt++; twidth += 80; }

	Ctype = request.getParameter("ctype");			//�����ǰ
	if(Ctype.length() != 0) {rcnt++; twidth += 80; }

	Chobby = request.getParameter("chobby");		//���
	if(Chobby.length() != 0) {rcnt++; twidth += 80; }

	Cspec = request.getParameter("cspec");			//Ư��
	if(Cspec.length() != 0) {rcnt++; twidth += 80; }

	Cweday = request.getParameter("cweday");		//��ȥ�����
	if(Cweday.length() != 0) {rcnt++; twidth += 80; }

	Cbirth = request.getParameter("cbirth");		//����
	if(Cbirth.length() != 0) {rcnt++; twidth += 80; }

	Chometel = request.getParameter("chometel");	//������ȭ
	if(Chometel.length() != 0) {rcnt++; twidth += 80; }

	Cmemo = request.getParameter("cmemo");			//��Ÿ����
	if(Cmemo.length() != 0) {rcnt++; twidth += 80; }

	Cclass = request.getParameter("cclass");		//������
	if(Cclass.length() != 0) {rcnt++; twidth += 80; }

	/***********************************************************************
		������� ��ȸ�ϱ�
	***********************************************************************/
	//�ķ����� ���Ѵ�.
	String[] dbColumns ={"a.*","b.name 'pname'"};
	String query = "where a.ap_id = b.ap_id order by name ASC";	//�⺻����
	bean.setTable("customer_table a,company_table b");
	bean.setColumns(dbColumns);
	bean.setSearchWrite(query);
	bean.init_write();	
//out.println("q:" + bean.makeQuery_write() + "<br>");
//out.println("q:" + bean.isAll() + "<br>");
%>
<html>
<head>
<meta http-equiv="Content-Language" content="ko">
<meta name="GENERATOR" content="Microsoft FrontPage 5.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">
<LINK href="../css/style.css" rel=stylesheet>
<title>������ Excel���</title>
</head>
<BODY leftmargin='0' bgcolor='#F7F7F7' topmargin='3' marginwidth='3' marginheight='3'  onLoad="javascript:centerWindow();">

<table border="1" cellspacing="0" width="100%" id="AutoNumber1" height="">
	<TR height=30><TD class='subB' align=center colspan='<%=rcnt%>'><B>�� �� �� ��</B></TD></TR>
	<TR height=30>
		<% 
		if(Pname.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>�����</TD>");
		}
		if(Dname.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>�μ���</TD>");
		}
		if(Cclass.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>������</TD>");
		}
		if(Cname.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>����</TD>");
		}
		if(Crank.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>�� ��</TD>");
		}
		if(Coffitel.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>��ȭ��ȣ</TD>");
		}
		if(Cfax.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>�� ��</TD>");
		}
		if(Chandtel.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>�ڵ���</TD>");
		}
		if(Cemail.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>���ڿ���</TD>");
		}
		if(Cjob.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>������</TD>");
		}
		if(Cpostno.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>�����ȣ</TD>");
		}
		if(Caddress.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>ȸ���ּ�</TD>");
		}
		if(Ctype.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>�����ǰ</TD>");
		}
		if(Chobby.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>�� ��</TD>");
		}
		if(Cspec.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>Ư ��</TD>");
		}
		if(Cweday.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>��ȥ�����</TD>");
		}
		if(Cbirth.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>�� ��</TD>");
		}
		if(Chometel.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>������ȭ</TD>");
		}
		if(Cmemo.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center>��Ÿ����</TD>");
		}	

		%>
	</TR>

	<% if (bean.isEmpty()) { %>
	<tr>
			<td colspan='<%=rcnt%>'><center>������ �����ϴ�.</center></td>
	</tr> 
	<% } %>	

	<% 
		while(bean.isAll()) { 
			out.println("<TR height=25>");
	
			if(Pname.length() != 0) out.println("<TD>&nbsp;"+bean.getData("pname")+"</TD>");
			if(Dname.length() != 0) out.println("<TD>&nbsp;"+bean.getData("division")+"</TD>");	
			if(Cclass.length() != 0) out.println("<TD>&nbsp;"+bean.getData("customer_class")+"</TD>");
			if(Cname.length() != 0) out.println("<TD>&nbsp;"+bean.getData("name")+"</TD>");
			if(Crank.length() != 0) out.println("<TD>&nbsp;"+bean.getData("rank")+"</TD>");
			if(Coffitel.length() != 0)out.println("<TD>&nbsp;"+bean.getData("office_tel")+"</TD>");
			if(Cfax.length() != 0) out.println("<TD>&nbsp;"+bean.getData("fax")+"</TD>");
			if(Chandtel.length() != 0) out.println("<TD>&nbsp;"+bean.getData("hand_tel")+"</TD>");
			if(Cemail.length() != 0) out.println("<TD>&nbsp;"+bean.getData("email")+"</TD>");
			if(Cjob.length() != 0) out.println("<TD>&nbsp;"+bean.getData("main_job")+"</TD>");
			if(Cpostno.length() != 0) out.println("<TD>&nbsp;"+bean.getData("post_no")+"</TD>");
			if(Caddress.length() != 0) out.println("<TD>&nbsp;"+bean.getData("address")+"</TD>");
			if(Ctype.length() != 0) out.println("<TD>&nbsp;"+bean.getData("customer_type")+"</TD>");
			if(Chobby.length() != 0) out.println("<TD>&nbsp;"+bean.getData("hobby")+"</TD>");
			if(Cspec.length() != 0) out.println("<TD>&nbsp;"+bean.getData("speciality")+"</TD>");
			if(Cweday.length() != 0) out.println("<TD>&nbsp;"+bean.getData("wedding_day")+"</TD>");
			if(Cbirth.length() != 0) out.println("<TD>&nbsp;"+bean.getData("birthday")+"</TD>");
			if(Chometel.length() != 0) out.println("<TD>&nbsp;"+bean.getData("home_tel")+"</TD>");
			if(Cmemo.length() != 0) out.println("<TD>&nbsp;"+bean.getData("memo")+"</TD>");

			out.println("</TR>");
		} //while 

	%>
	
</table>
</body>
</html>

<script language=javascript>
<!--
function centerWindow() 
{ 
        var sampleWidth = <%=twidth%>;                        // �������� ���� ������ ���� 
        var sampleHeight = 600;								 // �������� ���� ������ ���� 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

-->
</script>