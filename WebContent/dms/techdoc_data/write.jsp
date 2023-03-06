<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.dms.entity.*"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%!
	MasterTable master;
	TechDocTable techdoc;
	LinkUrl	redirect;
	DmsEnvTable dmsenv;
%>

<%
	/*********************
	 * ����� ����(�α�����)
	**********************/
	String id = sl.id; 				//ID
	String name = sl.name;			//�̸�
	String division = sl.division;	//�μ���

	/**********
	 * �������
	***********/
	java.util.Date now = new java.util.Date();
	java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
	String regi_date 	= vans.format(now);

	/**********
	 * �Ķ����
	***********/
	String mode = request.getParameter("mode");
	String no = request.getParameter("no");
	String data_id = request.getParameter("d_id");
	String category = request.getParameter("category");

	/******************************
	 * master_data ���̺��� ��������
	 ******************************/
	master = new MasterTable();
	master = (MasterTable)request.getAttribute("MasterData");
	String subject			= master.getSubject()==null?"":master.getSubject();
	String writer			= master.getWriter()==null?"":master.getWriter();
	String category_info	= master.getWhereCategory();
	String doc_no			= master.getDocNo() == null?"������Ͻ� �ڵ�����":master.getDocNo();
	String model_code		= master.getModelCode();
	String model_name		= master.getModelName();
	String pjt_code			= master.getPjtCode();
	String pjt_name			= master.getPjtName();
	String node_code		= master.getNodeCode();
	String node_name		= master.getNodeName();
	String model_info		= "";

	if(!model_code.equals("") && model_code != null){
		com.anbtech.gm.business.makeGoodsTreeItems mt = new com.anbtech.gm.business.makeGoodsTreeItems();
		model_info = mt.getGoodsClassStrByModelCode(0,model_code,"") + model_name;
	}

	/******************************
	 * techdoc_data ���̺��� ��������
	 ******************************/
	techdoc = new TechDocTable();
	techdoc = (TechDocTable)request.getAttribute("techdoc_data");
	String where_from = techdoc.getWhereFrom()==null?"��ü����":techdoc.getWhereFrom();
	String security_level = techdoc.getSecurityLevel()==null?"3":techdoc.getSecurityLevel();
	String save_period = techdoc.getSavePeriod()==null?"3":techdoc.getSavePeriod();
	String written_lang = techdoc.getWrittenLang()==null?"KOR":techdoc.getWrittenLang();
	String doc_type = techdoc.getDocType()==null?"FILE":techdoc.getDocType();
	String data_no = techdoc.getAncestor()==null?"":techdoc.getAncestor();
	String modify_info = techdoc.getModifyHistory()==null?"":techdoc.getModifyHistory();
	String why_revision = techdoc.getWhyRevision()==null?"":techdoc.getWhyRevision();
	String preview = techdoc.getPreview()==null?"":techdoc.getPreview();
	String category_id = techdoc.getCategoryId()==null?"":techdoc.getCategoryId();
	String save_url = techdoc.getSaveUrl()==null?"NA":techdoc.getSaveUrl();
	String copy_num = techdoc.getCopyNum()==null?"NA":techdoc.getCopyNum();
	String writer_s = techdoc.getWriterS()==null?"":techdoc.getWriterS();
	String version = techdoc.getVerCode() == null?"1.0":techdoc.getVerCode();

	/******************************
	 * ���� ��ũ���ڿ� ��������
	 ******************************/
	redirect = new LinkUrl();
	redirect = (LinkUrl)request.getAttribute("RedirectInWrite");
	String link_change_category = redirect.getLinkChangeCategory();
	String input_hidden = redirect.getInputHidden();

	/******************************
	 * ����з��� ���� ȯ�� ��������
	 ******************************/
	dmsenv = new DmsEnvTable();
	dmsenv = (DmsEnvTable)request.getAttribute("DmsEnv");
	String enable_project	= dmsenv.getEnableProject();	//������Ʈ ��������(y/n)
	String enable_model		= dmsenv.getEnableModel();		//���ø𵨸� ��������(y/n)
	String enable_revision	= dmsenv.getEnableRevision();	//������ ��������(y/n)

	//�ű��Է½ÿ��� ���ȵ�ް� �����Ⱓ�� ����Ʈ�� �����Ѵ�.
	if(mode.equals("write")){
		security_level = dmsenv.getSecurityLevel();
		save_period = dmsenv.getSavePeriod();
	}

	/******************************
	 * ������ Ÿ��Ʋ ���ڿ� ����
	 ******************************/
	String title = "";
	String reg_type = "";
	if(mode.equals("write")){
		title = "�űԹ������";
		reg_type = "���� ���";
	}else if(mode.equals("modify")){
		title = "���������������";
		reg_type = "���� ����";
	}else if(mode.equals("revision")){
		title = "����������";
		reg_type = "���� ������";
	}


	int ref_count		= 5;	// �ִ� �������� ���� ����
	int enableupload	= 4;	// ���ε� ���� ����
