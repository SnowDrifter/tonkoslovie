package ru.romanov.tonkoslovie.oauth;


import lombok.Data;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

@Data
public class ClientResources {

    @NestedConfigurationProperty
    private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

    @NestedConfigurationProperty
    private ResourceServerProperties resource = new ResourceServerProperties();

    @NestedConfigurationProperty
    private PrincipalExtractor principalExtractor;

    public ClientResources(PrincipalExtractor principalExtractor) {
        this.principalExtractor = principalExtractor;
    }

}