package com.icia.board.service;

import com.icia.board.dto.*;
import com.icia.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public boolean login(MemberDTO memberDTO) {
        memberDTO= memberRepository.login(memberDTO);
        if(memberDTO!=null){
            return true;
        }else{
            return false;
        }
    }
    public void save(MemberDTO memberDTO) throws IOException {
        if(memberDTO.getMemberProfile()==null){
            memberDTO.setFileAttached(0);
            memberRepository.save(memberDTO);
        }else {
            memberDTO.setFileAttached(1);
            MemberDTO saveMember = memberRepository.save(memberDTO);
            for (MultipartFile memberProfile : memberDTO.getMemberProfile()) {
                String originalFilename = memberProfile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "-" + originalFilename;
                MemberFileDTO memberFileDTO = new MemberFileDTO();
                memberFileDTO.setOriginalFileName(originalFilename);
                memberFileDTO.setStoredFileName(storedFileName);
                memberFileDTO.setMemberId(saveMember.getId());
                System.out.println(memberFileDTO);
                String savePath = "C:\\spring_img\\" + storedFileName;
                memberProfile.transferTo(new File(savePath)); //실제 파일옴겨주는 메소드
                memberRepository.saveFile(memberFileDTO);
            }
        }
    }

    public MemberDTO findEmail(String memberEmail) {
        return memberRepository.findEmail(memberEmail);
    }

    public Long findId(MemberDTO memberDTO) {
        return memberRepository.findId(memberDTO);
    }

    public MemberDTO findMember(Long id) {
        return memberRepository.findMember(id);
    }

    public List<MemberDTO> findAll() {
        return memberRepository.findAll();
    }

    public List<MemberFileDTO> findFile(Long memberId) {
        return memberRepository.findFile(memberId);
    }

    public void update(MemberDTO memberDTO) throws IOException {
        if(memberDTO.getMemberProfile()==null){
            memberDTO.setFileAttached(0);
            memberRepository.update(memberDTO);
        }else {
            memberDTO.setFileAttached(1);
            MemberDTO saveMember = memberRepository.update(memberDTO);
            for (MultipartFile memberProfile : memberDTO.getMemberProfile()) {
                String originalFilename = memberProfile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "-" + originalFilename;
                MemberFileDTO memberFileDTO = new MemberFileDTO();
                memberFileDTO.setOriginalFileName(originalFilename);
                memberFileDTO.setStoredFileName(storedFileName);
                memberFileDTO.setMemberId(saveMember.getId());
                System.out.println(memberFileDTO);
                String savePath = "C:\\spring_img\\" + storedFileName;
                memberProfile.transferTo(new File(savePath)); //실제 파일옴겨주는 메소드
                memberRepository.saveFile(memberFileDTO);
            }
        }
    }

    public List<MemberDTO> paginglist(int page) {
        int pageLimit = 3;
        int pageingStart = (page - 1) * pageLimit;
        Map<String, Integer> pagingMap = new HashMap<>();
        pagingMap.put("start", pageingStart);
        pagingMap.put("limit", pageLimit);
        return memberRepository.paginglist(pagingMap);
    }

    public PageDTO pageNumber(int page) {
        int pageLimit = 3; // 한페이지에 보여줄 글 갯수
        int blockLimit = 3; // 하단에 보여줄 페이지 번호 갯수
        // 전체 글 갯수 조회
        int boardCount = memberRepository.boardCount();
        // 전체 페이지 갯수 계산
        int maxPage = (int) (Math.ceil((double) boardCount / pageLimit));
        // 시작 페이지 값 계산(1, 4, 7, 10 ~~)
        int startPage = (((int) (Math.ceil((double) page / blockLimit))) - 1) * blockLimit + 1;
        // 마지막 페이지 값 계산(3, 6, 9, 12 ~~)
        int endPage = startPage + blockLimit - 1;
        // 전체 페이지 갯수가 계산한 endPage 보다 작을 때는 endPage 값을 maxPage 값과 같게 세팅
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setMaxPage(maxPage);
        pageDTO.setEndPage(endPage);
        pageDTO.setStartPage(startPage);
        return pageDTO;
    }

    public PageDTO searchPageNumber(String key, String query, int page) {
        int pageLimit = 3; // 한페이지에 보여줄 글 갯수
        int blockLimit = 3; // 하단에 보여줄 페이지 번호 갯수
        Map<String, String> pagingParams = new HashMap<>();
        pagingParams.put("q", query);
        pagingParams.put("k", key);
        // 전체 글 갯수 조회
        int boardCount = memberRepository.boardSearchCount(pagingParams);
        // 전체 페이지 갯수 계산
        int maxPage = (int) (Math.ceil((double) boardCount / pageLimit));
        // 시작 페이지 값 계산(1, 4, 7, 10 ~~)
        int startPage = (((int) (Math.ceil((double) page / blockLimit))) - 1) * blockLimit + 1;
        // 마지막 페이지 값 계산(3, 6, 9, 12 ~~)
        int endPage = startPage + blockLimit - 1;
        // 전체 페이지 갯수가 계산한 endPage 보다 작을 때는 endPage 값을 maxPage 값과 같게 세팅
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setMaxPage(maxPage);
        pageDTO.setEndPage(endPage);
        pageDTO.setStartPage(startPage);
        return pageDTO;
    }

    public List<MemberDTO> search(String key, String query, int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("k", key);
        map.put("q",query);
        int pageLimit = 3;
        int pageingStart = (page -1) * pageLimit;
        map.put("start", pageingStart);
        map.put("limit", pageLimit);
        return memberRepository.search(map);
    }

    public void delete(Long id) {
        memberRepository.delete(id);
    }
}
