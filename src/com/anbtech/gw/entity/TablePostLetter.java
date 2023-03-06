package com.anbtech.gw.entity;
public class TablePostLetter
{
	private String pid;
	private String post_subj;
	private String writer_id;
	private String writer_name;
	private String write_date;
	private String post_receiver;
	private String isopen;
	private String open_date;
	private String delete_date;

	public void setPlPid(String pid) {
		this.pid = pid;
	}
	public String getPlPid() {
		return this.pid;
	}

	public void setPlPostSubj(String post_subj) {
		this.post_subj = post_subj;
	}
	public String getPlPostSubj() {
		return this.post_subj;
	}

	public void setPlWriterId(String writer_id) {
		this.writer_id = writer_id;
	}
	public String getPlWriterId() {
		return this.writer_id;
	}

	public void setPlWriterName(String writer_name) {
		this.writer_name = writer_name;
	}
	public String getPlWriterName() {
		return this.writer_name;
	}

	public void setPlWriteDate(String write_date) {
		this.write_date = write_date;
	}
	public String getPlWriteDate() {
		return this.write_date;
	}

	public void setPlPostReceiver(String post_receiver) {
		this.post_receiver = post_receiver;
	}
	public String getPlPostReceiver() {
		return this.post_receiver;
	}

	public void setPlIsOpen(String isopen) {
		this.isopen = isopen;
	}
	public String getPlIsOpen() {
		return this.isopen;
	}

	public void setPlOpenDate(String open_date) {
		this.open_date = open_date;
	}
	public String getPlOpenDate() {
		return this.open_date;
	}
	
	public void setPlDeleteDate(String delete_date) {
		this.delete_date = delete_date;
	}
	public String getPlDeleteDate() {
		return this.delete_date;
	}
}
