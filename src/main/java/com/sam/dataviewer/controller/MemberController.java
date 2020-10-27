package com.sam.dataviewer.controller;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/member/new")
    public String createForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "member/createMemberForm";
    }

    @PostMapping("/member/new")
    public String createMember(@Valid MemberDto memberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/createMemberForm";
        }
        try {
            memberService.join(memberDto);
        } catch (IllegalStateException e) {
            bindingResult.rejectValue("username", "duplication", "이미 존재하는 아이디입니다.");
            return "member/createMemberForm";
        }
        return "redirect:/login";
    }

    @GetMapping("/member/memberDetail")
    public String memberDetail(Principal principal, Model model) {
        MemberDto memberDto = memberService.findOne(principal.getName());
        model.addAttribute("memberDto", memberDto);
        return "member/memberDetail";
    }

    @PostMapping("/member/update")
    public String updateMember(@Valid MemberDto memberDto) {
        memberService.updateMember(memberDto);
        return "redirect:/member/memberDetail";
    }
}