%>

<script language=JavaScript>
<!--
<%
	int i = 1;

	while(i < enableupload){
		if(i == enableupload-1){
%>

		function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%> size=50>"
		}
	<%
		 break;
		}
	%>
	function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=50><font id=id<%=i+1%>></font>"
	}
<%
	i++;
	}
%>
//-->
</script>

<%
	TechDocTable file = new TechDocTable();

	ArrayList file_list = new ArrayList();
	file_list = (ArrayList)request.getAttribute("File_List");
	Iterator file_iter = file_list.iterator();

	i = 1;

	String file_stat = "";
	while(file_iter.hasNext()){
		file = (TechDocTable)file_iter.next();
		file_stat = file_stat + "<input type=file name='attachfile"+i+"' size=50>" + file.getFileName()+" ����! <input type=checkbox name = 'deletefile"+i+"' value='delete'><br>";
		i++;
	}
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../dms/css/style.css" type="text/css">
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../dms/images/blet.gif"> <%=title%></TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300><a href="javascript:checkForm();"><img src="../dms/images/bt_save.gif" border="0" align="absmiddle"></a> <a href="javascript:history.go(-1);"><img src="../dms/images/bt_cancel.gif" border="0" align="absmiddle"></a></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<form method=post name="writeForm" action='../servlet/AnBDMS?tablename=techdoc_data&upload_size=50' enctype='multipart/form-data' style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">��ϱ���</td>
           <td width="37%" height="25" class="bg_04"><b><%=reg_type%></b><input type="hidden" name="mode" value="<%=mode%>"></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">������ó</td>
           <td width="37%" height="25" class="bg_04">
		   <%
				//���õ� �����з��� ��ü���۱������(�ڵ�:10101�� ����)�� ���� ��ü���۹����� ����
				//�ܺ��Լ�����ڷ�(�ڵ�:10102�� ����)�� ��쿡�� �Է¹޵��� �Ѵ�.
				if(category.indexOf("10101") == 0) out.print("<input type='text' name='where_from' value='��ü����' size='7' readOnly>");
				else if(category.indexOf("10102") == 0) out.print("<input type='text' name='where_from' value='' size='20'>");
		   %>
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">������ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=doc_no%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04"><input type=text name=ver_code size=4 readOnly value="<%=version%>"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����з�</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<%=category_info%><input type="hidden" name="category" value="<%=category%>">
				<% if(version.equals("1.0")){ //���� ������ ��츸 ī�װ� ���� ����	%>				
					<a href='<%=link_change_category%>'><img src="../dms/images/bt_search.gif" border="0" align="absmiddle"></a>
				<%	}	%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<%	if(enable_project.equals("y")){	//������Ʈ �����Ҷ��� ���	%>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����̸�</td>
           <td width="37%" height="25" class="bg_04" colspan='3'>
				<input type='text' name="pjt_code" value="<%=pjt_code%>" size="15" readOnly class="text_01"> <input type=text name="pjt_name" size='20' value="<%=pjt_name%>" readOnly class="text_01">
				<a href='javascript:pjt_search();'><img src="../dms/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04" colspan='3'>
				<input type='text' name="node_code" value="<%=node_code%>" size="15" readOnly> <input type=text name="node_name" size='20' value="<%=node_name%>" readOnly> <a href='javascript:node_search();'><img src="../dms/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
<%	}	%>
<%	if(enable_model.equals("y")){ //���ø𵨰����Ҷ��� ���	%>				
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">���ø𵨸�</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<input type="text" name="model_code" value="<%=model_code%>" size="15" readOnly class="text_01"> <input type=text name="model_name" size='20' value="<%=model_name%>" readOnly class="text_01"> <a href='javascript:m_search();'><img src="../dms/images/bt_search.gif" border="0" align="absmiddle"></a>
						   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<%	}	%>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">��������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type=text name=subject size=50 value="<%=subject%>" maxlength="50" class="text_01"></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�ۼ���(�Լ���)</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
					<input type=text name="writer_info" size="15" readOnly value="<%=writer_s%>" class="text_01">
					<a href='javascript:searchUser();'><img src="../dms/images/bt_search.gif" border="0" align="absmiddle"></a>
					<input type="checkbox" name="writer_same" value="same" onClick="selWriterSame();">����ڿ� ����
					<input type="hidden" name="writer" value="<%=writer%>">		   		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">����÷��</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<%
					if (enableupload > 0){
				%>
		            <%=file_stat%>
				<%
						if(i < enableupload){
				%>
				            <input type=file name=attachfile<%=i%> onClick='fileadd_action<%=i%>()' size="50">
				            <font id=id<%=i%>></font>
				<%
						}else if(i == enableupload){
				%>
				            <input type=file name=attachfile<%=i%> size="50">
				            <font id=id<%=i%>></font>
				<%
						}
					}
				%>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����</td>
           <td width="37%" height="25" class="bg_04"><%=id%>/<%=name%>
					<input type="hidden" name="register_info" value="<%=id%>/<%=name%>">
					<input type="hidden" name="register" value="<%=id%>">		   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����</td>
           <td width="37%" height="25" class="bg_04"><%=regi_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="7" name="preview" cols="93" class="text_01"><%=preview%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table>
