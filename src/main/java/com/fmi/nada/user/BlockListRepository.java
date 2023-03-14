package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockListRepository extends JpaRepository<BlockList,Long> {

    List<BlockList> findBlockListByMemberIdx(Long memberIdx);

}
