<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "과제검색하기[메인과제로 가능한과제] : SPACE LINK과제상태관리"		
	contentType = "text/html; charset=KSC5601" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.psm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.psm.entity.psmMasterTable table;
	com.anbtech.psm.entity.psmEnvTable color;
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String sItem = (String)request.getAttribute("sItem"); if(sItem == null) sItem = "pid";
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String psm_start_date = (String)request.getAttribute("psm_start_date"); 
	if(psm_start_date == null) psm_start_date = "";
	String first_year = (String)request.getAttribute("first_year"); if(first_year == null) first_year = "";
	String last_year = (String)request.getAttribute("last_year"); if(last_year == null) last_year = "";

	//년도 구간 구하기
	int fy = Integer.parseInt(first_year);
	int ly = Integer.parseInt(last_year);
	int df = ly - fy + 2;

	String[][] year = new String[df][2];
	year[0][0] = "";
	year[0][1] = "전체년도";
	for(int i=1,j=0; i<df; i++,j++) {
		year[i][0] = Integer.toString(fy+j);
		year[i][1] = Integer.toString(fy+j);
	}

	//전달해줄 파라미터
	String target = (String)request.getAttribute("target");				//서브릿에서
	if(target == null) target = request.getParameter("target");			//내부적으로

	String[] data = new String[10];
	StringTokenizer t = new StringTokenizer(target,"/");
	int n=0;
	while(t.hasMoreTokens()) {
		data[n]=t.nextToken();
		n++;
	}

	//--------------------------------------
	//리스트 가져오기
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("PSM_List");
	int pjt_cnt = table_list.size();
	table = new psmMasterTable();
	Iterator table_iter = table_list.iterator();

	String[][] project;
	if(pjt_cnt > 0) project = new String[pjt_cnt][10];		//데이터가 있을때
	else project = new String[1][10];						//데이터가 없을때
	int p = 0;
	while(table_iter.hasNext()) {
		table = (psmMasterTable)table_iter.next();
		project[p][0] = table.getPsmCode();				//과제코드
		project[p][1] = table.getPsmKorea();			//과제명

		project[p][2] = table.getPdCode();				//제품코드
		project[p][3] = table.getPdName();				//제품명
		project[p][4] = table.getCompName();			//과제고객
		project[p][5] = table.getCompCategory();		//과제카테고리

		project[p][6] = table.getContractDate();		//계약일자
		project[p][7] = table.getContractName();		//계약명
		project[p][8] = table.getContractPrice();		//계약금액
		project[p][9] = table.getCompleteDate();		//납기일자
		p++;
	}

%>

<HTML>
<HEAD>
<title>과제 찾기 </title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../psm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>
<form name="sForm" method="post" style="margin:0" encType="multipart/form-data" onSubmit='javascript:goSearch();return false;'>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">

	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../psm/images/pop_psm_item.gif"  alt='과제LIST'></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<!-- 본문 -->
	<table cellspacing=0 cellpadding=0 width="94%" border=0>
	   <tbody>
         <tr><td height="12"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1></td></tr>
         <tr>
           <td width="50%" style="padding-left:4px" background="../psm/images/bg-011.gif">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
				  <tr><td width="100%" height="100" valign='top'>
					   <%
						out.println("<select MULTIPLE size=6 name='project' onChange='javascript:goProject()' style=font-size:9pt;color='black';>");
						out.println("<OPTGROUP label='--------------------------------------'>");
						String psel = "";
						for(int i=0; i<pjt_cnt; i++) {
							psel = project[i][0]+"|"+project[i][1]+"|"+project[i][2]+"|"+project[i][3]+"|"+project[i][4]+"|"+project[i][5]+"|"+project[i][6]+"|"+project[i][7]+"|"+project[i][8]+"|"+project[i][9];
							out.print("<option value='"+psel+"'>");
							out.println(project[i][0]+" "+project[i][1]+"</option>");
						}
						out.println("</select>");
					   %>
					</td></tr>
					<tr><td width="100%" height="30" bgcolor="#EAF3FD"  align="center">
						<select name="psm_start_date" style=font-size:9pt;color="black";>  
						<%
							String ysel = "";
							for(int si=0; si<df; si++) {
								if(psm_start_date.equals(year[si][0])) ysel = "selected";
								else ysel = "";
								out.println("<option "+ysel+" value='"+year[si][0]+"'>"+year[si][1]+"</option>");
							}
						%>
						</select>
						<select name="sItem" style=font-size:9pt;color="black";>  
						<%
							String[] sitems = {"comp_name","comp_category","psm_korea"};
							String[] snames = {"과제고객","카테고리","과제명(한글)"};
							String sel = "";
							for(int si=0; si<sitems.length; si++) {
								if(sItem.equals(sitems[si])) sel = "selected";
								else sel = "";
								out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
							}
						%>
						</select>
						<input type="text" name="sWord" size="15" value="<%=sWord%>">
						<a href='Javascript:goSearch();'><img src='../psm/images/bt_search3.gif' border='0' align='absmiddle'></a>
				    </td></tr>
				</table>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height="13" colspan="2"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:self.close();"><img src="../psm/images/close.gif" width="46" height="19" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=1 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE>
	
	</td></tr>
</table>
<input type="hidden" name="mode" size="15" value="">
<input type="hidden" name="pid" size="15" value="">
<input type="hidden" name="pid" size="15" value="">
<input type='hidden' name='target' value='<%=target%>'>
<input type='hidden' name='psm_start_date' value='<%=psm_start_date%>'>
</form>

</BODY>
</HTML>

<script language=javascript>
<!--
//검색하기
function goSearch()
{
	document.sForm.action='../servlet/PsmProcessServlet';
	document.sForm.mode.value='search_single';
	document.sForm.submit();
}
//과제선택하기
function goProject()
{
	var project = document.sForm.project.value;
	var pjt = project.split('|');

	eval("opener.document." + '<%=data[0]%>').value = pjt[0];
	eval("opener.document." + '<%=data[1]%>').value = pjt[1];
	eval("opener.document." + '<%=data[2]%>').value = pjt[2];
	eval("opener.document." + '<%=data[3]%>').value = pjt[3];
	eval("opener.document." + '<%=data[4]%>').value = pjt[4];
	eval("opener.document." + '<%=data[5]%>').value = pjt[5];
	eval("opener.document." + '<%=data[6]%>').value = pjt[6];
	eval("opener.document." + '<%=data[7]%>').value = pjt[7];
	eval("opener.document." + '<%=data[8]%>').value = pjt[8];
	eval("opener.document." + '<%=data[9]%>').value = pjt[9];
	close();
}

-->
</script>