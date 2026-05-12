package org.example.steeldoor.utils.strategy;

import org.example.steeldoor.model.Company;
import org.example.steeldoor.utils.SortStrategy;

import java.util.Comparator;
import java.util.List;

public class SortByIndustry implements SortStrategy<Company> {
    @Override
    public void sort(List<Company> items) {
        items.sort(Comparator.comparing(Company::getIndustry, Comparator.nullsLast(String::compareToIgnoreCase)));
    }
}
