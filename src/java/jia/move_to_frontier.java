package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.List;

import model.graph.Graph;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;
import env.Percept;

/**
 * Retrieves the frontier position to move considering the given zone. 
 * </p>
 * Use: jia.jia.move_to_frontier(+Z,-Pos); </br>
 * Where: Z is zone and Pos is the frontier position.
 * 
 * @author mafranko
 */
public class move_to_frontier extends DefaultInternalAction {

	private static final long serialVersionUID = -5827900602735423794L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();
		Vertex myPosition = model.getMyVertex();
		
		if (!model.hasActiveMedicOnVertex(myPosition.getId())
				&& model.hasActiveSoldierOnVertex(myPosition.getId())
				&& !model.hasGreaterActiveSoldierOnVertex(myPosition)) {
			return un.unifies(terms[1], ASSyntax.createString("none"));
		}
		
		List<Term> zone = ((ListTerm) terms[0]).getAsList();
		List<Integer> zoneIds = new ArrayList<Integer>();
		for (Term term : zone) {
			int value = (int) ((NumberTerm) term).solve();
			zoneIds.add(value);
		}
		
		for (Integer id :zoneIds) {
			Vertex v = graph.getVertexById(id);
			if (null != v && !v.getTeam().equals(WorldModel.myTeam)) {
				String vertex = Percept.VERTEX_PREFIX + id;
				return un.unifies(terms[1], ASSyntax.createString(vertex));
			}
		}
		
		Vertex nextMove = graph.getNextPosToFrontier(myPosition);
		
		if (null == nextMove) {
			return un.unifies(terms[1], ASSyntax.createString("none"));
		}
		String vertex = Percept.VERTEX_PREFIX + nextMove.getId();
		return un.unifies(terms[1], ASSyntax.createString(vertex));
	}

}
