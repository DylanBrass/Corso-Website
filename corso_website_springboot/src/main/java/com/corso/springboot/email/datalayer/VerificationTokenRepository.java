package com.corso.springboot.email.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    VerificationToken getVerificationTokenByTokenAndEmail(String token, String email);

    VerificationToken getVerificationTokenByUserId(String userId);

    List<VerificationToken> getAllByUserId(String userId);
}
