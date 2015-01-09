Game.controller('HarvestingController', [
	'$scope',
	'$interval',
	function($scope, $interval) {
		var unreg = $scope.$root.$on('operation-harvest',
			function(_s, oper) {

				$scope.type = oper;

				$scope.apples = [];

				$scope.gameActive = true;
				$scope.basketPosition = [ {
					x : 50
				} ];
				$scope.game = {
					score : 0,
					totalGood : 0,
					totalBad : 0,
					goodCaught : 0,
					badCaught : 0,
					basketWidth : 6,
					timeLeft : 60,
					dropAppleInterval : 60
				};

				$scope.visible = true;

				$scope.$root.$emit("side-hide");

				$scope.$on("$destroy", function() {
					if (unreg) {
						unreg();
					}
				});

				$scope.hide = function() {
					$scope.visible = false;
				}

			});

	} ]);
