// Agent road in project traffic_system.mas2j

/* Initial beliefs */
+initialization(I1, I2): true
  <- +vehicles_moving_to_I_belief(I1, 0); // vehicles moving to one intersection
	 +vehicles_moving_to_I_belief(I2, 0); // vehicles moving to the other direction, there are only two entering points in the road
	 .concat("Initialization ", I1, " ", I2, ToPrintMessage);
     .print(ToPrintMessage).

/* Initial goals */

/* Plans */

+road_failure_percept: true
  <- .print("A failure occured in my road");
     .send(problemSolver, tell, roadFailureMessage);
	 -road_failure_percept.
	 
-road_failure_solved_percept: true
  <- .print("The failure was solved in my road").

+vehicle_enter_percept(FromI, N): vehicles_moving_to_I_belief(ToI, NI) 
  & FromI \== ToI
  <- .concat(N, " vehicles enter my road from ", FromI, ToPrintMessage);
     .print(ToPrintMessage);
	 -+vehicles_moving_to_I_belief(ToI, NI + N);
     -vehicle_enter_percept(FromI, _).
	 
+vehicle_exit_percept(FromI, N): vehicles_moving_to_I_belief(FromI, NI)
  <- .concat(N, " vehicles exit my road from ", FromI, ToPrintMessage);
     .print(ToPrintMessage);
	 -+vehicles_moving_to_I_belief(FromI, NI - N);
     -vehicle_exit_percept(FromI, _).

