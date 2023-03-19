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
    private final BlockListRepository blockListRepository;
    private final FriendsRepository friendsRepository;
    private final SympathyRepository sympathyRepository;

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

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).get();
    }

    public Member findByMemberIdx(Long memberIdx) {
        return memberRepository.findByMemberIdx(memberIdx);
    }

    public List<Member> memberList() {
        return memberRepository.findAllByOrderByMemberJoinDateDesc();
    }

    public void delMember(Long memberIdx) {
        memberRepository.deleteById(memberIdx);
    }

    public void updatePw(Member member) {
        memberRepository.save(member);
    }

    public void updateMember(Member member) {
        memberRepository.save(member);
    }

    /* 친구추가 */
    public Friends addFriends(Long memberIdx,
                              Long friendsMemberIdx) {
        return friendsRepository.save(new Friends(memberIdx,
                friendsMemberIdx));
    }

    public void delFriends(Long memberIdx,
                           Long friendsMemberIdx) {
        friendsRepository.deleteFriendsByMemberIdxAndFriendsMemberIdx(
                memberIdx,
                friendsMemberIdx);
    }

    public Sympathy getLikeIdx(Long memberIdx) {
        Sympathy likeIdx = sympathyRepository.findByMemberIdx(memberIdx);
        if (likeIdx == null) {
            return null;
        } else {
            return likeIdx;
        }
    }

    /* 친구리스트 */
    public List<Friends> friendsList(Long memberIdx) {
        return friendsRepository.findFriendsByMemberIdx(memberIdx);
    }

    /* 블록유저 리스트 */
    public List<BlockList> blockLists(Long memberIdx) {
        return blockListRepository.findBlockListByMemberIdx(memberIdx);
    }

}
