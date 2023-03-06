<%@ include file="configHead.jsp"%>
<%@ page		
	info= "사용자 정보 출력"		
	contentType = "text/html; charset=euc-kr" 		
	import="com.anbtech.text.Hanguel"
	import="java.io.*"
%>
<jsp:useBean id="bean2" scope="request" class="com.anbtech.ViewQueryBean" />

<% 
	String mode = request.getParameter("mode")==null?"":request.getParameter("mode");
	bean2.openConnection();

	if(mode.equals("modify")){ // 수정 했을 경우
		String mquery = "update user_table set main_job = '"+Hanguel.toHanguel(request.getParameter("main_job"))+"', office_tel = '"+Hanguel.toHanguel(request.getParameter("tel"))+"', email = '"+request.getParameter("email")+"', address = '"+Hanguel.toHanguel(request.getParameter("address"))+"', home_tel = '"+Hanguel.toHanguel(request.getParameter("hometel"))+"', hand_tel = '"+Hanguel.toHanguel(request.getParameter("handtel"))+"', review = '"+Hanguel.toHanguel(request.getParameter("review"))+"', decision = '"+Hanguel.toHanguel(request.getParameter("decision"))+"' where id = '"+request.getParameter("sid")+"'";
		//out.print(mquery);
		bean2.executeUpdate(mquery);
%>
		<script language="javascript">alert("정상적으로 수정되었습니다.");</script>
<%
	}
	String type = request.getParameter("type");
 	String sid = request.getParameter("sid")==null?login_id:request.getParameter("sid");
	String sql2="select a.*,b.ac_name,c.ar_name from user_table a,class_table b,rank_table c where id='"+sid+"' and a.ac_id = b.ac_id and a.rank = c.ar_code";
	bean2.executeQuery(sql2);
	bean2.next();
%>
<HTML>
<HEAD>
<title>그룹웨어 사용자 상세 정보</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</HEAD>

<BODY topmargin=0 leftmargin=0 marginwidth=0>
<table cellspacing=0 cellpadding=2 width=100% border=0>
   <tbody>
	 <tr><td height=5 colspan="4"></td></tr>
	 <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">이&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;름</td>
       <td width="30%%" height="25" class="bg_04"><%=bean2.getData("name")%></td>
       <td width="50%" height="25" class="bg_03" rowspan="10">
			<%
				String picpath =  root_path + "/admin/user/picture/"+ bean2.getData("id")+".gif";
				File imgexist = new File(picpath);
				if(imgexist.exists()){
			%>
				<p align="center"><img src="../admin/user/picture/<%=bean2.getData("id")%>.gif" border="0" width="100" height="120">
			<%	}else{	%>
				<p align="center"><img src="../admin/user/picture/default.gif" border="0" width="100" height="120">
			<%	}	%>	   
	   </td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">직&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;급</td>
       <td width="30%%" height="25" class="bg_04"><%=bean2.getData("ar_name")%></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">사&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;번</td>
       <td width="30%%" height="25" class="bg_04"><%=bean2.getData("id")%></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">입 사 일</td>
       <td width="30%%" height="25" class="bg_04"><%=bean2.getData("enter_day").substring(0,4) + "년 " + bean2.getData("enter_day").substring(4,6) + "월 " + bean2.getData("enter_day").substring(6,8) + "일"%></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">부 서 명</td>
       <td width="30%%" height="25" class="bg_04"><%=bean2.getData("ac_name")%></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
<% if(bean2.getData("id").equals(login_id)){%>
	 <tr><form name="modify" method="post" action="UserInfoView.jsp" style="margin:0">
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">주요업무</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><input type="text" size="20" name="main_job" value="<%=bean2.getData("main_job")%>"></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">회사전화</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><input type="text" size="10" name="tel" value="<%=bean2.getData("office_tel")%>"></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">전자우편</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><input type="text" size="20" name="email" value="<%=bean2.getData("email")%>"></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">자택주소</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><input type="text" size="40" name="address" value="<%=bean2.getData("address")%>"></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">자택전화</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><input type="text" size="15" name="hometel" value="<%=bean2.getData("home_tel")%>"></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">휴대전화</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><input type="text" size="15" name="handtel" value="<%=bean2.getData("hand_tel")%>"></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr></tbody></table>

<table cellspacing=0 cellpadding=2 width=100% border=0>
   <tbody>
     <tr><td height=5 colspan="4"></td></tr>
	 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">검토자사번</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><input type="text" size="10" name="review" value="<%=bean2.getData("review")%>"></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">승인자사번</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><input type="text" size="10" name="decision" value="<%=bean2.getData("decision")%>"> <a href="javascript:javascript:document.modify.submit();"><img src="images/bt_save.gif" border="0" align="absmiddle"></a> </td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr><td height=5 colspan="4"></td></tr></tbody></table>
<input type="hidden" name="sid" value="<%=bean2.getData("id")%>">
<input type="hidden" name="mode" value="modify">
</form>
<%}else{%>

	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">주요업무</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><%=bean2.getData("main_job")%></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">회사전화</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><%=bean2.getData("office_tel")%></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">전자우편</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><%=bean2.getData("email")%></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">자택주소</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><%=bean2.getData("address")%></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">자택전화</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><%=bean2.getData("home_tel").equals("null")?"":bean2.getData("home_tel")%></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
	 <tr>
       <td width="20%" height="25" class="bg_03" background="images/bg-01.gif">휴대전화</td>
       <td width="80%" height="25" colspan="3" class="bg_04"><%=bean2.getData("hand_tel")%></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
     <tr><td height=10 colspan="4"></td></tr></tbody></table>
<% }%>

</BODY>
</HTML>

<script language="javascript">

function centerWindow() 
{ 
        var sampleWidth = 700;                        // 윈도우의 가로 사이즈 지정 
        var sampleHeight = 500;                       // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

function view (item,word)
{
	var f = document.sForm.UserList
	var sid =""
	for (i=0;i<f.length;i++)
    {
        if(f.options[i].selected == true){
			sid += f.options[i].value;
			break;
		}
    }
	location.href="UserList2.jsp?sid="+sid+"&sItem="+item+"&sWord="+word
}

function changeAll()
{
	var f = document.mForm;
	if(f.sItem.options[0].selected){
		f.sItem.value = "name";
		f.sWord.value = "";
		f.submit();
	}

}

</script>