package data;

public class Vlasnik {

    public static Vlasnik getVlasnikPoId(Vlasnik[] vlasnici,int id){
        for(Vlasnik v : vlasnici){
            if(v.getId() == id)
                return v;
        }
        return null;
    }

    public static Vlasnik getVlasnikPoUsername(Vlasnik[] vlasnici, String username){
        for(Vlasnik vlasnik : vlasnici)
        {
            if(username.equals(vlasnik.getKorisnicko_ime()))
                return vlasnik;
        }
        return null;
    }


    private int id;
    private String ime;
    private String prezime;
    private String jmbg;
    private String broj_racuna;
    private String korisnicko_ime;
    private String lozinka;

    public Vlasnik(){}
    public Vlasnik(int id, String ime, String prezime, String jmbg, String broj_racuna, String korisnicko_ime, String lozinka) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.jmbg = jmbg;
        this.broj_racuna = broj_racuna;
        this.korisnicko_ime = korisnicko_ime;
        this.lozinka = lozinka;
    }

    public int getId() {
        return id;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getJmbg() {
        return jmbg;
    }

    public String getBroj_racuna() {
        return broj_racuna;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setBroj_racuna(String broj_racuna) {
        this.broj_racuna = broj_racuna;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    @Override
    public String toString() {
        return "Vlasnik{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", jmbg='" + jmbg + '\'' +
                ", broj_racuna='" + broj_racuna + '\'' +
                ", korisnicko_ime='" + korisnicko_ime + '\'' +
                '}';
    }
}
