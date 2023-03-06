package com.anbtech.dms.entity;
public class OfficialDocumentTable
{
	//������ ��ü�÷�(OfficialDocument,InDocument_receive,InDocument_send,OutDocument_receive,OutDocument_send)
	private String id;					//����:������ȣ
	private String user_id;				//����:���
	private String user_name;			//����:�̸�
	private String user_rank;			//����:����
	private int ac_id;					//����:�μ������ڵ�
	private String ac_code;				//����:�μ��ڵ�
	private String code;				//����:�μ��� Tree �����ڵ�
	private String ac_name;				//����:�μ���

	private String serial_no;			//��������,�系����,�系�߼�,��ܼ���,��ܹ߼�,
	private String class_no;			//��������,�系����,�系�߼�,��ܼ���,��ܹ߼�,
	private String doc_id;				//��������,�ó�����,�系�߼�,��ܼ���,��ܹ߼�,
	private String slogan;				//��������,       ,�系�߼�,       ,��ܹ߼�,
	private String title_name;			//��������,       ,�系�߼�,       ,��ܹ߼�,
	private String in_date;				//��������,�系����,�系�߼�,��ܼ���,��ܹ߼�,
	private String send_date;			//		 ,		 ,       ,��ܼ���,��ܹ߼�,
	private String enforce_date;		//��������,�系����,�系�߼�,��ܼ���,��ܹ߼�,		
	private String receive;				//��������,�系����,�系�߼�,��ܼ���,��ܹ߼�,
	private String reference;			//       ,       ,�系�߼�,       ,��ܹ߼�,
	private String sending;				//��������,�系����,�系�߼�,��ܼ���,��ܹ߼�,
	private String sheet_cnt;			//		 ,		 ,       ,��ܼ���,��ܹ߼�,
	private String receive_div;			//		 ,		 ,       ,��ܼ���,       ,
	private String receive_id;			//		 ,		 ,       ,��ܼ���,       ,
	private String receive_name;		//		 ,		 ,       ,��ܼ���,       ,
	private String receive_rank;		//		 ,		 ,       ,��ܼ���,       ,
	private String rec_company;			//       ,       ,       ,       ,��ܹ߼�,
	private String rec_id;				//       ,       ,�系�߼�,       ,       ,
	private String rec_name;			//       ,       ,�系�߼�,       ,��ܹ߼�,
	private String rec_rank;			//       ,       ,�系�߼�,       ,       ,
	private String rec_code;			//       ,       ,�系�߼�,       ,       ,
	private String rec_division;		//       ,       ,�系�߼�,       ,       ,
	private String rec_mail;			//       ,       ,       ,       ,��ܹ߼�,
	private String subject;				//��������,�系����,       ,��ܼ���,��ܹ߼�,		
	private String bon_path;			//��������,�系����,       ,       ,��ܹ߼�,
	private String bon_file;			//��������,�系����,       ,       ,��ܹ߼�,
	private String address;				//       ,       ,       ,       ,��ܹ߼�,
	private String tel;					//       ,       ,       ,       ,��ܹ߼�,
	private String fax;					//       ,       ,       ,       ,��ܹ߼�,
	private String firm_name;			//��������,       ,�系�߼�,       ,��ܹ߼�,
	private String representative;		//��������,       ,�系�߼�,       ,��ܹ߼�,
	private String etc;					//��������,       ,       ,       ,��ܹ߼�,
	
	private String delete_date;			//����:��������	
	private String file_path;			//����:����path		
	private String fname;				//����:���Ͽ�����	
	private String sname;				//����:���������		
	private String ftype;				//����:����Ȯ���ڸ�	
	private String fsize;				//����:����ũ��	
	private String flag;				//����:���ڰ������
	private String app_id;				//����:���ڰ������ �����ڵ�
	private String app_date;			//����:���ڰ������ ����

	private String module_name;			//��������,        ,�系�߼�,��ܼ���,       ,	
	private String module_add;			//       ,        ,�系�߼�,��ܼ���,       ,	
	private String mail;				//��������,        ,       ,       ,       ,
	private String mail_add;			//��������,        ,       ,       ,       ,

	private String modify;				//�������
	private String delete;				//�������
	private String distribute;			//�������
		
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

	//private int serial_no;				//��������,�系����,�系�߼�,��ܼ���,��ܹ߼�,
	public void setSerialNo(String serial_no)	{ this.serial_no = serial_no;	}
	public String getSerialNo()					{ return this.serial_no;		}

	//private String class_no;			//��������,�系����,�系�߼�,��ܼ���,��ܹ߼�,
	public void setClassNo(String class_no)		{ this.class_no = class_no;	}
	public String getClassNo()					{ return this.class_no;		}

