import java.util.Arrays;
import java.util.Random;

public abstract class ATSP {
    protected int size;
    private String name;
    private double avgTime;
    private double avgSolution;
    protected Double currentSolutionCost;
    protected int currentSolution[];
    protected Double firstSolutionCost;
    protected int firstSolution[];
    protected Double matrix[][];
    protected long iterations;

    protected Double bestSolutionCost;
    protected int bestSolution[];

    public ATSP(String name, int size, Double matrix[][]) {
        this.name = name;
        this.size = size;
        this.matrix = matrix;
        this.currentSolution = new int[this.size];
        this.firstSolution = new int[this.size];
        this.firstSolution = new int[this.size];
        this.bestSolutionCost = Double.POSITIVE_INFINITY;
    }

    protected int[] generateRandomPermutations(int permutation[]) {
        for (int i = 0; i < this.size; i++) {
            permutation[i] = i;
        }
        Random generator = new Random();
        int help;
        int randomArg;
        for (int i = this.size - 1; i > 0; i--) {
            help = permutation[i];
            randomArg = generator.nextInt(i + 1);
            permutation[i] = permutation[randomArg];
            permutation[randomArg] = help;
        }
        return permutation;
    }

    protected Double getArcCost(int solution[], int firstElement, int secondElement) {
        return this.matrix[solution[firstElement]][solution[secondElement]];
    }

    protected Double calculateCost(int solution[]) {
        Double sumOfSolutions = new Double(0);
        for (int i = 0; i < this.size - 1; i++) {
            sumOfSolutions += this.getArcCost(solution, i, i + 1);
        }
        sumOfSolutions += this.getArcCost(solution, this.size - 1, 0);
        return sumOfSolutions;
    }

    protected Double calculateCostChangeOnSwap(int firstElement, int secondElement) {
        return 0
                - this.getArcCost(this.currentSolution, Math.floorMod((firstElement - 1), this.size), firstElement)
                - this.getArcCost(this.currentSolution, firstElement, (firstElement + 1) % this.size)
                - this.getArcCost(this.currentSolution, Math.floorMod((secondElement - 1), this.size), secondElement)
                - this.getArcCost(this.currentSolution, secondElement, (secondElement + 1) % this.size)
                + this.getArcCost(this.currentSolution, Math.floorMod((firstElement - 1), this.size), secondElement)
                + this.getArcCost(this.currentSolution, secondElement, (firstElement + 1) % this.size)
                + this.getArcCost(this.currentSolution, Math.floorMod((secondElement - 1), this.size), firstElement)
                + this.getArcCost(this.currentSolution, firstElement, (secondElement + 1) % this.size);
    }

    protected void swapElements(int firstElement, int secondElement) {
        int help = this.currentSolution[firstElement];
        this.currentSolution[firstElement] = this.currentSolution[secondElement];
        this.currentSolution[secondElement] = help;
    }

    public void solve() {
        long minTime = 10 * 1000000000;//1 second
        int minL = 10;
        double l = 0;
        long sumOfSolutions = 0;
        long startTime = System.nanoTime();
        do {
            this.algorithm();
            sumOfSolutions += this.currentSolutionCost;
            if (this.bestSolutionCost > this.currentSolutionCost) {
                this.bestSolutionCost = this.currentSolutionCost;
                this.bestSolution = this.currentSolution.clone();
            }
            l++;
        } while (l < minL || System.nanoTime() - startTime < minTime);
        long estimatedTime = System.nanoTime() - startTime;
        this.avgTime = estimatedTime / l;
        this.avgSolution = sumOfSolutions / l;
        System.out.println("\n" + this.name);
        System.out.format("%.2f ns %n", this.avgTime);
        System.out.format("%.2f%n", this.avgSolution);
    }

    public void multisolveWithMileagePrints(int times) {
        System.out.println("\n" + this.name);
        for (int i = 0; i < times; i++) {
            this.algorithm();
            if (this.bestSolutionCost > this.currentSolutionCost) {
                this.bestSolutionCost = this.currentSolutionCost;
                this.bestSolution = this.currentSolution.clone();
            }
            //TODO better display
            System.out.println(Arrays.toString(firstSolution));
            System.out.println(firstSolutionCost);
            System.out.println(Arrays.toString(currentSolution));
            System.out.println(currentSolutionCost);
        }
        System.out.println(Arrays.toString(bestSolution));
        System.out.println(bestSolutionCost);
    }

    abstract void algorithm();
}
