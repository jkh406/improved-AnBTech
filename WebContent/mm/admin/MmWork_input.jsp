<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info = "작업장정보관리"
	errorPage	= "../../admin/errorpage.jsp"
	language = "java"
	contentType = "text/html;charset=KSC5601"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
    bean.openConnection();	

	String query		= "";
	String caption		= "";
	String j			= request.getParameter("j");		// 모드	
	
	String pid			= request.getParameter("pid");	//관리코드
	String work_type	= ""; 
	String work_no		= "자동생성";
	String work_name	= "";
	String mgr_id		 = "";
	String mgr_name		= "";
	String factory_no	= "";	
	String factory_name	= "";
	String c_image		= ""; // 타이틀 이미지

	if( j.equals("u")){ // 수정모드
		query = "SELECT * FROM mfg_work WHERE pid = '" + pid +"'";
		bean.executeQuery(query);

		while(bean.next()){
			pid			= bean.getData("pid");
			work_type	= bean.getData("work_type"); 
			work_no		= bean.getData("work_no");
			work_name	= bean.getData("work_name");
			mgr_id		= bean.getData("mgr_id");
			mgr_name	= bean.getData("mgr_name");
			factory_no	= bean.getData("factory_no");	
			factory_name= bean.getData("factory_name");	
		}
		caption = "수정";
		c_image = "../images/pop_modify_wplace_info.gif";
	}else if(j.equals("a")){ // 입력모드
		caption = "등록";
		c_image = "../images/pop_reg_wplace_info.gif";
	}

	//----------------------------------------------------
	//	편집 여부 판단하기
	//----------------------------------------------------
	String icon = "D";						//icon 출력여부
	String ab = "disabled";					//선택박스 수정가능 여부판단하기
	if(j.equals("a")) { ab=""; icon="E"; }


%>

<HTML><HEAD><TITLE>생산 작업장 관리정보 <%=caption%></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<FORM name="frm1" method="post" action="MmWork_process.jsp" onSubmit="return checkForm()">
<INPUT type=hidden name=j value='<%=j%>'>
<INPUT type=hidden name=pid value='<%=pid%>'>


<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
			<TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="<%=c_image%>" alt="작업장관리(<%=caption%>)"></TD></TR>
			<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<TABLE cellspacing=0 cellpadding=2 width="94%" border=0>
		<TBODY><TR><TD height=20 colspan="2"></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">작업장구분</TD>
				<TD width="70%" height="25" class="bg_04">
				<SELECT NAME="work_type" <%=ab%>>
			<%	String[] type = {"1","2"};
				String[] name = {"사내","외주"};
				for(int si = 0; si < type.length; si++) {
					String SEL = "";
					if(work_type.equals(type[si])) SEL = "SELECTED";
					else SEL = "";
					out.println("<OPTION value=" + type[si] + " " + SEL + ">" + name[si]); 
				}
			%>
				</SELECT></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">생산작업장번호</TD>
				<TD width="70%" height="25" class="bg_04">
				<INPUT type='text' name='work_no' value='<%=work_no%>' size='15'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">생산작업장명</TD>
				<TD width="70%" height="25" class="bg_04">
				<INPUT type='text' name='work_name' value='<%=work_name%>' size='15'></TD></TR>
		    <TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">생산담당자</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='mgr_id' value='<%=mgr_id%>' size='10' readonly>
				<INPUT type='text' name='mgr_name' value='<%=mgr_name%>' size='12' readonly>
				<a href="Javascript:searchSabun();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
				
			<!--
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">담당자명</TD>
				<TD width="70%" height="25" class="bg_04">
				<INPUT type='text' name='mgr_name' value='<%=mgr_name%>' size='20' readonly></TD></TR>-->
		    <TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">생산공장명</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='factory_no' value='<%=factory_no%>' size='10' readonly> 
				<INPUT type='text' name='factory_name' value='<%=factory_name%>' size='12' readonly>
			<% if(icon.equals("E")) { %>
				<a href="Javascript:searchDivision();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a>
			<% } %> </TD></TR>
		
			<!--
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">공장명</TD>
				<TD width="70%" height="25" class="bg_04">
				<INPUT type='text' name='factory_name' value='<%=factory_name%>' size='20' readonly></TD></TR>-->
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR></TBODY></TABLE><br>
	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"> 
			<IMG src='../images/bt_save.gif' onclick='javascript:checkForm()' align='absmiddle' style='cursor:hand'>
		    <IMG src='../images/bt_cancel.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'></TD>
          </TR>
          <TR>
            <TD width="100%" height=1 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></TD></TR>
</TABLE>
</form>
</BODY>
</HTML>


<script language=javascript>

function checkForm()
{
	var f = document.frm1;

	if(f.work_name.value == ""){ alert("작업장명을 입력하세요."); f.work_name.focus(); return; }
	if(f.mgr_id.value == ""){	alert("담당자를 입력하세요.");	return; }
	if(f.factory_no.value == ""){ alert("공장번호를 입력하세요."); return; }

	//a = f.mgr_code.selectedIndex;
	//var mgr_name = f.mgr_code.options[a].text;
	//f.mgr_name.value=mgr_name;
	f.submit();
}
//사번구하기
function searchSabun()
{
	wopen("../searchName.jsp?target=frm1.mgr_id/frm1.mgr_name","proxy","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//공장번호 찾기
function searchDivision()
{
	var f = document.frm1;
	var factory_no = f.factory_no.name;
	var factory_name = f.factory_name.name;
	
	var para = "field="+factory_no+"/"+factory_name;

	url = "../../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&"+para;
	wopen(url,'enterCode','400','307','scrollbars=yes,toolbar=no,status=no,resizable=no');

}
//창
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
</script>