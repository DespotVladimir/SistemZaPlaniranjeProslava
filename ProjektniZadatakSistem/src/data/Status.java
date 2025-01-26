package data;

public enum Status{
    NA_CEKANJU("NA CEKANJU"),
    ODOBREN("ODOBREN"),
    ODBIJEN("ODBIJEN");

    private String status;
    Status(String status){this.status=status;}

    @Override
    public String toString() {
        return status;
    }
}
