Game.controller('StateTestController',
		[
				'$scope',
				'$translate',
				'$http',
				'Store',
				'StoreItems',
				'$items',
				'$farmer',
				function($scope, $translate, $http, Store, StoreItems, $items,
						$farmer) {

					$.post("/authcontroller/context", function(data) {
						$scope.$root.context = data;
					});
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

				} ]);