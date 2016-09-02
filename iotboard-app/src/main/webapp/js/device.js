/*******************************************************************************
 * Copyright (c) 2015 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *   Bryan Boyd - Initial implementation
 *******************************************************************************/
(function(window) {


	function onConnectSuccess() {
		// The device connected successfully
		console.log("Connected Successfully!");
		isConnected = true;
		console.log("command is " + cmdTopic);
		window.client.subscribe(cmdTopic, {onSuccess: onSubscribeSuccess, onFailure: onSubscribeFailure});
	}
	
	function onSubscribeSuccess() {
		console.log("Subscribed successfully");
		window.client.onMessageArrived = onMessageArrived;
	}
	
	function onMessageArrived(message) {
		console.log("messsage received)");
		var jsonMsg = JSON.parse(message.payloadString);
		console.log(jsonMsg);
	}
	
	function onSubscribeFailure() {
		console.log("Failed to subscribe");
	}

	function onConnectFailure() {
		// The device failed to connect. Let's try again in one second.
		console.log("Could not connect to IBM Watson IoT Platform!");
	}

	function connectDevice() {
		eventTopic = window.eventTopic;
		cmdTopic = window.cmdTopic;
		console.log(window.deviceId, window.password);
		$("#deviceId").html(window.deviceId);

		console.log("Connecting device to IBM Watson IoT Platform...");
		window.client.connect({
			onSuccess: onConnectSuccess,
			onFailure: onConnectFailure,
			userName: "use-token-auth",
			password: window.password
		});
	}

	  $("#myform").submit(function() {
	      var playerIdVal = $("#playerId").val();
		  $.ajax({
			url: "/iotboard/v1/devices",
			type: "POST",
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			data: JSON.stringify({deviceId: playerIdVal}),
			success: function(response) {
				console.log("Response");
				console.log(response);
				var responseObject = response;
				window.iot_host = responseObject.iotMessagingOrgAndHost;
				window.iot_port = responseObject.iotMessagingPort;
				window.deviceId = responseObject.deviceId;
				window.password = responseObject.deviceAuthToken;
				window.iot_clientid = responseObject.iotClientId;
				window.eventTopic = responseObject.eventTopic;
				window.cmdTopic = responseObject.cmdTopic;
				window.client = new Paho.MQTT.Client(window.iot_host, window.iot_port, window.iot_clientid);
				connectDevice();
			},
			error: function(xhr, status, error) {
				console.log("oh dear");
			}
		  });
		  alert("You submitted");
		  return false;
	  });

	  $("#commandButton").click(function() {
		 $.ajax({
				url: "/iotboard/v1/devices/test",
				type: "POST",
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function(response) {
					console.log("Posted");
				},
				error: function(xhr, status, error) {
					console.log("whoops");
				}
		 }); 
	  });

}(window));
