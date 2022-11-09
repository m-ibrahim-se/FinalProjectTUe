/*********************************************************************
	Rhapsody	: 8.2 
	Login		: ibarosan
	Component	: DefaultComponent 
	Configuration 	: DefaultConfig
	Model Element	: Car
//!	Generated Date	: Wed, 10, Apr 2019  
	File Path	: DefaultComponent\DefaultConfig\Car.h
*********************************************************************/

#ifndef Car_H
#define Car_H

//## auto_generated
#include <oxf.h>
//## auto_generated
#include <aom.h>
//## auto_generated
#include "STRUCTURE.h"
//## auto_generated
#include <omthread.h>
//## auto_generated
#include <omreactive.h>
//## auto_generated
#include <state.h>
//## auto_generated
#include <event.h>
//## classInstance itsCarStructure
#include "CarStructure.h"
//## classInstance itsEngine
#include "Engine.h"
//## package STRUCTURE

//## class Car
class Car : public OMReactive {
    ////    Friends    ////
    
public :

#ifdef _OMINSTRUMENT
    friend class OMAnimatedCar;
#endif // _OMINSTRUMENT

    ////    Constructors and destructors    ////
    
    //## auto_generated
    explicit Car(IOxfActive* const theActiveContext = NULL);
    
    //## auto_generated
    virtual ~Car(void);
    
    ////    Operations    ////
    
    //## operation displayStatus()
    virtual void displayStatus(void);
    
    ////    Additional operations    ////
    
    //## auto_generated
    const bool getState(void) const;
    
    //## auto_generated
    void setState(const bool p_state);
    
    //## auto_generated
    const CarStructure* getItsCarStructure(void) const;
    
    //## auto_generated
    Rhp_int32_t getItsEngine(void) const;
    
    //## auto_generated
    virtual bool startBehavior(void);

protected :

    //## auto_generated
    void initStatechart(void);
    
    ////    Attributes    ////

private :

    bool state;		//## attribute state
    
    ////    Relations and components    ////
    
    CarStructure itsCarStructure;		//## classInstance itsCarStructure
    
    Engine itsEngine[2];		//## classInstance itsEngine
    
    ////    Framework operations    ////

public :

    // rootState:
    //## statechart_method
    inline RhpBoolean rootState_IN(void) const;
    
    // Parked:
    //## statechart_method
    inline RhpBoolean Parked_IN(void) const;
    
    // Off:
    //## statechart_method
    inline RhpBoolean Off_IN(void) const;
    
    // Drive:
    //## statechart_method
    inline RhpBoolean Drive_IN(void) const;

protected :

    //## statechart_method
    virtual void rootState_entDef(void);
    
    //## statechart_method
    virtual IOxfReactive::TakeEventStatus rootState_processEvent(void);
    
    ////    Framework    ////
    
//#[ ignore
    enum Car_Enum {
        OMNonState = 0,
        Parked = 1,
        Off = 2,
        Drive = 3
    };
//#]

private :

//#[ ignore
    Car_Enum rootState_subState;
    
    Car_Enum rootState_active;
//#]
};

#ifdef _OMINSTRUMENT
//#[ ignore
class OMAnimatedCar : virtual public AOMInstance {
    DECLARE_REACTIVE_META(Car, OMAnimatedCar)
    
    ////    Framework operations    ////
    
public :

    virtual void serializeAttributes(AOMSAttributes* aomsAttributes) const;
    
    virtual void serializeRelations(AOMSRelations* aomsRelations) const;
    
    //## statechart_method
    void rootState_serializeStates(AOMSState* aomsState) const;
    
    //## statechart_method
    void Parked_serializeStates(AOMSState* aomsState) const;
    
    //## statechart_method
    void Off_serializeStates(AOMSState* aomsState) const;
    
    //## statechart_method
    void Drive_serializeStates(AOMSState* aomsState) const;
};
//#]
#endif // _OMINSTRUMENT

inline RhpBoolean Car::rootState_IN(void) const {
    return true;
}

inline RhpBoolean Car::Parked_IN(void) const {
    return rootState_subState == Parked;
}

inline RhpBoolean Car::Off_IN(void) const {
    return rootState_subState == Off;
}

inline RhpBoolean Car::Drive_IN(void) const {
    return rootState_subState == Drive;
}

#endif
/*********************************************************************
	File Path	: DefaultComponent\DefaultConfig\Car.h
*********************************************************************/
