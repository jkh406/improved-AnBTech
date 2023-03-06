<%@ include file="../../../admin/configHead.jsp"%>
<%@ include file="../../../admin/chk/chkDM01.jsp"%>
<%@ page
	language	= "java" 
	import		= "java.sql.*" 
	contentType	= "text/HTML;charset=KSC5601" 
	errorPage	= "../../../admin/errorpage.jsp"
%>
<jsp:useBean id="recursion"  class="com.anbtech.admin.db.makeClassTree"/>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
/* 사용자 추가 */

    bean.openConnection();	

	String query = "";
	String caption = "";
	String j		=	request.getParameter("j")==null?"a":request.getParameter("j");		// 모드
	String ac_id	=	request.getParameter("ac_id");	// 사원이 포함된 조직의 ID
	String au_id	=	request.getParameter("au_id");	// 사원 ID
	String rank		=	"";
	String ar_name	=	"";
	String name		=	"";
	String id		=	"";
	String passwd	=	"";
	String email	=	"";
	String office_tel	=	"";
	String hand_tel	=	"";
	String fax		=	"";
	String main_job	=	"";
	String address	=	"";
	String post_no	=	"";
	String home_tel	=	"";
	String enter_day	=	"";
	String regi_date	=	"";
	String access_code  = "";
	String ac_code		= "";
	String code			= "";

	if( j.equals("u")){ // 사용자 정보 수정
		query = "select a.*,b.ar_name from user_table a, rank_table b where a.au_id = '"+au_id+"' and a.rank = b.ar_code";
		bean.executeQuery(query);
		
		while(bean.next()){
			ac_id		= bean.getData("ac_id");
			rank		= bean.getData("rank");
			ar_name		= bean.getData("ar_name");
			name		= bean.getData("name");
			id			= bean.getData("id");
			passwd		= bean.getData("passwd");
			email		= bean.getData("email");
			office_tel	= bean.getData("office_tel");
			hand_tel	= bean.getData("hand_tel");
			fax			= bean.getData("fax");
			main_job	= bean.getData("main_job");
			address		= bean.getData("address");
			post_no		= bean.getData("post_no");
			home_tel	= bean.getData("home_tel");
			enter_day	= bean.getData("enter_day");
			regi_date	= bean.getData("regi_date");
			access_code = bean.getData("access_code");

		}
		caption		 = "수정";
	}else if(j.equals("a")){ //사용자 추가
		caption = "등록";
	}

%>
<HTML>
<head><title></title></head>
<link rel="stylesheet" type="text/css" href="../../css/style.css">

<body topmargin="0" leftmargin="0" marginwidth="0" oncontextmenu="return false">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif" align="absmiddle"> 문서엑세스 권한설정</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				<a href="javascript:checkForm()"><IMG src='../images/bt_save.gif' align='absmiddle' border='0' style='cursor:hand'></a>
				<IMG src='../images/bt_cancel.gif' onclick='history.back()' align='absmiddle' border='0' style='cursor:hand'>
				</TD></TR></TBODY>
		</TABLE></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TBODY></TABLE>

<form name="frm1" method="get" action="userp.jsp" style="margin:0">
<!--사용자 정보-->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TR>
	<TD height=22 colspan="4"><img src="../images/user_info.gif" width="209" height="25" border="0"></TD></TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">소속</TD>
    <TD width="35%" height="25" class="bg_04" ><%=recursion.viewHistory_str(Integer.parseInt(ac_id),"")%></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">직위</TD>
    <TD width="35%" height="25" class="bg_04" ><%=ar_name%></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">성명</TD>
    <TD width="35%" height="25" class="bg_04" ><%=name%></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">사번</TD>
    <TD width="35%" height="25" class="bg_04" ><%=id%></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR></TABLE>

<%
	String str[]={"A","C","D","N"};
	String text[]={"모든문서 ACCESS","동일본부문서 ACCESS","동일부서문서 ACCESS","모든문서 접근불능"};
	String[] code_value={"","","","",""};
	String code_value_temp;

	if(!access_code.equals("")) {
		
		int codelength=access_code.length();
		code_value=new String[codelength];
		
		for(int k=0;k<codelength;k++){
			code_value_temp=access_code;
			code_value[k]=code_value_temp.substring(k,k+1);
		}
	}
