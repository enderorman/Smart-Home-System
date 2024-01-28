import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class InputHelper {
    public static List<String> readFile(String filePath) {
        try {
            Path path= Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);
            List<String> fixedLines = new ArrayList<String>();
            for (String line: lines){
                if (line.trim().equals("")){
                    continue;
                } fixedLines.add(line);
            }

            return fixedLines;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getCommands(String filePath){
        List<String> lines = readFile(filePath);
        int height = lines.size();
        String[] commands = new String[height];
        for (int i = 0; i < height; i++){
            String line = lines.get(i);
            commands[i] = line;
        }
        return commands;
    }

}
