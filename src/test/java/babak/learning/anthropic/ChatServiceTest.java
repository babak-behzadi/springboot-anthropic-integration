package babak.learning.anthropic;

import babak.learning.anthropic.chat.ChatMessageModel;
import babak.learning.anthropic.chat.ChatMessageType;
import babak.learning.anthropic.chat.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatServiceTest {

    @Autowired
    ChatService chatService;

    @Test
    public void talk() {
        String res = chatService.sendMessage(
                new ChatMessageModel("Hi, Claude! This is Babak.", ChatMessageType.TALK, 20));
        System.out.println(res);
    }

    @Test
    void summarize() {
        String res = chatService.sendMessage(
                new ChatMessageModel("""
                        Spring Security – OAuth2 Login
                        
                        Last updated: January 8, 2024
                        
                        
                        Written by:baeldung
                        
                        Reviewed by:Darragh Pigott
                        Spring SecurityAuthentication OAuth OAuth Spring Spring Security 5
                        announcement - icon
                        Yes, Spring Security can be complex, from the more advanced functionality within the Core to the deep OAuth support in the framework.
                        
                        I built the security material as two full courses - Core and OAuth, to get practical with these more complex scenarios. We explore when and how to use each feature and code through it on the backing project.
                        
                        You can explore the course here:
                        
                        >> Learn Spring Security
                        
                        1. Overview
                        Spring Security 5 introduces a new OAuth2LoginConfigurer class that we can use for configuring an external Authorization Server.
                        
                        In this tutorial, we’ll explore some of the various configuration options available for the oauth2Login() element.
                        
                        2. Maven Dependencies
                        In a Spring Boot project, we just need to add the starter spring-boot-starter-oauth2-client:
                        
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-oauth2-client</artifactId>
                            <version>2.3.3.RELEASE</version>
                        </dependency>
                        Copy
                        In a non-Boot project, in addition to the standard Spring and Spring Security dependencies, we’ll also need to explicitly add the spring-security-oauth2-client and spring-security-oauth2-jose dependencies:
                        
                        <dependency>
                            <groupId>org.springframework.security</groupId>
                            <artifactId>spring-security-oauth2-client</artifactId>
                            <version>5.3.4.RELEASE</version>
                        </dependency>
                        <dependency>
                            <groupId>org.springframework.security</groupId>
                            <artifactId>spring-security-oauth2-jose</artifactId>
                            <version>5.3.4.RELEASE</version>
                        </dependency>
                        Copy
                        3. Clients Setup
                        In a Spring Boot project, all we need to do is add a few standard properties for each client we want to configure.
                        
                        
                        Let’s set up our project for login with clients registered with Google and Facebook as authentication providers.
                        
                        3.1. Obtaining Client Credentials
                        To obtain client credentials for Google OAuth2 authentication, head on over to the Google API Console, “Credentials” section.
                        
                        Here we’ll create credentials of type “OAuth2 Client ID” for our web application. This results in Google setting up a client id and secret for us.
                        
                        We also have to configure an authorized redirect URI in the Google Console, which is the path that users will be redirected to after they successfully log in with Google.
                        
                        By default, Spring Boot configures this redirect URI as /login/oauth2/code/{registrationId}.
                        
                        
                        So, for Google we’ll add this URI:
                        
                        http://localhost:8081/login/oauth2/code/google
                        Copy
                        To obtain the client credentials for authentication with Facebook, we need to register an application on the Facebook for Developers website and set up the corresponding URI as a “Valid OAuth redirect URI”:
                        
                        http://localhost:8081/login/oauth2/code/facebook
                        Copy
                        3.2. Security Configuration
                        Next, we need to add the client credentials to the application.properties file.
                        
                        The Spring Security properties are prefixed with spring.security.oauth2.client.registration followed by the client name and then the name of the client property:
                        
                        spring.security.oauth2.client.registration.google.client-id=<your client id>
                        spring.security.oauth2.client.registration.google.client-secret=<your client secret>
                        
                        spring.security.oauth2.client.registration.facebook.client-id=<your client id>\s
                        spring.security.oauth2.client.registration.facebook.client-secret=<your client secret>
                        Copy
                        Adding these properties for at least one client will enable the Oauth2ClientAutoConfiguration class, which sets up all the necessary beans.
                        
                        
                        The automatic web security configuration is equivalent to defining a simple oauth2Login() element:
                        
                        @Configuration
                        public class SecurityConfig {
                        
                            @Bean
                            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                                http.authorizeRequests()
                                 .anyRequest().authenticated()
                                 .and()
                                 .oauth2Login();
                                return http.build();
                            }
                        }
                        Copy
                        Here we can see the oauth2Login() element is used in a similar manner to already known httpBasic() and formLogin() elements.
                        
                        Now, when we try to access a protected URL, the application will display an auto-generated login page with two clients:
                        
                        oauth login default
                        3.3. Other Clients
                        Note that the Spring Security project also contains default configurations for GitHub and Okta in addition to Google and Facebook. These default configurations provide all the necessary information for authentication, which is what allows us to only enter the client credentials.
                        
                        If we want to use a different authentication provider not configured in Spring Security, we’ll need to define the full configuration, with information such as authorization URI and token URI. Here’s a look at the default configurations in Spring Security to get an idea of the properties needed.
                        
                        4. Setup in a Non-Boot Project
                        4.1. Creating a ClientRegistrationRepository Bean
                        If we’re not working with a Spring Boot application, we’ll need to define a ClientRegistrationRepository bean that contains an internal representation of the client information owned by the authorization server:
                        
                        @Configuration
                        @EnableWebSecurity
                        @PropertySource("classpath:application.properties")
                        public class SecurityConfig {
                            private static List<String> clients = Arrays.asList("google", "facebook");
                        
                            @Bean
                            public ClientRegistrationRepository clientRegistrationRepository() {
                                List<ClientRegistration> registrations = clients.stream()
                                  .map(c -> getRegistration(c))
                                  .filter(registration -> registration != null)
                                  .collect(Collectors.toList());
                        
                                return new InMemoryClientRegistrationRepository(registrations);
                            }
                        }
                        Copy
                        Here we’re creating an InMemoryClientRegistrationRepository with a list of ClientRegistration objects.
                        
                        4.2. Building ClientRegistration Objects
                        Let’s see the getRegistration() method that builds these objects:
                        
                        
                        private static String CLIENT_PROPERTY_KEY\s
                          = "spring.security.oauth2.client.registration.";
                        
                        @Autowired
                        private Environment env;
                        
                        private ClientRegistration getRegistration(String client) {
                            String clientId = env.getProperty(
                              CLIENT_PROPERTY_KEY + client + ".client-id");
                        
                            if (clientId == null) {
                                return null;
                            }
                        
                            String clientSecret = env.getProperty(
                              CLIENT_PROPERTY_KEY + client + ".client-secret");
                        
                            if (client.equals("google")) {
                                return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                                  .clientId(clientId).clientSecret(clientSecret).build();
                            }
                            if (client.equals("facebook")) {
                                return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                                  .clientId(clientId).clientSecret(clientSecret).build();
                            }
                            return null;
                        }
                        Copy
                        Here we’re reading the client credentials from a similar application.properties file. Then we use the CommonOauth2Provider enum already defined in Spring Security for the rest of the client properties for Google and Facebook clients.
                        
                        Each ClientRegistration instance corresponds to a client.
                        
                        4.3. Registering the ClientRegistrationRepository
                        Finally, we have to create an OAuth2AuthorizedClientService bean based on the ClientRegistrationRepository bean and register both with the oauth2Login() element:
                        
                        @Bean
                        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                            http.authorizeRequests().anyRequest().authenticated()
                              .and()
                              .oauth2Login()
                              .clientRegistrationRepository(clientRegistrationRepository())
                              .authorizedClientService(authorizedClientService());
                            return http.build();
                        }
                        
                        @Bean
                        public OAuth2AuthorizedClientService authorizedClientService() {
                        
                            return new InMemoryOAuth2AuthorizedClientService(
                              clientRegistrationRepository());
                        }
                        Copy
                        As we can see, we can use the clientRegistrationRepository() method of oauth2Login() to register a custom registration repository.
                        
                        We’ll also have to define a custom login page, as it won’t be automatically generated anymore. We’ll see more information on this in the next section.
                        
                        Let’s continue with further customization of our login process.
                        
                        5. Customizing oauth2Login()
                        There are several elements that the OAuth 2 process uses and that we can customize with oauth2Login() methods.
                        
                        Note that all these elements have default configurations in Spring Boot and explicit configuration isn’t required.
                        
                        
                        Let’s see how we can customize these in our configuration.
                        
                        5.1. Custom Login Page
                        Even though Spring Boot generates a default login page for us, we’ll usually want to define our own customized page.
                        
                        Let’s start with configuring a new login URL for the oauth2Login() element by using the loginPage() method:
                        
                        @Bean
                        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                            http.authorizeRequests()
                              .antMatchers("/oauth_login")
                              .permitAll()
                              .anyRequest()
                              .authenticated()
                              .and()
                              .oauth2Login()
                              .loginPage("/oauth_login");
                            return http.build();
                        }
                        Copy
                        Here we’ve set up our login URL to be /oauth_login.
                        
                        Next, let’s define a LoginController with a method that maps to this URL:
                        
                        @Controller
                        public class LoginController {
                        
                            private static String authorizationRequestBaseUri
                              = "oauth2/authorization";
                            Map<String, String> oauth2AuthenticationUrls
                              = new HashMap<>();
                        
                            @Autowired
                            private ClientRegistrationRepository clientRegistrationRepository;
                        
                            @GetMapping("/oauth_login")
                            public String getLoginPage(Model model) {
                                // ...
                        
                                return "oauth_login";
                            }
                        }
                        Copy
                        This method has to send a map of the clients available and their authorization endpoints to the view, which we’ll obtain from the ClientRegistrationRepository bean:
                        
                        public String getLoginPage(Model model) {
                            Iterable<ClientRegistration> clientRegistrations = null;
                            ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                              .as(Iterable.class);
                            if (type != ResolvableType.NONE &&\s
                              ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
                                clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
                            }
                        
                            clientRegistrations.forEach(registration ->\s
                              oauth2AuthenticationUrls.put(registration.getClientName(),\s
                              authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
                            model.addAttribute("urls", oauth2AuthenticationUrls);
                        
                            return "oauth_login";
                        }
                        Copy
                        Finally, we need to define our oauth_login.html page:
                        
                        <h3>Login with:</h3>
                        <p th:each="url : ${urls}">
                            <a th:text="${url.key}" th:href="${url.value}">Client</a>
                        </p>
                        Copy
                        This is a simple HTML page that displays links to authenticate with each client.
                        
                        
                        After adding some styling to it, we can change the look of the login page:
                        
                        login
                        5.2. Custom Authentication Success and Failure Behavior
                        We can control the post-authentication behavior with different methods:
                        
                        defaultSuccessUrl() and failureUrl() to redirect the user to a given URL
                        successHandler() and failureHandler() to run custom logic following the authentication process
                        Let’s see how we can set custom URLs to redirect the user to:
                        
                        .oauth2Login()
                          .defaultSuccessUrl("/loginSuccess")
                          .failureUrl("/loginFailure");
                        Copy
                        If the user visited a secured page before authenticating, they will be redirected to that page after logging in. Otherwise, they will be redirected to /loginSuccess.
                        
                        If we want the user to always be sent to the /loginSuccess URL regardless if they were on a secured page before or not, we can use the method defaultSuccessUrl(“/loginSuccess”, true).
                        
                        To use a custom handler, we would have to create a class that implements the AuthenticationSuccessHandler or AuthenticationFailureHandler interfaces, override the inherited methods and then set the beans using the successHandler() and failureHandler() methods.
                        
                        5.3. Custom Authorization Endpoint
                        The authorization endpoint is the endpoint that Spring Security uses to trigger an authorization request to the external server.
                        
                        First, let’s set new properties for the authorization endpoint:
                        
                        
                        .oauth2Login()\s
                          .authorizationEndpoint()
                          .baseUri("/oauth2/authorize-client")
                          .authorizationRequestRepository(authorizationRequestRepository());
                        Copy
                        Here we’ve modified the baseUri to /oauth2/authorize-client instead of the default /oauth2/authorization.
                        
                        We’re also explicitly setting an authorizationRequestRepository() bean that we have to define:
                        
                        @Bean
                        public AuthorizationRequestRepository<OAuth2AuthorizationRequest>\s
                          authorizationRequestRepository() {
                        
                            return new HttpSessionOAuth2AuthorizationRequestRepository();
                        }
                        Copy
                        We’ve used the Spring-provided implementation for our bean, but we could also provide a custom one.
                        
                        5.4. Custom Token Endpoint
                        The token endpoint processes access tokens.
                        
                        Let’s explicitly configure the tokenEndpoint() with the default response client implementation:
                        
                        .oauth2Login()
                          .tokenEndpoint()
                          .accessTokenResponseClient(accessTokenResponseClient());
                        Copy
                        And here’s the response client bean:
                        
                        @Bean
                        public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>\s
                          accessTokenResponseClient() {
                        
                            return new NimbusAuthorizationCodeTokenResponseClient();
                        }
                        Copy
                        This configuration is the same as the default one, and it uses the Spring implementation, which is based on exchanging an authorization code with the provider.
                        
                        Of course, we could also substitute a custom response client.
                        
                        
                        5.5. Custom Redirection Endpoint
                        This is the endpoint to redirect to after authentication with the external provider.
                        
                        Let’s see how we can change the baseUri for the redirection endpoint:
                        
                        .oauth2Login()
                          .redirectionEndpoint()
                          .baseUri("/oauth2/redirect")
                        Copy
                        The default URI is login/oauth2/code.
                        
                        Note that if we change it, we also have to update the redirectUriTemplate property of each ClientRegistration and add the new URI as an authorized redirect URI for each client.
                        
                        5.6. Custom User Information Endpoint
                        The user info endpoint is the location we can leverage to obtain user information.
                        
                        We can customize this endpoint using the userInfoEndpoint() method. For this, we can use methods such as userService() and customUserType() to modify the way user information is retrieved.
                        
                        6. Accessing User Information
                        A common task we may want to achieve is finding information about the logged-in user. For this, we can make a request to the user information endpoint.
                        
                        First, we’ll have to get the client corresponding to the current user token:
                        
                        
                        @Autowired
                        private OAuth2AuthorizedClientService authorizedClientService;
                        
                        @GetMapping("/loginSuccess")
                        public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication) {
                            OAuth2AuthorizedClient client = authorizedClientService
                              .loadAuthorizedClient(
                                authentication.getAuthorizedClientRegistrationId(),\s
                                  authentication.getName());
                            //...
                            return "loginSuccess";
                        }
                        Copy
                        Next, we’ll send a request to the client’s user info endpoint and retrieve the userAttributes Map:
                        
                        String userInfoEndpointUri = client.getClientRegistration()
                          .getProviderDetails().getUserInfoEndpoint().getUri();
                        
                        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
                            RestTemplate restTemplate = new RestTemplate();
                            HttpHeaders headers = new HttpHeaders();
                            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                              .getTokenValue());
                            HttpEntity entity = new HttpEntity("", headers);
                            ResponseEntity <map>response = restTemplate
                              .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
                            Map userAttributes = response.getBody();
                            model.addAttribute("name", userAttributes.get("name"));
                        }
                        Copy
                        By adding the name property as a Model attribute, we can display it in the loginSuccess view as a welcome message to the user:
                        
                        welcome
                        Besides the name, the userAttributes Map also contains properties such as email, family_name, picture and locale.
                        
                        7. Conclusion
                        In this article, we saw how to use the oauth2Login() element in Spring Security to authenticate with different providers such as Google and Facebook.
                        
                        We also went through some common scenarios of customizing this process.
                        
                        The code backing this article is available on GitHub. Once you're logged in as a Baeldung Pro Member, start learning and coding on the project.
                        """, ChatMessageType.SUMMARIZE, 100)
        );
        System.out.println(res);
    }
}
