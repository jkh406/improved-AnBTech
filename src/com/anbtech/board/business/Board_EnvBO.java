package com.anbtech.board.business;

import com.anbtech.board.entity.*;
import com.anbtech.board.db.*;

import java.util.*;
import java.sql.*;

public class Board_EnvBO{

	private Connection con;

	public Board_EnvBO(Connection con){
		this.con = con;
	}

	public ArrayList getCategory_list(String tablename) throws Exception{

		Board_Env board_env = new Board_Env();
		Board_EnvDAO board_envDAO = new Board_EnvDAO(con);
		board_env = board_envDAO.getBoard_env(tablename);
		String category_items = board_env.getCategory_items();
		ArrayList category_list = new ArrayList();
		category_list = com.anbtech.util.Token.getTokenList(category_items);

		return category_list;
	}

	public ArrayList getLink_category_list(String tablename, String category) throws Exception{

		TableDAO tableDAO = new TableDAO(con);

		ArrayList category_list = new ArrayList();
		ArrayList link_category_list = new ArrayList();

		String category_view = "";
		Iterator category_iter = getCategory_list(tablename).iterator();
		
		category_iter.next();

		if(category.length() == 0) category = "0";
//프레임으로 나뉜 왼쪽 메뉴에 카테고리 분류를 넣기 위해 수정함.
/*-----------원소스-----------
		if(0 == Integer.parseInt(category)) category_view = "<B> 전체목록보기("+tableDAO.getTotal(tablename,"")+") </B>";
		else category_view = "<a href='AnBBoard?tablename="+tablename+"'> 전체목록보기("+tableDAO.getTotal(tablename,"")+") </a>";
		link_category_list.add(category_view);
----------------------------*/
//-------수정 후-------------
		category_view = "<a href='AnBBoard?tablename="+tablename+"'> 전체목록보기("+tableDAO.getTotal(tablename,"")+") </a>";
		link_category_list.add(category_view);
//---------------------------
		int i=1;
		while(category_iter.hasNext()){
			if(i == Integer.parseInt(category)) category_view = "<B>"+category_iter.next()+"("+tableDAO.getTotal(tablename," WHERE category='"+i+"'")+")</B>";
			else category_view = "<a href='AnBBoard?tablename="+tablename+"&category="+i+"'> "+category_iter.next()+"("+tableDAO.getTotal(tablename," WHERE category='"+i+"'")+") </a>";
			link_category_list.add(category_view);
			i++;
		}

		return link_category_list;
	}

	public boolean chkLogin(String tablename, String id, String password) throws Exception{

		boolean chkLogin = false;
		
		Board_Env board_env = new Board_Env();
		Board_EnvDAO board_envDAO = new Board_EnvDAO(con);
		board_env = board_envDAO.getBoard_env(tablename);

		if (id.equals(board_env.getAdmin_id()) && password.equals(board_env.getAdmin_pwd()))	chkLogin = true;
		
		return chkLogin;
	}
}