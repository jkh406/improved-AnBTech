<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page	
	language = "java"
	info= "���� �����ϱ�"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.text.Hanguel"	
	import="java.util.StringTokenizer"
%>
<%@	page import="com.anbtech.date.anbDate"	%>
<%@	page import="com.anbtech.text.StringProcess"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />		
<%
	//login ���� ����
	String id="";					//login

	//�޽��� ���޺���
	String Message="";				//�޽��� ���� ���� 
	String toDay="";				//������ ��/��/�� ǥ��
	String endDay="";				//������ ��/��/�� ǥ��

	//���޹��� ����
	String pid="";					//PID���޺���
	String Tact="";					//�����û? ����:modify, ����:delete

	//����� ���� (����)
	String uid="";					//���������� ���
	String una="";					//���������� �̸�
	String udi="";					//���������� �μ���

	String eop="";					//��������
	String eit="";					//�����׸�
	String Seit="";					//�����׸�(Scriptó����)

	String sub="";					//����
	String con="";					//ȸ�ǳ���
	String mse="";					//�������û���

	//����� ���� (�ɼ�:�����׸񺰷�)
	String mna="";					//�����ڸ�(ȸ��)
	String hda="";					//�����
	String mro="";					//���			(ȸ��,���,����,�ٹ���)
	String mte="";					//��ȭ��ȣ		(ȸ��,����,�ް�)

	String prs="";					//������		(ȸ��,���)
	
	String mye="";					//���۽ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)
	String mmo="";					//���۽ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)
	String mda="";					//���۽ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)

	String mti="";					//��ü �ð�		(ȸ��,���,���,�ް�,����,���,��Ÿ)

	String eye="";					//����ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)
	String emo="";					//����ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)
	String eda="";					//����ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)

	String ide="";					//�Է�����

	//ȸ��/�μ� ���������� 
	String FLAG = "";				//�޴��� ���޹��� ��������(����,ȸ��,�μ�)
	String CSMC_items = "";			//ȸ�� �������������� ���
	String CSMC_NY = "";			//ȸ�� �űԵ�� ���� ��������
	String CSMD_items = "";			//�μ� �������������� ���
	String CSMD_NY = "";			//�μ� �űԵ�� ���� ��������
	String cal_di = "";				//login �μ������ڵ� 
	String mod_permit = "";			//�������ɿ��� �Ǵ�
	String wName="";				//��ϴ���� ��

	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	
	/*********************************************************************
	 	����� login �˾ƺ���
	*********************************************************************/
	id = login_id; 			//������

	//�μ� �����ڵ� ã��
	String[] udiColumns = {"ac_id","id"};
	bean.setTable("user_table");
	bean.setColumns(udiColumns);
	bean.setOrder("ac_id DESC");	
	bean.setClear();	
	bean.setSearch("id",id);
	bean.init_unique();

	if(bean.isEmpty()) cal_di = "";
	else {
		while(bean.isAll()) cal_di = bean.getData("ac_id");
	}

	//�μ� �ʱ�ȭ
	uid=una=udi=eop=eit=sub=mro=mna=mte=prs=mse=mye=mmo=mda=mti=con="";
	hda=eye=emo=eda="";
	Tact="";

	/*****************************************************
	//ȸ��/�μ� ���� ���������� ����� ��������
	*****************************************************/
	String[] itemColumns = {"item","nlist"};
	String csmc_data = "where item='SMC' order by nlist DESC"; 
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(itemColumns);
	bean.setSearchWrite(csmc_data);
	bean.init_write();
	
	CSMC_items = ";";							//ȸ�� ���� ���������� ��� LIST (������ : ';')
	CSMC_NY = "";								//ȸ�� �ű� clear
	if(bean.isEmpty()) { 
		CSMC_NY = "new";						//�űԵ����
		CSMC_items = "";						//���� ���������� ��� ����
	} else {
		while(bean.isAll()) CSMC_items += bean.getData("nlist");
	}	

	//�μ� ���� ���� ������ ���
	String csmd_data = "where item='SMD' order by nlist DESC"; 
	bean.setSearchWrite(csmd_data);
	bean.init_write();
	
	CSMD_items = ";";							//�μ� ���� ���������� ��� LIST (������ : ';')
	CSMD_NY = "";								//�μ� �ű� clear
	if(bean.isEmpty()) { 
		CSMD_NY = "new";						//�űԵ����
		CSMD_items = "";						//���� ���������� ��� ����
	} else {
		while(bean.isAll()) CSMD_items += bean.getData("nlist");
	}	

	/*********************************************************************
	 	���޹��� ������ �ش�Ǵ� ���� �O��(����� �� ��ü ��ũ��Ʈ:����,����)
	*********************************************************************/
	String PID = request.getParameter("PID");
	if(PID != null) pid = PID;

	String TAG = request.getParameter("opendocument");
	if(TAG != null) Tact = TAG;

	String[] indColumns = {"pid","id","name","division","isopen","item","sub","mroom","mname","mtel","presents","isselect","myear","mmonth","mday","eyear","emonth","eday","mtime","content","hday","idate"};
	bean.setTable("CALENDAR_SCHEDULE");
	bean.setColumns(indColumns);
	bean.setOrder("mday ASC");
	bean.setClear();
	bean.setSearch("pid",pid);
	bean.init_unique();	

	while(bean.isAll()) {
		//�����׸�
		uid=bean.getData("id");						//���������� ���
		una=bean.getData("name");					//���������� �̸�
		udi=bean.getData("division");				//���������� �μ���
		eop=bean.getData("isopen");					//��������
		eit=bean.getData("item");					//�����׸�
		//��ũ��Ʈ ó����(������)
		if(eit.equals("ȸ��")) Seit = "Meeting";
		else if(eit.equals("���")) Seit = "Appointment";
		else if(eit.equals("���")) Seit = "Event";
		else if(eit.equals("��������")) Seit = "TripH";
		else if(eit.equals("�ؿ�����")) Seit = "TripA";
		else if(eit.equals("�ް�")) Seit = "Vacation";
		else if(eit.equals("�����")) Seit = "Holiday";
		else if(eit.equals("�湮")) Seit = "Area";
		else if(eit.equals("����/���̳�")) Seit = "Education";
		else Seit = eit;
			
		sub=bean.getData("sub");					//����
		String readcon=bean.getData("content");		//ȸ�ǳ���
		for(int ci=0; ci<readcon.length(); ci++){
				if(readcon.charAt(ci) == '\n') con += "<br>";
				else if(readcon.charAt(ci) == ' ') con += "&nbsp;";
				else con += readcon.charAt(ci);
		}

		mse=bean.getData("isselect");				//�������û���

		//�ɼ�:�����׸񺰷�
		mna=bean.getData("mname");					//�����ڸ�(ȸ��,����)
		hda=bean.getData("hday");					//�����
		mro=bean.getData("mroom");					//���			(ȸ��,���,����,�ٹ���,���,����)
		mte=bean.getData("mtel");					//��ȭ��ȣ		(ȸ��,����,�ް�,���,����)

		prs=bean.getData("presents");				//������		(ȸ��,���)
	
		mye=bean.getData("myear");					//���۽ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)
		mmo=bean.getData("mmonth");					//���۽ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)
		if((mmo != null) && (mmo.length() ==1)) mmo = "0"+mmo;
		mda=bean.getData("mday");					//���۽ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)
		if((mda != null) && (mda.length() ==1)) mda = "0"+mda;
		toDay = mye + "/" + mmo + "/" + mda;		//ȭ����� ������ (yyyy/mm/dd)

		mti=bean.getData("mtime");					//��ü �ð�		(ȸ��,���,���,�ް�,����,���,��Ÿ)

		eye=bean.getData("eyear");					//����ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)
		emo=bean.getData("emonth");					//����ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)
		if((emo != null) && (emo.length() ==1)) emo = "0"+emo;
		eda=bean.getData("eday");					//����ð� ��	(ȸ��,���,���,�ް�,����,���,��Ÿ)
		if((eda != null) && (eda.length() ==1)) eda = "0"+eda;
		endDay = eye + "/" + emo + "/" + eda;		//ȭ����� �շ��� (yyyy/mm/dd)

		ide=bean.getData("idate");					//�Է��� 
	}

	/*********************************************************************
	 	�������ɿ��� �Ǵ��ϱ�
	*********************************************************************/
	mod_permit = "N";	//�����Ұ���
	wName = "";
	//�����ڽ��� ���� ��������
	if(id.equals(uid)) { 
		mod_permit = "Y";		
		wName = una+"/"+uid+"/"+udi;
	} else {
		//ȸ������ ����� ���� �Ǵ��ϱ�
		StringTokenizer CSMC = new StringTokenizer(CSMC_items,";");
		while(CSMC.hasMoreTokens()) {
			String smc_id=CSMC.nextToken();
			if(smc_id.equals(id) && uid.equals("0")){
				mod_permit = "Y";
				wName = "ȸ������/"+company_name;
			}
		}

		//�μ����� ����� ���� �Ǵ��ϱ�
		StringTokenizer CSMD = new StringTokenizer(CSMD_items,";");
		while(CSMD.hasMoreTokens()) {
			String smd_id=CSMD.nextToken();
			if(smd_id.equals(id) && uid.equals(cal_di)) {
				mod_permit = "Y";
				wName = "�μ�����/"+udi;
			}
		}

		//������ ��������(�Ǵ� ���Ѿ��� ȸ��,�μ����� ����)
		if(mod_permit.equals("N"))	wName = una+"/"+uid+"/"+udi;
		
	}


	/*-----------------------------------------
	// �����ϱ�
	-----------------------------------------*/
	if(Tact.equals("delete")) { 
		String DELdata = "delete from calendar_schedule where pid='" + pid + "'";
		try { bean.execute(DELdata); Message="DELETE"; } catch (Exception e) { Message="QUERY"; }
	}

	/*-----------------------------------------
	// �����ϱ�
	-----------------------------------------*/
	if(Tact.equals("save")) {
		//--------------------------------------------------------------------------------	
		//�����׸� �б�
		//--------------------------------------------------------------------------------
		String Username = request.getParameter("hdUserName");

		eop=Hanguel.toHanguel(request.getParameter("hdOpen"));						//��������

		String ditem=Hanguel.toHanguel(request.getParameter("hdAppointmentType"));	//�����׸�
		if(ditem.equals("Meeting")) eit = "ȸ��";
		else if(ditem.equals("Appointment")) eit = "���";
		else if(ditem.equals("Event")) eit = "���";
		else if(ditem.equals("TripH")) eit = "��������";
		else if(ditem.equals("TripA")) eit = "�ؿ�����";
		else if(ditem.equals("Vacation")) eit = "�ް�";
		else if(ditem.equals("Holiday")) eit = "�����";
		else if(ditem.equals("Area")) eit = "�湮";
		else if(ditem.equals("Education")) eit = "����/���̳�";
		else eit = ditem;														

		sub=Hanguel.toHanguel(request.getParameter("hdSubject"));					//����
		con=Hanguel.toHanguel(request.getParameter("Body"));						//ȸ�ǳ���
		
		//--------------------------------------------------------------------------------
		//�ɼ��׸� �б� (�����׸񺰷� �ٸ�)
		//--------------------------------------------------------------------------------
		if(ditem.equals("Meeting")) {			//ȸ��
			mro=Hanguel.toHanguel(request.getParameter("hdLocation"));					//ȸ�����
			mna=Hanguel.toHanguel(request.getParameter("hdChair"));						//ȸ�� �����ڸ�
			mte=Hanguel.toHanguel(request.getParameter("hdChairTel"));					//ȸ�� ������ ��ȭ��ȣ

			String Meetdate = request.getParameter("hdMeetDate");
			int MFirst = Meetdate.indexOf("/");
			int MLast = Meetdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Meetdate.substring(0,MFirst));						//ȸ�ǽð� ��
			mmo=Hanguel.toHanguel(Meetdate.substring(MFirst+1,MLast));					//ȸ�ǽð� ��
			mda=Hanguel.toHanguel(Meetdate.substring(MLast+1,Meetdate.length()));		//ȸ�ǽð� ��

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
			else																		//�����߾ƴϸ� ���ڸ� ǥ��
				mti=eStartTime + "~" + "[" + eda + "]" + eEndTime;
		} else if(ditem.equals("Vacation")) {		//�ް�
			mte=Hanguel.toHanguel(request.getParameter("hdUrgencyTel"));				//��޿���ó ��ȭ��ȣ
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
			mti=mmo + "/" + mda + "~" + emo + "/" + eda;	
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
			mti=request.getParameter("hdHolidayTime");		
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
		//���� ���� �����ϱ� (����,ȸ��,�μ� ��� ����)
		//---------------------------------------------------------------
		String upDATA = "UPDATE CALENDAR_SCHEDULE set id='" + uid + "',name='" + una + "',division='" + udi + "',isopen='" + eop + "',item='" + eit;
			  upDATA += "',sub='" + sub + "',mroom='" + mro + "',mname='" + mna + "',mtel='" + mte + "',presents='" + prs + "',isselect='" + mse;
			  upDATA += "',myear='" + mye + "',mmonth='" + mmo + "',mday='" + mda + "',mtime='" + mti + "',content='" + con + "',hday='" + hda;
			  upDATA += "',eyear='" + eye + "',emonth='" + emo + "',eday='" + eda + "',idate='" + bean.getTime() + "' where pid='" + pid + "'";
		//out.println(upDATA);
		try { 
				bean.execute(upDATA);
				//�μ� �ʱ�ȭ
				uid=una=udi=eop=eit=sub=mro=mna=mte=prs=mse=hda=con="";
				Message="UPDATE"; 
		} catch (Exception e) { }

	}

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<meta http-equiv="Pragma" content="no-cache">
<SCRIPT LANGUAGE="JavaScript">
<!-- 
function handleDocument(action)  {
    var tmp = document.URL ;		
    var viewarray = tmp.split("?");
	var tmpname = viewarray[0];		//http://166.125.13.139:8080/Calendar_Modify.jsp
	var tmpvar = viewarray[1];		//PID=200212200909600&opendocument&view=Calendar_View
	var tmppg = tmpvar.split("&");
	var pgpid = tmppg[0];			//PID=200212200909600

   if (action == "modify") {
		location.href = "Calendar_Modify.jsp?" + pgpid + "&opendocument=modify";
   } else if (action == "delete") {
   		if (confirm("���� ������ �����Ͻðڽ��ϱ�")) {   				
			location.href = "Calendar_Modify.jsp?" + pgpid + "&opendocument=delete";
   		} else {
   				return;
   		}
    }

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
	if (menuname != "Meeting" && menuname != "Appointment" && menuname != "Event" && menuname != "TripH" && menuname != "TripA" && menuname != "Vacation" && menuname != "Holiday" && menuname != "Area") {
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
     if (confirm("����� ������ �����Ͻðڽ��ϱ�?")) {
           if (cform.hdDocType.value=="") {
                 if (cform.hdUserName.value == "") {
                      alert ("��ϴ���ڸ� �Է��Ͻʽÿ�."); 
                      cform.hdUserName.focus();
                      return;
                 } 
           }

           if (cform.hdAppointmentType.value == "Area" && cform.hdArea.value == "") {
                 alert ("�ٹ�ó�� �Է��Ͻʽÿ�."); 
                 cform.hdArea.focus();
                 return;
           } else if (cform.hdAppointmentType.value != "Area" && cform.hdSubject.value == "") {
                 alert ("������ �Է��Ͻʽÿ�."); 
                 cform.hdSubject.focus();
                 return;
           } else {              
	             if (action == "Meeting") {
                      if (cform.hdLocation.value == "") {
						  alert("ȸ����Ҹ� �Է��Ͻʽÿ�.");
						  cform.hdLocation.focus();
						  return;
					  }
                      if (cform.hdChair.value == "") {
						  alert("����ڸ� �Է��Ͻʽÿ�.");
						  cform.hdChair.focus();
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
	           } else if (action == "TripH") {
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
			   } else if (action == "TripA") {
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
						  alert("������Ҹ� �Է��Ͻʽÿ�.");  
						  cform.hdEduLocation.focus();
						  return;
					  }
                      if (cform.hdEduChair.value == "") {
						  alert("�����ְ��� �Է��Ͻʽÿ�.");
						  cform.hdEduChair.focus();
						  return;
					  }
						 var startTimeHour = cform.hdEduStartTime.options[cform.hdEduStartTime.selectedIndex].text.substring(0,2); 
						 var startTimeMin = cform.hdEduStartTime.options[cform.hdEduStartTime.selectedIndex].text.substring(3,5); 
						 var endTimeHour = cform.hdEduEndTime.options[cform.hdEduEndTime.selectedIndex].text.substring(0,2); 
						 var endTimeMin = cform.hdEduEndTime.options[cform.hdEduEndTime.selectedIndex].text.substring(3,5); 
						 if (parseInt(startTimeHour + startTimeMin,10) > parseInt(endTimeHour + endTimeMin,10)) {
								 alert("ȸ�� ���۽ð��� ����ð� ���� �����Դϴ�.\n\nȸ�� ����ð��� ���۽ð� ���� ���ķ� �����Ͻñ� �ٶ��ϴ�.");
								 return;
						 }
                       
			   } else if (action == "Appointment") {
			   } else if (action == "Holiday") {
			   } else if (action == "Area") {
			   } else {
					  var appType = cform.hdAppointmentType.text;
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
	newWIndow = window.open(strUrl, "Calendar", "width=200, height=270");
}

function monthlyView() {
	var form=parent.Left.document.forms[0]
	index=form.Sabun.options[form.Sabun.selectedIndex].value;		//������ϱ�
	document.location.href="Calendar_View.jsp?Sabun="+index;
}
-->
</Script>
<link rel="stylesheet" href="../css/style.css" type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> ������</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><A href="Javascript:monthlyView();"><img src="../images/bt_list.gif" border="0"></a></TD>
			  <TD width=4></TD>
<%	if(mod_permit.equals("Y")) { //login id �ڽ��� ��츸 ����/���� ����
		if(Tact.equals("modify")) { 
%>
			  <TD align=left width=30><a href="javascript:okClick();"><img src="../images/bt_save.gif" border="0"></a></TD>
			  <TD width=4></TD>
<%		} else {	%>
			  <TD align=left width=30><a href="javascript:handleDocument('modify');"><img src="../images/bt_modify.gif" border="0"></a></TD>
			  <TD width=4></TD>
<%		}	%>
			  <TD align=left width=30><a href="javascript:handleDocument('delete');"><img src="../images/bt_del.gif" border="0"></a></TD>
			  <TD width=4></TD>
<%	}	%>
			</TR></TBODY></TABLE></TD></TR>
	</TABLE>


<FORM METHOD=post ACTION="Calendar_Modify.jsp" NAME="_hdForm1" style="margin:0">
<input type='hidden' name='PID' value='<%=pid%>'>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--�⺻����-->
	<div id="Main" style="left:0px;visiblity:visible">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		  
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��ϱ���</td>
           <td width="37%" height="25" class="bg_04">��������</td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><%=wName%><INPUT type="hidden" NAME="hdUserName" VALUE="<%=wName%>"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�� �� ��</td>
			<td width="37%" height="25" class="bg_04"><%=una%>/<%=uid%>/<%=udi%></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����Ͻ�</td>
			<td width="37%" height="25" class="bg_04"><%=ide%></td>
		  </tr>
		  <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�����׸�</td>
			<td width="37%" height="25" class="bg_04"><%=eit%><INPUT TYPE=hidden NAME="hdAppointmentType" VALUE="<%=Seit%>" CLASS="etc"></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��������</td>
			<td width="37%" height="25" class="bg_04"><INPUT TYPE=radio NAME="hdOpen" VALUE="1" <% if(eop.equals("1")) out.print("CHECKED"); %> CLASS="etc">���� <INPUT TYPE=radio NAME="hdOpen" VALUE="0" <% if(eop.equals("0")) out.print("CHECKED"); %> CLASS="etc">�����</td>
		  </tr>
		  <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
			<% if(Tact.equals("modify")) { %> <INPUT NAME="hdSubject" VALUE="<%=sub%>" size=50 maxlength='50' class='text_01'>
			<% } else { %>
				<%=sub%>
			<% } %></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table></div>

<!-- ȸ�� -->
<% if(Seit.equals("Meeting")) { %>
	<div id="Meeting" class="expanded">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">ȸ�����</td>
           <td width="37%" height="25" class="bg_04">
			<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdLocation" VALUE="<%=mro%>" maxlength='20' class='text_01'>
			<% } else { %>
					<%=mro%>
			<% } %></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">ȸ�ǽð�</td>
           <td width="37%" height="25" class="bg_04">
			<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdMeetDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdMeetDate');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
					<SELECT NAME="hdStartTime" CLASS="etc">
					<%
						String[] asHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String msSEL = "";		
						String asHr = mti.substring(0,2);		//��
						if(asHr.substring(0,1).equals("0")) asHr = asHr.substring(1,2);
						String asMi = mti.substring(3,5);		//��
						for(int asH=0; asH<24; asH++){
							if(asHr.equals(Integer.toString(asH))) msSEL = "SELECTED"; else msSEL="";
							if(asMi.equals("00")) {
								out.println("<OPTION " + msSEL + ">" + asHour[asH] + ":" + "00");
								out.println("<OPTION>"  + asHour[asH] + ":" + "30");
							} else { 
								out.println("<OPTION>" + asHour[asH] + ":" + "00");
								out.println("<OPTION " + msSEL + ">"  + asHour[asH] + ":" + "30");
							}
						}
						out.println("</SELECT> ~ ");
					%>
					<SELECT NAME="hdEndTime" CLASS="etc">
					<%
						String[] aeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String meSEL = "";
						String aeHr = mti.substring(mti.length()-5,mti.length()-3);		//��
						if(aeHr.substring(0,1).equals("0")) aeHr = aeHr.substring(1,2);
						String aeMi = mti.substring(mti.length()-2,mti.length());		//��
						for(int aeH=0; aeH<24; aeH++){
							if(aeHr.equals(Integer.toString(aeH))) meSEL = "SELECTED"; else meSEL="";
							if(aeMi.equals("00")) {
								out.println("<OPTION " + meSEL + ">" + aeHour[aeH] + ":" + "00");
								out.println("<OPTION>" + aeHour[aeH] + ":" + "30");
							} else { 
								out.println("<OPTION>" + aeHour[aeH] + ":" + "00");
								out.println("<OPTION " + meSEL + ">"  + aeHour[aeH] + ":" + "30");
							}
						}
					%></SELECT>
			<% } else { %>
					<%=mye%>/<%=mmo%>/<%=mda%>&nbsp&nbsp<%=mti%>
			<% } %></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����ڸ�</td>
           <td width="37%" height="25" class="bg_04">
			<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdChair" VALUE="<%=mna%>" class='text_01' maxlength='10'>
			<% } else { %>
					<%=mna%>
			<% } %></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����ڿ���ó</td>
           <td width="37%" height="25" class="bg_04">
			<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdChairTel" VALUE="<%=mte%>">
			<% } else { %>
					<%=mte%>
			<% } %>			   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table></div>

<!-- ��� -->
<% } else if(Seit.equals("Appointment")) { %>
	<div id="Appointment" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��Ӵ��</td>
           <td width="37%" height="25" class="bg_04">
		<% if(Tact.equals("modify")) { %>
			<INPUT NAME="hdMeetUsers" VALUE="<%=prs%>">
		<% } else { %>
			<%=prs%>
		<% } %></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����ó</td>
           <td width="37%" height="25" class="bg_04">
		<% if(Tact.equals("modify")) { %>
			<INPUT NAME="hdAppointTel" VALUE="<%=mte%>">
		<% } else { %>
			<%=mte%>
		<% } %></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��ӽð�</td>
			<td width="37%" height="25" class="bg_04">
		<% if(Tact.equals("modify")) { %>
			<INPUT NAME="hdAppointDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdAppointDate');"><img src ="../img/calendar.gif" border=0></A>
			<SELECT NAME="hdAppointTime" CLASS="etc">
			<%
				String[] aptHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
				String aSEL = "";
				String aHr = mti.substring(mti.length()-5,mti.length()-3);		//��
				if(aHr.substring(0,1).equals("0")) aHr = aHr.substring(1,2);
				String aMi = mti.substring(mti.length()-2,mti.length());		//��
				for(int aptH=0; aptH<24; aptH++){
					if(aHr.equals(Integer.toString(aptH))) aSEL = "SELECTED"; else aSEL="";
					if(aMi.equals("00")) {
						out.println("<OPTION " + aSEL + ">" + aptHour[aptH] + ":" + "00");
						out.println("<OPTION>" + aptHour[aptH] + ":" + "30");
					} else {
						out.println("<OPTION>" + aptHour[aptH] + ":" + "00");
						out.println("<OPTION " + aSEL + ">" + aptHour[aptH] + ":" + "30");
					}
				}
			%></SELECT>
		<% } else { %>
			<%=toDay%>&nbsp&nbsp<%=mti%>
		<% } %>	</td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
		<% if(Tact.equals("modify")) { %>
			<INPUT NAME="hdAppointLocation" VALUE="<%=mro%>">
		<% } else { %>
			<%=mro%>
		<% } %></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

<!-- ��� -->
<% } else if(Seit.equals("Event")) { %>
	<div id="Event" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdEventDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdEventDate');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEventTime">
					<%
						String[] edHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String edSEL = "";
						String edHr = mti.substring(0,2);								//��
						if(edHr.substring(0,1).equals("0")) edHr = edHr.substring(1,2);
						String edMi = mti.substring(3,5);								//��

						for(int edH=0; edH<24; edH++){
							if(edHr.equals(Integer.toString(edH))) edSEL = "SELECTED"; else edSEL="";
							if(edMi.equals("00")) {
								out.println("<OPTION " + edSEL + ">" + edHour[edH] + ":" + "00");
								out.println("<OPTION>" + edHour[edH] + ":" + "30");
							} else {
								out.println("<OPTION>" + edHour[edH] + ":" + "00");
								out.println("<OPTION " + edSEL + ">" + edHour[edH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=toDay%>&nbsp&nbsp<% out.print(mti.substring(0,2)+":"+mti.substring(3,5));%>
				<% } %></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdEventDate_1" VALUE="<%=endDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdEventDate_1');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEventTime_1">
					<%
						String[] edeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String edeSEL = "";
						String edeHr = mti.substring(mti.length()-5,mti.length()-3);		//��
						if(edeHr.substring(0,1).equals("0")) edeHr = edeHr.substring(1,2);
						String edeMi = mti.substring(mti.length()-2,mti.length());		//��

						for(int edeH=0; edeH<24; edeH++){
							if(edeHr.equals(Integer.toString(edeH))) edeSEL = "SELECTED"; else edeSEL="";
							if(edeMi.equals("00")) {
								out.println("<OPTION " + edeSEL + ">" + edeHour[edeH] + ":" + "00");
								out.println("<OPTION>" + edeHour[edeH] + ":" + "30");
							} else {
								out.println("<OPTION>" + edeHour[edeH] + ":" + "00");
								out.println("<OPTION " + edeSEL + ">" + edeHour[edeH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=endDay%>&nbsp&nbsp<% out.print(mti.substring(mti.length()-5,mti.length()-3) + ":" + mti.substring(mti.length()-2,mti.length()));%>
				<% } %></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="87%" height="25" class="bg_04" colspan="3">
				<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdEventLocation" VALUE="<%=mro%>">
				<% } else { %>
					<%=mro%>
				<% } %></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

<!-- �ް� -->
<% } else if(Seit.equals("Vacation")) { %>
	<div id="Vacation" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdVacationDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdVacationDate');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdVaTime">
					<%
						String[] vdHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String vdSEL = "";
						String vdHr = mti.substring(0,2);								//��
						if(vdHr.substring(0,1).equals("0")) vdHr = vdHr.substring(1,2);
						String vdMi = mti.substring(3,5);								//��

						for(int vdH=0; vdH<24; vdH++){
							if(vdHr.equals(Integer.toString(vdH))) vdSEL = "SELECTED"; else vdSEL="";
							if(vdMi.equals("00")) {
								out.println("<OPTION " + vdSEL + ">" + vdHour[vdH] + ":" + "00");
								out.println("<OPTION>" + vdHour[vdH] + ":" + "30");
							} else {
								out.println("<OPTION>" + vdHour[vdH] + ":" + "00");
								out.println("<OPTION " + vdSEL + ">" + vdHour[vdH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=toDay%>
				<% } %></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdVacationDate_1" VALUE="<%=endDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdVacationDate_1');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdVaTime_1">
					<%
						String[] vdeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String vdeSEL = "";
						String vdeHr = mti.substring(mti.length()-5,mti.length()-3);		//��
						if(vdeHr.substring(0,1).equals("0")) vdeHr = vdeHr.substring(1,2);
						String vdeMi = mti.substring(mti.length()-2,mti.length());		//��

						for(int vdeH=0; vdeH<24; vdeH++){
							if(vdeHr.equals(Integer.toString(vdeH))) vdeSEL = "SELECTED"; else vdeSEL="";
							if(vdeMi.equals("00")) {
								out.println("<OPTION " + vdeSEL + ">" + vdeHour[vdeH] + ":" + "00");
								out.println("<OPTION>" + vdeHour[vdeH] + ":" + "30");
							} else {
								out.println("<OPTION>" + vdeHour[vdeH] + ":" + "00");
								out.println("<OPTION " + vdeSEL + ">"  + vdeHour[vdeH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=endDay%>
				<% } %>	</td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��޿���ó</td>
			<td width="87%" height="25" class="bg_04" colspan="3">
				<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdUrgencyTel" VALUE="<%=mte%>">
				<% } else { %>
					<%=mte%>
				<% } %>				
			</td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

<!-- �������� -->
<% } else if(Seit.equals("TripH")) { %>
	<div id="TripH" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdTripDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdTripDate');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdTripTime">
					<%
						String[] tdHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdSEL = "";
						String tdHr = mti.substring(0,2);								//��
						if(tdHr.substring(0,1).equals("0")) tdHr = tdHr.substring(1,2);
						String tdMi = mti.substring(3,5);								//��

						for(int tdH=0; tdH<24; tdH++){
							if(tdHr.equals(Integer.toString(tdH))) tdSEL = "SELECTED"; else tdSEL="";
							if(tdMi.equals("00")) {
								out.println("<OPTION " + tdSEL + ">" + tdHour[tdH] + ":" + "00");
								out.println("<OPTION>" + tdHour[tdH] + ":" + "30");
							} else {
								out.println("<OPTION>" + tdHour[tdH] + ":" + "00");
								out.println("<OPTION " + tdSEL + ">" + tdHour[tdH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=toDay%>&nbsp&nbsp<% out.print(mti.substring(0,2)+":"+mti.substring(3,5));%>
				<% } %>	</td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdTripDate_1" VALUE="<%=endDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdTripDate_1');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdTripTime_1">
					<%
						String[] tdeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdeSEL = "";
						String tdeHr = mti.substring(mti.length()-5,mti.length()-3);		//��
						if(tdeHr.substring(0,1).equals("0")) tdeHr = tdeHr.substring(1,2);
						String tdeMi = mti.substring(mti.length()-2,mti.length());		//��

						for(int tdeH=0; tdeH<24; tdeH++){
							if(tdeHr.equals(Integer.toString(tdeH))) tdeSEL = "SELECTED"; else tdeSEL="";
							if(tdeMi.equals("00")) {
								out.println("<OPTION " + tdeSEL + ">" + tdeHour[tdeH] + ":" + "00");
								out.println("<OPTION>" + tdeHour[tdeH] + ":" + "30");
							} else {
								out.println("<OPTION>" + tdeHour[tdeH] + ":" + "00");
								out.println("<OPTION " + tdeSEL + ">" + tdeHour[tdeH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=endDay%>&nbsp&nbsp<% out.print(mti.substring(mti.length()-5,mti.length()-3) + ":" + mti.substring(mti.length()-2,mti.length()));%>
				<% } %>	</td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">���� ����ó</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdTripLoc" VALUE="<%=mro%>">
				<% } else { %>
					<%=mro%>
				<% } %>				
			</td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��޿���ó</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdTripTel" VALUE="<%=mte%>">
				<% } else { %>
					<%=mte%>
				<% } %></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></Table></div>

<!-- �ؿ����� -->
<% } else if(Seit.equals("TripA")) { %>
	<div id="TripA" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdTripADate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdTripADate');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdTripATime">
					<%
						String[] tdAHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdASEL = "";
						String tdAHr = mti.substring(0,2);								//��
						if(tdAHr.substring(0,1).equals("0")) tdAHr = tdAHr.substring(1,2);
						String tdAMi = mti.substring(3,5);								//��

						for(int tdAH=0; tdAH<24; tdAH++){
							if(tdAHr.equals(Integer.toString(tdAH))) tdASEL = "SELECTED"; else tdASEL="";
							if(tdAMi.equals("00")) {
								out.println("<OPTION " + tdASEL + ">" + tdAHour[tdAH] + ":" + "00");
								out.println("<OPTION>" + tdAHour[tdAH] + ":" + "30");
							} else {
								out.println("<OPTION>" + tdAHour[tdAH] + ":" + "00");
								out.println("<OPTION " + tdASEL + ">" + tdAHour[tdAH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=toDay%>&nbsp&nbsp<% out.print(mti.substring(0,2)+":"+mti.substring(3,5));%>
				<% } %></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdTripADate_1" VALUE="<%=endDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdTripADate_1');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdTripATime_1">
					<%
						String[] tdeAHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdeASEL = "";
						String tdeAHr = mti.substring(mti.length()-5,mti.length()-3);		//��
						if(tdeAHr.substring(0,1).equals("0")) tdeAHr = tdeAHr.substring(1,2);
						String tdeAMi = mti.substring(mti.length()-2,mti.length());		//��

						for(int tdeAH=0; tdeAH<24; tdeAH++){
							if(tdeAHr.equals(Integer.toString(tdeAH))) tdeASEL = "SELECTED"; else tdeASEL="";
							if(tdeAMi.equals("00")) {
								out.println("<OPTION " + tdeASEL + ">" + tdeAHour[tdeAH] + ":" + "00");
								out.println("<OPTION>" + tdeAHour[tdeAH] + ":" + "30");
							} else {
								out.println("<OPTION>" + tdeAHour[tdeAH] + ":" + "00");
								out.println("<OPTION " + tdeASEL + ">" + tdeAHour[tdeAH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=endDay%>&nbsp&nbsp<% out.print(mti.substring(mti.length()-5,mti.length()-3) + ":" + mti.substring(mti.length()-2,mti.length()));%>
				<% } %>	</td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�ؿ� ����ó</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdTripALoc" VALUE="<%=mro%>">
				<% } else { %>
					<%=mro%>
				<% } %>				
			</td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��޿���ó</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdTripATel" VALUE="<%=mte%>">
				<% } else { %>
					<%=mte%>
				<% } %></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></Table></div>

<!-- ����� -->
<% } else if(Seit.equals("Holiday")) { %>
	<div id="Holiday" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�������</td>
			<td width="87%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdHolidayDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdHolidayDate');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdHolidayTime">
					<%
						String[] hdHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String hdSEL = "";
						String hdHr = mti.substring(0,2);								//��
						if(hdHr.substring(0,1).equals("0")) hdHr = hdHr.substring(1,2);
						String hdMi = mti.substring(3,5);								//��

						for(int hdH=0; hdH<24; hdH++){
							if(hdHr.equals(Integer.toString(hdH))) hdSEL = "SELECTED"; else hdSEL="";
							if(hdMi.equals("00")) {
								out.println("<OPTION " + hdSEL + ">" + hdHour[hdH] + ":" + "00");
								out.println("<OPTION>" + hdHour[hdH] + ":" + "30");
							} else {
								out.println("<OPTION>" + hdHour[hdH] + ":" + "00");
								out.println("<OPTION " + hdSEL + ">" + hdHour[hdH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=toDay%>&nbsp&nbsp<%=mti%>
				<% } %></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></Table></div>

<!-- �湮 -->
<% } else if(Seit.equals("Area")) { %>
	<div id="Area" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdAreaDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdAreaDate');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdAreaTime">
					<%
						String[] tdLHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdLSEL = "";
						String tdLHr = mti.substring(0,2);								//��
						if(tdLHr.substring(0,1).equals("0")) tdLHr = tdLHr.substring(1,2);
						String tdLMi = mti.substring(3,5);								//��

						for(int tdLH=0; tdLH<24; tdLH++){
							if(tdLHr.equals(Integer.toString(tdLH))) tdLSEL = "SELECTED"; else tdLSEL="";
							if(tdLMi.equals("00")) {
								out.println("<OPTION " + tdLSEL + ">" + tdLHour[tdLH] + ":" + "00");
								out.println("<OPTION>" + tdLHour[tdLH] + ":" + "30");
							} else {
								out.println("<OPTION>" + tdLHour[tdLH] + ":" + "00");
								out.println("<OPTION " + tdLSEL + ">" + tdLHour[tdLH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=toDay%>&nbsp&nbsp<% out.print(mti.substring(0,2)+":"+mti.substring(3,5));%>
				<% } %>	</td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdAreaDate_1" VALUE="<%=endDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdAreaDate_1');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdAreaTime_1">
					<%
						String[] tdeLHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdeLSEL = "";
						String tdeLHr = mti.substring(mti.length()-5,mti.length()-3);		//��
						if(tdeLHr.substring(0,1).equals("0")) tdeLHr = tdeLHr.substring(1,2);
						String tdeLMi = mti.substring(mti.length()-2,mti.length());		//��

						for(int tdeLH=0; tdeLH<24; tdeLH++){
							if(tdeLHr.equals(Integer.toString(tdeLH))) tdeLSEL = "SELECTED"; else tdeLSEL="";
							if(tdeLMi.equals("00")) {
								out.println("<OPTION " + tdeLSEL + ">" + tdeLHour[tdeLH] + ":" + "00");
								out.println("<OPTION>" + tdeLHour[tdeLH] + ":" + "30");
							} else {
								out.println("<OPTION>" + tdeLHour[tdeLH] + ":" + "00");
								out.println("<OPTION " + tdeLSEL + ">" + tdeLHour[tdeLH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=endDay%>&nbsp&nbsp<% out.print(mti.substring(mti.length()-5,mti.length()-3) + ":" + mti.substring(mti.length()-2,mti.length()));%>
				<% } %></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�湮��</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdArea" VALUE="<%=mro%>" class='text_01'>
				<% } else { %>
					<%=mro%>
				<% } %>				
			</td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��޿���ó</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdAreaTel" VALUE="<%=mte%>">
				<% } else { %>
					<%=mte%>
				<% } %></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></Table></div>

<!-- ����/���̳� -->
<% } else if(Seit.equals("Education")) { %>
	<div id="Education" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�����Ⱓ</td>
			<td width="37%" height="25" class="bg_04">
			<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdEducationDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdEducationDate');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
					<SELECT NAME="hdEduStartTime" CLASS="etc">
					<%
						String[] edusHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String msSEL = "";		
						String easHr = mti.substring(0,2);		//��
						if(easHr.substring(0,1).equals("0")) easHr = easHr.substring(1,2);
						String easMi = mti.substring(3,5);		//��
						for(int asH=0; asH<24; asH++){
							if(easHr.equals(Integer.toString(asH))) msSEL = "SELECTED"; else msSEL="";
							if(easMi.equals("00")) {
								out.println("<OPTION " + msSEL + ">" + edusHour[asH] + ":" + "00");
								out.println("<OPTION>"  + edusHour[asH] + ":" + "30");
							} else { 
								out.println("<OPTION>" + edusHour[asH] + ":" + "00");
								out.println("<OPTION " + msSEL + ">"  + edusHour[asH] + ":" + "30");
							}
						}
						out.println("</SELECT> ~ ");
					%>
					<SELECT NAME="hdEduEndTime" CLASS="etc">
					<%
						String[] edueHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String meSEL = "";
						String eaeHr = mti.substring(mti.length()-5,mti.length()-3);		//��
						if(eaeHr.substring(0,1).equals("0")) eaeHr = eaeHr.substring(1,2);
						String eaeMi = mti.substring(mti.length()-2,mti.length());		//��
						for(int aeH=0; aeH<24; aeH++){
							if(eaeHr.equals(Integer.toString(aeH))) meSEL = "SELECTED"; else meSEL="";
							if(eaeMi.equals("00")) {
								out.println("<OPTION " + meSEL + ">" + edueHour[aeH] + ":" + "00");
								out.println("<OPTION>" + edueHour[aeH] + ":" + "30");
							} else { 
								out.println("<OPTION>" + edueHour[aeH] + ":" + "00");
								out.println("<OPTION " + meSEL + ">"  + edueHour[aeH] + ":" + "30");
							}
						}
					%></SELECT>
			<% } else { %>
					<%=mye%>/<%=mmo%>/<%=mda%>&nbsp&nbsp<%=mti%>
			<% } %></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�������</td>
			<td width="37%" height="25" class="bg_04">
			<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdEduLocation" VALUE="<%=mro%>" class='text_01'>
			<% } else { %>
					<%=mro%>
			<% } %></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�����ְ�</td>
			<td width="37%" height="25" class="bg_04">
			<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdEduChair" VALUE="<%=mna%>" class='text_01'>
			<% } else { %>
					<%=mna%>
			<% } %></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����� ����ó</td>
			<td width="37%" height="25" class="bg_04">
			<% if(Tact.equals("modify")) { %>
					<INPUT NAME="hdEduChairTel" VALUE="<%=mte%>">
			<% } else { %>
					<%=mte%>
			<% } %></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

<!-- ��Ÿ -->
<% } else { %>
	<div id="Etc" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdEtcDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdEtcDate');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEtcTime">
					<%
						String[] etHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String etSEL = "";
						String etHr = mti.substring(0,2);								//��
						if(etHr.substring(0,1).equals("0")) etHr = etHr.substring(1,2);
						String etMi = mti.substring(3,5);								//��

						for(int etH=0; etH<24; etH++){
							if(etHr.equals(Integer.toString(etH))) etSEL = "SELECTED"; else etSEL="";
							if(etMi.equals("00")) {
								out.println("<OPTION " + etSEL + ">" + etHour[etH] + ":" + "00");
								out.println("<OPTION>" + etHour[etH] + ":" + "30");
							} else {
								out.println("<OPTION>" + etHour[etH] + ":" + "00");
								out.println("<OPTION " + etSEL + ">" + etHour[etH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=toDay%>&nbsp&nbsp<% out.print(mti.substring(0,2)+":"+mti.substring(3,5));%>
				<% } %></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
			<td width="37%" height="25" class="bg_04">
				<% if(Tact.equals("modify")) { %>
				<INPUT NAME="hdEtcDate_1" VALUE="<%=endDay%>" SIZE=10 READONLY><A Href="Javascript:OpenCalendar('hdEtcDate_1');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEtcTime_1">
					<%
						String[] eteHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String eteSEL = "";
						String eteHr = mti.substring(mti.length()-5,mti.length()-3);		//��
						if(eteHr.substring(0,1).equals("0")) eteHr = eteHr.substring(1,2);
						String eteMi = mti.substring(mti.length()-2,mti.length());		//��

						for(int eteH=0; eteH<24; eteH++){
							if(eteHr.equals(Integer.toString(eteH))) eteSEL = "SELECTED"; else eteSEL="";
							if(eteMi.equals("00")) {
								out.println("<OPTION " + eteSEL + ">" + eteHour[eteH] + ":" + "00");
								out.println("<OPTION>" + eteHour[eteH] + ":" + "30");
							} else {
								out.println("<OPTION>" + eteHour[eteH] + ":" + "00");
								out.println("<OPTION " + eteSEL + ">" + eteHour[eteH] + ":" + "30");
							}
						}
					%></SELECT>
				<% } else { %>
					<%=endDay%>&nbsp&nbsp<% out.print(mti.substring(mti.length()-5,mti.length()-3) + ":" + mti.substring(mti.length()-2,mti.length()));%>
				<% } %></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>
<% } %>

	<!-- ���� -->
	<div id="comment" style="left:0px;visiblity:visible;">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����</td>
			<td width="87%" height="25" class="bg_04">
		  <% if(Tact.equals("modify")) { 
				con = str.repWord(con,"<br>","");	  
		  %>
				<TEXTAREA NAME="Body" ROWS=7 COLS=70 WRAP=VIRTUAL><%=con%></TEXTAREA>
		  <% } else { %>
				<%=con%>
		  <% } %></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

</td></tr></table>
<input Type="hidden" Name="hdMembersInfo" value="">
<input Type="hidden" Name="hdDocType" value="">
<input Type="hidden" Name="opendocument" value="save">
</form>

<form>
<input Type="hidden" Name="hdFrom" value="opendocument&view=CalendarView">
<input Type="hidden" Name="hdRelationDocList" value="">
</form>

</BODY>
</HTML>

<% if(Message == "NO_SESSION") { %>
<script>
alert("login������ ������ϴ�. �ٽ� �����Ͻʽÿ�.")
</script>
<% Message = "" ; } %>

<% if(Message == "DELETE") { %>
<script>
document.location.href="Calendar_View.jsp";
</script>
<% Message = "" ; } %>

<% if(Message == "UPDATE") { %>
<script>
document.location.href="Calendar_View.jsp";
</script>
<% Message = "" ; } %>