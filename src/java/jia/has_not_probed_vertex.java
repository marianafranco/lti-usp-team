package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Term;
import model.graph.Graph;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;
import env.Percept;

/**
 * Returns the not probed vertex in the team zone or false.
 * </p>
 * Use: jia.jia.has_not_probed_vertex(-V); </br>
 * Where: V is the not probed vertex inside the team zone.
 * 
 * @author mafranko
 */
public class has_not_probed_vertex extends DefaultInternalAction {

	private static final long serialVersionUID = 1168992507523306792L;
	
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();
		
//		Term step = terms[1];
//		System.out.println("is_on_team_zone - Agent: " + ts.getUserAgArch().getAgName() + ", Step: " + step.toString());
		
		Vertex myPosition = model.getMyVertex();
		
		Vertex v = graph.hasNotProbedVertex(myPosition);
		
		if (null == v) {
//			return un.unifies(terms[0], ASSyntax.createString("none"));
			return false;
		}
		
		String vertex = Percept.VERTEX_PREFIX + v.getId();
		return un.unifies(terms[0], ASSyntax.createString(vertex));
	}
}
