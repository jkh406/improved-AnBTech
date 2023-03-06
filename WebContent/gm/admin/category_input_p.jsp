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
	
	if(j.equals("d")) { // 삭제 모드
	    query = "SELECT COUNT(*) FROM goods_structure WHERE ancestor ='" + mid +"'";
		bean.executeQuery(query);
		while(bean.next()){
		    if(Integer.parseInt(bean.getData(1)) > 0) error = "삭제하고자 분류에 하위분류가 존재합니다. 삭제할 수 없습니다.";
		}
	}

	if(j.equals("a")) { // 새 제품 입력
		// 하위분류 쿼리를 위한 내부코드(gcode)를 계산한다.
		query = "SELECT MAX(gcode) FROM goods_structure WHERE glevel = '" + level +"' and ancestor = '" + ancestor + "'";
		bean.executeQuery(query);
		bean.next();
		String max_gcode = bean.getData(1);

		if(max_gcode == null){
			if(level.equals("1")){	// 제품군일 경우는 최초 10으로 시작한다.
				gcode = "10";
			}else{					// 제품,모델군,모델일 경우
				//상위 분류의 gcode 값을 가져온 후, 상위gcode + 01
				query = "SELECT gcode FROM goods_structure WHERE mid = '" + ancestor +"'";
				bean.executeQuery(query);
				bean.next();
				String up_gcode = bean.getData("gcode");
				gcode = up_gcode + "01";
			}
		}else{
			gcode = Integer.toString(Integer.parseInt(max_gcode)+1);
		}

		//중복체크
		query = "SELECT COUNT(*) FROM goods_structure WHERE glevel = '" + level + "' AND code = '" + code + "'";
		bean.executeQuery(query);
		bean.next();
		String count  = bean.getData(1);

		if(!count.equals("0")){
%>
			<script language=javascript>
				alert("제품코드가 중복되어 추가할 수 없습니다. 제품코드를 확인후 다시 시도하십시오.");
				this.close();
			</script>
<%			return;	
		}
		//여기까지 중복체크

		query = "INSERT INTO goods_structure (code,name,glevel,ancestor,gcode) VALUES('"+code+"', '"+name+"','"+level+"','"+ancestor+"','"+gcode+"')";
		bean.executeUpdate(query);

	} else if (j.equals("u")) { // 수정모드

		//중복체크
		query = "SELECT COUNT(*) FROM goods_structure WHERE glevel = '" + level + "' AND code = '" + code + "' AND gcode != '" + gcode + "'";
		bean.executeQuery(query);
		bean.next();
		String count  = bean.getData(1);

		if(!count.equals("0")){
%>
			<script language=javascript>
				alert("제품코드가 중복되어 수정할 수 없습니다. 제품코드를 확인후 다시 시도하십시오.");
				this.close();
			</script>
<%			return;	
		}
		//여기까지 중복체크

		
		query = "UPDATE goods_structure SET code='"+code+"',name='"+name+"' WHERE gcode = '"+gcode+"'";
		bean.executeUpdate(query);

	} else if (j.equals("d")) { // 삭제모드
		if(error.equals("no")){
			query = "DELETE FROM goods_structure WHERE mid = '"+mid+"'";
			bean.executeUpdate(query);
		}else{
			out.println("<script>alert('"+error+"');history.go(-1);</script>");			
		}
	}
%>
<script language=javascript>
//	alert("정상적으로 처리되었습니다");
	opener.location.reload();
	this.close();
</script>