package org.example.domain.model;

public enum SimilarityLevel {
    High, Medium, Low, None;

    public static SimilarityLevel getByWeight(double weight) {
        if(weight < 0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be between 0 and 1");
        }

        if(weight == 0) {
            return None;
        }
        if(weight <= 0.50) {
            return Low;
        }

        if(weight <= 0.8) {
            return Medium;
        }
        return High;
    }
}
