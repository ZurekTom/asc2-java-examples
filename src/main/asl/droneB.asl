minus(X,Y):- Y is X - 1.

initial(1,1).

!init.
+!init: initial(X,Y) =>
    +at(X,Y);
    !whereIam(X,Y);
	#coms.inform(environment, at(X,Y));
    !radar();
    !moveup();
    !radar();
    !moveup();
    !radar().


+antidronefire(X,Y): at(Z,T) && X==Z && Y==T =>
#println("  drone kaput ").
    
    
+attack(X,Y) =>
    #println(" drone kaput ");
    +destroyed(true).    

+!radar(): at(X,Y) =>
    #println("radar on ");
    #coms.inform(fighter, at(X,Y));
    #coms.ask(environment, targeting(X,Y)).
    
+targeting(X,Y): at(Z,T) && X==Z && Y==T =>
    !target(X,Y).
       
               
+!moveup(): at(X,Y) && Z is Y + 1 =>
    #println("moving up");
    -at(X,Y);
    +at(X,Z);
    #coms.inform(environment,at(X,Z));
    !whereIam(X,Z).

+!movedown(): at(X,Y) && Z is Y - 1 =>
    #println("moving down");
    -at(X,Y);
    +at(X,Z);
    #coms.inform(environment,at(X,Z));
    !whereIam(X,Z).
    
    
+!moveleft(): at(X,Y) && Z is X - 1 =>
    #println("moving down");
    -at(X,Y);
    +at(Z,X);
    #coms.inform(environment,at(X,Z));
    !whereIam(X,Z).

+!moveright(X,Y): at(X,Y)  && Z is X + 1 =>
    #println("moving down");
    -at(X,Y);
    +at(Z,X);
    #coms.inform(environment,at(X,Z));
    !whereIam(X,Z).
        
+!whereIam(X,Y): at(X,Y) =>
    #println("I am at " + X + ", " + Y).

+!target(X,Y): at(X,Y) =>
    #println("target is " + X + ", " + Y);
    !fire(X,Y).
        
+!fire(X,Y): at(Z,T) && X == Z && Y == T =>
    +position(X,Y);
    #println("enemy battery found");
    #coms.inform(artillery, position(X,Y)).





