package com.anbtech.pu.business;

import java.util.*;
import java.io.*;

public class PurchaseCodeNameBO{
	public PurchaseCodeNameBO(){

	}

	/*****************************************************************
	 * 상태코드를 받아서 상태 문자열을 만들어 준다.
	 *****************************************************************/
	public String getStatus(String stat) throws Exception{

		String status = "";

		if(stat.equals("S01"))		status = "구매요청";	//최초 상태
		else if(stat.equals("S02")) status = "결재상신";	//구매요청 결재 상신 상태
		else if(stat.equals("S03")) status = "발주접수";	//구매요청 결재가 완료되어 구매부서로 넘어온 상태
		else if(stat.equals("S05")) status = "발주준비";	//구매부서에서 발주하기 위해 발주서를 작성한 상태
		else if(stat.equals("S06")) status = "일부발주";	//
		else if(stat.equals("S09")) status = "결재상신";	//구매부서에서 작성한 발주서를 결재상신한 상태
		else if(stat.equals("S13")) status = "발주완료";	//결재완료된 발주
		else if(stat.equals("S18")) status = "입고등록";	//입고등록처리중인 상태
		else if(stat.equals("S19")) status = "입고상신";	//입고결재상신중인 상태
		else if(stat.equals("S21"))	status = "검사전입고";	//입고결재승인인 완료된 상태
		else if(stat.equals("S22"))	status = "일부입고";	//
		else if(stat.equals("S25"))	status = "입고완료";	//입고품목의 품질검사가 완료된상태

		return status;
	}
}