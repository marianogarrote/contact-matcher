package org.example.infrastructure;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.example.domain.service.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;

public class OpenCsvIterator implements LineIterator, AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(OpenCsvIterator.class);
    private CSVReader reader;
    private String[] nextBatch;
    private final String filePath;

    public OpenCsvIterator(String filePath) {
        this.filePath = filePath;
        initReader();
    }

    private void initReader() {
        try {
            this.reader = new CSVReaderBuilder(new FileReader(filePath)).build();
            reader.skip(1); // Skipping headers
            readNextBatch();
        } catch (IOException e) {
            throw new RuntimeException("Error creating Iterator: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasNext() {
        return nextBatch != null && nextBatch[0] != null;
    }

    @Override
    public String[] next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        String[] result = nextBatch;
        readNextBatch();
        return result;
    }

    private void readNextBatch()  {
        try {
            nextBatch = reader.readNext();
        } catch (IOException  | CsvValidationException e) {
            log.error("Error reading CSV file: {}", e.getMessage(), e);
            throw new RuntimeException("Error reading CSV file: "+ e.getMessage(), e);
        }
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }

    private static final String DATA_PATH = "src/main/resources/data.csv";
    private static final String file_Path = new File(DATA_PATH).getAbsolutePath();
    public static void main(String[] args) {

        long iterations = 0;
        try (OpenCsvIterator iterator = new OpenCsvIterator(file_Path)) {
            while (iterator.hasNext()) {
                String[] line = iterator.next();
                System.out.printf("Batch #%s rows (Id = %s)%n", ++iterations, line[0]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reset() {
        log.info("Resetting iterator");
        initReader();
        log.info("Iterator was reset");
    }
}