import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TaskCoordinator {
    private static final Logger logger = Logger.getLogger(TaskCoordinator.class.getName());

    // Simulate 5 worker node endpoints
    private static final String[] WORKER_NODES = {
            "worker-node-1.example.com",
            "worker-node-2.example.com",
            "worker-node-3.example.com",
            "worker-node-4.example.com",
            "worker-node-5.example.com"
    };

    public static void main(String[] args) {
        configureLogger();
        TaskCoordinator coordinator = new TaskCoordinator();
        coordinator.distributeTasks();
    }

    private static void configureLogger() {
        Logger rootLogger = Logger.getLogger("");
        for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.INFO);
        rootLogger.addHandler(handler);
        rootLogger.setLevel(Level.INFO);
    }

    public void distributeTasks() {
        List<Thread> threads = new ArrayList<>();
        Map<String, String> workerToTask = new HashMap<>();
        
        // Map each worker node to its task
        for (int i = 0; i < WORKER_NODES.length; i++) {
            String worker = WORKER_NODES[i];
            String task = "Task-" + (i + 1);
            workerToTask.put(worker, task);
        }

        logger.info("Worker to Task Mapping: " + workerToTask);

        long start = System.currentTimeMillis();
        
        // Start one thread per worker node
        for (int i = 0; i < WORKER_NODES.length; i++) {
            final String worker = WORKER_NODES[i];
            final String task = workerToTask.get(worker);
            Runnable workerJob = new WorkerTask(worker, task);
            Thread thread = new Thread(workerJob, "Worker-Node-" + (i + 1));
            threads.add(thread);
            thread.start();
        }

        // Wait for all worker threads to complete
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                logger.severe("Interrupted while waiting for " + t.getName());
                Thread.currentThread().interrupt();
            }
        }

        long end = System.currentTimeMillis();
        logger.info("All tasks distributed. Total time elapsed: " + (end - start)/1000.0 + " seconds");
    }

    // Simulates a worker node accepting and processing the assigned task
    static class WorkerTask implements Runnable {
        private final String workerEndpoint;
        private final String task;
        private static final Logger threadLogger = Logger.getLogger(TaskCoordinator.class.getName());

        public WorkerTask(String workerEndpoint, String task) {
            this.workerEndpoint = workerEndpoint;
            this.task = task;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            threadLogger.info("[" + threadName + "] Sending task '" + task + "' to worker '" + workerEndpoint + "'");
            try {
                // Simulate HTTP POST with 2s delay
                simulateHttpPost();
                threadLogger.info("[" + threadName + "] Task '" + task + "' successfully assigned to '" + workerEndpoint + "'.");
            } catch (Exception e) {
                threadLogger.severe("[" + threadName + "] Failed to assign task '" + task + "' to worker '" + workerEndpoint + "': " + e);
            }
        }

        private void simulateHttpPost() throws Exception {
            // Randomly simulate an exception with small probability (optional)
            if (ThreadLocalRandom.current().nextDouble() < 0.2) { // 20% chance
                throw new RuntimeException("Simulated network timeout");
            }
            Thread.sleep(2000); // Simulate network delay
        }
    }
}
