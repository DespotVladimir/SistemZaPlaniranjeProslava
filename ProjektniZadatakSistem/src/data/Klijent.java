package data;

public class Klijent {

    public static Klijent getKlijentPoUsername(Klijent[] klijenti,String username)
    {
        for(Klijent klijent : klijenti)
        {
            if(username.equals(klijent.getKorisnicko_ime()))
                return klijent;
        }
        return null;
    }

    public static Klijent getKlijentPoID(Klijent[] klijenti,int id)
    {
        for(Klijent klijent : klijenti)
        {
            if(klijent.getId() == id)
                return klijent;
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



    public Klijent() {}
    public Klijent(int id, String ime, String prezime, String jmbg, String broj_racuna, String korisnicko_ime, String lozinka) {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public void setBroj_racuna(String broj_racuna) {
        this.broj_racuna = broj_racuna;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    @Override
    public String toString() {
        return "Klijent{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", jmbg='" + jmbg + '\'' +
                ", broj_racuna='" + broj_racuna + '\'' +
                ", korisnicko_ime='" + korisnicko_ime + '\'' +
                '}';
    }
}
