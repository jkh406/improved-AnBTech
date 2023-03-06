<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���� ���ߺ�� �󼼺���� LIST"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%
	/*********************************************************************
	 	�ʱ�ȭ ����
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//����
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//���ڿ� ó��
	com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("0,000");		//�������
	String pre_cost = "",rst_cost="";	//������,�������
	String cost_name = "";				//����̸�
	
	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String pjtWord="1",pjt_code="",pjt_name="",sItem="",sWord="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();
	
	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();	
		pjt_code = para.getPjtCode();	
		pjt_name = para.getPjtName();	
		pjtWord = para.getPjtword();
		sItem = para.getSitem();		
		sWord = para.getSword();		
	} 

	//�ش����� ��ü ���� LIST
	com.anbtech.pjt.entity.projectTable pjt;
	ArrayList pjt_list = new ArrayList();
	pjt_list = (ArrayList)request.getAttribute("PJT_List");
	pjt = new projectTable();
	Iterator pjt_iter = pjt_list.iterator();
	int pjt_cnt = pjt_list.size();

	String[][] project = new String[pjt_cnt][2];
	int p = 0;
	while(pjt_iter.hasNext()) {
		pjt = (projectTable)pjt_iter.next();
		project[p][0] = pjt.getPjtCode();
		project[p][1] = pjt.getPjtName();
		p++;
	}
	
	/*********************************************************************
	 	�ش���� ��� ����� �������� �б�
	*********************************************************************/
	com.anbtech.pjt.entity.projectTable scost;									
	ArrayList scost_list = new ArrayList();
	scost_list = (ArrayList)request.getAttribute("SCOST_List");
	scost = new projectTable();
	Iterator scost_iter = scost_list.iterator();

	//parameter �ޱ�
	String pjt_mbr_id="",mgt_plan="",pjt_status="",cost_exp="",cost_rst="",dif_cost="",mbr_exp="";
	String plan_start_date="",plan_end_date="";
	String chg_start_date="",chg_end_date="",rst_start_date="",rst_end_date="";
	String plan_labor="",plan_sample="",plan_metal="",plan_mup="",plan_oversea="",plan_plant="";
	String rst_labor="",rst_sample="",rst_metal="",rst_mup="",rst_oversea="",rst_plant="";
	if(scost_iter.hasNext()) {
		scost = (projectTable)scost_iter.next();

		pjt_code=scost.getPjtCode();
		pjt_name=scost.getPjtName();
		pjt_mbr_id=scost.getPjtMbrId();
		mgt_plan=scost.getMgtPlan();
		mbr_exp=Integer.toString(scost.getMbrExp());
		cost_exp=scost.getCostExp();		//���� �Ѻ��
		cost_rst=scost.getCostRst();		//���� �Ѻ��
		dif_cost = scost.getDifCost();		//���� - ����

		String psd = scost.getPlanStartDate();
		String ped = scost.getPlanEndDate();
		plan_start_date=psd.substring(0,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8);
		plan_end_date=ped.substring(0,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8);

		String csd = scost.getChgStartDate();	if(csd == null) csd = "";
		String ced = scost.getChgEndDate();		if(ced == null) ced = "";
		if(csd.length() > 4) chg_start_date=csd.substring(0,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8);
		if(ced.length() > 4) chg_end_date=ced.substring(0,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8);
		
		String rsd = scost.getRstStartDate();	if(rsd == null) rsd = "";
		String red = scost.getRstEndDate();		if(red == null) red = "";
		if(rsd.length() > 4) rst_start_date=rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8);
		if(red.length() > 4) rst_end_date=red.substring(0,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8);

		pjt_status = scost.getPjtStatus();	if(pjt_status == null) pjt_status = "S";
		plan_labor=scost.getPlanLabor();
		plan_sample=scost.getPlanSample();
		plan_metal=scost.getPlanMetal();
		plan_mup=scost.getPlanMup();
		plan_oversea=scost.getPlanOversea();
		plan_plant=scost.getPlanPlant();

		rst_labor=scost.getResultLabor();
		rst_sample=scost.getResultSample();
		rst_metal=scost.getResultMetal();
		rst_mup=scost.getResultMup();
		rst_oversea=scost.getResultOversea();
		rst_plant=scost.getResultPlant();
	}

	/*********************************************************************
	 	�ش���� ���� ��� �������� �б�
	*********************************************************************/
	com.anbtech.pjt.entity.projectTable cost;									
	ArrayList cost_list = new ArrayList();
	cost_list = (ArrayList)request.getAttribute("COST_List");
	cost = new projectTable();
	Iterator cost_iter = cost_list.iterator();
	int cost_cnt = cost_list.size();

	String[][] rcost = new String[cost_cnt][18];
	int r=0;
	while(cost_iter.hasNext()) {
		cost = (projectTable)cost_iter.next();
		rcost[r][0] = cost.getPid();
		rcost[r][1] = cost.getPjtCode();
		rcost[r][2] = cost.getPjtName();
		rcost[r][3] = cost.getNodeCode();	
		rcost[r][4] = cost.getNodeName();	
		rcost[r][5] = cost.getUserId();
		rcost[r][6] = cost.getUserName();
		rcost[r][7] = cost.getCostType(); 
		rcost[r][8] = cost.getNodeCost();							//�����ݾ�
		rcost[r][9] = cost.getExchange();
		rcost[r][10] = cost.getInDate();
		rcost[r][11] = cost.getRemark();
		rcost[r][12] = cost.getCostExp();							//����ݾ�
		rcost[r][13] = cost.getDifCost();							//���ױݾ�(����-����)
		rcost[r][14] = Double.toString(cost.getProgress());			//��������(����� ����)
		rcost[r][15] = cost.getView();
		rcost[r][16] = cost.getModify();
		rcost[r][17] = cost.getDelete();
		r++;
	}
	String tpage = request.getParameter("Tpage"); if(tpage == null) tpage = "1";
	String cpage = request.getParameter("Cpage"); if(cpage == null) cpage = "1";
	int Tpage = Integer.parseInt(tpage);
	int Cpage = Integer.parseInt(cpage);

	/*********************************************************************
	 	ȭ����¿� ��� �ݾ�
	*********************************************************************/
	if(sWord.equals("")) {				//��ü
		pre_cost = cost_exp;			//����
		rst_cost = cost_rst;			//����
	} else if(sWord.equals("1")) {		//�ΰǺ�
		pre_cost = plan_labor;
		rst_cost = rst_labor;
	} else if(sWord.equals("2")) {		//SAMPLE
		pre_cost = plan_sample;
		rst_cost = rst_sample;
	} else if(sWord.equals("3")) {		//������
		pre_cost = plan_metal;
		rst_cost = rst_metal;
	} else if(sWord.equals("4")) {		//���ں�
		pre_cost = plan_mup;
		rst_cost = rst_mup;
	} else if(sWord.equals("5")) {		//�԰ݽ��κ�
		pre_cost = plan_oversea;
		rst_cost = rst_oversea;
	} else if(sWord.equals("6")) {		//���ں�
		pre_cost = plan_plant;
		rst_cost = rst_plant;
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//���� ��� SUMMARY����
function goSummary()
{
	var sItem = 'pjt_code';
	var sWord = '';
	
	document.sForm.action='../servlet/projectStfCostServlet';
	document.sForm.mode.value='PCO_SML';
	document.sForm.sItem.value=sItem;
	document.sForm.sWord.value=sWord;
	document.sForm.submit();
}
//������ �̵��ϱ�
function goPage(a) 
{
	document.sForm.action='../servlet/projectStfCostServlet';
	document.sForm.mode.value='PCO_ACL';
	document.sForm.page.value=a;
	document.sForm.submit();
}
//�˻��ϱ�
function goSearch()
{
	document.sForm.action='../servlet/projectStfCostServlet';
	document.sForm.mode.value='PCO_ACL';
	document.sForm.page.value='1';
	document.sForm.submit();
}
//������� �ۼ��ϱ� �غ�
function costWrite()
{
	document.sForm.action='../servlet/projectStfCostServlet';
	document.sForm.mode.value='PCO_WV';
	document.sForm.submit();

}
//������� �����̷� ����[�����ۼ�/���ϼ���]
function costModify(pid)
{
	document.sForm.action='../servlet/projectStfCostServlet';
	document.sForm.mode.value='PCO_MV';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}
//������� �����̷� ����[�����ۼ�/���ϻ���]
function costDelete(pid)
{
	var d = confirm('�����Ͻðڽ��ϱ�?');
	if(!d) return;

	document.sForm.action='../servlet/projectStfCostServlet';
	document.sForm.mode.value='PCO_D';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}
//������� �����̷� �󼼺���
function costView(pid)
{
	sParam = "strSrc=../servlet/projectStfCostServlet&pid="+pid+"&mode=PCO_V";
	showModalDialog("../pjt/modalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:340px;resizable=0");
}
-->
</script>
<BODY topmargin="0" leftmargin="0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> �������� ���ߺ�� ������ �󼼺���</TD>
					<TD valign='middle'>
						<b><a href='Javascript:goSummary();'>SUMMARY����</b></a></TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0>
				<TBODY>
				<TR>
					<TD align=left width='5%'></TD>
					<TD align=left width='85%'>
						<form name="sForm" method="post" style="margin:0">
						<select name="sWord" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'>  
						<%
							String[] s_code = {"","1","2","3","4","5","6"};
							String[] s_name = {"��ü","�ΰǺ�","SAMPLE","������","���ڰ��","�԰ݽ��κ�","�ü����ں�"};
							String tsel = "";
							for(int ti=0; ti<s_code.length; ti++) {
								if(sWord.equals(s_code[ti])) {tsel = "selected"; cost_name=s_name[ti]; } 
								else tsel = "";
								out.println("<option "+tsel+" value='"+s_code[ti]+"'>"+s_name[ti]+"</option>");
							}
						%>
						</select>
						<input type="hidden" name="sItem" size="15" value="cost_type">
						<input type="hidden" name="mode" size="15" value="">
						<input type="hidden" name="pid" size="15" value="">
						<input type="hidden" name="page" size="15" value="">
						<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
						</form>
					</TD>
					<TD align=middle width='10%'>
						<a href='javascript:costWrite()'><b>�������Է�</b></a></TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!-- ����� ������� �� -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- ���� ������ -->
			   <td width="10%" height="25" colspan=4><b>�������� ������</b></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� �� ��</td>
			   <td width="40%" height="25" class="bg_04"><%=pjt_code%> <%=pjt_name%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">P M ��</td>
				<td width="40%" height="25" class="bg_04"><%=pjt_mbr_id%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��������</td>
			   <td width="40%" height="25" class="bg_04">
				<%
					if(pjt_status.equals("S")) out.println("������");
					else if(pjt_status.equals("0")) out.println("������");
					else if(pjt_status.equals("1")) out.println("������");
					else if(pjt_status.equals("2")) out.println("�Ϸ�");
					else if(pjt_status.equals("3")) out.println("DROP");
					else if(pjt_status.equals("4")) out.println("HOLDING");
				%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��ȹ�Ⱓ</td>
			   <td width="40%" height="25" class="bg_04">
					<%=plan_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=plan_end_date%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">����/����</td>
			   <td width="40%" height="25" class="bg_04">
					<input type=text value='<%=rst_cost%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=15> /
					<input type=text value='<%=pre_cost%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=15> [��]</td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
			   <td width="40%" height="25" class="bg_04">
					<%=chg_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=chg_end_date%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� ��</td>
			   <td width="40%" height="25" class="bg_04">
					<input type=text value='<%=dif_cost%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=33> [��]</td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
			   <td width="40%" height="25" class="bg_04">
					 <%=rst_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=rst_end_date%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>

<!-- ����Ʈ -->
<TABLE  cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> �ش�������� �����⳻��LIST</TD>
				<TD style="padding-right:10px" align='right' valign='middle'><%=Cpage%>/<%=Tpage%> <img src="../pjt/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--��ư �� ����¡-->
		<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
				<TD width=4>&nbsp;</TD>
				<TD width='' align='right' style="padding-right:10px">
					<%	if (Cpage <= 1) {	%>		
						<img src='../pjt/images/bt_previous.gif' border='0' align='absmiddle'>
					 <%	} else 	{	%>		
						<a href='javascript:goPage(<%=Cpage-1%>)'>
						<img src='../pjt/images/bt_previous.gif' border='0' align='absmiddle'></a>

					 <%	} if ((Cpage != Tpage) && (Tpage != -1 )) { %>		
							<a href='javascript:goPage(<%=Cpage+1%>)'>
							<img src='../pjt/images/bt_next.gif' border='0' align='absmiddle'></a> 		
					 <%	} else 	{  %>		
							<img src='../pjt/images/bt_next.gif' border='0' align='absmiddle'>
					 <%	} %>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	<!--����Ʈ-->
	<TR height=100%>
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			<TR vAlign=middle height=25>
				<TD noWrap width=10 align=middle class='list_title'>NO</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>��������</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>����ݾ� [��]</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>����ݾ� [��]</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>�� �� [��]</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>������� [%]</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=70 align=middle class='list_title'>�ۼ���</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>�ۼ���</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>�� ��</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=18></TD></TR>
		<% if (cost_list.size() == 0) { %>
			<TR vAlign=center height=22>
				 <td colspan='11' align="middle">***** ������ �����ϴ�. ****</td>
			</tr> 
		<% } %>	

		<% 
			for(int i=0,n=1; i<cost_cnt; i++,n++) {
		%>
			<form name="aForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=n%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=rcost[i][7]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=right height="24" class='list_bg'><%=rcost[i][12]%>&nbsp;</TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=right height="24" class='list_bg'><%=rcost[i][8]%>&nbsp;</TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=right height="24" class='list_bg'><%=rcost[i][13]%>&nbsp;</TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=rcost[i][14]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'>&nbsp;<%=rcost[i][6]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'>&nbsp;<%=rcost[i][10]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=rcost[i][15]%> <%=rcost[i][16]%> <%=rcost[i][17]%> </TD>
				<TD><IMG height=1 width=1></TD>
			</TR>
			<TR><TD colSpan=18 background="../pjt/images/dot_line.gif"></TD></TR>
		<% 
			}  //for

		%>
			<input type="hidden" name="mode" size="15" value="">
			<input type="hidden" name="pid" size="15" value="">
			<input type="hidden" name="page" size="15" value="">
			<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
			<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
			<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
			<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
			<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
			</form>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

</body>
</html>
