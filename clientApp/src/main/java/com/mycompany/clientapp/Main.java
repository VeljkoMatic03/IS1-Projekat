/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.clientapp;

import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * @author HP
 */
public class Main {
    
    private static String currentUser;

    public interface ApiService {

        @GET("korisnici")
        Call<String> getKorisnici();

        @POST("korisnici/novi")
        Call<String> createKorisnik(
                @Query("ime") String ime,
                @Query("email") String email,
                @Query("godiste") int godiste,
                @Query("pol") String pol,
                @Query("mesto") String mesto
        );

        @PUT("korisnici/email")
        Call<String> changeEmail(
                @Query("stari") String stari,
                @Query("novi") String novi
        );

        @PUT("korisnici/mesto/{email}")
        Call<String> changeMesto(
                @Path("email") String email,
                @Query("mesto") String mesto
        );

        @GET("audio")
        Call<String> getSnimci();

        @PUT("audio/promeni-naziv")
        Call<String> promeniNaziv(
                @Query("stariNaziv") String stari,
                @Query("autor") String email,
                @Query("noviNaziv") String novi
        );

        @GET("audio/kategorije")
        Call<String> getKategorijeSnimak(
                @Query("naziv") String naziv,
                @Query("email") String email
        );

        @POST("audio/create")
        Call<String> createAudio(
                @Query("naziv") String naziv,
                @Query("email") String email,
                @Query("datum") String datum,
                @Query("trajanje") int trajanje
        );

        @DELETE("audio/{naziv}")
        Call<String> deleteAudioSnimak(
                @Path("naziv") String naziv,
                @Query("autor") String email,
                @Header("Authorization") String header
        );

        @PUT("audio/{naziv}")
        Call<String> addKategorija(
                @Path("naziv") String naziv,
                @Query("autor") String email,
                @Query("kategorija") String kategorija
        );

        @GET("kategorija")
        Call<String> getKategorije();

        @POST("kategorija/{naziv}")
        Call<String> createKategorija(
                @Path("naziv") String naziv
        );

        @GET("mesto")
        Call<String> getMesta();

        @POST("mesto/{naziv}")
        Call<String> createMesto(
                @Path("naziv") String naziv
        );

        @POST("ocena/{naziv}")
        Call<String> createOcena(
                @Path("naziv") String naziv,
                @Query("autor") String emailAutor,
                @Query("slusalac") String emailSlusalac,
                @Query("ocena") int ocena
        );

        @PUT("ocena/update/{naziv}")
        Call<String> updateOcena(
                @Path("naziv") String naziv,
                @Query("autor") String emailAutor,
                @Query("slusalac") String emailSlusalac,
                @Query("ocena") int ocena
        );

        @DELETE("ocena/delete/{naziv}")
        Call<String> deleteOcena(
                @Path("naziv") String naziv,
                @Query("autor") String emailAutor,
                @Query("slusalac") String emailSlusalac
        );

        @GET("ocena")
        Call<String> getOcene(
                @Query("naziv") String naziv,
                @Query("autor") String email
        );

        @POST("omiljeni/{naziv}")
        Call<String> createOmiljeni(
                @Path("naziv") String naziv,
                @Query("autor") String emailAutor,
                @Query("slusalac") String emailSlusalac
        );

        @GET("omiljeni")
        Call<String> getOmiljeni(
                @Query("korisnik") String email
        );

        @POST("paket/{naziv}")
        Call<String> createPaket(
                @Path("naziv") String naziv,
                @Query("cena") int cena
        );

        @PUT("paket/{naziv}")
        Call<String> updateCenaPaket(
                @Path("naziv") String naziv,
                @Query("novaCena") int cena
        );

        @GET("paket")
        Call<String> getPaketi();

        @POST("pretplata/create")
        Call<String> createPretplata(
                @Query("paket") String naziv,
                @Query("korisnik") String email,
                @Query("cena") int cena,
                @Query("datum") String datum
        );

        @GET("pretplata")
        Call<String> getPretplate(
                @Query("korisnik") String email
        );

        @POST("slusanje/{slusalac}")
        Call<String> createSlusanje(
                @Path("slusalac") String slusalac,
                @Query("naziv") String naziv,
                @Query("autor") String autor,
                @Query("vremeSlusanja") int vremeSlusanja,
                @Query("pocetak") int pocetnaSekunda
        );

        @GET("slusanje")
        Call<String> getSlusanja(
                @Query("naziv") String naziv,
                @Query("autor") String email
        );

    }

