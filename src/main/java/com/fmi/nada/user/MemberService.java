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
    private final BanRepository banRepository;

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
        return memberRepository.findByUsername(username);
    }

    public Member findByMemberIdx(Long memberIdx) {
        return memberRepository.findByMemberIdx(memberIdx);
    }

    public Member findByMemberNickname(String memberNickname) {
        return memberRepository.findByMemberNickname(memberNickname);
    }

    public List<Member> memberList() {
        return memberRepository.findByAuthorityOrderByMemberJoinDateDesc("ROLE_USER");
    }

    public List<Member> memberSearchNicknameList(String keyword) {
        return memberRepository.findAllByMemberNicknameContainingOrderByMemberJoinDateDesc(keyword);
    }

    public List<Member> memberSearchNameList(String keyword) {
        return memberRepository.findAllByMemberNameContainingOrderByMemberJoinDateDesc(keyword);
    }

    public List<Member> memberSearchBirthList(String keyword) {
        return memberRepository.findAllByMemberBirthContainingOrderByMemberJoinDateDesc(keyword);
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

    public Sympathy getLikeIdx(Long memberIdx) {
        Sympathy likeIdx = sympathyRepository.findByMemberIdx(memberIdx);
        if (likeIdx == null) {
            return null;
        } else {
            return likeIdx;
        }
    }

    /* 친구추가 */
    public Friends addFriends(Member member,
                              Member friendMember) {
        return friendsRepository.save(new Friends(
                member.getMemberIdx(),
                friendMember.getMemberIdx(),
                friendMember.getMemberNickname()));
    }

    public List<Friends> findFriendsByMemberIdxAndFriendsMemberIdx(Member member, Member friendMember) {
        return friendsRepository.findByMemberIdxAndFriendsMemberIdx(
                member.getMemberIdx(),
                friendMember.getMemberIdx());
    }

    public void delFriends(Long memberIdx,
                           Long friendsMemberIdx) {
        friendsRepository.deleteFriendsByMemberIdxAndFriendsMemberIdx(
                memberIdx,
                friendsMemberIdx);
    }

    /* 친구리스트 */
    public List<Friends> friendsList(Long memberIdx) {
        return friendsRepository.findFriendsByMemberIdx(memberIdx);
    }

    public BlockList findByBlockMemberIdxAndMemberIdx(Long blockMemberIdx, Long memberIDx) {
        return blockListRepository.findByBlockMemberIdxAndMemberIdx(blockMemberIdx, memberIDx);
    }

    /* 블록유저 리스트 */
    public List<BlockList> blockLists(Long memberIdx) {
        return blockListRepository.findBlockListByMemberIdx(memberIdx);
    }

    public BlockList addBlockList(Member member, BlockListDto blockListDto) {
        return blockListRepository.save(new BlockList(
                member.getMemberIdx(),
                blockListDto.getBlockMemberIdx(),
                blockListDto.getBlockMemberNickname(),
                blockListDto.getBlockMemberReason()
        ));
    }

    public void delBlocks(Long memberIdx,
                          Long blockMemberIdx) {
        blockListRepository.deleteByMemberIdxAndBlockMemberIdx(
                memberIdx,
                blockMemberIdx);
    }

    public Ban findByBanEmail(String email) {
        return banRepository.findByBanEmail(email);
    }

    public void deleteByAdmin(Ban ban) {
        banRepository.save(ban);
    }

}
