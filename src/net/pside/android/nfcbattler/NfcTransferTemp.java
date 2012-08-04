
package net.pside.android.nfcbattler;

public class NfcTransferTemp {
    private static Boolean isSent = false;
    private static Boolean isReceived = false;

    public Boolean getIsSent() {
        return isSent;
    }

    public void setIsSent(Boolean isSent) {
        NfcTransferTemp.isSent = isSent;
    }

    public Boolean getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(Boolean isReceived) {
        NfcTransferTemp.isReceived = isReceived;
    }
}
