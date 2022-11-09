/********************************************************************
	Rhapsody	: 8.2 
	Login		: ibarosan
	Component	: DefaultComponent 
	Configuration 	: DefaultConfig
	Model Element	: Engine
//!	Generated Date	: Wed, 10, Apr 2019  
	File Path	: DefaultComponent\DefaultConfig\Engine.cpp
*********************************************************************/

//#[ ignore
#define NAMESPACE_PREFIX
//#]

//## auto_generated
#include "Engine.h"
//#[ ignore
#define STRUCTURE_Engine_Engine_SERIALIZE OM_NO_OP

#define STRUCTURE_Engine_accelerate_SERIALIZE OM_NO_OP

#define STRUCTURE_Engine_brake_SERIALIZE OM_NO_OP

#define STRUCTURE_Engine_start_SERIALIZE OM_NO_OP

#define STRUCTURE_Engine_stop_SERIALIZE OM_NO_OP
//#]

//## package STRUCTURE

//## class Engine
Engine::Engine(void) {
    NOTIFY_CONSTRUCTOR(Engine, Engine(), 0, STRUCTURE_Engine_Engine_SERIALIZE);
}

Engine::~Engine(void) {
    NOTIFY_DESTRUCTOR(~Engine, true);
}

void Engine::accelerate(void) {
    NOTIFY_OPERATION(accelerate, accelerate(), 0, STRUCTURE_Engine_accelerate_SERIALIZE);
    //#[ operation accelerate()
    //#]
}

void Engine::brake(void) {
    NOTIFY_OPERATION(brake, brake(), 0, STRUCTURE_Engine_brake_SERIALIZE);
    //#[ operation brake()
    //#]
}

void Engine::start(void) {
    NOTIFY_OPERATION(start, start(), 0, STRUCTURE_Engine_start_SERIALIZE);
    //#[ operation start()
    //#]
}

void Engine::stop(void) {
    NOTIFY_OPERATION(stop, stop(), 0, STRUCTURE_Engine_stop_SERIALIZE);
    //#[ operation stop()
    //#]
}

const int Engine::getPower(void) const {
    return power;
}

void Engine::setPower(const int p_power) {
    power = p_power;
}

const int Engine::getWeight(void) const {
    return weight;
}

void Engine::setWeight(const int p_weight) {
    weight = p_weight;
}

#ifdef _OMINSTRUMENT
//#[ ignore
void OMAnimatedEngine::serializeAttributes(AOMSAttributes* aomsAttributes) const {
    aomsAttributes->addAttribute("power", x2String(myReal->power));
    aomsAttributes->addAttribute("weight", x2String(myReal->weight));
}
//#]

IMPLEMENT_META_P(Engine, STRUCTURE, STRUCTURE, false, OMAnimatedEngine)
#endif // _OMINSTRUMENT

/*********************************************************************
	File Path	: DefaultComponent\DefaultConfig\Engine.cpp
*********************************************************************/
