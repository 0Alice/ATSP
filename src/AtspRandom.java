import java.util.Arrays;

public class AtspRandom extends ATSP {
    public AtspRandom(String name, int size, Double[][] matrix, long time) {
        super(name, size, matrix);
        this.timePerMileage = time;
        this.nextSolution = new int[this.size];
    }
    private int nextSolution[];

    private long timePerMileage;

    @Override
    void algorithm() {
        long startTime = System.nanoTime();

        this.firstSolution=this.generateRandomPermutations(this.firstSolution);
        this.firstSolutionCost = this.calculateCost(this.firstSolution);
        this.currentSolution = this.firstSolution.clone();
        this.currentSolutionCost = this.firstSolutionCost;
        this.iterations=1;
        while (System.nanoTime() - startTime < this.timePerMileage) {
            this.iterations++;
            this.generateRandomPermutations(this.nextSolution);
            Double cost = this.calculateCost(this.nextSolution);
            if (cost < this.currentSolutionCost) {
                this.currentSolutionCost = cost;
                this.currentSolution = this.nextSolution.clone();
            }
        }
    }
}
