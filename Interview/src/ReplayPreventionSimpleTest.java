import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class ReplayPreventionSimpleTest {

    static TokenReplayPrevention replayPrevention = TokenReplayPreventionFactory.singletonInstance;
    static Random random = new Random();


    public static void main(String[] args) throws UnsupportedEncodingException {
       
    	ReplayPreventionSimpleTest test = new ReplayPreventionSimpleTest();
       
    	test.testFirstToken();
    	test.testSameTokenSameTime();
    	test.testSameTokenDifferentTime();
        
    	test.testDifferentToken();
    	test.testSizeOfCacheMapForExpiration();
    	test.testTokenExpired();
    	test.testTokenFuture();

    }
   
    static Token createToken(String tId){
    	 // Create a test Token to test the TokenReplayPrevention
    	// A dummy token ID
        String tokenID = tId ; //"dummy-token-ID-1";
        

        // Some validity dates on the token
        Calendar notBefore = Calendar.getInstance();
        notBefore.add(Calendar.SECOND, -20);
        Calendar notAfter = Calendar.getInstance();
        notAfter.add(Calendar.SECOND, 60);

        // For testing, just convert the tokenID to bytes for the raw token value.  A real token might have more stuff
        // but this is sufficient for testing the replay prevention
        byte[] rawToken = tokenID.getBytes();

        // This TokenReplayPrevention class shouldn't even look at the signature so we'll just leave it null
        TokenSignature tokenSignature = null;

        // Create a test Token
        Token token = new Token(tokenID, notBefore.getTime(), notAfter.getTime(), tokenSignature, rawToken);
        return token;
    }
    
   private void testFirstToken(){
	   System.out.println("test case 1:testFirstToken : start" );
    	Token token = createToken("dummy-token-ID-1");
        this.assertTrue(!replayPrevention.isTokenReplayed(token));
        System.out.println("test case 1:testFirstToken : end" );
        
    }
 
   private void testSameTokenSameTime(){
    	 System.out.println("test case 2:testSameToken : start" );
    	Token token = createToken("dummy-token-ID-1");
        System.out.println(replayPrevention.isTokenReplayed(token));
        assertTrue(!replayPrevention.isTokenReplayed(token));
        System.out.println("test case 2:testSameToken : end" );
        
    }
   private void testSameTokenDifferentTime(){
  	 System.out.println("test case 2:testSameToken : start" );
  	 Token token = createToken("dummy-token-ID-1");
      System.out.println(replayPrevention.isTokenReplayed(token));
      assertTrue(!replayPrevention.isTokenReplayed(token));
      System.out.println("test case 2:testSameToken : end" );
      
  }
   
   
   private void testDifferentToken(){
    	System.out.println("test case 3:testDifferentToken : start" );
    	Token token1 = createToken("dummy-token-ID-1");
    	replayPrevention.isTokenReplayed(token1);
    	 Token token2 = createToken("dummy-token-ID-2");
         System.out.println("should be false-->" +replayPrevention.isTokenReplayed(token2));
         System.out.println("test case 3:testDifferentToken : end" );
    }
 
   private void testSizeOfCacheMapForExpiration(){
  	
   		System.out.println("size of map" + replayPrevention.sizeOfCacheMap());
     
   		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
       
  
  	System.out.println("size of map" + replayPrevention.sizeOfCacheMap());
  }
   
   private void testTokenExpired(){
	   System.out.println("test case 5:testTokenExpired : start" );

  	 // Create a test Token to test the TokenReplayPrevention
  	// A dummy token ID
      String tokenID ="dummy-token-ID-1";
      

      // Some validity dates on the token
      Calendar notBefore = Calendar.getInstance();
      notBefore.add(Calendar.DATE, -20);
      notBefore.add(Calendar.SECOND, -20);
      Calendar notAfter = Calendar.getInstance();
      notAfter.add(Calendar.DATE, -10);
      notAfter.add(Calendar.SECOND, 60);

      // For testing, just convert the tokenID to bytes for the raw token value.  A real token might have more stuff
      // but this is sufficient for testing the replay prevention
      byte[] rawToken = tokenID.getBytes();

      // This TokenReplayPrevention class shouldn't even look at the signature so we'll just leave it null
      TokenSignature tokenSignature = null;

      // Create a test Token
      Token token = new Token(tokenID, notBefore.getTime(), notAfter.getTime(), tokenSignature, rawToken);
  		
      assertTrue(!replayPrevention.isTokenReplayed(token));
      System.out.println("test case 5:testTokenExpired : end" );
 }
   
   private void testTokenFuture(){
	   System.out.println("test case 6:testTokenExpired : start" );

  	 // Create a test Token to test the TokenReplayPrevention
  	// A dummy token ID
      String tokenID ="dummy-token-ID-1";
      

      // Some validity dates on the token
      Calendar notBefore = Calendar.getInstance();
      notBefore.add(Calendar.DATE, 20);
      notBefore.add(Calendar.SECOND, -20);
      Calendar notAfter = Calendar.getInstance();
      notAfter.add(Calendar.DATE, 30);
      notAfter.add(Calendar.SECOND, 60);

      // For testing, just convert the tokenID to bytes for the raw token value.  A real token might have more stuff
      // but this is sufficient for testing the replay prevention
      byte[] rawToken = tokenID.getBytes();

      // This TokenReplayPrevention class shouldn't even look at the signature so we'll just leave it null
      TokenSignature tokenSignature = null;

      // Create a test Token
      Token token = new Token(tokenID, notBefore.getTime(), notAfter.getTime(), tokenSignature, rawToken);
  		
      assertTrue(!replayPrevention.isTokenReplayed(token));
      System.out.println("test case 6:testTokenExpired : end" );
 }
   

      void assertTrue(boolean assertion) {
        if (!assertion) {
            System.err.println(("Assertion Failed"));
        }
      }
      void assertFalse(boolean assertion) {
        if (!assertion) {
            System.out.println("validation test for failure");
        }
    }
}
