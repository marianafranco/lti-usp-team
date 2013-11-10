// Agent Saboteur

/* Initial beliefs and rules */

is_attack_visible_goal 					:- 	visRange(R) & jia.has_opponent_visible(R).


/* Initial goals */

/******************** Sabotage goal ***********************/
+!saboteur_create_zone_goal
	<-	//.print("Starting saboteur_create_zone_goal");
		!select_saboteur_create_zone_goal.


+!select_saboteur_create_zone_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_saboteur_create_zone_goal.

+!select_saboteur_create_zone_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_saboteur_create_zone_goal.

+!select_saboteur_create_zone_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_saboteur_create_zone_goal.

+!select_saboteur_create_zone_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_saboteur_create_zone_goal.

+!select_saboteur_create_zone_goal
	: stop_attack_goal
	<-	!init_goal(stop_attack);
			!!select_saboteur_create_zone_goal.
			
+!select_saboteur_create_zone_goal
	: is_attack_visible_goal
	<-	!init_goal(attack_visible);
			!!select_saboteur_create_zone_goal.

+!select_saboteur_create_zone_goal
	: is_goto_fail_goal
	<-	!init_goal(goto_failled);
			!!select_saboteur_create_zone_goal.

+!select_saboteur_create_zone_goal
	:	is_saboteur_buy_goal
	<-	!init_goal(saboteur_buy);
			!!select_saboteur_create_zone_goal.

+!select_saboteur_create_zone_goal
	: is_move_to_zone_goal
	<-	!init_goal(move_to_zone);
			!!select_saboteur_create_zone_goal.

+!select_saboteur_create_zone_goal
	: is_go_to_frontier_goal
	<-	!init_goal(move_to_frontier);
			!!select_saboteur_create_zone_goal.

+!select_saboteur_create_zone_goal
	: is_can_expand_goal
	<-	!init_goal(expand);
			!!select_saboteur_create_zone_goal.

+!select_saboteur_create_zone_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_saboteur_create_zone_goal.

+!select_saboteur_create_zone_goal
	:	is_on_target_goal
	<-	!init_goal(wait);
			!!select_saboteur_create_zone_goal.

+!select_saboteur_create_zone_goal
	<- 	!init_goal(random_walk);
			!!select_saboteur_create_zone_goal.



/**************** Plans *****************/

+!attack_visible :  visRange(R)
	<-	jia.get_opponent_name(R,Enemy);
		!do_and_wait_next_step(attack(Enemy)).