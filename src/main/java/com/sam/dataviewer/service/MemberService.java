package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.form.MemberForm;
import com.sam.dataviewer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
    * 회원 가입
    */
    @Transactional
    public Long join(MemberForm memberForm) {
        Member.createMember();
        validateUsername(member);
        memberRepository.save(member);
        return member.getId();
    }

    /*
    * 아이디 중복 검증
    */
    private void validateUsername(Member member) {
        Member existingMember = memberRepository.findByUsername(member.getUsername());
        if (existingMember != null) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    /*
    * 회원 전체 조회
    */
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    /*
    * 회원 한 명 조회
    */
    public Member findOne(Long memberId){
        return memberRepository.getOne(memberId);
    }
}
