<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���ڰ��� �ٰŹ��� LINK"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<%
	//���� ������ȣ
	String pid = request.getParameter("pid");			//������ȣ
	if(pid == null) pid = ""; else if(pid.equals("null")) pid = "";
	String doc_id = request.getParameter("doc_id");		//���ù��� ������ȣ
	if(doc_id == null) doc_id = ""; else if(doc_id.equals("null")) doc_id = "";
	String flag = request.getParameter("flag");			//�������
	if(flag == null) flag = ""; else if(flag.equals("null")) flag = "";

	//���缱�� ������� ó��
	String line = Hanguel.toHanguel(request.getParameter("line"));			//���缱 ����
	if(line == null) line = ""; else if(line.equals("null")) line = "";
	String vdate = request.getParameter("vdate");		//������ �������� (����ǥ�ø� ����)
	if(vdate == null) vdate = ""; else if(vdate.equals("null")) vdate = "";
	String ddate = request.getParameter("ddate");		//������ �������� (����ǥ�ø� ����)
	if(ddate == null) ddate = ""; else if(ddate.equals("null")) ddate = "";
	String wid = request.getParameter("wid");			//����ڻ��
	if(wid == null) wid = ""; else if(wid.equals("null")) wid = "";
	String vid = request.getParameter("vid");			//�����ڻ��
	if(vid == null) vid = ""; else if(vid.equals("null")) vid = "";
	String did = request.getParameter("did");			//�����ڻ��
	if(did == null) did = ""; else if(did.equals("null")) did = "";
	String wname = Hanguel.toHanguel(request.getParameter("wname"));			//����� �̸�
	if(wname == null) wname = ""; else if(wname.equals("null")) wname = "";
	String vname = Hanguel.toHanguel(request.getParameter("vname"));			//������ �̸�
	if(vname == null) vname = ""; else if(vname.equals("null")) vname = "";
	String dname = Hanguel.toHanguel(request.getParameter("dname"));			//������ �̸�
	if(dname == null) dname = ""; else if(dname.equals("null")) dname = "";
	String PROCESS = request.getParameter("PROCESS");			//PROCESS��
	if(PROCESS == null) PROCESS = ""; else if(PROCESS.equals("null")) PROCESS = "";
	String doc_ste = request.getParameter("doc_ste");			//�������
	if(doc_ste == null) doc_ste = ""; else if(doc_ste.equals("null")) doc_ste = "";


	if(flag.length() != 0) {
		if(flag.equals("OE_CHUL")) {					//�����
			response.sendRedirect("./SpaceLink/oe_chul_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("CHULJANG_SINCHEONG")) {	//�����û��
			response.sendRedirect("./SpaceLink/chuljang_sincheong_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("HYU_GA")) {				//��(��)����
			response.sendRedirect("./SpaceLink/hyu_ga_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("GEN")) {					//�Ϲݹ�������
			response.sendRedirect("./SpaceLink/general_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("BOGO")) {					//����
			response.sendRedirect("./SpaceLink/bogo_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("CHULJANG_BOGO")) {			//���庸��
			response.sendRedirect("./SpaceLink/chuljang_bogo_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("GIAN")) {					//��ȼ�
			response.sendRedirect("./SpaceLink/gian_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("MYEONGHAM")) {				//���Խ�û��
			response.sendRedirect("./SpaceLink/myeongham_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("SAYU")) {					//������
			response.sendRedirect("./SpaceLink/sayu_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("HYEOPJO")) {				//������
			response.sendRedirect("./SpaceLink/hyeopjo_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("YEONJANG")) {				//����ٹ���û��
			response.sendRedirect("./SpaceLink/yeonjang_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("GUIN")) {					//�����Ƿڼ�
			response.sendRedirect("./SpaceLink/guin_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("GYOYUK_ILJI")) {			//��������
			response.sendRedirect("./SpaceLink/gyoyuk_ilji_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("BAE_CHA")) {				//������û��
			response.sendRedirect("./module/baecha_sincheong_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("AKG")) {					//���ο�
			response.sendRedirect("./module/acknowledgment_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("TD")) {					//�������
			response.sendRedirect("./module/technical_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("SERVICE")) {				//������
			response.sendRedirect("./module/service_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("ODT")) {					//��������
			response.sendRedirect("./module/OfficialDocument_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("IDS")) {					//�系����
			response.sendRedirect("./module/InDocument_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("ODS")) {					//��ܰ���
			response.sendRedirect("./module/OutDocument_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("ASSET")) {					//�ڻ����
			response.sendRedirect("./module/Asset_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("EST")) {					//��������
			response.sendRedirect("./module/estimate_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("EWK")) {					//Ư�ٰ���
			response.sendRedirect("./module/extrawork_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("BOM")) {					//BOM����
			response.sendRedirect("./module/mbom_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("DCM")) {					//���躯�����
			response.sendRedirect("./module/cbom_eccLink.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("PCR")) {					//���ſ�û����
			response.sendRedirect("./module/pu_PurchaseLink.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("ODR")) {					//���ֿ�û����
			response.sendRedirect("./module/pu_OrderLink.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
	}

%>