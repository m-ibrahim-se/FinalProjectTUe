fpath = 'C:\Users\20204920\Downloads\Final_Project\SimulinkParserwithMatLab\Simulink\Models\SubSystem_In_Outport_Checking_Model_V1.slx';

[folder, baseFileNameNoExt, extension] = fileparts(fpath);
modelNameOnly = baseFileNameNoExt;
modelId = regexprep(modelNameOnly, ' ','_');
modelType = extension;

jsonData = "";   
loadedModel = load_system(fpath);


string1 = "inport1 outport2";
disp(regexprep(string1,'(\<\w)', '${upper($1)}'));

% gcs returns the path name of the current system (model)
%eval([gcs,'([],[],[],''compile'');']) 

get_block = find_system(loadedModel,'FindAll','on','FollowLinks','on','type','block');
 for i= 1:size(get_block, 1) %for each block in the list do    
    %Block info    
    eachBlockInfo = get(get_block(i));
    blockName = replace(replace(get(get_block(i), 'Name'), newline,' '),'"',"'");
    blockType = replace(replace(get(get_block(i), 'BlockType'), newline,' '),'"',"'");
    parentBlock = replace(replace(get(get_block(i), 'Parent'), newline,' '),'"',"'");
    if strcmp(blockType,"Inport")
        disp(eachBlockInfo.Parent);
        if strcmp(blockName,'In2')        
            parent_Block = get_param(parentBlock, 'BlockType');
            parent_Block_PortHandle = get_param(parentBlock,'PortHandles');
            inPort = parent_Block_PortHandle.Inport;
            disp(get(inPort, 'PortNumber'));
            disp(parent_Block_Handle)        
        end  
    elseif (strcmp(blockType,'Outport'))
        portBlockName = replace(strcat(portType,"",num2str(portNumber)), newline,' ');
        portBlockId = replace(strcat(portParent,"/",portBlockName), newline,' ');
        parentBlockId = replace(portParent, newline,' ');
        
    end
    
 end

block_info = get(get_param('SubSystem_In_Outport_Checking_Model_V1/Subsystem1/In1','LineHandles'));
in_src_handle = getfullname(get_param(blk_lines.Inport,'SrcBlockHandle'));
in_src_obj = get_param(in_src_handle,'ObjectParameters'); % If chosen subsystem has only one outport
 
in_dst_handle = getfullname(get_param(blk_lines.Inport,'DstBlockHandle'));
out_blockInfo = get(get_param(blk_lines.Inport,'DstBlockHandle'));
in_dst_obj2 = get_param(in_dst_handle,'ObjectParameters'); % If chosen subsystem has only one outport

out_src_handle = getfullname(get_param(blk_lines.Outport,'SrcBlockHandle'));
in_blockInfo = get(get_param(blk_lines.Outport,'SrcBlockHandle'));
dut_src_obj = get_param(out_src_handle,'ObjectParameters'); % If chosen subsystem has only one outport

out_dst_handle = getfullname(get_param(blk_lines.Outport,'DstBlockHandle'));
out_dst_obj2 = get_param(out_dst_handle,'ObjectParameters'); % If chosen subsystem has only one outport

get_block = find_system(loadedModel,'FindAll','on','FollowLinks','on','type','block');
for i= 1:size(get_block, 1) %for each block in the list do    
    %Block info    
    eachBlockInfo = get(get_block(i));
    
    blockName = replace(replace(get(get_block(i), 'Name'), newline,' '),'"',"'");
    blockType = replace(get(get_block(i), 'BlockType'), newline,' ');
    parentBlock = replace(get(get_block(i), 'Parent'), newline,' ');
    %blockHandle = get(get_block(i), 'Handle');  
    blockDescription = replace(replace(get(get_block(i),'BlockDescription'), newline,' '),'"',"'");
    isCommented = get(get_block(i), 'Commented');               

    handle = get(get_block(i),'Handle');
    handleId = num2str(handle);
    disp(handleId);
    portHandles = get(get_block(i), 'PortHandles');  
