// Agent Coordinator

/* Initial beliefs and rules */


/* Initial goals */

!start.
!check_want_to_play.

/* Plans */

+!start
	<- 	!add_available_roles;
		createWorkspace("marsWS");
  		joinWorkspace("marsWS",MarsMWsp);
		makeArtifact("marsGroupBoard","ora4mas.nopl.GroupBoard",["lti-usp-os.xml",lti_usp_team,false,false],GrArtId);
	 	setOwner(coordinator);
     	focus(GrArtId);
     	makeArtifact("zone1GroupBoard","ora4mas.nopl.GroupBoard",["lti-usp-os.xml",best_zone_group,false,false],Zone1GrArtId);
	 		setParentGroup(marsGroupBoard)[artifact_id(Zone1GrArtId)];
     	focus(Zone1GrArtId);
     	makeArtifact("zone2GroupBoard","ora4mas.nopl.GroupBoard",["lti-usp-os.xml",second_best_zone_group,false,false],Zone2GrArtId);
	 		setParentGroup(marsGroupBoard)[artifact_id(Zone2GrArtId)];
     	focus(Zone2GrArtId);
     	makeArtifact("attackGroupBoard","ora4mas.nopl.GroupBoard",["lti-usp-os.xml",attack_group,false,false],AttackGrArtId);
	 		setParentGroup(marsGroupBoard)[artifact_id(AttackGrArtId)];
     	focus(AttackGrArtId);
     	adoptRole(coordinator)[artifact_id(GrArtId)];
  		!!run_scheme.
-!start[error(I),error_msg(M)]
	<-	.print("Failure in starting! ",I,": ",M).


// scheme creation
+!run_scheme
	<-  makeArtifact(teamSch,"ora4mas.nopl.SchemeBoard",["lti-usp-os.xml", team_sch, false, false ],SchArtId);
		focus(SchArtId);
		.print("scheme teamSch created");
		addScheme(teamSch)[artifact_name("marsGroupBoard")]; 
		//.print("scheme is linked to responsible group");
		
		makeArtifact(bestZoneSch,"ora4mas.nopl.SchemeBoard",["lti-usp-os.xml", occupy_zone_sch, false, false ],SchArtId1);
		focus(SchArtId1);
		.print("scheme bestZoneSch created");
		addScheme(bestZoneSch)[artifact_name("zone1GroupBoard")]; 
		//.print("scheme is linked to responsible group");
		makeArtifact(secondBestZoneSch,"ora4mas.nopl.SchemeBoard",["lti-usp-os.xml", occupy_zone_sch, false, false ],SchArtId2);
		focus(SchArtId2);
		.print("scheme secondBestZoneSch created");
		addScheme(secondBestZoneSch)[artifact_name("zone2GroupBoard")]; 
		//.print("scheme is linked to responsible group");
		makeArtifact(attackSch,"ora4mas.nopl.SchemeBoard",["lti-usp-os.xml", attack_sch, false, false ],SchArtId3);
		focus(SchArtId3);
		.print("scheme attackSch created");
		addScheme(attackSch)[artifact_name("attackGroupBoard")];
		
		.broadcast(tell,environment(ok));
		//.print("scheme is linked to responsible group").
		!!coordinate_goal.
			
-!run_scheme[error(I),error_msg(M)]
	<- 	.print("failure creating scheme -- ",I,": ",M).

// plans to handle obligations
//+obligation(Ag,Norm,committed(Ag,Mission,Scheme),Deadline)
//    : .my_name(Ag)
//   <- //.print("I am obliged to commit to ",Mission," on ",Scheme);
//      commitMission(Mission)[artifact_name(Scheme)];
//      !check_commit_mission(Mission,Scheme).

//+obligation(Ag,Norm,achieved(Scheme,Goal,Ag),DeadLine)
//    : .my_name(Ag)
//   <- //.print("I am obliged to achieve goal ",Goal);
//      !Goal[scheme(Scheme)];
//      goalAchieved(Goal)[artifact_name(Scheme)].


// check commitment to mission
//+!check_commit_mission(M,S)
//	:	.my_name(A) & commitment(A,M,_)
//	<-	.print("I commited to ", M).

