<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������ǰ ��� LIST"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");	//����
	
	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";
	
	//--------------------------------------
	//���������� ������ ������ ��������
	//--------------------------------------
	com.anbtech.mm.entity.mfgProductMasterTable master = new com.anbtech.mm.entity.mfgProductMasterTable();
	master = (mfgProductMasterTable)request.getAttribute("PRODUCT_master");
	String mfg_no = master.getMfgNo();
	String item_code = master.getItemCode();
	String item_spec = master.getItemSpec();
	String order_count = Integer.toString(master.getOrderCount());
	String total_count = Integer.toString(master.getTotalCount());
	String good_count = Integer.toString(master.getGoodCount());
	String bad_count = Integer.toString(master.getBadCount());
	String output_status = master.getOutputStatus();
	String factory_no = master.getFactoryNo();
	
	//--------------------------------------
	//���������� ����Ʈ ��������
	//--------------------------------------
	com.anbtech.mm.entity.mfgProductItemTable item = new com.anbtech.mm.entity.mfgProductItemTable();
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("PRODUCT_ITEM_List");
	Iterator item_iter = item_list.iterator();

%>

<HTML><HEAD><TITLE>������ǰ ��� LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display();'>
<form name="sForm" method="post" style="margin:0">

<!--��� TITLE-->
<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%" >
<TBODY>
<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
<TR bgcolor="#BAC2CD"><TD valign='middle' class="title" style='padding-left:5px'><IMG src="../mm/images/blet.gif" align="absmiddle"> ������� ��Ȳ����</TD></TR></TBODY></TABLE>

<!---�ܰ� Line--->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height='92%' border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD valign='top'>

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=25>
			<TD vAlign=center style='padding-left:5px' class='bg_05'  background='../bm/images/title_bm_bg.gif'>������� ��Ȳ</TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=27>
		<TD vAlign=top>
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD width='10%' align=left style='padding-left:5px;'>
					<a href="javascript:sendConfirm();"><IMG src="../mm/images/bt_product_end.gif" align="absmiddle" border='0' align='absmiddle' alt='���긶��'></a>
				</TD>
				<TD width='90%' align=left>
					<IMG src="../mm/images/work_command_no.gif" border='0' align='absmiddle' alt='�۾����ù�ȣ'><font color='#639DE9'> <%=mfg_no%></font> &nbsp;&nbsp;
					<IMG src="../mm/images/work_product_no.gif" border='0' align='absmiddle' alt='���ǰ�ڵ�'><font color='#639DE9'> <%=item_code%></font> &nbsp;&nbsp;
					<IMG src="../mm/images/make_order_mount.gif" align="absmiddle" border='0' align='absmiddle' alt='������������'><font color='#639DE9'> <%=order_count%> </font> &nbsp;&nbsp;
					<IMG src="../mm/images/make_result_mount.gif" align="absmiddle" border='0' align='absmiddle' alt='�����������'><font color='#639DE9'> <%=total_count%> (G:<%=good_count%>/B:<%=bad_count%>)</font>&nbsp;&nbsp;
				</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--����Ʈ-->
		<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:scroll; overflow-y:auto;">	
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=2><TD colspan=12></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=90 align=middle class='list_title'>ǰ���ȣ</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>DESCRIPTION</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
					    <TD noWrap width=60 align=middle class='list_title'>�������</TD>
					    <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
					    <TD noWrap width=60 align=middle class='list_title'>��ǰ����</TD>
					    <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
					    <TD noWrap width=60 align=middle class='list_title'>�ҷ�����</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
					    <TD noWrap width=90 align=middle class='list_title'>�����</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=12></TD></TR>
<%
			while(item_iter.hasNext()){
				item = (mfgProductItemTable)item_iter.next();
%>
					<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
						
				<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
					<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
						<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
							<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
								<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR><TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
						<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR><TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
						<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
							<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
								<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
									<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
										<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
											<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
												<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR><TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR><TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>
						<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getTotalCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getGoodCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(item.getBadCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=item.getOutputDate()%></td>
					</TR>
					<TR><TD colspan=12 background="../mm/images/dot_line.gif"></TD></TR>

					
<%			}
%>				</TBODY></TABLE></DIV></TD></TR>
</TBODY></TABLE>

<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="">
<INPUT type="hidden" name="mfg_no" size="15" value="<%=mfg_no%>">
<INPUT type="hidden" name="item_code" size="15" value="<%=item_code%>">
<INPUT type="hidden" name="factory_no" size="15" value="<%=factory_no%>">
</FORM>
</TD></TR></TABLE>
</BODY>
</HTML>

<script language=javascript>
<!--
//�޽��� ����
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
}
//��ǰ��� �󼼳��뺸��
function productView(pid)
{
	var output_status = '<%=output_status%>';
	if(output_status != '1') { 
		if(output_status == '2') status = "��������";
		alert('�����ۼ� ���¿��� �����մϴ�. ���� '+status+' �����Դϴ�.'); return; 
	}

	var mfg_no = document.sForm.mfg_no.value;
	var item_code = document.sForm.item_code.value;
	var factory_no = document.sForm.factory_no.value;

	parent.reg.document.sForm.action='../servlet/mfgOrderServlet';
	parent.reg.document.sForm.mode.value='product_preview';
	parent.reg.document.sForm.pid.value=pid;
	parent.reg.document.sForm.mfg_no.value=mfg_no;
	parent.reg.document.sForm.assy_code.value=item_code;
	parent.reg.document.sForm.factory_no.value=factory_no;
	parent.reg.document.sForm.submit();
}
//���긶��
function sendConfirm()
{
	var order_count = '<%=order_count%>';
	var total_count = '<%=total_count%>';
	if(order_count != total_count){ alert('����������ŭ ����� ���긶���� �����մϴ�.'); return; }

	var output_status = '<%=output_status%>';
	if(output_status == '2') { alert('�̹� �������� �Ǿ����ϴ�.'); return; }

	var v = confirm('���긶���� �����Ͻðڽ��ϱ�?'); 
	if(v == false) { return; }

	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='product_confirm';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//������ ó���� ��ư����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}

function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // ���� �������� ũ��
	//var div_h = h - 538;
	var div_h = c_h - 86;
	item_list.style.height = div_h;
}

-->
</script>