# ksionszka-api
REST API

Książka
fizyczne id
wydanie

Wydanie
isbn
wydawnictwo
data
autor
język
gatunek

Użytkownik
email
hasło
rola

Wypożyczenie
data wypożyczenia
faktyczna data zwrotu
data zwrotu
użytkownik
książka

Rezerwacja
data utworzenia rezerwacji
czas rezerwacji [hardcoded]
użytkownik
książka


Wymagania:

MailDev (do testowania generowanych maili)
```
docker run -p 1025:1025 -p 1080:1080 --name maildev maildev/maildev
```

