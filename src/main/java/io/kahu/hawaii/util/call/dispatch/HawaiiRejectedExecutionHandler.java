/**
 * Copyright 2015 Q24
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.kahu.hawaii.util.call.dispatch;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class HawaiiRejectedExecutionHandler implements RejectedExecutionHandler {
    private final RejectedExecutionHandler delegate;

    public HawaiiRejectedExecutionHandler(RejectedExecutionHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
        /*
         * This does the actual put into the queue. Once the max threads have
         * been reached, the tasks will then queue up.
         */
        try {
            executor.getQueue().add(task);
        } catch (IllegalStateException e) {
            if (executor instanceof HawaiiThreadPoolExecutorImpl) {
                ((HawaiiThreadPoolExecutorImpl) executor).rejectTask();
            }
            delegate.rejectedExecution(task, executor);
        }
    }

}
