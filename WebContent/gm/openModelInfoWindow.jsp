<%@ include file="../admin/configPopUp.jsp"%>
<%@ page
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.pu.entity.*,com.anbtech.st.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%	/**** 모델정보 검색 ***************************************************************/
	/*  1. 호출 page에서 필요한 정보의 field name을 Parameter로 받는다.
	/*     
	/*  2. 모델정보를 가져오는 iframe은 모델의 정보를 얻기위해 Servlet으로
	/*     바로 접근한다.
	/*     1) servlet을 거쳐 접근한 jsp page에 "returnValue()"호출해서 
	/*        받아온 정보를 현재page(openModelInfoWindow.jsp)로 setting 하도록한다.
	/*        setting후 openModelInfoWindow.jsp에서 호출 page로 값을 넘겨주는 
	/*        메소드 (parent.return_value()) 자동 실행되어  호출한 page에 목적
	/*        정보를 넘겨준다.
	/*        요구정보 setting및 배열의 내용은 GoodsInfoDAO에서 searchModelList()을 참조한다.
	/*        정보 추가시 GoodsInfoDAO에서 구성하도록한다. 
	/*    
	/*   #. 호출페이지에서 parameter 넘기는 String 
	/*   var strUrl = "../gm/openModelInfoWindow.jsp?one_class=제품군 field명
	/*                &one_name=제품군명 field명&two_class=제품코드 field명&two_name=제품명 field명
	/*                &three_class=모델군코드 field명&three_name=모델군명 field 명
	/*                &four_class=모델코드 field명&four_name=모델명 field명&fg_code=fg코드 field명";
	/*
	/*	 #.	정보 추가시 수정해야할 파일 및 method
    /*      1. 호출 페이지의 요구정보 변수들 넘겨주는 parameter에 추가
	/*      2. gm/openModelInfoWindow.jsp (호출page에서 받기, iframe에서 받기)
	/*      3. GoodsInfoDAO 에 받을 정보 추가
	/*      4. gm/searchModelInfo.jsp 에 추가field 추가
	/*
	/********************************************************************************************/


	// 호출(요구)정보 field명 
	String one_class   = request.getParameter("one_class");		// 제품군코드
	String one_name    = request.getParameter("one_name");		// 제품군명
	String two_class   = request.getParameter("two_class");		// 제품코드
	String two_name    = request.getParameter("two_name");		// 제품명
	String three_class = request.getParameter("three_class");	// 모델군코드
	String three_name  = request.getParameter("three_name");	// 모델군명
	String four_class  = request.getParameter("four_class");	// 모델코드
	String four_name   = request.getParameter("four_name");		// 모델군명
	String fg_code	   = request.getParameter("fg_code");		// f/g코드	
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../../gm/css/style.css">
<HEAD>
<TITLE>모델정보검색</TITLE>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="eForm" style="margin:0">

