package com.anbtech.admin.entity;

public class UserInfoTable{
	//�⺻����
	private String no;						// ������ȣ
	private String user_id;					// ���
	private String user_name;				// �̸�
	private String user_rank;				// ����
	private String division;				// �μ���
	private String passwd;					// �н�����
	private String email;					// �̸��� �ּ�
	private String enter_day;				// �Ի���
	
	private String ac_code;					// �μ��ڵ�
	private String ac_name;					// �μ��̸�
	private String ac_id;					// �μ� ������ȣ
	private String ar_code;					// �����ڵ�
	private String code;					// �μ��ڵ� (���ΰ����ڵ�)

	//���������Ѱ���
	private String access_code;				// ���� ������ ����

	//���°���
	private String hyuga_rest_day;			// �ް� �ܷ�
	private String continuous_year;			// �ټ� ����
	private String hyuga_year_rest_day;		// ���� �ܷ�
	private String hyuga_month_rest_day;	// ���� �ܷ�

	//�������
	private String hourly_pay;				// �ñ� �ݾ�


	//�⺻����
	public String getNo(){		return no;	}
	public void setNo(String no){	this.no = no;	}

	public String getUserId(){		return user_id;	}
	public void setUserId(String user_id){		this.user_id = user_id;	}

	public String getUserName(){		return user_name;	}
	public void setUserName(String user_name){		this.user_name = user_name;	}

	public String getUserRank(){		return user_rank;	}
	public void setUserRank(String user_rank){		this.user_rank = user_rank;	}

	public String getDivision(){		return division;	}
	public void setDivision(String division){		this.division = division;	}

	public String getPasswd(){		return passwd;	}
	public void setPasswd(String passwd){		this.passwd = passwd;	}

	public String getEmail(){		return email;	}
	public void setEmail(String email){		this.email = email;	}

	public String getEnterDay(){		return enter_day;	}
	public void setEnterDay(String enter_day){		this.enter_day = enter_day;	}

	public void setAcCode(String ac_code){		this.ac_code=ac_code;	}
	public String getAcCode(){		return ac_code;	}

	public void setAcName(String ac_name){		this.ac_name=ac_name;	}
	public String getAcName(){		return ac_name;	}

	public void setAcId(String ac_id){		this.ac_id=ac_id;	}
	public String getAcId(){		return ac_id;	}

	public void setArCode(String ar_code){		this.ar_code=ar_code;	}
	public String getArCode(){		return ar_code;	}

    public void setCode(String code){		this.code = code;	}
	public String getCode(){		return code;	}

	//����������������Ѱ���
	public String getAccessCode(){		return access_code;	}
	public void setAccessCode(String access_code){		this.access_code = access_code;	}

	//���°���
	public String getHyuGaYearRestDay(){		return hyuga_year_rest_day;	}
	public void setHyuGaYearRestDay(String hyuga_year_rest_day){		this.hyuga_year_rest_day=hyuga_year_rest_day;	}

	public String getHyuGaMonthRestDay(){		return hyuga_month_rest_day;	}
	public void setHyuGaMonthRestDay(String hyuga_month_rest_day){		this.hyuga_month_rest_day=hyuga_month_rest_day;	}

	public String getHyuGaRestDay(){		return hyuga_rest_day;	}
	public void setHyuGaRestDay(String hyuga_rest_day){		this.hyuga_rest_day = hyuga_rest_day;	}

	public String getContinuousYear(){		return continuous_year;	}
	public void setContinuousYear(String continuous_year){		this.continuous_year = continuous_year;	}

	//�������
	public String getHourlyPay(){		return hourly_pay;	}
	public void setHourlyPay(String hourly_pay){		this.hourly_pay = hourly_pay;	}
}