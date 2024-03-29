package com.alzzaipo.notification.adapter.out.persistence.criterion;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberRepository;
import com.alzzaipo.notification.application.port.dto.UpdateNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.out.criterion.CheckNotificationCriterionOwnershipPort;
import com.alzzaipo.notification.application.port.out.criterion.CountMemberNotificationCriteriaPort;
import com.alzzaipo.notification.application.port.out.criterion.DeleteNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.criterion.FindAllNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.criterion.FindMemberNotificationCriteriaPort;
import com.alzzaipo.notification.application.port.out.criterion.RegisterNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.criterion.UpdateNotificationCriterionPort;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
@RequiredArgsConstructor
public class NotificationCriterionPersistenceAdapter implements RegisterNotificationCriterionPort,
	FindMemberNotificationCriteriaPort,
	UpdateNotificationCriterionPort,
	DeleteNotificationCriterionPort,
	FindAllNotificationCriterionPort,
	CountMemberNotificationCriteriaPort,
	CheckNotificationCriterionOwnershipPort {

	private final NotificationCriterionRepository notificationCriterionRepository;
	private final MemberRepository memberRepository;

	@Override
	public void registerNotificationCriterion(NotificationCriterion notificationCriterion) {
		MemberJpaEntity memberJpaEntity
			= memberRepository.findEntityById(notificationCriterion.getMemberId().get());

		NotificationCriterionJpaEntity entity = NotificationCriterionJpaEntity.build(
			notificationCriterion,
			memberJpaEntity);

		notificationCriterionRepository.save(entity);
	}

	@Override
	public List<NotificationCriterion> findMemberNotificationCriteria(Id memberId) {
		return notificationCriterionRepository.findByMemberJpaEntityId(memberId.get())
			.stream()
			.map(NotificationCriterionJpaEntity::toDomainEntity)
			.collect(Collectors.toList());
	}

	@Override
	public void updateNotificationCriterion(UpdateNotificationCriterionCommand command) {
		Long entityId = command.getNotificationCriterionId().get();

		NotificationCriterionJpaEntity entity = notificationCriterionRepository.findById(entityId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "알림 기준 조회 실패"));

		entity.changeMinCompetitionRate(command.getMinCompetitionRate());
		entity.changeMinLockupRate(command.getMinLockupRate());
	}

	@Override
	public void deleteNotificationCriterion(Id notificationCriterionId) {
		Long entityId = notificationCriterionId.get();

		NotificationCriterionJpaEntity entity = notificationCriterionRepository.findById(entityId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "알림 기준 조회 실패"));

		notificationCriterionRepository.delete(entity);
	}

	@Override
	public List<NotificationCriterion> findAllNotificationCriterion() {
		return notificationCriterionRepository.findAll()
			.stream()
			.map(NotificationCriterionJpaEntity::toDomainEntity)
			.collect(Collectors.toList());
	}

	@Override
	public int count(Id memberId) {
		return notificationCriterionRepository.countByMemberJpaEntityId(memberId.get());
	}

	@Override
	public boolean checkOwnership(Id memberId, Id notificationCriterionId) {
		return notificationCriterionRepository.checkOwnership(memberId.get(), notificationCriterionId.get());
	}
}
