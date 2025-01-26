package data;

import java.util.ArrayList;

public class Sto {
    public static Sto[] StoloviPoObjektu(Sto[] stolovi,Objekat objekat) {
        ArrayList<Sto> lista = new ArrayList<Sto>();
        for(Sto sto: stolovi)
        {
            if(sto.getObjekat().getId() == objekat.getId())
                lista.add(sto);
        }
        return lista.toArray(new Sto[0]);
    }

    public static Sto getStoPoID(Sto[] stolovi,int id){
        for(Sto sto: stolovi)
        {
            if(sto.getId() == id)
                return sto;
        }
        return null;
    }

    private int id;
    private Objekat objekat;
    private int broj_mjesta;

    public Sto(int id, Objekat objekat, int broj_mjesta) {
        this.id = id;
        this.objekat = objekat;
        this.broj_mjesta = broj_mjesta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public void setObjekat(Objekat objekat) {
        this.objekat = objekat;
    }

    public int getBroj_mjesta() {
        return broj_mjesta;
    }

    public void setBroj_mjesta(int broj_mjesta) {
        this.broj_mjesta = broj_mjesta;
    }
}
