<%@ include file="../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage = "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.am.entity.*,com.anbtech.util.normalFormat,java.text.NumberFormat"%>
<%!
	com.anbtech.am.entity.AsInfoTable asinfotable;
	com.anbtech.am.entity.AMLinkTable amLinkTable;
%>

<%
	String sb=(String)request.getAttribute("CategoryList");
	String c_no=request.getParameter("c_no")==null?"0":request.getParameter("c_no");
	String login=request.getParameter("login_id")==null?"":request.getParameter("login_id");
	
	//java.anbtech.util.normalFormat app = new java.anbtech.util.normalFormat("0,000");
	NumberFormat nf = NumberFormat.getInstance();

	//����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("assetList");
	asinfotable = new com.anbtech.am.entity.AsInfoTable();
	Iterator table_iter = table_list.iterator();

	//��ũ ���ڿ� ��������
	amLinkTable = new AMLinkTable();
	amLinkTable = (AMLinkTable)request.getAttribute("Redirect");
	String view_total = ""+amLinkTable.getViewTotal();
	String view_boardpage = amLinkTable.getViewBoardpage();
	String view_totalpage = ""+amLinkTable.getViewtotalpage();
	String view_pagecut = amLinkTable.getViewPagecut();
	String category_full = amLinkTable.getWhereCategory();
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../am/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- Ÿ��Ʋ �� ������ ���� -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../am/images/blet.gif" align="absmiddle"> �ڻ�˻�</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../am/images/setup_total.gif" border="0" align="absmiddle"><%=view_total%> <img src="../am/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../am/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top><!--��ư �� ����¡-->
		<form method="get" name="asForm" style="margin:0" action='../servlet/AssetServlet' 
				  onSubmit="if( !document.asForm.searchscope.value=='' && document.asForm.searchword.value=='') {
								alert('�˻�� ������ �ֽʽÿ�');
								return false;
							} else if(document.asForm.searchscope.value=='' && !document.asForm.searchword.value==''){
								alert('�˻� �׸��� ������ �ֽʽÿ�');
								return false;
							} else return true">
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY> 
			
			<TR><TD align=left width='700'>
				&nbsp;
				<SELECT name="c_no">
					<OPTION value=''>��ü</OPTION>
					<%=sb%>
				<%	if(!c_no.equals("")){	%>
						<script language='javascript'>
							document.asForm.c_no.value = "<%=c_no%>";
						</script>
				<%	}	%>
				</SELECT>
				<SELECT name='searchscope'>
					<OPTION value="" selected>�˻��׸�</OPTION>
					<OPTION value="as_mid">�ڻ��ȣ</OPTION>
					<OPTION value="model_name">�𵨸�</OPTION>
					<OPTION value="as_serial">������ȣ</OPTION>
					<OPTION value="crr_name">�����</OPTION>
					<OPTION value="crr_rank">�μ���</OPTION>
					<OPTION value="as_mid">ī�װ�����ڵ�</OPTION>
				</SELECT>
				<input type='text' size=10 name='searchword'>
				<input type="image" onfocus=blur() src="../am/images/bt_search3.gif" border="0" align="absmiddle" >
				<a href='javascript:search_detail()'><IMG src="../am/images/bt_search_d.gif" border='0' align="absmiddle"></a>
				</TD>
				<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR>
				<input type='hidden' name=mode value='user_asset_list'>
				<input type='hidden' name=div  value='general'>
	</form></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--����Ʈ-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=2><TD colspan=13></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=100 align=middle class='list_title'>ǰ��</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>�ڻ��ȣ</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>�𵨸�</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>�������</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>��������</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>�ڻ갡ġ</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=70 align=middle class='list_title'>���</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
		<%
				  int     as_no=0;			// ������ȣ
				  String  as_mid="";		// �ڻ��ȣ
				  String  ct_id="";			//ī�װ�ID
				  //String  c_no="";
				  String  as_item_no="";	//�ڻ����no
				  String  w_id="";
				  String  w_name="";
				  String  w_rank="";
				  String  b_id="";			//�ڻ�ǰ������ID
				  String  b_name="";		//�ڻ�ǰ�������̸�
				  String  b_rank="";		//�ڻ�ǰ�μ���
				  String  model_name="";	//�𵨸�
				  String  as_name="";		//�ڻ�ǰ���̸�
				  String  as_serial="";		//�ڻ�ǰ�ø���
				  String  buy_date="";		//��������
				  String  as_price="";		//ǰ�� ����
				  String  dc_date="";		//�������ѳ⵵
				  String  dc_bound="";		//��������Ƚ��
				  String  as_each_dc="";	//ǰ�� ���� ����
				  String  as_value="";		//�ڻ갡��
				  String  crr_id="";		//���� ����� ID
				  String  crr_name="";		//���� ����� �̸�
				  String  crr_rank="";		//���� ����� �μ�
				  String  buy_where="";		//������(ȸ��)
				  String  as_maker="";		//�ڻ�ǰ�����(ȸ��)
				  String  as_setting="";	//�԰�(���)
				  String  bw_tel="";		//����ȸ�翬��ó
				  String  bw_address="";	//����ȸ���ּ�
				  String  bw_employee="";	//����ȸ������
				  String  bw_mgr_tel="";	//����ȸ�����ڿ���ó
				  String  etc="";			//��Ÿ
				  String  as_status="";		//�ڻ����(���,�����,����������)
				  String  as_status_name="";
				  String  as_except_day="";	//�ڻ� ��� ����
				  String  as_except_reason="";// �ڻ� ��� ����
				  String  handle ="";
				  String  now_status ="";
				  String  u_id = "";
				  String  u_name = "";

			while(table_iter.hasNext()){
				asinfotable = (com.anbtech.am.entity.AsInfoTable)table_iter.next();

					as_no		= asinfotable.getAsNo();			
					as_mid		= asinfotable.getAsMid();	
					c_no		= asinfotable.getCno();
					as_item_no	= asinfotable.getAsItemNo();	
					model_name	= asinfotable.getModelName();	
					as_name		= asinfotable.getAsName();		
					as_serial	= asinfotable.getAsSerial();		
					buy_date	= asinfotable.getBuyDate();		
					//as_price	= asinfotable.getAsPrice();		
					//dc_date	= asinfotable.getDcDate();		
					dc_bound	= asinfotable.getDcBound();		
					as_each_dc	= asinfotable.getAsEachDc();	
					as_value	= asinfotable.getAsValue();	
					crr_id		= asinfotable.getCrrId();
					crr_name	= asinfotable.getCrrName();		
					u_id		= asinfotable.getUid();
					u_name		= asinfotable.getUname();
					//crr_rank	= asinfotable.getCrrRank();		
					buy_where	= asinfotable.getBuyWhere();		
					as_maker	= asinfotable.getAsMaker();		
					//as_setting= asinfotable.getAsSetting();	
					bw_tel		= asinfotable.getBwTel();		
					bw_address	= asinfotable.getBwAddress();	
					bw_employee	= asinfotable.getBwEmployee();	
					bw_mgr_tel	= asinfotable.getBwMgrTel();	
					//etc		= asinfotable.getEtc();			
					as_status	= asinfotable.getAsStatus();		
					as_status_name=asinfotable.getAsStatusName();
					handle		= asinfotable.getHandle();
					now_status	= asinfotable.getNowStatus();
					
					if(now_status==null || now_status.equals("")) now_status="��밡��";
					if(as_status.equals("13")) now_status = as_status_name;
		%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg' height="24"><%=as_name%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=as_mid%>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=model_name%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=u_name%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=buy_date.substring(0,4)%>-<%=buy_date.substring(4,6)%>-<%=buy_date.substring(6,8)%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=right class='list_bg'><%=nf.format(nf.parse(as_value))%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><a href="javascript:go_history('<%=as_no%>','<%=c_no%>')"><img src='../am/images/lt_view_d.gif' border='0' align='absmiddle'></a></td>
						<TD><IMG height=1 width=1></TD>
					</TR>
					<TR><TD colspan=13 background="../am/images/dot_line.gif"></TD></TR>
		<input type='hidden' name="c_no" value=<%=c_no%>>
		<%
			}
		%>
				</TBODY></TABLE></TD></TR>

</TBODY></TABLE>
</body>
</html>

<script language='javascript'>

	var f = document.asForm;

	//�󼼺���
	function go_asInfo(as_no,c_no){
		location.href ="../servlet/AssetServlet?mode=asset_form&div=view&as_no="+as_no+"&c_no="+c_no;
	}

	// �̷º���
	function go_history(as_no,c_no){
		location.href ="../servlet/AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no;
	}

	// ����/�̰�/�뿩 Form���� �̵�
	function go_form(as_no,status){
		location.href = "../servlet/AssetServlet?mode=user_moving_req&as_no="+as_no+"&o_status="+status;
	}

	function wopen(url, t, w, h,st) {
		var sw;
		var sh;
		sw = (screen.Width - w) / 2;
		sh = (screen.Height - h) / 2 - 50;

		window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
	}

	//�󼼰˻�
	function search_detail() {
				
	var c_no = f.c_no.value;
		wopen("../am/as_user/search.jsp?category_full=<%=category_full%>&c_no="+c_no,'search','515','285','scrollbars=no,toolbar=no,status=no,resizable=no');
	}
</script>

