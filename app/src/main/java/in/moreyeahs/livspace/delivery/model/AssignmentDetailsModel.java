package in.moreyeahs.livspace.delivery.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssignmentDetailsModel {
    @SerializedName("PickerType")
    private String pickertype;
    @SerializedName("purDetails")
    private List<PurdetailsEntity> purdetails;
    @SerializedName("SupplierAddress")
    private String supplieraddress;
    @SerializedName("POageinDays")
    private int poageindays;
    @SerializedName("POageinHours")
    private int poageinhours;
    @SerializedName("Itemcount")
    private int itemcount;
    @SerializedName("hours")
    private int hours;
    @SerializedName("GRcount")
    private int grcount;
    @SerializedName("DepoName")
    private String deponame;
    @SerializedName("DepoId")
    private int depoid;
    @SerializedName("UpdatedBy")
    private int updatedby;
    @SerializedName("UpdatedDate")
    private String updateddate;
    @SerializedName("CanceledDate")
    private String canceleddate;
    @SerializedName("CanceledByName")
    private String canceledbyname;
    @SerializedName("CanceledById")
    private int canceledbyid;
    @SerializedName("GrStatus")
    private String grstatus;
    @SerializedName("GrNumber")
    private String grnumber;
    @SerializedName("IrRejectComment")
    private String irrejectcomment;
    @SerializedName("VehicleNumber")
    private String vehiclenumber;
    @SerializedName("VehicleType")
    private String vehicletype;
    @SerializedName("IrStatus")
    private String irstatus;
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
    @SerializedName("RejectedBy")
    private String rejectedby;
    @SerializedName("ApprovedBy")
    private String approvedby;
    @SerializedName("CreatedBy")
    private String createdby;
    @SerializedName("CreationDate")
    private String creationdate;
    @SerializedName("SupplierRejectReason")
    private String supplierrejectreason;
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
    @SerializedName("Commentsystem")
    private String commentsystem;
    @SerializedName("CommentApvl")
    private String commentapvl;
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
    @SerializedName("GrPersonName")
    private String grpersonname;
    @SerializedName("GrPersonId")
    private int grpersonid;
    @SerializedName("Gr_Date")
    private String grDate;
    @SerializedName("POAssignmentNo")
    private int poassignmentno;
    @SerializedName("Gr_Amount")
    private double grAmount;
    @SerializedName("discount")
    private int discount;
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

    public String getPickertype() {
        return pickertype;
    }

    public void setPickertype(String pickertype) {
        this.pickertype = pickertype;
    }

    public List<PurdetailsEntity> getPurdetails() {
        return purdetails;
    }

    public void setPurdetails(List<PurdetailsEntity> purdetails) {
        this.purdetails = purdetails;
    }

    public String getSupplieraddress() {
        return supplieraddress;
    }

    public void setSupplieraddress(String supplieraddress) {
        this.supplieraddress = supplieraddress;
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

    public int getGrcount() {
        return grcount;
    }

    public void setGrcount(int grcount) {
        this.grcount = grcount;
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

    public String getCanceleddate() {
        return canceleddate;
    }

    public void setCanceleddate(String canceleddate) {
        this.canceleddate = canceleddate;
    }

    public String getCanceledbyname() {
        return canceledbyname;
    }

    public void setCanceledbyname(String canceledbyname) {
        this.canceledbyname = canceledbyname;
    }

    public int getCanceledbyid() {
        return canceledbyid;
    }

    public void setCanceledbyid(int canceledbyid) {
        this.canceledbyid = canceledbyid;
    }

    public String getGrstatus() {
        return grstatus;
    }

    public void setGrstatus(String grstatus) {
        this.grstatus = grstatus;
    }

    public String getGrnumber() {
        return grnumber;
    }

    public void setGrnumber(String grnumber) {
        this.grnumber = grnumber;
    }

    public String getIrrejectcomment() {
        return irrejectcomment;
    }

    public void setIrrejectcomment(String irrejectcomment) {
        this.irrejectcomment = irrejectcomment;
    }

    public String getVehiclenumber() {
        return vehiclenumber;
    }

    public void setVehiclenumber(String vehiclenumber) {
        this.vehiclenumber = vehiclenumber;
    }

    public String getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(String vehicletype) {
        this.vehicletype = vehicletype;
    }

    public String getIrstatus() {
        return irstatus;
    }

    public void setIrstatus(String irstatus) {
        this.irstatus = irstatus;
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

    public String getRejectedby() {
        return rejectedby;
    }

    public void setRejectedby(String rejectedby) {
        this.rejectedby = rejectedby;
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

    public String getSupplierrejectreason() {
        return supplierrejectreason;
    }

    public void setSupplierrejectreason(String supplierrejectreason) {
        this.supplierrejectreason = supplierrejectreason;
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

    public String getCommentsystem() {
        return commentsystem;
    }

    public void setCommentsystem(String commentsystem) {
        this.commentsystem = commentsystem;
    }

    public String getCommentapvl() {
        return commentapvl;
    }

    public void setCommentapvl(String commentapvl) {
        this.commentapvl = commentapvl;
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

    public String getGrpersonname() {
        return grpersonname;
    }

    public void setGrpersonname(String grpersonname) {
        this.grpersonname = grpersonname;
    }

    public int getGrpersonid() {
        return grpersonid;
    }

    public void setGrpersonid(int grpersonid) {
        this.grpersonid = grpersonid;
    }

    public String getGrDate() {
        return grDate;
    }

    public void setGrDate(String grDate) {
        this.grDate = grDate;
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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
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

    public static class PurdetailsEntity {
        @SerializedName("ItemNumber")
        private String itemnumber;
        @SerializedName("GRDate5")
        private String grdate5;
        @SerializedName("GRDate4")
        private String grdate4;
        @SerializedName("GRDate3")
        private String grdate3;
        @SerializedName("GRDate2")
        private String grdate2;
        @SerializedName("GRDate1")
        private String grdate1;
        @SerializedName("ExpQtyRecived5")
        private int expqtyrecived5;
        @SerializedName("ExpQtyRecived4")
        private int expqtyrecived4;
        @SerializedName("ExpQtyRecived3")
        private int expqtyrecived3;
        @SerializedName("ExpQtyRecived2")
        private int expqtyrecived2;
        @SerializedName("ExpQtyRecived1")
        private int expqtyrecived1;
        @SerializedName("DamagQtyRecived5")
        private int damagqtyrecived5;
        @SerializedName("DamagQtyRecived4")
        private int damagqtyrecived4;
        @SerializedName("DamagQtyRecived3")
        private int damagqtyrecived3;
        @SerializedName("DamagQtyRecived2")
        private int damagqtyrecived2;
        @SerializedName("DamagQtyRecived1")
        private int damagqtyrecived1;
        @SerializedName("ItemName5")
        private String itemname5;
        @SerializedName("ItemName4")
        private String itemname4;
        @SerializedName("ItemName3")
        private String itemname3;
        @SerializedName("ItemName2")
        private String itemname2;
        @SerializedName("ItemName1")
        private String itemname1;
        @SerializedName("ItemMultiMRPId5")
        private int itemmultimrpid5;
        @SerializedName("ItemMultiMRPId4")
        private int itemmultimrpid4;
        @SerializedName("ItemMultiMRPId3")
        private int itemmultimrpid3;
        @SerializedName("ItemMultiMRPId2")
        private int itemmultimrpid2;
        @SerializedName("ItemMultiMRPId1")
        private int itemmultimrpid1;
        @SerializedName("ItemMultiMRPId")
        private int itemmultimrpid;
        @SerializedName("MFGDate5")
        private String mfgdate5;
        @SerializedName("MFGDate4")
        private String mfgdate4;
        @SerializedName("MFGDate3")
        private String mfgdate3;
        @SerializedName("MFGDate2")
        private String mfgdate2;
        @SerializedName("MFGDate1")
        private String mfgdate1;
        @SerializedName("BatchNo5")
        private String batchno5;
        @SerializedName("BatchNo4")
        private String batchno4;
        @SerializedName("BatchNo3")
        private String batchno3;
        @SerializedName("BatchNo2")
        private String batchno2;
        @SerializedName("BatchNo1")
        private String batchno1;
        @SerializedName("QtyRecived")
        private int qtyrecived;
        @SerializedName("CessTaxPercentage")
        private int cesstaxpercentage;
        @SerializedName("dis5")
        private String dis5;
        @SerializedName("dis4")
        private String dis4;
        @SerializedName("dis3")
        private String dis3;
        @SerializedName("dis2")
        private String dis2;
        @SerializedName("dis1")
        private String dis1;
        @SerializedName("Price5")
        private int price5;
        @SerializedName("Price4")
        private int price4;
        @SerializedName("Price3")
        private int price3;
        @SerializedName("Price2")
        private int price2;
        @SerializedName("Price1")
        private int price1;
        @SerializedName("QtyRecived5")
        private int qtyrecived5;
        @SerializedName("QtyRecived4")
        private int qtyrecived4;
        @SerializedName("QtyRecived3")
        private int qtyrecived3;
        @SerializedName("QtyRecived2")
        private int qtyrecived2;
        @SerializedName("QtyRecived1")
        private int qtyrecived1;
        @SerializedName("CreatedBy")
        private String createdby;
        @SerializedName("CreationDate")
        private String creationdate;
        @SerializedName("Status")
        private String status;
        @SerializedName("TotalAmountIncTax")
        private int totalamountinctax;
        @SerializedName("PurchaseQty")
        private int purchaseqty;
        @SerializedName("TotalTaxPercentage")
        private int totaltaxpercentage;
        @SerializedName("TaxAmount")
        private int taxamount;
        @SerializedName("TotalQuantity")
        private int totalquantity;
        @SerializedName("MOQ")
        private int moq;
        @SerializedName("PriceRecived")
        private int pricerecived;
        @SerializedName("Price")
        private double price;
        @SerializedName("MRP")
        private int mrp;
        @SerializedName("ItemName")
        private String itemname;
        @SerializedName("PurchaseSku")
        private String purchasesku;
        @SerializedName("SKUCode")
        private String skucode;
        @SerializedName("HSNCode")
        private String hsncode;
        @SerializedName("SellingSku")
        private String sellingsku;
        @SerializedName("ItemId")
        private int itemid;
        @SerializedName("SupplierName")
        private String suppliername;
        @SerializedName("isDeleted")
        private boolean isdeleted;
        @SerializedName("WarehouseName")
        private String warehousename;
        @SerializedName("WarehouseId")
        private int warehouseid;
        @SerializedName("SupplierId")
        private int supplierid;
        @SerializedName("PurchaseOrderId")
        private int purchaseorderid;
        @SerializedName("PurchaseOrderDetailId")
        private int purchaseorderdetailid;
        @SerializedName("PurchaseOrderMasterRecivedId")
        private int purchaseordermasterrecivedid;
        @SerializedName("CompanyId")
        private int companyid;
        @SerializedName("PurchaseOrderDetailRecivedId")
        private int purchaseorderdetailrecivedid;
        @SerializedName("SupplierAcceptQty")
        private int SupplierAcceptQty;

        public int getSupplierAcceptQty() {
            return SupplierAcceptQty;
        }

        public void setSupplierAcceptQty(int supplierAcceptQty) {
            SupplierAcceptQty = supplierAcceptQty;
        }

        public String getItemnumber() {
            return itemnumber;
        }

        public void setItemnumber(String itemnumber) {
            this.itemnumber = itemnumber;
        }

        public String getGrdate5() {
            return grdate5;
        }

        public void setGrdate5(String grdate5) {
            this.grdate5 = grdate5;
        }

        public String getGrdate4() {
            return grdate4;
        }

        public void setGrdate4(String grdate4) {
            this.grdate4 = grdate4;
        }

        public String getGrdate3() {
            return grdate3;
        }

        public void setGrdate3(String grdate3) {
            this.grdate3 = grdate3;
        }

        public String getGrdate2() {
            return grdate2;
        }

        public void setGrdate2(String grdate2) {
            this.grdate2 = grdate2;
        }

        public String getGrdate1() {
            return grdate1;
        }

        public void setGrdate1(String grdate1) {
            this.grdate1 = grdate1;
        }

        public int getExpqtyrecived5() {
            return expqtyrecived5;
        }

        public void setExpqtyrecived5(int expqtyrecived5) {
            this.expqtyrecived5 = expqtyrecived5;
        }

        public int getExpqtyrecived4() {
            return expqtyrecived4;
        }

        public void setExpqtyrecived4(int expqtyrecived4) {
            this.expqtyrecived4 = expqtyrecived4;
        }

        public int getExpqtyrecived3() {
            return expqtyrecived3;
        }

        public void setExpqtyrecived3(int expqtyrecived3) {
            this.expqtyrecived3 = expqtyrecived3;
        }

        public int getExpqtyrecived2() {
            return expqtyrecived2;
        }

        public void setExpqtyrecived2(int expqtyrecived2) {
            this.expqtyrecived2 = expqtyrecived2;
        }

        public int getExpqtyrecived1() {
            return expqtyrecived1;
        }

        public void setExpqtyrecived1(int expqtyrecived1) {
            this.expqtyrecived1 = expqtyrecived1;
        }

        public int getDamagqtyrecived5() {
            return damagqtyrecived5;
        }

        public void setDamagqtyrecived5(int damagqtyrecived5) {
            this.damagqtyrecived5 = damagqtyrecived5;
        }

        public int getDamagqtyrecived4() {
            return damagqtyrecived4;
        }

        public void setDamagqtyrecived4(int damagqtyrecived4) {
            this.damagqtyrecived4 = damagqtyrecived4;
        }

        public int getDamagqtyrecived3() {
            return damagqtyrecived3;
        }

        public void setDamagqtyrecived3(int damagqtyrecived3) {
            this.damagqtyrecived3 = damagqtyrecived3;
        }

        public int getDamagqtyrecived2() {
            return damagqtyrecived2;
        }

        public void setDamagqtyrecived2(int damagqtyrecived2) {
            this.damagqtyrecived2 = damagqtyrecived2;
        }

        public int getDamagqtyrecived1() {
            return damagqtyrecived1;
        }

        public void setDamagqtyrecived1(int damagqtyrecived1) {
            this.damagqtyrecived1 = damagqtyrecived1;
        }

        public String getItemname5() {
            return itemname5;
        }

        public void setItemname5(String itemname5) {
            this.itemname5 = itemname5;
        }

        public String getItemname4() {
            return itemname4;
        }

        public void setItemname4(String itemname4) {
            this.itemname4 = itemname4;
        }

        public String getItemname3() {
            return itemname3;
        }

        public void setItemname3(String itemname3) {
            this.itemname3 = itemname3;
        }

        public String getItemname2() {
            return itemname2;
        }

        public void setItemname2(String itemname2) {
            this.itemname2 = itemname2;
        }

        public String getItemname1() {
            return itemname1;
        }

        public void setItemname1(String itemname1) {
            this.itemname1 = itemname1;
        }

        public int getItemmultimrpid5() {
            return itemmultimrpid5;
        }

        public void setItemmultimrpid5(int itemmultimrpid5) {
            this.itemmultimrpid5 = itemmultimrpid5;
        }

        public int getItemmultimrpid4() {
            return itemmultimrpid4;
        }

        public void setItemmultimrpid4(int itemmultimrpid4) {
            this.itemmultimrpid4 = itemmultimrpid4;
        }

        public int getItemmultimrpid3() {
            return itemmultimrpid3;
        }

        public void setItemmultimrpid3(int itemmultimrpid3) {
            this.itemmultimrpid3 = itemmultimrpid3;
        }

        public int getItemmultimrpid2() {
            return itemmultimrpid2;
        }

        public void setItemmultimrpid2(int itemmultimrpid2) {
            this.itemmultimrpid2 = itemmultimrpid2;
        }

        public int getItemmultimrpid1() {
            return itemmultimrpid1;
        }

        public void setItemmultimrpid1(int itemmultimrpid1) {
            this.itemmultimrpid1 = itemmultimrpid1;
        }

        public int getItemmultimrpid() {
            return itemmultimrpid;
        }

        public void setItemmultimrpid(int itemmultimrpid) {
            this.itemmultimrpid = itemmultimrpid;
        }

        public String getMfgdate5() {
            return mfgdate5;
        }

        public void setMfgdate5(String mfgdate5) {
            this.mfgdate5 = mfgdate5;
        }

        public String getMfgdate4() {
            return mfgdate4;
        }

        public void setMfgdate4(String mfgdate4) {
            this.mfgdate4 = mfgdate4;
        }

        public String getMfgdate3() {
            return mfgdate3;
        }

        public void setMfgdate3(String mfgdate3) {
            this.mfgdate3 = mfgdate3;
        }

        public String getMfgdate2() {
            return mfgdate2;
        }

        public void setMfgdate2(String mfgdate2) {
            this.mfgdate2 = mfgdate2;
        }

        public String getMfgdate1() {
            return mfgdate1;
        }

        public void setMfgdate1(String mfgdate1) {
            this.mfgdate1 = mfgdate1;
        }

        public String getBatchno5() {
            return batchno5;
        }

        public void setBatchno5(String batchno5) {
            this.batchno5 = batchno5;
        }

        public String getBatchno4() {
            return batchno4;
        }

        public void setBatchno4(String batchno4) {
            this.batchno4 = batchno4;
        }

        public String getBatchno3() {
            return batchno3;
        }

        public void setBatchno3(String batchno3) {
            this.batchno3 = batchno3;
        }

        public String getBatchno2() {
            return batchno2;
        }

        public void setBatchno2(String batchno2) {
            this.batchno2 = batchno2;
        }

        public String getBatchno1() {
            return batchno1;
        }

        public void setBatchno1(String batchno1) {
            this.batchno1 = batchno1;
        }

        public int getQtyrecived() {
            return qtyrecived;
        }

        public void setQtyrecived(int qtyrecived) {
            this.qtyrecived = qtyrecived;
        }

        public int getCesstaxpercentage() {
            return cesstaxpercentage;
        }

        public void setCesstaxpercentage(int cesstaxpercentage) {
            this.cesstaxpercentage = cesstaxpercentage;
        }

        public String getDis5() {
            return dis5;
        }

        public void setDis5(String dis5) {
            this.dis5 = dis5;
        }

        public String getDis4() {
            return dis4;
        }

        public void setDis4(String dis4) {
            this.dis4 = dis4;
        }

        public String getDis3() {
            return dis3;
        }

        public void setDis3(String dis3) {
            this.dis3 = dis3;
        }

        public String getDis2() {
            return dis2;
        }

        public void setDis2(String dis2) {
            this.dis2 = dis2;
        }

        public String getDis1() {
            return dis1;
        }

        public void setDis1(String dis1) {
            this.dis1 = dis1;
        }

        public int getPrice5() {
            return price5;
        }

        public void setPrice5(int price5) {
            this.price5 = price5;
        }

        public int getPrice4() {
            return price4;
        }

        public void setPrice4(int price4) {
            this.price4 = price4;
        }

        public int getPrice3() {
            return price3;
        }

        public void setPrice3(int price3) {
            this.price3 = price3;
        }

        public int getPrice2() {
            return price2;
        }

        public void setPrice2(int price2) {
            this.price2 = price2;
        }

        public int getPrice1() {
            return price1;
        }

        public void setPrice1(int price1) {
            this.price1 = price1;
        }

        public int getQtyrecived5() {
            return qtyrecived5;
        }

        public void setQtyrecived5(int qtyrecived5) {
            this.qtyrecived5 = qtyrecived5;
        }

        public int getQtyrecived4() {
            return qtyrecived4;
        }

        public void setQtyrecived4(int qtyrecived4) {
            this.qtyrecived4 = qtyrecived4;
        }

        public int getQtyrecived3() {
            return qtyrecived3;
        }

        public void setQtyrecived3(int qtyrecived3) {
            this.qtyrecived3 = qtyrecived3;
        }

        public int getQtyrecived2() {
            return qtyrecived2;
        }

        public void setQtyrecived2(int qtyrecived2) {
            this.qtyrecived2 = qtyrecived2;
        }

        public int getQtyrecived1() {
            return qtyrecived1;
        }

        public void setQtyrecived1(int qtyrecived1) {
            this.qtyrecived1 = qtyrecived1;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getTotalamountinctax() {
            return totalamountinctax;
        }

        public void setTotalamountinctax(int totalamountinctax) {
            this.totalamountinctax = totalamountinctax;
        }

        public int getPurchaseqty() {
            return purchaseqty;
        }

        public void setPurchaseqty(int purchaseqty) {
            this.purchaseqty = purchaseqty;
        }

        public int getTotaltaxpercentage() {
            return totaltaxpercentage;
        }

        public void setTotaltaxpercentage(int totaltaxpercentage) {
            this.totaltaxpercentage = totaltaxpercentage;
        }

        public int getTaxamount() {
            return taxamount;
        }

        public void setTaxamount(int taxamount) {
            this.taxamount = taxamount;
        }

        public int getTotalquantity() {
            return totalquantity;
        }

        public void setTotalquantity(int totalquantity) {
            this.totalquantity = totalquantity;
        }

        public int getMoq() {
            return moq;
        }

        public void setMoq(int moq) {
            this.moq = moq;
        }

        public int getPricerecived() {
            return pricerecived;
        }

        public void setPricerecived(int pricerecived) {
            this.pricerecived = pricerecived;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getMrp() {
            return mrp;
        }

        public void setMrp(int mrp) {
            this.mrp = mrp;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getPurchasesku() {
            return purchasesku;
        }

        public void setPurchasesku(String purchasesku) {
            this.purchasesku = purchasesku;
        }

        public String getSkucode() {
            return skucode;
        }

        public void setSkucode(String skucode) {
            this.skucode = skucode;
        }

        public String getHsncode() {
            return hsncode;
        }

        public void setHsncode(String hsncode) {
            this.hsncode = hsncode;
        }

        public String getSellingsku() {
            return sellingsku;
        }

        public void setSellingsku(String sellingsku) {
            this.sellingsku = sellingsku;
        }

        public int getItemid() {
            return itemid;
        }

        public void setItemid(int itemid) {
            this.itemid = itemid;
        }

        public String getSuppliername() {
            return suppliername;
        }

        public void setSuppliername(String suppliername) {
            this.suppliername = suppliername;
        }

        public boolean getIsdeleted() {
            return isdeleted;
        }

        public void setIsdeleted(boolean isdeleted) {
            this.isdeleted = isdeleted;
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

        public int getSupplierid() {
            return supplierid;
        }

        public void setSupplierid(int supplierid) {
            this.supplierid = supplierid;
        }

        public int getPurchaseorderid() {
            return purchaseorderid;
        }

        public void setPurchaseorderid(int purchaseorderid) {
            this.purchaseorderid = purchaseorderid;
        }

        public int getPurchaseorderdetailid() {
            return purchaseorderdetailid;
        }

        public void setPurchaseorderdetailid(int purchaseorderdetailid) {
            this.purchaseorderdetailid = purchaseorderdetailid;
        }

        public int getPurchaseordermasterrecivedid() {
            return purchaseordermasterrecivedid;
        }

        public void setPurchaseordermasterrecivedid(int purchaseordermasterrecivedid) {
            this.purchaseordermasterrecivedid = purchaseordermasterrecivedid;
        }

        public int getCompanyid() {
            return companyid;
        }

        public void setCompanyid(int companyid) {
            this.companyid = companyid;
        }

        public int getPurchaseorderdetailrecivedid() {
            return purchaseorderdetailrecivedid;
        }

        public void setPurchaseorderdetailrecivedid(int purchaseorderdetailrecivedid) {
            this.purchaseorderdetailrecivedid = purchaseorderdetailrecivedid;
        }
    }
}
