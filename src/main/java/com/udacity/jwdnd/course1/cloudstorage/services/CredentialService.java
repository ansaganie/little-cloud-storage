package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final EncryptionService encryptionService;
    private final CredentialMapper credentialMapper;

    public CredentialService(EncryptionService encryptionService, CredentialMapper credentialMapper) {
        this.encryptionService = encryptionService;
        this.credentialMapper = credentialMapper;
    }


    public int save(Credential credential) {
        return credentialMapper.insert(encryptCred(credential));
    }

    public List<Credential> getAllCredentialsByUserId(Integer userId) {
        List<Credential> credentials = credentialMapper.getCredentialsByUserId(userId);
        List<Credential> result = new ArrayList<>();
        for (Credential credential: credentials) {
            result.add(decryptCred(credential));
        }
        return result;
    }

    public int update(Credential credential, Integer userId) {
        Credential existing = credentialMapper.getCredentialById(credential.getCredentialId());
        if (existing.getUserId().equals(userId)) {
            return credentialMapper.updateCredential(encryptCred(credential));
        } else {
            return 0;
        }
    }

    public int deleteById(Integer id, Integer userId) {
        return credentialMapper.deleteById(id, userId);
    }

    private Credential encryptCred(Credential credential) {
        String key = credential.getKey();
        if (key == null) {
            SecureRandom sr = new SecureRandom();
            byte[] keyBytes = new byte[16];
            sr.nextBytes(keyBytes);
            key = Base64.getEncoder().encodeToString(keyBytes);
        }
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setKey(key);
        credential.setPassword(encryptedPassword);
        return credential;
    }

    private Credential decryptCred(Credential credential) {
        String key = credential.getKey();
        String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), key);
        credential.setDecryptedPassword(decryptedPassword);
        return credential;
    }

    public boolean exists(Credential credential, Integer userId) {
        Credential existing = credentialMapper.getCredential(
                credential.getUrl(),
                credential.getUsername(),
                userId
        );
        if (existing == null) return false;
        else {
            String key = existing.getKey();
            String decryptedPassword = encryptionService.decryptValue(existing.getPassword(), key);
            return decryptedPassword.equals(credential.getPassword());
        }
    }
}
