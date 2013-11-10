package model.graph;

import java.util.HashSet;
import java.util.Set;

import env.Percept;

/**
 * Vertex class.
 * 
 * @author mafranko
 */
public class Vertex {

	// vertex's properties
	private int id;
	private String team = Percept.TEAM_UNKNOWN;
	private int value = 1;
	private boolean probed = false;
	private Set<Vertex> neighbors;

	private int visited = 0;	// indicates how many times this vertex was visited by the agent

	// attributes used on the distance and path algorithms
	private int distance = 0;
	private Vertex parent = null;

	public Vertex(int id, String team) {
		this.id = id;
		team = team.replaceAll("\"", "");
		this.team = team;
		neighbors = new HashSet<Vertex>();
	}

	public Vertex(int id) {
		this.id = id;
		neighbors = new HashSet<Vertex>();
	}

	public void addNeighbor(Vertex v) {
		neighbors.add(v);
	}

	public void addDistance(int value) {
		distance += value;
	}

	public void addVisited() {
		visited ++;
	}

	/* Getters and Setters */

	public int getId() {
		return id;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		team = team.replaceAll("\"", "");
		this.team = team;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Set<Vertex> getNeighbors() {
		return neighbors;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Vertex getParent() {
		return parent;
	}

	public void setParent(Vertex parent) {
		this.parent = parent;
	}

	public boolean isProbed() {
		return probed;
	}

	public void setProbed(boolean probed) {
		this.probed = probed;
	}

	public int getVisited() {
		return visited;
	}

	public void setVisited(int visited) {
		this.visited = visited;
	}

	@Override
	public boolean equals(Object object) {
		if (null == object) {
			return false;
		}
		if (object instanceof Vertex) {
			Vertex other = (Vertex) object;
			if (this.id == other.id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return "vertex(" + id + "," + team + "," + value + ")"; 
	}
}