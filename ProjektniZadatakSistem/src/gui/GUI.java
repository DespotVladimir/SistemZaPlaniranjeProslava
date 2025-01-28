package gui;

import data.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.function.UnaryOperator;


public class GUI extends Application {

    private Database db;

    private Stage glavnaScena;

    public GUI()
    {
        try {
            db = new Database();
        } catch (SQLException e) {
            System.out.println("Nije moguce se povezati sa bazom.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void start()
    {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        glavnaScena = stage;
        stage.setResizable(false);
        Login();
    }

    // --------------------------------------------------
    // SCENA ZA LOGIN
    // --------------------------------------------------

    public void Login()
    {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblKorisnickoIme=new Label("Korisnicko Ime");

        TextField txtKorisnickoIme = new TextField();
        txtKorisnickoIme.setPromptText("Korisnicko ime...");


        Label lblLozinka = new Label("Lozinka");
        PasswordField pfLozinka = new PasswordField();
        pfLozinka.setPromptText("Lozinka...");


        Label noviNalog = new Label("Napravi novi nalog");
        noviNalog.setTextFill(Color.BLUE);
        noviNalog.setOnMouseClicked(_ -> {

            //stage.close();
            napraviNalog();

        });

        Label lblGreska = new Label("");
        lblGreska.setTextFill(Color.RED);

        Button btnLogin = new Button("Login");
        btnLogin.setOnAction(_ -> {
            Korisnik korisnik = checkLogin(txtKorisnickoIme.getText().trim(), pfLozinka.getText().trim());
            switch (korisnik)
            {
                case ADMIN -> {
                    Admin admin = Admin.getAdminPoUsername(db.getAdmin(),txtKorisnickoIme.getText().trim());
                    assert admin != null;
                    adminScena(admin);
                }
                case KLIJENT -> {
                    Klijent klijent = Klijent.getKlijentPoUsername(db.getKlijent(),txtKorisnickoIme.getText().trim());
                    klijentScena(klijent);
                }
                case VLASNIK -> {
                    Vlasnik vlasnik = Vlasnik.getVlasnikPoUsername(db.getVlasnik(),txtKorisnickoIme.getText().trim());
                    vlasnikScena(vlasnik);
                }

                case null -> {
                    lblGreska.setText("Pogresno korisnicko ime ili lozinka");
                }
            }

        });

        root.getChildren().addAll(lblKorisnickoIme,txtKorisnickoIme,lblLozinka,pfLozinka,lblGreska,btnLogin, noviNalog);

        Scene scene = new Scene(root,300,250);
        glavnaScena.setTitle("Login");
        glavnaScena.setScene(scene);
        glavnaScena.show();
    }

    private Korisnik checkLogin(String korisnickoIme, String lozinka)
    {
        Vlasnik[] vlasnici = db.getVlasnik();
        Admin[] admini = db.getAdmin();
        Klijent[] klijenti = db.getKlijent();

        for(Vlasnik vlasnik : vlasnici)
        {
            if(korisnickoIme.equals(vlasnik.getKorisnicko_ime())
                    && lozinka.equals(vlasnik.getLozinka()))
            {
                return Korisnik.VLASNIK;
            }
        }

        for(Klijent klijent : klijenti)
        {
            if(korisnickoIme.equals(klijent.getKorisnicko_ime())
                    && lozinka.equals(klijent.getLozinka()))
            {
                return Korisnik.KLIJENT;
            }
        }

        for(Admin admin:admini)
        {
            if(korisnickoIme.equals(admin.getKorisnicko_ime())
                    && lozinka.equals(admin.getLozinka()))
            {
                return Korisnik.ADMIN;
            }
        }

        return null;
    }

    // SCENA ZA PRAVLJENJE NALOGA
    private void napraviNalog()
    {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Button btn = new Button("Nazad");
        btn.setOnAction(_ -> {
            //stage.close();
            Login();
        });


        Label lblIme = new Label("Ime");
        TextField txtIme = new TextField();
        txtIme.setPromptText("Ime...");

        Label lblPrezime = new Label("Prezime");
        TextField txtPrezime = new TextField();
        txtPrezime.setPromptText("Prezime...");

        Label lblKorisnickoIme = new Label("Korisnicko Ime");
        TextField txtKorisnickoIme = new TextField();
        txtKorisnickoIme.setPromptText("Korisnicko ime...");

        Label lblJMBG = new Label("JMBG");
        TextField txtJMBG = new TextField();
        txtJMBG.setPromptText("JMBG...");

        Label lblBrojRacuna = new Label("Broj racuna");
        TextField txtBrojRacuna = new TextField();
        txtBrojRacuna.setPromptText("Broj racuna...");

        Label lblLozinka = new Label("Lozinka");
        PasswordField pfLozinka = new PasswordField();
        pfLozinka.setPromptText("Lozinka...");

        Label lblPotvrdaLozinka = new Label("Potvrda Lozinke");
        PasswordField pfPotvrdaLozinka = new PasswordField();
        pfPotvrdaLozinka.setPromptText("Potvrda Lozinke...");

        HBox hbox = new HBox(10);

        ToggleGroup kGroup = new ToggleGroup();
        RadioButton rbKlijent = new RadioButton("Klijent");
        RadioButton rbVlasnik = new RadioButton("Vlasnik");

        rbKlijent.setToggleGroup(kGroup);
        rbKlijent.setSelected(true);
        rbVlasnik.setToggleGroup(kGroup);

        hbox.getChildren().addAll(rbKlijent,rbVlasnik);

        Label lblGreska = new Label("");
        lblGreska.setTextFill(Color.RED);


        Button btnNapraviNalog = new Button("Napravi Nalog");
        btnNapraviNalog.setOnAction(_ -> {
            String username=txtKorisnickoIme.getText().trim();
            if(txtKorisnickoIme.getText().trim().isEmpty()
                    || txtIme.getText().trim().isEmpty()
                    || pfLozinka.getText().trim().isEmpty()
                    || pfPotvrdaLozinka.getText().trim().isEmpty()
                    || txtPrezime.getText().trim().isEmpty()
                    || txtBrojRacuna.getText().trim().isEmpty()
                    || txtJMBG.getText().trim().isEmpty()
            ) {
                lblGreska.setText("Sva polja su obavezna");
                return;
            }
            if(Admin.getAdminPoUsername(db.getAdmin(),username)!=null
                    || Klijent.getKlijentPoUsername(db.getKlijent(),username)!=null
                    || Vlasnik.getVlasnikPoUsername(db.getVlasnik(),username)!=null)
            {
                lblGreska.setText("Zauzeto je korisničko ime '"+username+"'");
                return;

            }
            if(!pfLozinka.getText().trim().equals(pfPotvrdaLozinka.getText().trim()))
            {
                lblGreska.setText("Ne podudaraju se lozinke");
                return;
            }
            if(BankovniRacun.getBankovniRacunPoRacunu(db.getBankovniRacun(),txtBrojRacuna.getText().trim())==null){
                lblGreska.setText("Nepostojući račun");
                return;
            }
            if(!BankovniRacun.getBankovniRacunPoRacunu(db.getBankovniRacun(),txtBrojRacuna.getText().trim()).getJmbg().equals(txtJMBG.getText().trim())){
                lblGreska.setText("Nevažeći račun");
                return;
            }
            String ime = txtIme.getText().trim();
            String prezime = txtPrezime.getText().trim();
            String brojRacuna = txtBrojRacuna.getText().trim();
            String jmbg = txtJMBG.getText().trim();
            String lozinka = pfLozinka.getText().trim();
            String korisnicko_ime = txtKorisnickoIme.getText().trim();
            if(rbVlasnik.isSelected())
            {
                try {
                    db.dodajVlasnikaUBazu(new Vlasnik(0,ime,prezime,jmbg,brojRacuna,korisnicko_ime,lozinka));
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    System.exit(-50);
                }
            }else if(rbKlijent.isSelected())
            {
                try {
                    db.dodajKlijentaUBazu(new Klijent(0,ime,prezime,jmbg,brojRacuna,korisnicko_ime,lozinka));
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    System.exit(-50);
                }
            }

            Login();

        });

        root.getChildren().addAll(lblIme,txtIme,lblPrezime,txtPrezime,lblKorisnickoIme,txtKorisnickoIme);
        root.getChildren().addAll(lblJMBG,txtJMBG,lblBrojRacuna,txtBrojRacuna,lblLozinka,pfLozinka,lblPotvrdaLozinka,pfPotvrdaLozinka);
        root.getChildren().addAll(hbox,lblGreska,btnNapraviNalog);
        root.getChildren().add(btn);

        Scene scene = new Scene(root,300,600);
        glavnaScena.setTitle("Napravi Nalog");
        glavnaScena.setScene(scene);
        glavnaScena.show();
    }

    // --------------------------------------------------
    // SCENA ZA ADMIN-A
    // --------------------------------------------------

    private void adminScena(Admin admin)
    {
        HBox root = new HBox(10);
        root.setPadding(new Insets(10,10,10,10));


       // lijeva strana
        VBox leftSide = new VBox(5);
        leftSide.setPadding(new Insets(10,10,10,10));

        Label lblIme = new Label("Ime: "+admin.getIme());
        Label lblPrezime = new Label("Prezime: "+admin.getPrezime());
        Label lblKorisnickoIme = new Label("Korisnicko ime: "+admin.getKorisnicko_ime());

        Button btnPromjeniLozinku = new Button("Promjeni lozinku");
        btnPromjeniLozinku.setOnAction(_ -> {
            VBox temp = new VBox(4);

            btnPromjeniLozinku.setVisible(false);

            Label lblNovaSifra = new Label("Nova Sifra: ");
            PasswordField pfNovaSifra = new PasswordField();

            Label lblPotvrdiSifru = new Label("Potvrdi Sifru: ");
            PasswordField pfPotvrdiSifru = new PasswordField();

            Label lblGreska = new Label("");
            lblGreska.setTextFill(Color.RED);

            HBox hbox = new HBox(10);


            Button btnPromjeniSifru = new Button("Promjeni Sifru");
            btnPromjeniSifru.setOnAction(_ -> {
                if(pfNovaSifra.getText().trim().isEmpty()||pfPotvrdiSifru.getText().trim().isEmpty())
                {
                    lblGreska.setText("Upisite oba polja");
                }
                else if(pfNovaSifra.getText().trim().equals(pfPotvrdiSifru.getText().trim())) {
                    try {
                        db.promjeniLozinku(admin,pfPotvrdiSifru.getText().trim());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    leftSide.getChildren().remove(temp);
                    btnPromjeniLozinku.setVisible(true);
                }
                else {
                    lblGreska.setText("Lozinke se ne podudaraju. ");
                }
            });
            Button btnOtkazi = new Button("Otkazi");
            btnOtkazi.setOnAction(_ -> {
                leftSide.getChildren().remove(temp);
                btnPromjeniLozinku.setVisible(true);
            });
            hbox.getChildren().addAll(btnPromjeniSifru, btnOtkazi);

            temp.getChildren().addAll(lblNovaSifra,pfNovaSifra,lblPotvrdiSifru,pfPotvrdiSifru,lblGreska,hbox);

            leftSide.getChildren().add(3,temp);

        });

        Button btnNazad = new Button("Nazad");
        btnNazad.setOnAction(_->{
            Login();
        });

        leftSide.getChildren().addAll(lblIme,lblPrezime,lblKorisnickoIme,btnPromjeniLozinku,btnNazad);


        // desna strana
        VBox rightSide = new VBox(5);
        rightSide.setPadding(new Insets(10,20,10,0));

        VBox farRightSide = new VBox(5);
        farRightSide.setPadding(new Insets(10,10,10,10));

        Label lblObjektiZaObradu = new Label("Objekti za obradu");

        ScrollPane spObjektiZaObradu = new ScrollPane();
        spObjektiZaObradu.setPrefViewportHeight(260);
        spObjektiZaObradu.setPrefViewportWidth(200);
        spObjektiZaObradu.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        updateListeObjekata(spObjektiZaObradu,farRightSide);

        rightSide.getChildren().addAll(lblObjektiZaObradu,spObjektiZaObradu);

        root.getChildren().addAll(leftSide,rightSide,farRightSide);

        Scene scene = new Scene(root,700,400);
        glavnaScena.setTitle("Kontrolna tabla");
        glavnaScena.setScene(scene);
    }

    // Dodaje sve objekte koji su na cekanju u scrollpane za obradu
    private void updateListeObjekata(ScrollPane spObjektiZaObradu, VBox farRightSide)
    {
        Objekat[] objekti = db.getObjekat();
        VBox objektiList = new VBox(0);
        for(Objekat objekat : objekti) {
            if(objekat.getStatus()!=Status.NA_CEKANJU)
                continue;

            Button btnObjekat = new Button("Naziv: "+objekat.getNaziv()+", Vlasnik: "+objekat.getVlasnik().getIme()+" "+objekat.getVlasnik().getPrezime());
            btnObjekat.setPrefWidth(200);
            btnObjekat.setOnAction(_ -> {

                farRightSide.getChildren().clear();

                Label lblNaziv = new Label("Naziv: "+objekat.getNaziv());
                Label lblVlasnik = new Label("Vlasnik: "+objekat.getVlasnik().getIme()+" "+objekat.getVlasnik().getPrezime());
                Label lblGrad = new Label("Grad: "+objekat.getGrad());
                Label lblAdresa = new Label("Adresa: "+objekat.getAdresa());
                Label lblMjesta = new Label("Broj mjesta: "+objekat.getBroj_mjesta());

                Sto[] stoloviAdmin = Sto.StoloviPoObjektu(db.getSto(),objekat);
                Label lblBrojStolova = new Label("Broj stolova: "+stoloviAdmin.length);
                int brojStolovaMjesta = 0;
                for(Sto sto : stoloviAdmin) {
                    brojStolovaMjesta+=sto.getBroj_mjesta();
                }

                Label lblBrojMjestaStolova = new Label("Broj mjesta za stolovima: "+brojStolovaMjesta);


                Meni[] menis = Meni.getMeniPoObjektu(db.getMeni(),objekat);
                ScrollPane spSviMenijiAdmin = new ScrollPane();
                spSviMenijiAdmin.setPrefViewportHeight(260);
                spSviMenijiAdmin.setPrefViewportWidth(200);
                spSviMenijiAdmin.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                spSviMenijiAdmin.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                VBox vSviMeniji = new VBox();

                if(menis.length>0)
                {
                    for(Meni meni : menis) {
                        HBox hb = getMeniBox(meni.getOpis(), meni.getCijena_po_osobi(),false);
                        vSviMeniji.getChildren().add(hb);
                        spSviMenijiAdmin.setContent(vSviMeniji);
                    }
                }
                else{
                    TextArea txtMeniAdmin = new TextArea();
                    txtMeniAdmin.setEditable(false);
                    txtMeniAdmin.setWrapText(true);
                    txtMeniAdmin.setPrefWidth(200);
                    txtMeniAdmin.setPrefHeight(50);
                    txtMeniAdmin.setText("Meniji nisu napisani! ");
                    vSviMeniji.getChildren().add(txtMeniAdmin);
                    spSviMenijiAdmin.setContent(vSviMeniji);
                }





                ToggleGroup tg = new ToggleGroup();
                RadioButton rbOdobren = new RadioButton("Odobren");
                RadioButton rbOdbijen = new RadioButton("Odbijen");

                rbOdobren.setToggleGroup(tg);
                rbOdbijen.setToggleGroup(tg);

                rbOdobren.setSelected(true);

                TextArea txtKomentar = new TextArea();
                txtKomentar.setEditable(false);
                txtKomentar.setWrapText(true);
                txtKomentar.setPrefSize(200,200);

                rbOdbijen.setOnAction(_ -> {
                    txtKomentar.setEditable(true);
                });
                rbOdobren.setOnAction(_ -> {
                    txtKomentar.setEditable(false);
                    txtKomentar.clear();
                });

                Button btnPotvrdi = new Button("Potvrdi");
                btnPotvrdi.setOnAction(_ -> {
                    if(rbOdobren.isSelected())
                    {
                        try {
                            db.odobriObjekat(objekat);
                        } catch (SQLException e) {
                            System.out.println("NIJE ODOBREN OBJEKAT "+objekat.getNaziv());
                            System.out.println(e.getMessage());
                        }
                    }
                    else if(rbOdbijen.isSelected())
                    {
                        try {
                            db.odbijObjekat(objekat,txtKomentar.getText());
                        } catch (SQLException e) {
                            System.out.println("NIJE ODBIJEN OBJEKAT "+objekat.getNaziv());
                        }
                    }
                    farRightSide.getChildren().clear();
                    updateListeObjekata(spObjektiZaObradu,farRightSide);
                });


                farRightSide.getChildren().addAll(lblNaziv,lblVlasnik,lblGrad,lblAdresa,lblMjesta,lblBrojStolova,lblBrojMjestaStolova);
                farRightSide.getChildren().addAll(spSviMenijiAdmin,rbOdobren,rbOdbijen,txtKomentar,btnPotvrdi);
            });

            objektiList.getChildren().add(btnObjekat);

        }

        spObjektiZaObradu.setContent(objektiList);
    }

    // --------------------------------------------------
    // SCENA ZA KLIJENTA
    // --------------------------------------------------

    private void klijentScena(Klijent klijent)
    {
        HBox root = new HBox(10);

        // INIT

        VBox lijevaStrana = new VBox(5);
        VBox srednjaStrana = new VBox(5);
        VBox desnaStrana = new VBox(10);


        // LIJEVA STRANA

        lijevaStrana.setPadding(new Insets(10,10,10,10));

        // gornja lijeva strana
        VBox goreLijevo = new VBox(5);

        Button btnPromjeniLozinku= new Button("Promjeni lozinku");
        btnPromjeniLozinku.setOnAction(_ -> {
            VBox temp = new VBox(4);

            btnPromjeniLozinku.setVisible(false);

            Label lblNovaSifra = new Label("Nova Sifra: ");
            PasswordField pfNovaSifra = new PasswordField();

            Label lblPotvrdiSifru = new Label("Potvrdi Sifru: ");
            PasswordField pfPotvrdiSifru = new PasswordField();

            pfNovaSifra.setPrefWidth(200);
            pfPotvrdiSifru.setPrefWidth(200);
            pfNovaSifra.setMaxWidth(PasswordField.USE_PREF_SIZE);
            pfPotvrdiSifru.setMaxWidth(PasswordField.USE_PREF_SIZE);


            Label lblGreska = new Label("");
            lblGreska.setTextFill(Color.RED);

            HBox hbox = new HBox(10);


            Button btnPromjeniSifru = new Button("Promjeni Sifru");
            btnPromjeniSifru.setOnAction(_ -> {
                if(pfNovaSifra.getText().trim().isEmpty()||pfPotvrdiSifru.getText().trim().isEmpty())
                {
                    lblGreska.setText("Upisite oba polja");
                }
                else if(pfNovaSifra.getText().trim().equals(pfPotvrdiSifru.getText().trim())) {
                    try {
                        db.promjeniLozinku(klijent,pfPotvrdiSifru.getText().trim());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    goreLijevo.getChildren().remove(temp);
                    btnPromjeniLozinku.setVisible(true);
                }
                else {
                    lblGreska.setText("Lozinke se ne podudaraju. ");
                }
            });
            Button btnOtkazi = new Button("Otkazi");
            btnOtkazi.setOnAction(_ -> {
                goreLijevo.getChildren().remove(temp);
                btnPromjeniLozinku.setVisible(true);
            });
            hbox.getChildren().addAll(btnPromjeniSifru, btnOtkazi);

            temp.getChildren().addAll(lblNovaSifra,pfNovaSifra,lblPotvrdiSifru,pfPotvrdiSifru,lblGreska,hbox);

            goreLijevo.getChildren().add(6,temp);

        });

        Button btnNazad = new Button("Nazad");
        btnNazad.setOnAction(_->{
            Login();
        });

        Label lblIme = new Label("Ime: "+klijent.getIme());
        Label lblPrezime = new Label("Prezime: "+klijent.getPrezime());
        Label lblJMBG = new Label("JMBG: "+klijent.getJmbg());
        Label lblBankovniRacun = new Label("Bankovni racun: " + klijent.getBroj_racuna());
        Label lblStanjeRacuna = new Label("Stanje na racunu: "+ BankovniRacun.getBankovniRacunPoRacunu(db.getBankovniRacun(), klijent.getBroj_racuna()).getStanje());


        goreLijevo.getChildren().addAll(lblIme,lblPrezime,lblJMBG,lblBankovniRacun,lblStanjeRacuna,btnPromjeniLozinku,btnNazad);

        lijevaStrana.getChildren().add(goreLijevo);

        // donja lijeva strana

        VBox doleLijevo = new VBox(5);
        doleLijevo.setPadding(new Insets(10,0,0,0));

        Button btnPrikaziObjekte = new Button("Prikaži objekte");
        btnPrikaziObjekte.setOnAction(_ -> {
            prikazObjekataKlijentu(srednjaStrana,desnaStrana,klijent,lblStanjeRacuna);
        });

        Label lblProslave = new Label("Proslave: ");
        lblProslave.setTextFill(Color.GRAY);

        HBox hProslave = new HBox(5);

        Button btnPrikaziProsle = new Button("Prošle");
        Button btnPrikaziBuduce = new Button("Buduće");
        Button btnPrikaziOtkazane = new Button("Otkazane");

        hProslave.getChildren().addAll(btnPrikaziProsle,btnPrikaziBuduce,btnPrikaziOtkazane);

        ScrollPane spProslave = new ScrollPane();
        VBox vProslave = new VBox(5);

        spProslave.setPrefViewportWidth(200);
        spProslave.setMaxWidth(ScrollPane.USE_PREF_SIZE);
        spProslave.setMinWidth(ScrollPane.USE_PREF_SIZE);
        spProslave.setPrefHeight(200);
        spProslave.setContent(vProslave);


        ArrayList<Proslava> prosleProslaveList = new ArrayList<>();
        ArrayList<Proslava> aktivneProslaveList = new ArrayList<>();
        ArrayList<Proslava> otkazaneProslaveList = new ArrayList<>();
        Proslava[] sveProslaveObjekta = Proslava.getProslavaPoKlijentu(db.getProslava(),klijent);

        for (Proslava p: sveProslaveObjekta) {
            if(p.jeOtkazana()){
                otkazaneProslaveList.add(p);
            }
            else if(p.jeProsla()){
                prosleProslaveList.add(p);
            }
            else
                aktivneProslaveList.add(p);
        }

        btnPrikaziProsle.setOnAction(_->{
            dodajProslaveKlijenta(spProslave,vProslave,prosleProslaveList.toArray(new Proslava[0]),srednjaStrana,desnaStrana,klijent,lblStanjeRacuna);
        });
         btnPrikaziBuduce.setOnAction(_->{
             aktivneProslaveList.clear();
             Proslava[] s = Proslava.getProslavaPoKlijentu(db.getProslava(),klijent);

             for (Proslava p: s) {
                 if(!(p.jeOtkazana()||p.jeProsla())){
                     aktivneProslaveList.add(p);
                 }
             }
            dodajProslaveKlijenta(spProslave,vProslave,aktivneProslaveList.toArray(new Proslava[0]),srednjaStrana,desnaStrana,klijent,lblStanjeRacuna);
         });
         btnPrikaziOtkazane.setOnAction(_->{
             otkazaneProslaveList.clear();
             Proslava[] s = Proslava.getProslavaPoKlijentu(db.getProslava(),klijent);

             for (Proslava p: s) {
                 if(p.jeOtkazana()){
                     otkazaneProslaveList.add(p);
                 }
             }


             dodajProslaveKlijenta(spProslave,vProslave,otkazaneProslaveList.toArray(new Proslava[0]),srednjaStrana,desnaStrana,klijent,lblStanjeRacuna);
         });
         dodajProslaveKlijenta(spProslave,vProslave,aktivneProslaveList.toArray(new Proslava[0]),srednjaStrana,desnaStrana,klijent,lblStanjeRacuna);



        doleLijevo.getChildren().addAll(btnPrikaziObjekte,lblProslave,hProslave,spProslave);

        lijevaStrana.getChildren().add(doleLijevo);
        lijevaStrana.setPrefWidth(200);


        // SREDNJA STRANA

        prikazObjekataKlijentu(srednjaStrana,desnaStrana,klijent,lblStanjeRacuna);



        // DESNA STRANA
        desnaStrana.setPrefWidth(250);
        desnaStrana.setPadding(new Insets(10,10,10,10));

        root.getChildren().addAll(lijevaStrana,srednjaStrana,desnaStrana);

        Scene scene = new Scene(root,800,650);
        glavnaScena.setTitle("Klijent " + klijent.getIme()+ " " +klijent.getPrezime());
        glavnaScena.setScene(scene);
    }

    // Prikaz svih proslava u donjem lijevom cosku
    private void dodajProslaveKlijenta(ScrollPane spProslave, VBox vProslave, Proslava[] proslave, VBox srednjaStrana, VBox desnaStrana,Klijent klijent,Label lblStanje){
        vProslave.getChildren().clear();
        for(Proslava pr: proslave)
        {
            Button btnPrikaziDatum = new Button(pr.getDatum().toString());
            btnPrikaziDatum.setPrefWidth(200);
            btnPrikaziDatum.setOnAction(_ -> {
                srednjaStrana.getChildren().clear();
                desnaStrana.getChildren().clear();


                Label lblImeObjekta = new Label("Ime objekta: "+pr.getObjekat().getNaziv());
                Label lblAdresaObjekta = new Label("Adresa objekta: "+pr.getObjekat().getAdresa());
                Label lblGradObjekta = new Label("Grad objekta: "+pr.getObjekat().getGrad());
                Label lblDatumProslave = new Label("Datum proslave: "+pr.getDatum().toString());
                Label lblBrojGostiju = new Label("Broj gostiju: " + pr.getBroj_gostiju());

                Label lblRasporedGostiju = new Label("Raspored gostiju");
                lblRasporedGostiju.setTextFill(Color.GRAY);
                HBox hOdabirIzgleda = new HBox(5);
                ToggleGroup tg = new ToggleGroup();
                RadioButton rPrezimena = new RadioButton("Prezime");
                RadioButton rBrojGostiju = new RadioButton("Broj gostiju");

                rPrezimena.setToggleGroup(tg);
                rBrojGostiju.setToggleGroup(tg);

                hOdabirIzgleda.getChildren().addAll(rPrezimena,rBrojGostiju);

                Raspored[] rasporedi = Raspored.getRasporedPoProslavi(db.getRaspored(),pr);
                ScrollPane spRasporedGostiju = new ScrollPane();
                VBox vRasporedGostiju = new VBox();

                rPrezimena.setOnAction(_->{
                    vRasporedGostiju.getChildren().clear();
                    int i=1;
                    for (Raspored r: rasporedi)
                    {
                        if(r.getGosti().isEmpty())
                            continue;
                        TextArea txtRaspored = new TextArea() ;
                        txtRaspored.setText("Sto "+ (i++)+": "+r.getGosti());
                        txtRaspored.setEditable(false);
                        txtRaspored.setWrapText(true);
                        txtRaspored.setPrefWidth(200);
                        txtRaspored.setPrefHeight(100);
                        vRasporedGostiju.getChildren().add(txtRaspored);
                    }
                    spRasporedGostiju.setContent(vRasporedGostiju);

                });


                rBrojGostiju.setOnAction(_->{
                    vRasporedGostiju.getChildren().clear();
                    int i=1;
                    for (Raspored r: rasporedi)
                    {
                        if(r.getGosti().isEmpty())
                            continue;
                        int brojZaStolom = r.getGosti().split(",").length;
                        Label lblBrojStola = new Label("Sto "+(i++) + ": "+brojZaStolom);
                        vRasporedGostiju.getChildren().add(lblBrojStola);
                    }
                    spRasporedGostiju.setContent(vRasporedGostiju);
                });

                TextArea txtMeni = new TextArea();

                txtMeni.setText("Meni : "+ pr.getMeni().getOpis() + ";\nCijena po osobi: " + pr.getMeni().getCijena_po_osobi());
                txtMeni.setEditable(false);
                txtMeni.setMinWidth(200);
                txtMeni.setMaxWidth(200);
                txtMeni.setMinHeight(100);
                txtMeni.setMaxHeight(100);
                txtMeni.setWrapText(true);

                Label lblGreska = new Label("");
                lblGreska.setTextFill(Color.RED);


                // IZMJENI PROSLAVU

                Button btnIzmjeni =  new Button("Izmjeni proslavu ");
                btnIzmjeni.setOnAction(_->{

                    if(pr.jeOtkazana()){
                        lblGreska.setText("Nemoguce izmjeniti proslavu \n(Proslava je prošla)");

                    }
                    if(!pr.jeIzmjenjiva()){
                        lblGreska.setText("Nemoguce izmjeniti proslavu \n(Manje od 3 dana do proslave)");
                        return;
                    }
                    if(pr.jeProsla()){
                        lblGreska.setText("Nemoguce izmjeniti proslavu \n(Proslava je prošla)");
                        return;
                    }

                    desnaStrana.getChildren().clear();

                    Label lblIzmjenagostiju = new Label("Raspored gostiju: ");
                    Label lblFormatIzmjena = new Label("(Gost 1, Gost 2, Gost 3...)");
                    lblFormatIzmjena.setTextFill(Color.GRAY);

                    ScrollPane spIzmjenaRasporeda = new ScrollPane();
                    VBox vIzmjeniRasporeda = new VBox();
                    spIzmjenaRasporeda.setMinWidth(250);
                    spIzmjenaRasporeda.setMaxWidth(250);
                    spIzmjenaRasporeda.setMaxHeight(200);
                    spIzmjenaRasporeda.setMinHeight(200);

                    Sto[] stoloviObjekta = Sto.StoloviPoObjektu(db.getSto(),pr.getObjekat());


                    int i=0;
                    for (Sto sto: stoloviObjekta){
                        HBox hOdabirIzmjeni = new HBox();
                        Label lblSto = new Label("Sto " + ((i++)+1 + "\n("+sto.getBroj_mjesta()+" mjesta)"));
                        TextArea txtSto = new TextArea();
                        txtSto.setPrefWidth(150);
                        txtSto.setPrefHeight(100);
                        txtSto.setMaxHeight(100);
                        txtSto.setMinHeight(100);
                        txtSto.setMinWidth(150);
                        txtSto.setMaxWidth(150);
                        txtSto.setWrapText(true);

                        if(rasporedi.length>i-1){
                            txtSto.setText(rasporedi[i-1].getGosti());
                        }


                        hOdabirIzmjeni.getChildren().addAll(lblSto,txtSto);

                        vIzmjeniRasporeda.getChildren().add(hOdabirIzmjeni);
                    }
                    spIzmjenaRasporeda.setContent(vIzmjeniRasporeda);

                    Label lblGreskaIzmjena = new Label("");
                    lblGreskaIzmjena.setTextFill(Color.RED);

                    Button btnSacuvaj = new Button("Sačuvaj");
                    btnSacuvaj.setOnAction(_->{
                        ArrayList<String> izmjenaLista = new ArrayList<>();
                        int ik = 0;
                        for(Node n : vIzmjeniRasporeda.getChildren()){
                            HBox h = (HBox) n;
                            TextArea tx = (TextArea) h.getChildren().getLast();
                            izmjenaLista.add(tx.getText());

                            if(tx.getText().split(",").length>stoloviObjekta[ik++].getBroj_mjesta()){
                                lblGreskaIzmjena.setText("Nedovoljno mjesta za stolom: " + ik);
                                return;
                            }
                        }

                        try {
                            db.updateProslava(pr,izmjenaLista.toArray(new String[0]));
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        }
                        srednjaStrana.getChildren().clear();
                        desnaStrana.getChildren().clear();
                    });

                    Label lblPotvrdiS = new Label("Potvrdi sifru: ");
                    PasswordField pswPotvrdaSifre = new PasswordField();

                    Button btnPlati = new Button("Plati");
                    btnPlati.setOnAction(_->{
                        if(!pswPotvrdaSifre.getText().equals(klijent.getLozinka())){
                            lblGreskaIzmjena.setText("Pogrešna lozinka");
                            return;
                        }

                        ArrayList<String> izmjenaLista = new ArrayList<>();
                        int ik = 0;
                        for(Node n : vIzmjeniRasporeda.getChildren()){
                            HBox h = (HBox) n;
                            TextArea tx = (TextArea) h.getChildren().getLast();
                            izmjenaLista.add(tx.getText());

                            if(tx.getText().split(",").length>stoloviObjekta[ik++].getBroj_mjesta()){
                                lblGreskaIzmjena.setText("Nedovoljno mjesta za stolom: " + ik);
                                return;
                            }
                        }

                        try {
                            BankovniRacun brKlijenta= BankovniRacun.getBankovniRacunPoRacunu(db.getBankovniRacun(),klijent.getBroj_racuna());
                            db.updateProslava(pr,izmjenaLista.toArray(new String[0]));
                            if(pr.getUkupna_cijena()-pr.getUplacen_iznos()>brKlijenta.getStanje())
                            {
                                lblGreskaIzmjena.setText("Nedovoljno stanja na računu");
                                return;
                            }
                            db.platiProslavu(pr);
                            Label stanjeKlijenta = new Label("Stanje na racunu: "+BankovniRacun.getBankovniRacunPoRacunu(db.getBankovniRacun(),klijent.getBroj_racuna()).getStanje());
                            srednjaStrana.getChildren().clear();
                            srednjaStrana.getChildren().add(stanjeKlijenta);
                            lblStanje.setText("Stanje na racunu: "+BankovniRacun.getBankovniRacunPoRacunu(db.getBankovniRacun(),klijent.getBroj_racuna()).getStanje());
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        }



                        desnaStrana.getChildren().clear();

                    });
                    desnaStrana.getChildren().addAll(lblIzmjenagostiju,lblFormatIzmjena,spIzmjenaRasporeda,lblGreskaIzmjena,btnSacuvaj,lblPotvrdiS,pswPotvrdaSifre,btnPlati);

                });

                // OTKAZI PROSLAVU

                Button btnOtkazi =  new Button("Otkaži proslavu ");
                btnOtkazi.setOnAction(_->{
                    if(pr.jeProsla()){
                        lblGreska.setText("Nemoguce otkazati proslavu \n(Proslava je prošla)");
                        return;
                    }
                    if(pr.jeOtkazana()){
                        lblGreska.setText("Proslava je već otkazana");
                        return;
                    }

                    try {
                        db.obrisiProslavu(pr);
                        srednjaStrana.getChildren().clear();
                        desnaStrana.getChildren().clear();
                        dodajProslaveKlijenta(spProslave,vProslave,proslave,srednjaStrana,desnaStrana,klijent,lblStanje);
                        BankovniRacun br = BankovniRacun.getBankovniRacunPoRacunu(db.getBankovniRacun(),klijent.getBroj_racuna());
                        lblStanje.setText("Stanje: "+br.getStanje());
                        Label lbl = new Label("Stanje na računu: "+br.getStanje());
                        lbl.setPadding(new Insets(30,0,0,0));
                        desnaStrana.getChildren().add(lbl);

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });



                srednjaStrana.getChildren().addAll(lblImeObjekta,lblAdresaObjekta,lblGradObjekta,lblDatumProslave,lblBrojGostiju,txtMeni);
                srednjaStrana.getChildren().addAll(lblRasporedGostiju,hOdabirIzgleda,spRasporedGostiju,btnIzmjeni,btnOtkazi,lblGreska);

                if(pr.jeProsla()){
                    Label lblPlacenGreska = new Label();
                    lblPlacenGreska.setTextFill(Color.RED);

                    Button btnPlatiSve = new Button("Plati proslavu");
                    btnPlatiSve.setOnAction(_->{
                        if(pr.jePlacena()){
                            lblPlacenGreska.setText("Proslava je plaćena");
                            return;
                        }
                        BankovniRacun brKlijenta = BankovniRacun.getBankovniRacunPoRacunu(db.getBankovniRacun(),klijent.getBroj_racuna());
                        if(pr.getUkupna_cijena() - pr.getUplacen_iznos() > brKlijenta.getStanje())
                        {
                            lblPlacenGreska.setText("Nedovoljno stanja na računu");
                            return;
                        }
                        try{
                            db.platiProslavu(pr);
                            Label stanjeKlijenta = new Label("Stanje na računu: "+BankovniRacun.getBankovniRacunPoRacunu(db.getBankovniRacun(),klijent.getBroj_racuna()).getStanje());
                            srednjaStrana.getChildren().clear();
                            srednjaStrana.getChildren().add(stanjeKlijenta);
                        }catch (SQLException e) {
                            System.out.println(e.getMessage());;
                            throw new RuntimeException(e);
                        }

                    });
                    srednjaStrana.getChildren().addAll(lblPlacenGreska,btnPlatiSve);
                }


            });
            vProslave.getChildren().addAll(btnPrikaziDatum);
        }
        spProslave.setContent(vProslave);
    }

    // Prikazuje sve objekte koje su u sistemu
    private void prikazObjekataKlijentu(VBox srednjaStrana, VBox desnaStrana, Klijent klijent,Label lblStanje){
        srednjaStrana.getChildren().clear();
        desnaStrana.getChildren().clear();

        HBox hGrad = new HBox(5);
        Label lblGrad = new Label("Grad: ");
        TextField txtGrad = new TextField();
        hGrad.getChildren().addAll(lblGrad,txtGrad);

        HBox hDatum = new HBox(5);
        Label lblDatum = new Label("Datum: ");
        DatePicker datePicker = new DatePicker();
        hDatum.getChildren().addAll(lblDatum,datePicker);

        HBox hBrojMjesta =  new HBox(5);
        Label lblBrojMjesta = new Label("Broj gostiju: ");
        TextField txtBrojMjesta = getBrojcanoPolje();
        hBrojMjesta.getChildren().addAll(lblBrojMjesta,txtBrojMjesta);

        Button btnPotvrdiPotragu = new Button("Potvrdi");

        srednjaStrana.getChildren().addAll(hGrad,hDatum,hBrojMjesta,btnPotvrdiPotragu);


        ScrollPane spSviObjekti = new ScrollPane();
        spSviObjekti.setPrefWidth(200);
        spSviObjekti.setMaxWidth(200);
        spSviObjekti.setMinHeight(200);
        spSviObjekti.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox vSviObjekti = new VBox();

        VBox vPregledObjekta = new VBox();

        srednjaStrana.getChildren().addAll(spSviObjekti,vPregledObjekta);


        btnPotvrdiPotragu.setOnAction(_ -> {
            potragaPrikazaObjekataKlijentu(spSviObjekti,vSviObjekti,desnaStrana,txtGrad.getText(),datePicker.getValue(),txtBrojMjesta.getText(),klijent,lblStanje);
        });
        potragaPrikazaObjekataKlijentu(spSviObjekti,vSviObjekti,desnaStrana,txtGrad.getText(),datePicker.getValue(),"1",klijent,lblStanje);

    }

    // Prikazuje objekte u zavisnosti od filtera
    private void potragaPrikazaObjekataKlijentu(ScrollPane spSviObjekti, VBox vSviObjekti,VBox vPregledObjekta,
                                                String grad, LocalDate datum, String BrojGostiju,Klijent klijent,Label lblStanje){
        vSviObjekti.getChildren().clear();

        Objekat[] sviObjekti = db.getObjekat();
        ArrayList<Objekat> objektiZaDodati= new ArrayList<>();

        int broj_gostiju = Integer.parseInt(BrojGostiju);

        for(Objekat ob: sviObjekti){
            if ((!grad.isEmpty()) && (!ob.getGrad().equalsIgnoreCase(grad)))
                continue;
            if(ob.getBroj_mjesta()<broj_gostiju)
                continue;
            if(datum!=null){
                LocalDate[] ld = ob.getSlobodniDatumi();
                boolean nastavi = true;
                for (LocalDate localDate : ld) {
                    if (localDate.equals(datum)) {
                        nastavi = false;
                    }
                }
                if(nastavi)
                    continue;
            }

            objektiZaDodati.add(ob);
        }
        objektiZaDodati.sort(Comparator.comparing(Objekat::getNaziv));
        for(Objekat ob: objektiZaDodati){
            Button btnObjekat = new Button(ob.getNaziv());
            btnObjekat.setPrefWidth(200);
            vSviObjekti.getChildren().add(btnObjekat);


            // Prikazuje mogucnosti rezervacije
            btnObjekat.setOnAction(_ -> {
                vPregledObjekta.getChildren().clear();

                VBox gornjiDio = new VBox(10);
                VBox donjiDio = new VBox(5);

                Label lblIme = new Label("Naziv: " + ob.getNaziv());
                Label lblAdresa = new Label("Adresa: " + ob.getAdresa());
                Label lblGrad = new Label("Grad: " + ob.getGrad());
                Label lblBrojMjesta = new Label("Broj mjesta: " + ob.getBroj_mjesta());
                Label lblBrojStolova = new Label("Broj stolova: " + ob.getBroj_stolova());
                Label lblCijenaRezervacije = new Label("Cijena rezervacije: " + ob.getCijena_rezervacije());
                TextArea txtMeniji = new TextArea();
                txtMeniji.setPrefWidth(200);
                txtMeniji.setPrefHeight(150);
                txtMeniji.setWrapText(true);
                txtMeniji.setEditable(false);
                Meni[] meniji = Meni.getMeniPoObjektu(db.getMeni(),ob);
                for(Meni meni: meniji){
                    txtMeniji.appendText(meni.getOpis() + ";\n\n");
                }

                Set<LocalDate> slobodniDatumi = Set.of(ob.getSlobodniDatumi());
                //GridPane kalendar = getKalendarKlijentu(slobodniDatumi,false,m);

                final long[] m = {0};
                HBox mjeseciPlus = new HBox();
                Button btnKalendarLijevo = new Button("<<");
                Button btnKalendarDesno = new Button(">>");
                mjeseciPlus.getChildren().addAll(btnKalendarLijevo,btnKalendarDesno);

                final GridPane[] kalendar = {getKalendarKlijentu(slobodniDatumi, false, m[0])};

                btnKalendarLijevo.setOnAction(_->{
                    gornjiDio.getChildren().remove(7);
                    m[0]--;
                    kalendar[0] = getKalendarKlijentu(slobodniDatumi, false, m[0]);
                    gornjiDio.getChildren().add(7,kalendar[0]);
                });
                btnKalendarDesno.setOnAction(_->{
                    gornjiDio.getChildren().remove(7);
                    m[0]++;
                    kalendar[0] = getKalendarKlijentu(slobodniDatumi, false, m[0]);
                    gornjiDio.getChildren().add(7,kalendar[0]);
                });


                vPregledObjekta.getChildren().addAll(mjeseciPlus,kalendar[0]);

                Button btnRezervisi = new Button("Rezerviši");
                btnRezervisi.setOnAction( _ -> {
                    donjiDio.getChildren().clear();

                    ScrollPane spMeniji = new ScrollPane();
                    spMeniji.setPrefViewportWidth(215);
                    VBox vS = new VBox();

                    Meni odabranMeni = new Meni();

                    for(int i=0; i < meniji.length; i++){
                        HBox hMeniji = new HBox();
                        TextArea txtMe = new TextArea();
                        txtMe.setMinWidth(120);
                        txtMe.setMaxWidth(120);
                        txtMe.setMaxHeight(100);
                        txtMe.setMinHeight(100);
                        txtMe.setEditable(false);
                        txtMe.setWrapText(true);
                        Button btnMeni = new Button("Meni " + (i+1));
                        txtMe.setMaxHeight(100);
                        txtMe.setMinHeight(100);
                        int finalI = i;
                        txtMe.setText(meniji[finalI].getOpis()+" - Cijena: "+meniji[finalI].getCijena_po_osobi());
                        btnMeni.setOnAction((_) -> {
                            odabranMeni.setId(meniji[finalI].getId());
                            odabranMeni.setObjekat(meniji[finalI].getObjekat());
                            odabranMeni.setOpis(meniji[finalI].getOpis());
                            odabranMeni.setCijena_po_osobi(meniji[finalI].getCijena_po_osobi());
                        });
                        hMeniji.getChildren().addAll(txtMe,btnMeni);
                        hMeniji.setAlignment(Pos.CENTER);
                        vS.getChildren().add(hMeniji);
                    }
                    spMeniji.setContent(vS);

                    Label lblDatum = new Label("Odaberi dan: ");
                    DatePicker odabranDatum = new DatePicker();

                    Label lblPotvrdiSifru = new Label("Potvrdi sifru: ");
                    PasswordField pswKlijenta = new PasswordField();

                    Label lblGreska = new Label("");
                    lblGreska.setTextFill(Color.RED);

                    Button potvrdiRezervaciju = new Button("Potvrdi");

                    potvrdiRezervaciju.setOnAction(_ -> {

                        if(!pswKlijenta.getText().equals(klijent.getLozinka()))
                        {
                            lblGreska.setText("Ne podudara se šifra");
                            return;
                        }
                        LocalDate datumZaProslave = odabranDatum.getValue();
                        if(LocalDate.now().plusDays(3).isAfter(datumZaProslave)){
                            lblGreska.setText("Nemoguće rezervisati 3 dana prije");
                            return;
                        }
                        LocalDate[] sl = ob.getSlobodniDatumi();
                        if(datumZaProslave.isBefore(LocalDate.now()))
                        {
                            lblGreska.setText("Nevažeći datum");
                            return;
                        }
                        boolean prekid = true;
                        for(LocalDate s: sl){
                            if(s.equals(datumZaProslave))
                                prekid = false;
                        }
                        if(prekid){
                            lblGreska.setText("Nije slobodan datum");
                            return;
                        }
                        float naRacunu = BankovniRacun.getBankovniRacunPoRacunu(db.getBankovniRacun(),klijent.getBroj_racuna()).getStanje();
                        if(ob.getCijena_rezervacije()>naRacunu){
                            lblGreska.setText("Nije dovoljan iznos na racunu");
                            return;
                        }
                        if(odabranMeni.getObjekat()==null){
                            lblGreska.setText("Morate odabrati meni");
                            return;
                        }

                        Proslava proslava = new Proslava(-1,ob,klijent,odabranMeni,datumZaProslave,0,ob.getCijena_rezervacije(),ob.getCijena_rezervacije(),"");
                        try {
                            db.postaviProslavu(klijent,proslava);
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        }
                        float stanje =BankovniRacun.getBankovniRacunPoRacunu(db.getBankovniRacun(), klijent.getBroj_racuna()).getStanje();
                        lblStanje.setText("Stanje na racunu: "+ stanje);
                        vPregledObjekta.getChildren().clear();

                        donjiDio.getChildren().clear();
                        Label lblStanjer = new Label("Preostalo stanje na računu: " + stanje);
                        vPregledObjekta.getChildren().add(lblStanjer);
                    });

                    donjiDio.getChildren().addAll(spMeniji,lblDatum,odabranDatum,lblPotvrdiSifru,pswKlijenta,lblGreska,potvrdiRezervaciju);

                });

                gornjiDio.getChildren().addAll(lblIme,lblAdresa,lblGrad,lblBrojMjesta,lblBrojStolova,lblCijenaRezervacije,mjeseciPlus,kalendar[0]);
                donjiDio.getChildren().addAll(txtMeniji,btnRezervisi);
                vPregledObjekta.getChildren().addAll(gornjiDio,donjiDio);

            });

        }
        spSviObjekti.setContent(vSviObjekti);
    }

    // --------------------------------------------------
    // SCENA ZA VLASNIKA
    // --------------------------------------------------

    private void vlasnikScena(Vlasnik vlasnik)
    {
        HBox root = new HBox(10);

        // INIT
        VBox lijevaStrana= new VBox(30);
        VBox srednjaStrana = new VBox(5);
        final VBox desnaStrana = new VBox(10);

        // LIJEVA STRANA

        lijevaStrana.setPadding(new Insets(10,10,10,10));

        // gornja lijeva strana
        VBox goreLijevo = new VBox(5);

        Button btnPromjeniLozinku= new Button("Promjeni lozinku");
        btnPromjeniLozinku.setOnAction(_ -> {
            VBox temp = new VBox(4);

            btnPromjeniLozinku.setVisible(false);

            Label lblNovaSifra = new Label("Nova Sifra: ");
            PasswordField pfNovaSifra = new PasswordField();

            Label lblPotvrdiSifru = new Label("Potvrdi Sifru: ");
            PasswordField pfPotvrdiSifru = new PasswordField();

            Label lblGreska = new Label("");
            lblGreska.setTextFill(Color.RED);

            HBox hbox = new HBox(10);


            Button btnPromjeniSifru = new Button("Promjeni Sifru");
            btnPromjeniSifru.setOnAction(_ -> {
                if(pfNovaSifra.getText().trim().isEmpty()||pfPotvrdiSifru.getText().trim().isEmpty())
                {
                    lblGreska.setText("Upisite oba polja");
                }
                else if(pfNovaSifra.getText().trim().equals(pfPotvrdiSifru.getText().trim())) {
                    try {
                        db.promjeniLozinku(vlasnik,pfPotvrdiSifru.getText().trim());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    goreLijevo.getChildren().remove(temp);
                    btnPromjeniLozinku.setVisible(true);
                }
                else {
                    lblGreska.setText("Lozinke se ne podudaraju. ");
                }
            });
            Button btnOtkazi = new Button("Otkaži");
            btnOtkazi.setOnAction(_ -> {
                goreLijevo.getChildren().remove(temp);
                btnPromjeniLozinku.setVisible(true);
            });
            hbox.getChildren().addAll(btnPromjeniSifru, btnOtkazi);

            temp.getChildren().addAll(lblNovaSifra,pfNovaSifra,lblPotvrdiSifru,pfPotvrdiSifru,lblGreska,hbox);

            goreLijevo.getChildren().add(6,temp);

        });

        Button btnNazad = new Button("Nazad");
        btnNazad.setOnAction(_->{
            Login();
        });

        Label lblIme = new Label("Ime: "+vlasnik.getIme());
        Label lblPrezime = new Label("Prezime: "+vlasnik.getPrezime());
        Label lblJMBG = new Label("JMBG: "+vlasnik.getJmbg());
        Label lblBankovniRacun = new Label("Bankovni racun: " + vlasnik.getBroj_racuna());
        Label lblStanjeRacuna = new Label("Stanje na racunu: "+ BankovniRacun.getBankovniRacunPoRacunu(db.getBankovniRacun(), vlasnik.getBroj_racuna()).getStanje());


        goreLijevo.getChildren().addAll(lblIme,lblPrezime,lblJMBG,lblBankovniRacun,lblStanjeRacuna,btnPromjeniLozinku,btnNazad);

        lijevaStrana.getChildren().add(goreLijevo);

        //donja lijeva strana

        VBox doleLijevo = new VBox(10);

        Button btnNoviObjekatForma=new Button("Novi objekat");
        btnNoviObjekatForma.setOnAction(_->{
            novaObjekatForma(desnaStrana,vlasnik,srednjaStrana);
        });
        doleLijevo.getChildren().add(btnNoviObjekatForma);

        Label lblInbox = new Label("Vase poruke: ");
        ScrollPane spObjekti = new ScrollPane();
        spObjekti.setPrefViewportHeight(300);
        spObjekti.setPrefViewportWidth(200);
        spObjekti.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        updatePorukeObjekata(spObjekti,srednjaStrana,vlasnik,lblInbox,desnaStrana);

        doleLijevo.getChildren().addAll(lblInbox,spObjekti);


        HBox dugmadiZaObjekte = new HBox(10);
        Button btnPoruke = new Button("Poruke");
        Button btnSviObjekti = new Button("Svi Objekti");
        dugmadiZaObjekte.getChildren().addAll(btnPoruke,btnSviObjekti);


        btnSviObjekti.setOnAction(_ -> {
            updateSveObjekte(spObjekti,srednjaStrana,vlasnik,lblInbox,desnaStrana);
        });


        btnPoruke.setOnAction(_->{
            updatePorukeObjekata(spObjekti,srednjaStrana,vlasnik,lblInbox,desnaStrana);
        });

        doleLijevo.getChildren().add(dugmadiZaObjekte);



        lijevaStrana.getChildren().add(doleLijevo);



        // SREDINA

        Label lblPorukaZaVlasnika = new Label();
        Objekat[] objektiVlasnika = Objekat.getObjektiPoVlasniku(db.getObjekat(),vlasnik);
        StringBuilder odbijeniObjekti = new StringBuilder();
        StringBuilder prihvaceniObjekti = new StringBuilder();

        for(Objekat ob:objektiVlasnika){
            if(ob.getStatus()==Status.NA_CEKANJU)
                continue;
            Obavjestenje obav = Obavjestenje.getObavjestenjePoObjektu(db.getObavjestenje(),ob);
            if(obav==null)
                continue;
            if(obav.getTekst().isEmpty() && ob.getStatus()==Status.ODOBREN)
                prihvaceniObjekti.append(ob.getNaziv()).append(", ");
            if(ob.getStatus()==Status.ODBIJEN)
                odbijeniObjekti.append(ob.getNaziv()).append(", ");
        }
        StringBuilder konacnaPoruka = new StringBuilder();
        konacnaPoruka.append("Vaše poruke: \n");
        if(odbijeniObjekti.isEmpty() && prihvaceniObjekti.isEmpty()){
            konacnaPoruka.append("Nemate poruka! ");
        }
        if(!odbijeniObjekti.isEmpty()){
            konacnaPoruka.append("Odbijeni objekti: ").append(odbijeniObjekti.toString()).append("\n");
        }
        if(!prihvaceniObjekti.isEmpty()){
            konacnaPoruka.append("Prihvaceni objekti: ").append(prihvaceniObjekti.toString()).append("\n");
        }
        lblPorukaZaVlasnika.setText(konacnaPoruka.toString());

        srednjaStrana.getChildren().add(lblPorukaZaVlasnika);

        // DESNA STRANA
        desnaStrana.setPadding(new Insets(10,10,10,0));


        root.getChildren().addAll(lijevaStrana,srednjaStrana,desnaStrana);
        Scene scene = new Scene(root,750,600);
        glavnaScena.setTitle("Vlasnik "+ vlasnik.getIme()+" "+vlasnik.getPrezime());
        glavnaScena.setScene(scene);
    }


    // Prikazuje sve objekte koji su odobreni i na čekanju
    private void updateSveObjekte(ScrollPane spObjekti,VBox srednjaStrana,Vlasnik vlasnik,Label lbl,VBox desnaStrana){
        lbl.setText("Svi objekti: ");
        Objekat[] objektiVlasnika = Objekat.getObjektiPoVlasniku(db.getObjekat(),vlasnik);
        VBox temp = new VBox();
        for(Objekat ob: objektiVlasnika) {
            if(ob.getStatus()==Status.ODBIJEN) {
                continue;
            }
            if(ob.getStatus()==Status.ODOBREN){
                Obavjestenje obav = Obavjestenje.getObavjestenjePoObjektu(db.getObavjestenje(),ob);
                if(obav==null){
                    try {
                        db.obrisiObjekat(ob);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                if(obav.getTekst().equalsIgnoreCase("")){
                    continue;
                }
            }

            Button btnObjekat = new Button("Naziv: " + ob.getNaziv() + ", Status: " + ob.getStatus());
            btnObjekat.setPrefWidth(spObjekti.getPrefViewportWidth());
            btnObjekat.setOnAction(_ -> {
                srednjaStrana.getChildren().clear();
                desnaStrana.getChildren().clear();

                Label lblImeObjekta = new Label("Naziv: "+ob.getNaziv());
                Label lblStatus = new Label("Status: "+ ob.getStatus());
                Label lblAdresa = new Label("Adresa: "+ob.getAdresa());
                Label lblBrojMjesta = new Label("Broj mjesta: "+ob.getBroj_mjesta());
                Label lblCijenaRezervacije = new Label("Cijena rezervacije: "+ ob.getCijena_rezervacije());
                Label lblZarada =  new Label("Zarada: "+ob.getZarada());

                srednjaStrana.getChildren().addAll(lblImeObjekta,lblStatus,lblAdresa,lblBrojMjesta);
                srednjaStrana.getChildren().addAll(lblCijenaRezervacije,lblZarada);

                if(ob.getStatus()==Status.ODOBREN) {

                    // PROSLAVE
                    Proslava[] sveProslaveObjekta = Proslava.getProslavaPoObjektu(db.getProslava(),ob);

                    Label lblRezervacije = new Label("Rezervacije: ");
                    lblRezervacije.setTextFill(Color.GRAY);

                    HBox proslavePrikaz = new HBox(5);
                    Button btnProsleProslave = new Button("Protekle");
                    Button btnBuduceProslave = new Button("Aktivne");
                    Button btnOtkazaneProslave = new Button("Otkazane");
                    proslavePrikaz.getChildren().addAll(btnProsleProslave,btnBuduceProslave,btnOtkazaneProslave);

                    ArrayList<Proslava> proslaveProsleList = new ArrayList<>();
                    ArrayList<Proslava> proslaveBuduceList = new ArrayList<>();
                    ArrayList<Proslava> proslaveOtkazaneList = new ArrayList<>();

                    ScrollPane spSveProslave = new ScrollPane();
                    spSveProslave.setPrefHeight(200);
                    spSveProslave.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    VBox vSveProslave = new VBox();

                    btnProsleProslave.setOnAction(_->{
                        dodajDugmeProslave(proslaveProsleList.toArray(new Proslava[0]),desnaStrana,vSveProslave,spSveProslave);
                    });
                    btnBuduceProslave.setOnAction(_->{
                        dodajDugmeProslave(proslaveBuduceList.toArray(new Proslava[0]),desnaStrana,vSveProslave,spSveProslave);
                    });
                    btnOtkazaneProslave.setOnAction(_->{
                        dodajDugmeProslave(proslaveOtkazaneList.toArray(new Proslava[0]),desnaStrana,vSveProslave,spSveProslave);
                    });

                    dodajDugmeProslave(proslaveBuduceList.toArray(new Proslava[0]),desnaStrana,vSveProslave,spSveProslave);

                    LocalDate danasnjiDatum = LocalDate.now();

                    for(Proslava pr: sveProslaveObjekta)
                    {
                        if(pr.getProslavacol().equalsIgnoreCase("OTKAZAN")){
                            proslaveOtkazaneList.add(pr);
                        }
                        else if(pr.getDatum().isBefore(danasnjiDatum)) {
                            proslaveProsleList.add(pr);
                        }else{
                            proslaveBuduceList.add(pr);
                        }
                    }

                    srednjaStrana.getChildren().addAll(lblRezervacije,proslavePrikaz,spSveProslave);

                    Button btnDatumDodaj = new Button("Prikaži datume");
                    btnDatumDodaj.setOnAction(_->{
                        desnaStrana.getChildren().clear();
                        Set<LocalDate> odabraniDani = new HashSet<>();
                        Set<LocalDate> zauzetiDani = new HashSet<>();

                        Proslava[] sveProslaveOb = Proslava.getProslavaPoObjektu(db.getProslava(),ob);
                        for(Proslava p:sveProslaveOb){
                            if(!p.jeOtkazana())
                                zauzetiDani.add(p.getDatum());
                        }
                        odabraniDani.addAll(Arrays.asList(ob.getSlobodniDatumi()));

                        final long[] m = {0};
                        HBox mjeseciPlus = new HBox();
                        Button btnKalendarLijevo = new Button("<<");
                        Button btnKalendarDesno = new Button(">>");
                        mjeseciPlus.getChildren().addAll(btnKalendarLijevo,btnKalendarDesno);

                        final GridPane[] kalendar = {getKalendar(odabraniDani, zauzetiDani, m[0])};

                        btnKalendarLijevo.setOnAction(_->{
                            desnaStrana.getChildren().remove(1);
                            m[0]--;
                            kalendar[0] = getKalendar(odabraniDani,zauzetiDani, m[0]);
                            desnaStrana.getChildren().add(1,kalendar[0]);
                        });
                        btnKalendarDesno.setOnAction(_->{
                            desnaStrana.getChildren().remove(1);
                            m[0]++;
                            kalendar[0] = getKalendar(odabraniDani,zauzetiDani, m[0]);
                            desnaStrana.getChildren().add(1,kalendar[0]);
                        });

                        desnaStrana.getChildren().addAll(mjeseciPlus, kalendar[0]);

                        Button btnPromjeniDatume = new Button("Potvrdi promjene");
                        btnPromjeniDatume.setOnAction(_->{
                           desnaStrana.getChildren().clear();
                            try {
                                db.promjeniDatume(odabraniDani,ob);
                            } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                throw new RuntimeException(e);
                            }
                        });
                        desnaStrana.getChildren().add(btnPromjeniDatume);
                    });

                    Button btnObrisiObjekat = new Button("Obrisi Objekat");
                    btnObrisiObjekat.setOnAction(_->{
                        try {
                            db.obrisiObjekat(ob);
                            srednjaStrana.getChildren().clear();
                            updateSveObjekte(spObjekti,srednjaStrana,vlasnik,lbl,desnaStrana);
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        }
                    });
                    srednjaStrana.getChildren().addAll(btnDatumDodaj,btnObrisiObjekat);

                }



            });
            temp.getChildren().add(btnObjekat);
        }
        spObjekti.setContent(temp);
    }

    // Dodaje proslave u listu kod odobrenih objekata
    private void dodajDugmeProslave(Proslava[] proslave,VBox desnaStrana,VBox proslaveSve,ScrollPane spProslave){
        proslaveSve.getChildren().clear();
        for(Proslava pr: proslave)
        {
            Button btnPrikaziDatum= new Button(pr.getDatum().toString());
            btnPrikaziDatum.setPrefWidth(200);
            btnPrikaziDatum.setOnAction(_ -> {
                Label lblBrojGostiju = new Label("Broj gostiju: " + pr.getBroj_gostiju());

                Label lblRasporedGostiju = new Label("Raspored gostiju");
                lblRasporedGostiju.setTextFill(Color.GRAY);

                Raspored[] rasporedi = Raspored.getRasporedPoProslavi(db.getRaspored(),pr);
                ScrollPane spRasporedGostiju = new ScrollPane();
                VBox vRasporedGostiju = new VBox();

                int i=1;
                for(Raspored raspored: rasporedi)
                {
                    TextArea txt = new TextArea();
                    txt.setEditable(false);
                    txt.setWrapText(true);
                    txt.setText("Sto "+(i++)+": "+raspored.getGosti());
                    txt.setPrefWidth(200);
                    txt.setPrefHeight(150);

                    vRasporedGostiju.getChildren().add(txt);
                }
                spRasporedGostiju.setContent(vRasporedGostiju);

                Label lblMeni = new Label("Meni");
                lblMeni.setTextFill(Color.GRAY);
                TextArea txtMeni = new TextArea();
                txtMeni.setEditable(false);
                txtMeni.setWrapText(true);
                txtMeni.setText(pr.getMeni().getOpis());
                txtMeni.setPrefWidth(200);
                txtMeni.setPrefHeight(150);

                Label lblUkupnaCijena = new Label("Ukupna cijena: "+ pr.getUkupna_cijena());
                Label lblJelUplacen=new Label();
                if(pr.getUkupna_cijena()<=pr.getUplacen_iznos()){
                    lblJelUplacen.setText("Uplacen sav iznos");
                    lblJelUplacen.setTextFill(Color.GREEN);
                }else{
                    lblJelUplacen.setText("Nije uplacen sav iznos");
                    lblJelUplacen.setTextFill(Color.RED);
                }

                desnaStrana.getChildren().clear();
                desnaStrana.getChildren().addAll(lblBrojGostiju,lblRasporedGostiju,spRasporedGostiju,lblMeni,txtMeni,lblUkupnaCijena,lblJelUplacen);
            });
            proslaveSve.getChildren().addAll(btnPrikaziDatum);
        }
        spProslave.setContent(proslaveSve);
    }

    // Prikazuje sve objekte koji su odobreni ali nisu podeseni i odbijeni
    private void updatePorukeObjekata(ScrollPane spObjekti,VBox srednjaStrana,Vlasnik vlasnik,Label lbl,VBox desnaStrana)
    {
        lbl.setText("Poruke: ");
        Objekat[] objektiVlasnika = Objekat.getObjektiPoVlasniku(db.getObjekat(),vlasnik);
        VBox temp = new VBox();
        for(Objekat ob: objektiVlasnika) {
            if(ob.getStatus()==Status.NA_CEKANJU)
                continue;
            if(ob.getStatus()==Status.ODOBREN)
            {
                Obavjestenje obav = Obavjestenje.getObavjestenjePoObjektu(db.getObavjestenje(),ob);
                if(obav==null)
                    obav = new Obavjestenje(1,ob,"");

                if(obav.getTekst().equalsIgnoreCase("ODOBREN"))
                    continue;

            }


            Button btnObjekat = new Button("Naziv: " + ob.getNaziv() + ", Status: " + ob.getStatus());
            btnObjekat.setPrefWidth(spObjekti.getPrefViewportWidth());
            btnObjekat.setOnAction(_ -> {
                srednjaStrana.getChildren().clear();

                Label lblImeObjekta = new Label("Naziv: "+ob.getNaziv());
                Label lblStatus = new Label("Status: "+ ob.getStatus());
                Label lblAdresa = new Label("Adresa: "+ob.getAdresa());
                Label lblBrojMjesta = new Label("Broj mjesta: "+ob.getBroj_mjesta());
                Label lblCijenaRezervacije = new Label("Cijena rezervacije: "+ ob.getCijena_rezervacije());
                Label lblZarada =  new Label("Zarada: "+ob.getZarada());

                srednjaStrana.getChildren().addAll(lblImeObjekta,lblStatus,lblAdresa,lblBrojMjesta);
                srednjaStrana.getChildren().addAll(lblCijenaRezervacije,lblZarada);


                if(ob.getStatus()==Status.ODOBREN) {
                    Label lblUpozorenje = new Label("Potrebno je postaviti slobodne datume!");
                    lblUpozorenje.setTextFill(Color.RED);

                    Button btnDodajDatume = new Button("Dodaj Datume");
                    btnDodajDatume.setOnAction(_ -> {
                        desnaStrana.getChildren().clear();

                        Set<LocalDate> odabraniDani = new HashSet<>();
                        Set<LocalDate> zauzetiDani = new HashSet<>();

                        Proslava[] sveProslaveOb = Proslava.getProslavaPoObjektu(db.getProslava(),ob);
                        for(Proslava p:sveProslaveOb){
                            zauzetiDani.add(p.getDatum());
                        }
                        odabraniDani.addAll(Arrays.asList(ob.getSlobodniDatumi()));
                        final long[] m = {0};
                        HBox mjeseciPlus = new HBox();
                        Button btnKalendarLijevo = new Button("<<");
                        Button btnKalendarDesno = new Button(">>");
                        mjeseciPlus.getChildren().addAll(btnKalendarLijevo,btnKalendarDesno);

                        final GridPane[] kalendar = {getKalendar(odabraniDani, zauzetiDani, m[0])};

                        btnKalendarLijevo.setOnAction(_->{
                            desnaStrana.getChildren().remove(1);
                            m[0]--;
                            kalendar[0] = getKalendar(odabraniDani,zauzetiDani, m[0]);
                            desnaStrana.getChildren().add(1,kalendar[0]);
                        });
                        btnKalendarDesno.setOnAction(_->{
                            desnaStrana.getChildren().remove(1);
                            m[0]++;
                            kalendar[0] = getKalendar(odabraniDani,zauzetiDani, m[0]);
                            desnaStrana.getChildren().add(1,kalendar[0]);
                        });


                        desnaStrana.getChildren().addAll(mjeseciPlus,kalendar[0]);

                        Button btnPromjeniDatume = new Button("Potvrdi promjene");
                        btnPromjeniDatume.setOnAction(_->{
                            desnaStrana.getChildren().clear();
                            try {
                                db.promjeniDatume(odabraniDani,ob);
                                db.zavrsiOdobravanjeObjekta(ob);
                                srednjaStrana.getChildren().clear();
                                updatePorukeObjekata(spObjekti,srednjaStrana,vlasnik,lbl,desnaStrana);
                            } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                throw new RuntimeException(e);
                            }
                        });
                        desnaStrana.getChildren().add(btnPromjeniDatume);


                    });

                    srednjaStrana.getChildren().addAll(btnDodajDatume,lblUpozorenje);
                }
                else if(ob.getStatus()==Status.ODBIJEN) {
                    Obavjestenje obavjestenje = Obavjestenje.getObavjestenjePoObjektu(db.getObavjestenje(),ob);

                    VBox vPorukaAdmina = new VBox();
                    if(obavjestenje!=null)
                    {
                        Label lblPorukaAdmina = new Label("");
                        lblPorukaAdmina.setText("Poruka admina: ");


                        lblPorukaAdmina.setTextFill(Color.RED);
                        TextArea txtPorukaAdmina = new TextArea();
                        txtPorukaAdmina.setEditable(false);
                        txtPorukaAdmina.setPrefWidth(200);
                        txtPorukaAdmina.setPrefHeight(100);

                        txtPorukaAdmina.setText(obavjestenje.getTekst());
                        vPorukaAdmina.getChildren().addAll(lblPorukaAdmina,txtPorukaAdmina);
                    }


                    Button btnObnovaForma = new Button("Promjeni prijavu");
                    btnObnovaForma.setOnAction(_->{
                        updatePorukeObjekata(spObjekti,srednjaStrana,vlasnik,lbl,desnaStrana);
                        updateObjekatForma(ob,desnaStrana,srednjaStrana);
                    });

                    Button btnObrisiFormu = new Button("Obriši prijavu");
                    btnObrisiFormu.setOnAction(_->{
                        try {
                            db.obrisiObjekat(ob);
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        }
                        updatePorukeObjekata(spObjekti,srednjaStrana,vlasnik,lbl,desnaStrana);
                        srednjaStrana.getChildren().clear();

                    });

                    srednjaStrana.getChildren().addAll(vPorukaAdmina,btnObrisiFormu,btnObnovaForma);
                }



            });
            temp.getChildren().add(btnObjekat);
        }

        spObjekti.setContent(temp);


    }


    // Forma za slanje update-ovanog objekta
    private void updateObjekatForma(Objekat objekat,VBox desnaStrana,VBox srednjaStrana){
        desnaStrana.getChildren().clear();


        HBox hBoxIme = new HBox();
        Label lblImeObjekta = new Label("Naziv: ");
        TextField txtImeObjekta = new TextField();
        txtImeObjekta.setPromptText("Ime...");
        txtImeObjekta.setText(objekat.getNaziv());
        hBoxIme.getChildren().addAll(lblImeObjekta,txtImeObjekta);

        HBox hBoxAdresa = new HBox();
        Label lblAdresaObjekta = new Label("Adresa: ");
        TextField txtAdresaObjekta = new TextField();
        txtAdresaObjekta.setPromptText("Adresa...");
        txtAdresaObjekta.setText(objekat.getAdresa());
        hBoxAdresa.getChildren().addAll(lblAdresaObjekta,txtAdresaObjekta);

        HBox hBoxGrad = new HBox();
        Label lblGradObjekta = new Label("Grad: ");
        TextField txtGradObjekta = new TextField();
        txtGradObjekta.setPromptText("Grad...");
        txtGradObjekta.setText(objekat.getNaziv());
        hBoxGrad.getChildren().addAll(lblGradObjekta,txtGradObjekta);

        HBox hBoxBrojMjesta = new HBox();
        Label lblBrojMjesta = new Label("Broj mjesta: ");
        TextField txtBrojMjesta= getBrojcanoPolje();
        hBoxBrojMjesta.getChildren().addAll(lblBrojMjesta,txtBrojMjesta);


        HBox hCijenaRezervacije = new HBox();
        Label lblCijenaRezervacije = new Label("Cijena rezervacije: ");
        TextField txtCijenaRezervacije = getBrojcanoPolje();
        txtCijenaRezervacije.setText(Float.toString(objekat.getCijena_rezervacije()));
        hCijenaRezervacije.getChildren().addAll(lblCijenaRezervacije,txtCijenaRezervacije);

        ScrollPane spMeniji = new ScrollPane();
        spMeniji.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        spMeniji.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spMeniji.setPrefWidth(200);
        spMeniji.setPrefHeight(150);
        VBox sviMeniji = new VBox(1);

        Button btnDodajMeni = new Button("Dodaj meni");
        btnDodajMeni.setOnAction(_->{
            HBox hbMeni = getMeniBox(true);

            sviMeniji.getChildren().add(hbMeni);
            spMeniji.setContent(sviMeniji);
        });

        Meni[] menis = Meni.getMeniPoObjektu(db.getMeni(),objekat);
        for(Meni meni:menis)
        {
            HBox hbMeni = getMeniBox(meni.getOpis(),meni.getCijena_po_osobi(),false);


            sviMeniji.getChildren().add(hbMeni);
            spMeniji.setContent(sviMeniji);
        }


        Button btnDodajSto = new Button("Dodaj sto");
        ScrollPane spStolovi = new ScrollPane();
        spStolovi.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        spStolovi.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox stolovi = new VBox();
        spStolovi.setPrefWidth(150);
        spStolovi.setPrefHeight(200);


        Sto[] stoloviIZBaze = Sto.StoloviPoObjektu(db.getSto(),objekat);
        for(Sto sto:stoloviIZBaze)
        {
            dodajStoloviObjekta(spStolovi,stolovi,sto.getBroj_mjesta());
        }

        Label lblGreska = new Label("");
        lblGreska.setTextFill(Color.RED);

        spStolovi.setContent(stolovi);

        btnDodajSto.setOnAction(_->{
            dodajStoloviObjekta(spStolovi,stolovi);
        });

        Button btnUpdateFormu=new Button("Update formu");
        btnUpdateFormu.setOnAction(_->{
            if(txtAdresaObjekta.getText().isEmpty()
                    || txtGradObjekta.getText().isEmpty()
                    || txtImeObjekta.getText().isEmpty()
                    || txtBrojMjesta.getText().isEmpty()
                    || txtCijenaRezervacije.getText().isEmpty()
                    || sviMeniji.getChildren().isEmpty()
            )
            {
                lblGreska.setText("Moraju biti sva polja popunjena. ");
                return;
            }


            objekat.setStatus(Status.NA_CEKANJU);
            objekat.setAdresa(txtAdresaObjekta.getText());
            objekat.setGrad(txtGradObjekta.getText());
            objekat.setCijena_rezervacije(Float.parseFloat(txtCijenaRezervacije.getText()));
            objekat.setBroj_stolova(stolovi.getChildren().size());
            objekat.setBroj_mjesta(Integer.parseInt(txtBrojMjesta.getText()));
            objekat.setNaziv(txtImeObjekta.getText());


            ArrayList<Meni> SviMeniji = new ArrayList<>();
            for(Node node : sviMeniji.getChildren())
            {
                Meni meni = new Meni(
                        0,
                        objekat,
                        ((TextField)((HBox)node).getChildren().getFirst()).getText(),
                        (Float.parseFloat(((TextField)((HBox)node).getChildren().getLast()).getText()))
                );
                SviMeniji.add(meni);
            }

            ArrayList<Sto> stolo = new ArrayList<>();
            for(Node child: stolovi.getChildren()) {
                assert child instanceof HBox;
                TextField txt = (TextField) ((HBox)child).getChildren().getLast();
                Sto s = new Sto(1,objekat,Integer.parseInt(txt.getText()));
                stolo.add(s);
            }

            Objekat noviObjekat = new Objekat(
                    -1,
                    objekat.getVlasnik(),
                    txtImeObjekta.getText(),
                    Float.parseFloat(txtCijenaRezervacije.getText()),
                    txtGradObjekta.getText(),
                    txtAdresaObjekta.getText(),
                    Integer.parseInt(txtBrojMjesta.getText()),
                    stolovi.getChildren().size(),
                    "",
                    0,
                    Status.NA_CEKANJU
            );


            try {
                db.obrisiObjekat(objekat);
                db.noviObjekat(noviObjekat,SviMeniji.toArray(new Meni[0]),stolo.toArray(new Sto[0]));
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
            desnaStrana.getChildren().clear();
            srednjaStrana.getChildren().clear();
        });

        Button btnOtkaziFormu=new Button("Otkaži ");
        btnOtkaziFormu.setOnAction(_->{
            desnaStrana.getChildren().clear();
        });

        desnaStrana.getChildren().addAll(hBoxIme,hBoxGrad,hBoxAdresa,hBoxBrojMjesta,btnDodajMeni,spMeniji);
        desnaStrana.getChildren().addAll(lblGreska,btnDodajSto,spStolovi,btnUpdateFormu,btnOtkaziFormu);

    }

    // Forma za slanje novog objekta
    private void novaObjekatForma(VBox desnaStrana,Vlasnik vlasnik,VBox srednjaStrana){
        desnaStrana.getChildren().clear();
        srednjaStrana.getChildren().clear();

        HBox hBoxIme = new HBox();
        Label lblImeObjekta = new Label("Naziv: ");
        TextField txtImeObjekta = new TextField();
        txtImeObjekta.setPromptText("Ime...");
        hBoxIme.getChildren().addAll(lblImeObjekta,txtImeObjekta);

        HBox hBoxAdresa = new HBox();
        Label lblAdresaObjekta = new Label("Adresa: ");
        TextField txtAdresaObjekta = new TextField();
        txtAdresaObjekta.setPromptText("Adresa...");
        hBoxAdresa.getChildren().addAll(lblAdresaObjekta,txtAdresaObjekta);

        HBox hBoxGrad = new HBox();
        Label lblGradObjekta = new Label("Grad: ");
        TextField txtGradObjekta = new TextField();
        txtGradObjekta.setPromptText("Grad...");
        hBoxGrad.getChildren().addAll(lblGradObjekta,txtGradObjekta);


        Button btnDodajSto=new Button("Dodaj sto");
        ScrollPane spStolovi = new ScrollPane();
        spStolovi.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        spStolovi.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox stolovi = new VBox();
        spStolovi.setPrefWidth(200);
        spStolovi.setPrefHeight(150);

        HBox hBoxBrojMjesta = new HBox();
        Label lblBrojMjesta = new Label("Broj mjesta: ");
        TextField txtBrojMjesta= getBrojcanoPolje();
        hBoxBrojMjesta.getChildren().addAll(lblBrojMjesta,txtBrojMjesta);

        HBox hCijenaRezervacije = new HBox();
        Label lblCijenaRezervacije = new Label("Cijena rezervacije: ");
        TextField txtCijenaRezervacije = getBrojcanoPolje();
        hCijenaRezervacije.getChildren().addAll(lblCijenaRezervacije,txtCijenaRezervacije);


        ScrollPane spMeniji = new ScrollPane();
        spMeniji.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        spMeniji.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spMeniji.setPrefWidth(200);
        spMeniji.setPrefHeight(150);
        VBox sviMeniji = new VBox(1);

        Button btnDodajMeni = new Button("Dodaj meni");
        btnDodajMeni.setOnAction(_->{
            HBox meniHBOX = getMeniBox(true);

            sviMeniji.getChildren().add(meniHBOX);
            spMeniji.setContent(sviMeniji);
        });

        Label lblGreska = new Label("");
        lblGreska.setTextFill(Color.RED);

        spStolovi.setContent(stolovi);
        btnDodajSto.setOnAction(_->{
            dodajStoloviObjekta(spStolovi,stolovi);
        });


        Button btnUpdateFormu = new Button("Posalji formu");
        btnUpdateFormu.setOnAction(_->{
            if(txtAdresaObjekta.getText().isEmpty()
                    || txtGradObjekta.getText().isEmpty()
                    || txtImeObjekta.getText().isEmpty()
                    || txtBrojMjesta.getText().isEmpty()
                    || txtCijenaRezervacije.getText().isEmpty()
            )
            {
                lblGreska.setText("Moraju biti sva polja popunjena. ");
                return;
            }

            Objekat noviObjekat = new Objekat(
                    -1,
                    vlasnik,
                    txtImeObjekta.getText(),
                    Float.parseFloat(txtCijenaRezervacije.getText()),
                    txtGradObjekta.getText(),
                    txtAdresaObjekta.getText(),
                    Integer.parseInt(txtBrojMjesta.getText()),
                    stolovi.getChildren().size(),
                    "",
                    0,
                    Status.NA_CEKANJU
            );

            ArrayList<Meni> SviMeniji = new ArrayList<>();
            for(Node node : sviMeniji.getChildren())
            {
                Meni meni = new Meni(
                        0,
                        noviObjekat,
                        ((TextArea)((HBox)node).getChildren().getFirst()).getText(),
                        (Float.parseFloat(((TextField)((HBox)node).getChildren().getLast()).getText()))
                );
                SviMeniji.add(meni);
            }

            ArrayList<Sto> stolo = new ArrayList<>();
            for(Node child: stolovi.getChildren()) {
                assert child instanceof HBox;
                TextField txt = (TextField) ((HBox)child).getChildren().getLast();
                Sto s = new Sto(1,noviObjekat,Integer.parseInt(txt.getText()));
                stolo.add(s);
            }
            try {
                db.noviObjekat(noviObjekat,SviMeniji.toArray(new Meni[0]),stolo.toArray(new Sto[0]));
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
            desnaStrana.getChildren().clear();
            srednjaStrana.getChildren().clear();
        });

        Button btnOtkaziFormu = new Button("Otkaži ");
        btnOtkaziFormu.setOnAction(_->{
            desnaStrana.getChildren().clear();
        });

        desnaStrana.getChildren().addAll(hBoxIme,hBoxGrad,hBoxAdresa,hBoxBrojMjesta,hCijenaRezervacije,btnDodajMeni,spMeniji);
        desnaStrana.getChildren().addAll(lblGreska,btnDodajSto,spStolovi,btnUpdateFormu);
        desnaStrana.getChildren().add(btnOtkaziFormu);
    }


    // Dodaje stolove u listu za Formu objekata
    private void dodajStoloviObjekta(ScrollPane SviStolovi,VBox stolovi){
        dodajStoloviObjekta(SviStolovi,stolovi,0);
    }

    private void dodajStoloviObjekta(ScrollPane SviStolovi,VBox stolovi,int broj)
    {
        HBox hbSto = new HBox(10);

        Label lblSto = new Label("Sto " + (stolovi.getChildren().size()+1)+": ");

        TextField tfSto = getBrojcanoPolje();
        tfSto.setText(Integer.toString(broj));

        hbSto.getChildren().addAll(lblSto,tfSto);
        stolovi.getChildren().addAll(hbSto);
        SviStolovi.setContent(stolovi);
    }

    // --------------------------------------------------
    // POMOCNE METODE
    // --------------------------------------------------

    private HBox getMeniBox(boolean editable){
        return getMeniBox("",0,editable);
    }

    private HBox getMeniBox(String meni,float broj, boolean editable){
        HBox hbMeni = new HBox();
        TextArea txtMeni = new TextArea();
        txtMeni.setText(meni);
        txtMeni.setPromptText("Meni...");
        txtMeni.setPrefWidth(150);
        txtMeni.setPrefHeight(60);
        txtMeni.setWrapText(true);
        txtMeni.setEditable(editable);

        TextField txtBroj;
        if(editable){
            txtBroj = getBrojcanoPolje();
            txtBroj.setText(Float.toString(broj));

        }
        else{
            txtBroj = new TextField();
            txtBroj.setText(Float.toString(broj));
        }
        txtBroj.setPrefWidth(50);
        txtBroj.setEditable(editable);

        hbMeni.getChildren().addAll(txtMeni,txtBroj);
        return hbMeni;

    }

    private TextField getBrojcanoPolje(){
        TextField Numeric_Field = new TextField();
        UnaryOperator<TextFormatter.Change> Integer_Filter = change -> {
            String Demo_Text = change.getControlNewText();
            if (Demo_Text.matches("-?([1-9][0-9]*)?")) {
                return change;
            } else if ("-".equals(change.getText())) {
                if (change.getControlText().startsWith("-")) {
                    change.setText("");
                    change.setRange(0, 1);
                    change.setCaretPosition(change.getCaretPosition() - 2);
                    change.setAnchor(change.getAnchor() - 2);
                    return change;
                } else {
                    change.setRange(0, 0);
                    return change;
                }
            }
            return null;
        };

        StringConverter<Integer> String_Converter = new IntegerStringConverter() {
            @Override
            public Integer fromString(String s) {
                if (s.isEmpty())
                    return 0;
                return super.fromString(s);
            }
        };

        TextFormatter<Integer> Text_Formatter = new TextFormatter<Integer>(String_Converter, 0, Integer_Filter);
        Numeric_Field.setTextFormatter(Text_Formatter);
        Numeric_Field.setOnMouseClicked(_->{
            Numeric_Field.selectAll();
        });

        return Numeric_Field;
    }

    private GridPane getKalendar(Set<LocalDate> odabraniDani,Set<LocalDate> zauzetiDani,long mjeseci){
        GridPane kalendar = new GridPane();
        kalendar.setHgap(10);
        kalendar.setVgap(10);
        kalendar.setAlignment(Pos.CENTER);

        YearMonth trenutniMjesec = YearMonth.now().plusMonths(mjeseci);

        LocalDate dan1 = trenutniMjesec.atDay(1);
        int prviDanSedmice = dan1.getDayOfWeek().getValue();
        int daniMjeseca = trenutniMjesec.lengthOfMonth();


        String[] dani = {"Pon", "Uto", "Sri", "Čet", "Pet", "Sub", "Ned"};
        for (int i = 0; i < dani.length; i++) {
            Label lblDani = new Label(dani[i]);
            kalendar.add(lblDani, i, 1);
        }

        int vrsta = 2;
        int kolona = prviDanSedmice - 1;

        for (int dan = 1; dan <= daniMjeseca; dan++) {
            LocalDate trenutniDatum = trenutniMjesec.atDay(dan);


            Label lblDan = new Label(String.valueOf(dan));
            if(zauzetiDani.contains(trenutniDatum)){
                lblDan.setTextFill(Color.RED);
            }
            else if(odabraniDani.contains(trenutniDatum)){
                lblDan.setTextFill(Color.GREEN);
            }
            else
                lblDan.setTextFill(Color.BLACK);

            HBox hBox = new HBox();
            hBox.getChildren().add(lblDan);
            hBox.setOnMouseClicked(event -> {
                if(lblDan.getTextFill()==Color.RED){
                    return;
                }
                if (odabraniDani.contains(trenutniDatum)) {
                    odabraniDani.remove(trenutniDatum);
                    lblDan.setTextFill(Color.BLACK);
                } else {
                    odabraniDani.add(trenutniDatum);
                    lblDan.setTextFill(Color.GREEN);
                }
            });

            kalendar.add(hBox, kolona, vrsta);

            kolona++;
            if (kolona > 6) {
                kolona = 0;
                vrsta++;
            }
        }



        return kalendar;
    }

    private GridPane getKalendarKlijentu(Set<LocalDate> slobodniDani, boolean editable,long mjeseci){
        GridPane kalendar = new GridPane();
        kalendar.setHgap(10);
        kalendar.setVgap(10);
        kalendar.setAlignment(Pos.CENTER);

        YearMonth trenutniMjesec = YearMonth.now().plusMonths(mjeseci);

        LocalDate dan1 = trenutniMjesec.atDay(1);
        int prviDanSedmice = dan1.getDayOfWeek().getValue();
        int daniMjeseca = trenutniMjesec.lengthOfMonth();


        String[] dani = {"Pon", "Uto", "Sri", "Čet", "Pet", "Sub", "Ned"};
        for (int i = 0; i < dani.length; i++) {
            Label lblDani = new Label(dani[i]);
            kalendar.add(lblDani, i, 0);
        }

        int vrsta = 1;
        int kolona = prviDanSedmice - 1;

        for (int dan = 1; dan <= daniMjeseca; dan++) {
            LocalDate trenutniDatum = trenutniMjesec.atDay(dan);


            Label lblDan = new Label(String.valueOf(dan));
            if (slobodniDani.contains(trenutniDatum)) {
                lblDan.setTextFill(Color.GREEN);
            } else
                lblDan.setTextFill(Color.BLACK);

            HBox hBox = new HBox();
            hBox.getChildren().add(lblDan);
            hBox.setOnMouseClicked(event -> {
                if (!editable) {
                    return;
                }

                if (slobodniDani.contains(trenutniDatum)) {
                    slobodniDani.remove(trenutniDatum);
                    lblDan.setTextFill(Color.BLACK);
                } else {
                    slobodniDani.add(trenutniDatum);
                    lblDan.setTextFill(Color.GREEN);
                }
            });

            kalendar.add(hBox, kolona, vrsta);

            kolona++;
            if (kolona > 6) {
                kolona = 0;
                vrsta++;
            }
        }
        return kalendar;
    }

}
