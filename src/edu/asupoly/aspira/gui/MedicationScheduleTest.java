package edu.asupoly.aspira.gui;

import static org.junit.Assert.*;

import org.junit.Test;

public class MedicationScheduleTest {

	@Test
	public void test() {
		MedicationSchedule dinamizine1= new MedicationSchedule("dinamizine", "0");
		MedicationSchedule dinamizine2 = new MedicationSchedule("dinamizine", "1");
		MedicationSchedule madeupipran = new MedicationSchedule("madeupipran","2");
		
		assertTrue(dinamizine1.equals(dinamizine2));
		assertFalse(dinamizine1.equals(madeupipran));
	}

}
