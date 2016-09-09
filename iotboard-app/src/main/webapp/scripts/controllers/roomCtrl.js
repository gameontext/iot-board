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

//control the LEDs on a virtual board

'use strict';

angular.module('iotBoardApp')
.controller('roomCtrl', ['$scope', '$http', function($scope, $http) {

  console.log("Room : using controller 'roomCtrl'");
  
  $scope.deviceId = ""; 	//id to register device for
  $scope.iotf = {con : { colour : 'yellow'}, sub : { colour : 'yellow'}, rcv : { colour : 'yellow'}};	//IoT foundation configuration
  $scope.iotfClient = {};	//IoT foundation client connection
  $scope.lightState = { selected: true };
  
  $scope.registerDevice = function() {
	 console.log("Registering device for room " + $scope.roomName);
	 $scope.siteId = $scope.playerId + '_' + $scope.roomName;
	 $http({
	     url: "/iotboard/v1/devices",
	     method: "POST",
	     headers: {  
	                 'contentType': 'application/json; charset=utf-8"', //what is being sent to the server
	     },
	     data: JSON.stringify({deviceType: 'GameOnRoom', deviceId: $scope.deviceId, roomName: $scope.roomName, playerId: $scope.playerId, siteId: $scope.siteId}).trim()
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
	  console.log("Room : processing incoming message : " + JSON.stringify(msg));
	  $scope.lightState.selected = msg.data.lightState;
	  $scope.$apply();
  }
  
  $scope.sendMessage = function() {
	  console.log("Sending message");
	  var payload = {
        d : {
             id : $scope.iotf.deviceId,
             playerId : $scope.playerId,
             siteId: $scope.siteId,
             roomName: $scope.roomName,
             data: {
            	 lightAddress: $scope.lightAddress,
                 lightState: $scope.lightState.selected            	 
             }
        }
      }
	  
	  var message = new Paho.MQTT.Message(JSON.stringify(payload));
	  message.destinationName = $scope.iotf.eventTopic;
	  try {
		  $scope.iotfClient.send(message);
		  console.log("[%s] Published", new Date().getTime());
	  } catch (err) {
		  console.error(err);
	  }
  }
  

}]);