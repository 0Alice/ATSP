public class AtspGreedy extends ATSP {
    public AtspGreedy(String name, int size, Double[][] matrix) {
        super(name, size, matrix);
    }

    @Override
    void algorithm() {
        this.firstSolution=this.generateRandomPermutations(this.firstSolution);
        this.firstSolutionCost = this.calculateCost(this.firstSolution);
        this.currentSolution = this.firstSolution.clone();
        this.currentSolutionCost = this.firstSolutionCost;
        this.iterations=1;
        Double costChange;
        do {
            costChange = this.getNextResult();
        } while (costChange<0);
        this.currentSolutionCost = this.calculateCost(this.currentSolution);
    }

    private Double getNextResult() {
        for (int i = 0; i < this.size; i++) {
            for (int j = i + 1; j < this.size; j++) {
                Double costChange = calculateCostChangeOnSwap(i, j);
                if (costChange < 0) {
                    this.iterations++;
                    this.swapElements(i, j);
                    return costChange;
                }
            }
        }
        return new Double(0);
    }
}
