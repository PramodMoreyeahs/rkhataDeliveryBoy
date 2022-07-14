package in.moreyeahs.livspace.delivery.model;

import com.google.gson.annotations.SerializedName;

public class CompanyInfoResponse {
    @SerializedName("companydetails")
    private Companydetails companydetails;
    @SerializedName("Status")
    private boolean Status;
    @SerializedName("Message")
    private String Message;

    public Companydetails getCompanyDetails() {
        return companydetails;
    }

    public void setCompanyDetails(Companydetails companydetails) {
        this.companydetails = companydetails;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public class Companydetails {
        @SerializedName("Id")
        private int Id;
        @SerializedName("Name")
        private String Name;
        @SerializedName("Contact")
        private String Contact;
        @SerializedName("Email")
        private String Email;
        @SerializedName("Website")
        private String Website;
        @SerializedName("LogoUrl")
        private String LogoUrl;
        @SerializedName("GatewayName")
        private String GatewayName;
        @SerializedName("GatewayURL")
        private String GatewayURL;
        @SerializedName("HDFCWorkingKey")
        private String HDFCWorkingKey;
        @SerializedName("HDFCAccessCode")
        private String HDFCAccessCode;
        @SerializedName("HDFCMerchantId")
        private String HDFCMerchantId;
        @SerializedName("TrupayAccessToken")
        private String TrupayAccessToken;
        @SerializedName("TruepayCollectorId")
        private String TruepayCollectorId;
        @SerializedName("TruepayPaymentMethod")
        private String TruepayPaymentMethod;
        @SerializedName("ePayLaterEndpoint")
        private String ePayLaterEndpoint;
        @SerializedName("E_PAY_LATER_URL")
        private String EPAYLATERURL;
        @SerializedName("ENCODED_KEY")
        private String ENCODEDKEY;
        @SerializedName("BEARER_TOKEN")
        private String BEARERTOKEN;
        @SerializedName("IV")
        private String IV;
        @SerializedName("M_CODE")
        private String MCODE;
        @SerializedName("category")
        private String category;
        @SerializedName("CreatedDate")
        private String CreatedDate;
        @SerializedName("ModifiedDate")
        private String ModifiedDate;
        @SerializedName("IsActive")
        private boolean IsActive;
        @SerializedName("IsDeleted")
        private boolean IsDeleted;
        @SerializedName("CreatedBy")
        private int CreatedBy;
        @SerializedName("ModifiedBy")
        private String ModifiedBy;
        @SerializedName("redirect_url")
        private String redirect_url;
        @SerializedName("cancel_url")
        private String cancel_url;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getContact() {
            return Contact;
        }

        public void setContact(String Contact) {
            this.Contact = Contact;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getWebsite() {
            return Website;
        }

        public void setWebsite(String Website) {
            this.Website = Website;
        }

        public String getLogoUrl() {
            return LogoUrl;
        }

        public void setLogoUrl(String LogoUrl) {
            this.LogoUrl = LogoUrl;
        }

        public String getGatewayName() {
            return GatewayName;
        }

        public void setGatewayName(String GatewayName) {
            this.GatewayName = GatewayName;
        }

        public String getGatewayURL() {
            return GatewayURL;
        }

        public void setGatewayURL(String GatewayURL) {
            this.GatewayURL = GatewayURL;
        }

        public String getHDFCWorkingKey() {
            return HDFCWorkingKey;
        }

        public void setHDFCWorkingKey(String HDFCWorkingKey) {
            this.HDFCWorkingKey = HDFCWorkingKey;
        }

        public String getHDFCAccessCode() {
            return HDFCAccessCode;
        }

        public void setHDFCAccessCode(String HDFCAccessCode) {
            this.HDFCAccessCode = HDFCAccessCode;
        }

        public String getHDFCMerchantId() {
            return HDFCMerchantId;
        }

        public void setHDFCMerchantId(String HDFCMerchantId) {
            this.HDFCMerchantId = HDFCMerchantId;
        }

        public String getTrupayAccessToken() {
            return TrupayAccessToken;
        }

        public void setTrupayAccessToken(String TrupayAccessToken) {
            this.TrupayAccessToken = TrupayAccessToken;
        }

        public String getTruepayCollectorId() {
            return TruepayCollectorId;
        }

        public void setTruepayCollectorId(String TruepayCollectorId) {
            this.TruepayCollectorId = TruepayCollectorId;
        }

        public String getTruepayPaymentMethod() {
            return TruepayPaymentMethod;
        }

        public void setTruepayPaymentMethod(String TruepayPaymentMethod) {
            this.TruepayPaymentMethod = TruepayPaymentMethod;
        }

        public String getEPayLaterEndpoint() {
            return ePayLaterEndpoint;
        }

        public void setEPayLaterEndpoint(String ePayLaterEndpoint) {
            this.ePayLaterEndpoint = ePayLaterEndpoint;
        }

        public String getEPAYLATERURL() {
            return EPAYLATERURL;
        }

        public void setEPAYLATERURL(String EPAYLATERURL) {
            this.EPAYLATERURL = EPAYLATERURL;
        }

        public String getENCODEDKEY() {
            return ENCODEDKEY;
        }

        public void setENCODEDKEY(String ENCODEDKEY) {
            this.ENCODEDKEY = ENCODEDKEY;
        }

        public String getBEARERTOKEN() {
            return BEARERTOKEN;
        }

        public void setBEARERTOKEN(String BEARERTOKEN) {
            this.BEARERTOKEN = BEARERTOKEN;
        }

        public String getIV() {
            return IV;
        }

        public void setIV(String IV) {
            this.IV = IV;
        }

        public String getMCODE() {
            return MCODE;
        }

        public void setMCODE(String MCODE) {
            this.MCODE = MCODE;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getModifiedDate() {
            return ModifiedDate;
        }

        public void setModifiedDate(String ModifiedDate) {
            this.ModifiedDate = ModifiedDate;
        }

        public boolean isIsActive() {
            return IsActive;
        }

        public void setIsActive(boolean IsActive) {
            this.IsActive = IsActive;
        }

        public boolean isIsDeleted() {
            return IsDeleted;
        }

        public void setIsDeleted(boolean IsDeleted) {
            this.IsDeleted = IsDeleted;
        }

        public int getCreatedBy() {
            return CreatedBy;
        }

        public void setCreatedBy(int CreatedBy) {
            this.CreatedBy = CreatedBy;
        }

        public String getModifiedBy() {
            return ModifiedBy;
        }

        public void setModifiedBy(String ModifiedBy) {
            this.ModifiedBy = ModifiedBy;
        }

        public String getRedirect_url() {
            return redirect_url;
        }

        public void setRedirect_url(String redirect_url) {
            this.redirect_url = redirect_url;
        }

        public String getCancel_url() {
            return cancel_url;
        }

        public void setCancel_url(String cancel_url) {
            this.cancel_url = cancel_url;
        }
    }
}
