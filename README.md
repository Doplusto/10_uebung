
_Übungsaufgabe zur Veranstaltung [IT
Systeme](https://hsro-wif-it.github.io) im [Bachelorstudiengang
Wirtschaftsinformatik](https://www.th-rosenheim.de/technik/informatik-mathematik/wirtschaftsinformatik-bachelor/) an der [Hochschule Rosenheim](http://www.th-rosenheim.de)._

# 10 - HTTP und REST

Diese Übung behandelt speziell das Thema _Web Anwendungen_ und _REST_. Dabie geht es darum mit den verschiedenen Technologien um HTTP, REST, JSON etc. vertraut zu werden.

> Note: **Die Lösung befindet sich im Branch _Musterlösung_.**

## Aufgabe 1: Starten sie den Server lokal (auf ihrem Rechner)  

Wir beschäftigen uns mal wieder mit dem Thema **Chat**. Nachdem wir uns nun wirklich ausgiebig mit Sockets beschäftigt haben, 
versuchen wir es diesmal auf einem robusteren Weg.

In dem `src` Package befindet sich eine einfache RESTful Server Implementierung. 
Diese verwendet [`Spark`](http://sparkjava.com/), ein kleines Framework, um Web Applikationen mit möglichst wenig Aufwand zu entwickeln.

Der Server bietet 3 Endpunkte:
1. `GET /hello` : Dient zum einfachen Testen, ob der Server auch wirklich läuft. Ein Aufruf an diesen Endpunkt hat das Format: `GET http://localhost:8080/hello`
1. `GET /messages/:name` : Zum Abrufen von Nachrichten für einen bestimmten Benutzer (`:name`). Dabei ist `:` nur ein Marker. Ein Aufruf an diesen Endpunkt hat das Format: `GET http://localhost:8080/messages/mustermann`
1. `POST /messages/:toName` : Zum Verschicken einer Nachricht an den Benutzer (`:toName`). Dabei ist `:` nur ein Marker. Ein Aufruf an diesen Endpunkt hat das Format: `POST http://localhost:8080/messages/mustermann` +HTTP Body: `"{\"toUser\":\"test\", \"fromUser\":\"Marcel\",\"Bmessage\":\"Hallo\"}"`

Der Code für den Server befindet sich in [./src/main/java/de/wif/rest](./src/main/java/de/wif/rest).

a)

Machen Sie folgendes:
1. Clonen Sie das Projekt direkt in IntelliJ.
1. Kompilieren und starten Sie den `Server`.
    -  Evtl. muüssen Sie in der Klasse `Server.java` den Port setzen!
1. Schauen Sie sich den Code an und versuchen zu verstehen, was in den jeweiligen Pfaden in der `init()` Methode passiert
1. Testen Sie, ob der Server läuft, in dem Sie in ihrem Browser den entprechenden `/hello` Pfad aufrufen! 

b)

Testen Sie die Endpunkte, in dem Sie entsprechende `Curl`-Aufrufe definieren. Benutzen Sie entweder die `Git Bash` (Windows) oder `Terminal` (Mac/Linux).

1. Schreiben Sie den `Curl`-Aufruf fur `GET /hello`
1. Schreiben Sie den `Curl`-Aufruf fur `GET /messages/:name`
1. Schreiben Sie den `Curl`-Aufruf fur `POST /messages/:toName`. Bedenken Sie, dass Sie hier Daten im Request-Body im JSON Forat uebergeben muessen!
1. Schreiben Sie ein Skript (test.sh), dass die `Curl`-Aufrufe von 2. und 3. kombiniert. Welche Szenarien gibt es? 

**Lösung siehe: ./src/main/shell/test.sh**

## Aufgabe 2: REST Client  

Schreiben Sie einen `Client.java`, der die REST Aufrufe ausführt. Verwenden Sie hierzu die `Client` Klasse und ersetzen die `\\ todo` mit enstprechenden Code.

1. Schreiben Sie die Methode `sendHello`
1. Schreiben Sie die Methode `sendMessage`
1. Schreiben Sie die Methode `getMessages`

Halten Sie sich jeweils an die gegebenen Methodensignaturen und verwenden Sie die Hilfsmethoden wo möglich.



## Aufgabe 3: Benachrichtigungen via Messages

Es gibt nach wie vor die unschöne Situation, daß der Client stets (periodisch?) beim Server nachfragen muss, ob neue Nachrichten vorliegen. 
Es wäre schöner, wenn er Server den Client benachrichtigen würde, sobald neue Nachrichten vorliegen. Der Client daraufhin mit der Methode `getMessages` die neusten Nachrichten abholen würde (Reuse!).

In der Vorlesung haben wir von **message-orientierten (MO) Systemen** gelernt. Das können wir hier gut verwenden!

**Funktionalität:**

Sobald der Serer eine neue Nachricht erhält, schickt er an die MO Middleware eine Nachricht (**Publisher**). 
Der Client als **Subscriber** bekommt die Nachricht und triggert den Request zum Abholen der Nachricht.

In diesem Beispiel verwenden wir **MQTT**. Ein entsprechender MQTT Broker läuft in der Cloud und kann in diesem Beispiel verwendet werden.


- Der `Server` hat entsprechenden Publisher-Code bereits. Er muss nur mit dem entsprechenden Flag gestartet werden.
- Im `Client` befinden sich entsprechende Hilfmethoden.

Aufgaben:
1. Ändern Sie den `Server` Code, so daß der Server mit der Benachrichtung startet
1. Ergänzen Se den `Client` Code um die Subscriber Funktionalität
1. Testen, testen, testen ...

**Was passiert, wenn Sie beides starten?**

Leider gibt es Konflikte, wenn der MQTT Client sowohl im Server, als auch im Client verwendet wird. Ändern Sie die Addresse des Servers,
so daß Sie nur den Client starten müssen. Die Server-Adresse bekommen Sie in der Übung! 

    