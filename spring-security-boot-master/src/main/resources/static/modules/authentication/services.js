angular.module('Authentication', []).factory(
		'AuthenticationService',
		[
				'$http',
				'$q',
				'constant',
				function($http, $q, constant) {
					return {

						userlogin : function(dataJson) {
							return $http.post(constant.loginUrl, dataJson)
									.then(function(response) {
										return response;
									}, function(errResponse) {
										console.log('not able to login');
										return $q.reject(errResponse);
									});
						},

						userlogout : function() {
							return $http.post(
									constant.logoutUrl,
									{
										headers : {
											'X-API-TOKEN' : localStorage
													.getItem('X-API-TOKEN')
										}
									}).then(function(response) {
								return response;
							}, function(errResponse) {
								console.log('not able to logout');
								return $q.reject(errResponse);
							});
						}
					}
				} ]);