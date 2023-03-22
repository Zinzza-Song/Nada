package com.fmi.nada.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 차단 유저 목록 엔티티
 */
@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class BlockList {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "block_idx")
    private Long blockIdx;

    @Column(name = "member_idx")
    private Long memberIdx;

    @Column(name = "block_member_idx")
    private Long blockMemberIdx;

    @Column(name = "block_member_nickname")
    private String blockMemberNickname;

    @Column(name = "block_member_reason")
    private String blockMemberReason;

    @CreatedDate
    @Column(name = "block_member_date")
    private LocalDateTime blockMemberDate;

    public BlockList(
            Long memberIdx,
            Long blockMemberIdx,
            String blockMemberNickname,
            String blockMemberReason
    ) {
        this.memberIdx = memberIdx;
        this.blockMemberIdx = blockMemberIdx;
        this.blockMemberNickname = blockMemberNickname;
        this.blockMemberReason = blockMemberReason;
    }

}
