package edu.asupoly.aspira.gui;

import java.sql.Time;
import java.util.ArrayList;

public class MedicationSchedule {
	
	private String name;
	private ArrayList<Time> schedule;
	private int doses;
	
	public MedicationSchedule(String name, ArrayList<Time> schedule, String doses)
	{
		this.name = name;
		this.schedule = schedule;
		this.doses = Integer.parseInt(doses);
	}
	
	public MedicationSchedule(String name, String doses)
	{
		this.name = name;
		schedule = new ArrayList<Time>();
		this.doses = Integer.parseInt(doses);
	}
	
	public String getName()
	{
		return name;
	}
	
	public ArrayList<Time> getSchedule()
	{
		return schedule;
	}
	
	public int getDoses()
	{
		return doses;
	}
	
	public void addTimeToSchedule(Time time)
	{
		schedule.add(time);
	}
	
	public boolean equals(MedicationSchedule x)
	{
		boolean isEqual = false;
		
		if( name.equals(x.getName()) )
			isEqual = true;
		
		return isEqual;
	}

}
