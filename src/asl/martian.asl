// Agent Martian

// include role plans for agents
{ include("common.asl") }
{ include("map_explorer.asl") }
{ include("map_explorer_helper.asl") }
{ include("zone_explorer.asl") }
{ include("inspector.asl") }
{ include("repairer.asl") }
{ include("saboteur.asl") }
{ include("sentinel.asl") }
{ include("guardian.asl") }
{ include("medic.asl") }
{ include("soldier_explorer.asl") }
{ include("soldier_inspector.asl") }
{ include("soldier_repairer.asl") }
{ include("soldier_saboteur.asl") }
{ include("soldier_sentinel.asl") }

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

// join the created workspace and lookup the org artifacts
+!start 
	<- 	!join;
		lookupArtifact("marsGroupBoard",GrId);
		focus(GrId).

-!start
	<- 	.wait(200);
		!!start.

+!join 
	<- 	.my_name(Me);
		joinWorkspace("marsWS",_).
-!join
	<- 	.wait(200);
		!!join.


// keep focused on schemes my groups are responsible for
+schemes(L)
	<-	for ( .member(S,L) ) {
				lookupArtifact(S,ArtId);
				focus(ArtId)
      }.

// keep focused on subgroups
+subgroups(L)
	<-	for ( .member(S,L) ) {
				lookupArtifact(S,ArtId);
				focus(ArtId)
      }.


// init
+simStart
	:	not availableRole(R,F,M,S,G)
	<-	!!init.

+!init
	:	availableRole(R,F,M,S,G) & environment(ok)
	<-	!!restart.

+!init
	: schemes(L) & environment(ok)
	<-	!!playRole.

+!init
	<-	.wait({+environment(ok)},200,_);
		!!init.

+!restart
	:	availableRole(R,F,M,S,G) & role(X)
	<-	-role(X);
		+role(R);
		!!commit_to_mission.

//+!restart
//	:	availableRole(R,F,M,S,G)
//	<-	+role(R);
//		!!commit_to_mission.

-!restart
	<- 	.wait(200);
		!!restart.


// plan to start to play a role
+!playRole
	:	role(R) & simStart
	<- 	jia.to_lower_case(R,S);
		-role(R);
		+role(S);
		.print("I want to play role ",S);
		.send(coordinator,tell,want_to_play(S));
		!!check_available_role.

-!playRole
	<- 	.wait(200);
		!!playRole.


+!check_available_role : availableRole(R,F,M,S,G).

+!check_available_role : role(S)
	<-	.wait({+availableRole(_,_,_,_,_)},400,_);
		.send(coordinator,tell,want_to_play(S));
		.print("Waiting role ",S);
		!!check_available_role.


+availableRole(R,F,M,S,G): .my_name(Ag)
	<-	!adoptRole(F,G);
//		.print("I'm playing ",R, " on ",G);
		!check_play(Ag,F);
		!!commit_to_mission.

+!adoptRole(F,G)
	<-	lookupArtifact(G,GrId);
		adoptRole(F)[artifact_id(GrId)].



-!adoptRole(F,G)[error(I),error_msg(M)]
	: availableRole(R,F,M,S,G)
	<-	.print("Failure in adoptRole! ",I,": ",M);
		.print("[ERROR] I could not adoptRole ", F);
		-availableRole(R,F,M,S,G);
		!!check_available_role.
//		!adoptRole(F,G).

-!adoptRole(F,G)[error(I),error_msg(M)]
	<-	.print("[ERROR] Failure in adoptRole! ",I,": ",M);
		.print("[ERROR] I could not adoptRole ", F);
		!!check_available_role.

+!check_play(A,R)
	:	play(A,R,_) & .my_name(A).

+!check_play(A,R)
	<- 	.wait({+play(_,_,_)},200,_);
		.print("Try again adoptRole ", R);
		adoptRole(R)[artifact_id(GrArtId)];
		!check_play(A,R,_).


// plans to commit to missions which the agent has permission/obligation
+!commit_to_mission
	:	availableRole(R,F,M,S,G)
	<-	//.print("I will try to commit to ", M);
		commitMission(M)[artifact_name(S)];
		!!check_commit_mission(M,S).

+!commit_to_mission
	<- .print("[ERROR] I did not commit to any mission!!").

-!commit_to_mission[error(I),error_msg(EM)]
	: availableRole(R,F,M,S,G)
	<-	.print("[ERROR] Failure in commit_to_mission! ",I,": ",EM);
		.print("[ERROR] I could not commit to ", M).


// plans to handle obligations
//+obligation(Ag,Norm,committed(Ag,Mission,Scheme),Deadline)
//	: .my_name(Ag)
//   <- //.print("I am obliged to commit to ",Mission," on ",Scheme);
//      commitMission(Mission)[artifact_name(Scheme)];
//      !!check_commit_mission(Mission,Scheme).


// check commitment to mission
+!check_commit_mission(M,S)
	:	.my_name(A) & commitment(A,M,_) & play(A,F,G) & role(R)
	<-	.print("I commited to ", M);
		.broadcast(tell,coworker(A,R,M,G));		// broadcast
		+started_goal;
		.abolish(environment(_));
		!start_goal(M,S).

+!check_commit_mission(M,S)
	<-	.wait({+commitment(_,_,_)},500,_);
		.print("[ERROR] Trying again to commit to ",M," on ",S);
		commitMission(M)[artifact_name(S)];
		!!check_commit_mission(M,S).


