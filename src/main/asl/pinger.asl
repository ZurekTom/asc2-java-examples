isHeadquarter(X):- at(X).

!init.

+!init =>

    #println(Self + ": looking for target ");
    PongerAgentName = "ponger";
    #println(Self + ": asking about target " + PongerAgentName);
    #coms.ask(PongerAgentName, at(X));
    #println("question asked");
    +at(X);
    #println("at " +X);
    #println("target"); 
    !destroy_headquarters.
    


+!destroy_headquarters: isHeadquarter(X) => #println("Fire at "+X).
