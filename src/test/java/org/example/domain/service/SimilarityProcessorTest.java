package org.example.domain.service;


import org.assertj.core.api.Assertions;
import org.example.domain.model.ContactSimilarity;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SimilarityProcessorTest {

    @Test
    public void testSuccessfulProcessing() {
        SimilarityProcessor processor = new SimilarityProcessor(
                CalculatorUtils.createCalculator(),
                new LineIteratorMock(),
                new LineIteratorMock()
        );

        List<ContactSimilarity> result = processor.calculateSimilarity();

        int n = lines.size() - 1;
        int expectedElement = n*(n+1)/2;
        Assertions.assertThat(result).hasSize(expectedElement);
    }


    private static final List<String[]> lines = initLines();

    private static List<String[]> initLines() {
        List<String[]> lines = new ArrayList<>();
        lines.add(new String[]{ "1","Ciara","French","mollis.lectus.pede@outlook.net","39746","\"P.O. Box 775, 8910 Arcu. Road\""});
        lines.add(new String[]{"2","Charles","Pacheco","nulla.eget@protonmail.couk","76837","Ap #312-8611 Lacus. Ave"});
        lines.add(new String[]{"3","Victor","Savage","orci@protonmail.net","82025","P.O. Box 775, 8910 Arcu. Road"});
        lines.add(new String[]{"4","Paul","Gaines","quis.diam@aol.couk","95904","735-3498 Magna. Street"});
        lines.add(new String[]{"5","Freya","West","vivamus.rhoncus@aol.com","83604","4811 Aliquam St."});
        lines.add(new String[]{"6","Irma","Lott","a.mi@outlook.edu","36883","309-4003 Metus. Rd."});
        lines.add(new String[]{"7","Irma","Lott","a.mi@outlook.edu","36883","309-4003 Metus. Rd."});
        return lines;
    }


    private static class LineIteratorMock implements LineIterator {
        private int position;
        public LineIteratorMock() {
            position = 0;
        }

        @Override
        public void reset() {
            position = 0;
        }

        @Override
        public boolean hasNext() {
            return position < lines.size();
        }

        @Override
        public String[] next() {
            return lines.get(position++);
        }
    }
}
