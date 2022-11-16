/********************************************************************
	Rhapsody	: 9.0 
	Login		: 20204920
	Component	: TheSystem_02Comp 
	Configuration 	: Simulink
	Model Element	: Generator_02
//!	Generated Date	: Thu, 3, Nov 2022  
	File Path	: TheSystem_02Comp\Simulink\Generator_02.cpp
*********************************************************************/

//## auto_generated
#include <oxf\omthread.h>
//## auto_generated
#include "Generator_02.h"
//## package InterConnection

//## class Generator_02
Generator_02::Generator_02(IOxfActive* theActiveContext) {
    setActiveContext(theActiveContext, false);
    initStatechart();
}

Generator_02::~Generator_02() {
}

RhpReal Generator_02::getOp() const {
    return op;
}

void Generator_02::setOp(RhpReal p_op) {
    op = p_op;
}

bool Generator_02::startBehavior() {
    bool done = false;
    done = OMReactive::startBehavior();
    return done;
}

void Generator_02::initStatechart() {
    rootState_subState = OMNonState;
    rootState_active = OMNonState;
}

void Generator_02::rootState_entDef() {
    {
        pushNullTransition();
        rootState_subState = state_0;
        rootState_active = state_0;
    }
}

IOxfReactive::TakeEventStatus Generator_02::rootState_processEvent() {
    IOxfReactive::TakeEventStatus res = eventNotConsumed;
    switch (rootState_active) {
        // State state_0
        case state_0:
        {
            if(IS_EVENT_TYPE_OF(OMNullEventId))
                {
                    popNullTransition();
                    //#[ transition 0 
                    setOp(2);
                    //#]
                    pushNullTransition();
                    rootState_subState = state_1;
                    rootState_active = state_1;
                    res = eventConsumed;
                }
            
        }
        break;
        // State state_1
        case state_1:
        {
            if(IS_EVENT_TYPE_OF(OMNullEventId))
                {
                    popNullTransition();
                    //#[ transition 1 
                    setOp(-2);
                    //#]
                    pushNullTransition();
                    rootState_subState = state_0;
                    rootState_active = state_0;
                    res = eventConsumed;
                }
            
        }
        break;
        default:
            break;
    }
    return res;
}

/*********************************************************************
	File Path	: TheSystem_02Comp\Simulink\Generator_02.cpp
*********************************************************************/
