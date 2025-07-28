package com.yeobi.mall.domain.task.service;

import com.yeobi.mall.common.enums.TaskStatus;
import com.yeobi.mall.common.enums.TaskType;
import com.yeobi.mall.common.exception.ServiceException;
import com.yeobi.mall.common.exception.ServiceExceptionCode;
import com.yeobi.mall.domain.task.entity.TaskQueue;
import com.yeobi.mall.domain.task.repository.TaskQueueRepository;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskQueueService {

  private final TaskQueueRepository taskQueueRepository;

  @Transactional(propagation = Propagation.SUPPORTS)
  public TaskQueue requestQueue(TaskType taskType) {
    TaskQueue taskQueue = TaskQueue.builder()
        .taskType(taskType)
        .status(TaskStatus.PENDING)
        .build();

    return taskQueueRepository.save(taskQueue);
  }

  @Transactional
  public void processQueueById(Long taskQueueId, Consumer<TaskQueue> task) {
    TaskQueue taskQueue = taskQueueRepository.findByIdForUpdate(taskQueueId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_TASK));

    updateStatus(taskQueue, TaskStatus.PROCESSING);

    task.accept(taskQueue);

    updateStatus(taskQueue, TaskStatus.COMPLETED);
  }

  private void updateStatus(TaskQueue taskQueue, TaskStatus taskStatus) {
    taskQueue.setStatus(taskStatus);
    taskQueueRepository.flush();
  }

}