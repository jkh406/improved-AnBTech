import com.anbtech.gw.entity.*;
import com.anbtech.gw.db.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.text.Hanguel;
import com.anbtech.admin.SessionLib;

import com.oreilly.servlet.MultipartRequest;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ApprovalMenuServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 15;

	//소멸자 = con소멸
	public void close(Connection con) throws ServletException {
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;

		//모드 및 현재 페이지 파리미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"APP_ING":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"app_subj":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//삭제문서 보존내용 검색파라미터 추가[문서종류,년도]
		String flag = Hanguel.toHanguel(request.getParameter("flag"))==null?"":Hanguel.toHanguel(request.getParameter("flag"));
		String syear = Hanguel.toHanguel(request.getParameter("syear"))==null?"":Hanguel.toHanguel(request.getParameter("syear"));
		
		try {
			// connection 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			//-------------------------------------
			// 각 항목별 전체 LIST 보기
			//-------------------------------------
			//미결함 : 전자결재 각항목별 전체LIST 보기 모드 
			if ("APP_ING".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_ING",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage(); 

				//부재중 대리결재자 지정되었는지 찾기
				com.anbtech.gw.db.AppAttorneyDAO attDAO = new com.anbtech.gw.db.AppAttorneyDAO(con);
				String attorney_yn = attDAO.checkAttorney(login_id);

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_ING&Tpage="+Tpage+"&Cpage="+Cpage+"&attorney_yn="+attorney_yn).forward(request,response);
			}
			//진행함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("ASK_ING".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"ASK_ING",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=ASK_ING&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//반려함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("REJ_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"REJ_BOX",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=REJ_BOX&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//임시저장함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("TMP_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"TMP_BOX",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=TMP_BOX&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 전체 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_BOX",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_BOX&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 일반 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_GEN".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_GEN",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_GEN&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 고객관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_SER".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_SER",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_SER&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 외출계 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_OUT".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_OUT",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_OUT&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 출장신청서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_BTR".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_BTR",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_BTR&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 휴가원 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_HDY".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_HDY",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_HDY&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 배차신청 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_CAR".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_CAR",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_CAR&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 보고서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_REP".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_REP",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_REP&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 출장보고서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_BRP".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_BRP",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_BRP&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 기안서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_DRF".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_DRF",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_DRF&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 명함신청서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_CRD".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_CRD",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_CRD&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 사유서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_RSN".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_RSN",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_RSN&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 협조전 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_HLP".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_HLP",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_HLP&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 연장근무신청서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_OTW".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_OTW",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_OTW&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 구인의뢰서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_OFF".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_OFF",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_OFF&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 교육일지 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_EDU".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_EDU",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_EDU&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 승인원 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_AKG".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_AKG",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_AKG&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 기술문서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_TD".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_TD",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_TD&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 공지공문 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_ODT".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_ODT",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_ODT&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 사내공문 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_IDS".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_IDS",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_IDS&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 사외공문 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_ODS".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_ODS",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_ODS&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 자산관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_AST".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_AST",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_AST&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 견적관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_EST".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_EST",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_EST&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 특근관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_EWK".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_EWK",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_EWK&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 BOM관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_BOM".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_BOM",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_BOM&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 설계변경관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_DCM".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_DCM",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_DCM&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 구매요청관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_PCR".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_PCR",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_PCR&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 발주요청관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_ODR".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_ODR",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_ODR&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 구매입고관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_PWH".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_PWH",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_PWH&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 부품출고관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_TGW".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_TGW",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_TGW&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//통보함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("SEE_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"SEE_BOX",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=SEE_BOX&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//삭제함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("DEL_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTableDelBoxlist (login_id,flag,syear,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=DEL_BOX&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);			//con소멸
		}
	
	}

	/**********************************
	 * post방식으로 넘어왔을 때 처리
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;

		//모드 및 현재 페이지 파리미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"APP_ING":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"app_subj":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//삭제문서 보존내용 검색파라미터 추가[문서종류,년도]
		String flag = Hanguel.toHanguel(request.getParameter("flag"))==null?"":Hanguel.toHanguel(request.getParameter("flag"));
		String syear = Hanguel.toHanguel(request.getParameter("syear"))==null?"":Hanguel.toHanguel(request.getParameter("syear"));
		
		try {
			// connection 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			//-------------------------------------
			// 각 항목별 전체 LIST 보기
			//-------------------------------------
			//미결함 : 전자결재 각항목별 전체LIST 보기 모드 
			if ("APP_ING".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_ING",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage(); 

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_ING&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//진행함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("ASK_ING".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"ASK_ING",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=ASK_ING&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//반려함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("REJ_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"REJ_BOX",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=REJ_BOX&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//임시저장함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("TMP_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"TMP_BOX",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=TMP_BOX&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 전체 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_BOX",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_BOX&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 일반 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_GEN".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_GEN",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_GEN&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 고객관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_SER".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_SER",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_SER&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 외출계 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_OUT".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_OUT",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_OUT&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 출장신청서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_BTR".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_BTR",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_BTR&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 휴가원 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_HDY".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_HDY",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_HDY&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 배차신청 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_CAR".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_CAR",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기시 양식결재는 PROCESS을 APP_FPP로 통일함
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_CAR&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 보고서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_REP".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_REP",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기시 양식결재는 PROCESS을 APP_FPP로 통일함
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_REP&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 출장보고서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_BRP".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_BRP",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기시 양식결재는 PROCESS을 APP_FPP로 통일함
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_BRP&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 기안서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_DRF".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_DRF",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기시 양식결재는 PROCESS을 APP_FPP로 통일함
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_DRF&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 명함신청서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_CRD".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_CRD",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기시 양식결재는 PROCESS을 APP_FPP로 통일함
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_CRD&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 사유서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_RSN".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_RSN",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기시 양식결재는 PROCESS을 APP_FPP로 통일함
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_RSN&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 협조전 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_HLP".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_HLP",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기시 양식결재는 PROCESS을 APP_FPP로 통일함
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_HLP&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 연장근무신청서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_OTW".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_OTW",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기시 양식결재는 PROCESS을 APP_FPP로 통일함
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_OTW&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 구인의뢰서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_OFF".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_OFF",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기시 양식결재는 PROCESS을 APP_FPP로 통일함
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_OFF&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 교육일지 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_EDU".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_EDU",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기시 양식결재는 PROCESS을 APP_FPP로 통일함
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_EDU&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 승인원 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_AKG".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_AKG",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기시 양식결재는 PROCESS을 APP_FPP로 통일함
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_AKG&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 기술문서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_TD".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_TD",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기시 양식결재는 PROCESS을 APP_FPP로 통일함
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_TD&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 공지공문 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_ODT".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_ODT",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_ODT&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 사내공문 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_IDS".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_IDS",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_IDS&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 사외공문 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_ODS".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_ODS",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_ODS&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 자산관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_AST".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_AST",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_AST&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//기결함 견적관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_EST".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"APP_EST",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				//분기
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=APP_EST&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//통보함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("SEE_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTable_list (login_id,"SEE_BOX",sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=SEE_BOX&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
			//삭제함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("DEL_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = masterDAO.getTableDelBoxlist (login_id,flag,syear,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Table_List", table_list);	
				int Tpage = masterDAO.getTotalPage();
				int Cpage = masterDAO.getCurrentPage();
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=DEL_BOX&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);			//con소멸
		}
	}
}