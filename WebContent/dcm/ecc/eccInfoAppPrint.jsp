<%@ include file = "../../admin/configHead.jsp"%>
<%@ page		
	info		 = "설계변경 내용보기"		
	contentType  = "text/html; charset=euc-kr" 		
	errorPage	 = "../../admin/errorpage.jsp" 
	import		 = "java.io.*"
	import		 = "java.util.*"
	import		 = "com.anbtech.text.*"
	import		 = "com.anbtech.dcm.entity.*"
	import		 = "com.anbtech.date.anbDate"
%>

<%
	//----------------------------------------------------
	//	초기화값
	//----------------------------------------------------
	com.anbtech.date.anbDate anbdt = new anbDate();
	//ECR정보 읽기
	String fname="",sname="",ftype="",fsize = "";						
	String chg_position="",trouble="",condition="",solution="";

	//ECO정보 읽기
	String eco_fname="",eco_sname="",eco_ftype="",eco_fsize = "";						
	String eco_chg_position="",eco_trouble="",eco_condition="",eco_solution="";

	String gid = (String)request.getAttribute("gid");
	//----------------------------------------------------
	//	ECC COM 읽기
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccComTable ecc;
	ecc = (eccComTable)request.getAttribute("COM_List");

	//----------------------------------------------------
	//	ECC MODEL 읽기
	//----------------------------------------------------
	ArrayList model_list = new ArrayList();
	model_list = (ArrayList)request.getAttribute("MODEL_List");
	com.anbtech.dcm.entity.eccModelTable model = new com.anbtech.dcm.entity.eccModelTable();
	Iterator model_iter = model_list.iterator();

	//----------------------------------------------------
	//	ECC REQ 읽기
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccReqTable ecr;
	ecr = (eccReqTable)request.getAttribute("REQ_List");

	//----------------------------------------------------
	//	ECC ORD 읽기
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccOrdTable eco;
	eco = (eccOrdTable)request.getAttribute("ORD_List");

	//----------------------------------------------------
	//	첨부파일 읽기
	//----------------------------------------------------
	//ECR의 내용을 읽을때
	fname = ecr.getFname();							//첨부파일명
	sname = ecr.getSname();							//첨부파일 저장명
	ftype = ecr.getFtype();							//첨부파일Type
	fsize = ecr.getFsize();							//첨부파일Size
	chg_position = ecr.getChgPosition();
	trouble = ecr.getTrouble();
	condition = ecr.getCondition();
	solution = ecr.getSolution();
	String bon_path = "/dcm/"+ecc.getEcrId();		//첨부파일 저장 확장path
	 
	//ECR 첨부파일 개별로 읽기
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	String[][] addFile = new String[cnt][5];
	for(int i=0; i<cnt; i++) for(int j=0; j<5; j++) addFile[i][j]="";

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
			addFile[m][4] = addFile[m][3];
			addFile[m][3] = addFile[m][3].trim() + ".bin";			//저장파일명
			addFile[m][4] = addFile[m][4].trim();					//TEMP 저장파일명
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}

	//ECO의 내용을 읽을때
	eco_fname = eco.getFname();						//첨부파일명
	eco_sname = eco.getSname();						//첨부파일 저장명
	eco_ftype = eco.getFtype();						//첨부파일Type
	eco_fsize = eco.getFsize();						//첨부파일Size
	eco_chg_position = eco.getChgPosition();
	eco_trouble = eco.getTrouble();
	eco_condition = eco.getCondition();
	eco_solution = eco.getSolution();
	String eco_bon_path = "/dcm/"+ecc.getEcoId();	//첨부파일 저장 확장path

	//ECO 첨부파일 개별로 읽기
	int eco_cnt = 0;
	for(int i=0; i<eco_fname.length(); i++) if(eco_fname.charAt(i) == '|') eco_cnt++;

	String[][] eco_addFile = new String[eco_cnt][5];
	for(int i=0; i<eco_cnt; i++) for(int j=0; j<5; j++) eco_addFile[i][j]="";

	if(eco_fname.length() != 0) {
		StringTokenizer eco_f = new StringTokenizer(eco_fname,"|");		//파일명 담기
		int eco_m = 0;
		while(eco_f.hasMoreTokens()) {
			eco_addFile[eco_m][0] = eco_f.nextToken();
			eco_addFile[eco_m][0] = eco_addFile[eco_m][0].trim(); 
			if(eco_addFile[eco_m][0] == null) eco_addFile[eco_m][0] = "";
			eco_m++;
		}
		StringTokenizer eco_t = new StringTokenizer(eco_ftype,"|");		//파일type 담기
		eco_m = 0;
		while(eco_t.hasMoreTokens()) {
			eco_addFile[eco_m][1] = eco_t.nextToken();
			eco_addFile[eco_m][1] = eco_addFile[eco_m][1].trim();
			if(eco_addFile[eco_m][1] == null) eco_addFile[eco_m][1] = "";
			eco_m++;
		}
		StringTokenizer eco_s = new StringTokenizer(eco_fsize,"|");		//파일크기 담기
		eco_m = 0;
		while(eco_s.hasMoreTokens()) {
			eco_addFile[eco_m][2] = eco_s.nextToken();
			eco_addFile[eco_m][2] = eco_addFile[eco_m][2].trim();
			if(eco_addFile[eco_m][2] == null) eco_addFile[eco_m][2] = "";
			eco_m++;
		}
		StringTokenizer eco_o = new StringTokenizer(eco_sname,"|");		//저장파일 담기
		eco_m = 0;
		int eco_no = 1;
		while(eco_o.hasMoreTokens()) {
			eco_addFile[eco_m][3] = eco_o.nextToken();	
			eco_addFile[eco_m][4] = eco_addFile[eco_m][3];
			eco_addFile[eco_m][3] = eco_addFile[eco_m][3].trim() + ".bin";			//저장파일명
			eco_addFile[eco_m][4] = eco_addFile[eco_m][4].trim();					//TEMP 저장파일명
			if(eco_addFile[eco_m][3] == null) eco_addFile[eco_m][3] = "";
			eco_m++;
			eco_no++;
		}
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//BOM변경내역서
function viewBom()
{
	var gid = document.eForm.gid.value;
	var eco_no = document.eForm.eco_no.value;
	var para = "gid="+gid+"&eco_no="+eco_no;
	wopen("../servlet/CbomChangeServlet?mode=eco_changebomexcel&"+para,"proxy","870","600","scrollbars=yes,toolbar=no,status=no,resizable=no");
}
//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
-->
</script>
<BODY>
<!--내용-->
<TABLE width="620" cellSpacing=0 cellPadding=0>
	<TR>
		<TD align="center">
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<TBODY>
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>ECO NO</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEcoNo()%></TD>
			   <TD width="15%" height="25" class="bg_05" align='middle'>제 목</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEccSubject()%></TD>
			</TR>
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>변경사유</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEccReason()%></TD>
				<TD width="15%" height="25" class="bg_05" align='middle'>적용구분</TD>
			    <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEccFactor()%></TD>
			</TR>
			
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>적용범위</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEccScope()%></TD>
				<TD width="15%" height="25" class="bg_05" align='middle'>업무구분</TD>
			    <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEccKind()%></TD>
			</TR>
			
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>문제발생모델(FG)</TD>
			   <TD width="35%" height="25" class="bg_06">
					&nbsp;<%=ecc.getFgCode()%></TD>
			   <TD width="15%" height="25" class="bg_05" align='middle'>문제발생부품<br></TD>
			   <TD width="35%" height="25" class="bg_06">
					&nbsp;<%=ecc.getPartCode()%></TD>
			</TR>
			
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>적용모델(FG)</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;
					<% while(model_iter.hasNext()){
						model = (eccModelTable)model_iter.next();	
						out.println(model.getFgCode()+" ["+model.getModelCode()+": "+model.getModelName()+"]");
					} %></TD>
			   <TD width="15%" height="25" class="bg_05" align='middle'>적용일</TD>
			   <TD width="35%" height="25" class="bg_06" >&nbsp;<%=anbdt.getSepDate(ecc.getOrderDate(),"-")%></TD>
			</TR> 
			<!-- ECO 작성 -->
			
			<TR><TD height="25" colspan="4"><img src='../dcm/images/title_pre_check.gif' align='absmiddle' alt='기술검토전 정보'></TD></TR>
			
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle' >문제분류</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=trouble%></TD>
			   <TD width="15%" height="25" class="bg_05" align='middle' >문제발생부분</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=chg_position%></TD>
			</TR>
			
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle' >현상및원인</TD>
			   <TD width="35%" height="25" class="bg_06">
					&nbsp;<%=condition%></TD>
			   <TD width="15%" height="25" class="bg_05" align='middle' >ECO적용내용</TD>
			   <TD width="35%" height="25" class="bg_06">
					&nbsp;<%=solution%></TD>
			</TR>
			
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle' >첨부파일</TD>
			   <TD width="35%" height="25" class="bg_06" colspan='3'>&nbsp;
				<% 
				for(int i=0; i<eco_cnt; i++) {
					out.print("<a href='../dcm/attach_download.jsp?fname="+eco_addFile[i][0]);
					out.print("&ftype="+eco_addFile[i][1]+"&fsize="+eco_addFile[i][2]);
					out.print("&sname="+eco_addFile[i][3]+"&extend="+eco_bon_path+"'>"+eco_addFile[i][0]+"</a>");
					out.println("<br>"); 
				}
				%>		   
			   </TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height="25" colspan="4"><img src='../dcm/images/title_after_check.gif' align='absmiddle' alt='기술검토후 정보'></TD></TR>
			
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle' >문제분류</TD>
			   <TD width="35%" height="25" class="bg_06" colspan='3'>&nbsp;<%=eco_trouble%></TD>
			</TR>
			
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle' >문제발생부분</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=eco_chg_position%></TD>
			   <TD width="15%" height="25" class="bg_05" align='middle' >현상및원인</TD>
			   <TD width="35%" height="25" class="bg_06">
					&nbsp;<%=eco_condition%></TD>
			</TR>
			
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle' >ECO적용내용</TD>
			   <TD width="35%" height="25" class="bg_06">
					&nbsp;<%=eco_solution%></TD>
			   <TD width="15%" height="25" class="bg_05" align='middle' >첨부파일</TD>
			   <TD width="35%" height="25" class="bg_06">
				&nbsp;<% 
				for(int i=0; i<eco_cnt; i++) {
					out.print("<a href='../dcm/attach_download.jsp?fname="+eco_addFile[i][0]);
					out.print("&ftype="+eco_addFile[i][1]+"&fsize="+eco_addFile[i][2]);
					out.print("&sname="+eco_addFile[i][3]+"&extend="+eco_bon_path+"'>"+eco_addFile[i][0]+"</a>");
					out.println("<br>"); 
				}
				%>		   
			   </TD>
			</TR>
			
			<TR><TD height='5' colspan='4'>&nbsp;</TD></TR>
			
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle' align='middle'>작성자</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEcrName()%></TD>
			   <TD width="15%" height="25" class="bg_05" align='middle' >부서명</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEcrDivName()%></TD>
			</TR>
			
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle' >전화번호</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEcrTel()%></TD>
			   <TD width="15%" height="25" class="bg_05" align='middle' >작성일</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=anbdt.getSepDate(ecc.getEcrDate(),"-")%></TD>
			</TR> 
			
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle' >검토책임자</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getMgrName()%></TD>
			   <TD width="15%" height="25" class="bg_05" align='middle' >검토담당자</TD>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEcoName()%></TD>
			</TR> 
						</TBODY></TABLE></TD>
	</TR>
</TABLE>

<FORM name="eForm" method="post" style="margin:0">
<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="gid" value='<%=gid%>'>
<INPUT type="hidden" name="eco_no" value='<%=ecc.getEcoNo()%>'>
</FORM>

</BODY>
</HTML>
