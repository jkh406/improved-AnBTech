<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage = "../../admin/errorpage.jsp"
%>
<%@ page import="java.util.*,com.anbtech.board.entity.*"%>
<%@	page import="com.anbtech.admin.SessionLib"%>
<%!
	Board_Env board_env;
	Table table;
	Redirect redirect;
%>
<%
	/*********************************************************************
	 	사용자 정보 가져오기
	*********************************************************************/
	SessionLib sl;
	sl = (SessionLib)session.getValue(session.getId());

	String id = sl.id; 			//접속자 ID
	String name = sl.name;			//접속자 이름
	String passwd = sl.passwd;		//접속자 패스워드
	String division = sl.division;		//접속자 부서

%>
<%
	//board_env에서 가져오기 필요없는 것은 //처리
	board_env = new Board_Env();
	board_env = (Board_Env)request.getAttribute("Board_Env");
	String tablename = board_env.getTablename();

	//스킨부분
	String skin = board_env.getSkin();
	String t_width = board_env.getT_width();
	int t_border = board_env.getT_border();
	String t_topbgcolor = board_env.getT_topbgcolor();
	String t_rowbgcolor = board_env.getT_rowbgcolor();
	String t_rowbgcolor_o = board_env.getT_rowbgcolor_o();
	String t_tinybgcolor = board_env.getT_tinybgcolor();

	//기능부분

	int l_maxsubjectlen = board_env.getL_maxsubjectlen();
	int enablecategory = board_env.getEnablecategory();
	int enableupload = board_env.getEnableupload();
	int upload_size = board_env.getUpload_size();

	//추가부분
	String mode = board_env.getMode();
	String mode_print = "";
	if("write".equals(mode)){
		mode_print = "새 글을 씁니다.";
	}else if("reply".equals(mode)){
		mode_print = "답글을 씁니다.";
	}else{
		mode_print = "썼던 글을 수정합니다.";
	}
	String mapping = board_env.getMapping();

	//redirect에서 가져오기
	redirect = new Redirect();
	redirect = (Redirect)request.getAttribute("Redirect");
	String input_hidden = redirect.getInput_hidden();

	//table_list에서 가져오기

	table = new Table();
	table = (Table)request.getAttribute("Table");
	String no = table.getNo();
	String thread = table.getThread();
	String depth = table.getDepth();
	String pos = table.getPos();
	String writer = table.getWriter();
	String email = table.getEmail();
	String homepage = table.getHomepage();
	String subject = table.getSubject();
	String content = table.getContent();
	String category = table.getCategory();
	String chk_html = table.getHtml();
	String chk_email_forward = table.getEmail_forward();

	//category_list에서 가져오기
	ArrayList category_list = new ArrayList();
	category_list = (ArrayList)request.getAttribute("Category_List");
	Iterator category_iter = category_list.iterator();
%>
<script language=JavaScript>
<!--
function checkValue(){
<%
	if(enablecategory > 0 && ("".equals(category)||!"reply".equals(mode))){//카테고리 사용시 등록하게
%>
	var myindex=document.writeForm.category[1].selectedIndex;
	if(myindex<1){
		alert('카테고리를 선택하여 주십시요');
		return;
	}
<%
	}
%>
	if(!document.writeForm.subject.value){
		alert('제목을 입력하세요.');
		document.writeForm.subject.focus();
	}else if(!document.writeForm.content.value){
		alert('내용을 입력하세요.');
		document.writeForm.content.focus();
	}else{
		document.writeForm.submit();
	}
}


<%
	int i = 1;

	while(i < enableupload){
		if(i == enableupload-1){
%>
function fileadd_action<%=i%>() {
	id<%=i%>.innerHTML="<br><input class=kissofgod-input type=file name=attachfile<%=i+1%> size=45>"
}
<%
		 break;
		}
%>
function fileadd_action<%=i%>() {
	id<%=i%>.innerHTML="<br><input class=kissofgod-input type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=45><font id=id<%=i+1%>></font>"
}
<%
	i++;
	}
%>
//-->
</script>
<%
	Table file = new Table();

	ArrayList file_list = new ArrayList();
	file_list = (ArrayList)request.getAttribute("File_List");
	Iterator file_iter = file_list.iterator();

	i = 1;

	String file_stat = "";
	while(file_iter.hasNext()){
		file = (Table)file_iter.next();
		file_stat = file_stat + "<input class=kissofgod-input type=file name='attachfile"+i+"' size=45> " + file.getFilename()+" 삭제! <input type=checkbox name = 'deletefile"+i+"' value='delete'><br>";
		i++;
	}
%>
  <TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 
valign="top">
  <TBODY>
    <tr>
      <td valign='top' height="27"><%@ include file="write_top.jsp" %></td>
    </tr>
    <tr>
      <td valign='top' height="32"><%@ include file="write_menu.jsp" %></td>
    </tr>
	<tr>
      <td valign='top' height="100%"><%@ include file="write_content.jsp" %></td>
    </tr>
   </TBODY>
  </table>