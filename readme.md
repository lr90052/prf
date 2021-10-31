# Proof-It recuitment demo

no external API provided
run with com.proof.policy.calculator.PremiumCalculator#calculate or execute tests
for changing policy configuration see (test/)application.properties

assemble & test with gradle: gradle build

implementation notes:
* There's no validation. Only very basic null safety is included. 
In a commercial solution we'd verify the inputs for sensible data.
* Similarly, there's no common exception handling
* Premium calculators are to high extent similar. Depending on further development they
could be unified, but the real word business logic tend to become more complex. 
Then such unification would be to a disadvantage.    
