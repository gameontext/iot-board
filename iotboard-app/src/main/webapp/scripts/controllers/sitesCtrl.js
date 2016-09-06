/*******************************************************************************
 * Copyright (c) 2016 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

'use strict';

angular.module('iotBoardApp')
.controller('sitesCtrl', ['$scope', '$http', function($scope, $http) {

  console.log("IoTBoard : using controller 'sitesCtrl'");
  
  $scope.sites = [];
  
  $scope.gid = ""; 	//Game On! id to register device for
  $scope.iotf = {con : { colour : 'yellow'}, sub : { colour : 'yellow'}, rcv : { colour : 'yellow'}};	//IoT foundation configuration
  $scope.iotfClient = {};	//IoT foundation client connection
  
  $scope.registerDevice = function() {
	 console.log("Registering device for user " + $scope.gid);
	 $http({
	     url: "/iotboard/v1/devices",
	     method: "POST",
	     headers: {  'gameon-id': $scope.gid,
	                 'contentType': 'application/json; charset=utf-8"', //what is being sent to the server
	     },
	     data: JSON.stringify({deviceType: 'VirtualBoard', playerId: $scope.gid}).trim()
	   }).then(function (response) {
		    console.log('Device registration successful : response from server : ' + response.status);
		    $scope.iotf.iot_host = response.data.iotMessagingOrgAndHost;
		    $scope.iotf.iot_port = response.data.iotMessagingPort;
		    $scope.iotf.deviceId = response.data.deviceId;
		    $scope.iotf.password = response.data.deviceAuthToken;
		    $scope.iotf.iot_clientid = response.data.iotClientId;
		    $scope.iotf.eventTopic = response.data.eventTopic;
		    $scope.iotf.cmdTopic = response.data.cmdTopic;
		    console.log("IoTBoard : config : " + JSON.stringify($scope.iotf));
		    $scope.iotfClient = new Paho.MQTT.Client($scope.iotf.iot_host, $scope.iotf.iot_port, $scope.iotf.iot_clientid);
			$scope.iotfClient.onConnectionLost = onConnectionLost;
			connectDevice();
   		}, function (response) {
   			alert('Device registration failed : response from server : ' + response.data + ':' + response.status + "violations " + response.data.violations);
   		}
	 );
  }
//***************************************************************************************
// Test functions to be removed
//***************************************************************************************
  $scope.addSite = function() {
	  $scope.sites.push({sid:'Site number ' + $scope.sites.length, reg : {colour: 'red'}, player : {colour: 'red'}});
  }
  
  $scope.changeName = function() {
	$scope.sites[1].reg.colour = "green";  
  }
  
  $scope.testConnect = function() { 
	  $scope.iotf.iot_host = "pmoxqf.messaging.internetofthings.ibmcloud.com";
	  $scope.iotf.iot_port = 1883;
	  $scope.iotf.iot_clientid = "d:pmoxqf:vdev:adamTest";
	  $scope.iotf.password = "";
	  $scope.iotf.cmdTopic = "iot-2/cmd/+/fmt/json";
      $scope.iotfClient = new Paho.MQTT.Client($scope.iotf.iot_host, $scope.iotf.iot_port, $scope.iotf.iot_clientid);
	  $scope.iotfClient.onConnectionLost = onConnectionLost;
	  connectDevice();
  }
  
  $scope.testSendCommand = function() { 
		 console.log("Telling the server to send a command ");
		 $http({
		     url: "/iotboard/v1/devices/test",
		     method: "POST",
		     headers: {  'gameon-id': $scope.gid,
		                 'contentType': 'application/json; charset=utf-8"', //what is being sent to the server
		     },
		     data: $scope.iotf.deviceId
		   }).then(function (response) {
			    console.log('Command completed successfully');
	   		}, function (response) {
	   			alert('Command failed: ' + response.data + ':' + response.status);
	   		}
		 );
  }
  
  $scope.testTriggerplayer = function() { 
		 console.log("Telling the server to send a command ");
		 $http({
		     url: "/iotboard/v1/devices/testplayer",
		     method: "POST",
		     headers: {  'gameon-id': $scope.gid,
		                 'contentType': 'application/json; charset=utf-8"', //what is being sent to the server
		     },
		     data: $scope.gid
		   }).then(function (response) {
			    console.log('Command completed successfully');
	   		}, function (response) {
	   			alert('Command failed: ' + response.data + ':' + response.status);
	   		}
		 );
}

  
  $scope.testReceiveMessage = function() {
	  //setup a simulated event for receiving 
	  var event = {};
	  var d = {d: {sid: 'Site number 0', name: 'A new Room', gid: 'git:1234567', data : {light : 'reg', status: true}}};
	  event.payloadString = JSON.stringify(d);
	  setTimeout(onMessageArrived, 3000, event);
	  event = {};
	  d = {d: {sid: 'Site number 0', name: 'A new Room', gid: 'git:1234567', data : {light : 'player', status: true}}};
	  event.payloadString = JSON.stringify(d);
	  setTimeout(onMessageArrived, 5000, event);
	  event = {};
	  d = {d: {sid: 'Site number 0', name: 'A new Room', gid: 'git:1234567', data : {light : 'player', status: false}}};
	  event.payloadString = JSON.stringify(d);
	  setTimeout(onMessageArrived, 7000, event);
  }
  
  //test that a button can have it's colours toggled
  $scope.testButtonToggle = function(sid) {
	  console.log("IOTBoard : button toggle test for " + sid);
	  for(var i = 0; i < $scope.sites.length; i++) {
		  if($scope.sites[i].sid = sid) {
			  var site = $scope.sites[i];
			  var toggle = site.player.colour == 'red' ? true : false;
			  var d = {d: {sid: site.sid, name: site.name, gid: site.gid, data : {light : 'player', status: toggle}}};
			  var event = {};
			  event.payloadString = JSON.stringify(d);
			  onMessageArrived(event);
			  return;
		  }
	  }
	  console.log("IoTBoard : error could not find site to test with");
  }
//*********************************************************************************************
  
	function onConnectSuccess() {
		// The device connected successfully
		console.log("Connected Successfully!");
		$scope.iotfClient.subscribe($scope.iotf.cmdTopic, {onSuccess: onSubscribeSuccess, onFailure: onSubscribeFailure});
		$scope.iotf.con.colour =  'green';
		$scope.iotf.msg = "Connected to IBM Watson IoT Platform!"
		$scope.$apply();
	}
	
	function onSubscribeSuccess() {
		$scope.iotfClient.onMessageArrived = onMessageArrived;
		$scope.iotf.sub.colour =  'green';
		$scope.iotf.msg = "Subscribed successfully to IBM Watson IoT Platform!"
		$scope.$apply();
	}
	
	function onSubscribeFailure() {
		$scope.iotf.sub.colour =  'red';
		$scope.iotf.alert = "Failed to subscribe to event messages!"
		$scope.$apply();
	}

	function onConnectFailure() {
		$scope.iotf.con.colour =  'red';
		$scope.iotf.alert = "Could not connect to IBM Watson IoT Platform!"
		$scope.$apply();
	}

	function connectDevice() {
		console.log("IoTBoard : Connecting device to IBM Watson IoT Platform...");
		$scope.iotfClient.connect({
			onSuccess: onConnectSuccess,
			onFailure: onConnectFailure,
			userName: "use-token-auth",
			password: $scope.iotf.password
		});
	}
	
	function onConnectionLost(responseObject) {
		if (responseObject.errorCode !== 0) {
			$scope.iotf.con.colour =  'red';
			$scope.iotf.alert = "Connection lost to IBM Watson IoT Platform! " + responseObject.errorMessage; 
			$scope.$apply();
		}
	}
  
  
  function onMessageArrived(event) {
	  var payload = JSON.parse(event.payloadString);
	  var msg = payload.d;
	  console.log("IoTBoard : processing incoming message : " + JSON.stringify(msg));
	  console.log("IoTBoard : sites " + JSON.stringify($scope.sites));
	  var site = getExisting(msg);
	  if(!site) {
		  //this is a new site and so add it to the board display
		  console.log("IoTBoard : creating new site");
		  site = {sid: msg.sid, gid : msg.gid, name : msg.name, reg : {colour: 'red'}, player : {colour: 'red'}};
		  $scope.sites.push(site);
	  }
	  if(msg.data.light == 'reg') {
		  site.reg.colour = msg.data.status ? 'green' : 'red';
	  }
	  if(msg.data.light == 'player') {
		  site.player.colour = msg.data.status ? 'green' : 'red';
	  }
	  $scope.$apply();
  }
  
  function getExisting(site) {
	  for(var i = 0; i < $scope.sites.length; i++) {
		  if($scope.sites[i].sid = site.sid) {
			  return $scope.sites[i];
		  }
	  }
	  return undefined;
  }
  

}]);