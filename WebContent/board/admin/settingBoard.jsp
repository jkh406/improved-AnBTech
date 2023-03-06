<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkBD01.jsp"%>

<%@ page language="java"
	contentType="text/html;charset=euc-kr" 
	errorPage = "../../admin/errorpage.jsp" 
%>
<%@ page import="java.sql.*"%>

<%!
	com.anbtech.dbconn.DBConnectionManager connMgr;
	Connection con;
	Statement stmt;
	Statement stmt_skin;
	ResultSet rs;
%>

<%	
	String owners_string;
	String[] itemList,mString;
	String[] manager_list;
	String last="0";
	String str;

	String tablename = request.getParameter("tablename");
	String mode = request.getParameter("mode")==null?"save":request.getParameter("mode");

	String boardpath = getServletContext().getRealPath("") + com.anbtech.admin.db.ServerConfig.getConf("boardpath");

	try {
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	
		String boardstart = com.anbtech.admin.db.ServerConfig.getConf("serverURL")+"/servlet/AnBBoard?tablename="+tablename;
	
		//카테고리 등록
		if ("add".equals(mode)){
			String[] ctegory_token;
			String category_add = new String(request.getParameter("category_add"));
			category_add = com.anbtech.text.StringProcess.kwordProcess(category_add); //category_add한글처리
			
			if(category_add.trim() != "추가할 카테고리" && category_add.trim() != ""){
				String query = "select category_items from board_env where tablename='" + tablename + "'";
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				rs.next();
				String category_items = rs.getString("category_items");

				java.util.StringTokenizer categoryToken=new java.util.StringTokenizer(category_items,"|");
				int tokenNum=categoryToken.countTokens();
				int cnum=0;
				ctegory_token=new String[tokenNum];

				while(categoryToken.hasMoreElements()){
					ctegory_token[cnum]=categoryToken.nextToken();

					if(ctegory_token[cnum].equals(category_add)){
						category_add="";
					%>
						<script>
							alert("동일한 항목이 존재합니다.");
						</script>
					<%
					}

					cnum++;
				}

				stmt.close();
				rs.close();

				if(!category_add.equals("")) category_items = category_items + "|" + category_add;
				query = "update board_env set category_items='"+category_items+"' where tablename='" + tablename + "'";
				stmt = con.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
			}

		//카테고리 삭제
		}else if("remove".equals(mode)){
			String category_remove = new String(request.getParameter("category_remove"));
			category_remove = com.anbtech.text.StringProcess.kwordProcess(category_remove);//category_remove한글처리
			if(category_remove != null){
				String query = "select category_items from board_env where tablename='" + tablename + "'";
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				rs.next();
				String category_items = rs.getString("category_items");

				stmt.close();
				rs.close();

				java.util.StringTokenizer items = new java.util.StringTokenizer(category_items, "|");
				category_items = "";
				while(items.hasMoreElements()){
					String item = items.nextToken();
					if(!category_remove.equals(item)) category_items = category_items + item + "|";
				}

				query = "update board_env set category_items='"+category_items+"' where tablename='" + tablename + "'";
				stmt = con.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
			}

		//게시판 관리자 추가
		}else if("UserAdd".equals(mode)) {
			String manager_add = new String(request.getParameter("manager_add"));
			manager_add = com.anbtech.text.StringProcess.kwordProcess(manager_add);//category_remove한글처
						
					str = "SELECT id FROM user_table WHERE id='"+manager_add+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(str);
											
					if(rs.next()) { // 사용자 ID 가 존재할경우....

						String[] mname;           // 분리된 토큰 저변수
						String   mname_temp="";   // owners_id 저장 변수
						String   mname_temp2="";  // 저장할 owners_id의 저장 변수
						
						// 관리자 리스트 String
						String str2 = "SELECT owners_id FROM board_env WHERE tablename='"+tablename+"'";
							stmt = con.createStatement();
							ResultSet rs2 = stmt.executeQuery(str2);
						rs2.next();
						mname_temp = rs2.getString("owners_id");
					
						java.util.StringTokenizer mToken = new java.util.StringTokenizer(mname_temp, "|");
						
						int tokennum=mToken.countTokens(); // 토큰 수
						int no=0;				
						mname=new String[tokennum];

						while(mToken.hasMoreElements()){
							mname[no]=mToken.nextToken();
							if(mname[no].equals(manager_add)) manager_add=""; // if..관리자 ID 중복 문제처리	
							mname[no]=mname[no]+"|";
							mname_temp2=mname_temp2+mname[no];
							no++;
						}
						
						// 중복이 되지 않았다면, owners_id 에 ID 추가 
						if(manager_add.equals("")) {
							%>
							<script>
							alert("관리자 ID가 이미 존재합니다.");
							</script>
							<%
						}else {	
						mname_temp2 = mname_temp2 + manager_add + "|";
						}
																												
						str = "UPDATE board_env SET owners_id='"+mname_temp2+"' WHERE tablename='"+tablename+"'";
						stmt = con.createStatement();
						stmt.executeUpdate(str);
						
						rs2.close();
						stmt.close();
					} else
					{
					//사용자 ID가 존재하지 않을경우...
%>					<script>
						alert("사용자 ID가 없습니다.");
						manager_remove.focus();

					</script>
<%
		
					}
						rs.close();
						stmt.close();

		//게시판 관리자 삭제
		}else if("UserDel".equals(mode)){
				
			int tno=0;
			String[] mRemove_temp; // 분리된 토큰 저장 변수
			
			String manager_remove = new String(request.getParameter("manager_remove"));
			manager_remove = com.anbtech.text.StringProcess.kwordProcess(manager_remove);//category_remove한글처리


			if(manager_remove != null){

				java.util.StringTokenizer man_item = new java.util.StringTokenizer(manager_remove, "/");
				
				int p=man_item.countTokens();
				mRemove_temp=new String[p];
				while(man_item.hasMoreElements()){
					mRemove_temp[tno]=man_item.nextToken();
					tno++;
				}
				
				str = "SELECT owners_id FROM board_env WHERE tablename='"+tablename+"'";
				stmt = con.createStatement();
				rs=stmt.executeQuery(str);
				rs.next();
				String owners_id = rs.getString("owners_id");

				java.util.StringTokenizer rev_items = new java.util.StringTokenizer(owners_id, "|");
				owners_id = "";
			
				while(rev_items.hasMoreElements()){
					String item = rev_items.nextToken();
					
					if(mRemove_temp[p-1].equals(item)) { 
							owners_id = owners_id +""; // owner ID 삭제처리
					} else 	{ 
							owners_id = owners_id + item + "|";
					}
				}
				str = "update board_env set owners_id='"+owners_id+"' where tablename='"+tablename+"'";
				stmt = con.createStatement();
				stmt.executeUpdate(str);
				rs.close();
				stmt.close();
			} 

		//설정 저장
		}else if("save".equals(mode)){
			String admin_id = request.getParameter("admin_id");
			String admin_pwd = request.getParameter("admin_pwd"); 

			String l_maxlist = request.getParameter("l_maxlist");
			String l_maxpage = request.getParameter("l_maxpage");
			String l_maxsubjectlen = request.getParameter("l_maxsubjectlen");

			String v_listmode = request.getParameter("v_listmode");
			String enablecomment = request.getParameter("enablecomment");

			String adminonly = request.getParameter("adminonly");

			String enableupload = request.getParameter("enableupload");
			String upload = request.getParameter("upload");
			String upload_size = request.getParameter("upload_size");

			String query = "" +
				"UPDATE board_env set admin_id=?,admin_pwd=?,l_maxlist=?,l_maxpage=?,l_maxsubjectlen=?,adminonly=?," +
				"enableupload=?,v_listmode=?,enablecomment=?," +
				"upload_size=? where tablename='" + tablename + "'";

			PreparedStatement pstmt = con.prepareStatement(query);

			if ("n".equals(enableupload)) upload = "0";
			pstmt.setString(1, admin_id);
			pstmt.setString(2, admin_pwd);
			pstmt.setInt(3, Integer.parseInt(l_maxlist));
			pstmt.setInt(4, Integer.parseInt(l_maxpage));
			pstmt.setInt(5, Integer.parseInt(l_maxsubjectlen));
			pstmt.setString(6, adminonly);
			pstmt.setInt(7, Integer.parseInt(upload));
			pstmt.setString(8, v_listmode);
			pstmt.setString(9, enablecomment);
			pstmt.setInt(10, Integer.parseInt(upload_size));
			
			pstmt.executeUpdate();
			pstmt.close();
		}  
		////////////////////////

		// 관리자 리스트 String
			str = "SELECT owners_id FROM board_env WHERE tablename='"+tablename+"'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(str);
			rs.next();
			owners_string = rs.getString("owners_id");
			rs.close();
			stmt.close();

		
			int i=0;
		    java.util.StringTokenizer itemToken = new java.util.StringTokenizer(owners_string, "|");
			int j=itemToken.countTokens();
			itemList = new String[j];	// 토큰저장 변수
			manager_list=new String[j]; // owner의 부서/직책/이름/사원번호 STRING 저장 변수
			
			while (itemToken.hasMoreElements())
			{
				itemList[i] = itemToken.nextToken();
		    
	           // user_table 에서 name,rank,ac_id SELECT
				String query1 = "SELECT  name,rank,ac_id FROM  user_table WHERE id='"+itemList[i]+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(query1);
					rs.next();

				String name=rs.getString("name");	// 사용자 이름
				String rank=rs.getString("rank");	// Rank ID
				String ac_id=rs.getString("ac_id"); // 

				// rank_table에서 ar_name SELECT
				query1 = "SELECT ar_name FROM rank_table WHERE ar_code='"+rank+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(query1);
					rs.next();
				String ar_name = rs.getString("ar_name");

				// class_table에서 ac_name SELECT
				query1 = "SELECT ac_name FROM class_table WHERE ac_id='"+ac_id+"'";
					stmt = con.createStatement();
					 rs = stmt.executeQuery(query1);
					rs.next();
				
				String ac_name = rs.getString("ac_name");

				manager_list[i]=ac_name+"/"+ar_name+"/"+name+"/"+itemList[i];

				rs.close();
				stmt.close();
			i++;
			}
		// 

		String query = "select * from board_env where tablename='" + tablename + "'";
		
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		rs.next();
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> 게시판환경설정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32>
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=300><a href="javascript:document.mbForm.submit();"><img src="../images/bt_save.gif" border="0" align="absmiddle"></a> <a href="javascript:history.go(-1);"><img src="../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD>
			  <TD width=4></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--내용-->
<FORM NAME='mbForm' ACTION='settingBoard.jsp' METHOD='post' STYLE="margin:0">
<input type=hidden name=tablename value='<%=rs.getString("tablename")%>'>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!-- 게시판 정보 -->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">게시판명</td>
           <td width="80%" height="25" class="bg_04">
			<%
			if(rs.getString("tablename").equals("notice_board")){
				out.print("공지 게시판");
			}else if(rs.getString("tablename").equals("free_board")){
				out.print("자유 게시판");
			}
			%>		   
		   </td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">게시판 URL</td>
           <td width="80%" height="25" class="bg_04"><%=boardstart%></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
<!--
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">관리자 아이디</td>
           <td width="80%" height="25" class="bg_04"><INPUT TYPE=text NAME='admin_id' SIZE=10 MAXLENGTH=10 VALUE='<%=rs.getString("admin_id")%>'> 해당 게시판을 관리할 사원의 사번을 입력하세요.</td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">관리자패스워드</td>
           <td width="80%" height="25" class="bg_04"><input type=text name='admin_pwd' size=10 maxlength=40 value='<%=rs.getString("admin_pwd")%>'></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
 -->
         <tr><td height=10 colspan="2"></td></tr></tbody></table></td></tr></table>

	<!-- 게시판 기능 설정 -->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">페이지당 게시물 수</td>
           <td width="80%" height="25" class="bg_04"><INPUT TYPE=text NAME='l_maxlist' SIZE=3 MAXLENGTH=3 VALUE='<%=rs.getInt("l_maxlist")%>'></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">화면당 페이지 수</td>
           <td width="80%" height="25" class="bg_04"><INPUT TYPE=text NAME='l_maxpage' SIZE=3 MAXLENGTH=3 VALUE='<%=rs.getInt("l_maxpage")%>'></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">글 제목의 최대 길이</td>
           <td width="80%" height="25" class="bg_04"><INPUT TYPE=text NAME='l_maxsubjectlen' SIZE=3 MAXLENGTH=3 VALUE='<%=rs.getInt("l_maxsubjectlen")%>'>
              글자 (리스트에서 보여지는 제목)</td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">글보기시 전체목록 보기</td>
           <td width="80%" height="25" class="bg_04">
 <%
			String chk_v_listmode_n = "";
			String chk_v_listmode_y = "";
			if ("y".equalsIgnoreCase(rs.getString("v_listmode")))
				chk_v_listmode_y = "checked";
			else
				chk_v_listmode_n = "checked";
%>
		   <input style=border:0 type=radio name='v_listmode' value='y' <%=chk_v_listmode_y%>>사용함 <input style=border:0 type=radio name='v_listmode' value='n' <%=chk_v_listmode_n%>>사용안함
		   </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">간단 의견 달기</td>
           <td width="80%" height="25" class="bg_04">
<%
			String chk_enablecomment_n = "";
			String chk_enablecomment_y = "";
			if ("y".equalsIgnoreCase(rs.getString("enablecomment")))
				chk_enablecomment_y = "checked";
			else
				chk_enablecomment_n = "checked";
%>
		   <INPUT STYLE=border:0 TYPE=radio NAME='enablecomment' VALUE='y' <%=chk_enablecomment_y%>>사용함 <INPUT STYLE=border:0 TYPE=radio NAME='enablecomment' VALUE='n' <%=chk_enablecomment_n%>>사용안함
		   </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">글쓰기 권한</td>
           <td width="80%" height="25" class="bg_04">
<%
			String chk_adminonly_n = "";
			String chk_adminonly_y = "";
			if ("y".equalsIgnoreCase(rs.getString("adminonly")))
				chk_adminonly_y = "checked";
			else
				chk_adminonly_n = "checked";
%>		   
		   <INPUT STYLE=border:0 TYPE=radio NAME='adminonly' VALUE='y' <%=chk_adminonly_y%>>관리자만 글쓰기 기능 <INPUT STYLE=border:0 TYPE=radio NAME='adminonly' VALUE='n' <%=chk_adminonly_n%>>사원모드 글쓰기 가능</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">파일 업로드 사용</td>
           <td width="80%" height="25" class="bg_04">
<%
			String chk_enableupload_n = "";
			String chk_enableupload_y = "";
			int upload = rs.getInt("enableupload");
			if (upload > 0)
				chk_enableupload_y = "checked";
			else
				chk_enableupload_n = "checked";
%>

			<input style=border:0 type=radio name='enableupload' value='y' <%=chk_enableupload_y%>>사용함 <input style=border:0 type=radio name='enableupload' value='n' <%=chk_enableupload_n%>>사용 안함</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">업로드 가능한 파일 수</td>
           <td width="80%" height="25" class="bg_04"><input type=text name='upload' size=5 maxlength=5 value='<%=upload%>'></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">업로드 파일의 최대 크기</td>
           <td width="80%" height="25" class="bg_04"><input type=text name='upload_size' size=5 maxlength=5 value='<%=rs.getInt("upload_size")%>'> Mbyte</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr><td height=10 colspan="2"></td></tr></tbody></table>
		 
	<!-- 카테고리 관리 -->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">카테고리 설정</td>
           <td width="80%" height="25" class="bg_04">
				<select name="category_remove">
					<option class=del selected>삭제할 카테고리를 선택하세요</option>
<%
					String category_items = rs.getString("category_items");
					java.util.StringTokenizer items = new java.util.StringTokenizer(category_items, "|");
					items.nextToken(); // admin SKIP
					if(rs.getString("tablename").equals("notice_board")){
						items.nextToken(); // 사내 공문 SKIP
						items.nextToken(); // 사외 공문 SKIP
					}
					while(items.hasMoreElements()){
						String item = items.nextToken();
%>
						<option value="<%=item%>"><%=item%></option>
<%					}
%>
                    </select>&nbsp;
				<input type=submit value='remove' name='mode' onClick="if(!category_remove.value){alert('삭제할 카테고리를 선택하세요');category_remove.focus();return false;}else return true;"><br>

				<input type=text name='category_add' size=16 maxlength=20 value='추가할 카테고리' onFocus="this.value='';return true;">&nbsp;
				<input type=submit value='add' name='mode' onClick="if(!category_add.value || category_add.value == '추가할 카테고리'){alert('카테고리를 입력하세요.');category_add.focus();return false;}else return true;">
		   </td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr><td height=10 colspan="2"></td></tr></tbody></table></td></tr></table>

	<!-- 관리자 관리 -->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">관리자 설정</td>
           <td width="80%" height="25" class="bg_04">
               <select name="manager_remove">
                 <option class=del selected>삭제할 관리자를 선택하세요</option>
					<%
						for(int m=0;m<manager_list.length;m++){
						%>
						<option value="<%=manager_list[m]%>"><%=manager_list[m]%></option>
			            <%}%>
                      </select>&nbsp;
               <input type=submit value='UserDel' name='mode' onClick="if(!manager_remove.value){alert('삭테할관리자를 선택하세요');manager_remove.focus();return false;}else return true;"><br>

               <input type=text name='manager_add' size=16 maxlength=20 value='추가할 관리자' onFocus="this.value='';return true;">&nbsp;
               <input type=submit value='UserAdd' name='mode' onClick="if(!manager_add.value || manager_add.value == '추가할 관리자'){alert('관리자를 입력하세요.');manager_add.focus();return false;}else return true;">
		   </td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr><td height=10 colspan="2"></td></tr></tbody></table></td></tr></table>		 
		 
</td></tr></table>

<%
				rs.close();
				connMgr.freeConnection("mssql", con);
			}catch (Exception e){
				connMgr.freeConnection("mssql", con);
				out.println(e.toString());
			}
%>	
</form>
</body></html>
