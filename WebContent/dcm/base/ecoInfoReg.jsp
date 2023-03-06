<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "ECO �ۼ��ϱ�"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage   = "../../admin/errorpage.jsp" 
	import		= "java.util.*"
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
	int attache_cnt = 4;			//÷������ �ִ밹�� (�̸�)
	
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
	//	ECC REO �б�
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

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0" oncontexmenu="return false">
<FORM name="eForm" method="post" style="margin:0" encType="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../dcm/images/blet.gif"> ECO�ۼ�</TD></TR>
			</TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		<TABLE cellSpacing=0 cellPadding=0 border=0>
			<TBODY>
				<TR><TD align=left width='20%' style='padding-left:5px;'>
					<A href="javascript:sendSave();"><img src="../dcm/images/bt_reg.gif" border=0></a></TD>
				</TR>
			</TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--����-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY><!-- �������� �ۼ� -->
			<!--<TR>
				<TD height="25" colspan="4"><img src="../dcm/images/basic_info.gif" width="209" height="25" border="0"></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>-->
			<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�� ��</TD>
				<TD width="87%" height="25" class="bg_04" colspan=3>
					<INPUT type="text" name="ecc_subject" value="<%=ecc.getEccSubject()%>" size="40" maxlength='60' class="text_01"></TD>
			</TR>			
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�������</TD>
				<TD width="37%" height="25" class="bg_04">
					<select name="ecc_reason" class="text_01"> 
					<OPTGROUP label='---------------'>
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
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">���뱸��</TD>
			    <TD width="37%" height="25" class="bg_04">
					<select name="ecc_factor" class="text_01"> 
					<OPTGROUP label='    ------------'>
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
			<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�������</TD>
				<TD width="37%" height="25" class="bg_04">
					<select name="ecc_scope" class="text_01"> 
					<OPTGROUP label='    --------------'>
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
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��������</TD>
			    <TD width="37%" height="25" class="bg_04">
					<select name="ecc_kind" class="text_01"> 
					<OPTGROUP label='    ------------'>
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
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻���(F/G)</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT NAME="fg_code" size='12' maxlength='12' class="text_01" readonly><%=ecc.getFgCode()%>
					<a href="Javascript:searchModel();"><img src="../dcm/images/bt_search.gif" border="0" align="absmiddle"></a>
					</TD>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">������</TD>
				<TD width="37%" height="25" class="bg_04" colspan='3'>
					<INPUT type="text" name="order_date" value="<%=ecc.getOrderDate()%>" size="10" class="text_01" readonly> <A Href="Javascript:OpenCalendar('order_date');"><img src="../dcm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
			   
					</TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻���ǰ</TD>
				<TD width="87%" height="25" class="bg_04" colspan='3' valign='top'>
					<TEXTAREA NAME="part_code" rows='4' cols='30' readonly><%=ecc.getPartCode()%></TEXTAREA>
					<a href="Javascript:searchItems();"><img src="../dcm/images/bt_search.gif" border="0" align="top"></a>
					
			</TD></TR> 
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<!-- ECO �ۼ� -->
			<!--
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD height="23" colspan="4"><img src='../dcm/images/title_eco_info.gif' border='0' align='absmiddle' alt='ECO ����'></TD></TR>-->
			<TR><TD height='5'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����з�</TD>
				<TD width="37%" height="25" class="bg_04">
					<select name="trouble" class="text_01"> 
					<OPTGROUP label='   ---'>
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
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻��κ�</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT type='text' name='chg_position' value='<%=eco.getChgPosition()%>' size='20' maxlength='25' class="text_01"></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">����׿���</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="condition" rows='4' cols='38' class="text_01"><%=eco.getCondition()%></TEXTAREA></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO���볻��</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="solution" rows='4' cols='38' class="text_01"><%=eco.getSolution()%></TEXTAREA></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">÷������</TD>
			   <TD width="87%" height="25" class="bg_04" colspan="3">
				<%
					for(int i=1; i<attache_cnt; i++) {
						out.println("<INPUT type='file' name='attachfile"+i+"' size='40'><br>");
					}
				%>		   
			</TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height="5" colspan="4"></TD>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�ۼ���</TD>
				<TD width="37%" height="25" class="bg_04"><%=ecr_name%>
					<INPUT type="hidden" name="ecr_name" value="<%=ecr_name%>" size="20"></TD>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�μ���</TD>
				<TD width="37%" height="25" class="bg_04"><%=ecr_div_name%>
					<INPUT type="hidden" name="ecr_div_name" value="<%=ecr_div_name%>" size="30"></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��ȭ��ȣ</TD>
				<TD width="37%" height="25" class="bg_04">
					<INPUT type="text" name="ecr_tel" value="<%=ecr_tel%>" size="20"></TD>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�ۼ���</TD>
				<TD width="37%" height="25" class="bg_04"><%=anbdt.getDate()%>
					<INPUT type="hidden" name="ecr_date" value="<%=ecc.getEcrDate()%>" size="14" maxlength='14'></TD>
			</TR> 
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
</TABLE>
<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value='<%=ecc.getPid()%>'>
<INPUT type='hidden' name='eco_no' value=''>
<INPUT type='hidden' name='ecr_id' value='<%=ecr_id%>'>
<INPUT type='hidden' name='ecr_code' value='<%=ecr_code%>'>
<INPUT type='hidden' name='ecr_div_code' value='<%=ecr_div_code%>'>
<INPUT type='hidden' name='eco_id' value='<%=ecr_id%>'>
<INPUT type='hidden' name='eco_name' value='<%=ecr_name%>'>
<INPUT type='hidden' name='eco_code' value='<%=ecr_code%>'>
<INPUT type='hidden' name='eco_div_code' value='<%=ecr_div_code%>'>
<INPUT type='hidden' name='eco_div_name' value='<%=ecr_div_name%>'>
<INPUT type='hidden' name='eco_tel' value='<%=ecr_tel%>'>
<INPUT type='hidden' name='pdg_code' value='<%=ecc.getPdgCode()%>'>
<INPUT type='hidden' name='pd_code' value='<%=ecc.getPdCode()%>'>
</FORM>

	<DIV id="lding" style="position:absolute;left:300px;top:300px;width:300px;height:100px;visibility:hidden;">
		<IMG src='../bm/images/loading8.gif' border='0' width='214' height='200'>
	</DIV>

