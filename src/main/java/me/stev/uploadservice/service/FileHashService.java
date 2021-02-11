package me.stev.uploadservice.service;

import me.stev.uploadservice.repository.FileHashRepository;
import me.stev.uploadservice.exception.HashesDoNotMatchException;
import me.stev.uploadservice.entity.FileHash;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.*;

@Component
public class FileHashService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${aes.key}")
    private String keyEncoded;

    @Value("${aes.iv}")
    private String ivEncoded;

    @Autowired
    private FileHashRepository repository;

    @Async
    public void hashFile(MultipartFile file) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        logger.info("Hashing file...");
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest sha256Digest = new SHA256.Digest();
        byte[] hash = DigestUtils.digest(sha256Digest, new BufferedInputStream(file.getInputStream()));
        String cipher = AESUtils.encrypt(hash, AESUtils.getKey(keyEncoded), AESUtils.getIV(ivEncoded));
        FileHash fileHash = new FileHash();
        fileHash.setHash(cipher);
        repository.save(fileHash);
        logger.info("Finished hashing file");
    }

    public void verifyFile(MultipartFile file, Integer id) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        logger.info("Verifying File...");
        Security.addProvider(new BouncyCastleProvider());
        FileHash savedCipher = repository.getOne(id);
        MessageDigest sha256Digest = new SHA256.Digest();
        String newHex = Hex.toHexString(DigestUtils.digest(sha256Digest, new BufferedInputStream(file.getInputStream())));
        String savedHash = AESUtils.decrypt(savedCipher.getHash(), AESUtils.getKey(keyEncoded), AESUtils.getIV(ivEncoded));
        if (!newHex.equals(savedHash))
            throw new HashesDoNotMatchException("Hashes do not match!!!");
        logger.info("Finished verifying file");
    }
}
