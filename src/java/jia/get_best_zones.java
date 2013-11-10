package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import model.graph.Graph;
import model.graph.Vertex;
import model.graph.VertexComparator;
import arch.CoordinatorArch;
import arch.WorldModel;

/**
 * Retrieves the best and second best zones.
 * </p>
 * Use: jia.get_best_zones(-F,-S);</br>
 * Where: F is the best zone and -S is the second best zone.
 * 
 * @author mafranko
 */
public class get_best_zones extends DefaultInternalAction {

	private static final long serialVersionUID = 1029168629645434366L;
	
	private static final VertexComparator comparator = new VertexComparator();

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((CoordinatorArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();
		
		List<Set<Vertex>> bestZones =  graph.getBestZones();
		if (!bestZones.isEmpty()) {
			List<Vertex> bestZone = new ArrayList<Vertex>(bestZones.get(0));
			Collections.sort(bestZone, comparator);
			ListTerm bestZoneIDs = retrieveVertexIDs(bestZone);
			
			ListTerm secondBestZoneIDs = bestZoneIDs;
			if (bestZones.size() == 2) {
				List<Vertex> secondBestZone = new ArrayList<Vertex>(bestZones.get(1));
				Collections.sort(secondBestZone, comparator);
				secondBestZoneIDs = retrieveVertexIDs(secondBestZone);
			}
			
			return un.unifies(terms[0], bestZoneIDs) & un.unifies(terms[1], secondBestZoneIDs);
		} else {
			return un.unifies(terms[0], new ListTermImpl()) & un.unifies(terms[1], new ListTermImpl());
		}
	}
	
	private ListTerm retrieveVertexIDs(List<Vertex> list) {
		ListTerm ids = new ListTermImpl();
		for (Vertex v : list) {
			//ids.add(ASSyntax.createString(Percept.VERTEX_PREFIX + v.getId()));
			ids.add(ASSyntax.createNumber(v.getId()));
		}
		return ids;
	}
}
