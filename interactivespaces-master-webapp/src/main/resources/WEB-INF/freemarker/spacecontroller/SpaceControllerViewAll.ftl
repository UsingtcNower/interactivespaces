<#--
 * Copyright (C) 2012 Google Inc.
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
 -->
<!DOCTYPE html>
<html>
<head>
<title>Interactive Spaces Admin: Space Controllers</title>

<#include "/allpages_head.ftl">
</head>

<body>
<script type="text/javascript">
function doAjaxCommand(command) {
  $.ajax({
      url: '/interactivespaces/spacecontroller/all/' + command + '.json',
      success: function(data) {
        $('#commandResult').html(data.result);
      }
  });
}

function cleanActivitiesTempDataAllControllers() {
    if (confirm("Are you sure you want to clean the tmp data for all live activities on all space controllers?")) {
        doAjaxCommand('cleanactivitiestmpdata');
    }
}

function cleanActivitiesPermanentDataAllControllers() {
    if (confirm("Are you sure you want to clean the permanent data for all live activities on all space controllers?")) {
        doAjaxCommand('cleanactivitiespermanentdata');
    }
}

function cleanTempDataAllControllers() {
    if (confirm("Are you sure you want to clean the tmp data for all space controllers?")) {
        doAjaxCommand('cleantmpdata');
    }
}

function cleanPermanentDataAllControllers() {
    if (confirm("Are you sure you want to clean the permanent data for all space controllers?")) {
        doAjaxCommand('cleanpermanentdata');
    }
}

function shutdownAllControllers() {
    if (confirm("Are you sure you want to shutdown all controllers?")) {
        window.location='/interactivespaces/spacecontroller/all/shutdown.html';
    }
}

function captureDataAllControllers() {
  if (confirm("Are you sure you want to capture data bundles for all controllers?")) {
    doAjaxCommand('capturedata');
  }
}

function restoreDataAllControllers() {
  if (confirm("Are you sure you want to restore data bundles for all controllers?")) {
    doAjaxCommand('restoredata');
  }
}

function shutdownAllActivitiesAllControllers() {
    if (confirm("Are you sure you want to shut down all applications on all controllers?")) {
        window.location='/interactivespaces/spacecontroller/all/activities/shutdown.html';
    }
}
</script>

<#include "/allpages_body_header.ftl">

<h1>Space Controllers</h1>

<div class="commandBar"><ul>
<li><button type="button" id="connectAllButton" onclick="window.location='/interactivespaces/spacecontroller/all/connect.html'" title="Connect to all known controllers">Connect All</button></li>
<li><button type="button" id="disconnectAllButton" onclick="window.location='/interactivespaces/spacecontroller/all/disconnect.html'" title="Disconnect from all known controllers">Disconnect All</button></li>
<li><button type="button" id="statusAllButton" onclick="doAjaxCommand('status')" title="Status from all controllers that claim they are in some sort of connection">Status All</button></li>
<li><button type="button" id="forceStatusAllButton" onclick="doAjaxCommand('forcestatus')" title="Status from all controllers, whether or not they have been connected">Force Status All</button></li>
<li><button type="button" id="shutdownActivitiesAllButton" onclick="shutdownAllActivitiesAllControllers();" title="Shutdown all activities on all connected controllers">Shutdown All Activities</button></li>
<li><button type="button" id="shutdownAllButton" onclick="shutdownAllControllers();" title="Shut down all connected controllers">Shutdown All</button></li>
<li><button type="button" onclick="cleanActivitiesTempDataAllControllers()" title="Clean the tmp data for all live activities on all controllers">Clean Activities Tmp</button></li>
<li><button type="button" onclick="cleanActivitiesPermanentDataAllControllers()" title="Clean the permanent data for all live activities on all controllers">Clean Activities Permanent</button></li>
<li><button type="button" onclick="cleanTempDataAllControllers()" title="Clean the tmp data for all controllers">Clean Tmp</button></li>
<li><button type="button" onclick="cleanPermanentDataAllControllers()" title="Clean the permanent data for all controllers">Clean Permanent</button></li>
<li><button type="button" id="captureDataAllButton" onclick="captureDataAllControllers()" title="Capture All Data">Capture All Data</button></li>
<li><button type="button" id="restoreDataAllButton" onclick="restoreDataAllControllers()" title="Restore All Data">Restore All Data</button></li>
<li><button type="button" id="newButton" onclick="window.location='/interactivespaces/spacecontroller/new.html?mode=embedded'" title="Create a new controller">New</button></li>
</ul></div>

<div id="commandResult">
</div>

<table>
  <tr>
    <th>Name</th>
    <th class="status-header">Status</th>
    <th class="databundle-header">Data Bundle</th>
  </tr>

  <#list spacecontrollers as spacecontroller>
    <tr>
      <td>
        <a href="${spacecontroller.controller.id}/view.html">${spacecontroller.controller.name}</a>
      </td>
      <td class="status-value">
        <#if spacecontroller.lastStateUpdate??>
          <#assign t  = spacecontroller.lastStateUpdateDate?datetime>
        <#else>
          <#assign t = 'Unknown'>
        </#if>
        <span class="spacecontroller-status spacecontroller-status-${spacecontroller.state.name()}">
          <@spring.message spacecontroller.state.description />
        </span>
        as of ${t}
      </td>
      <td class="databundle-value">
        <#if spacecontroller.lastDataBundleStateUpdate??>
          <#assign t  = spacecontroller.lastDataBundleStateUpdateDate?datetime>
        <#else>
          <#assign t = 'Unknown'>
        </#if>
        <span class="spacecontroller-status spacecontroller-status-${spacecontroller.dataBundleState.name()}">
          <@spring.message spacecontroller.dataBundleState.description />
        </span>
        as of ${t}
      </td>
    </tr>
  </#list>
</ul>


</body>
<html>