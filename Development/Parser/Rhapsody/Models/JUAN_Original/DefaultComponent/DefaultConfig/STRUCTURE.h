/*********************************************************************
	Rhapsody	: 8.2 
	Login		: ibarosan
	Component	: DefaultComponent 
	Configuration 	: DefaultConfig
	Model Element	: STRUCTURE
//!	Generated Date	: Wed, 10, Apr 2019  
	File Path	: DefaultComponent\DefaultConfig\STRUCTURE.h
*********************************************************************/

#ifndef STRUCTURE_H
#define STRUCTURE_H

//## auto_generated
#include <oxf.h>
//## auto_generated
#include <aom.h>
//## auto_generated
#include <event.h>
//## auto_generated
class Car;

//## auto_generated
class CarStructure;

//## auto_generated
class Engine;

//## package STRUCTURE



//## event start()
class start : public OMEvent {
    ////    Friends    ////
    
public :

#ifdef _OMINSTRUMENT
    friend class OMAnimatedstart;
#endif // _OMINSTRUMENT

    ////    Constructors and destructors    ////
    
    //## auto_generated
    start(void);
};

#ifdef _OMINSTRUMENT
//#[ ignore
class OMAnimatedstart : virtual public AOMEvent {
    DECLARE_META_EVENT(start)
};
//#]
#endif // _OMINSTRUMENT

//#[ ignore
extern const IOxfEvent::ID start_STRUCTURE_id;
//#]

//## event parking()
class parking : public OMEvent {
    ////    Friends    ////
    
public :

#ifdef _OMINSTRUMENT
    friend class OMAnimatedparking;
#endif // _OMINSTRUMENT

    ////    Constructors and destructors    ////
    
    //## auto_generated
    parking(void);
};

#ifdef _OMINSTRUMENT
//#[ ignore
class OMAnimatedparking : virtual public AOMEvent {
    DECLARE_META_EVENT(parking)
};
//#]
#endif // _OMINSTRUMENT

//#[ ignore
extern const IOxfEvent::ID parking_STRUCTURE_id;
//#]

//## event stop()
class stop : public OMEvent {
    ////    Friends    ////
    
public :

#ifdef _OMINSTRUMENT
    friend class OMAnimatedstop;
#endif // _OMINSTRUMENT

    ////    Constructors and destructors    ////
    
    //## auto_generated
    stop(void);
};

#ifdef _OMINSTRUMENT
//#[ ignore
class OMAnimatedstop : virtual public AOMEvent {
    DECLARE_META_EVENT(stop)
};
//#]
#endif // _OMINSTRUMENT

//#[ ignore
extern const IOxfEvent::ID stop_STRUCTURE_id;
//#]

#endif
/*********************************************************************
	File Path	: DefaultComponent\DefaultConfig\STRUCTURE.h
*********************************************************************/
