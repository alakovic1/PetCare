# PetCare

Ovaj projekat je realiziran na predmetu Napredne Web Tehnologije Elektrotehničkog fakulteta u Sarajevu, Master studij, 1. godina. 

Članovi tima (***Amile i ostali***) koji su učestvovali u realizaciji ovog projekta su:

1. Amila Laković (***alakovic1@etf.unsa.ba***)
2. Amila Hrustić (***ahrustic2@etf.unsa.ba***)
3. Samra Mujčinović (***smujcinovi1@etf.unsa.ba***)
4. Emir Pita (***epita1@etf.unsa.ba***)

## Opis projekta

Cilj ove stranice jeste mogućnost da svaka životinja ima priliku da bude udomljena. 

Naše osnovne 2 funkcionalnosti jesu dodavanje peta (odnosno slanje zahtjeva za dodavanje) u slučaju da nađete neku lutalicu, ili ne možete više izdražavati vlastitog ljubimca i udomljavanje ljubimca (odnosno slanje zahtjeva za udomljavanje). Pod slanjem zahtjeva za udomljavanje/dodavanje mislimo na čekanje potvrde. Naš projekat sadrži 2 vrste korisnika: admina i usera. Da bi poslali zahtjev morate biti registrovni user, dok admin ima pravo da sve zahtjeve koje pošaljete prihvati ili odbije. U slučaju da je zahtjev prihvaćen dolazi notifikacija useru o stanju zahtjeva (tj. da li je prihvaćen ili nije). Nakon prihvaćanja zahtjeva, ako je u pitanju dodavanje taj ljubimac se dodaje u listu petova koji su dostupni za udomljavanje, a u slučaju prihvaćanja zahtjeva za udomljavanje ljubimac se briše iz liste dostupnih ljubimaca jer je on u tom slučaju udomljen od strane usera čiji je zahtjev prihvaćen.

Pored ove osnovne dvije funkcionalnosti svi registrovani useri imaju pravo da dodaju komentare za nakog ljubimca, kao i za neku rasu, da bi mogli dati neke savjete kako da se ponašaju sa nekom rasom, ili mogu postaviti neka pitanja gdje je omogućeno i dodavanje odgovora svim vrstama korisnika. Svako može svoje vlastite komentare i odgovore uređivati i brisati, te su komentari vidljivi svima koji pristupe stranici.

Što se tiče svih ljubimaca, oni su vidljivi svima koji pristupe našoj stanici, ali za sve ostale funkcionalnosti potrebna je registracija. Ljubimci su poredani po kategorijama, te rasama, ali može se pristupiti i svim ljubimcima na "Find a pet". 

Pored samog pregleda stranice, neregistrovani korisnik ima još pravo i popunjavanja Contact us forme, u slučaju da postoji neko pitanje gdje se pri slanju šalje e-mail administraciji koja kasnije može odgovoriti na userov e-mail naveden u Constact us formi.

Za korisnike sa računom (i admini i useri), pored slanja zahtjeva moguće je i urediti svoj profil, promijeniti i oporaviti šifru (uz Security pitanje), te ga obrisati.

Što se tiče admina, oni imaju neke dodatne privilegije u odnosu na usere iz razloga što oni upravljaju čitavom stranicom. Pored osnovnih funkcionalnosti prihvatanja i odbijanja zahtjeva oni imaju pravo dodavanja i brisanja neke kategorije, rase ili ljubimca, te imaju pristup svim informacijama o kreiranom zahtjevu, kao i mogućnost brisanja istih. Pored toga oni su obaviješteni o svim dešavanjima, te dobivaju notifikacije kada se neko registruje, popuni Contact us formu, te kreira zahtjev.

## Pokretanje pomoću Dockera

Nakon što se clone-a repozitorij, potrebno je buildati sve projekte. To se može uraditi sa naredbom kroz konzolu (nalazite se u repozitoriju sa svim projektima):

```bash
./mvnw package -Dmaven.test.skip=true
```
pod uslovom da Vam je defaultna verzija Jave na računaru minimalno 11. 

U slučaju da neki build ne prođe, potrebno je ručno buildati projekat koji je pao, ili pokušati sa

```bash
mvn clean
mvn install
```
Nakon uspješnog buildanja, svaki projekat bi trebalo da sadrži target folder sa .jar fileom tog projekta.

Za dalje pokretanje potrebno je imati Docker instaliran na računaru, te opet kroz konzolu (nalazite se u repozitoriju sa svim projektima i docker-compose.yml fileom) pokrenuti **jednu od** sljedeće 2 naredbe:

