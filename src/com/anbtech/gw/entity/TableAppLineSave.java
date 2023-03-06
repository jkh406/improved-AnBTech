package com.anbtech.gw.entity;
public class TableAppLineSave
{
	private String pid;
	private String line_subj;
	private String writer;
	private String write_date;
	private String bon_path;
	private String line_file;

	public void setAlPid(String pid) {
		this.pid = pid;
	}
	public String getAlPid() {
		return this.pid;
	}

	public void setAlLineSubj(String line_subj) {
		this.line_subj = line_subj;
	}
	public String getAlLineSubj() {
		return this.line_subj;
	}
	
	public void setAlWriter(String writer) {
		this.writer = writer;
	}
	public String getAlWriter() {
		return this.writer;
	}
	
	public void setAlWriteDate(String write_date) {
		this.write_date = write_date;
	}
	public String getAlWriteDate() {
		return this.write_date;
	}
	
	public void setAlBonPath(String bon_path) {
		this.bon_path = bon_path;
	}
	public String getAlBonPath() {
		return this.bon_path;
	}

	public void setAlLineFile(String line_file) {
		this.line_file = line_file;
	}
	public String getAlLineFile() {
		return this.line_file;
	}
	
}
