package com.example.demo.util;

import com.example.demo.exception.QueueException;

import java.util.*;

public class UserQueue {

    // Set holding people there is in queue
    // only mutated tournament service that shares the same lock
    private Set<Integer> inQueue;

    // queues for each country
    private List<Queue<Integer>> queues;

    public UserQueue() {
        queues = new ArrayList<>();
        inQueue = new HashSet<>();
        for(int i = 0; i < 5; i++) {
            queues.add(new PriorityQueue<>());
        }
    }

    public synchronized List<Integer> addQueue(int userId, int countryIdx) {
        // checking if there are users of corresponding country's queue
        boolean wasEmpty = queues.get(countryIdx).isEmpty();
        queues.get(countryIdx).add(userId);

        // if there are users throws waiting for other players exception
        if (!wasEmpty) throw new QueueException("Waiting for other players!");

        // check other queues to see if there are users
        boolean allHas = true;

        for (Queue<Integer> q : queues) {
            if (q.isEmpty()) {
                allHas = false;
                break;
            }
        }

        // If one or more of the queues is empty throws waiting for other players exception
        if (!allHas) throw new QueueException("Waiting for other players!");

        // Otherwise forms the group
        List<Integer> group = new ArrayList<>(5);
        for (Queue<Integer> x : queues) {
            // Removes from queues
            group.add(x.poll());
        }

        return group;
    }

    public void addToInQueue(int id) {
        inQueue.add(id);
    }

    public void removeFromInQueue(int id) {
        inQueue.remove(id);
    }

    public List<Queue<Integer>> getQueues() {
        return queues;
    }

    public void setQueues(List<Queue<Integer>> queues) {
        this.queues = queues;
    }

    public Set<Integer> getInQueue() {
        return inQueue;
    }

    public void setInQueue(Set<Integer> inQueue) {
        this.inQueue = inQueue;
    }
}
