package com.icia.board.service;

import com.icia.board.dto.CommentDTO;
import com.icia.board.dto.HeartDTO;
import com.icia.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public void save(CommentDTO commentDTO) {
        commentRepository.save(commentDTO);
    }

    public List<CommentDTO> findAll(Long boardId) {
        return commentRepository.findAll(boardId);
    }

    public void delete(Long id) {
        commentRepository.delete(id);
    }

    public CommentDTO find(CommentDTO commentDTO) {
        return commentRepository.find(commentDTO);
    }

    public List<HeartDTO> findHeart() {
        return commentRepository.findHeart();
    }

    public void insert(HeartDTO heartDTO) {
        commentRepository.insert(heartDTO);
    }

    public void update(Long cid) {
        commentRepository.update(cid);
    }


    public void drop(HeartDTO heartDTO) {
        commentRepository.drop(heartDTO);
    }

    public void updated(Long cid) {
        commentRepository.updated(cid);
    }

    public int select(Long cid) {
        return commentRepository.select(cid);
    }
}