<%@ include file="../../admin/configHead.jsp"%>
<%@ page
	language	= "java"
	info		= "이력정보 수정"
	contentType = "text/html; charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query		= "";
	String j			= request.getParameter("j")==null?"a":request.getParameter("j");
	String ah_id		= request.getParameter("ah_id");
	String ap_id		= "";	// 고객회사 ID
	String at_id		= "";	// 고객 ID
	String ap_name		= "";
	String at_name		= "";

	String s_day		= "";
	String s_time		= "";
	String s_time_s		= "";
	String s_time_e		= "";
	String category		= "";
	String subject		= "";
	String content		= "";
	String result		= "";
	String other_customer = "";
	String file_name	= "";
	String file_size	= "";
	String umask		= "";
	String writer_id	= "";

	/***********************
	 * history_table	: a
	 * user_table		: b
	 ***********************/
	query = "SELECT a.*,b.name 'writer_id' FROM history_table a,user_table b WHERE a.ah_id = '"+ah_id+"' and a.au_id = b.id";
	bean.openConnection();	
	bean.executeQuery(query);
	while(bean.next()){
		s_day		= bean.getData("s_day"); 
		s_day		= s_day.substring(0,4)+"/"+s_day.substring(4,6)+"/"+s_day.substring(6,8);
		s_time		= bean.getData("s_time");
		s_time_s	= s_time.substring(0,5);
		s_time_e	= s_time.substring(6,11);

		category	= bean.getData("class");
		subject		= bean.getData("subject");
		content		= bean.getData("content");
		result		= bean.getData("result");
		if(result ==null) result = "없슴.";
		other_customer = bean.getData("other_customer");
		file_name	= bean.getData("file_name");
		file_size	= bean.getData("file_size");
		umask		= bean.getData("umask");

		ap_id		= bean.getData("ap_id"); // 회사 사업자 등록번호
		at_id		= bean.getData("at_id"); // 고객ID
		ap_name		= bean.getData("ap_name"); // 고객사명
		at_name		= bean.getData("at_name"); // 고객명
		writer_id	= bean.getData("writer_id");// 등록자 사번
		
	}
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
</head>

<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0' oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> 고객서비스이력 수정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="javascript:checkForm();"><img src="../images/bt_save.gif" border="0"></a></TD>
			  <TD align=left width=30><a href="javascript:history.go(-1);"><img src="../images/bt_cancel.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center"><form name="frm1" method="post" action="historyp.jsp" encType="multipart/form-data" style="margin:0">
    <!--일자,회사-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../images/basic_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">등록자</td>
           <td width="37%" height="25" class="bg_04"><input type="text" size="15" value="<%=login_name%>(<%=login_id%>)" readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문일자</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="s_day" size="10" value="<%=s_day%>" readOnly> <a href="Javascript:OpenCalendar('s_day');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문회사명</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="text" name="ap_name" size="30" value="<%=ap_name%>" readOnly class="text_01"> <a href="javascript:searchCompany();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a><input type="hidden" name="ap_id" value='<%=ap_id%>'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!-- 방문 시작 --> 
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../images/history.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문시간</td>
           <td width="37%" height="25" class="bg_04">
			<SELECT NAME="s_time_s" class="text_01">
			<%
			String[] asHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};

				for(int asH=0; asH<24; asH++){
					out.println("<OPTION value=" + asHour[asH] + ":" + "00 " + ">" + asHour[asH] + ":" + "00");
					out.println("<OPTION value=" + asHour[asH] + ":" + "30"+">" + asHour[asH] + ":" + "30");
				}
				out.println("</SELECT> ~ ");
			%>	
			<%	if(s_time_s !=null){	%>
					<script language='javascript'>
						document.frm1.s_time_s.value = '<%=s_time_s%>';
					</script>
			<%	}	%>	
			<SELECT NAME="s_time_e" class="text_01">
			<%
			String[] aeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};

				for(int aeH=0; aeH<24; aeH++){

					out.println("<OPTION value=" + aeHour[aeH] + ":" + "00 " + ">" + aeHour[aeH] + ":" + "00");
					out.println("<OPTION value=" + aeHour[aeH] + ":" + "30"+ ">" + aeHour[aeH] + ":" + "30");
				}
			%></SELECT>
			<%	if(s_time_e !=null){	%>
					<script language='javascript'>
						document.frm1.s_time_e.value = '<%=s_time_e%>';
					</script>
			<%	}	%>	
			<input type=hidden name=s_time>	</td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">고객명</td>
           <td width="37%" height="25" class="bg_04"><input type="hidden" name="at_id1" value="<%=at_id%>"><input type="text" name="at_name1" size="15" value="<%=at_name%>" readOnly class="text_01"> <a href="javascript:chooseCustomer();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문목적/제목</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
			<select size="1" name="category" class="text_01">
				<option value="">분류선택</option>
				<option value="A/S">A/S</option>
				<option value="상담">상담</option>
				<option value="견적">견적</option>
				<option value="설계">설계</option>
				<option value="제작">제작</option>
				<option value="교육(세미나)">교육(세미나)</option>
				<option value="도입">도입</option>
			</select> <input type="text" name="subject" size="50" value="<%=subject%>" class="text_01">
