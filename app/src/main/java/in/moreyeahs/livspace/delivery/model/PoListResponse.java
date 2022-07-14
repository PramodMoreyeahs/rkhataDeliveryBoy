package in.moreyeahs.livspace.delivery.model;

import com.google.gson.annotations.SerializedName;

public class PoListResponse {

    @SerializedName("POageinDays")
    private int poageindays;
    @SerializedName("POageinHours")
    private int poageinhours;
    @SerializedName("Itemcount")
    private int itemcount;
    @SerializedName("hours")
    private int hours;
    @SerializedName("DepoName")
    private String deponame;
    @SerializedName("DepoId")
    private int depoid;
    @SerializedName("UpdatedBy")
    private int updatedby;
    @SerializedName("UpdatedDate")
    private String updateddate;
    @SerializedName("Gr_Process")
    private boolean grProcess;
    @SerializedName("IsSendSupplierApp")
    private boolean issendsupplierapp;
    @SerializedName("IsPDFsentGmail")
    private boolean ispdfsentgmail;
    @SerializedName("IsPDFsentWhatsApp")
    private boolean ispdfsentwhatsapp;
    @SerializedName("IsPDFcreated")
    private boolean ispdfcreated;
    @SerializedName("Acitve")
    private boolean acitve;
    @SerializedName("ApprovedBy")
    private String approvedby;
    @SerializedName("CreatedBy")
    private String createdby;
    @SerializedName("CreationDate")
    private String creationdate;
    @SerializedName("DboyMobileNo")
    private String dboymobileno;
    @SerializedName("DboyName")
    private String dboyname;
    @SerializedName("DboyId")
    private int dboyid;
    @SerializedName("IR_Progress")
    private String irProgress;
    @SerializedName("progress")
    private String progress;
    @SerializedName("Level")
    private String level;
    @SerializedName("Comment")
    private String comment;
    @SerializedName("PoType")
    private String potype;
    @SerializedName("ETotalAmount")
    private double etotalamount;
    @SerializedName("Advance_Amt")
    private int advanceAmt;
    @SerializedName("TotalAmount")
    private double totalamount;
    @SerializedName("POAssignmentNo")
    private int poassignmentno;
    @SerializedName("Gr_Amount")
    private double grAmount;
    @SerializedName("Status")
    private String status;
    @SerializedName("WarehouseCity")
    private String warehousecity;
    @SerializedName("WarehouseName")
    private String warehousename;
    @SerializedName("WarehouseId")
    private int warehouseid;
    @SerializedName("SupplierName")
    private String suppliername;
    @SerializedName("SupplierId")
    private int supplierid;
    @SerializedName("CompanyId")
    private int companyid;
    @SerializedName("PurchaseOrderId")
    private int purchaseorderid;
    @SerializedName("SupplierAddress")
    private String supplierAddress;
    @SerializedName("SupplierMobileNo")
    private long supplierMobileMo;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lg")
    private double longitude;
    @SerializedName("Ewaybillurl")
    private String Ewaybillurl;

    public String getEwaybillurl() {
        return Ewaybillurl;
    }

    public void setEwaybillurl(String ewaybillurl) {
        Ewaybillurl = ewaybillurl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getSupplierMobileMo() {
        return supplierMobileMo;
    }

    public void setSupplierMobileMo(long supplierMobileMo) {
        this.supplierMobileMo = supplierMobileMo;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public int getPoageindays() {
        return poageindays;
    }

    public void setPoageindays(int poageindays) {
        this.poageindays = poageindays;
    }

    public int getPoageinhours() {
        return poageinhours;
    }

    public void setPoageinhours(int poageinhours) {
        this.poageinhours = poageinhours;
    }

    public int getItemcount() {
        return itemcount;
    }

    public void setItemcount(int itemcount) {
        this.itemcount = itemcount;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getDeponame() {
        return deponame;
    }

    public void setDeponame(String deponame) {
        this.deponame = deponame;
    }

    public int getDepoid() {
        return depoid;
    }

    public void setDepoid(int depoid) {
        this.depoid = depoid;
    }

    public int getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(int updatedby) {
        this.updatedby = updatedby;
    }

    public String getUpdateddate() {
        return updateddate;
    }

    public void setUpdateddate(String updateddate) {
        this.updateddate = updateddate;
    }

    public boolean getGrProcess() {
        return grProcess;
    }

    public void setGrProcess(boolean grProcess) {
        this.grProcess = grProcess;
    }

    public boolean getIssendsupplierapp() {
        return issendsupplierapp;
    }

    public void setIssendsupplierapp(boolean issendsupplierapp) {
        this.issendsupplierapp = issendsupplierapp;
    }

    public boolean getIspdfsentgmail() {
        return ispdfsentgmail;
    }

    public void setIspdfsentgmail(boolean ispdfsentgmail) {
        this.ispdfsentgmail = ispdfsentgmail;
    }

    public boolean getIspdfsentwhatsapp() {
        return ispdfsentwhatsapp;
    }

    public void setIspdfsentwhatsapp(boolean ispdfsentwhatsapp) {
        this.ispdfsentwhatsapp = ispdfsentwhatsapp;
    }

    public boolean getIspdfcreated() {
        return ispdfcreated;
    }

    public void setIspdfcreated(boolean ispdfcreated) {
        this.ispdfcreated = ispdfcreated;
    }

    public boolean getAcitve() {
        return acitve;
    }

    public void setAcitve(boolean acitve) {
        this.acitve = acitve;
    }

    public String getApprovedby() {
        return approvedby;
    }

    public void setApprovedby(String approvedby) {
        this.approvedby = approvedby;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }

    public String getDboymobileno() {
        return dboymobileno;
    }

    public void setDboymobileno(String dboymobileno) {
        this.dboymobileno = dboymobileno;
    }

    public String getDboyname() {
        return dboyname;
    }

    public void setDboyname(String dboyname) {
        this.dboyname = dboyname;
    }

    public int getDboyid() {
        return dboyid;
    }

    public void setDboyid(int dboyid) {
        this.dboyid = dboyid;
    }

    public String getIrProgress() {
        return irProgress;
    }

    public void setIrProgress(String irProgress) {
        this.irProgress = irProgress;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPotype() {
        return potype;
    }

    public void setPotype(String potype) {
        this.potype = potype;
    }

    public double getEtotalamount() {
        return etotalamount;
    }

    public void setEtotalamount(double etotalamount) {
        this.etotalamount = etotalamount;
    }

    public int getAdvanceAmt() {
        return advanceAmt;
    }

    public void setAdvanceAmt(int advanceAmt) {
        this.advanceAmt = advanceAmt;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }

    public int getPoassignmentno() {
        return poassignmentno;
    }

    public void setPoassignmentno(int poassignmentno) {
        this.poassignmentno = poassignmentno;
    }

    public double getGrAmount() {
        return grAmount;
    }

    public void setGrAmount(double grAmount) {
        this.grAmount = grAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWarehousecity() {
        return warehousecity;
    }

    public void setWarehousecity(String warehousecity) {
        this.warehousecity = warehousecity;
    }

    public String getWarehousename() {
        return warehousename;
    }

    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }

    public int getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(int warehouseid) {
        this.warehouseid = warehouseid;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    public int getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(int supplierid) {
        this.supplierid = supplierid;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public int getPurchaseorderid() {
        return purchaseorderid;
    }

    public void setPurchaseorderid(int purchaseorderid) {
        this.purchaseorderid = purchaseorderid;
    }
}
