import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    private static List<ATSP> atspList = new ArrayList<>();
    private static String type;

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

            ATSP atsp=getAtspType(name,size,br);
            //ATSP atsp = new AtspGreedy(name, size, name.contains("ftv") ? readftv(br, size) : readOther(br, size));
            atspList.add(atsp);

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


    private static int[][] readftv(BufferedReader br, int size) throws IOException {
        int[][] completeMatrix = new int[size][size];
        int mainCounter = 0;
        int counter = 0;
        int[] matrix = new int[size];
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
                        matrix = new int[size];
                        mainCounter++;
                        counter = 0;
                    }
                    matrix[counter] = Integer.parseInt(element);
                    counter++;
                }
            }
        }
        return completeMatrix;
    }

    private static int[][] readOther(BufferedReader br, int size) throws IOException {
        int mainCounter = 0;
        int[][] completeMatrix = new int[size][size];
        while (mainCounter < size) {
            int counter = 0;
            int[] matrix = new int[size];
            while (counter < size) {
                String singleLine = br.readLine();
                String tabSingleLine[] = singleLine.split("\\s");
                for (String element : tabSingleLine) {
                    if (!element.equals("")) {
                        matrix[counter] = Integer.parseInt(element);
                        counter++;
                    }
                }
            }
            completeMatrix[mainCounter] = matrix;
            mainCounter++;
        }
        return completeMatrix;
    }


    private static ATSP getAtspType(String name,int size,BufferedReader br) throws IOException {
        ATSP atsp=null;
    switch(type.toLowerCase()){
        case "greedy":
            atsp=new AtspGreedy(name, size, name.contains("ftv") ? readftv(br, size) : readOther(br, size));
        case "steepest":
            //TODO
        case "random":
            //TODO
        default:
            break;

    }
    return atsp;
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String tab[];
        tab=in.nextLine().split(" ");
        readAllFiles(tab[0]);
        type=tab[1];



        for (ATSP atsp : atspList) {
            atsp.solve();
        }
        //"D:\\__studia2\\2\\MIOB\\ATSP\\atsp"
        //"E:\\PROJECTS\\ATSP\\atsp"
    }
}
