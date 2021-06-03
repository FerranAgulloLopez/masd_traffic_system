// Agent problemSolver in project traffic_system.mas2j

/* Initial beliefs */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("hello world.").

+roadFailureMessage
  <- .print("A road failure occured in my controlled zone");
     sendRepairCarEnvAction.
     -roadFailureMessage.

