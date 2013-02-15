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

/**
 * Triggers allow application developers to plug in rules for when 
 * and how often a task should run. The trigger can be as simple as 
 * a single, absolute date-time or can include Java&trade; EE business 
 * calendar logic. A Trigger implementation is created by the 
 * application developer (or may be supplied to the application 
 * externally) and is registered with a task when it is submitted 
 * to a {@link ManagedScheduledExecutorService} using any of the 
 * schedule methods. Each method will run with unspecified context. 
 * The methods can be made contextual through creating contextual
 * proxy objects using {@link ContextService}.
 * <p>
 * Each Trigger instance will be invoked within the same process 
 * in which it was registered.
 * <p>
 * 
 * Example:
 * <pre>
 * &#47;**
 *  * A trigger that only returns a single date.
 *  *&#47;
 *  public class SingleDateTrigger implements Trigger {
 *      private Date fireTime;
 *      
 *      public TriggerSingleDate(Date newDate) {
 *          fireTime = newDate;
 *      }
 *
 *      public Date getNextRunTime(
 *         LastExecution lastExecutionInfo, Date taskScheduledTime) {
 *         
 *         if(taskScheduledTime.after(fireTime)) {
 *             return null;
 *         }
 *         return fireTime;
 *      }
 *
 *      public boolean skipRun(LastExecution lastExecutionInfo, Date scheduledRunTime) {
 *          return scheduledRunTime.after(fireTime);
 *      }
 *  }
 *
 * &#47;**
 *  * A fixed-rate trigger that will skip any runs if
 *  * the latencyAllowance threshold is exceeded (the task
 *  * ran too late).
 *  *&#47;
 *  public class TriggerFixedRateLatencySensitive implements Trigger {
 *      private Date startTime;
 *      private long delta;
 *      private long latencyAllowance;
 *
 *      public TriggerFixedRateLatencySensitive(Date startTime, long delta, long latencyAllowance) {
 *          this.startTime = startTime;
 *          this.delta = delta;
 *          this.latencyAllowance = latencyAllowance;
 *      }
 *
 *      public Date getNextRunTime(LastExecution lastExecutionInfo, 
 *                                 Date taskScheduledTime) {
 *          if(lastExecutionInfo==null) {
 *              return startTime;
 *          }
 *          return new Date(lastExecutionInfo.getScheduledStart().getTime() + delta);
 *      }
 *
 *      public boolean skipRun(LastExecution lastExecutionInfo, Date scheduledRunTime) {
 *          return System.currentTimeMillis() - scheduledRunTime.getTime() > latencyAllowance;
 *      }
 *  }
 *
 * </pre>
 * <P>
 *
 * @since 1.0
 */
public interface Trigger {

  /**
   * Retrieve the next time that the task should run after.
   * 
   * @param lastExecutionInfo information about the last execution of the task. 
   *                   This value will be null if the task has not yet run.
   * @param taskScheduledTime the date/time in which the task was scheduled using
   *                          the {@code ManagedScheduledExecutorService.schedule} 
   *                          method.
   * @return the date/time in which the next task iteration should execute on or 
   *         after.


   */
  public java.util.Date getNextRunTime(LastExecution lastExecutionInfo,
                                       java.util.Date taskScheduledTime);
  
  /**
   * Return true if this run instance should be skipped.
   * <p>
   * This is useful if the task shouldn't run because it is late or if the task 
   * is paused or suspended.
   * <p>
   * Once this task is skipped, the state of it's Future's result will throw a 
   * {@link SkippedException}. Unchecked exceptions will be wrapped in a 
   * <code>SkippedException</code>.
   * 
   * @param lastExecutionInfo information about the last execution of the task. 
   *                   This value will be null if the task has not yet run.
   * @param scheduledRunTime the date/time that the task was originally scheduled 
   *                         to run.
   * @return true if the task should be skipped and rescheduled.
   */
  public boolean skipRun(LastExecution lastExecutionInfo,
                         java.util.Date scheduledRunTime);
  
}
