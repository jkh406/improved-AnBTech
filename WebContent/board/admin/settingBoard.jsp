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
	
		//ī�װ� ���
		if ("add".equals(mode)){
			String[] ctegory_token;
			String category_add = new String(request.getParameter("category_add"));
			category_add = com.anbtech.text.StringProcess.kwordProcess(category_add); //category_add�ѱ�ó��
			
			if(category_add.trim() != "�߰��� ī�װ�" && category_add.trim() != ""){
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
							alert("������ �׸��� �����մϴ�.");
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

		//ī�װ� ����
		}else if("remove".equals(mode)){
			String category_remove = new String(request.getParameter("category_remove"));
			category_remove = com.anbtech.text.StringProcess.kwordProcess(category_remove);//category_remove�ѱ�ó��
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

		//�Խ��� ������ �߰�
		}else if("UserAdd".equals(mode)) {
			String manager_add = new String(request.getParameter("manager_add"));
			manager_add = com.anbtech.text.StringProcess.kwordProcess(manager_add);//category_remove�ѱ�ó
						
					str = "SELECT id FROM user_table WHERE id='"+manager_add+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(str);
											
					if(rs.next()) { // ����� ID �� �����Ұ��....

						String[] mname;           // �и��� ��ū ������
						String   mname_temp="";   // owners_id ���� ����
						String   mname_temp2="";  // ������ owners_id�� ���� ����
						
						// ������ ����Ʈ String
						String str2 = "SELECT owners_id FROM board_env WHERE tablename='"+tablename+"'";
							stmt = con.createStatement();
							ResultSet rs2 = stmt.executeQuery(str2);
						rs2.next();
						mname_temp = rs2.getString("owners_id");
					
						java.util.StringTokenizer mToken = new java.util.StringTokenizer(mname_temp, "|");
						
						int tokennum=mToken.countTokens(); // ��ū ��
						int no=0;				
						mname=new String[tokennum];

						while(mToken.hasMoreElements()){
							mname[no]=mToken.nextToken();
							if(mname[no].equals(manager_add)) manager_add=""; // if..������ ID �ߺ� ����ó��	
							mname[no]=mname[no]+"|";
							mname_temp2=mname_temp2+mname[no];
							no++;
						}
						
						// �ߺ��� ���� �ʾҴٸ�, owners_id �� ID �߰� 
						if(manager_add.equals("")) {
							%>
							<script>
							alert("������ ID�� �̹� �����մϴ�.");
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
					//����� ID�� �������� �������...
%>					<script>
						alert("����� ID�� �����ϴ�.");
						manager_remove.focus();

					</script>
<%
		
					}
						rs.close();
						stmt.close();

		//�Խ��� ������ ����
		}else if("UserDel".equals(mode)){
				
			int tno=0;
			String[] mRemove_temp; // �и��� ��ū ���� ����
			
			String manager_remove = new String(request.getParameter("manager_remove"));
			manager_remove = com.anbtech.text.StringProcess.kwordProcess(manager_remove);//category_remove�ѱ�ó��


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
							owners_id = owners_id +""; // owner ID ����ó��
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

		//���� ����
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

		// ������ ����Ʈ String
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
			itemList = new String[j];	// ��ū���� ����
			manager_list=new String[j]; // owner�� �μ�/��å/�̸�/�����ȣ STRING ���� ����
			
			while (itemToken.hasMoreElements())
			{
				itemList[i] = itemToken.nextToken();
		    
	           // user_table ���� name,rank,ac_id SELECT
				String query1 = "SELECT  name,rank,ac_id FROM  user_table WHERE id='"+itemList[i]+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(query1);
					rs.next();

				String name=rs.getString("name");	// ����� �̸�
				String rank=rs.getString("rank");	// Rank ID
				String ac_id=rs.getString("ac_id"); // 

				// rank_table���� ar_name SELECT
				query1 = "SELECT ar_name FROM rank_table WHERE ar_code='"+rank+"'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(query1);
					rs.next();
				String ar_name = rs.getString("ar_name");

				// class_table���� ac_name SELECT
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
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> �Խ���ȯ�漳��</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32>
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=300><a href="javascript:document.mbForm.submit();"><img src="../images/bt_save.gif" border="0" align="absmiddle"></a> <a href="javascript:history.go(-1);"><img src="../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD>
			  <TD width=4></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--����-->
<FORM NAME='mbForm' ACTION='settingBoard.jsp' METHOD='post' STYLE="margin:0">
<input type=hidden name=tablename value='<%=rs.getString("tablename")%>'>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!-- �Խ��� ���� -->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�Խ��Ǹ�</td>
           <td width="80%" height="25" class="bg_04">
			<%
			if(rs.getString("tablename").equals("notice_board")){
				out.print("���� �Խ���");
			}else if(rs.getString("tablename").equals("free_board")){
				out.print("���� �Խ���");
			}
			%>		   
		   </td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�Խ��� URL</td>
           <td width="80%" height="25" class="bg_04"><%=boardstart%></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
<!--
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">������ ���̵�</td>
           <td width="80%" height="25" class="bg_04"><INPUT TYPE=text NAME='admin_id' SIZE=10 MAXLENGTH=10 VALUE='<%=rs.getString("admin_id")%>'> �ش� �Խ����� ������ ����� ����� �Է��ϼ���.</td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�������н�����</td>
           <td width="80%" height="25" class="bg_04"><input type=text name='admin_pwd' size=10 maxlength=40 value='<%=rs.getString("admin_pwd")%>'></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
 -->
         <tr><td height=10 colspan="2"></td></tr></tbody></table></td></tr></table>

	<!-- �Խ��� ��� ���� -->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�������� �Խù� ��</td>
           <td width="80%" height="25" class="bg_04"><INPUT TYPE=text NAME='l_maxlist' SIZE=3 MAXLENGTH=3 VALUE='<%=rs.getInt("l_maxlist")%>'></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">ȭ��� ������ ��</td>
           <td width="80%" height="25" class="bg_04"><INPUT TYPE=text NAME='l_maxpage' SIZE=3 MAXLENGTH=3 VALUE='<%=rs.getInt("l_maxpage")%>'></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�� ������ �ִ� ����</td>
           <td width="80%" height="25" class="bg_04"><INPUT TYPE=text NAME='l_maxsubjectlen' SIZE=3 MAXLENGTH=3 VALUE='<%=rs.getInt("l_maxsubjectlen")%>'>
              ���� (����Ʈ���� �������� ����)</td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�ۺ���� ��ü��� ����</td>
           <td width="80%" height="25" class="bg_04">
 <%
			String chk_v_listmode_n = "";
			String chk_v_listmode_y = "";
			if ("y".equalsIgnoreCase(rs.getString("v_listmode")))
				chk_v_listmode_y = "checked";
			else
				chk_v_listmode_n = "checked";
%>
		   <input style=border:0 type=radio name='v_listmode' value='y' <%=chk_v_listmode_y%>>����� <input style=border:0 type=radio name='v_listmode' value='n' <%=chk_v_listmode_n%>>������
		   </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">���� �ǰ� �ޱ�</td>
           <td width="80%" height="25" class="bg_04">
<%
			String chk_enablecomment_n = "";
			String chk_enablecomment_y = "";
			if ("y".equalsIgnoreCase(rs.getString("enablecomment")))
				chk_enablecomment_y = "checked";
			else
				chk_enablecomment_n = "checked";
%>
		   <INPUT STYLE=border:0 TYPE=radio NAME='enablecomment' VALUE='y' <%=chk_enablecomment_y%>>����� <INPUT STYLE=border:0 TYPE=radio NAME='enablecomment' VALUE='n' <%=chk_enablecomment_n%>>������
		   </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�۾��� ����</td>
           <td width="80%" height="25" class="bg_04">
<%
			String chk_adminonly_n = "";
			String chk_adminonly_y = "";
			if ("y".equalsIgnoreCase(rs.getString("adminonly")))
				chk_adminonly_y = "checked";
			else
				chk_adminonly_n = "checked";
%>		   
		   <INPUT STYLE=border:0 TYPE=radio NAME='adminonly' VALUE='y' <%=chk_adminonly_y%>>�����ڸ� �۾��� ��� <INPUT STYLE=border:0 TYPE=radio NAME='adminonly' VALUE='n' <%=chk_adminonly_n%>>������ �۾��� ����</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">���� ���ε� ���</td>
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

			<input style=border:0 type=radio name='enableupload' value='y' <%=chk_enableupload_y%>>����� <input style=border:0 type=radio name='enableupload' value='n' <%=chk_enableupload_n%>>��� ����</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">���ε� ������ ���� ��</td>
           <td width="80%" height="25" class="bg_04"><input type=text name='upload' size=5 maxlength=5 value='<%=upload%>'></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">���ε� ������ �ִ� ũ��</td>
           <td width="80%" height="25" class="bg_04"><input type=text name='upload_size' size=5 maxlength=5 value='<%=rs.getInt("upload_size")%>'> Mbyte</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr><td height=10 colspan="2"></td></tr></tbody></table>
		 
	<!-- ī�װ� ���� -->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">ī�װ� ����</td>
           <td width="80%" height="25" class="bg_04">
				<select name="category_remove">
					<option class=del selected>������ ī�װ��� �����ϼ���</option>
<%
					String category_items = rs.getString("category_items");
					java.util.StringTokenizer items = new java.util.StringTokenizer(category_items, "|");
					items.nextToken(); // admin SKIP
					if(rs.getString("tablename").equals("notice_board")){
						items.nextToken(); // �系 ���� SKIP
						items.nextToken(); // ��� ���� SKIP
					}
					while(items.hasMoreElements()){
						String item = items.nextToken();
%>
						<option value="<%=item%>"><%=item%></option>
<%					}
%>
                    </select>&nbsp;
				<input type=submit value='remove' name='mode' onClick="if(!category_remove.value){alert('������ ī�װ��� �����ϼ���');category_remove.focus();return false;}else return true;"><br>

				<input type=text name='category_add' size=16 maxlength=20 value='�߰��� ī�װ�' onFocus="this.value='';return true;">&nbsp;
				<input type=submit value='add' name='mode' onClick="if(!category_add.value || category_add.value == '�߰��� ī�װ�'){alert('ī�װ��� �Է��ϼ���.');category_add.focus();return false;}else return true;">
		   </td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr><td height=10 colspan="2"></td></tr></tbody></table></td></tr></table>

	<!-- ������ ���� -->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">������ ����</td>
           <td width="80%" height="25" class="bg_04">
               <select name="manager_remove">
                 <option class=del selected>������ �����ڸ� �����ϼ���</option>
					<%
						for(int m=0;m<manager_list.length;m++){
						%>
						<option value="<%=manager_list[m]%>"><%=manager_list[m]%></option>
			            <%}%>
                      </select>&nbsp;
               <input type=submit value='UserDel' name='mode' onClick="if(!manager_remove.value){alert('�����Ұ����ڸ� �����ϼ���');manager_remove.focus();return false;}else return true;"><br>

               <input type=text name='manager_add' size=16 maxlength=20 value='�߰��� ������' onFocus="this.value='';return true;">&nbsp;
               <input type=submit value='UserAdd' name='mode' onClick="if(!manager_add.value || manager_add.value == '�߰��� ������'){alert('�����ڸ� �Է��ϼ���.');manager_add.focus();return false;}else return true;">
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
