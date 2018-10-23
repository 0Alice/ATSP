public class AtspGreedy extends ATSP {
    public AtspGreedy(String name, int size, int[][] matrix) {
        super(name, size, matrix);
    }

    //it is greedy not steepest
    @Override
    void algorithm(int result[]) {
        this.generateRandomPermutations(result);
        long costChange;
        int i=1000000;
        do {
            costChange = this.getNextResult(result);
            i--;
        } while (costChange<0 && i>0);
        this.bestSolutionCost = this.calculateCost(result);;
        this.bestSolution = result;
    }

    private long getNextResult(int result[]) {
        for (int i = 0; i < this.size; i++) {
            for (int j = i + 1; j < this.size; j++) {
                long costChange = calculateCostChangeOnSwap(result, i, j);
                if (costChange < 0) {
                    this.swapElements(result, i, j);
                    return costChange;
                }
            }
        }
        return 0;
    }
}
