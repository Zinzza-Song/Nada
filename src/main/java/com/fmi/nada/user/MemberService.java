package com.fmi.nada.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 회원 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final FriendsRepository friendsRepository;

    public Member join(
            String username,
            String password,
            String memberName,
            String memberNickname,
            String memberBirth,
            String memberAddress,
            String memberPhone
    ) {
        return memberRepository.save(new Member(username,
                passwordEncoder.encode(password),
                "ROLE_USER",
                memberName,
                memberNickname,
                memberBirth,
                memberAddress,
                memberPhone));
    }

    /* 친구추가 */
    public Friends addFriends(Long memberIdx,
                              Long friendsMemberIdx) {
        return friendsRepository.save(new Friends(memberIdx,
                friendsMemberIdx));
    }
    public void delFriends(Long memberIdx,
                           Long friendsMemberIdx){
        friendsRepository.deleteFriendsByMemberIdxAndFriendsMemberIdx(
                memberIdx,
                friendsMemberIdx);
    }


    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);    }

    /* 친구리스트 */
    public List<Friends> friendsList(Long memberIdx){
        return memberRepository.findFriendsByMemberIdx(memberIdx);
    }



    /* 블록유저 리스트 */
    public List<BlockList> blockLists(Long memberIdx){
        return memberRepository.findBlockListByMemberIdx(memberIdx);
    }

    public List<Member> memberList() {
        return memberRepository.findAllByOrderByMemberJoinDateDesc();
    }

    public void delMember(Long memberIdx) {
        memberRepository.deleteMemberByMemberIdx(memberIdx);
    }

    public void updatePw(Member member) {
        memberRepository.save(member);
    }



}
