initial(1,1).
battery(1,3.0).

+at(X,Y): not position(Z,T) =>
    +position(X,Y).

+at(X,Y): position(Z,T) =>
    -position(Z,T);
    +position(X,Y).
    
+?targeting(K,L): battery(K,L) =>
        #println("radar received");
        DroneBAgentName = "droneB";
        #println("target report: " + K + ", " + L);
        #coms.inform(DroneBAgentName, targeting(K,L)).

+fired(X,Y): battery(X,Y) =>
    #println("destroyed enemy battery located at: " + X + ", " + Y).
    
%+antidronefire(X,Y): position(X,Y) =>
    #println("drone destroyed ").