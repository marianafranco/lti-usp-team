package model.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import arch.WorldModel;
import env.Percept;

/**
 * Graph class.
 * 
 * @author mafranko
 */
public class Graph {

	/**
	 * Map <id, Vertex>. Id is the vertex number.
	 */
	private HashMap<Integer, Vertex> vertices;

	/**
	 * Map <Edge, value>. Value is the weight of the edge. 
	 */
	private HashMap<Edge, Integer> edges;

	private int maxNumOfVertices = Integer.MAX_VALUE;
	private int maxNumOfEdges = Integer.MAX_VALUE;

	/**
	 * List that keeps the information about the best zones in the graph.
	 */
	private List<Set<Vertex>> bestZones;

	WorldModel model;
	
	/**
	 * Indicates if the graph was update or not.
	 */
	private boolean updated = false;

	public static final int MAX_DIST = 10;  

	public Graph(WorldModel model) {
		vertices = new HashMap<Integer, Vertex>();
		edges = new HashMap<Edge, Integer>();
		bestZones = new ArrayList<Set<Vertex>>();
		this.model = model;
	}

	public boolean existsPath(int v1, int v2) {
		Vertex vertex1 = vertices.get(v1);
		Vertex vertex2 = vertices.get(v2);
		return existsPath(vertex1, vertex2);
	}

