package jia;

import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

/**
 * Returns true or false indicating if exists an opponent in the same vertex as me.
 * </p>
 * Use: jia.has_opponent_on_vertex;</br>
 * 
 * @author mafranko
 */
public class has_opponent_on_vertex extends DefaultInternalAction {

	private static final long serialVersionUID = 1182494310168253005L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		if (model.hasActiveOpponentOnVertex(myPosition)) {
			return true;
		}
		
		int visibility = 0;
		String vis = ((Atom) terms[0]).getFunctor();
		if (null == vis) {
			visibility = (int) ((NumberTerm) terms[0]).solve();
		} else {
			visibility = Integer.parseInt(vis);
		}
		
		if (visibility > 1) {
			for (Vertex neighbor : myPosition.getNeighbors()) {
				if (model.hasActiveSaboteurOpponentOnVertex(neighbor)) {
					return true;
				}
			}
		}
		return false;
	}
}
