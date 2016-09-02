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

/*
	Provide a representation of a site on the page
*/

'use strict';

angular.module('iotBoardApp')
.directive('site', function() {
  return {
    restrict: 'E',
    transclude: true,
    templateUrl: 'templates/site.html',
    link: function(scope, elem, attr, sitesController) {
    	console.log("IoTBoard : linking site to page");
    	console.log("IoTBoard : " + JSON.stringify(scope.sites));
    },
  };
});

angular.module('iotBoardApp')
.controller('sitesCtrl', ['$scope', '$http', function($scope, $http) {

  console.log("IoTBoard : using controller 'sitesCtrl'");
  
  $scope.sites = [];
  
  $scope.addSite = function() {
	  $scope.sites.push({sid:'Site number ' + $scope.sites.length, reg : {colour: 'red'}, player : {colour: 'red'}});
  }
  
  $scope.changeName = function() {
	$scope.sites[1].reg.colour = "green";  
  }
  
  //simulate being called my Pahoe with a message for this device
  function onMessageArrived(msg) {
	  console.log("IoTBoard : processing incoming message");
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
  setTimeout(onMessageArrived, 3000, {sid: '123456789', name: 'A new Room', gid: 'git:1234567', data : {light : 'reg', status: true}});
  setTimeout(onMessageArrived, 5000, {sid: '123456789', name: 'A new Room', gid: 'git:1234567', data : {light : 'player', status: true}});
  setTimeout(onMessageArrived, 7000, {sid: '123456789', name: 'A new Room', gid: 'git:1234567', data : {light : 'player', status: false}});
}]);
