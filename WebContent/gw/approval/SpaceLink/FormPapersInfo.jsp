<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*"%>
<%
	String pg = "";
	String fp = request.getParameter("ag_name2");
	if(fp == null) fp = ""; else if(fp.equals("null")) fp = "";

	if(fp.length() != 0) {
		if(fp.equals("OE_CHUL")) {						//외출계
			//response.sendRedirect("oe_chul_FP_write.jsp");
			pg = "oe_chul_FP_write.jsp";
		}
		else if(fp.equals("CHULJANG_SINCHEONG")) {		//출장신청서
			pg = "chuljang_sincheong_FP_write.jsp";
		}
		else if(fp.equals("HYU_GA")) {					//휴(공)가원
			pg = "hyu_ga_FP_write.jsp";
		}
		else if(fp.equals("GENER")) {					//일반문서
			pg = "general_FP_write.jsp";
		}
		else if(fp.equals("BOGO")) {					//보고서
			pg = "bogo_FP_write.jsp";
		}
		else if(fp.equals("CHULJANG_BOGO")) {			//출장보고서
			pg = "chuljang_bogo_FP_write.jsp";
		}
		else if(fp.equals("GIAN")) {					//기안서
			pg = "gian_FP_write.jsp";
		}
		else if(fp.equals("MYEONGHAM")) {				//명함신청서
			pg = "myeongham_FP_write.jsp";
		}
		else if(fp.equals("SAYU")) {					//사유서
			pg = "sayu_FP_write.jsp";
		}
		else if(fp.equals("HYEOPJO")) {					//협조전
			pg = "hyeopjo_FP_write.jsp";
		}
		else if(fp.equals("YEONJANG")) {				//연장근무신청서
			pg = "yeonjang_FP_write.jsp";
		}
		else if(fp.equals("GUIN")) {					//구인의뢰서
			pg = "guin_FP_write.jsp";
		}
		else if(fp.equals("GYOYUK_ILJI")) {				//교육일지
			pg = "gyoyuk_ilji_FP_write.jsp";
		}
		else if(fp.equals("BAE_CHA")) {					//배차신청서
			pg = "baecha_sincheong_FP_ReApp.jsp?link_id=1";
		}
		else if(fp.equals("PRJ_SOMO")) {				//프로젝트용소모품신청서
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
<h4>전자결재 양식 사용방법</h4>
<h5>1. 전자결재 상신방법</h5>
<ol>
	<li>전자결재 상신하고자 하는 양식을 선택합니다. </li>
	<li>해당양식의 입력란에 내용을 작성합니다. </li>
	<li>상신버튼을 누르면 전자결재로 상신됩니다. </li>
	<li>저장버튼을 누르면 전자결재의 저장함으로 임시저장 됩니다. </li>
	<li>저장함에 있는내용은 재작성하여 상신을 할 수 있습니다. </li>
</ol>
<h5>2. 전자결재 결재선 지정방법</h5>
<ol>
	<li>결재선은 기안,검토,승인,통보 순으로 구분됩니다. </li>
	<li>통보는 관련부서 담당자에게 전달되는 기능입니다. </li>
	<li>관련부서에 협조사항이 있을때는 검토 다음단계에 협조로 입력하면 됩니다.  </li>
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