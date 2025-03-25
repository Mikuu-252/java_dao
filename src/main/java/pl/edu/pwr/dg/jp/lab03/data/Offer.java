package pl.edu.pwr.dg.jp.lab03.data;

public class Offer {
    private int id;
    private String parameters;

    public Offer(int id, String parameters) {
        this.id = id;
        this.parameters = parameters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
