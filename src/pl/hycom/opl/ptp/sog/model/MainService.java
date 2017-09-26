package pl.hycom.opl.ptp.sog.model;

public enum MainService {
    NEOI0010("Neostrada"), TRPL0010("3P");
    private String value;

    private MainService(String value) {
        this.value = value;
    }

    public static MainService byValue(String value) {
        for (MainService mse : MainService.values()) {
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
