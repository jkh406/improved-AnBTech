/************************************************************
 * 
 * 게시판 내용에 다는 코멘트 내용을 담는 변수에 값을 입력 및
   출력하는 빈즈
 *
 ************************************************************/

package com.anbtech.board.entity;

public class Table_Cmt{

	private int no;
	private int ono;
	private String writer;
	private String comment;
	private String w_time;
	private String passwd;

	public int getNo(){
		return no;
	}
	public void setNo(int no){
		this.no = no;
	}

	public int getOno(){
		return ono;
	}
	public void setOno(int ono){
		this.ono = ono;
	}

	public String getWriter(){
		return writer;
	}
	public void setWriter(String writer){
		this.writer = writer;
	}

	public String getComment(){
		return comment;
	}
	public void setComment(String comment){
		this.comment = comment;
	}

	public String getW_time(){
		return w_time;
	}
	public void setW_time(String w_time){
		this.w_time = w_time;
	}
	
	public String getPasswd(){
		return passwd;
	}
	public void setPasswd(String passwd){
		this.passwd = passwd;
	}
}