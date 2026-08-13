package com.daniyal.controller;

import com.daniyal.dto.*;
import com.daniyal.service.CryptoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {
    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostMapping("/key")
    public KeyResponse generateKey() throws Exception {
        return cryptoService.generateKey();
    }

    @PostMapping("/enctypt")
    public EncryptResponse encrypt(@RequestBody EncryptRequest request) throws Exception {
        return cryptoService.encrypt(request);
    }

    @PostMapping("/decrypt")
    public DecryptResponse decrypt(@RequestBody DecryptRequest request) throws Exception {
        return cryptoService.decrypt(request);
    }
}
