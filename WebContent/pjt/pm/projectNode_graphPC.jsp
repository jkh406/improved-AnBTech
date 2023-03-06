<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�����߰��� Gantt Chart[������/������]����"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();
	String psd="",ped="",csd="",ced="",rsd="",red="";

	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String pjt_code="",pjtWord="",sItem="",sWord="",parent_node="",child_node="";
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
	}

	//-----------------------------------
	//	������������ �����ϱ�
	//-----------------------------------
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

	//�ش������ ���� ��ü LIST
	com.anbtech.pjt.entity.projectTable sch;
	ArrayList sch_list = new ArrayList();
	sch_list = (ArrayList)request.getAttribute("SCH_List");
	sch = new projectTable();
	Iterator sch_iter = sch_list.iterator();
	int sch_cnt = sch_list.size();

	String[][] schedule = new String[sch_cnt][10];
	int m = 0;
	while(sch_iter.hasNext()) {
		sch = (projectTable)sch_iter.next();
		
		schedule[m][0] = sch.getNodeName();							//����
		schedule[m][1] = sch.getPlanStartDate();					//��ȹ������
		schedule[m][2] = sch.getPlanEndDate();						//��ȹ������

		csd = sch.getChgStartDate();	
		if(csd.length() != 0) schedule[m][1] = csd;					//���������� ��
		ced = sch.getChgEndDate();
		if(ced.length() != 0) schedule[m][2] = ced;					//���������� ��
 
		schedule[m][3] = Double.toString(sch.getProgress()*100);	//������(%)

		schedule[m][4] = Integer.toString(sch.getPlanCnt());		//��ȹ�ϼ�
		if(csd.length() != 0) schedule[m][4] = Integer.toString(sch.getChgCnt());//�����ϼ� ��

		schedule[m][5] = schedule[m][1];							//����������(��ȹ�����ϰ� �����ϰ�)

		//���������ϼ�(�������� ���)
		if(sch.getProgress() != 1.0) {		//100%
			String ps = Double.toString(sch.getProgress() * Integer.parseInt(schedule[m][4]));	
			ps = ps.substring(0,ps.indexOf("."));
			int dps = Integer.parseInt(ps);
			schedule[m][6] = anbdt.getDate(schedule[m][1],dps,"");

			//�ϼ��� �ʹ� ������ �������ڰ� ����� �־� ����
			if(schedule[m][2].equals(schedule[m][6])) {
				schedule[m][6] = anbdt.getDate(schedule[m][2],-1,"");	//-1�� "":�����������
			}
		} else {
			schedule[m][6] = schedule[m][2];
		}

		schedule[m][7] = sch.getLevelNo();							//��� Level No
		schedule[m][8] = sch.getChgStartDate();						//�������� ��
		schedule[m][9] = sch.getChgEndDate();						//�������� ��
		m++;
	}

	//��ü �������� ���� ���ϱ�
	int pn_cnt = Integer.parseInt(schedule[0][4]);	

	//������ü ������ ���ϱ�
	String pjt_progress = schedule[0][3];
	pjt_progress = Double.toString(Double.parseDouble(pjt_progress)+0.001);
	pjt_progress = pjt_progress.substring(0,pjt_progress.indexOf('.')+2);

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet>
<script language=javascript>
<!--
//�˻��ϱ�
function goSearch()
{
	var v = document.gForm.v.value;

	document.sForm.action='../servlet/projectNodeAppServlet';
	if(v == 'ganttPP') document.sForm.mode.value='PSN_GPP';			//gantt chart ���� : ������/��ȹ��
	else if(v == 'ganttPC') document.sForm.mode.value='PSN_GPC';	//gantt chart ���� : ������/������
	else if(v == 'ganttSP') document.sForm.mode.value='PSN_GSP';	//gantt chart ���� : ������/��ȹ��
	else if(v == 'ganttSC') document.sForm.mode.value='PSN_GSC';	//gantt chart ���� : ������/������
	else document.sForm.mode.value='PSN_L';							//text�� ����

	document.sForm.submit();
}
//���������ϱ�
function goProject()
{
	var sItem = '<%=sItem%>';
	var sWord = '<%=sWord%>';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var v = document.gForm.v.value;
	
	document.eForm.action='../servlet/projectNodeAppServlet';
	if(v == 'ganttPP') document.eForm.mode.value='PSN_GPP';			//gantt chart ���� : ������/��ȹ��
	else if(v == 'ganttPC') document.eForm.mode.value='PSN_GPC';	//gantt chart ���� : ������/������
	else if(v == 'ganttSP') document.eForm.mode.value='PSN_GSP';	//gantt chart ���� : ������/��ȹ��
	else if(v == 'ganttSC') document.eForm.mode.value='PSN_GSC';	//gantt chart ���� : ������/������
	else document.eForm.mode.value='PSN_L';							//text�� ����

	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.submit();
}
//���� ���⼱���ϱ�
function goView()
{
	var sItem = '<%=sItem%>';
	var sWord = '<%=sWord%>';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var v = document.gForm.v.value;

	document.eForm.action='../servlet/projectNodeAppServlet';
	if(v == 'ganttPP') document.eForm.mode.value='PSN_GPP';			//gantt chart ���� : ������/��ȹ��
	else if(v == 'ganttPC') document.eForm.mode.value='PSN_GPC';	//gantt chart ���� : ������/������
	else if(v == 'ganttSP') document.eForm.mode.value='PSN_GSP';	//gantt chart ���� : ������/��ȹ��
	else if(v == 'ganttSC') document.eForm.mode.value='PSN_GSC';	//gantt chart ���� : ������/������
	else document.eForm.mode.value='PSN_L';							//text�� ����

	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.submit();
}
//�ش��� �̷º���
function detailNode(pjt_code,parent_node,child_node)
{
	document.vForm.action='../servlet/projectNodeAppServlet';
	document.vForm.mode.value='PSN_AV';
	document.vForm.pjt_code.value=pjt_code;
	document.vForm.parent_node.value=parent_node;
	document.vForm.child_node.value=child_node;
	document.vForm.submit();
}
//ǳ�� ���� ����
var select_obj;
function ANB_layerAction(obj,status) 
{ 
	var _tmpx,_tmpy, marginx, marginy;
		_tmpx = event.clientX + parseInt(obj.offsetWidth);
		_tmpy = event.clientY + parseInt(obj.offsetHeight);
		_marginx = document.body.clientWidth - _tmpx;
		_marginy = document.body.clientHeight - _tmpy ;
	if(_marginx < 0)
		_tmpx = event.clientX + document.body.scrollLeft + _marginx ;
	else
		_tmpx = event.clientX + document.body.scrollLeft ;
	if(_marginy < 0)
		_tmpy = event.clientY + document.body.scrollTop + _marginy +20;
	else
		_tmpy = event.clientY + document.body.scrollTop ;

	obj.style.posLeft=_tmpx-13;
	obj.style.posTop=_tmpy+20;

	if(status=='visible') {
		if(select_obj) {
			select_obj.style.visibility='hidden';
			select_obj=null;
		}
		select_obj=obj;
	}else{
		select_obj=null;
	}
	obj.style.visibility=status; 
}
-->
</script>
</HEAD>

