package org.zahangirbd.aad;

import java.net.URI;
import java.util.Properties;
import java.util.Set;

import org.zahangirbd.util.FileUtil;

import com.microsoft.aad.msal4j.IAccount;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.ITokenCacheAccessAspect;
import com.microsoft.aad.msal4j.InteractiveRequestParameters;
import com.microsoft.aad.msal4j.MsalException;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.aad.msal4j.SilentParameters;

/**
 * This is Azure Active Directory (AAD) Token Handler class 
 * It is used to get accessToken, refresh token from the AAD  
 * 
 * @author Zahangir Alam
 *
 */
public class TokenHandler {
	private Properties prop = null;
	
	public String getAccessToken() {
		IAuthenticationResult authresult = null;
        try {
        	if (prop == null) {
            	prop = FileUtil.readPropertiesFromResFile(TokenHandler.class, "application.properties");        		
        	}
            String clientId = prop.getProperty("clinetId");
            String authEndpointUrl = prop.getProperty("authEndpointUrl");
            
            String scopesStr = prop.getProperty("scopes");
            Set<String> scopes = null; 
            if (scopesStr != null && scopesStr.length() > 0) {
            	String scopeArr[] = scopesStr.split(",");
            	scopes = Set.of(scopeArr);
            }
            
            String redirectUrl = prop.getProperty("redirectUrl");
            authresult = acquireTokenInteractive(clientId, authEndpointUrl, redirectUrl, scopes);
            if (authresult != null) {
            	return authresult.accessToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("getAccessToken(): Exception = " + e.getMessage());
        }
        return null;
    }

    public static IAuthenticationResult acquireTokenInteractive(String clientId, String authEndpointUrl, String redirectUrl, Set<String> scopes) throws Exception {
    	if (clientId == null || authEndpointUrl == null || redirectUrl == null || scopes == null) {
    		String errMsg = "acquireTokenInteractive(...): Invalid arguments passed, thus, failed to acquire token";
    		System.err.println(errMsg);
    		throw new IllegalArgumentException(errMsg);
    	}
    	
    	// loading tokenCacheAccessAspect for MSAL
    	ITokenCacheAccessAspect tokenCacheAccessAspect = TokenPersistence.getTokenCacheAccessAspect();

        PublicClientApplication pca = PublicClientApplication.builder(clientId)
                .authority(authEndpointUrl)
                .setTokenCacheAccessAspect(tokenCacheAccessAspect)
                .build();

        Set<IAccount> accountsInCache = pca.getAccounts().join();
        // Take first account in the cache. In a production application, you would filter
        // accountsInCache to get the right account for the user authenticating.
        IAccount account = accountsInCache.iterator().next();

        IAuthenticationResult result;
        try {
            SilentParameters silentParameters =
                    SilentParameters
                            .builder(scopes, account)
                            .build();

            // try to acquire token silently. This call will fail since the token cache
            // does not have any data for the user you are trying to acquire a token for
            result = pca.acquireTokenSilently(silentParameters).join();
        } catch (Exception ex) {
            if (ex.getCause() instanceof MsalException) {

                InteractiveRequestParameters parameters = InteractiveRequestParameters
                        .builder(new URI(redirectUrl))
                        .scopes(scopes)
                        .build();

                // Try to acquire a token interactively with system browser. If successful, you should see
                // the token and account information printed out to console
                result = pca.acquireToken(parameters).join();
            } else {
                // Handle other exceptions accordingly
                throw ex;
            }
        }
        return result;
    }
}
