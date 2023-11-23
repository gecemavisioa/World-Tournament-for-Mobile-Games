package com.example.demo.util;

import com.example.demo.exception.QueueException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserQueueTest {
    private UserQueue userQueue = new UserQueue();

    @Test
    public void UserQueueTest_InsertLastUser_ReturnsListUserIds() {
        List<Queue<Integer>> queue = userQueue.getQueues();
        queue.get(0).add(1);
        queue.get(1).add(2);
        queue.get(2).add(3);
        queue.get(3).add(4);
        userQueue.setQueues(queue);

        List<Integer> returnedValue = userQueue.addQueue(5, 4);

        assertEquals(Arrays.asList(1, 2, 3, 4, 5), returnedValue);
    }

    @Test
    public void UserQueueTest_InsertEmptyCountryList_ThrowsQueueException() {
        QueueException exception = assertThrows(QueueException.class, () -> {
            userQueue.addQueue(1, 1);
        });

        assertEquals("Waiting for other players!", exception.getMessage());
    }

    @Test
    public void UserQueueTest_AppendToCountryList_ThrowsQueueException() {
        List<Queue<Integer>> queue = userQueue.getQueues();
        queue.get(1).add(1);
        userQueue.setQueues(queue);

        QueueException exception = assertThrows(QueueException.class, () -> {
            userQueue.addQueue(2, 1);
        });

        assertEquals("Waiting for other players!", exception.getMessage());
    }
}
