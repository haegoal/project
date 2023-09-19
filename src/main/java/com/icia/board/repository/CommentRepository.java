package com.icia.board.repository;

import com.icia.board.dto.CommentDTO;
import com.icia.board.dto.HeartDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {
    @Autowired
    private SqlSessionTemplate sql;

    public void save(CommentDTO commentDTO) {
        sql.insert("Comment.save", commentDTO);
    }

    public List<CommentDTO> findAll(Long boardId) {
        return sql.selectList("Comment.findAll", boardId);
    }


    public void delete(Long id) {
        sql.delete("Comment.delete", id);
    }

    public CommentDTO find(CommentDTO commentDTO) {
        return sql.selectOne("Comment.find", commentDTO);
    }

    public List<HeartDTO> findHeart() {
        return sql.selectList("Comment.findHeart");
    }

    public void insert(HeartDTO heartDTO) {
        sql.insert("Comment.insert", heartDTO);
    }

    public void update(Long cid) {
        sql.update("Comment.update", cid);
    }

    public void drop(HeartDTO heartDTO) {
        sql.delete("Comment.drop", heartDTO);
    }

    public void updated(Long cid) {
        sql.update("Comment.updated", cid);
    }

    public int select(Long id) {
        return sql.selectOne("Comment.select", id);
    }
}