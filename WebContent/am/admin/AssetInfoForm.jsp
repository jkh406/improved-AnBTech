<%@ include file="../../admin/configHead.jsp"%>
<%@ page language	= "java" 
	contentType		= "text/html;charset=euc-kr" 
	errorPage		= "../../admin/errorpage.jsp" 
%>
<%@ page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page import="java.util.*,com.anbtech.am.entity.*,com.anbtech.text.Hanguel,java.text.NumberFormat"%>
<%	
	com.anbtech.am.entity.AsInfoTable asInfoTable;
	com.anbtech.am.entity.AsInfoTable asInfoTable2;
	ArrayList arry;
	Iterator file_iter2;
	Iterator file_iter;
	int enableupload	= 3;	// 업로드 개수 지정
	//  오늘 날짜
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
	String hrs = anbdt.getDateNoformat();	

	NumberFormat nf = NumberFormat.getInstance();
	
	// 변수 
	String   div		= request.getParameter("div")==null?"input":request.getParameter("div");
	String	 mode		= request.getParameter("mode")==null?"asset_manager":request.getParameter("mode");
	String   as_each_dc = request.getParameter("DcPercent")==""?"0":request.getParameter("DcPercent");
	String   cno		= request.getParameter("c_no");
	//cno = "";
	//if("input".equals(div)) { cno = request.getParameter("c_no"); }
	String   caption	="";
	String   readonly	="";
	String   sb			="";
	String   user_id	="";
	String   user_name	="";
	String   user_rank	="";
	//String   hrs		="";
	String   handle		="";
	String   as_no		="";			
	String   as_mid		="";		
	String   c_no		="";
	String   w_id		="";
	String   w_name		="";
	String   w_rank		="";
	String   b_id		="";
	String   b_name		="";
	String   b_rank		="";
	String   as_item_no	="";	
	String   model_name	="";	
	String   as_name	="";		
	String   as_serial	="";		
	String   buy_date	="";		
	String   as_price	="0";		
	String   dc_count	="0";	
	String   apply_dcdate ="";
	//String   dc_bound="";		
	//String   as_each_dc="0";	
	String   as_value	="0";	
	String   crr_id		="";
	String   crr_name	="";		
	String   crr_rank	="";		
	String	 buy_where	="";		
	String   as_maker	="";		
	String   as_setting	="";	
	String	 bw_tel		="";		
	String   bw_address	="";	
	String   bw_employee	="";	
	String   bw_mgr_tel	="";	
    String   etc		="";			
	String   as_status	="";	
	String   as_except_day="";
	String	 as_except_reason="";
	String   file_se	="";		
	String   file_name	="";		
	String   file_type	="";		
	String   file_size	="";		
	String   file_umask	="";
	String   file_path	="";
	String   file_stat	= "";
	String   ct_id		= "";
	String   view		="";
	String   type		="text";
	String   del_form	="";
	String   del_reason	="";
			
	if("input".equals(div))      { caption = " 자산등록 ";
	} else  if("view".equals(div))       { caption = " 자산상세정보 ";
	} else  if("modify".equals(div))     { caption = " 자산수정 ";
	} else  if("delete".equals(div))     { caption = " 자산폐기 ";
	} else  if("delete_view".equals(div)){ caption = " 폐기자산 상세정보";
	}

	//자원 등록 모드
	if( "input".equals(div) ) {
		com.anbtech.am.entity.AMUserTable auser = new com.anbtech.am.entity.AMUserTable();
		auser = (com.anbtech.am.entity.AMUserTable)request.getAttribute("user");
		
		// -- 현재 사용자 정보------------------
		user_id		= auser.getUserId();
		user_name	= auser.getUserName();
		user_rank	= auser.getUserRank();
		
		user_name	= user_id+"/"+user_name;
		w_id		= user_id;
		w_name		= user_name;
		w_rank		= user_rank;
		b_id		= user_id;
		b_name		= user_name;
		crr_id		= user_id;
		crr_name	= user_name;

		sb=(String)request.getAttribute("CategoryList");

	//자원 정보보기,수정,삭제시 정보보기 모드
	} else if("delete_view".equals(div)||"view".equals(div)|| "modify".equals(div) || "delete".equals(div) ){
		asInfoTable = new com.anbtech.am.entity.AsInfoTable();
		asInfoTable = (com.anbtech.am.entity.AsInfoTable)request.getAttribute("assetInfo");
		
		sb=(String)request.getAttribute("CategoryList"); // 카테고리 ComboList
		    as_no		= "" + asInfoTable.getAsNo();
		    as_mid		= asInfoTable.getAsMid();		
		    as_item_no	= asInfoTable.getAsItemNo();	
		    model_name	= asInfoTable.getModelName();
			w_id		= asInfoTable.getWid();
			w_name		= asInfoTable.getWname();
			w_rank		= asInfoTable.getWrank();
			//w_name = w_id+"/"+w_name;
			b_id		= asInfoTable.getBid();
			b_name		= asInfoTable.getBname();
			b_rank		= asInfoTable.getBrank();
			//b_name = b_id+"/"+b_name;
			c_no		= asInfoTable.getCno();
		    as_name		= asInfoTable.getAsName();		
		    as_serial	= asInfoTable.getAsSerial();		
		    buy_date	= asInfoTable.getBuyDate();		
		    as_price	= asInfoTable.getAsPrice();		
		    dc_count	= asInfoTable.getDcCount();		
			apply_dcdate= asInfoTable.getApplyDcDate();
		    //dc_bound	= asInfoTable.getDcBound();		
		    as_each_dc	= asInfoTable.getAsEachDc();	
		    as_value	= asInfoTable.getAsValue();		
			crr_id		= asInfoTable.getCrrId();
			crr_name	= asInfoTable.getCrrName();		
		    crr_rank	= asInfoTable.getCrrRank();		
			//crr_name  = crr_id +"/"+ crr_name;
		    buy_where	= asInfoTable.getBuyWhere();		
		    as_maker	= asInfoTable.getAsMaker();		
		    as_setting	= asInfoTable.getAsSetting();	
		    bw_tel		= asInfoTable.getBwTel();		
		    bw_address	= asInfoTable.getBwAddress();	
		    bw_employee	= asInfoTable.getBwEmployee();	
		    bw_mgr_tel	= asInfoTable.getBwMgrTel();	
		    etc			= asInfoTable.getEtc();			
		    as_status	= asInfoTable.getAsStatus();		
		    file_se		= asInfoTable.getFileSe();		
		    file_name	= asInfoTable.getFileName();		
		    file_type	= asInfoTable.getFileType();		
		    file_size	= asInfoTable.getFileSize();		
		    file_umask	= asInfoTable.getFileUmask();
		    file_path	= asInfoTable.getFilePath();
			handle		= asInfoTable.getHandle();
			ct_id		= asInfoTable.getCtId();
			del_form	= asInfoTable.getDelForm()==null?"":asInfoTable.getDelForm();
			del_reason	= asInfoTable.getDelReason()==null?"":asInfoTable.getDelReason();

			as_except_day = asInfoTable.getAsExceptDay();  
			if(as_except_day==null) { as_except_day="";
			} else if(as_except_day.length() > 4) { as_except_day = as_except_day.substring(0,4)+"-"+as_except_day.substring(4,6)+"-"+as_except_day.substring(6,8);
			}

			as_except_reason=asInfoTable.getAsExceptReason();
			if(as_except_reason==null || as_except_reason.equals("")) {
				as_except_reason="";
			}		
			
			if(apply_dcdate=="0") { apply_dcdate="감가적용 제외 자산입니다.";
			} else if(apply_dcdate.length()>4){
				apply_dcdate = apply_dcdate.substring(0,4)+"년"+apply_dcdate.substring(4,6)+"월";
			}
	}
