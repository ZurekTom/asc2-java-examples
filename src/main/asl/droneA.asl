isHeadquarter(X):- at(X).

!init.

+!init =>

    #println(Self + ": looking for target ");
    DroneBAgentName = "droneB";
    #println(Self + ": asking about target " + DroneBAgentName);
    #coms.ask(DroneBAgentName, at(X));
    #println("question asked");
    +at(X);
    #println("at " +X);
    #println("target"); 
    !destroy_headquarters.
    


+!destroy_headquarters: isHeadquarter(X) => #println("Fire at "+X).
