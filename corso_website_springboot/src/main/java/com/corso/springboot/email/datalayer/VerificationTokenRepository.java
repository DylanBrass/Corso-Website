package com.corso.springboot.email.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    VerificationToken getVerificationTokenByTokenAndEmail(String token, String email);

    VerificationToken getVerificationTokenByUserId(String userId);

}
