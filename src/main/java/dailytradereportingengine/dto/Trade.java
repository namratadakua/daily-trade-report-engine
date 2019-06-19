/**
 * 
 */
package dailytradereportingengine.dto;

import java.util.Date;

/**
 * @author namratadakua
 *
 */
public class Trade implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String entity;
	
	private char instructionType;
	
	private double agreedFx;
	
	private String currency;
	
	private Date instructionDate;
	
	private Date settlmentDate;
	
	private long units;
	
	private double pricePerUnit;
	
	private Date settledDate;
	
	private int rank;
	
	private double usdSettledAmount;
	
	public Trade(String entity, char instructionType, double agreedFx, String currency, Date instructionDate,
			Date settlmentDate, long units, double pricePerUnit) {
		super();
		this.entity = entity;
		this.instructionType = instructionType;
		this.agreedFx = agreedFx;
		this.currency = currency;
		this.instructionDate = instructionDate;
		this.settlmentDate = settlmentDate;
		this.units = units;
		this.pricePerUnit = pricePerUnit;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public char getInstructionType() {
		return instructionType;
	}

	public void setInstructionType(char instructionType) {
		this.instructionType = instructionType;
	}

	public double getAgreedFx() {
		return agreedFx;
	}

	public void setAgreedFx(double agreedFx) {
		this.agreedFx = agreedFx;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getInstructionDate() {
		return instructionDate;
	}

	public void setInstructionDate(Date instructionDate) {
		this.instructionDate = instructionDate;
	}

	public Date getSettlmentDate() {
		return settlmentDate;
	}

	public void setSettlmentDate(Date settlmentDate) {
		this.settlmentDate = settlmentDate;
	}

	public long getUnits() {
		return units;
	}

	public void setUnits(long units) {
		this.units = units;
	}

	public double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public Date getSettledDate() {
		return settledDate;
	}

	public void setSettledDate(Date settledDate) {
		this.settledDate = settledDate;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public double getUsdSettledAmount() {
		return usdSettledAmount;
	}

	public void setUsdSettledAmount(double usdSettledAmount) {
		this.usdSettledAmount = usdSettledAmount;
	}
	
}
