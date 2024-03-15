around(Z,T):- at(X,Y) && X==Z && Y==T.

initial(4,1).


+!start: initial(X,Y) =>
    +at(X,Y);
    !whereIam(X,Y);
	#coms.inform(environment, location(Self, X,Y));
    !moveup();
    !moveup();
    !radar();
    !moveup();
    !moveup();
    !moveup();
    !radar().


+antidronefire(X,Y): around(X,Y) =>
    +destroyed(true);
    #coms.inform(droneB, attacked(Self));
    #coms.inform(droneA, attacked(Self));
    #println("  drone " + Self + " kaput ").


+!radar(): at(X,Y) && not destroyed(true) =>
    #println(Self + " radar on ");
    #coms.inform(fighter, dronelocation(Self,X,Y));
    #coms.ask(environment, targeting(Self,X,Y)).
    
+targeting(Self, X,Y) =>
    !target(X,Y).
       
               
+!moveup(): at(X,Y) && Z is Y + 1 && not destroyed(true)  =>
    #println(Self + " moving up");
    -at(X,Y);
    +at(X,Z);
    #coms.inform(environment,location(Self,X,Z));
    !whereIam(X,Z).

+!movedown(): at(X,Y) && Z is Y - 1 && not destroyed(true) =>
    #println(Self + " moving down");
    -at(X,Y);
    +at(X,Z);
    #coms.inform(environment,location(Self,X,Z));
    !whereIam(X,Z).
    
    
+!moveleft(): at(X,Y) && Z is X - 1 && not destroyed(true) =>
    #println(Self + " moving left");
    -at(X,Y);
    +at(Z,X);
    #coms.inform(environment,location(Self,X,Z));
    !whereIam(X,Z).

+!moveright(X,Y): at(X,Y)  && Z is X + 1 && not destroyed(true) =>
    #println(Self + " moving right");
    -at(X,Y);
    +at(Z,X);
    #coms.inform(environment,location(Self,X,Z));
    !whereIam(X,Z).
        
+!whereIam(X,Y): at(X,Y) && not destroyed(true) =>
    #println(Self + " is at " + X + ", " + Y).

+!target(X,Y): not destroyed(true)  =>
    #println("target is " + X + ", " + Y);
    +targetposition(X,Y);
    #coms.inform(artillery, targetposition(X,Y)).
+attacked(X): not destroyed(true) =>
    #println(Self + " received info about destruction of " + X);
    +problems(X).

+!additionaltasks(): not destroyed(true) && problems(droneA)=>
    #println(self + " is going to replace droneA ");
    !movedown();
    !movedown();
    !moveleft();
    !radar();
    !movedown().

