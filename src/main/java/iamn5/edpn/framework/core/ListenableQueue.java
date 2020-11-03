package iamn5.edpn.framework.core;

import java.util.*;

public class ListenableQueue<T> extends AbstractQueue<T> {
    private final Queue<T> queue;
    private final List<IListener<T>> listeners = new ArrayList<>();

    public ListenableQueue(Queue<T> queue) {
        this.queue = queue;
    }

    public void addListener(IListener<T> listener) {
        listeners.add(listener);
    }

    @Override
    public boolean offer(T e) {
        if (queue.offer(e)) {
            listeners.forEach(listener -> listener.onElementAdded(e));
            return true;
        }

        return false;
    }

    @Override
    public T poll() {
        return queue.poll();
    }

    @Override
    public T peek() {
        return queue.peek();
    }

    @Override
    public Iterator<T> iterator() {
        return queue.iterator();
    }

    @Override
    public int size() {
        return queue.size();
    }
}
