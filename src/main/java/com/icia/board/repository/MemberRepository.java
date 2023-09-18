package com.icia.board.repository;

import com.icia.board.dto.BoardDTO;
import com.icia.board.dto.MemberDTO;
import com.icia.board.dto.MemberFileDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberRepository {
    @Autowired
    private SqlSessionTemplate sql;

    public List<MemberDTO> paginglist(Map<String, Integer> pagingMap) {
        return sql.selectList("Member.paging", pagingMap);
    }

    public int boardCount() {
        return sql.selectOne("Member.count");
    }

    public int boardSearchCount(Map<String, String> pagingParams) {
        return sql.selectOne("Member.searchCount", pagingParams);
    }

    public List<MemberDTO> search(HashMap<String, Object> map) {
        return sql.selectList("Member.search", map);
    }

    public MemberDTO login(MemberDTO memberDTO) {
        return sql.selectOne("Member.login", memberDTO);
    }

    public MemberDTO save(MemberDTO memberDTO) {
        sql.insert("Member.save", memberDTO);
        return memberDTO;
    }


    public void saveFile(MemberFileDTO memberFileDTO) {
        sql.insert("Member.saveFile", memberFileDTO);
    }

    public MemberDTO findEmail(String memberEmail) {
        return sql.selectOne("Member.findEmail", memberEmail);
    }

    public Long findId(MemberDTO memberDTO) {
        return sql.selectOne("Member.findId", memberDTO);
    }

    public MemberDTO findMember(Long id) {
        return sql.selectOne("Member.findMember", id);
    }

    public List<MemberDTO> findAll() {
        return sql.selectList("Member.findAll");
    }

    public List<MemberFileDTO> findFile(Long memberId) {
        return sql.selectList("Member.findFile", memberId);
    }

    public MemberDTO update(MemberDTO memberDTO) {
        sql.update("Member.update", memberDTO);
        return memberDTO;
    }

    public void delete(Long id) {
        sql.delete("Member.delete", id);
    }
}
