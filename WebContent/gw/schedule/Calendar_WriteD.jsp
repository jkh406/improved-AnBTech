<%@ include file="../../admin/configHead.jsp"%>
<%@ page	
	language = "java"
	info= "�μ����� �ۼ��ϱ�"		
	contentType = "text/html;charset=euc-kr"  	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
%>
<%@	page import="com.anbtech.text.Hanguel"	%>
<%@	page import="com.anbtech.date.anbDate"	%>
<%@	page import="com.anbtech.text.StringProcess"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	//login ���� ����
	String id="";					//login

	//�޽��� ���޺���
	String Message="";				//�޽��� ���� ����  
	String FLAG = "";				//ȸ��/�μ����� ����
	String DIV_ID = "";				//�μ������� �μ�ID

	//ȭ����¿� ���޺���
	String[] Aitem;					//�����׸� (�ѱ��׸�)
	String[] Eitem;					//�����׸� (�����׸� : ���ε����� ó����)
	int items_cnt=0;				//�����׸� �Ѱ���
	String toDay="";				//���� ��/��/�� ǥ��
	String wName="";				//��ϴ���� ��
	String hrs="";					//���� �ð�(hh)

	//����� ���� (����)
	String uid="";					//���������� ���
	String una="";					//���������� �̸�
	String udi="";					//���������� �μ���

	String eop="";					//��������
	String eit="";					//�����׸�

	String sub="";					//����
	String con="";					//ȸ�ǳ���
	String mse="";					//�������û���

	//����� ���� (�ɼ�:�����׸񺰷�)
	String mna="";					//�����ڸ�(ȸ��,����)
	String hda="";					//�����
	String mro="";					//���			(ȸ��,���,����,�ٹ���,���,����)
	String mte="";					//��ȭ��ȣ		(ȸ��,����,�ް�,���,����)

	String prs="";					//������		(ȸ��,���)
	
	String mye="";					//���۽ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ,����)
	String mmo="";					//���۽ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ,����)
	String mda="";					//���۽ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ,����)

	String mti="";					//��ü �ð�		(ȸ��,���,���,�ް�,����,���,��Ÿ,����)

	String eye="";					//����ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)
	String emo="";					//����ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)
	String eda="";					//����ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)

	//������ ��������
	String DATA="";

