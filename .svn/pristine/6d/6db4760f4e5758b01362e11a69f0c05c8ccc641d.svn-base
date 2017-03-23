package com.cloudmachine.pool;

public abstract class PriorityRunnable implements Runnable, Comparable {
    private int priority;

    public PriorityRunnable(int priority) {
        if (priority == 0)
            throw new IllegalArgumentException();
        this.priority = priority;
    }

    public int compareTo(PriorityRunnable another) {
        int my = this.getPriority();
        int other = another.getPriority();
        if(other > my)
        	return 1;
        if(other == my)
        	return 0;
        if(other < my)
        	return -1;
        return 0;
//        return my 1 : my > other ? -1 : 0;
    }

    @Override
    public void run() {
        doSth();
    }

    public abstract void doSth();

    public int getPriority() {
        return priority;
    }
}
