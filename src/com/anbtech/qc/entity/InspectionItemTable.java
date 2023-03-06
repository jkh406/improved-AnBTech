package com.anbtech.qc.entity;

public class InspectionItemTable {
	private String mid;
	private String inspection_class_code;
	private String inspection_class_name;
	private String inspection_code;
	private String inspection_name;
	private String inspection_result_type;

	public String getMid() { return mid; }
	public void setMid(String string) { mid = string; }

	public String getInspectionClassCode() { return inspection_class_code; }
	public void setInspectionClassCode(String string) { inspection_class_code = string; }

	public String getInspectionClassName() { return inspection_class_name; }
	public void setInspectionClassName(String string) { inspection_class_name = string; }

	public String getInspectionCode() { return inspection_code; }
	public void setInspectionCode(String string) { inspection_code = string; }

	public String getInspectionName() { return inspection_name; }
	public void setInspectionName(String string) { inspection_name = string; }

	public String getInspectionResultType() { return inspection_result_type; }
	public void setInspectionResultType(String string) { inspection_result_type = string; }
}
