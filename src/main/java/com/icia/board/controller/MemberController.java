package com.icia.board.controller;

import com.icia.board.dto.*;
import com.icia.board.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id,@RequestParam("user") String user,HttpServletResponse response, HttpSession session){
        System.out.println("id = " + id + ", user = " + user + ", response = " + response + ", session = " + session);
        memberService.delete(id);
        if(!(user.equals("admin@admin.com"))) {
            session.removeAttribute("user");
            session.removeAttribute("userId");
            Cookie cookie = new Cookie("memberId", "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/admin")
    public String list(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(value = "query", required = false, defaultValue = "") String query,
                       @RequestParam(value = "key", required = false, defaultValue = "memberEmail") String key,
                       Model model){

        List<MemberDTO> memberDTOList =null;
        PageDTO pageDTO = null;

        if(query.equals("")){
            memberDTOList = memberService.paginglist(page);
            pageDTO = memberService.pageNumber(page);
            System.out.println(pageDTO.getMaxPage());
        }else{
            memberDTOList = memberService.search(key, query, page);
            pageDTO = memberService.searchPageNumber(key, query, page);
        }
        model.addAttribute("memberList", memberDTOList);
        model.addAttribute("paging", pageDTO);
        model.addAttribute("query", query);
        model.addAttribute("page", page);
        return "admin";
    }

    @GetMapping("/findLogin")
    public String findLogin(@ModelAttribute MemberDTO memberDto,@RequestParam("bid") Long bid , Model model){
        System.out.println("memberDto = " + memberDto + ", model = " + model);
        model.addAttribute("member", memberDto);
        model.addAttribute("bid", bid);
        return "findLogin";
    }

    @GetMapping("/save")
    public String save(){
        return "save";
    }

    @GetMapping("/findMember")
    public ResponseEntity findMember(@RequestParam("id") Long id){
        System.out.println("id = " + id);
        MemberDTO memberDTO = memberService.findMember(id);
        if(memberDTO!=null){
            return new ResponseEntity<>(memberDTO, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findemail")
    public ResponseEntity findEmail(@RequestParam("email") String memberEmail){
        MemberDTO memberDTO = memberService.findEmail(memberEmail);
        if(memberDTO!=null){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity findEmail(){
        List<MemberDTO> memberDTOList = memberService.findAll();
        if(memberDTOList!=null){
            return new ResponseEntity<>(memberDTOList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }


    @GetMapping("/read")
    public String read(@RequestParam("id") Long memberId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "q", required = false, defaultValue = "") String q,
            @RequestParam(value = "type", required = false, defaultValue = "boardTitle") String type,Model model) {
        MemberDTO memberDTO = memberService.findMember(memberId);
        model.addAttribute("u", memberDTO);
        if (memberDTO.getFileAttached() == 1) {
            List<MemberFileDTO> memberFileDTOList = memberService.findFile(memberId);
            model.addAttribute("memberFileList", memberFileDTOList);
        }
        model.addAttribute("q", q);
        model.addAttribute("type", type);
        model.addAttribute("page", page);
        return "read";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) throws IOException {
        memberService.save(memberDTO);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response){
        session.removeAttribute("user");
        session.removeAttribute("userId");
        Cookie cookie=new Cookie("memberId", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO) throws IOException {
        System.out.println("memberDTO = " + memberDTO);
        memberService.update(memberDTO);
        return "redirect:/";
    }

    @PostMapping("/login")
    public ResponseEntity login(@ModelAttribute MemberDTO memberDTO, @RequestParam("keep") boolean keep, HttpSession session, HttpServletResponse response){
        boolean login = memberService.login(memberDTO);
        if(login){
            Long id = memberService.findId(memberDTO);
            System.out.println(id);
            session.setAttribute("user", memberDTO.getMemberEmail());
            session.setAttribute("userId", id);
            if(keep){
                Cookie cookie=new Cookie("memberEmail", memberDTO.getMemberEmail());
                cookie.setMaxAge(60*60*24*7);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            return new ResponseEntity<>(memberDTO, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
