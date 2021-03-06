package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import arch.MartianArch;
import arch.WorldModel;

/**
 * Changes the agent's status.
 * </p>
 * Use: jia.set_my_status(+S); </br>
 * Where: S is the new agent's status.
 * 
 * @author mafranko
 */
public class set_my_status extends DefaultInternalAction {

	private static final long serialVersionUID = 1323569319099989862L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String status = ((Atom) terms[0]).getFunctor();
		if (null == status) {
			status = ((StringTerm) terms[0]).getString();
		}
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		model.setAgentStatus(status);
		return true;
	}

}
