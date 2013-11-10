// Agent Explorer

/* Initial beliefs and rules */

/* Initial goals */

/***************** Explore goal ********************/
+!explorer_create_zone_goal
	<-	//.print("Starting explore goal");
		!select_explorer_create_zone_goal.


+!select_explorer_create_zone_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	: is_escape_goal
	<-	!init_goal(escape);
			!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	: is_goto_fail_goal
	<-	!init_goal(goto_failled);
			!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	: is_probe_goal
	<-	!init_goal(probe);
			!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	: is_move_to_zone_goal
	<-	!init_goal(move_to_zone);
			!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	: is_go_to_frontier_goal
	<-	!init_goal(move_to_frontier);
			!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	: is_can_expand_goal
	<-	!init_goal(expand);
			!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	: position(MyV) & jia.has_not_probed_neighbor(V)
	<- 	!init_goal(probe_neighbor(V));
			!!select_explorer_create_zone_goal.


+!select_explorer_create_zone_goal
	:	is_recharge_goal
	<-	!init_goal(be_at_full_charge);
			!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	:	is_on_target_goal
	<-	//!init_goal(move_to_neighbor_not_probed);
		!init_goal(wait);
			!!select_explorer_create_zone_goal.

+!select_explorer_create_zone_goal
	<-	!init_goal(move_to_not_probed);
			!!select_explorer_create_zone_goal.

-!select_explorer_create_zone_goal[error(I),error_msg(M)]
	<-	.print("failure in select_explorer_create_zone_goal! ",I,": ",M).



/********************* Plans ***********************/

+!probe_neighbor(V)
   <- !do_and_wait_next_step(probe(V)).