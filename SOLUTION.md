# Solution Steps

1. Define the TaskCoordinator class and its main method as the application entry point.

2. Set up Java logging with a ConsoleHandler and SimpleFormatter for clear logs.

3. Define a list of five worker nodes as string endpoints.

4. In the 'distributeTasks' method, map each worker node to a distinct task identifier, and log the mapping.

5. Create a WorkerTask class implementing Runnable. WorkerTask takes a worker endpoint and task string, and on 'run()', logs progress, simulates a 2s delay for the HTTP POST (with a possible random exception for demonstration), and logs outcome or errors.

6. In 'distributeTasks', for each worker node, create a new WorkerTask instance, wrap it in a Thread (naming it 'Worker-Node-N'), start it, and collect all threads in a list.

7. After starting all threads, use Thread.join() on each to block until all have completed.

8. Measure total time elapsed from before starting threads to after all threads have joined, and log the total time. Time taken should be ~2 seconds, verifying concurrency.

9. Demonstrate via logs that mapping, per-worker outcome, exceptions, and total elapsed time are all clear and correct.

