package in.moreyeahs.livspace.delivery.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssignmentListResponse {

    @SerializedName("Message")
    private String message;
    @SerializedName("status")
    private boolean status;
    @SerializedName("DeliveryIssuancepo")
    private List<AssignmentList> deliveryissuancepo;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<AssignmentList> getDeliveryissuancepo() {
        return deliveryissuancepo;
    }

    public void setDeliveryissuancepo(List<AssignmentList> deliveryissuancepo) {
        this.deliveryissuancepo = deliveryissuancepo;
    }

    public static class AssignmentList {

        @SerializedName("AssignmentBarcodeImage")
        private String assignmentbarcodeimage;
        @SerializedName("details")
        private List<Details> details;
        @SerializedName("TotalAssignmentAmount")
        private double totalassignmentamount;
        @SerializedName("TravelDistance")
        private double traveldistance;
        @SerializedName("IdealTime")
        private double idealtime;
        @SerializedName("UpdatedDate")
        private String updateddate;
        @SerializedName("CreatedDate")
        private String createddate;
        @SerializedName("IsActive")
        private boolean isactive;
        @SerializedName("Acceptance")
        private boolean acceptance;
        @SerializedName("PurchaseOrderIds")
        private String purchaseorderids;
        @SerializedName("GRNIds")
        private String grnids;
        @SerializedName("Status")
        private String status;
        @SerializedName("VehicleNumber")
        private String vehiclenumber;
        @SerializedName("VehicleId")
        private int vehicleid;
        @SerializedName("PeopleID")
        private int peopleid;
        @SerializedName("WarehouseId")
        private int warehouseid;
        @SerializedName("CompanyId")
        private int companyid;
        @SerializedName("DisplayName")
        private String displayname;
        @SerializedName("city")
        private String city;
        @SerializedName("Cityid")
        private int cityid;
        @SerializedName("POAssignmentId")
        private int poassignmentid;
        @SerializedName("IsSubmitted")
        private Boolean isSubmitted;

        public Boolean getSubmitted() {
            return isSubmitted;
        }

        public void setSubmitted(Boolean submitted) {
            isSubmitted = submitted;
        }

        public String getAssignmentbarcodeimage() {
            return assignmentbarcodeimage;
        }

        public void setAssignmentbarcodeimage(String assignmentbarcodeimage) {
            this.assignmentbarcodeimage = assignmentbarcodeimage;
        }

        public List<Details> getDetails() {
            return details;
        }

        public void setDetails(List<Details> details) {
            this.details = details;
        }

        public double getTotalassignmentamount() {
            return totalassignmentamount;
        }

        public void setTotalassignmentamount(double totalassignmentamount) {
            this.totalassignmentamount = totalassignmentamount;
        }

        public double getTraveldistance() {
            return traveldistance;
        }

        public void setTraveldistance(double traveldistance) {
            this.traveldistance = traveldistance;
        }

        public double getIdealtime() {
            return idealtime;
        }

        public void setIdealtime(double idealtime) {
            this.idealtime = idealtime;
        }

        public String getUpdateddate() {
            return updateddate;
        }

        public void setUpdateddate(String updateddate) {
            this.updateddate = updateddate;
        }

        public String getCreateddate() {
            return createddate;
        }

        public void setCreateddate(String createddate) {
            this.createddate = createddate;
        }

        public boolean getIsactive() {
            return isactive;
        }

        public void setIsactive(boolean isactive) {
            this.isactive = isactive;
        }

        public boolean getAcceptance() {
            return acceptance;
        }

        public void setAcceptance(boolean acceptance) {
            this.acceptance = acceptance;
        }

        public String getPurchaseorderids() {
            return purchaseorderids;
        }

        public void setPurchaseorderids(String purchaseorderids) {
            this.purchaseorderids = purchaseorderids;
        }

        public String getGrnids() {
            return grnids;
        }

        public void setGrnids(String grnids) {
            this.grnids = grnids;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getVehiclenumber() {
            return vehiclenumber;
        }

        public void setVehiclenumber(String vehiclenumber) {
            this.vehiclenumber = vehiclenumber;
        }

        public int getVehicleid() {
            return vehicleid;
        }

        public void setVehicleid(int vehicleid) {
            this.vehicleid = vehicleid;
        }

        public int getPeopleid() {
            return peopleid;
        }

        public void setPeopleid(int peopleid) {
            this.peopleid = peopleid;
        }

        public int getWarehouseid() {
            return warehouseid;
        }

        public void setWarehouseid(int warehouseid) {
            this.warehouseid = warehouseid;
        }

        public int getCompanyid() {
            return companyid;
        }

        public void setCompanyid(int companyid) {
            this.companyid = companyid;
        }

        public String getDisplayname() {
            return displayname;
        }

        public void setDisplayname(String displayname) {
            this.displayname = displayname;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getCityid() {
            return cityid;
        }

        public void setCityid(int cityid) {
            this.cityid = cityid;
        }

        public int getPoassignmentid() {
            return poassignmentid;
        }

        public void setPoassignmentid(int poassignmentid) {
            this.poassignmentid = poassignmentid;
        }

        public static class Details {
            @SerializedName("ItemMultiMRPId")
            private int itemmultimrpid;
            @SerializedName("Itemname")
            private String itemname;
            @SerializedName("TotalAmt")
            private double totalamt;
            @SerializedName("ItemId")
            private int itemid;
            @SerializedName("OrderQty")
            private String orderqty;
            @SerializedName("IsGRN")
            private boolean isgrn;
            @SerializedName("Qty")
            private int qty;
            @SerializedName("PurchaseOrderDetailId")
            private int purchaseorderdetailid;
            @SerializedName("PurchaseOrderId")
            private int purchaseorderid;
            @SerializedName("POAssignmentDetailsId")
            private int poassignmentdetailsid;

            public int getItemmultimrpid() {
                return itemmultimrpid;
            }

            public void setItemmultimrpid(int itemmultimrpid) {
                this.itemmultimrpid = itemmultimrpid;
            }

            public String getItemname() {
                return itemname;
            }

            public void setItemname(String itemname) {
                this.itemname = itemname;
            }

            public double getTotalamt() {
                return totalamt;
            }

            public void setTotalamt(double totalamt) {
                this.totalamt = totalamt;
            }

            public int getItemid() {
                return itemid;
            }

            public void setItemid(int itemid) {
                this.itemid = itemid;
            }

            public String getOrderqty() {
                return orderqty;
            }

            public void setOrderqty(String orderqty) {
                this.orderqty = orderqty;
            }

            public boolean getIsgrn() {
                return isgrn;
            }

            public void setIsgrn(boolean isgrn) {
                this.isgrn = isgrn;
            }

            public int getQty() {
                return qty;
            }

            public void setQty(int qty) {
                this.qty = qty;
            }

            public int getPurchaseorderdetailid() {
                return purchaseorderdetailid;
            }

            public void setPurchaseorderdetailid(int purchaseorderdetailid) {
                this.purchaseorderdetailid = purchaseorderdetailid;
            }

            public int getPurchaseorderid() {
                return purchaseorderid;
            }

            public void setPurchaseorderid(int purchaseorderid) {
                this.purchaseorderid = purchaseorderid;
            }

            public int getPoassignmentdetailsid() {
                return poassignmentdetailsid;
            }

            public void setPoassignmentdetailsid(int poassignmentdetailsid) {
                this.poassignmentdetailsid = poassignmentdetailsid;
            }
        }
    }
}
