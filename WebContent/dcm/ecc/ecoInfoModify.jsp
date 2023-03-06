<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "ECO �����ϱ�"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.bm.entity.*"
	import		= "com.anbtech.date.anbDate"
	import		= "com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//----------------------------------------------------
	//	parameter ����
	//----------------------------------------------------
	String pid="",ecr_id="",ecr_name="",ecr_code="",ecr_div_code="",ecr_div_name="",ecr_tel="";
	
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();
	String todate = anbdt.getDateNoformat();				//���ó���

	//----------------------------------------------------
	// 	������ login �˾ƺ���
	//----------------------------------------------------
	ecr_id = sl.id; 		//������ login id
	
	String[] idColumn = {"a.id","a.name","b.code","b.ac_code","b.ac_name","a.office_tel"};
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	String item_data = "where (a.id ='"+ecr_id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(item_data);
	bean.init_write();

	if(bean.isAll()) {
		ecr_name = bean.getData("name");			//������ ��
		ecr_code = bean.getData("code");			//������ �μ������ڵ�
		ecr_div_code = bean.getData("ac_code");		//������ �μ��ڵ�
		ecr_div_name = bean.getData("ac_name");		//������ �μ���
		ecr_tel = bean.getData("office_tel");		//������ ��ȭ��ȣ
	} //while

	//----------------------------------------------------
	//	ECC COM �б�
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccComTable ecc;
	ecc = (eccComTable)request.getAttribute("COM_List");
	
	//----------------------------------------------------
	//	ECC ORD �б�
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccOrdTable eco;
	eco = (eccOrdTable)request.getAttribute("ORD_List");

	//----------------------------------------------------
	//	���躯�� �����׸�[F01:��������] ������ �б�
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomEnvTable ecr;
	ArrayList ecr_list = new ArrayList();
	ecr_list = (ArrayList)request.getAttribute("ECR_List");
	ecr = new mbomEnvTable();
	Iterator ecr_iter = ecr_list.iterator();

	//----------------------------------------------------
	//	���躯�� �����׸�[F02:���뱸��] ������ �б�
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomEnvTable ecf;
	ArrayList ecf_list = new ArrayList();
	ecf_list = (ArrayList)request.getAttribute("ECF_List");
	ecf = new mbomEnvTable();
	Iterator ecf_iter = ecf_list.iterator();

	//----------------------------------------------------
	//	���躯�� �����׸�[F03:�������] ������ �б�
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomEnvTable ecs;
	ArrayList ecs_list = new ArrayList();
	ecs_list = (ArrayList)request.getAttribute("ECS_List");
	ecs = new mbomEnvTable();
	Iterator ecs_iter = ecs_list.iterator();

	//----------------------------------------------------
	//	���躯�� �����׸�[F04:��������] ������ �б�
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomEnvTable eck;
	ArrayList eck_list = new ArrayList();
	eck_list = (ArrayList)request.getAttribute("ECK_List");
	eck = new mbomEnvTable();
	Iterator eck_iter = eck_list.iterator();

	//----------------------------------------------------
	//	÷������ �б�
	//----------------------------------------------------
	String bon_path = "/dcm/"+ecc.getEcrId();	//÷������ ���� Ȯ��path
	String fname = eco.getFname();			//÷�����ϸ�
	String sname = eco.getSname();			//÷������ �����
	String ftype = eco.getFtype();			//÷������Type
	String fsize = eco.getFsize();			//÷������Size
	int attache_cnt = 4;					//÷������ �ִ밹�� (�̸�)
	

	//÷������ ������ �б�
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	String[][] addFile = new String[cnt][5];
	for(int i=0; i<cnt; i++) for(int j=0; j<5; j++) addFile[i][j]="";

	if(fname.length() != 0) {
		StringTokenizer f = new StringTokenizer(fname,"|");		//���ϸ� ���
		int m = 0;
		while(f.hasMoreTokens()) {
			addFile[m][0] = f.nextToken();
			addFile[m][0] = addFile[m][0].trim(); 
			if(addFile[m][0] == null) addFile[m][0] = "";
			m++;
		}
		StringTokenizer t = new StringTokenizer(ftype,"|");		//����type ���
		m = 0;
		while(t.hasMoreTokens()) {
			addFile[m][1] = t.nextToken();
			addFile[m][1] = addFile[m][1].trim();
			if(addFile[m][1] == null) addFile[m][1] = "";
			m++;
		}
		StringTokenizer s = new StringTokenizer(fsize,"|");		//����ũ�� ���
		m = 0;
		while(s.hasMoreTokens()) {
			addFile[m][2] = s.nextToken();
			addFile[m][2] = addFile[m][2].trim();
			if(addFile[m][2] == null) addFile[m][2] = "";
			m++;
		}
		StringTokenizer o = new StringTokenizer(sname,"|");		//�������� ���
		m = 0;
		int no = 1;
		while(o.hasMoreTokens()) {
			addFile[m][3] = o.nextToken();	
			addFile[m][4] = addFile[m][3];
			addFile[m][3] = addFile[m][3].trim() + ".bin";			//�������ϸ�
			addFile[m][4] = addFile[m][4].trim();					//TEMP �������ϸ�
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}

%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<FORM name="eForm" method="post" style="margin:0" encType="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
					<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
					<TR bgcolor="#BAC2CD">
						<TD valign='middle' class="title"><img src="../dcm/images/blet.gif"> ECO����</TD>
					</TR>
				</TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=30><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
					<A href="javascript:saveModify();"><img src="../dcm/images/bt_modify.gif" border=0 align='absmiddle'></a>
					<A href="javascript:delECC();"><img src="../dcm/images/bt_del.gif" border=0 align='absmiddle'></a>
					<A href="javascript:chgBOM();"><img src="../dcm/images/bt_bom_modify.gif" border='0' align='absmiddle' alt='BOM����'></a>
					</TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--����-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<TD align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
			 <!-- �������� �ۼ� -->
			
			<TR><TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO NO</TD>
				<TD width="36%" height="25" class="bg_04"><%=ecc.getEcoNo()%></TD>			
				<TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�� ��</TD>
				<TD width="36%" height="25" class="bg_04">
					<INPUT type="text" name="ecc_subject" value="<%=ecc.getEccSubject()%>" size="40" maxlength='50' class="text_01"></TD></TR>
			
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�������</TD>
				<TD width="36%" height="25" class="bg_04">
					<select name="ecc_reason" class="text_01"> 
					<OPTGROUP label='----------------'>
					<%
						String rsel="",ecc_reason=ecc.getEccReason();
						while(ecr_iter.hasNext()) {
							ecr = (mbomEnvTable)ecr_iter.next(); 
							if(ecc_reason.equals(ecr.getSpec())) rsel = "selected";
							else rsel = "";
							out.print("<option "+rsel+" value='"+ecr.getSpec()+"'>");
							out.println(ecr.getSpec()+"</option>");
						} 
					%></select></TD>
				<TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">���뱸��</TD>
			    <TD width="36%" height="25" class="bg_04">
					<select name="ecc_factor" class="text_01"> 
					<OPTGROUP label='---------------'>
					<%
						String fsel="",ecc_factor=ecc.getEccFactor();
						while(ecf_iter.hasNext()) {
							ecf = (mbomEnvTable)ecf_iter.next(); 
							if(ecc_factor.equals(ecf.getSpec())) fsel = "selected";
							else fsel = "";
							out.print("<option "+fsel+" value='"+ecf.getSpec()+"'>");
							out.println(ecf.getSpec()+"</option>");
						} 
					%></select></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�������</TD>
			   <TD width="36%" height="25" class="bg_04">
					<select name="ecc_scope" class="text_01"> 
					<OPTGROUP label='----------------'>
					<%
						String ssel="",ecc_scope=ecc.getEccScope();
						while(ecs_iter.hasNext()) {
							ecs = (mbomEnvTable)ecs_iter.next(); 
							if(ecc_scope.equals(ecs.getSpec())) ssel = "selected";
							else ssel = "";
							out.print("<option "+ssel+" value='"+ecs.getSpec()+"'>");
							out.println(ecs.getSpec()+"</option>");
						} 
					%></select></TD>
				<TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��������</TD>
			    <TD width="36%" height="25" class="bg_04">
					<select name="ecc_kind" class="text_01"> 
					<OPTGROUP label='---------------'>
					<%
						String sel="",ecc_kind=ecc.getEccKind();
						while(eck_iter.hasNext()) {
							eck = (mbomEnvTable)eck_iter.next(); 
							if(ecc_kind.equals(eck.getSpec())) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+eck.getSpec()+"'>");
							out.println(eck.getSpec()+"</option>");
						} 
					%></select></TD>
			</TR>
			<!--<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��ǰ��</TD>
			   <TD width="36%" height="25" class="bg_04">
					<INPUT type='text' name='pdg_code' value='<%=ecc.getPdgCode()%>' size='20' class="text_01" readonly></TD>
			   <TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��ǰ</TD>
			   <TD width="36%" height="25" class="bg_04">
					<INPUT type='text' name='pd_code' value='<%=ecc.getPdCode()%>' size='20' class="text_01" readonly></TD>
			</TR>-->

			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻���(FG)</TD>
			   <TD width="36%" height="25" class="bg_04">
					<INPUT NAME="fg_code" size='12' maxlength='12' class="text_01" value=<%=ecc.getFgCode()%> readonly> <a href="Javascript:searchModel();"><img src="../dcm/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
			   <TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">������</TD>
			   <TD width="36%" height="25" class="bg_04">
					<INPUT type="text" name="order_date" value="<%=anbdt.getSepDate(ecc.getOrderDate(),"/")%>" size="10" class="text_01" readonly> <A Href="Javascript:OpenCalendar('order_date');"><img src="../dcm/images/bt_calendar.gif" border="0" align='absmiddle'></A></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			    <TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻���ǰ</TD>
			    <TD width="87%" height="25" class="bg_04" colspan='3'>
					<TEXTAREA NAME="part_code" rows='4' cols='30' readonly><%=ecc.getPartCode()%></TEXTAREA> <a href="Javascript:searchItems();"><img src="../dcm/images/bt_search.gif" border="0"  align='top'></a></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height='5px'></TD></TR>
			<!-- ECO �ۼ� -->
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����з�</TD>
			   <TD width="36%" height="25" class="bg_04">
					<select name="trouble" class="text_01"> 
					<OPTGROUP label='-----'>
					<%
						String[] trb_data = {"��ǰ","����","����","����"};
						String tsel="",trouble=eco.getTrouble();
						for(int i=0; i<trb_data.length; i++) {
							if(trouble.equals(trb_data[i])) tsel = "selected";
							else tsel = "";
							out.print("<option "+tsel+" value='"+trb_data[i]+"'>");
							out.println(trb_data[i]+"</option>");
						} 
					%></select>
					</TD>
			   <TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻��κ�</TD>
			   <TD width="36%" height="25" class="bg_04">
					<INPUT type='text' name='chg_position' value='<%=eco.getChgPosition()%>' size='20' maxlength='25' class="text_01"></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">����׿���</TD>
			   <TD width="36%" height="25" class="bg_04">
					<TEXTAREA NAME="condition" rows='4' cols='37' class="text_01"><%=eco.getCondition()%></TEXTAREA></TD>
			   <TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO���볻��</TD>
			   <TD width="36%" height="25" class="bg_04">
					<TEXTAREA NAME="solution" rows='4' cols='37' class="text_01"><%=eco.getSolution()%></TEXTAREA></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">÷������</TD>
			   <TD width="87%" height="25" class="bg_04" colspan="3">
				<% 
				for(int i=0,no=1; no<attache_cnt; i++,no++) {
					out.println("<INPUT type='file' name='attachfile"+no+"' size='58'>");
					if((i<cnt) && (addFile[i][0].length() != 0)) {		//��ϵ� ÷�ΰ� ������
						out.print("&nbsp;<INPUT type='checkbox' name='delfile"+no+"' value='"+addFile[i][3]+"'>");
						out.print("<a href='../dcm/attach_download.jsp?fname="+addFile[i][0]);
						out.print("&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]);
						out.print("&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
						out.println("<br>"); 
					}
				}
				%>		   
			   </TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height=5></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�ۼ���</TD>
				<TD width="36%" height="25" class="bg_04"><%=ecr_name%>
					<INPUT type="hidden" name="ecr_name" value="<%=ecr_name%>" size="20">
					<INPUT type="hidden" name="ecr_div_name" value="<%=ecr_div_name%>" size="30"></TD>
				<TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�ۼ���</TD>
				<TD width="36%" height="25" class="bg_04"><%=anbdt.getDate()%>
					<INPUT type="hidden" name="ecr_date" value="<%=ecc.getEcrDate()%>" size="30"></TD>
			</TR> 
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD width="14%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��ȭ��ȣ</TD>
				<TD width="86%" height="25" class="bg_04" colspan='3'><%=ecr_tel%></TD>
			</TR> 
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY></TABLE></TD></TR>
</TABLE>
<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value='<%=ecc.getPid()%>'>
<INPUT type='hidden' name='eco_no' value='<%=ecc.getEcoNo()%>'>
<INPUT type='hidden' name='mgr_id' value='<%=ecc.getMgrId()%>'>
<INPUT type='hidden' name='ecr_id' value='<%=ecr_id%>'>
<INPUT type='hidden' name='ecr_code' value='<%=ecr_code%>'>
<INPUT type='hidden' name='ecr_div_code' value='<%=ecr_div_code%>'>
<INPUT type='hidden' name='eco_id' value='<%=ecr_id%>'>
<INPUT type='hidden' name='eco_name' value='<%=ecr_name%>'>
<INPUT type='hidden' name='eco_code' value='<%=ecr_code%>'>
<INPUT type='hidden' name='eco_div_code' value='<%=ecr_div_code%>'>
<INPUT type='hidden' name='eco_div_name' value='<%=ecr_div_name%>'>
<INPUT type='hidden' name='eco_tel' value='<%=ecr_tel%>'>
<INPUT type='hidden' name='ecc_status' value='<%=ecc.getEccStatus()%>'>
<INPUT type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>
<INPUT type='hidden' name='fname' value='<%=fname%>'>
<INPUT type='hidden' name='sname' value='<%=sname%>'>
<INPUT type='hidden' name='ftype' value='<%=ftype%>'>
<INPUT type='hidden' name='fsize' value='<%=fsize%>'>
<!--�߰�-->
<INPUT type='hidden' name='pdg_code' value='<%=ecc.getPdgCode()%>' readonly></TD>
<INPUT type='hidden' name='pd_code' value='<%=ecc.getPdCode()%>'  readonly></TD>

</FORM>

<DIV id="lding" style="position:absolute;left:300px;top:150px;width:300px;height:100px;visibility:hidden;">
	<IMG src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

</BODY>
</HTML>


<SCRIPT language='javascript'>
<!--
//�����ϱ��ϱ�
function saveModify()
{
	var f = document.eForm;

	var ecc_subject = f.ecc_subject.value;
	if(ecc_subject == '') { alert('���躯�������� �Էµ��� �ʾҽ��ϴ�.'); f.ecc_subject.focus(); return; }
	
	var fg_code = f.fg_code.value;
	if(fg_code == '') { alert('�����߻���(FG�ڵ�)�� �Էµ��� �ʾҽ��ϴ�.'); f.fg_code.focus(); return; }

	var order_date = f.order_date.value;
	if(order_date == '') { alert('�������� �Էµ��� �ʾҽ��ϴ�.'); f.order_date.focus(); return; }

	var chg_position = f.chg_position.value;
	if(chg_position == '') { alert('�����߻� �κ��� �Էµ��� �ʾҽ��ϴ�.'); f.chg_position.focus(); return; }
	
	var condition = f.condition.value;
	if(condition == '') { alert('���ι������� �Էµ��� �ʾҽ��ϴ�.'); f.condition.focus(); return; }

	var solution = f.solution.value;
	if(solution == '') { alert('ECO���� ������ �Էµ��� �ʾҽ��ϴ�.'); f.solution.focus(); return; }
	
	//���ڿ��� ���� Ư������ �����ϱ�
	var order_date = document.eForm.order_date.value;
	for(i=0; i<2; i++) order_date = order_date.replace('/','');

	var todate = '<%=todate%>';
	if(todate > order_date) { alert('�������� �������ں��� ū ���ڸ� �����Ͻʽÿ�.'); f.order_date.focus(); return; }
	
	document.onmousedown=dbclick; 
	
	//������ �����ϱ�
	document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���

	document.eForm.action='../servlet/CbomProcessServlet';
	document.eForm.mode.value='ecc_modify';
	document.eForm.order_date.value=order_date;
	document.eForm.submit();
}

// �����߿� �Ǵٸ� ��ư�� ������ ���ϵ��� ó��
function dbclick(){
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽýÿ�.");
}


//�߻���ǰ ã��
function searchItems()
{
	//��ǥ FG�ڵ常 �����Ѵ�:ù��°�� ��ǥFG�� ����
	var fg_list = document.eForm.fg_code.value;
	var fgc = fg_list.split('\n');
	var fg_code = fgc[0];
	if(fg_code.length == 0) { alert('�����߻���(FG)�� ���� �Է��Ͻʽÿ�.'); return; }
	wopen("../servlet/CbomBaseInfoServlet?mode=pl_list&fg_code="+fg_code,"proxy","700","400","scrollbars=no,toolbar=no,status=no,resizable=yes");

}
//�߻���(FG)�ڵ� �˻��ϱ�
//function searchModel(){
//	var url = "GoodsInfoServlet?mode=search_model_info&fname=eForm&one_class=g_code&one_name=pdg_code&two_class=m_code&two_name=m_name&three_class=pd_code&three_name=pd_code&four_class=model_code&four_name=model_name&fg_code=fg_code";	
//	wopen(url,"search_bominfo",'800','500','scrollbars=yes,toolbar=no,status=no,resizable=no');
//}

//FG�ڵ� �˻��ϱ�
function searchModel(){
		
	var strUrl = "../gm/openModelInfoWindow.jsp?one_class=pdg_code&one_name=pdg_name&two_class=pd_code&two_name=pd_name&three_class=modelg_code&three_name=modelg_name&four_class=model_code&four_name=model_name&fg_code=fg_code";
	
	wopen(strUrl,"search_bominfo",'820','405','scrollbars=no,toolbar=no,status=no,resizable=no');
}


//�����ϱ�
function delECC()
{
	if(confirm("���� ���� �Ͻðڽ��ϱ�?")){
		document.eForm.action='../servlet/CbomProcessServlet';
		document.eForm.mode.value='ecc_delete';
		document.eForm.submit();
	} else {
		return;
	}
}

//BOM�����۾�
function chgBOM()
{
	//��ǥ���� �ִ��� �˻�
	var fg_list = document.eForm.fg_code.value;
	var fgc = fg_list.split('\n');
	var fg_code = fgc[0];
	if(fg_code.length == 0) { alert('�����߻���(FG)�� ���� �Է��Ͻʽÿ�.'); return; }

	//���泻�� �����ߴ��� Ȯ���ϱ�
	if(!confirm('���泻���� �����ϼ̽��ϱ�? ��������Ϸ��� Ȯ���� �����Ͻʽÿ�')) { return; }

	//BOM�����۾��� �������� �Ǵ��ϱ�
	var status = '<%=ecc.getEccStatus()%>';
	if(status != '6') { 
		alert('BOM�����۾��� �� �� ���� �����Դϴ�. ���� ������ ������ �����Ͻʽÿ�.'); return;
	}

	var para = "fg_code="+fg_code+"&eco_no=<%=ecc.getEcoNo()%>";
	document.eForm.action='../dcm/ecc/CbomFrame.jsp?'+para;
	document.eForm.submit();
}

//��ȿ���� ã��
function OpenCalendar(FieldName) {
	var strUrl = "../dcm/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,'search_day','180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
-->
</SCRIPT>