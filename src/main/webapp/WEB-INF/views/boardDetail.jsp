<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <link rel="stylesheet" href="/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="/resources/css/style2.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css">
</head>
<body>
<div class="container">
    <div class="row ms-2">
        <jsp:include page="header.jsp"></jsp:include>
    </div>
    <div class="row ms-2">
        <h2 class="mt-4 ms-2" style="color: #333D77; font-weight:900;">Member Board &copy;</h2>
        <hr style="border: solid 3px #333D77;">
    </div>
    <div class="row ms-2">
        <div class="col" style="font-size: 20px">${board.boardTitle}</div>
    </div>
    <div class="row ms-2">
        <div class="col" style="font-size: 15px">
            ${board.boardWriter}
        </div>
        <div class="col">
            ${board.createdAt}
        </div>
        <div class="col">
            조회 ${board.boardHits}
        </div>
        <hr style="border: solid 2px">
    </div>
    <div class="row ms-2">
        <div class="col" id="board-list-area">
            <c:if test="${board.fileAttached == 1}">
                <c:forEach items="${boardFileList}" var="boardFile" varStatus="vs">
                    <img src="${pageContext.request.contextPath}/upload/${boardFile.storedFileName}"
                         alt="" width="500" height="500">
                </c:forEach>
            </c:if>
        </div>
    </div>
    <div class="row ms-4 mb-5">
        ${board.boardContents}
    </div>

    <div class="row justify-content-end">
        <div class="col-2" style="display:flex">
            <c:if test="${board.boardWriter == user or member==null and user!='admin@admin.com'}">
                <button onclick="board_update()" class="btn btn-primary m-1">수정</button>
                <button onclick="board_delete()" class="btn btn-primary m-1">삭제</button>
            </c:if>
            <c:if test="${user=='admin@admin.com'}">
                <button onclick="board_delete()" class="btn btn-primary m-1">삭제</button>
            </c:if>
            <button onclick="board_list()" class=" btn btn-primary m-1">목록</button>
        </div>
    </div>
    <hr style="border: solid 2px">

    <div class="text-end" id="pass-check" style="display: none;">
        <input type="text" id="board-pass" placeholder="(수정)비밀번호 입력하세요">
        <input type="button" onclick="pass_check()" value="확인">
    </div>

    <div class="text-end" id="pass-dcheck" style="display: none;">
        <input type="text" id="board-dpass" placeholder="(삭제)비밀번호 입력하세요">
        <input type="button" onclick="pass_dcheck()" value="확인">
    </div>

    <div class="table input-group mt-5">
        <div style="width:300px;height:175px;">
            <c:choose>
                <c:when test="${user!=null}">
                    <input type="text" id="comment-writer" value="${user}">
                    <input type="hidden" id="comment-password" value=null>
                </c:when>
                <c:otherwise>
                    <input type="text" id="comment-writer" placeholder="작성자 입력">
                    <input type="password" id="comment-password" placeholder="비번입력 입력">
                </c:otherwise>
            </c:choose>

        </div>
        <div>
            <textarea id="comment-contents" rows="4" cols="80"
                      placeholder="타인의 권리를 침해하거나 명예를 훼손하는 댓글은 운영원칙 및 관련 법률에 제재를 받을 수 있습니다."></textarea>
            <button class="btn btn-primary mb-4" onclick="comment_write()">댓글작성</button>
        </div>
        <div>
        </div>
    </div>
    <hr style="border: solid 2px">

    <div id="comment-list-area" class="mt-5">
        <c:choose>
        <c:when test="${commentList == null}">
            <h3 class="text-center">작성된 댓글이 없습니다.</h3>
        </c:when>
        <c:otherwise>
        <div id="comment-list">
            <c:forEach items="${commentList}" var="comment">
                <c:set var="asd" value="false"/>
                <div class="row">
                    <div class="col">${comment.commentWriter}</div>
                    <div class="col">${comment.commentContents}</div>
                    <div class="col">${comment.commentCreatedDate}</div>
                    <c:if test="${user == comment.commentWriter or comment.memberId ==null or user=='admin@admin.com'}">
                    <div class="col text-end">
                        <div class="col text-end"><i class="bi bi-x-square-fill" style='cursor: pointer;'
                                                     onclick="delete_fn(event, '${comment.id}', '${comment.memberId}')"></i>
                            <c:if test="${comment.memberId ==null}">
                                <input type="password" name="commentPass">
                            </c:if>
                        </div>
                        </c:if>
                    </div>
                    <div class="row">
                        <div class="col">
                            <c:set var="loop" value="false"/>
                            <c:forEach items="${heartDTOList}" var="heart" varStatus="status">
                                <c:if test="${heart.mid==userId and comment.id == heart.cid}">
                                    <i class="bi bi-hand-thumbs-up-fill" id="heart-${comment.id}"
                                       style="cursor: pointer;" onclick="heart_fn(event, ${comment.id})"></i>
                                    <c:set var="loop" value="true"/>
                                    <c:set var="asd" value="true"/>
                                </c:if>
                                <c:if test="${status.last and not asd}">
                                    <i class="bi bi-hand-thumbs-up" id="heart-${comment.id}" style="cursor: pointer;"
                                       onclick="heart_fn(event, ${comment.id})"></i>
                                    <c:set var="loop" value="true"/>
                                </c:if>
                            </c:forEach>
                            <c:set var="loopp" value="false"/>
                            <c:forEach items="${noDTOList}" var="no" varStatus="s">
                                <c:if test="${no.mid==userId and comment.id == no.cid}">
                                    <i class="bi bi-hand-thumbs-down-fill" id="no-${comment.id}" style="cursor: pointer;"
                                       onclick="no_fn(event, ${comment.id})"></i>
                                    <c:set var="loopp" value="true"/>
                                    <c:set var="asdd" value="true"/>
                                </c:if>
                                <c:if test="${s.last and not asdd}">
                                    <i class="bi bi-hand-thumbs-down" id="no-${comment.id}" style="cursor: pointer;"
                                       onclick="no_fn(event, ${comment.id})"></i>
                                    <c:set var="loopp" value="true"/>
                                </c:if>
                            </c:forEach>
                            <p id="cnt-${comment.id}">${comment.cnt}</p>
                        </div>
                    </div>
                    <hr class="mt-5" style="border: solid 2px">
                </div>
            </c:forEach>
            </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
