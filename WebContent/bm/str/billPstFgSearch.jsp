<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "붙이기할 FG코드 찾기"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String gid = (String)request.getAttribute("gid"); if(gid == null) gid = "";
	
	//-----------------------------------
	//	붙이기할 모델 리스트
	//-----------------------------------
	com.anbtech.bm.entity.mbomMasterTable fg;
	ArrayList fg_list = new ArrayList();
	fg_list = (ArrayList)request.getAttribute("FG_List");
	fg = new mbomMasterTable();
	Iterator fg_iter = fg_list.iterator();

	//-----------------------------------
	//	붙이기할 ASSY 리스트
	//-----------------------------------
	com.anbtech.bm.entity.mbomStrTable assy;
	ArrayList assy_list = new ArrayList();
	assy_list = (ArrayList)request.getAttribute("ASSY_List");
	assy = new mbomStrTable();
	Iterator assy_iter = assy_list.iterator();
	

%>

<SCRIPT language=javascript>
<!--
//검색하기
function goSearch()
{
	var sWord = document.sForm.sWord.value;
	if(sWord.length < 4) { 
		alert('검색할 FG코드를 4자이상 입력하십시오.'); 
		document.sForm.sWord.focus();
		return; 
	}

	document.sForm.action='../servlet/BomBillModifyServlet';
	document.sForm.mode.value='pst_fgsearch';
	document.sForm.submit();
}
//FG코드 선택하기
function selectFG()
{
	var a = document.sForm.fg.selectedIndex;
	if(a != -1) {
		var fg = document.sForm.fg.options[a].value; 
		var list = fg.split("|");
	}

	//복사대상과 같은 붙이기 대상인지 판단하기
	var ca = parent.copy.document.sForm.fg.selectedIndex; 
	if(ca != -1) {
		var cfg = parent.copy.document.sForm.fg.options[ca].value; 
		var clist = cfg.split("|");
		if(list[0] == clist[0]) { alert('같은 모델(FG코드)간에는 복사/붙이기를 할 수 없습니다.'); return; }
	} else { alert('복사하기를 먼저 진행하십시오.'); return; }

	document.sForm.action='../servlet/BomBillModifyServlet';
	document.sForm.mode.value='pst_fgsearch';
	document.sForm.gid.value=list[0];
	document.sForm.submit();
}
//붙이기하기
function pastePart()
{
	//현재상태를 찾는다.
	var status = parent.list.document.sForm.status.value;
	if(status == 'PASTE') { alert('복사하기를 진행후 붙이기를 하십시요.'); return; }

	//복사하기를 실행했는지를 판단한다.
	if(status == 'COPY') {
		var pcnt = parent.list.document.eForm.pcnt.value; 
		if(pcnt == '1') { alert('붙이기할 부품이 없습니다.'); return; }
	}

	//모델선택정보
	var a = document.sForm.fg.selectedIndex;
	if(a != -1) {
		var fg = document.sForm.fg.options[a].value; 
		var list = fg.split("|");
		var gid = list[0];
		var ccd = list[1];
		var model_code = list[3];
	}
	else { alert('붙이기할 모델을 선택하십시오.'); return; }

	//복사대상과 같은 제품군 코드의 붙이기 대상인지 판단하기
	var pdg_code = parent.list.document.sForm.pdg_code.value;
	var chk_fg = document.sForm.fg.options[0].value.split("|"); 
	var pdg_ccd = chk_fg[2];
	if(pdg_code != pdg_ccd){
		alert('같은 제품군내 에서만 붙이기를 할 수 있습니다.'); return;  
	}

	//ASSY선택정보
	var b = document.sForm.assy.selectedIndex;
	if(b != -1) {
		var assy = document.sForm.assy.options[b].value; 
		var list = assy.split("|");
		var ccd = list[2]; 
	} else { alert('붙이기할 ASSY코드를 선택하시오'); return; } 

	var a = confirm('붙이기를 진행합니다. 계속하기겠습니까?');
	if(a == false) return;

	//데이터 붙이기하기
	parent.list.document.all['lding'].style.visibility="visible";	//처리중 메시지 출력
	document.all['saving'].style.visibility="visible";				//메뉴버튼 Disable [붙이기 자신]
	parent.copy.document.all['saving'].style.visibility="visible";	//메뉴버튼 Disable [복사]

	//Assy정보로 붙이기
	parent.list.document.eForm.action='../servlet/BomBillModifyServlet';
	parent.list.document.eForm.mode.value='pst_write';
	parent.list.document.eForm.gid.value=list[0];
	parent.list.document.eForm.ln0.value=eval(list[1]-1);	//모델선택시와 똑같은 규칙을 맞추기 위해 -1
	parent.list.document.eForm.pc0.value=list[2];
	parent.list.document.eForm.cc0.value=ccd;
	parent.list.document.eForm.parent_code.value=model_code;
	parent.list.document.eForm.submit();
	
}
-->
</script>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">
<FORM name="sForm" method="post" style="margin:0" encType="multipart/form-data" onSubmit='javascript:goSearch();return false;'>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 >
	<TR><TD align='left' width='50%'><!-- 편집대상BOM검색 -->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
		 	<TBODY>
				<TR height=25><TD vAlign="center" class='bg_05' style='padding-left:5px;' background='../bm/images/title_bm_bg.gif'>붙이기대상:F/G코드</TD></TR>
				<TR height=25><TD align='left' style='padding-left:5px;' bgcolor=''>
					<INPUT type='text' name='sWord' value='<%=sWord%>' size='12' onClick="document.sForm.sWord.value=''">
					<a href='javascript:goSearch();'><img src='../bm/images/bt_search.gif' align='absmiddle' border=0></TD></TR>
				<TR><TD style='padding-left:5px;'>
					<SELECT size="6" name="fg" multiple onChange='javascript:selectFG();'> 
					<OPTGROUP label='----------------------'>
					<%
						String sel = "";
						while(fg_iter.hasNext()) {
							fg = (mbomMasterTable)fg_iter.next(); 
							if(gid.equals(fg.getPid())) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+fg.getPid()+"|"+fg.getFgCode());
							out.print("|"+fg.getPdgCode()+"|"+fg.getModelCode()+"'>");
							out.println(fg.getModelCode()+": "+fg.getFgCode()+"</option>");
						} 
					%>
					</SELECT></TD></TR></TBODY></TABLE></TD>
		
			<TD align='left' width='50%'><!--붙이기대상ASSY-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
			<TBODY>
				<TR height=25><TD vAlign="center" class='bg_05' style='padding-left:5px;' background='../bm/images/title_bm_bg.gif'>붙이기대상:ASSY코드</TD></TR>
				<TR height=25><TD align='left' style='padding-left:5px;' bgcolor=''>
					<a href='javascript:pastePart();'><img src='../bm/images/bt_paste.gif' border=0 alt='붙이기' align='absmiddle'></a></TD></TR>
				<TR><TD style='padding-left:5px;'>
					<SELECT size="6" name="assy" multiple>
					<OPTGROUP label='----------------'>
					<%
						while(assy_iter.hasNext()) {
							assy = (mbomStrTable)assy_iter.next(); 
							out.print("<option value='"+assy.getGid()+"|"+assy.getLevelNo()+"|"+assy.getParentCode()+"'>");
							out.println(assy.getLevelNo()+": "+assy.getParentCode()+"</option>");
						} 
					%>
					</SELECT></TD></TR>
					
					</TBODY></TABLE></TD>
			</TR></TABLE>

<INPUT type='hidden' name='sItem' value='fg_code'>
<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="gid" size="15" value="">
</FORM>

<DIV id="lding" style="position:absolute;left:180px;top:180px;width:250px;height:100px;visibility:hidden;">
		<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

<DIV id="saving" style="position:absolute;left:0px;top:0px;width:250px;height:100px;visibility:hidden;">
	<TABLE width="400" border="0" cellspacing=1 cellpadding=1 bgcolor="">
		<TR><TD height="50" align="center" valign="middle"></TD></TR></TABLE>
</DIV>

</BODY>
</HTML>
