/********************************
 �������� TABLE
 (��԰���, ��İ���, �޴������)
********************************/
package com.anbtech.share.entity;

public class ShareBdTable
{
	private int		no		= 0;	//  ������ȣ 		
	private String	subject = "";	//	����			varchar (100) 
	private String	ver		= "";	//	����			varchar (5) 		
	private String	wid		= "";	//	�����ID		varchar (20) 	
	private String	wname	= "";	//	������̸�	varchar (20) 	
	private String	wdate	= "";	//	�����		varchar (8) 	
	private String	doc_no	= "";	//	��Ϲ��� ��ȣ	varchar	(20)		
	private String	ac_code = "";	//	�μ�Code		varchar (20) 	
	private String	ac_name = "";	//	�μ���		varchar (20) 	
	private String	category= "";	//	ī�װ�		varchar (20) 	
	private String	content = "";	//	����			varchar (100) 
	private String	fname	= "";	//	ȭ���̸�		varchar (255) 
	private String	fsize	= "";	//	ȭ�ϻ�����	varchar (100) 
	private String	ftype	= "";	//	ȭ��Ÿ��		varchar (255) 
	private int		cnt		= 0;	//	��ȸ��		int			
	private String  tablename = ""; //  Table name
	private String  fpath	= "";	//  ���ϰ��
	private String  fumask  = "";   //  
	private String  fse		= "";   //  ���ϰ���
	private String  flink	= "";	// 
	private String  did     = "";
	private String  subject_link = ""; // �󼼺��� ��ũ ����
	private String  category_text = "";// ī�װ� �ؽ�Ʈ\

	private String  mid		= "";	//  ������ID
	private String	mname	= "";	//	�������̸�	varchar (20) 	
	private String	mdate	= "";	//	������		varchar (8) 	


	// setter...
	public void setNo(int no) {
		this.no = no;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public void setWname(String wname) {
		this.wname = wname;
	}
	public void setWdate(String wdate) {
		this.wdate = wdate;
	}
	public void setDocNo(String doc_no) {
		this.doc_no = doc_no;
	}
	public void setAcCode(String ac_code) {
		this.ac_code = ac_code;
	}
	public void setAcName(String ac_name) {
		this.ac_name = ac_name;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public void setFsize(String fsize) {
		this.fsize = fsize;
	}
	public void setFtype(String ftype) {
		this.ftype = ftype;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public void setTableName(String tablename){
		this.tablename = tablename;
	}
	public void setFpath(String fpath){
		this.fpath = fpath;
	}
	public void setFumask(String fumask){
		this.fumask = fumask;
	}
	public void setFlink(String flink){
		this.flink = flink;
	}
	public void setFse(String fse){
		this.fse = fse;
	}
	public void setDid(String did){
		this.did = did;
	}
	public void setSubjectLink(String subject_link){
		this.subject_link = subject_link;
	}
	public void setCategoryText(String category_text){
		this.category_text = category_text;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public void setMdate(String mdate) {
		this.mdate = mdate;
	}


	// getter....
	public int getNo() {
		return no;
	}
	public String getSubject() {
		return subject;
	}
	public String getVer() {
		return ver;
	}
	public String getWid() {
		return wid ;
	}
	public String getWname() {
		return wname;
	}
	public String getWdate() {
		return wdate;
	}
	public String getDocNo() {
		return doc_no;
	}
	public String getAcCode() {
		return ac_code;
	}
	public String getAcName() {
		return ac_name;
	}
	public String getCategory() {
		return category ;
	}
	public String getContent() {
		return content;
	}
	public String getFname() {
		return fname ;
	}
	public String getFsize() {
		return fsize;
	}
	public String getFtype() {
		return ftype;
	}
	public int getCnt() {
		return cnt;
	}
	public String getTableName(){
		return tablename;
	}
	public String getFpath(){
		return fpath;
	}
	public String getFumask(){
		return fumask;
	}
	public String getFlink(){
		return flink;
	}
	public String getFse(){
		return fse;
	}
	public String getDid(){
		return did;
	}
	public String getSubjectLink(){
		return subject_link;
	}
	public String getCategoryText(){
		return category_text;
	}
	public String getMid() {
		return mid ;
	}
	public String getMname() {
		return mname;
	}
	public String getMdate() {
		return mdate;
	}

	public static void main(String args) 
	{
		System.out.println("Hello World!");
	}
}
