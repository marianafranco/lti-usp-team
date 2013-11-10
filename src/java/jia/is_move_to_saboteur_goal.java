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
 * Returns the next position to go considering the nearest saboteur, or false.
 * </p>
 * Use: jia.is_move_to_saboteur_goal(-NextPos);</br>
 * Where: NextPos is the next position to move.
 * 
 * @author mafranko
 */
public class is_move_to_saboteur_goal extends DefaultInternalAction {

	private static final long serialVersionUID = 1168992507523306792L;
	
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		
		Vertex saboteurPosition = model.getSaboteurPosition();
		if (null == saboteurPosition) {
			return false;
		}
		
		Graph graph = model.getGraph();
		int dist = graph.getDistance(myPosition, saboteurPosition);
		if (dist > 2 && dist < Graph.MAX_DIST) {
			int nextMove = graph.returnNextMove(myPosition.getId(), saboteurPosition.getId());
			if (nextMove != -1) {
				String vertex = Percept.VERTEX_PREFIX + nextMove;
				return un.unifies(terms[0], ASSyntax.createString(vertex));
			}
		} else if (dist <= 2) {
			return un.unifies(terms[0], ASSyntax.createString("none"));
		} else {
			return false;
		}
		return false;
	}
}
