public class AtspRandom extends ATSP {
    public AtspRandom(String name, int size, int[][] matrix) {
        super(name, size, matrix);
    }

    @Override
    void algorithm(int result[]) {
        this.generateRandomPermutations(result);
        long cost= this.calculateCost(result);
        if(cost<this.bestSolutionCost){
            this.bestSolutionCost=cost;
            this.bestSolution=result;
        }
    }
}
