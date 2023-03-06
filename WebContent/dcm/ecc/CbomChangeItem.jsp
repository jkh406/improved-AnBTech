<%@ include file= "../../admin/configPopUp.jsp"%>
<%@ page		
	info		= "BOM변경내역서 내용보기"		
	contentType = "text/html; charset=euc-kr" 
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.bm.entity.*"
	import		= "com.anbtech.date.anbDate"
%>

<%
//contentType = "application/vnd.ms-excel; charset=KSC5601" 
//contentType = "text/html; charset=KSC5601" 
	//선언
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("0.0");		//출력형태
	String eco_no = (String)request.getAttribute("eco_no");

	//-----------------------------------
	//	BOM변경내역
	//-----------------------------------
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_List");
	eccBomTable item = new eccBomTable();
	Iterator item_iter = item_list.iterator();

	//-----------------------------------
	//	F01:변경이유
	//-----------------------------------
	ArrayList ecr_list = new ArrayList();
	ecr_list = (ArrayList)request.getAttribute("ECR_List");
	mbomEnvTable ecr = new mbomEnvTable();
	Iterator ecr_iter = ecr_list.iterator();
	int ecr_cnt = ecr_list.size();
	String[] reason = null;
	if(ecr_cnt != 0) {
		reason = new String[ecr_cnt];
		int n=0;
		while(ecr_iter.hasNext()) {
			ecr = (mbomEnvTable)ecr_iter.next();
			reason[n] = ecr.getSpec(); 
			n++;
		}
	}

	//-----------------------------------
	//	모품목코드 변경내역
	//-----------------------------------
	ArrayList pcd_list = new ArrayList();
	pcd_list = (ArrayList)request.getAttribute("PCD_List");
	eccBomTable pcd = new eccBomTable();
	Iterator pcd_iter = pcd_list.iterator();

	int pcd_cnt = pcd_list.size(); 
	if(pcd_cnt < 10) pcd_cnt = 10;
	else { if((pcd_cnt%2) == 1) pcd_cnt++; }	//짝수로 만듬

	String[][] pdata = new String[pcd_cnt][2];
	for(int i=0; i<pcd_cnt; i++) for(int j=0; j<2; j++) pdata[i][j]= "&nbsp;";

	int m=0;
	while(pcd_iter.hasNext()) {
			pcd = (eccBomTable)pcd_iter.next();
			pdata[m][0] = pcd.getParentCode()+"&nbsp;"; 
			pdata[m][1] = pcd.getPartSpec()+"&nbsp;"; 
			m++;
	}
%>

<HTML>
<HEAD><title>BOM 변경내역서</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--

