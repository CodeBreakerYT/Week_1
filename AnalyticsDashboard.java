import java.util.*;

class PageViewEvent {
    String url;
    String userId;
    String source;

    public PageViewEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class AnalyticsDashboard {

    // pageUrl -> visit count
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique users
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process incoming event
    public void processEvent(PageViewEvent event) {

        // Count page views
        pageViews.put(event.url, pageViews.getOrDefault(event.url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        // Track traffic source
        trafficSources.put(event.source,
                trafficSources.getOrDefault(event.source, 0) + 1);
    }

    // Get top 10 pages
    public List<String> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        List<String> result = new ArrayList<>();

        int count = 0;

        while (!pq.isEmpty() && count < 10) {

            Map.Entry<String, Integer> entry = pq.poll();

            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            result.add(page + " - " + views +
                    " views (" + unique + " unique)");

            count++;
        }

        return result;
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("Top Pages:");

        List<String> topPages = getTopPages();

        for (int i = 0; i < topPages.size(); i++) {
            System.out.println((i + 1) + ". " + topPages.get(i));
        }

        System.out.println("\nTraffic Sources:");

        for (String source : trafficSources.keySet()) {
            System.out.println(source + " → " + trafficSources.get(source));
        }
    }

    public static void main(String[] args) {

        AnalyticsDashboard dashboard = new AnalyticsDashboard();

        dashboard.processEvent(new PageViewEvent(
                "/article/breaking-news", "user_123", "google"));

        dashboard.processEvent(new PageViewEvent(
                "/article/breaking-news", "user_456", "facebook"));

        dashboard.processEvent(new PageViewEvent(
                "/sports/championship", "user_789", "google"));

        dashboard.processEvent(new PageViewEvent(
                "/article/breaking-news", "user_123", "direct"));

        dashboard.getDashboard();
    }
}