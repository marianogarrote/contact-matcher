package org.example;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import lombok.Getter;
import lombok.Setter;
import org.example.application.CalculateSimilarityUseCase;
import org.example.domain.model.ContactSimilarity;
import org.example.domain.service.SimilarityCalculator;
import org.example.domain.service.SimilarityProcessor;
import org.example.domain.service.StringRanker;

import org.example.infrastructure.OpenCsvIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final boolean[] FIELD_WITH_SIMILARITY_CALCULATION = new boolean[] {
            false,      // contactID <-- It is not taken into account for similarity calculation
            true,       //name
            true,       // name1
            true,       // email
            true,       // postalZip
            true        // address
    };

    private static final double[] FIELD_WEIGHTS = new double[]{
            0,      // contactID <-- It is not taken into account for similarity calculation
            .25,       //name
            .25,       // name1
            .15,       // email
            .15,       // postalZip
            .2        // address
    };

    private static final String defaultPath = "src/main/resources/data.csv";

    public static void main(String[] args) {
        AppArgs appArgs = new AppArgs();
        JCommander commander = JCommander.newBuilder().addObject(appArgs).build();

        try {
            commander.parse(args);
        } catch (ParameterException e) {
            System.out.println("Error parsing arguments: " + e.getMessage());
            commander.usage();
            System.exit(1);
        }

        double[] weights = appArgs.getWeights() != null? appArgs.getWeights().stream().mapToDouble(Double::doubleValue).toArray() : FIELD_WEIGHTS;
        boolean[] fields = FIELD_WITH_SIMILARITY_CALCULATION;
        if (appArgs.fields != null) {
            fields = new boolean[appArgs.fields.size()];
            for (int i = 0; i < appArgs.fields.size(); i++) {
                fields[i] = appArgs.fields.get(i);
            }
        }

        CalculateSimilarityUseCase calculateUseCase = new CalculateSimilarityUseCase(
                new SimilarityProcessor(
                        new SimilarityCalculator(new StringRanker(), weights, fields),
                        new OpenCsvIterator(new File(appArgs.path).getAbsolutePath()),
                        new OpenCsvIterator(new File(appArgs.path).getAbsolutePath())
                )
        );
        List<ContactSimilarity> result = calculateUseCase.calculate();


        log.info("============      Calculated similarity result({})        ============ ", result.size());
        List<String[]> lines = new ArrayList<>();
        lines.add(new String[] {"ContactIDSource,ContactID Match,Accuracy"});
        lines.addAll(result.stream().map(Main::mapResult).toList());
        save(new File(appArgs.outputPath), lines);
    }

    private static String[] mapResult(ContactSimilarity item) {
        return new String[]{String.format("%s,%s,%s",item.getContactIdSource(), item.getContactIdMatch(), item.getAccuracy().name())};
    }

    private static void save(File csvFile, List<String[]> lines) {
        if(!csvFile.exists()) {
            try {
                csvFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Creating output csv file at " + csvFile.getAbsolutePath() + " failed", e);
            }
        }
        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(csvFile.getAbsolutePath())).build()) {
                writer.writeAll(lines);
        } catch (IOException e) {
            log.error("Error writing to CSV file: {}", e.getMessage());
        }
    }

    @Getter
    @Setter
    @Parameters(commandNames = "main", commandDescription = "Main command")
    private static class AppArgs {
        @Parameter(
                names = "--weights",
                variableArity = true,
                description = "Weights for fields that will be taken into account during similarity calculation"
        )
        public List<Double> weights;

        @Parameter(
                names = "--fields",
                description = "Fields that will be taken into account during similarity calculation",
                variableArity = true
        )
        public List<Boolean> fields;

        @Parameter(
                names = {"-csv", "--cvsPath"},
                description = "Path for the CVS to be processed",
                required = true
        )
        public String path;

        @Parameter(names = "--outputPath", description = "Path where the result is saved", required = true)
        public String outputPath;
    }
}