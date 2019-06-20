package dailytradereportingengine.constants;

public enum ArabicWorkingDays {

	SUNDAY(1,true),
	MONDAY(2,true),
	TUESDAY(3,true),
	WEDNESDAY(4,true),
	THURSDAY(5,true),
	FRIDAY(6,false),
	SATURDAY(7,false);
	
	
	private ArabicWorkingDays(int dayOfWeek, boolean workingDay){
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
		
		for (ArabicWorkingDays day : ArabicWorkingDays.values()) {
			if(day.getDayOfWeek() == dayOfWeek) {
				return day.isWorkingDay();
			}
		}
		
		return false;
		
	}
}
