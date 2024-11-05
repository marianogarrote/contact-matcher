package org.example.application;

import lombok.AllArgsConstructor;
import org.example.domain.model.ContactSimilarity;
import org.example.domain.service.SimilarityProcessor;

import java.util.List;

@AllArgsConstructor
public class CalculateSimilarityUseCase {

    private final SimilarityProcessor similarityProcessor;
    public List<ContactSimilarity> calculate() {
        return similarityProcessor.calculateSimilarity();
    }
}
