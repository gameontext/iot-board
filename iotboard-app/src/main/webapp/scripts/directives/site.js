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

