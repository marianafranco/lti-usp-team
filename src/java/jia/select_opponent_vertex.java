package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Entity;
import model.graph.Graph;
import model.graph.Vertex;
import model.graph.VertexComparator;
import arch.MartianArch;
import arch.WorldModel;
import env.Percept;

/**
 * Returns the best opponent's vertex to occupy.
 * </p>
 *  Use: jia.select_opponent_vertex(-Pos); </br>
 *  Where: Pos is the opponent's position.
 * 
 * @author mafranko
 */
public class select_opponent_vertex extends DefaultInternalAction {

	private static final long serialVersionUID = 8122474421207101417L;

	private static final VertexComparator comparator = new VertexComparator();
	
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		// go to opponents best vertex
		List<Vertex> vertices = new ArrayList<Vertex>(graph.getVertices().values());
		// order by vertex value
		Collections.sort(vertices, comparator);
		
		for (Vertex v : vertices) {
			if (!v.getTeam().equals(WorldModel.myTeam)
					&& !v.getTeam().equals(Percept.TEAM_UNKNOWN)
					&& !v.getTeam().equals(Percept.TEAM_NONE)
					&& v.getValue() > 1
					&& !model.containsActiveOpponentSaboteurOnVertex(v)
					&& model.numOfActiveOpponentsAt(v) < 2) {
				String vertex = Percept.VERTEX_PREFIX + v.getId();
				return un.unifies(terms[0], ASSyntax.createString(vertex));
			}
		}
		
		// else go 'attack' the closer opponent (not saboteur)
		Entity closerOpponent = model.getCloserActiveOpponentNotSaboteur();
		if (null != closerOpponent) {
			Vertex vCloserOpponent = closerOpponent.getVertex();
			if (null != vCloserOpponent) {
				String vertex = Percept.VERTEX_PREFIX + vCloserOpponent.getId();
				return un.unifies(terms[0], ASSyntax.createString(vertex));
			}
		}
		// else go to the least visited vertex
		int nextMove = graph.returnLeastVisitedNeighbor(model.getMyVertex().getId());
		if (nextMove == -1) {
			return un.unifies(terms[0], ASSyntax.createString("none"));
		}
		String vertex = Percept.VERTEX_PREFIX + nextMove;
		return un.unifies(terms[0], ASSyntax.createString(vertex));
	}
}
