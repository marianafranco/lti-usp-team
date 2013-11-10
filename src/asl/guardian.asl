// Agent Saboteur

/* Initial beliefs and rules */

// conditions for goal selection
is_move_to_attack_goal			:- (bestZoneGoal & bestZone(Z1) & jia.has_opponent_on_zone(Z1))
									| (secondBestZoneGoal & secondBestZone(Z2) & jia.has_opponent_on_zone(Z2)).

is_can_expand_guardian_goal		:- position(X) & (bestZone(Z1) | secondBestZone(Z2))
									//& ( (zoneScore(S) & S < 150 & step(P) & P < 50)
									//| (zoneScore(S) & S < 180 & step(P) & P >= 50 & P < 100)
									//| (zoneScore(S) & S < 200 & step(P) & P >= 100))
									& jia.can_expand_guardian.

/* Initial goals */

/******************** Defend zone goal ***********************/
+!defend_zone_goal
	:	role(saboteur)
	<-	.print("Starting defend_zone_goal");
		!select_defend_zone_goal.


+!select_defend_zone_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_defend_zone_goal.

+!select_defend_zone_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	: stop_attack_goal
	<-	!init_goal(stop_attack);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	: is_attack_goal
	<-	!init_goal(attack);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	: is_goto_fail_goal
	<-	!init_goal(goto_failled);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	:	is_saboteur_buy_goal
	<-	!init_goal(saboteur_buy);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	: is_move_to_zone_goal
	<-	!init_goal(move_to_zone);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	: is_move_to_attack_goal
	<-	!init_goal(move_to_attack_zone);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	: bestZoneGoal & bestZone(Z) & jia.has_opponent_inside_zone(NextPos)
	<-	!init_goal(move_to_defend_zone(NextPos));
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	: secondBestZoneGoal & secondBestZone(Z) & jia.has_opponent_inside_zone(NextPos)
	<-	!init_goal(move_to_defend_zone(NextPos));
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	: is_go_to_frontier_goal
	<-	!init_goal(move_to_frontier);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	: is_can_expand_guardian_goal
	<-	!init_goal(expand);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	:	is_on_target_goal
	<-	!init_goal(wait);
			!!select_defend_zone_goal.

+!select_defend_zone_goal
	<- 	!init_goal(random_walk);
			!!select_defend_zone_goal.



/**************** Plans *****************/

+!move_to_attack_zone : bestZoneGoal & bestZone(Z)
	<-	jia.get_opponent_on_zone_vertex(Z,X);
		!move_to(X).

+!move_to_attack_zone : secondBestZoneGoal & secondBestZone(Z)
	<-	jia.get_opponent_on_zone_vertex(Z,X);
		!move_to(X).

+!move_to_defend_zone(X)
	<-	!move_to(X).