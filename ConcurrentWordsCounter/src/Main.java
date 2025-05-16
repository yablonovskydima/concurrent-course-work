import java.util.Map;

public class Main {
    public static void main(String[] args)   {
        String[] files = {
                "testData/file1.txt",
                "testData/file2.txt",
                "testData/file3.txt",
                "testData/file4.txt",
                "testData/file5.txt"
        };
        WordCounterModel model = new WordCounterModel();
        FileController controller = new FileController(model);
        Map<String, Integer> map = controller.run(files);
        map.forEach((w, c) -> System.out.println("Word: " + w + " Count: " + c));
    }
}