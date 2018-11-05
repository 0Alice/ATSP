public class AtspGreedy extends ATSP {
    public AtspGreedy(String name, int size, Double[][] matrix) {
        super(name, size, matrix);
    }

    @Override
    void algorithm() {
        firstSolution=generateRandomPermutations(firstSolution);
        firstSolutionCost = calculateCost(firstSolution);
        currentSolution = firstSolution.clone();
        currentSolutionCost = firstSolutionCost;
        iterations=0;
        evaluatedSolutions=0;
        Double costChange;
        do {
            costChange = getNextResult();
        } while (costChange<0);
        currentSolutionCost = calculateCost(currentSolution);
    }

    private Double getNextResult() {
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                evaluatedSolutions++;
                Double costChange = calculateCostChangeOnSwap(i, j);
                if (costChange < 0) {
                    iterations++;
                    swapElements(i, j);
                    return costChange;
                }
            }
        }
        return new Double(0);
    }
}
