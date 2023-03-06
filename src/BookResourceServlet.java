import com.anbtech.text.Hanguel;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.br.entity.CarInfoTable;
import com.anbtech.br.entity.CarUseInfoTable;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;


public class BookResourceServlet extends HttpServlet {

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
		String login_id			= sl.id;
		String login_name		= sl.name;
		String login_division	= sl.division;
		String redirectUrl		= "";

		String year = request.getParameter("y");
		String month = request.getParameter("m");
		String day = request.getParameter("d");

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (year == null) year = vans.format(now).substring(0,4);
		if (month == null) month = vans.format(now).substring(5,7);
		if (day == null) day = vans.format(now).substring(8,10);

		String cid			= request.getParameter("cid")==null?"":request.getParameter("cid");
		String cr_id		= request.getParameter("cr_id")==null?"":request.getParameter("cr_id");
		String st			= request.getParameter("st")==null?"":Hanguel.toHanguel(request.getParameter("st"));
		String chg_cont		= request.getParameter("chg_cont")==null?"":Hanguel.toHanguel(request.getParameter("chg_cont"));

		String tablename	= request.getParameter("tablename")==null?"":request.getParameter("tablename");
		String mode			= request.getParameter("mode");
		String page			= request.getParameter("page")==null?"1":request.getParameter("page");
		String no			= request.getParameter("no");
		String searchword	= request.getParameter("searchword")==null?"":request.getParameter("searchword");
		String searchscope	= request.getParameter("searchscope")==null?"":request.getParameter("searchscope");
		String category		= request.getParameter("category");

