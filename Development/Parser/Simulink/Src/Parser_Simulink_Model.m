function [file_count] = Parse_Simulink_Model()
    fullPath = mfilename('fullpath');
    onlyFileName = mfilename;
    currentFolder = erase(fullPath,onlyFileName);
    rootDir = erase(currentFolder,'Src\');

    parsedDataFilePath = fullfile(rootDir,'ParsedDataFiles\');
    modelFilesPath = fullfile(rootDir,'Models\');
    %mdlFileList = dir(fullfile(modelFilesPath,'**\*.slx'));
    allList = dir(fullfile(modelFilesPath,'**\')); %% consider all subfolder and files
    allModelFiles = allList(~[allList.isdir]);
    
    created_file_count = 0;
    delete(fullfile(parsedDataFilePath,'*')) % delete existing files

    for files= 1:size(allModelFiles, 1)
        fileName = allModelFiles(files).name;
        folderName = allModelFiles(files).folder;
        fileFullPath = fullfile(folderName,fileName);
        
        [folder, baseFileNameNoExt, extension] = fileparts(fileFullPath);
        if(strcmp(extension,'.mdl') || strcmp(extension,'.slx'))
            jsonData = "";        
            modelNameOnly = replace(baseFileNameNoExt, newline,' ');
            modelId = modelNameOnly; 
            modelType = extension;

            dataFileName = replace(erase(fullfile(folder,baseFileNameNoExt),modelFilesPath),'\','-');
            jsonDataFilePath = fullfile(parsedDataFilePath,strcat(dataFileName,'.json'));

            loadedModel = load_system(fileFullPath); 
            
            %%%%%%%% construct Simulink %%%%%%%%
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			modelInfo = '';
            modelInfo.id = replace(modelId, newline,' ');
            modelInfo.type = 'node';
            modelInfo.labels = {'Model'};
            modelInfo.properties.name = replace(replace(get_param(modelNameOnly, 'Name'), newline,' '),'"',"'");
            modelInfo.properties.type = replace(replace(get_param(modelNameOnly, 'Type'), newline,' '),'"',"'");
            modelInfo.properties.extension = modelType;
            modelInfo.properties.createdDate = get_param(modelNameOnly, 'Created');
            modelInfo.properties.creator = replace(get_param(modelNameOnly, 'Creator'), newline,' ');
            modelInfo.properties.modifiedBy = replace(get_param(modelNameOnly, 'ModifiedBy'), newline,' ');
            modelInfo.properties.modifiedDate = replace(get_param(modelNameOnly, 'ModifiedDate'), newline,' ');
            modelInfo.properties.modifiedComment = replace(replace(get_param(modelNameOnly, 'ModifiedComment'), newline,' '),'"',"'");
            modelInfo.properties.description = replace(replace(get_param(modelNameOnly, 'Description'), newline,' '),'"',"'");
            modelInfo.properties.startTime = get_param(modelNameOnly, 'StartTime');
            modelInfo.properties.stopTime = get_param(modelNameOnly, 'StopTime');
            %modelInfo.properties.version = string(get_param(modelNameOnly, 'Version'));

            jsonData = jsonData + jsonencode(modelInfo) + newline;
            %disp(['Added node for ' modelNameOnly]);

            %%%%%%%% List of Blocks %%%%%%%%
            
            get_block = find_system(loadedModel,'FindAll','on','FollowLinks','on','type','block');
            for i= 1:size(get_block, 1) %for each block in the list do    
                %Block info    
                blockName = replace(replace(get(get_block(i), 'Name'), newline,' '),'"',"'");
                blockType = replace(get(get_block(i), 'BlockType'), newline,' ');
                parentBlock = replace(get(get_block(i), 'Parent'), newline,' ');
                blockDescription = replace(replace(get(get_block(i),'BlockDescription'), newline,' '),'"',"'");
                isCommented = get(get_block(i), 'Commented');               
                
                portHandles = get(get_block(i), 'PortHandles');  

                numberOfInputPort = size(portHandles.Inport,2);
                numberOfOutputPort = size(portHandles.Outport,2);
                blockId = strcat(parentBlock,"/",blockName);

                % prepare and insert block/subsystem block node information   
				elementInfo = '';
                elementInfo.id = blockId;
                elementInfo.type = 'node';
                if strcmp(blockType,'SubSystem')
                    elementInfo.labels = {'SubSystem'};        
                elseif strcmp(blockType,'ModelReference')
                    elementInfo.labels = {'ReferenceModel'};
                else
                    elementInfo.labels = {'Block'};
                end

                elementInfo.properties.name = blockName;
                elementInfo.properties.type = blockType; 
                if (strcmp(blockType,'Inport') || strcmp(blockType,'Outport')) 
                    % for inport and outport type block the value will be inport and outport respectively. So we keep this block as ConnectivityBlock
                    elementInfo.properties.typeDescription = 'ConnectivityBlock'; 
                else
                     elementInfo.properties.typeDescription = ''; 
                end
                
                if strcmp(blockType,'ModelReference')
                    elementInfo.properties.referenceModelName = replace(get(get_block(i),'ModelName'), newline,' ');                    
                end
                
                elementInfo.properties.description = blockDescription;
                %elementInfo.properties.outDataType = ""; %% not able to get this
                    %property. This is block specific property
                elementInfo.properties.numberOfInputPort = numberOfInputPort;
                elementInfo.properties.numberOfOutputPort = numberOfOutputPort;
                elementInfo.properties.isCommented = isCommented;     
                
                jsonData = jsonData + jsonencode(elementInfo) + newline;

                %disp(['Added node for ' blockName]);

                % prepare and insert block/subsystem block relationship information
                % with immediate parent node
				relationshipInfo = '';
                relationshipInfo.id = "";
                relationshipInfo.type = 'relationship';
                relationshipInfo.label = 'CONTAINS';
                relationshipInfo.properties.type = "element"; %% default properties now
                relationshipInfo.start.id = parentBlock; % as source node
                relationshipInfo.end.id = blockId; % as destination node
                jsonData = jsonData + jsonencode(relationshipInfo) + newline;                
                %disp(strcat("Added edge for  ", parentBlock, "-->", blockName));
                
                %Create the model reference relationship 
                if strcmp(blockType,'ModelReference')
                    referenceModelId = replace(get(get_block(i),'ModelName'), newline,' ');  
					referenceRelationshipInfo = '';
                    referenceRelationshipInfo.id = "";
                    referenceRelationshipInfo.type = 'relationship';
                    referenceRelationshipInfo.label = 'HAS_REFERENCE_OF';
                    referenceRelationshipInfo.properties.type = "reference"; %% default properties now
                    referenceRelationshipInfo.start.id = blockId; % current block as source node
                    referenceRelationshipInfo.end.id = referenceModelId; % Actual model as destination node
                    jsonData = jsonData + jsonencode(referenceRelationshipInfo) + newline;
                    %disp(strcat("Added model reference edge for  ", blockId, "-->", referenceModelId));
                elseif (strcmp(blockType,'Inport') || strcmp(blockType,'Outport')) 
                    try
                        parent_Block_BlockType = get_param(parentBlock, 'BlockType');
                    catch
                        parent_Block_BlockType = "";
                    end
                    if strcmp(parent_Block_BlockType,'SubSystem')
                        inoutportBlockPortNumber = get(get_block(i), 'Port'); % this will be same as subsystem block's connected port number
                        inoutportBlockId = strcat(parentBlock,"/",blockType,num2str(inoutportBlockPortNumber));  
						inoutportBlockRelationshipInfo = '';
                        inoutportBlockRelationshipInfo.id = "";
                        inoutportBlockRelationshipInfo.type = 'relationship';
                        inoutportBlockRelationshipInfo.label = 'IS_CONNECTED_WITH';
                        inoutportBlockRelationshipInfo.properties.type = "connected"; %% default properties now
                        inoutportBlockRelationshipInfo.start.id = inoutportBlockId; % Port block as source node
                        inoutportBlockRelationshipInfo.end.id = blockId; % In/Out Port Block as destination node
                        jsonData = jsonData + jsonencode(inoutportBlockRelationshipInfo) + newline;
                        %disp(strcat("Added in/outport edge for  ", blockId, "-->", inoutportBlockId));
                    end
                end

            end           
            
            % compile the model - gcs returns the path name of the current system (model)
            try
                eval([gcs,'([],[],[],''compile'');']);
            catch e %e is an MException struct
                fprintf(1,'The identifier was: %s',e.identifier);
                fprintf(1,'\n There was an error in model compile! The message was: %s',e.message);
                %disp(strcat(modelNameOnly, ' model compilation ignored'));
            end
            %%%% input or output port info
            get_port = find_system(loadedModel,'FindAll','on','FollowLinks','on','type','port');
            for i = 1:size(get_port, 1)
                port = get_port(i);        
                %eachPortInfo = get(get_port(i));                
                portType= get(port, 'PortType'); 
                portNumber= get(port, 'PortNumber');    
                portParent= replace(get(port, 'Parent'),'//','/'); %% in mdl model I found an exceptional case with this

                % port node
                portBlockName = replace(strcat(regexprep(portType,'(\<\w)', '${upper($1)}'),"",num2str(portNumber)), newline,' ');
                portBlockId = replace(strcat(portParent,"/",portBlockName), newline,' ');
                parentBlockId = replace(portParent, newline,' ');

                % prepare and insert port node information
				portElementInfo = '';
                portElementInfo.id = portBlockId;
                portElementInfo.type = 'node';
                portElementInfo.labels = {'Port'};
                portElementInfo.properties.name = portBlockName;
                portElementInfo.properties.type = portType;
                portElementInfo.properties.portNumber = portNumber;                
                try
                    portElementInfo.properties.dataType = replace(replace(get(port, 'CompiledPortDataType'), newline,' '),'"',"'");
                catch 
                    portElementInfo.properties.dataType = "";
                end
                
                jsonData = jsonData + jsonencode(portElementInfo) + newline;
                %disp(strcat("Added node of ",parentBlockId,"--->",portBlockName));

                % Prepare and insert Port Node Relationship with Parent node
                if strcmp(portType,'inport')       
					inportRelationshipInfo = '';
                    inportRelationshipInfo.id = "";
                    inportRelationshipInfo.type = 'relationship';
                    inportRelationshipInfo.label = 'HAS_PORT'; %%% At this moment inport and outport both are same
                    inportRelationshipInfo.properties.type = "inport"; %% default properties now
                    inportRelationshipInfo.start.id = parentBlockId; %%% At this moment inport and outport both are same
                    inportRelationshipInfo.end.id = portBlockId;
                    jsonData = jsonData + jsonencode(inportRelationshipInfo) + newline;
                    %disp(strcat("Added relationship of ",parentBlockId,"--->",portBlockName));
                elseif strcmp(portType,'outport')   
					outportRelationshipInfo = '';
                    outportRelationshipInfo.id = "";
                    outportRelationshipInfo.type = 'relationship';
                    outportRelationshipInfo.label = 'HAS_PORT';
                    outportRelationshipInfo.properties.type = "outport"; %% default properties now
                    outportRelationshipInfo.start.id = parentBlockId;
                    outportRelationshipInfo.end.id = portBlockId;
                    jsonData = jsonData + jsonencode(outportRelationshipInfo) + newline;
                    %disp(strcat("Added relationship of ",parentBlockId,"--->",portBlockName));      
                end   
            end                 
            % terminate the compilation
            try
                eval([gcs,'([],[],[],''term'')']);
            catch e %e is an MException struct
                fprintf(1,'\n The identifier was:%s',e.identifier);
                fprintf(1,'\n There was an error in model termination! The message was: %s',e.message); 
                %disp(strcat(modelNameOnly, ' model Termination ignored'));
            end
            
            %%% Prepare Signal or Line information as edge
            get_line = find_system(loadedModel,'FindAll','on','FollowLinks','on','type','line');

            for i = 1:size(get_line, 1)
                line = get_line(i);        
                %eachLineInfo = get(get_line(i));

                signal_segmentType= get(line, 'SegmentType');
                signal_signalType= get(line, 'Type');
                sourcePorthandle = get(line, 'SrcPortHandle');
                dstPorthandle = get(line, 'DstPortHandle');

                source_block_handle = get_param(line, 'SrcBlockHandle');
                destination_block_handle = get_param(line, 'DstBlockHandle');    

                src_block_name = get_param(source_block_handle, 'name');
                dst_block_name = get_param(destination_block_handle, 'name');
                dst_block_count = size(dst_block_name,1);

                if dst_block_count == 1
                    sourceporttype = regexprep(get(sourcePorthandle, 'PortType'),'(\<\w)', '${upper($1)}');
                    sourcePortNumber= get(sourcePorthandle, 'PortNumber');    
                    sourcePortParent= strcat(sourceporttype,"",num2str(sourcePortNumber));

                    dstPorttype = regexprep(get(dstPorthandle, 'PortType'),'(\<\w)', '${upper($1)}');
                    dstPortNumber= get(dstPorthandle, 'PortNumber');    
                    dstPortParent= strcat(dstPorttype,"",num2str(dstPortNumber));

                    sourcePortPathId = replace(strcat(get(source_block_handle, 'Parent'),"/",src_block_name,"/",sourcePortParent), newline,' ');
                    dstPortPathId = replace(strcat(get(destination_block_handle, 'Parent'),"/",dst_block_name,"/",dstPortParent), newline,' ');

                    % Prepare and insert Signal/Line relationship with Port nodes
					lineRelationshipInfo = '';
                    lineRelationshipInfo.id = "";
                    lineRelationshipInfo.type = 'relationship';
                    lineRelationshipInfo.label = 'SEND_DATA_TO';
                    lineRelationshipInfo.properties.segmentType = signal_segmentType;
                    lineRelationshipInfo.properties.type = signal_signalType;        
                    lineRelationshipInfo.start.id = sourcePortPathId;
                    lineRelationshipInfo.end.id = dstPortPathId;
                    jsonData = jsonData + jsonencode(lineRelationshipInfo) + newline;
                    %disp(strcat("Added edge info from ", sourcePortPathId, "-->", dstPortPathId));        
                end     
            end         
            
            %file write operation
            fid=fopen(jsonDataFilePath,'w');
            fprintf(fid,jsonData);
            fclose('all');  
            disp('')
            disp(strcat(dataFileName,'.json',' file created successfully'));
            created_file_count =  created_file_count + 1;
        end
    end
    disp(strcat('Total created files are = ',num2str(created_file_count)));
    file_count = int16(created_file_count);
end