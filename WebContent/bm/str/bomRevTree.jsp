<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������ ��� TREE"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//-----------------------------------
	//	������ Tree�����
	//-----------------------------------
	String JS = (String)request.getAttribute("TREE");		
	if(JS.length() == 0) JS = "var TREE_ITEMS = [ ];";

	String COUNT = (String)request.getAttribute("COUNT"); if(COUNT == null) COUNT = "0";
	int cnt = Integer.parseInt(COUNT);
	
%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
<SCRIPT language='JavaScript' src='../bm/str/tree.js'></SCRIPT>
<SCRIPT language='JavaScript'>
<!--
var tree_tpl = {
	'target'  : 'list',	// name of the frame links will be opened in
							// other possible values are: _blank, _parent, _search, _self and _top

	'icon_e'  : '../bm/str/icons/empty.gif', // empty image
	'icon_l'  : '../bm/str/icons/line.gif',  // vertical line

    'icon_32' : '../bm/str/icons/base.gif',   // root leaf icon normal
    'icon_36' : '../bm/str/icons/base.gif',   // root leaf icon selected

	'icon_48' : '../bm/str/icons/base.gif',   // root icon normal
	'icon_52' : '../bm/str/icons/base.gif',   // root icon selected
	'icon_56' : '../bm/str/icons/base.gif',   // root icon opened
	'icon_60' : '../bm/str/icons/base.gif',   // root icon selected
	
	'icon_16' : '../bm/str/icons/folder.gif', // node icon normal
	'icon_20' : '../bm/str/icons/folderopen.gif', // node icon selected
	'icon_24' : '../bm/str/icons/folderopen.gif', // node icon opened
	'icon_28' : '../bm/str/icons/folderopen.gif', // node icon selected opened

	'icon_0'  : '../bm/str/icons/page.gif', // leaf icon normal
	'icon_4'  : '../bm/str/icons/page.gif', // leaf icon selected
	
	'icon_2'  : '../bm/str/icons/joinbottom.gif', // junction for leaf
	'icon_3'  : '../bm/str/icons/join.gif',       // junction for last leaf
	'icon_18' : '../bm/str/icons/plusbottom.gif', // junction for closed node
	'icon_19' : '../bm/str/icons/plus.gif',       // junctioin for last closed node
	'icon_26' : '../bm/str/icons/minusbottom.gif',// junction for opened node
	'icon_27' : '../bm/str/icons/minus.gif'       // junctioin for last opended node
};
-->
</SCRIPT>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display()' oncontextmenu="return false">
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR valign='top'><TD>	
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25><!-- Ÿ��Ʋ �� ������ ���� -->
			<TD vAlign="center" class="bg_05" BACKGROUND='../bm/images/title_bm_bg.gif' style='padding-left:5px;'>������ BOM TREE</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR></TBODY></TABLE>

<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:115; overflow-x:auto; overflow-y:auto;">	
<SCRIPT language='JavaScript'>
<!--
//Tree �����
<%=JS%>
	new tree (TREE_ITEMS, tree_tpl);

//������� ���� ��ġ��
//var cnt = '<%=cnt%>';
//for(i=1; i<cnt; i++)  trees[0].toggle(i);
-->
</SCRIPT>

<!-- �ش� PL �б� [from ����]-->
<FORM name="sForm" method="post" style="margin:0">
<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="child_code" value=''>
<INPUT type="hidden" name="url" value=''>
<INPUT type="hidden" name="sel_date" value=''>
</FORM>
</DIV>

</TD></TR></TABLE>
</BODY>
</HTML>

<SCRIPT language='javascript'>
<!--
// ������������ ���������� �и��� �ʵ��� ó��
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
		var c_h = this.document.body.scrollHeight; // ���� �������� ũ��
	//	var div_h = h - 376 ;
	var div_h = c_h - 29;
	item_list.style.height = div_h;
}
-->
</SCRIPT>