import java.util.Random;

public abstract class ATSP {
    protected int size;
    private String name;
    private double avgTime;
    private double avgSolution;
    //private int bestSolution;
    // private int worstSolution;
    private int matrix[][];

    public ATSP(String name, int size, int matrix[][]) {
        this.name = name;
        this.size = size;
        this.matrix = matrix;
    }

    protected int[] generateRandomPermutations(int permutation[]) {
        for (int i = 0; i < this.size; i++) {
            permutation[i] = i;
        }
        Random generator = new Random();
        int help = 0;
        int randomArg = 0;
        for (int i = this.size - 1; i > 0; i--) {
            help = permutation[i];
            randomArg = generator.nextInt(i + 1);
            permutation[i] = permutation[randomArg];
            permutation[randomArg] = help;
        }
        return permutation;
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
            for (int i = 0; i < this.size - 1; i++) {
                sumOfSolutions += this.matrix[result[i]][result[i + 1]];
            }
            sumOfSolutions += this.matrix[result[this.size - 1]][result[0]];
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
