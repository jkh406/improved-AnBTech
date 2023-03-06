import com.anbtech.ew.entity.*;
import com.anbtech.ew.db.*;
import com.anbtech.ew.business.*;
import com.anbtech.admin.*;
import com.anbtech.admin.entity.*;
import com.anbtech.text.Hanguel;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ExtraWorkServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;

	/********
	 * �Ҹ���
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get������� �Ѿ���� �� ó��
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		// �˻��ÿ� �Ѿ���� �Ķ���͵�
		String mode			= request.getParameter("mode")==null?"":request.getParameter("mode"); 
		String searchword	= Hanguel.toHanguel(request.getParameter("searchword"));
		String searchscope	= request.getParameter("searchscope");
		String category		= request.getParameter("category");
		String tablename	= "";
		String page			= request.getParameter("page");

		if ( page == null) page = "1";
		if ( searchword == null) searchword = "";
		if ( searchscope == null) searchscope = "";
		
		//���� �������� ����� ���̵� ��������
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
		String login_name = sl.name;
		String login_division = sl.division;
		String redirectUrl = "";

		//�ñ������������� �Ķ���͵�
		String div		= request.getParameter("div")==null?"":request.getParameter("div");
		String year		= request.getParameter("y");
		String month	= request.getParameter("m");
		String day		= request.getParameter("d");	
		
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		if (year == null) year = vans.format(now).substring(0,4);
		if (month == null) month = vans.format(now).substring(4,6);
		if (day == null) day = vans.format(now).substring(6,8);		
		
		//�����Ű��� �Ķ���͵�
		String ono_plus = request.getParameter("ono_plus");		//��û���� ������ȣ��,�����ݷ�(;)�α���
		String mno		= request.getParameter("mno");			//ew_master ���̺� ������ȣ
		
		//Ư����Ȳ������� �Ķ���͵�
		String eachid	= request.getParameter("eachid")==null?login_id:request.getParameter("eachid");

		//�μ⺸��� �ʿ��� �Ķ���͵�
		String o_no		= request.getParameter("o_no");			//ew_history ���̺� ������ȣ

		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			///////////////////////////////////////////////
			// ���رٹ��ð����� ���� �� ���� ��
			///////////////////////////////////////////////
			if("standard_wtime_fix".equals(mode)){
				com.anbtech.ew.entity.StandardWorkTimeTable swtimeTable =  new com.anbtech.ew.entity.StandardWorkTimeTable();
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				
				swtimeTable = ewDAO.getStandardWTime();
								
				String temp = ""+swtimeTable.getSwNo();				// ���� ���ؽð� ��������
				if(temp==null || temp.equals("")) div = "first";	// ��ϵ� ���ؽð��� ������ ����

				request.setAttribute("workTime",swtimeTable);
				getServletContext().getRequestDispatcher("/ew/admin/workTimeFix.jsp?div="+div).forward(request,response);
			}

			////////////////////////////////////
			// ���κ� �ð��ܱٹ� �ñ޼��� ����
			////////////////////////////////////
			else if("manager_hourly_pay".equals(mode)){
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = ewDAO.getUserPayInfo(year,div);
				request.setAttribute("Table_List", table_list);

				String url = "/ew/admin/manager_hourly_pay.jsp?y="+year+"&div="+div;
				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			/////////////////////////////////////////////////
			// Ư�ٽ�û ��
			/////////////////////////////////////////////////
			else if("req_extrawork".equals(mode)){
			
				com.anbtech.ew.db.ExtraWorkModuleDAO ewModule = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.entity.StandardWorkTimeTable swtimeTable = new com.anbtech.ew.entity.StandardWorkTimeTable();

				// �ٹ� �ð� ���� �������� 
				swtimeTable = (com.anbtech.ew.entity.StandardWorkTimeTable)ewModule.getStandardWTime();
				request.setAttribute("swtimeTable",swtimeTable);
				getServletContext().getRequestDispatcher("/ew/user/reqWorkForm.jsp").forward(request,response);
			}			

			///////////////////////////////////////////////////
			// ���κ� Ư�ٽ�û��Ȳ ����Ʈ
			///////////////////////////////////////////////////
			else if("eachEwList".equals(mode)){
				
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				com.anbtech.ew.entity.StandardWorkTimeTable swTimeTable = new com.anbtech.ew.entity.StandardWorkTimeTable();

				ArrayList arry = new ArrayList();
				arry = ewDAO.getMyEwReqList(mode,login_id,page);
				request.setAttribute("ew_array",arry);

				com.anbtech.ew.entity.EWLinkTable ewLinkTable = new com.anbtech.ew.entity.EWLinkTable();
				ewLinkTable = ewBO.getRedirect(mode,login_id,page);
				request.setAttribute("Redirect",ewLinkTable);

			 	getServletContext().getRequestDispatcher("/ew/user/eachEwList.jsp").forward(request,response);
			}

			/////////////////////////////////////////////////////
			// �μ��� �����Ŵ�� Ư�ٽ�û�� ����Ʈ ��������
			/////////////////////////////////////////////////////
			else if("ewReqList".equals(mode)){
				
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.entity.ExtraWorkHistoryTable ewHistoryTable = new com.anbtech.ew.entity.ExtraWorkHistoryTable();

				ArrayList arry = new ArrayList();
				arry = (ArrayList)ewDAO.getReqList(mode,div);
				request.setAttribute("ew_array",arry);
	
				getServletContext().getRequestDispatcher("/ew/admin/ewReqList.jsp?div=" + div).forward(request,response);
			}

			/////////////////////////////////////////////////////////
			// ���ڰ���� ���� ȭ��
			/////////////////////////////////////////////////////////
			else if("ew_req_view".equals(mode)){
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				
				if(mno != null && ono_plus == null)	ono_plus = ewDAO.getOnoPlus(mno);

				//Ư�ٽ�û����� ����Ʈ ��������
				ArrayList arry = new ArrayList();
				arry = (ArrayList)ewDAO.getWorkerList(ono_plus);	
				request.setAttribute("appArry",arry);

			    getServletContext().getRequestDispatcher("/ew/admin/appEwView.jsp?mno="+mno).forward(request,response);
			}			
			
			///////////////////////////////////////////////////////
			// �μ��� Ư�ټ��� ������ ��Ȳ ���
			///////////////////////////////////////////////////////
			else if("ew_process_list".equals(mode)){
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				ArrayList arry = new ArrayList();
	
				// �μ��� ����ó�� ��� ��������
				arry = ewDAO.getPewInfoInDivision2(mode,div);
				request.setAttribute("arry",arry);

			    getServletContext().getRequestDispatcher("/ew/admin/divisionEWProcessList.jsp?div=" + div).forward(request,response);
			}
			
			//////////////////////////////////////////////////////
			//  Ư�� ����ó��
			/////////////////////////////////////////////////////
			else if("process_jungsan".equals(mode)) {
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				ewBO.processJungsan(login_id,ono_plus);

				response.sendRedirect("../servlet/ExtraWorkServlet?mode=ew_process_list&div="+div);
			}
			
			///////////////////////////////////////////////////////
			// �μ��� ���� Ư�ټ��� ������Ȳ ���
			///////////////////////////////////////////////////////
			else if("ew_result_list".equals(mode)){
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				ArrayList arry = new ArrayList();
	
				// �μ��� ������Ȳ ��������
				arry = ewDAO.getJungSanResultList(mode,div,year,month);
				request.setAttribute("arry",arry);
			    getServletContext().getRequestDispatcher("/ew/admin/ew_result_list.jsp?div="+div+"&year="+year+"&month="+month).forward(request,response);
			}
			
			///////////////////////////////////////////////////
			//  ���κ� ���� Ư����Ȳ ���
			///////////////////////////////////////////////////
			else if("person_month".equals(mode)){
					
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);	
				com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
				ArrayList table_list = new ArrayList();	

				// ����� ������ �����´�.
				user_info = userinfoDAO.getUserListById(eachid);
				request.setAttribute("User_Info",user_info);

				// ����� �� Ư����Ȳ�� �����´�.
				table_list = ewBO.getPersonalStatusByMonth(year,month,eachid);
				request.setAttribute("Table_List", table_list);

				getServletContext().getRequestDispatcher("/ew/admin/person_month.jsp?y="+year+"&m="+month).forward(request,response);
			}

			///////////////////////////////////////////////////////
			// �Ϻ� Ư�� ����� ��Ȳ ����Ʈ ���
			///////////////////////////////////////////////////////
			else if("ew_daily_list".equals(mode)){
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				ArrayList arry = new ArrayList();
	
				//�Ϻ� Ư�ٴ���� ����Ʈ
				arry = ewDAO.getEwListByDay(mode,div,year,month,day);
				request.setAttribute("arry",arry);
			    getServletContext().getRequestDispatcher("/ew/admin/ew_daily_list.jsp?div="+div+"&year="+year+"&month="+month+"&day="+day).forward(request,response);
			}

			//////////////////////////////////////////////////////
			//  ���õ� Ư�ٽ�û�� ����ó��
			/////////////////////////////////////////////////////
			else if("delete_ew_info".equals(mode)) {
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				ewBO.deleteSelectedEwInfo(ono_plus);

				response.sendRedirect("../servlet/ExtraWorkServlet?mode=eachEwList");
			}

			///////////////////////////////////////////////////////////
			// �μ������� ���
			///////////////////////////////////////////////////////////
			else if(mode.equals("print")){	
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.entity.ExtraWorkHistoryTable table = new com.anbtech.ew.entity.ExtraWorkHistoryTable();
				com.anbtech.admin.entity.ApprovalInfoTable app_table = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

				//Ư������ ��������
				table = ewDAO.getHistoryInfo(o_no);
				request.setAttribute("EW_INFO", table);

				String aid = table.getAid();
				String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
				app_table = appDAO.getApprovalInfo("ew_app_save",aid,sign_path);
				request.setAttribute("Approval_Info", app_table);

				getServletContext().getRequestDispatcher("/ew/user/view_ew_info.jsp").forward(request,response);
			}
		} catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		} finally {
			close(con);
		}
	} // doGet()


/**********************************
 * post������� �Ѿ���� �� ó��
 **********************************/
