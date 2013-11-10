// Agent Saboteur

/* Initial beliefs and rules */

// conditions for goal selection
is_attack_goal 					:- 	visRange(R) & jia.has_opponent_on_vertex(R).
stop_attack_goal				:-	lastAction(attack) & lastActionResult(failed_away).

/* Initial goals */

/******************** Sabotage goal ***********************/
+!attack_goal
	<- 	//.print("Starting attack_goal");
			!select_attack_goal.


+!select_attack_goal
	:	is_call_help_goal
		<-	!init_goal(call_help);
				!!select_attack_goal.

+!select_attack_goal
	:	is_not_need_help_goal
	<-	!init_goal(not_need_help);
			!!select_attack_goal.

+!select_attack_goal
	:	is_energy_saboteur_goal
	<-	!init_goal(be_at_full_charge);
			!!select_attack_goal.

+!select_attack_goal
	:	is_disabled_goal
	<-	!init_goal(go_to_repairer);
			!!select_attack_goal.

+!select_attack_goal
	: stop_attack_goal
	<-	!init_goal(stop_attack);
			!!select_attack_goal.

+!select_attack_goal
	: is_attack_goal
	<-	!init_goal(attack);
			!!select_attack_goal.

+!select_attack_goal
	:	is_saboteur_buy_goal
	<-	!init_goal(saboteur_buy);
			!!select_attack_goal.

+!select_attack_goal
	: is_goto_fail_goal
	<-	!init_goal(goto_failled);
			!!select_attack_goal.

+!select_attack_goal
	<- 	!init_goal(go_attack);
			!!select_attack_goal.



/**************** Plans *****************/

/* Plans for attack */

+!go_attack
	<-	jia.select_opponent_to_attack(Pos);
			!move_to(Pos).

+!attack :  visRange(R)
	<-	jia.get_opponent_to_attack(R,Enemy);
		!do_and_wait_next_step(attack(Enemy)).

+!stop_attack
	<-	jia.stop_attack;
			-lastActionResult(failed_away).

/* Buy plans */
+!saboteur_buy
	<-	!buy(sensor).