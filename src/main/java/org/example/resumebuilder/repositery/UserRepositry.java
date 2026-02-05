package org.example.resumebuilder.repositery;


import org.example.resumebuilder.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepositry extends MongoRepository<User, String> {
    Optional<User> findByemail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String verificationtoken);
}
