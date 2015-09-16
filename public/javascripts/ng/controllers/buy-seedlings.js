Game
		.controller(
				'BuySeadlingsController',
				[
						'$scope',
						'Planting',
						'ExpertAdvice',
						function($scope, Planting, ExpertAdvice) {

							ExpertAdvice
									.setAdvice("Let’s go choose some apples! The number of early twigs on an apple seedling is one of the major indicators of its physical characteristics and quality. Planting material with more twigs is fruitful as early as the first year after planting, becomes fully fruitful in 3-4 years. Planting material without early twigs yields its first fruits in the third year after planting, becomes fully fruitful in 4-5 years. ");
							$scope.seedlings = [];
							$scope.appleTypes = [];
							$scope.seedlingTypes = [];
							$scope.bases = [];

							$scope.selected = {};
							$scope.selectedAppleTypePlants = [];
							$scope.showBase = false;
							$scope.seedlingChosen = false;
							$scope.count = 0;
							$scope.sumPrice = 0;

							function calculatePrice(type) {
								type.totalprice = type.price
										* $scope.selectedPlantType.coef;
							}

							$scope.setSelectedPlantType = function(type) {
								$scope.selectedPlantType = type;
							}
							Planting.maxSeedlingsAllowed(function(data) {
								$scope.maximumSeedlings = data.t;
							})

							$scope.total = function() {
								var total = 0;
								angular.forEach($scope.selected, function(val) {
									total += val.quantity * val.price;
								});
								return total;
							}
							$scope.$watch('selectedPlantType', function(n,o) {
								$scope.onChange(n);
							})
							
							
							$scope.checkToShow = function(appleType) {
								var isSelected = false;
								angular.forEach($scope.appleTypes, function(item) {
		                        	if (item.quantity!==undefined && item.quantity !==0) {
		                        		if (item.name === appleType.name) {
		                        			isSelected = true;
		                        		}
		                        	}
		                        });
								if ($scope.count >= 3) {
									return isSelected;
								}
								return true;
							}
							$scope.onChange = function() {
								$scope.sumPrice = 0;
								$scope.count = 0;
								$scope.totalSeedlings = 0;
								angular.forEach($scope.appleTypes, function(item) {
		                        	if (item.quantity!==undefined && item.quantity !=0) {
		                        		$scope.count++;
		                        		$scope.sumPrice +=item.quantity * $scope.selectedPlantType.price;
		                        		$scope.totalSeedlings += parseInt(item.quantity);
		                        	}
		                        });
							}
							
							$scope.select = function(seedling) {
								var key = seedling.type.id;
								var q = 0;
								if ($scope.selected[key]) {
									q = $scope.selected[key].quantity;
								}
								seedling.quantity = q;
								if ($scope.count > 3
										&& !$scope.selected.hasOwnProperty(key)) {
									alert('To many apple types. Remove some in order to add new')
								} else {
									if (!$scope.selected.hasOwnProperty(key)) {
										$scope.count++;
									}
									$scope.selected[key] = seedling;
								}
							};

							$scope.removeSelected = function(seedling) {
								var key = seedling.type.id;
								if ($scope.selected.hasOwnProperty(key)) {
									$scope.count--;
								}
								delete $scope.selected[key];
							};

							$scope.buySeedlings = function() {
								var bought = [];
								angular.forEach($scope.appleTypes, function(item) {
									if (item.quantity && item.quantity!==0){
										item.type=$scope.selectedPlantType;
										bought.push(item);
									}
								});
//								angular.forEach($scope.selected, function(val) {
//									bought.push(val);
//								});
								Planting.buySeedlings(bought, function() {
									ExpertAdvice.hide();
									$scope.showBase = true;
								});
							};

							Planting.seedlings(function(seedlings) {
								var at = {};
								var st = {};
								// seedlings
								$scope.seedlings = seedlings;
								angular.forEach(seedlings, function(val) {
									at[val.type.id] = val.type;
									st[val.seedlingType.id] = val.seedlingType;
								});
								// plantTypes
								$scope.appleTypes = [];
								angular.forEach(at, function(val) {
									val.quanity = 0;
									$scope.appleTypes.push(val);
								});
								// seedlingTypes
								$scope.seedlingTypes = [];
								angular.forEach(st, function(val) {
									$scope.seedlingTypes.push(val);
								});
							});
							
						} ]);
