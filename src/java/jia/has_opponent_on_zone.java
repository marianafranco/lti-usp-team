package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.List;

import arch.MartianArch;
import arch.WorldModel;

/**
 * Returns true or false indicating if exists an opponent in the zone.
 * </p>
 * Use: jia.has_opponent_on_zone;</br>
 * 
 * @author mafranko
 */
public class has_opponent_on_zone extends DefaultInternalAction {

	private static final long serialVersionUID = 1182494310168253005L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		
		List<Term> zone = ((ListTerm) terms[0]).getAsList();
		
		for (Term term : zone) {
			int value = (int) ((NumberTerm) term).solve();
			if (model.hasActiveOpponentOnVertex(value)) {
				return true;
			}
		}
		
		return false;
	}
}
