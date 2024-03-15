initial(1,1).
battery(1,3.0).
battery(3,2.0).
battery(4,5.0).
!init.


+!init =>
    #coms.achieve("droneA", start);
    #coms.achieve("droneB", start);
    #coms.achieve("droneC", start).

+location(D,X,Y): not position(D, Z,T) =>
    +position(C, X,Y).

+location(D,X,Y): position(D,Z,T) =>
    -position(D, Z,T);
    +position(D,X,Y).
    
+?targeting(D,K,L): battery(K,L) =>
        #println("target found at: " + K + ", " + L);
        #coms.inform(D, targeting(D,K,L)).

+fired(X,Y): battery(X,Y) =>
    #println("destroyed enemy battery located at: " + X + ", " + Y).
    