/**
 * 
 */
package tp1ex1.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tp1ex1.ConcurrentQueue;

/**
 * @author gaston
 *
 */
public class ConcurrentQueueTest {
	
	ConcurrentQueue<Integer> queue;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		queue = new ConcurrentQueue<Integer>();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link tp1ex1.ConcurrentQueue#ConcurrentQueue()}.
	 */
	@Test
	public void testConcurrentQueue() {
		queue = new ConcurrentQueue<Integer>();
		assertNotNull(queue);
		assertTrue(queue.size()==0);
	}

	/**
	 * Test method for {@link tp1ex1.ConcurrentQueue#ConcurrentQueue(java.lang.Object)}.
	 * @throws Exception 
	 */
	@Test
	public void testConcurrentQueueT() throws Exception {
		queue = new ConcurrentQueue<Integer>(2);
		assertTrue(queue.size()==1);
		assertNotSame(queue.poll(), new Integer(2));
	}

	/**
	 * Test insert method on queue of size 0
	 * @throws Exception 
	 */
	@Test
	public void testInsertOnEmptyQueue() throws Exception {
		Integer integer = new Integer(4);
		queue.insert(integer);
		assertSame(queue.poll(),integer);
	}
	
	/**
	 * Test insert method on a queue no empty queue
	 * @throws Exception 
	 */
	@Test
	public void testInsertOnEmptyQueue2() throws Exception {
		Integer integer1 = new Integer(4);
		Integer integer2 = new Integer(3);
		Integer integer3 = new Integer(1);
		queue.insert(integer1);
		queue.insert(integer2);
		queue.insert(integer3);
		Integer head = queue.poll();
		assertSame(head,integer1);
	}
	
	/**
	 * Test insert method on a queue no empty queue
	 * @throws Exception 
	 */
	@Test
	public void testInsertOnNoEmptyQueue2() throws Exception {
		queue = new ConcurrentQueue<Integer>(new Integer(4));
		Integer integer2 = new Integer(3);
		queue.insert(integer2);
		Integer integer1 = queue.poll();
		assertNotSame(queue.poll(),integer1);
	}

	/**
	 * Test method for {@link tp1ex1.ConcurrentQueue#poll()}.
	 * @throws Exception 
	 */
	@Test
	public void testPoll() throws Exception {
		Integer integer = new Integer(4);
		queue.insert(integer);
		assertSame(integer,queue.poll());
		
	}
	
	/**
	 * Test insert method on a queue no empty queue
	 * @throws Exception 
	 */
	@Test
	public void testPollOverNotEmp2() throws Exception {
		queue = new ConcurrentQueue<Integer>(new Integer(4));
		Integer head = queue.poll();
		System.out.println(head);
		System.out.println(queue.toString());
		assertEquals(head,new Integer(4));
	}

	/**
	 * Test method for {@link tp1ex1.ConcurrentQueue#size()}.
	 * @throws Exception 
	 */
	@Test
	public void testSize() throws Exception {
		Integer integer = new Integer(4);
		queue.insert(integer);
		queue.insert(integer);
		queue.poll();
		assertEquals("are the same",1,queue.size());
	}
	
	/**
	 * Test method for {@link tp1ex1.ConcurrentQueue#repOk()}.
	 * @throws Exception 
	 */
	@Test
	public void testrepOk() throws Exception {
		assertTrue(queue.repOk());
		Integer integer = new Integer(4);
		queue.insert(integer);
		queue.insert(integer);
		assertTrue(queue.repOk());
		queue.poll();
		assertTrue(queue.repOk());
		queue.poll();
		assertTrue(queue.repOk());
	}

	/**
	 * Test method for {@link tp1ex1.ConcurrentQueue#toString()}.
	 */
	@Test
	public void testToString() {
		assertEquals("a empty queue ",queue.toString(),"[]");
	}
	
	/**
	 * Test last after poll
	 */
	@Test
	public void testLastAfterPoll() {
		Integer integer = new Integer(4);
		queue.insert(integer);
		integer = new Integer(5);
		queue.insert(integer);
		Integer int1 = queue.poll();
		Integer int2 = queue.poll();
		integer = new Integer(6);
		queue.insert(integer);
		queue.poll();
		System.out.println("head: "+queue.getHead()+" last: "+queue.getLast());
		assertEquals(0,queue.size());
		System.out.println(queue.toString());
		assertEquals(integer,queue.getLast());
	}
	
	/**
	 * Test last after poll
	 */
	@Test
	public void testLastAfterPoll2() {
		Integer integer = new Integer(4);
		queue.insert(integer);
		integer = new Integer(5);
		queue.insert(integer);
		Integer int1 = queue.poll();
		Integer int2 = queue.poll();
		integer = new Integer(6);
		queue.insert(integer);
		queue.poll();
		integer = new Integer(5);
		queue.insert(integer);
		System.out.println("head: "+queue.getHead()+" last: "+queue.getLast());
		assertEquals(1,queue.size());
		System.out.println(queue.toString());
		assertEquals(queue.getHead(),queue.getLast());
	}
	
	

}
