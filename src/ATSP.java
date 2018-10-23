import java.util.Random;

public abstract class ATSP {
    protected int size;
    private String name;
    private double avgTime;
    private double avgSolution;
    protected long bestSolutionCost;
    protected int bestSolution[];
    // private int worstSolution;
    private int matrix[][];

    public ATSP(String name, int size, int matrix[][]) {
        this.name = name;
        this.size = size;
        this.matrix = matrix;
        this.bestSolution = new int[this.size];
    }

    protected int[] generateRandomPermutations(int permutation[]) {
        for (int i = 0; i < this.size; i++) {
            permutation[i] = i;
        }
        Random generator = new Random();
        int help ;
        int randomArg;
        for (int i = this.size - 1; i > 0; i--) {
            help = permutation[i];
            randomArg = generator.nextInt(i + 1);
            permutation[i] = permutation[randomArg];
            permutation[randomArg] = help;
        }
        return permutation;
    }

    private long getArcCost(int solution[], int firstElement, int secondElement) {
        return this.matrix[solution[firstElement]][solution[secondElement]];
    }

    protected long calculateCost(int solution[]) {
        long sumOfSolutions = 0;
        for (int i = 0; i < this.size - 1; i++) {
            sumOfSolutions += this.getArcCost(solution, i, i + 1);
        }
        sumOfSolutions += this.getArcCost(solution, this.size - 1, 0);
        return sumOfSolutions;
    }

    protected long calculateCostChangeOnSwap(int prevSolution[], int firstElement, int secondElement) {
        return  0
                - this.getArcCost(prevSolution, Math.floorMod((firstElement - 1), this.size), firstElement)
                - this.getArcCost(prevSolution, firstElement, (firstElement + 1) % this.size)
                - this.getArcCost(prevSolution, Math.floorMod((secondElement - 1), this.size), secondElement)
                - this.getArcCost(prevSolution, secondElement, (secondElement + 1) % this.size)
                + this.getArcCost(prevSolution, Math.floorMod((firstElement - 1), this.size), secondElement)
                + this.getArcCost(prevSolution, secondElement, (firstElement + 1) % this.size)
                + this.getArcCost(prevSolution, Math.floorMod((secondElement - 1), this.size), firstElement)
                + this.getArcCost(prevSolution, firstElement, (secondElement + 1) % this.size);
    }

    protected int [] swapElements(int prevSolution[], int firstElement, int secondElement) {
        int help = prevSolution[firstElement];
        prevSolution[firstElement] = prevSolution[secondElement];
        prevSolution[secondElement] = help;
        return prevSolution;
    }

    public void solve() {
        long minTime = 10 * 1000000000;//1 second
        int minL = 10;
        int[] result = new int[this.size];
        double l = 0;
        long sumOfSolutions = 0;
        long startTime = System.nanoTime();
        do {
            this.algorithm(result);
            sumOfSolutions += this.bestSolutionCost;
            l++;
        } while (l < minL || System.nanoTime() - startTime < minTime);
        long estimatedTime = System.nanoTime() - startTime;
        this.avgTime = estimatedTime / l;
        this.avgSolution = sumOfSolutions / l;
        System.out.println("\n" + this.name);
        System.out.format("%.2f ns %n", this.avgTime);
        System.out.format("%.2f%n", this.avgSolution);
    }

    abstract void algorithm(int result[]);
}
