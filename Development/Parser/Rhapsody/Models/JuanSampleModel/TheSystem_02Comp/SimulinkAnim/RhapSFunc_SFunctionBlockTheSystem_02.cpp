/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright IBM Corporation 2008, 2017. All Rights Reserved.
 *
 * Note to U.S. Government Users Restricted Rights:  
 * Use, duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 *******************************************************************************/
/*
 * sfuntmpl_basic.c: Basic 'C' template for a level 2 S-function.
 *
 *  -------------------------------------------------------------------------
 *  | See matlabroot/simulink/src/sfuntmpl_doc.c for a more detailed template |
 *  -------------------------------------------------------------------------
 *
 * Copyright 1990-2002 The MathWorks, Inc.
 * $Revision: 1.1 $
 */


/*
 * You must specify the S_FUNCTION_NAME as the name of your S-function
 * (i.e. replace sfuntmpl_basic with the name of your S-function).
 */


#define S_FUNCTION_NAME  RhapSFunc_SFunctionBlockTheSystem_02
#define S_FUNCTION_LEVEL 2

/*
 * Need to include simstruc.h for the definition of the SimStruct and
 * its associated macro definitions.
 */

#include "simstruc.h"

/* Rhapsody's Framework files */
#include <oxf/os.h>
#include <oxf/oxf.h>
#include <oxf/OMMainThread.h>
#include <oxf/omthread.h>
#include <oxf/OMTimerManager.h>

/*  Simulink Timer for Rhaposdy Framework */
#include <oxf/OMSFunctionTimerFactory.h>
#include <oxf/OMSFunctionTimer.h>

/* Add Rhapsody's S-Function implememtation class file */
#include "SFunctionBlockTheSystem_02.h"

#define SAMPLE_TIME_INDEX 0
#define SAMPLE_TIME_PARAM(S) ssGetSFcnParam(S, SAMPLE_TIME_INDEX)
#define NUM_PARAMS 1
#define IS_DOUBLE(value) (mxIsNumeric(value) && !mxIsLogical(value) && !mxIsEmpty(value) &&\
		!mxIsSparse(value) && !mxIsComplex(value) && mxIsDouble(value))
		
static bool bShouldWait = false;
static OMOSEventFlag* waitSignal = NULL;

static void SFunctionNotifyEventCbk(void)
{
	/* when we get notified from Rhapsody that an event has been put into 
		the event queue, we need to wait till Rhapsody is in idle */
	bShouldWait = true;
}
static void SFunctionNotifyIdleCbk(void)
{
	/* when we are notified that rhapsody is in Idle we can free Simulink */
	if(bShouldWait && waitSignal)
	{
		bShouldWait = false;
		waitSignal->signal();
	}
}
static void checkAndWaitForIdle(void)
{
	/* if the 'bShouldWait' flag is on we need to wait till Rhapsody notifies Idle */
	if(bShouldWait && waitSignal)
	{
		waitSignal->reset();
		waitSignal->wait(-1);
	}
}		

#ifdef _OMINSTRUMENT
static bool bRhapsodyStopped = false;
static OMOSEventFlag* stopSignal = NULL;

static void RhapsodyThreadsStoppedCbk(void)
{
	bRhapsodyStopped = true;
}
static void RhapsodyThreadsWorkingCbk(void)
{
	bRhapsodyStopped = false;
	if(stopSignal)
	{
		stopSignal->signal();
	}	
}
static void checkAndWaitForRhapsodyThreads(void)
{
	if(bRhapsodyStopped && stopSignal)
	{
		stopSignal->reset();
		stopSignal->wait(-1);
	}
}
#endif

/* Error handling
 * --------------
 *
 * You should use the following technique to report errors encountered within
 * an S-function:
 *
 *       ssSetErrorStatus(S,"Error encountered due to ...");
 *       return;
 *
 * Note that the 2nd argument to ssSetErrorStatus must be persistent memory.
 * It cannot be a local variable. For example the following will cause
 * unpredictable errors:
 *
 *      mdlOutputs()
 *      {
 *         char msg[256];         {ILLEGAL: to fix use "static char msg[256];"}
 *         sprintf(msg,"Error due to %s", string);
 *         ssSetErrorStatus(S,msg);
 *         return;
 *      }
 *
 * See matlabroot/simulink/src/sfuntmpl_doc.c for more details.
 */

