import com.anbtech.am.entity.*;
import com.anbtech.am.db.*;
import com.anbtech.am.business.*;
import com.anbtech.am.admin.*;
import com.anbtech.admin.entity.*;
import com.anbtech.text.Hanguel;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class AssetServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;

	/**********************************
	 * 소멸자
	 ***********************************/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException{

		// 필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		
		// 카테고리 정보 관련
		String mode				= request.getParameter("mode"); 
		String div				= request.getParameter("div")==null?"":request.getParameter("div");	// 카테고리 입력/수정/삭제
		String c_no				= request.getParameter("c_no")==null?"0":request.getParameter("c_no");	// 카테고리 관리번호
		String ct_id			= request.getParameter("ct_id")==null?"0":request.getParameter("ct_id");	// 카테고리 ID
		String ct_level			= request.getParameter("ct_level");	// 카테고리 LEVEL
		String ct_parent		= request.getParameter("ct_parent");	// 부모 카테고리
		String ct_word			= request.getParameter("ct_word");	// 카테고리 약자
		String ct_name			= request.getParameter("ct_name");	// 카테고리 이름
		String dc_percent		= request.getParameter("dc_percent");	// 카테고리 감가 비율(%)
		String as_no			= request.getParameter("as_no")==null?"0":request.getParameter("as_no");	// 자산번호
		String apply_dc			= request.getParameter("apply_dc");	// 감가 적용 유무 (count)

		// 반출/이관신청관련
		String w_id				= request.getParameter("w_id");						// 등록자 id 
		String w_name			= Hanguel.toHanguel(request.getParameter("w_name"));	// 등록자 이름()
		String w_rank			= Hanguel.toHanguel(request.getParameter("w_rank"));	// 등록자 부서명	
		String u_id				= request.getParameter("u_id");						// 자산 사용자 정보(id)
		String u_name			= Hanguel.toHanguel(request.getParameter("u_name"));	// 자산 사용자 정보(이름)
		String u_rank			= Hanguel.toHanguel(request.getParameter("u_rank"));	// 자산 사용자 부서명
		String takeout_reason	= Hanguel.toHanguel(request.getParameter("takeout_reason"));	// 자산 처리 신청 사유
		String out_destination	= request.getParameter("out_destination")==null?"":Hanguel.toHanguel(request.getParameter("out_destination"));	// 사용처/반출장소..	
		String as_status		= request.getParameter("as_status");					// 자산 상태 코드
		String o_status			= request.getParameter("o_status")==null?"o":request.getParameter("o_status") ;	// 자산 신청 구분 코드
		String as_statusinfo	= request.getParameter("as_statusinfo")==null? "":Hanguel.toHanguel(request.getParameter("as_statusinfo"));						
		String w_date			= request.getParameter("c_date");						// 신청일자
		String wi_date			= request.getParameter("wi_date");					// 반납처리일자
		String in_date			= request.getParameter("in_date")==null?"0":request.getParameter("in_date");	// 반납/입고일자
		String udate			= request.getParameter("u_date");						
		String u_date			= request.getParameter("sdate")==null?"0":request.getParameter("sdate"); // 실 가용(신청) 시작일자
		String tu_date			= request.getParameter("edate")==null?"0":request.getParameter("edate");
		String h_no				= request.getParameter("h_no");				// 자산 이력 리스트 관리번호
		String handle			= request.getParameter("handle");				// 자산 반출 가능 여부 코드
		String mode_temp		= request.getParameter("mode_temp")==null?"":request.getParameter("mode_temp");	// 분기 구분코드

		if(in_date.length()>8)  in_date= in_date.substring(0,4)+in_date.substring(5,7)+in_date.substring(8,10);
		if(u_date.length()>8)	u_date= u_date.substring(0,4)+u_date.substring(5,7)+u_date.substring(8,10);
		if(tu_date.length()>8)  tu_date= tu_date.substring(0,4)+tu_date.substring(5,7)+tu_date.substring(8,10);

		//모드 및 현재 페이지 파리미터
		String page				= request.getParameter("page");
		String no				= request.getParameter("no");
		String t_id				= request.getParameter("t_id");
		
		// 자산 목록 자산 적용관련 파라미터들
		String year				= request.getParameter("year");
		String month			= request.getParameter("month");
		String assetupdate		= request.getParameter("assetupdate")==null?"":request.getParameter("assetupdate"); // 자산 업데이트 여부결정
		String value			= request.getParameter("value");

		//검색시에 넘어오는 파라미터들
		String searchword		= Hanguel.toHanguel(request.getParameter("searchword"));
		String searchscope		= request.getParameter("searchscope");
		String category			= request.getParameter("category");
		String tablename		= "";

		if ( page == null) page = "1";
		if ( searchword == null) searchword = "";
		if ( searchscope == null) searchscope = "";

		String redirectUrl	= "";
		String pid	= request.getParameter("pid")==null?"":request.getParameter("pid");	 // 결재번호
		String pid2	= request.getParameter("pid2")==null?"":request.getParameter("pid2");// 결재번호

		//현재 접속중인 사용자 아이디 가져오기
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

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/////////////////////////////////////////////////////
			// 자산분류현황 리스트 출력
			/////////////////////////////////////////////////////
			if("category_list".equals(mode)) { 
				com.anbtech.am.admin.makeCtTree makeCtTree = new com.anbtech.am.admin.makeCtTree();
				StringBuffer sb = new StringBuffer();
				sb = makeCtTree.viewTree(1,1);
				request.setAttribute("CategoryList", sb);
				getServletContext().getRequestDispatcher("/am/admin/managerCategory.jsp").forward(request,response);
			}
						
			////////////////////////////////////////////////////
			//  자산분류 입력폼(카테고리 정보 보기/수정/삭제/등록)
			//  * 처리구분자(div) 
			//    f:최상위 카테고리 추가  a:추가, m:수정, d:삭제
			////////////////////////////////////////////////////
			else if("category_info_view".equals(mode)){ 
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO	= new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsCategoryTable asCategoryTable = new com.anbtech.am.entity.AsCategoryTable();
				
				if( "a".equals(div) || "m".equals(div) || "d".equals(div) ) { // 카테고리 추가or수정or삭제
					asCategoryTable = (com.anbtech.am.entity.AsCategoryTable)assetModuleDAO.getCtInfo(ct_id);//카테고리정보가져오기
					request.setAttribute("asCtTableM", asCategoryTable);
					getServletContext().getRequestDispatcher("/am/admin/inputCtForm.jsp?div="+div).forward(request,response);
				} else if("f".equals(div)) { // 최상위 카테고리 추가
					getServletContext().getRequestDispatcher("/am/admin/inputCtForm.jsp?div="+div).forward(request,response);
				}
			}
					
			//////////////////////////////////////////////////////////
			//   자산등록현황 리스트 출력
			//	   (mode:'asset_list'  div: 'detail' or 'general')	
			//	 
			//	 - 정보 List에서 감가적용 요청시 감가적용
			//     기본은 년 Base로 감가적용, 
			//     But, 당해 구입한 자산은 1년(12 개월)을 넘겨야 감가적용
			//   
			//   - 자산정보 list 요청시 검색범위 구분자 div
			//		(div: 'general'-일반검색, 'detail'-상세검색)
			//////////////////////////////////////////////////////////
			else if("asset_list".equals(mode)){
				com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
				com.anbtech.am.db.AssetModuleDAO     assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsInfoTable       asInfoTable = new com.anbtech.am.entity.AsInfoTable();			
				com.anbtech.am.admin.makeCtTree makeCtTree			= new com.anbtech.am.admin.makeCtTree();
				com.anbtech.am.business.AMLinkBO amLinkBO			= new com.anbtech.am.business.AMLinkBO(con);
				com.anbtech.am.entity.AMLinkTable amLinkTable		= new com.anbtech.am.entity.AMLinkTable();

				ArrayList searchList = new ArrayList();
				tablename = "as_info";
				String sb = "";					
				ct_id = assetModuleDAO.getCtId(c_no);

				//  카테고리 가져오기 
				sb = makeCtTree.viewCombo(1,1);             
					
				// 감가 적용 '현재 자산 update 요청시 실행'
				// (parameter: 년도(기준년), 달(기준달), 최저감가치 )
				if("update".equals(assetupdate)) 
					assetModuleBO.getAutoUpdate(year,month,value);
				
				// 검색결과 가져오기 
				searchList = assetModuleDAO.getAssetList(tablename,mode,searchword,searchscope,page,c_no,div,ct_id);
				
				// link String 및 paging
				amLinkTable = amLinkBO.getRedirect(tablename,mode,searchword,searchscope,page,c_no,div,ct_id,login_division);
				
				request.setAttribute("CategoryList", sb);
				request.setAttribute("assetList", searchList);
				request.setAttribute("Redirect",amLinkTable);

				//	자산 총액 가져오기 => 아직 적용하지 않음.
				//	String value ="";
				//	value = assetModuleDAO.sumAsValue();
				getServletContext().getRequestDispatcher("/am/admin/managerAsset.jsp?c_no="+c_no).forward(request,response);
			}
						
			////////////////////////////////////////////////////////////////////
			//   자산정보 등록 및 수정 폼
			//   - 처리 구분 CODE (div)
			//		input:등록폼, modify:수정폼, view:상세폼, delete:삭제폼
			//      delete_view: 폐기자산 정보보기 폼    download: 첨부화일다운로드시
			////////////////////////////////////////////////////////////////////
			else if("asset_form".equals(mode)){
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO	= new com.anbtech.am.db.AssetModuleDAO(con);	// 자산관리DAO
				com.anbtech.am.admin.makeCtTree makeCtTree		= new com.anbtech.am.admin.makeCtTree();
				com.anbtech.am.entity.AMUserTable amuserTable	= new com.anbtech.am.entity.AMUserTable();		// 사용자정보
				com.anbtech.am.entity.AsCategoryTable asCategoryTable	= new com.anbtech.am.entity.AsCategoryTable();
				com.anbtech.am.entity.AsInfoTable asInfoTable	= new com.anbtech.am.entity.AsInfoTable();
				
				// * 자산 정보 등록폼 *
				// 1. 사용자(등록자) 정보 setting : jsp로 parameter 넘길때 한글깨져서...
				// 2. 카테고리 정보 가져오기 (입력폼에서 select list BOX)
				// 3. 감가비율 정보 가져오기 (해당 카테고리 및 품목에 적용되는 감가비율)
				// 4. 등록폼으로 이동
				if("input".equals(div)) { 
					String sb		= "";
					String percent	= "";
				
					// 사용자 정보 Setting
					amuserTable.setUserId(login_id);
					amuserTable.setUserName(login_name);
					amuserTable.setUserRank(login_division);
								
					// 카테고리 정보 가져오기
					asCategoryTable=(com.anbtech.am.entity.AsCategoryTable)assetModuleDAO.getCtInfoByCno(c_no);
					
					// 감가비율정보 (자산 등록시 카테고리 및 품목에 감가비율가져오기)
					percent = asCategoryTable.getDcPercent();	
											
					sb = makeCtTree.viewCombo(1,1);	

					request.setAttribute("user",amuserTable);
					request.setAttribute("CategoryList", sb);
					getServletContext().getRequestDispatcher("/am/admin/AssetInfoForm.jsp?div=input&DcPercent="+percent+"&c_no="+c_no).forward(request,response);
			
				// * 정보 보기 폼 *
				// 1. 파일 리스트 가져오기
				// 2. 자산정보 가져오기
				// 3. 해당 품목 및 카테고리 전체 String 가져오기(예: "slink>전산장비>컴퓨터")
				} else if("view".equals(div)){
					String sb			= "";
					ArrayList arry		= new ArrayList();
					ArrayList file_list = new ArrayList();
					
					file_list = (ArrayList)assetModuleDAO.getFileList(as_no);		  // 파일 리스트 정보
					asInfoTable = 
					(com.anbtech.am.entity.AsInfoTable)assetModuleDAO.getInfo(as_no); // 자산 정보
					arry = (ArrayList)assetModuleDAO.getFileList(as_no);
					sb = makeCtTree.viewCategory(c_no,"");							  // 카테고리 풀-스트링

					request.setAttribute("assetInfo",asInfoTable);
					request.setAttribute("file",file_list);
					request.setAttribute("assetfile",arry);
					request.setAttribute("CategoryList", sb);
					getServletContext().getRequestDispatcher("/am/admin/AssetInfoForm.jsp?div=view&c_no"+c_no+"&login_id="+login_id).forward(request,response);

				// 폐기 자산 리스트에서 상세폐기정보 볼때...
				} else if("delete_view".equals(div)){
									
					ArrayList arry = new ArrayList();
					ArrayList file_list = new ArrayList();

					com.anbtech.am.db.AssetModuleDAO assetModuleDAO2 = new com.anbtech.am.db.AssetModuleDAO(con);
					file_list = (ArrayList)assetModuleDAO2.getFileList(as_no);
					asInfoTable = (com.anbtech.am.entity.AsInfoTable)assetModuleDAO.getInfo(as_no);
					arry = (ArrayList)assetModuleDAO.getFileList(as_no);
					
					// 카테고리 풀-스트링
					String sb = "";
					sb = makeCtTree.viewCategory(c_no,"");

					request.setAttribute("file",file_list);
					request.setAttribute("assetInfo",asInfoTable);
					request.setAttribute("assetfile",arry);
					request.setAttribute("CategoryList", sb);
					getServletContext().getRequestDispatcher("/am/admin/AssetInfoForm.jsp?div="+div+"&c_no"+c_no+"&login_id="+login_id).forward(request,response);

					// * 수정정보폼 *
					// 1. 카테고리 select list BOX 정보가져오기
					// 2. 자산정보 가져오기
					// 3. 첨부화일정보 가져오기
				} else if("modify".equals(div)){
					ArrayList arry = new ArrayList();
					String sb = "";

					sb = makeCtTree.viewCombo(1,1);
					asInfoTable = (com.anbtech.am.entity.AsInfoTable)assetModuleDAO.getInfo(as_no);
					arry = (ArrayList)assetModuleDAO.getFileList(as_no);

					request.setAttribute("CategoryList", sb);
					request.setAttribute("assetInfo",asInfoTable);
					request.setAttribute("assetfile",arry);
					getServletContext().getRequestDispatcher("/am/admin/AssetInfoForm.jsp?div=modify&c_no="+c_no).forward(request,response);
				
					// * 삭제시 삭제정보폼 *
					// 1. 카테고리 정보폼
					// 2. 자산정보 가져오기
					// 3. 첨부화일정보 가져오기
				}else if("delete".equals(div)){
					ArrayList arry = new ArrayList();
					ArrayList file_list = new ArrayList();
					String sb = "";

					sb = makeCtTree.viewCategory(c_no,"");								  // 카테고리
					asInfoTable = 
						(com.anbtech.am.entity.AsInfoTable)assetModuleDAO.getInfo(as_no); // 자산정보
					arry = (ArrayList)assetModuleDAO.getFileList(as_no);				  // 첨부화일정보						
					
					request.setAttribute("CategoryList", sb);
					request.setAttribute("assetInfo",asInfoTable);						
					request.setAttribute("assetfile",arry);

					getServletContext().getRequestDispatcher("/am/admin/AssetInfoForm.jsp?div=delete").forward(request,response);
				}else if ("download".equals(div)){ // AssetInfoForm.jsp에서 정보보기일경우 첨부화일 다운로드할때...
					com.anbtech.am.business.AssetModuleBO assetBO = new com.anbtech.am.business.AssetModuleBO(con);
					com.anbtech.am.entity.AsInfoTable file = new com.anbtech.am.entity.AsInfoTable();
					
					file = assetBO.getFile_fordown(as_no); // as_no (예: 15_1,15_2.....)
								
					String filename = file.getFileName();
					String filetype = file.getFileType();
					String filesize = file.getFileSize();

					// 다운로드 받을 경로
					String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/am/" + as_no + ".bin";

					if (filetype.indexOf("mage")<=0)
						filetype = "application/unknown";					
					
					String strClient=request.getHeader("User-Agent");

					if(strClient.indexOf("MSIE 5.5")>-1) 	response.setHeader("Content-Disposition","filename="+filename);
					else response.setHeader("Content-Disposition","attachment; filename="+filename);

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
			}
		
			/////////////////////////////////////////////////	
			//	[ 관리자 모드	 ]
			//	폐기 자산 리스트
			//  1. 카테고리정보 가져오기
			//  2. 검색결과(폐기 자산 정보) 
			//     - as_status가 '10'(폐기)인 자산
			/////////////////////////////////////////////////
			 else if("asset_del_list".equals(mode)){
					tablename = "as_info";

					com.anbtech.am.business.AssetModuleBO  assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
					com.anbtech.am.db.AssetModuleDAO      assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					com.anbtech.am.entity.AsInfoTable		 asInfoTable = new com.anbtech.am.entity.AsInfoTable();	
					com.anbtech.am.business.AMLinkBO			amLinkBO = new com.anbtech.am.business.AMLinkBO(con);
					com.anbtech.am.entity.AMLinkTable		 amLinkTable = new com.anbtech.am.entity.AMLinkTable();
					com.anbtech.am.admin.makeCtTree			  makeCtTree = new com.anbtech.am.admin.makeCtTree();
					ArrayList searchList = new ArrayList();
					String sb = "";
					
					sb = makeCtTree.viewCombo(1,1);    // 카테고리 가져오기 
						
					ct_id = assetModuleDAO.getCtId(c_no);
					//  검색결과 가져오기 
					searchList = assetModuleDAO.getAssetList(tablename,mode,searchword,searchscope,page,c_no,div,ct_id);
					//  link String 및 paging
					amLinkTable = amLinkBO.getRedirect(tablename,mode,searchword,searchscope,page,c_no,div,ct_id,login_division);
					
					request.setAttribute("CategoryList", sb);
					request.setAttribute("assetList", searchList);
					request.setAttribute("Redirect",amLinkTable);
														
					getServletContext().getRequestDispatcher("/am/admin/managerDelAsset.jsp?c_no="+c_no).forward(request,response);

				  
			////////////////////////////////////////////////////////////////
			//  [ 관리자 모드	 ]
			//	폐기되었던 자원 복구
			//  - as_info TABLE의 '폐기상태'CODE('10')을 정상 CODE('6')로 변경
			////////////////////////////////////////////////////////////////
			}	else if("asset_repair".equals(mode)){
				  
					com.anbtech.am.db.AssetModuleDAO  assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					assetModuleDAO.assetRepair(as_no);
					response.sendRedirect("AssetServlet?mode=asset_del_list");				


			/////////////////////////////////////////////////////////
			//  [ 관리자 모드 ]  
			//	DB의 쓰레기 정보 삭제(as_history TABLE)
			//  - 반출/이관/대여 신청후(as_status:'1') 상신하지 않은 DATA는
			//    모두 삭제(DELETE)
			////////////////////////////////////////////////////////
			}	else if("cleanDb".equals(mode)){
					com.anbtech.am.db.AssetModuleDAO  assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					assetModuleDAO.cleanDB();
					response.sendRedirect("AssetServlet?mode=asset_list");				
			

			/////////////////////////////////////////////////
			//	[ 사용자 모드 ]
			//	자산 리스트
			//	1. 카테고리 리스트 가져오기(select list BOX)
			//  2. 자산정보 가져오기
			//	   - 자산정보 list 요청시 검색범위 구분자 div
			//		 (div: 'general'-일반검색, 'detail'-상세검색)
			/////////////////////////////////////////////////
			}  else if("user_asset_list".equals(mode)){
					
				com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
				com.anbtech.am.admin.makeCtTree makeCtTree = new com.anbtech.am.admin.makeCtTree();
				com.anbtech.am.business.AMLinkBO amLinkBO = new com.anbtech.am.business.AMLinkBO(con);
				com.anbtech.am.entity.AMLinkTable amLinkTable = new com.anbtech.am.entity.AMLinkTable();
				
				String sb = "";
				tablename = "as_info";
				ArrayList searchList = new ArrayList();
				
				sb = makeCtTree.viewCombo(1,1);				// 카테고리 가져오기
				ct_id = assetModuleDAO.getCtId(c_no);		// as_category에서 ct_id가져오기

				// 자산정보
				searchList = assetModuleDAO.getAssetList(tablename,mode,searchword,searchscope,page,c_no,div,ct_id);
				
				// paging 
				amLinkTable = amLinkBO.getRedirect(tablename,mode,searchword,searchscope,page,c_no,div,ct_id,login_division);
				
				request.setAttribute("CategoryList", sb); 
				request.setAttribute("assetList", searchList);
				request.setAttribute("Redirect",amLinkTable);
				getServletContext().getRequestDispatcher("/am/as_user/managerAsReq.jsp?c_no="+c_no+"&login_id="+login_id).forward(request,response);

				
			/////////////////////////////////////////////////////
			//  [ 사용자 모드 ]
			//  선택된 자산(1개)의 상세정보
			//	1. 카테고리 리스트
			//  2. 첨부화일 리트트 가져오기
			//  3. 자산정보 가져오기
			/////////////////////////////////////////////////////
			}	else if("user_asset_view".equals(mode)){
					
					com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
					com.anbtech.am.admin.makeCtTree makeCtTree = new com.anbtech.am.admin.makeCtTree();
					ArrayList arry = new ArrayList();
					ArrayList file_list = new ArrayList();
					String sb = "";

					sb = makeCtTree.viewCategory(c_no,"");							// 카테고리 스트링
					file_list = (ArrayList)assetModuleDAO.getFileList(as_no);		// 첨부파일 리스트
					asInfoTable = (com.anbtech.am.entity.AsInfoTable)assetModuleDAO.getInfo(as_no);
					arry = (ArrayList)assetModuleDAO.getFileList(as_no);			

					request.setAttribute("CategoryList", sb);
					request.setAttribute("file",file_list);
					request.setAttribute("assetInfo",asInfoTable);
					request.setAttribute("assetfile",arry);
					getServletContext().getRequestDispatcher("/am/admin/AssetInfoForm.jsp?mode=user_asset_view&div=view&c_no="+c_no+"&login_id="+login_id).forward(request,response);
			
				
			/////////////////////////////////////////////////
			//  [ 사용자 모드 ]
			//  자산의 약식 정보 & 이력 정보
			//  1. 카테고리 리스트 가져오기
			//  2. 자산정보 가져오기(mode:'user_each_history' div:'each')
			//  3. paging(mode:'user_each_history' div:'each')
			/////////////////////////////////////////////////
			}	else if("user_each_history".equals(mode)){
					
					com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
					com.anbtech.am.admin.makeCtTree makeCtTree = new com.anbtech.am.admin.makeCtTree();
					com.anbtech.am.business.AMLinkBO amLinkBO = new com.anbtech.am.business.AMLinkBO(con);		// 링크관련
					com.anbtech.am.entity.AMLinkTable amLinkTable = new com.anbtech.am.entity.AMLinkTable();	// 링크관려 helper..

					ArrayList reqList = new ArrayList();
					String sb = "";
					tablename = "as_history";

					sb = makeCtTree.viewCategory(c_no,"");			// 카테고리 스트링
					asInfoTable = assetModuleDAO.getInfo(as_no);	// 자산 정보 가져오기 
					//자산의 이력 정보(리스트) 가져오기
					reqList = assetModuleDAO.getInfoList(tablename,mode,login_id,searchscope,page,as_no,"each",login_division);
					// paging
					amLinkTable = amLinkBO.getRedirect(tablename,mode,login_id,c_no,page,as_no,"each",ct_id,login_division);

					request.setAttribute("CategoryList", sb);
					request.setAttribute("assetInfo",asInfoTable);
					request.setAttribute("assetReqList1",reqList);
					request.setAttribute("Redirect",amLinkTable);
					getServletContext().getRequestDispatcher("/am/as_user/eachHistory.jsp?login_id="+login_id).forward(request,response);

					
			/////////////////////////////////////////////////
			//	[ 사용자 모드 ]
			//  - 자산 반출/이관/대여 신청폼
			//  - 선택된 자산의 정보 가져오기
			//  - 신청폼으로 이동
			/////////////////////////////////////////////////
			}	else if("user_moving_req".equals(mode)){
					
					com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
					com.anbtech.am.entity.AMUserTable amuserTable = new com.anbtech.am.entity.AMUserTable();
					// 사용자(신청 등록자) 정보 SETTING
					amuserTable.setUserId(login_id);
					amuserTable.setUserName(login_name);
					amuserTable.setUserRank(login_division);
					
					asInfoTable = assetModuleDAO.getInfo(as_no);	// 자산 정보 가져오기 

					request.setAttribute("user",amuserTable);
					request.setAttribute("assetInfo",asInfoTable);
					getServletContext().getRequestDispatcher("/am/as_user/each_moving_req.jsp?as_no="+as_no+"&o_status="+o_status).forward(request,response);

			//////////////////////////////////////////////////////////////////////////
			//  [ 사용자(자산 입/출 관리자) 모드 ] 
			//	
			//	자산 반출/이관/대여 정보 저장 & 상신 (as_status='2' => 상신)
			//	  
			//	1. 반출/대여 가능 여부 알아보기(신청일자 중복 CHECK)
			//     - 처리 가능 
			//     - 불가 메시지: msg-불가 메시지
			//
			//  2. * 가능할 경우
			//       - 자산 신청정보 등록(assetModuleDAO.saveAsHistory())
			//		   : 신청정보등록 후 결재진행시 필요한 정보를 SELECT해서 배열에 담아온다.
			//		   (h_no-이력정보 관리번호, as_no-자산 관리번호, o_status-신청구분코드,     
			//          as_status-자산상태코드)
			//       - 신청 결재 진행
			//		   : 결재 정보 가지고 confirm.jsp이동 -> 결재진행 여부 확인
			//     * 불가능할 경우
			//       - 이전 페이지로 이동
			//		
			//////////////////////////////////////////////////////////////////////////
			}   else if("user_asreq_process".equals(mode)){
				   
				com.anbtech.am.entity.AsHistoryTable asHistoryTable = new  com.anbtech.am.entity.AsHistoryTable();
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO		= new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
				com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);	// 사용자 정보 DB Access
				com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable(); // 사용자정보Helper..
				String msg ="enable";
				
				if(!tu_date.equals("0")) { 
					in_date = tu_date;	    
					msg = assetModuleBO.checkOut(u_date,in_date,as_no,o_status);  // 반출/대여 가능 여부 알아보기
				} else {
					// msg = assetModuleBO.checkTranse(u_date,as_no);			  // 코드구현 안됨(2/18)
				}

				// 상신 가능할 경우
				if(msg.equals("enable")) {

				 // 현재 자산을 사용할 사용자 정보 가져오기 
				 u_id	= (String)assetModuleBO.getUserInfo(u_name);
				 user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(u_id);
				 u_name = user_info.getUserName();
				 u_rank = user_info.getDivision();
				 u_name = u_id+"/"+u_name;

				 String[] app = new String[4];
				 String m_id = assetModuleDAO.getManagerId(as_no); // 자산의 관리자 정보(as_info) 가져오기 ID

				 // 사용자(자산 관리자) 정보 가져오기
				 user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(m_id);
				 String m_name = user_info.getUserName();
				 String m_rank = user_info.getDivision();
				 m_name = m_id+"/"+m_name;
				
				 // 신청정보 등록
				 app=(String[])assetModuleDAO.saveAsHistory(as_no,m_id,m_name,m_rank,w_id, w_name, w_rank, u_id, u_name, u_rank ,takeout_reason ,out_destination ,as_status,o_status ,w_date ,u_date ,tu_date);

				 // 결재진행
				 getServletContext().getRequestDispatcher("/am/admin/confirm.jsp?h_no="+app[0]+"&as_no="+app[1]+"&o_status="+app[2]+"&as_status="+app[3]).forward(request,response);	
				
				} else {
						// 아니면 신청 불가를 알려준고 뒤로 이동
						PrintWriter out7 = response.getWriter();
						out7.println("	<script>");
						out7.println("		alert('"+msg+"');");
						out7.println("		history.back()");
						out7.println("	</script>");
						out7.close();
				}
			
				
			/////////////////////////////////////////////////
			//	[ 사용자 (자산 신청 입/출 관리자) 모드 ]
			//	자산 반출/이관 신청 결재 대상 LIST
			//  1. 결재 대상 list (mode:'req_app_list'  div:'app')
			//  2. paging (mode:'req_app_list'  div:'app')
			//  3. 결재리스트 화면으로 이동
			/////////////////////////////////////////////////
			}	else if("req_app_list".equals(mode)){

				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AMLinkBO amLinkBO = new com.anbtech.am.business.AMLinkBO(con);
				com.anbtech.am.entity.AMLinkTable amLinkTable = new com.anbtech.am.entity.AMLinkTable();
				ArrayList reqList = new ArrayList();
				tablename = "as_history";
				
				// 1. 선택된 자산의 이력 정보(리스트) 가져오기
				reqList = assetModuleDAO.getInfoList(tablename,mode,searchword,searchscope,page,as_no,"app",login_division);
				request.setAttribute("assetReqList1",reqList);
									
				// 2. paging
				amLinkTable = amLinkBO.getRedirect(tablename,mode,c_no,searchscope,page,as_no,"app",ct_id,login_division);
				request.setAttribute("Redirect",amLinkTable);
				
				// 3. 이동
				getServletContext().getRequestDispatcher("/am/admin/req_app.jsp").forward(request,response);

			/////////////////////////////////////////////////
			//  [ 사용자 (자산 신청 입/출 관리자) 모드 ]
			//	2차 결재 요청 
			/////////////////////////////////////////////////
			}	else if("req_app2".equals(mode)){
										
				PrintWriter out7 = response.getWriter();
				out7.println("	<script>");
				out7.println("	location.href('../gw/approval/module/Asset_ReApp.jsp?mode=app_asset_view&h_no="+h_no+"&as_no="+as_no+"&o_status="+o_status+"&as_status="+as_status+"');");
				out7.println("	</script>");
				out7.close();
					
			/////////////////////////////////////////////////
			//  [ 사용자 (자산 신청 입/출 관리자) 모드 ]
			//  자산 대여 업무 관리 (대여 리스트)
			//  1. 자산 대여 업무관련 정보 list 
			//	   (mode:'lending_list' div='lending')
			//  2. paging (mode:'lending_list' div='lending')
			/////////////////////////////////////////////////
			}	else if("lending_list".equals(mode)){

				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AMLinkBO amLinkBO		= new com.anbtech.am.business.AMLinkBO(con);
				com.anbtech.am.entity.AMLinkTable amLinkTable	= new com.anbtech.am.entity.AMLinkTable();
				ArrayList reqList = new ArrayList();
				tablename = "as_history";

				// 자산의 정보(리스트) 가져오기
				reqList = assetModuleDAO.getInfoList(tablename,mode,login_id,searchscope,page,as_no,"lending",login_division);
				// paging
				amLinkTable = amLinkBO.getRedirect(tablename,mode,login_id,searchscope,page,as_no,"lending",ct_id,login_division);
				
				request.setAttribute("assetReqList1",reqList);
				request.setAttribute("Redirect",amLinkTable);
				getServletContext().getRequestDispatcher("/am/admin/req_app_lending.jsp").forward(request,response);
				
			/////////////////////////////////////////////////
			//	[사용자 (자산 신청 입/출 관리자) 모드]
			//	이관/반출/대여 신청 Form으로 이동  
			//  1. 자산정보 가져오기 
			//  2. 자산이력 정보 가져오기
			//  3. 카테고리 정보
			//  4. 신청폼으로 이동
			/////////////////////////////////////////////////
			}	else if("out_InputForm".equals(mode)){
					
				com.anbtech.am.entity.AMUserTable amuserTable	= new com.anbtech.am.entity.AMUserTable();
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsInfoTable asInfoTable	= new com.anbtech.am.entity.AsInfoTable();
				com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
				com.anbtech.am.admin.makeCtTree makeCtTree		= new com.anbtech.am.admin.makeCtTree();
				String sb = "";
				
				// 사용자 정보 setting해서 넘기기 (? servlet 에서 jsp로 넘어갈때 한글이 깨져서....)
				amuserTable.setUserId(login_id);
				amuserTable.setUserName(login_name);
				amuserTable.setUserRank(login_division);

				asInfoTable		= assetModuleDAO.getInfo(as_no);		// 자산 정보 가져오기 
				asHistoryTable	= assetModuleDAO.getHistory(h_no);		// 이력정보가져오기
				sb = makeCtTree.viewCategory(c_no,"");					// 카테고리 정보
				
				request.setAttribute("user",amuserTable);
				request.setAttribute("assetInfo",asInfoTable);
				request.setAttribute("historyInfo",asHistoryTable);
				request.setAttribute("CategoryList", sb);
				getServletContext().getRequestDispatcher("/am/admin/asEntering.jsp?as_no="+as_no+"&div="+div+"&as_status="+as_status).forward(request,response);	

			///////////////////////////////////////////////////////
			//	[사용자 모드] - 반출/대여 입고 정보 보기 (popup window)
			//  1. 자산정보 가져오기
			//  2. 이력정보 가져오기
			//  3. 카테고리 정보 가져오기
			///////////////////////////////////////////////////////
			} else if ("entering_info".equals(mode)) {

				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsInfoTable asInfoTable	= new com.anbtech.am.entity.AsInfoTable();
				com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
				com.anbtech.am.admin.makeCtTree makeCtTree		= new com.anbtech.am.admin.makeCtTree();
				String sb = "";
				
				asInfoTable = assetModuleDAO.getInfo(as_no);		// 자산 정보 가져오기 
				asHistoryTable = assetModuleDAO.getHistory(h_no);	// 이력정보가져오기
				sb = makeCtTree.viewCategory(c_no,"");
				
				request.setAttribute("assetInfo",asInfoTable);
				request.setAttribute("historyInfo",asHistoryTable);
				request.setAttribute("CategoryList", sb);
				
				getServletContext().getRequestDispatcher("/am/as_user/entering_info.jsp?as_status="+as_status).forward(request,response);					
				
			//////////////////////////////////////////////////////////////////////
			//  [사용자 (자산 신청 입/출 관리자) 모드]
			//  #. 입고/반납(div:lending) 업무처리건list          mode : EnteringList 
			//  #. 이관/반출/대여(div:lending) 업무 처리건 list    mode : TransOutList  
			//////////////////////////////////////////////////////////////////////
			}   else if("EnteringList".equals(mode) || "TransOutList".equals(mode)){
					
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AMLinkBO amLinkBO = new com.anbtech.am.business.AMLinkBO(con);
				com.anbtech.am.entity.AMLinkTable amLinkTable = new com.anbtech.am.entity.AMLinkTable();
				tablename = "as_history";
				ArrayList reqList = new ArrayList();
				
				//-- 선택된 자산의 이력 정보(리스트) 가져오기
				//   div(lending) :대여업무처리 리스트
				//       else     :반출/이관 업무 처리 리스트
				if("lending".equals(div)) {  
					reqList = assetModuleDAO.getInfoList(tablename,mode,login_id,searchscope,page,as_no,"lending",login_division);
				} else {	
					reqList = assetModuleDAO.getInfoList(tablename,mode,login_id,searchscope,page,as_no,"",login_division);
				}
				request.setAttribute("assetReqList1",reqList);
									
				// paging
				amLinkTable = amLinkBO.getRedirect(tablename,mode,login_id,c_no,page,"9",div,ct_id,login_division);
				request.setAttribute("Redirect",amLinkTable);
				
				if("lending".equals(div)){
					getServletContext().getRequestDispatcher("/am/admin/req_lending_process.jsp?mode="+mode+"&c_no="+c_no).forward(request,response);	
				} else {																				
					getServletContext().getRequestDispatcher("/am/admin/req_app_process.jsp?mode="+mode+"&c_no="+c_no).forward(request,response);
				}
					
			/////////////////////////////////////////////////////////////////////////////////
			//	[자산 관리자 모드]	
			//	
			//	1. 이관 가능여부 판단
			//     - 신청일자 확인	  : 예약된 날짜가 맞는지 확인
			//     - 현재 자산상태 확인 : 현 자산이 반출중/대여/수리/폐기등을 확인
			//     - 중복 날짜 확인	  : 현재 예약이 다른신청건과 중복되어있지는 않은지 확인
			//
			//	2. 이관 가능하면 처리
			//		 이관처리 : as_info TABLE 의 crr_id(관리자)를 u_id(이관자)로 변경
			//                 as_history TABLE 의 as_status(자산 상태코드) 를 '11'(이관)으로 변경
			//	3. 이동
			//     이력리스트화면에서 처리되었을때 
			//     (eachHistory.jsp)
			//	   이관/반출/대여업무 화면 리스트에서 처리되었을때
			//	   (req_app.jsp,req_app_process,req_app_lending,req_lending_process)
			//     각각 처리된 폼으로 이동
			//
			///////////////////////////////////////////////////////////////////////////////////
			}	else if("transfer_process".equals(mode)){
					
					com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					com.anbtech.am.business.AssetModuleBO asBO	= new com.anbtech.am.business.AssetModuleBO(con);
									
					String enable  = "";

					// 이관 가능 여부 판단
					enable = asBO.transEnable(udate,as_no,o_status);

					if(enable.equals("enable")) {
						// 이관처리	
						assetModuleDAO.asManagerProcess(u_id,as_no,h_no,o_status,as_status,mode);
					} else {
					PrintWriter out7 = response.getWriter();
						out7.println("	<script>");
						out7.println("  alert('"+enable+"')");
						out7.println("	history.back();");
						out7.println("	</script>");
						out7.close();
					}
					
					// 관리자가 자산 업무 처리를 했을경우(반출/대여/이관/반납/입고) 업무 화면에 따른 분기
					if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
						response.sendRedirect("AssetServlet?mode="+mode_temp);
					} else {	
						response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
					}
						

			/////////////////////////////////////////////////
			//	[자산관리자 모드]  
			//	이관 취소 
			//  1. as_history Table에 as_status를 '9'(취소)로 변환
			//  2.  각각 처리된 폼으로 이동
			/////////////////////////////////////////////////
			}	else if("cancel_transfer".equals(mode)){
			
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				// 이관취소처리
				assetModuleDAO.asManagerProcess(u_id,as_no,h_no,o_status,as_status,mode);
				
				// 관리자가 자산 업무 처리를 했을경우 - 업무 화면에 따른 분기
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp);
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}
					
				
			/////////////////////////////////////////////////////
			// [ 자산관리자 모드 ]
			// 반출 처리 
			// 1. as_history TABLE의 as_status를 '7'로 변환(반출중)  
			// 2.  각각 처리된 폼으로 이동
			/////////////////////////////////////////////////////
			}	else if("out_process".equals(mode)){
					
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				assetModuleDAO.asManagerProcess(u_id,as_no,h_no,o_status,as_status,mode);
									
				// 관리자가 자산 업무 처리를 했을경우 - 업무 화면에 따른 분기
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp);
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}
									
			/////////////////////////////////////////////////
			//  [ 자산관리자 모드 ]
			//  반출 취소 
			//	1. as_history의 as_status를 '9'로 변환(취소)
			//  2.  각각 처리된 폼으로 이동 
			/////////////////////////////////////////////////
			}	else if("cancel_out".equals(mode)){
					
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				assetModuleDAO.asManagerProcess(u_id,as_no,h_no,o_status,as_status,mode);
			
				// 관리자가 자산 업무 처리를 했을경우 - 업무 화면에 따른 분기
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp);
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}
		
			//////////////////////////////////////////////////////////////////
			//
			//  [ 자산관리자 모드 ]@
			//  ** 입고 처리  **
			//	1. - as_history의 as_status를 '12'로 변환(입고).
			//     - 현재 자산 사용자(as_info TABLE의 u_id)를 
			//       관리자 이름(as_info TABLE의 crr_id)으로 변경(반납)한다.
			//     
			//  2. - 입고시 자산수리 요청이 있으면,
			//       as_history & as_info TABLE의 as_status를 '13'으로 변경
			//     - 현재 사용자 정보를 관리자 정보로 변경(반납)한다.
			//
			///////////////////////////////////////////////////////////////////
			}	else if("out_Input".equals(mode)){
					
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				assetModuleDAO.asInputProcess(h_no,as_status,o_status,as_statusinfo,as_no, in_date,wi_date);
				
				// 관리자가 자산 업무 처리를 했을경우 - 업무 화면에 따른 분기
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp);
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}
			
			//////////////////////////////////////////////////
			//  [ 자산 관리자 모드 ]
			//
			//  ** 대여 처리 **
			//	- 현재 사용자 정보를 현재 자산 관리자 정보로 반납
			//  - as_status를 '16'(대여중)로 변환
			//
			//////////////////////////////////////////////////
			}	else if("lending_process".equals(mode)){
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				assetModuleDAO.asManagerProcess(u_id,as_no,h_no,o_status,as_status,mode);
				
				// 관리자가 자산 업무 처리를 했을경우 - 업무 화면에 따른 분기
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp+"&div=lending");
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}
				
			//////////////////////////////////////////////////
			//	[ 자산 관리자 모드 ] 
			//
			//	** 대여 반납 **
			//  - 현재 사용자 정보를 현재 자산 관리자 정보로 반납
			//  - as_status를 '17'(반납완료)로 변환
			//////////////////////////////////////////////////
			}   else if("lending_input".equals(mode)){
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				assetModuleDAO.asInputProcess(h_no,as_status,o_status,as_statusinfo,as_no, in_date,wi_date);
				
				// 관리자가 자산 업무 처리를 했을경우 - 업무 화면에 따른 분기
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp+"&div=lending");
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}				
				
			//////////////////////////////////////////////////
			//  [ 자산 관리자 모드 ]
			//
			//  ** 대여 취소 **
			//  - as_history의 as_status를 9로 변환(취소)
			//////////////////////////////////////////////////
			}  else if("cancel_lending".equals(mode)){
					
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				assetModuleDAO.asManagerProcess(u_id,as_no,h_no,o_status,as_status,mode);
			
				// 관리자가 자산 업무 처리를 했을경우 - 업무 화면에 따른 분기
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp+"&div=lending");
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}
							
			/////////////////////////////////////////////////
			//  ** 전자 결재용 보기 화면 **
			//  1. 자산정보 가져오기
			//  2. 이력정보 가져오기
			/////////////////////////////////////////////////
			}	else if("app_asset_view".equals(mode)){
						
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
				com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
				
				// 자산정보 가져오기
				asInfoTable = assetModuleDAO.getInfo(as_no);
				// history 정보 가져오기 
				asHistoryTable = assetModuleDAO.getHistory(h_no);

				request.setAttribute("assetInfo",asInfoTable);
				request.setAttribute("historyInfo",asHistoryTable);
				
				getServletContext().getRequestDispatcher("/am/as_user/appAsView.jsp?as_no="+as_no).forward(request,response);	
				
			/////////////////////////////////////////////////
			//  ** 전자 결재용 프린트 화면 **
			//  1. 자산정보 가져오기
			//  2. 이력정보 가져오기
			//  3. 결재정보(결재번호) 가져오기
			/////////////////////////////////////////////////
			}	else if("AppViewPrint".equals(mode)){
									
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO		= new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsInfoTable asInfoTable		= new com.anbtech.am.entity.AsInfoTable();
				com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
				com.anbtech.am.entity.AsApprovalInfoTable app		= new com.anbtech.am.entity.AsApprovalInfoTable();
				com.anbtech.am.entity.AsApprovalInfoTable app2		= new com.anbtech.am.entity.AsApprovalInfoTable();

				String sign_path = "../gw/approval/sign/";
				
				// 자산 정보 가져오기 
				asInfoTable = assetModuleDAO.getInfo(as_no);

				// history 정보 가져오기
				asHistoryTable = assetModuleDAO.getHistory(h_no);
				
				// 전자 결재 정보 가져오기
				pid = (String)assetModuleDAO.getPid(h_no);
				pid2 = (String)assetModuleDAO.getPid2(h_no);

				//1차 결재정보 가져오기
				app = assetModuleDAO.getApprovalInfo(pid,sign_path);
				
				//2차 결재정보 가져오기
				app2 = assetModuleDAO.getApprovalInfo(pid2,sign_path);

				request.setAttribute("assetInfo",asInfoTable);
				request.setAttribute("historyInfo",asHistoryTable);
				request.setAttribute("appInfo",app);
				request.setAttribute("appInfo2",app2);
							
				getServletContext().getRequestDispatcher("/am/as_user/appViewPrint.jsp?as_no="+as_no+"&pid="+pid+"&pid2="+pid2).forward(request,response);	
								
				}				
		} catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		} finally {
			close(con);
		}
	} // doGet()


	/**********************************
	 * post방식으로 넘어왔을 때 처리
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		//업로드할 때 필요한 것들 tablename,upload_size, filepath 선언
		String tablename = request.getParameter("tablename");
		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		//String filepath = getServletContext().getRealPath("") + "/upload/am/";
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/am/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		// 오늘 날짜
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
		String today	= anbdt.getDateNoformat();		// oooo**@@
		String thisyear = today.substring(0,4);			// 년도 0000

		String mode = multi.getParameter("mode"); 
		String div	= multi.getParameter("div");		// 카테고리(/자산) 입력/수정/삭제
		String redirectUrl = "";						

		////- 카테고리정보 관려 변수 - ////
		String c_no			= multi.getParameter("c_no")==null?
							  "0":multi.getParameter("c_no");	//카테고리 관리번호
		String ct_id		= multi.getParameter("ct_id")==null?
							  "":multi.getParameter("ct_id");	// 카테고리 ID
		String ct_level		= multi.getParameter("ct_level");	// 카테고리 LEVEL
		String ct_parent	= multi.getParameter("ct_parent");	// 부모 카테고리
		String ct_word		= multi.getParameter("ct_word");	// 카테고리 약자
		String ct_name		= multi.getParameter("ct_name");	// 카테고리 이름
		String apply_dc		= multi.getParameter("apply_dc");

		////- 자산이력정보 관련 변수 - ////
		String  as_no		= multi.getParameter("as_no")==null?
							  "":multi.getParameter("as_no");	// 자산이력 관리번호
		String  as_mid		= multi.getParameter("as_mid")==null?
							  "":multi.getParameter("as_no");	// 자산번호 
							  // (예:SL-"+카테고리약자+등록일자(월)+"-"+serial_no)
		String  as_item_no	= multi.getParameter("as_item_no")==null?
							  "":multi.getParameter("as_no");	      // 자산 품목 수량 번호
		String  b_id		= multi.getParameter("b_id")==null?
							  "":multi.getParameter("b_id");		  // 구매자ID	
		String  b_name		= multi.getParameter("b_name")==null?
							  "":multi.getParameter("b_name");		  // 구매자 이름
		String  b_rank		= multi.getParameter("b_rank")==null?
							  "":multi.getParameter("b_rank");		  // 구매자 부서명
		String  w_id		= multi.getParameter("w_id")==null?
							  "":multi.getParameter("w_id");		  // 작성자 ID
		String  w_name		= multi.getParameter("w_name")==null?
							  "":multi.getParameter("w_name");		  // 작성자 이름
		String  w_rank		= multi.getParameter("w_rank")==null?
							  "":multi.getParameter("w_rank");		  // 작성자 부서명
		String  as_name		= multi.getParameter("as_name")==null?
							  "":multi.getParameter("as_name");		  // 품목명(최종카테고리 명)
		String  model_name	= multi.getParameter("model_name")==null?
							  "":multi.getParameter("model_name");	  // 모델명
		String  as_serial	= multi.getParameter("as_serial")==null?
							  "":multi.getParameter("as_serial");	  // 자산 고유 serial number
		String  buy_date	= multi.getParameter("buy_date")==null?
							  "":multi.getParameter("buy_date");	  // 구매일자
		String  as_price	= multi.getParameter("as_price")==null?
							  "0":multi.getParameter("as_price");	  // 자산 구입 가격
		String  dc_count	= multi.getParameter("dc_count")==null?
							  "0":multi.getParameter("dc_count");		// 감가 적용횟수
		//String  dc_bound	= multi.getParameter("dc_bound")==null?
		//					  "0":multi.getParameter("dc_bound");		// 
		String  as_each_dc	= multi.getParameter("as_each_dc")==null?
							  "0":multi.getParameter("as_each_dc");		// 감가 각각 적용 비율
		String  dc_percent	= multi.getParameter("dc_percent")==null?
							  "0":multi.getParameter("dc_percent");		// (카테고리)감가 적용 비율
		String  as_value	= multi.getParameter("as_value")==null?
							  "1000":multi.getParameter("as_value");	// 자산 가치
		String  crr_id		= multi.getParameter("crr_id")==null?
							  "":multi.getParameter("crr_id");			// 자산 책임자 ID
		String  crr_name	= multi.getParameter("crr_name")==null?
							  "":multi.getParameter("crr_name");		// 자산 책임자 이름		
		String  crr_rank	= multi.getParameter("crr_rank")==null?
							  "":multi.getParameter("crr_rank");		// 자산 책임자 부서
		String  buy_where	= multi.getParameter("buy_where")==null?
							  "":multi.getParameter("buy_where");		// 자산 구입처
		String  as_maker	= multi.getParameter("as_maker")==null?
							  "":multi.getParameter("as_maker");		// 자산 메이커
		String  as_setting	= multi.getParameter("as_setting")==null?
							  "":multi.getParameter("as_setting");		// 자산 규격/ 사양
		String  bw_tel		= multi.getParameter("bw_tel")==null?
							  "":multi.getParameter("bw_tel");			// 구매처 전화번호
		String  bw_address	= multi.getParameter("bw_address")==null?
							  "":multi.getParameter("bw_address");		// 구매처 주소
		String  bw_employee	= multi.getParameter("bw_employee")==null?
							  "":multi.getParameter("bw_employee");		// 판매 담당자 이름
		String  bw_mgr_tel	= multi.getParameter("bw_mgr_tel")==null?
							  "":multi.getParameter("bw_mgr_tel");		// 판매 담당자 전화번호
		String  etc			= multi.getParameter("etc")==null?
							  "":multi.getParameter("etc");				// 기타/특이사항 정보
		String  as_status	= multi.getParameter("as_status")==null?
							  "":multi.getParameter("as_status");		// 자산 상태 코드
		String  as_except_day= multi.getParameter("as_except_day");		// 폐기 일자
		String  apply_dcdate = multi.getParameter("apply_dcdate");		// 감가 적용 일자									
		String  handle		= multi.getParameter("handle");				// 반출 가능 여부
		String  del_form	= multi.getParameter("del_form");			// 폐기 형태
		String  del_reason	= multi.getParameter("del_reason");			// 폐기 근거
		String  as_except_reason = multi.getParameter("as_except_reason")==null?
							"":multi.getParameter("as_except_reason"); // 폐기사유
		String  file_se= multi.getParameter("file_se");					// 첨부화일 개수
		String  file_name= multi.getParameter("file_name");				// 파일이름 string
		String  file_type= multi.getParameter("file_type");				// 파일타입 
		String  file_size= multi.getParameter("file_size");				// 파일사이즈( 234|23423|....)
		String  file_umask= multi.getParameter("file_umask");			// Renamed 파일
		String  file_path= multi.getParameter("file_path");				// file 경로
		
		//현재 접속중인 사용자 아이디 가져오기
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
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/////////////////////////////////////////////////////
			// 카테고리 관리
			// 1. 최초 감가적용 비율이 없으면, 적용비율은 '0'
			// 2. 카테고리 처리 구분자(div: a-추가, f-최초등록, m-수정)
			//
			/////////////////////////////////////////////////////
			if("category_manage".equals(mode)){
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
				com.anbtech.am.entity.AsCategoryTable asCategoryTable = new com.anbtech.am.entity.AsCategoryTable();
							
				if(dc_percent.equals("")) dc_percent="0"; // 감가 적용 비율 (감가적용 비율이 없으면 '0' 으로 setting)

				// 카테고리 수정/추가/등록
				if(div.equals("a") || div.equals("f") || div.equals("m") ){		
					assetModuleBO.setCtBusiness(div, c_no, ct_id, ct_level, ct_parent, ct_word, ct_name, dc_percent, apply_dc);
				//카테고리 삭제
				} else if(div.equals("d")){					
						String msg = assetModuleDAO.delete_ct(c_no,ct_id);
						PrintWriter out2 = response.getWriter();
						out2.println("	<script>");
						out2.println("	alert('"+msg+"');");
						out2.println("	location.href='../servlet/AssetServlet?mode=category_list'");
						out2.println("	</script>");
						out2.close();
				}
				response.sendRedirect("AssetServlet?mode=category_list");
			}

			/////////////////////////////////////////////////////////////////////////
			//	[ 관리자 모드  ]
			//  자산관리 (처리 구분자 div : input-등록, modify-수정, delete_process-삭제)
			//   
			//	1. 자산번호 가져오기
			//  2. 자산수량 확인
			//  3. 품명가져오기
			//  4. 자산 가치 
			//  5. 자산 등록 가능여부 확인
			//  6. 정보 등록
			//  
			/////////////////////////////////////////////////////////////////////////
			else if("asset_manager".equals(mode)){				
			
			con.setAutoCommit(false);	// 트랜잭션을 시작
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			try
			{
				if("input".equals(div)){
					
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
				com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
				com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
				com.anbtech.am.entity.AsInfoTable file = new com.anbtech.am.entity.AsInfoTable();

				tablename = "as_info";
				
				as_mid = assetModuleBO.getAsMid(c_no);	// 1. 카테고리에 해당하는 자산번호 가져오기
				int j = as_mid.length();				//    자산번호 길이
				as_item_no = as_mid.substring(j-3,j);	// 2. 자산번호 수량 정보 가져오기(해당 카테고리의 순번)
				as_name = (String)assetModuleDAO.getAsName(c_no);	// 3. 품명 가져오기
										
				// 사용자(구매자) 정보 setting 
				b_id		= (String)assetModuleBO.getUserInfo(b_name);
				user_info	= (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(b_id);
				b_name		= user_info.getUserName();
				b_rank		= user_info.getDivision();
				b_name		= b_id+"/"+b_name;

				// 자산 책임자 정보 setting
				crr_id		= assetModuleBO.getUserInfo(crr_name);
				user_info	= userinfoDAO.getUserListById(crr_id);
				crr_name	= user_info.getUserName();
				crr_rank	= user_info.getDivision();
				crr_name	= crr_id+"/"+crr_name;
				
				as_value = as_price; // 4. 최초 자산 값(구매 값)은 바로 자산 가치
				
				// 5. 현재 자산 등록 가능여부 판단 즉, 최하위 카테고리 항목에서만 등록 가능
				if(assetModuleDAO.assetChk(c_no)) {
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('하위 카테고리가 존재합니다.');");
					out.println("	history.back();");
					out.println("	</script>");
					out.close();
				} else {
			
				ct_id = assetModuleDAO.getCtId(c_no);// as_category에서 ct_id가져오기
				
				// 자산정보 등록하기
				assetModuleDAO.saveAssetInfo(as_mid, c_no,  as_item_no,  w_id,  w_name,  w_rank, b_id, b_name,b_rank,model_name, as_name,as_serial, buy_date, as_price, dc_count,  as_each_dc, as_value, crr_id,crr_name, crr_rank, buy_where, as_maker, as_setting, bw_tel, bw_address, bw_employee, bw_mgr_tel, etc,handle,ct_id);
				as_no = assetModuleDAO.getId(tablename,as_mid);
			
				// 파일정보 가져오기
				file = (com.anbtech.am.entity.AsInfoTable)assetModuleBO.getFile_frommulti(multi, as_no, filepath);
				
				// 파일정보 저장하기
				assetModuleBO.updFile(tablename, as_no, file.getFileName(), file.getFileType(), file.getFileSize(), file.getFileSe(),file.getFileUmask(),file.getFilePath());
				}
				
			/////////////////////////////////////////////
			//	[ 관리자 설정 ]	
			//	자산 정보 수정
			//  1. 감가책정
			//  2. 금액에 콤마넣기
			//  3. 카테고리 변경에 따른 자산번호 생성 
			//  4. 자산 등록 가능여부 판단
			//     불가 - 이전으로 이동
			//     가능 - 정보 수정
			/////////////////////////////////////////////
			}	else if("modify".equals(div)){
			
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
				com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
				com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
				
				tablename = "as_info";
				
				// 1. 감가적용 횟수가 '0'이면 자산 구입가를 자산 가치책정
				if(dc_count.equals("0")) { as_value = as_price; }	
				
				// 2. 금액에 천단위로 ','넣기
				as_value = assetModuleBO.getStringWon(as_value);	

				String temp_cno =  assetModuleDAO.getCno(as_no);	// c_no 값 가져오기

				// 3. 카테고리 변경에따른 자산번호 넣기
				//	  - 카테고리가 변경되지 않았을 경우 기존의 자산번호를 사용한다. 
				//    - 변경되었을경우에는 새로운 자산번호를 생성한다.
				if (temp_cno.equals(c_no))	{
					as_mid = assetModuleDAO.getInfoAsMid(as_no);	
				} else {
					as_mid = assetModuleBO.getAsMid(c_no);
				}
				int j = as_mid.length();
				as_item_no = as_mid.substring(j-3,j);
				as_name = (String)assetModuleDAO.getAsName(c_no);

				// 사용자 정보 가져오기 
				b_id	= (String)assetModuleBO.getUserInfo(b_name);
				user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(b_id);
				b_name = user_info.getUserName();
				b_rank = user_info.getDivision();
				b_name = b_id+"/"+b_name;

				crr_id = assetModuleBO.getUserInfo(crr_name);
				user_info = userinfoDAO.getUserListById(crr_id);
				crr_name = user_info.getUserName();
				crr_rank = user_info.getDivision();
				crr_name = crr_id+"/"+crr_name;
				
				// 4. 현재 자산 등록 가능여부 판단 즉, 최하위 카테고리 항목에서만 수정/등록 가능
				if(assetModuleDAO.assetChk(c_no)) {
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('하위 카테고리가 존재합니다.');");
					out.println("	history.back();");
					out.println("	</script>");
					out.close();
				} else {
					ct_id = assetModuleDAO.getCtId(c_no);// as_category에서 ct_id가져오기
										
					// 자산 정보 수정
					assetModuleDAO.modifyAssetInfo(as_no,as_mid, c_no,as_item_no,w_id,w_name,w_rank,b_id,b_name,b_rank,model_name, as_name,as_serial, buy_date, as_price, dc_count, as_each_dc, as_value, crr_id,crr_name, crr_rank, buy_where, as_maker, as_setting, bw_tel, bw_address, bw_employee, bw_mgr_tel, etc,handle,ct_id,as_status);

					// 첨부화일정보 가져오기
					ArrayList file_list = assetModuleDAO.getFileList(as_no);

					com.anbtech.am.entity.AsInfoTable file = new com.anbtech.am.entity.AsInfoTable();
					file = (com.anbtech.am.entity.AsInfoTable)assetModuleBO.getFile_frommulti(multi, as_no, filepath, file_list);

					// 첨부파일 정보 업데이트				
					assetModuleBO.updFile(tablename, as_no, file.getFileName(), file.getFileType(), file.getFileSize(), file.getFileSe(),file.getFileUmask(),file.getFilePath());					
					}
			
				/////////////////////////////////////////////
				//	[ 관리자 모드]	
				//	자산 폐기 정보 저장
				//  - as_info의 as_status를 '10'으로 변경
				/////////////////////////////////////////////
				}	else if("delete_process".equals(div)){
				
					com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					assetModuleDAO.asDelete(as_no,as_except_day,as_except_reason,del_form,del_reason);
				}

				response.sendRedirect("AssetServlet?mode=asset_list");
				con.commit(); // commit한다.
			
			} catch(Exception e) {
				con.rollback();
				request.setAttribute("ERR_MSG",e.toString());
				getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);		
			} finally {
				con.setAutoCommit(true);
			}
		}

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} //doPost()
}