<div id="d_why_revision" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">����������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="5" name="why_revision" cols="93" class="text_01"><%=why_revision%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table></div>
<div id="d_modify_history" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����̷�</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="3" name="modify_history" cols="93"></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table></div>
<div id="d_modify_info" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">��������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="5" name="modify_info" cols="93"><%=modify_info%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table></div>
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">���ȵ��</td>
           <td width="37%" height="25" class="bg_04">
					<select size="1" name="security_level" onFocus="selSecurityLevel();">
					<option value="1">1��</option>
					<option value="2">2��</option>
					<option value="3">3��</option>
					<option value="4">��ܺ�</option>
					<option value="5">�Ϲ�</option>
					</select>		   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����Ⱓ</td>
           <td width="37%" height="25" class="bg_04">
					<select size="1" name="save_period" onFocus="selSavePeriod();">
					<option value="1">1��</option>
					<option value="3">3��</option>
					<option value="5">5��</option>
					<option value="10">10��</option>
					<option value="0">����</option>
					</select>		   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�ۼ����</td>
           <td width="37%" height="25" class="bg_04">
					<select size="1" name="written_lang">
					<option value="KOR">�ѱ���</option>
					<option value="ENG">����</option>
					<option value="JPN">�Ϻ���</option>
					<option value="CHN">�߱���</option>
					<option value="DAU">���Ͼ�</option>
					<option value="ETC">��Ÿ</option>
					</select>		   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">����Ÿ��</td>
           <td width="37%" height="25" class="bg_04">
					<select name="doc_type" onChange="javascript:selDocType();">
					<option value="FILE">��������</option>
					<option value="BOOK">å������</option>
					<option value="SHEET">SHEET����</option>
					<option value="CD">CD(DVD)����</option>
					<option value="TAPE">����������</option>
					<option value="FILM">�ʸ�����</option>
					<option value="ETC">��Ÿ</option>
					</select>		   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">������ġ</td>
           <td width="37%" height="25" class="bg_04"><input type=text name="save_url" size=35  value="<%=save_url%>" readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����Ǽ�</td>
           <td width="37%" height="25" class="bg_04"><input type=text name="copy_num" size=5  value="<%=copy_num%>" readOnly></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����ڷ�</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
					<table border=0>
					  <tr>
						<td align=left width=52%>�����ڷ��</td>
						<td align=left width=23%>���ڸ�</td>
						<td align=left width=15%>���ǻ�</td>
						<td align=right width=10%>���ǳ⵵</td>
					  </tr>
					  <tr><td colspan='4'>
						<%
							for(int j=1; j<ref_count; j++){
								String view_str = "collapsed";
								if(j==1) view_str = "expanded";
						%>
								<div id="layer<%=j%>" class="<%=view_str%>">
								<input type=text name=ref_subject<%=j%> size="45" onFocus='add_action<%=j%>()'> <input type=text name=ref_writer<%=j%> size=20> <input type=text name=ref_press_name<%=j%> size=15> <input type=text name=ref_press_year<%=j%> size="4"><br></div>
						<%
							}
						%>
								<div id="layer<%=ref_count%>" class="collapsed">
								<input type=text name=ref_subject<%=ref_count%> size="45"> <input type=text name=ref_writer<%=ref_count%> size=20> <input type=text name=ref_press_name<%=ref_count%> size=15> <input type=text name=ref_press_year<%=ref_count%> size="4"></div>
						<input type='hidden' name='ref_count' value='<%=ref_count%>'>
						<input type='hidden' name='reference'></td>
					  </tr>
					</table>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
  <input type="hidden" name="tablename" value="techdoc_data">
  <input type="hidden" name="d_id" value="<%=data_id%>">
  <input type="hidden" name="no" value="<%=no%>">
  <input type="hidden" name="doc_no" value="<%=doc_no%>">
