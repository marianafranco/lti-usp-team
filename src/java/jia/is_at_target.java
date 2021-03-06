package jia;

import env.Percept;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;

/**
 * Returns true or false indicating if the agent is the given position.
 * </p>
 * Use: jia.is_at_target(+P);
 * Where: P is the position.
 * 
 * @author mafranko
 */
public class is_at_target extends DefaultInternalAction {

	private static final long serialVersionUID = 1256025311061772548L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String vertex = ((StringTerm) terms[0]).getString();
		if (null == vertex) {
			vertex = ((Atom) terms[0]).getFunctor();
		}

		if (vertex.equals("none")) {
			return false;
		}

		vertex = vertex.replace(Percept.VERTEX_PREFIX, "");
		int target = Integer.parseInt(vertex);

		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Vertex myVertex = model.getMyVertex();

		return myVertex.getId() == target;
	}
	
}
