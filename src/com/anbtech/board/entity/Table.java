/************************************************************
 * 
 * 게시판 내용들을 담는 변수에 값을 입력하고 출력하는 Bean
 *
 ************************************************************/

package com.anbtech.board.entity;

public class Table{

	private String no;
	private String thread;
	private String depth;
	private String pos;
	private int rid;
	private int vid;
	private int cid;
	private String writer;
	private String writer_id;
	private String writer_div;
	private String writer_rank;
	private String email;
	private String homepage;
	private String ip_addr;
	private String passwd;
	private String subject;
	private String content;
	private String html;
	private String email_forward;
	private String filename;
	private String filesize;
	private String filetype;
	private String did;
	private String category;
	private String w_time;
	private String u_time;

	private String filelink;
	private String filepreview;

	public String getNo(){
		return no;
	}
	public void setNo(String no){
		this.no = no;
	}

	public String getThread(){
		return thread;
	}
	public void setThread(String thread){
		this.thread = thread;
	}

	public String getDepth(){
		return depth;
	}
	public void setDepth(String depth){
		this.depth = depth;
	}

	public String getPos(){
		return pos;
	}
	public void setPos(String pos){
		this.pos = pos;
	}

	public int getRid(){
		return rid;
	}
	public void setRid(int rid){
		this.rid = rid;
	}

	public int getVid(){
		return vid;
	}
	public void setVid(int vid){
		this.vid = vid;
	}

	public int getCid(){
		return cid;
	}
	public void setCid(int cid){
		this.cid = cid;
	}

	public String getWriter(){
		return writer;
	}
	public void setWriter(String writer){
		this.writer = writer;
	}

	public String getWriterId(){
		return writer_id;
	}
	public void setWriterId(String writer_id){
		this.writer_id = writer_id;
	}

	public String getWriterRank(){
		return writer_rank;
	}
	public void setWriterRank(String writer_rank){
		this.writer_rank = writer_rank;
	}

	public String getWriterDiv(){
		return writer_div;
	}
	public void setWriterDiv(String writer_div){
		this.writer_div = writer_div;
	}

	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}

	public String getHomepage(){
		return homepage;
	}
	public void setHomepage(String homepage){
		this.homepage = homepage;
	}

	public String getIp_addr(){
		return ip_addr;
	}
	public void setIp_addr(String ip_addr){
		this.ip_addr = ip_addr;
	}

	public String getPasswd(){
		return passwd;
	}
	public void setPasswd(String passwd){
		this.passwd = passwd;
	}

	public String getSubject(){
		return subject;
	}
	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}

	public String getHtml(){
		return html;
	}
	public void setHtml(String html){
		this.html = html;
	}

	public String getEmail_forward(){
		return email_forward;
	}
	public void setEmail_forward(String email_forward){
		this.email_forward = email_forward;
	}

	public String getFilename(){
		return filename;
	}
	public void setFilename(String filename){
		this.filename = filename;
	}

	public String getFilesize(){
		return filesize;
	}
	public void setFilesize(String filesize){
		this.filesize = filesize;
	}

	public String getFiletype(){
		return filetype;
	}
	public void setFiletype(String filetype){
		this.filetype = filetype;
	}

	public String getDid(){
		return did;
	}
	public void setDid(String did){
		this.did = did;
	}

	public String getCategory(){
		return category;
	}
	public void setCategory(String category){
		this.category = category;
	}

	public String getW_time(){
		return w_time;
	}
	public void setW_time(String w_time){
		this.w_time = w_time;
	}

	public String getU_time(){
		return u_time;
	}
	public void setU_time(String u_time){
		this.u_time = u_time;
	}
	
	public String getFilelink(){
		return filelink;
	}
	public void setFilelink(String filelink){
		this.filelink = filelink;
	}
	
	public String getFilepreview(){
		return filepreview;
	}
	public void setFilepreview(String filepreview){
		this.filepreview = filepreview;
	}
}