		String user_id		= request.getParameter("user_id")==null?login_id:request.getParameter("user_id");
		String write_id		= request.getParameter("write_id")==null?login_id:request.getParameter("write_id");
		String user_name	= request.getParameter("user_name")==null?login_name:request.getParameter("user_name");
		String mgr_name		= request.getParameter("mgr_name")==null?login_name:request.getParameter("mgr_name");
		String mgr_id		= request.getParameter("mgr_id")==null?login_id:request.getParameter("mgr_id");
		String return_date  = request.getParameter("return_date");
		if(return_date==null) return_date = vans.format(now);

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//���� ����
			if(category.equals("car")){
				
				/////////////////////////////////
				// ���� ����Ʈ(������ ���)
				/////////////////////////////////
				if(mode.equals("list")){ 
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					ArrayList car_list = new ArrayList();

					car_list = carDAO.getAllCarList();
					request.setAttribute("CarList", car_list);
					
					getServletContext().getRequestDispatcher("/br/admin/managerCar.jsp").forward(request,response);
				}

				/////////////////////////////////
				// �ű� ���� ��� (������ ���)
				/////////////////////////////////
				else if(mode.equals("write_car")){ 

					com.anbtech.br.entity.CarInfoTable carinfoTable = new com.anbtech.br.entity.CarInfoTable();

					request.setAttribute("CarInfo", carinfoTable);
					getServletContext().getRequestDispatcher("/br/admin/add_car.jsp?model=write_car").forward(request,response);
				}

				/////////////////////////////////
				// ���� ���� �� ���� �� ����(������)
				/////////////////////////////////
				else if(mode.equals("view_detail")){ 
					
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					com.anbtech.br.entity.CarInfoTable carinfoTable = new com.anbtech.br.entity.CarInfoTable();

					carinfoTable = carDAO.getCarInfo(cid);
					request.setAttribute("CarInfo", carinfoTable);
					
					getServletContext().getRequestDispatcher("/br/admin/add_car.jsp?mode=modify_car").forward(request,response);
				}

				/////////////////////////////////
				// ���� �� ���� ������Ȳ ����
				/////////////////////////////////
				else if(mode.equals("view_stat")){ 
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
						
					// �ش� ���� �� �ϼ��� ���Ѵ�.
					int total_day = carDAO.getDaysInMonth(Integer.parseInt(month),Integer.parseInt(year));
					// ��� ���� Ȯ�� 
					int car_no = carDAO.getTotalCount("car_info","");

					ArrayList car_list = new ArrayList();
	
					// ��� ������ ������, ������� ���� ��������
					if(car_no>0){ car_list = carDAO.getResourceStat(year,month); }
					request.setAttribute("ResourceList", car_list);
					
					String url = "/br/car/viewBookingStat.jsp?y="+year+"&m="+month+"&td="+total_day+"&cid="+cid;
					getServletContext().getRequestDispatcher(url).forward(request,response);
				}

				/////////////////////////////////
				// �� ���� ���� ( �� ���� & �� �̷�)
				/////////////////////////////////
				else if(mode.equals("eachcar")){ 
					com.anbtech.br.db.CarResourceDAO carResourceDAO= new com.anbtech.br.db.CarResourceDAO(con);
					com.anbtech.br.entity.CarInfoTable carinfoTable = new com.anbtech.br.entity.CarInfoTable();
					
					//����¡ó��
					com.anbtech.br.business.CarLinkBO redirectBO = new com.anbtech.br.business.CarLinkBO(con);
					com.anbtech.br.entity.CarLinkTable redirect = new com.anbtech.br.entity.CarLinkTable();
					redirect = redirectBO.getRedirect(tablename,mode,year,searchscope,category,page,login_id,cid); 
					request.setAttribute("Redirect",redirect);

					String view_total = redirect.getViewTotal();
					String view_boardpage = redirect.getViewBoardpage();
					String view_totalpage = redirect.getViewTotalpage();
					
					//���� ����̷� ��������
					ArrayList eachcar_list = new ArrayList();
					eachcar_list = (ArrayList)carResourceDAO.getEachCarHistory(cid, page ,view_total,year);
					request.setAttribute("eachcar_list", eachcar_list);

					//�������� ��������	
					carinfoTable = carResourceDAO.getCarInfo(cid);
					request.setAttribute("each_info", carinfoTable);

					String url = "/br/car/eachCarInfo.jsp?login_id="+login_id+"&year="+year;
					getServletContext().getRequestDispatcher(url).forward(request,response);
				}

				/////////////////////////////////
				// �����û�����
				/////////////////////////////////
				else if(mode.equals("add_lending")){
					com.anbtech.br.db.CarResourceDAO carResourceDAO= new com.anbtech.br.db.CarResourceDAO(con);
					com.anbtech.br.entity.CarInfoTable carinfoTable = new com.anbtech.br.entity.CarInfoTable();
				
					carinfoTable = carResourceDAO.getCarInfo(cid);
					request.setAttribute("Table", carinfoTable);
					String user=user_id+"/"+user_name;

					String url = "/br/car/lendingCar.jsp?user_id="+user_id+"&user_name="+user+"&year="+year+"&month="+month+"&day="+day;
					getServletContext().getRequestDispatcher(url).forward(request,response);

				}

				/////////////////////////////////
				// �����û ��� ó��(by ������)
				/////////////////////////////////
				else if(mode.equals("lending_cancel")) {
					com.anbtech.br.db.CarResourceDAO carResourceDAO= new com.anbtech.br.db.CarResourceDAO(con);
					carResourceDAO.cancelBooking(cr_id);
												
					redirectUrl = "BookResourceServlet?category=car&mode=eachcar&cid="+cid+"&tablename=charyang_master";
				}

				/////////////////////////////////
				// ���� ó��
				/////////////////////////////////
				else if(mode.equals("lending_Process")) { 
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					carDAO.lendingProcess(cr_id,mgr_id,mgr_name); // ȣ�� : ����ó�� method 
					
					redirectUrl = "BookResourceServlet?category=car&mode=eachcar&cid="+cid+"&tablename=charyang_master";
				}

				/////////////////////////////////
				// ������ ������ �԰�ó�� ��
				/////////////////////////////////
				else if(mode.equals("entering_view")){
					com.anbtech.br.db.CarResourceDAO carResourceDAO= new com.anbtech.br.db.CarResourceDAO(con);
					com.anbtech.br.entity.CarUseInfoTable caruseTable = new com.anbtech.br.entity.CarUseInfoTable();
					com.anbtech.br.entity.CarInfoTable carinfoTable = new com.anbtech.br.entity.CarInfoTable();

					caruseTable = carResourceDAO.getEachCarUseInfo(cr_id,login_id,login_name);
					request.setAttribute("each_lending_info", caruseTable);

					carinfoTable = carResourceDAO.getCarInfo(cid);
					request.setAttribute("each_car_info", carinfoTable);

					getServletContext().getRequestDispatcher("/br/car/enteringCar.jsp").forward(request,response);
				}

				/////////////////////////////////
				// ������������ �� �԰����� �󼼺���
				/////////////////////////////////
				else if(mode.equals("carinfo_view")){
					com.anbtech.br.db.CarResourceDAO carResourceDAO= new com.anbtech.br.db.CarResourceDAO(con);
					com.anbtech.br.entity.CarUseInfoTable caruseTable = new com.anbtech.br.entity.CarUseInfoTable();
					com.anbtech.br.entity.CarInfoTable carinfoTable = new com.anbtech.br.entity.CarInfoTable();

					caruseTable = carResourceDAO.getEachCarUseInfo(cr_id,login_id,login_name);
					request.setAttribute("each_lending_info", caruseTable);

					carinfoTable = carResourceDAO.getCarInfo(cid);
					request.setAttribute("each_car_info", carinfoTable);

					getServletContext().getRequestDispatcher("/br/car/carInfo.jsp").forward(request,response);
				}

				/////////////////////////////////
				// ������ ������ �԰�ó��(status == 7)
				/////////////////////////////////
				else if(mode.equals("entering_Process")) {
					com.anbtech.br.business.CarResourceBO carBO = new com.anbtech.br.business.CarResourceBO(con);
					carBO.enteringProcess(cid,cr_id,mgr_id,mgr_name,st,chg_cont,return_date);

					redirectUrl = "BookResourceServlet?category=car&mode=eachcar&cid="+cid+"&tablename=charyang_master";
				}

				/////////////////////////////////
				// 1������Ϸ� �� 2�������� ����Ʈ ���
				/////////////////////////////////
				else if(mode.equals("req_list")){ 
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					tablename = "charyang_master";
					ArrayList car_list = new ArrayList();
					car_list = carDAO.getFirstFlagList(tablename,mode,searchword,searchscope,category,page,login_id,cid);
					
					request.setAttribute("FirstFlag",car_list);
						
					com.anbtech.br.business.CarLinkBO redirectBO = new com.anbtech.br.business.CarLinkBO(con);
					com.anbtech.br.entity.CarLinkTable redirect = new com.anbtech.br.entity.CarLinkTable();
					redirect = redirectBO.getRedirect(tablename,mode,searchword,searchscope,category,page,login_id,cid);
					request.setAttribute("Redirect",redirect); 

					getServletContext().getRequestDispatcher("/br/car/firstAppCarList.jsp").forward(request,response);
				}
			}

