import java.util.Random;

import static java.lang.Math.exp;

public class AtspSimulatedAnnealing extends ATSP {
    private Double currentBestSolutionCost;
    private int currentBestSolution[];
    private Double temperature;
    private Random generator;
    private double d;

    public AtspSimulatedAnnealing(String name, int size, Double[][] matrix, String fileName) {
        super(name, size, matrix, fileName);
        currentBestSolution = new int[size];
        currentBestSolutionCost = Double.POSITIVE_INFINITY;
        generator = new Random();
        d = avgDiff();
    }

    @Override
    void algorithm() {
        firstSolution = generateRandomPermutations(firstSolution);
        firstSolutionCost = calculateCost(firstSolution);
        currentSolution = firstSolution.clone();
        currentSolutionCost = firstSolutionCost;
        currentBestSolution = firstSolution.clone();
        currentBestSolutionCost = firstSolutionCost;

        temperature = d / 0.01005033585;//-ln(0.99)
        iterations = 0;
        int iterationsWithoutImprovement = 0;
        evaluatedSolutions = 0;
        Double costChange;
        do {
            costChange = getNextResult();
            if (costChange < 0) {
                iterationsWithoutImprovement = 0;
            } else {
                iterationsWithoutImprovement++;
            }
        }
        while (iterationsWithoutImprovement < Math.pow(this.size, 2));
        currentSolution = currentBestSolution.clone();
        currentSolutionCost = currentBestSolutionCost;
    }
    private double avgDiff() {
        double avgD= 0;
        int j = 0;
        int i = 0;
        do {
            currentSolution = generateRandomPermutations(currentSolution);
            Double cD = Math.abs(calculateCostChangeOnSwap(generator.nextInt(this.size), generator.nextInt(this.size)));
            if (cD > 0 && cD < Double.POSITIVE_INFINITY) {
                avgD += cD;
                j++;
            }
            i++;
        } while (i < 10000 || j < 5);
        return avgD/ j;
    }

    private Double getNextResult() {
        int i = generator.nextInt(this.size);
        int j = generator.nextInt(this.size);
        evaluatedSolutions++;
        temperature *= 0.75;
        Double costChange = calculateCostChangeOnSwap(i, j);
        if (costChange <= 0 || exp(-costChange / temperature) > generator.nextDouble()) {
            iterations++;
            swapElements(i, j);
            //currentSolutionCost += costChange;
            currentSolutionCost=calculateCost(currentSolution);
            if (currentSolutionCost <= currentBestSolutionCost) {
                currentBestSolution = currentSolution.clone();
                currentBestSolutionCost = calculateCost(currentSolution);
            }
            return costChange;

        }
        return Double.valueOf(0);
    }
}