stop : {
	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������

	/*********************************************************************
	 	����� login �˾ƺ���
	*********************************************************************/
	id = login_id; 			//������

	//�μ����� ������ ����
	String[] u_dbColumn={"a.id","a.name","b.ac_name"};	
	String query = "where (a.id ='"+id+"' and a.ac_id = b.ac_id)";
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(u_dbColumn);
	bean.setSearchWrite(query);
	bean.init_write();
	String wdn = "";
	while(bean.isAll()) wdn = bean.getData("ac_name");	//�ۼ��� �μ��� ã��
		
	//�μ����� ó���ϱ�
	String flag= request.getParameter("FLAG");				//ȸ��/�μ� ��������
	String div_id = request.getParameter("Sabun");			//�μ� �ڵ�
	if(div_id != null) {
		FLAG = "DIV";				//�μ��������� ������ DIV
		DIV_ID = div_id;			//�μ� ���� �ڵ�
	}
	wName = "�μ�����/"+wdn;

	//�μ� �ʱ�ȭ
	uid=una=udi=eop=eit=sub=mro=mna=mte=prs=mse=mye=mmo=mda=mti=con="";
	hda=eye=emo=eda=hrs="";

	/*****************************************************
	//ȭ����¿� �����׸� (����� �о� �迭�� ��´�.)
	*****************************************************/
	String[] itemColumns = {"id","item","nlist"};
	String item_data = "where (item='CIT') or (item='IIT' and id='" + id + "')"; 
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(itemColumns);
	bean.setSearchWrite(item_data);
	bean.init_write();
	
	String items = "";					//��ü �׸�LIST (������ : ';')
	while(bean.isAll()) {
		items += bean.getData("nlist");
	}	

	//�� �׸񰹼� ���ϱ�
	items_cnt = 0;						//�׸񰹼�
	for(int ii = 0; ii < items.length(); ii++)
		if(items.charAt(ii) == ';') items_cnt++;

	//�迭�� �׸���
	Aitem = new String[items_cnt];				//�迭�Ҵ�
	Eitem = new String[items_cnt];				//�迭�Ҵ�	
	int aj = 0;						//���� ������
	int ak = 0;						//�迭 ���ۼ�
	for(int ai = 0; ai < items.length(); ai++) {
		if(items.charAt(ai) == ';') {
			Aitem[ak] = items.substring(aj,ai);
			if(items.substring(aj,ai).equals("ȸ��")) Eitem[ak] = "Meeting";
			else if(items.substring(aj,ai).equals("���")) Eitem[ak] = "Appointment";
			else if(items.substring(aj,ai).equals("���")) Eitem[ak] = "Event";
			else if(items.substring(aj,ai).equals("��������")) Eitem[ak] = "TripH";
			else if(items.substring(aj,ai).equals("�ؿ�����")) Eitem[ak] = "TripA";
			else if(items.substring(aj,ai).equals("�ް�")) Eitem[ak] = "Vacation";
			else if(items.substring(aj,ai).equals("�����")) Eitem[ak] = "Holiday";
			else if(items.substring(aj,ai).equals("�湮")) Eitem[ak] = "Area";
			else if(items.substring(aj,ai).equals("����/���̳�")) Eitem[ak] = "Education";
			else Eitem[ak] = "Etc"+ak;

			aj = ai+1;				//���������
			ak++;
		}
	}

	/*****************************************************
	// ������ ����� �� �ð� ���ϱ� 
	*****************************************************/
	//����� ���ϱ�
	String tD = request.getParameter("DAY");	//from calendar_view.jsp �޷��� ���ڸ� ������
	if(tD == null)
		toDay = anbdt.getDate(0);
	else toDay = tD;
	
	//����� ���ϱ�
	hrs = anbdt.getHours();			//HH
	
	/*****************************************************
	//������ ���� �б� 
	*****************************************************/
	String Username = request.getParameter("hdUserName");
	if((Username == null) || (Username.length() == 0)) break stop;
	else {
		//--------------------------------------------------------------------------------	
		//�μ����� �����׸� �б�
		//--------------------------------------------------------------------------------	
		eop=Hanguel.toHanguel(request.getParameter("hdOpen"));							//��������
		String ditem=Hanguel.toHanguel(request.getParameter("hdAppointmentType"));		//�����׸�
	
		for(int si = 0; si < ak; si++) {
			if(ditem.equals("Meeting")) eit = "ȸ��";
			else if(ditem.equals("Appointment")) eit = "���";
			else if(ditem.equals("Event")) eit = "���";
			else if(ditem.equals("TripH")) eit = "��������";
			else if(ditem.equals("TripA")) eit = "�ؿ�����";
			else if(ditem.equals("Vacation")) eit = "�ް�";
			else if(ditem.equals("Holiday")) eit = "�����";
			else if(ditem.equals("Area")) eit = "�湮";
			else if(ditem.equals("Education")) eit = "����/���̳�";
			else {
				int ETC = Integer.parseInt(ditem.substring(3,ditem.length()));
				eit = Aitem[ETC];
			}
	
		} //for

		sub=Hanguel.toHanguel(request.getParameter("hdSubject"));						//����
		con=Hanguel.toHanguel(request.getParameter("Body"));							//ȸ�ǳ���

		//--------------------------------------------------------------------------------	
		//�ɼ��׸� �б� (�����׸񺰷� �ٸ�)
		//--------------------------------------------------------------------------------	
		if(ditem.equals("Meeting")) {			//ȸ��
			mro=Hanguel.toHanguel(request.getParameter("hdLocation"));					//ȸ�����
			mna=Hanguel.toHanguel(request.getParameter("hdChair"));						//ȸ�� �����ڸ�
			mte=Hanguel.toHanguel(request.getParameter("hdChairTel"));					//ȸ�� ������ ��ȭ��ȣ

			String Meetdate = request.getParameter("hdMeetDate");
			if(Meetdate != null) {
				int MFirst = Meetdate.indexOf("/");
				int MLast = Meetdate.lastIndexOf("/");
				mye=Hanguel.toHanguel(Meetdate.substring(0,MFirst));					//ȸ�ǽð� ��
				mmo=Hanguel.toHanguel(Meetdate.substring(MFirst+1,MLast));				//ȸ�ǽð� ��
				mda=Hanguel.toHanguel(Meetdate.substring(MLast+1,Meetdate.length()));	//ȸ�ǽð� ��
			}
			String StartTime = request.getParameter("hdStartTime");
			String EndTime = request.getParameter("hdEndTime");
			mti=StartTime + "~" + EndTime;												//ȸ�ǽð� �ð�
		} else if(ditem.equals("Appointment")) { //���
			prs=Hanguel.toHanguel(request.getParameter("hdMeetUsers"));					//��Ӵ����
			String Appointdate = request.getParameter("hdAppointDate");
			int AFirst = Appointdate.indexOf("/");
			int ALast = Appointdate.lastIndexOf("/");
			mte=Hanguel.toHanguel(request.getParameter("hdAppointTel"));				//����ó
			mye=Hanguel.toHanguel(Appointdate.substring(0,AFirst));						//��ӽð� ��
			mmo=Hanguel.toHanguel(Appointdate.substring(AFirst+1,ALast));				//��ӽð� ��
			mda=Hanguel.toHanguel(Appointdate.substring(ALast+1,Appointdate.length()));	//��ӽð� ��
			mti=request.getParameter("hdAppointTime");									//��ӽð�
			mro=Hanguel.toHanguel(request.getParameter("hdAppointLocation"));			//������
		} else if(ditem.equals("Event")) {		//���
			mro=Hanguel.toHanguel(request.getParameter("hdEventLocation"));				//������
			String Eventdate = request.getParameter("hdEventDate");
			int EFirst = Eventdate.indexOf("/");
			int ELast = Eventdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Eventdate.substring(0,EFirst));						//�����۽ð� ��
			mmo=Hanguel.toHanguel(Eventdate.substring(EFirst+1,ELast));					//�����۽ð� ��
			mda=Hanguel.toHanguel(Eventdate.substring(ELast+1,Eventdate.length()));		//�����۽ð� ��

			String Eventdate_1 = request.getParameter("hdEventDate_1");
			int EFirst_1 = Eventdate_1.indexOf("/");
			int ELast_1 = Eventdate_1.lastIndexOf("/");
			eye=Hanguel.toHanguel(Eventdate_1.substring(0,EFirst_1));					//��糡�ð� ��
			emo=Hanguel.toHanguel(Eventdate_1.substring(EFirst_1+1,ELast_1));			//��糡�ð� ��
			eda=Hanguel.toHanguel(Eventdate_1.substring(ELast_1+1,Eventdate_1.length()));//��糡�ð� ��

			String eStartTime = request.getParameter("hdEventTime");
			String eEndTime = request.getParameter("hdEventTime_1");
//			mti=eStartTime + "~" + eEndTime;											//���ð� �ð�
			if(mmo.equals(emo) && mda.equals(eda))										//�������϶� �ð���
				mti = eStartTime + "~" + eEndTime;
			else																//�����߾ƴϸ� ���ڸ� ǥ��
				mti=eStartTime + "~" + "[" + eda + "]" + eEndTime;
		} else if(ditem.equals("Vacation")) {		//�ް�
			mte=Hanguel.toHanguel(request.getParameter("hdUrgencyTel"));			//��޿���ó ��ȭ��ȣ
			String Vacationdate = request.getParameter("hdVacationDate");
			int VFirst = Vacationdate.indexOf("/");
			int VLast = Vacationdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Vacationdate.substring(0,VFirst));					//�ް����۽ð� ��
			mmo=Hanguel.toHanguel(Vacationdate.substring(VFirst+1,VLast));				//�ް����۽ð� ��
			mda=Hanguel.toHanguel(Vacationdate.substring(VLast+1,Vacationdate.length()));//�ް����۽ð� ��

			String Vacationdate_1 = request.getParameter("hdVacationDate_1");
			int VFirst_1 = Vacationdate_1.indexOf("/");
			int VLast_1 = Vacationdate_1.lastIndexOf("/");
			eye=Hanguel.toHanguel(Vacationdate_1.substring(0,VFirst_1));				//�ް����ð� ��
			emo=Hanguel.toHanguel(Vacationdate_1.substring(VFirst_1+1,VLast_1));		//�ް����ð� ��
			eda=Hanguel.toHanguel(Vacationdate_1.substring(VLast_1+1,Vacationdate_1.length()));	//�ް����ð� ��

			String vStartTime = request.getParameter("hdVaTime");
			String vEndTime = request.getParameter("hdVaTime_1");
//			mti=vStartTime + "~" + vEndTime;											//�ް��ð� 
			mti=mmo + "/" + mda + "~" + emo + "/" + eda;								//�ް��Ⱓ
		} else if(ditem.equals("TripH")) {			//��������
			mro=Hanguel.toHanguel(request.getParameter("hdTripLoc"));					//����ó
			mte=Hanguel.toHanguel(request.getParameter("hdTripTel"));					//��޿���ó ��ȭ��ȣ

			String Tripdate = request.getParameter("hdTripDate");
			int TFirst = Tripdate.indexOf("/");
			int TLast = Tripdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Tripdate.substring(0,TFirst));						//������۽ð� ��
			mmo=Hanguel.toHanguel(Tripdate.substring(TFirst+1,TLast));					//������۽ð� ��
			mda=Hanguel.toHanguel(Tripdate.substring(TLast+1,Tripdate.length()));		//������۽ð� ��

			String Tripdate_1 = request.getParameter("hdTripDate_1");
			int TFirst_1 = Tripdate_1.indexOf("/");
			int TLast_1 = Tripdate_1.lastIndexOf("/");
			eye=Hanguel.toHanguel(Tripdate_1.substring(0,TFirst_1));					//���峡�ð� ��
			emo=Hanguel.toHanguel(Tripdate_1.substring(TFirst_1+1,TLast_1));			//���峡�ð� ��
			eda=Hanguel.toHanguel(Tripdate_1.substring(TLast_1+1,Tripdate_1.length()));	//���峡�ð� ��

			String tStartTime = request.getParameter("hdTripTime");
			String tEndTime = request.getParameter("hdTripTime_1");
