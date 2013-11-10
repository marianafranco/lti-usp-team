package arch;

import jason.RevisionFailedException;
import jason.asSemantics.ActionExec;
import jason.asSemantics.Intention;
import jason.asSemantics.Message;
import jason.asSyntax.Literal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

import c4jason.CAgentArch;
import cartago.Op;
import env.MarsEnv;
import env.Percept;

/**
 * Common architecture for the agents.
 * It extends the Cartago architecture to adds the EIS environment. 
 * 
 * @author mafranko
 */
public class MartianArch extends CAgentArch {

	/**
	 * The EIS environment.
	 */
	private MarsEnv env;

	/**
	 * The agent's world model.
	 */
	private WorldModel model;

	private Logger logger;
	
	private boolean no_named = true;

	public MartianArch() {
		super();
		logger = Logger.getLogger("MartianArch");
		env = MarsEnv.getInstance();
		String name = getAgName();
		if (!name.equals("no-named")) {
			no_named = false;
		}
		model = new WorldModel(getAgName());
	}

	/**
	 * Perceives the Cartago and the EIS environments.
	 * It gets the EIS perceptions and updates the world model. Only relevant percepts are
	 * leaved in the list of perception for the agent.
	 */
	@Override
    public List<Literal> perceive() {
        super.perceive();
        
        if (no_named) {
        	String name = getAgName();
        	if (!name.equals("no-named")) {
        		no_named =  false;
        		model.getAgentEntity().setName(name);
        	} else {
        		System.out.println("NO_NAMED!!!!!!");
        	}
        }
        
        List<Literal> eisPercepts = env.getPercepts(getAgName());
//        if (!eisPercepts.isEmpty()) {
//        	logger.info("[" + getAgName() + "] Percepts: " + eisPercepts);
//        }

        if (eisPercepts == null) { // full map!!
        	// perform default action
        	logger.info("Full map!! size > 900");
        	ActionExec actionFullMap = new ActionExec(Literal.parseLiteral("recharge"), new Intention());
        	act(actionFullMap, null);
    		return null;
        }
        
        boolean fullMap = false;
        boolean broadcast = true;
        int perceptSize = eisPercepts.size();
        if (perceptSize > 540) {	// fullMap
        	fullMap = true;
        	broadcast = false;
        	logger.info("Full map!! size = " + perceptSize);

        	if (perceptSize > 740) {
            	// perform default action
            	ActionExec actionFullMap = new ActionExec(Literal.parseLiteral("recharge"), new Intention());
            	act(actionFullMap, null);
        	}
        }

        // updates the world model with the EIS percepts
        if (fullMap) {
        	eisPercepts = model.updateFullMap(eisPercepts);
        } else {
        	eisPercepts = model.update(eisPercepts);
        }

        perceptSize = eisPercepts.size();
        if (perceptSize > 85 && !fullMap) {
        	broadcast = false;
        	logger.info("Full map broadcast!! size = " + perceptSize);

//        	// perform default action
//        	ActionExec actionFullMap = new ActionExec(Literal.parseLiteral("recharge"), new Intention());
//        	act(actionFullMap, null);
        }

        for (Literal percept : eisPercepts) {
        	try {
        		// broadcast only new percepts
        		String  p = percept.getFunctor();
        		if (p.equals(Percept.visibleEdge) || p.equals(Percept.visibleEntity)
        				|| p.equals(Percept.visibleVertex) || p.equals(Percept.probedVertex)
        				|| p.equals(Percept.surveyedEdge) || p.equals(Percept.inspectedEntity)) {
//        			if (null == getTS().getAg().getBB().contains(percept)) {
        			if (broadcast) {
        				Message m = new Message("tell", null, null, percept);
           			 	broadcast(m);
        			}
        		} else if (p.equals(Percept.position)) {
        			if (null == getTS().getAg().getBB().contains(percept)) {
//        				if (broadcast) {
        					Message m = new Message("tell", null, null,
            						Percept.coworkerPosition + "(" + getAgName() + "," +
            								percept.getTerm(0).toString() + ")");
            				broadcast(m);
//        				}
        				// add percept to the base
        				getTS().getAg().addBel(percept);
        			}
        		} else {
        			// TODO maybe not all percepts need to be added to the base
            		// add percept to the base
    				getTS().getAg().addBel(percept);
        		}
			} catch (RevisionFailedException e) {
				// e.printStackTrace();
				logger.warning("Error when adding percepts from eis to the belief base.");
			} catch (Exception e) {
				// e.printStackTrace();
				logger.warning("Error on perceive.");
			}
        }

        /*
		 * THE METHOD MUST RETURN NULL:
		 * since the percept semantics is different (event vs. state),
		 * all the the percepts from the env must be managed here, not by the BUF.
		 * 
		 * see CAgentArch.java
		 */
        return null;
    }

	/**
	 * Sends the action to the Cartago or EIS environments.
	 */
	@Override
	public void act(ActionExec actionExec, List<ActionExec> feedback) {
		String action = actionExec.getActionTerm().getFunctor();
		// EIS actions
		if (action.equals("skip") || action.equals("goto") || action.equals("probe")
				|| action.equals("survey") || action.equals("buy") || action.equals("recharge")
				|| action.equals("attack") || action.equals("repair") || action.equals("parry")
				|| action.equals("inspect")) {
			boolean result = env.executeAction(this.getAgName(), actionExec.getActionTerm());
			actionExec.setResult(result);
			if (result) {
				Op op = new Op(action);
				notifyActionSuccess(op, actionExec.getActionTerm(), actionExec);
			} else {
				notifyActionFailure(actionExec, null, "Failled to performe the action: " + action);
			}
		// Cartago actions
		} else {
			super.act(actionExec, feedback);
		}
	}

	/**
	 * Reads the agent's mailbox and updates the world model. Only relevant messages are
	 * leaved in the list of perception for the agent. 
	 */
	@Override
    public void checkMail() {
		super.checkMail();
		List<Literal> percepts = convertMessageQueueToLiteralList(getTS().getC().getMailBox());
		model.update(percepts);

		Iterator<Message> im = getTS().getC().getMailBox().iterator();
		while (im.hasNext()) {
			Message message  = im.next();
			Literal  percept = Literal.parseLiteral(message.getPropCont().toString());
			String  p = percept.getFunctor();

//            String  ms = message.getPropCont().toString();
//            logger.info("[" + getAgName() + "] receved mail: " + ms);

			if (p.equals(Percept.visibleEdge) || p.equals(Percept.visibleEntity)
    				|| p.equals(Percept.visibleVertex) || p.equals(Percept.probedVertex)
    				|| p.equals(Percept.surveyedEdge) || p.equals(Percept.saboteur)
    				|| p.equals(Percept.inspectedEntity) || p.startsWith(Percept.coworker)) {
				im.remove();	// removes the percept from the mailbox
			}
		}

	}

	private List<Literal> convertMessageQueueToLiteralList(Queue<Message> messages) {
		List<Literal> literals = new ArrayList<Literal>();
		for (Message message : messages) {
			Literal  p = Literal.parseLiteral(message.getPropCont().toString());
			literals.add(p);
		}
		return literals;
	}

	public WorldModel getModel() {
		return this.model;
	}
}