			//ȸ�ǽ� ����
			else if(category.equals("meetroom")){
			}
			//������ ����
			else if(category.equals("eduroom")){
			}
			//�������� �� ����
			else if(category.equals("projector")){
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

		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/br/";
		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//��ξ����� �������
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

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
		String login_id		= sl.id;
		String login_name	= sl.name;
		String redirectUrl	= "";

		//���� �ĸ�����
		String tablename	= multi.getParameter("tablename");
		String mode			= multi.getParameter("mode");
		String page			= multi.getParameter("page")==null?"0":multi.getParameter("page");
		String no			= multi.getParameter("no");
		String searchword	= multi.getParameter("searchword")==null?"":multi.getParameter("searchword");
		String searchscope	= multi.getParameter("searchscope")==null?"":multi.getParameter("searchscope");
		String category		= multi.getParameter("category");

		//������ �ĸ����͵�(����)
		String cid				= multi.getParameter("cid");			// �� �ڿ��� ������ȣ
		String buy_date			= multi.getParameter("buy_date");		// ������
		String price			= multi.getParameter("price");			// ���԰�
		String model_name		= multi.getParameter("model_name");		// �𵨸�
		String maker_company	= multi.getParameter("maker_company");	// ����ȸ���
		String stat				= multi.getParameter("stat");			// �ڿ��� ���� ����
		
		//���������� �ʿ��� �Ķ��͵�
		String car_type			= multi.getParameter("car_type");		// ����(�¿�/����/Ʈ��)
		String car_no			= multi.getParameter("car_no");			// ������ȣ
		String produce_year		= multi.getParameter("produce_year");	// ���
		String fuel_type		= multi.getParameter("fuel_type");		// ���ᱸ��
		String fuel_efficiency	= multi.getParameter("fuel_efficiency");// ����
		String car_id			= multi.getParameter("car_id");			// ����������ȣ(ȸ�� ���ο��� �����Ǿ��� ���)

		//������ ����� ������
		String user_id			= multi.getParameter("user_id");		// ����� id (�ۼ���) - ���
		String user_name		= multi.getParameter("user_name");		// ����� �̸�
		
		//����� ����
		String user_code		= multi.getParameter("user_code");		// ����� ���� �ڵ�
		String user_rank		= multi.getParameter("user_rank");		// ����� ����
		String ac_id			= multi.getParameter("ac_id");			// ����� �μ�������ȣ
		String ac_code			= multi.getParameter("ac_code");		// ����� �μ��ڵ�
		String ac_name			= multi.getParameter("ac_name");		// ����� �μ���

		if(user_name!=null && (user_name.indexOf('/')<0)) {
			java.util.StringTokenizer token = new java.util.StringTokenizer(user_name,"/");
			int temp=token.countTokens();
			while(token.hasMoreTokens()){
				user_id=token.nextToken();
				user_name=token.nextToken();
			}
		}

		// ���� ���� ������
		String cr_id			= multi.getParameter("cr_id");			// ������ȣ(����Ͻú���)
		String fellow_names		= multi.getParameter("fellow_names");	// ������ ���/�̸�

		String sdate			= multi.getParameter("sdate");			// ���۳�¥
		String edate			= multi.getParameter("edate");			// �Ϸᳯ¥
		String stime			= multi.getParameter("stime");			// ���۽ð�
		String etime			= multi.getParameter("etime");			// �Ϸ�ð�
		String write_id			= multi.getParameter("write_id");
		String write_name		= multi.getParameter("write_name");

		String v_status			= multi.getParameter("v_status");		// �����ڿ�����
		String u_year			= multi.getParameter("u_year");			// ������û�ð�(��)
		String u_month			= multi.getParameter("u_month");		// ������û�ð�(��)
		String u_date			= multi.getParameter("u_date");			// ������û�ð�(��)
		String u_time			= multi.getParameter("u_time");			// ������û�ð�(��)
		String tu_year			= multi.getParameter("tu_year");		// �ݳ���û�ð�(��)
		String tu_month			= multi.getParameter("tu_month");		// �ݳ���û�ð�(��)
		String tu_date			= multi.getParameter("tu_date");		// �ݳ���û�ð�(��)
		String tu_time			= multi.getParameter("tu_time");		// �ݳ���û�ð�(��)
		String cr_purpose		= multi.getParameter("cr_purpose");		// ����
		String cr_dest			= multi.getParameter("cr_dest");		// �༱��
		String content			= multi.getParameter("content");		// �󼼳���(��������)
		String return_date		= multi.getParameter("return_date");	// �ݳ�����(��/��/��/��)
		String mgr_id			= multi.getParameter("mgr_id")==null?login_id:multi.getParameter("mgr_id");	// �ݳ�Ȯ���� ID
		String mgr_name			= multi.getParameter("mgr_name")==null?login_name:multi.getParameter("mgr_name");// �ݳ�Ȯ���� �̸�
		String chg_cont			= multi.getParameter("chg_cont");		// �������
		String c_year			= multi.getParameter("c_year");			// �ݳ�����ð�(��)
		String c_month			= multi.getParameter("c_month");		// �ݳ�����ð�(��)
		String c_date			= multi.getParameter("c_date");			// �ݳ�����ð�(��)
		String c_time			= multi.getParameter("c_time");			// �ݳ�����ð�(��)
		String md_date			= multi.getParameter("md_date");		// �ݳ����� ������(��/��/��/��)
		String em_tel			= multi.getParameter("em_tel");			// ��޿���ó
		String del_date			= multi.getParameter("del_date");		// ��������
		
		String doc_id			= multi.getParameter("doc_id");			// ����������ȣ
		String ys_kind			= multi.getParameter("ys_kind");		// BAE_CHA

		String file_path		= multi.getParameter("file_path");		// ������������path
		String fname			= multi.getParameter("fname");			// ÷�μ��������̸�
		String ftype			= multi.getParameter("ftype");			// ÷�μ���Ȯ���ڸ�
		String fsize			= multi.getParameter("fsize");			// ÷��ȭ��size
		
		String flag				= multi.getParameter("flag")==null?"":multi.getParameter("flag");	// 1���ְ��μ�(����flag)
		String flag2			= multi.getParameter("flag2")==null?"":multi.getParameter("flag2");;// 2���ְ��μ�(����flag)

		//���� ó���� �Էµ� ��¥ �и�
		if(sdate!=null) {						
			  u_year = sdate.substring(0,4);
			  u_month = sdate.substring(4,6);
			  u_date = sdate.substring(6,8);
			  u_time = stime;
		}

		if(edate!=null) {
			  tu_year = edate.substring(0,4);
			  tu_month = edate.substring(4,6);
			  tu_date = edate.substring(6,8);
			  tu_time = etime;
		}

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			if(category.equals("car")){
				//////////////////////////////////
				// �ű� ���� ���ó��
				//////////////////////////////////
				if(mode.equals("write_car")){
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					carDAO.saveNewCarInfo(car_type,car_no,model_name,produce_year,buy_date,price,fuel_type,fuel_efficiency,maker_company,car_id);

					redirectUrl = "BookResourceServlet?category=car&mode=list";
				}

				//////////////////////////////////
				// �������� ����ó��
				//////////////////////////////////
				else if(mode.equals("modify_car")){
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					carDAO.updateCarInfo(cid,car_type,car_no,model_name,produce_year,buy_date,price,fuel_type,fuel_efficiency,maker_company,car_id,stat);
					redirectUrl = "BookResourceServlet?category=car&mode=list";
				}

				//////////////////////////////////
				// ������������ ���ó��
				//////////////////////////////////
				else if(mode.equals("lendingsave")){
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					com.anbtech.br.business.CarResourceBO carBO = new com.anbtech.br.business.CarResourceBO(con);

					com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);

					//���� ���� com.anbtech.admin.entity
					com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
					user_info = userinfoDAO.getUserListById(user_id);
					
					user_name = user_info.getUserName();		//�ش��� ��
					user_code = user_info.getArCode();			//�ش��� ���� code
					user_rank = user_info.getUserRank();		//�ش��� ���޸�
					ac_id = user_info.getAcId();				//�ش��� �μ��� �����ڵ�
					ac_name = user_info.getDivision();			//�ش��� �μ��� 
					ac_code = user_info.getAcCode();			//�����ڵ�
				
					String startdate_str	= sdate+stime.substring(0,2)+stime.substring(3,5);
					String enddate_str		= edate+etime.substring(0,2)+etime.substring(3,5);
					
					//���డ�ɳ�¥�ΰ� üũ(��¥ ��ȿ�� �� �����ߺ� üũ)
					String check_msg ="";
					check_msg = carBO.checkLending(startdate_str,enddate_str,cid);
					
					if(!check_msg.equals(""))
					{
						PrintWriter out = response.getWriter();
						out.println("	<script>");
						out.println("	alert('"+check_msg+"');");
						out.println("	history.back()");
						out.println("	</script>");
						out.close();
						return;
					}							
					carDAO.saveLendingCar(cr_id,cid,write_id,write_name,user_id,user_name,user_code,user_rank,ac_id,ac_code,ac_name,fellow_names,u_year,u_month,u_date,u_time,tu_year,tu_month,tu_date,tu_time,cr_purpose,cr_dest,content,em_tel,flag,flag2);
					
					redirectUrl = "../gw/approval/module/baecha_sincheong_FP_App.jsp?link_id="+cr_id;
				}
			}

			//ȸ�ǽ� ����
			else if(category.equals("meetroom")){
			}
			//������ ����
			else if(category.equals("eduroom")){
			}
			//�������� �� ����
			else if(category.equals("projector")){
			}

			//redirectUrl�� ���� �����ÿ��� redirectUrl��η� �̵��Ѵ�.
			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	}
}
