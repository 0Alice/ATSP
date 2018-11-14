import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static jdk.nashorn.internal.objects.NativeMath.round;

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
    protected long evaluatedSolutions;

    protected Double bestSolutionCost;
    protected int bestSolution[];
    BufferedWriter fileWriter;
    protected long minTime;

    public ATSP(String name, int size, Double matrix[][], String fileName) {
        this.name = name;
        this.size = size;
        this.matrix = matrix;
        currentSolution = new int[size];
        firstSolution = new int[size];
        firstSolution = new int[size];
        bestSolutionCost = Double.POSITIVE_INFINITY;
        minTime = 1000000000;//1 second
        try {
            fileWriter = new BufferedWriter(new FileWriter(fileName + name + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected int[] generateRandomPermutations(int permutation[]) {
        for (int i = 0; i < size; i++) {
            permutation[i] = i;
        }
        Random generator = new Random();
        int help;
        int randomArg;
        for (int i = size - 1; i > 0; i--) {
            help = permutation[i];
            randomArg = generator.nextInt(i + 1);
            permutation[i] = permutation[randomArg];
            permutation[randomArg] = help;
        }
        return permutation;
    }

    protected Double getArcCost(int solution[], int firstElement, int secondElement) {
        return matrix[solution[firstElement]][solution[secondElement]];
    }

    protected Double calculateCost(int solution[]) {
        Double sumOfSolutions = new Double(0);
        for (int i = 0; i < size - 1; i++) {
            sumOfSolutions += getArcCost(solution, i, i + 1);
        }
        sumOfSolutions += getArcCost(solution, size - 1, 0);
        return sumOfSolutions;
    }

    protected Double calculateCostChangeOnSwap(int firstElement, int secondElement) {
        return 0
                - getArcCost(currentSolution, Math.floorMod((firstElement - 1), size), firstElement)
                - getArcCost(currentSolution, firstElement, (firstElement + 1) % size)
                - getArcCost(currentSolution, Math.floorMod((secondElement - 1), size), secondElement)
                - getArcCost(currentSolution, secondElement, (secondElement + 1) % size)
                + getArcCost(currentSolution, Math.floorMod((firstElement - 1), size), secondElement)
                + getArcCost(currentSolution, secondElement, (firstElement + 1) % size)
                + getArcCost(currentSolution, Math.floorMod((secondElement - 1), size), firstElement)
                + getArcCost(currentSolution, firstElement, (secondElement + 1) % size);
    }

    protected void swapElements(int firstElement, int secondElement) {
        int help = currentSolution[firstElement];
        currentSolution[firstElement] = currentSolution[secondElement];
        currentSolution[secondElement] = help;
    }

    public void solve() {
        int minL = 10;
        double l = 0;
        long sumOfSolutions = 0;
        long estimatedTime = 0;
        long sumIterations = 0;
        long sumOfEvaluatedSolutions = 0;
        List<Double> solutionsCost = new LinkedList<>();
        List<int[]> solutionsPermutation = new LinkedList<>();
        do {
            long startTime = System.nanoTime();
            algorithm();
            estimatedTime += System.nanoTime() - startTime;
            solutionsCost.add(currentSolutionCost);
            solutionsPermutation.add(currentSolution.clone());
            sumOfSolutions += currentSolutionCost;
            sumIterations += iterations;
            sumOfEvaluatedSolutions += evaluatedSolutions;
            if (bestSolutionCost > currentSolutionCost) {
                bestSolutionCost = currentSolutionCost;
                bestSolution = currentSolution.clone();
            }
            l++;
        } while (l < minL || estimatedTime < minTime);

        avgTime = estimatedTime / l;
        avgSolution = sumOfSolutions / l;
        try {
            fileWriter.write(name + "\n");
            fileWriter.write(Arrays.toString(bestSolution) + ";" + bestSolutionCost + ";" + avgSolution + ";" + avgTime
                    + ";" + round(2, sumIterations / l) + ";" + round(2, sumOfEvaluatedSolutions / l) + ";" + l + "\n");
            for (int i = 0; i < solutionsCost.size(); i++) {
                fileWriter.write(Arrays.toString(solutionsPermutation.get(i)) + ";" + solutionsCost.get(i) + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*System.out.println(name);
        System.out.println(Arrays.toString(bestSolution) + ";" + bestSolutionCost + ";" + avgSolution + ";" + avgTime
                + ";" + round(2,sumIterations / l) + ";" +  round(2,sumOfEvaluatedSolutions / l) + ";" + l);
        for (int i = 0; i < solutionsCost.size(); i++) {
            System.out.println(Arrays.toString(solutionsPermutation.get(i)) + ";" + solutionsCost.get(i));
        }*/
    }

    public void solveTimeOnly() {
        int minL = 10;
        double l = 0;
        long startTime = System.nanoTime();
        do {
            algorithm();
            l++;
        } while (l < minL || System.nanoTime() - startTime < minTime);
        long estimatedTime = System.nanoTime() - startTime;
        avgTime = estimatedTime / l;
        try {
            fileWriter.write(name + "\n");
            fileWriter.write(avgTime + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*System.out.println(name);
        System.out.println(avgTime);*/
    }

    public void multisolveWithMileagePrints(int times) {
        //System.out.println(name);
        try {
            fileWriter.write(name + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < times; i++) {
            algorithm();
            if (bestSolutionCost > currentSolutionCost) {
                bestSolutionCost = currentSolutionCost;
                bestSolution = currentSolution.clone();
            }
            try {
                fileWriter.write(firstSolutionCost + ";" + Arrays.toString(currentSolution) + ";" + currentSolutionCost + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(firstSolutionCost + ";" + Arrays.toString(currentSolution) + ";" + currentSolutionCost);
        }
        try {
            fileWriter.write(bestSolutionCost + ";" + Arrays.toString(bestSolution) + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(Arrays.toString(bestSolution) + ";" + bestSolutionCost);
    }

    abstract void algorithm();
}
