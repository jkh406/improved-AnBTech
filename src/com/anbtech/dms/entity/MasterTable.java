/************************************************************
 * 
 * master_data ���̺� ������ set/get
 *
 ************************************************************/

package com.anbtech.dms.entity;

public class MasterTable{

	private int m_id;				// ������ȣ
	private String doc_no;			// ������ȣ
	private String category_id;		// ī�װ� �ڵ�
	private String where_category;	// ���� ī�װ��� �з� ǥ��
	private String data_id;			// ���� ������ȣ(���ʹ����� ��)
	private String subject;			// ���� ����
	private String writer;			// �ۼ���(��������)
	private String written_day;		// �ۼ���(��������)
	private String register;		// �����(��������)
	private String register_day;	// �����(��������)
	private String search_keyword;	// �˻���
	private String hit;				// ����Ƚ��
	private String last_version;	// ��������
	private String stat;			// ������ ���� ����
	private String model_code;		// ��������(��)�ڵ�
	private String model_name;		// ��������(��)��
	private String pjt_code;		// ����������Ʈ�ڵ�
	private String pjt_name;		// ����������Ʈ��
	private String node_code;		// ���ù��������ڵ�
	private String node_name;		// ���ù���������

	public int getMid(){
		return m_id;
	}
	public void setMid(int m_id){
		this.m_id = m_id;
	}

	public String getDocNo(){
		return doc_no;
	}
	public void setDocNo(String doc_no){
		this.doc_no = doc_no;
	}

	public String getCategoryId(){
		return category_id;
	}
	public void setCategoryId(String category_id){
		this.category_id = category_id;
	}

	public String getWhereCategory(){
		return where_category;
	}
	public void setWhereCategory(String where_category){
		this.where_category = where_category;
	}

	public String getDataId(){
		return data_id;
	}
	public void setDataId(String data_id){
		this.data_id = data_id;
	}

	public String getSubject(){
		return subject;
	}
	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getWriter(){
		return writer;
	}
	public void setWriter(String writer){
		this.writer = writer;
	}

	public String getWrittenDay(){
		return written_day;
	}
	public void setWrittenDay(String written_day){
		this.written_day = written_day;
	}

	public String getRegister(){
		return register;
	}
	public void setRegister(String register){
		this.register = register;
	}

	public String getRegisterDay(){
		return register_day;
	}
	public void setRegisterDay(String register_day){
		this.register_day = register_day;
	}

	public String getSearchKeyword(){
		return search_keyword;
	}
	public void setSearchKeyword(String search_keyword){
		this.search_keyword = search_keyword;
	}

	public String getHit(){
		return hit;
	}
	public void setHit(String hit){
		this.hit = hit;
	}

	public String getLastVersion(){
		return last_version;
	}
	public void setLastVersion(String last_version){
		this.last_version = last_version;
	}

	public String getStat(){
		return stat;
	}
	public void setStat(String stat){
		this.stat = stat;
	}

	public String getModelCode(){
		return model_code;
	}
	public void setModelCode(String model_code){
		this.model_code = model_code;
	}

	public String getModelName(){
		return model_name;
	}
	public void setModelName(String model_name){
		this.model_name = model_name;
	}

	public String getPjtCode(){
		return pjt_code;
	}
	public void setPjtCode(String pjt_code){
		this.pjt_code = pjt_code;
	}

	public String getPjtName(){
		return pjt_name;
	}
	public void setPjtName(String pjt_name){
		this.pjt_name = pjt_name;
	}

	public String getNodeCode(){
		return node_code;
	}
	public void setNodeCode(String node_code){
		this.node_code = node_code;
	}

	public String getNodeName(){
		return node_name;
	}
	public void setNodeName(String node_name){
		this.node_name = node_name;
	}
}