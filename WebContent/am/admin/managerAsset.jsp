<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkAM02.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.am.entity.*,java.text.NumberFormat"%>

<%
	com.anbtech.am.entity.AsInfoTable asinfotable;
	com.anbtech.am.entity.AMLinkTable amLinkTable;
%>

<%
	String sb=(String)request.getAttribute("CategoryList");							 //  ī�װ� String ��������
	String c_no=request.getParameter("c_no")==null?"0":request.getParameter("c_no");
	
	NumberFormat nf = NumberFormat.getInstance();

	//����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("assetList");
	asinfotable = new com.anbtech.am.entity.AsInfoTable();
	Iterator table_iter = table_list.iterator();

	//-- ��ũ ���ڿ� �������� 
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
<LINK href="../am/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
	<TR height=27>
		<TD vAlign=top><!-- Ÿ��Ʋ �� ������ ���� -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../am/images/blet.gif" align="absmiddle"> �ڻ��ϰ���</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../am/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../am/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../am/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top><!--��ư �� ����¡-->
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY> 
			<form method="get" name="asForm" style="margin:0" action='../servlet/AssetServlet' 
				  onSubmit="if( !document.asForm.searchscope.value=='' && document.asForm.searchword.value=='') {
								alert('�˻�� ������ �ֽʽÿ�');
								return false;
							} else if(document.asForm.searchscope.value=='' && !document.asForm.searchword.value==''){
								alert('�˻� �׸��� ������ �ֽʽÿ�');
								return false;
							} else return true">
			<TR><TD align=left width='90%' style='padding-left:5px'>
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
				<IMG src="../am/images/bt_search3.gif" onclick ="javascript:go_search()" border='0' style='cursor:hand' align='absmiddle'>
				<IMG src="../am/images/bt_search_d.gif" onclick ="javascript:search_detail()"  border='0' style='cursor:hand' align='absmiddle'></TD>
				 <TD width='10%' align='right' style="padding-right:10px"><%=view_pagecut%></TD>
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan='3'></TD></TR>
				<TR><TD height='25' style='padding-left:5px;'>
				<a href='javascript:go_asset_form()'><IMG src="../am/images/bt_reg.gif" border='0' align='absmiddle'></a>
				<IMG src="../am/images/bt_am_pm.gif" onclick ="javascript:openwin()" border='0' style='cursor:hand' align='absmiddle'>
				<a href='javascript:go_cleanDB()'><IMG src="../am/images/bt_clean.gif" border='0' align='absmiddle' alt='CLEAN'></a>
				</TD>
			 </TR>
			  <input type='hidden' name=mode value='asset_list'>
			  <input type='hidden' name=div  value='general'>
			  </form></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--����Ʈ-->
<%
		  int     as_no		=0;			// ������ȣ
		  String  as_mid	="";		// �ڻ��ȣ
		  String  ct_id		="";			//ī�װ�ID
		  //String  c_no	="";
		  String  as_item_no="";	//�ڻ����no
		  String  w_id		="";			//�ۼ���ID
		  String  w_name	="";		//�ۼ���ID/�̸�
		  String  w_rank	="";		//�ۼ��ںμ�
		  String  b_id		="";			//�ڻ�ǰ������ID
		  String  b_name	="";		//�ڻ�ǰ�������̸�
		  String  b_rank	="";		//�ڻ�ǰ�μ���
		  String  model_name="";	//�𵨸�
		  String  as_name	="";		//�ڻ�ǰ���̸�
		  String  as_serial	="";		//�ڻ�ǰ�ø���
		  String  buy_date	="";		//��������
		  String  as_price	="";		//ǰ�� ����
		  String  dc_date	="";		//�������ѳ⵵
		 
		  String  as_each_dc="";	//ǰ�� ���� ����
		  String  as_value	="";		//�ڻ갡��
		  String  crr_id	="";		//���� ����� ID
		  String  crr_name	="";		//���� ����� �̸�
		  String  crr_rank	="";		//���� ����� �μ�
		  String  buy_where	="";		//������(ȸ��)
		  String  as_maker	="";		//�ڻ�ǰ�����(ȸ��)
		  String  as_setting="";	//�԰�(���)
		  String  bw_tel	="";		//����ȸ�翬��ó
		  String  bw_address="";	//����ȸ���ּ�
		  String  bw_employee="";	//����ȸ������
		  String  bw_mgr_tel="";	//����ȸ�����ڿ���ó
		  String  etc		="";			//��Ÿ
		  String  as_status	="";		//�ڻ����(���,�����,����������)
		  String  as_except_day="";	//�ڻ� ��� ����
		  String  as_except_reason="";	// �ڻ� ��� ����
		  String  as_status_name="";	// �ڻ����text
		  String  now_status	="";
		  String  u_name		="";
