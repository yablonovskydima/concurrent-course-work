import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileController {
    private final WordCounterModel model;
    private static final Pattern WORD = Pattern.compile("\\b\\p{L}+\\b");

    public FileController(WordCounterModel model) {
        this.model = model;
    }

    public Map<String, Integer> run(String[] files){
        ExecutorService executorService = Executors.newFixedThreadPool(files.length);
        for (String path : files) {
            executorService.submit(() -> processFile(path));
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                System.err.println("Execution time exceeded");
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }

        Map<String, Integer> result = new HashMap<>();
        model.getWordCountMap().forEach((word, count) -> result.put(word, count.get()));
        return result;
    }

    private void processFile(String filepath){
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line, word;
            Matcher matcher;
            while ((line = reader.readLine()) != null) {
                matcher = WORD.matcher(line.toLowerCase());
                while (matcher.find()) {
                    word = matcher.group();
                    if (word.length() > 1 && word.charAt(0) == word.charAt(word.length() - 1)) {
                        model.incrementWord(word);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error while reading file: " + filepath);
        }
    }
}