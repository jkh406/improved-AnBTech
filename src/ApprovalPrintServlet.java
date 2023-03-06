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

public class ApprovalPrintServlet extends HttpServlet {
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

		//모드 및 현재 페이지 파리미터
		String mode = request.getParameter("mode")==null?"informCnt":request.getParameter("mode");
		String page = request.getParameter("page")==null?"1":request.getParameter("page");

		//상세정보 보기시 넘어오는 파라미터
		String pid = request.getParameter("PID")==null?"":request.getParameter("PID");
		
		//검색시에 넘어오는 파라미터들
		

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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_ING").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=ASK_ING").forward(request,response);				
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=REJ_BOX").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=TMP_BOX").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_BOX").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_GEN").forward(request,response);	
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_SER").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=SEE_BOX").forward(request,response);
			}
			//삭제함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("DEL_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=DEL_BOX").forward(request,response);
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

		//모드 및 현재 페이지 파리미터
		String mode = request.getParameter("mode")==null?"informCnt":request.getParameter("mode");
		String page = request.getParameter("page")==null?"1":request.getParameter("page");

		//상세정보 보기시 넘어오는 파라미터
		String pid = request.getParameter("PID")==null?"":request.getParameter("PID");

		//검색시에 넘어오는 파라미터들
		

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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_ING").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=ASK_ING").forward(request,response);
				
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=REJ_BOX").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=TMP_BOX").forward(request,response);
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

				getServletContext().getRequestDispatcher("gw/approval/eleApproval_print.jsp?PROCESS=APP_BOX").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_GEN").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_SER").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=SEE_BOX").forward(request,response);
			}
			//삭제함 : 전자결재 각항목별 전체LIST 보기 모드 
			else if ("DEL_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//상세내용
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//결재선
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=DEL_BOX").forward(request,response);
				
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);			//con소멸
		}
	}
}