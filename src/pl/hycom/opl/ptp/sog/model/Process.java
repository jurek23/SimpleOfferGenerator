package pl.hycom.opl.ptp.sog.model;

public enum Process {
    AKWIZYCJA("akwizycja"), UTRZYMANIE("utrzymanie");

    private String niceName;

    private Process(String niceName) {
        this.niceName = niceName;
    }

    public String toString() {
        return niceName;
    }
    
}
