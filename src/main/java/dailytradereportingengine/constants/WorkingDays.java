package dailytradereportingengine.constants;

public enum WorkingDays {

	SUNDAY(1,false),
	MONDAY(2,true),
	TUESDAY(3,true),
	WEDNESDAY(4,true),
	THURSDAY(5,true),
	FRIDAY(6,true),
	SATURDAY(7,false);
	
	
	private WorkingDays(int dayOfWeek, boolean workingDay){
		this.dayOfWeek = dayOfWeek;
		this.workingDay = workingDay;
	}

	private int dayOfWeek;
	private boolean workingDay;
	
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public boolean isWorkingDay() {
		return workingDay;
	}

	public void setWorkingDay(boolean workingDay) {
		this.workingDay = workingDay;
	}
	public static boolean isWorkingDay(int dayOfWeek) {
		
		for (WorkingDays day : WorkingDays.values()) {
			if(day.getDayOfWeek() == dayOfWeek) {
				return day.isWorkingDay();
			}
		}
		
		return false;
		
	}
}
