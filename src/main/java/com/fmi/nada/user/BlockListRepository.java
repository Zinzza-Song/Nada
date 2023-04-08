package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 블락유저 레포지토리
 */
@Transactional
public interface BlockListRepository extends JpaRepository<BlockList, Long> {

    List<BlockList> findBlockListByMemberIdx(Long memberIdx);

    BlockList findByBlockMemberIdxAndMemberIdx(Long blockMemberIdx, Long memberIDx);

    void deleteByMemberIdxAndBlockMemberIdx(Long memberIdx, Long blockMemberIdx);

}
