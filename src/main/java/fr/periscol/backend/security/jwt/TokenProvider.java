package fr.periscol.backend.security.jwt;

import fr.periscol.backend.domain.Permission;
import fr.periscol.backend.domain.Role;
import fr.periscol.backend.service.UserService;
import fr.periscol.backend.service.dto.PermissionDTO;
import fr.periscol.backend.service.dto.RoleDTO;
import fr.periscol.backend.web.rest.errors.UserNotExistsException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final Key key;

    private final JwtParser jwtParser;

    private final long tokenValidityInMilliseconds;

    private final long tokenValidityInMillisecondsForRememberMe;

    private final UserService service;

    public TokenProvider(@Value("${security.authentication.jwt.base64-secret}") String secret,
                         @Value("${security.authentication.jwt.token-validity-in-seconds}") String tokenValidityInMilliseconds,
                         @Value("${security.authentication.jwt.token-validity-in-seconds-for-remember-me}") String tokenValidityInMillisecondsForRememberMe,
                         @Lazy UserService service) {
        this.service = service;
        byte[] keyBytes;
        if (!ObjectUtils.isEmpty(secret)) {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(secret);
        } else {
            log.warn(
                "Warning: the JWT key used is not Base64-encoded. " +
                "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security."
            );
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenValidityInMilliseconds = 1000L * Integer.parseInt(tokenValidityInMilliseconds);
        this.tokenValidityInMillisecondsForRememberMe =
                1000L * Integer.parseInt(tokenValidityInMillisecondsForRememberMe);
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = "";

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts
            .builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        final var userOpt = service.findOne(claims.getSubject());

        if(userOpt.isEmpty())
            throw new UserNotExistsException();

        Collection<SimpleGrantedAuthority> authorities = userOpt.get()
                .getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(PermissionDTO::getName)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toCollection(ArrayList::new));

        authorities.addAll(userOpt.get().getRoles().stream().map(RoleDTO::getName).map(SimpleGrantedAuthority::new).toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}
