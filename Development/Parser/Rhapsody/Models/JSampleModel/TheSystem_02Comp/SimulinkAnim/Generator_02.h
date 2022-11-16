/*********************************************************************
	Rhapsody	: 9.0 
	Login		: 20204920
	Component	: TheSystem_02Comp 
	Configuration 	: SimulinkAnim
	Model Element	: Generator_02
//!	Generated Date	: Thu, 3, Nov 2022  
	File Path	: TheSystem_02Comp\SimulinkAnim\Generator_02.h
*********************************************************************/

#ifndef Generator_02_H
#define Generator_02_H

//## auto_generated
#include <oxf\oxf.h>
//## auto_generated
#include <..\Profiles\SysML\SIDefinitions.h>
//## auto_generated
#include <aom\aom.h>
//## auto_generated
#include "InterConnection.h"
//## auto_generated
#include <oxf\omthread.h>
//## auto_generated
#include <oxf\omreactive.h>
//## auto_generated
#include <oxf\state.h>
//## auto_generated
#include <oxf\event.h>
//## package InterConnection

//## class Generator_02
class Generator_02 : public OMReactive {
    ////    Friends    ////
    
public :

#ifdef _OMINSTRUMENT
    friend class OMAnimatedGenerator_02;
#endif // _OMINSTRUMENT

    ////    Constructors and destructors    ////
    
    //## auto_generated
    Generator_02(IOxfActive* theActiveContext = 0);
    
    //## auto_generated
    ~Generator_02();
    
    ////    Additional operations    ////
    
    //## auto_generated
    RhpReal getOp() const;
    
    //## auto_generated
    void setOp(RhpReal p_op);
    
    //## auto_generated
    virtual bool startBehavior();

protected :

    //## auto_generated
    void initStatechart();
    
    ////    Attributes    ////
    
    RhpReal op;		//## attribute op
    
    ////    Framework operations    ////

public :

    // rootState:
    //## statechart_method
    inline bool rootState_IN() const;
    
    //## statechart_method
    virtual void rootState_entDef();
    
    //## statechart_method
    virtual IOxfReactive::TakeEventStatus rootState_processEvent();
    
    // state_1:
    //## statechart_method
    inline bool state_1_IN() const;
    
    // state_0:
    //## statechart_method
    inline bool state_0_IN() const;
    
    ////    Framework    ////

protected :

//#[ ignore
    enum Generator_02_Enum {
        OMNonState = 0,
        state_1 = 1,
        state_0 = 2
    };
    
    int rootState_subState;
    
    int rootState_active;
//#]
};

#ifdef _OMINSTRUMENT
//#[ ignore
class OMAnimatedGenerator_02 : virtual public AOMInstance {
    DECLARE_REACTIVE_META(Generator_02, OMAnimatedGenerator_02)
    
    ////    Framework operations    ////
    
public :

    virtual void serializeAttributes(AOMSAttributes* aomsAttributes) const;
    
    //## statechart_method
    void rootState_serializeStates(AOMSState* aomsState) const;
    
    //## statechart_method
    void state_1_serializeStates(AOMSState* aomsState) const;
    
    //## statechart_method
    void state_0_serializeStates(AOMSState* aomsState) const;
};
//#]
#endif // _OMINSTRUMENT

inline bool Generator_02::rootState_IN() const {
    return true;
}

inline bool Generator_02::state_1_IN() const {
    return rootState_subState == state_1;
}

inline bool Generator_02::state_0_IN() const {
    return rootState_subState == state_0;
}

#endif
/*********************************************************************
	File Path	: TheSystem_02Comp\SimulinkAnim\Generator_02.h
*********************************************************************/
