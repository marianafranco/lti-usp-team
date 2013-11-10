package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.List;

import env.Percept;

import model.Entity;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;

/**
 * Retrieves the name of the opponent to attack which is in the same position as me.
 * </p>
 * Use: jia.get_opponent_to_attack(-A); </br>
 * Where: A is the name of the opponent in the same position as me.
 * 
 * @author mafranko
 */
public class get_opponent_to_attack extends DefaultInternalAction {

	private static final long serialVersionUID = 2815473594430522520L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		List<Entity> opponents = model.getActiveOpponentsOnVertex(myPosition.getId());
		
		if (!opponents.isEmpty()) {
			for (Entity opponent : opponents) {
				if (opponent.getRole().equals(Percept.ROLE_SABOTEUR)) {
					opponent.setVertex(new Vertex(-1));
					return un.unifies(terms[1], ASSyntax.createString(opponent.getName()));
				}
			}
			for (Entity opponent : opponents) {
				if (opponent.getRole().equals(Percept.ROLE_REPAIRER)) {
					opponent.setVertex(new Vertex(-1));
					return un.unifies(terms[1], ASSyntax.createString(opponent.getName()));
				}
			}
			opponents.get(0).setVertex(new Vertex(-1));
			return un.unifies(terms[1], ASSyntax.createString(opponents.get(0).getName()));
			
		} else {
			int visibility = 0;
			String vis = ((Atom) terms[0]).getFunctor();
			if (null == vis) {
				visibility = (int) ((NumberTerm) terms[0]).solve();
			} else {
				visibility = Integer.parseInt(vis);
			}
			if (visibility > 1) {
				for (Vertex neighbor : myPosition.getNeighbors()) {
					Entity saboteur = model.getActiveSaboteurOpponentOnVertex(neighbor.getId());
					if (null != saboteur) {
						saboteur.setVertex(new Vertex(-1));
						return un.unifies(terms[1], ASSyntax.createString(saboteur.getName()));
					}
				}
			}
		}
		return un.unifies(terms[1], ASSyntax.createString("none"));
	}
}
