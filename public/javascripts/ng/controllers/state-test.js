Game.controller('StateTestController', [
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
		function($scope, $translate, $http, Store, StoreItems, $items, $farmer,
				$day, $weather, $diseases) {

			$scope.next = function() {
				$day.next();
				$scope.$root.context = $day;
			}

		} ]);