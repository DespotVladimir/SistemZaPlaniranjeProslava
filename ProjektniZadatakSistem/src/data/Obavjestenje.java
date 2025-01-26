package data;

public class Obavjestenje {
    public static Obavjestenje getObavjestenjePoObjektu(Obavjestenje[] obn,Objekat ob)
    {
        for(Obavjestenje obj : obn)
        {
            if(ob.equals(obj.getObjekat()))
            {
                return obj;
            }
        }
        return null;
    }

    private int id;
    private Objekat objekat;
    private String tekst;

    public Obavjestenje() {}
    public Obavjestenje(int id, Objekat objekat, String tekst) {
        this.id = id;
        this.objekat = objekat;
        this.tekst = tekst;
    }

    public int getId() {
        return id;
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
}
