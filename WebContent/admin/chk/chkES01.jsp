<%
/******************************************************
 * 각 모듈별 사용권한 관리을 위해서는 이 파일을 해당 
 * 페이지 내에 include 시킨다.
 *
 * 근태관리 모듈 관리자
 ******************************************************/
String prg_priv = sl.privilege;

int idx = prg_priv.indexOf("ES01");
if (idx < 0){ // 사용권한이 없을 경우
%>
	<script>
		alert("이 작업을 수행하기 위한 권한이 없습니다.\n관리자에게 문의하세요.");
		history.go(-1);
	</script>
<%
	return;
}
%>