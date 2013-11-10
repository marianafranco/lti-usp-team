// Agent Explorer

/* Initial beliefs and rules */

// conditions for goal selection

/* Initial goals */

/***************** Explore goal ********************/
+!help_explore_map_goal
	<-	.print("Starting explore map goal");
		!select_help_explore_map_goal.


+!select_help_explore_map_goal
	: .my_name(A) & role(R) & step(S) & S > 250
	<-	//!init_goal(start_new_mission(soldier,m_create_zone,"bestZoneSch","zone1GroupBoard")).
		.broadcast(tell,coworker(A,R,m_create_zone,"zone1GroupBoard"));		// broadcast
		!start_goal(m_create_zone,"bestZoneSch").


+!select_help_explore_map_goal
	:	is_call_help_goal
	<-	!init_goal(call_help);
			!!select_help_explore_map_goal.

+!select_help_explore_map_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_help_explore_map_goal.

+!select_help_explore_map_goal
	:	is_energy_goal
	<-	!init_goal(be_at_full_charge);
			!!select_help_explore_map_goal.

+!select_help_explore_map_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_help_explore_map_goal.

+!select_help_explore_map_goal
	: is_escape_goal
	<-	!init_goal(escape);
			!!select_help_explore_map_goal.

+!select_help_explore_map_goal
	: is_probe_goal
	<-	!init_goal(probe);
			!!select_help_explore_map_goal.

+!select_help_explore_map_goal
	: is_goto_fail_goal
	<-	!init_goal(goto_failled);
			!!select_help_explore_map_goal.

+!select_help_explore_map_goal
	<-	!init_goal(move_to_neighbor_not_probed);
			!!select_help_explore_map_goal.



/********************* Plans ***********************/

