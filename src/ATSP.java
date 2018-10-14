import java.util.Arrays;
import java.util.Random;

public abstract class ATSP {
    private String name;
    protected int size;
    private double avgTime;
    private double avgSolution;
    private int bestSolution;
    private int worstSolution;
    private int matrix[][];

    public ATSP(String name, int size, int matrix[][]) {
        this.name = name;
        this.size = size;
        this.matrix = matrix;

    }

    protected int[] generateRandomPermutations(int permutation[]) {
        Random generator = new Random();
        for(int i=this.size-1;i>0;i--){
            int help=permutation[i];
            int randomArg=generator.nextInt(i+1);
            permutation[i]=permutation[randomArg];
            permutation[randomArg]=help;
        }
        return permutation;
    }

    public void solve(){
        long minTime=10*1000000000;//1 secound
        int minL=10;
        int[] result=new int[this.size];
        double l=0;
        long sumOfSolutions=0;
        long startTime = System.nanoTime();
        do{
            this.algorithm(result);
            for(int i=0;i<this.size-1;i++){
                sumOfSolutions+=this.matrix[result[i]][result[i+1]];
            }
            sumOfSolutions+=this.matrix[result[this.size-1]][result[0]];
            l++;
        }while(l<minL||System.nanoTime() - startTime<minTime);
        long estimatedTime = System.nanoTime() - startTime;
        this.avgTime=estimatedTime/l;
        this.avgSolution=sumOfSolutions/l;
        System.out.println(this.avgTime+" nanoseconds");
        System.out.println(this.avgSolution);
    }

    abstract void algorithm(int result[]);
}