%>

<script language='JavaScript'>
<%
		int i = 1;
		while(i < enableupload){
			if(i == enableupload-1){
%>
			function fileadd_action<%=i%>() {
				id<%=i%>.innerHTML="<br><INPUT type=file name=attachfile<%=i+1%> size=50>";
			}
<%			break;
			}
%>
			function fileadd_action<%=i%>() {
				id<%=i%>.innerHTML="<br><INPUT type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=50><FONT id=id<%=i+1%>></FONT>";
			}
<%			i++;
		}
%>
</script>

<%
	//수정모들일 경우 기존 첨부파일 정보를 가져온다.
	if("delete".equals(div) || "modify".equals(div)) {
		
		com.anbtech.am.entity.AsInfoTable file = new com.anbtech.am.entity.AsInfoTable();

		ArrayList file_list = new ArrayList();
		file_list = (ArrayList)request.getAttribute("assetfile");
		file_iter = file_list.iterator();

		i = 1;

		file_stat = "";
		while(file_iter.hasNext()){
			file = (AsInfoTable)file_iter.next();
			file_stat = file_stat + "<INPUT type=file name='attachfile"+i+"' size=50>" + file.getFileName()+" 삭제! <INPUT type=checkbox name = 'deletefile"+i+"' value='delete'><br>";
			i++;
		}

	}  else {
		i=1;
	}
