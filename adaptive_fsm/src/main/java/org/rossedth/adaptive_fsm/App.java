package org.rossedth.adaptive_fsm;

import java.io.IOException;
import java.util.Scanner;

import org.jeasy.states.api.FiniteStateMachineException;
import org.rossedth.adaptable_fsm.GraphViz;
import org.rossedth.adaptable_fsm.RecognizerFSM;
import org.rossedth.adaptive_logic.AdaptiveLogic;
import org.rossedth.adaptive_logic.Executor;
import org.rossedth.adaptive_logic.Memory;
import org.rossedth.adaptive_logic.Monitor;
import org.rossedth.adaptive_logic.Reasoner;
import org.rossedth.adaptive_logic.Selector;


/**
 * 
 * This tutorial is an implementation as proof of concept of the Holistic Model for adaptivity developed as part of a PhD project at EPUSP
 * @author Rosalia Edith Caya Carhuanina (rosalia.caya@usp.br)
 * 
 */

class Launcher {

	public static void main(String[] args) throws FiniteStateMachineException, IOException {


		/*
		 * Create a RecognizerFSM instance
		 */
		RecognizerFSM recognizer=new RecognizerFSM();
		
		/*
		 * Setup GraphViz
		 */
	
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.setup_graph("GraphViz.config");

        /*
         * Create a AdaptiveLogic instance
         */

    	AdaptiveLogic AL=new AdaptiveLogic();
    	createAdaptiveLogic(AL,recognizer);
    	AL.init();
 
    	
    	/*
		 * Fire some events and print FSM state
		 */


		recognizer.printCurrentState(gv);

		Scanner scanner = new Scanner(System.in);
		System.out.println("Insert an input or Press [q] to quit tutorial.");
		System.out.println("=================================================");

		while (true) {
			String input = scanner.nextLine();

			recognizer.processInput(input);
			recognizer.printCurrentState(gv);

			if (recognizer.atFinalState()) {
				System.out.println("Recognizer has reach final state ");   
				System.exit(0);
				scanner.close();                
			}

			if (input.trim().equalsIgnoreCase("q")) {
				System.out.println("input = " + input.trim());
				System.out.println("Bye!");
				System.exit(0);
				scanner.close();
			}         

		}

	} 
    
    
    public static void createAdaptiveLogic(final AdaptiveLogic AL,RecognizerFSM sys_U) {
    	Memory mem= new Memory();
    	Monitor mon=new Monitor_FSM(mem);
    	Reasoner rea=new Reasoner_FSM(mem);  	
    	Selector sel=new Selector_FSM(mem); 
    	Executor ex=new Executor_FSM(mem);
    	
    	AL.loadAdaptiveComponents(mem, mon, rea, sel, ex);   	
    	AL.connect(sys_U);
    }
    
}