/*====================*
 * S-function methods *
 *====================*/
#define MDL_CHECK_PARAMETERS
static void mdlCheckParameters(SimStruct *S)
{
	if ((mxGetNumberOfElements(SAMPLE_TIME_PARAM(S)) != 1) || !IS_DOUBLE(SAMPLE_TIME_PARAM(S))) {
		ssSetErrorStatus(S, "Sample time of S-Function must be a scalar");
		return;
	} else if ((mxGetPr(SAMPLE_TIME_PARAM(S))[0] < 0)
			&& (mxGetPr(SAMPLE_TIME_PARAM(S))[0] != VARIABLE_SAMPLE_TIME)) {
		ssSetErrorStatus(S, "Sample time of S-Function must be positive, zero (continuous), or -2 (variable)");
		return;
	}
}


/* Function: mdlInitializeSizes ===============================================
 * Abstract:
 *    The sizes information is used by Simulink to determine the S-function
 *    block's characteristics (number of inputs, outputs, states, etc.).
 */
static void mdlInitializeSizes(SimStruct *S)
{
    /* See sfuntmpl_doc.c for more details on the macros below */

    ssSetNumSFcnParams(S, NUM_PARAMS);
	if (ssGetNumSFcnParams(S) != ssGetSFcnParamsCount(S)) {
		/* Return if number of expected != number of actual parameters */
		return;
	} else {
		mdlCheckParameters(S);
		if (ssGetErrorStatus(S) != NULL) {
			return;
		}
	}

    ssSetNumContStates(S, 0);
    ssSetNumDiscStates(S, 0);

    if (!ssSetNumInputPorts(S, 0)) return;
    
    /*ssSetInputPortWidth(S, 0, 1);*/

    
    /*ssSetInputPortRequiredContiguous(S, 0, true);*/ /*direct input signal access*/
    /*
     * Set direct feedthrough flag (1=yes, 0=no).
     * A port has direct feedthrough if the input is used in either
     * the mdlOutputs or mdlGetTimeOfNextVarHit functions.
     * See matlabroot/simulink/src/sfuntmpl_directfeed.txt.
     */
  /*  ssSetInputPortDirectFeedThrough(S, 0, 1); */
		

	
	
    if (!ssSetNumOutputPorts(S, 1)) return;
    ssSetOutputPortWidth(S, 0, 1);
    /*ssSetOutputPortWidth(S, 0, 1);*/

	ssSetOutputPortDataType(S, 0, SS_DOUBLE);
	
    ssSetNumSampleTimes(S, 1);
    ssSetNumRWork(S, 0);
    ssSetNumIWork(S, 0);
    ssSetNumPWork(S, 1);
    ssSetNumModes(S, 0);
    ssSetNumNonsampledZCs(S, 0);

    ssSetOptions(S, 0);
}



/* Function: mdlInitializeSampleTimes =========================================
 * Abstract:
 *    This function is used to specify the sample time(s) for your
 *    S-function. You must register the same number of sample times as
 *    specified in ssSetNumSampleTimes.
 */
static void mdlInitializeSampleTimes(SimStruct *S)
{
    real_T sampleTime = mxGetPr(SAMPLE_TIME_PARAM(S))[0];
	ssSetSampleTime(S, 0, sampleTime);
	if (sampleTime == CONTINUOUS_SAMPLE_TIME) {
		ssSetOffsetTime(S, 0, FIXED_IN_MINOR_STEP_OFFSET);
	} else {
		ssSetOffsetTime(S, 0, 0.0);
	}
}



#define MDL_INITIALIZE_CONDITIONS   /* Change to #undef to remove function */
#if defined(MDL_INITIALIZE_CONDITIONS)
  /* Function: mdlInitializeConditions ========================================
   * Abstract:
   *    In this function, you should initialize the continuous and discrete
   *    states for your S-function block.  The initial states are placed
   *    in the state vector, ssGetContStates(S) or ssGetRealDiscStates(S).
   *    You can also perform any other initialization activities that your
   *    S-function may require. Note, this routine will be called at the
   *    start of simulation and if it is present in an enabled subsystem
   *    configured to reset states, it will be call when the enabled subsystem
   *    restarts execution to reset the states.
   */
  static void mdlInitializeConditions(SimStruct *S)
  {


  }
