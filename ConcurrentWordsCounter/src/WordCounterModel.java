import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WordCounterModel {
    private final ConcurrentHashMap<String, AtomicInteger> wordCountMap = new ConcurrentHashMap<>();

    public void incrementWord(String word) {
        wordCountMap.computeIfAbsent(word, w -> new AtomicInteger(0)).incrementAndGet();
    }

    public Map<String, AtomicInteger> getWordCountMap() {
        return wordCountMap;
    }
}