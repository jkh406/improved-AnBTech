/************************************************************
 * 
 * ÷������ �̸�,ũ��,������,����Ʈ�� ������ ���� �Է��ϰ� ����ϴ� Bean
 *
 ************************************************************/

package com.anbtech.util;

public class UpFileTable{

	private String filename;
	private String filesize;
	private String filetype;
	private String umask;
	private String did;

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

	public String getUmask(){
		return umask;
	}
	public void setUmask(String umask){
		this.umask = umask;
	}

	public String getDid(){
		return did;
	}
	public void setDid(String did){
		this.did = did;
	}
}