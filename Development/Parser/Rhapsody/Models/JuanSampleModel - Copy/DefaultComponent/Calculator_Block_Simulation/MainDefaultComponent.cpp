/********************************************************************
	Rhapsody	: 9.0 
	Login		: 20204920
	Component	: DefaultComponent 
	Configuration 	: Calculator_Block_Simulation
	Model Element	: Calculator_Block_Simulation
//!	Generated Date	: Wed, 19, Oct 2022  
	File Path	: DefaultComponent\Calculator_Block_Simulation\MainDefaultComponent.cpp
*********************************************************************/

//## auto_generated
#include "MainDefaultComponent.h"
//## auto_generated
#include "Calculator_Block.h"
int main(int argc, char* argv[]) {
    int status = 0;
    if(OXF::initialize(argc, argv, 6423))
        {
            Calculator_Block * p_Calculator_Block;
            p_Calculator_Block = new Calculator_Block;
            p_Calculator_Block->startBehavior();
            //#[ configuration DefaultComponent::Calculator_Block_Simulation 
            //#]
            OXF::start();
            delete p_Calculator_Block;
            status = 0;
        }
    else
        {
            status = 1;
        }
    return status;
}

/*********************************************************************
	File Path	: DefaultComponent\Calculator_Block_Simulation\MainDefaultComponent.cpp
*********************************************************************/
