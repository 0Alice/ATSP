import java.util.Arrays;

public class AtspRandom extends ATSP {
    public AtspRandom(String name, int size, Double[][] matrix, long time) {
        super(name, size, matrix);
        timePerMileage = time;
        nextSolution = new int[size];
    }

    private int nextSolution[];

    private long timePerMileage;

    @Override
    void algorithm() {
        long startTime = System.nanoTime();
        iterations=0;
        evaluatedSolutions=0;
        do {
            firstSolution = generateRandomPermutations(firstSolution);
            firstSolutionCost = calculateCost(firstSolution);
            iterations++;
            evaluatedSolutions++;
        } while (firstSolutionCost == Double.POSITIVE_INFINITY);//to get acceptable solution
        currentSolution = firstSolution.clone();
        currentSolutionCost = firstSolutionCost;

        while (System.nanoTime() - startTime < timePerMileage) {
            iterations++;
            generateRandomPermutations(nextSolution);
            Double cost = calculateCost(nextSolution);
            if (cost < currentSolutionCost) {
                currentSolutionCost = cost;
                currentSolution = nextSolution.clone();
            }
        }
    }
}
