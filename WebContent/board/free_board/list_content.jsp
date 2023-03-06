<form name='mbListForm' style="margin:0">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR><TD height='2' bgcolor='#9DA8BA' colspan="11"></TD></TR>
			<TR vAlign=middle height=25>
			  <TD noWrap width=50 align=middle class='list_title'>
				<%	if((String)session.getAttribute(tablename + "_adminid") != null){	%>
						<input type=checkbox name=checkbox onClick="check(document.mbListForm.checkbox)">
				<%	}else{	%> 	번호 <%	}	%>
			  </TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../board/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>글제목</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../board/images/list_tep.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>등록자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../board/images/list_tep.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>첨부파일</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../board/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>등록일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../board/images/list_tep.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>열람회수</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	//******************************
	//루프로 table내용을 내보내는 곳 loop  **
	//******************************
	while(table_iter.hasNext()){
	table = (Table)table_iter.next();
	String pos = table.getPos();
	String no = table.getNo();
	String filelink = table.getFilelink();
	String subject = table.getSubject();
	String writer = table.getWriter();
//	if (writer.length() > 10) writer = writer.substring(0, 10) + "..";
	String writer_s = com.anbtech.text.StringProcess.repWord(writer, "\"", "&quot;");
	writer_s = com.anbtech.text.StringProcess.repWord(writer_s, "'", "`");//writer_s 는 "",''처리를 위해 만듬(' 를 `로 변경)
	String email = table.getEmail();
	String w_time = table.getW_time();
	int vid = table.getVid();
	int rid = table.getRid();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'>
				<%
					if((String)session.getAttribute(tablename + "_adminid") != null) out.print(no);
					else out.print(pos);
				%>			  
			  </TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=subject%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=writer%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=filelink%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=w_time%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=rid%></td>
			</TR>
			<TR><TD colSpan=11 background="../board/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></form>

<script language = 'javascript'>

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function view_doc(strSrc,no) {

	//var sRtnValue=showModalDialog("../ods/DocModalFrm.jsp?"+strSrc+"&no="+no,"view_doc","dialogWidth:520px;dialogHeight:335px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=1;resizable=0");

	if(strSrc.indexOf("OutDocumentRecServlet") >= 0) wopen('../servlet/'+strSrc,'view','510','322','scrollbars=yes,toolbar=no,status=no,resizable=no');

	if(strSrc.indexOf("OfficialDocumentServlet") >= 0) wopen('../servlet/'+strSrc,'view','680','650','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

var checkflag = false; 

function check(field) { 
	if (checkflag == false) { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = true; 
		} 
	checkflag = true; 
	}else { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = false; 
		} 
	checkflag = false; 
	} 
}

function chkval() {
	var f = document.mbListForm.checkbox;
	var tablename = document.srForm.tablename.value;
	var category = document.srForm.category.value;
	var s_count = 0;
	var mids = "";
    for(i=1;i<f.length;i++){
		if(f[i].checked){
			mids += f[i].value+"|";
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("삭제할 항목을 선택하신 후, 실행하세요.");
	   return;
    }
	var del_confirm = confirm("선택항목을 삭제하시겠습니까?");
	if(del_confirm) location.href = "../servlet/AnBBoard?mode=deleteall&tablename="+tablename+"&category="+category+"&no="+mids;
}
</script>