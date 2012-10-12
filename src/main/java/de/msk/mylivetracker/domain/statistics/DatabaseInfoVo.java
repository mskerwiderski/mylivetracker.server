package de.msk.mylivetracker.domain.statistics;

/**
 * DatabaseInfoVo.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-10-12
 * 
 */
public class DatabaseInfoVo {

	private String tableSchema;
	private Double sizeDataInMb;
	private Double sizeIndexInMb;
	private Double sizeOverallInMb;
	private Double sizeFreeInMb;

	public String getTableSchema() {
		return tableSchema;
	}
	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}
	public Double getSizeDataInMb() {
		return sizeDataInMb;
	}
	public void setSizeDataInMb(Double sizeDataInMb) {
		this.sizeDataInMb = sizeDataInMb;
	}
	public Double getSizeIndexInMb() {
		return sizeIndexInMb;
	}
	public void setSizeIndexInMb(Double sizeIndexInMb) {
		this.sizeIndexInMb = sizeIndexInMb;
	}
	public Double getSizeOverallInMb() {
		return sizeOverallInMb;
	}
	public void setSizeOverallInMb(Double sizeOverallInMb) {
		this.sizeOverallInMb = sizeOverallInMb;
	}
	public Double getSizeFreeInMb() {
		return sizeFreeInMb;
	}
	public void setSizeFreeInMb(Double sizeFreeInMb) {
		this.sizeFreeInMb = sizeFreeInMb;
	}
	@Override
	public String toString() {
		return "DatabaseInfoVo [tableSchema=" + tableSchema + ", sizeDataInMb="
			+ sizeDataInMb + ", sizeIndexInMb=" + sizeIndexInMb
			+ ", sizeOverallInMb=" + sizeOverallInMb + ", sizeFreeInMb="
			+ sizeFreeInMb + "]";
	}
}
