package com.alzzaipo.hexagonal.notification.adapter.in.web;

import com.alzzaipo.hexagonal.common.MemberPrincipal;
import com.alzzaipo.hexagonal.notification.adapter.in.web.dto.RegisterNotificationCriterionWebRequest;
import com.alzzaipo.hexagonal.notification.application.port.dto.NotificationCriterionView;
import com.alzzaipo.hexagonal.notification.application.port.dto.RegisterNotificationCriterionCommand;
import com.alzzaipo.hexagonal.notification.application.port.in.FindMemberNotificationCriteriaQuery;
import com.alzzaipo.hexagonal.notification.application.port.in.RegisterNotificationCriterionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NewNotificationController {

    private final RegisterNotificationCriterionUseCase registerNotificationCriterionUseCase;
    private final FindMemberNotificationCriteriaQuery findMemberNotificationCriteriaQuery;

    @PostMapping("/criteria/add")
    public ResponseEntity<String> add(@AuthenticationPrincipal MemberPrincipal principal,
                                      @RequestBody RegisterNotificationCriterionWebRequest dto) {
        RegisterNotificationCriterionCommand command = new RegisterNotificationCriterionCommand(
                principal.getMemberUID(),
                dto.getCompetitionRate(),
                dto.getLockupRate());

        registerNotificationCriterionUseCase.registerNotificationCriterion(command);

        return ResponseEntity.ok().body("알림 기준 추가 완료");
    }

    @GetMapping("/criteria/list")
    public ResponseEntity<List<NotificationCriterionView>> getNotificationCriteriaList(
            @AuthenticationPrincipal MemberPrincipal principal) {

        List<NotificationCriterionView> memberNotificationCriteria
                = findMemberNotificationCriteriaQuery.findMemberNotificationCriteria(principal.getMemberUID());

        return ResponseEntity.ok().body(memberNotificationCriteria);
    }
}