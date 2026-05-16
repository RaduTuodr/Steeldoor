package org.example.steeldoor.repository;

import org.example.steeldoor.model.Vote;
import org.example.steeldoor.model.keys.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.user.id = :userId AND v.submission.id = :submissionId")
    void deleteByUserIdAndSubmissionId(@Param("userId") Integer userId, @Param("submissionId") Integer submissionId);

    @Query("SELECT v FROM Vote v WHERE v.id.userId = :userId AND v.id.submissionId = :submissionId")
    Optional<Vote> findByUserIdAndSubmissionId(@Param("userId") Integer userId, @Param("submissionId") Integer submissionId);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.submission.id = :submissionId")
    Integer countBySubmissionId(@Param("submissionId") Integer submissionId);
}
