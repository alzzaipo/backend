package com.alzzaipo.hexagonal.notification.domain;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.common.util.UidGenerator;
import lombok.Getter;

@Getter
public class NotificationCriterion {

    private final Uid notificationCriterionUID;
    private final Uid memberUID;
    private int minCompetitionRate;
    private int minLockupRate;

    public NotificationCriterion(Uid notificationCriterionUID, Uid memberUID, int minCompetitionRate, int minLockupRate) {
        this.notificationCriterionUID = notificationCriterionUID;
        this.memberUID = memberUID;
        this.minCompetitionRate = minCompetitionRate;
        this.minLockupRate = minLockupRate;
    }

    public static NotificationCriterion create(Uid memberUID, int minCompetitionRate, int minLockupRate) {
        return new NotificationCriterion(
                UidGenerator.generate(),
                memberUID,
                minCompetitionRate,
                minLockupRate);
    }
}