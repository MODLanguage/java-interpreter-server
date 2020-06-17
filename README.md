# Java Interpreter Server
Exposes the Java Interpreter as an HTTP GET/POST endpoint inside a Spring Boot app.

# Example invocations using curl

```text
curl --location --request POST 'http://localhost:8080/mtoj' \
    --header 'Content-Type: text/plain' \
    --data-raw 'a=b'
```
```text
http://localhost:8080/mtoj?modl=a=b
```
