package org.example.domain.service;

public final class CalculatorUtils {

    private static final double[] fieldWeights = new double[]{
            0,      // contactID <-- It is not taken into account for similarity calculation
            .25,       //name
            .25,       // name1
            .15,       // email
            .15,       // postalZip
            .2        // address
    };

    private static final boolean[] fieldIndexed = {false, true,true,true,true,true};

    public static SimilarityCalculator createCalculator() {
        StringRanker stringRanker = new StringRanker();
        return new SimilarityCalculator(stringRanker, fieldWeights, fieldIndexed);
    }
}
