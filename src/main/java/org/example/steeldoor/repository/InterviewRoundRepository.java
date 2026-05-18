package org.example.steeldoor.repository;

import jakarta.validation.constraints.NotNull;
import org.example.steeldoor.model.InterviewRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRoundRepository extends JpaRepository<InterviewRound, Integer> {

    List<InterviewRound> findAllBySubmissionIdOrderByOrderIndexAsc(@NotNull Integer submissionId);
}
