<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value='${pageContext.request.contextPath}/'/>
<script>
	alert('�� �ۼ� �Ϸ�')
	location.href='${root}board/read?board_info_idx=${writeContentBean.content_board_idx}&content_idx=${writeContentBean.content_idx}'
</script>