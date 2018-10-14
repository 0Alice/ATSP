import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Main {


    private static List readAllFiles(final String folderPath) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(Main::readFile);
        }
        //TODO
        return  null;
    }

    private static void readFile(Path path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path.toString())));
            String name = br.readLine().split(":")[1].replaceAll("\\s", "");
            omitLine(br, 2);
            int size = Integer.parseInt(br.readLine().split(":")[1].replaceAll("\\s", ""));
            omitLine(br, 3);
            //System.out.println("NAME " + name);
            //System.out.println(br.readLine());
            //////////////////////////////////
            AtspRandom atspRandom = new AtspRandom(name, size, name.contains("ftv") ? readftv(br, size) : readOther(br, size));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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


    private static int[][] readftv(BufferedReader br, int size) {
        //TODO
        return new int[0][];
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


    public static void main(String[] args) throws IOException {
        List atspList = readAllFiles("D:\\__studia2\\2\\MIOB\\ATSP\\atsp");
    }
}
