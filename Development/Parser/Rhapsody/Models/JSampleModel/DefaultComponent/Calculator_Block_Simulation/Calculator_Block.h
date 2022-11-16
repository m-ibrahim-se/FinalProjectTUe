/*********************************************************************
	Rhapsody	: 9.0 
	Login		: 20204920
	Component	: DefaultComponent 
	Configuration 	: Calculator_Block_Simulation
	Model Element	: Calculator_Block
//!	Generated Date	: Wed, 19, Oct 2022  
	File Path	: DefaultComponent\Calculator_Block_Simulation\Calculator_Block.h
*********************************************************************/

#ifndef Calculator_Block_H
#define Calculator_Block_H

//## auto_generated
#include <oxf\oxf.h>
//## auto_generated
#include <..\Profiles\SysML\SIDefinitions.h>
//## auto_generated
#include <aom\aom.h>
//##   ignore
#include <simulation\OMActivityContext.h>
//## auto_generated
#include "DemoPkg.h"
//## package DemoPkg

//## class Calculator_Block
class Calculator_Block : virtual public OMActivityContext {
public :

    //## auto_generated
    class Activity_diagram_001OfCalculator_Block;
    
//#[ ignore
    class Activity_diagram_001OfCalculator_Block : virtual public OMActivity {
    public :
    
        class ActionReadXInActivityActivity_diagram_001OfCalculator_Block : virtual public OMContextualAction {
            ////    Constructors and destructors    ////
            
        public :
        
            ActionReadXInActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& activity, Calculator_Block& context);
            
            virtual OMList<OMString> filterPassableFlows();
            
            virtual int getA();
            
            void serializeTokens(AOMSAttributes& tokens);
            
            virtual void invokeContextMethod();
            
            ////    Framework operations    ////
            
            ////    Framework    ////
            
            Calculator_Block* mContext;
            
            int a;
        };
        
        class ActionReadYInActivityActivity_diagram_001OfCalculator_Block : virtual public OMContextualAction {
            ////    Constructors and destructors    ////
            
        public :
        
            ActionReadYInActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& activity, Calculator_Block& context);
            
            virtual OMList<OMString> filterPassableFlows();
            
            virtual int getB();
            
            void serializeTokens(AOMSAttributes& tokens);
            
            virtual void invokeContextMethod();
            
            ////    Framework operations    ////
            
            ////    Framework    ////
            
            Calculator_Block* mContext;
            
            int b;
        };
        
        class ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block : virtual public OMContextualAction {
            ////    Constructors and destructors    ////
            
        public :
        
            ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& activity, Calculator_Block& context);
            
            virtual OMList<OMString> filterPassableFlows();
            
            virtual void setA(int value);
            
            virtual void setB(int value);
            
            virtual int getC();
            
            void serializeTokens(AOMSAttributes& tokens);
            
            virtual void invokeContextMethod();
            
            ////    Framework operations    ////
            
            ////    Framework    ////
            
            Calculator_Block* mContext;
            
            int a;
            
            int b;
            
            int c;
        };
        
        class ActionWriteZInActivityActivity_diagram_001OfCalculator_Block : virtual public OMContextualAction {
            ////    Constructors and destructors    ////
            
        public :
        
            ActionWriteZInActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& activity, Calculator_Block& context);
            
            virtual OMList<OMString> filterPassableFlows();
            
            virtual void setC(int value);
            
            void serializeTokens(AOMSAttributes& tokens);
            
            virtual void invokeContextMethod();
            
            ////    Framework operations    ////
            
            ////    Framework    ////
            
            Calculator_Block* mContext;
            
            int c;
        };
        
        class ActionFinal_Block_01InActivityActivity_diagram_001OfCalculator_Block : virtual public OMActivityFinalNode {
            ////    Constructors and destructors    ////
            
        public :
        
            ActionFinal_Block_01InActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& activity, Calculator_Block& context);
            
            virtual OMList<OMString> filterPassableFlows();
            
            ////    Framework operations    ////
            
            ////    Framework    ////
            
            Calculator_Block* mContext;
        };
        
        class ControlFork_Block1InActivityActivity_diagram_001OfCalculator_Block : virtual public OMForkNode {
            ////    Constructors and destructors    ////
            
        public :
        
            ControlFork_Block1InActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& activity, Calculator_Block& context);
            
            virtual OMList<OMString> filterPassableFlows();
            
            ////    Framework operations    ////
            
            ////    Framework    ////
            
            Calculator_Block* mContext;
        };
        
        class DataFlow3InActivityActivity_diagram_001OfCalculator_Block : virtual public OMDataFlow<int> {
            ////    Constructors and destructors    ////
            
        public :
        
            DataFlow3InActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& context, ActionReadXInActivityActivity_diagram_001OfCalculator_Block& sourceAction, ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block& targetAction);
            
            virtual void giveToken();
            
            virtual void takeToken();
            
            void serializeTokens(AOMSAttributes& tokens);
            
            ////    Framework operations    ////
            
            ////    Framework    ////
            
            ActionReadXInActivityActivity_diagram_001OfCalculator_Block* dataSource;
            
            ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block* dataTarget;
        };
        
        class DataFlow4InActivityActivity_diagram_001OfCalculator_Block : virtual public OMDataFlow<int> {
            ////    Constructors and destructors    ////
            
        public :
        
            DataFlow4InActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& context, ActionReadYInActivityActivity_diagram_001OfCalculator_Block& sourceAction, ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block& targetAction);
            
            virtual void giveToken();
            
            virtual void takeToken();
            
            void serializeTokens(AOMSAttributes& tokens);
            
            ////    Framework operations    ////
            
            ////    Framework    ////
            
            ActionReadYInActivityActivity_diagram_001OfCalculator_Block* dataSource;
            
            ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block* dataTarget;
        };
        
        class DataFlow6InActivityActivity_diagram_001OfCalculator_Block : virtual public OMDataFlow<int> {
            ////    Constructors and destructors    ////
            
        public :
        
            DataFlow6InActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& context, ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block& sourceAction, ActionWriteZInActivityActivity_diagram_001OfCalculator_Block& targetAction);
            
            virtual void giveToken();
            
            virtual void takeToken();
            
            void serializeTokens(AOMSAttributes& tokens);
            
            ////    Framework operations    ////
            
            ////    Framework    ////
            
            ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block* dataSource;
            
            ActionWriteZInActivityActivity_diagram_001OfCalculator_Block* dataTarget;
        };
        
        ////    Constructors and destructors    ////
        
        Activity_diagram_001OfCalculator_Block(Calculator_Block& context);
        
        ////    Framework    ////
        
        Calculator_Block* mContext;
    };
