package data;

import java.time.LocalDate;
import java.util.ArrayList;

public class Objekat {

    public static Objekat getObjekatPoID(Objekat[] objekti,int id)
    {
        for(Objekat br : objekti)
        {
            if(br.getId()==id)
            {
                return br;
            }
        }
        return null;
    }

    public static Objekat[] getObjektiPoVlasniku(Objekat[] objekti,Vlasnik vlasnik){
        java.util.ArrayList<Objekat> lista = new java.util.ArrayList<Objekat>();
        for(Objekat br : objekti)
        {
            if(br.getVlasnik().getId()==vlasnik.getId())
            {
                lista.add(br);
            }
        }
        return lista.toArray(new Objekat[0]);
    }



    private int id;
    private Vlasnik vlasnik;
    private String naziv;
    private float cijena_rezervacije;
    private String grad;
    private String adresa;
    private int broj_mjesta;
    private int broj_stolova;
    private String datumi;
    private float zarada;
    private Status status;
    private ArrayList<LocalDate> slobodniDatumi;

    public Objekat(int id, Vlasnik vlasnik, String naziv, float cijena_rezervacije,
                   String grad, String adresa, int broj_mjesta, int broj_stolova,
                   String datumi, float zarada, Status status) {
        this.id = id;
        this.vlasnik = vlasnik;
        this.naziv = naziv;
        this.cijena_rezervacije = cijena_rezervacije;
        this.grad = grad;
        this.adresa = adresa;
        this.broj_mjesta = broj_mjesta;
        this.broj_stolova = broj_stolova;
        this.datumi = datumi;
        this.zarada = zarada;
        this.status = status;

        slobodniDatumi =new ArrayList<>();
        try{
            String[] nizDatuma = datumi.split(";");
            for(String datum : nizDatuma){
                LocalDate d = LocalDate.parse(datum);
                slobodniDatumi.add(d);
            }
        }catch(Exception _){

        }

    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setVlasnik(Vlasnik vlasnik) {
        this.vlasnik = vlasnik;
    }

    public void setZarada(float zarada) {
        this.zarada = zarada;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setCijena_rezervacije(float cijena_rezervacije) {
        this.cijena_rezervacije = cijena_rezervacije;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public void setBroj_mjesta(int broj_mjesta) {
        this.broj_mjesta = broj_mjesta;
    }

    public void setBroj_stolova(int broj_stolova) {
        this.broj_stolova = broj_stolova;
    }

    public void setDatumi(String datumi) {
        this.datumi = datumi;
        slobodniDatumi.clear();
        try{
            String[] nizDatuma = datumi.split(";");
            for(String datum : nizDatuma){
                LocalDate d = LocalDate.parse(datum);
                slobodniDatumi.add(d);
            }
        }catch(Exception _){}

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Vlasnik getVlasnik() {
        return vlasnik;
    }

    public String getNaziv() {
        return naziv;
    }

    public float getCijena_rezervacije() {
        return cijena_rezervacije;
    }

    public String getGrad() {
        return grad;
    }

    public String getAdresa() {
        return adresa;
    }

    public int getBroj_mjesta() {
        return broj_mjesta;
    }

    public int getBroj_stolova() {
        return broj_stolova;
    }

    public String getDatumi() {
        return datumi;
    }

    public float getZarada() {
        return zarada;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDate[] getSlobodniDatumi() {
        return slobodniDatumi.toArray(new LocalDate[0]);
    }

    public void setSlobodniDatumi(ArrayList<LocalDate> slobodniDatumi) {
        this.slobodniDatumi = slobodniDatumi;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Objekat objekat) {
            return this.id == objekat.id;
        }
        return false;
    }
}