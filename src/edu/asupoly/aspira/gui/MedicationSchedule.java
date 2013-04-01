package edu.asupoly.aspira.gui;

import java.sql.Time;
import java.util.ArrayList;

public class MedicationSchedule {
	
	private String name;
	private String frequency;
	private int doses;
	
	public MedicationSchedule(String name, String frequency, String doses)
	{
		this.name = name;
		this.frequency = frequency;
		this.doses = Integer.parseInt(doses);
	}
	
	public MedicationSchedule(String name)
	{
		this.name = name;
		frequency = null;
		doses = 0;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getFrequency()
	{
		return frequency;
	}
	
	public int getDoses()
	{
		return doses;
	}
	
	public boolean equals(MedicationSchedule x)
	{
		boolean isEqual = false;
		
		if( name.equals(x.getName()) )
			isEqual = true;
		
		return isEqual;
	}

}