#endif /* MDL_INITIALIZE_CONDITIONS */



#define MDL_START  /* Change to #undef to remove function */
#if defined(MDL_START)
/* Function: mdlStart =======================================================
* Abstract:
*    This function is called once at start of model execution. If you
*    have states that should be initialized once, this is the place
*    to do it.
*/
static void mdlStart(SimStruct *S)
{
	/* Set the framework tickTimerFactory to be the OMSFunctionTimerFactory */
	OXF::setTheTickTimerFactory(OMSFunctionTimerFactory::instance());
	OMTimerManager::setAllowDestroy(false);

	/* create the 'waitSignal', and set the notifyIdle/notifyEvent callbacks */
	waitSignal = OMOSFactory::instance()->createOMOSEventFlag();
	OXF::setNotifyEventCbk(SFunctionNotifyEventCbk);
	OXF::setNotifyIdleCbk(SFunctionNotifyIdleCbk);
#ifdef _OMINSTRUMENT
	/* create a 'stopSiganl, and set the notify threadsWorking/threadsStopped callbacks */
	stopSignal = OMOSFactory::instance()->createOMOSEventFlag();
	AOMSchedDispatcher::setNotifyThreadsWorkingCbk(RhapsodyThreadsWorkingCbk);
	AOMSchedDispatcher::setNotifyThreadsStoppedCbk(RhapsodyThreadsStoppedCbk);
#endif
	
	/* Initialize the Rhapsody Framework */
#ifdef _OMINSTRUMENT
	if(OXF::initialize(0, NULL, 6423))
#else
	if(OXF::initialize())
#endif	 
 	{
		/* Create and initialize the main Rhapsody instance */
		SFunctionBlockTheSystem_02* p_SFunctionBlockTheSystem_02 = new SFunctionBlockTheSystem_02(OMMainThread::instance());

	  	/* Make the instance persistent */
	  	ssGetPWork(S)[0] = (void *) p_SFunctionBlockTheSystem_02;

	  	/* start the instance's behavior */
	    p_SFunctionBlockTheSystem_02->startBehavior();

	    /* Start the Framework */
	    OXF::start(true);
	}
}
#endif /*  MDL_START */

static void updateRhapsody(SimStruct *S, int_T tid)
{
	 /* retreive the current time from simulink click, and assign the Rhapsody Framework with it*/
	SFunctionBlockTheSystem_02* p_SFunctionBlockTheSystem_02;
	timeUnit time;
	timeUnit sampleTime;

	time_T taskTime = ssGetTaskTime(S, 0);
	time_T simSampleTime = ssGetSampleTime(S, 0);
	sampleTime = (timeUnit)(simSampleTime*1000);
	time = (timeUnit)(taskTime*1000);

	/* due to conversion of Simulink time ('time_T' which is a 'double') to Rhapsody time ('timeUnit' which is an 'unsigned long') we need to check
	 that the time is correct */
	if((time % sampleTime) > 0)
	{
		int remainer = time % sampleTime;
		time = time + (sampleTime - remainer);
	}
	
	bShouldWait = false;
	
	/* Assign rhapsody's toplevel class inputs with the inputs comming from the S-Function block. */
	p_SFunctionBlockTheSystem_02 = (SFunctionBlockTheSystem_02*)ssGetPWork(S)[0];
	if(p_SFunctionBlockTheSystem_02)
	{
		
	}

	/* apply the new time on Rhapsody's framework */
	OMSFunctionTimer* pTimer = (OMSFunctionTimer*)OMSFunctionTimerFactory::instance()->getCurrentTimer();
	if(pTimer != NULL)
		pTimer->setSystemTime(time);
		
	/* wait till Rhapsody finishes processing all events */
	checkAndWaitForIdle();	
 }

 #define MDL_GET_TIME_OF_NEXT_VAR_HIT
static void mdlGetTimeOfNextVarHit(SimStruct *S)
{
#ifdef _OMINSTRUMENT	
	/* in animation mode, block the Simulink thread until Rhapsody threads are working */
	checkAndWaitForRhapsodyThreads();
#endif

	updateRhapsody(S, 0);
	time_T taskTime = ssGetTaskTime(S, 0);
	OxfTimeUnit nextTime = OMTimerManager::instance()->getNextTime();
	time_T tnext = ((time_t) nextTime) / 1000.0;
	if (taskTime > tnext) {
		tnext = ssGetTFinal(S) + 1;
	}
	else if (taskTime == tnext)
		tnext = tnext + 1/1000.0;
	
	ssSetTNext(S, tnext);
}

