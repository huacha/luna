package com.luna.bpm.process.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import com.luna.bpm.process.cmd.graph.ActivitiGraphBuilder;
import com.luna.bpm.process.cmd.graph.Graph;

public class FindGraphCmd implements Command<Graph> {
//    private static Logger logger = LoggerFactory.getLogger(FindGraphCmd.class);
    private String processDefinitionId;

    public FindGraphCmd(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public Graph execute(CommandContext commandContext) {
        return new ActivitiGraphBuilder(processDefinitionId).build();
    }
}
