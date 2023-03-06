import com.anbtech.gw.entity.*;
import com.anbtech.gw.db.*;
import com.anbtech.dbconn.DBConnectionManager;
//import com.anbtech.gw.business.*;
import com.anbtech.text.Hanguel;
import com.anbtech.admin.SessionLib;

import com.oreilly.servlet.MultipartRequest;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ApprovalDetailServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;

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
		String mode = request.getParameter("mode")==null?"informCnt":request.getParameter("mode");
		String page = request.getParameter("page")==null?"1":request.getParameter("page");

		//상세정보 보기시 넘어오는 파라미터
		String pid = request.getParameter("PID")==null?"":request.getParameter("PID");

		//첨부파일 삭제시에 넘어오는 파라미터들 (from eleAproval_Rewrite.jsp)
		String file1 = request.getParameter("file1"); if(file1 == null) file1 = ""; //첨부1
		String file2 = request.getParameter("file2"); if(file2 == null) file2 = ""; //첨부2
		String file3 = request.getParameter("file3"); if(file3 == null) file3 = ""; //첨부3

		try {
			// connection 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//-------------------------------------
			// 각 항목별 상세 보기
			//-------------------------------------
			//미결함 : 전자결재 각항목별 전체LIST 보기 모드 
			if ("APP_ING".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);	
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_ING").forward(request,response);
			}
			//진행함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("ASK_ING".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=ASK_ING").forward(request,response);
				
			}
			//반려함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("REJ_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=REJ_BOX").forward(request,response);
			}
			//임시저장함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("TMP_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=TMP_BOX").forward(request,response);
			}
			//기결함 전체 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_BOX".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();	
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_BOX").forward(request,response);
			}
			//기결함 일반 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_GEN".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_GEN").forward(request,response);
			}
			//기결함 고객관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_SER".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_SER").forward(request,response);
			}
			//기결함 외출계 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_OUT".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_OUT").forward(request,response);
			}
			//기결함 출장신청서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_BTR".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_BTR").forward(request,response);
			}
			//기결함 휴가원 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_HDY".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_HDY").forward(request,response);
			}
			//기결함 배차신청 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_CAR".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_CAR").forward(request,response);
			}
			//기결함 보고서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_REP".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_REP").forward(request,response);
			}
			//기결함 출장보고서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_BRP".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_BRP").forward(request,response);
			}
			//기결함 기안서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_DRF".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_DRF").forward(request,response);
			}
			//기결함 명함신청서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_CRD".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_CRD").forward(request,response);
			}
			//기결함 사유서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_RSN".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_RSN").forward(request,response);
			}
			//기결함 협조전 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_HLP".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_HLP").forward(request,response);
			}
			//기결함 연장근무신청서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_OTW".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_OTW").forward(request,response);
			}
			//기결함 구인의뢰서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_OFF".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_OFF").forward(request,response);
			}
			//기결함 교육일지 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_EDU".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_EDU").forward(request,response);
			}
			//기결함 승인원 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_AKG".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_AKG").forward(request,response);
			}
			//기결함 기술문서 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_TD".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_TD").forward(request,response);
			}
			//기결함 공지공문 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_ODT".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_ODT").forward(request,response);
			}
			//기결함 사내공문 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_IDS".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_IDS").forward(request,response);
			}
			//기결함 사외공문 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_ODS".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_ODS").forward(request,response);
			}
			//기결함 자산관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_AST".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_AST").forward(request,response);
			}
			//기결함 견적관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_EST".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_EST").forward(request,response);
			}
			//기결함 특근관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_EWK".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_EWK").forward(request,response);
			}
			//기결함 BOM관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_BOM".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_BOM").forward(request,response);
			}
			//기결함 설계변경관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_DCM".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_DCM").forward(request,response);
			}
			//기결함 구매요청관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_PCR".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_PCR").forward(request,response);
			}
			//기결함 발주요청관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_ODR".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_ODR").forward(request,response);
			}
			//기결함 구매입고관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_PWH".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_PWH").forward(request,response);
			}
			//기결함 부품출고관리 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("APP_TGW".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_TGW").forward(request,response);
			}
			//통보함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("SEE_BOX".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);	
				
				saveDAO.setReadInform(login_id);					//통보문서 보았음을 확인하기

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=SEE_BOX").forward(request,response);
			}
			//삭제함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("DEL_BOX".equals(mode)){
				com.anbtech.gw.db.AppStoreHouseDetailDAO houseDAO = new com.anbtech.gw.db.AppStoreHouseDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = houseDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = houseDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=DEL_BOX").forward(request,response);
				
			}
			//출력하기
			else if ("APP_PNT".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);	
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_PNT").forward(request,response);
			}
			//관련문서 LINK정보 보기
			else if ("APP_LNK".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = saveDAO.getTable_line();	
				request.setAttribute("Table_Line",table_line);	
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_LNK").forward(request,response);
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);			//con소멸
			out.close();
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
		String mode = request.getParameter("mode")==null?"informCnt":request.getParameter("mode");
		String page = request.getParameter("page")==null?"1":request.getParameter("page");

		//상세정보 보기시 넘어오는 파라미터
		String pid = request.getParameter("PID")==null?"":request.getParameter("PID");

		//첨부파일 삭제시에 넘어오는 파라미터들 (from eleAproval_Rewrite.jsp)
		String file1 = request.getParameter("file1"); if(file1 == null) file1 = ""; //첨부1
		String file2 = request.getParameter("file2"); if(file2 == null) file2 = ""; //첨부2
		String file3 = request.getParameter("file3"); if(file3 == null) file3 = ""; //첨부3

		try {
			// connection 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//-------------------------------------
			// 각 항목별 상세 보기
			//-------------------------------------
			
			//전자결재 재작성하기 보기
			if("REW".equals(mode)) {
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/SpaceLink/general_FP_Rewrite.jsp").forward(request,response);
			}

			//전자결재 재작성 첨부파일 삭제하기
			else if("DELFILE".equals(mode)) {
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				//첨부파일 삭제하기 (관련DB update)
				masterDAO.deleteFile(pid,file1,file2,file3);
		
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/SpaceLink/general_FP_Rewrite.jsp").forward(request,response);
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);			//con소멸
			out.close();
		}
	}

}