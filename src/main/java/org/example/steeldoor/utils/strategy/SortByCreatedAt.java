package org.example.steeldoor.utils.strategy;

import org.example.steeldoor.model.Submission;
import org.example.steeldoor.utils.SortStrategy;

import java.util.Comparator;
import java.util.List;

public class SortByCreatedAt implements SortStrategy<Submission>  {
    @Override
    public void sort(List<Submission> items) {
        items.sort(Comparator.comparing(Submission::getCreatedAt));
    }
}