%    disp(strcat("Block:",get(get_block(i), 'CompiledPortDataType')))
    
    numberOfInputPort = size(portHandles.Inport,2);
    numberOfOutputPort = size(portHandles.Outport,2);
    blockId = strcat(parentBlock,"/",blockName);

    % prepare and insert block/subsystem block node information
    elementInfo.id = blockId;
    elementInfo.type = 'node';
    if strcmp(blockType,'SubSystem')
        %disp('Inside SubSystem');
        elementInfo.labels = {'SubSystem'};        
    elseif strcmp(blockType,'ModelReference')
        elementInfo.labels = {'ReferenceModel'};
    else
        elementInfo.labels = {'Block'};
    end

    elementInfo.properties.name = blockName;
    %elementInfo.properties.elementId = num2str(get(get_block(i), 'Handle'));
    elementInfo.properties.type = blockType;
    if strcmp(blockType,'ModelReference')
        %elementInfo.labels = {'Block','ModelReference'};  
        elementInfo.properties.referenceModelName = replace(get(get_block(i),'ModelName'), newline,' ');                    
    end

    elementInfo.properties.description = blockDescription;
    %elementInfo.properties.outDataType = ""; %% not able to get this
        %property. This is block specific property
    elementInfo.properties.numberOfInputPort = numberOfInputPort;
    elementInfo.properties.numberOfOutputPort = numberOfOutputPort;
    elementInfo.properties.isCommented = isCommented;     

    jsonData = jsonData + jsonencode(elementInfo) + newline;
    disp(jsonencode(elementInfo));

    %newLine = {blockId, blockName, blockType, '''''', 'Element', '''''', ''''''};
    %nodeTempTable = [nodeTempTable ; newLine];  
    disp(['Added node for ' blockName]);

    % prepare and insert block/subsystem block relationship information
    % with immediate parent node
    relationshipInfo.id = "";
    relationshipInfo.type = 'relationship';
    relationshipInfo.label = 'CONTAINS';
    relationshipInfo.properties.type = "element"; %% default properties now
    relationshipInfo.start.id = parentBlock; % as source node
    relationshipInfo.end.id = blockId; % as destination node
    jsonData = jsonData + jsonencode(relationshipInfo) + newline;
    disp(jsonencode(relationshipInfo));

    %newLineContain = {modelId, blockId, '''''', '''''', 'CONTAINS', '''''', ''''''};    
    %edgeTempTable = [edgeTempTable ; newLineContain];  
    disp(strcat("Added edge for  ", parentBlock, "-->", blockName));

    % Create Subsystem's Inport and Outport Blocks relationship with
    % SubSystem input and output ports
    parent
    if strcmp(blockType,'SubSystem')
        inOutPortRelationshipInfo.id = "";
        inOutPortRelationshipInfo.type = 'relationship';
        inOutPortRelationshipInfo.label = 'CONNECTED_WITH';
        inOutPortRelationshipInfo.properties.type = "connection"; %% default properties now
        inOutPortRelationshipInfo.start.id = parentBlock; % as source node
        inOutPortRelationshipInfo.end.id = blockId; % as destination node
        jsonData = jsonData + jsonencode(inOutPortRelationshipInfo) + newline;
        disp(jsonencode(inOutPortRelationshipInfo));
    end
        
    %Create the model reference relationship

    if strcmp(blockType,'ModelReference')
        referenceModelId = replace(get(get_block(i),'ModelName'), newline,' ');  
        referenceRelationshipInfo.id = "";
        referenceRelationshipInfo.type = 'relationship';
        referenceRelationshipInfo.label = 'HAS_REFERENCE_OF';
        referenceRelationshipInfo.properties.type = "reference"; %% default properties now
        referenceRelationshipInfo.start.id = blockId; % current block as source node
        referenceRelationshipInfo.end.id = referenceModelId; % Actual model as destination node
        jsonData = jsonData + jsonencode(referenceRelationshipInfo) + newline;
        disp(jsonencode(referenceRelationshipInfo));
        disp(strcat("Added model reference edge for  ", blockId, "-->", referenceModelId));
    end

    %extract all the blocks that receive input from the current block.
    %sysOuts = find_system(blockHandle, 'type', 'line');

    %disp(sysOuts);
    %insert all the adjacent blocks into adjacent list.

    %while blocks in the adjacent list not empty do
    %while 
        %add a output dependency edge from the adjacent block to the current block.
    %end
    %end

