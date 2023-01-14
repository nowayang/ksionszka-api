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

#### Testowanie websocketu (informacja o ostatio zarezerwowanych książkach):
tworzymy taki plik i odpalamy w przeglądarce
```html
<html>
<head>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <!-- download stomp from https://raw.githubusercontent.com/stomp-js/stomp-websocket/master/lib/stomp.js -->
    <script src="./stomp.js"></script>
    <script type="text/javascript">
        var url = "ws://localhost:8080/ws";
        var client = Stomp.client(url);

        client.connect({}, function(frame) {
            client.subscribe("/topic/recent-reservations", function(message) {
                document.body.innerHTML = message;
            });
        })
    </script>
</head>
</html>
```

#### Dokumentacja
```
http://localhost:8080/swagger-ui/index.html
```


#### TODOS

##### Notyfikacje na maila o zbliżających sie terminach
