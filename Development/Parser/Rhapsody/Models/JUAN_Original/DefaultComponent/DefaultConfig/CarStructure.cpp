/********************************************************************
	Rhapsody	: 8.2 
	Login		: ibarosan
	Component	: DefaultComponent 
	Configuration 	: DefaultConfig
	Model Element	: CarStructure
//!	Generated Date	: Wed, 10, Apr 2019  
	File Path	: DefaultComponent\DefaultConfig\CarStructure.cpp
*********************************************************************/

//#[ ignore
#define NAMESPACE_PREFIX
//#]

//## auto_generated
#include "CarStructure.h"
//#[ ignore
#define STRUCTURE_CarStructure_CarStructure_SERIALIZE OM_NO_OP
//#]

//## package STRUCTURE

//## class CarStructure
CarStructure::CarStructure(void) {
    NOTIFY_CONSTRUCTOR(CarStructure, CarStructure(), 0, STRUCTURE_CarStructure_CarStructure_SERIALIZE);
}

CarStructure::~CarStructure(void) {
    NOTIFY_DESTRUCTOR(~CarStructure, true);
}

#ifdef _OMINSTRUMENT
IMPLEMENT_META_P(CarStructure, STRUCTURE, STRUCTURE, false, OMAnimatedCarStructure)
#endif // _OMINSTRUMENT

/*********************************************************************
	File Path	: DefaultComponent\DefaultConfig\CarStructure.cpp
*********************************************************************/
