package pl.edu.pwr.dg.jp.lab03.data;

public class Voucher {
    public enum Status {
        WAIT_FOR_ORGANIZER,
        READY_TO_BUY,
        PURCHASED,
        TO_REALIZE,
        DECLARED,
        REALIZED;
    }

    private int id;
    private String clientName;
    private String organizerName;
    private String offerParameters;
    private String info;
    private Status status;

    public Voucher(int id, String clientName, String organizerName, String offerParameters, String info, Status status) {
        this.id = id;
        this.clientName = clientName;
        this.organizerName = organizerName;
        this.offerParameters = offerParameters;
        this.info = info;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getOfferParameters() {
        return offerParameters;
    }

    public void setOfferParameters(String offerParameters) {
        this.offerParameters = offerParameters;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
