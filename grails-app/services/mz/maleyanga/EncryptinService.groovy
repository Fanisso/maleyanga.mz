package mz.maleyanga

import mz.maleyanga.security.Utilizador

import java.nio.charset.StandardCharsets

/**
 * EncryptinService
 * A service class encapsulates the core business logic of a Grails application
 */
class EncryptinService {

    Utilizador localUser
    byte[] encryptionKey = "MZygpewJsCpRrfOr".getBytes(StandardCharsets.UTF_8)
    AdvancedEncryptionStandard aes = new AdvancedEncryptionStandard(
            encryptionKey)

    byte[] encrypt(String key) {
        byte[] plainText = key.getBytes(StandardCharsets.UTF_8)

        byte[] cipherText = aes.encrypt(plainText)
        return cipherText
    }

    String decrypt(byte[] chiperTxt) {
        byte[] decryptedCipherText = aes.decrypt(chiperTxt)
        return (new String(decryptedCipherText))
    }

    boolean checkPassword(String pass, Utilizador user) {
        String passDB = decrypt(user.password)
        if (passDB.equals(pass)) {
            localUser = user
            return true
        }
        return false
    }

    boolean getPermission(String role) {

    }

}
