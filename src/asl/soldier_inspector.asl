// Agent Inspector

/* Initial beliefs and rules */

/* Initial goals */

/******************* Inspect goal ******************/
+!inspector_create_zone_goal
	<-	//.print("Starting inspector_create_zone_goal");
			!select_iinspector_create_zone_goal.


+!select_iinspector_create_zone_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	: is_escape_goal
	<- 	!init_goal(escape);
			!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	: is_goto_fail_goal
	<-	!init_goal(goto_failled);
			!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	: is_move_to_zone_goal
	<-	!init_goal(move_to_zone);
			!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	: is_go_to_frontier_goal
	<-	!init_goal(move_to_frontier);
			!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	: is_can_expand_goal
	<-	!init_goal(expand);
			!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	: jia.is_inspect_goal(X) & not lastAction(inspect)
	<- 	!init_goal(inspect_opponent(X));
		!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	:	is_recharge_goal
	<-	!init_goal(be_at_full_charge);
			!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	:	is_on_target_goal
	<-	!init_goal(wait);
			!!select_iinspector_create_zone_goal.

+!select_iinspector_create_zone_goal
	<- 	!init_goal(random_walk);
			!!select_iinspector_create_zone_goal.


/****************** Plans ***************************/

+!inspect_opponent(X)
	<-	!do_and_wait_next_step(inspect(X)).