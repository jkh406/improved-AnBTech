import com.anbtech.es.geuntae.entity.*;
import com.anbtech.es.geuntae.db.*;
import com.anbtech.es.geuntae.business.*;
import com.anbtech.text.Hanguel;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class GeunTaeServlet extends HttpServlet {

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

		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String tablename = "geuntae_master";
		String mode = request.getParameter("mode");
		String page = request.getParameter("page");
		String no = request.getParameter("no");
		String searchword = request.getParameter("searchword");
		String searchscope = request.getParameter("searchscope");
		String category = request.getParameter("category");

		String year = request.getParameter("y");
		String month = request.getParameter("m");
		String day = request.getParameter("d");
		
		String division = request.getParameter("div")==null?"":request.getParameter("div");
		String ys_kind = request.getParameter("k")==null?"":request.getParameter("k");
		String hd_var = request.getParameter("hd_var")==null?"":request.getParameter("hd_var");
		String sortby = request.getParameter("sortby")==null?"":request.getParameter("sortby");

		String ac_name = request.getParameter("ac_name")==null?"":request.getParameter("ac_name");
		String user_rank = request.getParameter("user_rank")==null?"":request.getParameter("user_rank");
		String user_name = request.getParameter("user_name")==null?"":request.getParameter("user_name");
		String c_end_date = "n"; // ���� ���� ��ӵ� ���� �ٹ��� ��� (d:����ٹ�, n:�Ϲ����(����))

		String login_id = request.getParameter("id");		//���Ƿ� ������ ����� �����
		String input_time = request.getParameter("t");		//���Ƿ� ������ ����� �ð�

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		if (year == null) year = vans.format(now).substring(0,4);
		if (month == null) month = vans.format(now).substring(4,6);
		if (day == null) day = vans.format(now).substring(6,8);
		
		if (mode == null) mode = "list";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

		String redirectUrl = "";

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
		if(login_id == null || login_id.equals("")) login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;

		String eachid = request.getParameter("eachid")==null?login_id:request.getParameter("eachid");

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////
			//���½ū���Ȳ ����Ʈ
			////////////////////////////////////
			if("list".equals(mode)){
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = geuntaeDAO.getTableList(tablename,mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Table_List", table_list);

				com.anbtech.es.geuntae.business.GeunTaeLinkBO redirectBO = new com.anbtech.es.geuntae.business.GeunTaeLinkBO(con);
				com.anbtech.es.geuntae.entity.GeunTaeLink redirect = new com.anbtech.es.geuntae.entity.GeunTaeLink();
				redirect = redirectBO.getRedirect(tablename,mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/es/geuntae/list.jsp").forward(request,response);
			}

			////////////////////////////////////
			//���� ���� ���� ��Ȳ
			////////////////////////////////////
			else if("person_month".equals(mode)){
				com.anbtech.es.geuntae.business.GeunTaeBO geuntaeBO = new com.anbtech.es.geuntae.business.GeunTaeBO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
				com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);

				com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
				com.anbtech.es.geuntae.db.HyuGaDayDAO hyugadayDAO = new com.anbtech.es.geuntae.db.HyuGaDayDAO(con);
				
				//���� ���� �� ����� �ܷ��� ��������
				user_info = userinfoDAO.getUserListById(eachid);
				String year_rest	= hyugadayDAO.getPersonalHoliday(eachid,year,"HD_006",month);
				String month_rest	= hyugadayDAO.getPersonalHoliday(eachid,year,"HD_003",month);
				user_info.setHyuGaYearRestDay(year_rest);
				user_info.setHyuGaMonthRestDay(month_rest);
				request.setAttribute("User_Info", user_info);

				//���� �ް���Ȳ ��������
				ArrayList table_list = new ArrayList();
				table_list = geuntaeBO.getPersonalStatusByMonth(year,month,eachid);
				request.setAttribute("Table_List", table_list);		
				getServletContext().getRequestDispatcher("/es/geuntae/personalStatusByMonth.jsp?y="+year+"&m="+month).forward(request,response);

			}

			////////////////////////////////////
			//���� �⺰ ���� ��Ȳ
			////////////////////////////////////
			else if("person_year".equals(mode)){
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
				ArrayList table_list = new ArrayList();

				try{
					table_list = geuntaeDAO.getPersonalStatusInDeptByYear(login_id,year,hd_var,division,sortby);
				}catch( Exception e1){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('������ �μ����� ���� �μ����� �������� �ʽ��ϴ�.');");
					out.println("   history.go(-1)");
					out.println("	</script>");
					out.close();
					return;			
				}
				request.setAttribute("Table_List", table_list);

				String url = "/es/geuntae/personalStatusByYear.jsp?div="+division+"&hd_var="+hd_var+"&y="+year;	
				getServletContext().getRequestDispatcher(url).forward(request,response);
		
			}

			////////////////////////////////////
			//�μ��� ���� ��Ȳ
			////////////////////////////////////
			else if("div_month".equals(mode)){
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = geuntaeDAO.getDivisionalStatusByYear(year,ys_kind);
				request.setAttribute("Table_List", table_list);
				
				String url = "/es/geuntae/divisionalStatusByMonth.jsp?y="+year+"&k="+ys_kind+"&a="+ac_name+"&r="+user_rank+"&n="+user_name;
				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			////////////////////////////////////
			//���κ� �ް� �ܷ��� ����(������ ���)
			////////////////////////////////////
			else if("manager_hyuga_day".equals(mode)){
				com.anbtech.es.geuntae.db.HyuGaDayDAO hyugadayDAO = new com.anbtech.es.geuntae.db.HyuGaDayDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = hyugadayDAO.getUserHyuGaRestDay(year,ys_kind,division);
				request.setAttribute("Table_List", table_list);

				String url = "/es/admin/managerHyuGaDay.jsp?y="+year+"&k="+ys_kind+"&div="+division;
				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			////////////////////////////////////
			//��� �ð� ��� ó��
			////////////////////////////////////
			else if("chk_in".equals(mode)){
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				//�ش� ������� ����� ����� �ִ��� üũ�Ѵ�.
				String wdate = year + month + day;
				String time = geuntaeDAO.getWorkTime(login_id,wdate);

				String time_s = time.substring(0,time.lastIndexOf("|"));			   // ��ٽð�
				String time_e = time.substring(time.lastIndexOf("|")+1,time.length()); // ��ٽð�

				if(time_s.equals("") || time_s==null){ // ��ٽð� ����� ������ ���� �����Ѵ�.
					//���� �ð��� �����´�.
					java.text.SimpleDateFormat vans2 = new java.text.SimpleDateFormat("kk:mm");
					time_s = vans2.format(now);

					//���Ƿ� ������ �ð��� ���� ��쿡�� �Էµ� �ð����� ����Ѵ�.
					if(input_time != null) time_s = input_time;

					//�������� Ŭ���̾�Ʈ�� IP Address�� �����´�.
					String ipaddress = request.getRemoteAddr();

					//db�� ����
					geuntaeDAO.saveWorkTimeS(login_id,wdate,time_s,ipaddress);
				
				}else{					// ��ٽð� ����� �̹� �� ���
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('�̹� ����ϼ̽��ϴ�. �Ϸ翡 �ѹ��� ��� �����մϴ�.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();				
				}
				redirectUrl = "GeunTaeServlet?mode=person_month&y="+year+"&m="+month;
			}

			////////////////////////////////////
			//��� �ð� ��� ó��
			//
			//	��� �ð� ��Ͻ� ����� ���
			//	============================
			//	(1)���ٹ� �� ����ϴ� ���
			//	(2)Ư�� �� ���Ͽ� ����ϴ� ���
			//	(3)Ư�� �� ����(������)�� ����ϴ� ���
			//
			////////////////////////////////////
			else if("chk_out".equals(mode)){
				com.anbtech.es.geuntae.business.GeunTaeBO geuntaeBO = new com.anbtech.es.geuntae.business.GeunTaeBO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
				com.anbtech.ew.db.ExtraWorkModuleDAO ewMDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.business.ExtraWorkModuleBO ewMBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);

				//���õ� ������� ����� ����� ��������
				String wdate = year + month + day;
				String time = geuntaeDAO.getWorkTime(login_id,wdate);
				String time_s = time.substring(0,time.lastIndexOf("|"));			   // ���� ��ٽð�
				String time_e = time.substring(time.lastIndexOf("|")+1,time.length()); // ���� ��ٽð�

				//���õ� ������� ����(������) ����� ��� ��������
				String yday	= geuntaeBO.getYesterday(wdate.substring(0,4),wdate.substring(4,6),wdate.substring(6,8));
				String yday_time = geuntaeDAO.getWorkTime(login_id,yday);
				String yday_time_s = yday_time.substring(0,yday_time.lastIndexOf("|"));	// ���� ��ٽð�
				String yday_time_e = yday_time.substring(yday_time.lastIndexOf("|")+1,yday_time.length()); // ���� ��ٽð�
				
				//���� �ð��� �����ͼ� �ð��� �������� ���� 6�� ������ ����
				//�Ϸ簡 ���� ������� �����Ѵ�.
				boolean is_previous_out = false;
				java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("kk");
				int now_hour = Integer.parseInt(sf.format(now));
				
				//���Ƿ� ������ �ð��� ���� ��쿡�� �Էµ� �ð����� �Ǵ��Ѵ�.
				if(input_time != null) now_hour = Integer.parseInt(input_time.substring(0,2));

				if(now_hour > 0 && now_hour < 6) is_previous_out = true;

				///////////////////////////
				//�Ϸ簡 ������ ����ҷ��� �ϴ� ���
				///////////////////////////
				if(is_previous_out){	
					if(yday_time_s.equals("")){ // ���� ��ٽð� ����� ������ ��ٽð� ����� ���� �ϵ��� ����
						PrintWriter out = response.getWriter();
						out.println("	<script>");
						out.println("	alert('��ٽð� ����� �����ϴ�. ��� �ð� ����� ���� �Ͻʽÿ�.');");
						out.println("	history.go(-1);");
						out.println("	</script>");
						out.close();
					}else{
						if(yday_time_e.equals("")){ // ��ٽð� ����� ������ ����Ѵ�.
							//���� �ð��� �����´�.
							java.text.SimpleDateFormat vans2 = new java.text.SimpleDateFormat("kk:mm");
							yday_time_e = vans2.format(now);

							//���Ƿ� ������ �ð��� ���� ��쿡�� �Էµ� �ð����� ����Ѵ�.
							if(input_time != null) yday_time_e = input_time;

							//�������� Ŭ���̾�Ʈ�� IP Address�� �����´�.
							String ipaddress = request.getRemoteAddr();

							boolean is_over_work = ewMDAO.checkWorked(login_id,yday);
							if(is_over_work){
								con.setAutoCommit(false);	// Ʈ������� ����
								con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
								try{
									//Ư�ٽ�û�� �� ��� Ư�� ��⿡ ��ٽð��� ���
									ewMBO.processWorkOut(login_id,yday,wdate,yday_time_e);

									//��ٽð��� ���� ���� ��⿡ ����
									geuntaeDAO.saveWorkTimeE(login_id,yday,"���� "+yday_time_e,ipaddress);

									con.commit();
								}catch(Exception e){
									con.rollback();
									request.setAttribute("ERR_MSG",e.toString());
									getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

								}finally{
									con.setAutoCommit(true);
								}
							}else{
								//��ٽð��� ���� ���� ��⿡ ����
								geuntaeDAO.saveWorkTimeE(login_id,yday,"���� "+yday_time_e,ipaddress);							
							}
							
							redirectUrl = "GeunTaeServlet?mode=person_month&y="+year+"&m="+month+"&eachid="+login_id;
						} else {	// ��ٽð� ����� �̹� �� ���
							PrintWriter out = response.getWriter();
							out.println("	<script>");
							out.println("	alert('�̹� ����ϼ̽��ϴ�. �Ϸ翡 �ѹ��� ��� �����մϴ�.');");
							out.println("	history.go(-1);");
							out.println("	</script>");
							out.close();	
						}
					} 				
				////////////////////////////
				//���� ����ҷ��� �ϴ� ���
				////////////////////////////
				}else{					
					if(time_s.equals("")){ // ���� ��ٽð� ����� ������ ��ٽð� ����� ���� �ϵ��� ����
						PrintWriter out = response.getWriter();
						out.println("	<script>");
						out.println("	alert('��ٽð� ����� �����ϴ�. ��� �ð� ����� ���� �Ͻʽÿ�.');");
						out.println("	history.go(-1);");
						out.println("	</script>");
						out.close();
					}else{
						if(time_e.equals("")){ // ��ٽð� ����� ������ ����Ѵ�.
							//���� �ð��� �����´�.
							java.text.SimpleDateFormat vans2 = new java.text.SimpleDateFormat("kk:mm");
							time_e = vans2.format(now);

							//���Ƿ� ������ �ð��� ���� ��쿡�� �Էµ� �ð����� ����Ѵ�.
							if(input_time != null) time_e = input_time;


							//�������� Ŭ���̾�Ʈ�� IP Address�� �����´�.
							String ipaddress = request.getRemoteAddr();

							boolean is_over_work = ewMDAO.checkWorked(login_id,wdate);
							if(is_over_work){
								con.setAutoCommit(false);	// Ʈ������� ����
								con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
								try{
									//Ư�ٽ�û�� �� ��� Ư�� ��⿡ ��ٽð��� ���
									ewMBO.processWorkOut(login_id,wdate,wdate,time_e);
									//��ٽð��� ���� ���� ��⿡ ����
									geuntaeDAO.saveWorkTimeE(login_id,wdate,time_e,ipaddress);

									con.commit();
								}catch(Exception e){
									con.rollback();
									request.setAttribute("ERR_MSG",e.toString());
									getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

								}finally{
									con.setAutoCommit(true);
								}
							}else{
								//��ٽð��� ���� ���� ��⿡ ����
								geuntaeDAO.saveWorkTimeE(login_id,wdate,time_e,ipaddress);				
							}
							
							redirectUrl = "GeunTaeServlet?mode=person_month&y="+year+"&m="+month+"&eachid="+login_id;
						} else {	// ��ٽð� ����� �̹� �� ���
							PrintWriter out = response.getWriter();
							out.println("	<script>");
							out.println("	alert('�̹� ����ϼ̽��ϴ�. �Ϸ翡 �ѹ��� ��� �����մϴ�.');");
							out.println("	history.go(-1);");
							out.println("	</script>");
							out.close();	
						}
					} 				
				}
			}
			
			////////////////////////////////////
			//���� �Ϻ� ����� �ð� ��Ȳ ���
			////////////////////////////////////
			else if("work_history".equals(mode)){
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = geuntaeDAO.getWorkHistoryByDay(year,month,day,division);
				request.setAttribute("Table_List", table_list);
				getServletContext().getRequestDispatcher("/es/geuntae/workHistoryByDay.jsp?y="+year+"&m="+month+"&d="+day+"&div="+division).forward(request,response);				
			}

			//redirectUrl�� ���� �����ÿ��� redirectUrl��η� �̵��Ѵ�.
			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
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
		PrintWriter out = response.getWriter();

		//Session
		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;

		//MultipartRequest ũ��, ������丮
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/es/"+login_id+"/addfile";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory�����ϱ�

		String maxFileSize = "10";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�


		//��� ���� �� ����� ���� (����)
		String mode = multi.getParameter("mode");				if(mode == null) mode = "";		//������ó�����
		String app_mode = multi.getParameter("app_mode");		if(app_mode == null) app_mode = "";		//���ڰ��� ó�����	
		String user_id = multi.getParameter("user_id");			if(user_id == null) user_id = "";		//user id
		String user_name = multi.getParameter("user_name");		if(user_name == null) user_name = "";	//user_name
		String user_code = multi.getParameter("user_code");		if(user_code == null) user_code = "";	//user_rank code
		String user_rank = multi.getParameter("user_rank");		if(user_rank == null) user_rank = "";	//user_rank 
		String div_id = multi.getParameter("div_id");			if(div_id == null) div_id = "";			//div_id 
		String div_name = multi.getParameter("div_name");		if(div_name == null) div_name = "";		//div_name 
		String div_code = multi.getParameter("div_code");		if(div_code == null) div_code = "";		//div_code 
		//out.println("	<script>	alert('"+mode+"');	</script>");	out.close();

		//��������� ���ڰ��� �����׸� (�����)
		String writer_id = multi.getParameter("writer_id");		if(writer_id == null) writer_id = "";	//�����(��ŵ���� �ϼ��� ����)
		String writer_name = multi.getParameter("writer_name");	if(writer_name == null) writer_name = "";	//�����(��ŵ���� �ϼ��� ����)
		String doc_pid = multi.getParameter("doc_id");			if(doc_pid == null) doc_pid = "";	//������ȣ
		String doc_line = multi.getParameter("doc_app_line");	if(doc_line == null) doc_line = "";	//���缱
		String doc_subj = multi.getParameter("doc_sub");		if(doc_subj == null) doc_subj = "";	//����
		String doc_peri = multi.getParameter("doc_per");		if(doc_peri == null) doc_peri = "";	//�����Ⱓ
		String doc_secu = multi.getParameter("doc_sec");		if(doc_secu == null) doc_secu = "";	//���ȵ��
		
		//������ id (�ӽ�������繮�� : ���� �� �űԵ������ ó��Ű ����)
		String old_id = multi.getParameter("old_id");			if(old_id == null) old_id = "";		//�ӽ������� ���� ������ȣ

		//�Ϲ� ����	
		String writer_date = multi.getParameter("date");		if(writer_date == null) writer_date = "";	//�Է��� �ú�
		String purpose = multi.getParameter("purpose");		if(purpose == null) purpose = "";				//���� �� ����
		String period = multi.getParameter("period");		if(period == null) period = "";					//�Ⱓ (�ϼ�,�ð� ��)

		//���� ����	
		String doc_syear = multi.getParameter("doc_syear");		if(doc_syear == null) doc_syear = "";	//���� �⵵
		String doc_smonth = multi.getParameter("doc_smonth");	if(doc_smonth == null) doc_smonth = "";	//���� ��
		String doc_sdate = multi.getParameter("doc_sdate");		if(doc_sdate == null) doc_sdate = "";	//���� ��
		String doc_edyear = multi.getParameter("doc_edyear");	if(doc_edyear == null) doc_edyear = "";	//���� �⵵
		String doc_edmonth = multi.getParameter("doc_edmonth");	if(doc_edmonth == null) doc_edmonth = "";//���� ��
		String doc_eddate = multi.getParameter("doc_eddate");	if(doc_eddate == null) doc_eddate = "";	//���� ��
		
		//��(��)����	
		String doc_huga = multi.getParameter("doc_huga");		if(doc_huga == null) doc_huga = "";		//�ް� �����ڵ�
		String doc_receiver = multi.getParameter("doc_receiver");	if(doc_receiver == null) doc_receiver = "";	//�����μ��ΰ���
		String doc_tel = multi.getParameter("doc_tel");			if(doc_tel == null) doc_tel = "";		//��޿���ó

		//�����û��
		String doc_chuljang = multi.getParameter("doc_chuljang"); if(doc_chuljang == null) doc_chuljang = "";//���� �����ڵ�
		String fellow_names = multi.getParameter("fellow_names"); if(fellow_names == null) fellow_names = "";//������ ���/�̸�;
		String prj_code = multi.getParameter("prj_code"); 		if(prj_code == null) prj_code = "";		//������Ʈ �ڵ�
		String bistrip_kind = multi.getParameter("bistrip_kind");if(bistrip_kind == null) bistrip_kind = "����";//������ ����(����/��)
		String bistrip_country = multi.getParameter("bistrip_country"); if(bistrip_country == null) bistrip_country = "";//������ ������
		String bistrip_city = multi.getParameter("bistrip_city"); if(bistrip_city == null) bistrip_city = "";//������ ���ø�
		String traffic_way = multi.getParameter("traffic_way");	if(traffic_way == null) traffic_way = "";	//������
		String receiver_id = multi.getParameter("receiver_id");	if(receiver_id == null) receiver_id = "";	//�����λ��
		String receiver_name = multi.getParameter("receiver_name");	if(receiver_name == null) receiver_name = "";	//�������̸�

		//2���μ� �����û�� 
		String link_id = multi.getParameter("link_id");	if(link_id == null) link_id = "";	//���ù��� ��ȣ

		//���ݾװ� ���⳻���� �迭�� ���
		String account_cnt = multi.getParameter("account_cnt");	if(account_cnt == null) account_cnt = "0";	//account�׸��
		int acnt_cnt = Integer.parseInt(account_cnt);
		String[][] cost = new String[0][3];

		//�����
		String doc_oechul = multi.getParameter("doc_oechul"); if(doc_oechul == null) doc_oechul = "";		//���� �����ڵ�
		String StartTime = multi.getParameter("hdStartTime"); if(StartTime == null) StartTime = "";			//���۽ð�
		String EndTime = multi.getParameter("hdEndTime"); if(EndTime == null) EndTime = "";					//����ð�
		String time_period = StartTime+"~"+EndTime;	//����ð�
		String dest = multi.getParameter("dest"); if(dest == null) dest = "";								//������

		if(acnt_cnt > 0) {
			int ac_cnt = acnt_cnt - 1;
			cost = new String[ac_cnt][3];
			String c_code = "";		//�����ڵ�
			String c_cost = "";		//������
			String c_cont = "";		//���⳻��
			for(int c=0,m=1; c < ac_cnt; c++,m++) {
				c_code = "code"+m;
				c_cost = "cost"+m;
				c_cont = "cont"+m;
				cost[c][0] = multi.getParameter(c_code);	if(cost[c][0] == null) cost[c][0] = "";		//�ڵ�	
				cost[c][1] = multi.getParameter(c_cost);	if(cost[c][1] == null) cost[c][1] = "0";	//���	
				cost[c][2] = multi.getParameter(c_cont);	if(cost[c][2] == null) cost[c][2] = "";		//����
			}
		}

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////
			//��(��)���� ���
			////////////////////////////////////
			if ("HYU_GA".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�ް���",doc_pid,"HYU_GA");
					
					//���°��� TABLE (geuntae_master) �Է��ϱ� (�ް���)
					geuntaeDAO.setGeuntaeHdyTable(doc_pid,"HYU_GA",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,doc_syear,doc_smonth,doc_sdate,
						doc_edyear,doc_edmonth,doc_eddate,purpose,period,doc_huga,doc_receiver,doc_tel);
					
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("geuntae_master","flag","gt_id",doc_pid,"EE");
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//��(��)���� �ӽú���
			////////////////////////////////////
			else if("HYU_GA_TMP".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�ް���",doc_pid,"HYU_GA");
					
					//���°��� TABLE (geuntae_master) �Է��ϱ� (�ް���)
					geuntaeDAO.setGeuntaeHdyTable(doc_pid,"HYU_GA",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,doc_syear,doc_smonth,doc_sdate,
						doc_edyear,doc_edmonth,doc_eddate,purpose,period,doc_huga,doc_receiver,doc_tel);
					
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//��(��)���� �ӽú��� �� ��� 
			////////////////////////////////////
			else if ("R_HYU_GA".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//������ȣ,login_id,root_path

					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�ް���",doc_pid,"HYU_GA");
					
					//���°��� TABLE (geuntae_master) �Է��ϱ� (�ް���)
					geuntaeDAO.setGeuntaeHdyTable(doc_pid,"HYU_GA",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,doc_syear,doc_smonth,doc_sdate,
						doc_edyear,doc_edmonth,doc_eddate,purpose,period,doc_huga,doc_receiver,doc_tel);
					
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("geuntae_master","flag","gt_id",doc_pid,"EE");
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('��ŵǾ����ϴ�.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////			
			//��(��)���� �ӽú��� �� �ӽú���
			////////////////////////////////////
			else if("R_HYU_GA_TMP".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//������ȣ,login_id,root_path

					//------------------------------
					// �ű� ������
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�ް���",doc_pid,"HYU_GA");
					
					//���°��� TABLE (geuntae_master) �Է��ϱ� (�ް���)
					geuntaeDAO.setGeuntaeHdyTable(doc_pid,"HYU_GA",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,doc_syear,doc_smonth,doc_sdate,
						doc_edyear,doc_edmonth,doc_eddate,purpose,period,doc_huga,doc_receiver,doc_tel);
					
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('����Ǿ����ϴ�.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}
			}
			
			////////////////////////////////////
			//�����û�� ���
			////////////////////////////////////
			else if ("CHULJANG_SINCHEONG".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����û��",doc_pid,"CHULJANG_SINCHEONG");
					
					//���°��� TABLE (geuntae_master) �Է��ϱ� (�����û��)
					geuntaeDAO.setGeuntaeBisTripTable(doc_pid,"","CHULJANG_SINCHEONG",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,fellow_names,prj_code,doc_syear,doc_smonth,doc_sdate,doc_edyear,doc_edmonth,doc_eddate,bistrip_city,bistrip_kind,bistrip_country,traffic_way,purpose,"","","",period,doc_chuljang,receiver_id,receiver_name,doc_receiver,doc_tel);
					//���� TABLE (geuntae_account)�� �Է��ϱ� (�����û��)
					for(int cj=0,cid=1; cj<cost.length; cj++,cid++) {
						//���ڵ� �����
						String doc_cid = doc_pid + "_" + cid;
						if(!cost[cj][1].equals("000"))
							geuntaeDAO.setGeuntaeBisTripAccountTable(doc_pid,doc_cid,cost[cj][0],cost[cj][1],cost[cj][2]);
					}
					
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("geuntae_master","flag","gt_id",doc_pid,"EE");
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//�����û�� �ӽú���
			////////////////////////////////////
			else if ("CHULJANG_SINCHEONG_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����û��",doc_pid,"CHULJANG_SINCHEONG");
					
					//���°��� TABLE (geuntae_master) �Է��ϱ� (�����û��)
					geuntaeDAO.setGeuntaeBisTripTable(doc_pid,"","CHULJANG_SINCHEONG",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,
							fellow_names,prj_code,doc_syear,doc_smonth,doc_sdate,doc_edyear,doc_edmonth,doc_eddate,
							bistrip_city,bistrip_kind,bistrip_country,traffic_way,purpose,"","","",period,doc_chuljang,receiver_id,receiver_name,doc_receiver,doc_tel);
					//���� TABLE (geuntae_account)�� �Է��ϱ� (�����û��)
					for(int cj=0,cid=1; cj<cost.length; cj++,cid++) {
						//���ڵ� �����
						String doc_cid = doc_pid + "_" + cid;
						if(!cost[cj][1].equals("000"))
							geuntaeDAO.setGeuntaeBisTripAccountTable(doc_pid,doc_cid,cost[cj][0],cost[cj][1],cost[cj][2]);
					}
				
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//�����û�� ����
			////////////////////////////////////
			else if ("R_CHULJANG_SINCHEONG".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//������ȣ,login_id,root_path

					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����û��",doc_pid,"CHULJANG_SINCHEONG");
					
					//���°��� TABLE (geuntae_master) �Է��ϱ� (�����û��)
					geuntaeDAO.setGeuntaeBisTripTable(doc_pid,"","CHULJANG_SINCHEONG",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,
							fellow_names,prj_code,doc_syear,doc_smonth,doc_sdate,doc_edyear,doc_edmonth,doc_eddate,
							bistrip_city,bistrip_kind,bistrip_country,traffic_way,purpose,"","","",period,doc_chuljang,receiver_id,receiver_name,doc_receiver,doc_tel);
					//���� TABLE (geuntae_account)�� �Է��ϱ� (�����û��)
					for(int cj=0,cid=1; cj<cost.length; cj++,cid++) {
						//���ڵ� �����
						String doc_cid = doc_pid + "_" + cid;
						if(!cost[cj][1].equals("000"))
							geuntaeDAO.setGeuntaeBisTripAccountTable(doc_pid,doc_cid,cost[cj][0],cost[cj][1],cost[cj][2]);
					}
					
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("geuntae_master","flag","gt_id",doc_pid,"EE");
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('��ŵǾ����ϴ�.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//�����û�� ���ӽ�����
			////////////////////////////////////
			else if ("R_CHULJANG_SINCHEONG_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//������ȣ,login_id,root_path

					//------------------------------
					// �ű� ���ӽ�����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����û��",doc_pid,"CHULJANG_SINCHEONG");
					
					//���°��� TABLE (geuntae_master) �Է��ϱ� (�����û��)
					geuntaeDAO.setGeuntaeBisTripTable(doc_pid,"","CHULJANG_SINCHEONG",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,
							fellow_names,prj_code,doc_syear,doc_smonth,doc_sdate,doc_edyear,doc_edmonth,doc_eddate,
							bistrip_city,bistrip_kind,bistrip_country,traffic_way,purpose,"","","",period,doc_chuljang,receiver_id,receiver_name,doc_receiver,doc_tel);
					//���� TABLE (geuntae_account)�� �Է��ϱ� (�����û��)
					for(int cj=0,cid=1; cj<cost.length; cj++,cid++) {
						//���ڵ� �����
						String doc_cid = doc_pid + "_" + cid;
						if(!cost[cj][1].equals("000"))
							geuntaeDAO.setGeuntaeBisTripAccountTable(doc_pid,doc_cid,cost[cj][0],cost[cj][1],cost[cj][2]);
					}
					
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('����Ǿ����ϴ�.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//�����û�� 2���μ� ���
			////////////////////////////////////
			else if ("CHULJANG_SINCHEONG_SEC".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����û��",link_id,"CHULJANG_SINCHEONG");
					
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("geuntae_master","flag2","gt_id",link_id,"EE");
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('��ŵǾ����ϴ�.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//����� ���
			////////////////////////////////////
			else if ("OE_CHUL".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����",doc_pid,"OE_CHUL");
					
					//���°��� TABLE (geuntae_master) �Է��ϱ� (�����)
					geuntaeDAO.setGeuntaeOutTable(doc_pid,"OE_CHUL",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,fellow_names,doc_syear,doc_smonth,doc_sdate,
						dest,traffic_way,purpose,time_period,doc_oechul,doc_receiver,doc_tel);
					
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("geuntae_master","flag","gt_id",doc_pid,"EE");
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//����� �ӽ�����
			////////////////////////////////////
			else if ("OE_CHUL_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����",doc_pid,"OE_CHUL");
					
					//���°��� TABLE (geuntae_master) �Է��ϱ� (�����)
					geuntaeDAO.setGeuntaeOutTable(doc_pid,"OE_CHUL",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,fellow_names,doc_syear,doc_smonth,doc_sdate,
						dest,traffic_way,purpose,time_period,doc_oechul,doc_receiver,doc_tel);
					
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//����� ����
			////////////////////////////////////
			else if ("R_OE_CHUL".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//������ȣ,login_id,root_path

					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����",doc_pid,"OE_CHUL");
					
					//���°��� TABLE (geuntae_master) �Է��ϱ� (�����)
					geuntaeDAO.setGeuntaeOutTable(doc_pid,"OE_CHUL",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,fellow_names,doc_syear,doc_smonth,doc_sdate,
						dest,traffic_way,purpose,time_period,doc_oechul,doc_receiver,doc_tel);
					
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("geuntae_master","flag","gt_id",doc_pid,"EE");
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('��ŵǾ����ϴ�.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//����� �� �ӽ�����
			////////////////////////////////////
			else if ("R_OE_CHUL_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//������ȣ,login_id,root_path

					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����",doc_pid,"OE_CHUL");
					
					//���°��� TABLE (geuntae_master) �Է��ϱ� (�����)
					geuntaeDAO.setGeuntaeOutTable(doc_pid,"OE_CHUL",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,fellow_names,doc_syear,doc_smonth,doc_sdate,
						dest,traffic_way,purpose,time_period,doc_oechul,doc_receiver,doc_tel);
					
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('����Ǿ����ϴ�.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			//con�Ҹ�
			close(con);
		}
	} //doPost()
}