##  The code was divided into 3 layers:
* application: here is the use case
* domain: all the business logic is in this package
* infrastructure: here is the csv reader implementation

##  To run the program you should send three parameters:
* --weights: Weights for fields that will be taken into account during similarity calculation
It is a list of doubles. Their sum should be 1. It size must be 6
* --fields: Fields that will be taken into account during similarity calculation
It is a list of booleans. It size must be 6
if fields[i] it means that the field in the i-position will be taken into account in the similarity calculation
* --cvsPath: Path for the CVS to be processed
* --outputPath: Path where the result is saved

## Used Frameworks 
* OpenCsv: CVS file parser.
* lombok: getters and setters generator (avoiding boiler plate code).
* Apache Commons Text: used to calculate Levenshtein Distance).
* slf4j/logback: logging stuff.
* JCommander: Command line arguments parser.
* Testing: AssertJ, JUnit, Mockito
* Maven: dependency management. Jacoco plugin was configured to measure coverage during building(to be improved)

## Flow
1. SimilarityProcessor#calculateSimilarity loads data provided by OpenCsvIterator
2. It iterates over all pair possible of lines. 
###### Similarity is calculated only for:
	- lines with different ids
	- pairs whose simularity has been calculated previously

## Pending tasks:
* Improve coverage
* Improve Main execution
* Program input: user should be able to select the output format (csv, json). Currently, the output is saved into a file.