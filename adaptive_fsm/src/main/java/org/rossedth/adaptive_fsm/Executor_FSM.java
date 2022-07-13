package org.rossedth.adaptive_fsm;

import java.util.Set;

import org.jeasy.states.api.FiniteStateMachine;
import org.jeasy.states.api.FiniteStateMachineException;
import org.jeasy.states.api.State;
import org.jeasy.states.api.Transition;
import org.jeasy.states.core.TransitionBuilder;
import org.rossedth.adaptive_logic.Executor;
import org.rossedth.adaptive_logic.Memory;
import org.rossedth.adaptive_logic.Plan;

public class Executor_FSM extends Executor {

	public Executor_FSM(Memory mem) {
		super(mem);
	}

	public void command(Plan response) {
		System.out.println("Commanding Actions from Executor"); 
		FiniteStateMachine sys= (FiniteStateMachine)((RecognizerFSM)this.getSysU()).getFSM();
		Transition transition = sys.getLastTransition();
		if (transition!=null) {
			System.out.println(transition.toString());
		}
		else {
			System.out.println("There was no transition made");
		}

		if (response!=null) {
			System.out.println("Executor needs to translate the plan and modify the FSM");	
			FSMAction action=(FSMAction)response.getActions().get(0);
			if(action.getType().equalsIgnoreCase("remove")) {
				System.out.println("Executor needs to remove a transition");			
				Set<Transition> transitions= sys.getTransitions();
				for (Transition t: transitions) {
					//looking for transition "P to Q with B"
			    	if (t.getSourceState().getName().equalsIgnoreCase("P")&&
			            (t.getEventType()==BEvent.class)){
						transitions.remove(t);
			            break;
			    	} 
			    }
				
				

				System.out.println("STATES ");
				for(State s:sys.getStates()) {
					System.out.println("State : " + s.getName());			
				}
				System.out.println("TRANSITIONS ");
				sys=(FiniteStateMachine)((RecognizerFSM)this.getSysU()).getFSM();
				for(Transition t:sys.getTransitions()) {
					System.out.println("Transition : " + t.getName());			
				}
				System.out.println("=================================================");

			}
			else if(action.getType().equalsIgnoreCase("new")) {
				State p1_State = new State("P1");

				Transition p_p1 = new TransitionBuilder()
						.name("P to P1 with k")
						.sourceState(sys.getCurrentState())
						.eventType(NNEvent.class)
						.targetState(p1_State)
						.build();

				Set<State> states=sys.getStates();

				State q_State=null;
				for(State s:states) {
					if (s.getName().equalsIgnoreCase("Q")) {
						q_State=s;
					}
				}

				Transition p1_q = new TransitionBuilder()
						.name("P1 to Q with z")
						.sourceState(p1_State)
						.eventType(NNEvent.class)
						.targetState(q_State)
						.build();

				sys.getStates().add(p1_State);
				Set<Transition> transitions= sys.getTransitions();
				transitions.add(p_p1);
				transitions.add(p1_q);
				
				((RecognizerFSM)this.getSysU()).addFSMAcceptedEntry("K");
				((RecognizerFSM)this.getSysU()).addFSMAcceptedEntry("Z");
				int delay=5000;
				((RecognizerFSM)this.getSysU()).getListener().onTimer(delay);

				System.out.println("STATES ");
				for(State s:sys.getStates()) {
					System.out.println("State : " + s.getName());			
				}
				System.out.println("TRANSITIONS ");
				for(Transition t:sys.getTransitions()) {
					System.out.println("Transition : " + t.getName());			
				}
				System.out.println("=================================================");

				try {
					sys.fire(new NNEvent("KEvent"));
				} catch (FiniteStateMachineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Current state "+ sys.getCurrentState().getName());   
			}
			
  

		}



	}

}
