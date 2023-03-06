import com.anbtech.pjt.entity.*;
import com.anbtech.pjt.db.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class projectPmCostServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 10;

	/********
	 * 소멸자
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리 (목록보기)
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//현재 접속중인 사용자 아이디 가져오기
		//세션이 종료되었을 경우 로긴 페이지로 강제 이동시킨다.
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	alert('사용자 정보가 없습니다. 초기 화면으로 이동합니다.\n\n이 메시지는 아래와 같은 경우에 나타납니다.\n1.로그인 후 장시간 아무 작업을 하지 않아서 자동 종료된 경우\n2.정상적으로 로그인하지 않고 특정 페이지를 엑세스하려고 시도한 경우');");
			out.println("	top.location.href('" + com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/new/index.html');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;

		//모드 및 현재 페이지 파리미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PCO_PJL":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"1":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//비용 정보 파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"0":Hanguel.toHanguel(request.getParameter("pid"));
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));
		String user_id = Hanguel.toHanguel(request.getParameter("user_id"))==null?"":Hanguel.toHanguel(request.getParameter("user_id"));
		String user_name = Hanguel.toHanguel(request.getParameter("user_name"))==null?"":Hanguel.toHanguel(request.getParameter("user_name"));
		String cost_type = Hanguel.toHanguel(request.getParameter("cost_type"))==null?"":Hanguel.toHanguel(request.getParameter("cost_type"));
		String node_cost = Hanguel.toHanguel(request.getParameter("node_cost"))==null?"":Hanguel.toHanguel(request.getParameter("node_cost"));
		String exchange = Hanguel.toHanguel(request.getParameter("exchange"))==null?"":Hanguel.toHanguel(request.getParameter("exchange"));
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));
		String remark = Hanguel.toHanguel(request.getParameter("remark"))==null?"":Hanguel.toHanguel(request.getParameter("remark"));
		
		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setPjtword(pjtWord);				//과제상태
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
		para_list.add(table);
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	비용관리정보 LIST가져오기
			//--------------------------------------------------------------------
			//비용관리정보 LIST가져오기 : summary Text
			if("PCO_SMLT".equals(mode)){	
				com.anbtech.pjt.db.pjtPmCostDAO costDAO = new com.anbtech.pjt.db.pjtPmCostDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//해당멤버의 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//해당과제 비용 전체 summary : 비용(예산/실적)
				ArrayList scost_list = new ArrayList();
				scost_list = costDAO.getPjtRead(pjt_code,sWord);
				request.setAttribute("SCOST_List", scost_list);
	
				//해당과제 진행현황 : 실적기간
				ArrayList node_list = new ArrayList();
				node_list = costDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectPmCost_summaryText.jsp").forward(request,response);
			}
			//비용관리정보 LIST가져오기 : summary Graph
			else if("PCO_SMLG".equals(mode)){	
				com.anbtech.pjt.db.pjtPmCostDAO costDAO = new com.anbtech.pjt.db.pjtPmCostDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//해당멤버의 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//해당과제 비용 전체 summary : 비용(예산/실적)
				ArrayList scost_list = new ArrayList();
				scost_list = costDAO.getPjtRead(pjt_code,sWord);
				request.setAttribute("SCOST_List", scost_list);
	
				//해당과제 진행현황 : 실적기간
				ArrayList node_list = new ArrayList();
				node_list = costDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//해당과제 진행현황 : 실적기간
				ArrayList gcost_list = new ArrayList();
				gcost_list = costDAO.getAccountNumber(pjt_code);
				request.setAttribute("GCOST_List", gcost_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectPmCost_summaryGraph.jsp").forward(request,response);
			}
			//개별 실적비용 보기 : 개별상세보기
			else if("PCO_V".equals(mode)){	
				com.anbtech.pjt.db.pjtPmCostDAO costDAO = new com.anbtech.pjt.db.pjtPmCostDAO(con);

				//실적비용 상세내용
				ArrayList ncost_list = new ArrayList();
				ncost_list = costDAO.getCostRead(pid);
				request.setAttribute("NCOST_List", ncost_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectPmCost_view.jsp").forward(request,response);
				
			}
			
	
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	} // doGet()


	/**********************************
	 * post방식으로 넘어왔을 때 처리 (입력,수정,삭제,상신)
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//현재 접속중인 사용자 아이디 가져오기
		//세션이 종료되었을 경우 로긴 페이지로 강제 이동시킨다.
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	alert('사용자 정보가 없습니다. 초기 화면으로 이동합니다.\n\n이 메시지는 아래와 같은 경우에 나타납니다.\n1.로그인 후 장시간 아무 작업을 하지 않아서 자동 종료된 경우\n2.정상적으로 로그인하지 않고 특정 페이지를 엑세스하려고 시도한 경우');");
			out.println("	top.location.href('" + com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/new/index.html');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;
		
		//모드 및 현재 페이지 파리미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PCO_PJL":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"1":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//비용 정보 파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"0":Hanguel.toHanguel(request.getParameter("pid"));
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));
		String user_id = Hanguel.toHanguel(request.getParameter("user_id"))==null?"":Hanguel.toHanguel(request.getParameter("user_id"));
		String user_name = Hanguel.toHanguel(request.getParameter("user_name"))==null?"":Hanguel.toHanguel(request.getParameter("user_name"));
		String cost_type = Hanguel.toHanguel(request.getParameter("cost_type"))==null?"":Hanguel.toHanguel(request.getParameter("cost_type"));
		String node_cost = Hanguel.toHanguel(request.getParameter("node_cost"))==null?"":Hanguel.toHanguel(request.getParameter("node_cost"));
		String exchange = Hanguel.toHanguel(request.getParameter("exchange"))==null?"":Hanguel.toHanguel(request.getParameter("exchange"));
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));
		String remark = Hanguel.toHanguel(request.getParameter("remark"))==null?"":Hanguel.toHanguel(request.getParameter("remark"));
		
		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setPjtword(pjtWord);				//과제상태
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
		para_list.add(table);

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	비용관리정보 LIST가져오기
			//--------------------------------------------------------------------
			//비용관리정보 LIST가져오기 : summary
			if("PCO_SMLT".equals(mode)){	
				com.anbtech.pjt.db.pjtPmCostDAO costDAO = new com.anbtech.pjt.db.pjtPmCostDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//해당멤버의 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//해당과제 비용 전체 summary : 비용(예산/실적)
				ArrayList scost_list = new ArrayList();
				scost_list = costDAO.getPjtRead(pjt_code,sWord);
				request.setAttribute("SCOST_List", scost_list);
	
				//해당과제 진행현황 : 실적기간
				ArrayList node_list = new ArrayList();
				node_list = costDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectPmCost_summaryText.jsp").forward(request,response);
			}
			//비용관리정보 LIST가져오기 : summary Graph
			else if("PCO_SMLG".equals(mode)){	
				com.anbtech.pjt.db.pjtPmCostDAO costDAO = new com.anbtech.pjt.db.pjtPmCostDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//해당멤버의 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//해당과제 비용 전체 summary : 비용(예산/실적)
				ArrayList scost_list = new ArrayList();
				scost_list = costDAO.getPjtRead(pjt_code,sWord);
				request.setAttribute("SCOST_List", scost_list);
	
				//해당과제 진행현황 : 실적기간
				ArrayList node_list = new ArrayList();
				node_list = costDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//해당과제 진행현황 : 실적기간
				ArrayList gcost_list = new ArrayList();
				gcost_list = costDAO.getAccountNumber(pjt_code);
				request.setAttribute("GCOST_List", gcost_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectPmCost_summaryGraph.jsp").forward(request,response);
			}
			//개별상세 내용 보기 : LIST
			else if("PCO_ACLV".equals(mode)){	
				com.anbtech.pjt.db.pjtPmCostDAO costDAO = new com.anbtech.pjt.db.pjtPmCostDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//해당멤버의 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,"pjt_code","");
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//해당과제 비용 전체 summary : 비용(예산/실적)
				ArrayList scost_list = new ArrayList();
				scost_list = costDAO.getPjtRead(pjt_code,sWord);
				request.setAttribute("SCOST_List", scost_list);
	
				//개별계정 비용 실적 List
				ArrayList cost_list = new ArrayList();
				cost_list = costDAO.getCostList(pjt_code,sItem,sWord,page,max_display_cnt);
				request.setAttribute("COST_List", cost_list);
				int Cpage = costDAO.getCurrentPage();	//현재페이지
				int Tpage = costDAO.getTotalPage();		//전체페이지

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectPmCost_detail.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//개별 실적비용 보기 : 개별상세보기
			else if("PCO_V".equals(mode)){	
				com.anbtech.pjt.db.pjtPmCostDAO costDAO = new com.anbtech.pjt.db.pjtPmCostDAO(con);

				//실적비용 상세내용
				ArrayList ncost_list = new ArrayList();
				ncost_list = costDAO.getCostRead(pid);
				request.setAttribute("NCOST_List", ncost_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectPmCost_view.jsp").forward(request,response);
				
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	
	} //doPost()
}



