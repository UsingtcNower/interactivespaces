/*
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package interactivespaces.liveactivity.runtime.monitor;

import interactivespaces.liveactivity.runtime.LiveActivityRuntime;
import interactivespaces.util.resource.ManagedResource;

/**
 * The remote debug service for a live activity runtime.
 *
 * @author Keith M. Hughes
 */
public interface RemoteLiveActivityRuntimeMonitorService extends ManagedResource {

  /**
   * The configuration property name for enabling the monitor.
   */
  String CONFIGURE_MONITOR_ENABLE = "space.activityruntime.monitor.enable";

  /**
   * The configuration default for enabling the monitor.
   */
  boolean CONFIGURE_MONITOR_ENABLE_DEFAULT = false;

  /**
   * The configuration property name for the monitor webserver port.
   */
  String CONFIGURATION_WEBSERVER_PORT = "space.activityruntime.monitor.webserver.port";

  /**
   * The configuration property default value for the monitor webserver port.
   */
  int CONFIGURATION_VALUE_DEFAULT_WEBSERVER_PORT = 8082;

  /**
   * Add in a plugin into the monitor service.
   *
   * @param plugin
   *          the plugin to add
   */
  void addPlugin(LiveActivityRuntimeMonitorPlugin plugin);

  /**
   * Set the live activity runtime to be debugged.
   *
   * @param liveActivityRuntime
   *          the live activity runtime to be debugged
   */
  void setLiveActivityRuntime(LiveActivityRuntime liveActivityRuntime);

  /**
   * Get the live activity runtime to be debugged.
   *
   * @return the live activity runtime
   */
  LiveActivityRuntime getLiveActivityRuntime();
}
