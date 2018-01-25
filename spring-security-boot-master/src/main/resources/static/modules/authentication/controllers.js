'use strict';
 
angular.module('Authentication')
 
.controller('LoginController',
    ['$scope', '$rootScope', '$location', 'AuthenticationService',
    function ($scope, $rootScope, $location, AuthenticationService) {
        // reset login status
        $scope.login = function () {
            $scope.dataLoading = true;
            $scope.error = '';
            var credentials = {
            		"username":$scope.username,
            		"password":$scope.password
            }
            
            AuthenticationService
			.userlogin(credentials)
			.then(
					function(responseData) {
						$scope.dataLoading = false;
						alert('Successfuly logged in.');
		                if(responseData.data.token != undefined) {
		                	localStorage.setItem('X-API-TOKEN',responseData.data.token);
		                	$scope.dataLoading = false;
		                	$location.path('/');
		                }
					},
					function(errResponse) {
						$scope.dataLoading = false;
						console
						.log('error while login.'+errResponse);
						$scope.error = errResponse.data.message;
					})
        };
    }]);