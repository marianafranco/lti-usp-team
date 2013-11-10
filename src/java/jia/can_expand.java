package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;

/**
 * Returns true or false indicating if the agent can move to expand the zone or not.
 * </p>
 * Use: jia.can_expand_guardian;</br>
 *  
 * @author mafranko
 */
public class can_expand extends DefaultInternalAction {

	private static final long serialVersionUID = 2711797878816377722L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		
//		if (model.getAgentEntity().getRole().equals(Percept.ROLE_SABOTEUR)) {
//			if (model.hasActiveCoworkerOnVertex(myPosition.getId())) {
//				return true;
//			} else {
////				Set<Vertex> neighbors = myPosition.getNeighbors();
////				int count = 0;
////				for (Vertex neighbor : neighbors) {
////					if (model.hasActiveCoworkerOnVertex(neighbor.getId())
////							&& model.isFrontierVertex(neighbor)) {
////						count++;
////					}
////				}
////				if (count >= 2) {
////					return true;
////				} else {
////					return false;
////				}
//				return false;
//			}
//		} else {
//			if (!model.hasGreaterActiveCoworkerOnVertex(myPosition)) {
//				return false;	
////			} else if (!model.hasActiveCoworkerOnVertex(myPosition.getId())) {
////				Set<Vertex> neighbors = myPosition.getNeighbors();
////				int count = 0;
////				for (Vertex neighbor : neighbors) {
////					if (model.hasActiveCoworkerOnVertex(neighbor.getId())
////							&& model.isFrontierVertex(neighbor)) {
////						count++;
////					}
////				}
////				if (count >= 2) {
////					return true;
////				} else {
////					return false;
////				}
////			}
//			} else if (model.hasActiveCoworkerOnVertex(myPosition.getId())) {
//				return true;
//			}
//		}
////		return true;
		
		if (model.hasGreaterActiveSoldierOrMedicOnVertex(myPosition.getId())) {
			return true;
		}
		return false;
	}
}
