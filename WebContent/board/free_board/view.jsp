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
	 	����� ���� ��������
	*********************************************************************/
	SessionLib sl;
	sl = (SessionLib)session.getValue(session.getId());

	String id = sl.id; 			//������ ID
	String name = sl.name;			//������ �̸�
	String passwd = sl.passwd;		//������ �н�����
	String division = sl.division;		//������ �μ�

%>

<%
	//board_env���� �������� �ʿ���� ���� //ó��
	board_env = new Board_Env();
	board_env = (Board_Env)request.getAttribute("Board_Env");
	String tablename = board_env.getTablename();

	//��Ų�κ�
	String skin = board_env.getSkin();
	String t_width = board_env.getT_width();
	int t_border = board_env.getT_border();
	String t_topbgcolor = board_env.getT_topbgcolor();
	String t_rowbgcolor = board_env.getT_rowbgcolor();
	String t_rowbgcolor_o = board_env.getT_rowbgcolor_o();
	String t_tinybgcolor = board_env.getT_tinybgcolor();

	//��ɺκ�
	String v_listmode = board_env.getV_listmode();
	String enablecomment = board_env.getEnablecomment();
	String enablevote = board_env.getEnablevote();
	String enablebagview = board_env.getEnablebagview();
	String adminonly = board_env.getAdminonly();

	//������ ������ �ִ��� üũ
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
	//������� ����üũ

	int v_defaultheight = board_env.getV_defaultheight();
	int enablecategory = board_env.getEnablecategory();
	int enableupload = board_env.getEnableupload();

	//�߰��κ�
	String mapping = board_env.getMapping();

	//����Ҽ� �ֵ��� ��ȯ ��Ų��.
	String mouseover = "";
	String mouseout = "";
	if(t_rowbgcolor_o.length() > 0){
		mouseover = "this.style.backgroundColor='"+t_rowbgcolor_o.substring(8,t_rowbgcolor_o.length())+"'";
		if(t_rowbgcolor.length() > 0) mouseout = "this.style.backgroundColor='"+t_rowbgcolor.substring(8,t_rowbgcolor.length())+"'";
		else mouseout = "this.style.backgroundColor=''";
	}
	//table_cmt_form���� ��������
	Table table_cmt_form = new Table();
	table_cmt_form = (Table)request.getAttribute("Table_Cmt_Form");
	String writer_cmt_form = table_cmt_form.getWriter();

	//table_view���� ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_View");
	table = new Table();
	Iterator table_iter = table_list.iterator();

	//redirect_view���� ��������
	ArrayList redirect_list = new ArrayList();
	redirect_list = (ArrayList)request.getAttribute("Redirect_View");
	redirect = new Redirect();
	Iterator redirect_iter = redirect_list.iterator();

	//redirect���� ��������
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
		//table_list���� ��������
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