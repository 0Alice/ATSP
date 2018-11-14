public class AtspSteepest extends ATSP {
    public AtspSteepest(String name, int size, Double[][] matrix,String fileName) {
        super(name, size, matrix,fileName);
    }

    @Override
    void algorithm() {
        firstSolution = generateRandomPermutations(firstSolution);
        firstSolutionCost = calculateCost(firstSolution);
        currentSolution = firstSolution.clone();
        currentSolutionCost = firstSolutionCost;
        iterations=0;
        evaluatedSolutions=0;
        Double costChange;
        do {
            costChange = getNextResult();
        } while (costChange < 0);
        currentSolutionCost = calculateCost(currentSolution);
    }

    private Double getNextResult() {
        int iBest=0;
        int jBest=0;
        Double bestCost=new Double(0);
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                evaluatedSolutions++;
                Double costChange = calculateCostChangeOnSwap(i, j);
                if (costChange < 0) {
                    iBest=i;
                    jBest=j;
                    bestCost=costChange;
                }
            }
        }
        if(bestCost<0){
            iterations++;
            swapElements(iBest, jBest);
        }
        return bestCost;
    }
}
