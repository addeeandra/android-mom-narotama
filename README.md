### Violation Note Taking App for (my) Faculty Student Organization

A simple written apps with no specific architecture.

It may help anyone who get started to code an Android Apps in a simple manner ways.

Wish u find it helpful.

Features :
- Showing categories and choose one,
- Showing violations by the choosen category,
- Choose manual submission or QRCode submission with Dialog,
- Showing QR Scan page (see QR Code encryption detail below) and scan a QR,
- Showing submission page (after scan, or after choose a manual submission) and submit,
- Do a custom submission


QRCode Encryption Steps :

```kotlin
String nim = ...
String name = ...
String group = ...

String texts = "$nim;$name;$group"
String encrypted = Base64.encode(texts)

... GenerateQRCode(encrypted)
```

QRCode Decryption Steps :
```kotlin
String rawScannedString = ... // encrypted base64
String scannedString = Base64.decode(rawScannedString)
String data = scannedString.splits(';')

String nim = data[0]
String name = data[1]
String group = data[2]

... use it wisely
```