//			mti=tStartTime + "~" + tEndTime;											//����ð� �ð�
			if(mmo.equals(emo) && mda.equals(eda))							//�������� ���� �ð���
				mti = tStartTime + "~" + tEndTime;
			else															//�������� �ƴѰ��� ���ڸ� ǥ��
				mti=tStartTime + "~" + "[" + eda + "]" + tEndTime;
		} else if(ditem.equals("TripA")) {			//�ؿ�����
			mro=Hanguel.toHanguel(request.getParameter("hdTripALoc"));					//����ó
			mte=Hanguel.toHanguel(request.getParameter("hdTripATel"));					//��޿���ó ��ȭ��ȣ

			String Tripdate = request.getParameter("hdTripADate");
			int TFirst = Tripdate.indexOf("/");
			int TLast = Tripdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Tripdate.substring(0,TFirst));						//������۽ð� ��
			mmo=Hanguel.toHanguel(Tripdate.substring(TFirst+1,TLast));					//������۽ð� ��
			mda=Hanguel.toHanguel(Tripdate.substring(TLast+1,Tripdate.length()));		//������۽ð� ��

			String Tripdate_1 = request.getParameter("hdTripADate_1");
			int TFirst_1 = Tripdate_1.indexOf("/");
			int TLast_1 = Tripdate_1.lastIndexOf("/");
			eye=Hanguel.toHanguel(Tripdate_1.substring(0,TFirst_1));					//���峡�ð� ��
			emo=Hanguel.toHanguel(Tripdate_1.substring(TFirst_1+1,TLast_1));			//���峡�ð� ��
			eda=Hanguel.toHanguel(Tripdate_1.substring(TLast_1+1,Tripdate_1.length()));	//���峡�ð� ��

			String tStartTime = request.getParameter("hdTripATime");
			String tEndTime = request.getParameter("hdTripATime_1");
//			mti=tStartTime + "~" + tEndTime;											//����ð� �ð�
			if(mmo.equals(emo) && mda.equals(eda))							//�������� ���� �ð���
				mti = tStartTime + "~" + tEndTime;
			else															//�������� �ƴѰ��� ���ڸ� ǥ��
				mti=tStartTime + "~" + "[" + eda + "]" + tEndTime;
		} else if(ditem.equals("Holiday")) {		//�����
			hda=Hanguel.toHanguel(request.getParameter("hdHolidayDate"));				//�������
			mye=hda.substring(0,hda.indexOf("/"));										//�����۽ð� ��
			mmo=hda.substring(hda.indexOf("/")+1,hda.lastIndexOf("/"));					//�����۽ð� ��
			mda=hda.substring(hda.lastIndexOf("/")+1,hda.length());						//�����۽ð� ��
			mti=request.getParameter("hdHolidayTime");									//�ð�
		} else if(ditem.equals("Area")) {			//�湮
			mro=Hanguel.toHanguel(request.getParameter("hdArea"));					//�湮ó
			mte=Hanguel.toHanguel(request.getParameter("hdAreaTel"));					//��޿���ó ��ȭ��ȣ

			String Tripdate = request.getParameter("hdAreaDate");
			int TFirst = Tripdate.indexOf("/");
			int TLast = Tripdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Tripdate.substring(0,TFirst));						//�湮���۽ð� ��
			mmo=Hanguel.toHanguel(Tripdate.substring(TFirst+1,TLast));					//�湮���۽ð� ��
			mda=Hanguel.toHanguel(Tripdate.substring(TLast+1,Tripdate.length()));		//�湮���۽ð� ��

			String Tripdate_1 = request.getParameter("hdAreaDate_1");
			int TFirst_1 = Tripdate_1.indexOf("/");
			int TLast_1 = Tripdate_1.lastIndexOf("/");
			eye=Hanguel.toHanguel(Tripdate_1.substring(0,TFirst_1));					//�湮���ð� ��
			emo=Hanguel.toHanguel(Tripdate_1.substring(TFirst_1+1,TLast_1));			//�湮���ð� ��
			eda=Hanguel.toHanguel(Tripdate_1.substring(TLast_1+1,Tripdate_1.length()));	//�湮���ð� ��

			String tStartTime = request.getParameter("hdAreaTime");
			String tEndTime = request.getParameter("hdAreaTime_1");
			if(mmo.equals(emo) && mda.equals(eda))							//�������� ���� �ð���
				mti = tStartTime + "~" + tEndTime;
			else															//�������� �ƴѰ��� ���ڸ� ǥ��
				mti=tStartTime + "~" + "[" + eda + "]" + tEndTime;
/*
			//�ð��� ���� ���ڷ� �Է�
			mye=anbdt.getYear();														//�湮���۽ð� ��
			mmo=anbdt.getMonth();														//�湮���۽ð� ��
			mda=anbdt.getDates();														//�湮���۽ð� ��
			mro=Hanguel.toHanguel(request.getParameter("hdArea"));						//�湮��
			mti=mro;																	//�湮�� ǥ��
*/
		} else if(ditem.equals("Education")) {			//����/���̳�
			mro=Hanguel.toHanguel(request.getParameter("hdEduLocation"));				//�������
			mna=Hanguel.toHanguel(request.getParameter("hdEduChair"));					//�����ְ��� ��
			mte=Hanguel.toHanguel(request.getParameter("hdEduChairTel"));				//��ȭ��ȣ

			String Educationdate = request.getParameter("hdEducationDate");
			if(Educationdate != null) {
				int MFirst = Educationdate.indexOf("/");
				int MLast = Educationdate.lastIndexOf("/");
				mye=Hanguel.toHanguel(Educationdate.substring(0,MFirst));						//��
				mmo=Hanguel.toHanguel(Educationdate.substring(MFirst+1,MLast));					//��
				mda=Hanguel.toHanguel(Educationdate.substring(MLast+1,Educationdate.length()));	//��
			}
			String StartTime = request.getParameter("hdEduStartTime");
			String EndTime = request.getParameter("hdEduEndTime");
			mti=StartTime + "~" + EndTime;	
		} else  {									//��Ÿ
			String Etcdate = request.getParameter("hdEtcDate");
			int EtcFirst = Etcdate.indexOf("/");
			int EtcLast = Etcdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Etcdate.substring(0,EtcFirst));						//��Ÿ���۽ð� ��
			mmo=Hanguel.toHanguel(Etcdate.substring(EtcFirst+1,EtcLast));				//��Ÿ���۽ð� ��
			mda=Hanguel.toHanguel(Etcdate.substring(EtcLast+1,Etcdate.length()));		//��Ÿ���۽ð� ��

			String Etcdate_1 = request.getParameter("hdEtcDate_1");
			int EtcFirst_1 = Etcdate_1.indexOf("/");
			int EtcLast_1 = Etcdate_1.lastIndexOf("/");
			eye=Hanguel.toHanguel(Etcdate_1.substring(0,EtcFirst_1));					//��Ÿ���ð� ��
			emo=Hanguel.toHanguel(Etcdate_1.substring(EtcFirst_1+1,EtcLast_1));			//��Ÿ���ð� ��
			eda=Hanguel.toHanguel(Etcdate_1.substring(EtcLast_1+1,Etcdate_1.length()));	//��Ÿ���ð� ��

			String etcStartTime = request.getParameter("hdEtcTime");
			String etcEndTime = request.getParameter("hdEtcTime_1");
