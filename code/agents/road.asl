// Agent road in project traffic_system.mas2j

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("hello world.").

+roadFailureBel : true
  <- .print("A failure occured in my road");
     .send(problemSolver, tell, roadFailureMessage).

