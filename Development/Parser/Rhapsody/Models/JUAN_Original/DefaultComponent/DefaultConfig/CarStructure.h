/*********************************************************************
	Rhapsody	: 8.2 
	Login		: ibarosan
	Component	: DefaultComponent 
	Configuration 	: DefaultConfig
	Model Element	: CarStructure
//!	Generated Date	: Wed, 10, Apr 2019  
	File Path	: DefaultComponent\DefaultConfig\CarStructure.h
*********************************************************************/

#ifndef CarStructure_H
#define CarStructure_H

//## auto_generated
#include <oxf.h>
//## auto_generated
#include <aom.h>
//## auto_generated
#include "STRUCTURE.h"
//## package STRUCTURE

//## class CarStructure
class CarStructure {
    ////    Friends    ////
    
public :

#ifdef _OMINSTRUMENT
    friend class OMAnimatedCarStructure;
#endif // _OMINSTRUMENT

    ////    Constructors and destructors    ////
    
    //## auto_generated
    CarStructure(void);
    
    //## auto_generated
    ~CarStructure(void);
};

#ifdef _OMINSTRUMENT
//#[ ignore
class OMAnimatedCarStructure : virtual public AOMInstance {
    DECLARE_META(CarStructure, OMAnimatedCarStructure)
};
//#]
#endif // _OMINSTRUMENT

#endif
/*********************************************************************
	File Path	: DefaultComponent\DefaultConfig\CarStructure.h
*********************************************************************/
