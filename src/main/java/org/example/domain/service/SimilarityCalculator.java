package org.example.domain.service;

import org.example.domain.model.ContactSimilarity;
import org.example.domain.model.SimilarityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class SimilarityCalculator {
    private static int EXPECTED_FIELDS = 6;
    private static final Logger log = LoggerFactory.getLogger(SimilarityCalculator.class);
    private final StringRanker stringRanker;
    private final double[] fieldWeights;
    private final boolean[] fieldIndexed;

    public SimilarityCalculator(StringRanker stringRanker, double[] fieldWeights, boolean[] fieldIndexed) {
        if(fieldWeights == null || fieldIndexed == null) {
            throw new IllegalArgumentException("fieldWeights or fieldIndexed are null");
        }

        if(fieldWeights.length != EXPECTED_FIELDS || fieldIndexed.length !=  EXPECTED_FIELDS) {
            throw new IllegalArgumentException(
                    "fieldWeights and fieldIndexed size should be " +EXPECTED_FIELDS +" but was " + fieldWeights.length + " and " + fieldIndexed.length
            );
        }

        double weightSum = Arrays.stream(fieldWeights).sum();
        if( weightSum!= 1.0) {
            throw new IllegalArgumentException("fieldWeights sum must be 1.0 but was " + weightSum);
        }

        this.stringRanker = stringRanker;
        this.fieldWeights = fieldWeights;
        this.fieldIndexed = fieldIndexed;
    }


    public ContactSimilarity calculateSimilarity(String[] lineParts1, String[] lineParts2) {
        double weight = stringRanker.rank(lineParts1, lineParts2, fieldWeights, fieldIndexed);
        SimilarityLevel similarity= SimilarityLevel.getByWeight(weight);
        return new ContactSimilarity(lineParts1[0], lineParts2[0], weight, similarity);
    }
}
