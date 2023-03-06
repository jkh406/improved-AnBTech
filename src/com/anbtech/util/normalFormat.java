package com.anbtech.util;
import java.text.DecimalFormat;


public class normalFormat extends Object {
	DecimalFormat fmt;
	private String format="";	//스트링 출력 포맷 
	
	//------------------------------
	// 생성자 (setFormat필요)
	//------------------------------
	public normalFormat() {
		
	}

	//------------------------------
	// 생성자 (출력할 format정의)
	//------------------------------	
	public normalFormat(String format) {
		setFormat(format);	
	}


	//------------------------------
	// 출력할 format 정의
	//------------------------------
	public void setFormat(String format) {
		this.format = format;
		fmt = new DecimalFormat(format);	
	}

	//------------------------------
	// 정규식 일련번호 생성하기 (10진수)
	//------------------------------
	public String toDigits(int decimal) {
		return fmt.format(decimal);	
	}

	//------------------------------
	// double을 스트링으로 바꾸기
	//------------------------------
	public String DoubleToString(double number) {
		return fmt.format(number);	
	}

	//------------------------------
	// 스트링을 스트링으로 바꾸기
	//------------------------------
	public String StringToString(String number) {
		return DoubleToString(Double.parseDouble(number));	
	}
}
