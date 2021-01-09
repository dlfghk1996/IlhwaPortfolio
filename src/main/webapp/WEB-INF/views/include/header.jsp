<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- navi -->
<nav class="navbar navbar-default navbar-static-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index.html">ILHWA's Portfolio</a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav navbar-right">
				<li><a href="board">게시판</a></li>
				<li><a href="#">menu2</a></li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">menu3<span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
						<li><a href="#">My To-Do List</a></li>
						<li><a href="#">Our To-Do List</a></li>
					</ul>
				</li>
				<c:set var="memberDTO" value="${members}"/>
				<c:choose>
					<c:when test="${memberDTO == null}">
						<li><a href="login">Login</a></li>
					</c:when>
					<c:otherwise>
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">${memberDTO.name} 님<span class="caret"></span></a>
							<ul class="dropdown-menu" role="menu">
								<li><a href="myPage">MyPage</a></li>
								<li><a href="logOut">LogOut</a></li>
							</ul>
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
</nav>