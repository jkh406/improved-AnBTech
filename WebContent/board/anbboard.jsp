<%@ page language="java" contentType="text/html;charset=euc-kr" %>
<%! com.anbtech.board.entity.Board_Env board_env; %>


<%
	//board_env에서 가져오기
	board_env = new com.anbtech.board.entity.Board_Env();
	board_env = (com.anbtech.board.entity.Board_Env)request.getAttribute("Board_Env");
	String mode = board_env.getMode();
	String skin = board_env.getSkin();

	if ("reply".equals(mode) || "modify".equals(mode)){
		mode = "write";
	}
	if ("adminlogin".equals(mode) || "delete".equals(mode)){
		mode = "login";
	}
	String top = com.anbtech.admin.db.ServerConfig.getConf("boardpath")+"/"+skin+"/top.jsp";
	String action = com.anbtech.admin.db.ServerConfig.getConf("boardpath")+"/"+skin+"/"+mode + ".jsp";
	String bottom = com.anbtech.admin.db.ServerConfig.getConf("boardpath")+"/"+skin+"/bottom.jsp";
%>
<!-----------------------------------------------------------------------------
------------------------- ANB Tech. Board ver1.0 -------------------------------
-------------------------------------------------------------------------------
프로그램명 : ANBBoard
Homepage : http://www.anbtech.co.kr
------------------------------------------------------------------------------>
<jsp:include page="<%=top%>" flush="true" />
<!----- // Top / Main // ----->
<jsp:include page="<%=action%>" flush="true" />
<!----- // Main / Bottom // ----->
<jsp:include page="<%=bottom%>" flush="true" />