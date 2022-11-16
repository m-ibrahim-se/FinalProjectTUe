/*********************************************************************
	Rhapsody	: 9.0 
	Login		: 20204920
	Component	: TheSystem_02Comp 
	Configuration 	: Simulink
	Model Element	: doubleFlowInterface
//!	Generated Date	: Thu, 3, Nov 2022  
	File Path	: TheSystem_02Comp\Simulink\doubleFlowInterface.h
*********************************************************************/

#ifndef doubleFlowInterface_H
#define doubleFlowInterface_H

//## auto_generated
#include <oxf\oxf.h>
//## auto_generated
#include <..\Profiles\SysML\SIDefinitions.h>
//#[ ignore
//## package FlowPortInterfaces

//## ignore
class doubleFlowInterface {
    ////    Constructors and destructors    ////
    
public :

    //## auto_generated
    doubleFlowInterface();
    
    //## auto_generated
    virtual ~doubleFlowInterface() = 0;
    
    ////    Operations    ////
    
    //## operation SetValue(double,void *)
    virtual void SetValue(double data, void * pCaller = NULL) = 0;
};
//#]

#endif
/*********************************************************************
	File Path	: TheSystem_02Comp\Simulink\doubleFlowInterface.h
*********************************************************************/
