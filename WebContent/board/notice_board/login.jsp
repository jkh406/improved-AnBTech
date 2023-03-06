<%@ page language="java" contentType="text/html;charset=euc-kr" %>
<%@ page import="java.util.*,com.anbtech.board.entity.*"%>
<%!
	Board_Env board_env;
	Redirect redirect;
%>
<%
	//board_env에서 가져오기 필요없는 것은 //처리
	board_env = new Board_Env();
	board_env = (Board_Env)request.getAttribute("Board_Env");
	String tablename = board_env.getTablename();
	//String html_title = board_env.getHtml_title();
	//String html_head = board_env.getHtml_head();
	//String html_tail = board_env.getHtml_tail();

	//스킨부분
	String skin = board_env.getSkin();
	//String html_bgcolor = board_env.getHtml_bgcolor();
	//String html_background = board_env.getHtml_background();
	String t_width = board_env.getT_width();
	int t_border = board_env.getT_border();
	String t_topbgcolor = board_env.getT_topbgcolor();
	String t_rowbgcolor = board_env.getT_rowbgcolor();
	String t_rowbgcolor_o = board_env.getT_rowbgcolor_o();
	String t_tinybgcolor = board_env.getT_tinybgcolor();

	//기능부분
	//String v_listmode = board_env.getV_listmode();
	//String category_items = board_env.getCategory_items();
	//String enablecomment = board_env.getEnablecomment();
	//String enablevote = board_env.getEnablevote();
	//String enablebagview = board_env.getEnablebagview();
	//String adminonly = board_env.getAdminonly();
	//String enablepreview = board_env.getEnablepreview();
	//int l_maxlist = board_env.getL_maxlist();
	//int l_maxpage = board_env.getL_maxpage();
	//int l_maxsubjectlen = board_env.getL_maxsubjectlen();
	//int v_defaultheight = board_env.getV_defaultheight();
	//int enablecategory = board_env.getEnablecategory();
	//int enableupload = board_env.getEnableupload();
	//int upload_size = board_env.getUpload_size();
	//int enablechkcool = board_env.getEnablechkcool();

	//추가부분
	String mode = board_env.getMode();
	String mapping = board_env.getMapping();

	//redirect에서 가져오기
	redirect = new Redirect();
	redirect = (Redirect)request.getAttribute("Redirect");
	String input_hidden = redirect.getInput_hidden();
%>
<%
		if ("adminlogin".equals(mode)){
%>
<%@ include file="login_admin.jsp" %>
<%
		}
		if ("delete".equals(mode)){
%>
<%@ include file="login_delete.jsp" %>
<%
		}
%>