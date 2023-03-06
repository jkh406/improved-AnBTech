<%@ page language="java" contentType="text/html;charset=euc-kr"%>
<%@ page import="java.util.*,com.anbtech.board.entity.*"%>
<%@ page import="com.anbtech.admin.SessionLib"%>
<%!
	Board_Env board_env;
	Table table;
	Table_Cmt table_cmt;
	Redirect redirect;
	Redirect redirect_bottom;
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
	String v_listmode = board_env.getV_listmode();
	String enablecomment = board_env.getEnablecomment();
	String enablevote = board_env.getEnablevote();
	String enablebagview = board_env.getEnablebagview();
	String adminonly = board_env.getAdminonly();

	//관리자 권한이 있는지 체크
	String owners_id = board_env.getOwnersId();
	boolean is_owner = false;

	ArrayList owners_list = new ArrayList();
	owners_list = com.anbtech.util.Token.getTokenList(owners_id);

	Iterator owners_list_iter = owners_list.iterator();
	while(owners_list_iter.hasNext()){
		if(((String)owners_list_iter.next()).equals(id)){
			is_owner = true;
		}
	}
	//여기까지 권한체크

	int v_defaultheight = board_env.getV_defaultheight();
	int enablecategory = board_env.getEnablecategory();
	int enableupload = board_env.getEnableupload();

	//추가부분
	String mapping = board_env.getMapping();

	//사용할수 있도록 변환 시킨다.
	String mouseover = "";
	String mouseout = "";
	if(t_rowbgcolor_o.length() > 0){
		mouseover = "this.style.backgroundColor='"+t_rowbgcolor_o.substring(8,t_rowbgcolor_o.length())+"'";
		if(t_rowbgcolor.length() > 0) mouseout = "this.style.backgroundColor='"+t_rowbgcolor.substring(8,t_rowbgcolor.length())+"'";
		else mouseout = "this.style.backgroundColor=''";
	}
	//table_cmt_form에서 가져오기
	Table table_cmt_form = new Table();
	table_cmt_form = (Table)request.getAttribute("Table_Cmt_Form");
	String writer_cmt_form = table_cmt_form.getWriter();

	//table_view에서 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_View");
	table = new Table();
	Iterator table_iter = table_list.iterator();

	//redirect_view에서 가져오기
	ArrayList redirect_list = new ArrayList();
	redirect_list = (ArrayList)request.getAttribute("Redirect_View");
	redirect = new Redirect();
	Iterator redirect_iter = redirect_list.iterator();

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
%>

  <TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 
valign="top">
  <TBODY>
	<tr>
      <td valign='top' height="28"><%@ include file="list_top.jsp" %></td>
    </tr>

	<tr>
      <td valign='top' height="100%"><%@ include file="view_content.jsp" %>
<%
	if("y".equals(v_listmode)){
		//table_list에서 가져오기
		table_list = new ArrayList();
		table_list = (ArrayList)request.getAttribute("Table_List");

		table = new Table();
		table_iter = table_list.iterator();
%>

<%@ include file="list_content.jsp" %>
<%
	}
%>
      </td>
    </tr>
  </table>