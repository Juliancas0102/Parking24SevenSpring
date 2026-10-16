package com.parking24seven.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Utilidad de seguridad para almacenar y verificar
 * contraseñas mediante PBKDF2.
 *
 * Las contraseñas nunca se almacenan en texto plano.
 */
public final class PasswordUtil {

    private static final SecureRandom RANDOM =
            new SecureRandom();

    private static final int ITERATIONS = 210_000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_BYTES = 16;

    private PasswordUtil() {
    }

    /**
     * Genera un hash seguro PBKDF2 para almacenar
     * una contraseña.
     */
    public static String hash(String password) {

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "La contraseña es obligatoria."
            );
        }

        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);

        byte[] derived = pbkdf2(
                password.toCharArray(),
                salt,
                ITERATIONS,
                KEY_LENGTH
        );

        return "pbkdf2$"
                + ITERATIONS
                + "$"
                + Base64.getEncoder()
                        .encodeToString(salt)
                + "$"
                + Base64.getEncoder()
                        .encodeToString(derived);
    }

    /**
     * Comprueba si una contraseña ingresada
     * corresponde al hash almacenado.
     */
    public static boolean matches(
            String password,
            String storedHash) {

        if (password == null
                || password.isBlank()
                || storedHash == null
                || storedHash.isBlank()) {

            return false;
        }

        try {

            String[] partes =
                    storedHash.split("\\$", 4);

            if (partes.length != 4) {
                return false;
            }

            if (!"pbkdf2".equals(partes[0])) {
                return false;
            }

            int iterations =
                    Integer.parseInt(partes[1]);

            byte[] salt =
                    Base64.getDecoder()
                            .decode(partes[2]);

            byte[] hashEsperado =
                    Base64.getDecoder()
                            .decode(partes[3]);

            byte[] hashCalculado =
                    pbkdf2(
                            password.toCharArray(),
                            salt,
                            iterations,
                            hashEsperado.length * 8
                    );

            return MessageDigest.isEqual(
                    hashEsperado,
                    hashCalculado
            );

        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Genera la clave derivada mediante
     * PBKDF2 con HMAC-SHA256.
     */
    private static byte[] pbkdf2(
            char[] password,
            byte[] salt,
            int iterations,
            int keyLength) {

        PBEKeySpec spec =
                new PBEKeySpec(
                        password,
                        salt,
                        iterations,
                        keyLength
                );

        try {

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256"
                    );

            return factory
                    .generateSecret(spec)
                    .getEncoded();

        } catch (GeneralSecurityException ex) {

            throw new IllegalStateException(
                    "No fue posible procesar la contraseña.",
                    ex
            );

        } finally {
            spec.clearPassword();
        }
    }
}