package org.example.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ContactSimilarity {
    private String contactIdSource;
    private String contactIdMatch;
    private double similarity;
    private SimilarityLevel accuracy;
}
