import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AtspTabu extends ATSP {
    private Double bestAmount;
    private int tabuTab[][];
    private List<Double[]> movesList;
    private int bestMovesAmount = 10; //k pierszych najlepszych ruchów
    private int remember = 3; //Jak długo ruch jest tabu
    private int bestPermutation[];
    private List<Double[]> neighborhoodCost;
    private int currentMove[];
    private Double currentMoveImprovement;
    private boolean improvement;

    public AtspTabu(String name, int size, Double[][] matrix, String fileName) {
        super(name, size, matrix, fileName);
        tabuTab = new int[size - 1][size];
    }


    @Override
    void algorithm() {
        initializeTabuTab();
        int iterationsWithoutImprovement = 0;
        iterations = 0;
        evaluatedSolutions = 0;
        firstSolution = generateRandomPermutations(firstSolution);
        firstSolutionCost = calculateCost(firstSolution);
        bestAmount = firstSolutionCost;
        bestPermutation = firstSolution.clone();
        currentSolution = firstSolution.clone();
        currentSolutionCost = firstSolutionCost;
        searchingNeighborhood();
        if (checkMove()) {
            decreaseTabuTab();
        } else {
            System.out.println("NO MOVE WAS CHOOSEN");
        }
        do {
            mainIteration();
            if (improvement) {
                improvement = false;
                iterationsWithoutImprovement = 0;
            } else {
                iterationsWithoutImprovement += 1;
            }
        }
        while (iterationsWithoutImprovement < this.size);
        currentSolution = bestPermutation.clone();
        currentSolutionCost = calculateCost(bestPermutation);
    }

    private void mainIteration() {
        if (movesList.size() != 0) {
            calculateMovesCost();
        } else {
            searchingNeighborhood();
        }
        if (checkMove()) {
            decreaseTabuTab();
        } else {
            System.out.println("NO MOVE WAS CHOOSEN");
        }
    }

    private void initializeTabuTab() {
        for (int i = 0; i < (size - 1); i++) {
            for (int j = i + 1; j < size; j++) {
                tabuTab[i][j] = 0;
            }
        }
    }


    private void decreaseTabuTab() {
        for (int i = 0; i < (size - 1); i++) {
            for (int j = i + 1; j < size; j++) {
                if (tabuTab[i][j] > 0) {
                    tabuTab[i][j] -= 1;
                }
            }
        }
    }


    private void searchingNeighborhood() {
        neighborhoodCost = new ArrayList<>();
        movesList = new ArrayList<>();
        for (int i = 0; i < this.size - 1; i++) {
            for (int j = i + 1; j < this.size; j++) {
                neighborhoodCost.add(new Double[]{calculateCostChangeOnSwap(i, j), new Double(i), new Double(j)});
                evaluatedSolutions += 1;
            }
        }
        Collections.sort(neighborhoodCost, (Comparator<Double[]>) (d1, d2) -> d1[0].compareTo(d2[0]));
        for (int i = 0; i < bestMovesAmount; i++) {
            movesList.add(neighborhoodCost.get(i));
        }
    }

    private void calculateMovesCost() {
        boolean goodList = false;
        for (int i = 0; i < movesList.size(); i++) {
            currentMove = new int[]{movesList.get(i)[1].intValue(), movesList.get(i)[2].intValue()};
            movesList.get(i)[0] = calculateCostChangeOnSwap(currentMove[0], currentMove[1]);
            evaluatedSolutions += 1;
            if (movesList.get(i)[0] < 0) {
                goodList = true;
            }
        }
        Collections.sort(movesList, (Comparator<Double[]>) (d1, d2) -> d1[0].compareTo(d2[0]));
        if (!goodList) {
            searchingNeighborhood();
        }
    }

    private boolean checkMove() {
        for (int i = 0; i < movesList.size(); i++) {
            currentMove = new int[]{movesList.get(i)[1].intValue(), movesList.get(i)[2].intValue()};
            currentMoveImprovement = movesList.get(i)[0];
            if ((tabuTab[currentMove[0]][currentMove[1]] == 0) || (currentSolutionCost + currentMoveImprovement < bestAmount)) {
                swapElements(currentMove[0], currentMove[1]);
                iterations += 1;
                currentSolutionCost += currentMoveImprovement;
                movesList.remove(i);
                tabuTab[currentMove[0]][currentMove[1]] = remember + 1;
                if (currentSolutionCost < bestAmount) {
                    improvement = true;
                    bestAmount = currentSolutionCost;
                    bestPermutation = currentSolution.clone();
                }
                return true;
            }
        }
        return false;
    }
}


















