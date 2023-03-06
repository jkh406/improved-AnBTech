import com.anbtech.dms.entity.*;
import com.anbtech.dms.db.*;
import com.anbtech.dms.business.*;
import com.anbtech.dms.admin.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class AnBDMS extends HttpServlet {

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

		String tablename	= request.getParameter("tablename");	//tablename ���� �������� ����(�������,���ȼ�,�Ϲݹ���)	
		String no			= request.getParameter("no");			//master_data ���̺��� ���ڵ� ������ȣ
		String t_id			= request.getParameter("t_id");			//���������� ���̺��� ���ڵ� ������ȣ
		String data_id		= request.getParameter("d_id");			//������ ��ȣ
		String ver_code		= request.getParameter("ver");			//�����ڵ�
		String aid			= request.getParameter("aid");			//���ڰ��� ������ȣ
		
		String mode			= request.getParameter("mode");			//���
		String page			= request.getParameter("page");			//������

		//�˻��ÿ� �Ѿ���� �Ķ���͵�
		String searchword	= request.getParameter("searchword");	//�˻���
		String searchscope	= request.getParameter("searchscope");	//�˻��ʵ�
		String category		= request.getParameter("category");		//ī�װ� �ڵ�
		String org_category = request.getParameter("org_category");	//��Ϻ��� ��ư ��ũ ���ڿ� ������ ���� ��.

		if (mode == null) mode = "list";							//ó������ mode�� �� �Ѿ���Ƿ� mode�� list�� ����
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);

		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

		//���� ��û ���� �Ǵ� �ݷ� ����
		String why_revision	= request.getParameter("why");			//������ �ϴ� ����
		String loan_day		= request.getParameter("loan_day");		//����Ⱓ
		String why			= request.getParameter("why");			//����ó���ÿ� �����ڰ� ����� �޽���
		String copy_num		= request.getParameter("copy_num");		//���� ī�Ǽ�
		String return_date	= request.getParameter("return_date");	//���� �Ⱓ
		if (why == null) why = "";
		else why = com.anbtech.text.StringProcess.kwordProcess(why);
		if (copy_num == null) copy_num = "1";
		if (return_date == null) return_date = "ó����";

		String redirectUrl = "";

		//���� �������� ����� ���̵� ��������
		//������ ����Ǿ��� ��� �α� �������� ���� �̵���Ų��.
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


		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			///////////////////////////////////////////////////////////
			// ��� ���� ���
			// 1.�Ѿ�� ī���ڸ� �ڵ带 ������ ���� ������ �ľ��Ѵ�.
			// 2.�ش繮���� ���̺��� ���ǿ� �´� ����Ʈ�� �����´�.
			///////////////////////////////////////////////////////////
			if("list".equals(mode) || "processing".equals(mode) || "mylist".equals(mode)){

				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
				//ī�װ� ID�� �ش��ϴ� ���̺���� �����´�.
				if(tablename == null) tablename = masterDAO.getTableName(category);

				ArrayList table_list = new ArrayList();

				//���̺�� ���� ���ǿ� �´� �����͸� �����´�.
				//master_data(��ü ����) ���� ��������
				if(tablename.equals("master_data")){
					table_list = masterDAO.getMasterData_List(login_id,tablename,mode,searchword,searchscope,category,page);
					request.setAttribute("MasterData_List", table_list);
				}
				//techdoc_data(�������) ���� ��������
				else if(tablename.equals("techdoc_data")){
					com.anbtech.dms.db.TechDocDAO techdocDAO = new com.anbtech.dms.db.TechDocDAO(con);
					table_list = techdocDAO.getTechDocData_List(login_id,tablename,mode,searchword,searchscope,category,page);
					request.setAttribute("TechdocData_List", table_list);
				}
				//proposal_data(���ȼ�) ���� ��������
				else if(tablename.equals("proposal_data")){
					com.anbtech.dms.db.ProposalDAO proposalDAO = new com.anbtech.dms.db.ProposalDAO(con);
					table_list = proposalDAO.getProposalData_List(login_id,tablename,mode,searchword,searchscope,category,page);
					request.setAttribute("ProposalData_List", table_list);
				}

				//��� ���� ��忡 �ʿ��� ��ũ ����
				com.anbtech.dms.business.LinkUrlBO redirectBO = new com.anbtech.dms.business.LinkUrlBO(con);
				com.anbtech.dms.entity.LinkUrl redirect = new com.anbtech.dms.entity.LinkUrl();
				redirect = redirectBO.getRedirect(tablename,mode,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);
				
				//��忡 ���� ������ jsp �������� �б��Ѵ�.
				if(mode.equals("mylist")){
					getServletContext().getRequestDispatcher("/dms/"+tablename+"/mylist.jsp").forward(request,response);
				}else{
					getServletContext().getRequestDispatcher("/dms/"+tablename+"/list.jsp?mode="+mode).forward(request,response);				
				}
			}


			///////////////////////////////////////////////////////////
			// �����Ƿ� ���� ����Ʈ
			///////////////////////////////////////////////////////////
			else if("loan".equals(mode)){
				tablename = "loan_list"; //�����Ƿڹ��� ����Ʈ ���̺� �̸� ����
				
				ArrayList table_list = new ArrayList();

				//loan_list ���� ��������
				com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
				table_list = loanDAO.getLoan_List(tablename,mode,searchword,searchscope,page);
				request.setAttribute("Loan_List", table_list);

				com.anbtech.dms.business.LinkUrlBO redirectBO = new com.anbtech.dms.business.LinkUrlBO(con);
				com.anbtech.dms.entity.LinkUrl redirect = new com.anbtech.dms.entity.LinkUrl();
				redirect = redirectBO.getRedirect(tablename,mode,searchword,searchscope,page);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/dms/"+tablename+"/list.jsp").forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// �����Ƿڵ� ���� ���� �� ����
			///////////////////////////////////////////////////////////
			else if ("view_l".equals(mode)){
		
				com.anbtech.dms.entity.LoanTable loan = new com.anbtech.dms.entity.LoanTable();
				com.anbtech.dms.business.LoanBO loanBO = new com.anbtech.dms.business.LoanBO(con);

				loan = loanBO.getData("loan_list",no);
				request.setAttribute("LoanInfo", loan);
	

				//���� ����ÿ� �ʿ��� ��ũ �� ��ư URL �����
				com.anbtech.dms.business.LinkUrlBO redirectBO = new com.anbtech.dms.business.LinkUrlBO(con);
				com.anbtech.dms.entity.LinkUrl redirect = new com.anbtech.dms.entity.LinkUrl();
			
				redirect = redirectBO.getLinkForView(tablename,mode,searchword,searchscope,page,no,data_id,ver_code);
				request.setAttribute("RedirectInView",redirect);

				//������ ������ �����ϼ� �� ��������
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
				category = masterDAO.getCategoryId(data_id);
				com.anbtech.dms.db.DmsEnvDAO dmsenvDAO = new com.anbtech.dms.db.DmsEnvDAO(con);
				com.anbtech.dms.entity.DmsEnvTable dmsenv = new com.anbtech.dms.entity.DmsEnvTable();
				dmsenv = dmsenvDAO.getDmsEnv(category);
				request.setAttribute("DmsEnv",dmsenv);

				getServletContext().getRequestDispatcher("/dms/loan_list/view.jsp").forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// ���� ���� �󼼺���
			//
			// mode �ǹ�
			// mode == view		: ī�װ��� ����(techdoc_data,proposal_data...) 
			// mode == view_a	: ���� �Ϸ�Ǿ� ������ Ȯ�� ���� ����
			// mode == view_t	: ��� ����	
			// mode == view_m	: ���� �ۼ��Ͽ� ����ϱ� ���� ����
			// mode == report	: ���ڰ��� �� ������ ����
			// mode == print	: �μ�ȭ��
			///////////////////////////////////////////////////////////
			else if ("view".equals(mode) || "view_a".equals(mode) || "view_t".equals(mode) || "view_m".equals(mode) || "report".equals(mode) || "print".equals(mode)){
/*
				boolean is_commit = true;

				//���� �˻� ��� ���� �󼼺���ÿ��� ������ ������ �ִ��� üũ�Ѵ�.
				if(mode.equals("view") || mode.equals("view_t")){
					com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
					is_commit = ac.getMyPrivilege(tablename,mode,login_id,data_id,ver_code);
				}

				//������������ ���� ��� �󼼺��� �� �� ���ٴ� �޽��� �Ѹ���.
				if(!is_commit){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('�˼��մϴ�. �� ������ �� ������ �� �����ϴ�.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;								
				}
*/
				com.anbtech.dms.entity.MasterTable master = new com.anbtech.dms.entity.MasterTable();
				com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);

				//master_data ���̺��� ���ڵ� ���� ��������
				//data_id�� ������ no�� ������ ��, no�� �ش��ϴ� ���ڵ� ������ �����´�.
				if(no == null) no = masterDAO.getMid(data_id);
				master = masterBO.getData("master_data",no);
				request.setAttribute("MasterInfo", master);

				//////////////////////////////////////
				// ���� ������ ���� �����͸� �����´�.
				/////////////////////////////////////
				if(tablename.equals("techdoc_data")){			// �������
					com.anbtech.dms.entity.TechDocTable techdoc = new com.anbtech.dms.entity.TechDocTable();
					com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);

					techdoc = techdocBO.getData(mode,tablename,data_id,ver_code);
					request.setAttribute(tablename, techdoc);
				}else if(tablename.equals("proposal_data")){	// ���ȼ�
					com.anbtech.dms.entity.ProposalTable proposal = new com.anbtech.dms.entity.ProposalTable();
					com.anbtech.dms.business.ProposalBO proposalBO = new com.anbtech.dms.business.ProposalBO(con);

					proposal = proposalBO.getData(mode,tablename,data_id,ver_code);
					request.setAttribute(tablename, proposal);
				}

				//���� ī�װ��� ȯ�� ��������(��ǥ����,���ȵ��,�����Ⱓ ��)
				if(category.equals("")) category = masterDAO.getCategoryId(data_id);
				com.anbtech.dms.db.DmsEnvDAO dmsenvDAO = new com.anbtech.dms.db.DmsEnvDAO(con);
				com.anbtech.dms.entity.DmsEnvTable dmsenv = new com.anbtech.dms.entity.DmsEnvTable();
				dmsenv = dmsenvDAO.getDmsEnv(category);
				request.setAttribute("DmsEnv",dmsenv);

				//���� �� ����ÿ� �ʿ��� ��ũ �� ��ư URL �����
				com.anbtech.dms.business.LinkUrlBO redirectBO = new com.anbtech.dms.business.LinkUrlBO(con);
				com.anbtech.dms.entity.LinkUrl redirect = new com.anbtech.dms.entity.LinkUrl();

				redirect = redirectBO.getLinkForView(tablename,mode,category,searchword,searchscope,page,no,data_id,ver_code,why_revision,org_category);
				request.setAttribute("RedirectInView",redirect);

				//�μ�ȭ�� ����� ���� ���� ������ �����´�.
				if(mode.equals("print")){
					com.anbtech.admin.entity.ApprovalInfoTable app_table = new com.anbtech.admin.entity.ApprovalInfoTable();
					com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
					//���� ������ȣ
					aid = masterDAO.getAid(tablename,data_id,ver_code);

					String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
					app_table = appDAO.getApprovalInfo("dms_approval_info",aid,sign_path);
					request.setAttribute("Approval_Info",app_table);
				}

				//���� ����ȸ���� +1 ������Ű��
				if(mode.equals("view") || mode.equals("view_t"))	masterDAO.updateHit("master_data",no);

				if(mode.equals("report")) getServletContext().getRequestDispatcher("/dms/"+tablename+"/report.jsp").forward(request,response);
				else if(mode.equals("print")) getServletContext().getRequestDispatcher("/dms/"+tablename+"/print.jsp").forward(request,response);
				else getServletContext().getRequestDispatcher("/dms/"+tablename+"/view.jsp").forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// ���� �ű� ���, ������, ����
			///////////////////////////////////////////////////////////
			else if ("write".equals(mode) || "revision".equals(mode) || "modify".equals(mode) || "modify_a".equals(mode)){

				boolean is_commit = true;

				//������ �Ǵ� ���� �ÿ��� ������ �� �ִ� ������ �ִ��� üũ�� �Ѵ�.
				if(mode.equals("revision")){
					com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
					is_commit = ac.getMyPrivilege(tablename,mode,login_id,data_id,ver_code);
				}
				//������ ���� ��� �޽��� �Ѹ���.
				String err_message = "";
				if(mode.equals("revision")) err_message = "�˼��մϴ�. �� ������ �������� �� �ִ� ������ �����ϴ�.";
				if(mode.equals("modify")) err_message = "�˼��մϴ�. �� ������ ������ �� �ִ� ������ �����ϴ�.";
				if(!is_commit){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('" + err_message + "');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;								
				}

				com.anbtech.dms.entity.MasterTable master = new com.anbtech.dms.entity.MasterTable();
				com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);

				//master_data ���� ��������
				master = masterBO.getWrite_form("master_data", no, mode, category, why_revision);
				request.setAttribute("MasterData", master);

				ArrayList file_list = new ArrayList();
				ArrayList ref_list = new ArrayList();

				//////////////////////////////////////
				// ���� ������ ���� �����͸� �����´�.
				/////////////////////////////////////
				if(tablename.equals("techdoc_data")){
					com.anbtech.dms.entity.TechDocTable techdoc = new com.anbtech.dms.entity.TechDocTable();
					com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);

					techdoc = techdocBO.getWrite_form(tablename,mode,data_id,ver_code,why_revision);
					request.setAttribute(tablename, techdoc);

					//modify�϶��� ÷������ ����Ʈ�� �����ڷ� ����Ʈ�� �����´�.
					if ("modify".equals(mode) || "modify_a".equals(mode)){
						com.anbtech.dms.db.TechDocDAO techdocDAO = new com.anbtech.dms.db.TechDocDAO(con);
						file_list = techdocDAO.getFile_list(tablename, data_id, ver_code);
						ref_list = techdocDAO.getReference_list(tablename, data_id, ver_code);
					}
				}else if(tablename.equals("proposal_data")){
					com.anbtech.dms.entity.ProposalTable proposal = new com.anbtech.dms.entity.ProposalTable();
					com.anbtech.dms.business.ProposalBO proposalBO = new com.anbtech.dms.business.ProposalBO(con);

					proposal = proposalBO.getWrite_form(tablename,mode,data_id,ver_code,why_revision);
					request.setAttribute(tablename, proposal);

					//modify�϶��� ÷������ ����Ʈ�� �����ڷ� ����Ʈ�� �����´�.
					if ("modify".equals(mode) || "modify_a".equals(mode)){
						com.anbtech.dms.db.ProposalDAO proposalDAO = new com.anbtech.dms.db.ProposalDAO(con);
						file_list = proposalDAO.getFile_list(tablename, data_id, ver_code);
						ref_list = proposalDAO.getReference_list(tablename, data_id, ver_code);
					}
				}

				request.setAttribute("File_List", file_list);
				request.setAttribute("Reference_List", ref_list);

				//�ʿ��� ��ũ �� ��ư URL �����
				com.anbtech.dms.business.LinkUrlBO redirectBO = new com.anbtech.dms.business.LinkUrlBO(con);
				com.anbtech.dms.entity.LinkUrl redirect = new com.anbtech.dms.entity.LinkUrl();
				redirect = redirectBO.getLinkForWrite(tablename,mode,category,searchword,searchscope,page,no,data_id,ver_code);
				request.setAttribute("RedirectInWrite",redirect);

				//���� ī�װ��� ȯ�� ��������(��ǥ����,���ȵ��,�����Ⱓ ��)
				com.anbtech.dms.db.DmsEnvDAO dmsenvDAO = new com.anbtech.dms.db.DmsEnvDAO(con);
				com.anbtech.dms.entity.DmsEnvTable dmsenv = new com.anbtech.dms.entity.DmsEnvTable();
				dmsenv = dmsenvDAO.getDmsEnv(category);
				request.setAttribute("DmsEnv",dmsenv);

				getServletContext().getRequestDispatcher("/dms/"+tablename+"/write.jsp?category="+category).forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// ÷������ �ٿ�ε� ó��
			///////////////////////////////////////////////////////////
			else if ("download".equals(mode)){

				//÷������ ������ ���� ü��
				com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
				boolean is_commit = ac.getMyPrivilege(tablename,mode,login_id,data_id,ver_code);

				//������ ���� ��� �޽��� �Ѹ���.
				if(!is_commit){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('�˼��մϴ�. ÷�������� ������ ������ �����ϴ�.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;								
				}
				
				//���� �̵��ϴ� �� ���� �ٿ�ε��Ų��.
				com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);
				com.anbtech.dms.entity.TechDocTable file = new com.anbtech.dms.entity.TechDocTable();
				file = techdocBO.getFile_fordown(tablename, t_id);
				String filename = file.getFileName();
				String filetype = file.getFileType();
				String filesize = file.getFileSize();

				//boardpath ���� ���ϱ��� ��� ����
				String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/dms/" + tablename + "/" + t_id + ".bin";
			
				if (filetype.indexOf("mage")<=0)
					filetype = "application/unknown";
				
				String strClient=request.getHeader("User-Agent");

				///////////////////////////////////////////////////////////
				//���ϸ��� �Ʒ��� ���� ����ó�� ������ �ѱ��� ������ �ʾ���.
				///////////////////////////////////////////////////////////
				filename = new String(filename.getBytes("euc-kr"),"8859_1"); 

				if(strClient.indexOf("MSIE 5.5")>-1) response.setHeader("Content-Disposition","filename="+filename);
				else response.setHeader("Content-Disposition","attachment; filename="+filename);
				
				//////////////////////////////////////////////////////////////////
				//db�� mysql�� ���� �Ʒ� �������� ��ġ���� �� �ѱ��� ������ �ʾ���.
				//response.setHeader("Content-Type", "application/octet-stream;"); 
				//response.setHeader("Content-Disposition", "attachment; filename=" + filename + ";"); 
				//////////////////////////////////////////////////////////////////

				response.setContentType(filetype);
				response.setContentLength(Integer.parseInt(filesize));
					
				byte b[] = new byte[Integer.parseInt(filesize)];
				java.io.File f = new java.io.File(downFile);
				java.io.FileInputStream fin = new java.io.FileInputStream(f);
				ServletOutputStream fout = response.getOutputStream();
				fin.read(b);
				fout.write(b,0,Integer.parseInt(filesize));
				fout.close();

			}

			///////////////////////////////////////////////////////////
			// ���� ���� ��û�� ����ó���Ѵ�.
			///////////////////////////////////////////////////////////
			else if("req_loan".equals(mode)){
				//1.data_id�� ������ ������ȣ�� �����´�.
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
				String doc_no = masterDAO.getDocNo(data_id);

				//2.�����ȣ�� ����Ѵ�.
				com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
				String loan_no = loanDAO.getLoanNo();

				//3.�����Ƿ������� loan_list ���̺� �����Ѵ�.
				loanDAO.saveData(loan_no,doc_no,data_id,ver_code,login_id,why,copy_num,return_date);

				PrintWriter out = response.getWriter();
				out.println("	<script>");
				out.println("	alert('���������� �����û�Ǿ����ϴ�. ó������� �׷����-���ڿ������� �뺸�˴ϴ�.');");
				out.println("	history.go(-1);");
				out.println("	</script>");
				out.close();
			}

			///////////////////////////////////////////////////////////
			// �����Ƿ� ������ ����ó����Ų��.
			//
			// stat == 1 : ó����
			// stat == 2 : �ݷ���
			// stat == 3 : ����Ϸ�
			// stat == 4 : �ݳ��Ϸ�
			///////////////////////////////////////////////////////////
			else if("loan_commit".equals(mode)){
				// 1.���⸮��Ʈ���� �ش� ����� �����ڵ带 3(����Ϸ�)�� ����
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
				com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
				loanDAO.updateStat(no,"3");

				//2.����ó�� ����� ������Ʈ�Ѵ�.
				loanDAO.updateWhy(no,why,loan_day);

				//3.�ش� ����� ÷�������� ���� ��� ÷�������� �ٿ�ε��� �� �ִ� ���ڿ���
				//  �ȳ��޽��� ���ڿ��� ����� ���ڿ����� �߼��Ѵ�.
				com.anbtech.dms.business.LoanBO loanBO = new com.anbtech.dms.business.LoanBO(con);
				loanBO.sendMail(tablename,data_id,ver_code,no,login_id,"y");

				//�ش� ������ ���°� ����Ÿ���� �ƴѰ�쿡�� ������ �����ڵ带 ���������� ����
				//����ڷ� �Ͽ��� ���� ������ �˷� ���� �����û�� ���� ���ؼ�.
				if(!masterDAO.isFileType(tablename,data_id,ver_code)){
					//4.�ش� ���̺��� �����ڵ带 ������(6)�� �����Ѵ�.
					masterDAO.updateStat(tablename,data_id,ver_code,"6");

					//5.���� ������ ���������� ������� master_data ���̺��� �����ڵ嵵 ����������
					//  ������ �ش�.
					com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
					if(ac.isLastVersion(tablename,data_id,ver_code))
						masterDAO.updateStat("","6",data_id);
				}

				redirectUrl = "AnBDMS?mode=loan";
			}

			///////////////////////////////////////////////////////////
			// �����Ƿ� ������ �ݷ���Ų��.
			///////////////////////////////////////////////////////////
			else if("loan_reject".equals(mode)){
				// 1.���⸮��Ʈ���� �ش� ����� �����ڵ带 2(����ݷ�)�� ����
				com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
				loanDAO.updateStat(no,"2");

				//2.�ݷ������� DB�� �����Ѵ�.
				loanDAO.updateWhy(no,why,loan_day);

				//3.�ݷ������� ���Ϸ� �뺸
				com.anbtech.dms.business.LoanBO loanBO = new com.anbtech.dms.business.LoanBO(con);
				loanBO.sendMail(tablename,data_id,ver_code,no,login_id,"n");

				redirectUrl = "AnBDMS?mode=loan";
			}

			///////////////////////////////////////////////////////////
			// ����� ������ �ݳ���Ų��.
			///////////////////////////////////////////////////////////
			else if("loan_return".equals(mode)){
				// 1.���⸮��Ʈ���� �ش� ����� �����ڵ带 2(����ݷ�)�� ����
				com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
				loanDAO.updateStat(no,"4");

				//2.ó����� DB�� �����Ѵ�.
				loanDAO.updateWhy(no,why,loan_day);

				//3.�ش� ������ ���°� ����Ÿ���� �ƴѰ�쿡�� ������ �����ڵ带 �����߿���
				//�������� ����
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
				if(!masterDAO.isFileType(tablename,data_id,ver_code)){
					masterDAO.updateStat(tablename,data_id,ver_code,"5");

					//4.���� ������ ���������� ������� master_data ���̺��� �����ڵ嵵 ��������
					//������ �ش�.
					com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
					if(ac.isLastVersion(tablename,data_id,ver_code))
						masterDAO.updateStat("","5",data_id);
				}

				redirectUrl = "AnBDMS?mode=loan";
			}

			///////////////////////////////////////////////////////////
			// ������� ������ ����ó���Ѵ�.
			///////////////////////////////////////////////////////////
			else if("delete".equals(mode)){
				//���� ���� üũ
				com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
				boolean is_commit = ac.getMyPrivilege(tablename,mode,login_id,data_id,ver_code);

				if(!is_commit){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('�˼��մϴ�. �� ������ ������ �� �ִ� ������ �����ϴ�.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;								
				}
				
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
				String prev_ver_code = masterDAO.getPrevVerCode(tablename,data_id,ver_code);

				con.setAutoCommit(false);	// Ʈ������� ����
				try{
					//1.÷������ �����ϱ�
					com.anbtech.dms.db.TechDocDAO techdocDAO = new com.anbtech.dms.db.TechDocDAO(con);
					t_id = techdocDAO.getId(tablename, data_id, ver_code);
					String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/dms/"+tablename+"/";
					for(int i=1; i<10; i++){
						java.io.File f = new java.io.File(filepath + "/" + t_id + "_" + i + ".bin");
						if(f.exists()) f.delete();
					}

					//2.db ������Ʈ�ϱ�
					//
					// ���� ������ ���
					if(prev_ver_code == null){	
						masterDAO.deleteByDataId(data_id);						// ������ ���̺��� �ش� �׸� ����;
						masterDAO.deletByVerCode(tablename,data_id,ver_code);	// �� ���̺��� �ش� �׸� ����;
					// �������� �̷���� ���
					}else{ 
						masterDAO.deletByVerCode(tablename,data_id,ver_code);	// �� ���̺��� �ش� �׸� ����;
						//
						if(tablename.equals("techdoc_data")){
							com.anbtech.dms.entity.TechDocTable techdoc = new com.anbtech.dms.entity.TechDocTable();
							techdoc = (com.anbtech.dms.entity.TechDocTable)techdocDAO.getTechDocData(tablename,data_id,prev_ver_code);

							String writer = techdoc.getWriter();
							String writer_s = techdoc.getWriterS();
							String register = techdoc.getRegister();
							String register_s = techdoc.getRegisterS();
							String register_day = techdoc.getRegisterDay();

							//������ ���̺��� ���ڵ� ������ �������� �ǵ�����.
							masterDAO.updateDataToPrev(data_id,writer,writer_s,register,register_s,register_day,prev_ver_code);
						}
					}
					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "AnBDMS?mode=processing&category=1";
			}

			///////////////////////////////////////////////////////////
			// �����Ƿ� ���� �����ϱ�
			///////////////////////////////////////////////////////////
			else if("loan_del".equals(mode)){
				com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
				loanDAO.deleteLoanData(no);

				redirectUrl = "AnBDMS?mode=loan";
			}

			///////////////////////////////////////////////////////////
			// �������� ó���� �Ѵ�.
			// ���� ���� �ÿ� �Ѿ���� �Ķ���ʹ� ������ ����.
			// mode == commit, tablename(���̺��), data_id(������ ��ȣ), ver_code (��������),
			// aid(���ڰ��� ������ȣ)
			//
			// ó�� ���� (�������(techdoc_data)�� �������� ���):
			// (1) dbo.groupware.app_save ���̺��� aid �� �ش��ϴ� ���ڵ� ������ �����ͼ�
			//     approval_info ���̺� �Է��Ѵ�.
			// (2) �Ѿ�� ������ �ű� ������� �������� ���� ��������� �ľ��Ѵ�.
			//     �Ѿ�� �����ڵ尡 1.0�̸� �ű�, �ƴϸ� ���������� �����Ѵ�.
			//
			// [�ű��϶�]
			// (3) techdoc_data ���̺��� stat �ʵ尪�� 3(������ Ȯ�� ���)���� ����
			// (4) master_data ���̺��� stat �ʵ尪�� 3(������ Ȯ�� ���)���� ����
			// (5) techdoc_data ���̺��� aid �ʵ尪�� aid �� ����
			//
			// [�������϶�]
			// (3) techdoc_data ���̺��� stat �ʵ尪�� 3(������ Ȯ�� ���)���� ����
			// (4) master_data ���̺��� �ش� ���ڵ��� last_version �� ���� �������� ����
			// (5) techdoc_data ���̺��� aid �ʵ尪�� aid �� ����
			// 
			///////////////////////////////////////////////////////////
			else if("commit".equals(mode)){
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

				try{
					com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);

					masterDAO.getAppInfoAndSave(aid);

					masterDAO.updateStat(tablename,data_id,ver_code,"3");
					
					if(ver_code.equals("1.0")) masterDAO.updateStat("","3",data_id);
					
					masterDAO.updateAid(tablename,data_id,ver_code,aid);
					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "ApprovalProcessServlet?mode=APP";
			}

			///////////////////////////////////////////////////////////
			// ���ó��
			// ������ �����ڵ带 ����������(2)�� �����Ѵ�.
			///////////////////////////////////////////////////////////
			else if("review".equals(mode)){
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

				try{
					com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
					masterDAO.updateStat(tablename,data_id,ver_code,"2");
					if(ver_code.equals("1.0")) masterDAO.updateStat("","2",data_id);
					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "TechnicalServlet";
			}

			///////////////////////////////////////////////////////////
			// �ݷ�ó��
			// ������ �����ڵ带 �ݷ�(4)�� �����Ѵ�.
			///////////////////////////////////////////////////////////
			else if("reject".equals(mode)){
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

				try{
					com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
					masterDAO.updateStat(tablename,data_id,ver_code,"4");
					if(ver_code.equals("1.0")) masterDAO.updateStat("","4",data_id);
					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
				
				redirectUrl = "ApprovalProcessServlet?mode=REJ";
			}

			///////////////////////////////////////////////////////////
			// ���� ������ ���� Ȯ�� ó��
			// ������ �����ڵ带 ����(5)�� �����Ѵ�.
			///////////////////////////////////////////////////////////
			else if("commit2".equals(mode)){
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

				try{
					com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
					masterDAO.updateStat(tablename,data_id,ver_code,"5");
					
					if(ver_code.equals("1.0")) masterDAO.updateStat("","5",data_id);
					else masterDAO.updateVerByDataId(ver_code,data_id);

					//������Ʈ���� ��⿡ ������Ͽ��θ� �˷��ֱ� ���� ������ ������.
					com.anbtech.pjt.db.pjtDocumentDAO pjtDAO = new com.anbtech.pjt.db.pjtDocumentDAO(con);
					pjtDAO.updateDocument(no);
					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "AnBDMS?mode=processing&tablename="+tablename+"&category=1";
			}

			///////////////////////////////////////////////////////////
			// ���ڰ��� ��� ó��
			///////////////////////////////////////////////////////////
			else if("approval".equals(mode)){
				getServletContext().getRequestDispatcher("/dms/confirm.jsp?tablename="+tablename+"&t_id="+t_id+"&data_id="+data_id+"&ver="+ver_code+"&category="+category).forward(request,response);
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

		//���ε��� �� �ʿ��� �͵� tablename,upload_size, filepath ����
		String tablename = request.getParameter("tablename");
		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/dms/"+tablename+"/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//��ξ����� �������
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//������������ �� �޾ƿ´�. multi���� ������
		String mode				= multi.getParameter("mode");			//������
		String page				= multi.getParameter("page");			//������������ȣ
		String searchword		= multi.getParameter("searchword");		//�˻���
		String searchscope		= multi.getParameter("searchscope");	//�˻��׸�
		String category_id		= multi.getParameter("category");		//ī�װ� �ڵ�
		String no				= multi.getParameter("no");				//������ ������ȣ
		String data_id			= multi.getParameter("d_id");			//������ ��ȣ
		String doc_no			= multi.getParameter("doc_no");			//������ȣ

		String where_from		= multi.getParameter("where_from");		//���� ��ó
		String model_code		= multi.getParameter("model_code");		//�� �ڵ�
		String pjt_code			= multi.getParameter("pjt_code");		//������Ʈ �ڵ�
		String node_code		= multi.getParameter("node_code");		//����ڵ�
		String subject			= multi.getParameter("subject");		//���� ����
		String ver_code			= multi.getParameter("ver_code");		//���� ����
		String register			= multi.getParameter("register");		//����� ���
		String writer			= multi.getParameter("writer");			//�����ۼ��� ��� or �Լ��� ���
		String preview			= multi.getParameter("preview");		//�������� ���
		String why_revision		= multi.getParameter("why_revision");	//���泻�� ���
		String modify_info		= multi.getParameter("modify_info");	//���� ��������
		String modify_history	= multi.getParameter("modify_history");	//�������� ���
		String security_level	= multi.getParameter("security_level");	//���� ���ȵ��
		String save_period		= multi.getParameter("save_period");	//���� �����Ⱓ
		String written_lang		=  multi.getParameter("written_lang");	//�ۼ����
		String doc_type			= multi.getParameter("doc_type");		//��������
		String save_url			= multi.getParameter("save_url");		//���� ������ġ
		String copy_num			= multi.getParameter("copy_num");		//ī�Ǽ�
		String reference		= multi.getParameter("reference");		//�����ڷ�
		String eco_no			= multi.getParameter("eco_no");			//ECO ��ȣ

		//���ȼ� �����Ͽ� �߰��Ǵ� �׸�
		String goods_name		= multi.getParameter("goods_name");		//������ǰ��
		String why_propose		= multi.getParameter("why_propose");	//���Ȼ���
		String country_name		= multi.getParameter("country_name");	//���ȱ�����
		String company_name		= multi.getParameter("company_name");	//����ȸ���
		String customer_name	= multi.getParameter("customer_name");	//����ڸ�
		String customer_tel		= multi.getParameter("customer_tel");	//�������ȭ��ȣ
		

		//�������� �Ѿ���ų� null�� �� ���鿡 ���� ó��.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category_id == null)  category_id = "";
		if (no == null) no = "";
		if (data_id == null) data_id = "";
		if (model_code == null) model_code = "";
		if (pjt_code == null) pjt_code = "";
		if (node_code == null) node_code = "";

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
		String login_id = sl.id;
		String login_name = sl.name;

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/////////////////////
			// ���� �ű� ��� ó��
			/////////////////////
			if ("write".equals(mode)){

				com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);

				//������ȣ�� ���Ѵ�.
				doc_no	= masterDAO.getDocNo("master_data",category_id);

				//�����͹�ȣ�� ���Ѵ�.
				data_id	= System.currentTimeMillis() + "";	// ���� �ð����� ������ ��ȣ�� ����

				String t_id = "";

				//��Ͻð�
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String w_time = vans.format(now);

				//�˻� ���ڿ� ����
				//�˻� ���ڿ��� ���Ե� �׸��� �������� �� �ִ�.
				//String search_keyword = subject + "|" + writer + "|" + register + "|" + preview;
				String search_keyword = "";
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

				///////////////////////////////////////////////////////////
				//if(level == 1) 
				//	conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				//else if(level == 2) 
				//	conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				//else if(level == 3) 
				//	conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				///////////////////////////////////////////////////////////

				try{

					//////////////////////////////////////
					// ���� ������ ���� ������ ����
					/////////////////////////////////////
					if(tablename.equals("techdoc_data")){
						com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);
						com.anbtech.dms.db.TechDocDAO techdocDAO = new com.anbtech.dms.db.TechDocDAO(con);
						//techdoc_table �Է�
						techdocDAO.saveData(tablename,ver_code,data_id,preview,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,w_time,reference,eco_no,copy_num,subject,doc_no,category_id,"1","0");

						//data_id �� ver_code �� �ش��ϴ� ������ȣ�� �����´�.
						t_id = techdocDAO.getId(tablename,data_id, ver_code);

						com.anbtech.dms.entity.TechDocTable file = new com.anbtech.dms.entity.TechDocTable();
						//÷������ ���δ�
						file = techdocBO.getFile_frommulti(multi, t_id, filepath);

						//���ε� �� ÷������ ������ DB�� �����ϱ�
						techdocBO.updFile(tablename, t_id, file.getFileName(), file.getFileType(), file.getFileSize());
					}else if(tablename.equals("proposal_data")){
						com.anbtech.dms.business.ProposalBO proposalBO = new com.anbtech.dms.business.ProposalBO(con);
						com.anbtech.dms.db.ProposalDAO proposalDAO = new com.anbtech.dms.db.ProposalDAO(con);
						//proposal_table �Է�
						proposalDAO.saveData(tablename,ver_code,data_id,preview,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,w_time,reference,eco_no,copy_num,subject,doc_no,category_id,"1","0",goods_name,why_propose,country_name,company_name,customer_name,customer_tel);

						//data_id �� ver_code �� �ش��ϴ� ������ȣ�� �����´�.
						t_id = proposalDAO.getId(tablename,data_id, ver_code);

						com.anbtech.dms.entity.ProposalTable file = new com.anbtech.dms.entity.ProposalTable();
						//÷������ ���δ�
						file = proposalBO.getFile_frommulti(multi, t_id, filepath);

						//���ε� �� ÷������ ������ DB�� �����ϱ�
						proposalBO.updFile(tablename, t_id, file.getFileName(), file.getFileType(), file.getFileSize());
					}

					//�� ������ ���̺� ���������� ����Ǿ��� ��쿡 master_data ���̺� �����Ѵ�.
					masterDAO.saveData("master_data", doc_no, category_id, data_id, subject, writer, register, w_time, search_keyword, ver_code, model_code,pjt_code,node_code);

					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				//����� �Ϸ�Ǹ� ���ڰ��縦 ź��.
				getServletContext().getRequestDispatcher("/dms/confirm.jsp?tablename="+tablename+"&category="+category_id+"&t_id="+t_id+"&data_id="+data_id+"&ver="+ver_code).forward(request,response);

			///////////////////////////////////////////////////////////
			// ���� ���� ó��
			///////////////////////////////////////////////////////////
			} else if ("modify".equals(mode) || "modify_a".equals(mode)){

				com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);

				//ī�װ� �з��� ����Ǹ� ������ȣ�� ����ȴ�.
				//����, ī�װ� �ڵ尡 ����Ǿ������� �켱 �˻��ؾ� �Ѵ�.
				//���� ī�װ� �ڵ� ��������
				String old_category_id = masterDAO.getCategoryId("master_data",no);
				//���� ī�װ� �ڵ�� ���� ī�װ� �ڵ尡 ���� ������ ������ȣ ���� ����
				if(!category_id.equals(old_category_id)){
					doc_no	= masterDAO.getDocNo("master_data",category_id);
				}

				//��Ͻð�
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String w_time = vans.format(now);

				//�˻� ���ڿ� ����
				//�˻� ���ڿ��� ���Ե� �׸��� �������� �� �ִ�.
				//String search_keyword = subject + "|" + writer + "|" + register + "|" + preview;
				String search_keyword = "";


				//�����̷� ����
				//modify_history += modify_history + "\n" + modify_info + "(������:" + register + ",������:" + w_time + ")";
				//if(mode.equals("modify_a")) modify_history = modify_info; // ������ ������ ���� �̷��� �� ����.

				con.setAutoCommit(false);	// Ʈ������� ���� 
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//////////////////////////////////////
					// ���� ������ ���� ������ ����
					/////////////////////////////////////
					if(tablename.equals("techdoc_data")){
						com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);
						com.anbtech.dms.db.TechDocDAO techdocDAO = new com.anbtech.dms.db.TechDocDAO(con);						
						//data_id �� ver_code �� �ش��ϴ� ������ȣ�� �����´�.
						String t_id = techdocDAO.getId(tablename,data_id, ver_code);

						//techdoc_table ������Ʈ
						techdocDAO.updateData(tablename,t_id,preview,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,w_time,reference,eco_no,copy_num,modify_history);

						//÷������ ���� ������Ʈ
						ArrayList file_list = techdocDAO.getFile_list(tablename,t_id);
						com.anbtech.dms.entity.TechDocTable file = new com.anbtech.dms.entity.TechDocTable();
						file = techdocBO.getFile_frommulti(multi, t_id, filepath, file_list);
						techdocBO.updFile(tablename, t_id, file.getFileName(), file.getFileType(), file.getFileSize());
					}else if(tablename.equals("proposal_data")){
						com.anbtech.dms.business.ProposalBO proposalBO = new com.anbtech.dms.business.ProposalBO(con);
						com.anbtech.dms.db.ProposalDAO proposalDAO = new com.anbtech.dms.db.ProposalDAO(con);						
						//data_id �� ver_code �� �ش��ϴ� ������ȣ�� �����´�.
						String t_id = proposalDAO.getId(tablename,data_id, ver_code);

						proposalDAO.updateData(tablename,t_id,preview,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,w_time,reference,eco_no,copy_num,modify_history,goods_name,why_propose,country_name,company_name,customer_name,customer_tel);

						//÷������ ���� ������Ʈ
						ArrayList file_list = proposalDAO.getFile_list(tablename,t_id);
						com.anbtech.dms.entity.ProposalTable file = new com.anbtech.dms.entity.ProposalTable();
						file = proposalBO.getFile_frommulti(multi, t_id, filepath, file_list);
						proposalBO.updFile(tablename, t_id, file.getFileName(), file.getFileType(), file.getFileSize());
					}

					masterDAO.updateData("master_data", doc_no, category_id, subject, writer, search_keyword, no,model_code,pjt_code,node_code);
					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
				//redirectUrl = "AnBDMS?mode=processing&category=1";
				if(mode.equals("modify"))
					redirectUrl = "AnBDMS?tablename="+tablename+"&mode=view_m&page="+page+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category_id+"&no="+no+"&d_id="+data_id+"&ver="+ver_code;
				else if(mode.equals("modify_a"))
					redirectUrl = "AnBDMS?tablename="+tablename+"&mode=view_a&page="+page+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category_id+"&no="+no+"&d_id="+data_id+"&ver="+ver_code;
			}

			///////////////////////////////////////////////////////////
			// ���� ������ ó��
			///////////////////////////////////////////////////////////
			else if ("revision".equals(mode)){

				com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);

				//��Ͻð�
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String w_time = vans.format(now);

				//�˻� ���ڿ� ����
				//�˻� ���ڿ��� ���Ե� �׸��� �������� �� �ִ�.
				//String search_keyword = subject + "|" + writer + "|" + register + "|" + preview + "|" + why_revision;
				
				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//////////////////////////////////////
					// ���� ������ ���� ������ ����
					/////////////////////////////////////
					if(tablename.equals("techdoc_data")){
						com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);
						com.anbtech.dms.db.TechDocDAO techdocDAO = new com.anbtech.dms.db.TechDocDAO(con);
						techdocDAO.saveData(tablename,ver_code,data_id,preview,why_revision,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,w_time,reference,eco_no,copy_num,subject,doc_no,category_id,"1","0");

						//data_id �� ver_code �� �ش��ϴ� ������ȣ�� �����´�.
						String t_id = techdocDAO.getId(tablename,data_id, ver_code);

						com.anbtech.dms.entity.TechDocTable file = new com.anbtech.dms.entity.TechDocTable();
						//÷������ ���δ�
						file = techdocBO.getFile_frommulti(multi, t_id, filepath);

						//���ε� �� ÷������ ������ DB�� �����ϱ�
						techdocBO.updFile(tablename, t_id, file.getFileName(), file.getFileType(), file.getFileSize());

					}else if(tablename.equals("proposal_data")){
						com.anbtech.dms.business.ProposalBO proposalBO = new com.anbtech.dms.business.ProposalBO(con);
						com.anbtech.dms.db.ProposalDAO proposalDAO = new com.anbtech.dms.db.ProposalDAO(con);
						proposalDAO.saveData(tablename,ver_code,data_id,preview,why_revision,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,w_time,reference,eco_no,copy_num,subject,doc_no,category_id,"1","0",goods_name,why_propose,country_name,company_name,customer_name,customer_tel);

						//data_id �� ver_code �� �ش��ϴ� ������ȣ�� �����´�.
						String t_id = proposalDAO.getId(tablename,data_id, ver_code);

						com.anbtech.dms.entity.ProposalTable file = new com.anbtech.dms.entity.ProposalTable();
						//÷������ ���δ�
						file = proposalBO.getFile_frommulti(multi, t_id, filepath);

						//���ε� �� ÷������ ������ DB�� �����ϱ�
						proposalBO.updFile(tablename, t_id, file.getFileName(), file.getFileType(), file.getFileSize());
					}

					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				//����� �Ϸ�Ǹ� ���ڰ��縦 ź��.
				getServletContext().getRequestDispatcher("/dms/confirm.jsp?tablename="+tablename+"&category="+category_id+"&data_id="+data_id+"&ver="+ver_code).forward(request,response);
			}

			//redirectUrl�� ���� �����ÿ��� redirectUrl��η� �̵��Ѵ�.
			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			//con�Ҹ�
			close(con);
		}
	} //doPost()
}