public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{

	//�ʿ��� �͵� ����
	response.setContentType("text/html;charset=euc-kr");
	HttpSession session = request.getSession(true);

	String mode = request.getParameter("mode"); 
	String redirectUrl = "";

	//�α���������
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
	String login_name = sl.name;

	// Ư�� ��û�� �Ѿ���� ����
	String duty			= Hanguel.toHanguel(request.getParameter("duty"));			// Ư�ٻ���
	String w_type		= request.getParameter("w_type");							// �ٹ�����(n:����,h:����,s:�����)	
	String w_sdate		= request.getParameter("w_sdate");							// Ư�ٽ�û��������(yyyymmdd)
	String w_stime		= request.getParameter("w_stime");							// Ư�ٽ�û���۽ð�(hh:mm)
	String to_tom		= request.getParameter("to_tom");							// ����(1) or ����(2)
	String w_etime		= request.getParameter("w_etime");							// Ư�� ��û ��ħ �ð�
	String duty_cont	= Hanguel.toHanguel(request.getParameter("duty_cont"));		// ��������
	
	// Ư�� ���� �ð� ���� ����(StandardWorkTimeTable)
	String sw_no			 = request.getParameter("sw_no");						// Ư�� ���� �ð� TABLE ������ȣ
	String modify_date		 = request.getParameter("modify_date");					// Ư�ٱ��ؽð����� ����
	String fix_stime		 = request.getParameter("fix_stime");					// ���� ���� ���� �ð�
	String fix_etime		 = request.getParameter("fix_etime");					// ���� ���� ��ħ �ð�
	String fix_stime_sat	 = request.getParameter("fix_stime_sat");				//���Ծ������۽ð� 
	String fix_etime_sat	 = request.getParameter("fix_etime_sat");				//���Ծ�����ħ�ð�
	String fix_stime_holiday = request.getParameter("fix_stime_holiday");			// ���� �ٹ� ���� �ð�
	String fix_etime_holiday = request.getParameter("fix_etime_holiday");			// ���� �ٹ� ��ħ �ð�
	String off_stime		 = request.getParameter("off_stime");					//�ð��ܱٹ����۽ð�
	String off_etime		 = request.getParameter("off_etime");					//�ð��ܱٹ���ħ�ð�
	String off_stime_sat	 = request.getParameter("off_stime_sat");				// ����(��) �ٹ� ���� �ð�
	String off_etime_sat	 = request.getParameter("off_etime_sat");				// ����(��) �ٹ� ��ħ �ð�
	String off_stime_holiday = request.getParameter("off_stime_holiday");			// ���� ����ٹ� ���� �ð�
	String off_etime_holiday = request.getParameter("off_etime_holiday");			// ���� ����ٹ� ��ħ �ð�

	String overday_n		 = request.getParameter("overday_n");					// ���Ͽ���ٹ�����
	String overday_h		 = request.getParameter("overday_h");					// ���Ͽ���ٹ�����
	String overday_s		 = request.getParameter("overday_s");					// ����Ͽ���ٹ�����

	// ���� ��¥
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
	String today = anbdt.getDateNoformat();	
	String thisyear = today.substring(0,4);
			

	try {
		// con����
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
		
		////////////////////////////////////////////////////////
		//  ���رٷνð����� ���ó��
		////////////////////////////////////////////////////////
		if("standard_wtime_save".equals(mode)){
			com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
			ewDAO.saveStandardWtime(modify_date,fix_stime,fix_etime,fix_stime_sat,fix_etime_sat,off_stime,off_etime,fix_stime_holiday,fix_etime_holiday,off_stime_sat,off_etime_sat,off_stime_holiday,off_etime_holiday,overday_n,overday_h,overday_s);

			response.sendRedirect("../servlet/ExtraWorkServlet?mode=standard_wtime_fix&div=wtime_view");
		}


		///////////////////////////////////////////////
		//	Ư�� ��û ���� ��� ó��
		///////////////////////////////////////////////
		 else if("input_data".equals(mode)){
			
			com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
			com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
			
			boolean bool = false;
			String err_msg = "";
			boolean bool2 = false;

/* ���ʿ��� ����̶� �����Ǿ� ���ܽ�Ŵ 2004.08.04 by �ڵ���

			// Ư�ٴ������ �ñ������� ��ϵǾ� �ִ��� üũ�Ѵ�.
			// ��ϵǾ� ���� ������ ���� Ư�������� �ȵǹǷ� ��û�� �ƿ� ���ϰ� �Ѵ�.
			bool = ewDAO.enableReqWork(thisyear,member_id);
			if(bool) {
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('"+login_name+"���� �ñ������� ã�� �� �����ϴ�. Ư�������� ���ؼ��� �ñ������� �ݵ�� ��ϵǾ� �־�� �մϴ�.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;		
			}
*/
			// �ߺ���û���� Ȯ��
			bool = ewDAO.enableSave(login_id,w_sdate);
			if(bool) {
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('������ ��¥�� Ư�ٽ�û������ �̹� �ֽ��ϴ�. �ߺ���û�� �� �� �����ϴ�.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;		
				
			}

			// Ư�ٸ�ħ���� �Ǵ�
			String w_edate = "";
			if(to_tom.equals("1")) {		// �����ϰ��
				w_edate = w_sdate;
			} else if(to_tom.equals("2")) {	// ������ ���
				w_edate = ewBO.getWantDate(w_sdate,"1","+");
			}
								
			// db ����
			ewDAO.saveHistory(login_id,w_sdate,w_stime,w_edate,w_etime,duty,duty_cont,w_type,"0");

			response.sendRedirect("../servlet/ExtraWorkServlet?mode=eachEwList");
		}
	}catch (Exception e){
		request.setAttribute("ERR_MSG",e.toString());
		getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
	}finally{
		close(con);
	}
  }
}