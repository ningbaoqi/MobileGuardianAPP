package ningbaoqi.com.mobileguardianapp.communicationguard.bean;

/**
 * Created by ningbaoqi on 18-4-24.
 */

public class BlackNumberInfo {
    /**
     * 黑名单电话号码
     * */
    private String number;
    /**
     * 黑名单拦截模式
     * */
    private String mode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
