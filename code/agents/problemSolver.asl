// Agent problemSolver in project traffic_system.mas2j

/* Initial beliefs and rules */
// know the position of all the repair vehicles
// send the one closest to the position of failure

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("hello world.").

+roadFailureMessage 
  <- .print("A road failure occured in my controlled zone");
     sendRepairCarEnvAction.
     -roadFailureMessage.

