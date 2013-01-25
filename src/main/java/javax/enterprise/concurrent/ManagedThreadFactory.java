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

import java.util.concurrent.ThreadFactory;

/**
 * A manageable version of a <CODE>ThreadFactory</CODE>.<p>
 *
 * A ManagedThreadFactory extends the Java&trade; SE ThreadFactory to provide 
 * a method for creating threads for execution in a Java&trade; EE environment.
 * Implementations of the ManagedThreadFactory are
 * provided by a Java&trade; EE Product Provider.  Application Component Providers
 * use the Java Naming and Directory Interface&trade; (JNDI) to look-up instances of one
 * or more ManagedThreadFactory objects using resource environment references.<p>
 *
 * The Concurrency Utilities for Java&trade; EE specification describes several
 * behaviors that a ManagedThreadFactory can implement.  The Application
 * Component Provider and Deployer identify these requirements and map the
 * resource environment reference appropriately.<p>
 *
 * Threads returned from the {@code newThread()} method should implement the
 * {@link ManageableThread} interface.
 * 
 * The Runnable task that is allocated to the new thread using the
 * {@link ThreadFactory#newThread(Runnable)} method
 * will run with the application component context of the component instance
 * that created (looked-up) this ManagedThreadFactory instance.<p>
 *
 * The task runs without an explicit transaction (they do not enlist in the application
 * component's transaction).  If a transaction is required, use a
 * <CODE>javax.transaction.UserTransaction</CODE> instance.  A UserTransaction instance is
 * available in JNDI using the name: &QUOT;java:comp/UserTransaction&QUOT<p>
 *
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
 * }</PRE>
 *
 * A ManagedThreadFactory can be used with Java SE ExecutorService implementations directly.<p>
 *
 * Example:
 * <pre>
 * &#47;**
 * * Create a ThreadPoolExecutor using a ManagedThreadFactory.
 * * Resource Mappings:
 * *  type:      javax.enterprise.concurrent.ManagedThreadFactory
 * *  jndi-name: concurrent/tf/DefaultThreadFactory
 * *&#47;
 *
 * &#64;Resource(name="concurrent/tf/DefaultThreadFactory")
 * ManagedThreadFactory tf;
 * 
 * public ExecutorService getManagedThreadPool() {
 *
 *   // All threads will run as part of this application component.
 *   return new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
 *       new ArrayBlockingQueue&LT;Runnable&GT;(10), tf);
 * }
 * </pre>
 * <P>
 *
 * @since 1.0
 */
public interface ManagedThreadFactory extends ThreadFactory {

}
