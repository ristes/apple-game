Game.controller('PlowingController', [
		'$scope',
		'$translate',
		'$http',
		'Store',
		'StoreItems',
		'Operations',
		'$farmer',
		'$items',
		'$plantation',
		'$weather',
		'$plowing',
		function($scope, $translate, $http, Store, StoreItems, Operations,
				$farmer, $items, $plantation, $weather, $plowing) {
			
			
			var unreg = $scope.$root.$on('operation-plowing',
					function(_s, oper) {
						
						$scope.$root.$emit('show-progress-global', {
							title : 'progress.plowing',
							duration : 5
						});
						$plowing.plowing();
					});

			$scope.$on("$destroy", function() {
				if (unreg) {
					unreg();
				}
			});

		} ]);