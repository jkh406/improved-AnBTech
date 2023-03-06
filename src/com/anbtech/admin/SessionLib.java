package com.anbtech.admin;

public class SessionLib {
	public String id;
	public String passwd;
	public String name;
	public String division;
	public String privilege;
//	public String dbkind;
//	public String rootpath;
	
	public SessionLib(String id,String passwd,String name,String division,String privilege) {
		this.id = id;
		this.passwd = passwd;
		this.name = name;
		this.division = division;
		this.privilege = privilege;
	}
/*	
	public SessionLib(String id,String passwd,String name,String dbkind,String rootpath) {
		this.id = id;
		this.passwd = passwd;
		this.name = name;
		this.dbkind = dbkind;
		this.rootpath = rootpath;
	}
*/
}