<%@ include file="../../admin/configPopUp.jsp"%>
<%@ 	page		
	info= "���̷� Excel���"		
	contentType = "application/vnd.ms-excel; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.Hanguel"	
%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.office.OfficeListBean" />
<%!
	private String Sdate = "";			//���ڼ���
	private String Stime = "";			//�˻� ������ 
	private String Etime = "";			//�˻� ������

	private String Scompany = "";		//�������
	private String Cname = "";			//���õ� �����
	private String Cname_id = "";		//���õ� ����� ID

	private String Scustomer = "";		//������
	private String Sname = "";			//���õ� ����
	private String Sname_id = "";		//���õ� ���� ID

	private String Spurpose = "";		//��������
	private String Pname = "";			//���õ� ������

	private String Subject = "";		//����
	private String Content = "";		//����
	private String Issue = "";			//�̽�����

	private String arg1 = "";			//����1
	private String arg2 = "";			//����2
	private String arg3 = "";			//����3

	private int rcnt = 0;				//���޹��� ����
	private int twidth = 0;				//������ �ʺ�
%>
<%	
	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	rcnt = 1;	//clear(�ۼ���)
	twidth = 80;
	/***********************************************************************
		���޺��� �ޱ� (from reportServiceExcel.jsp)
	***********************************************************************/
	Sdate = request.getParameter("sdate");							//���ڼ���
	if(Sdate != null) {
		Stime = request.getParameter("stime");						//�˻� ������ 
		Etime = request.getParameter("etime");						//�˻� ������
		twidth += 220;
		rcnt++;	rcnt++; rcnt++;		
	} else {
		Stime = anbdt.getYear() + "/" + anbdt.getMonth() + "/" + "01"; //�ش�� 1��
		Etime = anbdt.getDate(0);									//���ó���
	}

	Scompany = request.getParameter("scompany");					//�������
	if(Scompany != null) {
		Cname = Hanguel.toHanguel(request.getParameter("cname"));	//���õ� �����
		Cname_id = request.getParameter("cname_id");				//���õ� ����� ID
		twidth += 80;
		rcnt++;
	} else {
		Cname = "";
		Cname_id = "";
	}
//out.println(Scompany + ":" + Cname +":" + Cname_id);
	Scustomer = request.getParameter("scustomer");					//������
	if(Scustomer != null) {
		Sname = Hanguel.toHanguel(request.getParameter("sname"));	//���õ� ����
		Sname_id = request.getParameter("sname_id");				//���õ� ���� ID
		twidth += 60;
		rcnt++;
	} else {
		Sname = "";
		Sname_id = "";
	}

	Spurpose = request.getParameter("spurpose");					//��������
	Pname = Hanguel.toHanguel(request.getParameter("pname"));		//���õ� ������
	if(Spurpose.length() != 0) { twidth += 90;	rcnt++;}

	Subject = request.getParameter("subject");						//����
	if(Subject.length() != 0) { twidth += 200;	rcnt++;}
	Content = request.getParameter("content");						//����
	if(Content.length() != 0) { twidth += 200;	rcnt++;}
	Issue = request.getParameter("result");							//�̽�����
	if(Issue.length() != 0) { twidth += 200;	rcnt++;}

	arg1 = request.getParameter("arrange1");						//����1
	arg2 = request.getParameter("arrange2");						//����2
	arg3 = request.getParameter("arrange3");						//����3

	//window width
	if(twidth >= 1000) twidth = 1000;
	/***********************************************************************
		������� ��ȸ�ϱ�
	***********************************************************************/
	//�ķ����� ���Ѵ�.
	String[] dbColumns ={"a.*","b.name_kor","c.name_kor","d.name"};
	String query = "where a.ap_id=b.company_no and a.at_id=c.at_id and a.au_id=d.id ";	//�⺻����
	if(Sdate.length() != 0)	{														//����
		query += "and (a.s_day >= '" + str.repWord(Stime,"/","") + "' and a.s_day <= '" + str.repWord(Etime,"/","") + "') ";
	}
	if(Scompany.length() != 0) {													//����� ID
		if(!Cname_id.equals("all"))
			query += "and a.ap_id = '" + Cname_id + "' "; 
	}
	if(Scustomer.length() != 0) {													//���� ID
		if(!Sname_id.equals("all"))
			query += "and a.at_id = '" + Sname_id + "' "; 
	}
	if(Spurpose.length() != 0) {													//����
		if(!Pname.equals("all"))
			query += "and a.class = '" + Pname + "' "; 
	}
	query += "order by "+arg1+","+arg2+","+arg3;	//����
	
	bean.setTable("history_table a,company_customer b,personal_customer c,user_table d");
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
<title>���̷� Excel���</title>
</head>
<BODY leftmargin='0' bgcolor='#F7F7F7' topmargin='3' marginwidth='3' marginheight='3'  onLoad="javascript:centerWindow();">

<% if(!Cname_id.equals("all")) { //������� ǥ���Ѵ�.
	out.println("<table border='0' cellspacing='0' width='100%' id='AutoNumber0' height=''>");
	if(!Sname_id.equals("all")) { //����� �� ����
		rcnt--; rcnt--;
		out.println("<TR height=30><TD class='subB' align=center colspan='"+rcnt+"'><B>�� �� �� ��</B></TD></TR>");
		out.println("<TR height=30><td class='subE' colspan='"+rcnt+"' align='right'>�����: "+Cname+"&nbsp;&nbsp;&nbsp; ����: " + Sname + "&nbsp;&nbsp;&nbsp;</td></TR>");
	} else {						//�����
		rcnt--;
		out.println("<TR height=30><TD class='subB' align=center colspan='"+rcnt+"'><B>�� �� �� ��</B></TD></TR>");
		out.println("<td class='subE' colspan='"+rcnt+"' align='right'>�����: "+Cname+"&nbsp;&nbsp;&nbsp;</td>");
	}
	out.println("</table>");
} %>

