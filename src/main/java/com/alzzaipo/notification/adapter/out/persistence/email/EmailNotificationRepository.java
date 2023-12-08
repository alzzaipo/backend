package com.alzzaipo.notification.adapter.out.persistence.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotificationJpaEntity, Long> {

    @Query("SELECT e FROM EmailNotificationJpaEntity e WHERE e.memberJpaEntity.uid = :memberUID")
    Optional<EmailNotificationJpaEntity> findByMemberUID(@Param("memberUID") Long memberUID);
}
