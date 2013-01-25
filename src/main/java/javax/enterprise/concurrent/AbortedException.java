/*
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

import java.util.concurrent.ExecutionException;

/**
 * Exception indicating that the result of a task cannot be retrieved 
 * because the task failed to run for some reason other than being cancelled.
 * <p>
 * Use the {@link Throwable#getCause()} method to determine why the task was aborted.
 *
 * @since 1.0
 */
public class AbortedException extends ExecutionException implements java.io.Serializable {

  private static final long serialVersionUID = -8248124070283019190L;

/**
   * Constructs an AbortedException with <code>null</code> as its detail message. 
   * 
   * The cause is not initialized, and may subsequently be initialized by a call to 
   * {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public AbortedException() {
	super();
  }

  /**
   * Constructs an AbortedException exception with the specified detail message and cause.
   * <p>
   * Note that the detail message associated with cause is not automatically incorporated 
   * in this exception's detail message.
   *  
   * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()}
   *                method).
   * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). 
   *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public AbortedException(String message, Throwable cause) {
	super(message, cause);  
  }
  
  /**
   * Constructs an AbortedException exception with the specified detail message.
   * <p>
   * The cause is not initialized, and may subsequently be initialized by a call to 
   * {@link Throwable#initCause(java.lang.Throwable)}.
   *  
   * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()}
   *                method).
   */
  public AbortedException(String message) {
	super(message);  
  }
  
  /**
   * Constructs an AbortedException exception with the specified cause and a detail message of 
   * (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
   * 
   * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). 
   *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public AbortedException(Throwable cause) {
	super(cause);  
  }
}