//			mti=etcStartTime + "~" + etcEndTime;										//��Ÿ�ð� �ð�
			if(mmo.equals(emo) && mda.equals(eda))							//�������� ���� �ð���
				mti = etcStartTime + "~" + etcEndTime;
			else															//�������� �ƴѰ��� ���ڸ� ǥ��
				mti=etcStartTime + "~" + "[" + eda + "]" + etcEndTime;

		}
		//---------------------------------------------------------------
		//	DB������ ���� Ư������ �ٲٱ� (' -> `) 
		//---------------------------------------------------------------
		sub = str.quoteReplace(sub);		//����
		mro = str.quoteReplace(mro);		//���
		mna = str.quoteReplace(mna);		//�����
		mte = str.quoteReplace(mte);		//��ȭ��ȣ
		con = str.quoteReplace(con);		//����
		//---------------------------------------------------------------
		//�μ����� �����ϱ�
		//---------------------------------------------------------------
		//�μ����� ó���ϱ�
		uid = DIV_ID;					//�μ�ID
		una = company_name;				//ȸ���
		udi = wdn;						//�μ���

		DATA = "INSERT INTO CALENDAR_SCHEDULE(pid,id,name,division,isopen,item,sub,mroom,mname,mtel,presents,isselect,myear,mmonth,mday,mtime,content,hday,eyear,emonth,eday,idate)";
		      DATA += " VALUES('" + bean.getID() + "','" + uid + "','" + una + "','" + udi + "','" + eop + "','" + eit + "','" + sub + "','";
		      DATA += mro + "','" + mna + "','" + mte + "','" + prs + "','" + mse + "','" + mye + "','" + mmo + "','" + mda + "','" + mti + "','";
			  DATA += con + "','" + hda + "','" + eye + "','" + emo + "','" + eda + "','" + bean.getTime() + "')";
		//out.println(DATA); 
		try { bean.execute(DATA);	Message="INSERT"; 	} catch (Exception e) { out.println(e);}
	} //if

} //stop

%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
</head>

