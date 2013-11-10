// Agent Repairer

/* Initial beliefs and rules */

/* Initial goals */

+!repairer_create_zone_goal
	<-	//.print("Starting repairer_create_zone_goal"); 
			!select_repairer_create_zone_goal.


+!select_repairer_create_zone_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	:	is_not_help_goal
	<-	!init_goal(not_help);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	:	is_parry_goal
	<-	!init_goal(parry);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	:	is_escape_goal
	<-	!init_goal(escape);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	:	is_repair_goal
	<-	!init_goal(repair);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	: is_goto_fail_goal
	<-	!init_goal(goto_failled);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	:	is_repairer_buy_goal
	<-	!init_goal(repairer_buy);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	: is_move_to_zone_goal
	<-	!init_goal(move_to_zone);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	: is_go_to_frontier_goal
	<-	!init_goal(move_to_frontier);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	: is_can_expand_goal
	<-	!init_goal(expand);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	:	need_help(Ag) & visRange(R) & R > 1 & jia.agent_position(Ag,Pos) & jia.is_neighbor_vertex(Pos)
	<-	!init_goal(repair_neighbor(Ag));
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	:	is_recharge_goal
	<-	!init_goal(be_at_full_charge);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	:	is_on_target_goal
	<-	!init_goal(wait);
			!!select_repairer_create_zone_goal.

+!select_repairer_create_zone_goal
	<- 	!init_goal(random_walk);
			!!select_repairer_create_zone_goal.


/* Repair plans */
