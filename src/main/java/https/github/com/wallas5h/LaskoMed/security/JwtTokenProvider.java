package https.github.com.wallas5h.LaskoMed.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

  @Value("${app.jwt-secret}")
  private String jwtSecret;

  @Value("${app.jwt-expiration-milliseconds}")
  private long jwtExpirationDate;

  public String getUsername(String token){
    // get username from JWT token
    return extractClaim(token, Claims::getSubject);
  }

  private <T> T extractClaim (String token, Function<Claims, T> claimDecoder){
    Claims claims = extractAllClaims(token);
    return claimDecoder.apply(claims);
  }

  private Claims extractAllClaims(String token){
    return Jwts.parser()
        .verifyWith((SecretKey) key())
        .build()
        .parseSignedClaims(token)
        .getPayload();

  }

  public String generateToken(UserDetails userDetails){
    return generateToken(userDetails, new HashMap<>());
  }

  private String generateToken(UserDetails userDetails, Map<String, Object> extractAllClaims){

    Date currentDate = new Date(System.currentTimeMillis());
    Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

    return Jwts.builder()
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(expireDate)
        .signWith(key())
        .compact();
  }

  private Key key(){
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  // validate JWT token
  public boolean validateToken(String token, UserDetails userDetails){
    String username = getUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpire(token));
  }

  private boolean isTokenExpire(String token) {
    return extractExpirationTime(token).before(new Date());
  }

  private Date extractExpirationTime(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
