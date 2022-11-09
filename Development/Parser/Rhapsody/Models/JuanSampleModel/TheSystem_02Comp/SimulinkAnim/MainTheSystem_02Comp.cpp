/********************************************************************
	Rhapsody	: 9.0 
	Login		: 20204920
	Component	: TheSystem_02Comp 
	Configuration 	: SimulinkAnim
	Model Element	: SimulinkAnim
//!	Generated Date	: Thu, 3, Nov 2022  
	File Path	: TheSystem_02Comp\SimulinkAnim\MainTheSystem_02Comp.cpp
*********************************************************************/

//## auto_generated
#include "MainTheSystem_02Comp.h"
//## auto_generated
#include "InterConnection.h"
TheSystem_02Comp::TheSystem_02Comp() {
    InterConnection_initRelations();
}

int main(int argc, char* argv[]) {
    int status = 0;
    if(OXF::initialize(argc, argv, 6423))
        {
            TheSystem_02Comp initializer_TheSystem_02Comp;
            //#[ configuration TheSystem_02Comp::SimulinkAnim 
            //#]
            OXF::start();
            status = 0;
        }
    else
        {
            status = 1;
        }
    return status;
}

/*********************************************************************
	File Path	: TheSystem_02Comp\SimulinkAnim\MainTheSystem_02Comp.cpp
*********************************************************************/
