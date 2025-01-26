package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Raspored {
    public static Raspored[] getRasporedPoProslavi(Raspored[] rasporeds,Proslava proslava){
        ArrayList<Raspored> rasporedList = new ArrayList<>();
        for(Raspored raspored:rasporeds){
            if(raspored.getProslava().getId() == proslava.getId()){
                rasporedList.add(raspored);
            }
        }
        return rasporedList.toArray(new Raspored[0]);
    }

    private Sto sto;
    private Proslava proslava;
    private String gosti;

    public Raspored(Sto sto, Proslava proslava, String gosti) {
        this.sto = sto;
        this.proslava = proslava;

        String[] sviGosti = gosti.split(",");
        Arrays.sort(sviGosti, Comparator.comparing(o -> o.trim().split(" ")[1]));

        this.gosti = String.join(", ", sviGosti);
    }

    public Sto getSto() {
        return sto;
    }

    public void setSto(Sto sto) {
        this.sto = sto;
    }

    public Proslava getProslava() {
        return proslava;
    }

    public void setProslava(Proslava proslava) {
        this.proslava = proslava;
    }

    public String getGosti() {
        return gosti;
    }

    public void setGosti(String gosti) {
        this.gosti = gosti;
    }
}
