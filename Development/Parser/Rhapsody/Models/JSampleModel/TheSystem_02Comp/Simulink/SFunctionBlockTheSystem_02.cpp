/********************************************************************
	Rhapsody	: 9.0 
	Login		: 20204920
	Component	: TheSystem_02Comp 
	Configuration 	: Simulink
	Model Element	: SFunctionBlockTheSystem_02
//!	Generated Date	: Thu, 3, Nov 2022  
	File Path	: TheSystem_02Comp\Simulink\SFunctionBlockTheSystem_02.cpp
*********************************************************************/

//## auto_generated
#include <oxf\omthread.h>
//## auto_generated
#include "SFunctionBlockTheSystem_02.h"
//## package InterConnection

//## class SFunctionBlockTheSystem_02
//#[ ignore
SFunctionBlockTheSystem_02::itsMonitor_02_sinIn_SP_C::itsMonitor_02_sinIn_SP_C() : _p_(0) {
    itsDoubleFlowInterface = NULL;
}

SFunctionBlockTheSystem_02::itsMonitor_02_sinIn_SP_C::~itsMonitor_02_sinIn_SP_C() {
    cleanUpRelations();
}

void SFunctionBlockTheSystem_02::itsMonitor_02_sinIn_SP_C::SetValue(double data, void * pCaller) {
    
    if (itsDoubleFlowInterface != NULL) {
        itsDoubleFlowInterface->SetValue(data,pCaller);
    }
    
}

doubleFlowInterface* SFunctionBlockTheSystem_02::itsMonitor_02_sinIn_SP_C::getItsDoubleFlowInterface() {
    return this;
}

doubleFlowInterface* SFunctionBlockTheSystem_02::itsMonitor_02_sinIn_SP_C::getOutBound() {
    return this;
}

void SFunctionBlockTheSystem_02::itsMonitor_02_sinIn_SP_C::setItsDoubleFlowInterface(doubleFlowInterface* p_doubleFlowInterface) {
    itsDoubleFlowInterface = p_doubleFlowInterface;
}

void SFunctionBlockTheSystem_02::itsMonitor_02_sinIn_SP_C::cleanUpRelations() {
    if(itsDoubleFlowInterface != NULL)
        {
            itsDoubleFlowInterface = NULL;
        }
}
//#]

SFunctionBlockTheSystem_02::SFunctionBlockTheSystem_02(IOxfActive* theActiveContext) {
    setActiveContext(theActiveContext, false);
    {
        {
            itsGenerator_02.setShouldDelete(false);
        }
    }
}

SFunctionBlockTheSystem_02::~SFunctionBlockTheSystem_02() {
}

SFunctionBlockTheSystem_02::itsMonitor_02_sinIn_SP_C* SFunctionBlockTheSystem_02::getItsMonitor_02_sinIn_SP() const {
    return (SFunctionBlockTheSystem_02::itsMonitor_02_sinIn_SP_C*) &itsMonitor_02_sinIn_SP;
}

SFunctionBlockTheSystem_02::itsMonitor_02_sinIn_SP_C* SFunctionBlockTheSystem_02::get_itsMonitor_02_sinIn_SP() const {
    return (SFunctionBlockTheSystem_02::itsMonitor_02_sinIn_SP_C*) &itsMonitor_02_sinIn_SP;
}

real_T SFunctionBlockTheSystem_02::getItsMonitor_02_sinIn() const {
    return itsMonitor_02_sinIn;
}

void SFunctionBlockTheSystem_02::setItsMonitor_02_sinIn(real_T p_itsMonitor_02_sinIn) {
    itsMonitor_02_sinIn = p_itsMonitor_02_sinIn;
}

Generator_02* SFunctionBlockTheSystem_02::getItsGenerator_02() const {
    return (Generator_02*) &itsGenerator_02;
}

bool SFunctionBlockTheSystem_02::startBehavior() {
    bool done = true;
    done &= itsGenerator_02.startBehavior();
    done &= OMReactive::startBehavior();
    return done;
}

void SFunctionBlockTheSystem_02::setActiveContext(IOxfActive* theActiveContext, bool activeInstance) {
    OMReactive::setActiveContext(theActiveContext, activeInstance);
    {
        itsGenerator_02.setActiveContext(theActiveContext, false);
    }
}

void SFunctionBlockTheSystem_02::destroy() {
    itsGenerator_02.destroy();
    OMReactive::destroy();
}

/*********************************************************************
	File Path	: TheSystem_02Comp\Simulink\SFunctionBlockTheSystem_02.cpp
*********************************************************************/
