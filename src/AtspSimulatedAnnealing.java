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
        d = maxDiff();
        System.out.println(d);
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
        while (iterationsWithoutImprovement < 1000/*Math.pow(this.size,2)*/);//TODO >this.size or maybe more
        currentSolution = currentBestSolution.clone();
        currentSolutionCost = currentBestSolutionCost;
    }

    private double maxDiff() {
        Double maxD = new Double(0);
        for (int i = 0; i < 10000; i++) {
            currentSolution = generateRandomPermutations(currentSolution);
            Double cD = Math.abs(calculateCostChangeOnSwap(generator.nextInt(this.size), generator.nextInt(this.size)));
            if (cD < Double.POSITIVE_INFINITY && cD > maxD) {
                maxD = cD;
            }
        }
        return maxD.doubleValue();
    }

    private double avgDiff() {
        Double avgD = new Double(0);
        int j = 0;
        int i=0;
        do {
            currentSolution = generateRandomPermutations(currentSolution);
            Double cD = Math.abs(calculateCostChangeOnSwap(generator.nextInt(this.size), generator.nextInt(this.size)));
            if (cD>0&&cD < Double.POSITIVE_INFINITY) {
                avgD += cD;
                j++;
            }
            i++;
        } while (i<10000 || j < 5);
        System.out.println(avgD.doubleValue());
        return avgD.doubleValue() / j;
    }

    private Double getNextResult() {
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                evaluatedSolutions++;
                if (evaluatedSolutions % this.size == 0) {
                    temperature *= 0.9;
                }
                Double costChange = calculateCostChangeOnSwap(i, j);
                if (costChange <= 0 || exp(-costChange / temperature) > generator.nextDouble()) {
                    iterations++;
                    swapElements(i, j);
                    currentSolutionCost += costChange;
                    if (currentSolutionCost <= currentBestSolutionCost) {
                        currentBestSolution = currentSolution.clone();
                        currentBestSolutionCost = calculateCost(currentSolution);
                    }
                    return costChange;
                }
            }
        }
        return Double.POSITIVE_INFINITY;
    }
}