//+!check_commit_mission(M,S)
//	<-	.wait({+commitment(_,_,_)},200,_);
//			.print("[ERROR] Trying again to commit to ",M," on ",S);
//			commitMission(M)[artifact_name(S)];
//			!!check_commit_mission(M,S).


/* Plans to inform to the other agents their mission and group to join */

@wtp1[atomic]
+want_to_play(R)[source(Ag)]
	:	agentRole(Ag,R,F,M,S,G)
	<-	.print("Sending again to ",Ag," role ",R);
		.send(Ag,tell,availableRole(R,F,M,S,G));
		-want_to_play(R)[source(Ag)].

@wtp2[atomic]
+want_to_play(R)[source(Ag)]
	: availableRole(Id,R,F,M,S,G)
	<-	-availableRole(Id,R,F,M,S,G);
		+agentRole(Ag,R,F,M,S,G);
		.print("Sending to ",Ag," role ",R);
		.send(Ag,tell,availableRole(R,F,M,S,G));
		-want_to_play(R)[source(Ag)].

+want_to_play(R)[source(Ag)]
	<- .print("[ERROR] Can not send to ",Ag," role ",R).


+!check_want_to_play
	: want_to_play(R)[source(Ag)] & availableRole(Id,R,F,M,S,G)
	<- -availableRole(Id,R,F,M,S,G);
		+agentRole(Ag,R,F,M,S,G);
		.print("Sending to ",Ag," role ",R);
		.send(Ag,tell,availableRole(R,F,M,S,G));
		-want_to_play(R)[source(Ag)];
		!!check_want_to_play.

+!check_want_to_play
	<-	.wait(400);
		!!check_want_to_play.

/* Plans to finish the simulation and start a new one */
@simend[atomic]
+simEnd[source(martian1)]
	<-	.print("SIM END!!");
		.drop_all_desires;
		!remove_percepts;
		//!add_available_roles;
		jia.restart_world_model;
		!!restart_scheme.

+!remove_percepts
	<-	.print("Remove percepts!!");
		.abolish(achievement(_));
		//.abolish(coworker(_,_,_));
		//.abolish(coworkerPosition(_,_));
		.abolish(simEnd);
		.abolish(want_to_play(_)).
		//.abolish(step(_)).

-!remove_percepts[error(I),error_msg(M)]
	<-	.print("[ERROR] Failure in remove_percepts! ",I,": ",M).

+!restart_scheme_old
	<-	.print("Restart scheme!!");
		lookupArtifact("attackSch",SchId2);
      	destroy[artifact_id(SchId2)];
		disposeArtifact(SchId2);
		.print("disposed attackSch!!");
		lookupArtifact("secondBestZoneSch",SchId1);
      	destroy[artifact_id(SchId1)];
		disposeArtifact(SchId1);
		.print("disposed secondBestZoneSch!!");
		lookupArtifact("bestZoneSch",SchId);
      	destroy[artifact_id(SchId)];
		disposeArtifact(SchId);
		.print("disposed bestZoneSch!!");
   		lookupArtifact("teamSch",SchId3);
      	destroy[artifact_id(SchId3)];
		disposeArtifact(SchId3);
		.print("disposed teamSch!!");
		.abolish(step(_));
		!run_scheme.

+!restart_scheme
	<-	.abolish(step(_));
		.broadcast(tell,environment(ok));
		.print("Restart!!");
		!!coordinate_goal.

-!restart_scheme[error(I),error_msg(M)]
	<-	.print("[ERROR] Failure in restart_scheme! ",I,": ",M);
		!!restart_scheme.

/* Agents coordination plans */

// coordination goal
+!coordinate_goal
	<-	.print("Starting coordinate goal");
		!!coordinate.

+!coordinate
	: step(S)
	<- 	//.print("(step: ",S,") Executing goal coordination");
		!calc_best_zones;
		!wait_next_step(S);
		!!coordinate.

+!coordinate
	<-	//.print("No step yet... wait a bit");
      	.wait( { +step(_) }, 300, _);
	  	!!coordinate.


+!wait_next_step(S)
	: step(X) & X > S.

