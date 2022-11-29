package org.zahangirbd.aad;

import org.zahangirbd.util.FileUtil;

import com.microsoft.aad.msal4j.ITokenCacheAccessAspect;
import com.microsoft.aad.msal4j.ITokenCacheAccessContext;

/**
 * A SingleTone class for token persistence purpose using in store cache mechanism
 * It implements <code> ITokenCacheAccessAspect </code>  
 * @author Zahangir Alam
 *
 */
class TokenPersistence implements ITokenCacheAccessAspect {
	private static TokenPersistence instance;
	private String data;
	
	TokenPersistence(String data) {
		this.data = data;
	}
	
	@Override
	public void beforeCacheAccess(ITokenCacheAccessContext iTokenCacheAccessContext) {
		if(data != null) iTokenCacheAccessContext.tokenCache().deserialize(data);
	}
	
	@Override
	public void afterCacheAccess(ITokenCacheAccessContext iTokenCacheAccessContext) {
		data = iTokenCacheAccessContext.tokenCache().serialize();
		// TODO - we can implement logic here to write changes to file if we need to do
	}
	
	/**
	 * This method is used to get an instance of ITokenCacheAccessAspect
	 * @return
	 */
	public static ITokenCacheAccessAspect getTokenCacheAccessAspect() {
		if (TokenPersistence.instance != null) return TokenPersistence.instance;
		
		// Loads cache from resource file
		String dataToInitCache = FileUtil.readDataFromResFile(TokenPersistence.class, "cache_data/sample_token_serialized_cache.json");
		ITokenCacheAccessAspect persistenceAspect = new TokenPersistence(dataToInitCache);
		return persistenceAspect;
	}
}