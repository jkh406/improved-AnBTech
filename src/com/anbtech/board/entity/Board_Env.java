/************************************************************
 * 
 * 게시판 속성들을 담는 변수에 값을 입력하고 출력하는 Bean
 *
 ************************************************************/

package com.anbtech.board.entity;

public class Board_Env{

	private int no;
	private String tablename;
	private String html_title;
	private String html_head;
	private String html_tail;
	private String html_bgcolor;
	private String html_background;
	private String t_width;
	private int t_border;
	private String t_topbgcolor;
	private String t_rowbgcolor;
	private String t_rowbgcolor_o;
	private String t_tinybgcolor;
	private int l_maxlist;
	private int l_maxpage;
	private int l_maxsubjectlen;
	private int v_defaultheight;
	private String v_listmode;
	private int enablecategory;
	private String category_items;
	private int enableupload;
	private int upload_size;
	private String enablecomment;
	private String enablevote;
	private String skin;
	private String enablebagview;
	private int enablechkcool;
	private String admin_id;
	private String admin_pwd;
	private String adminonly;
	private String enablepreview;
	private String owners_id;

	//추가적으로 들어가는 거
	private String mode;
	private String mapping;

	//
	public int getNo(){
		return no;
	}
	public void setNo(int no){
		this.no = no;
	}

	public String getTablename(){
		return tablename;
	}
	public void setTablename(String tablename){
		this.tablename = tablename;
	}

	public String getHtml_title(){
		return html_title;
	}
	public void setHtml_title(String html_title){
		this.html_title = html_title;
	}

	public String getHtml_head(){
		return html_head;
	}
	public void setHtml_head(String html_head){
		this.html_head = html_head;
	}

	public String getHtml_tail(){
		return html_tail;
	}
	public void setHtml_tail(String html_tail){
		this.html_tail = html_tail;
	}

	public String getHtml_bgcolor(){
		return html_bgcolor;
	}
	public void setHtml_bgcolor(String html_bgcolor){
		this.html_bgcolor = html_bgcolor;
	}

	public String getHtml_background(){
		return html_background;
	}
	public void setHtml_background(String html_background){
		this.html_background = html_background;
	}

	public String getT_width(){
		return t_width;
	}
	public void setT_width(String t_width){
		this.t_width = t_width;
	}

	public int getT_border(){
		return t_border;
	}
	public void setT_border(int t_border){
		this.t_border = t_border;
	}

	public String getT_topbgcolor(){
		return t_topbgcolor;
	}
	public void setT_topbgcolor(String t_topbgcolor){
		this.t_topbgcolor = t_topbgcolor;
	}

	public String getT_rowbgcolor(){
		return t_rowbgcolor;
	}
	public void setT_rowbgcolor(String t_rowbgcolor){
		this.t_rowbgcolor = t_rowbgcolor;
	}

	public String getT_rowbgcolor_o(){
		return t_rowbgcolor_o;
	}
	public void setT_rowbgcolor_o(String t_rowbgcolor_o){
		this.t_rowbgcolor_o = t_rowbgcolor_o;
	}

	public String getT_tinybgcolor(){
		return t_tinybgcolor;
	}
	public void setT_tinybgcolor(String t_tinybgcolor){
		this.t_tinybgcolor = t_tinybgcolor;
	}

	public int getL_maxlist(){
		return l_maxlist;
	}
	public void setL_maxlist(int l_maxlist){
		this.l_maxlist = l_maxlist;
	}

	public int getL_maxpage(){
		return l_maxpage;
	}
	public void setL_maxpage(int l_maxpage){
		this.l_maxpage = l_maxpage;
	}

	public int getL_maxsubjectlen(){
		return l_maxsubjectlen;
	}
	public void setL_maxsubjectlen(int l_maxsubjectlen){
		this.l_maxsubjectlen = l_maxsubjectlen;
	}

	public int getV_defaultheight(){
		return v_defaultheight;
	}
	public void setV_defaultheight(int v_defaultheight){
		this.v_defaultheight = v_defaultheight;
	}

	public String getV_listmode(){
		return v_listmode;
	}
	public void setV_listmode(String v_listmode){
		this.v_listmode = v_listmode;
	}

	public int getEnablecategory(){
		return enablecategory;
	}
	public void setEnablecategory(int enablecategory){
		this.enablecategory = enablecategory;
	}

	public String getCategory_items(){
		return category_items;
	}
	public void setCategory_items(String category_items){
		this.category_items = category_items;
	}

	public int getEnableupload(){
		return enableupload;
	}
	public void setEnableupload(int enableupload){
		this.enableupload = enableupload;
	}

	public int getUpload_size(){
		return upload_size;
	}
	public void setUpload_size(int upload_size){
		this.upload_size = upload_size;
	}

	public String getEnablecomment(){
		return enablecomment;
	}
	public void setEnablecomment(String enablecomment){
		this.enablecomment = enablecomment;
	}

	public String getEnablevote(){
		return enablevote;
	}
	public void setEnablevote(String enablevote){
		this.enablevote = enablevote;
	}

	public String getSkin(){
		return skin;
	}
	public void setSkin(String skin){
		this.skin = skin;
	}

	public String getEnablebagview(){
		return enablebagview;
	}
	public void setEnablebagview(String enablebagview){
		this.enablebagview = enablebagview;
	}

	public int getEnablechkcool(){
		return enablechkcool;
	}
	public void setEnablechkcool(int enablechkcool){
		this.enablechkcool = enablechkcool;
	}

	public String getAdmin_id(){
		return admin_id;
	}
	public void setAdmin_id(String admin_id){
		this.admin_id = admin_id;
	}

	public String getAdmin_pwd(){
		return admin_pwd;
	}
	public void setAdmin_pwd(String admin_pwd){
		this.admin_pwd = admin_pwd;
	}

	public String getAdminonly(){
		return adminonly;
	}
	public void setAdminonly(String adminonly){
		this.adminonly = adminonly;
	}

	public String getEnablepreview(){
		return enablepreview;
	}
	public void setEnablepreview(String enablepreview){
		this.enablepreview = enablepreview;
	}

	public String getMode(){
		return mode;
	}
	public void setMode(String mode){
		this.mode = mode;
	}

	public String getMapping(){
		return mapping;
	}
	public void setMapping(String mapping){
		this.mapping = mapping;
	}

	public String getOwnersId(){
		return owners_id;
	}
	public void setOwnersId(String owners_id){
		this.owners_id = owners_id;
	}
}