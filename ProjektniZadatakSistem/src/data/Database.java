package data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class Database {
    private static final String DB_user = "root";
    private static final String DB_password = "";
    private static final int port = 3306;
    private static final String DB_name = "sistemzaplaniranjeproslava";
    private String connectionUrl;
    private Connection connection;


    private ArrayList<Admin> admin;
    private ArrayList<BankovniRacun> bankovniRacuni;
    private ArrayList<Klijent> klijenti;
    private ArrayList<Meni> meni;
    private ArrayList<Obavjestenje> obavjestenje;
    private ArrayList<Vlasnik> vlasnici;
    private ArrayList<Objekat> objekti;
    private ArrayList<Sto> stolovi;
    private ArrayList<Proslava> proslava;
    private ArrayList<Raspored> raspored;


    public Database() throws SQLException {

        vlasnici = new ArrayList<>();
        admin = new ArrayList<>();
        bankovniRacuni = new ArrayList<>();
        klijenti = new ArrayList<>();
        meni = new ArrayList<>();
        obavjestenje = new ArrayList<>();
        objekti = new ArrayList<>();
        stolovi = new ArrayList<>();
        proslava = new ArrayList<>();
        raspored = new ArrayList<>();

        connectionUrl = "jdbc:mysql://localhost" + ":" + port + "/" + DB_name;

        DBConnect();
        databaseGetAdmins();
        databaseGetVlasnik();
        databaseGetBankovniRacun();
        databaseGetKlijent();
        databaseGetObjekat();
        databaseGetObavjestenje();
        databaseGetMeni();
        databaseGetSto();
        databaseGetProslava();
        databaseGetRaspored();
    }

    private void DBConnect() throws SQLException /*, ClassNotFoundException*/ {
        //Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
    }


    // Update iz baze

    private void databaseGetKlijent()throws SQLException {
        klijenti.clear();
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM klijent";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String ime = resultSet.getString("ime");
            String prezime = resultSet.getString("prezime");
            String jmbg = resultSet.getString("jmbg");
            String broj_racuna = resultSet.getString("broj_racuna");
            String korisnicko_ime = resultSet.getString("korisnicko_ime");
            String lozinka = resultSet.getString("lozinka");

            Klijent klijent = new Klijent(id,ime,prezime,jmbg,broj_racuna,korisnicko_ime,lozinka);
            klijenti.add(klijent);
        }
    }

    private void databaseGetBankovniRacun() throws SQLException {
        bankovniRacuni.clear();
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM `bankovni racun`";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String broj_racuna = resultSet.getString("broj_racuna");
            String jmbg = resultSet.getString("jmbg");
            float stanje = resultSet.getFloat("stanje");

            BankovniRacun bn = new BankovniRacun(id, broj_racuna, jmbg, stanje);
            bankovniRacuni.add(bn);
        }
    }


    private void databaseGetAdmins() throws SQLException {
        admin.clear();
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM admin";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String ime = resultSet.getString("ime");
            String prezime = resultSet.getString("prezime");
            String korisnicko_ime = resultSet.getString("korisnicko_ime");
            String lozinka = resultSet.getString("lozinka");
            admin.add(new Admin(id,ime,prezime,korisnicko_ime,lozinka));
        }
    }

    private void databaseGetVlasnik() throws SQLException {
        vlasnici.clear();
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM vlasnik";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String ime = resultSet.getString("ime");
            String prezime = resultSet.getString("prezime");
            String jmbg = resultSet.getString("jmbg");
            String broj_racuna = resultSet.getString("broj_racuna");
            String korisnicko_ime = resultSet.getString("korisnicko_ime");
            String lozinka = resultSet.getString("lozinka");

            Vlasnik vlasnik = new Vlasnik(id,ime,prezime,jmbg,broj_racuna,korisnicko_ime,lozinka);

            vlasnici.add(vlasnik);
        }

    }

    private void databaseGetObjekat() throws SQLException {
        objekti.clear();
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM objekat";
        ResultSet resultSet = statement.executeQuery(SQLQuery);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            Vlasnik vlasnik = Vlasnik.getVlasnikPoId(this.getVlasnik(),resultSet.getInt("Vlasnik_id"));
            String naziv = resultSet.getString("naziv");
            float cijena_rezervacije = resultSet.getFloat("cijena_rezervacije");
            String grad = resultSet.getString("grad");
            String adresa = resultSet.getString("adresa");
            int brojMjesta = resultSet.getInt("broj_mjesta");
            int brojStolova =resultSet.getInt("broj_stolova");
            String datumi = resultSet.getString("datumi");
            float zarada = resultSet.getFloat("zarada");
            Status status = Status.valueOf(resultSet.getString("status").replace(" ","_"));

            Objekat objekat = new Objekat(id,vlasnik,naziv,cijena_rezervacije,grad,adresa,brojMjesta,brojStolova,datumi,zarada,status);

            objekti.add(objekat);
        }

    }

    private void databaseGetObavjestenje() throws SQLException {
        obavjestenje.clear();
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM obavjestenje";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            Objekat objekat = Objekat.getObjekatPoID(getObjekat(),resultSet.getInt("Objekat_id"));
            String text = resultSet.getString("tekst");

            Obavjestenje obavjestenjes = new Obavjestenje(id,objekat,text);
            obavjestenje.add(obavjestenjes);
        }
    }

    private void databaseGetMeni() throws SQLException{
        meni.clear();

        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM meni";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            Objekat objekat = Objekat.getObjekatPoID(getObjekat(),resultSet.getInt("Objekat_id"));
            String text = resultSet.getString("opis");
            float cijenaPoOsobi = resultSet.getFloat("cijena_po_osobi");

            Meni menij = new Meni(id,objekat,text,cijenaPoOsobi);
            meni.add(menij);
        }
    }

    private void databaseGetSto() throws SQLException{
        stolovi.clear();

        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM sto";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            Objekat objekat = Objekat.getObjekatPoID(getObjekat(),resultSet.getInt("Objekat_id"));
            int brojMjesta = resultSet.getInt("broj_mjesta");

            Sto sto = new Sto(id,objekat,brojMjesta);
            stolovi.add(sto);
        }
    }

    private void databaseGetProslava() throws SQLException {
        proslava.clear();

        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM proslava";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            Objekat objekat = Objekat.getObjekatPoID(getObjekat(),resultSet.getInt("Objekat_id"));
            Klijent klijent = Klijent.getKlijentPoID(getKlijent(),resultSet.getInt("Klijent_id"));
            Meni meni = Meni.getMeniPoID(getMeni(),resultSet.getInt("Meni_id"));
            String proslava_col= resultSet.getString("Proslavacol");

            LocalDate datum = LocalDate.parse(resultSet.getString("datum"));

            int broj_gostiju = resultSet.getInt("broj_gostiju");
            int ukupna_cijena = resultSet.getInt("ukupna_cijena");
            int uplacen_iznos = resultSet.getInt("uplacen_iznos");

            Proslava proslavas = new Proslava(id,objekat,klijent,meni,datum,broj_gostiju,ukupna_cijena,uplacen_iznos,proslava_col);
            proslava.add(proslavas);
        }

    }

    private void databaseGetRaspored() throws SQLException {
        raspored.clear();
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM raspored";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next()) {
            Sto Stoid = Sto.getStoPoID(getSto(),resultSet.getInt("idSto"));

            Proslava proslavas = Proslava.getProslavaPoID(getProslava(),resultSet.getInt("idProslava"));

            String gosti = resultSet.getString("gosti");
            Raspored raspored1 = new Raspored(Stoid,proslavas,gosti);
            raspored.add(raspored1);
        }
    }

    // Getteri za tabele

    public Admin[] getAdmin()
    {
        return admin.toArray(new Admin[0]);
    }


    public Klijent[] getKlijent()
    {
        return klijenti.toArray(new Klijent[0]);
    }

    public Vlasnik[] getVlasnik()
    {
        return vlasnici.toArray(new Vlasnik[0]);
    }

    public BankovniRacun[] getBankovniRacun()
    {
        return bankovniRacuni.toArray(new BankovniRacun[0]);
    }

    public Objekat[] getObjekat(){
        return objekti.toArray(new Objekat[0]);
    }

    public Obavjestenje[] getObavjestenje()
    {
        return obavjestenje.toArray(new Obavjestenje[0]);
    }

    public Meni[] getMeni(){
        return meni.toArray(new Meni[0]);
    }

    public Sto[] getSto(){
        return stolovi.toArray(new Sto[0]);
    }

    public Proslava[] getProslava(){
        return proslava.toArray(new Proslava[0]);
    }

    public Raspored[] getRaspored(){
        return raspored.toArray(new Raspored[0]);
    }

    // Update za bazu

    public void dodajVlasnikaUBazu(Vlasnik vlasnik) throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "INSERT INTO `vlasnik`(`ime`, `prezime`, `jmbg`, `broj_racuna`, `korisnicko_ime`, `lozinka`) " +
                "VALUES ('"+vlasnik.getIme()+"','"+vlasnik.getPrezime()+"','"+vlasnik.getJmbg()+"','"+vlasnik.getBroj_racuna()+"','"+vlasnik.getKorisnicko_ime()+"','"+vlasnik.getLozinka()+"')";

        statement.executeUpdate(SQLQuery);
        vlasnici.add(vlasnik);
    }

    public void dodajKlijentaUBazu(Klijent klijent) throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "INSERT INTO `klijent`(`ime`, `prezime`, `jmbg`, `broj_racuna`, `korisnicko_ime`, `lozinka`) " +
                "VALUES ('"+klijent.getIme()+"','"+klijent.getPrezime()+"','"+klijent.getJmbg()+"','"+klijent.getBroj_racuna()+"','"+klijent.getKorisnicko_ime()+"','"+klijent.getLozinka()+"')";

        statement.executeUpdate(SQLQuery);
        klijenti.add(klijent);
    }

    public void promjeniLozinku(Admin admin,String novaSifra) throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "UPDATE `admin` SET `lozinka`='"+novaSifra+"' " +
                "WHERE `korisnicko_ime`='"+admin.getKorisnicko_ime()+"'";
        statement.executeUpdate(SQLQuery);
        admin.setLozinka(novaSifra);
    }

    public void promjeniLozinku(Vlasnik vlasnik,String novaSifra) throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "UPDATE `vlasnik` SET `lozinka`='"+novaSifra+"' " +
                "WHERE `korisnicko_ime`='"+vlasnik.getKorisnicko_ime()+"'";
        statement.executeUpdate(SQLQuery);
        vlasnik.setLozinka(novaSifra);
    }

    public void promjeniLozinku(Klijent klijent,String novaSifra) throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "UPDATE `klijent` SET `lozinka`='"+novaSifra+"' " +
                "WHERE `korisnicko_ime`='"+klijent.getKorisnicko_ime()+"'";
        statement.executeUpdate(SQLQuery);
        klijent.setLozinka(novaSifra);
    }

    public void odobriObjekat(Objekat objekat) throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "UPDATE `objekat` SET `status`='"+Status.ODOBREN+"' " +
                "WHERE `id`='"+objekat.getId()+"'";

        Obavjestenje ob = Obavjestenje.getObavjestenjePoObjektu(getObavjestenje(),objekat);
        if(ob==null)
            dodajObavjestenje(new Obavjestenje(3,objekat,""));
        else {
            updateObavjestenje(ob,"");
        }

        statement.executeUpdate(SQLQuery);
        objekat.setStatus(Status.ODOBREN);


    }

    public void odbijObjekat(Objekat objekat,String komentar) throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "UPDATE `objekat` SET `status`='"+Status.ODBIJEN+"' " +
                "WHERE `id`='"+objekat.getId()+"'";
        statement.executeUpdate(SQLQuery);
        Obavjestenje ob = Obavjestenje.getObavjestenjePoObjektu(getObavjestenje(),objekat);
        if(ob==null)
            dodajObavjestenje(new Obavjestenje(3,objekat,komentar));
        else {

            updateObavjestenje(ob,komentar);
        }

        objekat.setStatus(Status.ODBIJEN);
    }

    public void dodajObavjestenje(Obavjestenje obavjestenje) throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "INSERT INTO `obavjestenje`(`Objekat_id`, `tekst`)" +
                " VALUES ('"+obavjestenje.getObjekat().getId()+"','"+obavjestenje.getTekst()+"')";
        statement.executeUpdate(SQLQuery);
        databaseGetObavjestenje();
    }

    public void updateObavjestenje(Obavjestenje obavjestenje,String updateText) throws SQLException {
        Statement statement = connection.createStatement();
        obavjestenje.setTekst(updateText);
        String SQLQuery = "UPDATE `obavjestenje` SET `tekst`='"+obavjestenje.getTekst()+"' WHERE `id`='"+obavjestenje.getId()+"'";
        statement.executeUpdate(SQLQuery);
    }

    public void obrisiObjekat(Objekat objekat) throws SQLException {
        String SQLQuery;
        Statement statement= connection.createStatement();
        Obavjestenje obavjestenje = Obavjestenje.getObavjestenjePoObjektu(getObavjestenje(),objekat);
        Sto[] stolovi = Sto.StoloviPoObjektu(getSto(),objekat);
        Meni[] menis = Meni.getMeniPoObjektu(getMeni(),objekat);

        Proslava[] sveProslave = Proslava.getProslavaPoObjektu(getProslava(),objekat);
        for(Proslava p: sveProslave){
            SQLQuery = "DELETE FROM `raspored` "+
                    "WHERE `idProslava`='"+p.getId()+"'";
            statement.executeUpdate(SQLQuery);
        }

        SQLQuery = "DELETE FROM `proslava` "+
                "WHERE `Objekat_id`='"+objekat.getId()+"'";
        statement.executeUpdate(SQLQuery);

        if(menis.length!=0)
        {
            for(Meni meni:menis){
                SQLQuery = "DELETE FROM `meni` "+
                        "WHERE `id`='"+meni.getId()+"'";
                statement.executeUpdate(SQLQuery);
            }
            databaseGetMeni();
        }

        if(stolovi.length!=0){
            for(Sto sto:stolovi){
                SQLQuery = "DELETE FROM `raspored` "+
                        "WHERE `idSto`='"+sto.getId()+"'";
                statement.executeUpdate(SQLQuery);

                SQLQuery = "DELETE FROM `sto` "+
                        "WHERE `id`='"+sto.getId()+"'";
                statement.executeUpdate(SQLQuery);
            }
            databaseGetSto();
        }
        if(obavjestenje!=null)
        {
            SQLQuery = "DELETE FROM `obavjestenje` "+
                    "WHERE `id`='"+obavjestenje.getId()+"'";
            statement.executeUpdate(SQLQuery);
            databaseGetObavjestenje();
        }
        SQLQuery = "DELETE FROM `objekat` "+
                "WHERE `id`='"+objekat.getId()+"'";
        statement.executeUpdate(SQLQuery);
        databaseGetObjekat();
    }

    public void noviObjekat(Objekat objekat,Meni[] menis,Sto[] stolovi) throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "INSERT INTO `objekat`" +
                "(`Vlasnik_id`, `naziv`, `cijena_rezervacije`, `grad`, `adresa`, `broj_mjesta`, `broj_stolova`, `datumi`, `zarada`, `status`) " +
                "VALUES ('"+objekat.getVlasnik().getId()+"','"+objekat.getNaziv()+"','"+objekat.getCijena_rezervacije()+
                "','"+objekat.getGrad()+"','"+objekat.getAdresa()+"','"+objekat.getBroj_mjesta()+
                "','"+objekat.getBroj_stolova()+"','"+objekat.getDatumi()+"','"+objekat.getZarada()+"','NA CEKANJU')";
        statement.executeUpdate(SQLQuery);

        SQLQuery = "SELECT * FROM objekat WHERE `Vlasnik_id`='"+objekat.getVlasnik().getId()+"' AND `naziv`='"+objekat.getNaziv()+"' AND adresa='"+objekat.getAdresa()+"'";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        resultSet.next();
        int objekatID = resultSet.getInt("id");
        objekat.setId(objekatID);

        for(Sto sto:stolovi){
            SQLQuery = "INSERT INTO `sto`(`Objekat_id`, `broj_mjesta`) " +
                    "VALUES ('"+objekatID+"','"+sto.getBroj_mjesta()+"')";
            statement.executeUpdate(SQLQuery);
        }

        for(Meni meni : menis)
        {
            SQLQuery = "INSERT INTO `meni`(`Objekat_id`, `opis`, `cijena_po_osobi`) " +
                    "VALUES ('"+objekatID+"','"+meni.getOpis()+"','"+meni.getCijena_po_osobi()+"')";
            statement.executeUpdate(SQLQuery);
        }

        databaseGetObjekat();
        databaseGetMeni();
        databaseGetSto();
    }

    public void updateObjekat(Objekat objekat,Meni meni,Sto[] stolovi) throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "UPDATE `objekat` SET " +
                "naziv='"+objekat.getNaziv()+"',cijena_rezervacije='"+objekat.getCijena_rezervacije()+
                "',grad='"+objekat.getGrad()+"',adresa='"+objekat.getAdresa()+"',broj_mjesta='"+objekat.getBroj_mjesta()+
                "',broj_stolova='"+objekat.getBroj_stolova()+"',status='NA CEKANJU' "+
                "WHERE id="+objekat.getId();

        statement.executeUpdate(SQLQuery);

        int objekatID = objekat.getId();
        objekat.setId(objekatID);

        SQLQuery = "DELETE FROM `sto` WHERE Objekat_id='"+objekatID+"'";
        statement.executeUpdate(SQLQuery);

        for(Sto sto:stolovi){
            SQLQuery = "INSERT INTO `sto`(`Objekat_id`, `broj_mjesta`) " +
                    "VALUES ('"+objekatID+"','"+sto.getBroj_mjesta()+"')";
            statement.executeUpdate(SQLQuery);
        }

        if(Meni.getMeniPoObjektu(getMeni(),objekat)==null)
        {
            SQLQuery = "INSERT INTO `meni`(`Objekat_id`, `opis`, `cijena_po_osobi`) " +
                    "VALUES ('"+objekatID+"','"+meni.getOpis()+"','"+meni.getCijena_po_osobi()+"')";
        }
        else
            SQLQuery = "UPDATE `meni` SET `opis`='"+meni.getOpis()+"',`cijena_po_osobi`='"+meni.getCijena_po_osobi()+"' " +
                "WHERE Objekat_id='"+objekatID+"'";
        statement.executeUpdate(SQLQuery);

        databaseGetObjekat();
        databaseGetMeni();
        databaseGetSto();

    }

    public void promjeniDatume(Set<LocalDate> odabraniDani, Objekat objekat) throws SQLException {

        StringBuilder sb = new StringBuilder();
        for(LocalDate localDate : odabraniDani)
        {
            sb.append(localDate.toString()).append(";");
        }
        if(!sb.isEmpty())
            sb.replace(sb.length()-1,sb.length(), "");
        String SQLQuery="UPDATE `objekat` SET datumi='" + sb.toString()+ "' WHERE id='"+objekat.getId()+"'";
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQLQuery);

        ArrayList<LocalDate> sd = new ArrayList<>(odabraniDani);
        objekat.setSlobodniDatumi(sd);
    }


    public void zavrsiOdobravanjeObjekta(Objekat objekat) throws SQLException {
        String SQLQuery= "UPDATE `obavjestenje` SET tekst='ODOBREN' WHERE Objekat_id='"+objekat.getId()+"'";
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQLQuery);

        Obavjestenje obavjestenje = Obavjestenje.getObavjestenjePoObjektu(getObavjestenje(),objekat);
        if(obavjestenje!=null)
            obavjestenje.setTekst("ODOBREN");
    }

    public void postaviProslavu(Klijent klijent, Proslava proslave) throws SQLException {
        String SQLQuery= "INSERT INTO `proslava`" +
                "(`Objekat_id`, `Klijent_id`, `Meni_id`, `Proslavacol`, `datum`, `broj_gostiju`, `ukupna_cijena`, `uplacen_iznos`) " +
                "VALUES ('"+proslave.getObjekat().getId()+"','"+proslave.getKlijent().getId()+"','"+proslave.getMeni().getId()+"'," +
                "'','"+proslave.getDatum()+"','"+proslave.getBroj_gostiju()+"','"+ proslave.getUkupna_cijena() +"','"+ proslave.getUplacen_iznos() +"')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQLQuery);
        float preostalo = BankovniRacun.getBankovniRacunPoRacunu(getBankovniRacun(),klijent.getBroj_racuna()).getStanje() - proslave.getUkupna_cijena();
        SQLQuery = "UPDATE `bankovni racun` SET stanje = '"+preostalo+"' WHERE `broj_racuna`='"+klijent.getBroj_racuna()+"'";
        statement.executeUpdate(SQLQuery);
        float sveObjekta = BankovniRacun.getBankovniRacunPoRacunu(getBankovniRacun(),proslave.getObjekat().getVlasnik().getBroj_racuna()).getStanje();
        sveObjekta += proslave.getUkupna_cijena();
        SQLQuery = "UPDATE `bankovni racun` SET stanje = '"+sveObjekta+"' WHERE `broj_racuna`='"+proslave.getObjekat().getVlasnik().getBroj_racuna()+"'";
        statement.executeUpdate(SQLQuery);
        databaseGetBankovniRacun();

        String[] datums = proslave.getObjekat().getDatumi().split(";");
        //System.out.println(Arrays.toString(datums));
        ArrayList<String> str = new ArrayList<>(List.of(datums));

        //System.out.println("Prije: "+str);
        for(String dtm : str){
            if(dtm.equals(proslave.getDatum().toString())){
                str.remove(dtm);
                break;
            }
        }

        //System.out.println("Poslije: "+str);
        StringBuilder sb = new StringBuilder();
        for(String str1 : str){
            sb.append(str1).append(";");
        }
        //System.out.println("SB prije: "+sb.toString());
        if(!sb.isEmpty())
            sb.replace(sb.length()-1,sb.length(), "");
        //System.out.println("SB poslije: "+sb.toString());
        proslave.getObjekat().setDatumi(sb.toString());
        SQLQuery = "UPDATE `objekat` SET datumi = '"+sb+"',zarada='"+(proslave.getObjekat().getZarada()+proslave.getUkupna_cijena())+"' " +
                "WHERE `id`='"+proslave.getObjekat().getId()+"'";
        statement.executeUpdate(SQLQuery);
        proslava.add(proslave);
    }


    public void obrisiProslavu(Proslava proslave) throws SQLException {
        String SQLQuery = "DELETE FROM `raspored` WHERE idProslava='"+proslave.getId()+"'";
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQLQuery);

        float cijenaRezervacije = proslave.getObjekat().getCijena_rezervacije();
        float cijenaUplate = proslave.getUplacen_iznos();
        float cijenaRazlike = cijenaUplate-cijenaRezervacije;
        if(cijenaRazlike>0){
            Objekat objekat = proslave.getObjekat();
            Vlasnik vlasnik = objekat.getVlasnik();
            BankovniRacun bObjekta = BankovniRacun.getBankovniRacunPoRacunu(getBankovniRacun(),vlasnik.getBroj_racuna());
            Klijent klijent = proslave.getKlijent();

            SQLQuery = "UPDATE `bankovni racun` SET stanje='"+(bObjekta.getStanje()-cijenaRazlike)+"' WHERE broj_racuna='"+vlasnik.getBroj_racuna()+"'";
            statement.executeUpdate(SQLQuery);
            SQLQuery = "UPDATE `bankovni racun` SET stanje='"+(bObjekta.getStanje()+cijenaRazlike)+"' WHERE broj_racuna='"+klijent.getBroj_racuna()+"'";
            statement.executeUpdate(SQLQuery);


        }
        proslave.setProslavacol("OTKAZAN");
        SQLQuery = "UPDATE `proslava` SET Proslavacol='OTKAZAN',ukupna_cijena='"+cijenaRezervacije+"',uplacen_iznos='"+cijenaRezervacije+"' WHERE id='"+proslave.getId()+"'";
        statement.executeUpdate(SQLQuery);


        Objekat objekat = proslave.getObjekat();
        objekat.setDatumi(objekat.getDatumi()+";"+proslave.getDatum());
        SQLQuery = "UPDATE `objekat` SET datumi='"+objekat.getDatumi()+"' WHERE id='"+objekat.getId()+"'";
        statement.executeUpdate(SQLQuery);
        proslava.remove(proslave);
    }

    public void updateProslava(Proslava proslava,String[] stolovi) throws SQLException {
        Sto[] stoloviProslave = Sto.StoloviPoObjektu(getSto(),proslava.getObjekat());
        Raspored[] rasporeds = Raspored.getRasporedPoProslavi(getRaspored(),proslava);
        int brojGostiju = 0;
        int prviBrojGostiju = proslava.getBroj_gostiju();
        Statement statement = connection.createStatement();
        String SQLQuery;
        for(int i=0;i<stolovi.length;i++){
            if(stolovi[i].trim().isEmpty() && !(rasporeds.length>i)){
                continue;
            }
            stolovi[i] = izbaciPrazanProstor(stolovi[i]);
            //System.out.println("UPDATE `raspored` SET gosti='"+stolovi[i]+"' WHERE idSto='"+stoloviProslave[i].getId()+"' AND idProslava='"+proslava.getId()+"'");
            if(rasporeds.length>i)
                SQLQuery = "UPDATE `raspored` SET gosti='"+stolovi[i]+"' WHERE idSto='"+stoloviProslave[i].getId()+"' AND idProslava='"+proslava.getId()+"'";
            else
                SQLQuery = "INSERT INTO `raspored`(`idSto`, `idProslava`, `gosti`) " +
                        "VALUES ('"+stoloviProslave[i].getId()+"','"+ proslava.getId()+"','"+stolovi[i]+"')";

            brojGostiju+=stolovi[i].split(",").length;
            statement.executeUpdate(SQLQuery);
        }
        float ukupnaCijena = proslava.getObjekat().getCijena_rezervacije()+brojGostiju*proslava.getMeni().getCijena_po_osobi();
        proslava.setUkupna_cijena(ukupnaCijena);
        proslava.setBroj_gostiju(brojGostiju);
        SQLQuery = "UPDATE `proslava` SET ukupna_cijena='"+ukupnaCijena+"',broj_gostiju='"+brojGostiju+"' WHERE id='"+proslava.getId()+"'";
        statement.executeUpdate(SQLQuery);

        vratiRazliku(proslava,prviBrojGostiju);

        databaseGetRaspored();
    }

    public void vratiRazliku(Proslava proslava,int prviBrojGostiju) throws SQLException {
        if(prviBrojGostiju<=proslava.getBroj_gostiju())
            return;
        float cijenaMenija = proslava.getMeni().getCijena_po_osobi();
        float cijenaRezervacije = proslava.getObjekat().getCijena_rezervacije();
        float novaCijenaProslave = cijenaMenija * proslava.getBroj_gostiju() + cijenaRezervacije;
        float staraCijenaProslave = cijenaMenija * prviBrojGostiju + cijenaRezervacije;
        float razlikaUCijeni = staraCijenaProslave-novaCijenaProslave;

        BankovniRacun brKlijent = BankovniRacun.getBankovniRacunPoRacunu(getBankovniRacun(),proslava.getKlijent().getBroj_racuna());
        BankovniRacun brVlasnik = BankovniRacun.getBankovniRacunPoRacunu(getBankovniRacun(),proslava.getObjekat().getVlasnik().getBroj_racuna());

        String SQLQuery;
        Statement statement = connection.createStatement();

        float stanje = brKlijent.getStanje() + razlikaUCijeni;
        SQLQuery = "UPDATE `bankovni racun` SET stanje='"+stanje+"' WHERE id="+brKlijent.getId();
        statement.executeUpdate(SQLQuery);
        brKlijent.setStanje(stanje);

        stanje = brVlasnik.getStanje() - razlikaUCijeni;
        SQLQuery = "UPDATE `bankovni racun` SET stanje='"+stanje+"' WHERE id="+brVlasnik.getId();
        statement.executeUpdate(SQLQuery);
        brVlasnik.setStanje(stanje);

        SQLQuery = "UPDATE `proslava` SET uplacen_iznos='"+proslava.getUplacen_iznos()+"' WHERE id="+proslava.getId();
        proslava.setUplacen_iznos(proslava.getUplacen_iznos());
        statement.executeUpdate(SQLQuery);

        SQLQuery = "UPDATE objekat SET zarada='"+(proslava.getObjekat().getZarada() - razlikaUCijeni)+"' WHERE id='"+proslava.getObjekat().getId()+"'";
        statement.executeUpdate(SQLQuery);

    }

    public void platiProslavu(Proslava proslava) throws SQLException {
        BankovniRacun brKlijent = BankovniRacun.getBankovniRacunPoRacunu(getBankovniRacun(),proslava.getKlijent().getBroj_racuna());
        BankovniRacun brVlasnik = BankovniRacun.getBankovniRacunPoRacunu(getBankovniRacun(),proslava.getObjekat().getVlasnik().getBroj_racuna());
        String SQLQuery = "UPDATE `proslava` SET Proslavacol='PLACEN',uplacen_iznos='"+proslava.getUkupna_cijena()+"' WHERE id='"+proslava.getId()+"'";
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQLQuery);
        proslava.setUplacen_iznos(proslava.getUkupna_cijena());

        float stanje = brKlijent.getStanje() - proslava.getUkupna_cijena() +proslava.getUplacen_iznos();
        SQLQuery = "UPDATE `bankovni racun` SET stanje='"+stanje+"' WHERE id="+brKlijent.getId();
        statement.executeUpdate(SQLQuery);
        brKlijent.setStanje(stanje);

        stanje = brVlasnik.getStanje() + proslava.getUkupna_cijena() - proslava.getUplacen_iznos();
        SQLQuery = "UPDATE `bankovni racun` SET stanje='"+stanje+"' WHERE id="+brVlasnik.getId();
        statement.executeUpdate(SQLQuery);
        brVlasnik.setStanje(stanje);

    }

    public String izbaciPrazanProstor(String s){
        String[] splitter = s.split(",");
        StringBuilder builder = new StringBuilder();
        for (String rijec: splitter ) {
            builder.append(rijec.trim()).append(",");
        }
        if(!builder.isEmpty()){
            builder.setLength(builder.length()-1);
        }
        return builder.toString();
    }
}

