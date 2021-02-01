<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/market/css/index/header.css">

<div class="first">
	<div class="main-logo">
		<img src="/market/image/index/fleamarket.png"
			style="height: 100%; cursor: pointer;" alt="로고들어갈 곳"
			onclick="location.href='/market/index.jsp'">
		<!-- <h5>플리마켓</h5> -->
	</div>

	<!-- 검색창 영역 -->
	<div class="search-area">
		<!-- 검색 입력 -->
		<form id="searchForm" name="searchForm">
		<div class="input-group">
			<input id="searchProduct" name="searchProduct" type="text" placeholder="상품명, @상점명 입력"> <i id="searchBtn" style="cursor: pointer;"
				onclick="location.href='/market/index/searchDisplay'"
				class="fas fa-search"></i>
		</div>
		</form>
	</div>

	<!-- 우측메뉴 -->
	<div  class="header-btn">
		<a onclick="location.href='/market/product/registForm'"> <i style="color:#0a58ca;"
			class="fas fa-won-sign"></i> 판매하기  
		</a><a onclick="location.href='/market/store/store'"> <i style="color:#0a58ca;"
			class="fas fa-user-check"></i> 내 상점
		</a> <a id="chat"><i style="color:#0a58ca;"
			class="far fa-comments"></i> 바다톡</a> <a><i class="fas fa-book-open" style="color:#0a58ca;"></i>
			커뮤니티</a>
	</div>

</div>

<div class="dropdown-area">
	<div class="dropdown">
		<a id="btn_menu" class="cateList" data-bs-toggle="dropdown" 
			aria-expanded="false">
			<div class="dropmenu">
				<ul>
					<li><a class="fas fa-bars" style="height: 30px; weight:30px;"></a>
						<ul>
							전체 카테고리
							<a href="#" class="cateBig">여성의류</a>
							<a href="#" class="cateBig">남성의류</a>
							<a href="#" class="cateBig">패션잡화</a>
							<a href="#" class="cateBig">생활/가구/식품</a>
							<a href="#" class="cateBig">디지털/가전</a>
							<a href="#" class="cateBig">도서/취미/애완</a>
							<a href="#" class="cateBig">스포츠/레저</a>
							<a href="#" class="cateBig">스타굿즈</a>
							<a href="#" class="cateBig">뷰티/미용</a>
							<a href="#" class="cateBig">차량/오토바이</a>
							<a href="#" class="cateBig">기타</a>
						</ul></li>
				</ul>
			</div>
		</a>
	</div>
</div>



<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="/market/js/index/header.js"></script>
<script type="text/javascript">
//로그인 함수
$("#searchProduct").keydown(function(key) {
	if (key.keyCode == 13) {
		$('#searchBtn').trigger('click');//강제로 이벤트를 발생
	
	}
});
</script>

