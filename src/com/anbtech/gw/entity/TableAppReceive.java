/************************************************************
**	전자결재 통보 테이블 컬럼 정의 클래스
*************************************************************/
package com.anbtech.gw.entity;
public class TableAppReceive
{
	//------------------------------------------------------
	// 변수 선언하기
	//------------------------------------------------------
	private String pid;
	private String app_subj;
	private String writer;
	private String writer_name;
	private String write_date;
	private String add_counter;
	private String isopen;
	private String receiver;
	private String delete_date;
	private String read_date;
	private String plid;
	private String send_bom;
	private String request_date;
	private String flag ;
	private String receiver_name;
	private String receiver_div;
	private String receiver_rank;

	//기타
	private String sub_link;		//sub link

	//------------------------------------------------------
	// method 만들기
	//------------------------------------------------------
	public void setAmPid(String pid) {
		this.pid = pid;
	}
	public String getAmPid() {
		return this.pid;
	}

	public void setAmAppSubj(String app_subj) {
		this.app_subj = app_subj;
	}
	public String getAmAppSubj() {
		return this.app_subj;
	}
	
	public void setAmWriter(String writer) {
		this.writer = writer;
	}
	public String getAmWriter() {
		return this.writer;
	}
	
	public void setAmWriterName(String writer_name) {
		this.writer_name = writer_name;
	}
	public String getAmWriterName() {
		return this.writer_name;
	}
	
	public void setAmWriteDate(String write_date) {
		this.write_date = write_date;
	}
	public String getAmWriteDate() {
		return this.write_date;
	}

	public void setAmAddCounter(String add_counter) {
		this.add_counter = add_counter;
	}
	public String getAmAddCounter() {
		return this.add_counter;
	}
	
	public void setAmIsOpen(String isopen) {
		this.isopen = isopen;
	}
	public String getAmIsOpen() {
		return this.isopen;
	}
	
	public void setAmReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getAmReceiver() {
		return this.receiver;
	}
	public void setAmDeleteDate(String delete_date) {
		this.delete_date = delete_date;
	}
	public String getAmDeleteDate() {
		return this.delete_date;
	}
	public void setAmReadDate(String read_date) {
		this.read_date = read_date;
	}
	public String getAmReadDate() {
		return this.read_date;
	}

	public void setAmPlid(String plid) {
		this.plid = plid;
	}
	public String getAmPlid() {
		return this.plid;
	}
	public void setAmSendBom(String send_bom) {
		this.send_bom = send_bom;
	}
	public String getAmSendBom() {
		return this.send_bom;
	}
	
	public void setAmRequestDate(String request_date) {
		this.request_date = request_date;
	}
	public String getAmRequestDate() {
		return this.request_date;
	}
	public void setAmFlag(String flag) {
		this.flag = flag;
	}
	public String getAmFlag() {
		return this.flag;
	}
	public void setAmReceiverName(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	public String getAmReceiverName() {
		return this.receiver_name;
	}

	public void setAmReceiverDiv(String receiver_div) {
		this.receiver_div = receiver_div;
	}
	public String getAmReceiverDiv() {
		return this.receiver_div;
	}

	public void setAmReceiverRank(String receiver_rank) {
		this.receiver_rank = receiver_rank;
	}
	public String getAmReceiverRank() {
		return this.receiver_rank;
	}

	public void setAmSubLink(String sub_link) {
		this.sub_link = sub_link;
	}
	public String getAmSubLink() {
		return this.sub_link;
	}
}
