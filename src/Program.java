
import java.util.HashMap;


public class Program {
    
    private static final int TEST_NUMBER_OF_GENERATED_SAMPLES = 1000000;
    
    private static final double TEST_MIX_COEF = 0.2;
    
    private static final int TEST_LAMBDA_1 = 10;
    
    private static final int TEST_LAMBDA_2 = 20;
    
    private static final int TEST2_NUMBER_OF_GENERATED_SAMPLES = 10000000;
    
    private static final double TEST2_MIX_COEF = 0.7;
    
    private static final int TEST2_LAMBDA_1 = 1;
    
    private static final int TEST2_LAMBDA_2 = 2;
    
    
    
    
    private int numberOfGeneratedNumbers;
    
    private double mixCoef;
    
    private int lambda1;
    
    private int lambda2;
    
    private double sumOfGeneratedNumbers;
    
    private double expectedValue;
    
    private double variance;
    
    private double[] generatedNumbers;
    
    private double[][] distribution;
    
    public static void main (String[] arguments){
        
        Program program = null;
        if(arguments.length == 4){
            program = new Program(Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2]), 
                    Integer.parseInt(arguments[3]), Integer.parseInt(arguments[4]));
        }
        else{
            program = new Program(TEST_NUMBER_OF_GENERATED_SAMPLES, TEST_MIX_COEF, TEST_LAMBDA_1, TEST_LAMBDA_2);
            program.run();
            program = new Program(TEST2_NUMBER_OF_GENERATED_SAMPLES, TEST2_MIX_COEF, TEST2_LAMBDA_1, TEST2_LAMBDA_2);
        }
        program.run();
    }

    public Program(int numberOfGeneratedSamples, double mixCoef, int lambda1, int lambda2) {
        this.numberOfGeneratedNumbers = numberOfGeneratedSamples;
        this.mixCoef = mixCoef;
        this.lambda1 = lambda1;
        this.lambda2 = lambda2;
    }
    
    
    
    public void run(){
        generateNumbers();
        printOutput();
    }

    private void printOutput() {
        System.out.printf("E_teorie=%f\n", getExpectedValueTheory());
        System.out.printf("D_teorie=%f\n", getVarianceTheory());
        System.out.printf("E_vypocet=%f\n", expectedValue);
        System.out.printf("D_vypocet=%f\n", variance);
        System.out.println();
        System.out.println("HISTOGRAM");
        printDistribution();
    }

    private double getExpectedValueTheory() {
        return mixCoef / lambda1 + (1 - mixCoef) / lambda2;
    }

    private double getVarianceTheory() {
        return 2 / Math.pow(lambda1, 2) * mixCoef + 2 / Math.pow(lambda2, 2) * (1 - mixCoef) - Math.pow(expectedValue, 2);
    }

    private void generateNumbers() {
        generatedNumbers = new double[numberOfGeneratedNumbers];
        int n = 0;
        double difference;
        double distanceSquareSum = 0;
        distribution = new double[][]{{0.1, 0}, {0.2, 0}, {0.3, 0}, {0.4, 0}, {0.5, 0}, {0.6, 0}, {0.7, 0}, {0.8, 0},
            {0.9, 0}, {1.0, 0}};
        for(int i = 0; i < numberOfGeneratedNumbers; i++){
            n++;
            double randomValue = getRandomValue();
            difference = randomValue - expectedValue;
            expectedValue += difference / n; 
            distanceSquareSum += difference * (randomValue - expectedValue);
            increaseDistribution(randomValue);
        }
        variance = distanceSquareSum / numberOfGeneratedNumbers;
    }

    private double getRandomValue() {
        final double distributionChoice = Math.random();
        if(distributionChoice < mixCoef){
            return getRandomNumberFromExponentialDistribution(lambda1);
        }
        else{
            return getRandomNumberFromExponentialDistribution(lambda2);
        }
    }

    private double getRandomNumberFromExponentialDistribution(final int lambda) {
        final double uniformDistributionRandomNumber = Math.random();
        return - Math.log(1 - uniformDistributionRandomNumber) / lambda;
    }

    private void increaseDistribution(double generatedNumber) {
        
        for(int i = 0; i < 10; i++){
            if(distribution[i][0] >= generatedNumber){
                distribution[i][1]++;
                break;
            }
        }
    }

    private void printDistribution() {
        int numberOfStars;
        String string;
        for(int i = 0; i < 10; i++){
            numberOfStars = (int) Math.round(distribution[i][1]++ / numberOfGeneratedNumbers * 50);
            string = "";
            for(int j = 0; j < numberOfStars; j++){
                string = string.concat("*");
            }
            System.out.println(string);
        }
    }
}