<%=input_hidden%>
</form>
</td></tr></table>
</body>
</html>


<script language=JavaScript>
<!--
<%

	for(int k=1; k<ref_count; k++){
%>

		function add_action<%=k%>() {
			show('layer<%=k+1%>');
<%
		int w = ref_count;
		while(w > k+1){
%>
			hide('layer<%=w%>');
<%
			w--;
		}
%>
		}
<%
	}
%>

	//�����ڷ� ����Ʈ �����ͼ� �ѷ��ֱ�
	var f = document.writeForm;
<%
	TechDocTable ref = new TechDocTable();

	ArrayList ref_list = new ArrayList();
	ref_list = (ArrayList)request.getAttribute("Reference_List");
	Iterator ref_iter = ref_list.iterator();

	int c = 1;

	while(ref_iter.hasNext()){
		ref = (TechDocTable)ref_iter.next();
		String ref_subject = ref.getRefSubject();
		String ref_writer = ref.getRefWriter();
		String ref_press_name = ref.getRefPressName();
		String ref_press_year = ref.getRefPressYear();
%>
		f.ref_subject<%=c%>.value = '<%=ref_subject%>';
		f.ref_writer<%=c%>.value = '<%=ref_writer%>';
		f.ref_press_name<%=c%>.value = '<%=ref_press_name%>';
		f.ref_press_year<%=c%>.value = '<%=ref_press_year%>';
<%
		c++;
	}
%>
//-->
</script>


<script language='javascript'>
<!--
<%	if(!security_level.equals("")){	%>
		document.writeForm.security_level.value = '<%=security_level%>';
<%	}	%>

<%	if(!save_period.equals("")){	%>
		document.writeForm.save_period.value = '<%=save_period%>';
<%	}	%>

<%	if(!written_lang.equals("")){	%>
		document.writeForm.written_lang.value = '<%=written_lang%>';
<%	}	%>

<%	if(!doc_type.equals("")){	%>
		document.writeForm.doc_type.value = '<%=doc_type%>';
<%	}	%>
<%	if(!doc_type.equals("FILE")){	%>
		document.writeForm.save_url.readOnly = false;
		document.writeForm.copy_num.readOnly = false;
<%	}	%>

//���� ����� ���
<%	if(mode.equals("modify")){	%>

	var f = document.writeForm;
	show('d_why_revision');
//	show('d_modify_info');
//	show('d_modify_history');
//	f.modify_history.readOnly = 'true';
//	f.modify_info.readOnly = 'false';
//	f.why_revision.readOnly = 'true';


<%	
	//���� �����ڷ� �׸��� ����ó���Ѵ�.
	for(int r=1; r<c-1; r++){
		out.print("add_action"+r+"();");
	}
}	
%>

//������ ����� ���
<%	if(mode.equals("revision")){	%>
	var f = document.writeForm;
	f.subject.readOnly = 'true';
	show('d_why_revision');
	f.modify_info.readOnly = 'true';
<%	}	%>

