package data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Proslava {

    public static Proslava getProslavaPoID(Proslava[] proslava, int id){
        for(Proslava proslavas : proslava){
            if(proslavas.getId() == id){
                return proslavas;
            }
        }
        return null;
    }

    public static Proslava[] getProslavaPoObjektu(Proslava[] proslava, Objekat objekat){
        ArrayList<Proslava> proslavaList = new ArrayList<>();
        for(Proslava proslavas : proslava){
            if(proslavas.getObjekat().getId() == objekat.getId()){
                proslavaList.add(proslavas);
            }
        }
        return proslavaList.toArray(new Proslava[0]);
    }

    public static Proslava[] getProslavaPoKlijentu(Proslava[] proslava, Klijent klijent){
        ArrayList<Proslava> proslavaList = new ArrayList<>();
        for(Proslava proslavas : proslava){
            if(proslavas.getKlijent().getId() == klijent.getId()){
                proslavaList.add(proslavas);
            }
        }
        return proslavaList.toArray(new Proslava[0]);
    }

    private int id;
    private Objekat objekat;
    private Klijent klijent;
    private Meni meni;
    private LocalDate datum;
    private int broj_gostiju;
    private float ukupna_cijena;
    private float uplacen_iznos;
    private String proslava_col;

    public Proslava(int id, Objekat objekat, Klijent klijent, Meni meni, LocalDate datum, int broj_gostiju, float ukupna_cijena, float uplacen_iznos,String proslava_col) {
        this.id = id;
        this.objekat = objekat;
        this.klijent = klijent;
        this.meni = meni;
        this.datum = datum;
        this.broj_gostiju = broj_gostiju;
        this.ukupna_cijena = ukupna_cijena;
        this.uplacen_iznos = uplacen_iznos;
        this.proslava_col = proslava_col;
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

    public Klijent getKlijent() {
        return klijent;
    }

    public void setKlijent(Klijent klijent) {
        this.klijent = klijent;
    }

    public Meni getMeni() {
        return meni;
    }

    public void setMeni(Meni meni) {
        this.meni = meni;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public int getBroj_gostiju() {
        return broj_gostiju;
    }

    public void setBroj_gostiju(int broj_gostiju) {
        this.broj_gostiju = broj_gostiju;
    }

    public float getUkupna_cijena() {
        return ukupna_cijena;
    }

    public void setUkupna_cijena(float ukupna_cijena) {
        this.ukupna_cijena = ukupna_cijena;
    }

    public float getUplacen_iznos() {
        return uplacen_iznos;
    }

    public void setUplacen_iznos(float uplacen_iznos) {
        this.uplacen_iznos = uplacen_iznos;
    }

    public String getProslavacol() {
        return proslava_col;
    }

    public void setProslavacol(String proslava_col) {
        this.proslava_col = proslava_col;
    }

    public boolean jeProsla(){
        return LocalDate.now().isAfter(datum);
    }

    public boolean jeOtkazana(){
        return proslava_col.equalsIgnoreCase("OTKAZAN");
    }

    public boolean jeIzmjenjiva(){
        return LocalDate.now().plusDays(3).isBefore(datum);
    }

    public boolean jePlacena(){
        return ukupna_cijena==uplacen_iznos;
    }

    @Override
    public String toString() {
        return "Proslava{" +
                "id=" + id +
                ", objekat=" + objekat +
                ", klijent=" + klijent +
                ", meni=" + meni +
                ", datum=" + datum +
                ", broj_gostiju=" + broj_gostiju +
                ", ukupna_cijena=" + ukupna_cijena +
                ", uplacen_iznos=" + uplacen_iznos +
                ", proslava_col='" + proslava_col + '\'' +
                '}';
    }
}
