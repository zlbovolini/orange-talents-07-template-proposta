package com.github.zlbovolini.proposta.comum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Dado {

    /**
     * 128 bits
     */
    // !TODO @Value("${proposta.crypto.secret-key}")
    private String secretKey = "770A8A65DA156D24EE2A093277530142";

    /**
     * 64 bits
     */
    // !TODO @Value("${proposta.crypto.iv}")
    private String initializationVector = "0123456789123456";

    private String hashedText;
    private String encryptedText;

    private static final MessageDigest DIGEST;
    private static final String CRYPTO = "AES";
    private static final String CIPHER = "AES/CBC/PKCS5PADDING";

    private static final Logger LOGGER = LoggerFactory.getLogger(Dado.class);

    static {
        try {
            DIGEST = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn("SHA 256 não suportado");
            throw new RuntimeException(e);
        }
    }

    private Dado() {
        check();
    }

    private Dado(String plainText) {
        check();

        byte[] hash = DIGEST.digest(plainText.getBytes(StandardCharsets.UTF_8));
        this.hashedText = Base64.getEncoder().encodeToString(hash);

        try {
            byte[] ivData = initializationVector.getBytes(StandardCharsets.UTF_8);
            byte[] keyData = secretKey.getBytes(StandardCharsets.UTF_8);

            IvParameterSpec iv = new IvParameterSpec(ivData);
            SecretKeySpec key = new SecretKeySpec(keyData, CRYPTO);

            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            this.encryptedText = Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn("Algoritmo nome={} não suportado causa={}", CRYPTO, e.getMessage());
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            LOGGER.warn("Padding nome={} não suportado causa={}", CIPHER, e.getMessage());
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            LOGGER.warn("Chave não suportada causa={}", e.getMessage());
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            LOGGER.warn("Erro ao criptografar dado causa={}", e.getMessage());
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            LOGGER.warn("Parâmetro inválido causa={}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Dado encode(String dadoOriginal) {
        return new Dado(dadoOriginal);
    }

    public static String decrypt(String dadoCriptografado) {
        byte[] encryptedData = Base64.getDecoder().decode(dadoCriptografado);
        return new Dado().decrypt(encryptedData);
    }

    public String hashed() {
        return hashedText;
    }

    public String encrypted() {
        return encryptedText;
    }

    private String decrypt(byte[] encryptedData) {
        try {
            byte[] ivData = initializationVector.getBytes(StandardCharsets.UTF_8);
            byte[] keyData = secretKey.getBytes(StandardCharsets.UTF_8);

            IvParameterSpec iv = new IvParameterSpec(ivData);
            SecretKeySpec key = new SecretKeySpec(keyData, CRYPTO);

            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            byte[] original = cipher.doFinal(encryptedData);

            return new String(original);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn("Algoritmo nome={} não suportado causa={}", CRYPTO, e.getMessage());
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            LOGGER.warn("Padding nome={} não suportado causa={}", CIPHER, e.getMessage());
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            LOGGER.warn("Chave não suportada causa={}", e.getMessage());
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            LOGGER.warn("Erro ao descriptografar dado causa={}", e.getMessage());
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            LOGGER.warn("Parâmetro inválido causa={}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void check() {
        Assert.notNull(secretKey, "Chave de criptografia não definida");
        Assert.isTrue(!secretKey.isBlank(), "Chave de criptografia vazia");
        Assert.notNull(initializationVector, "Vetor de inicialização não definido");
        Assert.isTrue(!initializationVector.isBlank(), "Vetor de inicialização vazio");
    }
}
