<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "전자결재 근거문서 LINK"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<%
	//문서 관리번호
	String pid = request.getParameter("pid");			//관리번호
	if(pid == null) pid = ""; else if(pid.equals("null")) pid = "";
	String doc_id = request.getParameter("doc_id");		//관련문서 관리번호
	if(doc_id == null) doc_id = ""; else if(doc_id.equals("null")) doc_id = "";
	String flag = request.getParameter("flag");			//양식종류
	if(flag == null) flag = ""; else if(flag.equals("null")) flag = "";

	//결재선및 결재사인 처리
	String line = Hanguel.toHanguel(request.getParameter("line"));			//결재선 내용
	if(line == null) line = ""; else if(line.equals("null")) line = "";
	String vdate = request.getParameter("vdate");		//검토자 검토일자 (사인표시를 위해)
	if(vdate == null) vdate = ""; else if(vdate.equals("null")) vdate = "";
	String ddate = request.getParameter("ddate");		//승인자 승인일자 (사인표시를 위해)
	if(ddate == null) ddate = ""; else if(ddate.equals("null")) ddate = "";
	String wid = request.getParameter("wid");			//기안자사번
	if(wid == null) wid = ""; else if(wid.equals("null")) wid = "";
	String vid = request.getParameter("vid");			//검토자사번
	if(vid == null) vid = ""; else if(vid.equals("null")) vid = "";
	String did = request.getParameter("did");			//승인자사번
	if(did == null) did = ""; else if(did.equals("null")) did = "";
	String wname = Hanguel.toHanguel(request.getParameter("wname"));			//기안자 이름
	if(wname == null) wname = ""; else if(wname.equals("null")) wname = "";
	String vname = Hanguel.toHanguel(request.getParameter("vname"));			//검토자 이름
	if(vname == null) vname = ""; else if(vname.equals("null")) vname = "";
	String dname = Hanguel.toHanguel(request.getParameter("dname"));			//승인자 이름
	if(dname == null) dname = ""; else if(dname.equals("null")) dname = "";
	String PROCESS = request.getParameter("PROCESS");			//PROCESS명
	if(PROCESS == null) PROCESS = ""; else if(PROCESS.equals("null")) PROCESS = "";
	String doc_ste = request.getParameter("doc_ste");			//결재상태
	if(doc_ste == null) doc_ste = ""; else if(doc_ste.equals("null")) doc_ste = "";


	if(flag.length() != 0) {
		if(flag.equals("OE_CHUL")) {					//외출계
			response.sendRedirect("./SpaceLink/oe_chul_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("CHULJANG_SINCHEONG")) {	//출장신청서
			response.sendRedirect("./SpaceLink/chuljang_sincheong_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("HYU_GA")) {				//휴(공)가원
			response.sendRedirect("./SpaceLink/hyu_ga_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("GEN")) {					//일반문서보고서
			response.sendRedirect("./SpaceLink/general_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("BOGO")) {					//보고서
			response.sendRedirect("./SpaceLink/bogo_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("CHULJANG_BOGO")) {			//출장보고
			response.sendRedirect("./SpaceLink/chuljang_bogo_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("GIAN")) {					//기안서
			response.sendRedirect("./SpaceLink/gian_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("MYEONGHAM")) {				//명함신청서
			response.sendRedirect("./SpaceLink/myeongham_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("SAYU")) {					//사유서
			response.sendRedirect("./SpaceLink/sayu_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("HYEOPJO")) {				//협조전
			response.sendRedirect("./SpaceLink/hyeopjo_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("YEONJANG")) {				//연장근무신청서
			response.sendRedirect("./SpaceLink/yeonjang_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("GUIN")) {					//구인의뢰서
			response.sendRedirect("./SpaceLink/guin_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("GYOYUK_ILJI")) {			//교육일지
			response.sendRedirect("./SpaceLink/gyoyuk_ilji_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("BAE_CHA")) {				//배차신청서
			response.sendRedirect("./module/baecha_sincheong_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("AKG")) {					//승인원
			response.sendRedirect("./module/acknowledgment_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("TD")) {					//기술문서
			response.sendRedirect("./module/technical_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("SERVICE")) {				//고객관리
			response.sendRedirect("./module/service_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("ODT")) {					//공지공문
			response.sendRedirect("./module/OfficialDocument_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("IDS")) {					//사내공문
			response.sendRedirect("./module/InDocument_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("ODS")) {					//사외공문
			response.sendRedirect("./module/OutDocument_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("ASSET")) {					//자산관리
			response.sendRedirect("./module/Asset_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("EST")) {					//견적관리
			response.sendRedirect("./module/estimate_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("EWK")) {					//특근관리
			response.sendRedirect("./module/extrawork_FP_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("BOM")) {					//BOM관리
			response.sendRedirect("./module/mbom_link.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("DCM")) {					//설계변경관리
			response.sendRedirect("./module/cbom_eccLink.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("PCR")) {					//구매요청관리
			response.sendRedirect("./module/pu_PurchaseLink.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
		else if(flag.equals("ODR")) {					//발주요청관리
			response.sendRedirect("./module/pu_OrderLink.jsp?doc_id="+doc_id+"&line="+line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+pid);
		}
	}

%>