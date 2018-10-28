import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AtspSimpleHeuristic extends ATSP {
    public AtspSimpleHeuristic(String name, int size, Double[][] matrix) {
        super(name, size, matrix);
    }

    boolean[] choosedElements;

    @Override
    void algorithm() {
        this.iterations = 0;
        this.getSolution();
        this.currentSolutionCost = this.calculateCost(this.currentSolution);
        this.firstSolution = this.currentSolution.clone();
        this.firstSolutionCost = this.currentSolutionCost;
    }

    private void getSolution() {
        this.iterations++;
        this.choosedElements = new boolean[this.size];
        Random generator = new Random();
        int currentNode = generator.nextInt(this.size);
        this.currentSolution[0] = currentNode;
        this.choosedElements[currentNode] = true;

        for (int i = 1; i < this.size; i++) {
            currentNode = this.argMinWithoutRepetition(currentNode);
            if (currentNode == -1) {
                break;
            }
            this.choosedElements[currentNode] = true;
            this.currentSolution[i] = currentNode;
        }
        if (currentNode == -1 || this.getArcCost(this.currentSolution, this.size - 1, 0) == Double.POSITIVE_INFINITY) {
            this.getSolution();
        }
    }

    private Integer argMinWithoutRepetition(int node) {
        Double min = Double.POSITIVE_INFINITY;
        int argmin = -1;
        for (int i = 0; i < this.size; i++) {
            if (this.matrix[node][i] < min && !this.choosedElements[i]) {
                min = this.matrix[node][i];
                argmin = i;
            }
        }
        return argmin;
    }
}