%>

<HTML>
<HEAD><TITLE></TITLE>
<META http-equiv='Content-Type' content='text/HTML; charset=EUC-KR'>
<LINK rel="stylesheet" href="../am/css/style.css" type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0">

<FORM method="post" name="asContForm" enctype='multipart/FORM-data' action="../servlet/AssetServlet" style="margin:0">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="TITLE"><img src="../am/images/blet.gif"><FONT color=#07367d><%=caption%></FONT></TD></TR></TBODY></TABLE></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR><TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0>
				<TBODY>
				<TR><TD align=left width=500 style='padding-left:5px'>
<%		if("user_asset_view".equals(mode)) { //일반사용자가 상세정보를 볼때	 %>
				<img src="../am/images/bt_list.gif" onClick="history.back()" style='cursor:hand' align='absmiddle'>
				<a href="javascript:go_form('modify','<%=as_no%>','<%=c_no%>')"><img src='../am/images/bt_modify.gif' align='absmiddle' border='0'></a>
				<a href="javascript:go_form('delete','<%=as_no%>','<%=c_no%>')"><img src='../am/images/bt_del.gif' align='absmiddle' border='0'></a>
<%		} else if("input".equals(div)){ // 자원 정보 등록시	%>
				<img src="../am/images/bt_save.gif" onclick="chk()" border='0' style='cursor:hand' >
				<img src="../am/images/bt_cancel.gif" onClick="history.go(-1)" border='0' style='cursor:hand' align='absmiddle'>
				<INPUT type='hidden' name='div' value='input'>
				<INPUT type='hidden' name='mode' value='asset_manager'>
<%		} else if("view".equals(div)){	// 자원 정보 볼때 
				readonly = "readonly";
				view = "hidden";
				if("asset_list".equals(mode)) { //관리자 리스트로 이동
%>
					<img src="../am/images/bt_list.gif" onClick="go_mlist()" style='cursor:hand' align='absmiddle'>		
<%				}else {	%>
					<img src="../am/images/bt_previous.gif" onClick="history.back()" style='cursor:hand' align='absmiddle'>
<%				}
		} else if("modify".equals(div)){ // 자원 정보 수정시 
				hrs = buy_date;
%>
				<img src="../am/images/bt_save.gif" onClick="javascript:chk()" style='cursor:hand' align='absmiddle'>
				<img src='../am/images/bt_cancel.gif' onClick="history.go(-1)" border='0' align='absmiddle' style='cursor:hand'>
				<INPUT type='hidden' name='div' value='modify'>
				<INPUT type='hidden' name='mode' value='asset_manager'>
				<INPUT type='hidden' name='as_no' value='<%=as_no%>'>
<%		} else if("delete".equals(div)){ // 자원정보 삭제확인시	%>			
				<img src='../am/images/bt_del.gif' onClick="javascript:as_delete()" border='0' align='absmiddle' style='cursor:hand'>
				<img src='../am/images/bt_cancel.gif' onClick="history.go(-1)" border='0' align='absmiddle' style='cursor:hand'>

<%		} else if("delete_view".equals(div)){	//페기자산 상세정보 보기시	%>
				<img src='../am/images/bt_list.gif' onClick="go_dellist()"  border='0' align='absmiddle' style='cursor:hand'>
				<a href="javascript:repair('<%=as_mid%>','<%=as_name%>','<%=as_no%>')"><img src='../am/images/bt_restore.gif' border='0' align='absmiddle' alt='복원'></a>
<%		}	%>
		</TD></TR></TBODY></TABLE></TD></TR>
		
