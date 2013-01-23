/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Map;

/**
 * A task submitted to an {@link ManagedExecutorService} or 
 * {@link ManagedScheduledExecutorService} can optionally implement this 
 * interface to provide identifying information about the task, to provide
 * a {@link ManagedTaskListener} to get notification of lifecycle events of
 * the task, or to provide additional execution properties.
 * <p>
 * See also {@link ManagedExecutors#managedTask(java.util.concurrent.Callable, java.util.Map, javax.enterprise.concurrent.ManagedTaskListener) ManagedExecutors.managedTask()}.
 * <p>
 * 
 * @since 1.0
 */
public interface ManagedTask {

  /**
   * Execution property to be returned in {@link #getExecutionProperties()} or
   * {@link ContextService#createContextualProxy(java.lang.Object, java.util.Map, java.lang.Class) ContextService.createContextualProxy()}
   * to provide hint about whether the task could take a long time to complete.
   * Java&trade; EE Product Providers may make use of this hint value to 
   * decide how to allocate thread resource for running this task.
   * Valid values are "true" or "false".
   */
  public static final String LONGRUNNING_HINT = "javax.enterprise.concurrent.LONGRUNNING_HINT";
  
  /**
   * Execution property to be returned in {@link #getExecutionProperties()} or
   * {@link ContextService#createContextualProxy(java.lang.Object, java.util.Map, java.lang.Class) ContextService.createContextualProxy()}
   * to provide a hint about whether the methods in {@link ManagedTaskListener} and 
   * {@link Trigger} associated with this task needs to be called under the same
   * context as the task. Any values other than "true" means the methods will be 
   * run with unspecified context.
   */
  public static final String CONTEXTUAL_CALLBACK_HINT = "javax.enterprise.concurrent.CONTEXTUAL_CALLBACK_HINT";
  
  /**
   * Execution property to be returned in {@link #getExecutionProperties()} or
   * {@link ContextService#createContextualProxy(java.lang.Object, java.util.Map, java.lang.Class) ContextService.createContextualProxy()}
   * to provide a String that identifies the task. It may be the name or ID that
   * allow management facilities to inspect the task to determine the intent 
   * of the task and its state. Implementations should not depend upon 
   * any thread execution context and should typically return only 
   * readily-available instance data to identify the task.
   */
  public static final String IDENTITY_NAME = "javax.enterprise.concurrent.IDENTITY_NAME";
  
  /**
   * The {@link ManagedTaskListener} to receive notification of lifecycle
   * events of this task.
   * 
   * @return The {@link ManagedTaskListener} to receive notification of 
   * lifecycle events of this task, or null if it is not necessary to get
   * notified of such events.
   */
  public ManagedTaskListener getManagedTaskListener();
  
  /**
   * Provides additional information to the {@link ManagedExecutorService} or
   * {@link ManagedScheduledExecutorService} when executing this task.<p>
   * 
   * Some standard property keys are defined in this class. 
   * Custom property keys may be defined but must not begin with 
   * "javax.enterprise.concurrent.".
   * 
   * @return A Map&lt;String, String&gt; containing additional execution properties, or
   * null if no additional information is provided for this task.
   */
  public Map<String, String> getExecutionProperties();
}
