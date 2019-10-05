package edu.uci.ics.huymt2.service.api_gateway.threadpool;

import edu.uci.ics.huymt2.service.api_gateway.logger.ServiceLogger;

public class ThreadPool {
    private int numWorkers;
    private Worker[] workers;
    private ClientRequestQueue queue;

    public ThreadPool(int numWorkers) {
        this.numWorkers = numWorkers;
        workers = new Worker[numWorkers];
        queue = new ClientRequestQueue();

        ServiceLogger.LOGGER.info("Number of worker: "+numWorkers);
        ServiceLogger.LOGGER.info("ThreadPool:: creating workers...");

        for (int i = 0; i < numWorkers; ++i){
            workers[i] = Worker.CreateWorker(i, this);
            ServiceLogger.LOGGER.info("Successfully created worker: "+(i+1));
            workers[i].start();
        }
    }

    public void add (ClientRequest clientRequest) {
        queue.enqueue(clientRequest);
    }

    public ClientRequest remove() {
        return queue.dequeue();
    }

    public ClientRequestQueue getQueue() {
        if (queue.isEmpty())
            return null;
        return queue;
    }
}
