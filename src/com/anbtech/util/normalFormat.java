package com.anbtech.util;
import java.text.DecimalFormat;


public class normalFormat extends Object {
	DecimalFormat fmt;
	private String format="";	//��Ʈ�� ��� ���� 
	
	//------------------------------
	// ������ (setFormat�ʿ�)
	//------------------------------
	public normalFormat() {
		
	}

	//------------------------------
	// ������ (����� format����)
	//------------------------------	
	public normalFormat(String format) {
		setFormat(format);	
	}


	//------------------------------
	// ����� format ����
	//------------------------------
	public void setFormat(String format) {
		this.format = format;
		fmt = new DecimalFormat(format);	
	}

	//------------------------------
	// ���Խ� �Ϸù�ȣ �����ϱ� (10����)
	//------------------------------
	public String toDigits(int decimal) {
		return fmt.format(decimal);	
	}

	//------------------------------
	// double�� ��Ʈ������ �ٲٱ�
	//------------------------------
	public String DoubleToString(double number) {
		return fmt.format(number);	
	}

	//------------------------------
	// ��Ʈ���� ��Ʈ������ �ٲٱ�
	//------------------------------
	public String StringToString(String number) {
		return DoubleToString(Double.parseDouble(number));	
	}
}
