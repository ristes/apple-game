Game.controller('StateTestController',
		[
				'$scope',
				'$translate',
				'$http',
				'Store',
				'StoreItems',
				'$items',
				'$farmer',
				'$day',
				'$weather',
				'$diseases',
				function($scope, $translate, $http, Store, StoreItems, $items,
						$farmer, $day, $weather, $diseases) {
					
					$scope.next =function() {
						$day.next();
						$scope.$root.context = $day;
					}
					/*
					$.post("/DeseasesExpertSystem/getDeseasePossibility",
							function(data) {
								$scope.$root.diseases = data;
							});
					
					$scope.nextDay = function() {
						$.post("/application/nextday", function(data) {
							$scope.$root.context = data;
						});
						$.post("/DeseasesExpertSystem/getDeseasePossibility",
								function(data) {
									$scope.$root.diseases = data;
								});
					};
					$scope.observeOneYear = function() {
						$.post("/application/observeDiseasesForThisYear",
								function(data) {
									$scope.report = data;
								});
					};
					*/

				} ]);