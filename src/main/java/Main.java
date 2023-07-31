import java.util.HashMap; import java.util.Map; import java.util.Random;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 1000;
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    String route = generateRoute("RLRFR", 100);
                    int count = countTurns(route);
                    synchronized (sizeToFreq) {
                        if (sizeToFreq.containsKey(count)) {
                            sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                        } else {
                            sizeToFreq.put(count, 1);
                        }
                    }
                    System.out.println(count);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int maxCount = 0;
        int maxFreq = 0;
        Map<Integer, Integer> freqToSize = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            int count = entry.getKey();
            int freq = entry.getValue();
            if (freq > maxFreq) {
                maxCount = count;
                maxFreq = freq;
            }
            freqToSize.putIfAbsent(freq, count);
        }

        System.out.println("Самое частое количество повторений " + maxCount + " (встретилось " + maxFreq + " раз)");
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : freqToSize.entrySet()) {
            int freq = entry.getKey();
            int count = entry.getValue();
            if (count != maxCount) {
                System.out.println("- " + count + " (" + freq + " раз)");
            }
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countTurns(String route) {
        int count = 0;
        for (int i = 0; i < route.length(); i++) {
            if (route.charAt(i) == 'R') {
                count++;
            }
        }
        return count;
    }
}