/* Function: mdlOutputs =======================================================
 * Abstract:
 *    In this function, you compute the outputs of your S-function
 *    block. Generally outputs are placed in the output vector, ssGetY(S).
 */
static void mdlOutputs(SimStruct *S, int_T tid)
{
	 SFunctionBlockTheSystem_02* p_SFunctionBlockTheSystem_02;
	 
#ifdef _OMINSTRUMENT	
	/* in animation mode, block the Simulink thread until Rhapsody threads are working */
	checkAndWaitForRhapsodyThreads();
#endif

	/* Update Rhapsody's time according to the Simulink time, and the main block flowports */
	 updateRhapsody(S, tid);
	 
	/* Assign Simulink S-Function's outputs with the outputs comming from Rhapsody's toplevel block. */
	 p_SFunctionBlockTheSystem_02 = (SFunctionBlockTheSystem_02*)ssGetPWork(S)[0];
	 if(p_SFunctionBlockTheSystem_02)
	 {
	 	real_T* out0 = (real_T*)ssGetOutputPortSignal(S, 0);
		out0[0] = p_SFunctionBlockTheSystem_02->getItsGenerator_02()->getOp();
	 }
}

#define MDL_UPDATE
/* #undef MDL_UPDATE */
#if defined(MDL_UPDATE)
  /* Function: mdlUpdate ======================================================
   * Abstract:
   *    This function is called once for every major integration time step.
   *    Discrete states are typically updated here, but this function is useful
   *    for performing any tasks that should only take place once per
   *    integration step.
   */
static void mdlUpdate(SimStruct *S, int_T tid)
{ 
	 
}
#endif /* MDL_UPDATE */



#define MDL_DERIVATIVES  /* Change to #undef to remove function */
#if defined(MDL_DERIVATIVES)
  /* Function: mdlDerivatives =================================================
   * Abstract:
   *    In this function, you compute the S-function block's derivatives.
   *    The derivatives are placed in the derivative vector, ssGetdX(S).
   */
  static void mdlDerivatives(SimStruct *S)
  {
  }
#endif /* MDL_DERIVATIVES */



/* Function: mdlTerminate =====================================================
 * Abstract:
 *    In this function, you should perform any actions that are necessary
 *    at the termination of a simulation.  For example, if memory was
 *    allocated in mdlStart, this is the place to free it.
 */
static void mdlTerminate(SimStruct *S)
{
	SFunctionBlockTheSystem_02* p_SFunctionBlockTheSystem_02;

#ifdef _OMINSTRUMENT
	AOMSchedDispatcher* inst = AOMSchedDispatcher::getInstance();
	if(inst != NULL)
		inst->setTerminating();
#endif
	
	/* release allocated memory and cleanup pointers of the main Rhapsody instance */
  (void)OMThread::stopAllThreads(NULL);
	p_SFunctionBlockTheSystem_02 = (SFunctionBlockTheSystem_02*)ssGetPWork(S)[0];
	if(p_SFunctionBlockTheSystem_02)
	{
		delete p_SFunctionBlockTheSystem_02;
	}

	/* close all framework threads and cleanup */
	OXF::cleanup();

	/* cleanup the SFunction Timer factory */
	OMSFunctionTimerFactory::cleanup();
	
	/* cleanup the waitSignal */
	if(waitSignal)
	{
		delete waitSignal;
		waitSignal = NULL;
	}
	bShouldWait = false;
	
#ifdef _OMINSTRUMENT
	/* cleanup stopSignal */
	if(stopSignal)
	{
		delete stopSignal;
		stopSignal = NULL;
	}
	bRhapsodyStopped = false;
#endif	
}


/*======================================================*
 * See sfuntmpl_doc.c for the optional S-function methods *
 *======================================================*/

/*=============================*
 * Required S-function trailer *
 *=============================*/

#ifdef  MATLAB_MEX_FILE    /* Is this file being compiled as a MEX-file? */
#include "simulink.c"      /* MEX-file interface mechanism */
#else
#include "cg_sfun.h"       /* Code generation registration function */
#endif