//�ʼ� �Է»��� üũ
function checkForm(){
	var f = document.writeForm;

	if(f.where_from.value.length < 3){
		alert("������ó�� �Է��Ͻʽÿ�.");
		f.where_from.focus();
		return;
	}

<% if(enable_project.equals("y")){ %>
	if(f.pjt_code.value == ''){
		alert("���ð����� ã�Ƽ� �����Ͻʽÿ�.");
		return;
	}
/*	if(f.node_code.value == ''){
		alert("���������� �����Ͻʽÿ�.");
		return;
	}
*/
<% }	%>

<% if(enable_model.equals("y")){ %>
	if(f.model_code.value == '10'){
		alert("���ø��� ã�Ƽ� �����Ͻʽÿ�.");
		return;
	}
<%	}	%>

	if(f.subject.value.length < 5){
		alert("���� ������ �Է��Ͻʽÿ�.(5�� �̻�)");
		f.subject.focus();
		return;
	}

	if(f.ver_code.value == ''){
		alert("���������� �Է��Ͻʽÿ�.");
		return;
	}

	if(f.writer.value == ''){
		alert("�ۼ���(or �Լ���)�� �����Ͻʽÿ�.");
		return;
	}

<%	if(mode.equals("write") || mode.equals("modify")){	%>
	if(f.preview.value.length < 30){
		alert("÷�ι����� �ֿ� ������ 30�� �̻����� ����Ͽ� �Է��Ͻʽÿ�.");
		f.preview.focus();
		return;
	}
<%	}	%>

<%	if(mode.equals("modify")){	%>
/* �����̷� ����� ���� ȿ�뼺�� ���� �ּ� ó����. 04/01/30
	if(f.modify_info.value.length < 5){
		alert("�����̷��� ����� ���� ���������� ����Ͽ� �Է��Ͻʽÿ�.");
		f.modify_info.focus();
		return;
	}
*/
<%	}	%>

<%	if(mode.equals("revision")){	%>
	if(f.why_revision.value.length < 10){
		alert("�������̷��� ����� ���� ��������� ����Ͽ� �Է��Ͻʽÿ�.");
		f.why_revision.focus();
		return;
	}
<%	}	%>

	if(f.doc_type.value != "FILE"){
		if(f.save_url.value == ''){
			alert("����������ġ�� �Է��Ͻʽÿ�.");
			f.save_url.focus();
			return;
		}
		if(f.copy_num.value == ''){
			alert("�����Ǽ��� �Է��Ͻʽÿ�.");
			f.copy_num.focus();
			return;
		}
	}


	//�����ڷḦ �ϳ� �̻� �Է��� ��� ������ �ݵ�� �Է��ϵ��� �Ѵ�.
<%	for(int m=1; m<=ref_count; m++){	%>
		if ((f.ref_writer<%=m%>.value.length > 0 || f.ref_press_name<%=m%>.value.length > 0 || f.ref_press_year<%=m%>.value.length > 0) && f.ref_subject<%=m%>.value.length < 1){
			alert("�����ڷ� ������ �ݵ�� �Է��ϼž� �մϴ�.");
			f.ref_subject<%=m%>.focus();
			return;
		}
		var ref<%=m%> = '';
		var ref_subject<%=m%> = f.ref_subject<%=m%>.value;
		var ref_writer<%=m%> = f.ref_writer<%=m%>.value;
		var ref_press_name<%=m%> = f.ref_press_name<%=m%>.value;
		var ref_press_year<%=m%> = f.ref_press_year<%=m%>.value;


		ref<%=m%> = ref_subject<%=m%> + "|" + ref_writer<%=m%> + "|" + ref_press_name<%=m%> + "|" + ref_press_year<%=m%>;
<%	}	%>


	var ref = '';

<%	for(int n=1; n<=ref_count; n++){	%>
		ref += ref<%=n%> + "^";
<%	}	%>
	
	f.reference.value = ref;

	document.onmousedown=dbclick;
	f.submit();

}

function dbclick() 
{
    if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�."); 	
}