<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> �μ��������</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="javascript:okClick();"><img src="../images/bt_save.gif" border="0"></a></TD>
			  <TD width=4></TD>
			  <TD align=left width=30><a href="Calendar_View.jsp?FLAG=IND&Sabun=<%=login_id%>"><img src="../images/bt_cancel.gif" border="0"></a></TD>
			  <TD width=4></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center"><FORM METHOD=post ACTION="Calendar_WriteD.jsp" NAME="_hdForm1" style="margin:0">
	<!--�⺻����-->
	<div id="Main" style="left:0px;visiblity:visible">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��ϱ���</td>
           <td width="37%" height="25" class="bg_04">��������</td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��ϴ��</td>
           <td width="37%" height="25" class="bg_04"><%=wName%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�����׸�</td>
           <td width="37%" height="25" class="bg_04">
				<table border=0><tr><td valign=middle width=50%>
				<SELECT NAME="hdAppointmentType" onChange="var sourceitem = document.forms[0].hdAppointmentType;
					var tmpIndex = sourceitem.selectedIndex;
					var tmpValue = sourceitem.options[tmpIndex].value;
					if (document.forms[0].hdDocType.value == &quot;&quot;) { //�������� ���
						document.forms[0].hdIndex.value = tmpIndex;
						show_menu(tmpValue);
					} else {
						alert (&quot;�����׸��� ������ �� �����ϴ�.&quot;);
						tmpPreIndex = document.forms[0].hdIndex.value;
						sourceitem.value = sourceitem.options[tmpPreIndex].value;
					}" CLASS="select">
				<%
					for(int si = 0 ; si < items_cnt; si++) {
						String SEL = "";
						if(si == 0) SEL = " SELECTED";
						else SEL = "";
						out.println("<OPTION" + SEL + " VALUE=" + Eitem[si] + ">" + Aitem[si]);
					}
				%>
				</SELECT></td><td valign=middle width=50%><a href="javascript:wopen('Calendar_addItem.jsp','manager_item','380','200')"><img src="../images/bt_add.gif" border="0" alt="�����׸��� �߰� �Ǵ� �����մϴ�."></a></td></tr></table></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04"><INPUT TYPE=radio NAME="hdOpen" VALUE="1" CHECKED>����	<INPUT TYPE=radio NAME="hdOpen" VALUE="0">�����</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><INPUT NAME="hdSubject" VALUE="" size=50 class='text_01'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table></div>

	<!-- ȸ�� -->
	<div id="Meeting" class="expanded">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">ȸ�����</td>
           <td width="37%" height="25" class="bg_04"><INPUT NAME="hdLocation" VALUE="" class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">ȸ�ǽð�</td>
           <td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdMeetDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdMeetDate', '', 180, 250)"><img src="../images/bt_calendar.gif" border="0" align='absmiddle'></A>
				<SELECT NAME="hdStartTime">
				<%
				String[] asHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String msSEL = "";
					for(int asH=0; asH<24; asH++){
						if(asH == Integer.parseInt(hrs)) msSEL = "SELECTED"; else msSEL="";
						out.println("<OPTION " + msSEL + ">" + asHour[asH] + ":" + "00");
						out.println("<OPTION>" + asHour[asH] + ":" + "30");
					}
					out.println("</SELECT> ~ ");
				%>
				<SELECT NAME="hdEndTime">
				<%
				String[] aeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String meSEL = "";
					for(int aeH=0; aeH<24; aeH++){
						if(aeH == Integer.parseInt(hrs)) meSEL = "SELECTED"; else meSEL="";
						out.println("<OPTION " + meSEL + ">" + aeHour[aeH] + ":" + "00");
						out.println("<OPTION>" + aeHour[aeH] + ":" + "30");
					}
				%></SELECT></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����ڸ�</td>
           <td width="37%" height="25" class="bg_04"><INPUT NAME="hdChair" VALUE="" class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����ڿ���ó</td>
           <td width="37%" height="25" class="bg_04"><INPUT NAME="hdChairTel" VALUE="" size="10"></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table></div>


	<!-- ��� -->
	<div id="Appointment" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��Ӵ��</td>
           <td width="37%" height="25" class="bg_04"><INPUT NAME="hdMeetUsers" VALUE=""></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����ó</td>
           <td width="37%" height="25" class="bg_04"><INPUT NAME="hdAppointTel" VALUE=""></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��ӽð�</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdAppointDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdAppointDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdAppointTime">
				<%
					String[] aptHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String aSEL = "";
					for(int aptH=0; aptH<24; aptH++){
						if(aptH == Integer.parseInt(hrs)) aSEL = "SELECTED"; else aSEL="";
						out.println("<OPTION " + aSEL + ">" + aptHour[aptH] + ":" + "00");
						out.println("<OPTION>" + aptHour[aptH] + ":" + "30");
					}
				%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdAppointLocation" VALUE=""></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

	<!-- ��� -->
	<div id="Event" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdEventDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdEventDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEventTime">
					<%
						String[] edHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String edSEL = "";
						for(int edH=0; edH<24; edH++){
							if(edH == Integer.parseInt(hrs)) edSEL = "SELECTED"; else edSEL="";
							out.println("<OPTION " + edSEL + ">" + edHour[edH] + ":" + "00");
							out.println("<OPTION>" + edHour[edH] + ":" + "30");
						}
					%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdEventDate_1" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdEventDate_1', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEventTime_1">
					<%
						String[] edeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String edeSEL = "";
						for(int edeH=0; edeH<24; edeH++){
							if(edeH == Integer.parseInt(hrs)) edeSEL = "SELECTED"; else edeSEL="";
							out.println("<OPTION " + edeSEL + ">" + edeHour[edeH] + ":" + "00");
							out.println("<OPTION>" + edeHour[edeH] + ":" + "30");
						}
					%></SELECT></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="87%" height="25" class="bg_04" colspan="3"><INPUT NAME="hdEventLocation" VALUE=""></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

	<!-- �ް� -->
	<div id="Vacation" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdVacationDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdVacationDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdVaTime">
					<%
						String[] vdHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String vdSEL = "";
						for(int vdH=0; vdH<24; vdH++){
							if(vdH == Integer.parseInt(hrs)) vdSEL = "SELECTED"; else vdSEL="";
							out.println("<OPTION " + vdSEL + ">" + vdHour[vdH] + ":" + "00");
							out.println("<OPTION>" + vdHour[vdH] + ":" + "30");
						}
					%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdVacationDate_1" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdVacationDate_1', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdVaTime_1">
					<%
						String[] vdeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String vdeSEL = "";
						for(int vdeH=0; vdeH<24; vdeH++){
							if(vdeH == Integer.parseInt(hrs)) vdeSEL = "SELECTED"; else vdeSEL="";
							out.println("<OPTION " + vdeSEL + ">" + vdeHour[vdeH] + ":" + "00");
							out.println("<OPTION>" + vdeHour[vdeH] + ":" + "30");
						}
					%></SELECT></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��޿���ó</td>
			<td width="87%" height="25" class="bg_04" colspan="3"><INPUT NAME="hdUrgencyTel" VALUE=""></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

	<!-- �������� -->
	<div id="TripH" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdTripDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdTripDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdTripTime">
					<%
						String[] tdHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdSEL = "";
						for(int tdH=0; tdH<24; tdH++){
							if(tdH == Integer.parseInt(hrs)) tdSEL = "SELECTED"; else tdSEL="";
							out.println("<OPTION " + tdSEL + ">" + tdHour[tdH] + ":" + "00");
							out.println("<OPTION>" + tdHour[tdH] + ":" + "30");
						}
					%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdTripDate_1" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdTripDate_1', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdTripTime_1">
					<%
						String[] tdeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdeSEL = "";
						for(int tdeH=0; tdeH<24; tdeH++){
							if(tdeH == Integer.parseInt(hrs)) tdeSEL = "SELECTED"; else tdeSEL="";
							out.println("<OPTION " + tdeSEL + ">" + tdeHour[tdeH] + ":" + "00");
							out.println("<OPTION>" + tdeHour[tdeH] + ":" + "30");
						}
					%></SELECT></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">���� ����ó</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdTripLoc" VALUE=""></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��޿���ó</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdTripTel" VALUE=""></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></Table></div>

	<!-- �ؿ����� -->
	<div id="TripA" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdTripADate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdTripADate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdTripATime">
					<%
						String[] tdAHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdASEL = "";
						for(int tdAH=0; tdAH<24; tdAH++){
							if(tdAH == Integer.parseInt(hrs)) tdASEL = "SELECTED"; else tdASEL="";
							out.println("<OPTION " + tdASEL + ">" + tdAHour[tdAH] + ":" + "00");
							out.println("<OPTION>" + tdAHour[tdAH] + ":" + "30");
						}
					%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdTripADate_1" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdTripADate_1', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdTripATime_1">
					<%
						String[] tdeAHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdeASEL = "";
						for(int tdeAH=0; tdeAH<24; tdeAH++){
							if(tdeAH == Integer.parseInt(hrs)) tdeASEL = "SELECTED"; else tdeASEL="";
							out.println("<OPTION " + tdeASEL + ">" + tdeAHour[tdeAH] + ":" + "00");
							out.println("<OPTION>" + tdeAHour[tdeAH] + ":" + "30");
						}
					%></SELECT></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�ؿ� ����ó</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdTripALoc" VALUE=""></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��޿���ó</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdTripATel" VALUE=""></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></Table></div>

	<!-- ����� -->
	<div id="Holiday" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�������</td>
			<td width="87%" height="25" class="bg_04">
			<INPUT NAME="hdHolidayDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdHolidayDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
			<SELECT NAME="hdHolidayTime">
				<%
					String[] hdHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String hdSEL = "";
					for(int hdH=0; hdH<24; hdH++){
						if(hdH == Integer.parseInt(hrs)) hdSEL = "SELECTED"; else hdSEL="";
						out.println("<OPTION " + hdSEL + ">" + hdHour[hdH] + ":" + "00");
						out.println("<OPTION>" + hdHour[hdH] + ":" + "30");
					}
				%></SELECT></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></Table></div>

	<!-- �湮 -->
	<div id="Area" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdAreaDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdAreaDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdAreaTime">
					<%
						String[] tdLHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdLSEL = "";
						for(int tdLH=0; tdLH<24; tdLH++){
							if(tdLH == Integer.parseInt(hrs)) tdLSEL = "SELECTED"; else tdLSEL="";
							out.println("<OPTION " + tdLSEL + ">" + tdLHour[tdLH] + ":" + "00");
							out.println("<OPTION>" + tdLHour[tdLH] + ":" + "30");
						}
					%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdAreaDate_1" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdAreaDate_1', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdAreaTime_1">
					<%
						String[] tdeLHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdeLSEL = "";
						for(int tdeLH=0; tdeLH<24; tdeLH++){
							if(tdeLH == Integer.parseInt(hrs)) tdeLSEL = "SELECTED"; else tdeLSEL="";
							out.println("<OPTION " + tdeLSEL + ">" + tdeLHour[tdeLH] + ":" + "00");
							out.println("<OPTION>" + tdeLHour[tdeLH] + ":" + "30");
						}
					%></SELECT></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�湮��</td>
			<td width="37%" height="25" class="bg_04">
			<INPUT NAME="hdArea" VALUE="" class='text_01'></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��޿���ó</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdAreaTel" VALUE=""></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></Table></div>

	<!-- ����/���̳� -->
	<div id="Education" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�����Ⱓ</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdEducationDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdEducationDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEduStartTime">
				<%
				String[] edusHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String emsSEL = "";
					for(int easH=0; easH<24; easH++){
						if(easH == Integer.parseInt(hrs)) emsSEL = "SELECTED"; else emsSEL="";
						out.println("<OPTION " + emsSEL + ">" + edusHour[easH] + ":" + "00");
						out.println("<OPTION>" + edusHour[easH] + ":" + "30");
					}
					out.println("</SELECT> ~ ");
				%>
				<SELECT NAME="hdEduEndTime">
				<%
				String[] edueHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String emeSEL = "";
					for(int eaeH=0; eaeH<24; eaeH++){
						if(eaeH == Integer.parseInt(hrs)) emeSEL = "SELECTED"; else emeSEL="";
						out.println("<OPTION " + emeSEL + ">" + edueHour[eaeH] + ":" + "00");
						out.println("<OPTION>" + edueHour[eaeH] + ":" + "30");
					}
				%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�������</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdEduLocation" VALUE="" class='text_01'></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�����ְ�</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdEduChair" VALUE="" class='text_01'></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����� ����ó</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdEduChairTel" VALUE="" size="10"></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

	<!-- ��Ÿ -->
	<div id="Etc" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdEtcDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdEtcDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEtcTime">
					<%
						String[] etHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String etSEL = "";
						for(int etH=0; etH<24; etH++){
							if(etH == Integer.parseInt(hrs)) etSEL = "SELECTED"; else etSEL="";
							out.println("<OPTION " + etSEL + ">" + etHour[etH] + ":" + "00");
							out.println("<OPTION>" + etHour[etH] + ":" + "30");
						}
					%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdEtcDate_1" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdEtcDate_1', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEtcTime_1">
					<%
						String[] eteHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String eteSEL = "";
						for(int eteH=0; eteH<24; eteH++){
							if(eteH == Integer.parseInt(hrs)) eteSEL = "SELECTED"; else eteSEL="";
							out.println("<OPTION " + eteSEL + ">" + eteHour[eteH] + ":" + "00");
							out.println("<OPTION>" + eteHour[eteH] + ":" + "30");
						}
					%></SELECT></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

	<!-- ���� -->
	<div id="comment" style="left:0px;visiblity:visible;">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����</td>
			<td width="87%" height="25" class="bg_04">
				<TEXTAREA NAME="Body" ROWS=7 COLS=70 WRAP=VIRTUAL></TEXTAREA></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

