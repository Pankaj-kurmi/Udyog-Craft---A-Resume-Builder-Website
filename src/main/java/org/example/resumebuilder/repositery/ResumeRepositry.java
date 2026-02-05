package org.example.resumebuilder.repositery;

import org.example.resumebuilder.document.Resume;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepositry extends MongoRepository<Resume ,String> {

    List<Resume> findByUserIdOrderByUpdatedAtDesc(String userId);

    Optional<Resume> findByUserIdAndId(String userId, String Id);



}