<%	if(category !=null){	%>
		<script language='javascript'>
			document.frm1.category.value = '<%=category%>';
		</script>
<%	}	%>	
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문내용</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="7" name="content" cols="80" class="text_01"><%=content%></textarea></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">이슈사항</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="3" name="result" cols="80"><%=result%></textarea></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">파일첨부</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<%	if(!umask.equals("") && umask != null && !umask.equals("null")){ 	
							out.print(file_name+" ("+file_size+"Byte)");
					}else {
							out.print("첨부된 파일이 없습니다.");
					}
				%><br>

				<input type="file" size="40" name="attach_file"><br>(새로 첨부하면 기존 첨부파일은 삭제됩니다.)
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
</td></tr></table>
<input type='hidden' name='j' value='<%=j%>'>
<input type='hidden' name='ah_id' value='<%=ah_id%>'>
<input type='hidden' name='au_id' value='<%=login_id%>'>
<input type='hidden' name='file_name' value='<%=file_name%>'>
<input type='hidden' name='file_size' value='<%=file_size%>'>
<input type='hidden' name='umask' value='<%=umask%>'>
</form>
</body>
</html>

<script language=javascript>
function chooseCustomer(){
	var sel = document.frm1.ap_id;
	var ap_id;

	if(sel.value==''){
		alert("고객사를 먼저 선택하십시오.");
		return;
	}else{
		ap_id = sel.value;
		window.open("chooseCustomer.jsp?ap_id="+ap_id,"chooseCustomer","width=330,height=370,scrollbar=no,toolbar=no,status=no,resizable=yes");
	}
}
function searchCompany(){
	//window.open("searchCompany.jsp","chooseCustomer","width=630,height=400,scrollbar=auto,toolbar=no,status=no,resizable=no");
	wopen("../../crm/company/searchCompany.jsp?sf=frm1&sid=ap_id&sname=ap_name","search_company",'600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function centerWindow() 
{ 
        var sampleWidth = 630;                        // 윈도우의 가로 사이즈 지정 
        var sampleHeight = 700;                       // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

function hide_all() { 
  document.all.menu_sub00.style.visibility = 'hidden'; 
} 

function show_div(divid){ 
        divid.style.visibility = 'visible'; 
}

function hide_div(divid){ 
        divid.style.visibility = 'hidden'; 
} 


function hide_thisdiv(divid){ 
        document.all.menu_sub00.style.visibility = 'visible'; 
        divid.style.visibility = 'hidden'; 
} 

function OpenCalendar(where) {
	var strUrl = "Calendar.jsp?where="+where;
	newWIndow = window.open(strUrl, "Calendar", "width=200, height=270");
}

function checkForm()
{
	var f = document.frm1;

	if(f.ap_id.value == ""){
			alert("대상 고객사를 선택하십시오.");
			f.ap_id.focus();
			return false;
	}

	
	if(f.at_id1.value == ""){
			alert("대상 고객을 선택하십시오.");
			//f.at_id1.focus();
			return false;
	}


	if(f.category.value == ""){
			alert("분류를 선택하십시오.");
			f.category.focus();
			return false;
	}
	if(f.subject.value == ""){
			alert("방문 목적을 입력하십시오.");
			f.subject.focus();
			return false;
	}
	if(f.content.value == ""){
			alert("방문 내용을 입력하십시오.");
			f.content.focus();
			return false;
	}

	var t = document.frm1;
	t.s_time.value = t.s_time_s.value + "/" + t.s_time_e.value;
	
	var tmp = document.frm1.s_day;
    var fromField=tmp.value.split("/")
    var syear	= fromField[0]
    var smonth	= fromField[1]
    var sday	= fromField[2]

	tmp.value = syear+smonth+sday;
	document.onmousedown=dbclick;

	f.submit();
}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
}
</script>


	<script type="text/javascript">
        var xmlHttp;
        var key;
        var bar_color = 'gray';
        var span_id = "block";
        var clear = "&nbsp;&nbsp;&nbsp;"

        function createXMLHttpRequest() {
            if (window.ActiveXObject) {
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            } 
            else if (window.XMLHttpRequest) {
                xmlHttp = new XMLHttpRequest();                
            }
        }

        function go() {
            createXMLHttpRequest();
            checkDiv();
            var url = "ProgressBarServlet?task=create";
            var button = document.getElementById("go");
            button.disabled = true;
            xmlHttp.open("GET", url, true);
            xmlHttp.onreadystatechange = goCallback;
            xmlHttp.send(null);
        }

        function goCallback() {
            if (xmlHttp.readyState == 4) {
                if (xmlHttp.status == 200) {
                    setTimeout("pollServer()", 2000);
                }
            }
        }
        
        function pollServer() {
            createXMLHttpRequest();
            var url = "ProgressBarServlet?task=poll&key=" + key;
            xmlHttp.open("GET", url, true);
            xmlHttp.onreadystatechange = pollCallback;
            xmlHttp.send(null);
        }
        
        function pollCallback() {
            if (xmlHttp.readyState == 4) {
                if (xmlHttp.status == 200) {
                    var percent_complete = xmlHttp.responseXML.getElementsByTagName("percent")[0].firstChild.data;
                    
                    var index = processResult(percent_complete);
                    for (var i = 1; i <= index; i++) {
                        var elem = document.getElementById("block" + i);
                        elem.innerHTML = clear;

                        elem.style.backgroundColor = bar_color;
                        var next_cell = i + 1;
                        if (next_cell > index && next_cell <= 9) {
                            document.getElementById("block" + next_cell).innerHTML = percent_complete + "%";
                        }
                    }
                    if (index < 9) {
                        setTimeout("pollServer()", 2000);
                    } else {
                        document.getElementById("complete").innerHTML = "Complete!";
                        document.getElementById("go").disabled = false;
                    }
                }
            }
        }
        
        function processResult(percent_complete) {
            var ind;
            if (percent_complete.length == 1) {
                ind = 1;
            } else if (percent_complete.length == 2) {
                ind = percent_complete.substring(0, 1);
            } else {
                ind = 9;
            }
            return ind;
        }

        function checkDiv() {
            var progress_bar = document.getElementById("progressBar");
            if (progress_bar.style.visibility == "visible") {
                clearBar();
                document.getElementById("complete").innerHTML = "";
            } else {
                progress_bar.style.visibility = "visible"
            }
        }
        
        function clearBar() {
            for (var i = 1; i < 10; i++) {
                var elem = document.getElementById("block" + i);
                elem.innerHTML = clear;
                elem.style.backgroundColor = "white";
            }
        }
    </script>