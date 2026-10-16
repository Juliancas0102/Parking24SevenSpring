package com.parking24seven.util;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Utilidad de seguridad para almacenar contraseñas mediante PBKDF2.
 * No se guardan contraseñas en texto plano.
 */
public final class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int ITERATIONS = 210_000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_BYTES = 16;

    private PasswordUtil() {
    }

    public static String hash(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] derived = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);

        return "pbkdf2$" + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(derived);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLength) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("No fue posible generar el hash de contraseña.", ex);
        } finally {
            spec.clearPassword();
        }
    }
}
