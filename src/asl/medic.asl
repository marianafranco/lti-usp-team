// Agent Repairer

/* Initial beliefs and rules */

is_go_to_best_vertex_goal		:- (bestZoneGoal & bestZone(Z1) & not jia.is_on_best_vertex(Z1))
									| (secondBestZoneGoal & secondBestZone(Z2) & not jia.is_on_best_vertex(Z2)).

/* Initial goals */

+!occupy_center_goal
	<-	//.print("Starting occupy_center_goal"); 
		!select_occupy_center_goal.

+!select_occupy_center_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_occupy_center_goal.

+!select_occupy_center_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	:	is_not_help_goal
	<-	!init_goal(not_help);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	:	is_parry_goal
	<-	!init_goal(parry);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	:	is_escape_goal
	<-	!init_goal(escape);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	:	is_repair_goal
	<-	!init_goal(repair);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	:	need_help(Ag) & visRange(R) & R > 1 & jia.agent_position(Ag,Pos) & jia.is_neighbor_vertex(Pos)
	<-	!init_goal(repair_neighbor(Ag));
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	: is_goto_fail_goal
	<-	!init_goal(goto_failled);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	:	is_repairer_buy_goal
	<-	!init_goal(repairer_buy);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	: is_move_to_zone_goal
	<-	!init_goal(move_to_zone);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	: is_go_to_best_vertex_goal
	<-	!init_goal(move_to_best);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	:	is_recharge_goal
	<-	!init_goal(be_at_full_charge);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	:	is_on_target_goal
	<-	!init_goal(wait);
			!!select_occupy_center_goal.

+!select_occupy_center_goal
	<- 	!init_goal(random_walk);
			!!select_occupy_center_goal.


/* Repair plans */

+!move_to_best : bestZoneGoal & bestZone([H|T])
	<-	jia.move_to(H,NextPos);
		!go_to(NextPos).

+!move_to_best : secondBestZoneGoal & secondBestZone([H|T])
	<-	jia.move_to(H,NextPos);
		!go_to(NextPos).

+!move_to_best
	<-	!do_and_wait_next_step(recharge).