</TABLE>

<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>

<%		if("delete".equals(div)) {	%>
	<INPUT type='hidden' name='as_no' value='<%=as_no%>'>
	<INPUT type='hidden' name="div" value="delete_process">
	<INPUT type='hidden' name="mode" value="asset_manager">
	<TR><TD height="25" colspan="4"><img src="../am/images/delam_info.gif" width="209" height="25" border="0"></TD></TR>
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR>
	   <TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">폐기형태</TD>
	   <TD width="35%" height="25" class="bg_04" >
			<SELECT name="del_form">
				<option value="매각">매각</option>
				<option value="기증">기증</option>
				<option value="장비노후">장비노후</option>
				<option value="파손">파손</option>
			</SELECT></TD>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">폐기날짜</TD>
		<TD width="35%" height="25" class="bg_04" >
			<INPUT type="text" name="as_except_day" size=10  value='<%=hrs%>'><a href="javascript:wopen('../am/admin/Calendar.jsp?FieldName=as_except_day', '', 180, 250);">
			<img src='../am/images/bt_calendar.gif' border='0' align='absmiddle'></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">폐기사유</TD>
	   <TD width="85%" height="25" class="bg_04" colspan='3'>
			<textarea name="as_except_reason" rows="3" cols="60" ></textarea></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR>
	   <TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">폐기근거</TD>
	   <TD width="85%" height="25" class="bg_04"  colspan='3'><INPUT type=text name="del_reason" size=40></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD height="10" colspan="4"></TD></TR>
<%		} //end if("delete".equals(div))	%>	
<%
	/*******************
	 * 정보등록 및 수정시 
	 *******************/
	if("input".equals(div) || "modify".equals(div)) {
		if(as_each_dc.equals("") || as_each_dc.equals(null) || as_each_dc.equals("null")){
			as_each_dc="0";
		}
%>
	<!-- 자산 정보 -->
	<!--
	<TR><TD height="25" colspan="4"><img src="../am/images/asset_info.gif" width="209" height="25" border="0"></TD></TR>-->
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산분류</TD>
		<TD width="35%" height="25" class="bg_04">
			<select name='c_no'  onChange="javscript:ct_change('<%=div%>','<%=as_no%>')" class='text_01'>
				<%=sb%>
			</select>
			<%	if(!cno.equals("")){	%>
				<script language='javascript'>
					document.asContForm.c_no.value = '<%=cno%>';
				</script>
			<%	}	%>		
		</TD>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산번호</TD>
		<TD width="35%" height="25" class="bg_04"><INPUT type="text" name="as_mid" maxlength="20" size="20"  value='<%=as_mid%>' readOnly></TD></TR>
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">모델명</TD>
		<TD width="35%" height="25" class="bg_04"><INPUT type="text" name="model_name" maxlength="20" size="20"   class='text_01' value=<%=model_name%>></TD>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">고유번호</TD>
		<TD width="35%" height="25" class="bg_04"><INPUT type="text" name="as_serial" maxlength="20" size="20"    class='text_01' value="<%=as_serial%>"></TD></TR>
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">규격</TD>
		<TD width="35%" height="25" class="bg_04" colspan=3><textarea name="as_setting" rows="3" cols="70"  ><%=as_setting%></textarea><br>* 사양,크기,제원 등의 정보를 상세히 기록하십시요.</TD></TR>
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR>
		<%	/// 수정시 field ///
			if("modify".equals(div)) {  
		%>		
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">가용상태</TD>
		<TD width="35%" height="25" class="bg_04">
			<SELECT name='as_status'  class='text_01'>
				<OPTION value='6'>정상</OPTION>
				<OPTION value='10'>폐기</OPTION>
				<OPTION value='13'>수리</OPTION>
			</SELECT>
			<%	if(!as_status.equals("")) {
			%>		<script language="javascript">
					   document.asContForm.as_status.value = "<%=as_status%>";
					</script>
			<%	}	%>
		</TD>
		<%}%>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">반출가능여부</TD>
		<TD width="35%" height="25" class="bg_04">
			<SELECT name="handle"  class='text_01'>
				<OPTION value="y">가능</OPTION>
				<OPTION value="n"'>불능</OPTION>
			</SELECT>
			<%	if(!handle.equals("")) {	%>
					<script language='javascript'>
						document.asContForm.handle.value="<%=handle%>";
					</script>
			<%	}	%>		
		</TD></TR>
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">제조업체명</TD>
		<TD width="35%" height="25" class="bg_04"><INPUT type="text" name="as_maker" maxlength="20" size="20"    value="<%=as_maker%>"></TD>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산관리자</TD>
		<TD width="35%" height="25" class="bg_04"><INPUT type="text" name="crr_name" maxlength="20" size="20" value="<%=crr_name%>"  readonly> <a href="javascript:searchSabun('2');"><img src='../am/images/bt_search.gif' border='0' align='absmiddle'></a></TD></TR>
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR><TD height="5" colspan="4"></TD></TR>

	<!-- 구매정보 -->
	<TR><TD height="25" colspan="4"><img src="../am/images/buy_info.gif" width="209" height="25" border="0"></TD></TR>
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">구매추진자</TD>
		<TD width="35%" height="25" class="bg_04" ><INPUT type="text" name="b_name" maxlength="20" size="20" value="<%=b_name%>"  readonly> <a href="javascript:searchSabun('1');"><img src='../am/images/bt_search.gif' border='0' align='absmiddle'></a></TD>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">구매일자</TD>
		<TD width="35%" height="25" class="bg_04">
			<INPUT type=text name="buy_date" size=12 maxlength=12 value='<%=hrs.substring(0,4)%>-<%=hrs.substring(4,6)%>-<%=hrs.substring(6,8)%>' readonly>
			<a href="javascript:wopen('../am/admin/Calendar.jsp?FieldName=buy_date', '', 180, 250);">
			<img src='../am/images/bt_calendar.gif' border='0' align='absmiddle'></TD></TR>
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">구매금액</TD>
		<TD width="35%" height="25" class="bg_04"><INPUT type="text" name="as_price" size="10"  value="<%=nf.parse(as_price)%>" style="text-align:right" class='text_01'> 원</TD>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">감가상각비율</TD>
		<TD width="35%" height="25" class="bg_04"><INPUT type="text" name="as_each_dc" size="3"  value="<%=as_each_dc%>" class='text_01'> %</TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD height="10" colspan="4"></TD></TR>
				
	<!-- 업체 정보 -->
	<TR><TD height="25" colspan="4"><img src="../am/images/buycompany_info.gif" width="209" height="25" border="0"></TD></TR>
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">구입처명</TD>
		<TD width="35%" height="25" class="bg_04"><INPUT type="text" name="buy_where" maxlength="20" size="20" value="<%=buy_where%>"  class='text_01'></TD>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">전화번호</TD>
		<TD width="35%" height="25" class="bg_04"><INPUT type="text" name="bw_tel" maxlength="20" size="20"  value="<%=bw_tel%>" class='text_01'></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">담당자</TD>
	    <TD width="35%" height="25" class="bg_04"><INPUT type="text" name="bw_employee" maxlength="20" size="20"  value="<%=bw_employee%>" class='text_01'></TD>
	    <TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">담당자연락처</TD>
	    <TD width="35%" height="25" class="bg_04"><INPUT type="text" name="bw_mgr_tel" maxlength="20" size="20" value="<%=bw_mgr_tel%>" class='text_01'></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">주소</TD>
		<TD width="35%" height="25" colspan="3" class="bg_04"><INPUT type="text" name="bw_address" maxlength="50" size="50"  class='text_01' value=<%=bw_address%>></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD height="10" colspan="4"></TD></TR>
			

	<!-- 비고 -->
	<TR><TD height="25" colspan="4"><img src="../am/images/gita_info.gif" width="209" height="25" border="0"></TD></TR>
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">관련문서</TD>
	    <TD width="35%" height="25" class="bg_04" colspan=3>
		<%	if (enableupload > 0){	%>
			<%=file_stat%>
		<%		if(i < enableupload){	%>
					<INPUT type=file name=attachfile<%=i%> onClick='fileadd_action<%=i%>()' size="50"><FONT id=id<%=i%>></FONT>
		<%		} else if(i == enableupload){	%>
					<INPUT type=file name=attachfile<%=i%> size="50"><FONT id=id<%=i%>></FONT>
		<%		}
			}
		%></TD></TR>
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">특이사항</TD>
		<TD width="35%" height="25" class="bg_04" colspan=3><textarea name="etc" rows="2" cols="60"><%=etc%></textarea></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">등록자</TD>
		<TD width="85%" height="25" class="bg_04" colspan='3' <%=readonly%>>
				<INPUT type="text" name="w_name" maxlength="20" size="20" value="<%=w_name%>" readonly></TD>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD height="10" colspan="4"></TD></TR></TABLE>
<INPUT type="hidden" name="w_id"	 maxlength="20" size="20" value="<%=w_id%>">
<INPUT type="hidden" name="w_rank"	 maxlength="20" size="20" value="<%=w_rank%>">
<INPUT type="hidden" name="dc_count" maxlength="20" size="20" value="<%=dc_count%>">
<INPUT type="hidden" name="crr_id"	 maxlength="20" size="20" value="<%=crr_id%>">
<INPUT type="hidden" name="crr_rank" maxlength="20" size="20" value="<%=crr_rank%>">
<INPUT type="hidden" name="as_value" maxlength="20" size="20" value="<%=as_value%>">
<INPUT type="hidden" name="b_id"	 maxlength="20" size="20" value="<%=b_id%>">
<INPUT type="hidden" name="b_rank"	 maxlength="20" size="20" value="<%=b_rank%>">
	
<%
	/*************
	 * 상세정보 보기
	 *************/
	}else if("view".equals(div) || "delete".equals(div) || "delete_view".equals(div)) {
		if("delete_view".equals(div)){
%>
	<!--
	<TR><TD height="25" colspan="4"><img src="../am/images/delam_info.gif" width="209" height="25" border="0"></TD></TR>-->
	<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
	<TR>
	   <TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">폐기형태</TD>
	   <TD width="85%" height="25" colspan="3" class="bg_04"><%=del_form%></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR>
	   <TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">폐기사유</TD>
	   <TD width="85%" height="25" colspan="3" class="bg_04"><%=as_except_reason%></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR>
	   <TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">폐기근거</TD>
	   <TD width="85%" height="25" colspan="3" class="bg_04"><%=del_reason%></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">폐기날짜</TD>
		<TD width="85%" height="25" colspan="3" class="bg_04"><%=as_except_day%></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD height="10" colspan="4"></TD></TR>
<%		} //end if("delete_view".equals(div))	%>
	<!--
	<TR>
		<TD height="25" colspan="4"><img src="../am/images/asset_info.gif" width="209" height="25" border="0"></TD></TR>-->
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산번호</TD>
		<TD width="35%" height="25" class="bg_04"><%=as_mid%></TD>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">모델명</TD>
		<TD width="35%" height="25" class="bg_04"><%=model_name%></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">분류명</TD>
		<TD width="35%" height="25" class="bg_04" ><%=sb%></TD>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">품목명</TD>
		<TD width="35%" height="25" class="bg_04"><%=as_name%></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">고유번호</TD>
		<TD width="35%" height="25" class="bg_04" ><%=as_serial%></TD>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">제조업체명</TD>
		<TD width="35%" height="25" class="bg_04"><%=as_maker%></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">규격</TD>
		<TD width="85%" height="25" class="bg_04" colspan='3'>
			<textarea name="as_setting" rows="5" cols="70" readonly ><%=as_setting%></textarea></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산관리자</TD>
		<TD width="85%" height="25" class="bg_04" colspan='3'><%=crr_name%><INPUT type="hidden" name="crr_id" maxlength="20" size="20" value="<%=crr_id%>"><INPUT type="hidden" name="crr_rank" maxlength="20" size="20" value="<%=crr_rank%>"></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD height="10" colspan="4"></TD></TR>
		
	<!-- 구매정보 -->
	<TR><TD height="25" colspan="4"><img src="../am/images/buy_info.gif" width="209" height="25" border="0"></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR>
        <TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">구매추진자</TD>
        <TD width="35%" height="25" class="bg_04"><%=b_name%></TD>
			<INPUT type="hidden" name="b_id" maxlength="20" size="20" value="<%=b_id%>">
			<INPUT type="hidden" name="b_rank" maxlength="20" size="20" value="<%=b_rank%>">
	    <TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">구매일자</TD>
        <TD width="35%" height="25" class="bg_04" ><%=buy_date.substring(0,4)%>년<%=buy_date.substring(4,6)%>월<%=buy_date.substring(6,8)%>일</TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">구매금액</TD>
        <TD width="35%" height="25" class="bg_04"><%=nf.format(nf.parse(as_price))%> 원</TD>
        <TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">감가적용기준일</TD>
        <TD width="35%" height="25" class="bg_04"><%=apply_dcdate%></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">감가상각비율</TD>
        <TD width="35%" height="25" class="bg_04"><%=as_each_dc%> %</TD>
        <TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산가치</TD>
        <TD width="35%" height="25" class="bg_04"><%=nf.format(nf.parse(as_value))%> 원</TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD height="10" colspan="4"></TD></TR>

<!-- 업체 정보 -->
	<TR><TD height="25" colspan="4"><img src="../am/images/buycompany_info.gif" width="209" height="25" border="0"></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR>
        <TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">구입처명</TD>
        <TD width="35%" height="25" class="bg_04"><%=buy_where%></TD>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">전화번호</TD>
        <TD width="35%" height="25" class="bg_04"><%=bw_tel%></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">담당자</TD>
        <TD width="35%" height="25" class="bg_04"><%=bw_employee%></TD>
		<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">담당자연락처</TD>
        <TD width="35%" height="25" class="bg_04"><%=bw_mgr_tel%></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">주소</TD>
        <TD width="85%" height="25" colspan="3" class="bg_04"><%=bw_address%></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD height="10" colspan="4"></TD></TR>
		
	<!-- 비고 -->
	<TR><TD height="25" colspan="4"><img src="../am/images/gita_info.gif" width="209" height="25" border="0"></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR>
        <TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">관련문서</TD>
        <TD width="85%" height="25" class="bg_04" colspan='3'>
<%				
			arry = new ArrayList();
			asInfoTable2 = new com.anbtech.am.entity.AsInfoTable();
			arry = (ArrayList)request.getAttribute("assetfile");
			file_iter2 = arry.iterator();

			// ---- 첨부파일 ----
			int k = 1;
			while(file_iter2.hasNext()){
				asInfoTable2 = (AsInfoTable)file_iter2.next();
%>				<a href="../servlet/AssetServlet?mode=asset_form&div=download&file_umask=<%=asInfoTable2.getFileUmask()%>&as_no=<%=as_no%>_<%=k%>"       onMouseOver="window.status='Download <%=asInfoTable2.getFileName()%> (<%=asInfoTable2.getFileSize()%>bytes)';return true;" onMouseOut="window.status='';return true;" ><%=asInfoTable2.getFileName()%> (<%=asInfoTable2.getFileSize()%>) bytes
				<br>
<%				k++;
			}
%>
		    </TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">특이사항</TD>
		<TD width="85%" height="25" class="bg_04" colspan='3'><%=etc%></TD>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TR>
	<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">등록자</TD>
	    <TD width="85%" height="25" class="bg_04" colspan='3'><%=w_name%>
			<INPUT type="hidden" name="w_id" maxlength="20" size="20" value="<%=w_id%>">
			<INPUT type="hidden" name="w_rank" maxlength="20" size="20" value="<%=w_rank%>"></TD></TR>
	<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	<TR><TD height="10" colspan="4"></TD></TR>
<%	} //end if	%>

