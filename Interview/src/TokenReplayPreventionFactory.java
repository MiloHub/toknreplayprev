public class TokenReplayPreventionFactory
{
    public final static TokenReplayPrevention singletonInstance = new TokenReplayPreventionImpl();
    private TokenReplayPreventionFactory (){
    }
}
