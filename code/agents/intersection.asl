// Agent intersection in project traffic_system.mas2j

/* Initial beliefs */
total_intersection_punctuation(0).
+initialization(GreenRoad, SurroundingRoadIntersectionAgents): true
  <- +green_road_belief(GreenRoad); // the vehicles coming from the GreenRoad can pass through the intersection
  	 +road_intersections_agents_belief(SurroundingRoadIntersectionAgents); // list of road and intersection agents in contact with the intersection in the form -> [[road1, intersection1], ...]
	 .concat("Initialization ", GreenRoad, " ", SurroundingRoadIntersectionAgents, ToPrintMessage);
     .print(ToPrintMessage).
	 
/* Rules */
list_sum([Item], Item).
list_sum([Item1,Item2 | Tail], Total) :- list_sum([Item1+Item2|Tail], Total).

compute_road_punctuation(VehiclesFromRoad, VehiclesFromIntersection, Punctuation) :- Punctuation = VehiclesFromRoad + 0.25 * VehiclesFromIntersection.

compute_overall_intersection_punctuation(TotalPunctuation) :- .findall(Punctuation, road_punctuation(_, Punctuation), L) &
  list_sum(L, TotalPunctuation).

/* Initial goals */

/* Plans */

+timer : true
  <- .print("timer percept");
     ?road_intersections_agents_belief(SurroundingRoadIntersectionAgents);
     !update_green_road;
	 !update_intersection_punctuation;
	 !update_roads_punctuation(SurroundingRoadIntersectionAgents);
	 -timer.

@UGR1
+!update_green_road : road_punctuation(RoadAgent, N1) & 
  green_road_belief(GreenRoad) &
  road_punctuation(GreenRoad, N2) &
  N1 > N2
  <- .print("update green direction");
     update_green_direction(RoadAgent).

@UGR2
+!update_green_road.
	 
@UGP1
+!update_intersection_punctuation: road_punctuation(_, _)
  <- ?compute_overall_intersection_punctuation(TotalPunctuation);
     -+total_intersection_punctuation(TotalPunctuation).

@UGP2
+!update_intersection_punctuation: true
  <- -+total_intersection_punctuation(0).

@URP1
+!update_roads_punctuation([[RoadAgent, IntersectionAgent] | L]) : IntersectionAgent \== "null"
  <- .send(RoadAgent, askOne, vehicles_moving_to_I_belief(Self, _), vehicles_moving_to_I_belief(Self, VehiclesFromRoad));
     .send(IntersectionAgent, askOne, total_intersection_punctuation(_), total_intersection_punctuation(VehiclesFromIntersection));
	 ?compute_road_punctuation(VehiclesFromRoad, VehiclesFromIntersection, Punctuation);
	 -road_punctuation(RoadAgent, _);
     +road_punctuation(RoadAgent, Punctuation);
	 !update_roads_punctuation(L).
	 
@URP2
+!update_roads_punctuation([[RoadAgent, IntersectionAgent] | L]) : IntersectionAgent == "null"
  <- .send(RoadAgent, askOne, vehicles_moving_to_I_belief(Self, _), vehicles_moving_to_I_belief(Self, VehiclesFromRoad));
  	 ?compute_road_punctuation(VehiclesFromRoad, 0, Punctuation);
	 -road_punctuation(RoadAgent, _);
     +road_punctuation(RoadAgent, Punctuation);
	 !update_roads_punctuation(L).
  
@URP3
+!update_roads_punctuation([]). 

