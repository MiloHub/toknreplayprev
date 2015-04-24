# toknreplayprev
The Exercise
-------------------------------------------------------------------------------
Imagine that you are part of a software development team that is building an
enterprise server that validates incoming security tokens.  Each member of your
team has to implement a different part of the token validation process. One
important part of the process is ensuring that every token is received only once
-- this is known as "replay prevention". Your job will be to implement an
interface that examines each token as it enters the system and verify it has not
been seen previously. 

Implement the interface by filling out the stub implementation in the
TokenReplayPreventionImpl class. You can find this class in the src/ directory. 

Key Requirements/Givens:

* This class will be instantiated as a singleton by a factory class. Thus,
multiple threads may be invoking methods on an instance of the object at any
given time. The interface must be thread safe.
  
* It is acceptable to track the tokens in memory -- don't worry about
maintaining state between server restarts. However, you must ensure that the
process does not run out of memory under reasonable load. Once a token has
expired, you do not need to continue tracking replays of it.

* All tokens passed to the interface are guaranteed to be current and
properly formed. Validation of their expiration, form, etc. is done prior to the
interface invocation. Your goal is simply to ensure that a token is not being
replayed.

* Comparison of two tokens may be done using the token ID.  

You should only need to modify the TokenReplayPreventionImpl class to complete
the exercise. You may add additional or more robust unit tests to the
ReplayPreventionSimpleTest.java -- this is considered to be extra credit.
