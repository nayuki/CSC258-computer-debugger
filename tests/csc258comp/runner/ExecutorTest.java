package csc258comp.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public final class ExecutorTest {
	
	private Machine m;
	
	
	
	@Before
	public void setUp() {
		m = new BasicMachine(new NullInputStream(true), new NullOutputStream(true));
		m.setProgramCounter(0);
		m.setAccumulator(0);
		m.setConditionCode(false);
		m.setHalted(false);
	}
	
	
	
	@Test
	public void testLda0() {
		m.setAccumulator(31415);
		m.setMemoryAt(1, 92653);  // Data
		m.setMemoryAt(0, 0x00000001);  // Instruction
		Executor.step(m);
		assertEquals(1, m.getProgramCounter());
		assertEquals(92653, m.getAccumulator());
		assertFalse(m.getConditionCode());
		assertFalse(m.isHalted());
	}
	
	
	@Test
	public void testLda1() {
		m.setAccumulator(27182);
		m.setConditionCode(true);
		m.setMemoryAt(1, 81828);  // Data
		m.setMemoryAt(0, 0x00000001);  // Instruction
		Executor.step(m);
		assertEquals(1, m.getProgramCounter());
		assertEquals(81828, m.getAccumulator());
		assertTrue(m.getConditionCode());
		assertFalse(m.isHalted());
	}
	
	
	@Test
	public void testSta0() {
		m.setAccumulator(14142);
		m.setMemoryAt(1, 13562);  // Data
		m.setMemoryAt(0, 0x01000001);  // Instruction
		Executor.step(m);
		assertEquals(14142, m.getMemoryAt(1));
		assertEquals(14142, m.getAccumulator());
		assertEquals(1, m.getProgramCounter());
		assertFalse(m.getConditionCode());
		assertFalse(m.isHalted());
	}
	
	
	@Test
	public void testSta1() {
		m.setAccumulator(16180);
		m.setConditionCode(true);
		m.setMemoryAt(1, 33988);  // Data
		m.setMemoryAt(0, 0x01000001);  // Instruction
		Executor.step(m);
		assertEquals(16180, m.getMemoryAt(1));
		assertEquals(1, m.getProgramCounter());
		assertEquals(16180, m.getAccumulator());
		assertTrue(m.getConditionCode());
		assertFalse(m.isHalted());
	}
	
	
	@Test
	public void testAdd() {
		m.setAccumulator(1);
		m.setMemoryAt(1, 2);  // Data
		m.setMemoryAt(0, 0x02000001);  // Instruction
		Executor.step(m);
		assertEquals(1, m.getProgramCounter());
		assertEquals(3, m.getAccumulator());
		assertFalse(m.getConditionCode());
		assertFalse(m.isHalted());
	}
	
	
	@Test
	public void testAddPositiveOverflow() {
		m.setAccumulator(0x40012340);
		m.setMemoryAt(1, 0x40005607);  // Data
		m.setMemoryAt(0, 0x02000001);  // Instruction
		Executor.step(m);
		assertEquals(1, m.getProgramCounter());
		assertEquals(0x80017947, m.getAccumulator());
		assertTrue(m.getConditionCode());
		assertFalse(m.isHalted());
	}
	
	
	@Test
	public void testAddNegativeOverflow() {
		m.setAccumulator(0x80000000);
		m.setMemoryAt(1, -1);  // Data
		m.setMemoryAt(0, 0x02000001);  // Instruction
		Executor.step(m);
		assertEquals(1, m.getProgramCounter());
		assertEquals(0x7FFFFFFF, m.getAccumulator());
		assertTrue(m.getConditionCode());
		assertFalse(m.isHalted());
	}
	
	
	
	@After
	public void tearDown() {
		m = null;
	}
	
}
