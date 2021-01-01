<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page isELIgnored="false" %>
<nav class="navbar navbar-default navbar-static-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
	            <span class="sr-only">Toggle navigation</span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
       		</button>
       		<a class="navbar-brand" href="index.html">NEXT LINE</a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav navbar-right">
			<c:set var ="memberDTO" value="${members}" />
				<c:choose>
					<c:when test="${memberDTO == null}">
						 <li><a href="login">Login</a></li>
					</c:when>
					<c:otherwise>  
						<li><a href="#">${memberDTO.name} </a></li>
						<li><a href="myPage">MyPage</a></li>
						<li><a href="logOut">Logout</a></li>
					</c:otherwise>
			    </c:choose>
			   	<li><a href="board">게시판</a></li>
				<li class="dropdown">
	        		<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">To-Do List <span class="caret"></span></a>
	        		<ul class="dropdown-menu" role="menu">
						<li><a href="#">My To-Do List</a></li>
				        <li><a href="#">Our To-Do List</a></li>
				    </ul>
				</li>
				<li><a href="Community.html">Community</a></li>
			</ul>
       	</div>
	</div>
</nav>