end
% compile the model - gcs returns the path name of the current system (model)
disp(strcat('start mode: ',gcs));
try
    eval([gcs,'([],[],[],''compile'');']);
catch 
    disp("ignored");
end            
get_port = find_system(loadedModel,'FindAll','on','FollowLinks','on','type','port');
for i = 1:size(get_port, 1)
    port = get_port(i);        
    eachPortInfo = get(get_port(i));

    type = get(port, 'Type');
    portType= get(port, 'PortType');
    portNumber= get(port, 'PortNumber');    
    portParent= replace(get(port, 'Parent'),'//','/'); %% in mdl model I found an exceptional case with this
    portLine= get(port, 'Line');
    pHandle = get(port, 'Handle');
    %portHandle = get(port, 'PortHandle');
    % port node
    portBlockName = replace(strcat(portType,"",num2str(portNumber)), newline,' ');
    portBlockId = replace(strcat(portParent,"/",portBlockName), newline,' ');
    parentBlockId = replace(portParent, newline,' ');

    % prepare and insert port node information
    portElementInfo.id = portBlockId;
    portElementInfo.type = 'node';
    portElementInfo.labels = {'Port'};
    portElementInfo.properties.name = portBlockName;
    %portElementInfo.properties.elementId = num2str(get(get_port(i), 'Handle'));
    portElementInfo.properties.type = portType;
    portElementInfo.properties.portNumber = portNumber;
    
    try
        portElementInfo.properties.dataType = replace(replace(get(port, 'CompiledPortDataType'), newline,' '),'"',"'");
    catch 
        portElementInfo.properties.dataType = "";
    end
                
    jsonData = jsonData + jsonencode(portElementInfo) + newline;
    disp(jsonencode(portElementInfo));

    disp(strcat("Added node of ",parentBlockId,"--->",portBlockName));

    % Prepare and insert Port Node Relationship with Parent node
    if(portType=="inport")        
        % get the port datatype
        %disp(strcat("Inport:",get(port, 'CompiledPortDataType')));%get_param(h.Outport,'CompiledPortDataType')));
        inportRelationshipInfo.id = "";
        inportRelationshipInfo.type = 'relationship';
        inportRelationshipInfo.label = 'HAS_PORT'; %%% At this moment inport and outport both are same
        inportRelationshipInfo.properties.type = "inport"; %% default properties now
        inportRelationshipInfo.start.id = parentBlockId; %%% At this moment inport and outport both are same
        inportRelationshipInfo.end.id = portBlockId;
        jsonData = jsonData + jsonencode(inportRelationshipInfo) + newline;
        disp(jsonencode(inportRelationshipInfo));
        %disp(jsonencode(elementInfo));     
        disp(strcat("Added relationship of ",parentBlockId,"--->",portBlockName));
    elseif(portType=="outport")      
        % get the port datatype
        %disp(strcat("Outport:",get(port, 'CompiledPortDataType')));%disp(strcat("inport:",get_param(h.Inport,'CompiledPortDataType')));
        outportRelationshipInfo.id = "";
        outportRelationshipInfo.type = 'relationship';
        outportRelationshipInfo.label = 'HAS_PORT';
        outportRelationshipInfo.properties.type = "outport"; %% default properties now
        outportRelationshipInfo.start.id = parentBlockId;
        outportRelationshipInfo.end.id = portBlockId;
        jsonData = jsonData + jsonencode(outportRelationshipInfo) + newline;
        disp(jsonencode(outportRelationshipInfo));    
        disp(strcat("Added relationship of ",parentBlockId,"--->",portBlockName));      
    end 
    
 
