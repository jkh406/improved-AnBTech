import com.anbtech.pdf.*;
import com.anbtech.barcode.*;
import com.anbtech.text.Hanguel;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class pdfCtrlServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;
	
	/********
	 * 소멸자
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리 (목록보기)
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
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

		//기본파라미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MBOM STR구성을 위한 기본파라미터
		String paper_type = Hanguel.toHanguel(request.getParameter("paper_type"))==null?"A4":Hanguel.toHanguel(request.getParameter("paper_type"));
		String width = Hanguel.toHanguel(request.getParameter("width"))==null?"":Hanguel.toHanguel(request.getParameter("width"));
		String height = Hanguel.toHanguel(request.getParameter("height"))==null?"":Hanguel.toHanguel(request.getParameter("height"));
		String left_margin = Hanguel.toHanguel(request.getParameter("left_margin"))==null?"0":Hanguel.toHanguel(request.getParameter("left_margin"));
		String right_margin = Hanguel.toHanguel(request.getParameter("right_margin"))==null?"0":Hanguel.toHanguel(request.getParameter("right_margin"));
		String upper_margin = Hanguel.toHanguel(request.getParameter("upper_margin"))==null?"0":Hanguel.toHanguel(request.getParameter("upper_margin"));
		String lower_margin = Hanguel.toHanguel(request.getParameter("lower_margin"))==null?"0":Hanguel.toHanguel(request.getParameter("lower_margin"));
		String column_count = Hanguel.toHanguel(request.getParameter("column_count"))==null?"1":Hanguel.toHanguel(request.getParameter("column_count"));
		String row_count = Hanguel.toHanguel(request.getParameter("row_count"))==null?"1":Hanguel.toHanguel(request.getParameter("row_count"));
		String upload_path = Hanguel.toHanguel(request.getParameter("upload_path"))==null?"":Hanguel.toHanguel(request.getParameter("upload_path"));
		String barcode = Hanguel.toHanguel(request.getParameter("barcode"))==null?"":Hanguel.toHanguel(request.getParameter("barcode"));
		String barcode_count = Hanguel.toHanguel(request.getParameter("barcode_count"))==null?"":Hanguel.toHanguel(request.getParameter("barcode_count"));
		String used_count = Hanguel.toHanguel(request.getParameter("used_count"))==null?"0":Hanguel.toHanguel(request.getParameter("used_count"));
		String goods_name = Hanguel.toHanguel(request.getParameter("goods_name"))==null?"":Hanguel.toHanguel(request.getParameter("goods_name"));
		String serial_no = Hanguel.toHanguel(request.getParameter("serial_no"))==null?"":Hanguel.toHanguel(request.getParameter("serial_no"));
		String barcode_type = Hanguel.toHanguel(request.getParameter("barcode_type"))==null?"EAN13":Hanguel.toHanguel(request.getParameter("barcode_type"));
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		BAR CODE 출력하기
			//------------------------------------------------------------
			//BAR CODE 입력폼
			if ("read_barini".equals(mode)){

				//분기하기
				getServletContext().getRequestDispatcher("/sc/bar/readBarcode.jsp").forward(request,response);
				
			}
			//BAR CODE 출력
			else if ("read_barcode".equals(mode)){
				com.anbtech.barcode.formtecBarCode bar = new com.anbtech.barcode.formtecBarCode();

				//출력
				bar.barcodeFORM_60_A4(upload_path,login_id,barcode,barcode_count,used_count,goods_name,serial_no,barcode_type); 

				//정보 전달
				request.setAttribute("file_path",bar.getFilePath());
				request.setAttribute("save_file",bar.getSaveFile());
				request.setAttribute("file_size",bar.getFileSize());

				request.setAttribute("barcode_type",barcode_type);
				request.setAttribute("barcode",barcode);
				request.setAttribute("paper_type",paper_type);
				request.setAttribute("barcode_count",barcode_count);
				request.setAttribute("used_count",used_count);
				request.setAttribute("goods_name",goods_name);
				request.setAttribute("serial_no",serial_no);

				//분기하기
				getServletContext().getRequestDispatcher("/sc/bar/pdfBarcode.jsp").forward(request,response);
				
			}
			//------------------------------------------------------------
			//		TEXT PDF변환하기
			//------------------------------------------------------------
			//TEXT 입력폼
			else if ("read_textini".equals(mode)){

				//분기하기
				getServletContext().getRequestDispatcher("/sc/pdf/readTextFile.jsp").forward(request,response);
				
			}
			
		}catch (Exception e){
				//에러출력 페이지로 분기
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}

	} //doGet()

	/**********************************
	 * post방식으로 넘어왔을 때 처리 
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
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

		//MultipartRequest 크기, 저장디렉토리
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/pdf/"+login_id;
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory생성하기
		String maxFileSize = "10";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		//기본파라미터
		String mode = multi.getParameter("mode")==null?"":multi.getParameter("mode");
		String page = multi.getParameter("page")==null?"1":multi.getParameter("page");
		String sItem = multi.getParameter("sItem")==null?"":multi.getParameter("sItem");
		String sWord = multi.getParameter("sWord")==null?"":multi.getParameter("sWord");
	
		//MBOM STR구성을 위한 기본파라미터
		String paper_type = multi.getParameter("paper_type")==null?"A4":multi.getParameter("paper_type");
		String width = multi.getParameter("width")==null?"":multi.getParameter("width");
		String height = multi.getParameter("height")==null?"":multi.getParameter("height");
		String left_margin = multi.getParameter("left_margin")==null?"0":multi.getParameter("left_margin");
		String right_margin = multi.getParameter("right_margin")==null?"0":multi.getParameter("right_margin");
		String upper_margin = multi.getParameter("upper_margin")==null?"0":multi.getParameter("upper_margin");
		String lower_margin = multi.getParameter("lower_margin")==null?"0":multi.getParameter("lower_margin");
		String column_count = multi.getParameter("column_count")==null?"1":multi.getParameter("column_count");
		String row_count = multi.getParameter("row_count")==null?"1":multi.getParameter("row_count");
		String upload_path = multi.getParameter("upload_path")==null?"":multi.getParameter("upload_path");
		String barcode = multi.getParameter("barcode")==null?"":multi.getParameter("barcode");
		String barcode_count = multi.getParameter("barcode_count")==null?"":multi.getParameter("barcode_count");
		String used_count = multi.getParameter("used_count")==null?"0":multi.getParameter("used_count");
		String goods_name = multi.getParameter("goods_name")==null?"":multi.getParameter("goods_name");
		String serial_no = multi.getParameter("serial_no")==null?"":multi.getParameter("serial_no");
		String barcode_type = multi.getParameter("barcode_type")==null?"":multi.getParameter("barcode_type");
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		BAR CODE 출력하기
			//------------------------------------------------------------
			//BAR CODE 입력폼
			if ("read_barini".equals(mode)){

				//분기하기
				getServletContext().getRequestDispatcher("/sc/bar/readBarcode.jsp").forward(request,response);
				
			}
			//BAR CODE 출력
			else if ("read_barcode".equals(mode)){
				com.anbtech.barcode.formtecBarCode bar = new com.anbtech.barcode.formtecBarCode();

				//출력
				bar.barcodeFORM_60_A4(upload_path,login_id,barcode,barcode_count,used_count,goods_name,serial_no,barcode_type); 

				//정보 전달
				request.setAttribute("file_path",bar.getFilePath());
				request.setAttribute("save_file",bar.getSaveFile());
				request.setAttribute("file_size",bar.getFileSize());

				request.setAttribute("barcode_type",barcode_type);
				request.setAttribute("barcode",barcode);
				request.setAttribute("paper_type",paper_type);
				request.setAttribute("barcode_count",barcode_count);
				request.setAttribute("used_count",used_count);
				request.setAttribute("goods_name",goods_name);
				request.setAttribute("serial_no",serial_no);

				//분기하기
				getServletContext().getRequestDispatcher("/sc/bar/pdfBarcode.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		TEXT PDF변환하기
			//------------------------------------------------------------
			//TEXT 입력폼
			else if ("read_textini".equals(mode)){

				//분기하기
				getServletContext().getRequestDispatcher("/sc/pdf/readTextFile.jsp").forward(request,response);
				
			}
			//BAR CODE 출력
			else if ("read_textpdf".equals(mode)){
				com.anbtech.pdf.TextToPDF textP = new com.anbtech.pdf.TextToPDF();

				//출력
				textP.changeTextToPDF(multi,filepath); 

				//정보 전달
				request.setAttribute("file_path",textP.getFilePath());
				request.setAttribute("save_file",textP.getSaveFile());
				request.setAttribute("org_file",textP.getOriginalFile());
				request.setAttribute("file_size",textP.getFileSize());

				//분기하기
				getServletContext().getRequestDispatcher("/sc/pdf/pdfText.jsp").forward(request,response);
				
			}
			
		}catch (Exception e){
				//에러출력 페이지로 분기
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} //doPost()
}