    public static void main(String args[]) {
        ApiService apiService = new Retrofit.Builder().baseUrl("http://localhost:8080/server_app/server/")
                .addConverterFactory(ScalarsConverterFactory.create()).build().create(ApiService.class);
        Scanner scanner = new Scanner(System.in);
        System.out.println("-------START------");
        System.out.println("Unesite email korisnika koji trenutno koristi servis: ");
        currentUser = scanner.nextLine();
        currentUser = new String(Base64.getEncoder().encodeToString(currentUser.getBytes()));
        while (true) {
            printMenu();
            System.out.println("Vas izbor: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if(choice == 0) break;
            execute(choice, apiService, scanner);
            System.out.println("Pritisnite ENTER za nastavak");
            scanner.nextLine();
        }
        System.out.println("-------KRAJ-------");
    }

    private static void printMenu() {
        System.out.println("-------MENI-------");
        System.out.println("Dostupne funkcije:");
        for (int i = 0; i <= 27; i++) {
            System.out.println("" + i + ". " + funkcije[i]);
        }
    }

    private static void execute(int funNumber, ApiService service, Scanner scanner) {
        try {
            Call<String> call;
//        String naziv, ime, pol, mesto, email;
//        int godiste;
            switch (funNumber) {
                case 1:
                    call = createMesto(scanner, service);
                    break;
                case 2:
                    call = createKorisnik(scanner, service);
                    break;
                case 3:
                    call = changeEmail(scanner, service);
                    break;
                case 4:
                    call = changeMesto(scanner, service);
                    break;
                case 5:
                    call = createKategorija(scanner, service);
                    break;
                case 6:
                    call = createAudio(scanner, service);
                    break;
                case 7:
                    call = promeniNaziv(scanner, service);
                    break;
                case 8:
                    call = addKategorija(scanner, service);
                    break;
                case 9:
                    call = createPaket(scanner, service);
                    break;
                case 10:
                    call = updateCenaPaket(scanner, service);
                    break;
                case 11:
                    call = createPretplata(scanner, service);
                    break;
                case 12:
                    call = createSlusanje(scanner, service);
                    break;
                case 13:
                    call = createOmiljeni(scanner, service);
                    break;
                case 14:
                    call = createOcena(scanner, service);
                    break;
                case 15:
                    call = updateOcena(scanner, service);
                    break;
                case 16:
                    call = deleteOcena(scanner, service);
                    break;
                case 17:
                    call = deleteAudioSnimak(scanner, service);
                    break;
                case 18:
                    call = getMesta(scanner, service);
                    break;
                case 19:
                    call = getKorisnici(scanner, service);
                    break;
                case 20:
                    call = getKategorije(scanner, service);
                    break;
                case 21:
                    call = getAudio(scanner, service);
                    break;
                case 22:
                    call = getKategorijeSnimak(scanner, service);
                    break;
                case 23:
                    call = getPaketi(scanner, service);
                    break;
                case 24:
                    call = getPretplate(scanner, service);
                    break;
                case 25:
                    call = getSlusanja(scanner, service);
                    break;
                case 26:
                    call = getOcene(scanner, service);
                    break;
                case 27:
                    call = getOmiljeni(scanner, service);
                    break;
                default:
                    call = new Call<String>() {
                        @Override
                        public Response<String> execute() {
                            return Response.success("Fake response");
                        }

                        @Override
                        public void enqueue(Callback<String> callback) {
                            callback.onResponse(this, Response.success("Fake response"));
                        }

                        @Override
                        public boolean isExecuted() {
                            return false;
                        }

                        @Override
                        public void cancel() {
                        }

                        @Override
                        public boolean isCanceled() {
                            return false;
                        }

                        @Override
                        public Call<String> clone() {
                            return null;
                        }

                        @Override
                        public Request request() {
                            return null;
                        }

                        @Override
                        public Timeout timeout() {
                            return null;
                        }
                    };
            }
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                String body = response.body();
                body = body.replace("|", " - ");
                body = body.replace(";", "\n");
                System.out.println("-----RESULT-----");
                System.out.println(body);
            } else {
                System.out.println("Error code: " + response.code());
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static Call<String> createMesto(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv mesta: ");
        String naziv = scanner.nextLine();
        Call<String> call = service.createMesto(naziv);
        return call;
    }

    private static Call<String> createKorisnik(Scanner scanner, ApiService service) {
        System.out.println("Unesite ime: ");
        String ime = scanner.nextLine();
        System.out.println("Unesite email: ");
        String email = scanner.nextLine();
        System.out.println("Unesite pol: ");
        String pol = scanner.nextLine();
        System.out.println("Unesite mesto: ");
        String mesto = scanner.nextLine();
        System.out.println("Unesite godiste: ");
        int godiste = Integer.parseInt(scanner.nextLine());
        Call<String> call = service.createKorisnik(ime, email, godiste, pol, mesto);
        return call;
    }

    private static Call<String> changeEmail(Scanner scanner, ApiService service) {
        System.out.println("Unesite stari email: ");
        String stari = scanner.nextLine();
        System.out.println("Unesite ime: ");
        String novi = scanner.nextLine();
        Call<String> call = service.changeEmail(stari, novi);
        return call;
    }

    private static Call<String> changeMesto(Scanner scanner, ApiService service) {
        System.out.println("Unesite email: ");
        String email = scanner.nextLine();
        System.out.println("Unesite mesto: ");
        String mesto = scanner.nextLine();
        Call<String> call = service.changeMesto(email, mesto);
        return call;
    }

    private static Call<String> createKategorija(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv: ");
        String naziv = scanner.nextLine();
        Call<String> call = service.createKategorija(naziv);
        return call;
    }

    private static Call<String> createAudio(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite email: ");
        String email = scanner.nextLine();
        System.out.println("Unesite datum u formatu 'dd-MM-yyyy hh:mm:ss': ");
        String datum = scanner.nextLine();
        datum = datum.replace(' ', '|');
        System.out.println("Unesite trajanje u sekundama: ");
        int trajanje = Integer.parseInt(scanner.nextLine());
        Call<String> call = service.createAudio(naziv, email, datum, trajanje);
        return call;
    }

    private static Call<String> promeniNaziv(Scanner scanner, ApiService service) {
        System.out.println("Unesite stari naziv: ");
        String stari = scanner.nextLine();
        System.out.println("Unesite email autora: ");
        String email = scanner.nextLine();
        System.out.println("Unesite novi naziv: ");
        String novi = scanner.nextLine();
        Call<String> call = service.promeniNaziv(stari, email, novi);
        return call;
    }

    private static Call<String> addKategorija(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv snimka: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite email autora: ");
        String email = scanner.nextLine();
        System.out.println("Unesite naziv kategorije: ");
        String kategorija = scanner.nextLine();
        Call<String> call = service.addKategorija(naziv, email, kategorija);
        return call;
    }

    private static Call<String> createPaket(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite cenu: ");
        int cena = Integer.parseInt(scanner.nextLine());
        Call<String> call = service.createPaket(naziv, cena);
        return call;
    }

    private static Call<String> updateCenaPaket(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite cenu: ");
        int cena = Integer.parseInt(scanner.nextLine());
        Call<String> call = service.updateCenaPaket(naziv, cena);
        return call;
    }

    private static Call<String> createPretplata(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite cenu: ");
        int cena = Integer.parseInt(scanner.nextLine());
        System.out.println("Unesite email korisnika: ");
        String email = scanner.nextLine();
        System.out.println("Unesite datum u formatu 'dd-MM-yyyy hh:m:ss': ");
        String datum = scanner.nextLine();
        datum = datum.replace(' ', '|');
        Call<String> call = service.createPretplata(naziv, email, cena, datum);
        return call;
    }

    private static Call<String> createSlusanje(Scanner scanner, ApiService service) {
        System.out.println("Unesite email slusaoca: ");
        String slusalac = scanner.nextLine();
        System.out.println("Unesite naziv audio snimka: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite email autora: ");
        String autor = scanner.nextLine();
        System.out.println("Unesite vreme slusanja audio snimka u sekundama: ");
        int vremeSlusanja = Integer.parseInt(scanner.nextLine());
        System.out.println("Unesite pocetnu sekundu reprodukcije: ");
        int pocetnaSekunda = Integer.parseInt(scanner.nextLine());
        Call<String> call = service.createSlusanje(slusalac, naziv, autor, vremeSlusanja, pocetnaSekunda);
        return call;
    }

    private static Call<String> createOmiljeni(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv audio snimka: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite email autora: ");
        String emailAutor = scanner.nextLine();
        System.out.println("Unesite email slusaoca: ");
        String emailSlusalac = scanner.nextLine();
        Call<String> call = service.createOmiljeni(naziv, emailAutor, emailSlusalac);
        return call;
    }

    private static Call<String> createOcena(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv audio snimka: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite email autora: ");
        String emailAutor = scanner.nextLine();
        System.out.println("Unesite email slusaoca: ");
        String emailSlusalac = scanner.nextLine();
        System.out.println("Unesite ocenu: ");
        int ocena = Integer.parseInt(scanner.nextLine());
        Call<String> call = service.createOcena(naziv, emailAutor, emailSlusalac, ocena);
        return call;
    }

    private static Call<String> updateOcena(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv audio snimka: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite email autora: ");
        String emailAutor = scanner.nextLine();
        System.out.println("Unesite email slusaoca: ");
        String emailSlusalac = scanner.nextLine();
        System.out.println("Unesite ocenu: ");
        int ocena = Integer.parseInt(scanner.nextLine());
        Call<String> call = service.updateOcena(naziv, emailAutor, emailSlusalac, ocena);
        return call;
    }

    private static Call<String> deleteOcena(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv audio snimka: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite email autora: ");
        String emailAutor = scanner.nextLine();
        System.out.println("Unesite email slusaoca: ");
        String emailSlusalac = scanner.nextLine();
        Call<String> call = service.deleteOcena(naziv, emailAutor, emailSlusalac);
        return call;
    }

    private static Call<String> deleteAudioSnimak(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite email autora: ");
        String email = scanner.nextLine();
        Call<String> call = service.deleteAudioSnimak(naziv, email, currentUser);
        return call;
    }

    private static Call<String> getMesta(Scanner scanner, ApiService service) {
        Call<String> call = service.getMesta();
        return call;
    }

    private static Call<String> getKorisnici(Scanner scanner, ApiService service) {
        Call<String> call = service.getKorisnici();
        return call;
    }

    private static Call<String> getKategorije(Scanner scanner, ApiService service) {
        Call<String> call = service.getKategorije();
        return call;
    }

    private static Call<String> getAudio(Scanner scanner, ApiService service) {
        Call<String> call = service.getSnimci();
        return call;
    }

    private static Call<String> getKategorijeSnimak(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv audio snimka: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite email autora: ");
        String email = scanner.nextLine();
        Call<String> call = service.getKategorijeSnimak(naziv, email);
        return call;
    }

    private static Call<String> getPaketi(Scanner scanner, ApiService service) {
        Call<String> call = service.getPaketi();
        return call;
    }

    private static Call<String> getPretplate(Scanner scanner, ApiService service) {
        System.out.println("Unesite email korisnika: ");
        String email = scanner.nextLine();
        Call<String> call = service.getPretplate(email);
        return call;
    }

    private static Call<String> getSlusanja(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv audio snimka: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite email autora: ");
        String email = scanner.nextLine();
        Call<String> call = service.getSlusanja(naziv, email);
        return call;
    }

    private static Call<String> getOcene(Scanner scanner, ApiService service) {
        System.out.println("Unesite naziv audio snimka: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite email autora: ");
        String email = scanner.nextLine();
        Call<String> call = service.getOcene(naziv, email);
        return call;
    }

    private static Call<String> getOmiljeni(Scanner scanner, ApiService service) {
        System.out.println("Unesite email korisnika: ");
        String email = scanner.nextLine();
        Call<String> call = service.getOmiljeni(email);
        return call;
    }

    private static String[] funkcije = {"Kraj programa", "Kreiranje grada", "Kreiranje korisnika", "Promena email adrese za korisnika",
        "Promena mesta za korisnika", "Kreiranje kategorije", "Kreiranje audio snimka", "Promena naziva audio snimka",
        "Dodavanje kategorije audio snimku", "Kreiranje paketa", "Promena mesecne cene za paket", "Kreiranje pretplate korisnika na paket",
        "Kreiranje slušanja audio snimka od strane korisnika", "Dodavanje audio snimka u omiljene od strane korisnika",
        "Kreiranje ocene korisnika za audio snimak", "Menjanje ocene korisnika za audio snimak", "Brisanje ocene korisnika za audio snimak",
        "Brisanje audio snimka od strane korisnika koji ga je kreirao", "Dohvatanje svih mesta", "Dohvatanje svih korisnika",
        "Dohvatanje svih kategorija", "Dohvatanje svih audio snimaka", "Dohvatanje kategorija za određeni audio snimak",
        "Dohvatanje svih paketa", "Dohvatanje svih pretplata za korisnika", "Dohvatanje svih slušanja za audio snimak",
        "Dohvatanje svih ocena za audio snimak", "Dohvatanje liste omiljenih audio snimaka za korisnika"
    };
    

}
