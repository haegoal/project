package com.icia.board.service;

import com.icia.board.dto.BoardDTO;
import com.icia.board.dto.BoardFileDTO;
import com.icia.board.dto.PageDTO;
import com.icia.board.repository.BoardRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    public List<BoardDTO> paginglist(int page, int pageLimit, String by) {
        int pageingStart = (page - 1) * pageLimit;
        Map<String, Object> pagingMap = new HashMap<>();
        pagingMap.put("start", pageingStart);
        pagingMap.put("limit", pageLimit);
        pagingMap.put("by", by);
        System.out.println(by);
        return boardRepository.paginglist(pagingMap);
    }

    public PageDTO pageNumber(int page, int pageLimit) {
        int blockLimit = 3; // 하단에 보여줄 페이지 번호 갯수
        // 전체 글 갯수 조회
        int boardCount = boardRepository.boardCount();
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

    public PageDTO searchPageNumber(String key, String query, int page, int pageLimit) {
        int blockLimit = 3; // 하단에 보여줄 페이지 번호 갯수
        Map<String, String> pagingParams = new HashMap<>();
        pagingParams.put("q", query);
        pagingParams.put("k", key);
        // 전체 글 갯수 조회
        int boardCount = boardRepository.boardSearchCount(pagingParams);
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

    public List<BoardDTO> search(String key, String query, int page, int pageLimit, String by) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("k", key);
        map.put("q",query);
        map.put("by", by);
        int pageingStart = (page -1) * pageLimit;
        map.put("start", pageingStart);
        map.put("limit", pageLimit);
        return boardRepository.search(map);
    }

    public void save(BoardDTO boardDTO) throws IOException, IOException {
        if (boardDTO.getBoardFile().get(0).isEmpty()) {
            boardDTO.setFileAttached(0);
            boardRepository.save(boardDTO);
        } else {
            //파일있다
            boardDTO.setFileAttached(1);
            //게시글 저장후 id값활용을 위해 리턴받음.
            BoardDTO saveBoard = boardRepository.save(boardDTO);
            //파일이 리스트형태니 반복문으로 하나씩 꺼내서 저장처리
            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                System.out.println(boardFile);
                //파일 이름가져오기
                String originalFilename = boardFile.getOriginalFilename();
                //저장용 이름만들기
                String storedFileName = System.currentTimeMillis() + "-" + originalFilename;
                BoardFileDTO boardFileDTO = new BoardFileDTO();
                boardFileDTO.setOriginalFileName(originalFilename);
                boardFileDTO.setStoredFileName(storedFileName);
                boardFileDTO.setBoardId(saveBoard.getId());
                //파일 저장용 폴더에 파일 저장 처리
                String savePath = "C:\\spring_img\\" + storedFileName;
                boardFile.transferTo(new File(savePath)); //실제 파일옴겨주는 메소드
                boardRepository.saveFile(boardFileDTO);
            }
        }
    }

    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public BoardDTO findById(Long id) {
        return boardRepository.findById(id);
    }

    public List<BoardFileDTO> findFile(Long id) {
        return boardRepository.findFile(id);
    }

    public void delete(Long id) {
        boardRepository.delete(id);
    }

    public void update(BoardDTO boardDTO) throws IOException {
        if (boardDTO.getBoardFile().get(0).isEmpty()) {
            boardDTO.setFileAttached(0);
            boardRepository.update(boardDTO);
        } else {
            //파일있다
            boardDTO.setFileAttached(1);
            List<BoardFileDTO> boardFileDTOList = boardRepository.findFile(boardDTO.getId());
            for(BoardFileDTO boardFileDTO : boardFileDTOList){
                boardRepository.deleteFile(boardFileDTO.getId());
            }
            BoardDTO saveBoard = boardRepository.update(boardDTO);
            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                //파일 이름가져오기
                String originalFilename = boardFile.getOriginalFilename();
                //저장용 이름만들기
                String storedFileName = System.currentTimeMillis() + "-" + originalFilename;
                BoardFileDTO boardFileDTO = new BoardFileDTO();
                boardFileDTO.setOriginalFileName(originalFilename);
                boardFileDTO.setStoredFileName(storedFileName);
                boardFileDTO.setBoardId(saveBoard.getId());
                //파일 저장용 폴더에 파일 저장 처리
                String savePath = "C:\\spring_img\\" + storedFileName;
                boardFile.transferTo(new File(savePath)); //실제 파일옴겨주는 메소드
                boardRepository.saveFile(boardFileDTO);
            }
        }
    }
}
