<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*"%>
<%
	String pg = "";
	String fp = request.getParameter("ag_name2");
	if(fp == null) fp = ""; else if(fp.equals("null")) fp = "";

	if(fp.length() != 0) {
		if(fp.equals("OE_CHUL")) {						//�����
			//response.sendRedirect("oe_chul_FP_write.jsp");
			pg = "oe_chul_FP_write.jsp";
		}
		else if(fp.equals("CHULJANG_SINCHEONG")) {		//�����û��
			pg = "chuljang_sincheong_FP_write.jsp";
		}
		else if(fp.equals("HYU_GA")) {					//��(��)����
			pg = "hyu_ga_FP_write.jsp";
		}
		else if(fp.equals("GENER")) {					//�Ϲݹ���
			pg = "general_FP_write.jsp";
		}
		else if(fp.equals("BOGO")) {					//����
			pg = "bogo_FP_write.jsp";
		}
		else if(fp.equals("CHULJANG_BOGO")) {			//���庸��
			pg = "chuljang_bogo_FP_write.jsp";
		}
		else if(fp.equals("GIAN")) {					//��ȼ�
			pg = "gian_FP_write.jsp";
		}
		else if(fp.equals("MYEONGHAM")) {				//���Խ�û��
			pg = "myeongham_FP_write.jsp";
		}
		else if(fp.equals("SAYU")) {					//������
			pg = "sayu_FP_write.jsp";
		}
		else if(fp.equals("HYEOPJO")) {					//������
			pg = "hyeopjo_FP_write.jsp";
		}
		else if(fp.equals("YEONJANG")) {				//����ٹ���û��
			pg = "yeonjang_FP_write.jsp";
		}
		else if(fp.equals("GUIN")) {					//�����Ƿڼ�
			pg = "guin_FP_write.jsp";
		}
		else if(fp.equals("GYOYUK_ILJI")) {				//��������
			pg = "gyoyuk_ilji_FP_write.jsp";
		}
		else if(fp.equals("BAE_CHA")) {					//������û��
			pg = "baecha_sincheong_FP_ReApp.jsp?link_id=1";
		}
		else if(fp.equals("PRJ_SOMO")) {				//������Ʈ��Ҹ�ǰ��û��
			pg = "prj_somo_FP_write.jsp";
		}
	}
%>
<html>

<head>
<meta HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=KSC5601">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title></title>
</head> 
<body>
<!--
<h4>���ڰ��� ��� �����</h4>
<h5>1. ���ڰ��� ��Ź��</h5>
<ol>
	<li>���ڰ��� ����ϰ��� �ϴ� ����� �����մϴ�. </li>
	<li>�ش����� �Է¶��� ������ �ۼ��մϴ�. </li>
	<li>��Ź�ư�� ������ ���ڰ���� ��ŵ˴ϴ�. </li>
	<li>�����ư�� ������ ���ڰ����� ���������� �ӽ����� �˴ϴ�. </li>
	<li>�����Կ� �ִ³����� ���ۼ��Ͽ� ����� �� �� �ֽ��ϴ�. </li>
</ol>
<h5>2. ���ڰ��� ���缱 �������</h5>
<ol>
	<li>���缱�� ���,����,����,�뺸 ������ ���е˴ϴ�. </li>
	<li>�뺸�� ���úμ� ����ڿ��� ���޵Ǵ� ����Դϴ�. </li>
	<li>���úμ��� ���������� �������� ���� �����ܰ迡 ������ �Է��ϸ� �˴ϴ�.  </li>
</ol> 
-->
<form action='<%=pg%>' name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='m'>
</form>

</body>
</html>
<script language=javascript>
<!--
var fp = '<%=fp%>';
if(fp.length != 0) {
	document.eForm.action = '<%=pg%>';
	document.eForm.submit();
} else {
	document.eForm.action = "oe_chul_FP_write.jsp";
	document.eForm.submit();
}
-->
</script>