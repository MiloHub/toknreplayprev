import java.util.Calendar;
import java.util.concurrent.TimeUnit;


class TokenReplayPreventionImpl implements TokenReplayPrevention {
	
	TimeLimitedCacheMap obtainedTokenInMemory = new TimeLimitedCacheMap(1, 1, 1, TimeUnit.SECONDS); 
    /**
     * Detect whether the given token is a replay or not.
     *
     * @param token the token that will be checked to ensure that it hasn't been used previously
     * @return true if it's a replay
     * 
     * 
     * method uses timebasedcachemap which extends java Map collection to clean up expired 
     * token based on notvalidateAfter time property
     * 
     */
	@Override
    public boolean isTokenReplayed(final Token token) {
    	 Calendar now = Calendar.getInstance();
    	//String tokenID = new String(token.getBytes(), "UTF-8");  
    	if(token.getTokenID()==null)return true ; // raise exception ? 
    	
    	else {
    		
    		if(obtainedTokenInMemory.getClonedMap().get(token.getTokenID()) !=null){
    			// token sent is same timeframe // is it true identify check. 
    			Long after =(Long) obtainedTokenInMemory.getClonedMap().get(token.getTokenID());
    			if(now.getTimeInMillis()<after){
    				return false;
    			}
    			return true;
    			
    		}else {
    			//case to handle expired/future new token and 
    			Long after = token.getNotValidAfter().getTime();
    			Long before =token.getNotValidBefore().getTime();
    			if(now.getTimeInMillis()<after&& now.getTimeInMillis()>before){
    				 obtainedTokenInMemory.put(token.getTokenID(), token.getNotValidAfter().getTime());
    				return false;
    			}
    			  return true;
    		}
    		
    		
    		
    	}
    }
    @Override
    public int sizeOfCacheMap(){
    	return obtainedTokenInMemory.getClonedMap().size();
    }
    
}
