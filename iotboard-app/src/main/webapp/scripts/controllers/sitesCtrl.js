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
  $scope.iotf = {};	//IoT foundation configuration
  $scope.iotfClient = {};	//IoT foundation client connection
  
  $scope.registerDevice = function() {
	 console.log("Registering device for user " + $scope.gid);
	 $http({
	     url: "/iotboard/v1/devices",
	     method: "POST",
	     headers: {  'gameon-id': $scope.gid,
	                 'contentType': 'application/json; charset=utf-8"', //what is being sent to the server
	     },
	     data: JSON.stringify({deviceId: $scope.gid}).trim()
	   }).then(function (response) {
		    console.log('Device registration successful : response from server : ' + response.status);
		    $scope.iotf.iot_host = responseObject.iotMessagingOrgAndHost;
		    $scope.iotf.iot_port = responseObject.iotMessagingPort;
		    $scope.iotf.deviceId = responseObject.deviceId;
		    $scope.iotf.password = responseObject.deviceAuthToken;
		    $scope.iotf.iot_clientid = responseObject.iotClientId;
		    $scope.iotf.eventTopic = responseObject.eventTopic;
		    $scope.iotf.cmdTopic = responseObject.cmdTopic;
		    console.log("IoTBoard : config : " + JSON.stringify($scope.iotf));
		    $scope.iotfClient = new Paho.MQTT.Client($scope.iot.iot_host, $scope.iot.iot_port, $scope.iot.iot_clientid);
   		}, function (response) {
   			alert('Device registration failed : response from server : ' + response.data + ':' + response.status);
   		}
	 );
  }
  
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
		     data: "adamTest"
		   }).then(function (response) {
			    console.log('Command completed successfully');
	   		}, function (response) {
	   			alert('Command failed: ' + response.data + ':' + response.status);
	   		}
		 );
  }
  
	function onConnectSuccess() {
		// The device connected successfully
		console.log("Connected Successfully!");
		$scope.iotfClient.subscribe($scope.iotf.cmdTopic, {onSuccess: onSubscribeSuccess, onFailure: onSubscribeFailure});
	}
	
	function onSubscribeSuccess() {
		console.log("Subscribed successfully");
		$scope.iotfClient.onMessageArrived = onMessageArrived;
	}
	
	function onSubscribeFailure() {
		console.log("Failed to subscribe");
	}

	function onConnectFailure() {
		console.log("IoTBoard : Could not connect to IBM Watson IoT Platform!");
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
		    console.log("onConnectionLost:"+responseObject.errorMessage);
		  }
		}
  
  
  function onMessageArrived(event) {
	  var payload = JSON.parse(event.payloadString);
	  var msg = payload.d;
	  console.log("IoTBoard : processing incoming message : " + JSON.stringify(msg));
	  var site = getExisting(msg);
	  if(!site) {
		  //this is a new site and so add it to the board display
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
		  if($scope.sites[i].name = site.name) {
			  return $scope.sites[i];
		  }
	  }
	  return undefined;
  }
  
  //setup a simulated event for receiving 
  //setTimeout(onMessageArrived, 3000, {sid: '123456789', name: 'A new Room', gid: 'git:1234567', data : {light : 'reg', status: true}});
  //setTimeout(onMessageArrived, 5000, {sid: '123456789', name: 'A new Room', gid: 'git:1234567', data : {light : 'player', status: true}});
  //setTimeout(onMessageArrived, 7000, {sid: '123456789', name: 'A new Room', gid: 'git:1234567', data : {light : 'player', status: false}});
}]);