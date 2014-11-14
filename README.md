cryptoid
========
This is a simple spring boot application that implements a RESTful encryption/decryption service.

I built it to get to know some of the latest features of Spring and to get to know AES/GCM with AAD 
encryption a little better.

Some of the neat things shown here:
* How to setup a basic spring-boot pom file.
* How to integrate BouncyCastle as a JCE provider in a spring boot application.
* How to implement a REST service that consumes and produces JSON from the request/response body.
* How to initialize and use an AES/GCM/NoPadding Cipher.
* How to manage multiple data encryption keys (naively).

NOTE: I am not pretending that this is prod-ready code. Don't use it. Just look at it, then build your real stuff :)
