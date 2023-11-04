package com.alzzaipo.hexagonal.member.adapter.out.persistence.LocalAccount;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.Member.MemberJpaEntity;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.Member.NewMemberRepository;
import com.alzzaipo.hexagonal.member.application.port.out.*;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class LocalAccountPersistenceAdapter implements
        FindLocalAccountByAccountIdPort,
        FindLocalAccountByEmailPort,
        RegisterLocalAccountPort,
        FindLocalAccountByMemberUidPort,
        ChangeLocalAccountPasswordPort {

    private final EntityManager entityManager;

    private final NewLocalAccountRepository localAccountRepository;
    private final NewMemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<SecureLocalAccount> findLocalAccountByAccountId(LocalAccountId localAccountId) {
        return localAccountRepository.findByAccountId(localAccountId.get())
                .map(this::toSecureLocalAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SecureLocalAccount> findLocalAccountByEmailPort(Email email) {
        return localAccountRepository.findByEmail(email.get())
                .map(this::toSecureLocalAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SecureLocalAccount> findLocalAccountByMemberUid(Uid memberUID) {
        return localAccountRepository.findByMemberUID(memberUID.get())
                .map(this::toSecureLocalAccount);
    }

    @Override
    public void registerLocalAccountPort(SecureLocalAccount secureLocalAccount) {
        MemberJpaEntity memberJpaEntity = memberRepository.findByUid(secureLocalAccount.getMemberUID().get())
                .orElseThrow(() -> new IllegalArgumentException("회원 엔티티 조회 실패"));

        LocalAccountJpaEntity localAccountJpaEntity = toJpaEntity(memberJpaEntity, secureLocalAccount);

        localAccountRepository.save(localAccountJpaEntity);
    }

    @Override
    public boolean changeLocalAccountPassword(LocalAccountId accountId, String encryptedNewAccountPassword) {
        Optional<LocalAccountJpaEntity> optionalLocalAccountJpaEntity
                = localAccountRepository.findByAccountId(accountId.get());

        optionalLocalAccountJpaEntity.ifPresent(entity -> {
            entity.changePassword(encryptedNewAccountPassword);
            entityManager.flush();
        });

        return optionalLocalAccountJpaEntity.isPresent();
    }

    private SecureLocalAccount toSecureLocalAccount(LocalAccountJpaEntity jpaEntity) {
        Uid memberUID = new Uid(jpaEntity.getMemberJpaEntity().getUid());
        LocalAccountId localAccountId = new LocalAccountId(jpaEntity.getAccountId());
        String encryptedLocalAccountPassword = jpaEntity.getAccountPassword();
        Email email = new Email(jpaEntity.getEmail());

        return new SecureLocalAccount(
                memberUID,
                localAccountId,
                encryptedLocalAccountPassword,
                email);
    }

    private LocalAccountJpaEntity toJpaEntity(MemberJpaEntity memberJpaEntity, SecureLocalAccount secureLocalAccount) {
        String accountId = secureLocalAccount.getAccountId().get();
        String encryptedAccountPassword = secureLocalAccount.getEncryptedAccountPassword();
        String email = secureLocalAccount.getEmail().get();

        return new LocalAccountJpaEntity(
                accountId,
                encryptedAccountPassword,
                email,
                memberJpaEntity);
    }
}
