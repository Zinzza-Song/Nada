package com.fmi.nada.admin;

import com.fmi.nada.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public List<Log> findAllByOrderByLogDateDesc() {
        return logRepository.findAllByOrderByLogDateDesc();
    }

    public List<Log> findAllByLogMemberEmailContainingOrderByLogDateDesc(String keyword) {
        return logRepository.findAllByLogMemberEmailContainingOrderByLogDateDesc(keyword);
    }

    public List<Log> findAllByLogUsedServiceContainingOrderByLogDateDesc(String keyword) {
        return logRepository.findAllByLogUsedServiceContainingOrderByLogDateDesc(keyword);
    }

    public void logLogin(Member member) {
        System.out.println("로그인 로그 남기기 돌입");
        logRepository.save(new Log(
                member.getMemberIdx(),
                true,
                member.getUsername(),
                "로그인"
        ));
    }

    public void logLogout(Member member) {
        logRepository.save(new Log(
                member.getMemberIdx(),
                true,
                member.getUsername(),
                "로그아웃"
        ));
    }

    public String countLogsDate(String logData, String logUsedService) {
        return logRepository.countLogsDate(logData, logUsedService);
    }

    public String countDiaryLog(String logData, String maxLogDate, String logUsedService) {
        return logRepository.countDiaryLog(logData, maxLogDate, logUsedService);
    }
}
