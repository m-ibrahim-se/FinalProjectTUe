/********************************************************************
	Rhapsody	: 9.0 
	Login		: 20204920
	Component	: DefaultComponent 
	Configuration 	: Calculator_Block_Simulation
	Model Element	: Calculator_Block
//!	Generated Date	: Wed, 19, Oct 2022  
	File Path	: DefaultComponent\Calculator_Block_Simulation\Calculator_Block.cpp
*********************************************************************/

//#[ ignore
#define NAMESPACE_PREFIX
//#]

//## auto_generated
#include "Calculator_Block.h"
//#[ ignore
#define DemoPkg_Calculator_Block_Calculator_Block_SERIALIZE OM_NO_OP
//#]

//## package DemoPkg

//## class Calculator_Block
//#[ ignore
Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionReadXInActivityActivity_diagram_001OfCalculator_Block::ActionReadXInActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& activity, Calculator_Block& context) : OMContextualAction(id, activity), mContext(&context) {
}

OMList<OMString> Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionReadXInActivityActivity_diagram_001OfCalculator_Block::filterPassableFlows() {
    return mContext->delegatedFilterPassableFlowsFromActionReadXInActivityActivity_diagram_001OfCalculator_Block(a);
}

int Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionReadXInActivityActivity_diagram_001OfCalculator_Block::getA() {
    return a;
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionReadXInActivityActivity_diagram_001OfCalculator_Block::serializeTokens(AOMSAttributes& tokens) {
    tokens.setCount(1);
    tokens.addAttribute("a", x2String(a));
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionReadXInActivityActivity_diagram_001OfCalculator_Block::invokeContextMethod() {
    mContext->delegatedInvokeContextMethodFromActionReadXInActivityActivity_diagram_001OfCalculator_Block(a);
}

Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionReadYInActivityActivity_diagram_001OfCalculator_Block::ActionReadYInActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& activity, Calculator_Block& context) : OMContextualAction(id, activity), mContext(&context) {
}

OMList<OMString> Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionReadYInActivityActivity_diagram_001OfCalculator_Block::filterPassableFlows() {
    return mContext->delegatedFilterPassableFlowsFromActionReadYInActivityActivity_diagram_001OfCalculator_Block(b);
}

int Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionReadYInActivityActivity_diagram_001OfCalculator_Block::getB() {
    return b;
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionReadYInActivityActivity_diagram_001OfCalculator_Block::serializeTokens(AOMSAttributes& tokens) {
    tokens.setCount(1);
    tokens.addAttribute("b", x2String(b));
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionReadYInActivityActivity_diagram_001OfCalculator_Block::invokeContextMethod() {
    mContext->delegatedInvokeContextMethodFromActionReadYInActivityActivity_diagram_001OfCalculator_Block(b);
}

Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block::ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& activity, Calculator_Block& context) : OMContextualAction(id, activity), mContext(&context) {
}

OMList<OMString> Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block::filterPassableFlows() {
    return mContext->delegatedFilterPassableFlowsFromActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block(a, b, c);
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block::setA(int value) {
    this->a = value;
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block::setB(int value) {
    this->b = value;
}

int Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block::getC() {
    return c;
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block::serializeTokens(AOMSAttributes& tokens) {
    tokens.setCount(3);
    tokens.addAttribute("a", x2String(a));
    tokens.addAttribute("b", x2String(b));
    tokens.addAttribute("c", x2String(c));
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block::invokeContextMethod() {
    mContext->delegatedInvokeContextMethodFromActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block(a, b, c);
}

Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionWriteZInActivityActivity_diagram_001OfCalculator_Block::ActionWriteZInActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& activity, Calculator_Block& context) : OMContextualAction(id, activity), mContext(&context) {
}

OMList<OMString> Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionWriteZInActivityActivity_diagram_001OfCalculator_Block::filterPassableFlows() {
    return mContext->delegatedFilterPassableFlowsFromActionWriteZInActivityActivity_diagram_001OfCalculator_Block(c);
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionWriteZInActivityActivity_diagram_001OfCalculator_Block::setC(int value) {
    this->c = value;
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionWriteZInActivityActivity_diagram_001OfCalculator_Block::serializeTokens(AOMSAttributes& tokens) {
    tokens.setCount(1);
    tokens.addAttribute("c", x2String(c));
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionWriteZInActivityActivity_diagram_001OfCalculator_Block::invokeContextMethod() {
    mContext->delegatedInvokeContextMethodFromActionWriteZInActivityActivity_diagram_001OfCalculator_Block(c);
}

Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionFinal_Block_01InActivityActivity_diagram_001OfCalculator_Block::ActionFinal_Block_01InActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& activity, Calculator_Block& context) : OMActivityFinalNode(id, activity), mContext(&context) {
}

OMList<OMString> Calculator_Block::Activity_diagram_001OfCalculator_Block::ActionFinal_Block_01InActivityActivity_diagram_001OfCalculator_Block::filterPassableFlows() {
    return mContext->delegatedFilterPassableFlowsFromActionFinal_Block_01InActivityActivity_diagram_001OfCalculator_Block();
}

Calculator_Block::Activity_diagram_001OfCalculator_Block::ControlFork_Block1InActivityActivity_diagram_001OfCalculator_Block::ControlFork_Block1InActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& activity, Calculator_Block& context) : OMForkNode(id, activity), mContext(&context) {
}

OMList<OMString> Calculator_Block::Activity_diagram_001OfCalculator_Block::ControlFork_Block1InActivityActivity_diagram_001OfCalculator_Block::filterPassableFlows() {
    return mContext->delegatedFilterPassableFlowsFromControlFork_Block1InActivityActivity_diagram_001OfCalculator_Block();
}

Calculator_Block::Activity_diagram_001OfCalculator_Block::DataFlow3InActivityActivity_diagram_001OfCalculator_Block::DataFlow3InActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& context, ActionReadXInActivityActivity_diagram_001OfCalculator_Block& sourceAction, ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block& targetAction) : OMDataFlow<int>(id, context, sourceAction, targetAction), dataSource(&sourceAction), dataTarget(&targetAction) {
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::DataFlow3InActivityActivity_diagram_001OfCalculator_Block::giveToken() {
    dataTarget->setA(dequeueToken());
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::DataFlow3InActivityActivity_diagram_001OfCalculator_Block::takeToken() {
    enqueueToken(dataSource->getA());
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::DataFlow3InActivityActivity_diagram_001OfCalculator_Block::serializeTokens(AOMSAttributes& tokens) {
    if(getTokenCount() > 0)
    {
    	tokens.setCount(getTokenCount());
    	for(int i = 0 ; i < getTokenCount() ; i++)
    	{
    		char idx[10];
    		OMitoa(i, idx, 10);
    		tokens.addAttribute(idx, x2String(mTokens.getAt(i)));
    	}
    }
}

Calculator_Block::Activity_diagram_001OfCalculator_Block::DataFlow4InActivityActivity_diagram_001OfCalculator_Block::DataFlow4InActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& context, ActionReadYInActivityActivity_diagram_001OfCalculator_Block& sourceAction, ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block& targetAction) : OMDataFlow<int>(id, context, sourceAction, targetAction), dataSource(&sourceAction), dataTarget(&targetAction) {
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::DataFlow4InActivityActivity_diagram_001OfCalculator_Block::giveToken() {
    dataTarget->setB(dequeueToken());
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::DataFlow4InActivityActivity_diagram_001OfCalculator_Block::takeToken() {
    enqueueToken(dataSource->getB());
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::DataFlow4InActivityActivity_diagram_001OfCalculator_Block::serializeTokens(AOMSAttributes& tokens) {
    if(getTokenCount() > 0)
    {
    	tokens.setCount(getTokenCount());
    	for(int i = 0 ; i < getTokenCount() ; i++)
    	{
    		char idx[10];
    		OMitoa(i, idx, 10);
    		tokens.addAttribute(idx, x2String(mTokens.getAt(i)));
    	}
    }
}

Calculator_Block::Activity_diagram_001OfCalculator_Block::DataFlow6InActivityActivity_diagram_001OfCalculator_Block::DataFlow6InActivityActivity_diagram_001OfCalculator_Block(const OMString& id, Activity_diagram_001OfCalculator_Block& context, ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block& sourceAction, ActionWriteZInActivityActivity_diagram_001OfCalculator_Block& targetAction) : OMDataFlow<int>(id, context, sourceAction, targetAction), dataSource(&sourceAction), dataTarget(&targetAction) {
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::DataFlow6InActivityActivity_diagram_001OfCalculator_Block::giveToken() {
    dataTarget->setC(dequeueToken());
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::DataFlow6InActivityActivity_diagram_001OfCalculator_Block::takeToken() {
    enqueueToken(dataSource->getC());
}

void Calculator_Block::Activity_diagram_001OfCalculator_Block::DataFlow6InActivityActivity_diagram_001OfCalculator_Block::serializeTokens(AOMSAttributes& tokens) {
    if(getTokenCount() > 0)
    {
    	tokens.setCount(getTokenCount());
    	for(int i = 0 ; i < getTokenCount() ; i++)
    	{
    		char idx[10];
    		OMitoa(i, idx, 10);
    		tokens.addAttribute(idx, x2String(mTokens.getAt(i)));
    	}
    }
}

Calculator_Block::Activity_diagram_001OfCalculator_Block::Activity_diagram_001OfCalculator_Block(Calculator_Block& context) : OMActivity(&context), mContext(&context) {
    // Setup nodes
    ActionReadXInActivityActivity_diagram_001OfCalculator_Block* varReadX = new ActionReadXInActivityActivity_diagram_001OfCalculator_Block("activity_diagram_001:ROOT.readX", *this, *mContext);
    ActionReadYInActivityActivity_diagram_001OfCalculator_Block* varReadY = new ActionReadYInActivityActivity_diagram_001OfCalculator_Block("activity_diagram_001:ROOT.readY", *this, *mContext);
    ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block* varAddingNumber = new ActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block("activity_diagram_001:ROOT.AddingNumber", *this, *mContext);
    ActionWriteZInActivityActivity_diagram_001OfCalculator_Block* varWriteZ = new ActionWriteZInActivityActivity_diagram_001OfCalculator_Block("activity_diagram_001:ROOT.writeZ", *this, *mContext);
    ActionFinal_Block_01InActivityActivity_diagram_001OfCalculator_Block* varFinal_Block_01 = new ActionFinal_Block_01InActivityActivity_diagram_001OfCalculator_Block("activity_diagram_001:ROOT.Final_Block_01", *this, *mContext);
    ControlFork_Block1InActivityActivity_diagram_001OfCalculator_Block* varFork_Block1 = new ControlFork_Block1InActivityActivity_diagram_001OfCalculator_Block("activity_diagram_001:ROOT.Fork_Block1", *this, *mContext);
    OMInitialAction* varInitialNode0 = new OMInitialAction("activity_diagram_001:0", *this);
    
    // Setup flows
    new OMControlFlow("activity_diagram_001:0", *this, *varInitialNode0, *varFork_Block1);
    new OMControlFlow("activity_diagram_001:1", *this, *varFork_Block1, *varReadX);
    new OMControlFlow("activity_diagram_001:2", *this, *varFork_Block1, *varReadY);
    new DataFlow3InActivityActivity_diagram_001OfCalculator_Block("activity_diagram_001:3", *this, *varReadX, *varAddingNumber);
    new DataFlow4InActivityActivity_diagram_001OfCalculator_Block("activity_diagram_001:4", *this, *varReadY, *varAddingNumber);
    new OMControlFlow("activity_diagram_001:5", *this, *varWriteZ, *varFinal_Block_01);
    new DataFlow6InActivityActivity_diagram_001OfCalculator_Block("activity_diagram_001:6", *this, *varAddingNumber, *varWriteZ);
}
//#]

Calculator_Block::Calculator_Block() : X(5), Y(7), Z(0) {
    NOTIFY_ACTIVITY_CONSTRUCTOR(Calculator_Block, Calculator_Block(), 0, DemoPkg_Calculator_Block_Calculator_Block_SERIALIZE);
}

Calculator_Block::~Calculator_Block() {
    NOTIFY_DESTRUCTOR(~Calculator_Block, true);
}

//#[ ignore
OMActivity* Calculator_Block::createMainActivity() {
    return new Activity_diagram_001OfCalculator_Block(*this);
}

void* Calculator_Block::getMe() {
    return this;
}
//#]

OMList<OMString> Calculator_Block::delegatedFilterPassableFlowsFromActionReadXInActivityActivity_diagram_001OfCalculator_Block(int& a) {
    //#[ activity_action activity_diagram_001:ROOT.readX
    OMList<OMString> ans;
    ans.add("activity_diagram_001:3");
    return ans;
    //#]
}

void Calculator_Block::delegatedInvokeContextMethodFromActionReadXInActivityActivity_diagram_001OfCalculator_Block(int& a) {
    //#[ activity_action activity_diagram_001:ROOT.readX
    a=getX();
    //#]
}

OMList<OMString> Calculator_Block::delegatedFilterPassableFlowsFromActionReadYInActivityActivity_diagram_001OfCalculator_Block(int& b) {
    //#[ activity_action activity_diagram_001:ROOT.readY
    OMList<OMString> ans;
    ans.add("activity_diagram_001:4");
    return ans;
    //#]
}

void Calculator_Block::delegatedInvokeContextMethodFromActionReadYInActivityActivity_diagram_001OfCalculator_Block(int& b) {
    //#[ activity_action activity_diagram_001:ROOT.readY
    b=getY();
    //#]
}

OMList<OMString> Calculator_Block::delegatedFilterPassableFlowsFromActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block(int a, int b, int& c) {
    //#[ activity_action activity_diagram_001:ROOT.AddingNumber
    OMList<OMString> ans;
    ans.add("activity_diagram_001:6");
    return ans;
    //#]
}

void Calculator_Block::delegatedInvokeContextMethodFromActionAddingNumberInActivityActivity_diagram_001OfCalculator_Block(int a, int b, int& c) {
    //#[ activity_action activity_diagram_001:ROOT.AddingNumber
    c = a + b;
    
    //#]
}

OMList<OMString> Calculator_Block::delegatedFilterPassableFlowsFromActionWriteZInActivityActivity_diagram_001OfCalculator_Block(int c) {
    //#[ activity_action activity_diagram_001:ROOT.writeZ
    OMList<OMString> ans;
    ans.add("activity_diagram_001:5");
    return ans;
    //#]
}

void Calculator_Block::delegatedInvokeContextMethodFromActionWriteZInActivityActivity_diagram_001OfCalculator_Block(int c) {
    //#[ activity_action activity_diagram_001:ROOT.writeZ
    setZ(c);
    //#]
}

OMList<OMString> Calculator_Block::delegatedFilterPassableFlowsFromActionFinal_Block_01InActivityActivity_diagram_001OfCalculator_Block() {
    //#[ activity_action activity_diagram_001:ROOT.Final_Block_01
    OMList<OMString> ans;
    return ans;
    //#]
}

OMList<OMString> Calculator_Block::delegatedFilterPassableFlowsFromControlFork_Block1InActivityActivity_diagram_001OfCalculator_Block() {
    //#[ activity_control activity_diagram_001:ROOT.Fork_Block1
    OMList<OMString> ans;
    ans.add("activity_diagram_001:1");
    ans.add("activity_diagram_001:2");
    return ans;
    //#]
}

int Calculator_Block::getX() const {
    return X;
}

void Calculator_Block::setX(int p_X) {
    X = p_X;
}

int Calculator_Block::getY() const {
    return Y;
}

void Calculator_Block::setY(int p_Y) {
    Y = p_Y;
}

int Calculator_Block::getZ() const {
    return Z;
}

void Calculator_Block::setZ(int p_Z) {
    Z = p_Z;
}

bool Calculator_Block::startBehavior() {
    bool done = false;
    done = OMActivityContext::startBehavior();
    return done;
}

#ifdef _OMINSTRUMENT
//#[ ignore
void OMAnimatedCalculator_Block::serializeAttributes(AOMSAttributes* aomsAttributes) const {
    aomsAttributes->addAttribute("X", x2String(myReal->X));
    aomsAttributes->addAttribute("Z", x2String(myReal->Z));
    aomsAttributes->addAttribute("Y", x2String(myReal->Y));
}
//#]

IMPLEMENT_ACTIVITY_META_P(Calculator_Block, DemoPkg, DemoPkg, false, OMAnimatedCalculator_Block)
#endif // _OMINSTRUMENT

/*********************************************************************
	File Path	: DefaultComponent\Calculator_Block_Simulation\Calculator_Block.cpp
*********************************************************************/