%>

	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
		<TR bgColor=#9DA9B9 height=2><TD colspan=19></TD></TR>
		<TR vAlign=middle height=23>
			  <TD noWrap width=100 align=middle class='list_title'>ǰ��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�ڻ��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>�𵨸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>��������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�ڻ갡ġ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>���</TD>

				 <!-- <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>���</TD>-->
		</TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
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
			  
				as_each_dc	= asinfotable.getAsEachDc();	
				as_value	= asinfotable.getAsValue();		
				crr_name	= asinfotable.getCrrName();	
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
				as_status_name= asinfotable.getAsStatusName();
				now_status	= asinfotable.getNowStatus();

				if(now_status==null || now_status.equals("")) now_status="ó������"; // ���� ���� 
	%>
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle class='list_bg' height="24"><%=as_name%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=as_mid%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=model_name%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=u_name%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=buy_date.substring(0,4)%>-<%=buy_date.substring(4,6)%>-<%=buy_date.substring(6,8)%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg'><%=nf.format(nf.parse(as_value))%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=as_status_name%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'>
						<a href="javascript:go_asInfo('<%=as_no%>','<%=c_no%>')">
						<IMG src='../am/images/lt_view_d.gif' border='0' align='absmiddle'></a></td>
				 <!-- <TD align=middle class='list_bg'>
						<a href="javascript:go_form('modify','<%=as_no%>','<%=c_no%>')">����</a>|<a href="javascript:go_form('delete','<%=as_no%>','<%=c_no%>')">���</a></td>
						<input type='hidden' name="c_no" value=<%=c_no%>>--></TR>
			<TR><TD colSpan=19 background="../am/images/dot_line.gif"></TD></TR>
<%
		}
%>
			</TBODY></TABLE>
	
		</TD>
	</TR>
	</TBODY>
</TABLE>
</body>
</html>

<script language="javascript">
var f = document.asForm;

// �ڻ� ��� ������ �̵�
function go_asset_form(){
	var c_no = f.c_no.value;
	location.href ="../servlet/AssetServlet?mode=asset_form&div=input&c_no="+c_no;
}

// �ڻ� ���� ����
function go_asInfo(as_no,c_no){
	location.href ="../servlet/AssetServlet?mode=user_asset_view&div=view&as_no="+as_no+"&c_no="+c_no;
}

	
function go_form(div,as_no,c_no){
	location.href ="../servlet/AssetServlet?mode=asset_form&div="+div+"&as_no="+as_no+"&c_no="+c_no;
}

// �Ϲ� �˻�
function go_search(){

	if( !f.searchscope.value=="" && f.searchword.value=="") {
		alert("�˻�� ������ �ֽʽÿ�");
		return false;
	} else if(f.searchscope.value=="" && !f.searchword.value==""){
		alert("�˻� �׸��� ������ �ֽʽÿ�");
		return false;
	} 

	var c_no = f.c_no.value;				// ����List ���� ���õ� ī�װ� id
	var searchscope = f.searchscope.value;  // �˻� �׸� (�ڻ��ȣ, ǰ��, ������ȣ ���)
	var searchword = f.searchword.value;	// �˻���
	var div ="general";
	
	f.action = "../servlet/AssetServlet?mode=asset_list&c_no="+c_no+"&searchscope="+searchscope+"&searchword="+searchword+"&div="+div;
	f.submit();
}

// �ڻ갨��ó��
function openwin() {

	wopen('../am/admin/measurePopup.jsp','measure','350','193','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//�󼼰˻�
function search_detail() {
	var c_no = f.c_no.value;
	wopen("../am/admin/search.jsp?category_full=<%=category_full%>&c_no="+c_no,'search','515','279','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

// ������ ���� ����ó��
function go_cleanDB(){
	if(confirm("������ ������ ���� �Ͻðڽ��ϱ�?")) 
	location.href ="../servlet/AssetServlet?mode=cleanDb";
}
</script>

