import com.anbtech.dcm.db.*;
import com.anbtech.dcm.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class CbomProcessServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 15;
	private int max_display_page = 5;

	/********
	 * �Ҹ���
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get������� �Ѿ���� �� ó�� (��Ϻ���)
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//Session
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;

		//�⺻�Ķ����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"ecc_premodify":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"ecc_subject":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//ECC_COM������ ���� �⺻�Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String ecc_subject = Hanguel.toHanguel(request.getParameter("ecc_subject"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_subject"));
		String eco_no = Hanguel.toHanguel(request.getParameter("eco_no"))==null?"":Hanguel.toHanguel(request.getParameter("eco_no"));
		String ecr_id = Hanguel.toHanguel(request.getParameter("ecr_id"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_id"));
		String ecr_name = Hanguel.toHanguel(request.getParameter("ecr_name"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_name"));
		String ecr_div_code = Hanguel.toHanguel(request.getParameter("ecr_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_div_code"));
		String ecr_div_name = Hanguel.toHanguel(request.getParameter("ecr_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_div_name"));
		String ecr_tel = Hanguel.toHanguel(request.getParameter("ecr_tel"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_tel"));
		String ecr_date = Hanguel.toHanguel(request.getParameter("ecr_date"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_date"));
		String mgr_id = Hanguel.toHanguel(request.getParameter("mgr_id"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_id"));
		String mgr_name = Hanguel.toHanguel(request.getParameter("mgr_name"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_name"));
		String mgr_code = Hanguel.toHanguel(request.getParameter("mgr_code"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_code"));
		String mgr_div_code = Hanguel.toHanguel(request.getParameter("mgr_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_div_code"));
		String mgr_div_name = Hanguel.toHanguel(request.getParameter("mgr_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_div_name"));
		String eco_id = Hanguel.toHanguel(request.getParameter("eco_id"))==null?"":Hanguel.toHanguel(request.getParameter("eco_id"));
		String eco_name = Hanguel.toHanguel(request.getParameter("eco_name"))==null?"":Hanguel.toHanguel(request.getParameter("eco_name"));
		String eco_code = Hanguel.toHanguel(request.getParameter("eco_code"))==null?"":Hanguel.toHanguel(request.getParameter("eco_code"));
		String eco_div_code = Hanguel.toHanguel(request.getParameter("eco_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("eco_div_code"));
		String eco_div_name = Hanguel.toHanguel(request.getParameter("eco_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("eco_div_name"));
		String eco_tel = Hanguel.toHanguel(request.getParameter("eco_tel"))==null?"":Hanguel.toHanguel(request.getParameter("eco_tel"));
		String ecc_reason = Hanguel.toHanguel(request.getParameter("ecc_reason"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_reason"));
		String ecc_factor = Hanguel.toHanguel(request.getParameter("ecc_factor"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_factor"));
		String ecc_scope = Hanguel.toHanguel(request.getParameter("ecc_scope"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_scope"));
		String ecc_kind = Hanguel.toHanguel(request.getParameter("ecc_kind"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_kind"));
		String pdg_code = Hanguel.toHanguel(request.getParameter("pdg_code"))==null?"":Hanguel.toHanguel(request.getParameter("pdg_code"));
		String pd_code = Hanguel.toHanguel(request.getParameter("pd_code"))==null?"":Hanguel.toHanguel(request.getParameter("pd_code"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String part_code = Hanguel.toHanguel(request.getParameter("part_code"))==null?"":Hanguel.toHanguel(request.getParameter("part_code"));
		String order_date = Hanguel.toHanguel(request.getParameter("order_date"))==null?"":Hanguel.toHanguel(request.getParameter("order_date"));
		String fix_date = Hanguel.toHanguel(request.getParameter("fix_date"))==null?"":Hanguel.toHanguel(request.getParameter("fix_date"));
		String ecc_status = Hanguel.toHanguel(request.getParameter("ecc_status"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_status"));
		
		//ECC_REQ/ECC_ORD������ ���� �⺻�Ķ����
		String chg_position = Hanguel.toHanguel(request.getParameter("chg_position"))==null?"":Hanguel.toHanguel(request.getParameter("chg_position"));
		String trouble = Hanguel.toHanguel(request.getParameter("trouble"))==null?"":Hanguel.toHanguel(request.getParameter("trouble"));
		String condition = Hanguel.toHanguel(request.getParameter("condition"))==null?"":Hanguel.toHanguel(request.getParameter("condition"));
		String solution = Hanguel.toHanguel(request.getParameter("solution"))==null?"":Hanguel.toHanguel(request.getParameter("solution"));
		String fname = Hanguel.toHanguel(request.getParameter("fname"))==null?"":Hanguel.toHanguel(request.getParameter("fname"));
		String sname = Hanguel.toHanguel(request.getParameter("sname"))==null?"":Hanguel.toHanguel(request.getParameter("sname"));
		String ftype = Hanguel.toHanguel(request.getParameter("ftype"))==null?"":Hanguel.toHanguel(request.getParameter("ftype"));
		String fsize = Hanguel.toHanguel(request.getParameter("fsize"))==null?"":Hanguel.toHanguel(request.getParameter("fsize"));
		String app_no = Hanguel.toHanguel(request.getParameter("app_no"))==null?"":Hanguel.toHanguel(request.getParameter("app_no"));
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		ECC ���� ó���ϱ�
			//------------------------------------------------------------
			//ECC_COM/ECC_REQ/ECC_ORD ���� �غ�
			if ("ecc_premodify".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//ECC_COM ������ ������ �б�
				com.anbtech.dcm.entity.eccComTable eccT = new com.anbtech.dcm.entity.eccComTable();
				eccT = cmodDAO.readEccCom(pid);
				request.setAttribute("COM_List", eccT);

				//ECC_REQ ������ ������ �б�
				com.anbtech.dcm.entity.eccReqTable ecrT = new com.anbtech.dcm.entity.eccReqTable();
				ecrT = cmodDAO.readEccReq(pid);
				request.setAttribute("REQ_List", ecrT);

				//ECC_ORD ������ ������ �б�
				com.anbtech.dcm.entity.eccOrdTable ecoT = new com.anbtech.dcm.entity.eccOrdTable();
				ecoT = cmodDAO.readEccOrd(pid);
				request.setAttribute("ORD_List", ecoT);

				//���躯�� �����׸� ������ �б�
				ArrayList ecr_list = new ArrayList();
				ecr_list = cmodDAO.getEccItem("F01");			//[F01:��������]
				request.setAttribute("ECR_List", ecr_list); 

				ArrayList ecf_list = new ArrayList();
				ecf_list = cmodDAO.getEccItem("F02");			//[F02:���뱸��]
				request.setAttribute("ECF_List", ecf_list); 

				ArrayList ecs_list = new ArrayList();
				ecs_list = cmodDAO.getEccItem("F03");			//[F03:�������]
				request.setAttribute("ECS_List", ecs_list); 

				ArrayList eck_list = new ArrayList();
				eck_list = cmodDAO.getEccItem("F04");			//[F04:��������]
				request.setAttribute("ECK_List", eck_list); 

				//�б��ϱ�
				String where = "where pid = '"+pid+"'";
				ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status",where);
				eco_no = cmodDAO.getColumData("ECC_COM","eco_no",where);
				String eco_no_head = eco_no.substring(0,1);
				if(ecc_status.equals("0") || ecc_status.equals("1")) {	//ECR ����
					getServletContext().getRequestDispatcher("/dcm/ecc/ecrInfoModify.jsp").forward(request,response);
				} else {												//ECO ����
					if(eco_no_head.equals("R"))				//ECR���Ǻ��� ���۵� ECO�ۼ�
						getServletContext().getRequestDispatcher("/dcm/ecc/eccInfoModify.jsp").forward(request,response);
					else if(eco_no_head.equals("E"))		//ECO���ǽ�			
						getServletContext().getRequestDispatcher("/dcm/ecc/ecoInfoModify.jsp").forward(request,response);
				}
			
			}
			//ECC_REQ����
			else if ("req_view".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);
				
				//ECC_COM ������ �б�
				com.anbtech.dcm.entity.eccComTable eccT = new com.anbtech.dcm.entity.eccComTable();
				eccT = cmodDAO.readEccCom(pid);
				request.setAttribute("COM_List", eccT);

				//ECC_REQ ������ �б�
				com.anbtech.dcm.entity.eccReqTable ecrT = new com.anbtech.dcm.entity.eccReqTable();
				ecrT = cmodDAO.readEccReq(pid);
				request.setAttribute("REQ_List", ecrT);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/eccReqView.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		������ LIST ó���ϱ�
			//------------------------------------------------------------
			//���� �ۼ��� LIST
			else if ("ecc_iwlist".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//�����ۼ��� LIST
				ArrayList ecc_list = new ArrayList();
				ecc_list = cmodDAO.getEccComList(sItem,sWord,"IW",login_id,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//�������� �ٷΰ��� List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = cmodDAO.getDisplayPage(sItem,sWord,"IW",login_id,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
				request.setAttribute("msg", msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/eccIndWriteList.jsp").forward(request,response);
			}
			//���� ������ LIST
			else if ("ecc_irlist".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//���μ����� LIST
				ArrayList ecc_list = new ArrayList();
				ecc_list = cmodDAO.getEccComList(sItem,sWord,"IR",login_id,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//�������� �ٷΰ��� List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = cmodDAO.getDisplayPage(sItem,sWord,"IR",login_id,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
				request.setAttribute("msg", msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/eccIndReceiveList.jsp").forward(request,response);
			}
			//���� �߽��� LIST
			else if ("ecc_islist".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//���ι߽��� LIST
				ArrayList ecc_list = new ArrayList();
				ecc_list = cmodDAO.getEccComList(sItem,sWord,"IS",login_id,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//�������� �ٷΰ��� List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = cmodDAO.getDisplayPage(sItem,sWord,"IS",login_id,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
				request.setAttribute("msg", msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/eccIndSendList.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		�μ��� LIST ó���ϱ�
			//------------------------------------------------------------
			//�μ� ������ LIST
			else if ("ecc_drlist".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//�μ� ������ LIST
				ArrayList ecc_list = new ArrayList();
				ecc_list = cmodDAO.getEccComList(sItem,sWord,"DR",login_id,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//�������� �ٷΰ��� List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = cmodDAO.getDisplayPage(sItem,sWord,"DR",login_id,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
				request.setAttribute("msg", msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/eccDivReceiveList.jsp").forward(request,response);
			}
			//�μ� �߽��� LIST
			else if ("ecc_dslist".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//�μ� �߽��� LIST
				ArrayList ecc_list = new ArrayList();
				ecc_list = cmodDAO.getEccComList(sItem,sWord,"DS",login_id,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//�������� �ٷΰ��� List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = cmodDAO.getDisplayPage(sItem,sWord,"DS",login_id,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
				request.setAttribute("msg", msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/eccDivSendList.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		������ ���� ����
			//------------------------------------------------------------
			// �����Ž� �󼼳��� ����
			else if ("ecc_app".equals(mode) || "ecc_app_print".equals(mode) || "ecc_app_mail".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);
				
				//ECC_COM ������ �б�
				com.anbtech.dcm.entity.eccComTable eccT = new com.anbtech.dcm.entity.eccComTable();
				eccT = cmodDAO.readEccCom(pid);
				request.setAttribute("COM_List", eccT);

				//ECC_MODEL ������ �б�
				String where = "where pid = '"+pid+"'";
				eco_no = cmodDAO.getColumData("ECC_COM","eco_no",where);
				ArrayList eccM = new ArrayList();
				eccM = cmodDAO.getEcoModel(eco_no);
				request.setAttribute("MODEL_List", eccM);

				//ECC_REQ ������ �б�
				com.anbtech.dcm.entity.eccReqTable ecrT = new com.anbtech.dcm.entity.eccReqTable();
				ecrT = cmodDAO.readEccReq(pid);
				request.setAttribute("REQ_List", ecrT);

				//ECC_ORD ������ �б�
				com.anbtech.dcm.entity.eccOrdTable ecoT = new com.anbtech.dcm.entity.eccOrdTable();
				ecoT = cmodDAO.readEccOrd(pid);
				request.setAttribute("ORD_List", ecoT);

				//gid 
				where = "where eco_no = '"+eco_no+"'";
				String fg_code_list = cmodDAO.getColumData("ecc_com","fg_code",where);
				StringTokenizer list = new StringTokenizer(fg_code_list,"\n");
				if(list.hasMoreTokens()) fg_code = list.nextToken();
			
				where = "where fg_code = '"+fg_code+"'";
				String gid = cmodDAO.getColumData("mbom_master","pid",where);
				request.setAttribute("gid",gid);
	
				//�б��ϱ�
				if("ecc_app".equals(mode))
					getServletContext().getRequestDispatcher("/dcm/ecc/eccInfoApp.jsp").forward(request,response);
				else if("ecc_app_mail".equals(mode))
					getServletContext().getRequestDispatcher("/dcm/ecc/eccInfoApp_mail.jsp").forward(request,response);
				else 
					getServletContext().getRequestDispatcher("/dcm/ecc/eccInfoAppPrint.jsp").forward(request,response);

			}

			//------------------------------------------------------------
			//		ECO AUDOT ó���ϱ�
			//------------------------------------------------------------
			//ECO AUDIT LIST
			else if ("audit_list".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//ECO AUDIT  LIST
				ArrayList ecc_list = new ArrayList();
				ecc_list = cmodDAO.getEccComList(sItem,sWord,"EA",login_id,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//�������� �ٷΰ��� List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = cmodDAO.getDisplayPage(sItem,sWord,"EA",login_id,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
				request.setAttribute("msg", msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/auditEcoList.jsp").forward(request,response);
			}

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
		
	} //doGet()

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
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;

		//MultipartRequest ũ��, ������丮
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/dcm/"+login_id+"/addfile";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory�����ϱ�
		String maxFileSize = "10";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//�⺻�Ķ����
		String mode = multi.getParameter("mode")==null?"ecc_premodify":multi.getParameter("mode");
		String page = multi.getParameter("page")==null?"1":multi.getParameter("page");
		
		//������ ����� �Ѿ���� �Ķ����
		String sItem = multi.getParameter("sItem")==null?"ecc_subject":multi.getParameter("sItem");
		String sWord = multi.getParameter("sWord")==null?"":multi.getParameter("sWord");
		
		//ECC_COM������ ���� �⺻�Ķ����
		String pid = multi.getParameter("pid")==null?"":multi.getParameter("pid");
		String ecc_subject = multi.getParameter("ecc_subject")==null?"":multi.getParameter("ecc_subject");
		String eco_no = multi.getParameter("eco_no")==null?"":multi.getParameter("eco_no");
		String ecr_id = multi.getParameter("ecr_id")==null?"":multi.getParameter("ecr_id");
		String ecr_name = multi.getParameter("ecr_name")==null?"":multi.getParameter("ecr_name");
		String ecr_code = multi.getParameter("ecr_code")==null?"":multi.getParameter("ecr_code");
		String ecr_div_code = multi.getParameter("ecr_div_code")==null?"":multi.getParameter("ecr_div_code");
		String ecr_div_name = multi.getParameter("ecr_div_name")==null?"":multi.getParameter("ecr_div_name");
		String ecr_tel = multi.getParameter("ecr_tel")==null?"":multi.getParameter("ecr_tel");
		String ecr_date = multi.getParameter("ecr_date")==null?"":multi.getParameter("ecr_date");
		String mgr_id = multi.getParameter("mgr_id")==null?"":multi.getParameter("mgr_id");
		String mgr_name = multi.getParameter("mgr_name")==null?"":multi.getParameter("mgr_name");
		String mgr_code = multi.getParameter("mgr_code")==null?"":multi.getParameter("mgr_code");
		String mgr_div_code = multi.getParameter("mgr_div_code")==null?"":multi.getParameter("mgr_div_code");
		String mgr_div_name = multi.getParameter("mgr_div_name")==null?"":multi.getParameter("mgr_div_name");
		String eco_id = multi.getParameter("eco_id")==null?"":multi.getParameter("eco_id");
		String eco_name = multi.getParameter("eco_name")==null?"":multi.getParameter("eco_name");
		String eco_code = multi.getParameter("eco_code")==null?"":multi.getParameter("eco_code");
		String eco_div_code = multi.getParameter("eco_div_code")==null?"":multi.getParameter("eco_div_code");
		String eco_div_name = multi.getParameter("eco_div_name")==null?"":multi.getParameter("eco_div_name");
		String eco_tel = multi.getParameter("eco_tel")==null?"":multi.getParameter("eco_tel");
		String ecc_reason = multi.getParameter("ecc_reason")==null?"":multi.getParameter("ecc_reason");
		String ecc_factor = multi.getParameter("ecc_factor")==null?"":multi.getParameter("ecc_factor");
		String ecc_scope = multi.getParameter("ecc_scope")==null?"":multi.getParameter("ecc_scope");
		String ecc_kind = multi.getParameter("ecc_kind")==null?"":multi.getParameter("ecc_kind");
		String pdg_code = multi.getParameter("pdg_code")==null?"":multi.getParameter("pdg_code");
		String pd_code = multi.getParameter("pd_code")==null?"":multi.getParameter("pd_code");
		String fg_code = multi.getParameter("fg_code")==null?"":multi.getParameter("fg_code");
		String part_code = multi.getParameter("part_code")==null?"":multi.getParameter("part_code");
		String order_date = multi.getParameter("order_date")==null?"":multi.getParameter("order_date");
		String fix_date = multi.getParameter("fix_date")==null?"":multi.getParameter("fix_date");
		String ecc_status = multi.getParameter("ecc_status")==null?"":multi.getParameter("ecc_status");
		
		//ECC_REQ / ECC_ORD������ ���� �⺻�Ķ����
		String chg_position = multi.getParameter("chg_position")==null?"":multi.getParameter("chg_position");
		String trouble = multi.getParameter("trouble")==null?"":multi.getParameter("trouble");
		String condition = multi.getParameter("condition")==null?"":multi.getParameter("condition");
		String solution = multi.getParameter("solution")==null?"":multi.getParameter("solution");
		String fname = multi.getParameter("fname")==null?"":multi.getParameter("fname");
		String sname = multi.getParameter("sname")==null?"":multi.getParameter("sname");
		String ftype = multi.getParameter("ftype")==null?"":multi.getParameter("ftype");
		String fsize = multi.getParameter("fsize")==null?"":multi.getParameter("fsize");
		String app_no = multi.getParameter("app_no")==null?"":multi.getParameter("app_no");

		//view���� �������ϴ� ECR, ECO���ù� ��Ϻ���, ������� ���ư� mode
		//String ECC = multi.getParameter("ECC")==null?"ECR":multi.getParameter("ECC");//ECR,ECO���뺸�ⱸ��
		String v_list = multi.getParameter("v_list")==null?"ecc_irlist":multi.getParameter("v_list");//���mode
		
		//������ ����÷�ι��� ó���ϱ�
		String attache_cnt = multi.getParameter("attache_cnt")==null?"0":multi.getParameter("attache_cnt");	//÷�ΰ��� �ִ���� �̸�
		String msg = multi.getParameter("msg")==null?"":multi.getParameter("msg");
		
		//����÷������ �����ϱ�
		int p_cnt = Integer.parseInt(attache_cnt);
		String[] delfile = new String[p_cnt];
		for(int i=0; i<p_cnt; i++) delfile[i] = "";

		for(int i=0,no=1; i<p_cnt; i++,no++) {
			String df="delfile"+no; 
			delfile[i]=multi.getParameter(df)==null?"":multi.getParameter(df);
		}

		//������� å���� ó���ϱ� �� ECO AUDITó��
		String user_id = multi.getParameter("user_id")==null?"":multi.getParameter("user_id");
		String user_name = multi.getParameter("user_name")==null?"":multi.getParameter("user_name");
		String note = multi.getParameter("note")==null?"":multi.getParameter("note");
		String receivers = multi.getParameter("receivers")==null?"":multi.getParameter("receivers");	
		String server_url = multi.getParameter("server_url")==null?"":multi.getParameter("server_url");	
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		ECC ����/���� ó���ϱ�
			//------------------------------------------------------------
			//ECC_COM/ECC_REQ/ECC_ORD ���� �غ�
			if ("ecc_premodify".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//ECC_COM ������ ������ �б�
				com.anbtech.dcm.entity.eccComTable eccT = new com.anbtech.dcm.entity.eccComTable();
				eccT = cmodDAO.readEccCom(pid);
				request.setAttribute("COM_List", eccT);

				//ECC_REQ ������ ������ �б�
				com.anbtech.dcm.entity.eccReqTable ecrT = new com.anbtech.dcm.entity.eccReqTable();
				ecrT = cmodDAO.readEccReq(pid);
				request.setAttribute("REQ_List", ecrT);

				//ECC_ORD ������ ������ �б�
				com.anbtech.dcm.entity.eccOrdTable ecoT = new com.anbtech.dcm.entity.eccOrdTable();
				ecoT = cmodDAO.readEccOrd(pid);
				request.setAttribute("ORD_List", ecoT);

				//���躯�� �����׸� ������ �б�
				ArrayList ecr_list = new ArrayList();
				ecr_list = cmodDAO.getEccItem("F01");			//[F01:��������]
				request.setAttribute("ECR_List", ecr_list); 

				ArrayList ecf_list = new ArrayList();
				ecf_list = cmodDAO.getEccItem("F02");			//[F02:���뱸��]
				request.setAttribute("ECF_List", ecf_list); 

				ArrayList ecs_list = new ArrayList();
				ecs_list = cmodDAO.getEccItem("F03");			//[F03:�������]
				request.setAttribute("ECS_List", ecs_list); 

				ArrayList eck_list = new ArrayList();
				eck_list = cmodDAO.getEccItem("F04");			//[F04:��������]
				request.setAttribute("ECK_List", eck_list); 

				//�б��ϱ�
				String where = "where pid = '"+pid+"'";
				ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status",where);
				eco_no = cmodDAO.getColumData("ECC_COM","eco_no",where);
				String eco_no_head = eco_no.substring(0,1);
				if(ecc_status.equals("0") || ecc_status.equals("1")) {	//ECR ����
					getServletContext().getRequestDispatcher("/dcm/ecc/ecrInfoModify.jsp").forward(request,response);
				} else {												//ECO ����
					if(eco_no_head.equals("R"))				//ECR���Ǻ��� ���۵� ECO�ۼ�
						getServletContext().getRequestDispatcher("/dcm/ecc/eccInfoModify.jsp").forward(request,response);
					else if(eco_no_head.equals("E"))		//ECO���ǽ�			
						getServletContext().getRequestDispatcher("/dcm/ecc/ecoInfoModify.jsp").forward(request,response);
				}
			}
			//ECC_COM/ECC_REQ/ECC_ORD ����
			else if("ecc_modify".equals(mode)) {
				com.anbtech.dcm.business.CbomBaseInfoBO cinfoBO = new com.anbtech.dcm.business.CbomBaseInfoBO(con);
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String save_head = ""; //÷������ ������ ���ι���

					//ECR or ECO ���� ���۵� ������ �Ǵ��ϱ�
					String noh = eco_no.substring(2,3);			//ECO NO�� ù����
					
					//ECR [ECC_COM,ECC_REQ] �����ϱ� 
					if(noh.equals("R") && (ecc_status.equals("0") || ecc_status.equals("1"))) {
						msg=cinfoBO.updateECR(pid,ecc_subject,ecr_id,ecr_name,ecr_code,ecr_div_code,ecr_div_name,ecr_tel,ecr_date,mgr_id,ecc_kind,pdg_code,pd_code,fg_code,part_code,chg_position,trouble,condition,solution);
						//÷�ι��� �ٽ� ó���ϱ�
						save_head = "r";							//÷������ ��������� ECR���� �˷��ش�
						cinfoBO.setUpdateFile(multi,"ECC_REQ",pid,save_head,filepath,fname,sname,ftype,fsize,attache_cnt,delfile);	
					} 
					//ECO [ECC_COM,ECC_ORD] �����ϱ�
					else {
						msg=cinfoBO.updateECO(pid,ecc_subject,ecr_id,ecr_name,ecr_code,ecr_div_code,ecr_div_name,ecr_tel,ecr_date,eco_id,eco_tel,ecc_reason,ecc_factor,ecc_scope,ecc_kind,pdg_code,pd_code,fg_code,part_code,order_date,chg_position,trouble,condition,solution);
						//÷�ι��� �ٽ� ó���ϱ�
						save_head = "o";							//÷������ ��������� ECO���� �˷��ش�
						cinfoBO.setUpdateFile(multi,"ECC_ORD",pid,save_head,filepath,fname,sname,ftype,fsize,attache_cnt,delfile);	
					}

					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//---- ������ ����� ������ ȭ�鿡 �״�� ����Ѵ�.----//
					out.println("	<script>");
					out.println("	self.location.href('CbomProcessServlet?mode=ecc_iwlist&pid="+pid+"&msg="+msg+"');");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			//ECC_COM/ECC_REQ/ECC_ORD �����ϱ�
			else if("ecc_delete".equals(mode)) {
				com.anbtech.dcm.business.CbomBaseInfoBO cinfoBO = new com.anbtech.dcm.business.CbomBaseInfoBO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					
					//ECR �����ϱ�
					if(ecc_status.equals("1")) {
						msg=cinfoBO.deleteECR(pid,filepath);
					} 
					//ECO �����ϱ�
					else if(ecc_status.equals("6")) {
						msg=cinfoBO.deleteECO(pid,filepath);
					}
					//�׿ܴ� �����Ұ�
					else msg = "������ �� �����ϴ�.";

					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//---- ������ ����� ������ ȭ�鿡 �״�� ����Ѵ�.----//
					out.println("	<script>");
					out.println("	self.location.href('CbomProcessServlet?mode=ecc_iwlist&pid="+pid+"&msg="+msg+"');");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			//------------------------------------------------------------
			//		������ LIST ó���ϱ�
			//------------------------------------------------------------
			//���� �ۼ��� LIST
			else if ("ecc_iwlist".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//�����ۼ��� LIST
				ArrayList ecc_list = new ArrayList();
				ecc_list = cmodDAO.getEccComList(sItem,sWord,"IW",login_id,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//�������� �ٷΰ��� List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = cmodDAO.getDisplayPage(sItem,sWord,"IW",login_id,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/eccIndWriteList.jsp").forward(request,response);
			}
			//���� ������ LIST
			else if ("ecc_irlist".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//���μ����� LIST
				ArrayList ecc_list = new ArrayList();
				ecc_list = cmodDAO.getEccComList(sItem,sWord,"IR",login_id,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//�������� �ٷΰ��� List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = cmodDAO.getDisplayPage(sItem,sWord,"IR",login_id,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/eccIndReceiveList.jsp").forward(request,response);
			}
			//���� �߽��� LIST
			else if ("ecc_islist".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//���ι߽��� LIST
				ArrayList ecc_list = new ArrayList();
				ecc_list = cmodDAO.getEccComList(sItem,sWord,"IS",login_id,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//�������� �ٷΰ��� List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = cmodDAO.getDisplayPage(sItem,sWord,"IS",login_id,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/eccIndSendList.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		�μ��� LIST ó���ϱ�
			//------------------------------------------------------------
			//�μ� ������ LIST
			else if ("ecc_drlist".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//�μ� ������ LIST
				ArrayList ecc_list = new ArrayList();
				ecc_list = cmodDAO.getEccComList(sItem,sWord,"DR",login_id,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//�������� �ٷΰ��� List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = cmodDAO.getDisplayPage(sItem,sWord,"DR",login_id,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/eccDivReceiveList.jsp").forward(request,response);
			}
			//�μ� �߽��� LIST
			else if ("ecc_dslist".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//�μ� �߽��� LIST
				ArrayList ecc_list = new ArrayList();
				ecc_list = cmodDAO.getEccComList(sItem,sWord,"DS",login_id,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//�������� �ٷΰ��� List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = cmodDAO.getDisplayPage(sItem,sWord,"DS",login_id,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/eccDivSendList.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		���� ���� ó���ϱ�
			//------------------------------------------------------------
			//ECC_COM/ECC_REQ/ECC_ORD ���뺸��
			else if ("ecc_view".equals(mode) || "ecc_mview".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//ECC_COM ������ ������ �б�
				com.anbtech.dcm.entity.eccComTable eccT = new com.anbtech.dcm.entity.eccComTable();
				eccT = cmodDAO.readEccCom(pid);
				request.setAttribute("COM_List", eccT);

				//ECC_REQ ������ ������ �б�
				com.anbtech.dcm.entity.eccReqTable ecrT = new com.anbtech.dcm.entity.eccReqTable();
				ecrT = cmodDAO.readEccReq(pid);
				request.setAttribute("REQ_List", ecrT);

				//ECC_ORD ������ ������ �б�
				com.anbtech.dcm.entity.eccOrdTable ecoT = new com.anbtech.dcm.entity.eccOrdTable();
				ecoT = cmodDAO.readEccOrd(pid);
				request.setAttribute("ORD_List", ecoT);

				//ECR���� ECO���� �Ǵ��ϱ�
				//request.setAttribute("ECC",ECC);

				//�б��ϱ�
				if("ecc_view".equals(mode)) {			//���ι߽���, �μ�������, �μ��߽���
					request.setAttribute("v_list",v_list);
					getServletContext().getRequestDispatcher("/dcm/ecc/eccInfoView.jsp").forward(request,response);
				} else if("ecc_mview".equals(mode)) {	//���μ����� å���� or ����� �� ���
					request.setAttribute("v_list",v_list);
					getServletContext().getRequestDispatcher("/dcm/ecc/eccInfoMgrView.jsp").forward(request,response);
				} 
			}
			//------------------------------------------------------------
			//		������� å���� ó���ϱ�
			//------------------------------------------------------------
			// ������� å���� ����
			else if("mgr_change".equals(mode)) {
				com.anbtech.dcm.business.CbomProcessBO prsBO = new com.anbtech.dcm.business.CbomProcessBO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					msg = prsBO.changeMgr(pid,mgr_id,user_id,user_name,note);

					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//---- ������ ����� ������ ȭ�鿡 �״�� ����Ѵ�.----//
					out.println("	<script>");
					out.println("	opener.location.href('CbomProcessServlet?mode=ecc_irlist&msg="+msg+"');");
					out.println("	self.close();");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			// ������� å������ ECR�ݷ�
			else if("mgr_reject".equals(mode)) {
				com.anbtech.dcm.business.CbomProcessBO prsBO = new com.anbtech.dcm.business.CbomProcessBO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					msg = prsBO.rejectMgr(pid,eco_id,eco_name,user_id,user_name,note);

					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//---- ������ ����� ������ ȭ�鿡 �״�� ����Ѵ�.----//
					out.println("	<script>");
					out.println("	location.href('CbomProcessServlet?mode=ecc_irlist&msg="+msg+"');");
					out.println("	self.close();");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			// ������� å������ ���������
			else if("eco_assign".equals(mode)) {
				com.anbtech.dcm.business.CbomProcessBO prsBO = new com.anbtech.dcm.business.CbomProcessBO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					msg = prsBO.setUser(pid,eco_id,mgr_id,mgr_name,note);

					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//---- ������ ����� ������ ȭ�鿡 �״�� ����Ѵ�.----//
					out.println("	<script>");
					out.println("	location.href('CbomProcessServlet?mode=ecc_irlist&msg="+msg+"');");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//------------------------------------------------------------
			//		ECO AUDOT ó���ϱ�
			//------------------------------------------------------------
			//ECO AUDIT LIST
			else if ("audit_list".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//ECO AUDIT  LIST
				ArrayList ecc_list = new ArrayList();
				ecc_list = cmodDAO.getEccComList(sItem,sWord,"EA",login_id,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//�������� �ٷΰ��� List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = cmodDAO.getDisplayPage(sItem,sWord,"EA",login_id,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
				request.setAttribute("msg", msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/auditEcoList.jsp").forward(request,response);
			}
			//ECO AUDIT ���뺸��
			else if ("audit_view".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//ECC_COM ������ �б�
				com.anbtech.dcm.entity.eccComTable eccT = new com.anbtech.dcm.entity.eccComTable();
				eccT = cmodDAO.readEccCom(pid);
				request.setAttribute("COM_List", eccT);

				//ECC_MODEL ������ �б�
				String where = "where pid = '"+pid+"'";
				eco_no = cmodDAO.getColumData("ECC_COM","eco_no",where);
				ArrayList eccM = new ArrayList();
				eccM = cmodDAO.getEcoModel(eco_no);
				request.setAttribute("MODEL_List", eccM);

				//ECC_REQ ������ �б�
				com.anbtech.dcm.entity.eccReqTable ecrT = new com.anbtech.dcm.entity.eccReqTable();
				ecrT = cmodDAO.readEccReq(pid);
				request.setAttribute("REQ_List", ecrT);

				//ECC_ORD ������ �б�
				com.anbtech.dcm.entity.eccOrdTable ecoT = new com.anbtech.dcm.entity.eccOrdTable();
				ecoT = cmodDAO.readEccOrd(pid);
				request.setAttribute("ORD_List", ecoT);

				//gid 
				where = "where eco_no = '"+eco_no+"'";
				String fg_code_list = cmodDAO.getColumData("ecc_com","fg_code",where);
				StringTokenizer list = new StringTokenizer(fg_code_list,"\n");
				if(list.hasMoreTokens()) fg_code = list.nextToken();
			
				where = "where fg_code = '"+fg_code+"'";
				String gid = cmodDAO.getColumData("mbom_master","pid",where);
				request.setAttribute("gid",gid);

				//�������� �ִ��� �Ǵ��ϱ�
				msg = modDAO.getEcoAuditGrade(login_id);
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/auditEcoView.jsp").forward(request,response);
				
			}
			// ECO AUDIT ����
			else if("audit_app".equals(mode)) {
				com.anbtech.bm.db.BomApprovalDAO appDAO = new com.anbtech.bm.db.BomApprovalDAO(con);
				com.anbtech.dcm.business.CbomProcessBO prsBO = new com.anbtech.dcm.business.CbomProcessBO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					appDAO.changeBomFix(eco_no,order_date);			//BOM STR�� �ݿ�
					appDAO.setEccStatus(pid,"","app");				//ECC COM�� ���λ��·� 
					prsBO.sendAuditMail(server_url,ecc_subject,user_id,user_name,pid,eco_no,receivers,note); //��������
					msg = "ECO AUDIT�� ���������� ���εǾ����ϴ�.";

					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//---- ������ ����� ������ ȭ�鿡 �״�� ����Ѵ�.----//
					out.println("	<script>");
					out.println("	location.href('CbomProcessServlet?mode=audit_list&msg="+msg+"');");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			// ECO AUDIT �ݷ�
			else if("audit_rej".equals(mode)) {
				com.anbtech.bm.db.BomApprovalDAO appDAO = new com.anbtech.bm.db.BomApprovalDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					appDAO.setEccBomStatus(eco_no,"0");				//ECC BOM�� ���� ��������
					appDAO.setEccStatus(pid,"","rej");				//ECC COM�� �ݷ����·� 
					msg = "ECO AUDIT�� �ݷ��Ǿ����ϴ�.";

					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//---- ������ ����� ������ ȭ�鿡 �״�� ����Ѵ�.----//
					out.println("	<script>");
					out.println("	location.href('CbomProcessServlet?mode=audit_list&msg="+msg+"');");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
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

