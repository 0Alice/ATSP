import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    private static List<ATSP> atspList = new ArrayList<>();

    private static void readAllFiles(final String folderPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(Main::readFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Read done");
    }

    private static void readFile(Path path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path.toString())));
            String name = br.readLine().split(":")[1].replaceAll("\\s", "");
            omitLine(br, 2);
            int size = Integer.parseInt(br.readLine().split(":")[1].replaceAll("\\s", ""));
            omitLine(br, 3);
            ATSP atspRandom = new AtspSimpleHeuristic(name, size, name.contains("ftv") ? readftv(br, size) : readOther(br, size));//,1000000000);
            atspList.add(atspRandom);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void omitLine(BufferedReader br, int amount) {
        for (int i = 0; i < amount; i++) {
            try {
                br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static Double[][] readftv(BufferedReader br, int size) throws IOException {
        Double[][] completeMatrix = new Double[size][size];
        int mainCounter = 0;
        int counter = 0;
        Double[] matrix = new Double[size];
        while (mainCounter < size) {

            String singleLine = br.readLine();
            String tabSingleLine[] = singleLine.split("\\s");
            for (String element : tabSingleLine) {
                if (element.equals("EOF")) {
                    completeMatrix[mainCounter] = matrix;
                    return completeMatrix;
                }
                if (!element.equals("")) {
                    if (counter == size) {
                        completeMatrix[mainCounter] = matrix;
                        matrix = new Double[size];
                        mainCounter++;
                        counter = 0;
                    }
                    matrix[counter] = Integer.parseInt(element)!=0?new Double(Integer.parseInt(element)):Double.POSITIVE_INFINITY;
                    counter++;
                }
            }
        }
        return completeMatrix;
    }

    private static Double[][] readOther(BufferedReader br, int size) throws IOException {
        int mainCounter = 0;
        Double[][] completeMatrix = new Double[size][size];
        while (mainCounter < size) {
            int counter = 0;
            Double[] matrix = new Double[size];
            while (counter < size) {
                String singleLine = br.readLine();
                String tabSingleLine[] = singleLine.split("\\s");
                for (String element : tabSingleLine) {
                    if (!element.equals("")) {
                        matrix[counter] = Integer.parseInt(element)!=0?new Double(Integer.parseInt(element)):Double.POSITIVE_INFINITY;
                        counter++;
                    }
                }
            }
            completeMatrix[mainCounter] = matrix;
            mainCounter++;
        }
        return completeMatrix;
    }


    public static void main(String[] args) {
        //Scanner in = new Scanner(System.in);
        //readAllFiles(in.nextLine());
        readAllFiles(args[0]);
        for (ATSP atsp : atspList) {
            atsp.solve();
            //atsp.multisolveWithMileagePrints(1);
        }
        //"atsp"
        //"test1"
    }
}