//#]

    ////    Friends    ////
    
#ifdef _OMINSTRUMENT
    friend class OMAnimatedCalculator_Block;
#endif // _OMINSTRUMENT

    ////    Constructors and destructors    ////
    
    //## auto_generated
    Calculator_Block();
    
    //## auto_generated
    ~Calculator_Block();
    
//#[ ignore
    virtual OMActivity* createMainActivity();
    
    virtual void* getMe();
//#]

    //## activity_action activity_diagram_001:ROOT.readX
    OMList<OMString> delegatedFilterPassableFlowsFromActionReadXInActivityActivity_diagram_001OfCalculator_Block(int& a);
    
    //## activity_action activity_diagram_001:ROOT.readX
    void delegatedInvokeContextMethodFromActionReadXInActivityActivity_diagram_001OfCalculator_Block(int& a);
    
    //## activity_action activity_diagram_001:ROOT.readY
    OMList<OMString> delegatedFilterPassableFlowsFromActionReadYInActivityActivity_diagram_001OfCalculator_Block(int& b);
    
    //## activity_action activity_diagram_001:ROOT.readY
    void delegatedInvokeContextMethodFromActionReadYInActivityActivity_diagram_001OfCalculator_Block(int& b);
    
    //## activity_action activity_diagram_001:ROOT.AddingNumber
    OMList<OMString> delegatedFilterPassableFlowsFromActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block(int a, int b, int& c);
    
    //## activity_action activity_diagram_001:ROOT.AddingNumber
    void delegatedInvokeContextMethodFromActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block(int a, int b, int& c);
    
    //## activity_action activity_diagram_001:ROOT.writeZ
    OMList<OMString> delegatedFilterPassableFlowsFromActionWriteZInActivityActivity_diagram_001OfCalculator_Block(int c);
    
    //## activity_action activity_diagram_001:ROOT.writeZ
    void delegatedInvokeContextMethodFromActionWriteZInActivityActivity_diagram_001OfCalculator_Block(int c);
    
    //## activity_action activity_diagram_001:ROOT.Final_Block_01
    OMList<OMString> delegatedFilterPassableFlowsFromActionFinal_Block_01InActivityActivity_diagram_001OfCalculator_Block();
    
    //## activity_control activity_diagram_001:ROOT.Fork_Block1
    OMList<OMString> delegatedFilterPassableFlowsFromControlFork_Block1InActivityActivity_diagram_001OfCalculator_Block();
    
    ////    Additional operations    ////
    
    //## auto_generated
    int getX() const;
    
    //## auto_generated
    void setX(int p_X);
    
    //## auto_generated
    int getY() const;
    
    //## auto_generated
    void setY(int p_Y);
    
    //## auto_generated
    int getZ() const;
    
    //## auto_generated
    void setZ(int p_Z);
    
    //## auto_generated
    virtual bool startBehavior();
    
    ////    Attributes    ////

protected :

    int X;		//## attribute X
    
    int Y;		//## attribute Y
    
    int Z;		//## attribute Z
    
    ////    Framework operations    ////
};

#ifdef _OMINSTRUMENT
//#[ ignore
class OMAnimatedCalculator_Block : virtual public AOMInstance {
    ////    Framework operations    ////
    
public :

    virtual void serializeAttributes(AOMSAttributes* aomsAttributes) const;

    DECLARE_ACTIVITY_META(Calculator_Block, OMAnimatedCalculator_Block)
};
//#]
#endif // _OMINSTRUMENT

#endif
/*********************************************************************
	File Path	: DefaultComponent\Calculator_Block_Simulation\Calculator_Block.h
*********************************************************************/
