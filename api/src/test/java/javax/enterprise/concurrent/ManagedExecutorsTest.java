/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package javax.enterprise.concurrent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import static org.junit.Assert.*;
import org.junit.Test;

public class ManagedExecutorsTest {
    
    /**
     * Basic test for ManagedExecutors.managedTask(Runnable, ManagedTaskListener)
     */
    @Test
    public void testManagedTask_Runnable_ManagedTaskListener() {
        RunnableImpl task = new RunnableImpl();
        ManagedTaskListenerImpl taskListener = new ManagedTaskListenerImpl();
        
        Runnable wrapped = ManagedExecutors.managedTask(task, taskListener);
        ManagedTask managedTask = (ManagedTask) wrapped;
        assertTrue(taskListener == managedTask.getManagedTaskListener());

        wrapped.run();
        assertTrue(task.ran);
}

    /**
     * Basic test for ManagedExecutors.managedTask(Runnable, Map, ManagedTaskListener)
     */
    @Test
    public void testManagedTask_Runnable_executionProperties_ManagedTaskListener() {
        RunnableImpl task = new RunnableImpl();
        ManagedTaskListenerImpl taskListener = new ManagedTaskListenerImpl();
        Map<String, String> executionProperties = new HashMap<>();
        final String TASK_NAME = "task1";
        executionProperties.put(ManagedTask.IDENTITY_NAME, TASK_NAME);
        executionProperties.put(ManagedTask.LONGRUNNING_HINT, "true");
        
        Runnable wrapped = ManagedExecutors.managedTask(task, executionProperties, taskListener);
        ManagedTask managedTask = (ManagedTask) wrapped;
        assertTrue(taskListener == managedTask.getManagedTaskListener());
        assertEquals("true", managedTask.getExecutionProperties().get(ManagedTask.LONGRUNNING_HINT));
        assertEquals(TASK_NAME, managedTask.getExecutionProperties().get(ManagedTask.IDENTITY_NAME));
        assertEquals(TASK_NAME, managedTask.getIdentityDescription(Locale.getDefault()));
        
        wrapped.run();
        assertTrue(task.ran);
    }

    /**
     * Test for ManagedExecutors.managedTask(Runnable, Map, ManagedTaskListener)
     * but task already implements ManagedTask, and both executionProerties and
     * taskListeners were passed to managedTask().
     */
    @Test
    public void testManagedTask_Runnable_ManagedTask() {
        ManagedTaskListenerImpl TASK_LISTENER = new ManagedTaskListenerImpl();
        Map<String, String> EXEC_PROPERTIES = new HashMap<>();
        EXEC_PROPERTIES.put(ManagedTask.DISTRIBUTABLE_HINT, "true");
        EXEC_PROPERTIES.put(ManagedTask.LONGRUNNING_HINT, "false");
        final String TASK_DESCRIPTION = "task1 description";
        ManagedTaskRunnableImpl task = new ManagedTaskRunnableImpl(TASK_DESCRIPTION, EXEC_PROPERTIES, TASK_LISTENER);

        ManagedTaskListenerImpl taskListener = new ManagedTaskListenerImpl();
        Map<String, String> executionProperties = new HashMap<>();
        final String TASK_NAME = "task1";
        executionProperties.put(ManagedTask.IDENTITY_NAME, TASK_NAME);
        executionProperties.put(ManagedTask.LONGRUNNING_HINT, "true");
        
        Runnable wrapped = ManagedExecutors.managedTask(task, executionProperties, taskListener);
        ManagedTask managedTask = (ManagedTask) wrapped;
        assertTrue(taskListener == managedTask.getManagedTaskListener());
        assertEquals("true", managedTask.getExecutionProperties().get(ManagedTask.LONGRUNNING_HINT));
        assertEquals(TASK_NAME, managedTask.getExecutionProperties().get(ManagedTask.IDENTITY_NAME));
        assertEquals("true", managedTask.getExecutionProperties().get(ManagedTask.DISTRIBUTABLE_HINT));
        assertEquals(TASK_DESCRIPTION, managedTask.getIdentityDescription(Locale.getDefault()));
    }

