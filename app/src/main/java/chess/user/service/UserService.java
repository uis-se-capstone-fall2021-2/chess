package chess.user.service;

import java.net.URI;
import java.net.http.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;

import chess.user.model.*;

@Service
public class UserService {

  @Autowired
  private Users users;
  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuer;

  private final HttpClient http = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .build();
  private final ObjectMapper jsonParser = new ObjectMapper();

  public User getUserById(String userId) {
    return users.getUserById(userId);
  }

  public User getUserByDisplayName(String displayName) {
    return users.getUserByDisplayName(displayName);
  }

  public Auth0UserInfo fetchUserProfile(Jwt principal) throws Exception {
    HttpRequest req = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(String.format("%suserinfo", issuer)))
        .setHeader("Authorization", String.format("Bearer %s", principal.getTokenValue()))
        .build();
      
    HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
    return jsonParser.readValue(res.body(), Auth0UserInfo.class);
  }

  public User provisionUser(Jwt principal) throws Exception {
    User user = getUserById(principal.getSubject());
    if(user != null) {
      return user;
    }

    Auth0UserInfo userInfo = fetchUserProfile(principal);

    int i = 0;
    String displayName = userInfo.nickname;
    while(users.getUserByDisplayName(displayName) != null) {
      displayName = String.format("%s_%d", displayName, ++i);
    }

    return users.createUser(
      userInfo.sub,
      userInfo.email,
      displayName
    );
  }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"sub",
"given_name",
"family_name",
"nickname",
"name",
"picture",
"locale",
"updated_at",
"email",
"email_verified"
})
class Auth0UserInfo {

  @JsonProperty("sub")
  public String sub;
  @JsonProperty("given_name")
  public String givenName;
  @JsonProperty("family_name")
  public String familyName;
  @JsonProperty("nickname")
  public String nickname;
  @JsonProperty("name")
  public String name;
  @JsonProperty("picture")
  public String picture;
  @JsonProperty("locale")
  public String locale;
  @JsonProperty("updated_at")
  public String updatedAt;
  @JsonProperty("email")
  public String email;
  @JsonProperty("email_verified")
  public Boolean emailVerified;

}