	//private String doc_id;				//��������,�ó�����,�系�߼�,��ܼ���,��ܹ߼�,
	public void setDocId(String doc_id)			{ this.doc_id = doc_id;	}
	public String getDocId()					{ return this.doc_id;		}

	//private String slogan;				//��������,       ,�系�߼�,       ,��ܹ߼�,
	public void setSlogan(String slogan)		{ this.slogan = slogan;	}
	public String getSlogan()					{ return this.slogan;		}

	//private String title_name;			//��������,       ,�系�߼�,       ,��ܹ߼�,
	public void setTitleName(String title_name)	{ this.title_name = title_name;	}
	public String getTitleName()				{ return this.title_name;		}

	//private String in_date;				//��������,�系����,�系�߼�,��ܼ���,��ܹ߼�,
	public void setInDate(String in_date)		{ this.in_date = in_date;	}
	public String getInDate()					{ return this.in_date;		}

	//private String send_date;			//		 ,		 ,       ,��ܼ���,��ܹ߼�,
	public void setSendDate(String send_date)	{ this.send_date = send_date;	}
	public String getSendDate()					{ return this.send_date;		}

	//private String enforce_date;		//��������,�系����,�系�߼�,��ܼ���,��ܹ߼�,
	public void setEnforceDate(String enforce_date)		{ this.enforce_date = enforce_date;	}
	public String getEnforceDate()				{ return this.enforce_date;		}

	//private String receive;				//��������,�系����,�系�߼�,��ܼ���,��ܹ߼�,
	public void setReceive(String receive)		{ this.receive = receive;	}
	public String getReceive()					{ return this.receive;		}

	//private String reference;			//       ,       ,�系�߼�,       ,��ܹ߼�,
	public void setReference(String reference)	{ this.reference = reference;	}
	public String getReference()				{ return this.reference;		}

	//private String sending;				//��������,�系����,�系�߼�,��ܼ���,��ܹ߼�,
	public void setSending(String sending)		{ this.sending = sending;	}
	public String getSending()					{ return this.sending;		}

	//private int sheet_cnt;				//		 ,		 ,       ,��ܼ���,��ܹ߼�,
	public void setSheetCnt(String sheet_cnt)	{ this.sheet_cnt = sheet_cnt;	}
	public String getSheetCnt()					{ return this.sheet_cnt;		}

	//private String receive_div;			//		 ,		 ,       ,��ܼ���,       ,
	public void setReceiveDiv(String receive_div)	{ this.receive_div = receive_div;	}
	public String getReceiveDiv()				{ return this.receive_div;		}

	//private String receive_id;			//		 ,		 ,       ,��ܼ���,       ,
	public void setReceiveId(String receive_id)	{ this.receive_id = receive_id;	}
	public String getReceiveId()				{ return this.receive_id;		}

	//private String receive_name;		//		 ,		 ,       ,��ܼ���,       ,
	public void setReceiveName(String receive_name)		{ this.receive_name = receive_name;	}
	public String getReceiveName()				{ return this.receive_name;		}

	//private String receive_rank;		//		 ,		 ,       ,��ܼ���,       ,
	public void setReceiveRank(String receive_rank)		{ this.receive_rank = receive_rank;	}
	public String getReceiveRank()				{ return this.receive_rank;		}

	//private String rec_company;			//       ,       ,       ,       ,��ܹ߼�,
	public void setRecCompany(String rec_company)		{ this.rec_company = rec_company;	}
	public String getRecCompany()				{ return this.rec_company;		}

	//private String rec_id;				//       ,       ,�系�߼�,       ,       ,
	public void setRecId(String rec_id)			{ this.rec_id = rec_id;	}
	public String getRecId()					{ return this.rec_id;		}

	//private String rec_name;			//       ,       ,�系�߼�,       ,��ܹ߼�,
	public void setRecName(String rec_name)		{ this.rec_name = rec_name;	}
	public String getRecName()					{ return this.rec_name;		}

	//private String rec_rank;			//       ,       ,�系�߼�,       ,       ,
	public void setRecRank(String rec_rank)		{ this.rec_rank = rec_rank;	}
	public String getRecRank()					{ return this.rec_rank;		}

	//private String rec_code;			//       ,       ,�系�߼�,       ,       ,
	public void setRecCode(String rec_code)		{ this.rec_code = rec_code;	}
	public String getRecCode()					{ return this.rec_code;		}

	//private String rec_division;		//       ,       ,�系�߼�,       ,       ,
	public void setRecDivision(String rec_division)		{ this.rec_division = rec_division;	}
	public String getRecDivision()				{ return this.rec_division;		}

	//private String rec_mail;			//       ,       ,       ,       ,��ܹ߼�,
	public void setRecMail(String rec_mail)		{ this.rec_mail = rec_mail;	}
	public String getRecMail()					{ return this.rec_mail;		}

