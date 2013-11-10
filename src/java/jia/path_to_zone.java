package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.List;

import model.graph.Graph;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;
import env.Percept;

/**
 * Retrieves the path to the given zone.
 * </p>
 * Use: jia.jia.path_to_zone(+Z,-P); </br>
 * Where: Z is the zone and P is path (list of vertices).
 * 
 * @author mafranko 
 */
public class path_to_zone extends DefaultInternalAction {

	private static final long serialVersionUID = -6710390609224328605L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		
		
		List<Term> zone = ((ListTerm) terms[0]).getAsList();
		
		List<Integer> zoneIds = new ArrayList<Integer>();
		for (Term term : zone) {
			int value = (int) ((NumberTerm) term).solve();
			zoneIds.add(value);
		}
		
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();
		
		Vertex myPosition = model.getMyVertex();
		
		if (zoneIds.contains(myPosition.getId())) {
			ListTerm myPosId = new ListTermImpl();
			myPosId.add(ASSyntax.createString(Percept.VERTEX_PREFIX + myPosition.getId()));
			return un.unifies(terms[1], myPosId);
		}

		List<Vertex> path = graph.getPathToZone(myPosition, zoneIds);
		if (path != null) {
			ListTerm pathIds = retrieveVertexIDs(path);
//			System.out.println("Path from v" + myPosition.getId() + " to zone " + zoneIds.toString() + " is " + pathIds.getAsList());
			return un.unifies(terms[1], pathIds);
		}
		
//		System.out.println("Could not find a path from v" + myPosition.getId() + " to zone " + zoneIds.toString());

		// go to the least visited vertex
		int nextMove = graph.returnLeastVisitedNeighbor(myPosition.getId());
		ListTerm nextMoveList = new ListTermImpl();
		if (nextMove != -1) {
			nextMoveList.add(ASSyntax.createString(Percept.VERTEX_PREFIX + nextMove));
		}
		return un.unifies(terms[1], nextMoveList);
	}
	
	private ListTerm retrieveVertexIDs(List<Vertex> list) {
		ListTerm ids = new ListTermImpl();
		for (Vertex v : list) {
			//ids.add(ASSyntax.createString(Percept.VERTEX_PREFIX + v.getId()));
			ids.add(ASSyntax.createString(Percept.VERTEX_PREFIX + v.getId()));
		}
		return ids;
	}
	
}
