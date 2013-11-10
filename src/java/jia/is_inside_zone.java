package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.Set;

import model.Entity;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;

/**
 * Returns true or false indicating if the agents is inside the zone or not.
 * </p>
 * Use: jia.is_inside_zone;</br>
 * 
 * @author mafranko
 */
public class is_inside_zone extends DefaultInternalAction {

	private static final long serialVersionUID = 1168992507523306792L;
	
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		
		Set<Vertex> neighbors = myPosition.getNeighbors();
		for (Vertex neighbor : neighbors) {
			if (!neighbor.getTeam().equals(WorldModel.myTeam)) {
				return false;
			}
		}
		// if inside check if coworker neighbors will stay in frontier
		for (Vertex neighbor : neighbors) {
			for (Vertex neneighbor : neighbor.getNeighbors()) {
				Entity coworker = model.getActiveCoworkerOnVertex(neneighbor.getId());
				if (null != coworker) {
					if ((coworker.getMission().startsWith("m_create_zone")
							|| coworker.getMission().startsWith("m_explore_zone")
							|| coworker.getMission().startsWith("m_defend_zone"))
							&& isOnFrontier(coworker)
							&& !model.hasActiveCoworkersOnNeighborsNotMe(coworker)) {
						return false;
					}
				}
			}
			
		}
//		if (!model.hasGreaterActiveCoworkerOnVertex(myPosition)) {
//			return true;
//		}
		return true;
	}
	
	
	private boolean isOnFrontier(Entity ag){
		Set<Vertex> neighbors = ag.getVertex().getNeighbors();
		for (Vertex neighbor : neighbors) {
			if (!neighbor.getTeam().equals(WorldModel.myTeam)) {
				return true;
			}
		}
		return false;
	}
}
