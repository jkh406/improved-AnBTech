// member_payinfo Table
package com.anbtech.ew.entity;


public class MemberPayInfoTable
{

	private int		p_no;			// ������ȣ
	private String	id;				// ID
	private String	name;			// �̸�
	private String	rank;			// ����
	private String  rank_name;		// ���޸�
	private String	division;		// �μ�
	private String  division_name;  // �μ���
	private String	emp_type;		// ��� ���� �ڵ�
	private String  emp_name;		// ��� ���� ��
	private String	salary_type;	// �޿� ���� �ڵ�
	private String  salary_name;	// �޿� ���� ��
	private String  salary_kind;	// �ϴ� ���� ����(�⺻��-ds or ����-ts)
	private String  salary_kindname;
	private String  yearly_pay;		// ���
	private String  monthly_pay;    // ����
	private String  hourly_pay;     // �ñ�
	private String  basic_pay;  // �⺻��

	
	// setter.....
	public void setPno(int p_no){
		this.p_no = p_no;
	}
	public void setId(String id){
		this.id = id;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setRank(String rank){
		this.rank = rank;
	}
	public void setDivision(String division){
		this.division = division;
	}
	public void setEmpType(String emp_type){
		this.emp_type = emp_type;
	}
	public void setSalaryType(String salary_type){
		this.salary_type = salary_type;
	}
	public void setYearlyPay(String yearly_pay){
		this.yearly_pay = yearly_pay;
	}
	public void setMonthlyPay(String monthly_pay){
		this.monthly_pay = monthly_pay;
	}
	public void setHourlyPay(String hourly_pay){
		this.hourly_pay = hourly_pay;
	}

	public void setRankName(String rank_name){
		this.rank_name = rank_name;
	}
	public void setDivisionName(String division_name){
		this.division_name = division_name;
	}
	public void setEmpName(String emp_name){
		this.emp_name = emp_name;
	}
	public void setSalaryName(String salary_name){
		this.salary_name = salary_name;
	}
	public void setSalaryKind(String salary_kind){
		this.salary_kind = salary_kind;
	}
	public void setSalaryKindName(String salary_kindname){
		this.salary_kindname = salary_kindname;
	}
	public void setBasicPay(String basic_pay) {
		this.basic_pay = basic_pay;
	}


	//  getter...
	public int getPno(){
		return p_no;
	}
	public String getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getRank(){
		return rank;
	}
	public String getDivision(){
		return division;
	}
	public String getEmpType(){
		return emp_type;
	}
	public String getSalaryType(){
		return salary_type;
	}
	public String getYearlyPay(){
		return yearly_pay;
	}
	public String getMonthlyPay(){
		return monthly_pay;
	}
	public String getHourlyPay(){
		return hourly_pay;
	}

	public String getRankName(){
		return rank_name;
	}
	public String getDivisionName(){
		return division_name;
	}
	public String getEmpName(){
		return emp_name;
	}
	public String getSalaryName(){
		return salary_name;
	}
	public String getSalaryKind(){
		return salary_kind;
	}
	public String getSalaryKindName(){
		return salary_kindname;
	}
	public String getBasicPay(){
		return basic_pay;
	}

	public static void main(String[] args) 
	{
		System.out.println("Hello World!");
	}
}
