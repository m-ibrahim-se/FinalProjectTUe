/********************************************************************
	Rhapsody	: 8.2 
	Login		: ibarosan
	Component	: DefaultComponent 
	Configuration 	: DefaultConfig
	Model Element	: STRUCTURE
//!	Generated Date	: Wed, 10, Apr 2019  
	File Path	: DefaultComponent\DefaultConfig\STRUCTURE.cpp
*********************************************************************/

//#[ ignore
#define NAMESPACE_PREFIX
//#]

//## auto_generated
#include "STRUCTURE.h"
//## auto_generated
#include "Car.h"
//## auto_generated
#include "CarStructure.h"
//## auto_generated
#include "Engine.h"
//#[ ignore
#define start_SERIALIZE OM_NO_OP

#define start_UNSERIALIZE OM_NO_OP

#define start_CONSTRUCTOR start()

#define parking_SERIALIZE OM_NO_OP

#define parking_UNSERIALIZE OM_NO_OP

#define parking_CONSTRUCTOR parking()

#define stop_SERIALIZE OM_NO_OP

#define stop_UNSERIALIZE OM_NO_OP

#define stop_CONSTRUCTOR stop()
//#]

//## package STRUCTURE


#ifdef _OMINSTRUMENT
static void serializeGlobalVars(AOMSAttributes* /* aomsAttributes */);

IMPLEMENT_META_PACKAGE(STRUCTURE, STRUCTURE)

static void serializeGlobalVars(AOMSAttributes* /* aomsAttributes */) {
}
#endif // _OMINSTRUMENT

//## event start()
start::start(void) : OMEvent() {
    NOTIFY_EVENT_CONSTRUCTOR(start)
    setId(start_STRUCTURE_id);
}

//#[ ignore
const IOxfEvent::ID start_STRUCTURE_id(21001);
//#]

IMPLEMENT_META_EVENT_P(start, STRUCTURE, STRUCTURE, start())

//## event parking()
parking::parking(void) : OMEvent() {
    NOTIFY_EVENT_CONSTRUCTOR(parking)
    setId(parking_STRUCTURE_id);
}

//#[ ignore
const IOxfEvent::ID parking_STRUCTURE_id(21002);
//#]

IMPLEMENT_META_EVENT_P(parking, STRUCTURE, STRUCTURE, parking())

//## event stop()
stop::stop(void) : OMEvent() {
    NOTIFY_EVENT_CONSTRUCTOR(stop)
    setId(stop_STRUCTURE_id);
}

//#[ ignore
const IOxfEvent::ID stop_STRUCTURE_id(21003);
//#]

IMPLEMENT_META_EVENT_P(stop, STRUCTURE, STRUCTURE, stop())

/*********************************************************************
	File Path	: DefaultComponent\DefaultConfig\STRUCTURE.cpp
*********************************************************************/
