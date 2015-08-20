Game
		.controller(
				'TipsController',
				[
						'$scope',
						'State',
						'$modal',
						'$timeout',
						'Toaster',
						'$interval',
						function($scope, State, $modal, $timeout, Toaster, $interval) {
							var windowTemplateUrl = '/public/templates/tip-dialog.html';

							$scope.priorities = {
								'' : 0,
								'tip' : 1,
								'disease' : 2
							};
							
							$scope.startSideShow = function() {
								$scope.$root.$emit('side-show');
							}
							$scope.current_data = "";
							
							

							$scope.isHigherPriority = function(priority) {
								if ($scope.priorities[$scope.current_data] < $scope.priorities[priority]) {
									return true;
								}
								return false;
							};
							
							

							$scope.subscribe = State
									.subscribe(
											'status',
											'TipsController',
											function(data) {
												$scope.message = data.tip;
												if (data.tip && data.tip != "") {
													if ($scope
															.isHigherPriority('tip')) {
														
														$scope.$root.tip = data.tip;
														$scope.$root.type_alert = 'info';
													}
												}
												if (data.farmer) {
													if (data.farmer.hasNewDisease) {
														if ($scope
																.isHigherPriority('disease')) {
															$scope.$root.tip = "Temperatures are on the rise and insects are waking up! Oh, no, your crops are sick! But don’t worry; there are plenty of things you can do to make them healthy again!";
															$scope.$root.type_alert = 'danger';
														}
													}
													if (data.message
															&& data.message != "") {
														Toaster
																.success(data.message);
													}
												}
											});

							$scope.isInfo = function() {
								return $scope.$root.type_alert === 'info';
							}

							$scope.isDanger = function() {
								return $scope.$root.type_alert === 'danger';
							}

						} ]);