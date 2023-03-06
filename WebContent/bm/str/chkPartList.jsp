<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "BOM 검사"		
	contentType = "text/html; charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.bm.entity.mbomStrTable table;
	com.anbtech.text.StringProcess str = new StringProcess();
	
	//----------------------------------------------------
	//	파라미터 읽기
	//----------------------------------------------------
	String assy_dup = (String)request.getAttribute("assy_dup");		//Assy 중복검사
	String loc_dup = (String)request.getAttribute("loc_dup");		//Location유무, 중복검사

	//Assy 중복검사 출력 
	if(assy_dup.length() != 0) {
		//3번째 , 에 <br>값 입력하기 
		String msg_data = "";
		int com_cnt = 0;
		for(int i=0; i<assy_dup.length(); i++) {
			//comma 갯수 counter
			if(assy_dup.charAt(i)==',') com_cnt++;

			//3번째에 <br>값 넣기
			if(com_cnt == 3) { msg_data += "<br>"; com_cnt = 0; }
			else msg_data += assy_dup.charAt(i);
		}
		assy_dup = msg_data;

		// | 에 <br>값 입력하기 : script 에서 사용됨
		assy_dup = str.repWord(assy_dup,"|","<br>");
	}
	else assy_dup = "Assy중복검사는 정상입니다.<br><br>";

	//Location유무, 중복검사
	// | 에 <br>값 입력하기 : script 에서 사용됨
	loc_dup = str.repWord(loc_dup,"|","<br>");
	if(loc_dup.length() == 0) loc_dup = "Location검사 결과는 정상입니다.";
%>

<HTML>
<HEAD><TITLE>BOM구성 검사</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- 타이틀 및 페이지 정보 -->
			<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=5 width="100%" border=0>
				<TBODY>
					<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
					<TR><TD valign='middle' height='32' bgcolor="#73AEEF"><img src="../bm/images/pop_bm_chkbom.gif" align="absmiddle" alt='BOM 검사결과 [Assy검사,Location검사]'> </TD></TR>
					<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
					</TD></TR>
		<TR bgColor='' height=10><TD></TD></TR>
  
<!--리스트-->
	<TR height=100%>
		<TD vAlign=top>
		
		<TABLE cellSpacing=0 cellPadding=8 width="98%" border=0 align=center>
			<TBODY>
				<TR><TD height='1' bgcolor='#9DA8BA' colspan='4'></TD></TR>
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					<td width="20%" height="50" class="bg_03" background="../bm/images/bg-01.gif" valign='top'>Assy검사</td>
					<td width="80%" height="50" class="bg_04">
						<!-- TABLE 안쪽 검사내용-->
						<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TBODY><TR><TD valign='top'>
						<DIV id="assy_check" style="position:relative; visibility:visible; width:100%; height:30; overflow-x:auto; overflow-y:scroll;">
						<%=assy_dup%>
						</DIV>
						</TD></TR></TBODY></TABLE>
						<!--------------------->
					</td>			
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					<td width="20%" height="50" class="bg_03" background="../bm/images/bg-01.gif"  valign='top'>Location검사</td>
					<td width="80%" height="100" class="bg_04">
						<!-- TABLE 안쪽 검사내용-->
						<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TBODY><TR><TD valign='top'>
							<DIV id="item_list" style="position:relative; visibility:visible; width:100%; height:100; overflow-x:auto; overflow-y:scroll;">	<%=loc_dup%>
							</DIV>
						</TD></TR></TBODY></TABLE>
						<!--------------------->
					</td>
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>	</TBODY></TABLE></TD></TR>
				<!--꼬릿말-->
				
				<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
					<a href='javascript:self.close()'>
					<img src='../bm/images/bt_close.gif' border='0' align='absmiddle'></a></TD></TR>
				</TD></TR>
		</TABLE></TBODY></TABLE>
</BODY>
</HTML>


<SCRIPT language='javascript'>
	// 페이지정보가 오른쪽으로 밀리지 않도록 처리
function display() { 

    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var div_h = h - 800 ;
	var div_assycheck = h - 870 ;
	
	item_list.style.height = div_h;
	assy_check.style.height = div_assycheck;
}
-->

</SCRIPT>

