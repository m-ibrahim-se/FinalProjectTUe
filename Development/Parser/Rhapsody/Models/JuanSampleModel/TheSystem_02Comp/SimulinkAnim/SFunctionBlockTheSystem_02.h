/*********************************************************************
	Rhapsody	: 9.0 
	Login		: 20204920
	Component	: TheSystem_02Comp 
	Configuration 	: SimulinkAnim
	Model Element	: SFunctionBlockTheSystem_02
//!	Generated Date	: Thu, 3, Nov 2022  
	File Path	: TheSystem_02Comp\SimulinkAnim\SFunctionBlockTheSystem_02.h
*********************************************************************/

#ifndef SFunctionBlockTheSystem_02_H
#define SFunctionBlockTheSystem_02_H

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
//## classInstance itsGenerator_02
#include "Generator_02.h"
//## attribute itsMonitor_02_sinIn
#include "rtwtypes.h"
//## class itsMonitor_02_sinIn_SP_C
#include "doubleFlowInterface.h"
//## package InterConnection

//## class SFunctionBlockTheSystem_02
class SFunctionBlockTheSystem_02 : public OMReactive {
public :

//#[ ignore
    //## package InterConnection
    class itsMonitor_02_sinIn_SP_C : public doubleFlowInterface {
        ////    Constructors and destructors    ////
        
    public :
    
        //## auto_generated
        itsMonitor_02_sinIn_SP_C();
        
        //## auto_generated
        virtual ~itsMonitor_02_sinIn_SP_C();
        
        ////    Operations    ////
        
        //## auto_generated
        virtual void SetValue(double data, void * pCaller = NULL);
        
        //## auto_generated
        doubleFlowInterface* getItsDoubleFlowInterface();
        
        //## auto_generated
        doubleFlowInterface* getOutBound();
        
        ////    Additional operations    ////
        
        //## auto_generated
        void setItsDoubleFlowInterface(doubleFlowInterface* p_doubleFlowInterface);
    
    protected :
    
        //## auto_generated
        void cleanUpRelations();
        
        ////    Attributes    ////
        
        int _p_;		//## attribute _p_
        
        ////    Relations and components    ////
        
        doubleFlowInterface* itsDoubleFlowInterface;		//## link itsDoubleFlowInterface
    };
//#]

    ////    Friends    ////
    
#ifdef _OMINSTRUMENT
    friend class OMAnimatedSFunctionBlockTheSystem_02;
#endif // _OMINSTRUMENT

    ////    Constructors and destructors    ////
    
    //## auto_generated
    SFunctionBlockTheSystem_02(IOxfActive* theActiveContext = 0);
    
    //## auto_generated
    ~SFunctionBlockTheSystem_02();
    
    ////    Additional operations    ////
    
    //## auto_generated
    itsMonitor_02_sinIn_SP_C* getItsMonitor_02_sinIn_SP() const;
    
    //## auto_generated
    itsMonitor_02_sinIn_SP_C* get_itsMonitor_02_sinIn_SP() const;
    
    //## auto_generated
    real_T getItsMonitor_02_sinIn() const;
    
    //## auto_generated
    void setItsMonitor_02_sinIn(real_T p_itsMonitor_02_sinIn);
    
    //## auto_generated
    Generator_02* getItsGenerator_02() const;
    
    //## auto_generated
    virtual bool startBehavior();
    
    ////    Attributes    ////

protected :

    real_T itsMonitor_02_sinIn;		//## attribute itsMonitor_02_sinIn
    
    ////    Relations and components    ////
    
//#[ ignore
    itsMonitor_02_sinIn_SP_C itsMonitor_02_sinIn_SP;
//#]

    Generator_02 itsGenerator_02;		//## classInstance itsGenerator_02
    
    ////    Framework operations    ////

public :

    //## auto_generated
    void setActiveContext(IOxfActive* theActiveContext, bool activeInstance);
    
    //## auto_generated
    virtual void destroy();
};

#ifdef _OMINSTRUMENT
//#[ ignore
class OMAnimatedSFunctionBlockTheSystem_02 : virtual public AOMInstance {
    DECLARE_META(SFunctionBlockTheSystem_02, OMAnimatedSFunctionBlockTheSystem_02)
    
    ////    Framework operations    ////
    
public :

    virtual void serializeAttributes(AOMSAttributes* aomsAttributes) const;
    
    virtual void serializeRelations(AOMSRelations* aomsRelations) const;
};
//#]
#endif // _OMINSTRUMENT

#endif
/*********************************************************************
	File Path	: TheSystem_02Comp\SimulinkAnim\SFunctionBlockTheSystem_02.h
*********************************************************************/
