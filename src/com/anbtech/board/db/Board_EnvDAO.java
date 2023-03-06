/************************************************************
 * 
 * board_env 테이블에서 파라미터로 넘어온 테이블명을 검색하여
 * 해당 테이블 속성값을 쿼리한 후, Board_Env 빈에 값을 넣는다.
 *
 ************************************************************/

package com.anbtech.board.db;

import com.anbtech.board.entity.Board_Env;

import java.sql.*;
import java.util.*;

public class Board_EnvDAO{

	private Connection con;

	public Board_EnvDAO(Connection con){
		this.con = con;
	}

	public Board_Env getBoard_env(String tablename) throws Exception{

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Board_Env board_env = null;

		String query = "SELECT * FROM board_env WHERE tablename = ? ";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1, tablename);
		rs = pstmt.executeQuery();

		if(rs.next()){
			board_env = new Board_Env();
			board_env.setTablename(rs.getString("tablename"));
			board_env.setHtml_title(rs.getString("html_title"));
			board_env.setHtml_head(rs.getString("html_head"));
			board_env.setHtml_tail(rs.getString("html_tail"));
			board_env.setHtml_bgcolor(rs.getString("html_bgcolor"));
			board_env.setHtml_background(rs.getString("html_background"));
			board_env.setT_width(rs.getString("t_width"));
			board_env.setT_topbgcolor(rs.getString("t_topbgcolor"));
			board_env.setT_rowbgcolor(rs.getString("t_rowbgcolor"));
			board_env.setT_rowbgcolor_o(rs.getString("t_rowbgcolor_o"));
			board_env.setT_tinybgcolor(rs.getString("t_tinybgcolor"));
			board_env.setV_listmode(rs.getString("v_listmode"));
			board_env.setCategory_items(rs.getString("category_items"));
			board_env.setEnablecomment(rs.getString("enablecomment"));
			board_env.setEnablevote(rs.getString("enablevote"));
			board_env.setSkin(rs.getString("skin"));
			board_env.setEnablebagview(rs.getString("enablebagview"));
			board_env.setAdminonly(rs.getString("adminonly"));
			board_env.setEnablepreview(rs.getString("enablepreview"));

			board_env.setT_border(rs.getInt("t_border"));
			board_env.setL_maxlist(rs.getInt("l_maxlist"));
			board_env.setL_maxpage(rs.getInt("l_maxpage"));
			board_env.setL_maxsubjectlen(rs.getInt("l_maxsubjectlen"));
			board_env.setV_defaultheight(rs.getInt("v_defaultheight"));
			board_env.setEnablecategory(rs.getInt("enablecategory"));
			board_env.setEnableupload(rs.getInt("enableupload"));
			board_env.setUpload_size(rs.getInt("upload_size"));
			board_env.setEnablechkcool(rs.getInt("enablechkcool"));

			board_env.setAdmin_id(rs.getString("admin_id"));
			board_env.setAdmin_pwd(rs.getString("admin_pwd"));

			board_env.setOwnersId(rs.getString("owners_id"));

			pstmt.close();
			rs.close();

			return board_env;
		}else{
			throw new Exception("<script> alert('게시판이 존재하지 않습니다.'); history.go(-1); </script>");
		}
	}
}