+!start_goal(m_explore_map,_)
	<-	!!explore_map_goal.

+!start_goal(m_help_explore_map,_)
	<-	!!help_explore_map_goal.

+!start_goal(m_explore_zone,"bestZoneSch")
	<-	+bestZoneGoal;
		!!explore_zone_goal.

+!start_goal(m_explore_zone,"secondBestZoneSch")
	<-	+secondBestZoneGoal;
		!!explore_zone_goal.

+!start_goal(m_repair,_)
	<-	+bestZoneGoal;
		!!repair_goal.

+!start_goal(m_occupy_center,"bestZoneSch")
	<-	+bestZoneGoal;
		!!occupy_center_goal.

+!start_goal(m_occupy_center,"secondBestZoneSch")
	<-	+secondBestZoneGoal;
		!!occupy_center_goal.

+!start_goal(m_attack,_)
	<-	!!attack_goal.

+!start_goal(m_defend_zone,"bestZoneSch")
	<-	+bestZoneGoal;
		!!defend_zone_goal.

+!start_goal(m_defend_zone,"secondBestZoneSch")
	<-	+secondBestZoneGoal;
		!!defend_zone_goal.

+!start_goal(m_sabotage,_)
	<-	!!sabotage_goal.

+!start_goal(m_inspect,_)
	<-	+bestZoneGoal;
		!!inspect_goal.

+!start_goal(m_create_zone,"bestZoneSch")
	: role(explorer)
	<-	+bestZoneGoal;
		!!explorer_create_zone_goal.

+!start_goal(m_create_zone,"secondBestZoneSch")
	: role(explorer)
	<-	+secondBestZoneGoal;
		!!explorer_create_zone_goal.

+!start_goal(m_create_zone,"bestZoneSch")
	: role(inspector)
	<-	+bestZoneGoal;
		!!inspector_create_zone_goal.

+!start_goal(m_create_zone,"secondBestZoneSch")
	: role(inspector)
	<-	+secondBestZoneGoal;
		!!inspector_create_zone_goal.

+!start_goal(m_create_zone,"bestZoneSch")
	: role(repairer)
	<-	+bestZoneGoal;
		!!repairer_create_zone_goal.

+!start_goal(m_create_zone,"secondBestZoneSch")
	: role(repairer)
	<-	+secondBestZoneGoal;
		!!repairer_create_zone_goal.

+!start_goal(m_create_zone,"bestZoneSch")
	: role(sentinel)
	<-	+bestZoneGoal;
		!!sentinel_create_zone_goal.

+!start_goal(m_create_zone,"secondBestZoneSch")
	: role(sentinel)
	<-	+secondBestZoneGoal;
		!!sentinel_create_zone_goal.

+!start_goal(m_create_zone,"bestZoneSch")
	: role(saboteur)
	<-	+bestZoneGoal;
		!!saboteur_create_zone_goal.

+!start_goal(m_create_zone,"secondBestZoneSch")
	: role(saboteur)
	<-	+secondBestZoneGoal;
		!!saboteur_create_zone_goal.



// start new mission
+!start_new_mission(F,M,S,G)
	: obligation(Me,Norm,achieved(Scheme,Goal,Ag),DeadLine) & .my_name(Me) //& play(Me,R,Grp)
	<-	.print("Achived goal ", Goal);
		goalAchieved(Goal)[artifact_name(Scheme)];
//		lookupArtifact(Grp,GrId);
//		leaveRole(R)[artifact_id(GrId)];
		lookupArtifact(G,GrId2);
		adoptRole(F)[artifact_id(GrId2)];
		//.print("I will try to commit to ", M);
		commitMission(M)[artifact_name(S)];
		!check_commit_mission(M,S).
+!start_new_mission(F,M,S,G)
	<-	lookupArtifact(G,GrId2);
		adoptRole(F)[artifact_id(GrId2)];
		//.print("I will try to commit to ", M);
		commitMission(M)[artifact_name(S)];
		!check_commit_mission(M,S).
-!start_new_mission(F,M,S,G)[error(I),error_msg(E)]
	<-	.print("[ERROR] Failure in start_new_mission! ",I,": ",E);
		!!start_new_mission(F,M,S,G).


/* Plans to finish the simulation and start a new one */
@sme1[atomic]
+simEnd
   : .my_name(martian1)
   <- 	.print("SIM END!!");
   		.drop_all_desires;
   		//.drop_all_intentions;
   		!remove_percepts;
		jia.restart_world_model;
		.send(coordinator,tell,simEnd);
		-simEnd;
		!!init.

@sme[atomic]
+simEnd
   : not .my_name(martian1)
   <- 	.print("SIM END!!");
   		.drop_all_desires;
   		//.drop_all_intentions;
   		!remove_percepts;
		jia.restart_world_model;
		//.send(coordinator,tell,simEnd);
		-simEnd;
		!!init.

+!remove_percepts
	<-	.abolish(role(_));
		-started_goal;
		.abolish(target(_));
		.abolish(need_help(_));
		.abolish(lastAction(_));
		.abolish(achievement(_));
		.abolish(bestZone(_));
		.abolish(secondBestZone(_));
		.abolish(step(_)).

@bye[atomic]
+bye
	<-	.print("BYE!!");
		.stopMAS.