</TABLE>
</FORM>
</BODY>
</HTML>

<script language="javascript">

	var f = document.asContForm
	//  정보 체크
	function chk(){
		if(f.c_no.value=="")		{ alert("카테고리를 입력하십시오"); f.c_no.focus();   return; }
		if(f.as_serial.value=="")	{ alert("장비 고유번호를 입력하십시오"); f.as_serial.focus();  return; } 
		if(f.model_name.value=="")	{ alert("모델명을 입력하십시오"); f.model_name.focus();  return; }	
		if(f.as_price.value=="")	{ alert("구입금액을 입력하십시오"); f.as_price.focus();  return; }
		if(f.buy_where.value=="")   { alert("구입회사(처)를 입력하십시오"); f.buy_where.focus();  return; }
		if(f.bw_tel.value=="")		{ alert("구입회사(처) 연락처를 입력하십시오"); f.bw_tel.focus(); return; }
		if(f.bw_employee.value=="") { alert("담당자를 입력하십시오"); f.bw_employee.focus();  return; }
		if(f.bw_mgr_tel.value=="")  { alert("담당자 연락처를 입력하십시오"); f.bw_mgr_tel.focus();  return; }
		if(f.bw_address.value=="")  { alert("구입회사 주소를 입력하십시오"); f.bw_address.focus();  return; }

		if(f.as_each_dc.value=="" || f.as_each_dc.value=="1"){	alert("감가상각 비율을 다시 입력해 주십시오"); f.as_each_dc.focus(); return;}

		var buy_date = f.buy_date.value
			
		var datefield = buy_date.split("-")
			buy_date =  datefield[0]+datefield[1]+datefield[2];
		f.buy_date.value =  buy_date;
		
		f.submit();
	}

	// 자산 폐기
	function as_delete() {
		
		if(confirm("폐기 시키겠습니까?")) {
			if(f.as_except_reason.value==""){ alert("폐기 사유를 기입하십시오."); return;	}
			f.submit();
		} else { return;
		}
	}

	// 이관 or 반출 신청 FORM 으로  분기
	function req_as_move() {
		location.href ="../servlet/AssetServlet?mode=user_moving_req&as_no=<%=as_no%>";
	}


	// reload
	function ct_change(div,as_no) {

		var c_no = f.c_no.value
	
		if(div=="modify"){	location.href ="../servlet/AssetServlet?mode=asset_form&div="+div+"&c_no="+c_no +"&as_no="+as_no;
		} else {  			location.href ="../servlet/AssetServlet?mode=asset_form&div="+div+"&c_no="+c_no ;}
	}

	//대상자 찾기
	function searchSabun(para)
	{
		var to
		if(para==1) {
			to = "asContForm.b_name/asConForm.b_id";
		} else if(para==2) {
			to = "asContForm.crr_name/asContForm.crr_id";
		}

		wopen("../am/admin/searchSabun.jsp?target="+to,"user","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

	}

	function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

	function list_go() {
		location.href ="../servlet/AssetServlet?mode=user_asset_list";				
	}

	function go_mlist() {
		location.href ="../servlet/AssetServlet?mode=asset_list";				
	}
	function go_dellist(){
		location.href ="../servlet/AssetServlet?mode=asset_del_list";				
	}

	// 수정및 삭제폼으로 이동
	function go_form(div,as_no,c_no){
		location.href ="../servlet/AssetServlet?mode=asset_form&div="+div+"&as_no="+as_no+"&c_no="+c_no;
	}

	// 복원
	function repair(as_mid,as_name,as_no){
	
		if(confirm("<%=as_name%>(<%=as_mid%>)를 복원하시겠습니까?")){

		location.href="../servlet/AssetServlet?mode=asset_repair&as_no="+as_no;
		} else {
			return
		}
	
	}
</script>