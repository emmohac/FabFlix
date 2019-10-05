package edu.uci.ics.huymt2.service.api_gateway.threadpool;

import edu.uci.ics.huymt2.service.api_gateway.logger.ServiceLogger;

import java.util.List;

public class ClientRequestQueue {
    private ListNode head;
    private ListNode tail;

    public ClientRequestQueue() {
        head = tail = null;
    }

    public synchronized void enqueue(ClientRequest clientRequest) {
//        if (isEmpty())
//            this.notifyAll();
//
//        ListNode newNode = new ListNode(clientRequest, null);
//        if (tail == null)
//            head = tail = newNode;
//        else{
//            tail.setNext(newNode);
//            tail = tail.getNext();
//        }
        if (head == null && tail == null)
            head = tail = new ListNode(clientRequest, null);
        else {
            tail.setNext(new ListNode(clientRequest, null));
            tail = tail.getNext();
        }
        this.notify();
    }

    public synchronized ClientRequest dequeue() {
        if (head == null){
            try{
                this.wait();
            }catch (InterruptedException e){
                ServiceLogger.LOGGER.info("Exception");
            }
        }

        ClientRequest toReturn = head.getClientRequest();
        if (head == tail)
            head = tail = null;
        else
            head = head.getNext();

        return toReturn;
    }

    boolean isEmpty() {
        return head == null;
    }
}
