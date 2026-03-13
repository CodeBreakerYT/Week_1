import java.util.*;

public class PlagiarismDetector {

    // nGram -> set of document IDs
    private HashMap<String, Set<String>> nGramIndex = new HashMap<>();

    // documentId -> document text
    private HashMap<String, String> documents = new HashMap<>();

    private int N = 5; // 5-gram

    // Add document to database
    public void addDocument(String docId, String text) {

        documents.put(docId, text);

        List<String> ngrams = generateNGrams(text);

        for (String gram : ngrams) {

            nGramIndex.putIfAbsent(gram, new HashSet<>());
            nGramIndex.get(gram).add(docId);
        }
    }

    // Generate n-grams
    private List<String> generateNGrams(String text) {

        String[] words = text.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            grams.add(gram.toString().trim());
        }

        return grams;
    }

    // Analyze document for plagiarism
    public void analyzeDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (nGramIndex.containsKey(gram)) {

                for (String existingDoc : nGramIndex.get(gram)) {

                    matchCount.put(existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Matches with " + doc + ": " +
                    matches + " n-grams");

            System.out.println("Similarity: " +
                    String.format("%.2f", similarity) + "%");

            if (similarity > 60) {
                System.out.println("PLAGIARISM DETECTED");
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector system = new PlagiarismDetector();

        system.addDocument("essay_089",
                "Artificial intelligence is transforming the modern world with advanced technology");

        system.addDocument("essay_092",
                "Artificial intelligence is transforming the modern world with powerful algorithms");

        String newEssay =
                "Artificial intelligence is transforming the modern world with advanced technology and innovation";

        system.analyzeDocument("essay_123", newEssay);
    }
}