-->
</script>
<BODY bgColor=#ffffff  topmargin="5" leftmargin="5" marginheight="0" marginwidth="0" oncontextmenu="return false">
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR height=100%>
				<TD vAlign='middle'>
					<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR vAlign='middle' height=50>
							<TD width='30%' align="left" valign="bottom"><img src="../dcm/images/logo.jpg" border="0"></TD>
							<TD width='40%' align="middle" class="title2">BOM 변경내역서</TD>
							<TD width='30%' align="right" valign="bottom">&nbsp;</TD></TR>
						<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
						<TR><TD height='5' colspan='3'></TD></TR>
						<TR vAlign='middle' height=27>
							<TD width='30%' style='padding-left:10px;'><img src="../dcm/images/eco_no.gif" border="0"  align='absmiddle'>&nbsp;:&nbsp;<%=eco_no%></TD>
							<TD width='40%'>&nbsp;</TD>
							<TD width='30%' align='right'><a href='Javascript:self.close();'><img src="../dms/images/bt_close.gif" border="0"></a></TD>
							</TR>
				</TBODY></TABLE></TD></TR>

		<!-- 문서 내용 시작 -->
		<TR height=100%>
			<TD vAlign=top>
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
				<TBODY>
				   <TR vAlign=middle height=25>
					  <TD noWrap width='20' align=middle class='list_title' rowspan=2>no</TD>
					  <TD noWrap width='30' align=middle class='list_title' rowspan=2>A</TD>
					  <TD noWrap width='80' align=middle class='list_title' rowspan=2>PART NUMBER</TD>
					  <TD noWrap width='200' align=middle class='list_title' rowspan=2>DESCRIPTION</TD>
					  <TD noWrap width='30' align=middle class='list_title' rowspan=2>단위</TD>
					  <TD noWrap width='60' align=middle class='list_title' rowspan=2>Loc NO</TD>
					  <TD noWrap width='100%' align=middle class='list_title' colspan=10>QTY/ASSY</TD>
					  <TD noWrap width='40' align=middle class='list_title' rowspan=2>공정<br>코드</TD>
					  <TD noWrap width='40' align=middle class='list_title' rowspan=2>변경<br>이유</TD>
					  <TD noWrap width='40' align=middle class='list_title' rowspan=2>비고</TD>
				  </TR>
				  <TR vAlign=middle height=25>
					  <TD noWrap width='10%' align=middle class='list_title'>1</TD>
					  <TD noWrap width='10%' align=middle class='list_title'>2</TD>
					  <TD noWrap width='10%' align=middle class='list_title'>3</TD>
					  <TD noWrap width='10%' align=middle class='list_title'>4</TD>
					  <TD noWrap width='10%' align=middle class='list_title'>5</TD>
					  <TD noWrap width='10%' align=middle class='list_title'>6</TD>
					  <TD noWrap width='10%' align=middle class='list_title'>7</TD>
					  <TD noWrap width='10%' align=middle class='list_title'>8</TD>
					  <TD noWrap width='10%' align=middle class='list_title'>9</TD>
					  <TD noWrap width='10%' align=middle class='list_title'>10</TD>
			      </TR>
				
			<%  int cnt = 1;
				int n=1,qty=1;
				while(item_iter.hasNext()) {
					item = (eccBomTable)item_iter.next();
					if((cnt%2) == 1) { 
						out.print("<TR onmouseover=\"this.style.backgroundColor='#F5F5F5'\" onmouseout=\"this.style.backgroundColor=''\" bgColor=#ffffff>");
						out.print("<TD align=middle height='24' class='list_bg' rowspan=2>"+n+"</TD>");
						out.print("<TD align=middle height='24'>&nbsp;"+item.getAdTag()+"</TD>");
						out.print("<TD align=middle height='24'>&nbsp;"+item.getChildCode()+"</TD>");
						out.print("<TD align=left height='24'>&nbsp;"+item.getPartSpec()+"</TD>");
						out.print("<TD align=middle height='24'>&nbsp;"+item.getQtyUnit()+"</TD>");
						out.print("<TD align=middle height='24'>&nbsp;"+item.getLocation()+"</TD>");
						
						//QTY/ASSY 갯수 출력하기
						for(int i=1; i<11; i++) {
							if(qty == i) {
								String part_cnt = item.getQty();
								if(part_cnt.length() != 0) part_cnt = "1";
								out.print("<TD align=middle height='24'>&nbsp;"+part_cnt+"</TD>");	
							} else	out.print("<TD align=middle height='24'>&nbsp;</TD>");
						}

						out.print("<TD align=middle height='24'>&nbsp;"+item.getOpCode()+"</TD>");

						//변경이유 번호로 출력하기
						String ecc_rsn = item.getEccReason(); 
						int ecr_no=1,dip=0;				//변경이유번호, 변경번호가 없으면.
						for(int i=0; i<ecr_cnt; i++) {
							if(ecc_rsn.equals(reason[i])) {
								out.print("<TD align=middle height='24'>&nbsp;"+ecr_no+"</TD>");
								dip++;
							}
							ecr_no++;
						}
						if(dip == 0) out.print("<TD align=middle height='24'>&nbsp;</TD>");

						out.print("<TD align=middle height='24'>&nbsp;</TD>");
						out.println("</TR>");
						n++;
					} else { 
						out.print("<TR onmouseover=\"this.style.backgroundColor='#F5F5F5'\" onmouseout=\"this.style.backgroundColor=''\" bgColor=#ffffff>");
						out.print("<TD align=middle height='24'>&nbsp;"+item.getAdTag()+"</TD>");
						out.print("<TD align=middle height='24'>&nbsp;"+item.getChildCode()+"</TD>");
						out.print("<TD align=left height='24'>&nbsp;"+item.getPartSpec()+"</TD>");
						out.print("<TD align=middle height='24'>&nbsp;"+item.getQtyUnit()+"</TD>");
						out.print("<TD align=middle height='24'>&nbsp;"+item.getLocation()+"</TD>");

						//QTY/ASSY 갯수 출력하기
						for(int i=1; i<11; i++) {
							if(qty == i) {
									out.print("<TD align=middle height='24'>&nbsp;1</TD>");		//item.getQty()
							} else	out.print("<TD align=middle height='24'>&nbsp;</TD>");
						}

						out.print("<TD align=middle height='24'>&nbsp;"+item.getOpCode()+"</TD>");
						String ecc_rsn = item.getEccReason(); 
						int ecr_no = 1;
						for(int i=0; i<ecr_cnt; i++) {
							if(ecc_rsn.equals(reason[i])) 
								out.print("<TD align=middle height='24'>&nbsp;"+ecr_no+"</TD>");
							ecr_no++;
						}
						out.print("<TD align=middle height='24'>&nbsp;</TD>");
						out.println("</TR>");
						qty++;
					} 
					cnt++;
				}  //while 

				//복수개 10개를 출력했는지 판단하여 10개이하일경우는 공란으로 출력하기
				if(n < 11) {
					boolean run = true;
					while(run) {
						if((cnt%2)==1){ 
							out.print("<TR onmouseover=\"this.style.backgroundColor='#F5F5F5'\" onmouseout=\"this.style.backgroundColor=''\" bgColor=#ffffff>");
							out.print("<TD align=middle height='24' class='list_bg' rowspan=2>"+n+"</TD>");
							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							out.print("<TD align=left height='24'>&nbsp;</TD>");
							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							
							//QTY/ASSY 갯수 출력하기
							for(int j=1; j<11; j++) {
								out.print("<TD align=middle height='24'>&nbsp;</TD>");
							}
							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							out.println("</TR>");
							n++;
						} else { 
							out.print("<TR onmouseover=\"this.style.backgroundColor='#F5F5F5'\" onmouseout=\"this.style.backgroundColor=''\" bgColor=#ffffff>");
							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							out.print("<TD align=left height='24'>&nbsp;</TD>");
							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							out.print("<TD align=middle height='24'>&nbsp;</TD>");

							//QTY/ASSY 갯수 출력하기
							for(int j=1; j<11; j++) {
								out.print("<TD align=middle height='24'>&nbsp;</TD>");
							}

							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							out.print("<TD align=middle height='24'>&nbsp;</TD>");
							out.println("</TR>");
							if(n == 11) run = false;
						} 
						cnt++;
					} //for
				} //if
	%>
				</TBODY></TABLE></TD></TR>

		<TR height=100%>
			<TD vAlign=top>
				<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
					<TBODY>
						<TR bgcolor="#FFFFFF">
							<TD align='left' valign='top' width='330' height="24" rowspan=6 colspan=4>
			<%	
				out.println("&nbsp;&nbsp;* 비 고 (변경이유 CODE)<br>");
				for(int i=0,r=1; i<ecr_cnt; i++,r++) {
					out.print("&nbsp;&nbsp;&nbsp;&nbsp;" + r + " : " + reason[i]);
					if(r%2 == 0) out.println("<br>");
				}
			%>
							</TD>
							<TD align='middle' valign='middle' width=30 height="24">NO</TD> 
							<TD align='middle' valign='middle' width=80 height="24">PARENT ASSY</TD> 
							<TD align='middle' valign='middle' width=200 height="24" colspan=6>DESCRIPTION</TD> 
							<TD align='middle' valign='middle' width=30 height="24">NO</TD> 
							<TD align='middle' valign='middle' width=80 height="24" colspan=4>PARENT ASSY</TD> 
							<TD align='middle' valign='middle' width=200 height="24" colspan=2>DESCRIPTION</TD> 
						</TR>
			<%
				int b = pcd_cnt / 2; 
				int no=1,sno=b+1;
				for(int a=0,c=b; a<b; a++,c++) {
					out.println("<TR bgcolor='#FFFFFF'>");
					out.println("<TD align='middle' valign='middle' width=30 height='24'>"+no+"</TD>");
					out.println("<TD align='middle' valign='middle' width=80 height='24'>"+pdata[a][0]+"</TD>");
					out.println("<TD align='middle' valign='middle' width=200 height='24' colspan=6>"+pdata[a][1]+"</TD>");

					out.println("<TD align='middle' valign='middle' width=30 height='24'>"+sno+"</TD>");
					out.println("<TD align='middle' valign='middle' width=80 height='24' colspan=4>"+pdata[c][0]+"</TD>");
					out.println("<TD align='middle' valign='middle' width=200 height='24' colspan=2>"+pdata[c][1]+"</TD>");
					out.println("</TR>");
					no++;
					sno++;
				}
			%>
						</TBODY></TABLE></TD></TR>
     </TABLE>
</BODY></HTML>

