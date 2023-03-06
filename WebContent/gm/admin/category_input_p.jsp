<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query	= "";
	String j		= request.getParameter("j")==null?"":request.getParameter("j");
	String gcode	= request.getParameter("gcode")==null?"":request.getParameter("gcode");
	String ancestor	= request.getParameter("ancestor") == null?"0":request.getParameter("ancestor");
	String mid		= request.getParameter("mid"); 
	String error	= "no";

	String code		= request.getParameter("code") == ""?"":request.getParameter("code");
	String name		= request.getParameter("name") == ""?"":Hanguel.toHanguel(request.getParameter("name"));
	String level	= request.getParameter("level") == ""?"":request.getParameter("level");

	bean.openConnection();	
	
	if(j.equals("d")) { // ���� ���
	    query = "SELECT COUNT(*) FROM goods_structure WHERE ancestor ='" + mid +"'";
		bean.executeQuery(query);
		while(bean.next()){
		    if(Integer.parseInt(bean.getData(1)) > 0) error = "�����ϰ��� �з��� �����з��� �����մϴ�. ������ �� �����ϴ�.";
		}
	}

	if(j.equals("a")) { // �� ��ǰ �Է�
		// �����з� ������ ���� �����ڵ�(gcode)�� ����Ѵ�.
		query = "SELECT MAX(gcode) FROM goods_structure WHERE glevel = '" + level +"' and ancestor = '" + ancestor + "'";
		bean.executeQuery(query);
		bean.next();
		String max_gcode = bean.getData(1);

		if(max_gcode == null){
			if(level.equals("1")){	// ��ǰ���� ���� ���� 10���� �����Ѵ�.
				gcode = "10";
			}else{					// ��ǰ,�𵨱�,���� ���
				//���� �з��� gcode ���� ������ ��, ����gcode + 01
				query = "SELECT gcode FROM goods_structure WHERE mid = '" + ancestor +"'";
				bean.executeQuery(query);
				bean.next();
				String up_gcode = bean.getData("gcode");
				gcode = up_gcode + "01";
			}
		}else{
			gcode = Integer.toString(Integer.parseInt(max_gcode)+1);
		}

		//�ߺ�üũ
		query = "SELECT COUNT(*) FROM goods_structure WHERE glevel = '" + level + "' AND code = '" + code + "'";
		bean.executeQuery(query);
		bean.next();
		String count  = bean.getData(1);

		if(!count.equals("0")){
%>
			<script language=javascript>
				alert("��ǰ�ڵ尡 �ߺ��Ǿ� �߰��� �� �����ϴ�. ��ǰ�ڵ带 Ȯ���� �ٽ� �õ��Ͻʽÿ�.");
				this.close();
			</script>
<%			return;	
		}
		//������� �ߺ�üũ

		query = "INSERT INTO goods_structure (code,name,glevel,ancestor,gcode) VALUES('"+code+"', '"+name+"','"+level+"','"+ancestor+"','"+gcode+"')";
		bean.executeUpdate(query);

	} else if (j.equals("u")) { // �������

		//�ߺ�üũ
		query = "SELECT COUNT(*) FROM goods_structure WHERE glevel = '" + level + "' AND code = '" + code + "' AND gcode != '" + gcode + "'";
		bean.executeQuery(query);
		bean.next();
		String count  = bean.getData(1);

		if(!count.equals("0")){
%>
			<script language=javascript>
				alert("��ǰ�ڵ尡 �ߺ��Ǿ� ������ �� �����ϴ�. ��ǰ�ڵ带 Ȯ���� �ٽ� �õ��Ͻʽÿ�.");
				this.close();
			</script>
<%			return;	
		}
		//������� �ߺ�üũ

		
		query = "UPDATE goods_structure SET code='"+code+"',name='"+name+"' WHERE gcode = '"+gcode+"'";
		bean.executeUpdate(query);

	} else if (j.equals("d")) { // �������
		if(error.equals("no")){
			query = "DELETE FROM goods_structure WHERE mid = '"+mid+"'";
			bean.executeUpdate(query);
		}else{
			out.println("<script>alert('"+error+"');history.go(-1);</script>");			
		}
	}
%>
<script language=javascript>
//	alert("���������� ó���Ǿ����ϴ�");
	opener.location.reload();
	this.close();
</script>