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

import java.util.concurrent.ExecutorService;

/**
 * A manageable version of a {@link java.util.concurrent.ExecutorService}.
 * <p>
 * A ManagedExecutorService extends the Java&trade; SE ExecutorService to provide
 * methods for submitting tasks for execution in a Java&trade; EE environment. 
 * Implementations of the ManagedExecutorService are
 * provided by a Java&trade; EE Product Provider.  Application Component Providers
 * use the Java Naming and Directory Interface&trade; (JNDI) to look-up instances of one
 * or more ManagedExecutorService objects using resource environment references.
 * ManagedExecutorService instances can also be injected into application
 * components through the use of the {@code Resource} annotation.
 * <p>
 * The Concurrency Utilities for Java&trade; EE specification describes several
 * behaviors that a ManagedExecutorService can implement.  The Application
 * Component Provider and Deployer identify these requirements and map the
 * resource environment reference appropriately.
 * <p>
 * The most common uses for a ManagedExecutorService is to run short-duration asynchronous
 * tasks such as for processing of asynchronous methods in Enterprise
 * JavaBean&trade; (EJB&trade;) or for processing async tasks for Servlets that
 * supports asynchronous processing.
 * <p>
 * Tasks are run in managed threads provided by the Java&trade; EE Product Provider
 * and are run within the application component context that submitted the task. 
 * All tasks run without an explicit transaction (they do not enlist in the application
 * component's transaction).  If a transaction is required, use a
 * {@link javax.transaction.UserTransaction} instance.  A UserTransaction instance is
 * available in JNDI using the name: &quot;java:comp/UserTransaction&quot; or by
 * requesting an injection of a {@link javax.transaction.UserTransaction} object
 * using the {@code Resource} annotation.
 * <p>
 * Example:
 * <pre>
 * public run() {
 *   // Begin of task
 *   InitialContext ctx = new InitialContext();
 *   UserTransaction ut = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
 *   ut.begin();
 *
 *   // Perform transactional business logic
 *
 *   ut.commit();
 * }
 * </pre>
 *
 * Tasks can optionally provide an {@link ManagedTaskListener} to receive 
 * notifications of lifecycle events, through the use of {@link ManagedTask}
 * interface.
 * <p>
 * Example:
 * <pre>
 * public class MyRunnable implements Runnable, ManagedTask {
 *   ...
 *   public void run() {
 *     ...
 *   }
 * 
 *   public ManagedTaskListener getManagedTaskListener() {
 *     return myManagedTaskListener;
 *   }
 *   ...
 * }
 * 
 * MyRunnable task = ...;
 * ManagedExecutorService executor = ...;
 * 
 * executor.submit(task); // lifecycle events will be notified to myManagedTaskListener
 * </pre>
 * 
 * Asynchronous tasks are typically submitted to the ManagedExecutorService using one
 * of the {@code submit} methods, each of which return a {@link java.util.concurrent.Future}
 * instance.  The {@code Future} represents the result of the task and can also be used to
 * check if the task is complete or wait for its completion.
 * <p>
 *
 * If the task is canceled, the result for the task is a
 * {@link java.util.concurrent.CancellationException} exception.  If the task is unable
 * to run due to a reason other than cancellation, the result is a
 * {@link AbortedException} exception.
 * <p>
 *
 *Example:
 *<pre>
 * &#47;**
 *  * Retrieve all accounts from several account databases in parallel.
 *  * Resource Mappings:
 *  *  type:      javax.enterprise.concurrent.ManagedExecutorService
 *  *  jndi-name: concurrent/ThreadPool
 *  *&#47;
 * public List&lt;Account&gt; getAccounts(long accountId) {
 *   try {
 *       javax.naming.InitialContext ctx = new InitialContext();
 *       <b>ManagedExecutorService mes = (ManagedExecutorService)
 *           ctx.lookup("java:comp/env/concurrent/ThreadPool");</b>
 *
 *       // Create a set of tasks to perform the account retrieval.
 *       ArrayList&lt;Callable&lt;Account&gt;&gt; retrieverTasks = new ArrayList&lt;Callable&lt;Account&gt;&gt;();
 *       retrieverTasks.add(new EISAccountRetriever());
 *       retrieverTasks.add(new RDBAccountRetriever());
 *
 *       // Submit the tasks to the thread pool and wait for them
 *       // to complete (successfully or otherwise).
 *       <b>List&lt;Future&lt;Account&gt;&gt; taskResults= mes.invokeAll(retrieverTasks);</b>
 *
 *       // Retrieve the results from the resulting Future list.
 *       ArrayList&lt;Account&gt; results = new ArrayList&lt;Account&gt;();
 *       for(Future&lt;Account&gt; taskResult : taskResults) {
 *           try {
 *               <b>results.add(taskResult.get());</b>
 *           } catch (ExecutionException e) {
 *               Throwable cause = e.getCause();
 *               // Handle the AccountRetrieverError.
 *           }
 *       }
 *
 *       return results;
 *
 *   } catch (NamingException e) {
 *       // Throw exception for fatal error.
 *   } catch (InterruptedException e) {
 *       // Throw exception for shutdown or other interrupt condition.
 *   }
 * }
 *
 *
 * public class EISAccountRetriever implements Callable&lt;Account&gt; {
 *     public Account call() {
 *         // Connect to our eis system and retrieve the info for the account.
 *         //...
 *         return null;
 *   }
 * }
 *
 * public class RDBAccountRetriever implements Callable&lt;Account>&gt; {
 *     public Account call() {
 *         // Connect to our database and retrieve the info for the account.
 *         //...
 *   }
 * }
 *
 * public class Account {
 *     // Some account data...
 * }
 * </pre>
 * <P>
 * 
 * @since 1.0
 */
public interface ManagedExecutorService extends ExecutorService {

}
