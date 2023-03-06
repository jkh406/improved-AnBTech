package com.anbtech.admin.entity;

public class UserInfoTable{
	//기본정보
	private String no;						// 관리번호
	private String user_id;					// 사번
	private String user_name;				// 이름
	private String user_rank;				// 직급
	private String division;				// 부서명
	private String passwd;					// 패스워드
	private String email;					// 이메일 주소
	private String enter_day;				// 입사일
	
	private String ac_code;					// 부서코드
	private String ac_name;					// 부서이름
	private String ac_id;					// 부서 관리번호
	private String ar_code;					// 직급코드
	private String code;					// 부서코드 (내부관리코드)

	//엑세스권한관련
	private String access_code;				// 문서 엑세스 권한

	//근태관련
	private String hyuga_rest_day;			// 휴가 잔량
	private String continuous_year;			// 근속 년한
	private String hyuga_year_rest_day;		// 년차 잔량
	private String hyuga_month_rest_day;	// 월차 잔량

	//수당관련
	private String hourly_pay;				// 시급 금액


	//기본정보
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

	//기술문서엑세스권한관련
	public String getAccessCode(){		return access_code;	}
	public void setAccessCode(String access_code){		this.access_code = access_code;	}

	//근태관련
	public String getHyuGaYearRestDay(){		return hyuga_year_rest_day;	}
	public void setHyuGaYearRestDay(String hyuga_year_rest_day){		this.hyuga_year_rest_day=hyuga_year_rest_day;	}

	public String getHyuGaMonthRestDay(){		return hyuga_month_rest_day;	}
	public void setHyuGaMonthRestDay(String hyuga_month_rest_day){		this.hyuga_month_rest_day=hyuga_month_rest_day;	}

	public String getHyuGaRestDay(){		return hyuga_rest_day;	}
	public void setHyuGaRestDay(String hyuga_rest_day){		this.hyuga_rest_day = hyuga_rest_day;	}

	public String getContinuousYear(){		return continuous_year;	}
	public void setContinuousYear(String continuous_year){		this.continuous_year = continuous_year;	}

	//수당관련
	public String getHourlyPay(){		return hourly_pay;	}
	public void setHourlyPay(String hourly_pay){		this.hourly_pay = hourly_pay;	}
}