</BODY>
</HTML>

<script language=javascript>
<!--
//����ϱ�
function sendSave()
{
	var f = document.eForm;

	var ecc_subject = f.ecc_subject.value;
	if(ecc_subject == '') { alert('���躯�������� �Է��Ͻʽÿ�.'); f.ecc_subject.focus(); return; }
	
	var fg_code = f.fg_code.value;
	if(fg_code == '') { alert('�����߻���(FG�ڵ�)�� �Է��Ͻʽÿ�.'); f.fg_code.focus(); return; }

	var order_date = f.order_date.value;
	if(order_date == '') { alert('�������� �Է��Ͻʽÿ�.'); f.order_date.focus(); return; }

	var chg_position = f.chg_position.value;
	if(chg_position == '') { alert('�����߻� �κ��� �Է��Ͻʽÿ�.'); f.chg_position.focus(); return; }
	
	var condition = f.condition.value;
	if(condition == '') { alert('���ι������� �Է��Ͻʽÿ�.'); f.condition.focus(); return; }

	var solution = f.solution.value;
	if(solution == '') { alert('ECO ���볻���� �Է��Ͻʽÿ�.'); f.solution.focus(); return; }
	
	//���ڿ��� ���� Ư������ �����ϱ�
	var order_date = document.eForm.order_date.value;
	for(i=0; i<2; i++) order_date = order_date.replace('/','');

	var todate = '<%=todate%>';
	if(todate > order_date) { alert('�������ں��� ū ���ڸ� �����Ͻʽÿ�.'); f.order_date.focus(); return; }

	document.onmousedown=dbclick;

	//������ �����ϱ�
	document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���

	document.eForm.action='../servlet/CbomBaseInfoServlet';
	document.eForm.mode.value='eco_write';
	document.eForm.order_date.value=order_date;
	document.eForm.submit();
}

// �����߿� �ٸ� ��ư ������ ���ϵ��� ó��
function dbclick(){
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}

//�߻���ǰ ã��
function searchItems()
{
	//��ǥ FG�ڵ常 �����Ѵ�:ù��°�� ��ǥFG�� ����
	var fg_list = document.eForm.fg_code.value;
	var fgc = fg_list.split('\n');
	var fg_code = fgc[0];
	if(fg_code.length == 0) { alert('�����߻���(FG)�� ���� �Է��Ͻʽÿ�.'); return; }
	wopen("../servlet/CbomBaseInfoServlet?mode=pl_list&fg_code="+fg_code,"proxy","700","400","scrollbars=auto,toolbar=no,status=no,resizable=yes");

}

//FG�ڵ� �˻��ϱ�
function searchModel(){
		
	var strUrl = "../gm/openModelInfoWindow.jsp?one_class=pdg_code&one_name=pdg_name&two_class=pd_code&two_name=pd_name&three_class=modelg_code&three_name=modelg_name&four_class=model_code&four_name=model_name&fg_code=fg_code";
	
	wopen(strUrl,"search_bominfo",'820','405','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//��ȿ���� ã�� 
function OpenCalendar(FieldName) {
	var strUrl = "../bm/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}


//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
-->
</script>
