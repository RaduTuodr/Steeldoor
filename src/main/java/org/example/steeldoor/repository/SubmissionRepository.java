package org.example.steeldoor.repository;

import jakarta.validation.constraints.NotNull;
import org.example.steeldoor.model.Submission;
import org.example.steeldoor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer>, JpaSpecificationExecutor<Submission> {
    List<Submission> findAllByUser(@NotNull User user);
}
