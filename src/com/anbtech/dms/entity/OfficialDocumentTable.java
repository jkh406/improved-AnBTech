package com.anbtech.dms.entity;
public class OfficialDocumentTable
{
	//공문서 전체컬럼(OfficialDocument,InDocument_receive,InDocument_send,OutDocument_receive,OutDocument_send)
	private String id;					//공통:관리번호
	private String user_id;				//공통:사번
	private String user_name;			//공통:이름
	private String user_rank;			//공통:직급
	private int ac_id;					//공통:부서관리코드
	private String ac_code;				//공통:부서코드
	private String code;				//공통:부서명 Tree 관리코드
	private String ac_name;				//공통:부서명

	private String serial_no;			//공지사항,사내수신,사내발송,사외수신,사외발송,
	private String class_no;			//공지사항,사내수신,사내발송,사외수신,사외발송,
	private String doc_id;				//공지사항,시내수신,사내발송,사외수신,사외발송,
	private String slogan;				//공지사항,       ,사내발송,       ,사외발송,
	private String title_name;			//공지사항,       ,사내발송,       ,사외발송,
	private String in_date;				//공지사항,사내수신,사내발송,사외수신,사외발송,
	private String send_date;			//		 ,		 ,       ,사외수신,사외발송,
	private String enforce_date;		//공지사항,사내수신,사내발송,사외수신,사외발송,		
	private String receive;				//공지사항,사내수신,사내발송,사외수신,사외발송,
	private String reference;			//       ,       ,사내발송,       ,사외발송,
	private String sending;				//공지사항,사내수신,사내발송,사외수신,사외발송,
	private String sheet_cnt;			//		 ,		 ,       ,사외수신,사외발송,
	private String receive_div;			//		 ,		 ,       ,사외수신,       ,
	private String receive_id;			//		 ,		 ,       ,사외수신,       ,
	private String receive_name;		//		 ,		 ,       ,사외수신,       ,
	private String receive_rank;		//		 ,		 ,       ,사외수신,       ,
	private String rec_company;			//       ,       ,       ,       ,사외발송,
	private String rec_id;				//       ,       ,사내발송,       ,       ,
	private String rec_name;			//       ,       ,사내발송,       ,사외발송,
	private String rec_rank;			//       ,       ,사내발송,       ,       ,
	private String rec_code;			//       ,       ,사내발송,       ,       ,
	private String rec_division;		//       ,       ,사내발송,       ,       ,
	private String rec_mail;			//       ,       ,       ,       ,사외발송,
	private String subject;				//공지사항,사내수신,       ,사외수신,사외발송,		
	private String bon_path;			//공지사항,사내수신,       ,       ,사외발송,
	private String bon_file;			//공지사항,사내수신,       ,       ,사외발송,
	private String address;				//       ,       ,       ,       ,사외발송,
	private String tel;					//       ,       ,       ,       ,사외발송,
	private String fax;					//       ,       ,       ,       ,사외발송,
	private String firm_name;			//공지사항,       ,사내발송,       ,사외발송,
	private String representative;		//공지사항,       ,사내발송,       ,사외발송,
	private String etc;					//공지사항,       ,       ,       ,사외발송,
	
	private String delete_date;			//공통:삭제일자	
	private String file_path;			//공통:파일path		
	private String fname;				//공통:파일원래명	
	private String sname;				//공통:파일저장명		
	private String ftype;				//공통:파일확장자명	
	private String fsize;				//공통:파일크디	
	private String flag;				//공통:전자결재상태
	private String app_id;				//공통:전자결재승인 관리코드
	private String app_date;			//공통:전자결재승인 일자

	private String module_name;			//공지사항,        ,사내발송,사외수신,       ,	
	private String module_add;			//       ,        ,사내발송,사외수신,       ,	
	private String mail;				//공지사항,        ,       ,       ,       ,
	private String mail_add;			//공지사항,        ,       ,       ,       ,

	private String modify;				//수정모드
	private String delete;				//삭제모드
	private String distribute;			//배포모드
		
	//help method
	public void setId(String id)				{ this.id = id;			}
	public String getId()						{ return this.id;		}

	public void setUserId(String user_id)		{ this.user_id = user_id;	}
	public String getUserId()					{ return this.user_id;		}

	public void setUserName(String user_name)	{ this.user_name = user_name;	}
	public String getUserName()					{ return this.user_name;		}

	public void setUserRank(String user_rank)	{ this.user_rank = user_rank;	}
	public String getUserRank()					{ return this.user_rank;		}

	public void setAcId(int ac_id)				{ this.ac_id = ac_id;		}
	public int getAcId()						{ return this.ac_id;		}

	public void setAcCode(String ac_code)		{ this.ac_code = ac_code;	}
	public String getAcCode()					{ return this.ac_code;		}

	public void setCode(String code)			{ this.code = code;	}
	public String getCode()						{ return this.code;		}