</td></tr></table>
<INPUT type="hidden" NAME="hdUserName" VALUE="<%=wName%>">
<input Type="hidden" Name="hdMembersInfo" value="">
<input Type="hidden" Name="hdDocType" value="">
<INPUT NAME="hdIndex" VALUE="0" Type=hidden>
<input type='hidden' name='Sabun' value='<%=DIV_ID%>'></form>
</body>
</html>


<% if(Message == "INSERT"){ %>
<script>
	var FLAG = '<%=FLAG%>';
	if((FLAG.indexOf("COM") == -1) && (FLAG.indexOf("DIV") == -1))
		document.location.href="Calendar_View.jsp";
</script>
<% Message = "" ; } %>

<!-- �ڹٽ�ũ��Ʈ -->
<Script Language="Javascript">
<!--

window.onload = setField;
function setField() {
	document.forms[0].hdSubject.focus();
}// -->

//��ϵ� �μ� ����LIST ����
function ListView()
{	
	var data = "Sabun="+'<%=DIV_ID%>'+"&Date="+'<%=toDay%>';	window.open("Calendar_divList.jsp?"+data,"divList","width=500,height=500,scrollbars=yes,toolbar=no,menubar=no,resizable=yes");
}

//�׸��߰��ϱ�
function addItem()
{
	window.open("Calendar_addItem.jsp","addItem","width=380,height=200,scrollbars=no,toolbar=no,menubar=no");

}
//----------------�� �����׸� ����ǥ��---------------------
function hide( menuname )
{
  if (navigator.appName =="Netscape" ) {
      document.layers[menuname].visibility="hide";
  } else {
      document.all[menuname].className="collapsed"
   }
}
function show( menuname )
{
  if (navigator.appName =="Netscape" ) {
       document.layers[menuname].visibility="show";
  } else {
       document.all[menuname].className="expanded"
  }
}
//������� ����
function show_menu( menuname )
{
   var obj = document.forms[0].hdAppointmentType;
   var maxItem = obj.length;
   var ItemArray = new Array(maxItem);
   
   for (var k=0; k < maxItem; k++) {
   		ItemArray[k] = obj.options[k].value;
   }

   for ( var i = 0; i < maxItem; i++ ) {
	  if ( menuname == ItemArray[i]  ) {
	     var tmpRt = checkEtc(menuname);
      	     show( tmpRt );
	     if (tmpRt == "Etc") { return false;}
      } else{
   	     var tmpRt = checkEtc(ItemArray[i]);
      	     hide( tmpRt );
      }
  }
}
function checkEtc( menuname )
{
	if (menuname != "Meeting" && menuname != "Appointment" && menuname != "Event" && menuname != "TripH" && menuname != "TripA" && menuname != "Vacation" && menuname != "Holiday" && menuname != "Area" && menuname != "Education") {
		return ("Etc");
	} else {
		return (menuname);
	}
}
//---------------------------------------------------------
//���� �����ϱ�
function okClick() {
     var cform = document.forms[0];
     var action = cform.hdAppointmentType.value;
     var altmsg = "";
     now = new Date();
     var nowday = Date.UTC(now.getYear(), now.getMonth(), now.getDate());
     if (confirm("����Ͻðڽ��ϱ�?")) {
           if (cform.hdDocType.value=="") {
                 if (cform.hdUserName.value == "") {
                      alert ("��ϴ���ڸ� �Է��Ͻʽÿ�."); 
                      cform.hdUserName.focus();
                      return;
                 } 
           }

           if (cform.hdAppointmentType.value != "Area" && cform.hdSubject.value == "") {
                 alert ("������ �Է��Ͻʽÿ�."); 
                 cform.hdSubject.focus();
                 return;
           } else {              
	             if (action == "Meeting") {
                       if (cform.hdLocation.value == "") {
							alert("ȸ�� ��Ҹ� �Է��Ͻʽÿ�.");
							return;
					  }
                      if (cform.hdChair.value == "") {
						 alert("����ڸ� �Է��Ͻʽÿ�.");
							return;

					  }
                     
						 var startTimeHour = cform.hdStartTime.options[cform.hdStartTime.selectedIndex].text.substring(0,2); 
						 var startTimeMin = cform.hdStartTime.options[cform.hdStartTime.selectedIndex].text.substring(3,5); 
						 var endTimeHour = cform.hdEndTime.options[cform.hdEndTime.selectedIndex].text.substring(0,2); 
						 var endTimeMin = cform.hdEndTime.options[cform.hdEndTime.selectedIndex].text.substring(3,5); 
						 if (parseInt(startTimeHour + startTimeMin,10) > parseInt(endTimeHour + endTimeMin,10)) {
								 alert("ȸ�� ���۽ð��� ����ð� ���� �����Դϴ�.\n\nȸ�� ����ð��� ���۽ð� ���� ���ķ� �����Ͻñ� �ٶ��ϴ�.");
								 return;
						 }
                               		
                 } else if (action == "Event") {
	                      var timevalue = cform.hdEventDate.value;
	                      var eventday = new Date(timevalue.substring(0, 4), timevalue.substring(5, 7)-1, timevalue.substring(8, 10));
     	                  var ceventday = Date.UTC(eventday.getYear(), eventday.getMonth(), eventday.getDate());
                          var timevalue_1 = cform.hdEventDate_1.value;
                          var eventday_1 = new Date(timevalue_1.substring(0, 4), timevalue_1.substring(5, 7)-1, timevalue_1.substring(8, 10));
                          var ceventday_1 = Date.UTC(eventday_1.getYear(), eventday_1.getMonth(), eventday_1.getDate());  
	                      if (ceventday > ceventday_1) {
		                          alert("��� �������� ������ ���ķ� �����Ǿ����ϴ�.\n\n��� �������� �����Ϻ��� ���ķ� �����Ͻñ� �ٶ��ϴ�.");
          		                  return;
	                      } else if (ceventday == ceventday_1) {
		                          var startTimeHour = cform.hdEventTime.options[cform.hdEventTime.selectedIndex].text.substring(0,2); 
		                          var startTimeMin = cform.hdEventTime.options[cform.hdEventTime.selectedIndex].text.substring(3,5); 
		                          var endTimeHour = cform.hdEventTime_1.options[cform.hdEventTime_1.selectedIndex].text.substring(0,2); 
		                          var endTimeMin = cform.hdEventTime_1.options[cform.hdEventTime_1.selectedIndex].text.substring(3,5); 
		                          if (parseInt(startTimeHour,10) > parseInt(endTimeHour,10)) 	{
				                          alert("��� ���۽ð��� ����ð� ���� �����Դϴ�.\n\n��� ����ð��� ���۽ð� ���� ���ķ� �����Ͻñ� �ٶ��ϴ�.");
				                          return;
		                          } 
		                          if (parseInt(startTimeHour,10) == parseInt(endTimeHour,10) && parseInt(startTimeMin,10) > parseInt(endTimeMin,10)) {
				                           alert("��� ���۽ð��� ����ð� ���� �����Դϴ�.\n\n��� ����ð��� ���۽ð� ���� ���ķ� �����Ͻñ� �ٶ��ϴ�.");
				                           return;
		                          }		
	                     }	
               } else if (action == "Vacation") {
                       var timevalue = cform.hdVacationDate.value;
                       var vacationday = new Date(timevalue.substring(0, 4), timevalue.substring(5, 7)-1, timevalue.substring(8, 10));
                       var cvacationday = Date.UTC(vacationday.getYear(), vacationday.getMonth(), vacationday.getDate());
                    
                       var timevalue_1 = cform.hdVacationDate_1.value;
                       var vacationday_1 = new Date(timevalue_1.substring(0, 4), timevalue_1.substring(5, 7)-1, timevalue_1.substring(8, 10));
                       var cvacationday_1 = Date.UTC(vacationday_1.getYear(), vacationday_1.getMonth(), vacationday_1.getDate());                    
		               if (cvacationday > cvacationday_1) {
		                       alert("�ް�/���� �������� �����Ϻ��� ũ�� �����Ǿ����ϴ�.\n\n�ް�/���� �������� �����Ϻ��� ũ�� �����Ͻñ� �ٶ��ϴ�..");
          		               return;
		               } else if (cvacationday == cvacationday_1) {
		                       var startTimeHour = cform.hdVaTime.options[cform.hdVaTime.selectedIndex].text.substring(0,2); 
		                       var startTimeMin = cform.hdVaTime.options[cform.hdVaTime.selectedIndex].text.substring(3,5); 
		                       var endTimeHour = cform.hdVaTime_1.options[cform.hdVaTime_1.selectedIndex].text.substring(0,2); 
		                       var endTimeMin = cform.hdVaTime_1.options[cform.hdVaTime_1.selectedIndex].text.substring(3,5); 
		                       if (parseInt(startTimeHour + startTimeMin,10) > parseInt(endTimeHour + endTimeMin,10)) {
			                         alert("�ް�/���� ���۽ð��� ����ð� ���� �����Դϴ�.\n\n�ް�/���� ����ð��� ���۽ð� ���� ���ķ� �����Ͻñ�  �ٶ��ϴ�.");
			                         return;
                               }		
		              }
	           } else if (action == "TripH"){
                       var timevalue = cform.hdTripDate.value;
                       var vacationday = new Date(timevalue.substring(0, 4), timevalue.substring(5, 7)-1, timevalue.substring(8, 10));
                       var cvacationday = Date.UTC(vacationday.getYear(), vacationday.getMonth(), vacationday.getDate());
                    
                       var timevalue_1 = cform.hdTripDate_1.value;
                       var vacationday_1 = new Date(timevalue_1.substring(0, 4), timevalue_1.substring(5, 7)-1, timevalue_1.substring(8, 10));
                       var cvacationday_1 = Date.UTC(vacationday_1.getYear(), vacationday_1.getMonth(), vacationday_1.getDate());
                    
		               if (cvacationday > cvacationday_1) {
		                       alert("���� �������� �����Ϻ��� ũ�� �����Ǿ����ϴ�.\n\n���� �������� �����Ϻ��� ũ�� �����Ͻñ� �ٶ��ϴ�..");
          		               return;
		               } else if (cvacationday == cvacationday_1) {
		                       var startTimeHour = cform.hdTripTime.options[cform.hdTripTime.selectedIndex].text.substring(0,2); 
		                       var startTimeMin = cform.hdTripTime.options[cform.hdTripTime.selectedIndex].text.substring(3,5); 
		                       var endTimeHour = cform.hdTripTime_1.options[cform.hdTripTime_1.selectedIndex].text.substring(0,2); 
		                       var endTimeMin = cform.hdTripTime_1.options[cform.hdTripTime_1.selectedIndex].text.substring(3,5); 
		                       if (parseInt(startTimeHour + startTimeMin,10) > parseInt(endTimeHour + endTimeMin,10)) {
			                           alert("���� ���۽ð��� ����ð� ���� �����Դϴ�.\n\n���� ����ð��� ���۽ð� ���� ���ķ� �����Ͻñ� �ٶ��ϴ�.");
			                           return;
                                }		
		               }
				} else if (action == "TripA"){
                       var timevalue = cform.hdTripADate.value;
                       var vacationday = new Date(timevalue.substring(0, 4), timevalue.substring(5, 7)-1, timevalue.substring(8, 10));
                       var cvacationday = Date.UTC(vacationday.getYear(), vacationday.getMonth(), vacationday.getDate());
                    
                       var timevalue_1 = cform.hdTripADate_1.value;
                       var vacationday_1 = new Date(timevalue_1.substring(0, 4), timevalue_1.substring(5, 7)-1, timevalue_1.substring(8, 10));
                       var cvacationday_1 = Date.UTC(vacationday_1.getYear(), vacationday_1.getMonth(), vacationday_1.getDate());
                    
		               if (cvacationday > cvacationday_1) {
		                       alert("���� �������� �����Ϻ��� ũ�� �����Ǿ����ϴ�.\n\n���� �������� �����Ϻ��� ũ�� �����Ͻñ� �ٶ��ϴ�..");
          		               return;
		               } else if (cvacationday == cvacationday_1) {
		                       var startTimeHour = cform.hdTripATime.options[cform.hdTripATime.selectedIndex].text.substring(0,2); 
		                       var startTimeMin = cform.hdTripATime.options[cform.hdTripATime.selectedIndex].text.substring(3,5); 
		                       var endTimeHour = cform.hdTripATime_1.options[cform.hdTripATime_1.selectedIndex].text.substring(0,2); 
		                       var endTimeMin = cform.hdTripATime_1.options[cform.hdTripATime_1.selectedIndex].text.substring(3,5); 
		                       if (parseInt(startTimeHour + startTimeMin,10) > parseInt(endTimeHour + endTimeMin,10)) {
			                           alert("���� ���۽ð��� ����ð� ���� �����Դϴ�.\n\n���� ����ð��� ���۽ð� ���� ���ķ� �����Ͻñ� �ٶ��ϴ�.");
			                           return;
                                }		
		               }
			   } else if (action == "Education") {
                      if (cform.hdEduLocation.value == "") {
							alert("���� ��Ҹ� �Է��Ͻʽÿ�.");
							return;
						}
                      if (cform.hdEduChair.value == "") {
						  alert("�����ְ��� �Է��Ͻʽÿ�.");
							return;
					  }

						 var startTimeHour = cform.hdEduStartTime.options[cform.hdEduStartTime.selectedIndex].text.substring(0,2); 
						 var startTimeMin = cform.hdEduStartTime.options[cform.hdEduStartTime.selectedIndex].text.substring(3,5); 
						 var endTimeHour = cform.hdEduEndTime.options[cform.hdEduEndTime.selectedIndex].text.substring(0,2); 
						 var endTimeMin = cform.hdEduEndTime.options[cform.hdEduEndTime.selectedIndex].text.substring(3,5); 
						 if (parseInt(startTimeHour + startTimeMin,10) > parseInt(endTimeHour + endTimeMin,10)) {
								 alert("����/���̳� ���۽ð��� ����ð� ���� �����Դϴ�.\n\n����/���̳� ����ð��� ���۽ð� ���� ���ķ� �����Ͻñ� �ٶ��ϴ�.");
								 return;
						 }
                      
			  } else if (action == "Appointment") {
			  } else if (action == "Holiday") {
			  } else if (action == "Area") {
				  var appType = cform.hdAppointmentType.options[cform.hdAppointmentType.selectedIndex].text;
		          var timevalue = cform.hdAreaDate.value;
		          var etcday = new Date(timevalue.substring(0,4), timevalue.substring(5,7)-1, timevalue.substring(8,10));
		          var cetcday = Date.UTC(etcday.getYear(), etcday.getMonth(), etcday.getDate());
		          var timevalue_1 = cform.hdAreaDate_1.value;
		          var etcday_1 = new Date(timevalue_1.substring(0, 4), timevalue_1.substring(5, 7)-1, timevalue_1.substring(8, 10));
		          var cetcday_1 = Date.UTC(etcday_1.getYear(), etcday_1.getMonth(), etcday_1.getDate());
		          if (cetcday > cetcday_1) {
		              alert(appType + " �������� �����Ϻ��� ũ�� �����Ǿ����ϴ�.\n\n" + appType + " �������� �����Ϻ��� ũ�� �����Ͻñ� �ٶ��ϴ�..");
		               return;
		          }
				  if(cform.hdSubject.value == "") {
					alert ("���� �Է��Ͻʽÿ�."); cform.hdSubject.focus(); return;
				  } else if (cform.hdArea.value == "") {
						alert ("�湮���� �Է��Ͻʽÿ�."); cform.hdArea.focus(); return;
				  }
	          } else {
		              var appType = cform.hdAppointmentType.options[cform.hdAppointmentType.selectedIndex].text;
		              var timevalue = cform.hdEtcDate.value;
		              var etcday = new Date(timevalue.substring(0,4), timevalue.substring(5,7)-1, timevalue.substring(8,10));
		              var cetcday = Date.UTC(etcday.getYear(), etcday.getMonth(), etcday.getDate());
		              var timevalue_1 = cform.hdEtcDate_1.value;
		              var etcday_1 = new Date(timevalue_1.substring(0, 4), timevalue_1.substring(5, 7)-1, timevalue_1.substring(8, 10));
		              var cetcday_1 = Date.UTC(etcday_1.getYear(), etcday_1.getMonth(), etcday_1.getDate());
		              if (cetcday > cetcday_1) {
		                       alert(appType + " �������� �����Ϻ��� ũ�� �����Ǿ����ϴ�.\n\n" + appType + " �������� �����Ϻ��� ũ�� �����Ͻñ� �ٶ��ϴ�..");
		                       return;
		              } else if (cetcday == cetcday_1) {
		                       var startTimeHour = cform.hdEtcTime.options[cform.hdEtcTime.selectedIndex].text.substring(0,2); 
		                       var startTimeMin = cform.hdEtcTime.options[cform.hdEtcTime.selectedIndex].text.substring(3,5); 
		                       var endTimeHour = cform.hdEtcTime_1.options[cform.hdEtcTime_1.selectedIndex].text.substring(0,2); 
		                       var endTimeMin = cform.hdEtcTime_1.options[cform.hdEtcTime_1.selectedIndex].text.substring(3,5); 
		                       if (parseInt(startTimeHour,10) > parseInt(endTimeHour,10)) 	{
				                      alert("���۽ð��� ����ð� ���� �����Դϴ�.\n\n��� ����ð��� ���۽ð� ���� ���ķ� �����Ͻñ� �ٶ��ϴ�.");
				                      return;
		                       } 
		                       if (parseInt(startTimeHour,10) == parseInt(endTimeHour,10) && parseInt(startTimeMin,10) > parseInt(endTimeMin,10)) {
				                       alert("���۽ð��� ����ð� ���� �����Դϴ�.\n\n��� ����ð��� ���۽ð� ���� ���ķ� �����Ͻʽÿ�.");
				                       return;
		                       }			
		              }
             }
     }

	 cform.submit();
     } else {
	        return;
     }
}

function selectkind(newKind) {
	var newOpt = new Option(newKind, newKind, false, true);
	var catField = document.forms[0].hdAppointmentType;
	catField.options[catField.options.length] = newOpt;
	var tmpstring = catField.options[catField.options.selectedIndex].value;
	show_menu( tmpstring );
}
function addkind() {
	var pathname = (window.location.pathname);
    		window.open(pathname.substring(0,(pathname.lastIndexOf('.nsf')+5))+'wAddKind?OpenForm','AddKind','screenX=0,screenY=0,width=370,height=170');
	return;
}
//------------------------------------------------------------------------
function OpenCalendar(FieldName) {
	var strUrl = "Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
}

//�����׸� �߰�
function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh);
}
-->
</Script>