//ī�װ� ����
function modify_category(tablename,mode,category,searchscope,searchword,page,no,data_id,ver) {
	var sParam = "src=modify_category.jsp&frmWidth=290&frmHeight=310&title=change_category&category="+category;
	
	var sRtnValue = showModalDialog("../dms/modalFrm.jsp?"+sParam,"change_category","dialogWidth:296px;dialogHeight:335px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");
	
	if (typeof sRtnValue != "undefined" && sRtnValue != "")
	{
		if(sRtnValue == '1' || sRtnValue == '101'){
			alert("���õ� �з��ܰ迡�� ������ ����Ͻ� �� �����ϴ�.");
			return;
		}
/*
		if(sRtnValue.indexOf("102") == 0){
			alert("���õ� �з��ܰ迡�� ������ ����Ͻ� �� �����ϴ�.");
			return;
		}
*/
		sParam = "&tablename="+tablename+"&category="+sRtnValue+"&searchscope="+searchscope+"&searchword="+searchword;
		sParam = sParam + "&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;
		location.href = "../servlet/AnBDMS?mode="+mode + sParam;
	}

	//wopen('../dms/modify_category.jsp?category='+category+'&tablename='+tablename+'searchscope='+searchscope+'&searchword='+searchword+'&page='+page+'&no='+no+'&d_id='+data_id+'&ver='+ver,'change_category','300','305','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//�� ã��
function m_search()
{
	var url = "../gm/openModelInfoWindow.jsp?fname=writeForm&one_class=pdg_code&one_name=pdg_name&two_class=pd_code&two_name=blank&three_class=modelg_code&three_name=modelg_name&four_class=model_code&four_name=model_name&fg_code=fg_code";
	wopen(url,"SEARCH_MODEL",'800','400','scrollbars=yes,toolbar=no,status=no,resizable=no');

}

//�����ڵ� ã��
function pjt_search()
{
	wopen('../servlet/PsmProcessServlet?mode=search_project&target=writeForm.pjt_code/writeForm.pjt_name','search_pjt','400','220','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//�ش���� ����[���]�ڵ� ã��
function node_search()
{
	var pjt_code = document.writeForm.pjt_code.value;
	if(pjt_code.length == 0) { alert('�����̸��� ���� �����Ͻʽÿ�.'); return; }
	wopen('../servlet/projectPmDocumentServlet?mode=PDT_NL&pjt_code='+pjt_code,'search_node','400','220','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// �ۼ��ڰ� ����ڿ� ������ ���� ó��
function selWriterSame(){
	var f = document.writeForm;

	if(f.writer_same.checked){
		f.writer_info.value = f.register_info.value;
		f.writer.value = f.register.value;
//		f.writer_search.disabled = true;
	}else{
		f.writer_info.value = "";
		f.writer.value = "";
//		f.writer_search.disabled = false;
	}
}

//����� ã��
function searchUser()
{

	wopen("../dms/searchUser.jsp","search_user","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

}

// �������� ���ý� ���� �׼� ����
function selDocType(){
	var f = document.writeForm;

	if(f.doc_type.value == 'FILE'){
		f.save_url.value = 'NA';
		f.copy_num.value = 'NA';
		f.save_url.readOnly = true;
		f.copy_num.readOnly = true;
	}else{
		f.save_url.value = '';
		f.copy_num.value = '';
		f.save_url.readOnly = false;
		f.copy_num.readOnly = false;	
	}
}

// ���ȵ�� ���� �� ��� �޽��� ���
function selSecurityLevel(){
	alert("���ȵ���� ���������� �°� �ڵ� �����Ǿ� �ֽ��ϴ�.\n�����Ͻ÷��� ���������ڿ� ������ �����Ͻʽÿ�.");
}

// �������� ���� �� ��� �޽��� ���
function selSavePeriod(){
	alert("���������� ���������� �°� �ڵ� �����Ǿ� �ֽ��ϴ�.\n�����Ͻ÷��� ���������ڿ� ������ �����Ͻʽÿ�.");
}


// ���õ� ���̾ ����
function hide( menuname )
{
  if (navigator.appName =="Netscape" ) {
	  document.layers[menuname].visibility="hide";
  } else {
	  document.all[menuname].className="collapsed"
   }
}

// ���õ� �����̸� ������
function show( menuname )
{
  if (navigator.appName =="Netscape" ) {
	   document.layers[menuname].visibility="show";
  } else {
	   document.all[menuname].className="expanded"
  }
}
//-->
</script>