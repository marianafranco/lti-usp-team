// Agent Sentinel

/* Initial beliefs and rules */


// conditions for goal selection

/* Initial goals */

/*************** Help sabotage goal *******************/

+!sentinel_create_zone_goal
	<-	//.print("Starting occupy_zone1 goal");
		!select_sentinel_create_zone_goal.


+!select_sentinel_create_zone_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_sentinel_create_zone_goal.

+!select_sentinel_create_zone_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_sentinel_create_zone_goal.

+!select_sentinel_create_zone_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_sentinel_create_zone_goal.

+!select_sentinel_create_zone_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_sentinel_create_zone_goal.

+!select_sentinel_create_zone_goal
	:	is_parry_goal
	<-	!init_goal(parry);
			!!select_sentinel_create_zone_goal.

+!select_sentinel_create_zone_goal
	: is_goto_fail_goal
	<-	!init_goal(goto_failled);
			!!select_sentinel_create_zone_goal.

+!select_sentinel_create_zone_goal
	: is_move_to_zone_goal
	<-	!init_goal(move_to_zone);
			!!select_sentinel_create_zone_goal.

+!select_sentinel_create_zone_goal
	: is_go_to_frontier_goal
	<-	!init_goal(move_to_frontier);
			!!select_sentinel_create_zone_goal.

+!select_sentinel_create_zone_goal
	: is_can_expand_goal
	<-	!init_goal(expand);
			!!select_sentinel_create_zone_goal.

+!select_sentinel_create_zone_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_sentinel_create_zone_goal.

+!select_sentinel_create_zone_goal
	:	is_recharge_goal
	<-	!init_goal(be_at_full_charge);
			!!select_sentinel_create_zone_goal.

+!select_sentinel_create_zone_goal
	:	is_on_target_goal
	<-	!init_goal(wait);
			!!select_sentinel_create_zone_goal.

+!select_sentinel_create_zone_goal
	<- 	!init_goal(random_walk);
			!!select_sentinel_create_zone_goal.


/**************** Plans *****************/
