package org.example.domain.service;

import lombok.AllArgsConstructor;
import org.example.domain.model.ContactSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.newFixedThreadPool;

@AllArgsConstructor
public class SimilarityProcessor {
    private static final Logger log = LoggerFactory.getLogger(SimilarityProcessor.class);
    private final SimilarityCalculator calculator;
    private final LineIterator parentProvider;
    private final LineIterator childrenProvider;


    public List<ContactSimilarity> calculateSimilarity() {

        List<ContactSimilarity> result = new ArrayList<>();
        List<Callable<ContactSimilarity>> futures = new ArrayList<>();
        Map<String, Set<String>> alreadyProcessedRelationships = new HashMap<>();
        while (parentProvider.hasNext()) {
            String[] line1 = parentProvider.next();
            String id1 = line1[0];
            while (childrenProvider.hasNext()) {
                String[] line2 = childrenProvider.next();
                String id2 = line2[0];
                if(Objects.equals(id1, id2)) {
                    continue;
                }

                if(wasAlreadyProcessed(id1, id2, alreadyProcessedRelationships)) {
                    log.info("Skipping id1={} id2={} because their similarity was calculated previously", id1, id2);
                    continue;
                }
                alreadyProcessedRelationships.computeIfAbsent(id1, it -> new HashSet<>()).add(id2);

                futures.add(() -> calculator.calculateSimilarity(line1, line2));
            }
            childrenProvider.reset();
        }
        try (ExecutorService executorService = newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            try {
                List<Future<ContactSimilarity>> executed = executorService.invokeAll(futures);

                for (Future<ContactSimilarity> future : executed) {
                    try {
                        result.add(future.get());
                    } catch (ExecutionException e) {
                        throw new RuntimeException("Error executing similarity calculation: " + e.getMessage(), e);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Error executing similarity calculation: " + e.getMessage(), e);
            }
        }

        return result;
    }


    private boolean wasAlreadyProcessed(String id1, String id2, Map<String, Set<String>> alreadyProcessedRelationships ) {
        Set<String> valuesForId1 = alreadyProcessedRelationships.get(id1);
        if(valuesForId1 != null && valuesForId1.contains(id2)) {
            return true;
        }
        Set<String> valuesForId2 = alreadyProcessedRelationships.get(id2);
        return valuesForId2 != null && valuesForId2.contains(id1);
    }
}