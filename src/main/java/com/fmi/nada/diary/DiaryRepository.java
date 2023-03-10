package com.fmi.nada.diary;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * 다이어리 레포지토리
 * */
public interface DiaryRepository extends JpaRepository<Diary, Long> {


}
