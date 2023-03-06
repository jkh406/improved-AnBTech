import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class PurchaseOtherMgrServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;

	/********
	 * 소멸자
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String mid			= request.getParameter("mid");
		String mode			= request.getParameter("mode");
		String page			= request.getParameter("page");
		String searchword	= request.getParameter("searchword");
		String searchscope	= request.getParameter("searchscope");
		String category		= request.getParameter("category");

		if (mode == null) mode				= "";
		if (page == null) page				= "1";
		if (searchword == null) searchword	= "";
		if (searchscope == null) searchscope= "";
		if (category == null) category		= "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);

		String redirectUrl = "";

		//현재 접속중인 사용자 아이디 가져오기
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			PrintWriter out = response.getWriter();
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;

		//견적의뢰시 필요한 Parameters
		String item_code	= request.getParameter("item_code");

		//과제별 재료비 체크시 필요한 파라미터들
		String request_no	= request.getParameter("request_no");
		String request_type	= request.getParameter("request_type");
		String project_code	= request.getParameter("project_code");
		String total_mount	= request.getParameter("total_mount");
	
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////////////////////
			// 선택된 품목의 견적의뢰 요청 화면
			////////////////////////////////////////////////////
			if(mode.equals("request_estimate")){
				com.anbtech.pu.business.PurchaseOtherMgrBO puotherBO = new com.anbtech.pu.business.PurchaseOtherMgrBO(con);
				com.anbtech.pu.entity.RequestInfoTable table = new com.anbtech.pu.entity.RequestInfoTable();

				ArrayList item_list = new ArrayList();
				item_list = puotherBO.getEstimateItemList(mode,item_code);
				request.setAttribute("ITEM_LIST",item_list);

				//요청자정보
				com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
				com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
				userinfo = userDAO.getUserListById(login_id);
				String requester_email	= userinfo.getEmail();
				
				getServletContext().getRequestDispatcher("/pu/other/request_estimate.jsp?email="+requester_email).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 과제별 재료비 체크 처리
			////////////////////////////////////////////////////
			else if(mode.equals("chk_budget")){
				com.anbtech.psm.business.psmProcessBO psmBO = new com.anbtech.psm.business.psmProcessBO(con);

				String err_message = "";
				
				//과제 진행상태 체크(1:미진행,2:진행,3:재진행,4:완료,5:보류,6취소)
				String pjt_stat = psmBO.checkProjectStatus(project_code);
				if(pjt_stat.equals("1") || pjt_stat.equals("4") || pjt_stat.equals("5") || pjt_stat.equals("6")){
					err_message = "선택하신 과제는 현재 진행중인 과제가 아닙니다. 계속할 수 없습니다.";
				}

				//과제의 잔액 구하기
				double rest_budget = psmBO.getBalanceMaterial(project_code);
				if(rest_budget < Double.parseDouble(total_mount)){
					err_message = "예상발주금액이 선택하신 과제에 책정된 예산잔액을 초과합니다. 계속할 수 없습니다.";
					err_message += "\\n(과제예산잔액:" + rest_budget + ",예상발주금액:" + Double.parseDouble(total_mount) + ")";
				}

				if(err_message.length() > 0){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('" + err_message + "');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;								
				}
				redirectUrl = "../gw/approval/module/pu_PurchaseApp.jsp?mode=request_app_view&request_no="+request_no+"&request_type="+request_type;

			}

			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} // doGet()

	/**********************************
	 * post방식으로 넘어왔을 때 처리
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/pu/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다
	
		//전페이지에서 값 받아온다. multi에서 가져옴
		String mode = multi.getParameter("mode");
		String page = multi.getParameter("page");
		String searchword = multi.getParameter("searchword");
		String searchscope = multi.getParameter("searchscope");
		String category = multi.getParameter("category");

		//공백으로 넘어오거나 null로 온 값들에 대한 처리.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";
			

		String redirectUrl = "";
		//현재 접속중인 사용자 아이디 가져오기
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			PrintWriter out = response.getWriter();
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;

		//견적의뢰 처리시 필요한 Parameters
		String item_code		= multi.getParameter("item_code");
		String sender_email		= multi.getParameter("sender_emial");
		String receiver_email	= multi.getParameter("receiver_email");

		//발주서 이메일 발송시 필요한 Parameters
		String order_no			= multi.getParameter("order_no");
		String req_message		= multi.getParameter("req_message");
		String receiver_name	= multi.getParameter("receiver_name");
		

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/////////////////////////
			// 품목견적의뢰 메일발송 처리
			////////////////////////
			if(mode.equals("request_estimate")){
				com.anbtech.pu.business.PurchaseOtherMgrBO puotherBO = new com.anbtech.pu.business.PurchaseOtherMgrBO(con);

				//메일본문내용 만들기
				String content = puotherBO.makeMailContent(item_code);
				//메일발송 및 결과 메시지 받기
				String result = puotherBO.sendEmail(sender_email,receiver_email,content);

				PrintWriter out = response.getWriter();
				out.println("	<script>");
//				out.println("	alert('"+result+"');");
				out.println("	alert('정상적으로 발송되었습니다.');");
//				out.println("	opener.location.reload();");
				out.println("	self.close();");
				out.println("	</script>");
				out.close();
			}

			////////////////////////////////////////////////////
			// 발주서 이메일 발송 처리
			///////////////////////////////////////////////////
			else if(mode.equals("send_order")){
				com.anbtech.pu.business.PurchaseOtherMgrBO puotherBO = new com.anbtech.pu.business.PurchaseOtherMgrBO(con);

				//메일본문내용 만들기
				String content = puotherBO.makeMailContentforOrderForm(order_no,req_message);
				//메일발송 및 결과 메시지 받기
				String result = puotherBO.sendEmail(sender_email,receiver_email,content);

				PrintWriter out = response.getWriter();
				out.println("	<script>");
//				out.println("	alert('"+result+"');");
				out.println("	alert('정상적으로 발송되었습니다.');");
//				out.println("	opener.location.reload();");
				out.println("	self.close();");
				out.println("	</script>");
				out.close();
			}

			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} //doPost()
}
