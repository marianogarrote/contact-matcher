package org.example.domain.service;

import org.assertj.core.api.Assertions;
import org.example.domain.model.ContactSimilarity;
import org.example.domain.model.SimilarityLevel;
import org.junit.Test;

public class SimilarityCalculatorTest {

    @Test
    public void test() {
        SimilarityCalculator calculator = CalculatorUtils.createCalculator();
        check(
                new String[] {"1","Ciara","French","mollis.lectus.pede@outlook.net","39746","449-6990 Tellus. Rd."},
                new String[]{"2","Ciara","French","mollis.lectus.pede@outlook.net","39746","449-6990 Tellus. Rd."},
                calculator,
                SimilarityLevel.High
        );

        check(
                new String[] {"1","ZZZZ","Z","wertyuio@outlook.net","1111","449-6990 Tellus. Rd."},
                new String[]{"2","AAAA","A","sdafdgdfg@outlook.net","99999","fghjk  asdfljasdas"},
                calculator,
                SimilarityLevel.Low
        );


    }


    private void check(String[] line1, String[] line2, SimilarityCalculator calculator, SimilarityLevel expected) {
        ContactSimilarity similarity1 = calculator.calculateSimilarity(line1, line2);
        Assertions.assertThat(similarity1.getAccuracy()).isEqualTo(expected);
    }

}
