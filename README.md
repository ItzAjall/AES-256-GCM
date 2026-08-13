# AES-256-GCM

A simple and secure AES-256-GCM encryption/decryption REST API built with Java 21 and Spring Boot.

## Features

* AES-256 encryption
* AES-GCM authenticated encryption
* 256-bit randomly generated AES keys
* Random 12-byte IV for every encryption
* 128-bit GCM authentication tag
* Base64 encoded keys and encrypted data
* RESTful API
* Invalid encryption/key error handling
* Java 21
* Spring Boot 4

## Cryptography

This project uses:

```text
Algorithm: AES/GCM/NoPadding
Key size: 256 bits
IV size: 12 bytes
Authentication tag: 128 bits
Encoding: Base64
```

AES-GCM provides both confidentiality and integrity. The authentication tag allows the application to detect modified or corrupted encrypted data.

A new random IV is generated for every encryption operation.

The encrypted output is structured as:

```text
Base64(
    IV || Ciphertext || Authentication Tag
)
```

The IV is not secret and is stored together with the encrypted data.

## Requirements

* Java 21+
* Maven 3.9+ (optional, Maven Wrapper is included)

## Running the Application

Clone the repository:

```bash
git clone https://github.com/ItzAjall/AES-256-GCM.git
cd AES-256-GCM
```

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

The application starts on:

```text
http://localhost:8080
```

## API

Base URL:

```text
/api/crypto
```

### Generate AES-256 Key

```http
POST /api/crypto/key
```

Example:

```bash
curl.exe -X POST http://localhost:8080/api/crypto/key
```

Example response:

```json
{
  "key": "BASE64_ENCODED_AES_256_KEY"
}
```

The generated key represents 32 random bytes (256 bits).

Keep this key secret.

---

### Encrypt Text

```http
POST /api/crypto/encrypt
Content-Type: application/json
```

Request:

```json
{
  "text": "Hello World",
  "key": "BASE64_ENCODED_AES_256_KEY"
}
```

Example:

```powershell
curl.exe -X POST http://localhost:8080/api/crypto/encrypt `
  -H "Content-Type: application/json" `
  -d '{"text":"Hello World","key":"BASE64_ENCODED_AES_256_KEY"}'
```

Example response:

```json
{
  "encrypted": "BASE64_ENCODED_CIPHERTEXT"
}
```

The encrypted value contains the randomly generated IV followed by the ciphertext and GCM authentication tag.

Encrypting the same plaintext multiple times with the same key produces different ciphertext because a new IV is generated for every operation.

---

### Decrypt Text

```http
POST /api/crypto/decrypt
Content-Type: application/json
```

Request:

```json
{
  "encrypted": "BASE64_ENCODED_CIPHERTEXT",
  "key": "BASE64_ENCODED_AES_256_KEY"
}
```

Example:

```powershell
curl.exe -X POST http://localhost:8080/api/crypto/decrypt `
  -H "Content-Type: application/json" `
  -d '{"encrypted":"BASE64_ENCODED_CIPHERTEXT","key":"BASE64_ENCODED_AES_256_KEY"}'
```

Example response:

```json
{
  "text": "Hello World"
}
```

If the key is incorrect or the encrypted data has been modified, decryption fails.

## Key Format

Keys are represented as Base64 strings.

An AES-256 key must contain exactly:

```text
256 bits
= 32 bytes
```

For example:

```text
BASE64_ENCODED_AES_256_KEY
```

Do not use normal passwords directly as AES keys.

If a password-based encryption system is required, a password-based key derivation function such as Argon2id or PBKDF2 should be used to derive the AES key.

## Security Notes

This project uses AES-256-GCM with a randomly generated IV for every encryption operation.

However, this project is primarily intended for learning and demonstration purposes.

If deployed as a public service, additional security controls should be implemented, including:

* HTTPS/TLS
* Authentication and authorization
* Rate limiting
* Request validation
* Secure key management
* Secret management
* Logging without exposing keys or plaintext
* Appropriate HTTP error handling

### Important

The API currently receives the AES key from the client.

Therefore, the server has access to the key and plaintext during encryption/decryption.

If the goal is zero-knowledge or end-to-end encryption, encryption should instead be performed on the client side and only ciphertext should be sent to the server.

## Project Structure

```text
src/
├── main/
│   └── java/
│       └── com/
│           └── daniyal/
│               ├── controller/
│               │   └── CryptoController.java
│               ├── dto/
│               │   ├── DecryptRequest.java
│               │   ├── DecryptResponse.java
│               │   ├── EncryptRequest.java
│               │   ├── EncryptResponse.java
│               │   └── KeyResponse.java
│               ├── exception/
│               │   ├── GlobalExceptionHandler.java
│               │   └── InvalidEncryptionException.java
│               ├── service/
│               │   └── CryptoService.java
│               └── util/
│                   └── Aes256.java
└── test/
    └── java/
        └── com/
            └── daniyal/
                └── Aes256GcmApplicationTests.java
```

## Technology Stack

* Java 21
* Spring Boot 4
* Spring Web MVC
* Maven
* AES-256-GCM
* Java Cryptography Architecture (JCA)

## License

This project is provided for educational purposes.
