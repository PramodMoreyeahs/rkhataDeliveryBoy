package in.moreyeahs.livspace.delivery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CashCollectionResponse implements Serializable {
           private Boolean Status;
        private String Message;
   private double TotalCollection;

        private List<ListData> Data;

        public Boolean getStatus() {
            return this.Status;
        }

        public void setStatus(Boolean Status) {
            this.Status = Status;
        }

        public String getMessage() {
            return this.Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }

        public List<ListData> getData() {
            return this.Data;
        }

        public void setData(List<ListData> Data) {
            this.Data = Data;
        }

    public double getTotalCollection() {
        return TotalCollection;
    }

    public static class ListData implements Serializable {
            private Boolean Delete;

            private String Status;

            private ArrayList<OrderDetails> orderDetails;

            private Boolean Active;

            private String ShopName;

            private String Address;

            private Double Amount;

            private String CustomerName;

            private Integer OrderId;

            private String PaymentStatus;
            private String PaymentDate;

            public String getPaymentDate() {
                return PaymentDate;
            }

            public void setPaymentDate(String paymentDate) {
                PaymentDate = paymentDate;
            }

            public Boolean getDelete() {
                return this.Delete;
            }

            public void setDelete(Boolean Delete) {
                this.Delete = Delete;
            }

            public String getStatus() {
                return this.Status;
            }

            public void setStatus(String Status) {
                this.Status = Status;
            }

            public ArrayList<OrderDetails> getOrderDetails() {
                return this.orderDetails;
            }

            public void setOrderDetails(ArrayList<OrderDetails> orderDetails) {
                this.orderDetails = orderDetails;
            }

            public Boolean getActive() {
                return this.Active;
            }

            public void setActive(Boolean Active) {
                this.Active = Active;
            }

            public String getShopName() {
                return this.ShopName;
            }

            public void setShopName(String ShopName) {
                this.ShopName = ShopName;
            }

            public String getAddress() {
                return this.Address;
            }

            public void setAddress(String Address) {
                this.Address = Address;
            }

            public Double getAmount() {
                return this.Amount;
            }

            public void setAmount(Double Amount) {
                this.Amount = Amount;
            }

            public String getCustomerName() {
                return this.CustomerName;
            }

            public void setCustomerName(String CustomerName) {
                this.CustomerName = CustomerName;
            }

            public Integer getOrderId() {
                return this.OrderId;
            }

            public void setOrderId(Integer OrderId) {
                this.OrderId = OrderId;
            }

            public String getPaymentStatus() {
                return this.PaymentStatus;
            }

            public void setPaymentStatus(String PaymentStatus) {
                this.PaymentStatus = PaymentStatus;
            }

            public static class OrderDetails implements Serializable {
                private String itemNumber;

                private String UpdatedDate;

                private Integer year;

                private Double NetAmtAfterDis;

                private Object marginPoint;

                private Object SubsubcategoryName;

                private Object promoPoint;

                private Object WarehouseName;

                private Integer ItemId;

                private Double CurrentStock;

                private String SizePerUnit;

                private Integer ItemMultiMRPId;

                private Double price;

                private String SellingSku;

                private Integer OrderDetailsId;

                private Integer day;

                private String Status;

                private Double NetAmmount;

                private Double SGSTTaxPercentage;

                private Double CGSTTaxAmmount;

                private String itemname;

                private Double MinOrderQtyPrice;

                private String GUID;

                private Integer CustomerId;

                private Object City;

                private Integer OrderId;

                private String Itempic;

                private Double NetPurchasePrice;

                private Integer month;

                private Double TotalAmt;

                private String itemcode;

                private String Barcode;

                private Integer qty;

                private String CreatedDate;

                private Integer VendorId;

                private String SubcategoryName;

                private Double TotalAmountAfterTaxDisc;

                private Integer Noqty;

                private Double CessTaxAmount;

                private Double AmtWithoutAfterTaxDisc;

                private Object CityId;

                private Double CGSTTaxPercentage;

                private String SellingUnitName;

                private String CustomerName;

                private Boolean Deleted;

                private Double TotalCessPercentage;

                private Double TaxAmmount;

                private Double AmtWithoutTaxDisc;

                private Integer CompanyId;

                private Double TaxPercentage;

                private Integer MinOrderQty;

                private Integer WarehouseId;

                private String Mobile;

                private Double Purchaseprice;

                private Double DiscountAmmount;

                private String OrderDate;

                private Double UnitPrice;

                private Double DiscountPercentage;

                private Object AgentCode;

                private Object SupplierName;

                private Double SGSTTaxAmmount;

                private String CategoryName;

                private String HSNCode;

                public String getItemNumber() {
                    return this.itemNumber;
                }

                public void setItemNumber(String itemNumber) {
                    this.itemNumber = itemNumber;
                }

                public String getUpdatedDate() {
                    return this.UpdatedDate;
                }

                public void setUpdatedDate(String UpdatedDate) {
                    this.UpdatedDate = UpdatedDate;
                }

                public Integer getYear() {
                    return this.year;
                }

                public void setYear(Integer year) {
                    this.year = year;
                }

                public Double getNetAmtAfterDis() {
                    return this.NetAmtAfterDis;
                }

                public void setNetAmtAfterDis(Double NetAmtAfterDis) {
                    this.NetAmtAfterDis = NetAmtAfterDis;
                }

                public Object getMarginPoint() {
                    return this.marginPoint;
                }

                public void setMarginPoint(Object marginPoint) {
                    this.marginPoint = marginPoint;
                }

                public Object getSubsubcategoryName() {
                    return this.SubsubcategoryName;
                }

                public void setSubsubcategoryName(Object SubsubcategoryName) {
                    this.SubsubcategoryName = SubsubcategoryName;
                }

                public Object getPromoPoint() {
                    return this.promoPoint;
                }

                public void setPromoPoint(Object promoPoint) {
                    this.promoPoint = promoPoint;
                }

                public Object getWarehouseName() {
                    return this.WarehouseName;
                }

                public void setWarehouseName(Object WarehouseName) {
                    this.WarehouseName = WarehouseName;
                }

                public Integer getItemId() {
                    return this.ItemId;
                }

                public void setItemId(Integer ItemId) {
                    this.ItemId = ItemId;
                }

                public Double getCurrentStock() {
                    return this.CurrentStock;
                }

                public void setCurrentStock(Double CurrentStock) {
                    this.CurrentStock = CurrentStock;
                }

                public String getSizePerUnit() {
                    return this.SizePerUnit;
                }

                public void setSizePerUnit(String SizePerUnit) {
                    this.SizePerUnit = SizePerUnit;
                }

                public Integer getItemMultiMRPId() {
                    return this.ItemMultiMRPId;
                }

                public void setItemMultiMRPId(Integer ItemMultiMRPId) {
                    this.ItemMultiMRPId = ItemMultiMRPId;
                }

                public Double getPrice() {
                    return this.price;
                }

                public void setPrice(Double price) {
                    this.price = price;
                }

                public String getSellingSku() {
                    return this.SellingSku;
                }

                public void setSellingSku(String SellingSku) {
                    this.SellingSku = SellingSku;
                }

                public Integer getOrderDetailsId() {
                    return this.OrderDetailsId;
                }

                public void setOrderDetailsId(Integer OrderDetailsId) {
                    this.OrderDetailsId = OrderDetailsId;
                }

                public Integer getDay() {
                    return this.day;
                }

                public void setDay(Integer day) {
                    this.day = day;
                }

                public String getStatus() {
                    return this.Status;
                }

                public void setStatus(String Status) {
                    this.Status = Status;
                }

                public Double getNetAmmount() {
                    return this.NetAmmount;
                }

                public void setNetAmmount(Double NetAmmount) {
                    this.NetAmmount = NetAmmount;
                }

                public Double getSGSTTaxPercentage() {
                    return this.SGSTTaxPercentage;
                }

                public void setSGSTTaxPercentage(Double SGSTTaxPercentage) {
                    this.SGSTTaxPercentage = SGSTTaxPercentage;
                }

                public Double getCGSTTaxAmmount() {
                    return this.CGSTTaxAmmount;
                }

                public void setCGSTTaxAmmount(Double CGSTTaxAmmount) {
                    this.CGSTTaxAmmount = CGSTTaxAmmount;
                }

                public String getItemname() {
                    return this.itemname;
                }

                public void setItemname(String itemname) {
                    this.itemname = itemname;
                }

                public Double getMinOrderQtyPrice() {
                    return this.MinOrderQtyPrice;
                }

                public void setMinOrderQtyPrice(Double MinOrderQtyPrice) {
                    this.MinOrderQtyPrice = MinOrderQtyPrice;
                }

                public String getGUID() {
                    return this.GUID;
                }

                public void setGUID(String GUID) {
                    this.GUID = GUID;
                }

                public Integer getCustomerId() {
                    return this.CustomerId;
                }

                public void setCustomerId(Integer CustomerId) {
                    this.CustomerId = CustomerId;
                }

                public Object getCity() {
                    return this.City;
                }

                public void setCity(Object City) {
                    this.City = City;
                }

                public Integer getOrderId() {
                    return this.OrderId;
                }

                public void setOrderId(Integer OrderId) {
                    this.OrderId = OrderId;
                }

                public String getItempic() {
                    return this.Itempic;
                }

                public void setItempic(String Itempic) {
                    this.Itempic = Itempic;
                }

                public Double getNetPurchasePrice() {
                    return this.NetPurchasePrice;
                }

                public void setNetPurchasePrice(Double NetPurchasePrice) {
                    this.NetPurchasePrice = NetPurchasePrice;
                }

                public Integer getMonth() {
                    return this.month;
                }

                public void setMonth(Integer month) {
                    this.month = month;
                }

                public Double getTotalAmt() {
                    return this.TotalAmt;
                }

                public void setTotalAmt(Double TotalAmt) {
                    this.TotalAmt = TotalAmt;
                }

                public String getItemcode() {
                    return this.itemcode;
                }

                public void setItemcode(String itemcode) {
                    this.itemcode = itemcode;
                }

                public String getBarcode() {
                    return this.Barcode;
                }

                public void setBarcode(String Barcode) {
                    this.Barcode = Barcode;
                }

                public Integer getQty() {
                    return this.qty;
                }

                public void setQty(Integer qty) {
                    this.qty = qty;
                }

                public String getCreatedDate() {
                    return this.CreatedDate;
                }

                public void setCreatedDate(String CreatedDate) {
                    this.CreatedDate = CreatedDate;
                }

                public Integer getVendorId() {
                    return this.VendorId;
                }

                public void setVendorId(Integer VendorId) {
                    this.VendorId = VendorId;
                }

                public String getSubcategoryName() {
                    return this.SubcategoryName;
                }

                public void setSubcategoryName(String SubcategoryName) {
                    this.SubcategoryName = SubcategoryName;
                }

                public Double getTotalAmountAfterTaxDisc() {
                    return this.TotalAmountAfterTaxDisc;
                }

                public void setTotalAmountAfterTaxDisc(Double TotalAmountAfterTaxDisc) {
                    this.TotalAmountAfterTaxDisc = TotalAmountAfterTaxDisc;
                }

                public Integer getNoqty() {
                    return this.Noqty;
                }

                public void setNoqty(Integer Noqty) {
                    this.Noqty = Noqty;
                }

                public Double getCessTaxAmount() {
                    return this.CessTaxAmount;
                }

                public void setCessTaxAmount(Double CessTaxAmount) {
                    this.CessTaxAmount = CessTaxAmount;
                }

                public Double getAmtWithoutAfterTaxDisc() {
                    return this.AmtWithoutAfterTaxDisc;
                }

                public void setAmtWithoutAfterTaxDisc(Double AmtWithoutAfterTaxDisc) {
                    this.AmtWithoutAfterTaxDisc = AmtWithoutAfterTaxDisc;
                }

                public Object getCityId() {
                    return this.CityId;
                }

                public void setCityId(Object CityId) {
                    this.CityId = CityId;
                }

                public Double getCGSTTaxPercentage() {
                    return this.CGSTTaxPercentage;
                }

                public void setCGSTTaxPercentage(Double CGSTTaxPercentage) {
                    this.CGSTTaxPercentage = CGSTTaxPercentage;
                }

                public String getSellingUnitName() {
                    return this.SellingUnitName;
                }

                public void setSellingUnitName(String SellingUnitName) {
                    this.SellingUnitName = SellingUnitName;
                }

                public String getCustomerName() {
                    return this.CustomerName;
                }

                public void setCustomerName(String CustomerName) {
                    this.CustomerName = CustomerName;
                }

                public Boolean getDeleted() {
                    return this.Deleted;
                }

                public void setDeleted(Boolean Deleted) {
                    this.Deleted = Deleted;
                }

                public Double getTotalCessPercentage() {
                    return this.TotalCessPercentage;
                }

                public void setTotalCessPercentage(Double TotalCessPercentage) {
                    this.TotalCessPercentage = TotalCessPercentage;
                }

                public Double getTaxAmmount() {
                    return this.TaxAmmount;
                }

                public void setTaxAmmount(Double TaxAmmount) {
                    this.TaxAmmount = TaxAmmount;
                }

                public Double getAmtWithoutTaxDisc() {
                    return this.AmtWithoutTaxDisc;
                }

                public void setAmtWithoutTaxDisc(Double AmtWithoutTaxDisc) {
                    this.AmtWithoutTaxDisc = AmtWithoutTaxDisc;
                }

                public Integer getCompanyId() {
                    return this.CompanyId;
                }

                public void setCompanyId(Integer CompanyId) {
                    this.CompanyId = CompanyId;
                }

                public Double getTaxPercentage() {
                    return this.TaxPercentage;
                }

                public void setTaxPercentage(Double TaxPercentage) {
                    this.TaxPercentage = TaxPercentage;
                }

                public Integer getMinOrderQty() {
                    return this.MinOrderQty;
                }

                public void setMinOrderQty(Integer MinOrderQty) {
                    this.MinOrderQty = MinOrderQty;
                }

                public Integer getWarehouseId() {
                    return this.WarehouseId;
                }

                public void setWarehouseId(Integer WarehouseId) {
                    this.WarehouseId = WarehouseId;
                }

                public String getMobile() {
                    return this.Mobile;
                }

                public void setMobile(String Mobile) {
                    this.Mobile = Mobile;
                }

                public Double getPurchaseprice() {
                    return this.Purchaseprice;
                }

                public void setPurchaseprice(Double Purchaseprice) {
                    this.Purchaseprice = Purchaseprice;
                }

                public Double getDiscountAmmount() {
                    return this.DiscountAmmount;
                }

                public void setDiscountAmmount(Double DiscountAmmount) {
                    this.DiscountAmmount = DiscountAmmount;
                }

                public String getOrderDate() {
                    return this.OrderDate;
                }

                public void setOrderDate(String OrderDate) {
                    this.OrderDate = OrderDate;
                }

                public Double getUnitPrice() {
                    return this.UnitPrice;
                }

                public void setUnitPrice(Double UnitPrice) {
                    this.UnitPrice = UnitPrice;
                }

                public Double getDiscountPercentage() {
                    return this.DiscountPercentage;
                }

                public void setDiscountPercentage(Double DiscountPercentage) {
                    this.DiscountPercentage = DiscountPercentage;
                }

                public Object getAgentCode() {
                    return this.AgentCode;
                }

                public void setAgentCode(Object AgentCode) {
                    this.AgentCode = AgentCode;
                }

                public Object getSupplierName() {
                    return this.SupplierName;
                }

                public void setSupplierName(Object SupplierName) {
                    this.SupplierName = SupplierName;
                }

                public Double getSGSTTaxAmmount() {
                    return this.SGSTTaxAmmount;
                }

                public void setSGSTTaxAmmount(Double SGSTTaxAmmount) {
                    this.SGSTTaxAmmount = SGSTTaxAmmount;
                }

                public String getCategoryName() {
                    return this.CategoryName;
                }

                public void setCategoryName(String CategoryName) {
                    this.CategoryName = CategoryName;
                }

                public String getHSNCode() {
                    return this.HSNCode;
                }

                public void setHSNCode(String HSNCode) {
                    this.HSNCode = HSNCode;
                }
            }
        }
    }