<table border="1" cellspacing="0" width="100%" id="AutoNumber1" height="">
	<TR height=30>
		<% 
		if(Sdate.length() != 0) { 
			out.println("<TD class='subB' WIDTH=80 align=center >��&nbsp;&nbsp;��</TD>");
			out.println("<TD class='subB' WIDTH=40 align=center >����</TD>");
			out.println("<TD class='subB' WIDTH=80 align=center >��&nbsp;&nbsp;��</TD>");
		}
		if((Scompany.length() != 0) && (Cname_id.equals("all"))){
			out.println("<TD class='subB' WIDTH=80 align=center >�����</TD>");
		}

		if((Scustomer.length() != 0) && (Sname_id.equals("all"))){
			out.println("<TD class='subB' WIDTH=60  align=center >����</TD>");
		}
		if(Spurpose.length() != 0) {
			out.println("<TD class='subB' WIDTH=90  align=center >��&nbsp;&nbsp;��</TD>");
		}
		if(Subject.length() != 0) {
			out.println("<TD class='subB' WIDTH=200  align=center >��&nbsp;&nbsp;��</TD>");
		}
		if(Content.length() != 0) {
			out.println("<TD class='subB' WIDTH=200  align=center >��&nbsp;&nbsp;��</TD>");
		}
		if(Issue.length() != 0) {
			out.println("<TD class='subB' WIDTH=200  align=center >ISSUE</TD>");
		}
		out.println("<TD class='subB' WIDTH=60  align=center>�ۼ���</TD>");

		%>
	</TR>

	<% if (bean.isEmpty()) { %>
	<tr>
			<td colspan=<%=rcnt%> rowspan=5><span class="text"><center>������ �����ϴ�.</center></td>
	</tr> 
	<% } %>	

	<% 
		while(bean.isAll()) { 
		out.println("<TR height=25>");

		if(Sdate.length() != 0) { 
			String dd = bean.getData("a.s_day");
			String cdate = "";
			int dayi = 0;
			if(dd != null) {
				int year = Integer.parseInt(dd.substring(0,4));
				int month = Integer.parseInt(dd.substring(5,6));
				int date = Integer.parseInt(dd.substring(7,8));
				dayi = anbdt.getDay(year,month,date);				//���� (����)
				cdate = dd.substring(0,4) + "-" +dd.substring(5,6) + "-" + dd.substring(7,8); //����
			} else dayi = 0;
			String[] day = {"","�Ͽ���","������","ȭ����","������","�����","�ݿ���","�����"};
			
			out.println("<TD align=center valign=top>" + cdate + "</TD>");
			out.println("<TD align=center valign=top>" + day[dayi] + "</TD>");
			out.println("<TD align=center valign=top>" + str.repWord(bean.getData("a.s_time"),"/","~") + "</TD>");

		}
	
		if((Scompany.length() != 0) && (Cname_id.equals("all")))		//�����
			out.println("<TD align=left valign=top>" + bean.getData("b.name") + "</TD>");
		if((Scustomer.length() != 0) && (Sname_id.equals("all")))		//����
			out.println("<TD align=center valign=top>" + bean.getData("c.name") + "</TD>");
		if(Spurpose.length() != 0)										//����
			out.println("<TD align=center valign=top>" + bean.getData("a.class") + "</TD>");
		if(Subject.length() != 0)										//����
			out.println("<TD align=left valign=top>" + bean.getData("a.subject") + "</TD>");
		if(Content.length() != 0) {										//����
			//�������� �ֱ� (�䳻�� �߰��� ����Ű(br)�ֱ�)
			String bonmun = bean.getData("a.content");
			Content = "";
			int tlen = bonmun.length();		//��������
			if(tlen > 60) {
				int mcut = 60;
				for(int m=0; m < tlen; ){
					if(mcut == tlen)	//������ ���� �б�
						Content += bonmun.substring(m,mcut);
					else 
						Content += bonmun.substring(m,mcut) + "<br>";
					m += 60;
					mcut += 60;
					if(tlen < mcut) mcut = tlen;		//���������� 
				}//for
			} else Content=bonmun;
			out.println("<TD align=left valign=top>" + Content + "</TD>");
//			out.println("<TD align=left valign=top><TEXTAREA>" + bean.getData("a.content") + "</TEXTAREA></TD>");
		}
		
		if(Issue.length() != 0) {										//�̽�����
			//Issue���� �ֱ� (�䳻�� �߰��� ����Ű(br)�ֱ�)
			String result = bean.getData("a.result");
			Issue = "";
			int tlen = result.length();		//��������
			if(tlen > 60) {
				int mcut = 60;
				for(int m=0; m < tlen; ){
					if(mcut == tlen)	//������ ���� �б�
						Issue += result.substring(m,mcut);
					else 
						Issue += result.substring(m,mcut) + "<br>";
					m += 60;
					mcut += 60;
					if(tlen < mcut) mcut = tlen;		//���������� 
				}//for
			} else Issue=result;
			out.println("<TD align=left valign=top>" + Issue + "</TD>");
//			out.println("<TD align=left valign=top><TEXTAREA>" + bean.getData("a.result") + "</TEXTAREA></TD>");
		}
		out.println("<TD align=center valign=top>" + bean.getData("d.name") + "</TD>"); //�ۼ���
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
        var sampleHeight = 600;                       // �������� ���� ������ ���� 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 
-->
</script>
