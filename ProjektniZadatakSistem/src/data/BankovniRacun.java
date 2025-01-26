package data;

public class BankovniRacun {
    public static BankovniRacun getBankovniRacunPoRacunu(BankovniRacun[] bankovniRacuni,String racun)
    {
        for(BankovniRacun br : bankovniRacuni)
        {
            if(br.getBroj_racuna().equals(racun))
            {
                return br;
            }
        }
        return null;
    }


    private int id;
    private String broj_racuna;
    private String jmbg;
    private float stanje;

    BankovniRacun() {}
    BankovniRacun(int id, String broj_racuna, String jmbg, float stanje) {
        this.id = id;
        this.broj_racuna = broj_racuna;
        this.jmbg = jmbg;
        this.stanje = stanje;
    }

    public int getId() {
        return id;
    }

    public String getBroj_racuna() {
        return broj_racuna;
    }

    public String getJmbg() {
        return jmbg;
    }

    public float getStanje() {
        return stanje;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBroj_racuna(String broj_racuna) {
        this.broj_racuna = broj_racuna;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public void setStanje(float stanje) {
        this.stanje = stanje;
    }

    @Override
    public String toString() {
        return "BankovniRacun{" +
                "id=" + id +
                ", broj_racuna='" + broj_racuna + '\'' +
                ", jmbg='" + jmbg + '\'' +
                ", stanje=" + stanje +
                '}';
    }
}