    /**
     * Test for ManagedExecutors.managedTask(Runnable, Map, ManagedTaskListener)
     * but task already implements ManagedTask, and both executionProerties and
     * taskListeners passed to managedTask() were null.
     */
    @Test
    public void testManagedTask_Runnable_ManagedTask_null_args() {
        ManagedTaskListenerImpl TASK_LISTENER = new ManagedTaskListenerImpl();
        Map<String, String> EXEC_PROPERTIES = new HashMap<>();
        EXEC_PROPERTIES.put(ManagedTask.DISTRIBUTABLE_HINT, "true");
        final String TASK_DESCRIPTION = "task1 description";
        ManagedTaskRunnableImpl task = new ManagedTaskRunnableImpl(TASK_DESCRIPTION, EXEC_PROPERTIES, TASK_LISTENER);
        
        Runnable wrapped = ManagedExecutors.managedTask(task, null, null);
        ManagedTask managedTask = (ManagedTask) wrapped;
        assertTrue(TASK_LISTENER == managedTask.getManagedTaskListener());
        assertEquals("true", managedTask.getExecutionProperties().get(ManagedTask.DISTRIBUTABLE_HINT));
        assertEquals(TASK_DESCRIPTION, managedTask.getIdentityDescription(Locale.getDefault()));
    }

    /**
     * Basic test for ManagedExecutors.managedTask(Callable, ManagedTaskListener)
     */
    @Test
    public void testManagedTask_Callable_ManagedTaskListener() throws Exception {
        final String RESULT = "result";
        CallableImpl<String> task = new CallableImpl<>(RESULT);
        ManagedTaskListenerImpl taskListener = new ManagedTaskListenerImpl();
        
        Callable<String> wrapped = ManagedExecutors.managedTask(task, taskListener);
        ManagedTask managedTask = (ManagedTask) wrapped;
        assertTrue(taskListener == managedTask.getManagedTaskListener());

        assertEquals(RESULT, wrapped.call());
    }

    /**
     * Basic test for ManagedExecutors.managedTask(Callable, Map, ManagedTaskListener)
     */
    @Test
    public void testManagedTask_Callable_executionProperties_ManagedTaskListener() throws Exception {
        final String RESULT = "result";
        CallableImpl<String> task = new CallableImpl<>(RESULT);
        ManagedTaskListenerImpl taskListener = new ManagedTaskListenerImpl();
        Map<String, String> executionProperties = new HashMap<>();
        final String TASK_NAME = "task1";
        executionProperties.put(ManagedTask.IDENTITY_NAME, TASK_NAME);
        executionProperties.put(ManagedTask.LONGRUNNING_HINT, "true");
        
        Callable<String> wrapped = ManagedExecutors.managedTask(task, executionProperties, taskListener);
        ManagedTask managedTask = (ManagedTask) wrapped;
        assertTrue(taskListener == managedTask.getManagedTaskListener());
        assertEquals("true", managedTask.getExecutionProperties().get(ManagedTask.LONGRUNNING_HINT));
        assertEquals(TASK_NAME, managedTask.getExecutionProperties().get(ManagedTask.IDENTITY_NAME));
        assertEquals(TASK_NAME, managedTask.getIdentityDescription(Locale.getDefault()));
        
        assertEquals(RESULT, wrapped.call());
    }

    /**
     * Test for ManagedExecutors.managedTask(Callable, Map, ManagedTaskListener)
     * but task already implements ManagedTask, and both executionProerties and
     * taskListeners were passed to managedTask().
     */
    @Test
    public void testManagedTask_Callable_ManagedTask() {
        final String RESULT = "result";
        ManagedTaskListenerImpl TASK_LISTENER = new ManagedTaskListenerImpl();
        Map EXEC_PROPERTIES = new HashMap<>();
        EXEC_PROPERTIES.put(ManagedTask.DISTRIBUTABLE_HINT, "true");
        EXEC_PROPERTIES.put(ManagedTask.LONGRUNNING_HINT, "false");
        final String TASK_DESCRIPTION = "task1 description";
        ManagedTaskCallableImpl<String> task = new ManagedTaskCallableImpl(RESULT, TASK_DESCRIPTION, EXEC_PROPERTIES, TASK_LISTENER);

        ManagedTaskListenerImpl taskListener = new ManagedTaskListenerImpl();
        Map<String, String> executionProperties = new HashMap<>();
        final String TASK_NAME = "task1";
        executionProperties.put(ManagedTask.IDENTITY_NAME, TASK_NAME);
        executionProperties.put(ManagedTask.LONGRUNNING_HINT, "true");
        
        Callable<String> wrapped = ManagedExecutors.managedTask(task, executionProperties, taskListener);
        ManagedTask managedTask = (ManagedTask) wrapped;
        assertTrue(taskListener == managedTask.getManagedTaskListener());
        assertEquals("true", managedTask.getExecutionProperties().get(ManagedTask.LONGRUNNING_HINT));
        assertEquals(TASK_NAME, managedTask.getExecutionProperties().get(ManagedTask.IDENTITY_NAME));
        assertEquals("true", managedTask.getExecutionProperties().get(ManagedTask.DISTRIBUTABLE_HINT));
        assertEquals(TASK_DESCRIPTION, managedTask.getIdentityDescription(Locale.getDefault()));
    }