	//private String subject;				//��������,�系����,       ,��ܼ���,��ܹ߼�,	
	public void setSubject(String subject)		{ this.subject = subject;	}
	public String getSubject()					{ return this.subject;		}

	//private String bon_path;			//��������,�系����,       ,       ,��ܹ߼�,
	public void setBonPath(String bon_path)		{ this.bon_path = bon_path;	}
	public String getBonPath()					{ return this.bon_path;		}

	//private String bon_file;			//��������,�系����,       ,       ,��ܹ߼�,
	public void setBonFile(String bon_file)		{ this.bon_file = bon_file;	}
	public String getBonFile()					{ return this.bon_file;		}

	//private String address;				//       ,       ,       ,       ,��ܹ߼�,
	public void setAddress(String address)		{ this.address = address;	}
	public String getAddress()					{ return this.address;		}

	//private String tel;					//       ,       ,       ,       ,��ܹ߼�,
	public void setTel(String tel)				{ this.tel = tel;	}
	public String getTel()						{ return this.tel;		}

	//private String fax;					//       ,       ,       ,       ,��ܹ߼�,
	public void setFax(String fax)				{ this.fax = fax;	}
	public String getFax()						{ return this.fax;		}

	//private String firm_name;			//��������,       ,�系�߼�,       ,��ܹ߼�,
	public void setFirmName(String firm_name)	{ this.firm_name = firm_name;	}
	public String getFirmName()					{ return this.firm_name;		}

	//private String representative;		//��������,       ,�系�߼�,       ,��ܹ߼�,
	public void setRepresentative(String representative)	{ this.representative = representative;	}
	public String getRepresentative()			{ return this.representative;		}

	//private String etc;					//��������,       ,       ,       ,��ܹ߼�,
	public void setEtc(String etc)				{ this.etc = etc;	}
	public String getEtc()						{ return this.etc;		}

	
	//private String delete_date;			//����:��������	
	public void setDeleteDate(String delete_date)		{ this.delete_date = delete_date;	}
	public String getDeleteDate()				{ return this.delete_date;		}

	//private String file_path;			//����:����path		
	public void setFilePath(String file_path)		{ this.file_path = file_path;	}
	public String getFilePath()					{ return this.file_path;		}

	//private String fname;				//����:���Ͽ�����	
	public void setFname(String fname)			{ this.fname = fname;	}
	public String getFname()					{ return this.fname;		}

	//private String sname;				//����:���������		
	public void setSname(String sname)			{ this.sname = sname;	}
	public String getSname()					{ return this.sname;		}

	//private String ftype;				//����:����Ȯ���ڸ�	
	public void setFtype(String ftype)			{ this.ftype = ftype;	}
	public String getFtype()					{ return this.ftype;		}

	//private String fsize;				//����:����ũ��	
	public void setFsize(String fsize)			{ this.fsize = fsize;	}
	public String getFsize()					{ return this.fsize;		}

	//private String flag;				//����:���ڰ������
	public void setFlag(String flag)			{ this.flag = flag;	}
	public String getFlag()						{ return this.flag;		}

	//private String app_date;			//����:���ڰ��� ���ΰ����ڵ�
	public void setAppId(String app_id)			{ this.app_id = app_id;	}
	public String getAppId()					{ return this.app_id;		}

	//private String app_date;			//����:���ڰ��� ��������
	public void setAppDate(String app_date)		{ this.app_date = app_date;	}
	public String getAppDate()					{ return this.app_date;		}

	//private String module_name;			//��������,        ,�系�߼�,��ܼ���,       ,	
	public void setModuleName(String module_name)		{ this.module_name = module_name;	}
	public String getModuleName()				{ return this.module_name;		}

	//private String module_add;			//       ,        ,�系�߼�,��ܼ���,       ,	
	public void setModuleAdd(String module_add)		{ this.module_add = module_add;	}
	public String getModuleAdd()				{ return this.module_add;		}

	//private String mail;				//��������,        ,       ,       ,       ,
	public void setMail(String mail)			{ this.mail = mail;	}
	public String getMail()						{ return this.mail;		}

	//private String mail_add;			//��������,        ,       ,       ,       ,
	public void setMailAdd(String mail_add)		{ this.mail_add = mail_add;	}
	public String getMailAdd()					{ return this.mail_add;		}

	//private String modify;				//�������
	public void setModify(String modify)		{ this.modify = modify;	}
	public String getModify()					{ return this.modify;		}

	//private String delete;				//�������
	public void setDelete(String delete)		{ this.delete = delete;	}
	public String getDelete()					{ return this.delete;		}
	
	//private String distribute;			//�������
	public void setDistribute(String distribute){ this.distribute = distribute;	}
	public String getDistribute()				{ return this.distribute;		}
}

