<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="container" id="search-area">
    <div class="row">
        <jsp:include page="header.jsp"></jsp:include>
    </div>
    <form action="/board/list" method="get" class="mb-3 row">
        <div class="input-group">
        <div class="col-2 mt-3">
                <c:if test="${key=='boardTitle'}">
        <select name="key" class="form-select">
            <option value="boardTitle" selected>제목</option>
            <option value="boardWriter">작성자</option>
        </select>
                </c:if>
                <c:if test="${key=='boardWriter'}">
                    <select name="key" class="form-select">
                        <option value="boardTitle">제목</option>
                        <option value="boardWriter" selected>작성자</option>
                    </select>
                </c:if>
        </div>
        <div class="col-2 mt-3">
        <input type="text" class="form-control" name="query" placeholder="검색어를 입력하세요">
        </div>
        <div class="col-2 mt-3">
        <input type="submit" value="검색" class="btn btn-primary">
        </div>
        </div>
    </form>
    </div>
</div>

<div class="container" id="list">
    <c:if test="${paging.maxPage>=1}">
    <table class="table table-striped table-hover text-center">
        <tr>
            <th>글번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성시간</th>
            <th>조회수</th>
        </tr>
        <c:forEach items="${boardList}" var="board">
            <tr style="cursor: pointer" onclick="location.href='/board?id=${board.id}&page=${paging.page}&query=${query}&key=${key}&pageLimit=${pageLimit}'">
                <td>${board.id}</td>
                <td>${board.boardTitle}</td>
                <td>${board.boardWriter}</td>
                <td>${board.createdAt}</td>
                <td>${board.boardHits}</td>
            </tr>
        </c:forEach>
    </table>
    </c:if>
    <c:if test="${paging.maxPage==0}">
        <h1 class="text-center mt-5">작성된 글이 없습니다.</h1>
    </c:if>
    <div class="col-1">
    <select name="pageLimit" id="pageLimit" class="form-select">
        <option value="3" >3개씩보기</option>
        <option value="5" >5개씩보기</option>
        <option value="10" >10개씩보기</option>
    </select>
    </div>
    <div class="text-end">
        <a href="/board/save?page=${paging.page}&query=${query}&key=${key}&pageLimit=${pageLimit}'"><button class="btn btn-primary">글쓰기</button></a>
    </div>
</div>


<%-- 페이지 번호 출력 부분 --%>
<div class="container">
    <ul class="pagination justify-content-center">
        <c:if test="${paging.maxPage>=1}">
        <c:choose>
            <%-- 현재 페이지가 1페이지면 이전 글자만 보여줌 --%>
            <c:when test="${paging.page<=paging.startPage}">
                <li class="page-item disabled">
                    <a class="page-link">[<<]</a>
                </li>
            </c:when>
            <%-- 1페이지가 아닌 경우에는 [이전]을 클릭하면 현재 페이지보다 1 작은 페이지 요청 --%>
            <c:otherwise>
                <li class="page-item">
                    <a class="page-link" href="/board/list?page=${paging.startPage}&query=${query}&key=${key}&pageLimit=${pageLimit}">[<<]</a>
                </li>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <%-- 현재 페이지가 1페이지면 이전 글자만 보여줌 --%>
            <c:when test="${paging.page<=1}">
                <li class="page-item disabled">
                    <a class="page-link">[이전]</a>
                </li>
            </c:when>
            <%-- 1페이지가 아닌 경우에는 [이전]을 클릭하면 현재 페이지보다 1 작은 페이지 요청 --%>
            <c:otherwise>
                <li class="page-item">
                    <a class="page-link" href="/board/list?page=${paging.page-1}&query=${query}&key=${key}&pageLimit=${pageLimit}">[이전]</a>
                </li>
            </c:otherwise>
        </c:choose>

        <%--  for(int i=startPage; i<=endPage; i++)      --%>
        <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="i" step="1">
            <c:choose>
                <%-- 요청한 페이지에 있는 경우 현재 페이지 번호는 텍스트만 보이게 --%>
                <c:when test="${i eq paging.page}">
                    <li class="page-item active">
                        <a class="page-link">${i}</a>
                    </li>
                </c:when>

                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link" href="/board/list?page=${i}&query=${query}&key=${key}&pageLimit=${pageLimit}">${i}</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:choose>
            <c:when test="${paging.page>=paging.maxPage}">
                <li class="page-item disabled">
                    <a class="page-link">[다음]</a>
                </li>
            </c:when>
            <c:otherwise>
                <li class="page-item">
                    <a class="page-link" href="/board/list?page=${paging.page+1}&query=${query}&key=${key}&pageLimit=${pageLimit}">[다음]</a>
                </li>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${paging.page>=paging.maxPage}">
                <li class="page-item disabled">
                    <a class="page-link">[>>]</a>
                </li>
            </c:when>
            <c:otherwise>
                <li class="page-item">
                    <a class="page-link" href="/board/list?page=${paging.endPage}&query=${query}&key=${key}&pageLimit=${pageLimit}">[>>]</a>
                </li>
            </c:otherwise>
        </c:choose>
        </c:if>
    </ul>
</div>
</body>
<script>
    console.log(${key})
    $("#pageLimit").on("change", function(){
        const pageLimit = document.getElementById("pageLimit").value;
        location.href="/board/list?page=${paging.endPage}&query=${query}&key=${key}&pageLimit=" + pageLimit;
    })
</script>
</html>