<BODY topmargin="0" link="blue" alink="blue" vlink="blue" leftmargin="0">

<!-- ��� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> ������������</TD>
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
				<TD align=left width='280'>
					<form name="sForm" method="post" style="margin:0">
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"pjt_name","pjt_code"};
						String[] snames = {"�����̸�","�����ڵ�"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(sItem.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=sWord%>">
					<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pid" size="15" value="">
					<a href='Javascript:goSearch();'><img src='../pjt/images/bt_search3.gif' border='0' align='absmiddle'></a>
					</form>
				</TD>
				<TD align=left width=''>
					<form name="eForm" method="post" style="margin:0">�� �� ��
					<%
						out.println("<select name='project' onChange='javascript:goProject()' style=font-size:9pt;color='black';>");
						String psel = "";
						for(int i=0; i<pjt_cnt; i++) {
							if(pjt_code.equals(project[i][0])) psel = "selected";
							else psel = "";
							out.print("<option "+psel+" value='"+project[i][0]+"|"+project[i][1]+"'>");
							out.println(project[i][0]+" "+project[i][1]+"</option>");
						}
					%>
					<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
					<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
					<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pjt_code" size="15" value="">
					<input type="hidden" name="pjt_name" size="15" value="">
					<input type="hidden" name="level_no" size="15" value="0">
					<input type="hidden" name="parent_node" size="15" value="0">
					<input type="hidden" name="pid" size="15" value="">
					</form>
				</TD>
				<TD align=left width=''>
					<form name="gForm" method="post" style="margin:0">
					<select name='v' onChange='javascript:goView()' style=font-size:9pt;color='black';>
						<option value='text'>TEXT</option>
						<option value='ganttPP'>�׷���[������/��ȹ��]</option>
						<option selected value='ganttPC'>�׷���[������/������]</option>
						<option value='ganttSP'>�׷���[������/��ȹ��]</option>
						<option value='ganttSC'>�׷���[������/������]</option>
					</select>
					</form>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	</TR>
	</TBODY>
</TABLE>

<!-- LIST ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 valign='top'>
	<TR vAlign=center bgColor=#E6F5FF height=22>
		<TD noWrap width=300 align=middle>
			�����ܰ� / ����
		</TD>
		<TD noWrap width=100% background="../pjt/img/list_bg.gif">
			<% //----- ���� LIST -----// 
			out.println("<TABLE cellSpacing=0 cellPadding=0 width='100%' border=1 valign='top'>");
			//���� ����
			String ymd = "",tag="1",pjt_sd="",pjt_ed="";
			int cy=0,cm=0,cd=0,colspan=0;	//��,��,��,colspan

			//--------------------------
			//phaseǥ���ϱ�
			//--------------------------
			out.println("<TR vAlign=center bgColor=#E6F5FF height=22>");
			pjt_sd = schedule[0][1];									//��Phase�� ��ȹ���� �����
			for(int i=0; i<sch_cnt; i++) {
				if(schedule[i][7].equals("1")) {
					pjt_ed = schedule[i][2];							//��Phase�� ��ȹ���� �����
					colspan = anbdt.getPeriodDate(pjt_sd,pjt_ed);		//�����ϼ� ���ϱ�
					out.print("<TD noWrap align=center colspan='"+colspan+"'  background='../pjt/img/list_bg.gif'>"+schedule[i][0]+"</TD>");
					pjt_sd = pjt_ed;									//��phase�� ����phase�� �����Ϸ�
				}
			}
			out.println("</TR>");

			//--------------------------
			//��� ǥ���ϱ�
			//--------------------------
			int yy = Integer.parseInt(schedule[0][1].substring(0,4));	//��ȹ(����)���� �⵵
			int mm = Integer.parseInt(schedule[0][1].substring(4,6));	//��ȹ(����)���� ��
			int dd = Integer.parseInt(schedule[0][1].substring(6,8));	//��ȹ(����)���� ��
			int lyy = Integer.parseInt(schedule[0][2].substring(0,4));	//��ȹ(����)���� �⵵
			int lmm = Integer.parseInt(schedule[0][2].substring(4,6));	//��ȹ(����)���� ��
			int ldd = Integer.parseInt(schedule[0][2].substring(6,8));	//��ȹ(����)���� �� (�������� �ϼ���)
			out.println("<TR vAlign=center bgColor=#E6F5FF height=22>");
			for(int i=0; i<pn_cnt; i++) {
				ymd = anbdt.getDate(yy,mm,dd,i);					//��/��/��
				cy = Integer.parseInt(ymd.substring(0,4));			//��
				cm = Integer.parseInt(ymd.substring(5,7));			//��
				cd = Integer.parseInt(ymd.substring(8,10));			//��
				colspan = anbdt.getDatePeriod(cy,cm,cd);			//�����ϼ� ���ϱ�(ù��,�߰��� ����)
				if((cy == lyy) && (cm == lmm)) colspan = ldd;		//�������� colspan

				if(tag.equals("1")) {	//ó�� ������ ���
					if(colspan > 15) {	//��� ǥ��
						out.print("<TD noWrap align=center colspan='"+colspan+"'  background='../pjt/img/list_bg.gif'>"+cy+"��"+cm+"��</TD>");
					} else {			//��� ǥ�� ����
						out.print("<TD noWrap align=center colspan='"+colspan+"'  background='../pjt/img/list_bg.gif'>&nbsp;</TD>");
					}
					tag = "0";
				} else if(cd == 1) {	//�� ù�޽����� 15���̸� ���
					if(colspan > 15) {	//��� ǥ��
						out.print("<TD noWrap align=center colspan='"+colspan+"'  background='../pjt/img/list_bg.gif'>"+cy+"��"+cm+"��</TD>");
					} else {			//��� ǥ�� ����
						out.print("<TD noWrap align=center colspan='"+colspan+"'  background='../pjt/img/list_bg.gif'>&nbsp;</TD>");
					}
				}
			}
			out.println("</TR>");

			//--------------------------
			//�Ʒ��� ����ǥ�� (���ڴ���)
			//--------------------------
			out.println("<TR vAlign=center bgColor=#E6F5FF height=5>");
			for(int i=0; i<pn_cnt; i++) {
				//ymd = anbdt.getDate(yy,mm,dd,i);					//��/��/��
				//cm = Integer.parseInt(ymd.substring(5,7));			//��
				out.println("<TD noWrap width=5 height=8 align=right bgcolor='darkgray'></TD>");
			}
			out.println("</TR>");
		
			out.println("</TABLE>");
			%>
		</TD>
	</TR>
	<TR vAlign=center bgColor=#E6F5FF height=22>
		<TD noWrap width=300 align=middle background="../pjt/img/list_bg.gif">
			<% //----- PROCESS LIST -----// 
			String progress = "",space="";
			for(int i=1; i<sch_cnt; i++) {
				//phase,step,activity�����ϱ�
				if(schedule[i][7].equals("1")) {
					space="<IMG src='../pjt/icons/base.gif'>";
				} else if(schedule[i][7].equals("2")) {
					space="&nbsp;&nbsp;<IMG src='../pjt/icons/folder.gif'>";
				} else if(schedule[i][7].equals("3")) {
					space = "&nbsp;&nbsp;&nbsp;&nbsp;<IMG src='../pjt/icons/page.gif'>";
				}

				//������ �Ҽ��� ����
				progress = schedule[i][3];
				progress = Double.toString(Double.parseDouble(progress)+0.001);
				progress = progress.substring(0,progress.indexOf('.')+2);

				out.println("<TABLE cellSpacing=0 cellPadding=0 width='100%' border=0 valign='top'>");
				out.println("	<TR vAlign=center bgColor=#E6F5FF height=22>");
				out.println("		<TD noWrap width=100% align=left rowspan=2  background='../pjt/img/list_bg.gif'>"+space+schedule[i][0]+"["+progress+"%]&nbsp;&nbsp;</TD>");
				out.println("	</TR>");
				out.println("	<TR vAlign=center height=2><TD noWrap width=100% ></TD></TR>");
				out.println("</TABLE>");
			}
			%>
		</TD>
		<TD noWrap width=100% background="../pjt/img/list_bg.gif">
			<% //----- ������(��ȹ/����) LIST -----// 
			String pdate = "";
			for(int r=1; r<sch_cnt; r++) {
				progress = schedule[r][3];
				progress = Double.toString(Double.parseDouble(progress)+0.001);
				progress = progress.substring(0,progress.indexOf('.')+2);
				out.println("<TABLE cellSpacing=0 cellPadding=0 width='100%' border=0 valign='top'>");

				//--------------------------
				//��ȹ ǥ���ϱ�
				//--------------------------
				out.println("	<TR vAlign=center bgColor=#E6F5FF height=22>");
				int py = Integer.parseInt(schedule[0][1].substring(0,4)); //�⵵
				int pm = Integer.parseInt(schedule[0][1].substring(4,6)); //��
				int pd = Integer.parseInt(schedule[0][1].substring(6,8)); //��
				String con = "0",pjsd="",pjed="",pcsd="",pced="";	//���ǥ��,��ȹ������,������,��������,������
				for(int i=0; i<pn_cnt; i++) {
					pdate = str.repWord(anbdt.getDate(py,pm,pd,i),"/",""); 
					//������
					if(pdate.equals(schedule[r][1])) {if(con.equals("0")) con = "1"; else con = "0"; }
					
					//ǳ�����򸻿�
					pjsd = anbdt.getSepDate(schedule[r][1],"/");	//��ȹ������
					pjed = anbdt.getSepDate(schedule[r][2],"/");	//��ȹ������ 	
					if(schedule[r][8].length() == 8) pcsd=anbdt.getSepDate(schedule[r][8],"/");//����������
					if(schedule[r][9].length() == 8) pced=anbdt.getSepDate(schedule[r][9],"/");//����������

					//ǥ���ϱ�
					if(con.equals("1")) {
						out.print("<TD noWrap width=5 height=10 align=left bgcolor='479933' ");
						out.print("onmouseover=\"ANB_layerAction(z"+r+i+", 'visible')\" onmouseout=\"ANB_layerAction(z"+r+i+", 'hidden')\">");
						out.print("<div id=z"+r+i+" style=\"position:absolute;background-color:#FEFEED;width:250;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '����ü';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>["+schedule[r][0]+"]</font><br>��ȹ�Ⱓ:"+pjsd+" ~ "+pjed+"<br>�����Ⱓ:"+pcsd+" ~ "+pced+"<br>������:"+progress+"%</div>");
						out.println("</TD>");
					} 
					else if(pdate.equals(anbdt.getDateNoformat())) {		//����ǥ��
						out.println("<TD noWrap width=5 height=10 align=right bgcolor='ff9999'></TD>");
					}
					else {
						out.println("<TD noWrap width=5 height=10 align=right bgcolor='white'></TD>");
					}

					//������
					if(pdate.equals(schedule[r][2])) { if(con.equals("0")) con = "1"; else con = "0";}
				}
				out.println("	</TR>");

				//--------------------------
				//���ǥ���ϱ� (������)
				//--------------------------
				out.println("	<TR vAlign=center bgColor=#E6F5FF height=22>");
				for(int i=0; i<pn_cnt; i++) {
					pdate = str.repWord(anbdt.getDate(py,pm,pd,i),"/",""); 
					//������
					if(pdate.equals(schedule[r][5])) {if(con.equals("0")) con = "1"; else con = "0"; }

					//ǳ�����򸻿�
					pjsd = anbdt.getSepDate(schedule[r][1],"/");	//��ȹ������
					pjed = anbdt.getSepDate(schedule[r][2],"/");	//��ȹ������ 	
					if(schedule[r][8].length() == 8) pcsd=anbdt.getSepDate(schedule[r][8],"/");//����������
					if(schedule[r][9].length() == 8) pced=anbdt.getSepDate(schedule[r][9],"/");//����������


					//�������� ����
					if(schedule[r][3].equals("0.0")) con = "0";

					//ǥ���ϱ�
					if(con.equals("1")) {
						out.print("<TD noWrap width=5 height=10 align=left bgcolor='blue' ");
						out.print("onmouseover=\"ANB_layerAction(z0"+r+i+", 'visible')\" onmouseout=\"ANB_layerAction(z0"+r+i+", 'hidden')\">");
						out.print("<div id=z0"+r+i+" style=\"position:absolute;background-color:#FEFEED;width:250;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '����ü';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>["+schedule[r][0]+"]</font><br>��ȹ�Ⱓ:"+pjsd+" ~ "+pjed+"<br>�����Ⱓ:"+pcsd+" ~ "+pced+"<br>������:"+progress+"%</div>");
						out.println("</TD>");
					} 
					else if(pdate.equals(anbdt.getDateNoformat())) {		//����ǥ��
						out.println("<TD noWrap width=5 height=10 align=right bgcolor='ff9999'></TD>");
					}
					else {
						out.println("<TD noWrap width=5 height=10 align=right bgcolor='white'></TD>");
					}

					//������
					if(pdate.equals(schedule[r][6])) { if(con.equals("0")) con = "1"; else con = "0"; }
				}
				out.println("	</TR>");
				out.println("	<TR vAlign=center height=2><TD noWrap width=100% ></TD></TR>");
				out.println("</TABLE>");
			}
			%>
		</TD>
	</TR>
</TABLE>

</body>
</html>