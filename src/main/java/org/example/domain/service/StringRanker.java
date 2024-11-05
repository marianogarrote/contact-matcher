package org.example.domain.service;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class StringRanker {

    public double rank(String input1, String input2) {
        String longer = input1;
        String shorter = input2;
        if (input1.length() < input2.length()) { // longer should always have greater length
            longer = input2;
            shorter = input1;
        }

        int longerLength = longer.length();
        if (longerLength == 0) { // Both are empty strings
            return 1.0;
        }

        Integer distance = LevenshteinDistance.getDefaultInstance().apply(longer, shorter);
        return (longerLength - distance) / (double) longerLength;
    }


    private boolean areEquals(String[] line1, String[] line2, boolean[] fieldIndexed) {
        for (int i = 0; i < line1.length; i++) {
            if(fieldIndexed[i] && !line1[i].equals(line2[i])) {
                return false;
            }
        }
        return true;
    }

    public double rank(String[] input1, String[] input2, double[] fieldWeights, boolean[] fieldIndexed) {
        if(input1.length != input2.length) {
            throw new IllegalArgumentException(
                    String.format(
                            "(id1 = %s and id2 = %s) input1.length != input2.length (%s vs %s)",
                            input1[0],
                            input2[0],
                            input1.length,
                            input2.length
                    )
            );
        }
        // Avoid the cost of Levenshtein Distance calculation
        if(areEquals(input1, input2, fieldIndexed)) {
            return 1.0;
        }

        double result = 0;
        for(int i = 0; i < input1.length; i++) {
            if(fieldIndexed[i]) {
                result += rank(input1[i], input2[i]) * fieldWeights[i];
            }
        }
        return result;
    }
}
