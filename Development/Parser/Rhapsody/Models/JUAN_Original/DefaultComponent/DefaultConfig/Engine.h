/*********************************************************************
	Rhapsody	: 8.2 
	Login		: ibarosan
	Component	: DefaultComponent 
	Configuration 	: DefaultConfig
	Model Element	: Engine
//!	Generated Date	: Wed, 10, Apr 2019  
	File Path	: DefaultComponent\DefaultConfig\Engine.h
*********************************************************************/

#ifndef Engine_H
#define Engine_H

//## auto_generated
#include <oxf.h>
//## auto_generated
#include <aom.h>
//## auto_generated
#include "STRUCTURE.h"
//## package STRUCTURE

//## class Engine
class Engine {
    ////    Friends    ////
    
public :

#ifdef _OMINSTRUMENT
    friend class OMAnimatedEngine;
#endif // _OMINSTRUMENT

    ////    Constructors and destructors    ////
    
    //## auto_generated
    Engine(void);
    
    //## auto_generated
    virtual ~Engine(void);
    
    ////    Operations    ////
    
    //## operation accelerate()
    virtual void accelerate(void);
    
    //## operation brake()
    virtual void brake(void);
    
    //## operation start()
    virtual void start(void);
    
    //## operation stop()
    virtual void stop(void);
    
    ////    Additional operations    ////
    
    //## auto_generated
    const int getPower(void) const;
    
    //## auto_generated
    void setPower(const int p_power);
    
    //## auto_generated
    const int getWeight(void) const;
    
    //## auto_generated
    void setWeight(const int p_weight);
    
    ////    Attributes    ////

private :

    int power;		//## attribute power
    
    int weight;		//## attribute weight
};

#ifdef _OMINSTRUMENT
//#[ ignore
class OMAnimatedEngine : virtual public AOMInstance {
    DECLARE_META(Engine, OMAnimatedEngine)
    
    ////    Framework operations    ////
    
public :

    virtual void serializeAttributes(AOMSAttributes* aomsAttributes) const;
};
//#]
#endif // _OMINSTRUMENT

#endif
/*********************************************************************
	File Path	: DefaultComponent\DefaultConfig\Engine.h
*********************************************************************/