	public void setAcName(String ac_name)		{ this.ac_name = ac_name;	}
	public String getAcName()					{ return this.ac_name;		}

	//private int serial_no;				//공지사항,사내수신,사내발송,사외수신,사외발송,
	public void setSerialNo(String serial_no)	{ this.serial_no = serial_no;	}
	public String getSerialNo()					{ return this.serial_no;		}

	//private String class_no;			//공지사항,사내수신,사내발송,사외수신,사외발송,
	public void setClassNo(String class_no)		{ this.class_no = class_no;	}
	public String getClassNo()					{ return this.class_no;		}

	//private String doc_id;				//공지사항,시내수신,사내발송,사외수신,사외발송,
	public void setDocId(String doc_id)			{ this.doc_id = doc_id;	}
	public String getDocId()					{ return this.doc_id;		}

	//private String slogan;				//공지사항,       ,사내발송,       ,사외발송,
	public void setSlogan(String slogan)		{ this.slogan = slogan;	}
	public String getSlogan()					{ return this.slogan;		}

	//private String title_name;			//공지사항,       ,사내발송,       ,사외발송,
	public void setTitleName(String title_name)	{ this.title_name = title_name;	}
	public String getTitleName()				{ return this.title_name;		}

	//private String in_date;				//공지사항,사내수신,사내발송,사외수신,사외발송,
	public void setInDate(String in_date)		{ this.in_date = in_date;	}
	public String getInDate()					{ return this.in_date;		}

	//private String send_date;			//		 ,		 ,       ,사외수신,사외발송,
	public void setSendDate(String send_date)	{ this.send_date = send_date;	}
	public String getSendDate()					{ return this.send_date;		}

	//private String enforce_date;		//공지사항,사내수신,사내발송,사외수신,사외발송,
	public void setEnforceDate(String enforce_date)		{ this.enforce_date = enforce_date;	}
	public String getEnforceDate()				{ return this.enforce_date;		}

	//private String receive;				//공지사항,사내수신,사내발송,사외수신,사외발송,
	public void setReceive(String receive)		{ this.receive = receive;	}
	public String getReceive()					{ return this.receive;		}

	//private String reference;			//       ,       ,사내발송,       ,사외발송,
	public void setReference(String reference)	{ this.reference = reference;	}
	public String getReference()				{ return this.reference;		}

	//private String sending;				//공지사항,사내수신,사내발송,사외수신,사외발송,
	public void setSending(String sending)		{ this.sending = sending;	}
	public String getSending()					{ return this.sending;		}

	//private int sheet_cnt;				//		 ,		 ,       ,사외수신,사외발송,
	public void setSheetCnt(String sheet_cnt)	{ this.sheet_cnt = sheet_cnt;	}
	public String getSheetCnt()					{ return this.sheet_cnt;		}

	//private String receive_div;			//		 ,		 ,       ,사외수신,       ,
	public void setReceiveDiv(String receive_div)	{ this.receive_div = receive_div;	}
	public String getReceiveDiv()				{ return this.receive_div;		}

	//private String receive_id;			//		 ,		 ,       ,사외수신,       ,
	public void setReceiveId(String receive_id)	{ this.receive_id = receive_id;	}
	public String getReceiveId()				{ return this.receive_id;		}

	//private String receive_name;		//		 ,		 ,       ,사외수신,       ,
	public void setReceiveName(String receive_name)		{ this.receive_name = receive_name;	}
	public String getReceiveName()				{ return this.receive_name;		}

	//private String receive_rank;		//		 ,		 ,       ,사외수신,       ,
	public void setReceiveRank(String receive_rank)		{ this.receive_rank = receive_rank;	}
	public String getReceiveRank()				{ return this.receive_rank;		}

	//private String rec_company;			//       ,       ,       ,       ,사외발송,
	public void setRecCompany(String rec_company)		{ this.rec_company = rec_company;	}
	public String getRecCompany()				{ return this.rec_company;		}

	//private String rec_id;				//       ,       ,사내발송,       ,       ,
	public void setRecId(String rec_id)			{ this.rec_id = rec_id;	}
	public String getRecId()					{ return this.rec_id;		}

	//private String rec_name;			//       ,       ,사내발송,       ,사외발송,
	public void setRecName(String rec_name)		{ this.rec_name = rec_name;	}
	public String getRecName()					{ return this.rec_name;		}

	//private String rec_rank;			//       ,       ,사내발송,       ,       ,
	public void setRecRank(String rec_rank)		{ this.rec_rank = rec_rank;	}
	public String getRecRank()					{ return this.rec_rank;		}

	//private String rec_code;			//       ,       ,사내발송,       ,       ,
	public void setRecCode(String rec_code)		{ this.rec_code = rec_code;	}
	public String getRecCode()					{ return this.rec_code;		}

	//private String rec_division;		//       ,       ,사내발송,       ,       ,
	public void setRecDivision(String rec_division)		{ this.rec_division = rec_division;	}
	public String getRecDivision()				{ return this.rec_division;		}

