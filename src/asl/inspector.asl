// Agent Inspector

/* Initial beliefs and rules */

// conditions for goal selection
is_inspect_role_goal		:- jia.is_inspect_role_goal.
has_uninspected_opponent	:- jia.has_uninspected_opponent.

/* Initial goals */

/******************* Inspect goal ******************/
+!inspect_goal
	<-	.print("Starting inspect goal");
		!select_inspect_goal.


+!select_inspect_goal
	: not has_uninspected_opponent
	<-	!init_goal(start_new_mission(soldier,m_create_zone,"secondBestZoneSch","zone2GroupBoard")).

+!select_inspect_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_inspect_goal.

+!select_inspect_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_inspect_goal.

+!select_inspect_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_inspect_goal.

+!select_inspect_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_inspect_goal.

+!select_inspect_goal
	: is_escape_goal
	<- 	!init_goal(escape);
			!!select_inspect_goal.

+!select_inspect_goal
	: is_inspect_role_goal
	<- 	!init_goal(inspect);
			!!select_inspect_goal.

+!select_inspect_goal
	: is_survey_goal
	<- 	!init_goal(survey);
			!!select_inspect_goal.

+!select_inspect_goal
	: is_goto_fail_goal
	<-	!init_goal(goto_failled);
			!!select_inspect_goal.

+!select_inspect_goal
	: has_uninspected_opponent
	<-	!init_goal(go_to_uninspected);
			!!select_inspect_goal.

+!select_inspect_goal
	<- 	!init_goal(random_walk);
			!!select_inspect_goal.


/****************** Plans ***************************/

/* Go to uninspected opponent */
+!go_to_uninspected
	<-	jia.closer_uninspected_opponent(Pos);
			!move_to(Pos).

/* Inspect plans */
+!inspect
	<-	!do_and_wait_next_step(inspect).