```bash
docker-compose up -d
docker-compose up
```
Prilikom našeg testiranja pokretana je prva naredba, te se prati stanje u Docker Desktop-u.

Kreiranje containera traje malo duže, obzirom da se ispituje healthcheck za neke projekte, te se to stanje može pratiti u konzoli. Nakon što se uspješno kreiraju svi containeri potrebno je sačekati da se svi servisi pokrenu, te da se pokrene frontend container (on bi trebao zadnji da se pokrene), i nakon toga se može pristupiti stranici pomoću (potrebno je samo prije pristupanja obrisati sve spašene Cookies koji su vezani za localhost): 

[http://localhost:3000](http://localhost:3000)

***NAPOMENA:*** Da bi brže radilo, mi smo na našim mašinama konfigurisali da docker koristi maksimalno 4 CPUs i maksimalno 5GB Memory. Međutim, trebalo bi da radi ispravno i sa manjim vrijednostima, samo duže da traje, ali u tom slučaju Eureka zna bacati grešku zbog zauzeća CPUa gdje smo mi povećali vrijednost u eureka_serveru, te više ne izbacuje, ali ako se to desi, ili ako dođe do još nekih problema, molimo da nekog od članova tima kontaktirate. Ova greška ovisi od jačine CPUa Vašeg računara. Nadajmo se da do toga neće doći.

## Login informacije za testiranje

Pošto stranica sadrži admin usera, kreiran je admin user za Vaše testiranje i može mu se pristupti pomoću sljedećih informacija:

- Name: **Irfan**
- Surname: **Prazina**
- Username: **iprazina1**
- Email: **iprazina1@etf.unsa.ba**
- Password: **NWTpassword123!**
- Odgovor na sigurnosno pitanje: **Sarajevo**

Login možete obaviti pomoću usernamea ili emaila, uz password.

Ako želite da se ulogujete kao user, možete izvršiti novu registraciju, ili koristiti sljedeće podatke:

- Name: **Name**
- Surname: **Surname**
- Username: **username**
- Email: **email@etf.unsa.ba**
- Password: **string123!A**
- Odgovor na sigurnosno pitanje: **Ella**

Pored toga, napravljen je i gmail račun, s kojim testirate popunjavanje Contact us forme, njegove informacije su:

- Email: **nwt.pet.care.adm2021@gmail.com**
- Password: (**ask the owner!**)

U slučaju da želite pristupiti RabbitMQ na [http://localhost:15672](http://localhost:15672) koristite defaultne podatke:

- Username: **guest**
- Password: **guest**

Link config repozitorija (varijable za servise i config_server):

https://github.com/alakovic1/config

## Videi

Mi smo kreirali 3 posebna videa (za frontend, za backend i za testove). Sva 3 videa se nalaze na sljedećem linku:

https://drive.google.com/drive/folders/1UR9v5H9BeKcDkDKx-l_HoXj6ajY95llc?usp=sharing

Pored 3 osnovna videa, kreiran je i video koji objašnjava docker compose i njegovo pokretanje, tj. sve ono što je navedeno u paragrafu iznad.

Posebni linkovi:

1. Frontend: https://drive.google.com/file/d/1_PlPVKHeWQWtlFajqyiS404rg2zd13Dp/view?usp=sharing
2. Backend: https://drive.google.com/file/d/1t3oEOzEy-t3qBQwg14BTyd_Hw435-_UG/view?usp=sharing
3. Testovi: https://drive.google.com/file/d/1IjT05Ywf2QPgOtfdBXJP3vZRLkbQnoPb/view?usp=sharing

Pošto su videi malo duži, nismo uspjeli da ih skratimo, a da sve uzmemo u obzir što smo uradili, predlažemo da videe gledate na Playback speed 1.75 ili 2. Samim tim će videi i kraće trajati.

## Učestvovanje u projektu

Za svo uređivanje koda, slobodno kreirajte pull request. Za veće promjene otvorite issue da bi prodiskutovali o promjenama.

Molimo da prilikom bilo kakvog uređivanja da uredite i testove.

## Članovi

>Tim Amile i ostali

<a href="https://github.com/alakovic1" target="_blank"><img width="100px" height="100px" src="https://github.com/alakovic1.png"></a>
<a href="https://github.com/ahrustic" target="_blank"><img width="100px" height="100px" src="https://github.com/ahrustic.png"></a>
<a href="https://github.com/SamraMujcinovic" target="_blank"><img width="100px" height="100px" src="https://github.com/SamraMujcinovic.png"></a>
<a href="https://github.com/emirpita" target="_blank"><img width="100px" height="100px" src="https://github.com/emirpita.png"></a>
