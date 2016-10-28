package csc258comp.debugger;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import csc258comp.runner.Machine;


public final class CopyOnWriteMemoryTest {
	
	private CopyOnWriteMemory m;
	
	
	
	@Before
	public void setUp() {
		m = new CopyOnWriteMemory();
	}
	
	
	
	@Test
	public void testInitiallyBlank() {
		Random r = new Random();
		for (int i = 0; i < 1000; i++) {
			int addr = r.nextInt(Machine.ADDRESS_SPACE_SIZE);
			assertEquals(0, m.get(addr));
		}
	}
	
	
	@Test
	public void testSetGet() {
		long seed = System.currentTimeMillis() + System.nanoTime();
		Map<Integer,Integer> values = new HashMap<Integer,Integer>();
		
		Random r = new Random(seed);
		for (int i = 0; i < 30000; i++) {
			int val = r.nextInt();
			int addr = r.nextInt(Machine.ADDRESS_SPACE_SIZE);
			m.set(addr, val);
			values.put(addr, val);
		}
		
		r = new Random(seed);
		for (int i = 0; i < 30000; i++) {
			r.nextInt();
			int addr = r.nextInt(Machine.ADDRESS_SPACE_SIZE);
			assertEquals((int)values.get(addr), m.get(addr));
		}
		for (int i = 0; i < 30000; i++) {
			int addr = r.nextInt(Machine.ADDRESS_SPACE_SIZE);
			if (values.containsKey(addr))
				assertEquals((int)values.get(addr), m.get(addr));
			else
				assertEquals(0, m.get(addr));
		}
	}
	
	
	
	@After
	public void tearDown() {
		m = null;
	}
	
}
