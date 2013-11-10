// Agent Sentinel

/* Initial beliefs and rules */


// conditions for goal selection

/* Initial goals */

/*************** Sabotage goal *******************/

+!sabotage_goal
	<- 	//.print("Starting sabotage_goal");
		!select_sabotage_goal.


+!select_sabotage_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
			!!select_sabotage_goal.

+!select_sabotage_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
		!!select_sabotage_goal.

+!select_sabotage_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
		!!select_sabotage_goal.

+!select_sabotage_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
		!!select_sabotage_goal.

+!select_sabotage_goal
	:	is_parry_goal
	<-	!init_goal(parry);
		!!select_sabotage_goal.

+!select_sabotage_goal
	: is_survey_goal
	<- 	!init_goal(survey);
		!!select_sabotage_goal.

+!select_sabotage_goal
	: is_goto_fail_goal
	<-	!init_goal(goto_failled);
		!!select_sabotage_goal.

+!select_sabotage_goal
	<- 	!init_goal(help_sabotage);
		!!select_sabotage_goal.



/**************** Plans *****************/


/* Help sabotage plan */
+!help_sabotage
	<-	jia.select_opponent_vertex(Pos);
		!move_to(Pos).
