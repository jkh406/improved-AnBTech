<%@ include file="../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage = "../../admin/errorpage.jsp" 	
%>
<%@ page import="java.util.*,com.anbtech.board.entity.*"%>
<%!
	Board_Env board_env;
	Table table;
	Redirect redirect;
%>
<%
	board_env = new Board_Env();
	board_env = (Board_Env)request.getAttribute("Board_Env");
	String tablename = board_env.getTablename();

	String skin = board_env.getSkin();
	String t_width = board_env.getT_width();
	int t_border = board_env.getT_border();
	String t_topbgcolor = board_env.getT_topbgcolor();
	String t_rowbgcolor = board_env.getT_rowbgcolor();
	String t_rowbgcolor_o = board_env.getT_rowbgcolor_o();
	String t_tinybgcolor = board_env.getT_tinybgcolor();


	String enablevote = board_env.getEnablevote();
	String enablebagview = board_env.getEnablebagview();
	String adminonly = board_env.getAdminonly();
	int enablecategory = board_env.getEnablecategory();
	int enableupload = board_env.getEnableupload();

	String mapping = board_env.getMapping();


	//사용할수 있도록 변환 시킨다.
	String mouseover = "";
	String mouseout = "";
	if(t_rowbgcolor_o.length() > 0){
		mouseover = "this.style.backgroundColor='"+t_rowbgcolor_o.substring(8,t_rowbgcolor_o.length())+"'";
		if(t_rowbgcolor.length() > 0) mouseout = "this.style.backgroundColor='"+t_rowbgcolor.substring(8,t_rowbgcolor.length())+"'";
		else mouseout = "this.style.backgroundColor=''";
	}

	//redirect에서 가져오기
	redirect = new Redirect();
	redirect = (Redirect)request.getAttribute("Redirect");
	String link_login = redirect.getLink_login();
	String link_manager = redirect.getLink_manager();
	String link_multiview = redirect.getLink_multiview();
	String link_write = redirect.getLink_write();
	String input_hidden_search = redirect.getInput_hidden();
	int view_total = redirect.getView_total();
	int view_boardpage = redirect.getView_boardpage();
	int view_totalpage = redirect.getView_totalpage();
	String view_pagecut = redirect.getView_pagecut();

	//table_list에서 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_List");
	table = new Table();
	Iterator table_iter = table_list.iterator();

%>

  <TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 
valign="top">
  <TBODY>
    <tr>
      <td valign='top' height="27"><%@ include file="list_top.jsp" %></td>
    </tr>
    <tr>
      <td valign='top' height="32"><%@ include file="list_menu.jsp" %></td>
    </tr>
	<tr>
      <td valign='top' height="100%"><%@ include file="list_content.jsp" %></td>
    </tr>
   </TBODY>
  </table>