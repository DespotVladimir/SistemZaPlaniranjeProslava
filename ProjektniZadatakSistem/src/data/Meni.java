package data;

import java.util.ArrayList;

public class Meni {
    public static Meni[] getMeniPoObjektu(Meni[] meniji,Objekat objekat)
    {
        ArrayList<Meni> menis = new ArrayList<>();
        for(Meni meni : meniji)
        {
            if(meni.getObjekat().getId() == objekat.getId())
                menis.add(meni);
        }
        return menis.toArray(new Meni[0]);
    }

    public static Meni getMeniPoID(Meni[] meniji, int id){
        for(Meni meni : meniji)
        {
            if(meni.getId() == id)
                return meni;
        }
        return null;
    }

    private int id;
    private Objekat objekat;
    private String opis;
    private float cijena_po_osobi;


    public Meni(){}
    public Meni(int id, Objekat objekat, String opis, float cijena_po_osobi) {
        this.id = id;
        this.objekat = objekat;
        this.opis = opis;
        this.cijena_po_osobi = cijena_po_osobi;
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

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public float getCijena_po_osobi() {
        return cijena_po_osobi;
    }

    public void setCijena_po_osobi(float cijena_po_osobi) {
        this.cijena_po_osobi = cijena_po_osobi;
    }
}
