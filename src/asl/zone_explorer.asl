// Agent Explorer

/* Initial beliefs and rules */

is_can_expand_zone_explorer_goal	:- position(X) & (bestZone(Z1) | secondBestZone(Z2))
									//& ( (zoneScore(S) & S < 150 & step(P) & P < 50)
									//| (zoneScore(S) & S < 180 & step(P) & P >= 50 & P < 100)
									//| (zoneScore(S) & S < 200 & step(P) & P >= 100))
									& jia.can_expand_zone_explorer.

other_agent_going_to(X)				:- secondBestZoneGoal & not_probeb_target(X).

/* Initial goals */

/***************** Explore zone goal ********************/
+!explore_zone_goal
	<-	.print("Starting explore_zone_goal");
		!select_explore_zone_goal.

+!select_explore_zone_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_explore_zone_goal.

+!select_explore_zone_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	: is_escape_goal
	<-	!init_goal(escape);
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	: is_goto_fail_goal
	<-	!init_goal(goto_failled);
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	: is_probe_goal
	<-	!init_goal(probe);
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	:	is_recharge_goal
	<-	!init_goal(be_at_full_charge);
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	: is_move_to_zone_goal
	<-	!init_goal(move_to_zone);
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	: jia.has_not_probed_vertex(X) & not other_agent_going_to(X)
	<-	!init_goal(move_to_not_probed_inside_zone(X));
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	: is_go_to_frontier_goal
	<-	!init_goal(move_to_frontier);
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	: is_can_expand_zone_explorer_goal
	<-	!init_goal(expand);
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	:	is_on_target_goal
	<-	//!init_goal(move_to_neighbor_not_probed);
		!init_goal(wait);
			!!select_explore_zone_goal.

+!select_explore_zone_goal
	<-	!init_goal(move_to_not_probed);
			!!select_explore_zone_goal.

-!select_explore_zone_goal[error(I),error_msg(M)]
	<-	.print("failure in select_explore_zone_goal! ",I,": ",M).



/********************* Plans ***********************/

+!move_to_not_probed_inside_zone(X) : bestZoneGoal
	<- 	.broadcast(tell,not_probeb_target(X));
		!move_to(X).

+!move_to_not_probed_inside_zone(X)
	<- 	!move_to(X).