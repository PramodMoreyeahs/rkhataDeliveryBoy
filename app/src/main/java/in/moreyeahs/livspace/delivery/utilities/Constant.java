package in.moreyeahs.livspace.delivery.utilities;

import in.moreyeahs.livspace.delivery.BuildConfig;

public class Constant {

    public static final String UPLOAD_URL = BuildConfig.apiEndpoint + "/api/AssignmentCopyUpload";
    public static final String UPLOAD_RETURN_URL = BuildConfig.apiEndpoint + "/api/itemimageupload/UploadKKReturnReplaceImages";
    public static final String AssignmentSettleURL = BuildConfig.apiEndpoint + "/api/DBoyAssignmentDeposit/UploadDboySignature";
    // cash management upload cheque image url
//    public static final String UPLOAD_CHEQUE = BuildConfig.apiEndpoint + "/api/MobileDelivery/CurrencyUploadChequeImageForMobile";
    public static final String UPLOAD_CHEQUE = BuildConfig.apiEndpoint + "/api/MobileDelivery/CurrencyUploadChequeImageForMobile";

    // hdfc parameters
    public static final String PARAMETER_SEP = "&";
    public static final String PARAMETER_EQUALS = "=";
    public static final String TRANS_URL = "https://test.ccavenue.com/transaction/initTrans";
}