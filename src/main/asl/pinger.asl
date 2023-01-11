!init.

+!init =>
    PongerAgentName = "ponger";
    #println(Self + ": pinging " + PongerAgentName);
    #coms.inform(PongerAgentName, ping).

+pong => #println(Self + ": ponged by " + Source).






