package org.rossedth.adaptive_fsm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Timer;

import org.jeasy.states.api.AbstractEvent;
import org.jeasy.states.api.FiniteStateMachine;
import org.jeasy.states.api.FiniteStateMachineException;
import org.jeasy.states.api.State;
import org.jeasy.states.api.Transition;
import org.jeasy.states.core.FiniteStateMachineBuilder;
import org.jeasy.states.core.TransitionBuilder;
import org.rossedth.adaptive_logic.Action;
import org.rossedth.adaptive_logic.Data;
import org.rossedth.adaptive_logic.Memory;
import org.rossedth.adaptive_logic.Plan;
import org.rossedth.adaptive_logic.Reasoner;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;

public class Reasoner_FSM extends Reasoner{

	public Reasoner_FSM(Memory mem) {
		super(mem);
	}
	
	public void process(Data data) {
		FSMData fsm_data=(FSMData)data;
		State s=fsm_data.getState();
		List<Plan> responses=new ArrayList<Plan>();;
		
		AbstractEvent e=fsm_data.getEvent();
		
		if(s.getName().equalsIgnoreCase("P") &&
			e.getName().equalsIgnoreCase("K")) {
			Plan plan=new Plan();
			List<Action> actions=new ArrayList<Action>();
			
			actions.add(new FSMAction("new", "state", "P1"));
			actions.add(new FSMAction("new", "transition", "P to P1", "P", "P1", "KEvent"));
			actions.add(new FSMAction("new", "transition", "P1 to Q", "P1", "Q", "ZEvent"));
			plan.setActions(actions);
			responses.add(plan);
			
		}
		if(e.getName().equalsIgnoreCase("TimeEvent")) {
			Plan plan=new Plan();
			List<Action> actions=new ArrayList<Action>();
			actions.add(new FSMAction("remove", "transition", "P to Q","P","Q","BEvent"));
			plan.setActions(actions);
			responses.add(plan);
		}
		setResponses(responses);
		sendResponses();
		
	}

}