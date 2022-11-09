/********************************************************************
	Rhapsody	: 8.2 
	Login		: ibarosan
	Component	: DefaultComponent 
	Configuration 	: DefaultConfig
	Model Element	: Car
//!	Generated Date	: Wed, 10, Apr 2019  
	File Path	: DefaultComponent\DefaultConfig\Car.cpp
*********************************************************************/

//#[ ignore
#define NAMESPACE_PREFIX

#define _OMSTATECHART_ANIMATED
//#]

//## auto_generated
#include "Car.h"
//#[ ignore
#define STRUCTURE_Car_Car_SERIALIZE OM_NO_OP

#define STRUCTURE_Car_displayStatus_SERIALIZE OM_NO_OP
//#]

//## package STRUCTURE

//## class Car
Car::Car(IOxfActive* const theActiveContext) : OMReactive(), state(false) {
    NOTIFY_REACTIVE_CONSTRUCTOR(Car, Car(), 0, STRUCTURE_Car_Car_SERIALIZE);
    setActiveContext(theActiveContext, false);
    initStatechart();
}

Car::~Car(void) {
    NOTIFY_DESTRUCTOR(~Car, true);
}

void Car::displayStatus(void) {
    NOTIFY_OPERATION(displayStatus, displayStatus(), 0, STRUCTURE_Car_displayStatus_SERIALIZE);
    //#[ operation displayStatus()
    //#]
}

const bool Car::getState(void) const {
    return state;
}

void Car::setState(const bool p_state) {
    state = p_state;
}

const CarStructure* Car::getItsCarStructure(void) const {
    return &itsCarStructure;
}

Rhp_int32_t Car::getItsEngine(void) const {
    Rhp_int32_t iter = 0;
    return iter;
}

bool Car::startBehavior(void) {
    bool done = true;
    if(done == true)
        {
            done = OMReactive::startBehavior();
        }
    return done;
}

void Car::initStatechart(void) {
    rootState_subState = OMNonState;
    rootState_active = OMNonState;
}

void Car::rootState_entDef(void) {
    {
        NOTIFY_STATE_ENTERED("ROOT");
        NOTIFY_TRANSITION_STARTED("0");
        NOTIFY_STATE_ENTERED("ROOT.Off");
        rootState_subState = Off;
        rootState_active = Off;
        NOTIFY_TRANSITION_TERMINATED("0");
    }
}

IOxfReactive::TakeEventStatus Car::rootState_processEvent(void) {
    IOxfReactive::TakeEventStatus res = eventNotConsumed;
    switch (rootState_active) {
        // State Off
        case Off:
        {
            if(IS_EVENT_TYPE_OF(start_STRUCTURE_id) == 1)
                {
                    //## transition 1 
                    if( state)
                        {
                            NOTIFY_TRANSITION_STARTED("1");
                            NOTIFY_STATE_EXITED("ROOT.Off");
                            NOTIFY_STATE_ENTERED("ROOT.Drive");
                            rootState_subState = Drive;
                            rootState_active = Drive;
                            NOTIFY_TRANSITION_TERMINATED("1");
                            res = eventConsumed;
                        }
                }
            
        }
        break;
        // State Parked
        case Parked:
        {
            if(IS_EVENT_TYPE_OF(stop_STRUCTURE_id) == 1)
                {
                    NOTIFY_TRANSITION_STARTED("3");
                    NOTIFY_STATE_EXITED("ROOT.Parked");
                    NOTIFY_STATE_ENTERED("ROOT.Off");
                    rootState_subState = Off;
                    rootState_active = Off;
                    NOTIFY_TRANSITION_TERMINATED("3");
                    res = eventConsumed;
                }
            
        }
        break;
        // State Drive
        case Drive:
        {
            if(IS_EVENT_TYPE_OF(parking_STRUCTURE_id) == 1)
                {
                    NOTIFY_TRANSITION_STARTED("2");
                    NOTIFY_STATE_EXITED("ROOT.Drive");
                    NOTIFY_STATE_ENTERED("ROOT.Parked");
                    rootState_subState = Parked;
                    rootState_active = Parked;
                    NOTIFY_TRANSITION_TERMINATED("2");
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
void OMAnimatedCar::serializeAttributes(AOMSAttributes* aomsAttributes) const {
    aomsAttributes->addAttribute("state", x2String(myReal->state));
}

void OMAnimatedCar::serializeRelations(AOMSRelations* aomsRelations) const {
    aomsRelations->addRelation("itsEngine", true, false);
    {
        Rhp_int32_t iter = 0;
        while (iter < 2){
            aomsRelations->ADD_ITEM(((Engine*)&myReal->itsEngine[iter]));
            iter++;
        }
    }
    aomsRelations->addRelation("itsCarStructure", true, true);
    aomsRelations->ADD_ITEM(&myReal->itsCarStructure);
}

void OMAnimatedCar::rootState_serializeStates(AOMSState* aomsState) const {
    aomsState->addState("ROOT");
    switch (myReal->rootState_subState) {
        case Car::Off:
        {
            Off_serializeStates(aomsState);
        }
        break;
        case Car::Parked:
        {
            Parked_serializeStates(aomsState);
        }
        break;
        case Car::Drive:
        {
            Drive_serializeStates(aomsState);
        }
        break;
        default:
            break;
    }
}

void OMAnimatedCar::Parked_serializeStates(AOMSState* aomsState) const {
    aomsState->addState("ROOT.Parked");
}

void OMAnimatedCar::Off_serializeStates(AOMSState* aomsState) const {
    aomsState->addState("ROOT.Off");
}

void OMAnimatedCar::Drive_serializeStates(AOMSState* aomsState) const {
    aomsState->addState("ROOT.Drive");
}
//#]

IMPLEMENT_REACTIVE_META_P(Car, STRUCTURE, STRUCTURE, false, OMAnimatedCar)
#endif // _OMINSTRUMENT

/*********************************************************************
	File Path	: DefaultComponent\DefaultConfig\Car.cpp
*********************************************************************/
