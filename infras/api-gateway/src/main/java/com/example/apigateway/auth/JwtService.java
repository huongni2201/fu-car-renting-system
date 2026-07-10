package com.example.apigateway.auth;

import com.example.apigateway.auth.dto.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JwtService {

  private static final String HMAC_SHA256 = "HmacSHA256";

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${app.jwt.secret}")
  private String secret;

  @Value("${app.jwt.expiration-seconds}")
  private long expirationSeconds;

  public String generateToken(AuthenticatedUser user) {
    long now = Instant.now().getEpochSecond();
    long expiration = now + expirationSeconds;

    Map<String, Object> header = new LinkedHashMap<>();
    header.put("alg", "HS256");
    header.put("typ", "JWT");

    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put("sub", user.userId() != null ? String.valueOf(user.userId()) : "0");
    payload.put("email", user.email());
    payload.put("name", user.name());
    payload.put("role", user.role());
    payload.put("iat", now);
    payload.put("exp", expiration);

    String encodedHeader = base64Url(toJson(header));
    String encodedPayload = base64Url(toJson(payload));
    String unsignedToken = encodedHeader + "." + encodedPayload;

    return unsignedToken + "." + sign(unsignedToken);
  }

  public AuthenticatedUser parseToken(String token) {
    String[] chunks = token.split("\\.");
    if (chunks.length != 3) {
      throw new IllegalArgumentException("Invalid JWT format");
    }

    String unsignedToken = chunks[0] + "." + chunks[1];
    if (!MessageDigest.isEqual(sign(unsignedToken).getBytes(StandardCharsets.UTF_8), chunks[2].getBytes(StandardCharsets.UTF_8))) {
      throw new IllegalArgumentException("Invalid JWT signature");
    }

    Map<?, ?> payload = fromJson(new String(Base64.getUrlDecoder().decode(chunks[1]), StandardCharsets.UTF_8));
    long expiration = ((Number) payload.get("exp")).longValue();
    if (expiration < Instant.now().getEpochSecond()) {
      throw new IllegalArgumentException("Expired JWT");
    }

    String subject = String.valueOf(payload.get("sub"));
    Long userId = "0".equals(subject) ? null : Long.valueOf(subject);

    return new AuthenticatedUser(
        userId,
        String.valueOf(payload.get("email")),
        String.valueOf(payload.get("name")),
        String.valueOf(payload.get("role"))
    );
  }

  public long getExpirationSeconds() {
    return expirationSeconds;
  }

  private byte[] toJson(Map<String, Object> value) {
    try {
      return objectMapper.writeValueAsBytes(value);
    } catch (Exception exception) {
      throw new IllegalStateException("Cannot create JWT JSON", exception);
    }
  }

  private Map<?, ?> fromJson(String value) {
    try {
      return objectMapper.readValue(value, Map.class);
    } catch (Exception exception) {
      throw new IllegalArgumentException("Invalid JWT payload", exception);
    }
  }

  private String sign(String value) {
    try {
      Mac mac = Mac.getInstance(HMAC_SHA256);
      mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256));
      return base64Url(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception exception) {
      throw new IllegalStateException("Cannot sign JWT", exception);
    }
  }

  private String base64Url(byte[] value) {
    return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
  }
}
