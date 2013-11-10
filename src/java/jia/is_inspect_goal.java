package jia;

import java.util.List;

import env.Percept;

import model.Entity;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Term;

/**
 * Returns true or false to indicate if there are opponents to be inspected near my position.
 * </p>
 * Use: jia.is_inspect_goal;</br>
 * 
 * @author mafranko
 */
public class is_inspect_goal extends DefaultInternalAction {

	private static final long serialVersionUID = -7144570990733006566L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		if (null == myPosition) {
			return false;
		}
		List<Entity> opponents = model.getOpponentsOnVertex(myPosition.getId());
		if (!opponents.isEmpty()) {
			for (Entity opponent : opponents) {
				if (opponent.getRole().equals(Percept.ROLE_UNKNOWN)) {
					opponent.setVertex(new Vertex(-1));
					return un.unifies(terms[0], ASSyntax.createString(opponent.getName()));
				}
			}
		}
		for (Vertex neighbor : myPosition.getNeighbors()) {
			opponents = model.getOpponentsOnVertex(neighbor.getId());
			if (!opponents.isEmpty()) {
				for (Entity opponent : opponents) {
					if (opponent.getRole().equals(Percept.ROLE_UNKNOWN)) {
						opponent.setVertex(new Vertex(-1));
						return un.unifies(terms[0], ASSyntax.createString(opponent.getName()));
					}
				}
			}
		}
		return false;
	}
}
