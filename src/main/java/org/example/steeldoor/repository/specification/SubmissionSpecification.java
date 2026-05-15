package org.example.steeldoor.repository.specification;

import org.example.steeldoor.model.Company;
import org.example.steeldoor.model.Submission;
import org.springframework.data.jpa.domain.Specification;

public class SubmissionSpecification {

    public static Specification<Submission> hasCompany(Company company) {
        return (root, query, cb) ->
                cb.equal(root.get("company"), company);
    }

    public static Specification<Submission> offerReceived(boolean offerReceived) {
        return (root, query, cb) ->
                cb.equal(root.get("offerReceived"), offerReceived);
    }

    public static Specification<Submission> positionContains(String position) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("position")),
                        "%" + position.toLowerCase() + "%"
                );
    }
}