<TABLE  cellSpacing=0 cellPadding=0 width="100%">
	<TR><TD vAlign=top>
		<TABLE  cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF" ><img src="images/gm_modelsearch.gif" alt='모델정보검색'></TD>
					<TD style="padding-right:10px" align='right' valign='middle' bgcolor="#73AEEF"></TD></TR>
				<TR><TD height='1' bgcolor='#9DA8BA' colspan='2'></TD></TR>
				<TR><TD height="2" bgcolor="2167B6"  colspan='2'></TD></TR>
				</TBODY></TABLE></TD></TR>
	<TR height='32'><TD vAlign=middle>
		<TABLE cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR align='center'><TD>
					<IFRAME name="search" src="../servlet/GoodsInfoServlet?mode=search_model_info" width="98%" height="330" border="0" frameborder="0" scrolling="no">브라우저가 인라인 프레임을 지원하지 않거나 현재 인라인 프레임을 표시하지 않도록 설정되어 있습니다.</IFRAME></TD></TR>
				
				</TBODY></TABLE></TD></TR>
	<TR><TD height=5 colspan="2"></TD></TR>
	<!--꼬릿말-->
	<TR><TD>
		<TABLE cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR><TD width='820' height="33" align="right" bgcolor="#73AEEF" style='padding-right:4px'>
					<A href='javascript:self.close()'><img src='images/bt_close.gif' border='0' align='absright'></a></TD></TR>
				<TR><TD height="" bgcolor="0C2C55" colspan='2'></TD></TR></TBODY></TABLE></TD></TR>
		<!-- IFRAME에서 받아오는 변수 값 -->
		<INPUT type='hidden' name='fname'>
		<INPUT type='hidden' name='one_class'>
		<INPUT type='hidden' name='one_name'>
		<INPUT type='hidden' name='two_class'>
		<INPUT type='hidden' name='two_name'>
		<INPUT type='hidden' name='three_class'>
		<INPUT type='hidden' name='three_name'>
		<INPUT type='hidden' name='four_class'>
		<INPUT type='hidden' name='four_name'>
		<INPUT type='hidden' name='fg_code'>

		<!-- 호출 페이지의 Field명 -->
		<INPUT type='hidden' name='field_one_class' value='<%=one_class%>'>
		<INPUT type='hidden' name='field_one_name' value='<%=one_name%>'>
		<INPUT type='hidden' name='field_two_class' value='<%=two_class%>'>
		<INPUT type='hidden' name='field_two_name' value='<%=two_name%>'>
		<INPUT type='hidden' name='field_three_class' value='<%=three_class%>'>
		<INPUT type='hidden' name='field_three_name' value='<%=three_name%>'>
		<INPUT type='hidden' name='field_four_class' value='<%=four_class%>'>
		<INPUT type='hidden' name='field_four_name' value='<%=four_name%>'>
		<INPUT type='hidden' name='field_fg_code' value='<%=fg_code%>'>

</FORM>

</TABLE>
</BODY>
</HTML>

<script language='javascript'>
	
function return_value(){
	var f = document.forms[0];
	
		var one_class	= f.one_class.value;
		var one_name	= f.one_name.value;
		var two_class	= f.two_class.value;
		var two_name	= f.two_name.value;
		var three_class	= f.three_class.value;
		var three_name	= f.three_name.value;
		var four_class	= f.four_class.value;
		var four_name	= f.four_name.value;
		var fg_code		= f.fg_code.value;

		var f_one_class		= f.field_one_class.value;
		var f_one_name		= f.field_one_name.value;
		var f_two_class		= f.field_two_class.value;
		var f_two_name		= f.field_two_name.value;
		var f_three_class	= f.field_three_class.value;
		var f_three_name	= f.field_three_name.value;
		var f_four_class	= f.field_four_class.value;
		var f_four_name		= f.field_four_name.value;
		var f_fg_code		= f.field_fg_code.value;

		if(opener.document.forms[0].<%=one_class%>){
			opener.document.forms[0].<%=one_class%>.value = one_class;
		}

		if(opener.document.forms[0].<%=one_name%>){
			opener.document.forms[0].<%=one_name%>.value = one_name;
		}

		if(opener.document.forms[0].<%=two_class%>){
			opener.document.forms[0].<%=two_class%>.value = two_class;
		}

		if(opener.document.forms[0].<%=two_name%>){
			opener.document.forms[0].<%=two_name%>.value = two_name;
		}

		if(opener.document.forms[0].<%=three_class%>){
			opener.document.forms[0].<%=three_class%>.value = three_class;
		}

		if(opener.document.forms[0].<%=three_name%>){
			opener.document.forms[0].<%=three_name%>.value = three_name;
		}

		if(opener.document.forms[0].<%=four_class%>){
			opener.document.forms[0].<%=four_class%>.value = four_class;
		}

		if(opener.document.forms[0].<%=four_name%>){
			opener.document.forms[0].<%=four_name%>.value = four_name;
		}

		if(opener.document.forms[0].<%=fg_code%>){
			opener.document.forms[0].<%=fg_code%>.value = fg_code;
		}

		self.close();
}
</script>