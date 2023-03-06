<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������ ����ϱ�"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();

	//---------------------------------------------------------
	//	�Ķ���� �ޱ�
	//--------------------------------------------------------
	String pjt_code="",pjtWord="0",sItem="",sWord="",parent_node="",child_node="",chg_note="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();

	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();
		pjtWord = para.getPjtword();
		sItem = para.getSitem();
		sWord = para.getSword();
		parent_node = para.getParentNode();
		child_node = para.getChildNode();
		chg_note = para.getChgNote();				//�����������
	}
	//�����Է� �ϷῩ�� �Ǵ��ϱ�
	String Complete_Schedule = request.getParameter("Complete_Schedule");	
	if(Complete_Schedule == null) Complete_Schedule = "N";

	//�ش������ ù���� ������ ��� ã��
	String FLnode = request.getParameter("FLnode");	
	if(FLnode == null) FLnode = "|";
	String Fnode = FLnode.substring(0,FLnode.indexOf("|"));						//ù��° ���
	String Lnode = FLnode.substring(FLnode.indexOf("|")+1,FLnode.length());		//������ ���

	//��ü weight�� ã�� [1,10,100������ ���߱� ����]
	String tWeight = request.getParameter("tWeight");
	tWeight = Double.toString(Double.parseDouble(tWeight)+0.001);
	tWeight = tWeight.substring(0,tWeight.indexOf('.')+2);

	//----------------------------------------------------
	//	�ش���� ��ȹ��/������ �� �������� ã�� [�⺻�������� ã��]
	//----------------------------------------------------
	String plan_sd="",plan_ed="",change_sd="",change_ed="";	//��ȹ����,������, ��������,������
	String pjt_status = "";									//�����������
	com.anbtech.pjt.entity.projectTable gen;
	ArrayList gen_list = new ArrayList();
	gen_list = (ArrayList)request.getAttribute("GEN_List");
	gen = new projectTable();
	Iterator gen_iter = gen_list.iterator();

	if(gen_iter.hasNext()) {
		gen = (projectTable)gen_iter.next();
		plan_sd = gen.getPlanStartDate();		if(plan_sd == null) plan_sd = "";		//��ȹ������
		plan_ed = gen.getPlanEndDate();			if(plan_ed == null) plan_ed = "";		//��ȹ������
		change_sd = gen.getChgStartDate();		if(change_sd == null) change_sd = "";	//����������
		change_ed = gen.getChgEndDate();		if(change_ed == null) change_ed = "";	//����������
		pjt_status = gen.getPjtStatus();
	}

	//------------------------------------------------------------
	//	�ش� ������ ��� LIST
	//------------------------------------------------------------
	com.anbtech.pjt.entity.projectTable man;
	ArrayList man_list = new ArrayList();
	man_list = (ArrayList)request.getAttribute("MAN_List");
	man = new projectTable();
	Iterator man_iter = man_list.iterator();
	int man_cnt = man_list.size();

	String[][] member = new String[man_cnt][4];
	String pjt_member = "";		//���߸�������� ����
	int p = 0;
	while(man_iter.hasNext()) {
		man = (projectTable)man_iter.next();
		member[p][0] = man.getPjtMbrType();
		member[p][1] = man.getPjtMbrId();
		member[p][2] = man.getPjtMbrName();
		member[p][3] = man.getPjtMbrJob();
		pjt_member += member[p][0]+"|"+member[p][1]+"|"+member[p][2]+"|"+member[p][3]+";";
		p++;
	}

	//-----------------------------------------------------------
	//	�ش� ������ ��� ������ [������ �Է�����]
	//-----------------------------------------------------------
	String pid="",pjt_name="",level_no="",node_name="",weight="",user_id="",user_name="",pjt_node_mbr="";
	String plan_cnt="",chg_cnt="",rst_cnt="",node_status="",remark="";
	String psd="",ped="",csd="",ced="",rsd="",red="";		//DB���� ��ȹ��,������,�Ϸ���
	String dpsd="",dped="",dcsd="",dced="",drsd="",dred="";	//ȭ����¿�
	com.anbtech.pjt.entity.projectTable sch;
	ArrayList sch_list = new ArrayList();
	sch_list = (ArrayList)request.getAttribute("NODE_List");
	sch = new projectTable();
	Iterator sch_iter = sch_list.iterator();

	if(sch_iter.hasNext()) {
		sch = (projectTable)sch_iter.next();

		pid = sch.getPid();
		pjt_code = sch.getPjtCode();
		pjt_name = sch.getPjtName();
		parent_node = sch.getParentNode();
		child_node = sch.getChildNode();
		level_no = sch.getLevelNo();
		node_name = sch.getNodeName();
		weight = Double.toString(sch.getWeight());
		user_id = sch.getUserId();					if(user_id == null) user_id = "";
		user_name = sch.getUserName();				if(user_name == null) user_name = "";
		pjt_node_mbr = sch.getPjtNodeMbr();			if(pjt_node_mbr == null) pjt_node_mbr = "";

		psd = sch.getPlanStartDate();	if(psd == null) psd = "";
		if(psd.length() != 0) dpsd = psd.substring(0,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8);
		else dpsd = anbdt.getDate(0);
		ped = sch.getPlanEndDate();	if(ped == null) ped = "";
		if(ped.length() != 0) dped = ped.substring(0,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8);
		else dped = anbdt.getDate(30);

		csd = sch.getChgStartDate();	if(csd == null) csd = "";
		if(csd.length() != 0) dcsd = csd.substring(0,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8);
		else dcsd = "";
		ced = sch.getChgEndDate();	if(ced == null) ced = "";
		if(ced.length() != 0) dced = ced.substring(0,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8);
		else dced = "";

		rsd = sch.getRstStartDate();	if(rsd == null) rsd = "";
		if(rsd.length() != 0) drsd = rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8);
		else drsd = "";
		red = sch.getRstEndDate();	if(red == null) red = "";
		if(red.length() != 0) dred = red.substring(0,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8);
		else dred = "";

		plan_cnt = Integer.toString(sch.getPlanCnt());
		chg_cnt = Integer.toString(sch.getChgCnt());
		rst_cnt = Integer.toString(sch.getResultCnt());
		node_status = sch.getNodeStatus();	if(node_status == null) node_status = "0";
		if(node_status.length() == 0) node_status = "0";
		remark = sch.getRemark();			if(remark == null) remark = "";
	}

	//------------------------------------------------------------
	//	�ش����� ���������� �̷�����
	//------------------------------------------------------------
	com.anbtech.pjt.entity.projectTable chg;
	ArrayList chg_list = new ArrayList();
	chg_list = (ArrayList)request.getAttribute("CHG_List");
	chg = new projectTable();
	Iterator chg_iter = chg_list.iterator();
	int chg_h_cnt = chg_list.size();

	String[][] change = new String[chg_h_cnt][11];
	int c = 0;
	while(chg_iter.hasNext()) {
		chg = (projectTable)chg_iter.next();
		change[c][0] = chg.getPid();
		change[c][1] = chg.getPjtCode();
		change[c][2] = chg.getPjtName();
		change[c][3] = chg.getNodeCode();
		change[c][4] = chg.getNodeName();
		change[c][5] = chg.getUserId();
		change[c][6] = chg.getUserName();
		change[c][7] = chg.getInDate();
		change[c][8] = chg.getChgNote();
		change[c][9] = chg.getView();
		change[c][10] = chg.getModify();
		c++;
	}
	String tpage = request.getParameter("Tpage"); if(tpage == null) tpage = "1";
	String cpage = request.getParameter("Cpage"); if(cpage == null) cpage = "1";
	int Tpage = Integer.parseInt(tpage);
	int Cpage = Integer.parseInt(cpage);
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
var pjt_status = '<%=pjt_status%>';
if(pjt_status == 'S') {
	alert('�����η��� ��ϵ������� �����Դϴ�.\n\n�����η��� ����� ������ ����Ͻʽÿ�.');
	history.go(-1);
}
//�������� ������ �̵��ϱ�
function goPage(a) 
{
	var chg_note = document.eForm.chg_note.value;

	document.sForm.action='../servlet/projectSchServlet';
	document.sForm.mode.value='PSC_VS';
	document.sForm.page.value=a;
	document.sForm.chg_note.value=chg_note;
	document.sForm.submit();
}
//�������� �˻��ϱ�
function goSearch()
{
	var chg_note = document.eForm.chg_note.value;

	document.sForm.action='../servlet/projectSchServlet';
	document.sForm.mode.value='PSC_VS';
	document.sForm.page.value='1';
	document.sForm.chg_note.value=chg_note;
	document.sForm.submit();
}
//������ �Է��ϱ�
function sendSave()
{
	var node_mgr = document.eForm.node_mgr.value;
	var user = node_mgr.split('|');

	//���ڿ��� '/'����
	var psd = document.eForm.plan_start_date.value;		//��ȹ�Ⱓ ������
	for(i=0;i<2;i++) psd = psd.replace('/','');	
	var ped = document.eForm.plan_end_date.value;		//��ȹ�Ⱓ �Ϸ���
	for(i=0;i<2;i++) ped = ped.replace('/','');	
	if(psd > ped) { alert('��ȹ�Ⱓ�� �����ϰ� �Ϸ��� �Է¿� �߸��ֽ��ϴ�.'); return; } 

	var csd = document.eForm.chg_start_date.value;		//�����Ⱓ ������
	for(i=0;i<2;i++) csd = csd.replace('/','');	
	var ced = document.eForm.chg_end_date.value;		//�����Ⱓ �Ϸ���
	for(i=0;i<2;i++) ced = ced.replace('/','');	
	if(csd > ced) { alert('�����Ⱓ�� �����ϰ� �Ϸ��� �Է¿� �߸��ֽ��ϴ�.'); return; } 
	
	//������ �ľ�[���Ϸ�ô� �����]
	var node_status = document.eForm.node_status.value;
	if(node_status == '2') { alert('�Ϸ�� ����Դϴ�.'); return; };
	
	//�����Է¿��� �Ǵ�
	var chg_note = document.eForm.chg_note.value;
	if((csd.length != 0) && (ced.length != 0) && (chg_note.length < 4)) { 
		alert('4���̻��� �������� ������ �Է��Ͻʽÿ�.'); return; 
	}

	var rsd = document.eForm.rst_start_date.value;		//�����Ⱓ ������
	for(i=0;i<2;i++) rsd = rsd.replace('/','');	
	var red = document.eForm.rst_end_date.value;		//�����Ⱓ �Ϸ���
	for(i=0;i<2;i++) red = red.replace('/','');	

	//�⺻������ ��ȹ���� �� ���������� ���Ͽ� �޽��� �˷��ֱ�
	var plan_sd='<%=plan_sd%>';	
	var plan_ed='<%=plan_ed%>';
	var change_sd='<%=change_sd%>';
	var change_ed='<%=change_ed%>';

	var pnsd = plan_sd.substring(0,4)+'/'+plan_sd.substring(4,6)+'/'+plan_sd.substring(6,8);
	var pned = plan_ed.substring(0,4)+'/'+plan_ed.substring(4,6)+'/'+plan_ed.substring(6,8);
	var cned = change_sd.substring(0,4)+'/'+change_sd.substring(4,6)+'/'+change_sd.substring(6,8);
	var cned = change_ed.substring(0,4)+'/'+change_ed.substring(4,6)+'/'+change_ed.substring(6,8);

	if(node_status == '0') {			//��ȹ�� �˻��ϱ�
		if(psd < plan_sd) { 
			alert('���� �⺻������ ��ȹ�����Ͽ��� ��� �����Դϴ�.\n\n[��ȹ�Ⱓ: '+pnsd+' ~ '+pned+']'); return; }
		if(ped > plan_ed) { 
			alert('���� �⺻������ ��ȹ�����Ͽ��� ��� �����Դϴ�.\n\n[��ȹ�Ⱓ: '+pnsd+' ~ '+pned+']'); return; }
	} else if(node_status == '1') {		//������ �˻��ϱ�
		if(change_sd.length == 0) {		//������ȹ�� �Էµ��� ������ ��ȹ�ϰ� ��
			if(csd < plan_sd) { 
				alert('���� �⺻������ ��ȹ�����Ͽ��� ��� �����Դϴ�.\n\n[��ȹ�Ⱓ: '+pnsd+' ~ '+pned+']'); return; }
			if(ced > plan_ed) { 
				alert('���� �⺻������ ��ȹ�����Ͽ��� ��� �����Դϴ�.\n\n[��ȹ�Ⱓ: '+pnsd+' ~ '+pned+']'); return; }
		} else {						//������ȹ�� �Էµ� ���
			if(csd < change_sd) { 
				alert('���� �⺻������ ���������Ͽ��� ��� �����Դϴ�.\n\n[�����Ⱓ: '+cned+' ~ '+cned+']'); return; }
			if(ced > change_ed) { 
				alert('���� �⺻������ ���������Ͽ��� ��� �����Դϴ�.\n\n[�����Ⱓ: '+cned+' ~ '+cned+']'); return; }
		}
	}

	//ù����� �����ϰ� ����������� �������� �⺻������ ��ȹ������ ���ƾ� �Ѵ�.
	var Fnode = '<%=Fnode%>';				//ù ���
	var Lnode = '<%=Lnode%>';				//������ ���
	var child_node = '<%=child_node%>';		//�Է��� ������

	if(child_node == Fnode) {				//ù�������� ������ �˻�
		if(plan_sd != psd) {alert('ù����� ��ȹ�������� ��ȹ�Ⱓ�� �����ϰ� ���ƾ� �մϴ�.['+pnsd+']'); 
			return;
		} 
	}
	if(child_node == Lnode) {				//��������� ������ ������ �˻�
		if(plan_ed != ped) {alert('����������� ��ȹ�������� ��ȹ�Ⱓ�� �����ϰ� ���ƾ� �մϴ�.['+pned+']');
			return;
		} 
	}

	//weight�� �Է°��ɼ��� �˻�
	var weight = document.eForm.weight.value; 
	if(isNaN(weight)) {alert('��尡��ġ�� ���ڸ� �Է°����մϴ�.'); return; }
	if(weight == '') {alert('��尡��ġ�� �Է����� �ʾҽ��ϴ�.'); return; }

	if(weight < 0.1) { alert('��尡��ġ�� �Է°����� weight�� 0.1 ~ 10.0 �Դϴ�.'); return; }
	else if(weight > 10.0) { alert('��尡��ġ�� �Է°����� weight�� 0.1 ~ 10.0 �Դϴ�.'); return; }

	var w_point = weight.split("."); if(w_point[1] == undefined) w_point[1] = 0;
	var w_len = w_point[1].length; if(w_len == null) w_len = 0;
	if(w_len > 1){alert('��尡��ġ�� �Ҽ������� ���ڸ����ڸ� �����մϴ�.'); return; }

	//weight�� �Ѱ������� 1,10,100 ������ ���߱����� ����������Է½� �˷��ֱ�
	var bWeight = '<%=weight%>';						//�ش����� ���� ��ϵ� weight��
	var tWeight = '<%=tWeight%>';						//DB�� ��ϵ� ��ü weight��
	var dWeight = eval(eval(tWeight)-eval(bWeight));	//�ش��带 ������ ��ü weight��
	var cWht = eval(eval(dWeight) + eval(weight));		//����� ��üweight��
	if(child_node == Lnode) {							//��������� 
		//��������忡�� �Է°����� weight�� ���ϱ�
		var cmt = '��üweight �հ�� 1 or 10 or 100 �̾�� �մϴ�.\n\n';
			cmt += '�Է��� ��ü��: '+dWeight+'\n\n';
			cmt += '�Է��� ��ü��: '+cWht+'\n\n';
		var p = 0;
		if(tWeight > 10) {
			p = eval(100-eval(dWeight)); 
			if(cWht != 100){ alert(cmt+'�Է°����Ѱ�: '+p); return; }
		} else if(tWeight > 1) { 
			p = eval(10-eval(dWeight)); 
			if(cWht != 10){ alert(cmt+'�Է°����Ѱ�: '+p); return; }
		} else if(tWeight > 0) {
			p = eval(1-eval(dWeight)); 
			if(cWht != 1){ alert(cmt+'�Է°����Ѱ�: '+p); return; }
		}
	}



	//�Է�ó��
	document.eForm.action='../servlet/projectSchServlet';
	document.eForm.mode.value='PSC_S';
	document.eForm.user_id.value=user[0];
	document.eForm.user_name.value=user[1];
	document.eForm.plan_start_date.value=psd;
	document.eForm.plan_end_date.value=ped;
	document.eForm.chg_start_date.value=csd;
	document.eForm.chg_end_date.value=ced;
	document.eForm.rst_start_date.value=rsd;
	document.eForm.rst_end_date.value=red;
	document.eForm.submit();

}
//���߸���Է��ϱ�
function searchMan(FieldName) {
	var pjt_mbr = '<%=pjt_member%>';
	var strUrl = "../pjt/searchMember.jsp?FieldName="+FieldName+"&pjt_member="+pjt_mbr;
	newWIndow = window.open(strUrl, "member", "width=0, height=0,  scrollbars=yes");
}
//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../pjt/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
}
//��� �۾����� �ϱ�
function orderJob() {
	//���ڿ��� '/'����
	var psd = document.eForm.plan_start_date.value;		//��ȹ�Ⱓ ������
	for(i=0;i<2;i++) psd = psd.replace('/','');	
	var ped = document.eForm.plan_end_date.value;		//��ȹ�Ⱓ �Ϸ���
	for(i=0;i<2;i++) ped = ped.replace('/','');	

	//DB ��Ͽ��� �Ǵ��ϱ�
	var spsd = '<%=psd%>';								//DB�� ��ϵ� ��ȹ������
	if(spsd.length == 0) { alert('�������� ��ϵ��� �ʾҽ��ϴ�.'); return; }

	//�������� �����Է½� ����۾����ð� ����
	var Complete_Schedule = '<%=Complete_Schedule%>';
	if(Complete_Schedule == 'N') { 
		alert('�ش������ ����������� �Ϸ���� �ʾҽ��ϴ�.\n\n ������� �Ϸ��� �����Ͻʽÿ�.'); return; 
	}

	//��� ���δ������ �Ǵ��ϱ� : �����Ϸ����� �ִµ� ���°� "1[������]"�� ���
	var red = document.eForm.rst_end_date.value;		//�����Ⱓ �Ϸ���
	for(i=0;i<2;i++) red = red.replace('/','');	
	var node_status = '<%=node_status%>';				//��� ����
	if((red.length != 0) && (node_status == '1')) {
		alert('�ش���� ���Ϸ� ���δ�� �����Դϴ�.\n\n ����۾��� ������ �� �����ϴ�.'); return; 
	} 
	//��� �Ϸ����� �Ǵ��ϱ�
	else if((red.length != 0) && (node_status == '2')) {
		alert('�ش���� ���Ϸ� �����Դϴ�.\n\n ����۾��� ������ �� �����ϴ�.'); return; 
	}

	document.eForm.action='../servlet/projectSchServlet';
	document.eForm.mode.value='PSC_VJ';
	document.eForm.pjt_code.value='<%=pjt_code%>';
	document.eForm.parent_node.value='<%=parent_node%>';
	document.eForm.child_node.value='<%=child_node%>';
	document.eForm.submit();

}
//�ش����� �������泻�� �󼼺���
function changeView(pid)
{
	sParam = "strSrc=../servlet/projectChangeSchServlet&pid="+pid+"&mode=PCS_V"; 
	showModalDialog("../pjt/modalFrm.jsp?"+sParam,"","dialogWidth:560px;dialogHeight:300px;resizable=0");
}
//�ش����� �������泻�� �����غ� �󼼺���
function changeModify(pid)
{
	sParam = "strSrc=../servlet/projectChangeSchServlet&pid="+pid+"&mode=PCS_MV";
	var rtn = showModalDialog("../pjt/modalFrm.jsp?"+sParam,"","dialogWidth:560px;dialogHeight:300px;resizable=0");

	if(rtn == 'RL') {
		var chg_note = document.eForm.chg_note.value;

		document.sForm.action='../servlet/projectSchServlet';
		document.sForm.mode.value='PSC_VS';
		document.sForm.page.value=1;
		document.sForm.chg_note.value=chg_note;
		document.sForm.submit();
	}
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
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> ���������� ����ϱ�</TD>
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
					<TD align=left width=5></TD>
					<TD align=left width=100%>&nbsp;&nbsp;�����ڵ� : <%=pjt_code%> 
				&nbsp;&nbsp;&nbsp;&nbsp;������ : <%=pjt_name%> &nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:sendSave();"><img src="../pjt/images/bt_save.gif" border="0"></a> <a href="javascript:history.go(-1)"><img src="../pjt/images/bt_cancel.gif" border="0"></a> <a href="javascript:orderJob();"><B>�۾�����</B></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><form name="eForm" method="post" style="margin:0">
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">����̸�</td>
			   <td width="40%" height="25" class="bg_04"><%=child_node%> <%=node_name%>&nbsp;&nbsp;&nbsp;&nbsp;
				<%
					String status = "";
					if(node_status.equals("")) status = "�̵��";
					else if(node_status.equals("0")) status = "������";
					else if(node_status.equals("1")) status = "������";
					else if(node_status.equals("2")) status = "�Ϸ�";
					else if(node_status.equals("3")) status = "DROP";
					else if(node_status.equals("4")) status = "HOLD";
					else if(node_status.equals("5")) status = "SKIP";
							
					if((red.length() != 0) && (node_status.equals("1"))) status = "���δ����";

					out.print("<font color=blue>[���� : "+status+"]</font>"); 
				%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��尡��ġ</td>
				<td width="40%" height="25" class="bg_04">
				<%
					if(pjt_status.equals("0")){  //���� �������� ��츸 ��尡��ġ �Է°���
						out.println("<input type='text' name='weight' size='3' value='"+weight+"'> [�Է�:0.1 ~ 10.0]");
					} else {
						out.println(weight+" [�Է�:0.1 ~ 10.0]");
						out.println("<input type='hidden' name='weight' size='3' value='"+weight+"'>");
					}
				%>
				</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� �� ��</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<%
					String[] grade_no = {"B","C","D","E","F","G"};
					String[] grade_name = {"��� PL","���� PL","SUB-PL","���㰳��","��������","�ܺ��η�"};
							
					if(node_status.length() == 0) {			//������:���� �Է½�
						out.println("<select name='node_mgr'>");
						for(int i=0; i<man_cnt; i++) {
							out.print("<OPTION value='"+member[i][1]+"|"+member[i][2]+"'>");	//���|�̸�
							out.print(member[i][2] + " [");			//�̸�
							for(int g=0; g<grade_no.length; g++) {	//��å:������
								if(member[i][0].equals(grade_no[g]))out.print(grade_name[g]+" : ");
							}
							out.println(member[i][3] + "]" +"</OPTION>");
						}
						out.println("</select>");
					} else if(node_status.equals("0")){		//������:�������� ������
						String sel = "";
						out.println("<select name='node_mgr'>");
						for(int i=0; i<man_cnt; i++) {
							if(user_id.equals(member[i][1])) sel = "selected";	//���
							else sel = "";
							out.print("<OPTION "+sel+" value='"+member[i][1]+"|"+member[i][2]+"'>");//���|�̸�
							out.print(member[i][2] + " [");			//�̸�
							for(int g=0; g<grade_no.length; g++) {	//��å:������
								if(member[i][0].equals(grade_no[g]))out.print(grade_name[g]+" : ");
							}
							out.println(member[i][3] + "]" +"</OPTION>");
						}
						out.println("</select>");
					} else {									//������:��������
						out.print(user_name+" [");					//�̸�
						for(int i=0; i<man_cnt; i++) {
							if(user_id.equals(member[i][1])) {	//����� ������
								for(int g=0; g<grade_no.length; g++) {	//��å:������
									if(member[i][0].equals(grade_no[g]))out.print(grade_name[g]+" : ");
								}
								out.println(member[i][3] + "]");
							}
						}
						out.println("<input type='hidden' name='node_mgr' value='"+user_id+"|"+user_name+"'>");
					}
				%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���߸��</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
			   <%
					if(node_status.length() == 0) {			//������:���� �Է½�
						out.println("<textarea rows=2 name='pjt_node_mbr' value='' readonly></textarea>");
						out.println("&nbsp;<a href=\"Javascript:searchMan('pjt_node_mbr');\"><img src='../pjt/images/bt_search2.gif' border='0' valign='absbottom'></a>");
					} else if(node_status.equals("0")){		//������:�������� ������
						out.println("<textarea rows=2 name='pjt_node_mbr' value='"+pjt_node_mbr+"' readonly>"+pjt_node_mbr+"</textarea>");
						out.println("&nbsp;<a href=\"Javascript:searchMan('pjt_node_mbr');\"><img src='../pjt/images/bt_search2.gif' border='0' valign='absbottom'></a>");
					} else {									//������:��������
						out.println("<textarea rows=2 name='pjt_node_mbr' value='"+pjt_node_mbr+"' readonly>"+pjt_node_mbr+"</textarea>");
					}
			%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��ȹ�Ⱓ</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
			   <% if(pjt_status.equals("S") || pjt_status.equals("0")){	//��������:�̵��,������	%>
					<input type='text' name='plan_start_date' value='<%=dpsd%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('plan_start_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A>&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='plan_end_date' value='<%=dped%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('plan_end_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A>
				<% } else {								//��������:���������� %>
					<input type='text' name='plan_start_date' value='<%=dpsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='plan_end_date' value='<%=dped%>' size=10 readonly>
				<% } %>
				&nbsp;&nbsp;&nbsp;&nbsp;[���������� ��ȹ�Ⱓ�� �Է��մϴ�.]</td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
			   <% if(node_status.equals("")){						//������:�̵��	%>
					<input type='text' name='chg_start_date' value='' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='' size=10 readonly>
				<% } else if(node_status.equals("0")){				//������:��������	%>
					<input type='text' name='chg_start_date' value='<%=dcsd%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_start_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='<%=dced%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_end_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A>
				<% } else if(node_status.equals("2")){		//������:���Ϸ�	%>
					<input type='text' name='chg_start_date' value='<%=dcsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='<%=dced%>' size=10 readonly>
				<% } else {									//������:������(1),�� �׿� %>	
					<input type='text' name='chg_start_date' value='<%=dcsd%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_start_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='<%=dced%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_end_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A>
				<% } %>
				&nbsp;&nbsp;&nbsp;&nbsp;[������� ���� ��ȹ�Ⱓ ����� �����Ⱓ�� �Է��մϴ�.] </td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<input type='text' name='rst_start_date' value='<%=drsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='rst_end_date' value='<%=dred%>' size=10 readonly>
					&nbsp;&nbsp;&nbsp;&nbsp;[�����������Ҷ� ������������ ��ϵǸ� �Ϸ����� ����ڰ� �Է��մϴ�.]</td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<% if(!node_status.equals("2")){				//������:���Ϸᰡ�ƴ�	%>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��������<br>�� ��</td>
				<td width="90%" height="25" class="bg_04" colspan=3>
					<textarea rows=3 cols=60 name='chg_note' value=''><%=chg_note%></textarea>
				</td>
				</tr>
			<% } %>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='user_id' value=''>
<input type='hidden' name='user_name' value=''>
<input type='hidden' name='pjt_code' value='<%=pjt_code%>'>
<input type='hidden' name='pjtWord' value='<%=pjtWord%>'>
<input type='hidden' name='sItem' value='pjt_name'>
<input type='hidden' name='sWord' value='<%=sWord%>'>
<input type='hidden' name='parent_node' value='<%=parent_node%>'>
<input type='hidden' name='child_node' value='<%=child_node%>'>
<input type='hidden' name='node_status' value='<%=node_status%>'>
<input type='hidden' name='pjt_name' value='<%=pjt_name%>'>
<input type='hidden' name='node_name' value='<%=node_name%>'>
</form>

<!-- ����Ʈ -->
<TABLE  cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> �������� �̷�����</TD>
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
				<TD align=left width='400'>
					<form name="sForm" method="post" style="margin:0">
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"chg_note"};
						String[] snames = {"�������"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(sItem.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=sWord%>">
					<a href='Javascript:goSearch();'><img src='../pjt/images/bt_search3.gif' border='0' align='absmiddle'></a>

					<input type='hidden' name='mode' value=''>
					<input type='hidden' name='page' value=''>
					<input type='hidden' name='pid' value='<%=pid%>'>
					<input type='hidden' name='user_id' value=''>
					<input type='hidden' name='user_name' value=''>
					<input type='hidden' name='pjt_code' value='<%=pjt_code%>'>
					<input type='hidden' name='pjtWord' value='<%=pjtWord%>'>
					<input type='hidden' name='sItem' value='<%=sItem%>'>
					<input type='hidden' name='sWord' value='<%=sWord%>'>
					<input type='hidden' name='parent_node' value='<%=parent_node%>'>
					<input type='hidden' name='child_node' value='<%=child_node%>'>
					<input type='hidden' name='node_status' value='<%=node_status%>'>
					<input type='hidden' name='chg_note' value='<%=chg_note%>'>
					<input type='hidden' name='pjt_name' value='<%=pjt_name%>'>
					<input type='hidden' name='node_name' value='<%=node_name%>'>
					</form>
				</TD>
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
				<TD noWrap width=60 align=middle class='list_title'>�ۼ���</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>�ۼ���</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>�������</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>View</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
		<% if (chg_list.size() == 0) { %>
			<TR vAlign=center height=22>
				 <td colspan='11' align="middle">***** ������ �����ϴ�. ****</td>
			</tr> 
		<% } %>	

		<% 
			String note = "";			//�������泻��
			for(int i=0; i<chg_h_cnt; i++) {
				 note = change[i][8];
				 if(note.length() > 50) note = note.substring(0,50) + "....";
		%>
			<form name="aForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=change[i][6]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=change[i][7]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'>&nbsp;<%=note%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=change[i][9]%> <%=change[i][10]%></TD>
			</TR>
			<TR><TD colSpan=11 background="../pjt/images/dot_line.gif"></TD></TR>
		<% 
			}  //while 

		%>
			<input type='hidden' name='mode' value=''>
			<input type='hidden' name='page' value=''>
			<input type='hidden' name='pid' value='<%=pid%>'>
			<input type='hidden' name='user_id' value=''>
			<input type='hidden' name='user_name' value=''>
			<input type='hidden' name='pjt_code' value='<%=pjt_code%>'>
			<input type='hidden' name='pjtWord' value='<%=pjtWord%>'>
			<input type='hidden' name='sItem' value='<%=sItem%>'>
			<input type='hidden' name='sWord' value='<%=sWord%>'>
			<input type='hidden' name='parent_node' value='<%=parent_node%>'>
			<input type='hidden' name='child_node' value='<%=child_node%>'>
			<input type='hidden' name='node_status' value='<%=node_status%>'>
			<input type='hidden' name='chg_note' value='<%=chg_note%>'>
			<input type='hidden' name='pjt_name' value='<%=pjt_name%>'>
			<input type='hidden' name='node_name' value='<%=node_name%>'>
			</form> 
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">������ ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.</marquee>
	</td> 
	</tr>
</table>
</div>

</body>
</html>