    /**
     * Test for ManagedExecutors.managedTask(Callable, Map, ManagedTaskListener)
     * but task already implements ManagedTask, and both executionProerties and
     * taskListeners passed to managedTask() were null.
     */
    @Test
    public void testManagedTask_Callable_ManagedTask_null_args() {
        final String RESULT = "result";
        ManagedTaskListenerImpl TASK_LISTENER = new ManagedTaskListenerImpl();
        Map EXEC_PROPERTIES = new HashMap<>();
        EXEC_PROPERTIES.put(ManagedTask.DISTRIBUTABLE_HINT, "true");
        final String TASK_DESCRIPTION = "task1 description";
        ManagedTaskCallableImpl<String> task = new ManagedTaskCallableImpl(RESULT, TASK_DESCRIPTION, EXEC_PROPERTIES, TASK_LISTENER);
        
        Callable wrapped = ManagedExecutors.managedTask(task, null, null);
        ManagedTask managedTask = (ManagedTask) wrapped;
        assertTrue(TASK_LISTENER == managedTask.getManagedTaskListener());
        assertEquals("true", managedTask.getExecutionProperties().get(ManagedTask.DISTRIBUTABLE_HINT));
        assertEquals(TASK_DESCRIPTION, managedTask.getIdentityDescription(Locale.getDefault()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testManagedTask_null_Runnable_task() {
        Runnable task = null;
        ManagedExecutors.managedTask(task, new ManagedTaskListenerImpl());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testManagedTask_null_Runnable_task_2() {
        Runnable task = null;
        ManagedExecutors.managedTask(task, new HashMap<String, String>(), new ManagedTaskListenerImpl());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testManagedTask_null_Callable_task() {
        Callable<?> task = null;
        ManagedExecutors.managedTask(task, new ManagedTaskListenerImpl());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testManagedTask_null_Callable_task_2() {
        Callable<?> task = null;
        ManagedExecutors.managedTask(task, new HashMap<String, String>(), new ManagedTaskListenerImpl());
    }

    static class RunnableImpl implements Runnable {

        boolean ran = false;
        
        @Override
        public void run() {
            ran = true;
        }
        
    }
    
    static class ManagedTaskRunnableImpl extends RunnableImpl implements ManagedTask {

        final String description;
        final ManagedTaskListener taskListener;
        final Map<String, String> executionProperties;

        public ManagedTaskRunnableImpl(String description, Map<String, String> executionProperties, ManagedTaskListener taskListener) {
            this.description = description;
            this.taskListener = taskListener;
            this.executionProperties = executionProperties;
        }
        
        @Override
        public String getIdentityDescription(Locale locale) {
            return description;
        }

        @Override
        public ManagedTaskListener getManagedTaskListener() {
            return taskListener;
        }

        @Override
        public Map<String, String> getExecutionProperties() {
            return executionProperties;
        }
        
    }

    static class CallableImpl<V> implements Callable<V> {

        V result;

        public CallableImpl(V result) {
            this.result = result;
        }
        
        @Override
        public V call() throws Exception {
            return result;
        }
        
    }
    
    static class ManagedTaskCallableImpl<V> extends CallableImpl<V> implements ManagedTask {

        final String description;
        final ManagedTaskListener taskListener;
        final Map<String, String> executionProperties;

        public ManagedTaskCallableImpl(V result, String description, Map<String, String> executionProperties, ManagedTaskListener taskListener) {
            super(result);
            this.description = description;
            this.taskListener = taskListener;
            this.executionProperties = executionProperties;
        }
        
        @Override
        public String getIdentityDescription(Locale locale) {
            return description;
        }

        @Override
        public ManagedTaskListener getManagedTaskListener() {
            return taskListener;
        }

        @Override
        public Map<String, String> getExecutionProperties() {
            return executionProperties;
        }
        
    }

    static class ManagedTaskListenerImpl implements ManagedTaskListener {

        @Override
        public void taskSubmitted(Future<?> future, ManagedExecutorService executor) {
        }

        @Override
        public void taskAborted(Future<?> future, ManagedExecutorService executor, Throwable exception) {
        }

        @Override
        public void taskDone(Future<?> future, ManagedExecutorService executor, Throwable exception) {
        }

        @Override
        public void taskStarting(Future<?> future, ManagedExecutorService executor) {
        }
        
    }
}