%>

<!--기술문서 엑세스권한정보-->
<TABLE  cellSpacing=0 cellPadding=0 width="100%" border=0>
	<TR><TD height=10 colspan="4"></TD></TR>
	<TR><TD height=22 colspan="4"><img src="../images/dms_access_info.gif" width="209" height="25" border="0"></TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
	<TR>
		<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">1급문서</TD>
		<TD width="35%" height="25" class="bg_04" >
			<SELECT name="authority_1Doc">
				<OPTION value="N" <% if(access_code.equals("N")) {%> selected <%}%> > 모든문서 접근불능 </OPTION>
				<%
					for(int i=0;i<4;i++) {      %>
					<OPTION value="<%=str[i]%>" <% if(str[i].equals(code_value[0])){%> selected <%}%> > <%=text[i]%> </OPTION>
				<%  }                    	    %>
			</SELECT>		
		</TD>
		<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">2급문서</TD>
		<TD width="35%" height="25" class="bg_04" >
			<SELECT name="authority_2Doc">
				<OPTION value="N" <% if(access_code.equals("N")) {%> selected <%}%> > 모든문서 접근불능 </OPTION>
				<%
					for(int i=0;i<4;i++) {      %>
					<OPTION value="<%=str[i]%>" <% if(str[i].equals(code_value[1])){%> selected <%}%> > <%=text[i]%> </OPTION>
				<%  }                    		%>
			</SELECT>		
		</TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
	<TR>
		<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">3급문서</TD>
		<TD width="35%" height="25" class="bg_04" >
			<SELECT name="authority_3Doc">
				<OPTION value="C" <% if(access_code.equals("C")) {%> selected <%}%> > 동일본부문서 ACCESS </OPTION>
				<%
					for(int i=0;i<4;i++) {      %>
					<OPTION value="<%=str[i]%>" <% if(str[i].equals(code_value[2])){%> selected <%}%> > <%=text[i]%> </OPTION>
				<%  }                   		%>
			</SELECT>		
		</TD>
		<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">대외비문서</TD>
		<TD width="35%" height="25" class="bg_04" >
			<SELECT name="authority_4Doc">
				<OPTION value="C" <% if(access_code.equals("C")) {%> selected <%}%> > 동일본부문서 ACCESS </OPTION>
				<%
					for(int i=0;i<4;i++) {      %>
					<OPTION value="<%=str[i]%>" <% if(str[i].equals(code_value[3])){%> selected <%}%> > <%=text[i]%> </OPTION>
				<%  }                           %>
			</SELECT>		
		</TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
	<TR>
		<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">일반문서</TD>
		<TD width="35%" height="25" class="bg_04" >
			<SELECT name="authority_5Doc">
				<OPTION value="A" <% if(access_code.equals("A")) {%> selected <%}%> > 모든문서 ACCESS </OPTION>
				<%
					for(int i=0;i<4;i++) {      %>
					<OPTION value="<%=str[i]%>" <% if(str[i].equals(code_value[4])){%> selected <%}%> > <%=text[i]%> </OPTION>
				<%  }                       	%>
			</SELECT>		
		</TD>
		<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif"></TD>
		<TD width="35%" height="25" class="bg_04" ></TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
</TABLE>

<input type="hidden" name="j" value="<%=j%>">
<input type="hidden" name="au_id" value="<%=au_id%>">
<input type="hidden" name="ac_id" value="<%=ac_id%>">
<input type="hidden" name="authorityString">
</BODY>
</HTML>


<script>
<!--
 
function checkForm()
{
	var f = document.frm1;

	var concat=f.authority_1Doc.value+f.authority_2Doc.value+f.authority_3Doc.value+f.authority_4Doc.value+f.authority_5Doc.value;
	f.authorityString.value=concat;
	f.submit();
}
-->
</script>