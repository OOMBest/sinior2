<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="/resource/css/css.css" />
<script type="text/javascript">
	
	function pagination(m) {
		 location.href = "redisList?currentPage=" + m;
	}

</script>
</head>
<body>

	<table>

		<tr>
			<td>id</td>
			<td>名称</td>
			<td>价格</td>
			<td>已售百分比</td>
		</tr>

		<c:forEach items="${list }" var="a">
			<tr>
				<td>${a.id}</td>
				<td>${a.name}</td>
				<td>${a.price}</td>
				<td>${a.soldOut}</td>
			</tr>
		</c:forEach>

		<tr>
			<td colspan="5">${page.pagination.paginationTab }</td>
		</tr>

	</table>



</body>
</html>