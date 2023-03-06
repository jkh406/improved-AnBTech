import com.anbtech.bm.db.*;
import com.anbtech.bm.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class BomBillModifyServlet extends HttpServlet {
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"cpy_fgsearch":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pid":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MBOM STR������ ���� �⺻�Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String gid = Hanguel.toHanguel(request.getParameter("gid"))==null?"":Hanguel.toHanguel(request.getParameter("gid"));
		String parent_code = Hanguel.toHanguel(request.getParameter("parent_code"))==null?"":Hanguel.toHanguel(request.getParameter("parent_code"));
		String child_code = Hanguel.toHanguel(request.getParameter("child_code"))==null?"":Hanguel.toHanguel(request.getParameter("child_code"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));
		String part_name = Hanguel.toHanguel(request.getParameter("part_name"))==null?"":Hanguel.toHanguel(request.getParameter("part_name"));
		String part_spec = Hanguel.toHanguel(request.getParameter("part_spec"))==null?"":Hanguel.toHanguel(request.getParameter("part_spec"));
		String location = Hanguel.toHanguel(request.getParameter("location"))==null?"":Hanguel.toHanguel(request.getParameter("location"));
		String op_code = Hanguel.toHanguel(request.getParameter("op_code"))==null?"":Hanguel.toHanguel(request.getParameter("op_code"));
		String qty_unit = Hanguel.toHanguel(request.getParameter("qty_unit"))==null?"":Hanguel.toHanguel(request.getParameter("qty_unit"));
		String qty = Hanguel.toHanguel(request.getParameter("qty"))==null?"":Hanguel.toHanguel(request.getParameter("qty"));
		String price_unit = Hanguel.toHanguel(request.getParameter("price_unit"))==null?"":Hanguel.toHanguel(request.getParameter("price_unit"));
		String price = Hanguel.toHanguel(request.getParameter("price"))==null?"":Hanguel.toHanguel(request.getParameter("price"));
		String adtag = Hanguel.toHanguel(request.getParameter("adtag"))==null?"":Hanguel.toHanguel(request.getParameter("adtag"));
		String eco_no = Hanguel.toHanguel(request.getParameter("eco_no"))==null?"":Hanguel.toHanguel(request.getParameter("eco_no"));
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		String url = Hanguel.toHanguel(request.getParameter("url"))==null?"":Hanguel.toHanguel(request.getParameter("url"));

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		MBOM ���� ����/���̱� 
			//------------------------------------------------------------
			//������FG�ڵ�, ���̱��� FG�ڵ� �˻�
			if ("cpy_fgsearch".equals(mode) || "pst_fgsearch".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//FG�ڵ� LIST ��ȸ
				if("cpy_fgsearch".equals(mode)) {			//������
					ArrayList fg_list = new ArrayList();
					fg_list = modDAO.getAppFGList(sWord);
					request.setAttribute("FG_List", fg_list); 
				} else {									//���̱���
					ArrayList fg_list = new ArrayList();
					fg_list = modDAO.getMakeFGList(sWord,login_id);
					request.setAttribute("FG_List", fg_list); 
				}

				//Assy List��ȸ
				ArrayList assy_list = new ArrayList();
				assy_list = modDAO.getAssyListCP(gid);
				request.setAttribute("ASSY_List", assy_list); 

				//�����ϱ�
				request.setAttribute("sWord",sWord);
				request.setAttribute("gid",gid);
				
				//�б��ϱ�
				if("cpy_fgsearch".equals(mode))
					getServletContext().getRequestDispatcher("/bm/str/billCpyFgSearch.jsp").forward(request,response);
				else if("pst_fgsearch".equals(mode))
					getServletContext().getRequestDispatcher("/bm/str/billPstFgSearch.jsp").forward(request,response);
				
			}
			//������ ��ǰ����Ʈ, ���̱��� ��ǰ����Ʈ
			else if ("cpy_list".equals(mode) || "pst_list".equals(mode)){
				com.anbtech.bm.business.BomBillModifyBO modBO = new com.anbtech.bm.business.BomBillModifyBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//����� ���躯�������� �˻��ϱ� 
				if("cpy_list".equals(mode)) {
					msg = modBO.checkCBomStatus(gid);
					if(msg.length() != 0) {
						out.println("	<script>");
						out.println("	alert('"+msg+"');");
						out.println("	history.back(-1);");
						out.println("	</script>");
						out.close();
					}
				}
				
				//����,���̱� ����� ���LIST
				ArrayList part_list = new ArrayList();
				if(gid.length() != 0) part_list = modBO.getCpyStrList(gid,level_no,parent_code);
				request.setAttribute("PART_List", part_list); 

				//������ �����б�
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				if(gid.length() != 0) masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//�޽��� ����
				request.setAttribute("msg",msg);

				//�б��ϱ�
				if("cpy_list".equals(mode))			//������
					getServletContext().getRequestDispatcher("/bm/str/billCpyList.jsp").forward(request,response);
				else if("pst_list".equals(mode))	//���̱���
					getServletContext().getRequestDispatcher("/bm/str/billPstList.jsp").forward(request,response);
			
			}
		
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
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
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/mbom/"+login_id+"/tmp";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory�����ϱ�
		String maxFileSize = "10";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//PL IMPORTó���ϱ� : ���� ������
		String ck = multi.getParameter("ck")==null?"tab":multi.getParameter("ck"); 
		if(ck.equals("tab")) ck = "	";
		else if(ck.equals("semi")) ck = ";";
		else if(ck.equals("pause")) ck = ",";
		else if(ck.equals("space")) ck = " ";
		else if(ck.equals("etc")) ck = multi.getParameter("etc");

		//�⺻�Ķ����
		String mode = multi.getParameter("mode")==null?"cpy_fgsearch":multi.getParameter("mode");
		String page = multi.getParameter("page")==null?"1":multi.getParameter("page");
		String sItem = multi.getParameter("sItem")==null?"pid":multi.getParameter("sItem");
		String sWord = multi.getParameter("sWord")==null?"":multi.getParameter("sWord");
	
		//MBOM STR������ ���� �⺻�Ķ����
		String pid = multi.getParameter("pid")==null?"":multi.getParameter("pid");
		String gid = multi.getParameter("gid")==null?"":multi.getParameter("gid");
		String parent_code = multi.getParameter("parent_code")==null?"":multi.getParameter("parent_code");
		String child_code = multi.getParameter("child_code")==null?"":multi.getParameter("child_code");
		String level_no = multi.getParameter("level_no")==null?"":multi.getParameter("level_no");
		String part_name = multi.getParameter("part_name")==null?"":multi.getParameter("part_name");
		String part_spec = multi.getParameter("part_spec")==null?"":multi.getParameter("part_spec");
		String location = multi.getParameter("location")==null?"":multi.getParameter("location");
		String op_code = multi.getParameter("op_code")==null?"":multi.getParameter("op_code");
		String qty_unit = multi.getParameter("qty_unit")==null?"":multi.getParameter("qty_unit");
		String qty = multi.getParameter("qty")==null?"":multi.getParameter("qty");
		String price_unit = multi.getParameter("price_unit")==null?"":multi.getParameter("price_unit");
		String price = multi.getParameter("price")==null?"":multi.getParameter("price");
		String adtag = multi.getParameter("adtag")==null?"":multi.getParameter("adtag");
		String eco_no = multi.getParameter("eco_no")==null?"":multi.getParameter("eco_no");
		String part_cnt = multi.getParameter("part_cnt")==null?"":multi.getParameter("part_cnt");
		String part_type = multi.getParameter("part_type")==null?"":multi.getParameter("part_type");
		String url = multi.getParameter("url")==null?"":multi.getParameter("url");
		String msg = multi.getParameter("msg")==null?"":multi.getParameter("msg");

		//���̱��� �Ķ���� �ޱ�
		String pcnt = multi.getParameter("pcnt")==null?"0":multi.getParameter("pcnt");	//��ü����
		int p_cnt = Integer.parseInt(pcnt);

		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
		com.anbtech.bm.entity.mbomStrTable paste = null;
		ArrayList paste_list = new ArrayList();
		for(int i=0; i<p_cnt; i++) {
			paste = new com.anbtech.bm.entity.mbomStrTable();

			pid = anbdt.getNumID(i);
			paste.setPid(pid);
			paste.setGid(gid);
			String pc="pc"+i; pc=multi.getParameter(pc)==null?"":multi.getParameter(pc);
			paste.setParentCode(pc);
			String cc="cc"+i; cc=multi.getParameter(cc)==null?"":multi.getParameter(cc);
			paste.setChildCode(cc);
			String ln="ln"+i; ln=multi.getParameter(ln)==null?"0":multi.getParameter(ln);
			paste.setLevelNo(ln);
			String lc="lc"+i; lc=multi.getParameter(lc)==null?"":multi.getParameter(lc);
			paste.setLocation(lc);
			String oc="oc"+i; oc=multi.getParameter(oc)==null?"":multi.getParameter(oc);
			paste.setOpCode(oc);
			String qu="qu"+i; qu=multi.getParameter(qu)==null?"":multi.getParameter(qu);
			paste.setQtyUnit(qu);
			String qy="qy"+i; qy=multi.getParameter(qy)==null?"0":multi.getParameter(qy);
			paste.setQty(qy);
			String ad="ad"+i; ad=multi.getParameter(ad)==null?"":multi.getParameter(ad);
			paste.setAssyDup(ad);

			paste_list.add(paste);
		}


		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//������FG�ڵ�, ���̱��� FG�ڵ� �˻�
			if ("cpy_fgsearch".equals(mode) || "pst_fgsearch".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//FG�ڵ� LIST ��ȸ
				if("cpy_fgsearch".equals(mode)) {			//������
					ArrayList fg_list = new ArrayList();
					fg_list = modDAO.getAppFGList(sWord);
					request.setAttribute("FG_List", fg_list); 
				} else {									//���̱���
					ArrayList fg_list = new ArrayList();
					fg_list = modDAO.getMakeFGList(sWord,login_id);
					request.setAttribute("FG_List", fg_list); 
				}

				//Assy List��ȸ
				ArrayList assy_list = new ArrayList();
				assy_list = modDAO.getAssyListCP(gid);
				request.setAttribute("ASSY_List", assy_list); 

				//�����ϱ�
				request.setAttribute("sWord",sWord);
				request.setAttribute("gid",gid);
				
				//�б��ϱ�
				if("cpy_fgsearch".equals(mode))
					getServletContext().getRequestDispatcher("/bm/str/billCpyFgSearch.jsp").forward(request,response);
				else if("pst_fgsearch".equals(mode))
					getServletContext().getRequestDispatcher("/bm/str/billPstFgSearch.jsp").forward(request,response);
				
			}
			//������ ��ǰ����Ʈ, ���̱��� ��ǰ����Ʈ
			else if ("cpy_list".equals(mode) || "pst_list".equals(mode)){
				com.anbtech.bm.business.BomBillModifyBO modBO = new com.anbtech.bm.business.BomBillModifyBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
			
				//����� ���躯�������� �˻��ϱ� 
				if("cpy_list".equals(mode)) {
					msg = modBO.checkCBomStatus(gid);
				}
				
				//����,���̱� ����� ���LIST (�����̸� part_list���� 0)
				ArrayList part_list = new ArrayList();
				if(msg.length() == 0) part_list = modBO.getCpyStrList(gid,level_no,parent_code);
				request.setAttribute("PART_List", part_list); 

				//������ �����б�
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//�޽��� ����
				request.setAttribute("msg",msg);

				//�б��ϱ�
				if("cpy_list".equals(mode))			//������
					getServletContext().getRequestDispatcher("/bm/str/billCpyList.jsp").forward(request,response);
				else if("pst_list".equals(mode))	//���̱���
					getServletContext().getRequestDispatcher("/bm/str/billPstList.jsp").forward(request,response);
			
			}
			//���̱� �����ϱ�
			else if ("pst_write".equals(mode)){
				com.anbtech.bm.business.BomBillModifyBO modBO = new com.anbtech.bm.business.BomBillModifyBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//�������� �ִ��� �Ǵ��ϱ�
				//msg = modDAO.getFgGrade(login_id,gid);

				//���̱� ����
				//if(msg.length() == 0) msg = modBO.pastePartList(paste_list);
				//else gid = "";
				msg = modBO.pastePartList(paste_list);

				if(msg.length() != 0) {
					out.println("	<script>");
					out.println("	alert('"+msg+"');");
					out.println("	</script>");
				} else {
					msg = "���������� ��ϵǾ����ϴ�.";
				}

				//MBOM TREE �Է�â���� ��ũ
				String data = "&gid="+gid+"&level_no=0&parent_code="+parent_code;
				out.println("	<script>");
				out.println("	parent.list.location.href('BomBillModifyServlet?mode=pst_list&msg="+data+"');");
				out.println("	</script>");
				out.close();
			}
			
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} //doPost()
}