<script>
    const user = '${user}';
    const userId = '${userId}'

    const heart_fn = (event, id) => {
        const cnt = document.getElementById("cnt-" + id);
        if (${user==null}) {
            alert("로그인해주세요.")
        } else if ($(`#heart-` + id).hasClass("bi-hand-thumbs-up")) {
            const cid = id;
            const mid = '${userId}';
            $.ajax({
                url: "/comment/insert",
                type: "get",
                data: {
                    cid,
                    mid
                },
                success: function (data) {
                    cnt.innerText = data;
                }
            })
        } else if ($(`#heart-` + id).hasClass("bi-hand-thumbs-up-fill")) {
            const cid = id;
            const mid = '${userId}';
            $.ajax({
                url: "/comment/drop",
                type: "get",
                data: {
                    cid,
                    mid
                },
                success: function (data) {
                    cnt.innerText = data;
                }
            })
        }
        if (${user!=null}) {
            if ($(`#no-` + id).hasClass("bi-hand-thumbs-down-fill")) {
                $(`#no-` + id).removeClass("bi-hand-thumbs-down-fill").addClass("bi-hand-thumbs-down");
            }
            $(event.target).toggleClass("bi-hand-thumbs-up bi-hand-thumbs-up-fill");
        }
    }

    const no_fn = (event, id) => {
        const cnt = document.getElementById("cnt-" + id);
        if (${user==null}) {
            alert("로그인해주세요.")
        } else if ($(`#heart-` + id).hasClass("bi-hand-thumbs-up-fill")) {
            const cid = id;
            const mid = '${userId}';
            $.ajax({
                url: "/comment/drop",
                type: "get",
                data: {
                    cid,
                    mid
                },
                success: function (data) {
                    cnt.innerText = data;
                }
            })
        }else if ($(`#no-` + id).hasClass("bi-hand-thumbs-down-fill")) {
            const cid = id;
            const mid = '${userId}';
            $.ajax({
                url: "/comment/ddrop",
                type: "get",
                data: {
                    cid,
                    mid
                },
                success: function (data) {
                    cnt.innerText = data;
                }
            })
        }
        if (${user!=null}) {
            if ($(`#heart-` + id).hasClass("bi-hand-thumbs-up-fill")) {
                $(`#heart-` + id).removeClass("bi-hand-thumbs-up-fill").addClass("bi-hand-thumbs-up");
            }
            $(event.target).toggleClass("bi-hand-thumbs-down bi-hand-thumbs-down-fill");
        }
    }


    $(document).ready(function () {
        $(frm.boardFile).on("change", function (e) {
            const files = e.target.files;
            const list = document.getElementById("board-list-area");
            list.innerHTML = "";
            for (let i = 0; i < files.length; i++) {
                const file = files[i];
                const img = document.createElement("img");
                img.src = URL.createObjectURL(file);
                img.classList.add("board-image");
                list.appendChild(img);
            }
            const imageCount = files.length;
            $.ajax({
                type: "get",
                url: "/board/count",
                data: {
                    length: imageCount
                },
                success: function (data) {
                    console.log("서버 응답 데이터:", data);
                }
            });
        });
    });


    const comment_write = () => {
        const commentWriter = document.getElementById("comment-writer").value;
        const commentContents = document.querySelector("#comment-contents").value;
        const boardId = '${board.id}';
        const memberId = '${userId}';
        const commentPassword = document.querySelector("#comment-password").value
        const result = document.getElementById("comment-list-area");
        if (commentWriter == "") {
            alert("작성자 입력바람!")
        } else if (commentPassword == "") {
            alert("비번입력바람!")
        } else if (commentContents == "") {
            alert("내용입력바람")
        } else {
            $.ajax({
                type: "post",
                url: "/comment/save",
                data: {
                    commentWriter,
                    commentContents,
                    boardId,
                    commentPassword,
                    memberId,
                },
                success: function (res) {
                    $.ajax({
                        type: "GET",
                        url: "/comment/findHeart",
                        success: function (data) {
                            let output = "<div id=\"comment-list\">\n"
                            for (let i in res) {
                                output += "    <div class=\"row\" >\n";
                                output += "        <div class=\"col\">" + res[i].commentWriter + "</div>\n";
                                output += "        <div class=\"col\">" + res[i].commentContents + "</div>\n";
                                output += "        <div class=\"col\">" + res[i].commentCreatedDate + "</div>\n";
                                if (res[i].memberId == null || user == res[i].commentWriter || user == 'admin@admin.com') {
                                    output += "        <div class=\"col text-end\">";
                                    output += "        <div class=\"col text-end\"><i class='bi bi-x-square-fill' style='cursor: pointer' onclick=\"delete_fn(event, '" + res[i].id + "', '" + res[i].memberId + "')\"></i>"+ "</div>\n";
                                    if (res[i].memberId == null) {
                                        output += "<input type='password' name='commentPass'></div>\n";
                                    }
                                }
                                output += "</div>\n";
                                output += "        <div class=\"row\">" + "<div class=\"col\">";
                                for(let j in data){
                                    if (data[j].mid == userId && res[i].id == data[j].cid) {
                                        output += "<i class='bi bi-hand-thumbs-up-fill' id='heart-" + res[i].id + "' style='cursor: pointer;' onclick='heart_fn(event, " + res[i].id + ")'></i>";
                                        break;
                                    } else if(j==data.length && data[j].mid != userId || res[i].id != data[j].cid) {
                                        output += "<i class='bi bi-hand-thumbs-up' id='heart-" + res[i].id + "' style='cursor: pointer;' onclick='heart_fn(event, " + res[i].id + ")'></i>";
                                        break;
                                    }
                                }
                                output += "<i class='bi bi-hand-thumbs-down' id='no-" + res[i].id + "' style='cursor: pointer;' onclick='no_fn(event, " + res[i].id + ")'></i>";
                                output += "<p id='cnt-" + res[i].id + "'>" + res[i].cnt + "</p>";
                                output += "    </div>\n";
                                output += "    </div>\n";
                                output += "        <hr class=\"mt-5\" style=\"border: solid 2px\">";
                                output += "    </div>\n";
                            }
                            output += "</div>";
                            result.innerHTML = output;
                            document.getElementById("comment-contents").value = "";
                        }
                    })
                },
                error: function () {
                    console.log("댓글 작성 실패");
                }
            });
        }
    }

    const delete_fn = (event, id, memberId) => {
        if (memberId == "" && ${user!="admin@admin.com"} || memberId == null && ${user!="admin@admin.com"}) {
            const commentPassword = $(event.target).parent().find('input[name="commentPass"]').val();
            if (commentPassword == "") {
                alert("비번입력바람!")
            } else {
                $.ajax({
                    type: "post",
                    data: {id, commentPassword},
                    url: "/comment/delete",
                    success: function () {
                        $("#modal-comment").modal("hide");
                        alert("삭제완료");
                        location.href = "/board?id=${board.id}&page=${paging.page}&query=${query}&key=${key}"
                    }, error: function () {
                        alert("비밀번호가 일치하지않습니다.");
                    }
                })
            }
        } else if (memberId == '${userId}' || ${user=="admin@admin.com"}) {
            $.ajax({
                type: "get",
                data: {id},
                url: "/comment/delete",
                success: function () {
                    alert("삭제완료");
                    location.href = "/board?id=${board.id}&page=${paging.page}&query=${query}&key=${key}"
                }
            })
        }
    }


    const board_list = () => {
        const page = '${page}';
        const query = '${query}';
        const key = '${key}';
        const by = '${by}';
        location.href = "/board/list?page=" + page + "&query=" + query + "&key=" + key + "&by=" + by;
    }
    const board_update = () => {
        if (user == '${board.boardWriter}') {
            if (confirm("수정하시겠습니까?")) {
                const id = '${board.id}'
                location.href = "/board/update?id=" + id;
            }
        } else {
            const id = '${board.boardId}';
            $.ajax({
                type: "get",
                url: "/member/findMember",
                data: {id},
                success: function (data) {
                    const memberEmail = data.memberEmail;
                    const memberPassword = data.memberPassword;
                    console.log(data)
                    location.href = "/member/findLogin?memberEmail=" + memberEmail + "&memberPassword=" + memberPassword + "&bid=${board.id}";
                }, error: function () {
                    const passArea = document.getElementById("pass-check");
                    const passDarea = document.getElementById("pass-dcheck");
                    passArea.style.display = "block";
                    passDarea.style.display = "none";
                }
            })
        }
    }
    const board_delete = () => {
        if (user == '${board.boardWriter}' || user == 'admin@admin.com') {
            if (confirm("삭제하시겠습니까?")) {
                const id = '${board.id}';
                location.href = "/board/delete?id=" + id
            }
        } else {
            const id = '${board.boardId}';
            $.ajax({
                type: "get",
                url: "/member/findMember",
                data: {id},
                success: function (data) {
                    const memberEmail = data.memberEmail;
                    const memberPassword = data.memberPassword;
                    console.log(data)
                    location.href = "/member/findLogin?memberEmail=" + memberEmail + "&memberPassword=" + memberPassword + "&bid=${board.id}";
                }, error: function () {
                    const passDarea = document.getElementById("pass-dcheck");
                    const passArea = document.getElementById("pass-check");
                    passDarea.style.display = "block";
                    passArea.style.display = "none";
                }
            })
        }
    }
    const pass_check = () => {
        const inputPass = document.getElementById("board-pass").value;
        const pass = '${board.boardPass}';
        const id = '${board.id}';
        if (inputPass == pass) {
            if (confirm("수정하시겠습니까?")) {
                location.href = "/board/update?id=" + id;
            }
        } else {
            alert("비밀번호 불일치!");
        }
    }

    const pass_dcheck = () => {
        const inputPass = document.getElementById("board-dpass").value;
        const pass = '${board.boardPass}';
        const id = '${board.id}';
        if (inputPass == pass) {
            if (confirm("삭제하시겠습니까?")) {
                location.href = "/board/delete?id=" + id;
            }
        } else {
            alert("비밀번호 불일치!");
        }
    }
</script>
</html>