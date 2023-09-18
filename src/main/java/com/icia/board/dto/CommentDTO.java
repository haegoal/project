package com.icia.board.dto;

import lombok.Data;

@Data
public class CommentDTO{
    private Long id;
    private String commentWriter;
    private String commentContents;
    private String commentCreatedDate;
    private Long boardId;
    private String commentPassword;
    private Long memberId;
}
