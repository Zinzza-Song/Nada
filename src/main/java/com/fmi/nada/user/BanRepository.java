package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BanRepository extends JpaRepository<Ban, Long> {

    Member findByBanEmail(String email);

}
