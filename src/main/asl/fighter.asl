missiles(2).
+dronelocation(D, X,Y) =>
    #println("fighter knows location of " + D);
    !firemissile(D,X,Y).

+!firemissile(D, X,Y): missiles(N) && NN is N - 1 && N > 0 =>
    -missiles(N);
    +missiles(NN);
    #println(Self + " firing anti drone Missile at " + X + ", " +Y + " to destroy " +D);
    #println("missile is flying");
    #coms.inform(D,antidronefire(X,Y));
    #println(NN + " missiles remains").

    
+!firemissile(D, X,Y): missiles(N) && N == 0 =>
    #println(Self + " cannot fire at " + X + ", " +Y + " to destroy " +D);
    #println(Self + " out of missiles").
