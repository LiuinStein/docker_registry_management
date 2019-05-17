package cn.shaoqunliu.c.hub.mgr.security;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class JwtsUtils {

    private static RSAPrivateKey privateKey;

    public static void init() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        // get public & private key
        String pkcs8Path = System.getenv("PKCS8_PATH");
        // get Https SSL RSA private key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(getByteArrayFromFile(pkcs8Path));
        privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static JwtParser getParser() {
        return Jwts.parser()
                .setSigningKey(privateKey)
                .requireIssuer("A certain powerful developer surnamed Liu")
                .requireAudience("A docker registry developed by a sane developer - Shaoqun Liu");
    }

    public static Claims getClaimsFromToken(String token) {
        try {
            Jws<Claims> parser = getParser().parseClaimsJws(token);
            return parser.getBody();
        } catch (JwtException e) {
            // the parseClaimsJws method will check if the Jwts valid automatically,
            // and throw some exceptions subclassed JwtException when invalid Jwts was given
            // such as SignatureException and ExpiredJwtException
            throw new BadCredentialsException(e.getMessage());
        }
    }

    private static byte[] getByteArrayFromFile(String path) throws IOException {
        File file = new File(path);
        FileInputStream in = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        in.read(bytes);
        return bytes;
    }
}