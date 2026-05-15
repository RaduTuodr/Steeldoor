package org.example.steeldoor.utils.strategy;

import org.example.steeldoor.model.Submission;
import org.example.steeldoor.utils.SortStrategy;

import java.util.List;

public class SortByVotes implements SortStrategy<Submission> {
    @Override
    public void sort(List<Submission> items) {
        // TODO: implement sort by no. votes + totalVotesCount in submission
    }
}