	//private String rec_mail;			//       ,       ,       ,       ,사외발송,
	public void setRecMail(String rec_mail)		{ this.rec_mail = rec_mail;	}
	public String getRecMail()					{ return this.rec_mail;		}

	//private String subject;				//공지사항,사내수신,       ,사외수신,사외발송,	
	public void setSubject(String subject)		{ this.subject = subject;	}
	public String getSubject()					{ return this.subject;		}

	//private String bon_path;			//공지사항,사내수신,       ,       ,사외발송,
	public void setBonPath(String bon_path)		{ this.bon_path = bon_path;	}
	public String getBonPath()					{ return this.bon_path;		}

	//private String bon_file;			//공지사항,사내수신,       ,       ,사외발송,
	public void setBonFile(String bon_file)		{ this.bon_file = bon_file;	}
	public String getBonFile()					{ return this.bon_file;		}

	//private String address;				//       ,       ,       ,       ,사외발송,
	public void setAddress(String address)		{ this.address = address;	}
	public String getAddress()					{ return this.address;		}

	//private String tel;					//       ,       ,       ,       ,사외발송,
	public void setTel(String tel)				{ this.tel = tel;	}
	public String getTel()						{ return this.tel;		}

	//private String fax;					//       ,       ,       ,       ,사외발송,
	public void setFax(String fax)				{ this.fax = fax;	}
	public String getFax()						{ return this.fax;		}

	//private String firm_name;			//공지사항,       ,사내발송,       ,사외발송,
	public void setFirmName(String firm_name)	{ this.firm_name = firm_name;	}
	public String getFirmName()					{ return this.firm_name;		}

	//private String representative;		//공지사항,       ,사내발송,       ,사외발송,
	public void setRepresentative(String representative)	{ this.representative = representative;	}
	public String getRepresentative()			{ return this.representative;		}

	//private String etc;					//공지사항,       ,       ,       ,사외발송,
	public void setEtc(String etc)				{ this.etc = etc;	}
	public String getEtc()						{ return this.etc;		}

	
	//private String delete_date;			//공통:삭제일자	
	public void setDeleteDate(String delete_date)		{ this.delete_date = delete_date;	}
	public String getDeleteDate()				{ return this.delete_date;		}

	//private String file_path;			//공통:파일path		
	public void setFilePath(String file_path)		{ this.file_path = file_path;	}
	public String getFilePath()					{ return this.file_path;		}

	//private String fname;				//공통:파일원래명	
	public void setFname(String fname)			{ this.fname = fname;	}
	public String getFname()					{ return this.fname;		}

	//private String sname;				//공통:파일저장명		
	public void setSname(String sname)			{ this.sname = sname;	}
	public String getSname()					{ return this.sname;		}

	//private String ftype;				//공통:파일확장자명	
	public void setFtype(String ftype)			{ this.ftype = ftype;	}
	public String getFtype()					{ return this.ftype;		}

	//private String fsize;				//공통:파일크디	
	public void setFsize(String fsize)			{ this.fsize = fsize;	}
	public String getFsize()					{ return this.fsize;		}

	//private String flag;				//공통:전자결재상태
	public void setFlag(String flag)			{ this.flag = flag;	}
	public String getFlag()						{ return this.flag;		}

	//private String app_date;			//공통:전자결재 승인관리코드
	public void setAppId(String app_id)			{ this.app_id = app_id;	}
	public String getAppId()					{ return this.app_id;		}

	//private String app_date;			//공통:전자결재 승인일자
	public void setAppDate(String app_date)		{ this.app_date = app_date;	}
	public String getAppDate()					{ return this.app_date;		}

	//private String module_name;			//공지사항,        ,사내발송,사외수신,       ,	
	public void setModuleName(String module_name)		{ this.module_name = module_name;	}
	public String getModuleName()				{ return this.module_name;		}

	//private String module_add;			//       ,        ,사내발송,사외수신,       ,	
	public void setModuleAdd(String module_add)		{ this.module_add = module_add;	}
	public String getModuleAdd()				{ return this.module_add;		}

	//private String mail;				//공지사항,        ,       ,       ,       ,
	public void setMail(String mail)			{ this.mail = mail;	}
	public String getMail()						{ return this.mail;		}

	//private String mail_add;			//공지사항,        ,       ,       ,       ,
	public void setMailAdd(String mail_add)		{ this.mail_add = mail_add;	}
	public String getMailAdd()					{ return this.mail_add;		}

	//private String modify;				//수정모드
	public void setModify(String modify)		{ this.modify = modify;	}
	public String getModify()					{ return this.modify;		}

	//private String delete;				//삭제모드
	public void setDelete(String delete)		{ this.delete = delete;	}
	public String getDelete()					{ return this.delete;		}
	
	//private String distribute;			//배포모드
	public void setDistribute(String distribute){ this.distribute = distribute;	}
	public String getDistribute()				{ return this.distribute;		}
}

