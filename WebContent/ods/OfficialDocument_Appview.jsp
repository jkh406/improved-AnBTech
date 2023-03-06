<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "공지공문 보기(결재완료된 것)"		
	contentType = "text/html; charset=KSC5601" 
	errorPage = "../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.dms.entity.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.file.textFileReader"
%>
<%
	//-----------------------------------
	//초기화 선언
	//-----------------------------------
	com.anbtech.date.anbDate anbdt = new anbDate();							//날자
	textFileReader text = new com.anbtech.file.textFileReader();			//본문파일 읽기
	
	//결재선 관련
	String pid = "";				//관리번호
	String line="";					//읽은문서 결재선
	String t_line="";				//읽은문서 결재선 (textarea로 표시)
	String wdate = "";				//기안자 기안 일자
	String vdate = "";				//검토자 검토 일자
	String ddate = "";				//승인자 승인 일자
	String wid = "";				//기안자사번
	String vid = "";				//검토자사번
	String did = "";				//승인자사번
	String wname = "";				//기안자
	String vname = "";				//검토자
	String dname = "";				//승인자

	String aids = "";				//협조자 사번들
	String anames = "";				//협조자 이름들
	String aranks = "";				//협조자 직급들
	String adivs = "";				//협조자 부서들
	String adates = "";				//협조자 날자들
	String acomms = "";				//협조자 의견들
	int line_cnt = 0;				//결재선에 출력될 라인 갯수

	//-----------------------------------
	// 변수선언
	//-----------------------------------
	String user_name="";			//기안자 이름
	String doc_id="";				//문서번호
	String slogan="";				//슬로건
	String title_name="";			//부서 Title명
	String in_date="";				//기안일자		
	String receive="";				//수신
	String reference="";			//참조
	String sending="";				//발신
	String subject="";				//제목	
	String bon_path="";				//본문저장 확장path
	String bon_file="";				//본문저장 파일명
	String read_con="";				//본문내용
	String firm_name="";			//발신부서명
	String representative="";		//발신부서 대표명	
	String fname="";				//공통:파일원래명	
	String sname="";				//공통:파일저장명		
	String ftype="";				//공통:파일확장자명	
	String fsize="";				//공통:파일크기
	String[][] addFile;				//첨부관련내용 담기
	String module_name="";			//보낼모듈종류
	String mail="";					//전자우편종류
	String mail_add="";				//전자우편주소(사번/이름;)

	/**********************************************************/
	//	결재선정보 내용 읽기
	/**********************************************************/
	com.anbtech.dms.entity.OfficialDocumentAppTable app;					//helper 
	ArrayList app_list = new ArrayList();
	app_list = (ArrayList)request.getAttribute("App_One");
	
	app = new OfficialDocumentAppTable();
	Iterator app_iter = app_list.iterator();
	if(app_iter.hasNext()){
		app = (OfficialDocumentAppTable)app_iter.next();
		pid = app.getId();								//결재선 저장관리번호[전자결재 관리번호와 동일]

		wdate = app.getGianDate();		wdate=wdate.trim();		//기안자 기안 일자
		vdate = app.getReviewDate();	vdate=vdate.trim();		//검토자 검토 일자
		ddate = app.getDecisionDate();	ddate=ddate.trim();		//승인자 승인 일자
		wid = app.getGianId();			wid=wid.trim();			//기안자사번
		vid = app.getReviewId();		vid=vid.trim();			//검토자사번
		did = app.getDecisionId();		did=did.trim();			//승인자사번
		wname = app.getGianName();		wname=wname.trim();		//기안자
		vname = app.getReviewName();	vname=vname.trim();		//검토자
		dname = app.getDecisionName();	dname=dname.trim();		//승인자
		aids = app.getAgreeIds();		aids=aids.trim();		//협조자 사번들
		anames = app.getAgreeNames();	anames=anames.trim();	//협조자 이름들
		aranks = app.getAgreeRanks();	aranks=aranks.trim();	//협조자 직급들
		adivs = app.getAgreeDivs();		adivs=adivs.trim();		//협조자 부서들
		adates = app.getAgreeDates();	adates=adates.trim();	//협조자 날자들
		acomms = app.getAgreeComments();acomms=acomms.trim();	//협조자 의견들

		line = "기안 "+wname+"("+app.getGianRank()+")"+app.getGianDiv()+" "+wdate+"<br>";
		t_line = "기안 "+wname+"("+app.getGianRank()+")"+app.getGianDiv()+" "+wdate+"\r";
		line_cnt++;
		if(vdate.length() > 10) {
			line += "검토 "+vname+"("+app.getReviewRank()+")"+app.getReviewDiv()+" "+vdate+" "+app.getReviewComment()+"<br>";
			t_line += "검토 "+vname+"("+app.getReviewRank()+")"+app.getReviewDiv()+" "+vdate+" "+app.getReviewComment()+"\r";
			line_cnt++;
		}
		if(adates.length() > 10) {
			int cnt = 0;
			for(int i=0; i<adates.length(); i++) if(adates.charAt(i) == ';') cnt++;
			String[][] agree = new String[cnt][6];
			//배열에 담기
			String[] agr_list = {aids,anames,aranks,adivs,adates,acomms};
			for(int i=0; i<agr_list.length; i++) {
				StringTokenizer agl = new StringTokenizer(agr_list[i],";");
				int n = 0;
				while(agl.hasMoreTokens()) {
					agree[n][i] = agl.nextToken();
					n++;
				}
			}
			//line만들기
			for(int i=0; i<cnt; i++) {
				line += "협조 "+agree[i][1]+"("+agree[i][2]+")"+agree[i][3]+" "+agree[i][4]+" "+agree[i][5]+"<br>";
				t_line += "협조 "+agree[i][1]+"("+agree[i][2]+")"+agree[i][3]+" "+agree[i][4]+" "+agree[i][5]+"\r";
				line_cnt++;
			}

		}
		line += "승인 "+dname+"("+app.getDecisionRank()+")"+app.getDecisionDiv()+" "+ddate+" "+app.getDecisionComment()+"<br>";
		t_line += "승인 "+dname+"("+app.getDecisionRank()+")"+app.getDecisionDiv()+" "+ddate+" "+app.getDecisionComment()+"\r";
		line_cnt++;
	}
	
	/**********************************************************/
	//	공지공문 내용 읽기
	/**********************************************************/
	com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_One");
	table = new OfficialDocumentTable();
	Iterator table_iter = table_list.iterator();
	
	if(table_iter.hasNext()){
		table = (OfficialDocumentTable)table_iter.next();

		user_name=table.getUserName();				//기안자 이름
		doc_id=table.getDocId(); 
			if(doc_id == null) doc_id = "결재승인후 자동채번";			//문서번호
		slogan=table.getSlogan();					//슬로건
		title_name=table.getTitleName();			//부서 Title명
		in_date=table.getInDate();					//기안일자
			in_date = in_date.substring(0,10);
		receive=table.getReceive();;				//수신
		reference=table.getReference();				//참조
			if(reference == null) reference = "";
		sending=table.getSending();					//발신
		subject=table.getSubject();					//제목	
		bon_path=table.getBonPath();				//본문저장 확장path
		bon_file=table.getBonFile();				//본문저장 파일명
		firm_name=table.getFirmName();				//발신부서명
		representative=table.getRepresentative();	//발신부서 대표명	
		fname=table.getFname();						//공통:파일원래명	
		sname=table.getSname();						//공통:파일저장명		
		ftype=table.getFtype();						//공통:파일확장자명	
		fsize=table.getFsize();						//공통:파일크기
		module_name=table.getModuleName();			//보낼모듈종류
		mail=table.getMail();						//전자우편종류
		mail_add=table.getMailAdd();				//전자우편주소(사번/이름;)
	}

	//본문파일읽기
	String full_path = upload_path + bon_path + "/bonmun/" + bon_file;
	read_con = text.getFileString(full_path);

	//첨부파일읽어 배열에 담기
	if(fname == null) fname = "";
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	addFile = new String[cnt][4];
	for(int i=0; i<cnt; i++) for(int j=0; j<4; j++) addFile[i][j]="";

	if(fname.length() != 0) {
		StringTokenizer f = new StringTokenizer(fname,"|");		//파일명 담기
		int m = 0;
		while(f.hasMoreTokens()) {
			addFile[m][0] = f.nextToken();
			addFile[m][0] = addFile[m][0].trim(); 
			if(addFile[m][0] == null) addFile[m][0] = "";
			m++;
		}
		StringTokenizer t = new StringTokenizer(ftype,"|");		//파일type 담기
		m = 0;
		while(t.hasMoreTokens()) {
			addFile[m][1] = t.nextToken();
			addFile[m][1] = addFile[m][1].trim();
			if(addFile[m][1] == null) addFile[m][1] = "";
			m++;
		}
		StringTokenizer s = new StringTokenizer(fsize,"|");		//파일크기 담기
		m = 0;
		while(s.hasMoreTokens()) {
			addFile[m][2] = s.nextToken();
			addFile[m][2] = addFile[m][2].trim();
			if(addFile[m][2] == null) addFile[m][2] = "";
			m++;
		}
		StringTokenizer o = new StringTokenizer(sname,"|");		//저장파일 담기
		m = 0;
		int no = 1;
		while(o.hasMoreTokens()) {
			addFile[m][3] = o.nextToken();
			addFile[m][3] = addFile[m][3].trim() + ".bin";			
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}

%>

<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title>공지공문</title>
<LINK href="../ods/css/style.css" rel=stylesheet>
</head>

<BODY leftmargin='0' topmargin='10' marginwidth='0' marginheight='0'>
<center>

<% //slogan, title_name 출력 
	int div_h = 77;
	out.println("<font size=2>&nbsp;"+slogan+"&nbsp;</font><br>");
	if(title_name.length() > 0) {
		out.println("<font size=6>&nbsp;"+title_name+"&nbsp;</font><br>");
	} else {
		out.println("<font size=6>&nbsp;&nbsp;</font><br>");
		div_h = 72;
	}

%>

<table width='640' border="0" cellspacing="0" cellpadding="0">
	<tr>
	<td width=50% align='left' height=20><img src='../ods/images/logo.jpg' align='middle' border='0' align='absmiddle'></td>
	<td width=50% align='right' height=20>
		<div id="print" style="position:relative;visibility:visible;">
			<a href='Javascript:winprint();'><img src='../ods/images/bt_print.gif' align='absmiddle' border='0'></a> <!-- 출력 -->
			<a href='Javascript:self.close();'><img src='../ods/images/bt_close.gif' align='absmiddle' border='0'></a> <!-- 닫기 -->
		</div>
	</td></tr>
    <TR><TD height='2' bgcolor='#9CA9BA' colspan="2"></TD></TR>
    <TR><TD height='5' colspan="2"></TD></TR>
</table>

<TABLE cellSpacing=0 cellPadding=0 width="640" border=1 bordercolordark="white" bordercolorlight="#9CA9BA"> 
	<tr>   
		<td width="20" height="96" rowspan=3 align="center" class="bg_05">메<p>모</td>
		<td width="420" height="96" rowspan=3 align="left" valign="top">
		<% if(line_cnt < 7) {    
			out.println(line);
		 } else {				 
			out.println("<TEXTAREA rows=7 cols=66 readOnly style='text-align:left;font-size:9pt;border:1px solid #787878;'>"+t_line+"</TEXTAREA>");
		 }	%> </td>
		<td width="20" height="96" rowspan=3 align="center" class="bg_05">결<p>재</td>                     
		<td width="60" align="center" height="22" class="bg_07">기 안</td>            
		<td width="60" align="center" height="22" class="bg_07">검 토</td>            
		<td width="60" align="center" height="22" class="bg_07">승 인</td>            
	</tr>     
	<tr height="26">         
		<td width="60" height="50" align="center"><img src="../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></td>   
		<td width="60" height="50" align="center">
		<% //검토자 승인자 싸인 표시하기 (단, 반려문서가 아닌경우만)
			if(vdate.length() == 0)	{//검토자
				if(ddate.length() == 0) out.println("&nbsp;");
				else out.println("전결");
			} else {
				out.println("<img src='../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
			}
		%></td>   
		<td width="60" height="50" align="center">
		<%
			if(ddate.length() == 0)	{//승인자
				out.println("&nbsp;");
			} else {
				out.println("<img src='../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
			}
		%>	
		</td>   
	</tr>   
	<tr height="26">         
		<td width="60" height="24" align="center" class="bg_07">&nbsp;<%=wname%>&nbsp;</td>   
		<td width="60" height="24" align="center" class="bg_07">&nbsp;<%=vname%>&nbsp;</td>   
		<td width="60" height="24" align="center" class="bg_07">&nbsp;<%=dname%>&nbsp;</td>   
	</tr>   
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0'>
	<tr><td width=100% height="2"></td></tr>
	<TR><TD width=100% height='2' bgcolor='#9CA9BA'></TD></TR>
	<tr><td width=100% height="5"></td></tr>
</table>


<table width='640' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111"> 
	<tr>
		<td width="90" height="20" align="center" valign="middle">문서번호 :</td>
		<td width="230" height="20" align="left" valign="middle"><%=doc_id%></td>
		<td width="90" height="20" align="center" valign="middle">
			일&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;자 :</td>
		<td width="230" height="20" align="left" valign="middle"><%=in_date%></td>
	</tr>
	<tr>
		<td width="90" height="20" align="center" valign="middle">
			수&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;신 :</td>
		<td width="230" height="20" align="left" valign="middle"><%=receive%></td>
		<td width="90" height="20" align="center" valign="middle">기 안 자 : </td>
		<td width="230" height="20" align="left" valign="middle"><%=user_name%></td>
	</tr>
	<tr>
		<td width="90" height="20" align="center" valign="middle">
			참&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;조 :</td>
		<td width="550" height="20" align="left" valign="middle" colspan=3><%=reference%></td>
	</tr>
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
	<tr>
		<td width="100%" height="10" align="left" valign="middle" colspan=2></td>
	</tr>
	<tr>
		<td width="90" height="30" align="center" valign="middle">
			<font size=3><b>제&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;목 :</b></font></td>
		<td width="550" height="30" align="left" valign="middle">
			<font size=3><b><%=subject%></b></font></td>
	</tr>
	<tr><td width=100% height="2" bgcolor='#9CA9BA' colspan=2></td></tr>
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
	<tr>
		<td width="640" height="10" align="left" valign="top"></td>
	</tr>
	<tr>
		<td width="640" align="left" valign="top">
		<textarea rows=30 cols=88 style="border:1px solid #787878;" readonly><%=read_con%></textarea></td>
	</tr>
	<tr>
		<td width="100%" height="10" align="left" valign="middle" colspan=2></td>
	</tr>
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
	<tr>
		<td width="90" height="60" align="center" valign="middle">첨부파일 : </td>
		<td width="550" height="60" align="left" valign="middle">
		<%
		for(int i=0; i<cnt; i++) {
			out.println("&nbsp;<a href='../ods/attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a><br>");
		} 
		%>
		</td>
	</tr>
</table>

<font size=6><b><%=firm_name%></b></font><br>
<font size=6><b><%=representative%></b></font>

<table width='640' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
	<tr><td width=100% height="30" align="center" valign="middle" colspan=2 ></td></tr>
</table>
</center>

<div id="Mark" style="position:absolute;left:500px;top:830px;width:300px;height:100px;visibility:visible;">
	<img src='../ods/images/stamp1.gif' align='absmiddle' border='0'>
</div>

</body>
</html>

<script language='javascript'>
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
</script>