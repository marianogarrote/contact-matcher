package org.example.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.example.domain.model.SimilarityLevel.*;


public class SimilarityLevelTest {

    @Test
    public void validateValues() {
        double[] values = new double[]{
                0.0, .2, .25, .5, // Low
                0.50001, .75, .8,  // Medium
                .85, .95, .99, 1.0 // High
        };
        SimilarityLevel[] expected = new SimilarityLevel[]{
                None, Low, Low, Low, Medium, Medium, Medium, High,High,High,High
        };

        for (int i = 0; i < values.length; i++) {
            Assertions.assertThat(expected[i]).isEqualTo(SimilarityLevel.getByWeight(values[i]));
        }
    }

    @Test
    public void testInvalidValues() {
        for(double value : new double[]{10.0, 1.000001, -1.0,-0.0001}) {
            Assertions.assertThatThrownBy(() -> SimilarityLevel.getByWeight(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Weight must be between 0 and 1");

        }
    }

}
