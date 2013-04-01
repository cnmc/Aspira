package edu.asupoly.aspira.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.asupoly.aspira.gui.MedicationSchedule;

public class MedicationScheduleTest {

	@Test
	public void test() {
		MedicationSchedule dinamizine1= new MedicationSchedule("dinamizine", "0", "1");
		MedicationSchedule dinamizine2 = new MedicationSchedule("dinamizine", "1", "1");
		MedicationSchedule madeupipran = new MedicationSchedule("madeupipran","2", "1");
		
		assertTrue(dinamizine1.equals(dinamizine2));
		assertFalse(dinamizine1.equals(madeupipran));
	}

}