%     % compile the model
%     %name = strcat(modelNameOnly,'/Step Block New');
%     h = get_param('SignalTestingModel_v1/Scope','Porthandles');
%     % get the port handles
%     disp(strcat("Outport:",get_param(h.Outport,'CompiledPortDataType')));
% 
%     disp(strcat("inport:",get_param(h.Inport,'CompiledPortDataType')));
%     % get the output port data type
    %SignalTestingModel_v1([],[],[],'term')  
    
end
% terminate the compilation
disp(strcat('End model: ',gcs));
try
    eval([gcs,'([],[],[],''term'')']);
catch 
    disp("ignored");
end
%%% Prepare Signal or Line information as edge
get_line = find_system(loadedModel,'FindAll','on','FollowLinks','on','type','line');

for i = 1:size(get_line, 1)
    line = get_line(i);        
    eachLineInfo = get(get_line(i));
    %disp(strcat("Line:",get(line, 'CompiledPortDataType')));
    signal_segmentType= get(line, 'SegmentType');
    signal_sourcePort= get(line, 'SourcePort');
    signal_destPort= get(line, 'DstPort');
    %signal_signalName= get(line, 'Name');
    signal_signalType= get(line, 'Type');
    %signal_signalAnnotation = get(line, 'Annotation');
    %signal_portDataType = get(line, 'DataType');
    sourcePorthandle = get(line, 'SrcPortHandle');
    %signal_sourcePortHandleInfo = get(sourcePorthandle);
    dstPorthandle = get(line, 'DstPortHandle');

    source_block_handle = get_param(line, 'SrcBlockHandle');
    destination_block_handle = get_param(line, 'DstBlockHandle');    

    src_block_name = get_param(source_block_handle, 'name');
    dst_block_name = get_param(destination_block_handle, 'name');
    dst_block_count = size(dst_block_name,1);

    %disp(strcat(src_block_name, "-->", dst_block_name));
    if dst_block_count == 1
        sourceporttype = get(sourcePorthandle, 'PortType');
        sourcePortNumber= get(sourcePorthandle, 'PortNumber');    
        sourcePortParent= strcat(sourceporttype,"",num2str(sourcePortNumber));

        dstPorttype = get(dstPorthandle, 'PortType');
        dstPortNumber= get(dstPorthandle, 'PortNumber');    
        dstPortParent= strcat(dstPorttype,"",num2str(dstPortNumber));

        sourcePortPathId = replace(strcat(get(source_block_handle, 'Parent'),"/",src_block_name,"/",sourcePortParent), newline,' ');
        dstPortPathId = replace(strcat(get(destination_block_handle, 'Parent'),"/",dst_block_name,"/",dstPortParent), newline,' ');

        % Prepare and insert Signal/Line relationship with Port nodes
        lineRelationshipInfo.id = "";
        lineRelationshipInfo.type = 'relationship';
        lineRelationshipInfo.label = 'SEND_DATA_TO';
        %lineRelationshipInfo.properties.Annotation = ""; %% no properties now
        %lineRelationshipInfo.properties.name = signal_signalName; 
        lineRelationshipInfo.properties.segmentType = signal_segmentType;
        lineRelationshipInfo.properties.type = signal_signalType;        
        lineRelationshipInfo.start.id = sourcePortPathId;
        lineRelationshipInfo.end.id = dstPortPathId;
        jsonData = jsonData + jsonencode(lineRelationshipInfo) + newline;
        disp(jsonencode(lineRelationshipInfo));

        disp(strcat("Added edge info from ", sourcePortPathId, "-->", dstPortPathId));        
    end     
end  

