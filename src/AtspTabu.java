import java.util.*;

public class AtspTabu  extends ATSP{
    private Double bestAmount; //Moze tu bedzie potrzeba zmiany na double
    private int tabuTab[][];
    private List movesList;
    private int bestMovesAmount=4; //k pierszych najlepszych ruchów
    private Double treshold; //tu cos trzeba wymyslic!!!TODO
    private int remember=3; //Jak długo ruch jest tabu
    private int bestPermutation[];
    private List<Double[]> neighborhoodCost;

//calculateCostChangeOnSwap calculate cost without swaping
//swapElements swap two elements in current solution
    public AtspTabu(String name, int size, Double[][] matrix, String fileName) {
        super(name, size, matrix, fileName);
        tabuTab=new int[size-1][size];
        initializeTabuTab(size);
    }





    @Override
    void algorithm() {
        firstSolution = generateRandomPermutations(firstSolution);
        firstSolutionCost = calculateCost(firstSolution);
        bestAmount=firstSolutionCost;
        bestPermutation=firstSolution.clone();
        currentSolution=firstSolution.clone();
        currentSolutionCost=firstSolutionCost;
        searchingNeighborhood();
    }



    private void initializeTabuTab(int size){
       for(int i=0;i<(size-1);i++){
           for(int j=i+1;j<size;j++){
               tabuTab[i][j]=0;
           }
       }
    }


    private void decreaseTabuTab(int size){
        for(int i=0;i<(size-1);i++){
            for(int j=i+1;j<size;j++){
                tabuTab[i][j]-=1;
            }
        }
    }


    private void searchingNeighborhood(){
        neighborhoodCost = new ArrayList<>();
        for(int i=0;i<this.size-1;i++){
            for(int j=i+1;j<this.size;j++){
                neighborhoodCost.add(new Double[]{calculateCostChangeOnSwap(i,j),new Double(i),new Double(j)});
            }
        }
        Collections.sort(neighborhoodCost, (Comparator<Double[]>) (d1, d2) -> d1[0].compareTo(d2[0]));

       // for(int i=0;i<bestMovesAmount;i++) {
      //      movesList.add()
       // }


    }



















}