	public boolean existsPath(Vertex v1, Vertex v2) {
		// uses breadth-first search
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		frontier.add(v1);
		while (true) {
			if (frontier.isEmpty()) {
				return false;	// failure, could not find a path
			}
			Vertex v = frontier.poll();
			if (v.equals(v2)) {
				return true;
			}
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)) {
					if (neighbor.equals(v2)) {
						return true;
					}
					frontier.add(neighbor);
				}
			}
		}
	}

	public List<Vertex> getPathToZone(Vertex v1, List<Integer> zoneIds) {
		// uses breadth-first search
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		v1.setParent(null);
		frontier.add(v1);
		while (true) {
			if (frontier.isEmpty()) {
				return null;	// failure, could not find a path
			}
			Vertex v = frontier.poll();
			if (zoneIds.contains(v.getId())) {
				List<Vertex> result = new ArrayList<Vertex>();
				result.add(v);
				return result;
			}
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)) {
					neighbor.setParent(v);
					if (zoneIds.contains(neighbor.getId())) {
						List<Vertex> path = getPath(neighbor);
						return path;
					}
					frontier.add(neighbor);
				}
			}
		}
	}
	
	private List<Vertex> getPath(Vertex v) {
		List<Vertex> path = new ArrayList<Vertex>();
		path.add(v);
		Vertex parent = v.getParent();
		while (null != parent.getParent()) {
			path.add(parent);
			parent = parent.getParent();
		}
		
		// inverse list
		List<Vertex> invPath = new ArrayList<Vertex>(path.size());
		for (int i = path.size() - 1; i >= 0; i--) {
			invPath.add(path.get(i));
		}
		return invPath;
	}
	
	public Vertex getNextPosToFrontier(Vertex pos) {
		// uses breadth-first search
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		pos.setParent(null);
		pos.setDistance(0);
		frontier.add(pos);
		while (true) {
			if (frontier.isEmpty()) {
				return null;	// failure, could not find a path
			}
			Vertex v = frontier.poll();
			if (!v.getTeam().equals(WorldModel.myTeam)
//					&& !model.hasActiveCoworkerOnVertex(v.getId())
					&& !model.hasActiveSaboteurOpponentOnVertex(v)) {
				return nextMove(v);
			}
			if (v.getDistance() > MAX_DIST) {
				return null;
			}
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)) {
					neighbor.setParent(v);
					if (!neighbor.getTeam().equals(WorldModel.myTeam)
							&& !model.hasActiveCoworkerOnVertex(neighbor.getId())
							&& !model.hasActiveSaboteurOpponentOnVertex(neighbor)) {
						Vertex nextMove = nextMove(neighbor);
						return nextMove;
					}
					neighbor.setDistance(v.getDistance() + 1);
					frontier.add(neighbor);
				}
			}
		}
	}
	
	
	public Vertex getNextPosToOpponentOnZone(Vertex pos) {
		// uses breadth-first search
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		pos.setParent(null);
		pos.setDistance(0);
		frontier.add(pos);
		while (true) {
			if (frontier.isEmpty()) {
				return null;	// failure, could not find a path
			}
			Vertex v = frontier.poll();
			if (model.hasActiveOpponentOnVertex(v)) {
				return nextMove(v);
			}
			if (v.getDistance() > MAX_DIST) {
				return null;
			}
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)) {
					neighbor.setParent(v);
					if (model.hasActiveOpponentOnVertex(neighbor)) {
						Vertex nextMove = nextMove(neighbor);
						return nextMove;
					}
					if (neighbor.getTeam().equals(WorldModel.myTeam)) {
						neighbor.setDistance(v.getDistance() + 1);
						frontier.add(neighbor);
					}
				}
			}
		}
	}

	public int getDistance(Vertex vertex1, Vertex vertex2) {
		if (vertex1.equals(vertex2)) {
			return 0;
		}
		// uses breadth-first search
		vertex1.setDistance(0);
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		frontier.add(vertex1);
		while (true) {
			if (frontier.isEmpty()) {
				return Integer.MAX_VALUE;	// failure, could not find a path
			}
			Vertex v = frontier.poll();
			if (v.getDistance() > MAX_DIST) {
				return Integer.MAX_VALUE;	// failure, could not find a path
			}
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)) {
					if (neighbor.equals(vertex2)) {
						return v.getDistance() + 1;
					}
					neighbor.setDistance(v.getDistance() + 1);
					frontier.add(neighbor);
				}
			}
		}
	}
	
	public boolean isOnZone(Vertex pos, List<Integer> zone, boolean occupiedZone) {
		if (zone.contains(pos.getId())) {
			return true;
		}
		
		// uses breadth-first search
		pos.setDistance(0);
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		frontier.add(pos);
		while (true) {
			if (frontier.isEmpty()) {
				return false;	// failure, could not find a path
			}
			Vertex v = frontier.poll();
			if (v.getDistance() > 5) {	// depth limit
				return occupiedZone;
			}
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)
						&& neighbor.getTeam().equals(WorldModel.myTeam)) {
					if (zone.contains(neighbor.getId())) {
//						System.out.println("IN ZONE!! POS:" + pos.getId() + " ZONE:" + neighbor.getId() + " DIST:" + v.getDistance());
						return true;
					}
					neighbor.setDistance(v.getDistance() + 1);
					frontier.add(neighbor);
				}
			}
		}
	}

	public Vertex hasNotProbedVertex(Vertex pos) {
		if (!pos.isProbed()) {
			return pos;
		}
		
		// uses breadth-first search
		pos.setDistance(0);
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		frontier.add(pos);
		while (true) {
			if (frontier.isEmpty()) {
				return null;	// failure, could not find a path
			}
			Vertex v = frontier.poll();
			if (v.getDistance() > MAX_DIST) {	// depth limit
				return null;
			}
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)
						&& neighbor.getTeam().equals(WorldModel.myTeam)) {
					if (!neighbor.isProbed()) {
//						System.out.println("IN ZONE!! POS:" + pos.getId() + " ZONE:" + neighbor.getId() + " DIST:" + v.getDistance());
						return neighbor;
					}
					neighbor.setDistance(v.getDistance() + 1);
					frontier.add(neighbor);
				}
			}
		}
	}

	public int returnLeastVisitedNeighbor(int v1) {
		Vertex vertex1 = vertices.get(v1);
		List<Vertex> neighbors = new ArrayList<Vertex>(vertex1.getNeighbors());

		if (neighbors.isEmpty()) {
			return -1;
		}

		Collections.shuffle(neighbors);
		Vertex leastVisited = neighbors.remove(0);
		int minValue = leastVisited.getVisited();
		for (Vertex neighbor : neighbors) {
			if (neighbor.getVisited() < minValue) {
				leastVisited = neighbor;
			}
		}
		return leastVisited.getId();
	}

	public List<Vertex> returnNotProbedNeighbors(Vertex vertex1) {
		List<Vertex> notProbedNeighbors = new ArrayList<Vertex>();
		List<Vertex> neighbors = new ArrayList<Vertex>(vertex1.getNeighbors());

		for (Vertex neighbor : neighbors) {
			if (!neighbor.isProbed()) {
				notProbedNeighbors.add(neighbor);
			}
		}
		return notProbedNeighbors;
	}

	public List<Vertex> returnTeamNotProbedNeighbors(Vertex vertex1) {
		List<Vertex> notProbedNeighbors = new ArrayList<Vertex>();
		List<Vertex> neighbors = new ArrayList<Vertex>(vertex1.getNeighbors());

		for (Vertex neighbor : neighbors) {
			if (!neighbor.isProbed() && neighbor.getTeam().equals(WorldModel.myTeam)) {
				notProbedNeighbors.add(neighbor);
			}
		}
		return notProbedNeighbors;
	}

	public List<Vertex> returnTeamNotProbedVertices() {
		List<Vertex> notProbed = new ArrayList<Vertex>();
		for (Vertex v : vertices.values()) {
			if (!v.isProbed() && v.getTeam().equals(WorldModel.myTeam)) {
				notProbed.add(v);
			}
		}
		return notProbed;
	}

	public int returnNextMove(int v1, int v2) {
		Vertex vertex1 = vertices.get(v1);
		vertex1.setParent(null);
		Vertex vertex2 = vertices.get(v2);
		// uses breadth-first search
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		frontier.add(vertex1);
		while (true) {
			if (frontier.isEmpty()) {
				return -1;	// failure, could not find a path
			}
			Vertex v = frontier.poll();
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)) {
					neighbor.setParent(v);
					if (neighbor.equals(vertex2)) {
						Vertex nextVertex = nextMove(neighbor);
						return nextVertex.getId();
					}
					frontier.add(neighbor);
				}
			}
		}
	}

	private Vertex nextMove(Vertex v) {
		Vertex end = v;
		Vertex parent = v.getParent();
		if (null == parent) {
			return v;
		}
		while (null != parent.getParent()) {
			end = parent;
			parent = end.getParent();
		}
		return end;
	}

	public List<Set<Vertex>> getBestZones() {
		if (!updated) {
			return bestZones;
		}

		List<Set<Vertex>> newBestZones = new ArrayList<Set<Vertex>>();

		List<Vertex> verticesList = new ArrayList<Vertex>(vertices.values());
		VertexComparator comparator = new VertexComparator();
		Collections.sort(verticesList, comparator);

		// best zone
		int maxZoneValue = 0;
		Set<Vertex> bestZone = null;
		for (Vertex v : verticesList) {
			Set<Vertex> zone = new HashSet<Vertex>();
			zone.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			zone.addAll(neighbors);
			Set<Vertex> zoneMoreNeighbors = new HashSet<Vertex>(zone);
			for (Vertex neighbor : neighbors) {
				zoneMoreNeighbors.addAll(neighbor.getNeighbors());
			}
			int zoneValue = countZoneValue(zoneMoreNeighbors);
			if (zoneValue > maxZoneValue && zoneValue > 10) {
				maxZoneValue = zoneValue;
				bestZone = zone;
			}
		}
		if (bestZone != null) {
			newBestZones.add(bestZone);
		} else {
			return bestZones;
		}

		// second best zone
		maxZoneValue = 0;
		Set<Vertex> secondBestZone = null;
		verticesList.removeAll(bestZone);
		for (Vertex v : verticesList) {
			Set<Vertex> zone = new HashSet<Vertex>();
			zone.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			zone.addAll(neighbors);
			Set<Vertex> zoneMoreNeighbors = new HashSet<Vertex>(zone);
			for (Vertex neighbor : neighbors) {
				zoneMoreNeighbors.addAll(neighbor.getNeighbors());
			}
			int zoneValue = countZoneValue(zoneMoreNeighbors);
			if (zoneValue > maxZoneValue && zoneValue > 20
					&& !hasAtLeastOneVertexOnZone(zone, bestZone)) {
				maxZoneValue = zoneValue;
				secondBestZone = zone;
			}
		}

		if (secondBestZone != null) {
			newBestZones.add(secondBestZone);
		}

//		bestZones = newBestZones;
		
		if (bestZones.size() < 2 || newBestZones.size() < 2) {
			bestZones = newBestZones;
		} else {
			if (hasAtLeastOneVertexOnZone(bestZones.get(0), newBestZones.get(0))) {
				bestZones = newBestZones;
			} else if (hasAtLeastOneVertexOnZone(bestZones.get(0), newBestZones.get(1))) {
				bestZones.set(0, newBestZones.get(1));
				bestZones.set(1, newBestZones.get(0));
			} else {
				bestZones = newBestZones;
			}
		}
		updated = false;
		return bestZones;
	}

	public boolean hasAtLeastOneVertexOnZone(Set<Vertex> zone, Set<Vertex> vertices) {
		for (Vertex v : vertices) {
			if (zone.contains(v)) {
				return true;
			}
		}
		return false;
	}

	private int countZoneValue(Set<Vertex> zone) {
		int totalValue = 0;
		for (Vertex v : zone) {
			totalValue += v.getValue();
		}
		return totalValue;
	}
	
	public void addVertex(int id, String team) {
		Vertex v = vertices.get(id);
		if (null == v) {
			vertices.put(id, new Vertex(id, team));
			updated = true;
		} else {
			v.setTeam(team);
		}
	}

	public void addVertex(Vertex v) {
		Vertex vertex = vertices.get(v.getId());
		if (null == vertex) {
			vertices.put(v.getId(), v);
			updated = true;
		} else {
			vertex.setTeam(v.getTeam());
		}
	}

	public void addEdge(int v1, int v2) {
		edges.put(new Edge(v1, v2), -1);
		Vertex vertex1 = vertices.get(v1);
		if (null == vertex1) {
			vertex1 = new Vertex(v1, Percept.TEAM_UNKNOWN);
			addVertex(vertex1);
		}
		Vertex vertex2 = vertices.get(v2);
		if (null == vertex2) {
			vertex2 = new Vertex(v2, Percept.TEAM_UNKNOWN);
			addVertex(vertex2);
		}
		vertex1.addNeighbor(vertex2);
		vertex2.addNeighbor(vertex1);
	}

	public boolean containsVertex(int id, String team) {
		if (vertices.containsKey(id)) {
			Vertex v = vertices.get(id);
			if (v.getTeam().equals(team)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsEdge(int v1, int v2) {
		return edges.containsKey(new Edge(v1, v2));
	}

	public int getVertexValue(int id) {
		if (vertices.containsKey(id)) {
			Vertex v = vertices.get(id);
			return v.getValue();
		}
		return -1;
	}
	
	public Vertex getVertexById(int id) {
		if (vertices.containsKey(id)) {
			Vertex v = vertices.get(id);
			return v;
		}
		return null;
	}

	public void addVertexValue(int id, int value) {
		if (vertices.containsKey(id)) {
			Vertex v = vertices.get(id);
			v.setProbed(true);
			v.setValue(value);
		} else {
			Vertex v = new Vertex(id, Percept.TEAM_UNKNOWN);
			v.setValue(value);
			v.setProbed(true);
			vertices.put(id, v);
		}
		updated = true;
	}

	public int getEdgeValue(int v1, int v2) {
		Edge e = new Edge(v1, v2);
		if (edges.containsKey(e)) {
			return edges.get(e);
		}
		return -1;
	}

	public void addEdgeValue(int v1, int v2, int value) {
		Edge e = new Edge(v1, v2);
		edges.put(e, value);
		Vertex vertex1 = vertices.get(v1);
		if (null == vertex1) {
			vertex1 = new Vertex(v1, Percept.TEAM_UNKNOWN);
			addVertex(vertex1);
		}
		Vertex vertex2 = vertices.get(v2);
		if (null == vertex2) {
			vertex2 = new Vertex(v2, Percept.TEAM_UNKNOWN);
			addVertex(vertex2);
		}
		vertex1.addNeighbor(vertex2);
		vertex2.addNeighbor(vertex1);
	}

	public boolean isProbedVertex(int id) {
		Vertex v = vertices.get(id);
		if (null == v) {
			return false;
		}
		return v.isProbed();
	}

	/* Getters and Setters */

	public HashMap<Integer, Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(HashMap<Integer, Vertex> vertices) {
		this.vertices = vertices;
	}

	public HashMap<Edge, Integer> getEdges() {
		return edges;
	}

	public void setEdges(HashMap<Edge, Integer> edges) {
		this.edges = edges;
	}

	public int getMaxNumOfVertices() {
		return maxNumOfVertices;
	}

	public void setMaxNumOfVertices(int maxNumOfVertexs) {
		this.maxNumOfVertices = maxNumOfVertexs;
	}

	public int getMaxNumOfEdges() {
		return maxNumOfEdges;
	}

	public void setMaxNumOfEdges(int maxNumOfEdges) {
		this.maxNumOfEdges = maxNumOfEdges;
	}
}
