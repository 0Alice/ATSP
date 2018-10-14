public abstract class ATSP {
    private String name;
    private int size;
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

    private void generateRandomPermutations() {
        //TODO
    }

    public void solve(){
        //TODO
    }

    abstract void algorithm();
}
