'use strict';

angular.module('Home')

.controller(
		'HomeController',
		[
				'$scope',
				'$rootScope',
				'$location',
				'AuthenticationService',
				function($scope, $rootScope, $location, AuthenticationService) {

					
					var token = localStorage.getItem('X-API-TOKEN');
					if(token == undefined){
						$location.path('#/login');
					}
					
					$scope.logout = function() {

						AuthenticationService.userlogout().then(
								function(responseData) {
									alert('Successfuly loggedout in.');
								 	localStorage.removeItem('X-API-TOKEN');
									$scope.dataLoading = false;
									$location.path('#/login');
								},
								function(errResponse) {
									$scope.dataLoading = false;
									console.log('error while logut.'
											+ errResponse);
									$scope.error = errResponse.data.message;
								})
					};
					
					

				} ]);