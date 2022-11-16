/********************************************************************
	Rhapsody	: 9.0 
	Login		: 20204920
	Component	: TheSystem_02Comp 
	Configuration 	: SimulinkAnim
	Model Element	: Generator_02
//!	Generated Date	: Thu, 3, Nov 2022  
	File Path	: TheSystem_02Comp\SimulinkAnim\Generator_02.cpp
*********************************************************************/

//#[ ignore
#define NAMESPACE_PREFIX

#define _OMSTATECHART_ANIMATED
//#]

//## auto_generated
#include "Generator_02.h"
//#[ ignore
#define InterConnection_Generator_02_Generator_02_SERIALIZE OM_NO_OP
//#]

//## package InterConnection

//## class Generator_02
Generator_02::Generator_02(IOxfActive* theActiveContext) {
    NOTIFY_REACTIVE_CONSTRUCTOR(Generator_02, Generator_02(), 0, InterConnection_Generator_02_Generator_02_SERIALIZE);
    setActiveContext(theActiveContext, false);
    initStatechart();
}

Generator_02::~Generator_02() {
    NOTIFY_DESTRUCTOR(~Generator_02, true);
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
        NOTIFY_STATE_ENTERED("ROOT");
        NOTIFY_TRANSITION_STARTED("2");
        NOTIFY_STATE_ENTERED("ROOT.state_0");
        pushNullTransition();
        rootState_subState = state_0;
        rootState_active = state_0;
        NOTIFY_TRANSITION_TERMINATED("2");
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
                    NOTIFY_TRANSITION_STARTED("0");
                    popNullTransition();
                    NOTIFY_STATE_EXITED("ROOT.state_0");
                    //#[ transition 0 
                    setOp(2);
                    //#]
                    NOTIFY_STATE_ENTERED("ROOT.state_1");
                    pushNullTransition();
                    rootState_subState = state_1;
                    rootState_active = state_1;
                    NOTIFY_TRANSITION_TERMINATED("0");
                    res = eventConsumed;
                }
            
        }
        break;
        // State state_1
        case state_1:
        {
            if(IS_EVENT_TYPE_OF(OMNullEventId))
                {
                    NOTIFY_TRANSITION_STARTED("1");
                    popNullTransition();
                    NOTIFY_STATE_EXITED("ROOT.state_1");
                    //#[ transition 1 
                    setOp(-2);
                    //#]
                    NOTIFY_STATE_ENTERED("ROOT.state_0");
                    pushNullTransition();
                    rootState_subState = state_0;
                    rootState_active = state_0;
                    NOTIFY_TRANSITION_TERMINATED("1");
                    res = eventConsumed;
                }
            
        }
        break;
        default:
            break;
    }
    return res;
}

#ifdef _OMINSTRUMENT
//#[ ignore
void OMAnimatedGenerator_02::serializeAttributes(AOMSAttributes* aomsAttributes) const {
    aomsAttributes->addAttribute("op", x2String(myReal->op));
}

void OMAnimatedGenerator_02::rootState_serializeStates(AOMSState* aomsState) const {
    aomsState->addState("ROOT");
    switch (myReal->rootState_subState) {
        case Generator_02::state_0:
        {
            state_0_serializeStates(aomsState);
        }
        break;
        case Generator_02::state_1:
        {
            state_1_serializeStates(aomsState);
        }
        break;
        default:
            break;
    }
}

void OMAnimatedGenerator_02::state_1_serializeStates(AOMSState* aomsState) const {
    aomsState->addState("ROOT.state_1");
}

void OMAnimatedGenerator_02::state_0_serializeStates(AOMSState* aomsState) const {
    aomsState->addState("ROOT.state_0");
}
//#]

IMPLEMENT_REACTIVE_META_P(Generator_02, InterConnection, InterConnection, false, OMAnimatedGenerator_02)
#endif // _OMINSTRUMENT

/*********************************************************************
	File Path	: TheSystem_02Comp\SimulinkAnim\Generator_02.cpp
*********************************************************************/
