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

public class CbomBaseInfoServlet extends HttpServlet {
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"ecr_prewrite":Hanguel.toHanguel(request.getParameter("mode"));
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
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		ECR �⺻���� ó���ϱ�
			//------------------------------------------------------------
			//ECC_COM/ECC_REQ ���/���� �غ�
			if ("ecr_prewrite".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//ECC_COM �Է�/������ ������ �б�
				com.anbtech.dcm.entity.eccComTable eccT = new com.anbtech.dcm.entity.eccComTable();
				eccT = cmodDAO.readEccCom(pid);
				request.setAttribute("COM_List", eccT);

				//ECC_REQ �Է�/������ ������ �б�
				com.anbtech.dcm.entity.eccReqTable ecrT = new com.anbtech.dcm.entity.eccReqTable();
				ecrT = cmodDAO.readEccReq(pid);
				request.setAttribute("REQ_List", ecrT);

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
				getServletContext().getRequestDispatcher("/dcm/base/ecrInfoReg.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		ECO �⺻���� ó���ϱ�
			//------------------------------------------------------------
			//ECC_COM/ECC_ORD ���/���� �غ�
			else if ("eco_prewrite".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//ECC_COM �Է�/������ ������ �б�
				com.anbtech.dcm.entity.eccComTable eccT = new com.anbtech.dcm.entity.eccComTable();
				eccT = cmodDAO.readEccCom(pid);
				request.setAttribute("COM_List", eccT);

				//ECC_ORD �Է�/������ ������ �б�
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
				getServletContext().getRequestDispatcher("/dcm/base/ecoInfoReg.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		�ش��ǰ ã��
			//------------------------------------------------------------
			//���躯�� �ش� ��ǰ ã��
			else if ("pl_list".equals(mode)){
				com.anbtech.dcm.business.CbomBaseInfoBO cinfoBO = new com.anbtech.dcm.business.CbomBaseInfoBO(con);
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//�ش� ��ǰ ������ �б�
				ArrayList part_list = new ArrayList();
				part_list = cinfoBO.viewStrList(fg_code);			
				request.setAttribute("PART_List", part_list); 

				//���ڵ�� FG�ڵ� ����
				String where = "where fg_code = '"+fg_code+"'";
				String model_code = cmodDAO.getColumData("MBOM_MASTER","model_code",where);
				request.setAttribute("model_code", model_code); 
				request.setAttribute("fg_code", fg_code); 

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/eventPartList.jsp").forward(request,response);
			}

		}catch (Exception e){
			//������� �������� �б�
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
		String mode = multi.getParameter("mode")==null?"ecr_prewrite":multi.getParameter("mode");
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
		String attache_cnt = multi.getParameter("attache_cnt")==null?"":multi.getParameter("attache_cnt");	//÷�ΰ��� �ִ���� �̸�
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//------------------------------------------------------------
			//		ECR �⺻���� ó���ϱ�
			//------------------------------------------------------------
			//ECC_COM/ECC_REQ ���/���� �غ�
			if ("ecr_prewrite".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//ECC_COM �Է�/������ ������ �б�
				com.anbtech.dcm.entity.eccComTable eccT = new com.anbtech.dcm.entity.eccComTable();
				eccT = cmodDAO.readEccCom(pid);
				request.setAttribute("COM_List", eccT);

				//ECC_REQ �Է�/������ ������ �б�
				com.anbtech.dcm.entity.eccReqTable ecrT = new com.anbtech.dcm.entity.eccReqTable();
				ecrT = cmodDAO.readEccReq(pid);
				request.setAttribute("REQ_List", ecrT);

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
				getServletContext().getRequestDispatcher("/dcm/base/ecrInfoReg.jsp").forward(request,response);
			}
			//ECC_COM/ECC_REQ ���
			else if("ecr_write".equals(mode)) {
				com.anbtech.dcm.business.CbomBaseInfoBO cinfoBO = new com.anbtech.dcm.business.CbomBaseInfoBO(con);
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//����ϱ� : ECC_COM, ECC_REQ
					cinfoBO.insertECR(pid,ecc_subject,ecr_id,ecr_name,ecr_code,ecr_div_code,ecr_div_name,ecr_tel,ecr_date,mgr_id,ecc_kind,pdg_code,pd_code,fg_code,part_code,chg_position,trouble,condition,solution);
					
					//÷������ �����ϱ� : ECC_REQ
					String save_id = "r"+pid;		//÷������ ��������� ECR���� �˷��ش�
					cinfoBO.setAddFile(multi,"ECC_REQ",pid,save_id,filepath);	

					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//---- ������ ����� ������ ȭ�鿡 �״�� ����Ѵ�.----//
					out.println("	<script>");
					out.println("	self.location.href('CbomProcessServlet?mode=ecc_iwlist&pid="+pid+"');");
					out.println("	</script>");
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					out.println("ECC_COM,ECC_REQ ���忡��:"+e.toString());//�����޽������
				}
			}
			//------------------------------------------------------------
			//		ECO �⺻���� ó���ϱ�
			//------------------------------------------------------------
			//ECC_COM/ECC_ORD ���/���� �غ�
			else if ("eco_prewrite".equals(mode)){
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//ECC_COM �Է�/������ ������ �б�
				com.anbtech.dcm.entity.eccComTable eccT = new com.anbtech.dcm.entity.eccComTable();
				eccT = cmodDAO.readEccCom(pid);
				request.setAttribute("COM_List", eccT);

				//ECC_ORD �Է�/������ ������ �б�
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
				getServletContext().getRequestDispatcher("/dcm/base/ecoInfoReg.jsp").forward(request,response);
			}
			//ECC_COM/ECC_ORD ���
			else if("eco_write".equals(mode)) {
				com.anbtech.dcm.business.CbomBaseInfoBO cinfoBO = new com.anbtech.dcm.business.CbomBaseInfoBO(con);
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//����ϱ� : ECC_COM, ECC_ORD
					cinfoBO.insertECO(pid,ecc_subject,ecr_id,ecr_name,ecr_code,ecr_div_code,ecr_div_name,ecr_tel,ecr_date,eco_id,eco_tel,ecc_reason,ecc_factor,ecc_scope,ecc_kind,pdg_code,pd_code,fg_code,part_code,order_date,chg_position,trouble,condition,solution);
					
					//÷������ �����ϱ� : ECC_ORD
					String save_id = "o"+pid;		//÷������ ��������� ECR���� �˷��ش�
					cinfoBO.setAddFile(multi,"ECC_ORD",pid,save_id,filepath);	

					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//---- ������ ����� ������ ȭ�鿡 �״�� ����Ѵ�.----//
					out.println("	<script>");
					out.println("	self.location.href('CbomProcessServlet?mode=ecc_iwlist&pid="+pid+"');");
					out.println("	</script>");
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}

		}catch (Exception e){
			//������� �������� �б�
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	} //doPost()
}

