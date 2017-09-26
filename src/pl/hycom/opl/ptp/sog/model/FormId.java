package pl.hycom.opl.ptp.sog.model;

public enum FormId {
    NEO_INS("neo-ins"), NEO_MOD("neo-mod"), NEO_INS_BIZ("neo-ins-biz"), NEO_MOD_BIZ("neo-mod-biz"), TRPL_MOD("3p-mod"), TRPL_INS(
            "3p-ins"), ;
    private String value;

    private FormId(String value) {
        this.value = value;
    }

    public static FormId byValue(String value) {
        for (FormId mse : FormId.values()) {
            if (value.equalsIgnoreCase(mse.getValue())) {
                return mse;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

}
