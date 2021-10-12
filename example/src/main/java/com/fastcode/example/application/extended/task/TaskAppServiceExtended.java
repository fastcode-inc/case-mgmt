package com.fastcode.example.application.extended.task;

import com.fastcode.example.application.core.task.TaskAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.extended.cases.ICasesRepositoryExtended;
import com.fastcode.example.domain.extended.task.ITaskRepositoryExtended;
import org.springframework.stereotype.Service;

@Service("taskAppServiceExtended")
public class TaskAppServiceExtended extends TaskAppService implements ITaskAppServiceExtended {

    public TaskAppServiceExtended(
        ITaskRepositoryExtended taskRepositoryExtended,
        ICasesRepositoryExtended casesRepositoryExtended,
        ITaskMapperExtended mapper,
        LoggingHelper logHelper
    ) {
        super(taskRepositoryExtended, casesRepositoryExtended, mapper, logHelper);
    }
    //Add your custom code here

}