+!wait_next_step(S)
	<-	.wait( { +step(_) }, 1000, _);
		!wait_next_step(S).


+!calc_best_zones
	<-	jia.get_best_zones(F, S);
		!send_best_zones(F, S).

+!send_best_zones([],[])
	<-	.print("empty bestZone").
+!send_best_zones(F, S)
 	<- 	.print("bestZone: ",F);
 		.broadcast(tell,bestZone(F));
 		.broadcast(tell,secondBestZone(S)).

-!send_best_zones[error(I),error_msg(M)]
	<-	.print("[ERROR] Failure in send_best_zones! ",I,": ",M).


	
// available roles in the beginning of the match
// the coordinator is responsible to distribute these roles among the other agents
+!add_available_roles
	<-	+availableRole(1,explorer,map_explorer,m_explore_map,"teamSch","marsGroupBoard");
		+availableRole(2,explorer,zone_explorer,m_explore_zone,"bestZoneSch","zone1GroupBoard");
		+availableRole(3,explorer,zone_explorer,m_explore_zone,"secondBestZoneSch","zone2GroupBoard");
		//+availableRole(4,explorer,soldier,m_create_zone,"bestZoneSch","zone1GroupBoard");
		+availableRole(4,explorer,map_explorer_helper,m_help_explore_map,"teamSch","marsGroupBoard");
		+availableRole(5,explorer,soldier,m_create_zone,"bestZoneSch","zone1GroupBoard");
		+availableRole(6,explorer,soldier,m_create_zone,"secondBestZoneSch","zone2GroupBoard");

		+availableRole(7,repairer,repairer,m_repair,"attackSch","attackGroupBoard");
		+availableRole(8,repairer,medic,m_occupy_center,"bestZoneSch","zone1GroupBoard");
		+availableRole(9,repairer,medic,m_occupy_center,"secondBestZoneSch","zone2GroupBoard");
		+availableRole(10,repairer,soldier,m_create_zone,"bestZoneSch","zone1GroupBoard");
		+availableRole(11,repairer,soldier,m_create_zone,"bestZoneSch","zone1GroupBoard");
		+availableRole(12,repairer,soldier,m_create_zone,"secondBestZoneSch","zone2GroupBoard");

		+availableRole(13,saboteur,saboteur,m_attack,"attackSch","attackGroupBoard");
		+availableRole(14,saboteur,guardian,m_defend_zone,"bestZoneSch","zone1GroupBoard");
		+availableRole(15,saboteur,guardian,m_defend_zone,"secondBestZoneSch","zone2GroupBoard");
		+availableRole(16,saboteur,soldier,m_create_zone,"bestZoneSch","zone1GroupBoard");

		+availableRole(17,sentinel,sentinel,m_sabotage,"attackSch","attackGroupBoard");
		+availableRole(18,sentinel,soldier,m_create_zone,"bestZoneSch","zone1GroupBoard");
		+availableRole(19,sentinel,soldier,m_create_zone,"bestZoneSch","zone1GroupBoard");
		+availableRole(20,sentinel,soldier,m_create_zone,"bestZoneSch","zone1GroupBoard");
		+availableRole(21,sentinel,soldier,m_create_zone,"secondBestZoneSch","zone2GroupBoard");
		+availableRole(22,sentinel,soldier,m_create_zone,"secondBestZoneSch","zone2GroupBoard");

		//+availableRole(23,inspector,inspector,m_inspect,"attackSch","attackGroupBoard");
		+availableRole(23,inspector,soldier,m_create_zone,"secondBestZoneSch","zone2GroupBoard");
		+availableRole(24,inspector,soldier,m_create_zone,"bestZoneSch","zone1GroupBoard");
		+availableRole(25,inspector,soldier,m_create_zone,"bestZoneSch","zone1GroupBoard");
		+availableRole(26,inspector,soldier,m_create_zone,"bestZoneSch","zone1GroupBoard");
		+availableRole(27,inspector,soldier,m_create_zone,"secondBestZoneSch","zone2GroupBoard");
		+availableRole(28,inspector,soldier,m_create_zone,"secondBestZoneSch","zone2GroupBoard").