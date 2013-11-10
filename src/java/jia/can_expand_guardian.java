package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;

/**
 * Returns true or false indicating if the guardian agent can move to expand the zone or not.
 * </p>
 * Use: jia.can_expand_guardian;</br>
 *  
 * @author mafranko
 */
public class can_expand_guardian extends DefaultInternalAction {

	private static final long serialVersionUID = 2711797878816377722L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();

		if (model.hasActiveSoldierOrMedicOnVertex(myPosition.getId())) {
			return true;
		} else if (model.hasGreaterActiveGuardianOnVertex(myPosition.getId())) {
			return true;
		}
		return false;
	}
}
