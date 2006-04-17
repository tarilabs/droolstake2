package org.drools.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.commons.collections.BinaryHeap;

public class BinaryHeapPriorityQueueTest extends TestCase {
    public void testOptimised() {
        Random random = new Random();
        List items = new LinkedList();

        Queue queue = new BinaryHeapFifoQueue( NaturalComparator.INSTANCE,
                                               100000 );

        for ( int i = 0; i < 100000; ++i ) {
            items.add( new LongQueueable( random.nextLong() ) );
        }

        long startEnqueue = System.currentTimeMillis();

        for ( Iterator i = items.iterator(); i.hasNext(); ) {
            queue.enqueue( (Queueable) i.next() );
        }

        long elapsedEnqueue = System.currentTimeMillis() - startEnqueue;

        long startDequeue = System.currentTimeMillis();

        for ( Iterator i = items.iterator(); i.hasNext(); ) {
            ((Queueable) i.next()).dequeue();
        }

        //        while (!queue.isEmpty()) {
        //            queue.dequeue();
        //        }

        long elapsedDequeue = System.currentTimeMillis() - startDequeue;

        System.out.println( "elapsedEnqueue = " + elapsedEnqueue );
        System.out.println( "elapsedDequeue = " + elapsedDequeue );
    }

    public void xxxtestBasic() {
        Random random = new Random();
        List items = new LinkedList();

        BinaryHeap queue = new BinaryHeap();

        for ( int i = 0; i < 100000; ++i ) {
            items.add( new LongQueueable( random.nextLong() ) );
        }

        long startEnqueue = System.currentTimeMillis();

        for ( Iterator i = items.iterator(); i.hasNext(); ) {
            queue.add( i.next() );
        }

        long elapsedEnqueue = System.currentTimeMillis() - startEnqueue;

        long startDequeue = System.currentTimeMillis();

        for ( Iterator i = items.iterator(); i.hasNext(); ) {
            queue.remove( i.next() );
        }

        //        while (!queue.isEmpty()) {
        //            queue.pop();
        //        }

        long elapsedDequeue = System.currentTimeMillis() - startDequeue;

        System.out.println( "elapsedEnqueue = " + elapsedEnqueue );
        System.out.println( "elapsedDequeue = " + elapsedDequeue );
    }
}
