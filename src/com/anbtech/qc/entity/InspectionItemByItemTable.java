package com.anbtech.qc.entity;

public class InspectionItemByItemTable {
	private String mid;
	private String factory_code;
	private String factory_name;
	private String item_code;
	private String item_code_link;
	private String item_name;
	private String item_desc;
	private String inspection_class_code;
	private String inspection_class_name;
	private String inspection_code;
	private String inspection_name;
	private String inspection_result_type;
	private String inspection_order;
	private String inspection_type_code;
	private String inspection_type_name;
	private String inspection_grade;
	private String low_standard;
	private String upper_standard;

	private String sampled_quantity;	//시료수
	private String good_quantity;		//합격수량
	private String bad_quantity;		//불합격수량


	public String getMid() { return mid; }
	public void setMid(String string) { mid = string; }

	public String getFactoryCode() { return factory_code; }
	public void setFactoryCode(String string) { factory_code = string; }

	public String getFactoryName() { return factory_name; }
	public void setFactoryName(String string) { factory_name = string; }

	public String getItemCode() { return item_code; }
	public void setItemCode(String string) { item_code = string; }

	public String getItemCodeLink() { return item_code_link; }
	public void setItemCodeLink(String string) { item_code_link = string; }

	public String getItemName() { return item_name; }
	public void setItemName(String string) { item_name = string; }

	public String getItemDesc() { return item_desc; }
	public void setItemDesc(String string) { item_desc = string; }

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

	public String getInspectionOrder() { return inspection_order; }
	public void setInspectionOrder(String string) { inspection_order = string; }

	public String getInspectionTypeCode() { return inspection_type_code; }
	public void setInspectionTypeCode(String string) { inspection_type_code = string; }

	public String getInspectionTypeName() { return inspection_type_name; }
	public void setInspectionTypeName(String string) { inspection_type_name = string; }

	public String getInspectionGrade() { return inspection_grade; }
	public void setInspectionGrade(String string) { inspection_grade = string; }

	public String getLowStandard() { return low_standard; }
	public void setLowStandard(String string) { low_standard = string; }

	public String getUpperStandard() { return upper_standard; }
	public void setUpperStandard(String string) { upper_standard = string; }

	public String getSampledQuantity() { return sampled_quantity; }
	public void setSampledQuantity(String string) { sampled_quantity = string; }

	public String getGoodQuantity() { return good_quantity; }
	public void setGoodQuantity(String string) { good_quantity = string; }

	public String getBadQuantity() { return bad_quantity; }
	public void setBadQuantity(String string) { bad_quantity = string; }
}
